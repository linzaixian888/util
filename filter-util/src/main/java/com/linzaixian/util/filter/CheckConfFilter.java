package com.linzaixian.util.filter;

import java.lang.reflect.Method;

/**
 * 需要检测配置的处理器
 * @author lzx
 *
 */
public abstract class CheckConfFilter<P extends Params> implements Filter{
	Class<?> classType=this.getClass();
	/**
	 * 获取需要检测的配置项集合
	 * @return
	 */
	public abstract String[] getConfNames();
	
	public Result validate(P param)throws Exception{
		String[] confNames=getConfNames();
		if(confNames!=null&&confNames.length>0){
			for(String confName:confNames){
				Result result= validate(confName, getValue(confName, param));
				if(result!=null){
					return result;
				}
			}
		}
		return null;
	}
	public Object getValue(String confName,P param){
	    return param.get(confName);
	}
	
	public Result validate(String confName,Object value) throws Exception{
		try {
			Method m=classType.getDeclaredMethod("validate"+firstUp(confName), Object.class);
			return (Result) m.invoke(this, value);
		} catch (Exception e) {
		}
		return null;
	}
	/**
     * 首字母大写
     * @param str
     * @return
     */
    public String firstUp(String str){
        if(str==null||"".equals(str)){
            return str;
        }else{
            String temp=str.substring(0,1);
            return temp.toUpperCase()+str.substring(1);
        }
    }
	/**
	 * 检测配置项处理结果
	 * @author lzx
	 *
	 */
	public enum Result{
		/**
		 * 停止运行
		 */
		stop,
		/**
		 * 跳过该处理器的运行
		 */
		skip
	}
}
