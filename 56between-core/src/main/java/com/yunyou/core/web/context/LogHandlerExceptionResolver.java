package com.yunyou.core.web.context;

import com.yunyou.modules.sys.utils.LogUtils;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 异常日志
 *
 * @author yunyou
 * @since 2023/7/27 14:38
 */
public class LogHandlerExceptionResolver implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 保存日志
        LogUtils.saveLog(request, handler, ex, null);
        return null;
    }
}
