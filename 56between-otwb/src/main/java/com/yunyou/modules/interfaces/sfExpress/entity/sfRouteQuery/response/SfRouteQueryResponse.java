package com.yunyou.modules.interfaces.sfExpress.entity.sfRouteQuery.response;

import java.util.List;

public class SfRouteQueryResponse {

    private String errorCode;
    private String message;
    private int httpStatus;
    private List<SfRouteQueryResponseData> data;

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

    public List<SfRouteQueryResponseData> getData() {
        return data;
    }

    public void setData(List<SfRouteQueryResponseData> data) {
        this.data = data;
    }
}
