package com.yunyou.modules.wms.weigh.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.core.persistence.DataEntity;

import java.util.Date;

/**
 * 称重中间表Entity
 * @author zyf
 * @version 2020-01-06
 */
public class BanQinWmMiddleWeigh extends DataEntity<BanQinWmMiddleWeigh> {
	
	private static final long serialVersionUID = 1L;
	private String boxNum;			// 称重条码
	private String orgId;			// 机构
	private Double tareQty;			// 皮重
	private Double weighQty;		// 重量
	private Date weighTime;			// 称重时间
	private String weighStatus;		// 称重保存处理状态
	private String weighMsg;		// 称重保存处理信息
	private String shipStatus;		// 自动发货处理状态
	private String shipMsg;			// 自动发货处理信息
	private String deviceNo;		// 称重设备号
	private String def1;			// 自定义1
	private String def2;			// 自定义2
	private String def3;			// 自定义3
	private String def4;			// 自定义4
	private String def5;			// 自定义5

	public BanQinWmMiddleWeigh() {
		super();
		this.recVer = 0;
	}

	public BanQinWmMiddleWeigh(String id){
		super(id);
	}

	@ExcelField(title="称重条码", align=2, sort=7)
	public String getBoxNum() {
		return boxNum;
	}

	public void setBoxNum(String boxNum) {
		this.boxNum = boxNum;
	}
	
	@ExcelField(title="机构", align=2, sort=8)
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
	@ExcelField(title="皮重", align=2, sort=9)
	public Double getTareQty() {
		return tareQty;
	}

	public void setTareQty(Double tareQty) {
		this.tareQty = tareQty;
	}
	
	@ExcelField(title="重量", align=2, sort=10)
	public Double getWeighQty() {
		return weighQty;
	}

	public void setWeighQty(Double weighQty) {
		this.weighQty = weighQty;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="称重时间", align=2, sort=11)
	public Date getWeighTime() {
		return weighTime;
	}

	public void setWeighTime(Date weighTime) {
		this.weighTime = weighTime;
	}
	
	@ExcelField(title="称重保存处理状态", align=2, sort=12)
	public String getWeighStatus() {
		return weighStatus;
	}

	public void setWeighStatus(String weighStatus) {
		this.weighStatus = weighStatus;
	}
	
	@ExcelField(title="称重保存处理信息", align=2, sort=13)
	public String getWeighMsg() {
		return weighMsg;
	}

	public void setWeighMsg(String weighMsg) {
		this.weighMsg = weighMsg;
	}
	
	@ExcelField(title="自动发货处理状态", align=2, sort=14)
	public String getShipStatus() {
		return shipStatus;
	}

	public void setShipStatus(String shipStatus) {
		this.shipStatus = shipStatus;
	}
	
	@ExcelField(title="自动发货处理信息", align=2, sort=15)
	public String getShipMsg() {
		return shipMsg;
	}

	public void setShipMsg(String shipMsg) {
		this.shipMsg = shipMsg;
	}

	public String getDeviceNo() {
		return deviceNo;
	}

	public void setDeviceNo(String deviceNo) {
		this.deviceNo = deviceNo;
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
}