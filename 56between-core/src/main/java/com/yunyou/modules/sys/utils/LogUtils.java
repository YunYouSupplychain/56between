package com.yunyou.modules.sys.utils;

import com.yunyou.common.utils.SpringContextHolder;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.modules.sys.entity.Log;
import com.yunyou.modules.sys.mapper.LogMapper;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

/**
 * 字典工具类
 *
 * @author yunyou
 * @version 2017-11-7
 */
public class LogUtils {
    private static final LogMapper MAPPER = SpringContextHolder.getBean(LogMapper.class);

    /**
     * 保存日志
     */
    public static void saveLog(HttpServletRequest request, String title) {
        saveLog(request, null, null, title);
    }

    /**
     * 保存日志
     */
    public static void saveLog(HttpServletRequest request, Object handler, Exception ex, String title) {
        Log log = new Log();
        log.setTitle(title);
        log.setType(ex == null ? Log.TYPE_ACCESS : Log.TYPE_EXCEPTION);
        log.setRemoteAddr(StringUtils.getRemoteAddr(request));
        log.setUserAgent(request.getHeader("user-agent"));
        log.setRequestUri(request.getRequestURI());
        log.setMethod(request.getMethod());
        if (!request.getParameterMap().isEmpty()) {
            log.setParams(request.getParameterMap());
        } else {
            log.setParamString(getBodyString(request));
        }
        new SaveLogThread(log, handler, ex).start();
    }

    /**
     * 保存日志线程
     */
    public static class SaveLogThread extends Thread {

        private final Log log;
        private final Object handler;
        private final Exception ex;

        public SaveLogThread(Log log, Object handler, Exception ex) {
            super(SaveLogThread.class.getSimpleName());
            this.log = log;
            this.handler = handler;
            this.ex = ex;
        }

        @Override
        public void run() {
            // 获取日志标题
            if (StringUtils.isBlank(log.getTitle()) && handler instanceof HandlerMethod) {
                Method m = ((HandlerMethod) handler).getMethod();
                log.setTitle(m.getDeclaringClass().getName() + "." + m.getName());
            }
            // 如果有异常，设置异常信息
            if (ex != null) {
                StringWriter stringWriter = new StringWriter();
                ex.printStackTrace(new PrintWriter(stringWriter));
                log.setException(stringWriter.toString());
            }
            // 保存日志信息
            log.preInsert();
            MAPPER.insert(log);
        }
    }

    private static String getBodyString(HttpServletRequest request){
        StringBuilder sb = new StringBuilder();
        InputStream inputStream;
        BufferedReader reader;
        try {
            inputStream = request.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            String line = "";
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (sb.length() > 1024) {
            return sb.substring(1, 1024).trim();
        }
        return sb.toString().trim();
    }

}
