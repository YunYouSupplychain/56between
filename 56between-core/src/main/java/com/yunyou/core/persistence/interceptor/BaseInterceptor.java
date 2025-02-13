package com.yunyou.core.persistence.interceptor;

import com.yunyou.common.config.Global;
import com.yunyou.common.utils.Reflections;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.persistence.dialect.Dialect;
import com.yunyou.core.persistence.dialect.db.*;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.plugin.Interceptor;

import java.io.Serializable;

/**
 * Mybatis分页拦截器基类
 */
public abstract class BaseInterceptor implements Interceptor, Serializable {

    private static final long serialVersionUID = 1L;

    protected static final String PAGE = "page";

    protected static final String DELEGATE = "delegate";

    protected static final String MAPPED_STATEMENT = "mappedStatement";

    protected Log log = LogFactory.getLog(this.getClass());

    protected Dialect DIALECT;

    /**
     * 对参数进行转换和检查
     *
     * @param parameterObject 参数对象
     * @return 分页对象
     */
    @SuppressWarnings("unchecked")
    protected static Page<Object> convertParameter(Object parameterObject) {
        try {
            if (parameterObject instanceof Page) {
                return (Page<Object>) parameterObject;
            } else {
                return (Page<Object>) Reflections.getFieldValue(parameterObject, PAGE);
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 设置属性，支持自定义方言类和制定数据库的方式
     * <code>dialectClass</code>,自定义方言类。可以不配置这项
     * <ode>dbms</ode> 数据库类型，插件支持的数据库
     * <code>sqlPattern</code> 需要拦截的SQL ID
     */
    protected void initProperties() {
        Dialect dialect = dialect = new MySQLDialect();
        String dbType = Global.getConfig("jdbc.type");
        DIALECT = dialect;
        /*_SQL_PATTERN = p.getProperty("sqlPattern");
        _SQL_PATTERN = Global.getConfig("mybatis.pagePattern");
        if (StringUtils.isEmpty(_SQL_PATTERN)) {
            throw new RuntimeException("sqlPattern property is not found!");
        }*/
    }
}
