package com.yunyou.modules.wms.basicdata.entity;

import com.yunyou.core.persistence.DataEntity;
import com.yunyou.common.utils.excel.annotation.ExcelField;

import javax.validation.constraints.NotNull;

/**
 * 区域Entity
 * @author WMJ
 * @version 2019-01-25
 */
public class BanQinCdWhArea extends DataEntity<BanQinCdWhArea> {
	
	private static final long serialVersionUID = 1L;
	private String areaCode;		// 区域编码
	private String areaName;		// 区域名称
	private String def1;		// 自定义1
	private String def2;		// 自定义2
	private String def3;		// 自定义3
	private String def4;		// 自定义4
	private String def5;		// 自定义5
	private String orgId;		// 分公司
	private String areaCodeAndName; // 模糊查询字段
	
	public BanQinCdWhArea() {
		super();
		this.recVer = 0;
	}

	public BanQinCdWhArea(String id){
		super(id);
	}

	@NotNull(message="区域编码不能为空")
	@ExcelField(title="区域编码", align=2, sort=2)
	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	@NotNull(message="区域名称不能为空")
	@ExcelField(title="区域名称", align=2, sort=3)
	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	
	@ExcelField(title="自定义1", align=2, sort=5)
	public String getDef1() {
		return def1;
	}

	public void setDef1(String def1) {
		this.def1 = def1;
	}
	
	@ExcelField(title="自定义2", align=2, sort=6)
	public String getDef2() {
		return def2;
	}

	public void setDef2(String def2) {
		this.def2 = def2;
	}
	
	@ExcelField(title="自定义3", align=2, sort=7)
	public String getDef3() {
		return def3;
	}

	public void setDef3(String def3) {
		this.def3 = def3;
	}
	
	@ExcelField(title="自定义4", align=2, sort=8)
	public String getDef4() {
		return def4;
	}

	public void setDef4(String def4) {
		this.def4 = def4;
	}
	
	@ExcelField(title="自定义5", align=2, sort=9)
	public String getDef5() {
		return def5;
	}

	public void setDef5(String def5) {
		this.def5 = def5;
	}

	@ExcelField(title="分公司", align=2, sort=17)
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getAreaCodeAndName() {
		return areaCodeAndName;
	}

	public void setAreaCodeAndName(String areaCodeAndName) {
		this.areaCodeAndName = areaCodeAndName;
	}
}