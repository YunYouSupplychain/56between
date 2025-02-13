package com.yunyou.modules.wms.outbound.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.List;

/**
 * 装车单明细LdDetailEntity
 * @author WMJ
 * @version 2019/02/20
 */
public class BanQinWmLdDetailEntity extends BanQinWmLdDetail {
	// 波次单号
	private String waveNo;
	// 出库单号
	private String soNo;
	// SO行号
	private String soLineNo;
	// 货主编码
	private String ownerCode;
	// 商品编码
	private String skuCode;
	// 批次号
	private String lotNum;
	// 库位编码
	private String locCode;
	// 跟踪号
	private String traceId;
	// 收货人编码
	private String consigneeCode;
	// 分配状态
	private String allocStatus;
	// 复核状态(00未复核、99已复核、90不复核)
	private String checkStatus;
	// 包装编码
	private String packCode;
	// 包装单位
	private String uom;
	// 包装数量
	private Double qtyUom;
	// EA数量
	private Double qtyEa;
	// 目标库位编码
	private String toLoc;
	// 目标跟踪号
	private String toId;
	// 拣货人
	private String pickOp;
	// 拣货时间
	private Date pickTime;
	// 复核人
	private String checkOp;
	// 复核时间
	private Date checkTime;
	// 打包人
	private String packOp;
	// 打包时间
	private Date packTime;
	// 发货人
	private String shipOp;
	// 发货时间
	private Date shipTime;
	// 打印次数
	private Long printNum;
	// 拣货单号
	private String pickNo;
	// 备注
	private String remark;
	// 快递单号
	private String trackingNo;
	// 货主名称
	private String ownerName;
	// 商品名称
	private String skuName;
	// 包装规格
	private String packDesc;
	// 包装单位描述
	private String uomDesc;
	// 包装换算数量
	private Double uomQty;
	// 装载包装数量
	private Double loadQty;
	// 装车人名称
	private String ldOpName;
	// 数量
	private Double wsaQtyEa;
	// 毛重
	private Double wsaGrossWeight;
	// 净重
	private Double wsaNetWeight;
	// 体积
	private Double wsaCubic;
	
	private String isSerial;

	private Integer rowNum;

	// 订单时间
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date orderTime;
	
	// 用作查询传参
    private List<String> allocIds;
    private List<String> soNos;
    private List<String> toIds;
    private List<String> waveNos;
    private List<String> ldNos;
    
    private String asnNo;
    private String rcvLineNo;
    private String isFullDelivery;
    private String soType;
    private String ldStatus;
	private Date fmEtd;
	private Date toEtd;
	private Date fmOrderDate;
	private Date toOrderDate;
	private String carrierCode;
	private String consigneeName;
	private String consigneeTel;
	private String consigneeAddr;
	private String contactName;
	private String contactTel;
	private String contactAddr;
	private String line;
	private String logisticNo;

	public String getWaveNo() {
		return waveNo;
	}

	public void setWaveNo(String waveNo) {
		this.waveNo = waveNo;
	}

	public String getSoNo() {
		return soNo;
	}

	public void setSoNo(String soNo) {
		this.soNo = soNo;
	}

	public String getSoLineNo() {
		return soLineNo;
	}

	public void setSoLineNo(String soLineNo) {
		this.soLineNo = soLineNo;
	}

	public String getOwnerCode() {
		return ownerCode;
	}

	public void setOwnerCode(String ownerCode) {
		this.ownerCode = ownerCode;
	}

	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

	public String getLotNum() {
		return lotNum;
	}

	public void setLotNum(String lotNum) {
		this.lotNum = lotNum;
	}

	public String getLocCode() {
		return locCode;
	}

	public void setLocCode(String locCode) {
		this.locCode = locCode;
	}

	public String getTraceId() {
		return traceId;
	}

	public void setTraceId(String traceId) {
		this.traceId = traceId;
	}

	public String getConsigneeCode() {
		return consigneeCode;
	}

