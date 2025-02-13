package com.yunyou.core.web.context;

import javax.servlet.ServletContext;

import org.springframework.web.context.WebApplicationContext;

import com.yunyou.modules.sys.service.SystemService;

/**
 * @author yunyou
 */
public class WebContextListener extends org.springframework.web.context.ContextLoaderListener {

    @Override
    public WebApplicationContext initWebApplicationContext(ServletContext servletContext) {
        if (!SystemService.printKeyLoadMessage()){
            return null;
        }
        return super.initWebApplicationContext(servletContext);
    }
}
