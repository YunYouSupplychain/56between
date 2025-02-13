package com.yunyou.modules.wms.qc.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.core.persistence.DataEntity;

import java.util.Date;

/**
 * 质检单明细Entity
 * @author WMJ
 * @version 2019-01-26
 */
public class BanQinWmQcDetail extends DataEntity<BanQinWmQcDetail> {
	
	private static final long serialVersionUID = 1L;
	private String qcNo;		// 质检单号
	private String lineNo;		// 质检明细行号
	private String qcLineNo;		// 质检单行号
	private String orderNo;		// 单据号
	private String orderLineNo;		// 单据行号
	private String status;		// 状态（创建、完全质检）
	private String ownerCode;		// 货主编码
	private String skuCode;		// 商品编码
	private String lotNum;		// 收货批次号
	private String locCode;		// 收货库位
	private String traceId;		// 收货跟踪号
	private Date qcTime;		// 质检时间
	private String packCode;		// 包装编码
	private String uom;		// 包装单位
	private Double qtyAvailQcEa;		// 可质检数EA
	private String qcRcvId;		// 质检收货明细号
	private String quaLotNum;		// 质检良品批次号
	private String quaLoc;		// 质检良品库位
	private String quaTraceId;		// 质检良品跟踪号
	private String qcQuaRcvId;		// 质检良品收货明细号
	private String quaPaId;		// 良品上架任务ID
	private String unquaLotNum;		// 质检不良品批次号
	private String unquaLoc;		// 质检不良品库位
	private String unquaTraceId;		// 质检不良品跟踪号
	private String qcUnquaRcvId;		// 质检不良品收货明细号
	private String unquaPaId;		// 不良品上架任务ID
	private String qcRule;		// 质检规则
	private String itemGroupCode;		// 质检项组编码
	private Double qtyQuaEa;		// 合格数EA
	private Double qtyUnquaEa;		// 不合格数EA
	private Double qtyQcQuaEa;		// 质检合格数EA
	private Double qtyQcUnquaEa;		// 质检不合格数EA
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
	private String def6;		// 自定义6
	private String def7;		// 自定义7
	private String def8;		// 自定义8
	private String def9;		// 自定义9
	private String def10;		// 自定义10
	private String orgId;		// 分公司
	private String headId;		// 质检单ID
	
	public BanQinWmQcDetail() {
		super();
		this.qtyAvailQcEa = 0d;
		this.qtyQuaEa = 0d;
		this.qtyUnquaEa = 0d;
		this.qtyQcQuaEa = 0d;
		this.qtyQcUnquaEa = 0d;
		this.recVer = 0;
	}

	public BanQinWmQcDetail(String id){
		super(id);
	}

	@ExcelField(title="质检单号", align=2, sort=2)
	public String getQcNo() {
		return qcNo;
	}

	public void setQcNo(String qcNo) {
		this.qcNo = qcNo;
	}
	
	@ExcelField(title="质检明细行号", align=2, sort=3)
	public String getLineNo() {
		return lineNo;
	}

	public void setLineNo(String lineNo) {
		this.lineNo = lineNo;
	}
	
	@ExcelField(title="质检单行号", align=2, sort=4)
	public String getQcLineNo() {
		return qcLineNo;
	}

	public void setQcLineNo(String qcLineNo) {
		this.qcLineNo = qcLineNo;
	}
	
	@ExcelField(title="单据号", align=2, sort=5)
	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	
	@ExcelField(title="单据行号", align=2, sort=6)
	public String getOrderLineNo() {
		return orderLineNo;
	}

	public void setOrderLineNo(String orderLineNo) {
		this.orderLineNo = orderLineNo;
	}
	
	@ExcelField(title="状态（创建、完全质检）", dictType="", align=2, sort=7)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@ExcelField(title="货主编码", align=2, sort=8)
	public String getOwnerCode() {
		return ownerCode;
	}

	public void setOwnerCode(String ownerCode) {
		this.ownerCode = ownerCode;
	}
	
