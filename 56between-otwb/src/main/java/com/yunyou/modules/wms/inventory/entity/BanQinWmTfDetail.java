package com.yunyou.modules.wms.inventory.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.yunyou.core.persistence.DataEntity;
import com.yunyou.common.utils.excel.annotation.ExcelField;

/**
 * 转移单明细Entity
 * @author WMJ
 * @version 2019-01-28
 */
public class BanQinWmTfDetail extends DataEntity<BanQinWmTfDetail> {
	
	private static final long serialVersionUID = 1L;
	private String tfNo;		// 转移单号
	private String lineNo;		// 行号
	private String status;		// 状态
	private String fmOwner;		// 源货主编码
	private String fmSku;		// 源商品编码
	private String fmLot;		// 源批次号
	private String fmLoc;		// 源库位编码
	private String fmId;		// 源跟踪号
	private String fmPack;		// 源包装编码
	private String fmUom;		// 源单位
	private Double fmQtyUom;		// 转出数量
	private Double fmQtyEa;		// 转出数量EA
	private String toOwner;		// 目标货主编码
	private String toSku;		// 目标商品编码
	private String toLot;		// 目标批次号
	private String toLoc;		// 目标库位编码
	private String toId;		// 目标跟踪号
	private String toPack;		// 目标包装编码
	private String toUom;		// 目标单位
	private Double toQtyUom;		// 转入数量
	private Double toQtyEa;		// 转入数量EA
	private Date ediSendTime;		// EDI发送时间
	private String isEdiSend;		// EDI是否已发送
	private String def1;		// 自定义1
	private String def2;		// 自定义2
	private String def3;		// 自定义3
	private String def4;		// 自定义4
	private String def5;		// 自定义5
	private String orgId;		// 分公司
	private String headerId;		// 头表Id
	
	public BanQinWmTfDetail() {
		super();
		this.recVer = 0;
	}

	public BanQinWmTfDetail(String id){
		super(id);
	}

	@ExcelField(title="转移单号", align=2, sort=2)
	public String getTfNo() {
		return tfNo;
	}

	public void setTfNo(String tfNo) {
		this.tfNo = tfNo;
	}
	
	@ExcelField(title="行号", align=2, sort=3)
	public String getLineNo() {
		return lineNo;
	}

	public void setLineNo(String lineNo) {
		this.lineNo = lineNo;
	}
	
	@ExcelField(title="状态", align=2, sort=4)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@ExcelField(title="源货主编码", align=2, sort=5)
	public String getFmOwner() {
		return fmOwner;
	}

	public void setFmOwner(String fmOwner) {
		this.fmOwner = fmOwner;
	}
	
	@ExcelField(title="源商品编码", align=2, sort=6)
	public String getFmSku() {
		return fmSku;
	}

	public void setFmSku(String fmSku) {
		this.fmSku = fmSku;
	}
	
	@ExcelField(title="源批次号", align=2, sort=7)
	public String getFmLot() {
		return fmLot;
	}

	public void setFmLot(String fmLot) {
		this.fmLot = fmLot;
	}
	
	@ExcelField(title="源库位编码", align=2, sort=8)
	public String getFmLoc() {
		return fmLoc;
	}

	public void setFmLoc(String fmLoc) {
		this.fmLoc = fmLoc;
	}
	
	@ExcelField(title="源跟踪号", align=2, sort=9)
	public String getFmId() {
		return fmId;
	}

	public void setFmId(String fmId) {
		this.fmId = fmId;
	}
	
	@ExcelField(title="源包装编码", align=2, sort=10)
	public String getFmPack() {
		return fmPack;
	}

	public void setFmPack(String fmPack) {
		this.fmPack = fmPack;
	}
	
	@ExcelField(title="源单位", align=2, sort=11)
	public String getFmUom() {
		return fmUom;
	}

	public void setFmUom(String fmUom) {
		this.fmUom = fmUom;
	}
	
	@ExcelField(title="转出数量", align=2, sort=12)
	public Double getFmQtyUom() {
		return fmQtyUom;
	}

