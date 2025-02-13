package com.yunyou.modules.tms.order.entity;

import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.core.persistence.DataEntity;

/**
 * 发货标签Entity
 *
 * @author liujianhua
 * @version 2020-03-11
 */
public class TmDeliverLabel extends DataEntity<TmDeliverLabel> {

    private static final long serialVersionUID = 1L;
    // 发货网点编码
    private String deliverOutletCode;
    // 客户单号
    private String customerNo;
    // 派车单号
    private String dispatchNo;
    // 运输单号
    private String transportNo;
    // 标签号
    private String labelNo;
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
    // 承运商编码
    private String carrierCode;
    // 货主编码
    private String ownerCode;
    // 商品编码
    private String skuCode;

    public TmDeliverLabel() {
        super();
    }

    public TmDeliverLabel(String id) {
        super(id);
    }

    public String getDeliverOutletCode() {
        return deliverOutletCode;
    }

    public void setDeliverOutletCode(String deliverOutletCode) {
        this.deliverOutletCode = deliverOutletCode;
    }

    @ExcelField(title = "标签号", align = 2, sort = 7)
    public String getLabelNo() {
        return labelNo;
    }

    public void setLabelNo(String labelNo) {
        this.labelNo = labelNo;
    }

    @ExcelField(title = "运输单号", align = 2, sort = 8)
    public String getTransportNo() {
        return transportNo;
    }

    public void setTransportNo(String transportNo) {
        this.transportNo = transportNo;
    }

    @ExcelField(title = "客户单号", align = 2, sort = 9)
    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    @ExcelField(title = "派车单号", align = 2, sort = 10)
    public String getDispatchNo() {
        return dispatchNo;
    }

    public void setDispatchNo(String dispatchNo) {
        this.dispatchNo = dispatchNo;
    }

    @ExcelField(title = "机构ID", align = 2, sort = 11)
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

    @ExcelField(title = "承运商编码", align = 2, sort = 13)
    public String getCarrierCode() {
        return carrierCode;
    }

    public void setCarrierCode(String carrierCode) {
        this.carrierCode = carrierCode;
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