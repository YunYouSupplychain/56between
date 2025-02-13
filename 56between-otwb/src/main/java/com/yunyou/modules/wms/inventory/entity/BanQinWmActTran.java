package com.yunyou.modules.wms.inventory.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.core.persistence.DataEntity;

import java.util.Date;

/**
 * 库存交易Entity
 * @author WMJ
 * @version 2019-01-26
 */
public class BanQinWmActTran extends DataEntity<BanQinWmActTran> {
	
	private static final long serialVersionUID = 1L;
	private String tranId;		// 交易ID
	private String tranType;		// 交易类型
	private String orderType;		// 单据类型
	private String orderNo;		// 单据号
	private String lineNo;		// 行号
	private String tranOp;		// 操作人
	private Date tranTime;		// 交易时间
	private String fmOwner;		// 源货主编码
    private String fmSku;		// 源商品编码
	private String fmLot;		// 源批次号
	private String fmLoc;		// 源库位编码
	private String fmId;		// 源跟踪号
	private String fmPack;		// 源包装编码
	private String fmUom;		// 源单位
	private Double fmQtyUomOp;		// 源操作数量（增量）
	private Double fmQtyEaOp;		// 源操作数量EA（增量）
	private Double fmQtyEaBefore;		// 源操作前 库存数
	private Double fmQtyEaAfter;		// 源操作后 库存数
	private String toOwner;		// 目标货主编码
	private String toSku;		// 目标商品编码
	private String toLot;		// 目标批次号
	private String toLoc;		// 目标库位编码
	private String toId;		// 目标跟踪号
	private String toPack;		// 目标包装编码
	private String toUom;		// 目标单位
	private Double toQtyUomOp;		// 目标操作数量（增量）
	private Double toQtyEaOp;		// 目标操作数量EA（增量）
	private Double toQtyEaBefore;		// 目标操作前 库存数
	private Double toQtyEaAfter;		// 目标操作后 库存数
	private String taskId;		// 任务ID
	private String taskLineNo;		// 任务行号
	private String orgId;		// 分公司
    private String fmOwnerName; // 源货主名称
    private String fmSkuName;	// 源商品名称
    private Date lotAtt01;
    private Date lotAtt02;
    private Date lotAtt03;
    private String lotAtt04;
    private String lotAtt05;
    private String lotAtt06;
    private String lotAtt07;
    private String lotAtt08;
    private String lotAtt09;
    private String lotAtt10;
    private String lotAtt11;
    private String lotAtt12;
    private String toOwnerName;	// 目标货主名称
    private String toSkuName;	// 目标商品名称
    private Date tlotAtt01;
    private Date tlotAtt02;
    private Date tlotAtt03;
    private String tlotAtt04;
    private String tlotAtt05;
    private String tlotAtt06;
    private String tlotAtt07;
    private String tlotAtt08;
    private String tlotAtt09;
    private String tlotAtt10;
    private String tlotAtt11;
    private String tlotAtt12;
    private Date tranTimeFrom;
    private Date tranTimeTo;
    private String fmToOwner;
    private String fmToSku;
    private String fmToLot;
    private String fmToLoc;
    private String fmToId;
    private String orgName;
    private String fmToOwnerName;
    private String fmToSkuName;
	
	public BanQinWmActTran() {
		super();
		this.recVer = 0;
	}

	public BanQinWmActTran(String id){
		super(id);
	}

	@ExcelField(title="交易ID", align=2, sort=2)
	public String getTranId() {
		return tranId;
	}

	public void setTranId(String tranId) {
		this.tranId = tranId;
	}
	
	@ExcelField(title="交易类型", align=2, sort=3)
	public String getTranType() {
		return tranType;
	}

	public void setTranType(String tranType) {
		this.tranType = tranType;
	}
	
	@ExcelField(title="单据类型", align=2, sort=4)
	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	
	@ExcelField(title="单据号", align=2, sort=5)
	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	
	@ExcelField(title="行号", align=2, sort=6)
	public String getLineNo() {
		return lineNo;
	}

	public void setLineNo(String lineNo) {
		this.lineNo = lineNo;
	}
	
	@ExcelField(title="操作人", align=2, sort=7)
	public String getTranOp() {
		return tranOp;
	}

