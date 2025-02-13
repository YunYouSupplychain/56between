package com.yunyou.modules.tms.applet.entity.request;

import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.List;

public class TmAppHandoverConfirmRequest implements Serializable {

    private String userId;
    private String isLabel;

    private String dispatchNo;
    private String transportNo;
    private String outletCode;
    private String labelNo;
    private String confirmPerson;
    private String remarks;
    private String rsFlag;
    private String orgId;
    private String operator;

    private List<TmAppHandoverConfirmSkuListRequest> skuList = Lists.newArrayList();

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDispatchNo() {
        return dispatchNo;
    }

    public void setDispatchNo(String dispatchNo) {
        this.dispatchNo = dispatchNo;
    }

    public String getOutletCode() {
        return outletCode;
    }

    public void setOutletCode(String outletCode) {
        this.outletCode = outletCode;
    }

    public String getLabelNo() {
        return labelNo;
    }

    public void setLabelNo(String labelNo) {
        this.labelNo = labelNo;
    }

    public String getConfirmPerson() {
        return confirmPerson;
    }

    public void setConfirmPerson(String confirmPerson) {
        this.confirmPerson = confirmPerson;
    }

    public String getRsFlag() {
        return rsFlag;
    }

    public void setRsFlag(String rsFlag) {
        this.rsFlag = rsFlag;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getTransportNo() {
        return transportNo;
    }

    public void setTransportNo(String transportNo) {
        this.transportNo = transportNo;
    }

    public String getIsLabel() {
        return isLabel;
    }

    public void setIsLabel(String isLabel) {
        this.isLabel = isLabel;
    }

    public List<TmAppHandoverConfirmSkuListRequest> getSkuList() {
        return skuList;
    }

    public void setSkuList(List<TmAppHandoverConfirmSkuListRequest> skuList) {
        this.skuList = skuList;
    }
}