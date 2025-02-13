package com.yunyou.common.http;

import com.yunyou.common.exception.GlobalException;
import com.yunyou.common.utils.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 封装HttpClient工具类
 *
 * @author WMJ
 * @version 2018-08-23
 */
public class HttpClientUtil {
    private static Logger log = LoggerFactory.getLogger(HttpClientUtil.class);

    private static final Pattern pattern = Pattern.compile("(?<=//|)((\\w)+\\.)+\\w+");

    private RequestConfig requestConfig = RequestConfig.custom()
            .setSocketTimeout(40000)
            .setConnectTimeout(20000)
            .setConnectionRequestTimeout(20000)
            .build();

    private static HttpClientUtil instance = null;

    public HttpClientUtil() {
    }

    public static HttpClientUtil getInstance() {
        if (instance == null) {
            instance = new HttpClientUtil();
        }
        return instance;
    }

    /**
     * 发送 post请求
     *
     * @param httpUrl 地址
     */
    public String sendHttpPost(String httpUrl) {
        return sendHttp(null, new HttpPost(httpUrl));
    }

    /**
     * 发送 post请求
     *
     * @param httpUrl 地址
     * @param params  参数(格式:key1=value1&key2=value2)/(JSON格式)
     * @param isJson  是否json
     */
    public String sendHttpPost(String httpUrl, String params, boolean isJson) {
        HttpPost httpPost = new HttpPost(httpUrl);
        try {
            if (isJson) {
                httpPost.addHeader("Content-Type", "application/json;charset=utf-8");
            }
            //设置参数
            StringEntity stringEntity = new StringEntity(params, "UTF-8");
            stringEntity.setContentEncoding("UTF-8");
            stringEntity.setContentType(isJson ? "application/json" : "application/x-www-form-urlencoded");
            httpPost.setEntity(stringEntity);
        } catch (Exception e) {
            log.error("参数设置异常", e);
        }
        return sendHttp(null, httpPost);
    }

