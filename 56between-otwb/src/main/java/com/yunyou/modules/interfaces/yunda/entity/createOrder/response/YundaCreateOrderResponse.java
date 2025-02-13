package com.yunyou.modules.interfaces.yunda.entity.createOrder.response;

import java.util.List;

public class YundaCreateOrderResponse {

    List<YundaCreateOrderResponseData> response;

    public List<YundaCreateOrderResponseData> getResponse() {
        return response;
    }

    public void setResponse(List<YundaCreateOrderResponseData> response) {
        this.response = response;
    }
}
