package com.yunyou.modules.interfaces.gps.g7;

import com.alibaba.fastjson.JSON;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.modules.interfaces.gps.HttpPoolUtil;
import com.yunyou.modules.interfaces.gps.MessageDigestUtil;
import com.yunyou.modules.interfaces.gps.Request;
import com.yunyou.modules.interfaces.gps.Response;
import com.yunyou.modules.interfaces.gps.constant.HttpMethod;
import com.yunyou.modules.interfaces.gps.constant.HttpSchema;
import com.yunyou.modules.interfaces.gps.constant.Method;
import com.yunyou.modules.interfaces.gps.g7.constant.Constants;
import com.yunyou.modules.interfaces.gps.g7.constant.HttpConstants;
import com.yunyou.modules.interfaces.gps.g7.util.SignUtil;
import org.apache.http.Header;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static com.yunyou.modules.interfaces.gps.constant.Constants.SPE2_COLON;

public class G7Client {
    private static final Logger log = LoggerFactory.getLogger(G7Client.class);

    public static Response get(String path, Map<String, String> queries) throws Exception {
        Map<String, String> headers = new HashMap<>();
        headers.put(HttpConstants.HTTP_HEADER_G7_TIMESTAMP, String.valueOf(System.currentTimeMillis()));

        Request request = new Request();
        request.setHost(HttpSchema.HTTP + Constants.BASE_URL);
        request.setAccessId(Constants.ACCESS_ID);
        request.setSecretKey(Constants.SECRET_KEY);
        request.setPath(path);
        request.setMethod(Method.GET);
        request.setHeaders(headers);
        request.setQueries(queries);
        return execute(request);
    }

    public static Response post(String path, Map<String, Object> queries) throws Exception {
        Request request = new Request();
        request.setHost(HttpSchema.HTTP + Constants.BASE_URL);
        request.setAccessId(Constants.ACCESS_ID);
        request.setSecretKey(Constants.SECRET_KEY);
        request.setPath(path);
        String body = JSON.toJSONString(queries);

        Map<String, String> headers = new HashMap<>();
        headers.put(HttpConstants.HTTP_HEADER_CONTENT_MD5, MessageDigestUtil.base64AndMD5(body));
        headers.put(HttpConstants.HTTP_HEADER_CONTENT_TYPE, HttpConstants.CONTENT_TYPE_JSON);
        headers.put(HttpConstants.HTTP_HEADER_G7_TIMESTAMP, String.valueOf(System.currentTimeMillis()));

        request.setMethod(Method.POST_JSON);
        request.setHeaders(headers);
        request.setJsonStrBody(body);
        return execute(request);
    }

    private static Response execute(Request request) throws Exception {
        switch (request.getMethod()) {
            case GET:
                return httpGet(request.getHost(),
                        request.getPath(),
                        request.getAccessId(),
                        request.getSecretKey(),
                        request.getHeaders(),
                        request.getQueries());
            case POST_JSON:
                return httpPost(request.getHost(),
                        request.getPath(),
                        request.getAccessId(),
                        request.getSecretKey(),
                        request.getHeaders(),
                        request.getQueries(),
                        request.getJsonStrBody());
            case PUT_JSON:
                return httpPut(request.getHost(),
                        request.getPath(),
                        request.getAccessId(),
                        request.getSecretKey(),
                        request.getHeaders(),
                        request.getQueries(),
                        request.getJsonStrBody());
            case DELETE:
                return httpDelete(request.getHost(),
                        request.getPath(),
                        request.getAccessId(),
                        request.getSecretKey(),
                        request.getHeaders(),
                        request.getQueries());
            default:
                throw new IllegalArgumentException(String.format("unsupported method:%s", request.getMethod()));
        }
    }

    /**
     * 初始化基础Header
     */
    private static Map<String, String> initialBasicHeader(String method, String path,
                                                          Map<String, String> headers,
                                                          Map<String, String> queries,
                                                          String appKey, String appSecret) {
        if (headers == null) {
            headers = new HashMap<>();
        }
        String tmppath = path;
        if (tmppath.startsWith("/custom")) {
            tmppath = tmppath.replace("/custom", "");
        }
        String sign = SignUtil.sign(appSecret, method, tmppath, headers, queries, null);
        log.info("sign: {}", sign);

        headers.put(HttpConstants.X_CA_SIGNATURE, Constants.AUTH_PREFIX + " " + appKey + SPE2_COLON + sign);
        log.info("headers: {}", headers);
        return headers;
    }

