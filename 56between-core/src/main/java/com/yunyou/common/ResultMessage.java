package com.yunyou.common;

import com.yunyou.common.utils.StringUtils;

import java.io.Serializable;

/**
 * 结果Message
 */
public class ResultMessage implements Serializable {
    private static final long serialVersionUID = 1L;
    private boolean success = true;
    private String message;
    private Object data;

    public ResultMessage() {
    }

    public ResultMessage(boolean success) {
        this.success = success;
    }

    public ResultMessage(String message) {
        this.message = message;
    }

    public ResultMessage(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void addMessage(String message) {
        this.message = StringUtils.isBlank(this.message) ? message : (this.message + message);
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
