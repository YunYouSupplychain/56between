package com.yunyou.modules.sys.interceptor;

import com.yunyou.common.json.AjaxJson;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 文件上传拦截器
 *
 * @author zyf
 * @version 2020-3-11
 */
public class FileUploadInterceptor implements HandlerInterceptor {

    private long maxSize;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request != null && ServletFileUpload.isMultipartContent(request)) {
            ServletRequestContext ctx = new ServletRequestContext(request);
            long requestSize = ctx.contentLength();
            if (requestSize > maxSize) {
                try {
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    AjaxJson j = new AjaxJson();
                    j.setSuccess(false);
                    j.setMsg("失败！文件太大，应小于" + maxSize + ", 实际为：" + requestSize);
                    PrintWriter writer = response.getWriter();
                    writer.write(j.getJsonStr());
                    writer.flush();
                    return false;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }

    public void setMaxSize(long maxSize) {
        this.maxSize = maxSize;
    }
}
