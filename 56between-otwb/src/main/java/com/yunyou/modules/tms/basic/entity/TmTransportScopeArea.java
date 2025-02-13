package com.yunyou.modules.tms.basic.entity;

import com.yunyou.common.utils.excel.annotation.ExcelField;

import java.io.Serializable;

/**
 * 业务服务范围区域Entity
 * @author liujianhua
 * @version 2020-02-20
 */
public class TmTransportScopeArea implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String headId;		// 业务服务范围ID
	private String areaId;		// 区域ID

	public TmTransportScopeArea() {
		super();
	}

	public TmTransportScopeArea(String headId, String areaId) {
		this.headId = headId;
		this.areaId = areaId;
	}

	@ExcelField(title="业务服务范围ID", align=2, sort=7)
	public String getHeadId() {
		return headId;
	}

	public void setHeadId(String headId) {
		this.headId = headId;
	}
	
	@ExcelField(title="区域ID", align=2, sort=8)
	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
}