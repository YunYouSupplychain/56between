package com.yunyou.modules.interfaces.zto.entity;

/**
 * 中通面单号获取接口参数内容体
 * @author WMJ
 * @version 2020-05-07
 */
public class ZtoOrderNoRequest {
    // 商家ID，又称电子面单账号、电子面单账户、客户ID(正式环境由中通合作网点提供，测试环境使用test)
    private String partner;
    // 订单号由合作商平台产生，具有平台唯一性。(测试的id一律使用xfs101100111011)必填
    private String id;
    // 1:为电子面单类 （其他类型具体可调用获取类型接口）；74:星联 必填
    private String typeid;
    // 交易号，由合作商平台产生
    private String tradeid;
    // 网点编码，客户购买电子面单的网点
    private String branch_id;
    // 卖家，最好是卖家ID
    private String seller;
    // 买家，最好是买家ID
    private String buyer;
    // 发件人
    private ZtoOrderNoSenderRequest sender;
    // 收件人
    private ZtoOrderNoReceiverRequest receiver;
    // 订单总重量 （千克）
    private Double weight;
    // 订单包裹大小（厘米）, 用半角的逗号来分隔长宽高
    private String size;
    // 订单包裹内货物总数量
    private String quantity;
    // 订单包裹中商品总价值
    private Double price;
    // 运输费
    private Double freight;
    // 保险费
    private Double premium;
    // 包装费
    private Double pack_charges;
    // 其他费用
    private Double other_charges;
    // 订单总金额
    private Double order_sum;
    // 到达收取币种
    private String collect_moneytype;
    // 到达收取金额，一般代收货款或者到付件才需指定
    private Double collect_sum;
    // 订单备注
    private String remark;
    // 订单类型：0标准；1代收；2到付
    private String order_type;

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTypeid() {
        return typeid;
    }

    public void setTypeid(String typeid) {
        this.typeid = typeid;
    }

    public String getTradeid() {
        return tradeid;
    }

    public void setTradeid(String tradeid) {
        this.tradeid = tradeid;
    }

    public String getBranch_id() {
        return branch_id;
    }

    public void setBranch_id(String branch_id) {
        this.branch_id = branch_id;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public ZtoOrderNoSenderRequest getSender() {
        return sender;
    }

    public void setSender(ZtoOrderNoSenderRequest sender) {
        this.sender = sender;
    }

    public ZtoOrderNoReceiverRequest getReceiver() {
        return receiver;
    }

    public void setReceiver(ZtoOrderNoReceiverRequest receiver) {
        this.receiver = receiver;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getFreight() {
        return freight;
    }

    public void setFreight(Double freight) {
        this.freight = freight;
    }

    public Double getPremium() {
        return premium;
    }

    public void setPremium(Double premium) {
        this.premium = premium;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Double getPack_charges() {
        return pack_charges;
    }

    public void setPack_charges(Double pack_charges) {
        this.pack_charges = pack_charges;
    }

    public Double getOther_charges() {
        return other_charges;
    }

    public void setOther_charges(Double other_charges) {
        this.other_charges = other_charges;
    }

    public Double getOrder_sum() {
        return order_sum;
    }

    public void setOrder_sum(Double order_sum) {
        this.order_sum = order_sum;
    }

    public String getCollect_moneytype() {
        return collect_moneytype;
    }

    public void setCollect_moneytype(String collect_moneytype) {
        this.collect_moneytype = collect_moneytype;
    }

    public Double getCollect_sum() {
        return collect_sum;
    }

    public void setCollect_sum(Double collect_sum) {
        this.collect_sum = collect_sum;
    }

    public String getOrder_type() {
        return order_type;
    }

    public void setOrder_type(String order_type) {
        this.order_type = order_type;
    }
}
