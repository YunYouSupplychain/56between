package com.yunyou.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.Lists;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import com.yunyou.common.config.Global;
import com.yunyou.core.mapper.JsonMapper;
import com.yunyou.core.persistence.BaseEntity;
import com.yunyou.core.web.context.LogHandlerExceptionResolver;
import com.yunyou.modules.sys.interceptor.LogInterceptor;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MarshallingHttpMessageConverter;
import org.springframework.oxm.xstream.XStreamMarshaller;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.XmlViewResolver;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Properties;

@Configuration
@EnableWebMvc
@PropertySource(value = "classpath:/properties/yunyou.properties", ignoreResourceNotFound = true)
@ComponentScan(basePackages = "com.yunyou",
        includeFilters = @ComponentScan.Filter(classes = Controller.class), useDefaultFilters = false)
public class SpringMvcConfig extends WebMvcConfigurerAdapter {

    private final static String ADMIN_PATH = Global.getAdminPath();

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 部分资源文件优先从classpath:/META-INF/resources路径下查找，解决读取打包在jar中的资源文件
        // webpage资源文件
        registry.addResourceHandler("/webpage/**")
                .addResourceLocations("classpath:/META-INF/webpage/", "/webpage/");
        // static资源文件
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/META-INF/resources/static/", "/static/")
                .setCachePeriod(31536000);
        // WEB-INF资源文件
        registry.addResourceHandler("/WEB-INF/**")
                .addResourceLocations("classpath:/META-INF/resources/WEB-INF/", "/WEB-INF/")
                .setCachePeriod(31536000);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("redirect:/a");
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor())
                .addPathPatterns(ADMIN_PATH + "/**")
                .excludePathPatterns(ADMIN_PATH + "/")
                .excludePathPatterns(ADMIN_PATH + "/login")
                .excludePathPatterns(ADMIN_PATH + "/sys/menu/treeData");
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.mediaType("xml", MediaType.APPLICATION_XML);
        configurer.mediaType("json", MediaType.APPLICATION_JSON);
        configurer.ignoreUnknownPathExtensions(true);
        configurer.ignoreAcceptHeader(true);
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new StringHttpMessageConverter(StandardCharsets.UTF_8));

        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter =
                new MappingJackson2HttpMessageConverter();
        mappingJackson2HttpMessageConverter.setSupportedMediaTypes(
                Lists.newArrayList(MediaType.APPLICATION_JSON_UTF8));
        mappingJackson2HttpMessageConverter.setPrettyPrint(false);
        mappingJackson2HttpMessageConverter.setObjectMapper(
                new JsonMapper(JsonInclude.Include.NON_NULL));
        converters.add(mappingJackson2HttpMessageConverter);

        XStreamMarshaller xStreamMarshaller = new XStreamMarshaller();
        xStreamMarshaller.setStreamDriver(new StaxDriver());
        xStreamMarshaller.setAnnotatedClasses(BaseEntity.class);
        MarshallingHttpMessageConverter marshallingHttpMessageConverter =
                new MarshallingHttpMessageConverter(xStreamMarshaller);
        marshallingHttpMessageConverter.setSupportedMediaTypes(
                Lists.newArrayList(MediaType.APPLICATION_XML));
        converters.add(marshallingHttpMessageConverter);
    }

    @Override
    public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
        exceptionResolvers.add(0, new LogHandlerExceptionResolver());
    }

    @Bean(name = "defaultAdvisorAutoProxyCreator")
    @DependsOn(value = "lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator proxyCreator = new DefaultAdvisorAutoProxyCreator();
        proxyCreator.setProxyTargetClass(true);
        return proxyCreator;
    }

    @Bean(name = "freemarkerConfig")
    public FreeMarkerConfigurer freeMarkerConfigurer(Environment env) {
        FreeMarkerConfigurer freeMarkerConfigurer = new FreeMarkerConfigurer();
        freeMarkerConfigurer.setTemplateLoaderPath(env.getProperty("web.view.prefix"));
        return freeMarkerConfigurer;
    }

    @Bean(name = "jasperReportResolver")
    public XmlViewResolver jasperReportResolver(ApplicationContext context) {
        XmlViewResolver resolver = new XmlViewResolver();
        resolver.setLocation(context.getResource("classpath:/jasper-defs.xml"));
        resolver.setOrder(0);
        return resolver;
    }

    @Bean(name = "htmlViewResolver")
    public ViewResolver htmlViewResolver() {
        FreeMarkerViewResolver viewResolver = new FreeMarkerViewResolver("", ".html");
        viewResolver.setContentType("text/html;charset=utf-8");
        viewResolver.setOrder(1);
        return viewResolver;
    }

    @Bean
    public ViewResolver viewResolver(Environment env) {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix(env.getProperty("web.view.prefix"));
        resolver.setSuffix(env.getProperty("web.view.suffix"));
        resolver.setOrder(2);
        return resolver;
    }

    @Bean
    public LogInterceptor logInterceptor() {
        return new LogInterceptor();
    }

    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver multipartResolver(Environment env) {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setMaxUploadSize(Integer.parseInt(env.getProperty("web.maxUploadSize")));
        return resolver;
    }

    @Bean(name = "simpleMappingExceptionResolver")
    public SimpleMappingExceptionResolver simpleMappingExceptionResolver() {
        Properties exceptionMappings = new Properties();
        exceptionMappings.setProperty("org.apache.shiro.authz.UnauthorizedException", "error/403");
        exceptionMappings.setProperty("java.lang.Throwable", "error/500");

        SimpleMappingExceptionResolver resolver = new SimpleMappingExceptionResolver();
        resolver.setExceptionMappings(exceptionMappings);
        resolver.addStatusCode("error/400", 400);
        resolver.addStatusCode("error/403", 403);
        resolver.addStatusCode("error/404", 404);
        resolver.addStatusCode("error/500", 500);
        return resolver;
    }

}
