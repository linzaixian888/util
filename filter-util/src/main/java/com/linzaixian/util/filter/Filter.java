package com.linzaixian.util.filter;

public interface Filter {
	void doFilter(Request request, Response response,FilterChain filterChain);
}
