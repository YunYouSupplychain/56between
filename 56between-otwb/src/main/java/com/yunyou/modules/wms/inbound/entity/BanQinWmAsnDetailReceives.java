package com.yunyou.modules.wms.inbound.entity;

import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.core.persistence.DataEntity;

/**
 * 收货箱明细Entity
 * @author WMJ
 * @version 2019-01-25
 */
public class BanQinWmAsnDetailReceives extends DataEntity<BanQinWmAsnDetailReceives> {
	
	private static final long serialVersionUID = 1L;
	private String headId;		// 入库单ID
	private String receiveId;		// 收货明细ID
	private String cartonNo;		// 箱号
	private String orgId;		// 分公司
	
	public BanQinWmAsnDetailReceives() {
		super();
		this.recVer = 0;
	}

	public BanQinWmAsnDetailReceives(String id){
		super(id);
	}

	@ExcelField(title="入库单ID", align=2, sort=1)
	public String getHeadId() {
		return headId;
	}

	public void setHeadId(String headId) {
		this.headId = headId;
	}
	
	@ExcelField(title="收货明细ID", align=2, sort=2)
	public String getReceiveId() {
		return receiveId;
	}

	public void setReceiveId(String receiveId) {
		this.receiveId = receiveId;
	}
	
	@ExcelField(title="箱号", align=2, sort=3)
	public String getCartonNo() {
		return cartonNo;
	}

	public void setCartonNo(String cartonNo) {
		this.cartonNo = cartonNo;
	}

	@ExcelField(title="分公司", align=2, sort=12)
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
}