package com.linzaixian.util.filter;

import java.util.ArrayList;
import java.util.List;

public class FilterChain implements Filter{
	private List<Filter> filters = new ArrayList<Filter>();
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
	 * 处理完其中的过滤器链后接下来要处理的过滤器,也就是要返回之前的过滤器继续处理
	 */
	private FilterChain lastFilterChain;
	public FilterChain addFilter(Filter filter){
	    this.filters.add(filter);
	    return this;
	}
	public void doFilter(Request request, Response response, FilterChain filterChain) {
		FilterChain chain;
		if(index>=filters.size()){
			if(this.lastFilterChain!=null){
				//如果该过滤器链处理完了，判断还有没有之前的过滤器链要处理
				chain=this.lastFilterChain;
				if(chain!=null){
					//当前过滤器链处理完，把数量增加到之前的过滤器链，并继续运行之前的过滤器链
					chain.addFilterCount(this.filterCount);
					chain.doFilter(request, response, chain);
					
				}
			}
			
		}else{
			Filter nowFilter=filters.get(index);
			index++;
			if(nowFilter instanceof FilterChain){
				//判断到要处理的是过滤器链
				chain=(FilterChain) nowFilter;
				chain.lastFilterChain=this;
				chain.doFilter(request, response, chain);
				
			}else {
				//判断当前要处理的是过滤器
				//计算是第几个处理器 
				this.addFilterCount(1);
				System.out.println(nowFilter);
				nowFilter.doFilter(request, response, this);
			}
		}
	    
	    
	    
	    
	}
	
	private synchronized void addFilterCount(int add){
		this.filterCount=this.filterCount+add;
	}
	/**
	 * 获取运行过的过滤器数量(运行报错的也算，没运行过的就不算)
	 * @return
	 */
	public synchronized int getFilterCount() {
		return filterCount;
	}

}
