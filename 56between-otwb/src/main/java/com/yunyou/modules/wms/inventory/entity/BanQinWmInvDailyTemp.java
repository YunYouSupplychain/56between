package com.yunyou.modules.wms.inventory.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.core.persistence.DataEntity;

import java.util.Date;

/**
 * 库存临时表Entity
 * @author WMJ
 * @version 2019-01-25
 */
public class BanQinWmInvDailyTemp extends DataEntity<BanQinWmInvDailyTemp> {
	
	private static final long serialVersionUID = 1L;
	private Date tranTime;		// 库存日期
	private String ownerCode;		// 货主编码
	private String ownerName;		// 货主名称
	private String skuCode;		// 商品编码
	private String skuName;		// 商品名称
	private String lotNum;		// 批次号
	private String locCode;		// 库位
	private String traceId;		// 跟踪号
	private Double invQty;		// 库存数
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
	private String orgId;		// 分公司
	
	public BanQinWmInvDailyTemp() {
		super();
		this.recVer = 0;
	}

	public BanQinWmInvDailyTemp(String id){
		super(id);
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="库存日期", align=2, sort=2)
	public Date getTranTime() {
		return tranTime;
	}

	public void setTranTime(Date tranTime) {
		this.tranTime = tranTime;
	}
	
	@ExcelField(title="货主编码", align=2, sort=3)
	public String getOwnerCode() {
		return ownerCode;
	}

	public void setOwnerCode(String ownerCode) {
		this.ownerCode = ownerCode;
	}
	
	@ExcelField(title="货主名称", align=2, sort=4)
	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	
	@ExcelField(title="商品编码", align=2, sort=5)
	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}
	
	@ExcelField(title="商品名称", align=2, sort=6)
	public String getSkuName() {
		return skuName;
	}

	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}
	
	@ExcelField(title="批次号", align=2, sort=7)
	public String getLotNum() {
		return lotNum;
	}

	public void setLotNum(String lotNum) {
		this.lotNum = lotNum;
	}
	
	@ExcelField(title="库位", align=2, sort=8)
	public String getLocCode() {
		return locCode;
	}

	public void setLocCode(String locCode) {
		this.locCode = locCode;
	}
	
	@ExcelField(title="跟踪号", align=2, sort=9)
	public String getTraceId() {
		return traceId;
	}

	public void setTraceId(String traceId) {
		this.traceId = traceId;
	}
	
	@ExcelField(title="库存数", align=2, sort=10)
	public Double getInvQty() {
		return invQty;
	}

	public void setInvQty(Double invQty) {
		this.invQty = invQty;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="批次属性01(生产日期)", align=2, sort=11)
	public Date getLotAtt01() {
		return lotAtt01;
	}

	public void setLotAtt01(Date lotAtt01) {
		this.lotAtt01 = lotAtt01;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="批次属性02(失效日期)", align=2, sort=12)
	public Date getLotAtt02() {
		return lotAtt02;
	}

	public void setLotAtt02(Date lotAtt02) {
		this.lotAtt02 = lotAtt02;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="批次属性03(入库日期)", align=2, sort=13)
	public Date getLotAtt03() {
		return lotAtt03;
	}

	public void setLotAtt03(Date lotAtt03) {
		this.lotAtt03 = lotAtt03;
	}
	
	@ExcelField(title="批次属性04(品质)", align=2, sort=14)
	public String getLotAtt04() {
		return lotAtt04;
	}

	public void setLotAtt04(String lotAtt04) {
		this.lotAtt04 = lotAtt04;
	}
	
	@ExcelField(title="批次属性05", align=2, sort=15)
	public String getLotAtt05() {
		return lotAtt05;
	}

	public void setLotAtt05(String lotAtt05) {
		this.lotAtt05 = lotAtt05;
	}
	
	@ExcelField(title="批次属性06", align=2, sort=16)
	public String getLotAtt06() {
		return lotAtt06;
	}

	public void setLotAtt06(String lotAtt06) {
		this.lotAtt06 = lotAtt06;
	}
	
	@ExcelField(title="批次属性07", align=2, sort=17)
	public String getLotAtt07() {
		return lotAtt07;
	}

	public void setLotAtt07(String lotAtt07) {
		this.lotAtt07 = lotAtt07;
	}
	
	@ExcelField(title="批次属性08", align=2, sort=18)
	public String getLotAtt08() {
		return lotAtt08;
	}

	public void setLotAtt08(String lotAtt08) {
		this.lotAtt08 = lotAtt08;
	}
	
	@ExcelField(title="批次属性09", align=2, sort=19)
	public String getLotAtt09() {
		return lotAtt09;
	}

	public void setLotAtt09(String lotAtt09) {
		this.lotAtt09 = lotAtt09;
	}
	
	@ExcelField(title="批次属性10", align=2, sort=20)
	public String getLotAtt10() {
		return lotAtt10;
	}

	public void setLotAtt10(String lotAtt10) {
		this.lotAtt10 = lotAtt10;
	}
	
	@ExcelField(title="批次属性11", align=2, sort=21)
	public String getLotAtt11() {
		return lotAtt11;
	}

	public void setLotAtt11(String lotAtt11) {
		this.lotAtt11 = lotAtt11;
	}
	
	@ExcelField(title="批次属性12", align=2, sort=22)
	public String getLotAtt12() {
		return lotAtt12;
	}

	public void setLotAtt12(String lotAtt12) {
		this.lotAtt12 = lotAtt12;
	}

	@ExcelField(title="分公司", align=2, sort=30)
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
}