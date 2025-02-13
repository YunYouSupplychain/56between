package com.yunyou.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.yunyou.core.persistence.BaseEntity;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.core.transaction.MyDataSourceTransactionManager;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.TypeAliasesUtils;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@EnableAspectJAutoProxy
@EnableTransactionManagement
@EnableAsync
@EnableScheduling
@PropertySource(value = "classpath:/properties/yunyou.properties", ignoreResourceNotFound = true)
@ComponentScan(basePackages = {"com.yunyou"},
        excludeFilters = {@ComponentScan.Filter(classes = Controller.class),
                @ComponentScan.Filter(classes = EnableWebMvc.class)
        })
public class SpringContextConfig {

    @Bean(name = "dataSource")
    public DataSource dataSource(Environment env) throws SQLException {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(env.getProperty("jdbc.driver"));
        dataSource.setUrl(env.getProperty("jdbc.url"));
        dataSource.setUsername(env.getProperty("jdbc.username"));
        dataSource.setPassword(env.getProperty("jdbc.password"));
        dataSource.setInitialSize(Integer.parseInt(env.getProperty("jdbc.pool.initialSize")));
        dataSource.setMinIdle(Integer.parseInt(env.getProperty("jdbc.pool.minIdle")));
        dataSource.setMaxActive(Integer.parseInt(env.getProperty("jdbc.pool.maxActive")));
        dataSource.setMaxWait(Long.parseLong(env.getProperty("jdbc.pool.maxWait")));
        dataSource.setMinEvictableIdleTimeMillis(Long.parseLong(env.getProperty("jdbc.pool.minEvictableIdleTimeMillis")));
        dataSource.setTimeBetweenEvictionRunsMillis(Long.parseLong(env.getProperty("jdbc.pool.timeBetweenEvictionRunsMillis")));
        dataSource.setConnectionErrorRetryAttempts(Integer.parseInt(env.getProperty("jdbc.pool.connectionErrorRetryAttempts", "1")));
        dataSource.setBreakAfterAcquireFailure(Boolean.parseBoolean(env.getProperty("jdbc.pool.breakAfterAcquireFailure", "false")));
        dataSource.setValidationQuery(env.getProperty("jdbc.pool.validationQuery"));
        dataSource.setFilters(env.getProperty("jdbc.pool.filters"));
        dataSource.setTestWhileIdle(true);
        dataSource.setTestOnBorrow(false);
        dataSource.setTestOnReturn(false);
        dataSource.setUsePingMethod(false);
        return dataSource;
    }

    /**
     * 配置事物管理器
     */
    @Bean(name = "transactionManager")
    public MyDataSourceTransactionManager transactionManager(DataSource dataSource) {
        return new MyDataSourceTransactionManager(dataSource);
    }

    /**
     * myBatis 配置
     */
    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactoryBean sqlSessionFactory(DataSource dataSource) throws IOException {
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

        Properties configuration = new Properties();
        configuration.setProperty("dual", "");

        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setTypeAliasesSuperType(BaseEntity.class);
        factoryBean.setTypeAliases(TypeAliasesUtils.findTypeAliases("classpath*:com/yunyou/modules/**/entity/**.class"));
        factoryBean.setMapperLocations(resolver.getResources("classpath*:com/yunyou/modules/**/*Mapper.xml"));
        factoryBean.setConfigLocation(resolver.getResource("classpath:/mybatis-config.xml"));
        factoryBean.setConfigurationProperties(configuration);
        return factoryBean;
    }

    @Bean(name = "mapperScannerConfigurer")
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer configurer = new MapperScannerConfigurer();
        configurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
        configurer.setBasePackage("com.yunyou");
        configurer.setAnnotationClass(MyBatisMapper.class);
        return configurer;
    }

    @Bean(name = "jdbcTemplate")
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean(name = "executor")
    public Executor executor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setThreadNamePrefix("BQExecutor-");
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(200);
        executor.setQueueCapacity(10);
        executor.initialize();
        return executor;
    }

    @Bean(name = "scheduler")
    public TaskScheduler scheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(10);
        return scheduler;
    }

    @Bean(name = "schedulerFactoryBean")
    public SchedulerFactoryBean schedulerFactoryBean(DataSource dataSource) throws IOException {
        Properties properties = new Properties();
        ClassLoader cl = getClass().getClassLoader();
        InputStream in = cl.getResourceAsStream("properties/quartz.properties");
        if (in == null) {
            in = cl.getResourceAsStream("quartz.properties");
        }
        properties.load(in);

        // 是否自动启动任务调度程序，默认启动
        boolean isAutoStartup = Boolean.parseBoolean(properties.getProperty("scheduler.autoStartup", "true"));

        SchedulerFactoryBean factoryBean = new SchedulerFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setQuartzProperties(properties);
        factoryBean.setAutoStartup(isAutoStartup);
        return factoryBean;
    }

    @Bean(name = "validator")
    public LocalValidatorFactoryBean validator() {
        return new LocalValidatorFactoryBean();
    }

}