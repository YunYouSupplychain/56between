package com.yunyou.modules.interfaces.sfExpress.entity.sfCreateOrder.response;

public class SfCreateOrderResponse {

    private String errorCode;       // 错误编码
    private String message;         // 反馈信息
    private int httpStatus;         // 反馈状态（200成功）
    private SfCreateOrderResponseData data;      // 反馈数据

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
    }

    public SfCreateOrderResponseData getData() {
        return data;
    }

    public void setData(SfCreateOrderResponseData data) {
        this.data = data;
    }
}
