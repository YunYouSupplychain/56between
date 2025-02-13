package com.yunyou.modules.wms.task.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.core.persistence.DataEntity;

import java.util.Date;

/**
 * 补货任务Entity
 * @author WMJ
 * @version 2019-01-26
 */
public class BanQinWmTaskRp extends DataEntity<BanQinWmTaskRp> {
	
	private static final long serialVersionUID = 1L;
	private String rpId;		// 补货ID
	private String status;		// 状态
	private String ownerCode;		// 货主编码
	private String skuCode;		// 商品编码
	private String lotNum;		// 批次号
	private String fmLoc;		// 源库位编码
	private String fmId;		// 源跟踪号
	private String toLoc;		// 目标库位编码
	private String toId;		// 目标跟踪号
	private String packCode;		// 包装编码
	private String uom;		// 包装单位
	private Double qtyRpUom;		// 补货包装数量
	private Double qtyRpEa;		// 补货数EA
	private String rpOp;		// 补货操作人
	private Date rpTime;		// 补货时间
	private Integer printNum;		// 打印次数
	private String orgId;		// 分公司
	
	public BanQinWmTaskRp() {
		super();
		this.recVer = 0;
	}

	public BanQinWmTaskRp(String id){
		super(id);
	}

	@ExcelField(title="补货ID", align=2, sort=2)
	public String getRpId() {
		return rpId;
	}

	public void setRpId(String rpId) {
		this.rpId = rpId;
	}
	
	@ExcelField(title="状态", dictType="", align=2, sort=3)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@ExcelField(title="货主编码", align=2, sort=4)
	public String getOwnerCode() {
		return ownerCode;
	}

	public void setOwnerCode(String ownerCode) {
		this.ownerCode = ownerCode;
	}
	
	@ExcelField(title="商品编码", align=2, sort=5)
	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}
	
	@ExcelField(title="批次号", align=2, sort=6)
	public String getLotNum() {
		return lotNum;
	}

	public void setLotNum(String lotNum) {
		this.lotNum = lotNum;
	}
	
	@ExcelField(title="源库位编码", align=2, sort=7)
	public String getFmLoc() {
		return fmLoc;
	}

	public void setFmLoc(String fmLoc) {
		this.fmLoc = fmLoc;
	}
	
	@ExcelField(title="源跟踪号", align=2, sort=8)
	public String getFmId() {
		return fmId;
	}

	public void setFmId(String fmId) {
		this.fmId = fmId;
	}
	
	@ExcelField(title="目标库位编码", align=2, sort=9)
	public String getToLoc() {
		return toLoc;
	}

	public void setToLoc(String toLoc) {
		this.toLoc = toLoc;
	}
	
	@ExcelField(title="目标跟踪号", align=2, sort=10)
	public String getToId() {
		return toId;
	}

	public void setToId(String toId) {
		this.toId = toId;
	}
	
	@ExcelField(title="包装编码", align=2, sort=11)
	public String getPackCode() {
		return packCode;
	}

	public void setPackCode(String packCode) {
		this.packCode = packCode;
	}
	
	@ExcelField(title="包装单位", align=2, sort=12)
	public String getUom() {
		return uom;
	}

	public void setUom(String uom) {
		this.uom = uom;
	}
	
	@ExcelField(title="补货包装数量", align=2, sort=13)
	public Double getQtyRpUom() {
		return qtyRpUom;
	}

	public void setQtyRpUom(Double qtyRpUom) {
		this.qtyRpUom = qtyRpUom;
	}
	
	@ExcelField(title="补货数EA", align=2, sort=14)
	public Double getQtyRpEa() {
		return qtyRpEa;
	}

	public void setQtyRpEa(Double qtyRpEa) {
		this.qtyRpEa = qtyRpEa;
	}
	
	@ExcelField(title="补货操作人", align=2, sort=15)
	public String getRpOp() {
		return rpOp;
	}

	public void setRpOp(String rpOp) {
		this.rpOp = rpOp;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="补货时间", align=2, sort=16)
	public Date getRpTime() {
		return rpTime;
	}

	public void setRpTime(Date rpTime) {
		this.rpTime = rpTime;
	}
	
	@ExcelField(title="打印次数", align=2, sort=17)
	public Integer getPrintNum() {
		return printNum;
	}

	public void setPrintNum(Integer printNum) {
		this.printNum = printNum;
	}

	@ExcelField(title="分公司", align=2, sort=26)
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
}