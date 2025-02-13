package com.yunyou.modules.wms.inventory.entity;

import com.yunyou.core.persistence.DataEntity;
import com.yunyou.common.utils.excel.annotation.ExcelField;

/**
 * 库存操作悲观锁记录表Entity
 * @author WMJ
 * @version 2019-01-25
 */
public class BanQinWmInvLock extends DataEntity<BanQinWmInvLock> {
	
	private static final long serialVersionUID = 1L;
	private String lotNum;		// 批次号
	private String orgId;		// 分公司
	
	public BanQinWmInvLock() {
		super();
		this.recVer = 0;
	}

	public BanQinWmInvLock(String id){
		super(id);
	}

	@ExcelField(title="批次号", align=2, sort=2)
	public String getLotNum() {
		return lotNum;
	}

	public void setLotNum(String lotNum) {
		this.lotNum = lotNum;
	}

	@ExcelField(title="分公司", align=2, sort=10)
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
}