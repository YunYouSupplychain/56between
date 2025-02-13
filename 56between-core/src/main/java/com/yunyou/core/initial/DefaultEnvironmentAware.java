package com.yunyou.core.initial;

import com.yunyou.common.config.Global;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.*;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Bean初始化时加载默认配置资源文件到上下文环境中
 * 执行于@PropertySource加载配置之后
 *
 * @author yunyou
 */
@Component
public class DefaultEnvironmentAware implements EnvironmentAware, BeanFactoryPostProcessor {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final String DEFAULT_CONFIG_PROPERTY_RESOURCE_NAME = "defaultInitParam";

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
    }

    @Override
    public void setEnvironment(Environment environment) {
        MutablePropertySources propertySources = ((ConfigurableEnvironment) environment).getPropertySources();

        // 读取classpath下的default.properties
        InputStream in = getClass().getClassLoader().getResourceAsStream("default.properties");
        if (in == null) {
            if (logger.isInfoEnabled()) {
                logger.info("Default default.properties not found in class path");
            }
        } else {
            try {
                EncodedResource encodedResource =
                        new EncodedResource(new InputStreamResource(in), StandardCharsets.UTF_8);
                ResourcePropertySource newSource =
                        new ResourcePropertySource(DEFAULT_CONFIG_PROPERTY_RESOURCE_NAME, encodedResource);
                if (propertySources.contains(DEFAULT_CONFIG_PROPERTY_RESOURCE_NAME)) {
                    PropertySource<?> existing = propertySources.get(DEFAULT_CONFIG_PROPERTY_RESOURCE_NAME);
                    if (existing instanceof CompositePropertySource) {
                        ((CompositePropertySource) existing).addPropertySource(newSource);
                    } else {
                        CompositePropertySource composite = new CompositePropertySource(DEFAULT_CONFIG_PROPERTY_RESOURCE_NAME);
                        if (existing instanceof ResourcePropertySource) {
                            composite.addPropertySource(((ResourcePropertySource) existing).withResourceName());
                        }
                        composite.addPropertySource(newSource);
                        propertySources.replace(DEFAULT_CONFIG_PROPERTY_RESOURCE_NAME, composite);
                    }
                } else {
                    CompositePropertySource composite = new CompositePropertySource(DEFAULT_CONFIG_PROPERTY_RESOURCE_NAME);
                    composite.addPropertySource(newSource);
                    propertySources.addLast(composite);
                }
            } catch (IOException e) {
                if (logger.isInfoEnabled()) {
                    logger.info("Resource properties file: 'default.properties' "
                            + "could not be read from the classpath.");
                }
            }
        }

        // 将Environment注入Global
        Global.setEnvironment(environment);
    }

}
