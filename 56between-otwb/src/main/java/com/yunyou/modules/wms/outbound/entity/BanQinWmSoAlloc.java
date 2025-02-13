package com.yunyou.modules.wms.outbound.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.core.persistence.DataEntity;

import java.util.Date;

/**
 * 分配拣货明细Entity
 * @author WMJ
 * @version 2019-02-14
 */
public class BanQinWmSoAlloc extends DataEntity<BanQinWmSoAlloc> {
	
	private static final long serialVersionUID = 1L;
	private String allocId;		// 分配ID
	private String preallocId;		// 预配ID
	private String waveNo;		// 波次单号
	private String soNo;		// 出库单号
	private String lineNo;		// 行号
	private String ownerCode;		// 货主编码
	private String skuCode;		// 商品编码
	private String lotNum;		// 批次号
	private String locCode;		// 库位编码
	private String traceId;		// 跟踪号
	private String consigneeCode;		// 收货人编码
	private String status;		// 状态
	private String checkStatus;		// 复核状态(00未复核、99已复核、90不复核)
	private String packCode;		// 包装编码
	private String uom;		// 包装单位
	private Double qtyUom;		// 包装数量
	private Double qtyEa;		// EA数量
	private String toLoc;		// 目标库位编码
	private String toId;		// 目标跟踪号
	private String pickOp;		// 拣货人
	private Date pickTime;		// 拣货时间
	private String checkOp;		// 复核人
	private Date checkTime;		// 复核时间
	private String packOp;		// 打包人
	private Date packTime;		// 打包时间
	private String shipOp;		// 发货人
	private Date shipTime;		// 发货时间
	private Integer printNum;		// 打印次数
	private String pickNo;		// 拣货单号
	private String trackingNo;		// 快递单号
	private Double packWeight;		// 包裹总重量
	private String asnNo;		// 入库单号
	private String asnLineNo;		// 入库单行号
	private String rcvLineNo;		// 收货明细行号
	private String cdType;		// 越库类型
	private String cdOutStep;		// 直接越库时出库执行的步骤
	private String orgId;		// 分公司
	private String caseNo;		// 箱号
    private Integer packScanCount;  // 打包扫描次数

	public BanQinWmSoAlloc() {
		super();
		this.recVer = 0;
	}

	public BanQinWmSoAlloc(String id){
		super(id);
	}

	@ExcelField(title="分配ID", align=2, sort=2)
	public String getAllocId() {
		return allocId;
	}

	public void setAllocId(String allocId) {
		this.allocId = allocId;
	}
	
	@ExcelField(title="预配ID", align=2, sort=3)
	public String getPreallocId() {
		return preallocId;
	}

	public void setPreallocId(String preallocId) {
		this.preallocId = preallocId;
	}
	
	@ExcelField(title="波次单号", align=2, sort=4)
	public String getWaveNo() {
		return waveNo;
	}

	public void setWaveNo(String waveNo) {
		this.waveNo = waveNo;
	}
	
	@ExcelField(title="出库单号", align=2, sort=5)
	public String getSoNo() {
		return soNo;
	}

	public void setSoNo(String soNo) {
		this.soNo = soNo;
	}
	
	@ExcelField(title="行号", align=2, sort=6)
	public String getLineNo() {
		return lineNo;
	}

	public void setLineNo(String lineNo) {
		this.lineNo = lineNo;
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
	
	@ExcelField(title="库位编码", align=2, sort=10)
	public String getLocCode() {
		return locCode;
	}

	public void setLocCode(String locCode) {
		this.locCode = locCode;
	}
	
	@ExcelField(title="跟踪号", align=2, sort=11)
	public String getTraceId() {
		return traceId;
	}

	public void setTraceId(String traceId) {
		this.traceId = traceId;
	}
	
	@ExcelField(title="收货人编码", align=2, sort=12)
	public String getConsigneeCode() {
		return consigneeCode;
	}

	public void setConsigneeCode(String consigneeCode) {
		this.consigneeCode = consigneeCode;
	}
	
	@ExcelField(title="状态", dictType="", align=2, sort=13)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@ExcelField(title="复核状态(00未复核、99已复核、90不复核)", dictType="", align=2, sort=14)
	public String getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}
	
	@ExcelField(title="包装编码", align=2, sort=15)
	public String getPackCode() {
		return packCode;
	}

	public void setPackCode(String packCode) {
		this.packCode = packCode;
	}
	
	@ExcelField(title="包装单位", align=2, sort=16)
	public String getUom() {
		return uom;
	}

	public void setUom(String uom) {
		this.uom = uom;
	}
	
	@ExcelField(title="包装数量", align=2, sort=17)
	public Double getQtyUom() {
		return qtyUom;
	}

