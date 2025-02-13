package com.yunyou.modules.wms.inventory.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.core.persistence.DataEntity;

import java.util.Date;

/**
 * 调整单明细Entity
 * @author WMJ
 * @version 2019-01-28
 */
public class BanQinWmAdDetail extends DataEntity<BanQinWmAdDetail> {
	
	private static final long serialVersionUID = 1L;
	private String adNo;		// 调整单号
	private String lineNo;		// 行号
	private String status;		// 状态
	private String ownerCode;		// 货主编码
	private String skuCode;		// 商品编码
	private String lotNum;		// 批次号
	private String locCode;		// 库位编码
	private String traceId;		// 跟踪号
	private String adMode;		// 调整方式（A 增加、D减少）
	private String packCode;		// 包装编码
	private String uom;		// 包装单位
	private Double qtyAdUom;		// 调整操作数
	private Double qtyAdEa;		// 调整操作数EA
	private Date ediSendTime;		// EDI发送时间
	private String isEdiSend;		// EDI是否已发送
	private String def1;		// 自定义1
	private String def2;		// 自定义2
	private String def3;		// 自定义3
	private String def4;		// 自定义4
	private String def5;		// 自定义5
	private String orgId;		// 分公司
    private String headerId;  // 表头Id

	public BanQinWmAdDetail() {
		super();
		this.recVer = 0;
	}

	public BanQinWmAdDetail(String id){
		super(id);
	}

	@ExcelField(title="调整单号", align=2, sort=2)
	public String getAdNo() {
		return adNo;
	}

	public void setAdNo(String adNo) {
		this.adNo = adNo;
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
	
	@ExcelField(title="货主编码", align=2, sort=5)
	public String getOwnerCode() {
		return ownerCode;
	}

	public void setOwnerCode(String ownerCode) {
		this.ownerCode = ownerCode;
	}
	
	@ExcelField(title="商品编码", align=2, sort=6)
	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}
	
	@ExcelField(title="批次号", align=2, sort=7)
	public String getLotNum() {
		return lotNum;
	}

	public void setLotNum(String lotNum) {
		this.lotNum = lotNum;
	}
	
	@ExcelField(title="库位编码", align=2, sort=8)
	public String getLocCode() {
		return locCode;
	}

	public void setLocCode(String locCode) {
		this.locCode = locCode;
	}
	
	@ExcelField(title="跟踪号", align=2, sort=9)
	public String getTraceId() {
		return traceId;
	}

	public void setTraceId(String traceId) {
		this.traceId = traceId;
	}
	
	@ExcelField(title="调整方式（A 增加、D减少）", align=2, sort=10)
	public String getAdMode() {
		return adMode;
	}

	public void setAdMode(String adMode) {
		this.adMode = adMode;
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
	
	@ExcelField(title="调整操作数", align=2, sort=13)
	public Double getQtyAdUom() {
		return qtyAdUom;
	}

	public void setQtyAdUom(Double qtyAdUom) {
		this.qtyAdUom = qtyAdUom;
	}
	
	@ExcelField(title="调整操作数EA", align=2, sort=14)
	public Double getQtyAdEa() {
		return qtyAdEa;
	}

	public void setQtyAdEa(Double qtyAdEa) {
		this.qtyAdEa = qtyAdEa;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="EDI发送时间", align=2, sort=15)
	public Date getEdiSendTime() {
		return ediSendTime;
	}

	public void setEdiSendTime(Date ediSendTime) {
		this.ediSendTime = ediSendTime;
	}
	
	@ExcelField(title="EDI是否已发送", align=2, sort=16)
	public String getIsEdiSend() {
		return isEdiSend;
	}

	public void setIsEdiSend(String isEdiSend) {
		this.isEdiSend = isEdiSend;
	}
	
	@ExcelField(title="自定义1", align=2, sort=17)
	public String getDef1() {
		return def1;
	}

	public void setDef1(String def1) {
		this.def1 = def1;
	}
	
	@ExcelField(title="自定义2", align=2, sort=18)
	public String getDef2() {
		return def2;
	}

	public void setDef2(String def2) {
		this.def2 = def2;
	}
	
	@ExcelField(title="自定义3", align=2, sort=19)
	public String getDef3() {
		return def3;
	}

	public void setDef3(String def3) {
		this.def3 = def3;
	}
	
	@ExcelField(title="自定义4", align=2, sort=20)
	public String getDef4() {
		return def4;
	}

	public void setDef4(String def4) {
		this.def4 = def4;
	}
	
	@ExcelField(title="自定义5", align=2, sort=21)
	public String getDef5() {
		return def5;
	}

	public void setDef5(String def5) {
		this.def5 = def5;
	}

	@ExcelField(title="分公司", align=2, sort=30)
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