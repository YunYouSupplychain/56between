package com.yunyou.modules.wms.outbound.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.core.persistence.DataEntity;

import java.util.Date;

/**
 * 取消分配拣货记录Entity
 * @author WMJ
 * @version 2019-02-14
 */
public class BanQinWmDelAlloc extends DataEntity<BanQinWmDelAlloc> {
	
	private static final long serialVersionUID = 1L;
	private String allocId;		// 分配ID
	private String allocSeq;		// 取消分配流水号
	private String preallocId;		// 预配ID
	private String orderNo;		// 单据号
	private String orderType;		// 单据类型
	private String lineNo;		// 行号
	private String waveNo;		// 波次单号
	private String status;		// 状态（40完全分配，60完全拣货）
	private String ownerCode;		// 货主编码
	private String skuCode;		// 商品编码
	private String lotNum;		// 批次号
	private String locCode;		// 库位编码
	private String traceId;		// 跟踪号
	private String pkSeq;		// 拣货顺序
	private String consigneeCode;		// 收货人编码
	private String packCode;		// 包装编码
	private String uom;		// 包装单位
	private Double qtyUom;		// 数量
	private Double qtyEa;		// 数量EA
	private String toLoc;		// 目标库位编码
	private String toId;		// 目标跟踪号
	private String op;		// 操作人
	private Date opTime;		// 操作时间
	private String orgId;		// 分公司
	
	public BanQinWmDelAlloc() {
		super();
		this.recVer = 0;
	}

	public BanQinWmDelAlloc(String id){
		super(id);
	}

	@ExcelField(title="分配ID", align=2, sort=2)
	public String getAllocId() {
		return allocId;
	}

	public void setAllocId(String allocId) {
		this.allocId = allocId;
	}
	
	@ExcelField(title="取消分配流水号", align=2, sort=3)
	public String getAllocSeq() {
		return allocSeq;
	}

	public void setAllocSeq(String allocSeq) {
		this.allocSeq = allocSeq;
	}
	
	@ExcelField(title="预配ID", align=2, sort=4)
	public String getPreallocId() {
		return preallocId;
	}

	public void setPreallocId(String preallocId) {
		this.preallocId = preallocId;
	}
	
	@ExcelField(title="单据号", align=2, sort=5)
	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	
	@ExcelField(title="单据类型", align=2, sort=6)
	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	
	@ExcelField(title="行号", align=2, sort=7)
	public String getLineNo() {
		return lineNo;
	}

	public void setLineNo(String lineNo) {
		this.lineNo = lineNo;
	}
	
	@ExcelField(title="波次单号", align=2, sort=8)
	public String getWaveNo() {
		return waveNo;
	}

	public void setWaveNo(String waveNo) {
		this.waveNo = waveNo;
	}
	
	@ExcelField(title="状态（40完全分配，60完全拣货）", align=2, sort=9)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@ExcelField(title="货主编码", align=2, sort=10)
	public String getOwnerCode() {
		return ownerCode;
	}

	public void setOwnerCode(String ownerCode) {
		this.ownerCode = ownerCode;
	}
	
	@ExcelField(title="商品编码", align=2, sort=11)
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
	
	@ExcelField(title="库位编码", align=2, sort=13)
	public String getLocCode() {
		return locCode;
	}

	public void setLocCode(String locCode) {
		this.locCode = locCode;
	}
	
	@ExcelField(title="跟踪号", align=2, sort=14)
	public String getTraceId() {
		return traceId;
	}

	public void setTraceId(String traceId) {
		this.traceId = traceId;
	}
	
	@ExcelField(title="拣货顺序", align=2, sort=15)
	public String getPkSeq() {
		return pkSeq;
	}

	public void setPkSeq(String pkSeq) {
		this.pkSeq = pkSeq;
	}
	
	@ExcelField(title="收货人编码", align=2, sort=16)
	public String getConsigneeCode() {
		return consigneeCode;
	}

	public void setConsigneeCode(String consigneeCode) {
		this.consigneeCode = consigneeCode;
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
	
	@ExcelField(title="数量", align=2, sort=19)
	public Double getQtyUom() {
		return qtyUom;
	}

	public void setQtyUom(Double qtyUom) {
		this.qtyUom = qtyUom;
	}
	
	@ExcelField(title="数量EA", align=2, sort=20)
	public Double getQtyEa() {
		return qtyEa;
	}

	public void setQtyEa(Double qtyEa) {
		this.qtyEa = qtyEa;
	}
	
	@ExcelField(title="目标库位编码", align=2, sort=21)
	public String getToLoc() {
		return toLoc;
	}

	public void setToLoc(String toLoc) {
		this.toLoc = toLoc;
	}
	
	@ExcelField(title="目标跟踪号", align=2, sort=22)
	public String getToId() {
		return toId;
	}

	public void setToId(String toId) {
		this.toId = toId;
	}
	
	@ExcelField(title="操作人", align=2, sort=23)
	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="操作时间", align=2, sort=24)
	public Date getOpTime() {
		return opTime;
	}

	public void setOpTime(Date opTime) {
		this.opTime = opTime;
	}

	@ExcelField(title="分公司", align=2, sort=33)
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
}