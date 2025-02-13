package com.yunyou.modules.interfaces.gps;

import com.yunyou.common.utils.StringUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.yunyou.modules.interfaces.gps.constant.Constants.*;

/**
 * FAQ：列举部分使用sdk遇到的问题，仅供参考
 * 1、每次请求new HttpClient，tcp + handshake + ssl...导致接口调用效率低。httpclient是一个线程安全的类，推荐多线程复用httpclient
 * 2、多线程下，未根据场景修改DefaultMaxPerRoute值，默认为5，调用效率低。 同一时间向同一域名发起的总请求数<=DefaultMaxPerRoute<=MaxTotal
 */
public class HttpPoolUtil {
    private static final Logger log = LoggerFactory.getLogger(HttpPoolUtil.class);

    public static volatile boolean isClosed = false;
    //总并发线程数
    public static final int MAX_TOTAL_POOL = 200;
    //同一域名下，并发线程数
    public static final int MAX_TOTAL_PER_ROUTE = 10;
    public static final int MAX_TIMEOUT = 10 * 1000;
    public static final int REQUEST_TIMEOUT = 5000;

    private static RequestConfig requestConfig;
    private static HttpClientBuilder httpClientBuilder;
    private static PoolingHttpClientConnectionManager poolConnManager;
    private static IdleConnectionMonitorThread idleConnectionMonitorThread;
    private static CloseableHttpClient httpClient;

    static {
        // 设置连接池
        poolConnManager = new PoolingHttpClientConnectionManager();
        poolConnManager.setMaxTotal(MAX_TOTAL_POOL);
        poolConnManager.setDefaultMaxPerRoute(MAX_TOTAL_PER_ROUTE);

        RequestConfig.Builder configBuilder = RequestConfig.custom();
        // 设置连接超时
        configBuilder.setConnectTimeout(MAX_TIMEOUT);
        // 设置读取超时
        configBuilder.setSocketTimeout(MAX_TIMEOUT);
        // 设置从连接池获取连接实例的超时
        configBuilder.setConnectionRequestTimeout(REQUEST_TIMEOUT);
        // 在提交请求之前 测试连接是否可用（有性能问题）
        // configBuilder.setStaleConnectionCheckEnabled(true);
        idleConnectionMonitorThread = new IdleConnectionMonitorThread(poolConnManager);
        idleConnectionMonitorThread.start();
        requestConfig = configBuilder.build();
        httpClientBuilder = HttpClientBuilder.create()
                .setConnectionManager(poolConnManager)
                /*.setConnectionManagerShared(true)*/
                .setDefaultRequestConfig(requestConfig);
        httpClient = httpClientBuilder.build();
        log.info(">>>>>>>>>>> PoolingHttpClientConnectionManager初始化成功 >>>>>>>>>>>");
    }

    /**
     * 单例
     * pClient
     *
     * @return htt
     */
    public static CloseableHttpClient getHttpClient() {
        return httpClient;
    }

    /**
     * 获得状态
     */
    public static String getTotalStats() {
        return poolConnManager.getTotalStats().toString();
    }

    /**
     * 关闭连接池资源
     */
    public static void closePool() {
        if (!isClosed) {
            isClosed = true;
            poolConnManager.close();
        }
    }

    /**
     * 空闲连接监控线程
     */
    public static class IdleConnectionMonitorThread extends Thread {

        private final HttpClientConnectionManager connMgr;
        private volatile boolean shutdown;

        public IdleConnectionMonitorThread(HttpClientConnectionManager connMgr) {
            super();
            this.connMgr = connMgr;
        }

        @Override
        public void run() {
            try {
                while (!shutdown) {
                    synchronized (this) {
                        wait(30 * 1000);
                        // Close expired connections
                        connMgr.closeExpiredConnections();
                        // Close connections that have been idle longer than 30 sec
                        connMgr.closeIdleConnections(30, TimeUnit.SECONDS);
                    }
                }
            } catch (InterruptedException ex) {
                // terminate
            }
        }

        public void shutdown() {
            shutdown = true;
            synchronized (this) {
                notifyAll();
            }
        }
    }

    /**
     * 获取url
     *
     * @param host    连接地址
     * @param path    路径
     * @param queries 参数
     */
    public static String getUrl(String host, String path, Map<String, String> queries) throws UnsupportedEncodingException {
        StringBuilder sbUrl = new StringBuilder(host);
        if (StringUtils.isNotBlank(path)) {
            sbUrl.append(path);
        }
        if (queries == null) {
            return sbUrl.toString();
        }
        StringBuilder sbQuery = new StringBuilder();
        for (Map.Entry<String, String> query : queries.entrySet()) {
            if (StringUtils.isBlank(query.getKey())) {
                continue;
            }
            if (0 < sbQuery.length()) {
                sbQuery.append(SPE3_CONNECT);
            }
            sbQuery.append(query.getKey()).append(SPE4_EQUAL);
            if (StringUtils.isNotBlank(query.getValue())) {
                sbQuery.append(URLEncoder.encode(query.getValue(), "UTF-8"));
            }
        }
        if (0 < sbQuery.length()) {
            sbUrl.append(SPE5_QUESTION).append(sbQuery);
        }
        return sbUrl.toString();
    }


}