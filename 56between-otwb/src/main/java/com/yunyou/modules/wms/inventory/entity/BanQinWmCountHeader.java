package com.yunyou.modules.wms.inventory.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.core.persistence.DataEntity;

import java.util.Date;

/**
 * 库存盘点Entity
 * @author WMJ
 * @version 2019-01-28
 */
public class BanQinWmCountHeader extends DataEntity<BanQinWmCountHeader> {
	
	private static final long serialVersionUID = 1L;
	private String countNo;		// 盘点单号
	private String status;		// 状态
	private String countType;		// 盘点单类型（普通盘点、差异盘点）
	private String countRange;		// 盘点范围（全盘、抽盘、动碰盘点、循环盘点）
	private String countMethod;		// 盘点方法（静态盘点、动态盘点）
	private String countMode;		// 盘点方式（明盘、盲盘）
	private String ownerCode;		// 货主编码
	private String ownerName;		// 货主名称
	private String skuCode;		// 商品编码
	private String skuName;		// 商品名称
	private String lotNum;		// 批次号
	private String areaCode;		// 区域编码
	private String areaName;		// 区域名称
	private String zoneCode;		// 库区编码
	private String zoneName;		// 库区名称
	private String fmLoc;		// 源库位
	private String toLoc;		// 目标库位
	private String traceId;		// 跟踪号
	private Date takeStartTime;		// 动盘开始时间
	private Date takeEndTime;		// 动盘结束时间
	private Double randomNum;		// 抽盘数量
	private Double randomRate;		// 抽盘比例
	private String parentCountNo;		// 上次盘点单号
	private Date closeTime;		// 盘点关闭时间
	private String isCreateCheck;		// 是否产生复盘单
	private String isSerial;		// 是否序列号管理
	private String monitorOp;		// 监盘人
	private Date lotAtt01;		// 批次属性01(生产日期)
	private Date lotAtt02;		// 批次属性02(失效日期)
	private Date lotAtt03;		// 批次属性03(入库日期)
	private String lotAtt04;		// 批次属性04(品质)
	private String lotAtt05;		// 批次属性05
	private String lotAtt06;		// 批次属性06
	private String lotAtt07;		// 批次属性07
	private String lotAtt08;		// 批次属性08
	private String lotAtt09;		// 批次属性09
	private String lotAtt10;		// 批次属性10
	private String lotAtt11;		// 批次属性11
	private String lotAtt12;		// 批次属性12
	private String def1;		// 自定义1
	private String def2;		// 自定义2
	private String def3;		// 自定义3
	private String def4;		// 自定义4
	private String def5;		// 自定义5
	private String orgId;		// 分公司
	
	public BanQinWmCountHeader() {
		super();
		this.recVer = 0;
	}

	public BanQinWmCountHeader(String id){
		super(id);
	}

	@ExcelField(title="盘点单号", align=2, sort=2)
	public String getCountNo() {
		return countNo;
	}

	public void setCountNo(String countNo) {
		this.countNo = countNo;
	}
	
	@ExcelField(title="状态", align=2, sort=3)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@ExcelField(title="盘点单类型（普通盘点、差异盘点）", align=2, sort=4)
	public String getCountType() {
		return countType;
	}

	public void setCountType(String countType) {
		this.countType = countType;
	}
	
	@ExcelField(title="盘点范围（全盘、抽盘、动碰盘点、循环盘点）", align=2, sort=5)
	public String getCountRange() {
		return countRange;
	}

	public void setCountRange(String countRange) {
		this.countRange = countRange;
	}
	
	@ExcelField(title="盘点方法（静态盘点、动态盘点）", align=2, sort=6)
	public String getCountMethod() {
		return countMethod;
	}

	public void setCountMethod(String countMethod) {
		this.countMethod = countMethod;
	}
	
	@ExcelField(title="盘点方式（明盘、盲盘）", align=2, sort=7)
	public String getCountMode() {
		return countMode;
	}

	public void setCountMode(String countMode) {
		this.countMode = countMode;
	}
	
	@ExcelField(title="货主编码", align=2, sort=8)
	public String getOwnerCode() {
		return ownerCode;
	}

	public void setOwnerCode(String ownerCode) {
		this.ownerCode = ownerCode;
	}
	
