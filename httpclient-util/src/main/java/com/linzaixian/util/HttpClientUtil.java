package com.linzaixian.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Assert;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HttpClientUtil {
    private String charset = StandardCharsets.UTF_8.name();
    private CloseableHttpClient closeableHttpClient;

    public String doGet(String url, Map<String, String> param) throws IOException {
        return executeForm(HttpGet.METHOD_NAME, url, param, null, new ResponseHandler<String>() {
            @Override
            public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
                return EntityUtils.toString(response.getEntity(), charset);
            }
        });
    }

    public String doPost(String url, Map<String, String> param) throws IOException {
        return executeForm(HttpPost.METHOD_NAME, url, param, null, new ResponseHandler<String>() {
            @Override
            public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
                return EntityUtils.toString(response.getEntity(), charset);
            }
        });
    }

    public String doPostJSON(String url, String paramJSON) throws IOException {
        return executeJSON(HttpPost.METHOD_NAME, url, paramJSON, null, new ResponseHandler<String>() {
            @Override
            public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
                return EntityUtils.toString(response.getEntity(), charset);
            }
        });
    }
    public String doPostXml(String url, String paramXml) throws IOException{
        return executeXml(HttpPost.METHOD_NAME, url, paramXml, null, new ResponseHandler<String>() {
            @Override
            public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
                return EntityUtils.toString(response.getEntity(), charset);
            }
        });
    }

    public <T> T executeJSON(String method, String url, String paramJSON, Map<String, String> headers,
                             ResponseHandler<? extends T> responseHandler) throws IOException {
        return executeString(method,url,paramJSON,"application/json",headers,responseHandler);
    }
    public <T> T executeXml(String method, String url, String paramJSON, Map<String, String> headers,
                             ResponseHandler<? extends T> responseHandler) throws IOException {
        return executeString(method,url,paramJSON,"application/xml",headers,responseHandler);
    }
    public <T> T executeString(String method, String url, String param, String mimeType, Map<String, String> headers,
                               ResponseHandler<? extends T> responseHandler) throws IOException {
        String encodeUrl = encodeUrlParam(url, HttpGet.METHOD_NAME.equalsIgnoreCase(method) ? StandardCharsets.UTF_8.name() : this.charset);
        RequestBuilder builder = RequestBuilder.create(method).setUri(encodeUrl);
        if (StringUtils.isNotBlank(charset)) {
            builder.setCharset(Charset.forName(charset));
        }
        builder.setEntity(new StringEntity(param, ContentType.create(mimeType, this.charset)));
        if (headers != null && headers.size() > 0) {
            Iterator<Map.Entry<String, String>> it = headers.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> entry = it.next();
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        return execute(builder.build(), responseHandler);

    }

    public <T> T executeForm(String method, String url, Map<String, String> params, Map<String, String> headers,
                             ResponseHandler<? extends T> responseHandler) throws IOException {
        String encodeUrl = encodeUrlParam(url, HttpGet.METHOD_NAME.equalsIgnoreCase(method) ? StandardCharsets.UTF_8.name() : this.charset);
        RequestBuilder builder = RequestBuilder.create(method).setUri(encodeUrl);
        if (StringUtils.isNotBlank(charset)) {
            builder.setCharset(Charset.forName(charset));
        }
        if (params != null && params.size() > 0) {
            Iterator<Map.Entry<String, String>> it = params.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> entry = it.next();
                builder.addParameter(entry.getKey(), entry.getValue());
            }
        }
        if (headers != null && headers.size() > 0) {
            Iterator<Map.Entry<String, String>> it = headers.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> entry = it.next();
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        return execute(builder.build(), responseHandler);
    }

    public <T> T execute(HttpUriRequest request,
                         ResponseHandler<? extends T> responseHandler) throws IOException {
        return closeableHttpClient.execute(request, responseHandler);
    }

    public void setCharset(String charset) {
        Assert.assertNotNull(charset);
        this.charset = charset;
    }

    public HttpClientUtil(CloseableHttpClient closeableHttpClient) {
        this.closeableHttpClient = closeableHttpClient;
    }

    public HttpClientUtil() {
        closeableHttpClient = HttpClients.createDefault();
    }

    public static HttpClientUtilBuilder createBuilder() {
        return HttpClientUtilBuilder.create();
    }

    static class HttpClientUtilBuilder {
        private String charset = StandardCharsets.UTF_8.name();
        private RequestConfig.Builder builder;
        private RequestConfig requestConfig;

        public HttpClientUtilBuilder setCharset(String charset) {
            this.charset = charset;
            return this;
        }

        private HttpClientUtilBuilder() {

        }

        /**
         * 设置从连接池获取连接的timeout
         *
         * @param connectionRequestTimeout
         * @return
         */
        public HttpClientUtilBuilder setConnectionRequestTimeout(int connectionRequestTimeout) {
            if (builder == null) {
                builder = RequestConfig.custom();
            }
            builder.setConnectionRequestTimeout(connectionRequestTimeout);
            return this;

        }

        /**
         * 设置和服务器建立连接的timeout
         *
         * @param connectTimeout
         * @return
         */
        public HttpClientUtilBuilder setConnectTimeout(int connectTimeout) {
            if (builder == null) {
                builder = RequestConfig.custom();
            }
            builder.setConnectTimeout(connectTimeout);
            return this;
        }

        /**
         * 设置从服务器读取数据的timeout
         *
         * @param socketTimeout
         * @return
         */
        public HttpClientUtilBuilder setSocketTimeout(int socketTimeout) {
            if (builder == null) {
                builder = RequestConfig.custom();
            }
            builder.setSocketTimeout(socketTimeout);
            return this;
        }

        public HttpClientUtilBuilder setRequestConfig(RequestConfig requestConfig) {
            this.requestConfig = requestConfig;
            return this;
        }

        public static HttpClientUtilBuilder create() {
            return new HttpClientUtilBuilder();
        }

        public HttpClientUtil build() {
            if (requestConfig == null && builder != null) {
                requestConfig = builder.build();
            }
            HttpClientUtil httpClientUtil;
            if (requestConfig == null) {
                httpClientUtil = new HttpClientUtil();
            } else {
                CloseableHttpClient closeableHttpClient = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();
                httpClientUtil = new HttpClientUtil(closeableHttpClient);
            }
            httpClientUtil.setCharset(charset);
            return httpClientUtil;
        }
    }

    /**
     * 格式化url地址里的参数
     *
     * @param url
     * @return
     * @throws UnsupportedEncodingException
     */
    private String encodeUrlParam(String url, String charset) throws UnsupportedEncodingException {
        if(StringUtils.isNotBlank(url)&&url.indexOf("?")!=-1){
            StringBuilder sb = new StringBuilder();
            String[] array = url.split("\\?");
            sb.append(array[0]);
            if (StringUtils.isNotBlank(array[1])) {
                sb.append("?");
                String[] paramArray = array[1].split("\\&");
                Iterator<String> it = Arrays.asList(paramArray).iterator();
                while (it.hasNext()) {
                    String[] oneParam = it.next().split("=");
                    if (oneParam.length > 1) {
                        sb.append(URLEncoder.encode(oneParam[0], charset));
                        sb.append("=");
                        sb.append(URLEncoder.encode(oneParam[1], charset));
                    }
                    if (it.hasNext()) {
                        sb.append("&");
                    }

                }
            }
            return sb.toString();
        }else{
            return url;
        }


    }

    public static void main(String[] args) throws IOException, URISyntaxException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> map = new HashMap<String, String>();
        map.put("第一参数", "中国");
        map.put("第二参数", "中国人");
        String json = objectMapper.writeValueAsString(map);

        HttpClientUtil util = HttpClientUtil.createBuilder().setCharset("gbk").build();
//        System.out.println(util.doGet("http://localhost:9999/abc?默认=测试&默认2=测试3",map));
//        System.out.println(util.doPost("http://localhost:9999/abc?默认=测试&默认2=测试3",map));
//        System.out.println(util.doPostJSON("http://localhost:6789/abc?默认=测试&默认2=测试3", json));
        System.out.println(util.doPostXml("http://localhost:6799", json));

    }
}
