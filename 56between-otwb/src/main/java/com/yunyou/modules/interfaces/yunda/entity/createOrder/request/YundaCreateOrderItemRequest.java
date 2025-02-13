package com.yunyou.modules.interfaces.yunda.entity.createOrder.request;

public class YundaCreateOrderItemRequest {

    // 商品名称（cod订单必填）
    private String name;
    // 数量
    private String number;
    // 商品备注
    private String remark;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
