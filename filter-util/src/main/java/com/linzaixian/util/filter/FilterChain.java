package com.linzaixian.util.filter;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 过滤器链
 * @author linzaixian
 * @since 2017-05-24 00:46:04
 */
public class FilterChain<P extends Params,C extends FilterChain> implements Filter<P,C>{
    private static Logger logger=LoggerFactory.getLogger(FilterChain.class);
	private List<Filter> filters = new ArrayList<Filter>();
	/**
	 * 当前处理的过滤器和过滤器链的下标
	 */
	private int index = 0;
	/**
	 * 启动过的过滤器数量
	 */
	private int filterCount=0;
	
	/**
     * 是否成功处理所有
     */
    private boolean isSuccess=true;
	/**
	 * 是否自动执行过滤器
	 */
	private boolean autoDoFilter=true;
	
	/**
	 * 处理完其中的过滤器链后接下来要处理的过滤器,也就是要返回之前的过滤器继续处理
	 */
	private FilterChain lastFilterChain;
	public C addFilter(Filter filter){
	    this.filters.add(filter);
	    return (C) this;
	}
	public void doFilter(P params) throws Exception {
	    doFilter(params,(C) this);
	}
	
	public void doFilter(P params,  C filterChain) throws Exception {
		FilterChain chain;
		if(index>=filters.size()){
			if(this.lastFilterChain!=null){
				//如果该过滤器链处理完了，判断还有没有之前的过滤器链要处理
				chain=this.lastFilterChain;
				if(chain!=null){
					//当前过滤器链处理完，继续运行之前的过滤器链
					chain.doFilter(params, chain);
					
				}
			}
			
		}else{
			Filter nowFilter=filters.get(index);
			index++;
			if(nowFilter instanceof FilterChain){
				//判断到要处理的是过滤器链
				chain=(FilterChain) nowFilter;
				//初始化该过滤器链，使之可被重复添加处理
				chain.index=0;
                processFilterChain(params, chain);
				
			}else {
				//判断当前要处理的是过滤器
				//计算是第几个处理器 
                processFilter(params, nowFilter);
			}
		}
	}
	
	/**
	 * 处理过滤器链
	 * @param params
	 * @param nowFilterChain
	 * @throws Exception
	 */
	private void processFilterChain(P params,FilterChain nowFilterChain) throws Exception{
        //标识要返回原来的过滤器链
	    nowFilterChain.lastFilterChain=this;
	    nowFilterChain.doFilter(params, nowFilterChain);
    }
	
	/**
	 * 处理过滤器
	 * @param params
	 * @param nowFilter
	 * @throws Exception
	 */
	private void processFilter(P params,Filter nowFilter)throws Exception{
	  //判断当前要处理的是过滤器
	    boolean isProcess=true;//是否要运行处理器的标识
        try {
            if(nowFilter instanceof CheckConfFilter){
                CheckConfFilter checkConfFilter=(CheckConfFilter) nowFilter;
                CheckConfFilter.Result result=checkConfFilter.validate(params);
                if(result==null||result==CheckConfFilter.Result.success){
                    logger.debug("检测正常");
                }else if(result==CheckConfFilter.Result.skip){
                    isProcess=false;
                    logger.warn("配置项检测不合格,位于{}链中的过滤器{}将跳过运行,",this,nowFilter);
                }else if(result==CheckConfFilter.Result.stop){
                    logger.error("配置项检测不合格,位于{}链中的过滤器{}将导致运行停止,",this,nowFilter);
                    throw new RuntimeException("配置项检测不合格,停止运行");
                }
                
            }
            if(isProcess){
                //计算是第几个处理器且运行
                this.addFilterCount(1);
                logger.debug("***当前为第{}个过滤器:{},位于{}链中***",this.getFilterCount(),nowFilter.getClass().getName(),this);
                logger.debug("运行前参数{}",params);
                nowFilter.doFilter(params, this);
                logger.debug("运行后参数{}",params);
            }
            
        } catch (Exception e) {
            Logger filterLog=LoggerFactory.getLogger(nowFilter.getClass());
            filterLog.error("发生异常:{}",e);
            this.isSuccess=false;
            throw e;
        }
        if(autoDoFilter){
          //过滤器链继续往下处理
          this.doFilter(params, (C) this);
        }
        
    }
	
	
	private synchronized void addFilterCount(int add){
		this.filterCount=this.filterCount+add;
		if(lastFilterChain!=null){
		    lastFilterChain.addFilterCount(add);
		}
	}
	/**
	 * 获取运行过的过滤器数量(运行报错的也算，没运行过的就不算)
	 * @return
	 */
	public synchronized int getFilterCount() {
	    if(lastFilterChain!=null){
	        return lastFilterChain.getFilterCount();
	    }
		return filterCount;
	}
	/**
     * 获取正常运行的过滤器数量
     * @return
     */
    public synchronized int getSuccessFilterCount(){
        int count=getFilterCount();
        if(!isSuccess){
            return count>0?--count:0;
        }else{
            return count;
        }
    }
    public void setAutoDoFilter(boolean autoDoFilter) {
        this.autoDoFilter = autoDoFilter;
    }
   
	
    

}
