package com.yunyou.modules.tms.basic.entity;

import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.core.persistence.DataEntity;

/**
 * 承运商路由信息Entity
 * @author liujianhua
 * @version 2020-02-20
 */
public class TmCarrierRouteRelation extends DataEntity<TmCarrierRouteRelation> {
	
	private static final long serialVersionUID = 1L;
	private String code;		// 编码
	private String name;		// 名称
	private String originId;		// 起始地ID
	private String destinationId;	// 目的地ID
	private String carrierCode;		// 承运商编码
	private Double mileage;		// 标准里程(km)
	private Double time;		// 标准时效(h)
	private String orgId;		// 机构ID

	public TmCarrierRouteRelation() {
		super();
	}

	public TmCarrierRouteRelation(String id){
		super(id);
	}

	public TmCarrierRouteRelation(String code, String orgId) {
		this.code = code;
		this.orgId = orgId;
	}

	public TmCarrierRouteRelation(String carrierCode, String originId, String destinationId, String orgId) {
		this.carrierCode = carrierCode;
		this.originId = originId;
		this.destinationId = destinationId;
		this.orgId = orgId;
	}

	@ExcelField(title="编码", align=2, sort=7)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@ExcelField(title="名称", align=2, sort=8)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ExcelField(title="承运商编码", align=2, sort=9)
	public String getCarrierCode() {
		return carrierCode;
	}

	public void setCarrierCode(String carrierCode) {
		this.carrierCode = carrierCode;
	}

	@ExcelField(title="起始地ID", align=2, sort=10)
	public String getOriginId() {
		return originId;
	}

	public void setOriginId(String originId) {
		this.originId = originId;
	}
	
	@ExcelField(title="目的地ID", align=2, sort=11)
	public String getDestinationId() {
		return destinationId;
	}

	public void setDestinationId(String destinationId) {
		this.destinationId = destinationId;
	}
	
	@ExcelField(title="标准里程(km)", align=2, sort=12)
	public Double getMileage() {
		return mileage;
	}

	public void setMileage(Double mileage) {
		this.mileage = mileage;
	}
	
	@ExcelField(title="标准时效(h)", align=2, sort=13)
	public Double getTime() {
		return time;
	}

	public void setTime(Double time) {
		this.time = time;
	}
	
	@ExcelField(title="机构ID", align=2, sort=14)
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
}