    private static Response convert(CloseableHttpResponse response) throws Exception {
        Response res = new Response();
        if (response == null) {
            // 服务器无回应
            res.setStatusCode(500);
            res.setErrorMessage("No Response");
            return res;
        }
        res.setStatusCode(response.getStatusLine().getStatusCode());
        for (Header header : response.getAllHeaders()) {
            res.setHeader(header.getName(), MessageDigestUtil.iso88591ToUtf8(header.getValue()));
        }
        res.setContentType(res.getHeader("Content-Type"));
        res.setRequestId(res.getHeader("X-Ca-Request-Id"));
        res.setErrorMessage(res.getHeader("X-Ca-Error-Message"));
        try {
            res.setBody(EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8));
        } finally {
            EntityUtils.consume(response.getEntity());
            response.close();
        }
        return res;
    }

    /**
     * HTTP GET
     */
    private static Response httpGet(String host, String path,
                                    String appKey, String appSecret,
                                    Map<String, String> headers,
                                    Map<String, String> queries) throws Exception {
        headers = initialBasicHeader(HttpMethod.GET, path, headers, queries, appKey, appSecret);
        String url = HttpPoolUtil.getUrl(host, path, queries);

        HttpGet get = new HttpGet(url);
        if (headers != null) {
            for (Map.Entry<String, String> e : headers.entrySet()) {
                get.addHeader(e.getKey(), MessageDigestUtil.utf8ToIso88591(e.getValue()));
            }
        }
        Response response = convert(HttpPoolUtil.getHttpClient().execute(get));
        if (log.isInfoEnabled()) {
            log.info("url={}, headers={}, res={}", url, get.getAllHeaders(), response.getBody());
        }
        return response;
    }

    /**
     * HTTP POST
     */
    private static Response httpPost(String host, String path,
                                     String appKey, String appSecret,
                                     Map<String, String> headers,
                                     Map<String, String> queries,
                                     String body) throws Exception {
        headers = initialBasicHeader(HttpMethod.POST, path, headers, queries, appKey, appSecret);
        String url = HttpPoolUtil.getUrl(host, path, queries);

        HttpPost post = new HttpPost(url);
        if (headers != null) {
            for (Map.Entry<String, String> e : headers.entrySet()) {
                post.addHeader(e.getKey(), MessageDigestUtil.utf8ToIso88591(e.getValue()));
            }
        }
        if (StringUtils.isNotBlank(body)) {
            post.setEntity(new StringEntity(body, StandardCharsets.UTF_8));

        }
        Response response = convert(HttpPoolUtil.getHttpClient().execute(post));
        if (log.isInfoEnabled()) {
            log.info("url={}, headers={}, body={}, res={}", url, post.getAllHeaders(), body, response.getBody());
        }
        return response;
    }

    /**
     * HTTP PUT
     */
    private static Response httpPut(String host, String path,
                                    String appKey, String appSecret,
                                    Map<String, String> headers,
                                    Map<String, String> queries,
                                    String body) throws Exception {
        headers = initialBasicHeader(HttpMethod.PUT, path, headers, queries, appKey, appSecret);
        String url = HttpPoolUtil.getUrl(host, path, queries);

        HttpPut put = new HttpPut(url);
        if (headers != null) {
            for (Map.Entry<String, String> e : headers.entrySet()) {
                put.addHeader(e.getKey(), MessageDigestUtil.utf8ToIso88591(e.getValue()));
            }
        }
        if (StringUtils.isNotBlank(body)) {
            put.setEntity(new StringEntity(body, StandardCharsets.UTF_8));
        }
        Response response = convert(HttpPoolUtil.getHttpClient().execute(put));
        if (log.isInfoEnabled()) {
            log.info("url={}, headers={}, body={}, res={}", url, put.getAllHeaders(), body, response.getBody());
        }
        return response;
    }

    /**
     * HTTP DELETE
     */
    private static Response httpDelete(String host, String path,
                                       String appKey, String appSecret,
                                       Map<String, String> headers,
                                       Map<String, String> queries) throws Exception {
        headers = initialBasicHeader(HttpMethod.DELETE, path, headers, queries, appKey, appSecret);
        String url = HttpPoolUtil.getUrl(host, path, queries);

        HttpDelete delete = new HttpDelete(url);
        if (headers != null) {
            for (Map.Entry<String, String> e : headers.entrySet()) {
                delete.addHeader(e.getKey(), MessageDigestUtil.utf8ToIso88591(e.getValue()));
            }
        }
        Response response = convert(HttpPoolUtil.getHttpClient().execute(delete));
        if (log.isInfoEnabled()) {
            log.info("url={}, headers={}, res={}", url, delete.getAllHeaders(), response.getBody());
        }
        return response;
    }

}
