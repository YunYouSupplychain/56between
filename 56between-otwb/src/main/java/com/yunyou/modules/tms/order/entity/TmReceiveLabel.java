package com.yunyou.modules.tms.order.entity;

import com.yunyou.core.persistence.DataEntity;

/**
 * 收货标签Entity
 *
 * @author liujianhua
 * @version 2020-03-11
 */
public class TmReceiveLabel extends DataEntity<TmReceiveLabel> {

    private static final long serialVersionUID = 1L;
    // 收货网点编码
    private String receiveOutletCode;
    // 运输单号
    private String transportNo;
    // 客户单号
    private String customerNo;
    // 派车单号
    private String dispatchNo;
    // 标签号
    private String labelNo;
    // 是否App录入
    private String isAppInput;
    // 数量
    private Long qty;
    // 重量
    private Double weight;
    // 体积
    private Double cubic;
    // 机构ID
    private String orgId;
    // 基础数据机构ID
    private String baseOrgId;
    // 货主编码
    private String ownerCode;
    // 商品编码
    private String skuCode;

    public TmReceiveLabel() {
    }

    public TmReceiveLabel(String id) {
        super(id);
    }

    public String getReceiveOutletCode() {
        return receiveOutletCode;
    }

    public void setReceiveOutletCode(String receiveOutletCode) {
        this.receiveOutletCode = receiveOutletCode;
    }

    public String getTransportNo() {
        return transportNo;
    }

    public void setTransportNo(String transportNo) {
        this.transportNo = transportNo;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public String getDispatchNo() {
        return dispatchNo;
    }

    public void setDispatchNo(String dispatchNo) {
        this.dispatchNo = dispatchNo;
    }

    public String getLabelNo() {
        return labelNo;
    }

    public void setLabelNo(String labelNo) {
        this.labelNo = labelNo;
    }

    public String getIsAppInput() {
        return isAppInput;
    }

    public void setIsAppInput(String isAppInput) {
        this.isAppInput = isAppInput;
    }

    public Long getQty() {
        return qty;
    }

    public void setQty(Long qty) {
        this.qty = qty;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getCubic() {
        return cubic;
    }

    public void setCubic(Double cubic) {
        this.cubic = cubic;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getBaseOrgId() {
        return baseOrgId;
    }

    public void setBaseOrgId(String baseOrgId) {
        this.baseOrgId = baseOrgId;
    }

    public String getOwnerCode() {
        return ownerCode;
    }

    public void setOwnerCode(String ownerCode) {
        this.ownerCode = ownerCode;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }
}