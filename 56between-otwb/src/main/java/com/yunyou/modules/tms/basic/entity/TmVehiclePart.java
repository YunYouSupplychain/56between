package com.yunyou.modules.tms.basic.entity;

import com.yunyou.core.persistence.DataEntity;
import com.yunyou.common.utils.excel.annotation.ExcelField;

/**
 * 车辆配件Entity
 * @author liujianhua
 * @version 2020-02-20
 */
public class TmVehiclePart extends DataEntity<TmVehiclePart> {
	
	private static final long serialVersionUID = 1L;
	private String carNo;		// 车牌号
	private String partNo;		// 配件编号
	private String partName;		// 配件名称
	private Long partNumber;		// 配件数量
	private String orgId;		// 机构ID

	public TmVehiclePart() {
		super();
	}

	public TmVehiclePart(String id){
		super(id);
	}

	public TmVehiclePart(String carNo, String orgId) {
		this.carNo = carNo;
		this.orgId = orgId;
	}

	public TmVehiclePart(String carNo, String partNo, String orgId) {
		this.carNo = carNo;
		this.partNo = partNo;
		this.orgId = orgId;
	}

	@ExcelField(title="车牌号", align=2, sort=7)
	public String getCarNo() {
		return carNo;
	}

	public void setCarNo(String carNo) {
		this.carNo = carNo;
	}
	
	@ExcelField(title="配件编号", align=2, sort=8)
	public String getPartNo() {
		return partNo;
	}

	public void setPartNo(String partNo) {
		this.partNo = partNo;
	}
	
	@ExcelField(title="配件名称", align=2, sort=9)
	public String getPartName() {
		return partName;
	}

	public void setPartName(String partName) {
		this.partName = partName;
	}
	
	@ExcelField(title="配件数量", align=2, sort=10)
	public Long getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(Long partNumber) {
		this.partNumber = partNumber;
	}
	
	@ExcelField(title="机构ID", align=2, sort=11)
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
}