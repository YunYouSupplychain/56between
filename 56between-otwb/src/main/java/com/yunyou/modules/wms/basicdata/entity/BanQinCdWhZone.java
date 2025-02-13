package com.yunyou.modules.wms.basicdata.entity;

import com.yunyou.core.persistence.DataEntity;
import com.yunyou.common.utils.excel.annotation.ExcelField;

import javax.validation.constraints.NotNull;

/**
 * 库区Entity
 * @author WMJ
 * @version 2019-01-25
 */
public class BanQinCdWhZone extends DataEntity<BanQinCdWhZone> {
	
	private static final long serialVersionUID = 1L;
	@ExcelField(title = "库区编码**必填")
	private String zoneCode;
	@ExcelField(title = "库区名称**必填")
	private String zoneName;
	@ExcelField(title = "库区类型**必填 填写\n平面仓库区\n货架仓库区\n高架仓库区\n重力式货架库区\n堆垛机货架库区", dictType = "SYS_WM_ZONE_TYPE")
	private String type;
	@ExcelField(title = "区域编码**必填")
	private String areaCode;
	@ExcelField(title = "区域名称", type = 1)
	private String areaName;
	private String def1;		// 自定义1
	private String def2;		// 自定义2
	private String def3;		// 自定义3
	private String def4;		// 自定义4
	private String def5;		// 自定义5
	private String orgId;		// 分公司

	public BanQinCdWhZone() {
		super();
		this.recVer = 0;
	}

	public BanQinCdWhZone(String id){
		super(id);
	}

	@NotNull(message="库区编码不能为空")
	public String getZoneCode() {
		return zoneCode;
	}

	public void setZoneCode(String zoneCode) {
		this.zoneCode = zoneCode;
	}

	@NotNull(message="库区名称不能为空")
	public String getZoneName() {
		return zoneName;
	}

	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@NotNull(message="区域编码不能为空")
	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

	public String getDef1() {
		return def1;
	}

	public void setDef1(String def1) {
		this.def1 = def1;
	}
	
	public String getDef2() {
		return def2;
	}

	public void setDef2(String def2) {
		this.def2 = def2;
	}
	
	public String getDef3() {
		return def3;
	}

	public void setDef3(String def3) {
		this.def3 = def3;
	}
	
	public String getDef4() {
		return def4;
	}

	public void setDef4(String def4) {
		this.def4 = def4;
	}
	
	public String getDef5() {
		return def5;
	}

	public void setDef5(String def5) {
		this.def5 = def5;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

}