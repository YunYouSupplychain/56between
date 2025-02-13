package com.yunyou.modules.wms.outbound.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.core.persistence.DataEntity;

import java.util.Date;

/**
 * 销售单明细Entity
 * @author WMJ
 * @version 2019-02-14
 */
public class BanQinWmSaleDetail extends DataEntity<BanQinWmSaleDetail> {

	private static final long serialVersionUID = 1L;
	private String saleNo;		// 销售单号
	private String lineNo;		// 行号
	private String logisticNo;		// 物流单号
	private String logisticLineNo;		// 物流单行号
	private String status;		// 状态
	private String ownerCode;		// 货主编码
	private String skuCode;		// 商品编码
	private String packCode;		// 包装编码
	private String uom;		// 包装单位
	private Double qtySaleEa;		// 销售数EA
	private Double qtySoEa;		// 订货数EA
	private Double qtyPreallocEa;		// 预配数EA
	private Double qtyAllocEa;		// 分配数EA
	private Double qtyPkEa;		// 已拣货数EA
	private Double qtyShipEa;		// 已发货数EA
	private Double price;		// 价格
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
    
    private String headId;
	
	public BanQinWmSaleDetail() {
		super();
		this.recVer = 0;
	}

	public BanQinWmSaleDetail(String id){
		super(id);
	}

	@ExcelField(title="销售单号", align=2, sort=2)
	public String getSaleNo() {
		return saleNo;
	}

	public void setSaleNo(String saleNo) {
		this.saleNo = saleNo;
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
	
	@ExcelField(title="状态", dictType="", align=2, sort=6)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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
	
	@ExcelField(title="包装编码", align=2, sort=9)
	public String getPackCode() {
		return packCode;
	}

	public void setPackCode(String packCode) {
		this.packCode = packCode;
	}
	
	@ExcelField(title="包装单位", align=2, sort=10)
	public String getUom() {
		return uom;
	}

	public void setUom(String uom) {
		this.uom = uom;
	}
	
	@ExcelField(title="销售数EA", align=2, sort=11)
	public Double getQtySaleEa() {
		return qtySaleEa;
	}

	public void setQtySaleEa(Double qtySaleEa) {
		this.qtySaleEa = qtySaleEa;
	}
	
	@ExcelField(title="订货数EA", align=2, sort=12)
	public Double getQtySoEa() {
		return qtySoEa;
	}

	public void setQtySoEa(Double qtySoEa) {
		this.qtySoEa = qtySoEa;
	}
	
	@ExcelField(title="预配数EA", align=2, sort=13)
	public Double getQtyPreallocEa() {
		return qtyPreallocEa;
	}

	public void setQtyPreallocEa(Double qtyPreallocEa) {
		this.qtyPreallocEa = qtyPreallocEa;
	}
	
	@ExcelField(title="分配数EA", align=2, sort=14)
	public Double getQtyAllocEa() {
		return qtyAllocEa;
	}

	public void setQtyAllocEa(Double qtyAllocEa) {
		this.qtyAllocEa = qtyAllocEa;
	}
	
	@ExcelField(title="已拣货数EA", align=2, sort=15)
	public Double getQtyPkEa() {
		return qtyPkEa;
	}

	public void setQtyPkEa(Double qtyPkEa) {
		this.qtyPkEa = qtyPkEa;
	}
	
	@ExcelField(title="已发货数EA", align=2, sort=16)
	public Double getQtyShipEa() {
		return qtyShipEa;
	}

	public void setQtyShipEa(Double qtyShipEa) {
		this.qtyShipEa = qtyShipEa;
	}
	
	@ExcelField(title="价格", align=2, sort=17)
	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="批次属性01(生产日期)", align=2, sort=18)
	public Date getLotAtt01() {
		return lotAtt01;
	}

	public void setLotAtt01(Date lotAtt01) {
		this.lotAtt01 = lotAtt01;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="批次属性02(失效日期)", align=2, sort=19)
	public Date getLotAtt02() {
		return lotAtt02;
	}

	public void setLotAtt02(Date lotAtt02) {
		this.lotAtt02 = lotAtt02;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="批次属性03(入库日期)", align=2, sort=20)
	public Date getLotAtt03() {
		return lotAtt03;
	}

	public void setLotAtt03(Date lotAtt03) {
		this.lotAtt03 = lotAtt03;
	}
	
	@ExcelField(title="批次属性04(品质)", dictType="", align=2, sort=21)
	public String getLotAtt04() {
		return lotAtt04;
	}