	public void setTranOp(String tranOp) {
		this.tranOp = tranOp;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="交易时间", align=2, sort=8)
	public Date getTranTime() {
		return tranTime;
	}

	public void setTranTime(Date tranTime) {
		this.tranTime = tranTime;
	}
	
	@ExcelField(title="源货主编码", align=2, sort=9)
	public String getFmOwner() {
		return fmOwner;
	}

	public void setFmOwner(String fmOwner) {
		this.fmOwner = fmOwner;
	}
	
	@ExcelField(title="源商品编码", align=2, sort=10)
	public String getFmSku() {
		return fmSku;
	}

	public void setFmSku(String fmSku) {
		this.fmSku = fmSku;
	}
	
	@ExcelField(title="源批次号", align=2, sort=11)
	public String getFmLot() {
		return fmLot;
	}

	public void setFmLot(String fmLot) {
		this.fmLot = fmLot;
	}
	
	@ExcelField(title="源库位编码", align=2, sort=12)
	public String getFmLoc() {
		return fmLoc;
	}

	public void setFmLoc(String fmLoc) {
		this.fmLoc = fmLoc;
	}
	
	@ExcelField(title="源跟踪号", align=2, sort=13)
	public String getFmId() {
		return fmId;
	}

	public void setFmId(String fmId) {
		this.fmId = fmId;
	}
	
	@ExcelField(title="源包装编码", align=2, sort=14)
	public String getFmPack() {
		return fmPack;
	}

	public void setFmPack(String fmPack) {
		this.fmPack = fmPack;
	}
	
	@ExcelField(title="源单位", align=2, sort=15)
	public String getFmUom() {
		return fmUom;
	}

	public void setFmUom(String fmUom) {
		this.fmUom = fmUom;
	}
	
	@ExcelField(title="源操作数量（增量）", align=2, sort=16)
	public Double getFmQtyUomOp() {
		return fmQtyUomOp;
	}

	public void setFmQtyUomOp(Double fmQtyUomOp) {
		this.fmQtyUomOp = fmQtyUomOp;
	}
	
	@ExcelField(title="源操作数量EA（增量）", align=2, sort=17)
	public Double getFmQtyEaOp() {
		return fmQtyEaOp;
	}

	public void setFmQtyEaOp(Double fmQtyEaOp) {
		this.fmQtyEaOp = fmQtyEaOp;
	}
	
	@ExcelField(title="源操作前 库存数", align=2, sort=18)
	public Double getFmQtyEaBefore() {
		return fmQtyEaBefore;
	}

	public void setFmQtyEaBefore(Double fmQtyEaBefore) {
		this.fmQtyEaBefore = fmQtyEaBefore;
	}
	
	@ExcelField(title="源操作后 库存数", align=2, sort=19)
	public Double getFmQtyEaAfter() {
		return fmQtyEaAfter;
	}

	public void setFmQtyEaAfter(Double fmQtyEaAfter) {
		this.fmQtyEaAfter = fmQtyEaAfter;
	}
	
	@ExcelField(title="目标货主编码", align=2, sort=20)
	public String getToOwner() {
		return toOwner;
	}

	public void setToOwner(String toOwner) {
		this.toOwner = toOwner;
	}
	
	@ExcelField(title="目标商品编码", align=2, sort=21)
	public String getToSku() {
		return toSku;
	}

	public void setToSku(String toSku) {
		this.toSku = toSku;
	}
	
	@ExcelField(title="目标批次号", align=2, sort=22)
	public String getToLot() {
		return toLot;
	}

	public void setToLot(String toLot) {
		this.toLot = toLot;
	}
	
	@ExcelField(title="目标库位编码", align=2, sort=23)
	public String getToLoc() {
		return toLoc;
	}

	public void setToLoc(String toLoc) {
		this.toLoc = toLoc;
	}
	
	@ExcelField(title="目标跟踪号", align=2, sort=24)
	public String getToId() {
		return toId;
	}

	public void setToId(String toId) {
		this.toId = toId;
	}
	
	@ExcelField(title="目标包装编码", align=2, sort=25)
	public String getToPack() {
		return toPack;
	}

	public void setToPack(String toPack) {
		this.toPack = toPack;
	}
	
	@ExcelField(title="目标单位", align=2, sort=26)
	public String getToUom() {
		return toUom;
	}

	public void setToUom(String toUom) {
		this.toUom = toUom;
	}
	
