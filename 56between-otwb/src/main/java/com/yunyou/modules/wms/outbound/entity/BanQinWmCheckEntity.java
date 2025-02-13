package com.yunyou.modules.wms.outbound.entity;

import java.util.List;

/**
 * 复核entity
 * @author WMJ
 * @version 2019/02/25
 */
public class BanQinWmCheckEntity {
	// 出库单快递单号
	private String soTrackingNo;
	// 复核查询结果列表
	private List<BanQinWmSoAllocEntity> checkItems;
	//
	private String soNo;
	//
	private String ownerCode;
	//
	private String skuCode;
	//
	private String serialNo;
	//
	private List<String> lotNums;

	private List<String> allocIds;

	private String orgId;

	private List<BanQinWmSoSerialEntity> soSerialList;

	private List<String> noList;

	private String processByCode;

	public String getSoTrackingNo() {
		return soTrackingNo;
	}

	public void setSoTrackingNo(String soTrackingNo) {
		this.soTrackingNo = soTrackingNo;
	}

    public List<BanQinWmSoAllocEntity> getCheckItems() {
        return checkItems;
    }

    public void setCheckItems(List<BanQinWmSoAllocEntity> checkItems) {
        this.checkItems = checkItems;
    }

	public String getSoNo() {
		return soNo;
	}

	public void setSoNo(String soNo) {
		this.soNo = soNo;
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

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public List<String> getLotNums() {
		return lotNums;
	}

	public void setLotNums(List<String> lotNums) {
		this.lotNums = lotNums;
	}

	public List<String> getAllocIds() {
		return allocIds;
	}

	public void setAllocIds(List<String> allocIds) {
		this.allocIds = allocIds;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public List<BanQinWmSoSerialEntity> getSoSerialList() {
		return soSerialList;
	}

	public void setSoSerialList(List<BanQinWmSoSerialEntity> soSerialList) {
		this.soSerialList = soSerialList;
	}

	public List<String> getNoList() {
		return noList;
	}

	public void setNoList(List<String> noList) {
		this.noList = noList;
	}

	public String getProcessByCode() {
		return processByCode;
	}

	public void setProcessByCode(String processByCode) {
		this.processByCode = processByCode;
	}
}