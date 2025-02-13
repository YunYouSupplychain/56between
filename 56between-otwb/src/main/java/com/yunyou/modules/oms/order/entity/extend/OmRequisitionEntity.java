package com.yunyou.modules.oms.order.entity.extend;

import com.google.common.collect.Lists;
import com.yunyou.modules.oms.order.entity.OmRequisitionHeader;

import java.util.Date;
import java.util.List;

public class OmRequisitionEntity extends OmRequisitionHeader {
    private static final long serialVersionUID = -8216865396487939968L;
    private String orgName;
    private String ownerName;
    private String carrierName;
    private Date orderTimeFm;
    private Date orderTimeTo;
    private Date planArrivalTimeFm;
    private Date planArrivalTimeTo;
    private String fmOrgName;
    private String toOrgName;
    private String shipAreaName;
    private String consigneeAreaName;
    private List<OmRequisitionDetailEntity> omRequisitionDetailList = Lists.newArrayList();

    public OmRequisitionEntity() {
    }

    public OmRequisitionEntity(String id) {
        super(id);
    }

    public OmRequisitionEntity(String reqNo, String orgId) {
        super(reqNo, orgId);
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getCarrierName() {
        return carrierName;
    }

    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
    }

    public Date getOrderTimeFm() {
        return orderTimeFm;
    }

    public void setOrderTimeFm(Date orderTimeFm) {
        this.orderTimeFm = orderTimeFm;
    }

    public Date getOrderTimeTo() {
        return orderTimeTo;
    }

    public void setOrderTimeTo(Date orderTimeTo) {
        this.orderTimeTo = orderTimeTo;
    }

    public Date getPlanArrivalTimeFm() {
        return planArrivalTimeFm;
    }

    public void setPlanArrivalTimeFm(Date planArrivalTimeFm) {
        this.planArrivalTimeFm = planArrivalTimeFm;
    }

    public Date getPlanArrivalTimeTo() {
        return planArrivalTimeTo;
    }

    public void setPlanArrivalTimeTo(Date planArrivalTimeTo) {
        this.planArrivalTimeTo = planArrivalTimeTo;
    }

    public String getFmOrgName() {
        return fmOrgName;
    }

    public void setFmOrgName(String fmOrgName) {
        this.fmOrgName = fmOrgName;
    }

    public String getToOrgName() {
        return toOrgName;
    }

    public void setToOrgName(String toOrgName) {
        this.toOrgName = toOrgName;
    }

    public String getShipAreaName() {
        return shipAreaName;
    }

    public void setShipAreaName(String shipAreaName) {
        this.shipAreaName = shipAreaName;
    }

    public String getConsigneeAreaName() {
        return consigneeAreaName;
    }

    public void setConsigneeAreaName(String consigneeAreaName) {
        this.consigneeAreaName = consigneeAreaName;
    }

    public List<OmRequisitionDetailEntity> getOmRequisitionDetailList() {
        return omRequisitionDetailList;
    }

    public void setOmRequisitionDetailList(List<OmRequisitionDetailEntity> omRequisitionDetailList) {
        this.omRequisitionDetailList = omRequisitionDetailList;
    }
}
