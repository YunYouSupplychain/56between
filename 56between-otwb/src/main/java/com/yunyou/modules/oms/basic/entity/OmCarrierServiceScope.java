package com.yunyou.modules.oms.basic.entity;

import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.core.persistence.DataEntity;

/**
 * 承运商服务范围Entity
 * @author Jianhua Liu
 * @version 2019-10-23
 */
public class OmCarrierServiceScope extends DataEntity<OmCarrierServiceScope> {
	
	private static final long serialVersionUID = 1L;
	private String carrierCode;	// 承运商编码
	private String groupCode;	// 业务服务范围编码
	private Double maxWeight;	// 最大重量
	private Double maxVolume;	// 最大体积
	private Double maxCost;		// 最大费用
	private String orgId;		// 机构ID
    private String ownerCode;   // 货主编码
	
	public OmCarrierServiceScope() {
		super();
	}

	public OmCarrierServiceScope(String id){
		super(id);
	}

	@ExcelField(title="承运商编码", align=2, sort=7)
	public String getCarrierCode() {
		return carrierCode;
	}

	public void setCarrierCode(String carrierCode) {
		this.carrierCode = carrierCode;
	}
	
	@ExcelField(title="业务服务范围编码", align=2, sort=8)
	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}
	
	@ExcelField(title="最大重量", align=2, sort=9)
	public Double getMaxWeight() {
		return maxWeight;
	}

	public void setMaxWeight(Double maxWeight) {
		this.maxWeight = maxWeight;
	}
	
	@ExcelField(title="最大体积", align=2, sort=10)
	public Double getMaxVolume() {
		return maxVolume;
	}

	public void setMaxVolume(Double maxVolume) {
		this.maxVolume = maxVolume;
	}
	
	@ExcelField(title="最大费用", align=2, sort=11)
	public Double getMaxCost() {
		return maxCost;
	}

	public void setMaxCost(Double maxCost) {
		this.maxCost = maxCost;
	}
	
	@ExcelField(title="机构ID", align=2, sort=12)
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

    public String getOwnerCode() {
        return ownerCode;
    }

    public void setOwnerCode(String ownerCode) {
        this.ownerCode = ownerCode;
    }
}