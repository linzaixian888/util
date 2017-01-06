package com.linzaixian.util;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class HttpResult {
	private int statusCode;
	private String content;

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
    public String toString() {
      return ReflectionToStringBuilder.toString(this);
    }
}