    public String sendHttpPost(String httpUrl, String params) {
        HttpPost httpPost = new HttpPost(httpUrl);
        try {
            httpPost.addHeader("Content-Type", "application/json;charset=utf-8");
            //设置参数
            StringEntity stringEntity = new StringEntity(params, "UTF-8");
            stringEntity.setContentEncoding("UTF-8");
            stringEntity.setContentType("application/json");
            httpPost.setEntity(stringEntity);

            HttpClient httpClient = HttpClients.createDefault();
            httpPost.setConfig(requestConfig);
            HttpResponse response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                return EntityUtils.toString(response.getEntity(), "UTF-8");
            }
            throw new GlobalException(statusCode + "", EntityUtils.toString(response.getEntity(), "UTF-8"));
        } catch (GlobalException e) {
            throw e;
        } catch (Exception e) {
            throw new GlobalException(e.getMessage());
        }
    }

    public void sendHttpPostAsync(String httpUrl, String params) {
        HttpPost httpPost = new HttpPost(httpUrl);
        try {
            httpPost.addHeader("Content-Type", "application/json;charset=utf-8");
            //设置参数
            StringEntity stringEntity = new StringEntity(params, "UTF-8");
            stringEntity.setContentEncoding("UTF-8");
            stringEntity.setContentType("application/json");
            httpPost.setEntity(stringEntity);

            CloseableHttpAsyncClient httpclient = HttpAsyncClients.custom().build();
            httpPost.setConfig(requestConfig);
            httpclient.execute(httpPost, new MyCallback());
        } catch (Exception e) {
            log.error("参数设置异常", e);
        }
    }

    /**
     * 发送 post请求
     *
     * @param httpUrl 地址
     * @param maps    参数
     */
    public String sendHttpPost(String httpUrl, Map<String, String> maps) {
        // 创建httpPost
        HttpPost httpPost = new HttpPost(httpUrl);
        // 创建参数队列
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        for (String key : maps.keySet()) {
            nameValuePairs.add(new BasicNameValuePair(key, maps.get(key)));
        }
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
        } catch (Exception e) {
            log.error("参数设置异常", e);
        }
        return sendHttp(null, httpPost);
    }

    /**
     * 发送 get请求
     *
     * @param httpUrl
     */
    public String sendHttpGet(String httpUrl) {
        return sendHttp(new HttpGet(httpUrl), null);
    }

    /**
     * 发送请求
     *
     * @param httpGet
     * @param httpPost
     */
    private String sendHttp(HttpGet httpGet, HttpPost httpPost) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        String responseContent = null;
        try {
            // 创建默认的httpClient实例.
            httpClient = HttpClients.createDefault();
            if (null == httpGet) {
                httpPost.setConfig(requestConfig);
                // 执行请求
                response = httpClient.execute(httpPost);
            } else if (null == httpPost) {
                httpGet.setConfig(requestConfig);
                // 执行请求
                response = httpClient.execute(httpGet);
            }
            responseContent = EntityUtils.toString(response.getEntity(), "UTF-8");
        } catch (Exception e) {
            log.error("http请求异常", e);
        } finally {
            try {
                // 关闭连接,释放资源
                if (response != null) {
                    response.close();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                log.error("http资源关闭异常", e);
                e.printStackTrace();
            }
        }
        return responseContent;
    }

    /**
     * 描述：发送POST请求
     *
     * @param url
     * @param params
     * @param isJson
     * @param userName
     * @param password
     * @author Jianhua on 2019/4/10
     */
    public String sendHttpPost(String url, String params, boolean isJson, String userName, String password) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        String responseContent = null;
        try {
            //此处添加账号和密码
            CredentialsProvider credentialsProvider = null;
            if (StringUtils.isNotBlank(userName) && StringUtils.isNotBlank(password)) {
                credentialsProvider = new BasicCredentialsProvider();
                credentialsProvider.setCredentials(new AuthScope(getHost(url), AuthScope.ANY_PORT), new UsernamePasswordCredentials(userName, password));
            }

            //创建Http post请求
            HttpPost httpPost = new HttpPost(url);
            try {
                if (isJson) {
                    httpPost.addHeader("Content-Type", "application/json;charset=utf-8");
                }
                //设置参数
                StringEntity stringEntity = new StringEntity(params, "UTF-8");
                stringEntity.setContentEncoding("UTF-8");
                stringEntity.setContentType(isJson ? "application/json" : "application/x-www-form-urlencoded");
                httpPost.setEntity(stringEntity);
            } catch (Exception e) {
                log.error("参数设置异常", e);
            }
            httpPost.setConfig(requestConfig);
            // 创建默认的httpClient实例.
            httpClient = HttpClients.custom().setDefaultCredentialsProvider(credentialsProvider).build();
            // 执行请求
            response = httpClient.execute(httpPost);

            responseContent = EntityUtils.toString(response.getEntity(), "UTF-8");
        } catch (Exception e) {
            log.error("http请求异常", e);
        } finally {
            try {
                // 关闭连接,释放资源
                if (response != null) {
                    response.close();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                log.error("http资源关闭异常", e);
                e.printStackTrace();
            }
        }
        return responseContent;
    }

    public static String getHost(final String url) {
        if (url == null || "".equals(url.trim())) {
            return "";
        }
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            return matcher.group();
        } else {
            return "";
        }
    }

    /**
     * 发送 post请求
     *
     * @param httpUrl 地址
     * @param maps    参数
     */
    public String sendPost(String httpUrl, Map<String, String> maps) {
        StringBuilder buffer = new StringBuilder();
        // 创建httpPost
        HttpPost httpPost = new HttpPost(httpUrl);
        try {
            String request = "";
            if (maps != null && maps.size() > 0) {
                for (String str : maps.keySet()) {
                    buffer.append(str).append("=").append(URLEncoder.encode(maps.get(str), "utf-8")).append("&");
                }
                // 去掉最后一个&并urlencode
                request = buffer.toString().substring(0, buffer.toString().length() - 1);
            }
            StringEntity stringEntity = new StringEntity(request, "UTF-8");
            stringEntity.setContentEncoding("UTF-8");
            stringEntity.setContentType("application/x-www-form-urlencoded;charset=UTF-8");
            httpPost.setEntity(stringEntity);
        } catch (Exception e) {
            log.error("参数设置异常", e);
        }
        return sendHttp(null, httpPost);
    }

    public String sendHttpsPost(String httpUrl, Map<String, String> maps) {
        StringBuilder buffer = new StringBuilder();
        // 创建httpPost
        HttpPost httpPost = new HttpPost(httpUrl);
        try {
            String request = "";
            if (maps != null && maps.size() > 0) {
                for (String str : maps.keySet()) {
                    buffer.append(str).append("=").append(URLEncoder.encode(maps.get(str), "utf-8")).append("&");
                }
                // 去掉最后一个&并urlencode
                request = buffer.substring(0, buffer.toString().length() - 1);
            }
            StringEntity stringEntity = new StringEntity(request, "UTF-8");
            stringEntity.setContentEncoding("UTF-8");
            stringEntity.setContentType("application/x-www-form-urlencoded;charset=UTF-8");
            httpPost.setEntity(stringEntity);
        } catch (Exception e) {
            log.error("参数设置异常", e);
        }
        return sendHttps(null, httpPost);
    }

    /**
     * 发送请求HTTPS
     *
     * @param httpGet
     * @param httpPost
     */
    private String sendHttps(HttpGet httpGet, HttpPost httpPost) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        HttpEntity entity = null;
        String responseContent = null;
        try {
            // 创建默认的httpClient实例.
            httpClient = new SSLClient();
            if (null == httpGet) {
                httpPost.setConfig(requestConfig);
                // 执行请求
                response = httpClient.execute(httpPost);
            } else if (null == httpPost) {
                httpGet.setConfig(requestConfig);
                // 执行请求
                response = httpClient.execute(httpGet);
            }
            entity = response.getEntity();
            responseContent = EntityUtils.toString(entity, "UTF-8");
        } catch (Exception e) {
            log.error("https请求异常", e);
        } finally {
            try {
                // 关闭连接,释放资源
                if (response != null) {
                    response.close();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                log.error("http资源关闭异常", e);
                e.printStackTrace();
            }
        }
        return responseContent;
    }
}