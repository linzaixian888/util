package com.linzaixian.util.filter;

public abstract class NotNullConfSkipFilter extends CheckConfFilter{
	@Override
	public Result validate(String confName, Object value) throws Exception {
		if(value==null){
			return Result.skip;
		}
		return null;
	}

}
