package com.yunyou.modules.interfaces.kdBest.utils;

public interface BaseRequest {
    public String obtainServiceType();

    BaseResponse makeResponse(String rsp, String format);
}
