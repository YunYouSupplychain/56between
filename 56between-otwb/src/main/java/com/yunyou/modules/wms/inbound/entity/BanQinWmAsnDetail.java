package com.yunyou.modules.wms.inbound.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.core.persistence.DataEntity;

import java.util.Date;

/**
 * 入库单明细Entity
 * @author WMJ
 * @version 2019-01-25
 */
public class BanQinWmAsnDetail extends DataEntity<BanQinWmAsnDetail> {
	
	private static final long serialVersionUID = 1L;
	private String asnNo;		// 入库单号
	private String lineNo;		// 行号
	private String logisticNo;		// 物流单号
	private String logisticLineNo;		// 物流单行号
	private String poNo;		// 采购单号
	private String poLineNo;		// 采购单行号
	private String status;		// 订单行状态
	private String ownerCode;		// 货主编码
	private String skuCode;		// 商品编码
	private String packCode;		// 包装编码
	private String uom;		// 包装单位
	private Double qtyAsnEa;		// 预收货数EA
	private Double qtyRcvEa;		// 收货数量EA
	private Double price;		// 单价
	private String planToLoc;		// 计划收货库位
	private String traceId;		// 跟踪号
	private String isPalletize;		// 是否已做码盘
	private String reserveCode;		// 上架库位指定规则
	private String paRule;		// 上架规则
	private String isQc;		// 是否质检管理
	private String qcStatus;		// 质检状态（创建、部分质检、完全质检）
	private String qcPhase;		// 质检阶段
	private String qcRule;		// 质检规则
	private String itemGroupCode;		// 质检项组编码
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
	private Date ediSendTime;		// EDI发送时间
	private String isEdiSend;		// EDI是否已发送
	private String def1;		// 自定义1	// 供应链订单号
	private String def2;		// 自定义2	// 供应链订单行号
	private String def3;		// 自定义3	// 作业任务号
	private String def4;		// 自定义4	// 客户订单号
	private String def5;		// 自定义5	// 数据来源
	private String def6;		// 自定义6
	private String def7;		// 自定义7
	private String def8;		// 自定义8
	private String def9;		// 自定义9
	private String def10;		// 自定义10
	private String orgId;		// 分公司
	private String headId;		// 出库单ID
	private Double totalWeight;	// 总重量
	private Date inboundTime;	// 入库时间
	
	public BanQinWmAsnDetail() {
		super();
		this.traceId = "*";
		this.recVer = 0;
	}

	public BanQinWmAsnDetail(String id){
		super(id);
	}

	@ExcelField(title="入库单号", align=2, sort=2)
	public String getAsnNo() {
		return asnNo;
	}

	public void setAsnNo(String asnNo) {
		this.asnNo = asnNo;
	}
	
	@ExcelField(title="行号", align=2, sort=3)
	public String getLineNo() {
		return lineNo;
	}

	public void setLineNo(String lineNo) {
		this.lineNo = lineNo;
	}
	
	@ExcelField(title="物流单号", align=2, sort=4)
	public String getLogisticNo() {
		return logisticNo;
	}

	public void setLogisticNo(String logisticNo) {
		this.logisticNo = logisticNo;
	}
	
	@ExcelField(title="物流单行号", align=2, sort=5)
	public String getLogisticLineNo() {
		return logisticLineNo;
	}

	public void setLogisticLineNo(String logisticLineNo) {
		this.logisticLineNo = logisticLineNo;
	}
	
