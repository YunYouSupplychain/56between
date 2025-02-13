package com.yunyou.modules.wms.inbound.entity;

import java.util.Date;
import java.util.List;

/**
 * 入库单扩展Entity
 *
 * @author WMJ
 * @version 2019-01-25
 */
public class BanQinWmAsnEntity extends BanQinWmAsnHeader {

    private static final long serialVersionUID = 1L;
    // 货主名称
    private String ownerName;
    // 供应商名称
    private String supplierName;
    // 供应商电话
    private String supplierTel;
    // 供应商传真
    private String supplierFax;
    // 供应商地址
    private String supplierAddress;
    // 供应商行业类型
    private String supplierIndustryType;

    // 承运商名称
    private String carrierName;
    // 承运商电话
    private String carrierTel;
    // 承运商传真
    private String carrierFax;
    // 承运商地址
    private String carrierAddress;
    // 承运商行业类型
    private String carrierIndustryType;

    // 结算人
    private String settleName;
    // 结算人电话
    private String settleTel;
    // 结算人传真
    private String settleFax;
    // 结算人地址
    private String settleAddress;
    // 结算人行业类型
    private String settleIndustryType;

    private String auditName;
    private String holdName;
    // 创建人
    private String creatorLoginName;
    private String creatorUserName;
    // 修改人
    private String modifierLoginName;
    private String modifierUserName;
    private String orgName;

    private Date beginOrderTime;		// 开始 订单时间
    private Date endOrderTime;		// 结束 订单时间

    // 预收货通知单明细
    private List<BanQinWmAsnDetailEntity> wmAsnDetailEntities;
    // 收货明细
    private List<BanQinWmAsnDetailReceiveEntity> wmAsnDetailReceiveEntities;

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getSupplierTel() {
        return supplierTel;
    }

    public void setSupplierTel(String supplierTel) {
        this.supplierTel = supplierTel;
    }

    public String getSupplierFax() {
        return supplierFax;
    }

    public void setSupplierFax(String supplierFax) {
        this.supplierFax = supplierFax;
    }

    public String getSupplierAddress() {
        return supplierAddress;
    }

    public void setSupplierAddress(String supplierAddress) {
        this.supplierAddress = supplierAddress;
    }

    public String getSupplierIndustryType() {
        return supplierIndustryType;
    }

    public void setSupplierIndustryType(String supplierIndustryType) {
        this.supplierIndustryType = supplierIndustryType;
    }

    public String getCarrierName() {
        return carrierName;
    }

    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
    }

    public String getCarrierTel() {
        return carrierTel;
    }

    public void setCarrierTel(String carrierTel) {
        this.carrierTel = carrierTel;
    }

    public String getCarrierFax() {
        return carrierFax;
    }

    public void setCarrierFax(String carrierFax) {
        this.carrierFax = carrierFax;
    }

    public String getCarrierAddress() {
        return carrierAddress;
    }

    public void setCarrierAddress(String carrierAddress) {
        this.carrierAddress = carrierAddress;
    }

    public String getCarrierIndustryType() {
        return carrierIndustryType;
    }

    public void setCarrierIndustryType(String carrierIndustryType) {
        this.carrierIndustryType = carrierIndustryType;
    }

    public String getSettleName() {
        return settleName;
    }

    public void setSettleName(String settleName) {
        this.settleName = settleName;
    }

    public String getSettleTel() {
        return settleTel;
    }

    public void setSettleTel(String settleTel) {
        this.settleTel = settleTel;
    }

    public String getSettleFax() {
        return settleFax;
    }

    public void setSettleFax(String settleFax) {
        this.settleFax = settleFax;
    }

    public String getSettleAddress() {
        return settleAddress;
    }

    public void setSettleAddress(String settleAddress) {
        this.settleAddress = settleAddress;
    }

    public String getSettleIndustryType() {
        return settleIndustryType;
    }

    public void setSettleIndustryType(String settleIndustryType) {
        this.settleIndustryType = settleIndustryType;
    }

    public String getAuditName() {
        return auditName;
    }

    public void setAuditName(String auditName) {
        this.auditName = auditName;
    }

    public String getHoldName() {
        return holdName;
    }

    public void setHoldName(String holdName) {
        this.holdName = holdName;
    }

    public String getCreatorLoginName() {
        return creatorLoginName;
    }

    public void setCreatorLoginName(String creatorLoginName) {
        this.creatorLoginName = creatorLoginName;
    }

    public String getCreatorUserName() {
        return creatorUserName;
    }

    public void setCreatorUserName(String creatorUserName) {
        this.creatorUserName = creatorUserName;
    }

    public String getModifierLoginName() {
        return modifierLoginName;
    }

    public void setModifierLoginName(String modifierLoginName) {
        this.modifierLoginName = modifierLoginName;
    }

    public String getModifierUserName() {
        return modifierUserName;
    }

    public void setModifierUserName(String modifierUserName) {
        this.modifierUserName = modifierUserName;
    }

    public List<BanQinWmAsnDetailEntity> getWmAsnDetailEntities() {
        return wmAsnDetailEntities;
    }

    public void setWmAsnDetailEntities(List<BanQinWmAsnDetailEntity> wmAsnDetailEntities) {
        this.wmAsnDetailEntities = wmAsnDetailEntities;
    }

    public List<BanQinWmAsnDetailReceiveEntity> getWmAsnDetailReceiveEntities() {
        return wmAsnDetailReceiveEntities;
    }

    public void setWmAsnDetailReceiveEntities(List<BanQinWmAsnDetailReceiveEntity> wmAsnDetailReceiveEntities) {
        this.wmAsnDetailReceiveEntities = wmAsnDetailReceiveEntities;
    }

    public Date getBeginOrderTime() {
        return beginOrderTime;
    }

    public void setBeginOrderTime(Date beginOrderTime) {
        this.beginOrderTime = beginOrderTime;
    }

    public Date getEndOrderTime() {
        return endOrderTime;
    }

    public void setEndOrderTime(Date endOrderTime) {
        this.endOrderTime = endOrderTime;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }
}