	@ExcelField(title="商品编码", align=2, sort=9)
	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}
	
	@ExcelField(title="收货批次号", align=2, sort=10)
	public String getLotNum() {
		return lotNum;
	}

	public void setLotNum(String lotNum) {
		this.lotNum = lotNum;
	}
	
	@ExcelField(title="收货库位", align=2, sort=11)
	public String getLocCode() {
		return locCode;
	}

	public void setLocCode(String locCode) {
		this.locCode = locCode;
	}
	
	@ExcelField(title="收货跟踪号", align=2, sort=12)
	public String getTraceId() {
		return traceId;
	}

	public void setTraceId(String traceId) {
		this.traceId = traceId;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="质检时间", align=2, sort=13)
	public Date getQcTime() {
		return qcTime;
	}

	public void setQcTime(Date qcTime) {
		this.qcTime = qcTime;
	}
	
	@ExcelField(title="包装编码", align=2, sort=14)
	public String getPackCode() {
		return packCode;
	}

	public void setPackCode(String packCode) {
		this.packCode = packCode;
	}
	
	@ExcelField(title="包装单位", align=2, sort=15)
	public String getUom() {
		return uom;
	}

	public void setUom(String uom) {
		this.uom = uom;
	}
	
	@ExcelField(title="可质检数EA", align=2, sort=16)
	public Double getQtyAvailQcEa() {
		return qtyAvailQcEa;
	}

	public void setQtyAvailQcEa(Double qtyAvailQcEa) {
		this.qtyAvailQcEa = qtyAvailQcEa;
	}
	
	@ExcelField(title="质检收货明细号", align=2, sort=17)
	public String getQcRcvId() {
		return qcRcvId;
	}

	public void setQcRcvId(String qcRcvId) {
		this.qcRcvId = qcRcvId;
	}
	
	@ExcelField(title="质检良品批次号", align=2, sort=18)
	public String getQuaLotNum() {
		return quaLotNum;
	}

	public void setQuaLotNum(String quaLotNum) {
		this.quaLotNum = quaLotNum;
	}
	
	@ExcelField(title="质检良品库位", align=2, sort=19)
	public String getQuaLoc() {
		return quaLoc;
	}

	public void setQuaLoc(String quaLoc) {
		this.quaLoc = quaLoc;
	}
	
	@ExcelField(title="质检良品跟踪号", align=2, sort=20)
	public String getQuaTraceId() {
		return quaTraceId;
	}

	public void setQuaTraceId(String quaTraceId) {
		this.quaTraceId = quaTraceId;
	}
	
	@ExcelField(title="质检良品收货明细号", align=2, sort=21)
	public String getQcQuaRcvId() {
		return qcQuaRcvId;
	}

	public void setQcQuaRcvId(String qcQuaRcvId) {
		this.qcQuaRcvId = qcQuaRcvId;
	}
	
	@ExcelField(title="良品上架任务ID", align=2, sort=22)
	public String getQuaPaId() {
		return quaPaId;
	}

	public void setQuaPaId(String quaPaId) {
		this.quaPaId = quaPaId;
	}
	
	@ExcelField(title="质检不良品批次号", align=2, sort=23)
	public String getUnquaLotNum() {
		return unquaLotNum;
	}

	public void setUnquaLotNum(String unquaLotNum) {
		this.unquaLotNum = unquaLotNum;
	}
	
	@ExcelField(title="质检不良品库位", align=2, sort=24)
	public String getUnquaLoc() {
		return unquaLoc;
	}

	public void setUnquaLoc(String unquaLoc) {
		this.unquaLoc = unquaLoc;
	}
	
	@ExcelField(title="质检不良品跟踪号", align=2, sort=25)
	public String getUnquaTraceId() {
		return unquaTraceId;
	}

	public void setUnquaTraceId(String unquaTraceId) {
		this.unquaTraceId = unquaTraceId;
	}
	
	@ExcelField(title="质检不良品收货明细号", align=2, sort=26)
	public String getQcUnquaRcvId() {
		return qcUnquaRcvId;
	}

	public void setQcUnquaRcvId(String qcUnquaRcvId) {
		this.qcUnquaRcvId = qcUnquaRcvId;
	}
	
	@ExcelField(title="不良品上架任务ID", align=2, sort=27)
	public String getUnquaPaId() {
		return unquaPaId;
	}

	public void setUnquaPaId(String unquaPaId) {
		this.unquaPaId = unquaPaId;
	}
	
	@ExcelField(title="质检规则", align=2, sort=28)
	public String getQcRule() {
		return qcRule;
	}

	public void setQcRule(String qcRule) {
		this.qcRule = qcRule;
	}
	
	@ExcelField(title="质检项组编码", align=2, sort=29)
	public String getItemGroupCode() {
		return itemGroupCode;
	}

	public void setItemGroupCode(String itemGroupCode) {
		this.itemGroupCode = itemGroupCode;
	}
	
	@ExcelField(title="合格数EA", align=2, sort=30)
	public Double getQtyQuaEa() {
		return qtyQuaEa;
	}

	public void setQtyQuaEa(Double qtyQuaEa) {
		this.qtyQuaEa = qtyQuaEa;
	}
	
	@ExcelField(title="不合格数EA", align=2, sort=31)
	public Double getQtyUnquaEa() {
		return qtyUnquaEa;
	}

	public void setQtyUnquaEa(Double qtyUnquaEa) {
		this.qtyUnquaEa = qtyUnquaEa;
	}
	
	@ExcelField(title="质检合格数EA", align=2, sort=32)
	public Double getQtyQcQuaEa() {
		return qtyQcQuaEa;
	}

	public void setQtyQcQuaEa(Double qtyQcQuaEa) {
		this.qtyQcQuaEa = qtyQcQuaEa;
	}
	
	@ExcelField(title="质检不合格数EA", align=2, sort=33)
	public Double getQtyQcUnquaEa() {
		return qtyQcUnquaEa;
	}

	public void setQtyQcUnquaEa(Double qtyQcUnquaEa) {
		this.qtyQcUnquaEa = qtyQcUnquaEa;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	@ExcelField(title="批次属性01(生产日期)", align=2, sort=34)
	public Date getLotAtt01() {
		return lotAtt01;
	}

	public void setLotAtt01(Date lotAtt01) {
		this.lotAtt01 = lotAtt01;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	@ExcelField(title="批次属性02(失效日期)", align=2, sort=35)
	public Date getLotAtt02() {
		return lotAtt02;
	}

	public void setLotAtt02(Date lotAtt02) {
		this.lotAtt02 = lotAtt02;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	@ExcelField(title="批次属性03(入库日期)", align=2, sort=36)
	public Date getLotAtt03() {
		return lotAtt03;
	}

	public void setLotAtt03(Date lotAtt03) {
		this.lotAtt03 = lotAtt03;
	}
	
	@ExcelField(title="批次属性04(品质)", dictType="", align=2, sort=37)
	public String getLotAtt04() {
		return lotAtt04;
	}

	public void setLotAtt04(String lotAtt04) {
		this.lotAtt04 = lotAtt04;
	}
	
	@ExcelField(title="批次属性05", align=2, sort=38)
	public String getLotAtt05() {
		return lotAtt05;
	}

	public void setLotAtt05(String lotAtt05) {
		this.lotAtt05 = lotAtt05;
	}
	
	@ExcelField(title="批次属性06", align=2, sort=39)
	public String getLotAtt06() {
		return lotAtt06;
	}

	public void setLotAtt06(String lotAtt06) {
		this.lotAtt06 = lotAtt06;
	}
	
	@ExcelField(title="批次属性07", align=2, sort=40)
	public String getLotAtt07() {
		return lotAtt07;
	}

	public void setLotAtt07(String lotAtt07) {
		this.lotAtt07 = lotAtt07;
	}
	
	@ExcelField(title="批次属性08", align=2, sort=41)
	public String getLotAtt08() {
		return lotAtt08;
	}

	public void setLotAtt08(String lotAtt08) {
		this.lotAtt08 = lotAtt08;
	}
	
	@ExcelField(title="批次属性09", align=2, sort=42)
	public String getLotAtt09() {
		return lotAtt09;
	}

	public void setLotAtt09(String lotAtt09) {
		this.lotAtt09 = lotAtt09;
	}
	
	@ExcelField(title="批次属性10", align=2, sort=43)
	public String getLotAtt10() {
		return lotAtt10;
	}

	public void setLotAtt10(String lotAtt10) {
		this.lotAtt10 = lotAtt10;
	}
	
	@ExcelField(title="批次属性11", align=2, sort=44)
	public String getLotAtt11() {
		return lotAtt11;
	}

	public void setLotAtt11(String lotAtt11) {
		this.lotAtt11 = lotAtt11;
	}
	
	@ExcelField(title="批次属性12", align=2, sort=45)
	public String getLotAtt12() {
		return lotAtt12;
	}

	public void setLotAtt12(String lotAtt12) {
		this.lotAtt12 = lotAtt12;
	}
	
	@ExcelField(title="自定义1", align=2, sort=46)
	public String getDef1() {
		return def1;
	}

	public void setDef1(String def1) {
		this.def1 = def1;
	}
	
	@ExcelField(title="自定义2", align=2, sort=47)
	public String getDef2() {
		return def2;
	}

	public void setDef2(String def2) {
		this.def2 = def2;
	}
	
	@ExcelField(title="自定义3", align=2, sort=48)
	public String getDef3() {
		return def3;
	}

	public void setDef3(String def3) {
		this.def3 = def3;
	}
	
	@ExcelField(title="自定义4", align=2, sort=49)
	public String getDef4() {
		return def4;
	}

	public void setDef4(String def4) {
		this.def4 = def4;
	}
	
	@ExcelField(title="自定义5", align=2, sort=50)
	public String getDef5() {
		return def5;
	}

	public void setDef5(String def5) {
		this.def5 = def5;
	}
	
	@ExcelField(title="自定义6", align=2, sort=51)
	public String getDef6() {
		return def6;
	}

	public void setDef6(String def6) {
		this.def6 = def6;
	}
	
	@ExcelField(title="自定义7", align=2, sort=52)
	public String getDef7() {
		return def7;
	}

	public void setDef7(String def7) {
		this.def7 = def7;
	}
	
	@ExcelField(title="自定义8", align=2, sort=53)
	public String getDef8() {
		return def8;
	}

	public void setDef8(String def8) {
		this.def8 = def8;
	}
	
	@ExcelField(title="自定义9", align=2, sort=54)
	public String getDef9() {
		return def9;
	}

	public void setDef9(String def9) {
		this.def9 = def9;
	}
	
	@ExcelField(title="自定义10", align=2, sort=55)
	public String getDef10() {
		return def10;
	}

	public void setDef10(String def10) {
		this.def10 = def10;
	}

	@ExcelField(title="分公司", align=2, sort=64)
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
	@ExcelField(title="质检单ID", align=2, sort=65)
	public String getHeadId() {
		return headId;
	}

	public void setHeadId(String headId) {
		this.headId = headId;
	}
	
}