	public void setConsigneeCode(String consigneeCode) {
		this.consigneeCode = consigneeCode;
	}

	public String getAllocStatus() {
		return allocStatus;
	}

	public void setAllocStatus(String allocStatus) {
		this.allocStatus = allocStatus;
	}

	public String getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}

	public String getPackCode() {
		return packCode;
	}

	public void setPackCode(String packCode) {
		this.packCode = packCode;
	}

	public String getUom() {
		return uom;
	}

	public void setUom(String uom) {
		this.uom = uom;
	}

	public Double getQtyUom() {
		return qtyUom;
	}

	public void setQtyUom(Double qtyUom) {
		this.qtyUom = qtyUom;
	}

	public Double getQtyEa() {
		return qtyEa;
	}

	public void setQtyEa(Double qtyEa) {
		this.qtyEa = qtyEa;
	}

	public String getToLoc() {
		return toLoc;
	}

	public void setToLoc(String toLoc) {
		this.toLoc = toLoc;
	}

	public String getToId() {
		return toId;
	}

	public void setToId(String toId) {
		this.toId = toId;
	}

	public String getPickOp() {
		return pickOp;
	}

	public void setPickOp(String pickOp) {
		this.pickOp = pickOp;
	}

	public Date getPickTime() {
		return pickTime;
	}

	public void setPickTime(Date pickTime) {
		this.pickTime = pickTime;
	}

	public String getCheckOp() {
		return checkOp;
	}

	public void setCheckOp(String checkOp) {
		this.checkOp = checkOp;
	}

	public Date getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(Date checkTime) {
		this.checkTime = checkTime;
	}

	public String getPackOp() {
		return packOp;
	}

	public void setPackOp(String packOp) {
		this.packOp = packOp;
	}

	public Date getPackTime() {
		return packTime;
	}

	public void setPackTime(Date packTime) {
		this.packTime = packTime;
	}

	public String getShipOp() {
		return shipOp;
	}

	public void setShipOp(String shipOp) {
		this.shipOp = shipOp;
	}

	public Date getShipTime() {
		return shipTime;
	}

	public void setShipTime(Date shipTime) {
		this.shipTime = shipTime;
	}

	public Long getPrintNum() {
		return printNum;
	}

	public void setPrintNum(Long printNum) {
		this.printNum = printNum;
	}

	public String getPickNo() {
		return pickNo;
	}

	public void setPickNo(String pickNo) {
		this.pickNo = pickNo;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getTrackingNo() {
		return trackingNo;
	}

	public void setTrackingNo(String trackingNo) {
		this.trackingNo = trackingNo;
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

	public String getPackDesc() {
		return packDesc;
	}

	public void setPackDesc(String packDesc) {
		this.packDesc = packDesc;
	}

	public String getUomDesc() {
		return uomDesc;
	}

	public void setUomDesc(String uomDesc) {
		this.uomDesc = uomDesc;
	}

	public Double getUomQty() {
		return uomQty;
	}

	public void setUomQty(Double uomQty) {
		this.uomQty = uomQty;
	}

	public String getLdOpName() {
		return ldOpName;
	}

	public void setLdOpName(String ldOpName) {
		this.ldOpName = ldOpName;
	}

	public Double getLoadQty() {
		return loadQty;
	}

	public void setLoadQty(Double loadQty) {
		this.loadQty = loadQty;
	}

	public Double getWsaGrossWeight() {
		return wsaGrossWeight;
	}

	public void setWsaGrossWeight(Double wsaGrossWeight) {
		this.wsaGrossWeight = wsaGrossWeight;
	}

	public Double getWsaNetWeight() {
		return wsaNetWeight;
	}

	public void setWsaNetWeight(Double wsaNetWeight) {
		this.wsaNetWeight = wsaNetWeight;
	}

	public Double getWsaCubic() {
		return wsaCubic;
	}

	public void setWsaCubic(Double wsaCubic) {
		this.wsaCubic = wsaCubic;
	}

    public List<String> getAllocIds() {
        return allocIds;
    }

    public void setAllocIds(List<String> allocIds) {
        this.allocIds = allocIds;
    }

    public List<String> getSoNos() {
        return soNos;
    }

    public void setSoNos(List<String> soNos) {
        this.soNos = soNos;
    }

    public List<String> getToIds() {
        return toIds;
    }

    public void setToIds(List<String> toIds) {
        this.toIds = toIds;
    }

    public String getIsSerial() {
        return isSerial;
    }

    public void setIsSerial(String isSerial) {
        this.isSerial = isSerial;
    }

    public String getAsnNo() {
        return asnNo;
    }

    public void setAsnNo(String asnNo) {
        this.asnNo = asnNo;
    }

    public String getRcvLineNo() {
        return rcvLineNo;
    }

    public void setRcvLineNo(String rcvLineNo) {
        this.rcvLineNo = rcvLineNo;
    }

    public String getIsFullDelivery() {
        return isFullDelivery;
    }

    public void setIsFullDelivery(String isFullDelivery) {
        this.isFullDelivery = isFullDelivery;
    }

    public List<String> getWaveNos() {
        return waveNos;
    }

    public void setWaveNos(List<String> waveNos) {
        this.waveNos = waveNos;
    }

	public Integer getRowNum() {
		return rowNum;
	}

	public void setRowNum(Integer rowNum) {
		this.rowNum = rowNum;
	}

	public String getSoType() {
		return soType;
	}

	public void setSoType(String soType) {
		this.soType = soType;
	}

	public String getLdStatus() {
		return ldStatus;
	}

	public void setLdStatus(String ldStatus) {
		this.ldStatus = ldStatus;
	}

	public Date getFmEtd() {
		return fmEtd;
	}

	public void setFmEtd(Date fmEtd) {
		this.fmEtd = fmEtd;
	}

	public Date getToEtd() {
		return toEtd;
	}

	public void setToEtd(Date toEtd) {
		this.toEtd = toEtd;
	}

	public List<String> getLdNos() {
		return ldNos;
	}

	public void setLdNos(List<String> ldNos) {
		this.ldNos = ldNos;
	}

	public Double getWsaQtyEa() {
		return wsaQtyEa;
	}

	public void setWsaQtyEa(Double wsaQtyEa) {
		this.wsaQtyEa = wsaQtyEa;
	}

	public String getCarrierCode() {
		return carrierCode;
	}

	public void setCarrierCode(String carrierCode) {
		this.carrierCode = carrierCode;
	}

	public String getConsigneeName() {
		return consigneeName;
	}

	public void setConsigneeName(String consigneeName) {
		this.consigneeName = consigneeName;
	}

	public String getConsigneeTel() {
		return consigneeTel;
	}

	public void setConsigneeTel(String consigneeTel) {
		this.consigneeTel = consigneeTel;
	}

	public String getConsigneeAddr() {
		return consigneeAddr;
	}

	public void setConsigneeAddr(String consigneeAddr) {
		this.consigneeAddr = consigneeAddr;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactTel() {
		return contactTel;
	}

	public void setContactTel(String contactTel) {
		this.contactTel = contactTel;
	}

	public String getContactAddr() {
		return contactAddr;
	}

	public void setContactAddr(String contactAddr) {
		this.contactAddr = contactAddr;
	}

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

	public String getLogisticNo() {
		return logisticNo;
	}

	public void setLogisticNo(String logisticNo) {
		this.logisticNo = logisticNo;
	}

	public Date getFmOrderDate() {
		return fmOrderDate;
	}

	public void setFmOrderDate(Date fmOrderDate) {
		this.fmOrderDate = fmOrderDate;
	}

	public Date getToOrderDate() {
		return toOrderDate;
	}

	public void setToOrderDate(Date toOrderDate) {
		this.toOrderDate = toOrderDate;
	}

	public Date getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}
}