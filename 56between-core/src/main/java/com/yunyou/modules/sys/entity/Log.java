package com.yunyou.modules.sys.entity;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.DataEntity;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.util.Date;
import java.util.Map;

/**
 * 日志Entity
 *
 * @author yunyou
 * @version 2017-8-19
 */
public class Log extends DataEntity<Log> {

    private static final long serialVersionUID = 1L;
    /**
     * 日志类型（1：接入日志；2：错误日志）
     */
    private String type;
    /**
     * 日志标题
     */
    private String title;
    /**
     * 操作用户的IP地址
     */
    private String remoteAddr;
    /**
     * 操作的URI
     */
    private String requestUri;
    /**
     * 操作的方式
     */
    private String method;
    /**
     * 操作提交的数据
     */
    private String params;
    /**
     * 操作用户代理信息
     */
    private String userAgent;
    /**
     * 异常信息
     */
    private String exception;

    /**
     * 开始日期
     */
    private Date beginDate;
    /**
     * 结束日期
     */
    private Date endDate;

    /**
     * 日志类型（1：接入日志；2：错误日志）
     */
    public static final String TYPE_ACCESS = "1";
    public static final String TYPE_EXCEPTION = "2";

    public Log() {
        super();
    }

    public Log(String id) {
        super(id);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRemoteAddr() {
        return remoteAddr;
    }

    public void setRemoteAddr(String remoteAddr) {
        this.remoteAddr = remoteAddr;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getRequestUri() {
        return requestUri;
    }

    public void setRequestUri(String requestUri) {
        this.requestUri = requestUri;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * 设置请求参数
     *
     * @param paramMap 请求参数集合
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void setParams(Map paramMap) {
        if (paramMap == null) {
            return;
        }
        StringBuilder params = new StringBuilder();
        for (Map.Entry<String, String[]> param : ((Map<String, String[]>) paramMap).entrySet()) {
            params.append("".equals(params.toString()) ? "" : "&").append(param.getKey()).append("=");
            String paramValue = (param.getValue() != null && param.getValue().length > 0 ? param.getValue()[0] : "");
            params.append(StringUtils.abbr(StringUtils.endsWithIgnoreCase(param.getKey(), "password") ? "" : paramValue, 100));
        }
        this.params = params.toString();
    }

    public void setParamString(String params) {
        this.params = params;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}