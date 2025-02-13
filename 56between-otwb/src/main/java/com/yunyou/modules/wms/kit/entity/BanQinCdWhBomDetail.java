package com.yunyou.modules.wms.kit.entity;

import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.core.persistence.DataEntity;

/**
 * 组合件明细Entity
 * @author Jianhua Liu
 * @version 2019-08-19
 */
public class BanQinCdWhBomDetail extends DataEntity<BanQinCdWhBomDetail> {
	
	private static final long serialVersionUID = 1L;
	private String lineNo;			// 行号
	private String ownerCode;		// 货主编码
	private String parentSkuCode;	// 父件编码
	private String kitType;			// 加工类型
	private String subSkuCode;		// 子件编码
	private String subSkuType;		// 子件类型
	private String packCode;		// 包装代码
	private String uom;		// 包装单位
	private Double qty;		// 数量
	private Double qtyEa;	// EA数量
	private String orgId;	// 机构ID
	private String headerId;// 头Id
	
	public BanQinCdWhBomDetail() {
		super();
		this.recVer = 0;
	}

	public BanQinCdWhBomDetail(String id){
		super(id);
	}

	@ExcelField(title="货主编码", align=2, sort=2)
	public String getOwnerCode() {
		return ownerCode;
	}

	public void setOwnerCode(String ownerCode) {
		this.ownerCode = ownerCode;
	}
	
	@ExcelField(title="父件编码", align=2, sort=3)
	public String getParentSkuCode() {
		return parentSkuCode;
	}

	public void setParentSkuCode(String parentSkuCode) {
		this.parentSkuCode = parentSkuCode;
	}
	
	@ExcelField(title="加工类型", align=2, sort=4)
	public String getKitType() {
		return kitType;
	}

	public void setKitType(String kitType) {
		this.kitType = kitType;
	}
	
	@ExcelField(title="行号", align=2, sort=5)
	public String getLineNo() {
		return lineNo;
	}

	public void setLineNo(String lineNo) {
		this.lineNo = lineNo;
	}
	
	@ExcelField(title="子件编码", align=2, sort=6)
	public String getSubSkuCode() {
		return subSkuCode;
	}

	public void setSubSkuCode(String subSkuCode) {
		this.subSkuCode = subSkuCode;
	}
	
	@ExcelField(title="子件类型", align=2, sort=7)
	public String getSubSkuType() {
		return subSkuType;
	}

	public void setSubSkuType(String subSkuType) {
		this.subSkuType = subSkuType;
	}
	
	@ExcelField(title="包装代码", align=2, sort=8)
	public String getPackCode() {
		return packCode;
	}

	public void setPackCode(String packCode) {
		this.packCode = packCode;
	}
	
	@ExcelField(title="包装单位", align=2, sort=9)
	public String getUom() {
		return uom;
	}

	public void setUom(String uom) {
		this.uom = uom;
	}
	
	@ExcelField(title="数量", align=2, sort=10)
	public Double getQty() {
		return qty;
	}

	public void setQty(Double qty) {
		this.qty = qty;
	}
	
	@ExcelField(title="EA数量", align=2, sort=11)
	public Double getQtyEa() {
		return qtyEa;
	}

	public void setQtyEa(Double qtyEa) {
		this.qtyEa = qtyEa;
	}

	@ExcelField(title="机构ID", align=2, sort=20)
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getHeaderId() {
		return headerId;
	}

	public void setHeaderId(String headerId) {
		this.headerId = headerId;
	}
}