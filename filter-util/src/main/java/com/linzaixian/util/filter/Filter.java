package com.linzaixian.util.filter;
/**
 * 过滤器
 * @author linzaixian
 * @since 2017-05-24 01:19:36 
 * @param <P>
 */
public interface Filter<P extends Params,C extends FilterChain> {
    /**
     * 过滤器运行
     * @param params
     * @param filterChain
     * @throws Exception
     */
	void doFilter(P params, C filterChain) throws Exception;
}
