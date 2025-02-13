package com.yunyou.modules.wms.basicdata.entity;

import com.yunyou.core.persistence.DataEntity;
import com.yunyou.common.utils.excel.annotation.ExcelField;

import javax.validation.constraints.NotNull;

/**
 * 循环级别Entity
 * @author WMJ
 * @version 2019-01-25
 */
public class BanQinCdWhCycle extends DataEntity<BanQinCdWhCycle> {
	
	private static final long serialVersionUID = 1L;
	private String cycleCode;		// 循环级别编码
	private String cycleName;		// 循环级别名称
	private Double cycleLife;		// 循环周期
	private String orgId;		// 分公司
	private String cycleCodeAndName; // 模糊查询字段
	
	public BanQinCdWhCycle() {
		super();
		this.recVer = 0;
	}

	public BanQinCdWhCycle(String id){
		super(id);
	}
	
	@NotNull(message="循环级别编码不能为空")
	@ExcelField(title="循环级别编码", align=2, sort=2)
	public String getCycleCode() {
		return cycleCode;
	}

	public void setCycleCode(String cycleCode) {
		this.cycleCode = cycleCode;
	}

	@NotNull(message="循环级别名称不能为空")
	@ExcelField(title="循环级别名称", align=2, sort=3)
	public String getCycleName() {
		return cycleName;
	}

	public void setCycleName(String cycleName) {
		this.cycleName = cycleName;
	}
	
	@ExcelField(title="循环周期", align=2, sort=4)
	public Double getCycleLife() {
		return cycleLife;
	}

	public void setCycleLife(Double cycleLife) {
		this.cycleLife = cycleLife;
	}

	@ExcelField(title="分公司", align=2, sort=13)
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getCycleCodeAndName() {
		return cycleCodeAndName;
	}

	public void setCycleCodeAndName(String cycleCodeAndName) {
		this.cycleCodeAndName = cycleCodeAndName;
	}
}