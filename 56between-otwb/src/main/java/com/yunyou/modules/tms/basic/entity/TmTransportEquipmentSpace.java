package com.yunyou.modules.tms.basic.entity;

import com.yunyou.core.persistence.DataEntity;
import com.yunyou.common.utils.excel.annotation.ExcelField;

/**
 * 运输设备空间信息Entity
 * @author liujianhua
 * @version 2020-02-20
 */
public class TmTransportEquipmentSpace extends DataEntity<TmTransportEquipmentSpace> {
	
	private static final long serialVersionUID = 1L;
	private String transportEquipmentTypeCode;		// 运输设备类型编码
	private String transportEquipmentNo;		// 设备编号
	private String transportEquipmentLocation;		// 设备位置
	private String isTemperatureControl;		// 是否温度控制
	private String isHumidityControl;		// 是否湿度控制
	private Double loadWeight;		// 装载重量
	private Double loadCubic;		// 装载容积
	private String orgId;		// 机构ID

	public TmTransportEquipmentSpace() {
		super();
	}

	public TmTransportEquipmentSpace(String id){
		super(id);
	}

	public TmTransportEquipmentSpace(String transportEquipmentTypeCode, String orgId) {
		this.transportEquipmentTypeCode = transportEquipmentTypeCode;
		this.orgId = orgId;
	}

	public TmTransportEquipmentSpace(String transportEquipmentTypeCode, String transportEquipmentNo, String orgId) {
		this.transportEquipmentTypeCode = transportEquipmentTypeCode;
		this.transportEquipmentNo = transportEquipmentNo;
		this.orgId = orgId;
	}

	@ExcelField(title="运输设备类型编码", align=2, sort=7)
	public String getTransportEquipmentTypeCode() {
		return transportEquipmentTypeCode;
	}

	public void setTransportEquipmentTypeCode(String transportEquipmentTypeCode) {
		this.transportEquipmentTypeCode = transportEquipmentTypeCode;
	}
	
	@ExcelField(title="设备编号", align=2, sort=8)
	public String getTransportEquipmentNo() {
		return transportEquipmentNo;
	}

	public void setTransportEquipmentNo(String transportEquipmentNo) {
		this.transportEquipmentNo = transportEquipmentNo;
	}
	
	@ExcelField(title="设备位置", align=2, sort=9)
	public String getTransportEquipmentLocation() {
		return transportEquipmentLocation;
	}

	public void setTransportEquipmentLocation(String transportEquipmentLocation) {
		this.transportEquipmentLocation = transportEquipmentLocation;
	}
	
	@ExcelField(title="是否温度控制", dictType="SYS_YES_NO", align=2, sort=10)
	public String getIsTemperatureControl() {
		return isTemperatureControl;
	}

	public void setIsTemperatureControl(String isTemperatureControl) {
		this.isTemperatureControl = isTemperatureControl;
	}
	
	@ExcelField(title="是否湿度控制", dictType="SYS_YES_NO", align=2, sort=11)
	public String getIsHumidityControl() {
		return isHumidityControl;
	}

	public void setIsHumidityControl(String isHumidityControl) {
		this.isHumidityControl = isHumidityControl;
	}
	
	@ExcelField(title="装载重量", align=2, sort=12)
	public Double getLoadWeight() {
		return loadWeight;
	}

	public void setLoadWeight(Double loadWeight) {
		this.loadWeight = loadWeight;
	}
	
	@ExcelField(title="装载容积", align=2, sort=13)
	public Double getLoadCubic() {
		return loadCubic;
	}

	public void setLoadCubic(Double loadCubic) {
		this.loadCubic = loadCubic;
	}
	
	@ExcelField(title="机构ID", align=2, sort=14)
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
}