	public void setLotAtt04(String lotAtt04) {
		this.lotAtt04 = lotAtt04;
	}
	
	@ExcelField(title="批次属性05", align=2, sort=22)
	public String getLotAtt05() {
		return lotAtt05;
	}

	public void setLotAtt05(String lotAtt05) {
		this.lotAtt05 = lotAtt05;
	}
	
	@ExcelField(title="批次属性06", align=2, sort=23)
	public String getLotAtt06() {
		return lotAtt06;
	}

	public void setLotAtt06(String lotAtt06) {
		this.lotAtt06 = lotAtt06;
	}
	
	@ExcelField(title="批次属性07", align=2, sort=24)
	public String getLotAtt07() {
		return lotAtt07;
	}

	public void setLotAtt07(String lotAtt07) {
		this.lotAtt07 = lotAtt07;
	}
	
	@ExcelField(title="批次属性08", align=2, sort=25)
	public String getLotAtt08() {
		return lotAtt08;
	}

	public void setLotAtt08(String lotAtt08) {
		this.lotAtt08 = lotAtt08;
	}
	
	@ExcelField(title="批次属性09", align=2, sort=26)
	public String getLotAtt09() {
		return lotAtt09;
	}

	public void setLotAtt09(String lotAtt09) {
		this.lotAtt09 = lotAtt09;
	}
	
	@ExcelField(title="批次属性10", align=2, sort=27)
	public String getLotAtt10() {
		return lotAtt10;
	}

	public void setLotAtt10(String lotAtt10) {
		this.lotAtt10 = lotAtt10;
	}
	
	@ExcelField(title="批次属性11", align=2, sort=28)
	public String getLotAtt11() {
		return lotAtt11;
	}

	public void setLotAtt11(String lotAtt11) {
		this.lotAtt11 = lotAtt11;
	}
	
	@ExcelField(title="批次属性12", align=2, sort=29)
	public String getLotAtt12() {
		return lotAtt12;
	}

	public void setLotAtt12(String lotAtt12) {
		this.lotAtt12 = lotAtt12;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="EDI发送时间", align=2, sort=30)
	public Date getEdiSendTime() {
		return ediSendTime;
	}

	public void setEdiSendTime(Date ediSendTime) {
		this.ediSendTime = ediSendTime;
	}
	
	@ExcelField(title="EDI是否已发送", dictType="", align=2, sort=31)
	public String getIsEdiSend() {
		return isEdiSend;
	}

	public void setIsEdiSend(String isEdiSend) {
		this.isEdiSend = isEdiSend;
	}
	
	@ExcelField(title="自定义1", align=2, sort=32)
	public String getDef1() {
		return def1;
	}

	public void setDef1(String def1) {
		this.def1 = def1;
	}
	
	@ExcelField(title="自定义2", align=2, sort=33)
	public String getDef2() {
		return def2;
	}

	public void setDef2(String def2) {
		this.def2 = def2;
	}
	
	@ExcelField(title="自定义3", align=2, sort=34)
	public String getDef3() {
		return def3;
	}

	public void setDef3(String def3) {
		this.def3 = def3;
	}
	
	@ExcelField(title="自定义4", align=2, sort=35)
	public String getDef4() {
		return def4;
	}

	public void setDef4(String def4) {
		this.def4 = def4;
	}
	
	@ExcelField(title="自定义5", align=2, sort=36)
	public String getDef5() {
		return def5;
	}

	public void setDef5(String def5) {
		this.def5 = def5;
	}
	
	@ExcelField(title="自定义6", align=2, sort=37)
	public String getDef6() {
		return def6;
	}

	public void setDef6(String def6) {
		this.def6 = def6;
	}
	
	@ExcelField(title="自定义7", align=2, sort=38)
	public String getDef7() {
		return def7;
	}

	public void setDef7(String def7) {
		this.def7 = def7;
	}
	
	@ExcelField(title="自定义8", align=2, sort=39)
	public String getDef8() {
		return def8;
	}

	public void setDef8(String def8) {
		this.def8 = def8;
	}
	
	@ExcelField(title="自定义9", align=2, sort=40)
	public String getDef9() {
		return def9;
	}

	public void setDef9(String def9) {
		this.def9 = def9;
	}
	
	@ExcelField(title="自定义10", align=2, sort=41)
	public String getDef10() {
		return def10;
	}

	public void setDef10(String def10) {
		this.def10 = def10;
	}

	@ExcelField(title="分公司", align=2, sort=50)
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

    public String getHeadId() {
        return headId;
    }

    public void setHeadId(String headId) {
        this.headId = headId;
    }
}