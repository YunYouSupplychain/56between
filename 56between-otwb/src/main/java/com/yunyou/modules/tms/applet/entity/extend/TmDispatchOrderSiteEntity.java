package com.yunyou.modules.tms.applet.entity.extend;


import java.io.Serializable;

/**
 * 派车单配送点Entity
 *
 * @author zyf
 * @version 2020-03-18
 */
public class TmDispatchOrderSiteEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String remarks;
    private String dispatchNo;        // 派车单号
    private Integer dispatchSeq;        // 配送顺序
    private String outletCode;        // 配送网点编码
    private String receiveShip;        // 提货(R)/送货(S)
    private String orgId;        // 机构ID
    private String baseOrgId;   // 基础数据机构ID

    private String outletName;        // 配送网点名称
    private String outletContact;     // 配送网点联系人
    private String outletPhone;       // 配送网点联系电话
    private String outletAddress;     // 配送网点地址
    private String outletType;

    private Double labelCount;        // 标签数
    private Double orderCount;        // 订单数

    public TmDispatchOrderSiteEntity(){
    }

    public TmDispatchOrderSiteEntity(String dispatchNo, String orgId) {
        this.dispatchNo = dispatchNo;
        this.orgId = orgId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getDispatchNo() {
        return dispatchNo;
    }

    public void setDispatchNo(String dispatchNo) {
        this.dispatchNo = dispatchNo;
    }

    public Integer getDispatchSeq() {
        return dispatchSeq;
    }

    public void setDispatchSeq(Integer dispatchSeq) {
        this.dispatchSeq = dispatchSeq;
    }

    public String getOutletCode() {
        return outletCode;
    }

    public void setOutletCode(String outletCode) {
        this.outletCode = outletCode;
    }

    public String getReceiveShip() {
        return receiveShip;
    }

    public void setReceiveShip(String receiveShip) {
        this.receiveShip = receiveShip;
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

    public String getOutletName() {
        return outletName;
    }

    public void setOutletName(String outletName) {
        this.outletName = outletName;
    }

    public String getOutletContact() {
        return outletContact;
    }

    public void setOutletContact(String outletContact) {
        this.outletContact = outletContact;
    }

    public String getOutletPhone() {
        return outletPhone;
    }

    public void setOutletPhone(String outletPhone) {
        this.outletPhone = outletPhone;
    }

    public String getOutletAddress() {
        return outletAddress;
    }

    public void setOutletAddress(String outletAddress) {
        this.outletAddress = outletAddress;
    }

    public Double getLabelCount() {
        return labelCount;
    }

    public void setLabelCount(Double labelCount) {
        this.labelCount = labelCount;
    }

    public Double getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Double orderCount) {
        this.orderCount = orderCount;
    }

    public String getOutletType() {
        return outletType;
    }

    public void setOutletType(String outletType) {
        this.outletType = outletType;
    }
}