	@ExcelField(title="商品编码", align=2, sort=10)
	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}
	
	@ExcelField(title="批次号", align=2, sort=12)
	public String getLotNum() {
		return lotNum;
	}

	public void setLotNum(String lotNum) {
		this.lotNum = lotNum;
	}
	
	@ExcelField(title="区域编码", align=2, sort=13)
	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	
	@ExcelField(title="库区编码", align=2, sort=15)
	public String getZoneCode() {
		return zoneCode;
	}

	public void setZoneCode(String zoneCode) {
		this.zoneCode = zoneCode;
	}
	
	@ExcelField(title="源库位", align=2, sort=17)
	public String getFmLoc() {
		return fmLoc;
	}

	public void setFmLoc(String fmLoc) {
		this.fmLoc = fmLoc;
	}
	
	@ExcelField(title="目标库位", align=2, sort=18)
	public String getToLoc() {
		return toLoc;
	}

	public void setToLoc(String toLoc) {
		this.toLoc = toLoc;
	}
	
	@ExcelField(title="跟踪号", align=2, sort=19)
	public String getTraceId() {
		return traceId;
	}

	public void setTraceId(String traceId) {
		this.traceId = traceId;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="动盘开始时间", align=2, sort=20)
	public Date getTakeStartTime() {
		return takeStartTime;
	}

	public void setTakeStartTime(Date takeStartTime) {
		this.takeStartTime = takeStartTime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="动盘结束时间", align=2, sort=21)
	public Date getTakeEndTime() {
		return takeEndTime;
	}

	public void setTakeEndTime(Date takeEndTime) {
		this.takeEndTime = takeEndTime;
	}
	
	@ExcelField(title="抽盘数量", align=2, sort=22)
	public Double getRandomNum() {
		return randomNum;
	}

	public void setRandomNum(Double randomNum) {
		this.randomNum = randomNum;
	}
	
	@ExcelField(title="抽盘比例", align=2, sort=23)
	public Double getRandomRate() {
		return randomRate;
	}

	public void setRandomRate(Double randomRate) {
		this.randomRate = randomRate;
	}
	
	@ExcelField(title="上次盘点单号", align=2, sort=24)
	public String getParentCountNo() {
		return parentCountNo;
	}

	public void setParentCountNo(String parentCountNo) {
		this.parentCountNo = parentCountNo;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="盘点关闭时间", align=2, sort=25)
	public Date getCloseTime() {
		return closeTime;
	}

	public void setCloseTime(Date closeTime) {
		this.closeTime = closeTime;
	}
	
	@ExcelField(title="是否产生复盘单", align=2, sort=26)
	public String getIsCreateCheck() {
		return isCreateCheck;
	}

	public void setIsCreateCheck(String isCreateCheck) {
		this.isCreateCheck = isCreateCheck;
	}
	
	@ExcelField(title="是否序列号管理", align=2, sort=27)
	public String getIsSerial() {
		return isSerial;
	}

	public void setIsSerial(String isSerial) {
		this.isSerial = isSerial;
	}
	
	@ExcelField(title="监盘人", align=2, sort=28)
	public String getMonitorOp() {
		return monitorOp;
	}

	public void setMonitorOp(String monitorOp) {
		this.monitorOp = monitorOp;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="批次属性01(生产日期)", align=2, sort=29)
	public Date getLotAtt01() {
		return lotAtt01;
	}

	public void setLotAtt01(Date lotAtt01) {
		this.lotAtt01 = lotAtt01;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="批次属性02(失效日期)", align=2, sort=30)
	public Date getLotAtt02() {
		return lotAtt02;
	}

	public void setLotAtt02(Date lotAtt02) {
		this.lotAtt02 = lotAtt02;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="批次属性03(入库日期)", align=2, sort=31)
	public Date getLotAtt03() {
		return lotAtt03;
	}

	public void setLotAtt03(Date lotAtt03) {
		this.lotAtt03 = lotAtt03;
	}
	
	@ExcelField(title="批次属性04(品质)", align=2, sort=32)
	public String getLotAtt04() {
		return lotAtt04;
	}

	public void setLotAtt04(String lotAtt04) {
		this.lotAtt04 = lotAtt04;
	}
	
	@ExcelField(title="批次属性05", align=2, sort=33)
	public String getLotAtt05() {
		return lotAtt05;
	}

	public void setLotAtt05(String lotAtt05) {
		this.lotAtt05 = lotAtt05;
	}
	
	@ExcelField(title="批次属性06", align=2, sort=34)
	public String getLotAtt06() {
		return lotAtt06;
	}

	public void setLotAtt06(String lotAtt06) {
		this.lotAtt06 = lotAtt06;
	}
	
	@ExcelField(title="批次属性07", align=2, sort=35)
	public String getLotAtt07() {
		return lotAtt07;
	}

	public void setLotAtt07(String lotAtt07) {
		this.lotAtt07 = lotAtt07;
	}
	
	@ExcelField(title="批次属性08", align=2, sort=36)
	public String getLotAtt08() {
		return lotAtt08;
	}

	public void setLotAtt08(String lotAtt08) {
		this.lotAtt08 = lotAtt08;
	}
	
	@ExcelField(title="批次属性09", align=2, sort=37)
	public String getLotAtt09() {
		return lotAtt09;
	}

	public void setLotAtt09(String lotAtt09) {
		this.lotAtt09 = lotAtt09;
	}
	
	@ExcelField(title="批次属性10", align=2, sort=38)
	public String getLotAtt10() {
		return lotAtt10;
	}

	public void setLotAtt10(String lotAtt10) {
		this.lotAtt10 = lotAtt10;
	}
	
	@ExcelField(title="批次属性11", align=2, sort=39)
	public String getLotAtt11() {
		return lotAtt11;
	}

	public void setLotAtt11(String lotAtt11) {
		this.lotAtt11 = lotAtt11;
	}
	
	@ExcelField(title="批次属性12", align=2, sort=40)
	public String getLotAtt12() {
		return lotAtt12;
	}

	public void setLotAtt12(String lotAtt12) {
		this.lotAtt12 = lotAtt12;
	}
	
	@ExcelField(title="自定义1", align=2, sort=41)
	public String getDef1() {
		return def1;
	}

	public void setDef1(String def1) {
		this.def1 = def1;
	}
	
	@ExcelField(title="自定义2", align=2, sort=42)
	public String getDef2() {
		return def2;
	}

	public void setDef2(String def2) {
		this.def2 = def2;
	}
	
	@ExcelField(title="自定义3", align=2, sort=43)
	public String getDef3() {
		return def3;
	}

	public void setDef3(String def3) {
		this.def3 = def3;
	}
	
	@ExcelField(title="自定义4", align=2, sort=44)
	public String getDef4() {
		return def4;
	}

	public void setDef4(String def4) {
		this.def4 = def4;
	}
	
	@ExcelField(title="自定义5", align=2, sort=45)
	public String getDef5() {
		return def5;
	}

	public void setDef5(String def5) {
		this.def5 = def5;
	}

	@ExcelField(title="分公司", align=2, sort=54)
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getSkuName() {
		return skuName;
	}

	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getZoneName() {
		return zoneName;
	}

	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
	}
}