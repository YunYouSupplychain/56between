package com.yunyou.modules.wms.outbound.entity;

import java.util.List;

/**
 * 销售entity
 * @author WMJ
 * @version 2019/02/20
 */
public class BanQinWmSaleEntity extends BanQinWmSaleHeader {
	private String ownerName;
	private String carrierName;
	private String settleTel;
	private String settleFax;
	private String settleIndustryType;
	private String settleAddress;
	private String lineName;
	private String creatorLoginName;
	private String creatorName;
	private String modifierLoginName;
	private String modifierName;
	private String auditOpName;
	private String settleName;
	// 销售单明细表信息对象集合
	private List<BanQinWmSaleDetailEntity> wmSaleDetailList;

    public List<BanQinWmSaleDetailEntity> getWmSaleDetailList() {
        return wmSaleDetailList;
    }

    public void setWmSaleDetailList(List<BanQinWmSaleDetailEntity> wmSaleDetailList) {
        this.wmSaleDetailList = wmSaleDetailList;
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

	public String getSettleIndustryType() {
		return settleIndustryType;
	}

	public void setSettleIndustryType(String settleIndustryType) {
		this.settleIndustryType = settleIndustryType;
	}

	public String getSettleAddress() {
		return settleAddress;
	}

	public void setSettleAddress(String settleAddress) {
		this.settleAddress = settleAddress;
	}

	public String getLineName() {
		return lineName;
	}

	public void setLineName(String lineName) {
		this.lineName = lineName;
	}

	public String getCreatorLoginName() {
		return creatorLoginName;
	}

	public void setCreatorLoginName(String creatorLoginName) {
		this.creatorLoginName = creatorLoginName;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public String getModifierLoginName() {
		return modifierLoginName;
	}

	public void setModifierLoginName(String modifierLoginName) {
		this.modifierLoginName = modifierLoginName;
	}

	public String getModifierName() {
		return modifierName;
	}

	public void setModifierName(String modifierName) {
		this.modifierName = modifierName;
	}

	public String getAuditOpName() {
		return auditOpName;
	}

	public void setAuditOpName(String auditOpName) {
		this.auditOpName = auditOpName;
	}

	public String getSettleName() {
		return settleName;
	}

	public void setSettleName(String settleName) {
		this.settleName = settleName;
	}

}
