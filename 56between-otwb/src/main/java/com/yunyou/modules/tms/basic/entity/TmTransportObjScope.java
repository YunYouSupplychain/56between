package com.yunyou.modules.tms.basic.entity;

import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.core.persistence.DataEntity;

/**
 * 业务对象服务范围Entity
 * @author liujianhua
 * @version 2020-02-20
 */
public class TmTransportObjScope extends DataEntity<TmTransportObjScope> {
	
	private static final long serialVersionUID = 1L;
	private String transportScopeCode;		// 业务服务范围编码
	private String transportObjCode;		// 业务对象编码
	private String transportScopeType;		// 服务范围类型
	private Double maxLoadWeight;		// 最大装载重量
	private Double maxLoadCubic;		// 最大装载体积
	private Double maxAmount;		// 最大金额
	private String orgId;		// 机构ID

	public TmTransportObjScope() {
		super();
	}

	public TmTransportObjScope(String id){
		super(id);
	}

	public TmTransportObjScope(String transportScopeCode, String transportObjCode, String transportScopeType, String orgId) {
		this.transportScopeCode = transportScopeCode;
		this.transportObjCode = transportObjCode;
		this.transportScopeType = transportScopeType;
		this.orgId = orgId;
	}

	@ExcelField(title="业务服务范围编码", align=2, sort=7)
	public String getTransportScopeCode() {
		return transportScopeCode;
	}

	public void setTransportScopeCode(String transportScopeCode) {
		this.transportScopeCode = transportScopeCode;
	}
	
	@ExcelField(title="业务对象编码", align=2, sort=8)
	public String getTransportObjCode() {
		return transportObjCode;
	}

	public void setTransportObjCode(String transportObjCode) {
		this.transportObjCode = transportObjCode;
	}

	@ExcelField(title="最大装载重量", align=2, sort=9)
	public Double getMaxLoadWeight() {
		return maxLoadWeight;
	}

	public void setMaxLoadWeight(Double maxLoadWeight) {
		this.maxLoadWeight = maxLoadWeight;
	}
	
	@ExcelField(title="最大装载体积", align=2, sort=10)
	public Double getMaxLoadCubic() {
		return maxLoadCubic;
	}

	public void setMaxLoadCubic(Double maxLoadCubic) {
		this.maxLoadCubic = maxLoadCubic;
	}
	
	@ExcelField(title="最大金额", align=2, sort=11)
	public Double getMaxAmount() {
		return maxAmount;
	}

	public void setMaxAmount(Double maxAmount) {
		this.maxAmount = maxAmount;
	}
	
	@ExcelField(title="机构ID", align=2, sort=12)
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	@ExcelField(title="服务范围类型", dictType = "TMS_TRANSPORT_SCOPE_TYPE", align=2, sort=13)
	public String getTransportScopeType() {
		return transportScopeType;
	}

	public void setTransportScopeType(String transportScopeType) {
		this.transportScopeType = transportScopeType;
	}
}