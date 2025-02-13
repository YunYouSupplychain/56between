package com.yunyou.modules.tms.order.entity;

import com.yunyou.core.persistence.DataEntity;

import java.util.Date;

/**
 * 运输订单标签信息Entity
 *
 * @author liujianhua
 * @version 2020-03-04
 */
public class TmTransportOrderLabel extends DataEntity<TmTransportOrderLabel> {

    private static final long serialVersionUID = 1L;
    // 运输单号
    private String transportNo;
    // 客户单号
    private String customerNo;
    // 标签号
    private String labelNo;
    // 状态(新建、已收货、已签收、已回单)
    private String status;
    // 数量
    private Double qty;
    // 重量
    private Double weight;
    // 体积
    private Double cubic;
    // 机构ID
    private String orgId;
    // 基础数据机构ID
    private String baseOrgId;
    // 签收人
    private String signBy;
    // 签收时间
    private Date signTime;
    // 签收备注
    private String signRemarks;
    // 回单人
    private String receiptBy;
    // 回单时间
    private Date receiptTime;
    // 回单备注
    private String receiptRemarks;
    // 货主编码
    private String ownerCode;
    // 商品编码
    private String skuCode;
    // 明细行号
    private String lineNo;

    private String def1; // 栈板号
    private String def2; // 框号
    private String def3;
    private String def4;
    private String def5;
    private String def6;
    private String def7;
    private String def8;
    private String def9;
    private String def10;

    public TmTransportOrderLabel() {
        super();
    }

    public TmTransportOrderLabel(String id) {
        super(id);
    }

    public TmTransportOrderLabel(String transportNo, String orgId) {
        this.transportNo = transportNo;
        this.orgId = orgId;
    }

    public TmTransportOrderLabel(String transportNo, String ownerCode, String lineNo, String skuCode, String orgId) {
        this.transportNo = transportNo;
        this.ownerCode = ownerCode;
        this.lineNo = lineNo;
        this.skuCode = skuCode;
        this.orgId = orgId;
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

    public String getLabelNo() {
        return labelNo;
    }

    public void setLabelNo(String labelNo) {
        this.labelNo = labelNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getSignBy() {
        return signBy;
    }

    public void setSignBy(String signBy) {
        this.signBy = signBy;
    }

    public Date getSignTime() {
        return signTime;
    }

    public void setSignTime(Date signTime) {
        this.signTime = signTime;
    }

    public String getSignRemarks() {
        return signRemarks;
    }

    public void setSignRemarks(String signRemarks) {
        this.signRemarks = signRemarks;
    }

    public String getReceiptBy() {
        return receiptBy;
    }

    public void setReceiptBy(String receiptBy) {
        this.receiptBy = receiptBy;
    }

    public Date getReceiptTime() {
        return receiptTime;
    }

    public void setReceiptTime(Date receiptTime) {
        this.receiptTime = receiptTime;
    }

    public String getReceiptRemarks() {
        return receiptRemarks;
    }

    public void setReceiptRemarks(String receiptRemarks) {
        this.receiptRemarks = receiptRemarks;
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

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    public String getDef1() {
        return def1;
    }

    public void setDef1(String def1) {
        this.def1 = def1;
    }

    public String getDef2() {
        return def2;
    }

    public void setDef2(String def2) {
        this.def2 = def2;
    }

    public String getDef3() {
        return def3;
    }

    public void setDef3(String def3) {
        this.def3 = def3;
    }

    public String getDef4() {
        return def4;
    }

    public void setDef4(String def4) {
        this.def4 = def4;
    }

    public String getDef5() {
        return def5;
    }

    public void setDef5(String def5) {
        this.def5 = def5;
    }

    public String getDef6() {
        return def6;
    }

    public void setDef6(String def6) {
        this.def6 = def6;
    }

    public String getDef7() {
        return def7;
    }

    public void setDef7(String def7) {
        this.def7 = def7;
    }

    public String getDef8() {
        return def8;
    }

    public void setDef8(String def8) {
        this.def8 = def8;
    }

    public String getDef9() {
        return def9;
    }

    public void setDef9(String def9) {
        this.def9 = def9;
    }

    public String getDef10() {
        return def10;
    }

    public void setDef10(String def10) {
        this.def10 = def10;
    }

    public String getLineNo() {
        return lineNo;
    }

    public void setLineNo(String lineNo) {
        this.lineNo = lineNo;
    }
}