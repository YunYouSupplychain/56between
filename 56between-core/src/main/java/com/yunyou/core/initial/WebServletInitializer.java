package com.yunyou.core.initial;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.opensymphony.sitemesh.webapp.SiteMeshFilter;
import com.yunyou.config.SpringContextConfig;
import com.yunyou.config.SpringMvcConfig;
import com.yunyou.config.SpringShiroConfig;
import com.yunyou.core.servlet.UserFileDownloadServlet;
import com.yunyou.core.servlet.ValidateCodeServlet;
import com.yunyou.core.web.HttpServletRequestReplacedFilter;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import org.springframework.web.util.Log4jConfigListener;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

/**
 * WEB应用初始化
 *
 * @author yunyou
 */
public class WebServletInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        servletContext.setInitParameter("webAppRootKey", "webapp.root");
        servletContext.setInitParameter("log4jConfigLocation", getLog4jConfigLocation());
        // 监听
        servletContext.addListener(new Log4jConfigListener());
        servletContext.addListener(new RequestContextListener());
        // 过滤器
        this.addFilter(servletContext);
        // servlet
        this.addServlet(servletContext);

        super.onStartup(servletContext);
    }

    /**
     * rootConfig 想当于之前的 application-context.xml
     */
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[]{SpringContextConfig.class, SpringShiroConfig.class};
    }

    /**
     * servletConfig 相当于之前的 mvc-servlet.xml
     */
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[]{SpringMvcConfig.class};
    }

    /**
     * DispatchServlet 路径
     */
    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    private void addFilter(ServletContext servletContext) {
        this.addEncodingFilter(servletContext);
        this.addShiroFilter(servletContext);
        this.addSitemeshFilter(servletContext);
        this.addDruidWebStatFilter(servletContext);
        this.addServletFilter(servletContext);
    }

    private void addServlet(ServletContext servletContext) {
        this.addUserFileDownloadServlet(servletContext);
        this.addDruidStatViewServlet(servletContext);
        this.addValidateCodeServlet(servletContext);
        this.addJasperImageServlet(servletContext);
    }

    private void addEncodingFilter(ServletContext context) {
        FilterRegistration.Dynamic filter = context.addFilter("encodingFilter", CharacterEncodingFilter.class);
        filter.setInitParameter("encoding", "UTF-8");
        filter.setInitParameter("forceEncoding", "true");
        filter.addMappingForServletNames(null, true, "encodingFilter");
        filter.addMappingForUrlPatterns(null, true, "/*");
    }

    private void addShiroFilter(ServletContext context) {
        FilterRegistration.Dynamic filter = context.addFilter("shiroFilter", DelegatingFilterProxy.class);
        filter.setInitParameter("targetFilterLifecycle", "true");
        filter.addMappingForServletNames(null, true, "shiroFilter");
        filter.addMappingForUrlPatterns(null, true, "/*");
    }

    private void addSitemeshFilter(ServletContext context) {
        FilterRegistration.Dynamic filter = context.addFilter("sitemeshFilter", SiteMeshFilter.class);
        filter.addMappingForServletNames(null, true, "sitemeshFilter", "sitemeshFilter");
        filter.addMappingForUrlPatterns(null, true, "/a/*");
    }

    private void addDruidWebStatFilter(ServletContext context) {
        FilterRegistration filter = context.addFilter("druidWebStatFilter", WebStatFilter.class);
        filter.setInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,*.jsp,/druid/*,/download/*");
        filter.setInitParameter("sessionStatMaxCount", "2000");
        filter.setInitParameter("principalSessionName", "session_user_key");
        filter.setInitParameter("profileEnable", "true");
        filter.addMappingForServletNames(null, true, "druidWebStatFilter");
        filter.addMappingForUrlPatterns(null, true, "/*");
    }

    private void addServletFilter(ServletContext context) {
        FilterRegistration filter = context.addFilter("httpServletRequestReplacedFilter", HttpServletRequestReplacedFilter.class);
        filter.addMappingForServletNames(null, true, "httpServletRequestReplacedFilter", "httpServletRequestReplacedFilter");
        filter.addMappingForUrlPatterns(null, true, "/a/*");
    }

    private void addUserFileDownloadServlet(ServletContext context) {
        ServletRegistration.Dynamic servlet = context.addServlet("UserFileDownloadServlet", UserFileDownloadServlet.class);
        servlet.addMapping("/userfiles/*");
    }

    private void addDruidStatViewServlet(ServletContext context) {
        ServletRegistration.Dynamic servlet = context.addServlet("DruidStatView", StatViewServlet.class);
        servlet.setInitParameter("resetEnable", "true");
        servlet.setInitParameter("loginUsername", "druid");
        servlet.setInitParameter("loginPassword", "yunlian!@2021#");
        servlet.addMapping("/druid/*");
    }

    private void addValidateCodeServlet(ServletContext context) {
        ServletRegistration.Dynamic servlet = context.addServlet("ValidateCodeServlet", ValidateCodeServlet.class);
        servlet.addMapping("/servlet/validateCodeServlet");
    }

    private void addJasperImageServlet(ServletContext context) {
//        ServletRegistration.Dynamic servlet = context.addServlet("JasperImageServlet", ImageServlet.class);
//        servlet.addMapping("/servlet/image");
    }

    private String getLog4jConfigLocation () {
        String location = "classpath:properties/log4j.properties";
        if (!new DefaultResourceLoader().getResource(location).exists()) {
            location = "classpath:log4j.properties";
        }
        return location;
    }
}