	@ExcelField(title="采购单号", align=2, sort=6)
	public String getPoNo() {
		return poNo;
	}

	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}
	
	@ExcelField(title="采购单行号", align=2, sort=7)
	public String getPoLineNo() {
		return poLineNo;
	}

	public void setPoLineNo(String poLineNo) {
		this.poLineNo = poLineNo;
	}
	
	@ExcelField(title="订单行状态", dictType="", align=2, sort=8)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@ExcelField(title="货主编码", align=2, sort=9)
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
	
	@ExcelField(title="预收货数EA", align=2, sort=13)
	public Double getQtyAsnEa() {
		return qtyAsnEa;
	}

	public void setQtyAsnEa(Double qtyAsnEa) {
		this.qtyAsnEa = qtyAsnEa;
	}
	
	@ExcelField(title="收货数量EA", align=2, sort=14)
	public Double getQtyRcvEa() {
		return qtyRcvEa;
	}

	public void setQtyRcvEa(Double qtyRcvEa) {
		this.qtyRcvEa = qtyRcvEa;
	}
	
	@ExcelField(title="单价", align=2, sort=15)
	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}
	
	@ExcelField(title="计划收货库位", align=2, sort=16)
	public String getPlanToLoc() {
		return planToLoc;
	}

	public void setPlanToLoc(String planToLoc) {
		this.planToLoc = planToLoc;
	}
	
	@ExcelField(title="跟踪号", align=2, sort=17)
	public String getTraceId() {
		return traceId;
	}

	public void setTraceId(String traceId) {
		this.traceId = traceId;
	}
	
	@ExcelField(title="是否已做码盘", dictType="", align=2, sort=18)
	public String getIsPalletize() {
		return isPalletize;
	}

	public void setIsPalletize(String isPalletize) {
		this.isPalletize = isPalletize;
	}
	
	@ExcelField(title="上架库位指定规则", align=2, sort=19)
	public String getReserveCode() {
		return reserveCode;
	}

	public void setReserveCode(String reserveCode) {
		this.reserveCode = reserveCode;
	}
	
	@ExcelField(title="上架规则", align=2, sort=20)
	public String getPaRule() {
		return paRule;
	}

	public void setPaRule(String paRule) {
		this.paRule = paRule;
	}
	
	@ExcelField(title="是否质检管理", dictType="", align=2, sort=21)
	public String getIsQc() {
		return isQc;
	}

	public void setIsQc(String isQc) {
		this.isQc = isQc;
	}
	
	@ExcelField(title="质检状态（创建、部分质检、完全质检）", dictType="", align=2, sort=22)
	public String getQcStatus() {
		return qcStatus;
	}

	public void setQcStatus(String qcStatus) {
		this.qcStatus = qcStatus;
	}
	
	@ExcelField(title="质检阶段", dictType="", align=2, sort=23)
	public String getQcPhase() {
		return qcPhase;
	}

	public void setQcPhase(String qcPhase) {
		this.qcPhase = qcPhase;
	}
	
	@ExcelField(title="质检规则", align=2, sort=24)
	public String getQcRule() {
		return qcRule;
	}

	public void setQcRule(String qcRule) {
		this.qcRule = qcRule;
	}
	
	@ExcelField(title="质检项组编码", align=2, sort=25)
	public String getItemGroupCode() {
		return itemGroupCode;
	}

	public void setItemGroupCode(String itemGroupCode) {
		this.itemGroupCode = itemGroupCode;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	@ExcelField(title="批次属性01(生产日期)", align=2, sort=26)
	public Date getLotAtt01() {
		return lotAtt01;
	}

	public void setLotAtt01(Date lotAtt01) {
		this.lotAtt01 = lotAtt01;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	@ExcelField(title="批次属性02(失效日期)", align=2, sort=27)
	public Date getLotAtt02() {
		return lotAtt02;
	}

	public void setLotAtt02(Date lotAtt02) {
		this.lotAtt02 = lotAtt02;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	@ExcelField(title="批次属性03(入库日期)", align=2, sort=28)
	public Date getLotAtt03() {
		return lotAtt03;
	}

	public void setLotAtt03(Date lotAtt03) {
		this.lotAtt03 = lotAtt03;
	}
	
	@ExcelField(title="批次属性04(品质)", dictType="", align=2, sort=29)
	public String getLotAtt04() {
		return lotAtt04;
	}

	public void setLotAtt04(String lotAtt04) {
		this.lotAtt04 = lotAtt04;
	}
	
	@ExcelField(title="批次属性05", align=2, sort=30)
	public String getLotAtt05() {
		return lotAtt05;
	}

	public void setLotAtt05(String lotAtt05) {
		this.lotAtt05 = lotAtt05;
	}
	
	@ExcelField(title="批次属性06", align=2, sort=31)
	public String getLotAtt06() {
		return lotAtt06;
	}

	public void setLotAtt06(String lotAtt06) {
		this.lotAtt06 = lotAtt06;
	}
	
	@ExcelField(title="批次属性07", align=2, sort=32)
	public String getLotAtt07() {
		return lotAtt07;
	}

	public void setLotAtt07(String lotAtt07) {
		this.lotAtt07 = lotAtt07;
	}
	
	@ExcelField(title="批次属性08", align=2, sort=33)
	public String getLotAtt08() {
		return lotAtt08;
	}

	public void setLotAtt08(String lotAtt08) {
		this.lotAtt08 = lotAtt08;
	}
	
	@ExcelField(title="批次属性09", align=2, sort=34)
	public String getLotAtt09() {
		return lotAtt09;
	}

	public void setLotAtt09(String lotAtt09) {
		this.lotAtt09 = lotAtt09;
	}
	
	@ExcelField(title="批次属性10", align=2, sort=35)
	public String getLotAtt10() {
		return lotAtt10;
	}

	public void setLotAtt10(String lotAtt10) {
		this.lotAtt10 = lotAtt10;
	}
	
	@ExcelField(title="批次属性11", align=2, sort=36)
	public String getLotAtt11() {
		return lotAtt11;
	}

	public void setLotAtt11(String lotAtt11) {
		this.lotAtt11 = lotAtt11;
	}
	
	@ExcelField(title="批次属性12", align=2, sort=37)
	public String getLotAtt12() {
		return lotAtt12;
	}

	public void setLotAtt12(String lotAtt12) {
		this.lotAtt12 = lotAtt12;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="EDI发送时间", align=2, sort=38)
	public Date getEdiSendTime() {
		return ediSendTime;
	}

	public void setEdiSendTime(Date ediSendTime) {
		this.ediSendTime = ediSendTime;
	}
	
	@ExcelField(title="EDI是否已发送", dictType="", align=2, sort=39)
	public String getIsEdiSend() {
		return isEdiSend;
	}

	public void setIsEdiSend(String isEdiSend) {
		this.isEdiSend = isEdiSend;
	}
	
	@ExcelField(title="自定义1", align=2, sort=40)
	public String getDef1() {
		return def1;
	}

	public void setDef1(String def1) {
		this.def1 = def1;
	}
	
	@ExcelField(title="自定义2", align=2, sort=41)
	public String getDef2() {
		return def2;
	}

	public void setDef2(String def2) {
		this.def2 = def2;
	}
	
	@ExcelField(title="自定义3", align=2, sort=42)
	public String getDef3() {
		return def3;
	}

	public void setDef3(String def3) {
		this.def3 = def3;
	}
	
	@ExcelField(title="自定义4", align=2, sort=43)
	public String getDef4() {
		return def4;
	}

	public void setDef4(String def4) {
		this.def4 = def4;
	}
	
	@ExcelField(title="自定义5", align=2, sort=44)
	public String getDef5() {
		return def5;
	}

	public void setDef5(String def5) {
		this.def5 = def5;
	}
	
	@ExcelField(title="自定义6", align=2, sort=45)
	public String getDef6() {
		return def6;
	}

	public void setDef6(String def6) {
		this.def6 = def6;
	}
	
	@ExcelField(title="自定义7", align=2, sort=46)
	public String getDef7() {
		return def7;
	}

	public void setDef7(String def7) {
		this.def7 = def7;
	}
	
	@ExcelField(title="自定义8", align=2, sort=47)
	public String getDef8() {
		return def8;
	}

	public void setDef8(String def8) {
		this.def8 = def8;
	}
	
	@ExcelField(title="自定义9", align=2, sort=48)
	public String getDef9() {
		return def9;
	}

	public void setDef9(String def9) {
		this.def9 = def9;
	}
	
	@ExcelField(title="自定义10", align=2, sort=49)
	public String getDef10() {
		return def10;
	}

	public void setDef10(String def10) {
		this.def10 = def10;
	}

	@ExcelField(title="分公司", align=2, sort=58)
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
	@ExcelField(title="出库单ID", align=2, sort=59)
	public String getHeadId() {
		return headId;
	}

	public void setHeadId(String headId) {
		this.headId = headId;
	}

	public Double getTotalWeight() {
		return totalWeight;
	}

	public void setTotalWeight(Double totalWeight) {
		this.totalWeight = totalWeight;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getInboundTime() {
		return inboundTime;
	}

	public void setInboundTime(Date inboundTime) {
		this.inboundTime = inboundTime;
	}
}