	public void setQtyUom(Double qtyUom) {
		this.qtyUom = qtyUom;
	}
	
	@ExcelField(title="EA数量", align=2, sort=18)
	public Double getQtyEa() {
		return qtyEa;
	}

	public void setQtyEa(Double qtyEa) {
		this.qtyEa = qtyEa;
	}
	
	@ExcelField(title="目标库位编码", align=2, sort=19)
	public String getToLoc() {
		return toLoc;
	}

	public void setToLoc(String toLoc) {
		this.toLoc = toLoc;
	}
	
	@ExcelField(title="目标跟踪号", align=2, sort=20)
	public String getToId() {
		return toId;
	}

	public void setToId(String toId) {
		this.toId = toId;
	}
	
	@ExcelField(title="拣货人", align=2, sort=21)
	public String getPickOp() {
		return pickOp;
	}

	public void setPickOp(String pickOp) {
		this.pickOp = pickOp;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="拣货时间", align=2, sort=22)
	public Date getPickTime() {
		return pickTime;
	}

	public void setPickTime(Date pickTime) {
		this.pickTime = pickTime;
	}
	
	@ExcelField(title="复核人", align=2, sort=23)
	public String getCheckOp() {
		return checkOp;
	}

	public void setCheckOp(String checkOp) {
		this.checkOp = checkOp;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="复核时间", align=2, sort=24)
	public Date getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(Date checkTime) {
		this.checkTime = checkTime;
	}
	
	@ExcelField(title="打包人", align=2, sort=25)
	public String getPackOp() {
		return packOp;
	}

	public void setPackOp(String packOp) {
		this.packOp = packOp;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="打包时间", align=2, sort=26)
	public Date getPackTime() {
		return packTime;
	}

	public void setPackTime(Date packTime) {
		this.packTime = packTime;
	}
	
	@ExcelField(title="发货人", align=2, sort=27)
	public String getShipOp() {
		return shipOp;
	}

	public void setShipOp(String shipOp) {
		this.shipOp = shipOp;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="发货时间", align=2, sort=28)
	public Date getShipTime() {
		return shipTime;
	}

	public void setShipTime(Date shipTime) {
		this.shipTime = shipTime;
	}
	
	@ExcelField(title="打印次数", align=2, sort=29)
	public Integer getPrintNum() {
		return printNum;
	}

	public void setPrintNum(Integer printNum) {
		this.printNum = printNum;
	}
	
	@ExcelField(title="拣货单号", align=2, sort=30)
	public String getPickNo() {
		return pickNo;
	}

	public void setPickNo(String pickNo) {
		this.pickNo = pickNo;
	}
	
	@ExcelField(title="快递单号", align=2, sort=31)
	public String getTrackingNo() {
		return trackingNo;
	}

	public void setTrackingNo(String trackingNo) {
		this.trackingNo = trackingNo;
	}
	
	@ExcelField(title="包裹总重量", align=2, sort=32)
	public Double getPackWeight() {
		return packWeight;
	}

	public void setPackWeight(Double packWeight) {
		this.packWeight = packWeight;
	}
	
	@ExcelField(title="入库单号", align=2, sort=33)
	public String getAsnNo() {
		return asnNo;
	}

	public void setAsnNo(String asnNo) {
		this.asnNo = asnNo;
	}
	
	@ExcelField(title="入库单行号", align=2, sort=34)
	public String getAsnLineNo() {
		return asnLineNo;
	}

	public void setAsnLineNo(String asnLineNo) {
		this.asnLineNo = asnLineNo;
	}
	
	@ExcelField(title="收货明细行号", align=2, sort=35)
	public String getRcvLineNo() {
		return rcvLineNo;
	}

	public void setRcvLineNo(String rcvLineNo) {
		this.rcvLineNo = rcvLineNo;
	}
	
	@ExcelField(title="越库类型", dictType="", align=2, sort=36)
	public String getCdType() {
		return cdType;
	}

	public void setCdType(String cdType) {
		this.cdType = cdType;
	}
	
	@ExcelField(title="直接越库时出库执行的步骤", align=2, sort=37)
	public String getCdOutStep() {
		return cdOutStep;
	}

	public void setCdOutStep(String cdOutStep) {
		this.cdOutStep = cdOutStep;
	}

	@ExcelField(title="分公司", align=2, sort=46)
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getCaseNo() {
		return caseNo;
	}

	public void setCaseNo(String caseNo) {
		this.caseNo = caseNo;
	}

    public Integer getPackScanCount() {
        return packScanCount;
    }

    public void setPackScanCount(Integer packScanCount) {
        this.packScanCount = packScanCount;
    }
}