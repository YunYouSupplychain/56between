package com.yunyou.modules.tms.applet.entity.request;

import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.List;

public class TmAppSignRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private String dispatchNo;  // 派车单号
    private String dispatchSiteOutletCode; // 配送网点编码
    private String transportNo; // 运输订单号
    private String orgId;       // 机构ID
    private String baseOrgId;   // 基础数据机构ID
    private String receiveShip; // 提货(R) or 送货(S)
    private String signPerson;      // 签收人
    private String signRemarks;     // 签收备注

    private List<TmAppSignDetialRequest> detailList = Lists.newArrayList();

    public String getDispatchNo() {
        return dispatchNo;
    }

    public void setDispatchNo(String dispatchNo) {
        this.dispatchNo = dispatchNo;
    }

    public String getTransportNo() {
        return transportNo;
    }

    public void setTransportNo(String transportNo) {
        this.transportNo = transportNo;
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

    public String getDispatchSiteOutletCode() {
        return dispatchSiteOutletCode;
    }

    public void setDispatchSiteOutletCode(String dispatchSiteOutletCode) {
        this.dispatchSiteOutletCode = dispatchSiteOutletCode;
    }

    public String getSignPerson() {
        return signPerson;
    }

    public void setSignPerson(String signPerson) {
        this.signPerson = signPerson;
    }

    public String getSignRemarks() {
        return signRemarks;
    }

    public void setSignRemarks(String signRemarks) {
        this.signRemarks = signRemarks;
    }

    public List<TmAppSignDetialRequest> getDetailList() {
        return detailList;
    }

    public void setDetailList(List<TmAppSignDetialRequest> detailList) {
        this.detailList = detailList;
    }
}
