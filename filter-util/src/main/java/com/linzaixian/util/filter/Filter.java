package com.linzaixian.util.filter;

public interface Filter<P extends Params> {
	void doFilter(P params, FilterChain filterChain);
}
