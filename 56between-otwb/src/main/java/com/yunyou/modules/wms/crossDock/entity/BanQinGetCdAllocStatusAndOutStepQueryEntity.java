package com.yunyou.modules.wms.crossDock.entity;

/**
 * @author WMJ
 * @version 2020-02-14
 */
public class BanQinGetCdAllocStatusAndOutStepQueryEntity {
    private String asnNo;
    private String rcvLineNo;
    private String orgId;
    private String status;
    private String cdOutStep;

    public String getAsnNo() {
        return asnNo;
    }

    public void setAsnNo(String asnNo) {
        this.asnNo = asnNo;
    }

    public String getRcvLineNo() {
        return rcvLineNo;
    }

    public void setRcvLineNo(String rcvLineNo) {
        this.rcvLineNo = rcvLineNo;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCdOutStep() {
        return cdOutStep;
    }

    public void setCdOutStep(String cdOutStep) {
        this.cdOutStep = cdOutStep;
    }
}
