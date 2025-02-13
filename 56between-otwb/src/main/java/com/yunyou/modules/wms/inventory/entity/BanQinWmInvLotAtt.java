package com.yunyou.modules.wms.inventory.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.core.persistence.DataEntity;

import java.util.Date;

/**
 * 批次号库存表Entity
 * @author WMJ
 * @version 2019-01-25
 */
public class BanQinWmInvLotAtt extends DataEntity<BanQinWmInvLotAtt> {
	
	private static final long serialVersionUID = 1L;
	private String lotNum;		// 批次号
	private String ownerCode;		// 货主编码
	private String skuCode;		// 商品编码
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
	private String codeAndName; // 模糊查询字段
	
	public BanQinWmInvLotAtt() {
		super();
		this.recVer = 0;
	}

	public BanQinWmInvLotAtt(String id){
		super(id);
	}

	@ExcelField(title="批次号", align=2, sort=2)
	public String getLotNum() {
		return lotNum;
	}

	public void setLotNum(String lotNum) {
		this.lotNum = lotNum;
	}
	
	@ExcelField(title="货主编码", align=2, sort=3)
	public String getOwnerCode() {
		return ownerCode;
	}

	public void setOwnerCode(String ownerCode) {
		this.ownerCode = ownerCode;
	}
	
	@ExcelField(title="商品编码", align=2, sort=4)
	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	@ExcelField(title="批次属性01(生产日期)", align=2, sort=5)
	public Date getLotAtt01() {
		return lotAtt01;
	}

	public void setLotAtt01(Date lotAtt01) {
		this.lotAtt01 = lotAtt01;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	@ExcelField(title="批次属性02(失效日期)", align=2, sort=6)
	public Date getLotAtt02() {
		return lotAtt02;
	}

	public void setLotAtt02(Date lotAtt02) {
		this.lotAtt02 = lotAtt02;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	@ExcelField(title="批次属性03(入库日期)", align=2, sort=7)
	public Date getLotAtt03() {
		return lotAtt03;
	}

	public void setLotAtt03(Date lotAtt03) {
		this.lotAtt03 = lotAtt03;
	}
	
	@ExcelField(title="批次属性04(品质)", align=2, sort=8)
	public String getLotAtt04() {
		return lotAtt04;
	}

	public void setLotAtt04(String lotAtt04) {
		this.lotAtt04 = lotAtt04;
	}
	
	@ExcelField(title="批次属性05", align=2, sort=9)
	public String getLotAtt05() {
		return lotAtt05;
	}

	public void setLotAtt05(String lotAtt05) {
		this.lotAtt05 = lotAtt05;
	}
	
	@ExcelField(title="批次属性06", align=2, sort=10)
	public String getLotAtt06() {
		return lotAtt06;
	}

	public void setLotAtt06(String lotAtt06) {
		this.lotAtt06 = lotAtt06;
	}
	
	@ExcelField(title="批次属性07", align=2, sort=11)
	public String getLotAtt07() {
		return lotAtt07;
	}

	public void setLotAtt07(String lotAtt07) {
		this.lotAtt07 = lotAtt07;
	}
	
	@ExcelField(title="批次属性08", align=2, sort=12)
	public String getLotAtt08() {
		return lotAtt08;
	}

	public void setLotAtt08(String lotAtt08) {
		this.lotAtt08 = lotAtt08;
	}
	
	@ExcelField(title="批次属性09", align=2, sort=13)
	public String getLotAtt09() {
		return lotAtt09;
	}

	public void setLotAtt09(String lotAtt09) {
		this.lotAtt09 = lotAtt09;
	}
	
	@ExcelField(title="批次属性10", align=2, sort=14)
	public String getLotAtt10() {
		return lotAtt10;
	}

	public void setLotAtt10(String lotAtt10) {
		this.lotAtt10 = lotAtt10;
	}
	
	@ExcelField(title="批次属性11", align=2, sort=15)
	public String getLotAtt11() {
		return lotAtt11;
	}

	public void setLotAtt11(String lotAtt11) {
		this.lotAtt11 = lotAtt11;
	}
	
	@ExcelField(title="批次属性12", align=2, sort=16)
	public String getLotAtt12() {
		return lotAtt12;
	}

	public void setLotAtt12(String lotAtt12) {
		this.lotAtt12 = lotAtt12;
	}

	@ExcelField(title="分公司", align=2, sort=24)
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getCodeAndName() {
		return codeAndName;
	}

	public void setCodeAndName(String codeAndName) {
		this.codeAndName = codeAndName;
	}
}