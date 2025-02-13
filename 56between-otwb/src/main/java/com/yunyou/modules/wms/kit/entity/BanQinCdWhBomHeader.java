package com.yunyou.modules.wms.kit.entity;

import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.core.persistence.DataEntity;

import java.util.Date;

/**
 * 组合件Entity
 * @author Jianhua Liu
 * @version 2019-08-19
 */
public class BanQinCdWhBomHeader extends DataEntity<BanQinCdWhBomHeader> {
	
	private static final long serialVersionUID = 1L;
	private String ownerCode;		// 货主编码
	private String parentSkuCode;		// 父件编码
	private String kitType;		// 加工类型
	private String orgId;		// 机构ID
	private Date beginCreateDate;		// 开始 创建时间
	private Date endCreateDate;		// 结束 创建时间
	private Date beginUpdateDate;		// 开始 更新时间
	private Date endUpdateDate;		// 结束 更新时间
	
	public BanQinCdWhBomHeader() {
		super();
		this.recVer = 0;
	}

	public BanQinCdWhBomHeader(String id){
		super(id);
	}

	@ExcelField(title="货主编码", align=2, sort=2)
	public String getOwnerCode() {
		return ownerCode;
	}

	public void setOwnerCode(String ownerCode) {
		this.ownerCode = ownerCode;
	}
	
	@ExcelField(title="父件编码", align=2, sort=3)
	public String getParentSkuCode() {
		return parentSkuCode;
	}

	public void setParentSkuCode(String parentSkuCode) {
		this.parentSkuCode = parentSkuCode;
	}
	
	@ExcelField(title="加工类型", dictType="WMS_KIT_TYPE", align=2, sort=4)
	public String getKitType() {
		return kitType;
	}

	public void setKitType(String kitType) {
		this.kitType = kitType;
	}

	@ExcelField(title="机构ID", align=2, sort=13)
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
	public Date getBeginCreateDate() {
		return beginCreateDate;
	}

	public void setBeginCreateDate(Date beginCreateDate) {
		this.beginCreateDate = beginCreateDate;
	}
	
	public Date getEndCreateDate() {
		return endCreateDate;
	}

	public void setEndCreateDate(Date endCreateDate) {
		this.endCreateDate = endCreateDate;
	}
		
	public Date getBeginUpdateDate() {
		return beginUpdateDate;
	}

	public void setBeginUpdateDate(Date beginUpdateDate) {
		this.beginUpdateDate = beginUpdateDate;
	}
	
	public Date getEndUpdateDate() {
		return endUpdateDate;
	}

	public void setEndUpdateDate(Date endUpdateDate) {
		this.endUpdateDate = endUpdateDate;
	}
		
}