	@ExcelField(title="目标操作数量（增量）", align=2, sort=27)
	public Double getToQtyUomOp() {
		return toQtyUomOp;
	}

	public void setToQtyUomOp(Double toQtyUomOp) {
		this.toQtyUomOp = toQtyUomOp;
	}
	
	@ExcelField(title="目标操作数量EA（增量）", align=2, sort=28)
	public Double getToQtyEaOp() {
		return toQtyEaOp;
	}

	public void setToQtyEaOp(Double toQtyEaOp) {
		this.toQtyEaOp = toQtyEaOp;
	}
	
	@ExcelField(title="目标操作前 库存数", align=2, sort=29)
	public Double getToQtyEaBefore() {
		return toQtyEaBefore;
	}

	public void setToQtyEaBefore(Double toQtyEaBefore) {
		this.toQtyEaBefore = toQtyEaBefore;
	}
	
	@ExcelField(title="目标操作后 库存数", align=2, sort=30)
	public Double getToQtyEaAfter() {
		return toQtyEaAfter;
	}

	public void setToQtyEaAfter(Double toQtyEaAfter) {
		this.toQtyEaAfter = toQtyEaAfter;
	}
	
	@ExcelField(title="任务ID", align=2, sort=31)
	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	
	@ExcelField(title="任务行号", align=2, sort=32)
	public String getTaskLineNo() {
		return taskLineNo;
	}

	public void setTaskLineNo(String taskLineNo) {
		this.taskLineNo = taskLineNo;
	}

	@ExcelField(title="分公司", align=2, sort=40)
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

    public String getFmOwnerName() {
        return fmOwnerName;
    }

    public void setFmOwnerName(String fmOwnerName) {
        this.fmOwnerName = fmOwnerName;
    }

    public String getFmSkuName() {
        return fmSkuName;
    }

    public void setFmSkuName(String fmSkuName) {
        this.fmSkuName = fmSkuName;
    }

	@JsonFormat(pattern = "yyyy-MM-dd")
    public Date getLotAtt01() {
        return lotAtt01;
    }

    public void setLotAtt01(Date lotAtt01) {
        this.lotAtt01 = lotAtt01;
    }

	@JsonFormat(pattern = "yyyy-MM-dd")
    public Date getLotAtt02() {
        return lotAtt02;
    }

    public void setLotAtt02(Date lotAtt02) {
        this.lotAtt02 = lotAtt02;
    }

	@JsonFormat(pattern = "yyyy-MM-dd")
    public Date getLotAtt03() {
        return lotAtt03;
    }

    public void setLotAtt03(Date lotAtt03) {
        this.lotAtt03 = lotAtt03;
    }

    public String getLotAtt04() {
        return lotAtt04;
    }

    public void setLotAtt04(String lotAtt04) {
        this.lotAtt04 = lotAtt04;
    }

    public String getLotAtt05() {
        return lotAtt05;
    }

    public void setLotAtt05(String lotAtt05) {
        this.lotAtt05 = lotAtt05;
    }

    public String getLotAtt06() {
        return lotAtt06;
    }

    public void setLotAtt06(String lotAtt06) {
        this.lotAtt06 = lotAtt06;
    }

    public String getLotAtt07() {
        return lotAtt07;
    }

    public void setLotAtt07(String lotAtt07) {
        this.lotAtt07 = lotAtt07;
    }

    public String getLotAtt08() {
        return lotAtt08;
    }

    public void setLotAtt08(String lotAtt08) {
        this.lotAtt08 = lotAtt08;
    }

    public String getLotAtt09() {
        return lotAtt09;
    }

    public void setLotAtt09(String lotAtt09) {
        this.lotAtt09 = lotAtt09;
    }

    public String getLotAtt10() {
        return lotAtt10;
    }

    public void setLotAtt10(String lotAtt10) {
        this.lotAtt10 = lotAtt10;
    }

    public String getLotAtt11() {
        return lotAtt11;
    }

    public void setLotAtt11(String lotAtt11) {
        this.lotAtt11 = lotAtt11;
    }

    public String getLotAtt12() {
        return lotAtt12;
    }

    public void setLotAtt12(String lotAtt12) {
        this.lotAtt12 = lotAtt12;
    }

    public String getToOwnerName() {
        return toOwnerName;
    }

