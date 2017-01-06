package com.linzaixian.util;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpMessage;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HttpClientUtil {
	private Charset charset=null;
	
	private CloseableHttpClient client=DefaultHttpClientFactory.getCloseableHttpClient();
	
	public HttpResult doPostResult(String url,Map params) throws Exception{
		HttpResponse httpResponse=doPost(url, null, params, this.charset);
		return toHttpResult(httpResponse);
	}
	public HttpResult doGetResult(String url,Map params) throws Exception{
		HttpResponse httpResponse=doGet(url, null, params, this.charset);
		return toHttpResult(httpResponse);
	}
	
	
	
	public HttpResponse doPost(String url,Map headers,Map params,Charset charset) throws Exception{
		if(charset==null){
			charset=charset.defaultCharset();
		}
		HttpPost post=new HttpPost(url);
		if(params!=null&&params.size()!=0){
			Set keys=params.keySet();
			MultipartEntityBuilder entity = MultipartEntityBuilder.create();
			for(Object key:keys){
				Object value=params.get(key);
				if(key!=null&&value!=null){
					if(value instanceof File){
						entity.addBinaryBody(key.toString(), (File)value);
					}
					else{
						entity.addTextBody(key.toString(), value.toString(),ContentType.DEFAULT_TEXT.withCharset(charset));
					}
				}
			}
			entity.setCharset(charset);
			post.setEntity(entity.build());
			
		}
		setHeader(post, headers);
		HttpResponse response=client.execute(post);
		return response;
	}
	
	public HttpResponse doGet(String url,Map headers,Map params,Charset charset) throws Exception{
		if(charset==null){
			charset=charset.defaultCharset();
		}
		URIBuilder ub=new URIBuilder(url);
		if(params!=null&&params.size()>0){
			List<NameValuePair> paramlist=new ArrayList<NameValuePair>();
			for(Object key:params.keySet()){
				Object value=params.get(key);
				if(key!=null&&value!=null){
					paramlist.add(new BasicNameValuePair(key.toString(), params.get(key).toString()));
				}
			}
			ub.setParameters(paramlist);
			ub.setCharset(charset);
		}
		HttpGet get=new HttpGet(ub.build());
		if(headers!=null&&headers.size()>0){
			setHeader(get, headers);
		}
		HttpResponse response=client.execute(get);
		return response;
	}
	/**
	 * 关闭连接
	 * @param httpEntity
	 */
	public void closeHttpEntity(HttpEntity httpEntity){
		EntityUtils.consumeQuietly(httpEntity);
	}
	
	/**
	 * 转换为HttpResult对象
	 * @param httpResponse
	 * @return
	 * @throws Exception
	 */
	private HttpResult toHttpResult(HttpResponse httpResponse) throws Exception{
		HttpResult result=new HttpResult();
		result.setStatusCode(httpResponse.getStatusLine().getStatusCode());
		result.setContent(EntityUtils.toString(httpResponse.getEntity(), this.charset));
		return result;
	}
	
	/**
	 * 设置请求的header
	 * @param request 请求
	 * @param headers 
	 */
	private void setHeader(HttpMessage request,Map headers){
		if(headers!=null&&headers.size()>0){
			for(Object key:headers.keySet()){
				Object value=headers.get(key);
				if(key!=null&&value!=null){
					request.setHeader(new BasicHeader(key.toString(), value.toString()));
				}
			}
		}
	}
	
	
	public static void main(String[] args) throws Exception{
		final Map params=new HashMap();
		final String url="http://dotacq.cn/Dota/GetTrem.ashx";
		params.put("ip", "119");
		params.put("ids", ",97,15,94,93,78,");
		for(int i=0;i<20;i++){
			new Thread(){
				public void run() {
					try {
						System.out.println(new HttpClientUtil().doGetResult(url, params));
					} catch (Exception e) {
						e.printStackTrace();
					}
				};
			}.start();
		}
		
	}

	public Charset getCharset() {
		return charset;
	}

	public void setCharset(Charset charset) {
		this.charset = charset;
	}
	public void setCharset(String charset){
		this.charset=charset==null?Charset.defaultCharset():Charset.forName(charset);
	}

	
	
}
