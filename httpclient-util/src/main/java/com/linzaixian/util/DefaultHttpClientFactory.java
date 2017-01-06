package com.linzaixian.util;

import javax.net.ssl.SSLContext;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;

public class DefaultHttpClientFactory{
	private static PoolingHttpClientConnectionManager connManager;
	private static RequestConfig requestConfig;
	private static SSLContext sslContext;
	private static HttpClientBuilder httpClientBuilder;
	static{
		init();
	}
	private static void init(){
		 try {
			  connManager=new PoolingHttpClientConnectionManager();
			  connManager.setMaxTotal(1);
			  sslContext = SSLContextBuilder.create()
					.loadTrustMaterial(null, new MyTrustStrategy())
					.build();
			  requestConfig=RequestConfig.custom()
					.setSocketTimeout(-1) //从服务器读取数据的timeout,默认-1
					.setConnectTimeout(-1) //和服务器建立连接的timeout,默认-1
					.setConnectionRequestTimeout(-1) //从连接池获取连接的timeout,默认-1
					.build();
			 httpClientBuilder= HttpClients.custom()
				.setDefaultRequestConfig(requestConfig)
				.setConnectionManager(connManager);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static CloseableHttpClient getCloseableHttpClient() {
			return HttpClients.custom()
					.setDefaultRequestConfig(requestConfig)
					.setConnectionManager(connManager).build();
	}
}
