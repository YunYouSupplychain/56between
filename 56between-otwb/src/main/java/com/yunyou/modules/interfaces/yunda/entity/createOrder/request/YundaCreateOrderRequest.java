package com.yunyou.modules.interfaces.yunda.entity.createOrder.request;

import java.util.List;

public class YundaCreateOrderRequest {

    // 订单唯一序列号（由字母、数字、下划线组成，必须保证唯一，请对特殊符号进行过滤）
    private String order_serial_no;
    // 大客户系统订单的订单号（可与订单唯一序列号相同）
    private String khddh;
    // 内部参考号，供大客户自己使用，可以是客户的客户编号
    private String nbckh;
    // 运单类型（参数值对照order_type字典表）
    private String order_type;
    // 发件人
    private YundaCreateOrderCustomerRequest sender;
    // 收件人
    private YundaCreateOrderCustomerRequest receiver;
    // 物品重量
    private String weight;
    // 尺寸，格式（长,宽,高）,单位cm
    private String size;
    // 货物金额
    private String value;
    // 代收货款金额（目前仅用于cod订单，cod订单必填）
    private String collection_value;
    // 商品性质（保留字段，暂时不用）
    private String special;
    // 判断订单是否是隐私订单，如果是隐私订单把收件人姓名和电话进行脱敏处理（1表示是，0表示否）（隐私面单业务对接需与韵达方确认业务需求）
    private String isProtectPrivacy;
    // 商品
    private List<YundaCreateOrderItemRequest> items;
    // 订单备注
    private String remark;
    // 可以自定义显示信息1，打印在客户自定义区域1，换行请用\n
    private String cus_area1;
    // 可以自定义显示信息2，打印在客户自定义区域2，换行请用\n
    private String cus_area2;
    // 客户波次号，按照此号进行批量打印校验，批量下单时，此波次号需要相同
    private String wave_no;

    public String getOrder_serial_no() {
        return order_serial_no;
    }

    public void setOrder_serial_no(String order_serial_no) {
        this.order_serial_no = order_serial_no;
    }

    public String getKhddh() {
        return khddh;
    }

    public void setKhddh(String khddh) {
        this.khddh = khddh;
    }

    public String getNbckh() {
        return nbckh;
    }

    public void setNbckh(String nbckh) {
        this.nbckh = nbckh;
    }

    public String getOrder_type() {
        return order_type;
    }

    public void setOrder_type(String order_type) {
        this.order_type = order_type;
    }

    public YundaCreateOrderCustomerRequest getSender() {
        return sender;
    }

    public void setSender(YundaCreateOrderCustomerRequest sender) {
        this.sender = sender;
    }

    public YundaCreateOrderCustomerRequest getReceiver() {
        return receiver;
    }

    public void setReceiver(YundaCreateOrderCustomerRequest receiver) {
        this.receiver = receiver;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCollection_value() {
        return collection_value;
    }

    public void setCollection_value(String collection_value) {
        this.collection_value = collection_value;
    }

    public String getSpecial() {
        return special;
    }

    public void setSpecial(String special) {
        this.special = special;
    }

    public String getIsProtectPrivacy() {
        return isProtectPrivacy;
    }

    public void setIsProtectPrivacy(String isProtectPrivacy) {
        this.isProtectPrivacy = isProtectPrivacy;
    }

    public List<YundaCreateOrderItemRequest> getItems() {
        return items;
    }

    public void setItems(List<YundaCreateOrderItemRequest> items) {
        this.items = items;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCus_area1() {
        return cus_area1;
    }

    public void setCus_area1(String cus_area1) {
        this.cus_area1 = cus_area1;
    }

    public String getCus_area2() {
        return cus_area2;
    }

    public void setCus_area2(String cus_area2) {
        this.cus_area2 = cus_area2;
    }

    public String getWave_no() {
        return wave_no;
    }

    public void setWave_no(String wave_no) {
        this.wave_no = wave_no;
    }
}