	public void setFmQtyUom(Double fmQtyUom) {
		this.fmQtyUom = fmQtyUom;
	}
	
	@ExcelField(title="转出数量EA", align=2, sort=13)
	public Double getFmQtyEa() {
		return fmQtyEa;
	}

	public void setFmQtyEa(Double fmQtyEa) {
		this.fmQtyEa = fmQtyEa;
	}
	
	@ExcelField(title="目标货主编码", align=2, sort=14)
	public String getToOwner() {
		return toOwner;
	}

	public void setToOwner(String toOwner) {
		this.toOwner = toOwner;
	}
	
	@ExcelField(title="目标商品编码", align=2, sort=15)
	public String getToSku() {
		return toSku;
	}

	public void setToSku(String toSku) {
		this.toSku = toSku;
	}
	
	@ExcelField(title="目标批次号", align=2, sort=16)
	public String getToLot() {
		return toLot;
	}

	public void setToLot(String toLot) {
		this.toLot = toLot;
	}
	
	@ExcelField(title="目标库位编码", align=2, sort=17)
	public String getToLoc() {
		return toLoc;
	}

	public void setToLoc(String toLoc) {
		this.toLoc = toLoc;
	}
	
	@ExcelField(title="目标跟踪号", align=2, sort=18)
	public String getToId() {
		return toId;
	}

	public void setToId(String toId) {
		this.toId = toId;
	}
	
	@ExcelField(title="目标包装编码", align=2, sort=19)
	public String getToPack() {
		return toPack;
	}

	public void setToPack(String toPack) {
		this.toPack = toPack;
	}
	
	@ExcelField(title="目标单位", align=2, sort=20)
	public String getToUom() {
		return toUom;
	}

	public void setToUom(String toUom) {
		this.toUom = toUom;
	}
	
	@ExcelField(title="转入数量", align=2, sort=21)
	public Double getToQtyUom() {
		return toQtyUom;
	}

	public void setToQtyUom(Double toQtyUom) {
		this.toQtyUom = toQtyUom;
	}
	
	@ExcelField(title="转入数量EA", align=2, sort=22)
	public Double getToQtyEa() {
		return toQtyEa;
	}

	public void setToQtyEa(Double toQtyEa) {
		this.toQtyEa = toQtyEa;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="EDI发送时间", align=2, sort=23)
	public Date getEdiSendTime() {
		return ediSendTime;
	}

	public void setEdiSendTime(Date ediSendTime) {
		this.ediSendTime = ediSendTime;
	}
	
	@ExcelField(title="EDI是否已发送", align=2, sort=24)
	public String getIsEdiSend() {
		return isEdiSend;
	}

	public void setIsEdiSend(String isEdiSend) {
		this.isEdiSend = isEdiSend;
	}
	
	@ExcelField(title="自定义1", align=2, sort=25)
	public String getDef1() {
		return def1;
	}

	public void setDef1(String def1) {
		this.def1 = def1;
	}
	
	@ExcelField(title="自定义2", align=2, sort=26)
	public String getDef2() {
		return def2;
	}

	public void setDef2(String def2) {
		this.def2 = def2;
	}
	
	@ExcelField(title="自定义3", align=2, sort=27)
	public String getDef3() {
		return def3;
	}

	public void setDef3(String def3) {
		this.def3 = def3;
	}
	
	@ExcelField(title="自定义4", align=2, sort=28)
	public String getDef4() {
		return def4;
	}

	public void setDef4(String def4) {
		this.def4 = def4;
	}
	
	@ExcelField(title="自定义5", align=2, sort=29)
	public String getDef5() {
		return def5;
	}

	public void setDef5(String def5) {
		this.def5 = def5;
	}

	@ExcelField(title="分公司", align=2, sort=38)
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
	@ExcelField(title="头表Id", align=2, sort=39)
	public String getHeaderId() {
		return headerId;
	}

	public void setHeaderId(String headerId) {
		this.headerId = headerId;
	}
}