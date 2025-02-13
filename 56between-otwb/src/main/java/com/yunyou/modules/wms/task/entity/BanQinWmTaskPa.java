package com.yunyou.modules.wms.task.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.core.persistence.DataEntity;

import java.util.Date;

/**
 * 上架任务Entity
 * @author WMJ
 * @version 2019-01-26
 */
public class BanQinWmTaskPa extends DataEntity<BanQinWmTaskPa> {
	
	private static final long serialVersionUID = 1L;
	private String paId;		// 上架任务ID
	private String lineNo;		// 上架任务行号
	private String status;		// 状态
	private String orderNo;		// 单据号
	private String orderType;		// 单据类型
	private String ownerCode;		// 货主编码
	private String skuCode;		// 商品编码
	private String lotNum;		// 批次号
	private String fmLoc;		// 源库位编码
	private String fmId;		// 源跟踪号
	private String toLoc;		// 目标库位编码
	private String toId;		// 目标跟踪号
	private String reserveCode;		// 上架库位指定规则
	private String paRule;		// 上架规则
	private String suggestLoc;		// 推荐库位
	private String packCode;		// 包装编码
	private String uom;		// 包装单位
	private Double qtyPaUom;		// 上架包装数量
	private Double qtyPaEa;		// 上架数EA
	private String paOp;		// 上架操作人
	private Date paTime;		// 上架时间
	private Integer printNum;		// 打印次数
	private String orgId;		// 分公司
	private String traceId;		// 托盘ID
	
	public BanQinWmTaskPa() {
		super();
		this.recVer = 0;
	}

	public BanQinWmTaskPa(String id){
		super(id);
	}

	@ExcelField(title="上架任务ID", align=2, sort=2)
	public String getPaId() {
		return paId;
	}

	public void setPaId(String paId) {
		this.paId = paId;
	}
	
	@ExcelField(title="上架任务行号", align=2, sort=3)
	public String getLineNo() {
		return lineNo;
	}

	public void setLineNo(String lineNo) {
		this.lineNo = lineNo;
	}
	
	@ExcelField(title="状态", dictType="", align=2, sort=4)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@ExcelField(title="单据号", align=2, sort=5)
	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	
	@ExcelField(title="单据类型", dictType="", align=2, sort=6)
	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	
	@ExcelField(title="货主编码", align=2, sort=7)
	public String getOwnerCode() {
		return ownerCode;
	}

	public void setOwnerCode(String ownerCode) {
		this.ownerCode = ownerCode;
	}
	
	@ExcelField(title="商品编码", align=2, sort=8)
	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}
	
	@ExcelField(title="批次号", align=2, sort=9)
	public String getLotNum() {
		return lotNum;
	}

	public void setLotNum(String lotNum) {
		this.lotNum = lotNum;
	}
	
	@ExcelField(title="源库位编码", align=2, sort=10)
	public String getFmLoc() {
		return fmLoc;
	}

	public void setFmLoc(String fmLoc) {
		this.fmLoc = fmLoc;
	}
	
	@ExcelField(title="源跟踪号", align=2, sort=11)
	public String getFmId() {
		return fmId;
	}

	public void setFmId(String fmId) {
		this.fmId = fmId;
	}
	
	@ExcelField(title="目标库位编码", align=2, sort=12)
	public String getToLoc() {
		return toLoc;
	}

	public void setToLoc(String toLoc) {
		this.toLoc = toLoc;
	}
	
	@ExcelField(title="目标跟踪号", align=2, sort=13)
	public String getToId() {
		return toId;
	}

	public void setToId(String toId) {
		this.toId = toId;
	}
	
	@ExcelField(title="上架库位指定规则", align=2, sort=14)
	public String getReserveCode() {
		return reserveCode;
	}

	public void setReserveCode(String reserveCode) {
		this.reserveCode = reserveCode;
	}
	
	@ExcelField(title="上架规则", align=2, sort=15)
	public String getPaRule() {
		return paRule;
	}

	public void setPaRule(String paRule) {
		this.paRule = paRule;
	}
	
	@ExcelField(title="推荐库位", align=2, sort=16)
	public String getSuggestLoc() {
		return suggestLoc;
	}

	public void setSuggestLoc(String suggestLoc) {
		this.suggestLoc = suggestLoc;
	}
	
	@ExcelField(title="包装编码", align=2, sort=17)
	public String getPackCode() {
		return packCode;
	}

	public void setPackCode(String packCode) {
		this.packCode = packCode;
	}
	
	@ExcelField(title="包装单位", align=2, sort=18)
	public String getUom() {
		return uom;
	}

	public void setUom(String uom) {
		this.uom = uom;
	}
	
	@ExcelField(title="上架包装数量", align=2, sort=19)
	public Double getQtyPaUom() {
		return qtyPaUom;
	}

	public void setQtyPaUom(Double qtyPaUom) {
		this.qtyPaUom = qtyPaUom;
	}
	
	@ExcelField(title="上架数EA", align=2, sort=20)
	public Double getQtyPaEa() {
		return qtyPaEa;
	}

	public void setQtyPaEa(Double qtyPaEa) {
		this.qtyPaEa = qtyPaEa;
	}
	
	@ExcelField(title="上架操作人", align=2, sort=21)
	public String getPaOp() {
		return paOp;
	}

	public void setPaOp(String paOp) {
		this.paOp = paOp;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="上架时间", align=2, sort=22)
	public Date getPaTime() {
		return paTime;
	}

	public void setPaTime(Date paTime) {
		this.paTime = paTime;
	}
	
	@ExcelField(title="打印次数", align=2, sort=23)
	public Integer getPrintNum() {
		return printNum;
	}

	public void setPrintNum(Integer printNum) {
		this.printNum = printNum;
	}

	@ExcelField(title="分公司", align=2, sort=32)
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
	@ExcelField(title="托盘ID", align=2, sort=33)
	public String getTraceId() {
		return traceId;
	}

	public void setTraceId(String traceId) {
		this.traceId = traceId;
	}
	
}