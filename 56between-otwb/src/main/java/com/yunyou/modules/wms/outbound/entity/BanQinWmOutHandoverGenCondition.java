package com.yunyou.modules.wms.outbound.entity;

import java.io.Serializable;
import java.util.Date;

public class BanQinWmOutHandoverGenCondition implements Serializable {
    private static final long serialVersionUID = -4737928684411402819L;

    private String carrierCode; // 承运商编码
    private Date shipTimeFm;    // 发货时间从
    private Date shipTimeTo;    // 发货时间到
    private String orgId;       // 机构ID
    private Date handoverTime;  // 交接时间
    private String handoverOp;  // 交接人

    public String getCarrierCode() {
        return carrierCode;
    }

    public void setCarrierCode(String carrierCode) {
        this.carrierCode = carrierCode;
    }

    public Date getShipTimeFm() {
        return shipTimeFm;
    }

    public void setShipTimeFm(Date shipTimeFm) {
        this.shipTimeFm = shipTimeFm;
    }

    public Date getShipTimeTo() {
        return shipTimeTo;
    }

    public void setShipTimeTo(Date shipTimeTo) {
        this.shipTimeTo = shipTimeTo;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public Date getHandoverTime() {
        return handoverTime;
    }

    public void setHandoverTime(Date handoverTime) {
        this.handoverTime = handoverTime;
    }

    public String getHandoverOp() {
        return handoverOp;
    }

    public void setHandoverOp(String handoverOp) {
        this.handoverOp = handoverOp;
    }
}
