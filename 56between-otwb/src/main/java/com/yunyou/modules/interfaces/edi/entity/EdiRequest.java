package com.yunyou.modules.interfaces.edi.entity;

public class EdiRequest {

    private String ediType;
    private Object data;

    public EdiRequest() {
    }

    public EdiRequest(String ediType, Object data) {
        this.ediType = ediType;
        this.data = data;
    }

    public String getEdiType() {
        return ediType;
    }

    public void setEdiType(String ediType) {
        this.ediType = ediType;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
