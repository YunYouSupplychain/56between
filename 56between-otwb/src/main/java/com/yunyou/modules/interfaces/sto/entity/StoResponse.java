package com.yunyou.modules.interfaces.sto.entity;

import java.io.Serializable;

/**
 * 响应报文
 */
public class StoResponse<T> implements Serializable {
    private static final long serialVersionUID = -9083814794369414107L;
    // 是否成功
    private boolean success;
    // 错误编码
    private String errorCode;
    // 错误信息
    private String errorMsg;
    // 是否重试
    private boolean needRetry;
    // 请求id
    private String requestId;
    // 异常信息
    private String expInfo;
    private T data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public boolean isNeedRetry() {
        return needRetry;
    }

    public void setNeedRetry(boolean needRetry) {
        this.needRetry = needRetry;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getExpInfo() {
        return expInfo;
    }

    public void setExpInfo(String expInfo) {
        this.expInfo = expInfo;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
