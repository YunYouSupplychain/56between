package com.yunyou.modules.wms.outbound.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.core.persistence.DataEntity;

import java.util.Date;
/**
 * 描述：出库交接单
 *
 * @author Jianhua on 2020-2-6
 */
public class BanQinWmOutHandoverHeader extends DataEntity<BanQinWmOutHandoverHeader> {

    private static final long serialVersionUID = -5317909937818514579L;
    private String handoverNo;  // 交接单号
    private String carrierCode; // 承运商编码
    private String carrierName; // 承运商名称
    private Date handoverTime;  // 交接时间
    private String handoverOp;  // 交接人
    private String orgId;       // 机构ID
    private Date shipTimeFm;    // 发运时间从
    private Date shipTimeTo;    // 发运时间到

    public BanQinWmOutHandoverHeader() {
        super();
        this.recVer = 0;
    }

    public BanQinWmOutHandoverHeader(String id) {
        super(id);
    }

    public String getHandoverNo() {
        return handoverNo;
    }

    public void setHandoverNo(String handoverNo) {
        this.handoverNo = handoverNo;
    }

    public String getCarrierCode() {
        return carrierCode;
    }

    public void setCarrierCode(String carrierCode) {
        this.carrierCode = carrierCode;
    }

    public String getCarrierName() {
        return carrierName;
    }

    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getShipTimeFm() {
        return shipTimeFm;
    }

    public void setShipTimeFm(Date shipTimeFm) {
        this.shipTimeFm = shipTimeFm;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getShipTimeTo() {
        return shipTimeTo;
    }

    public void setShipTimeTo(Date shipTimeTo) {
        this.shipTimeTo = shipTimeTo;
    }
}