    public void setToOwnerName(String toOwnerName) {
        this.toOwnerName = toOwnerName;
    }

    public String getToSkuName() {
        return toSkuName;
    }

    public void setToSkuName(String toSkuName) {
        this.toSkuName = toSkuName;
    }

	@JsonFormat(pattern = "yyyy-MM-dd")
    public Date getTlotAtt01() {
        return tlotAtt01;
    }

    public void setTlotAtt01(Date tlotAtt01) {
        this.tlotAtt01 = tlotAtt01;
    }

	@JsonFormat(pattern = "yyyy-MM-dd")
    public Date getTlotAtt02() {
        return tlotAtt02;
    }

    public void setTlotAtt02(Date tlotAtt02) {
        this.tlotAtt02 = tlotAtt02;
    }

	@JsonFormat(pattern = "yyyy-MM-dd")
    public Date getTlotAtt03() {
        return tlotAtt03;
    }

    public void setTlotAtt03(Date tlotAtt03) {
        this.tlotAtt03 = tlotAtt03;
    }

    public String getTlotAtt04() {
        return tlotAtt04;
    }

    public void setTlotAtt04(String tlotAtt04) {
        this.tlotAtt04 = tlotAtt04;
    }

    public String getTlotAtt05() {
        return tlotAtt05;
    }

    public void setTlotAtt05(String tlotAtt05) {
        this.tlotAtt05 = tlotAtt05;
    }

    public String getTlotAtt06() {
        return tlotAtt06;
    }

    public void setTlotAtt06(String tlotAtt06) {
        this.tlotAtt06 = tlotAtt06;
    }

    public String getTlotAtt07() {
        return tlotAtt07;
    }

    public void setTlotAtt07(String tlotAtt07) {
        this.tlotAtt07 = tlotAtt07;
    }

    public String getTlotAtt08() {
        return tlotAtt08;
    }

    public void setTlotAtt08(String tlotAtt08) {
        this.tlotAtt08 = tlotAtt08;
    }

    public String getTlotAtt09() {
        return tlotAtt09;
    }

    public void setTlotAtt09(String tlotAtt09) {
        this.tlotAtt09 = tlotAtt09;
    }

    public String getTlotAtt10() {
        return tlotAtt10;
    }

    public void setTlotAtt10(String tlotAtt10) {
        this.tlotAtt10 = tlotAtt10;
    }

    public String getTlotAtt11() {
        return tlotAtt11;
    }

    public void setTlotAtt11(String tlotAtt11) {
        this.tlotAtt11 = tlotAtt11;
    }

    public String getTlotAtt12() {
        return tlotAtt12;
    }

    public void setTlotAtt12(String tlotAtt12) {
        this.tlotAtt12 = tlotAtt12;
    }

    public Date getTranTimeFrom() {
        return tranTimeFrom;
    }

    public void setTranTimeFrom(Date tranTimeFrom) {
        this.tranTimeFrom = tranTimeFrom;
    }

    public Date getTranTimeTo() {
        return tranTimeTo;
    }

    public void setTranTimeTo(Date tranTimeTo) {
        this.tranTimeTo = tranTimeTo;
    }

	public String getFmToOwner() {
		return fmToOwner;
	}

	public void setFmToOwner(String fmToOwner) {
		this.fmToOwner = fmToOwner;
	}

	public String getFmToSku() {
		return fmToSku;
	}

	public void setFmToSku(String fmToSku) {
		this.fmToSku = fmToSku;
	}

	public String getFmToLot() {
		return fmToLot;
	}

	public void setFmToLot(String fmToLot) {
		this.fmToLot = fmToLot;
	}

	public String getFmToLoc() {
		return fmToLoc;
	}

	public void setFmToLoc(String fmToLoc) {
		this.fmToLoc = fmToLoc;
	}

	public String getFmToId() {
		return fmToId;
	}

	public void setFmToId(String fmToId) {
		this.fmToId = fmToId;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getFmToOwnerName() {
		return fmToOwnerName;
	}

	public void setFmToOwnerName(String fmToOwnerName) {
		this.fmToOwnerName = fmToOwnerName;
	}

	public String getFmToSkuName() {
		return fmToSkuName;
	}

	public void setFmToSkuName(String fmToSkuName) {
		this.fmToSkuName = fmToSkuName;
	}
}