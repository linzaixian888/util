package com.linzaixian.util.filter;

public abstract class NotNullConfStopFilter extends CheckConfFilter{
	@Override
	public Result validate(String confName, Object value) throws Exception {
		if(value==null){
			return Result.stop;
		}
		return null;
	}

}
