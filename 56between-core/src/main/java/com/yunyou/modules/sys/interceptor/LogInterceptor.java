package com.yunyou.modules.sys.interceptor;

import com.yunyou.common.utils.time.DateFormatUtil;
import com.yunyou.common.utils.time.DateUtils;
import com.yunyou.core.service.BaseService;
import com.yunyou.modules.sys.utils.LogUtils;
import org.springframework.core.NamedThreadLocal;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 日志拦截器
 *
 * @author yunyou
 * @version 2017-8-19
 */
public class LogInterceptor extends BaseService implements HandlerInterceptor {

    private static final ThreadLocal<Long> THREAD_LOCAL = new NamedThreadLocal<>("ThreadLocal StartTime");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (logger.isDebugEnabled()) {
            // 开始时间
            long beginTime = System.currentTimeMillis();
            // 线程绑定变量（该数据只有当前请求的线程可见）
            THREAD_LOCAL.set(beginTime);
            logger.debug("开始计时: {}  URI: {}", DateFormatUtil.formatDate("hh:mm:ss.SSS", beginTime), request.getRequestURI());
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        if (modelAndView != null) {
            logger.info("ViewName: {}", modelAndView.getViewName());
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 保存日志
        LogUtils.saveLog(request, handler, ex, null);
        // 打印JVM信息。
        if (logger.isDebugEnabled()) {
            // 得到线程绑定的局部变量（开始时间）
            long beginTime = THREAD_LOCAL.get();
            // 结束时间
            long endTime = System.currentTimeMillis();
            logger.debug("计时结束：{}  耗时：{}  URI: {}  最大内存: {}m  已分配内存: {}m  已分配内存中的剩余空间: {}m  最大可用内存: {}m",
                    DateFormatUtil.formatDate("hh:mm:ss.SSS", endTime),
                    DateUtils.formatDateTime(endTime - beginTime),
                    request.getRequestURI(),
                    Runtime.getRuntime().maxMemory() / 1024 / 1024,
                    Runtime.getRuntime().totalMemory() / 1024 / 1024,
                    Runtime.getRuntime().freeMemory() / 1024 / 1024,
                    (Runtime.getRuntime().maxMemory() - Runtime.getRuntime().totalMemory() + Runtime.getRuntime().freeMemory()) / 1024 / 1024);
        }
        THREAD_LOCAL.remove();
    }

}
