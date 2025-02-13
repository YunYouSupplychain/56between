package com.yunyou.modules.wms.inventory.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.core.persistence.DataEntity;

import java.util.Date;

/**
 * 盘点任务Entity
 * @author WMJ
 * @version 2019-01-28
 */
public class BanQinWmTaskCount extends DataEntity<BanQinWmTaskCount> {
	
	private static final long serialVersionUID = 1L;
	private String countId;		// 盘点任务ID
	private String countNo;		// 盘点单号
	private String status;		// 盘点状态
	private String countMethod;		// 盘点方法（静态盘点、动态盘点）
	private String countMode;		// 盘点方式（明盘、盲盘）
	private String areaCode;		// 区域编码
	private String zoneCode;		// 库区编码
	private String countOp;		// 盘点操作人
	private String ownerCode;		// 货主编码
	private String skuCode;		// 商品编码
	private String lotNum;		// 批次号
	private String locCode;		// 库位编码
	private String traceId;		// 跟踪号
	private Double qty;		// 库存数
	private String packCode;		// 包装编码
	private String uom;		// 包装单位
	private Double qtyCountUom;		// 盘点库存
	private Double qtyCountEa;		// 盘点库存EA
	private Double qtyDiff;		// 盘点差异数
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
	private String dataSource;		// 数据来源（MANUAL手工输入、SYSTEM系统生成）
	private String orgId;		// 分公司
    private String headerId;    // 表头Id
    
    private String isSerial;    // 仅用作查询
	
	public BanQinWmTaskCount() {
		super();
		this.recVer = 0;
	}

	public BanQinWmTaskCount(String id){
		super(id);
	}

    public BanQinWmTaskCount(String id, String headerId){
        super(id);
        this.headerId = headerId;
    }

	@ExcelField(title="盘点任务ID", align=2, sort=2)
	public String getCountId() {
		return countId;
	}

	public void setCountId(String countId) {
		this.countId = countId;
	}
	
	@ExcelField(title="盘点单号", align=2, sort=3)
	public String getCountNo() {
		return countNo;
	}

	public void setCountNo(String countNo) {
		this.countNo = countNo;
	}
	
	@ExcelField(title="盘点状态", align=2, sort=4)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@ExcelField(title="盘点方法（静态盘点、动态盘点）", align=2, sort=5)
	public String getCountMethod() {
		return countMethod;
	}

	public void setCountMethod(String countMethod) {
		this.countMethod = countMethod;
	}
	
	@ExcelField(title="盘点方式（明盘、盲盘）", align=2, sort=6)
	public String getCountMode() {
		return countMode;
	}

	public void setCountMode(String countMode) {
		this.countMode = countMode;
	}
	
	@ExcelField(title="区域编码", align=2, sort=7)
	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	
	@ExcelField(title="库区编码", align=2, sort=8)
	public String getZoneCode() {
		return zoneCode;
	}

	public void setZoneCode(String zoneCode) {
		this.zoneCode = zoneCode;
	}
	
	@ExcelField(title="盘点操作人", align=2, sort=9)
	public String getCountOp() {
		return countOp;
	}

	public void setCountOp(String countOp) {
		this.countOp = countOp;
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
	
	@ExcelField(title="库存数", align=2, sort=15)
	public Double getQty() {
		return qty;
	}

	public void setQty(Double qty) {
		this.qty = qty;
	}
	
	@ExcelField(title="包装编码", align=2, sort=16)
	public String getPackCode() {
		return packCode;
	}

	public void setPackCode(String packCode) {
		this.packCode = packCode;
	}
	
	@ExcelField(title="包装单位", align=2, sort=17)
	public String getUom() {
		return uom;
	}

	public void setUom(String uom) {
		this.uom = uom;
	}
	
	@ExcelField(title="盘点库存", align=2, sort=18)
	public Double getQtyCountUom() {
		return qtyCountUom;
	}

	public void setQtyCountUom(Double qtyCountUom) {
		this.qtyCountUom = qtyCountUom;
	}
	
	@ExcelField(title="盘点库存EA", align=2, sort=19)
	public Double getQtyCountEa() {
		return qtyCountEa;
	}

	public void setQtyCountEa(Double qtyCountEa) {
		this.qtyCountEa = qtyCountEa;
	}
	
	@ExcelField(title="盘点差异数", align=2, sort=20)
	public Double getQtyDiff() {
		return qtyDiff;
	}

	public void setQtyDiff(Double qtyDiff) {
		this.qtyDiff = qtyDiff;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	@ExcelField(title="批次属性01(生产日期)", align=2, sort=21)
	public Date getLotAtt01() {
		return lotAtt01;
	}

	public void setLotAtt01(Date lotAtt01) {
		this.lotAtt01 = lotAtt01;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	@ExcelField(title="批次属性02(失效日期)", align=2, sort=22)
	public Date getLotAtt02() {
		return lotAtt02;
	}

	public void setLotAtt02(Date lotAtt02) {
		this.lotAtt02 = lotAtt02;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	@ExcelField(title="批次属性03(入库日期)", align=2, sort=23)
	public Date getLotAtt03() {
		return lotAtt03;
	}

	public void setLotAtt03(Date lotAtt03) {
		this.lotAtt03 = lotAtt03;
	}
	
	@ExcelField(title="批次属性04(品质)", align=2, sort=24)
	public String getLotAtt04() {
		return lotAtt04;
	}

	public void setLotAtt04(String lotAtt04) {
		this.lotAtt04 = lotAtt04;
	}
	
	@ExcelField(title="批次属性05", align=2, sort=25)
	public String getLotAtt05() {
		return lotAtt05;
	}

	public void setLotAtt05(String lotAtt05) {
		this.lotAtt05 = lotAtt05;
	}
	
	@ExcelField(title="批次属性06", align=2, sort=26)
	public String getLotAtt06() {
		return lotAtt06;
	}

	public void setLotAtt06(String lotAtt06) {
		this.lotAtt06 = lotAtt06;
	}
	
	@ExcelField(title="批次属性07", align=2, sort=27)
	public String getLotAtt07() {
		return lotAtt07;
	}

	public void setLotAtt07(String lotAtt07) {
		this.lotAtt07 = lotAtt07;
	}
	
	@ExcelField(title="批次属性08", align=2, sort=28)
	public String getLotAtt08() {
		return lotAtt08;
	}

	public void setLotAtt08(String lotAtt08) {
		this.lotAtt08 = lotAtt08;
	}
	
	@ExcelField(title="批次属性09", align=2, sort=29)
	public String getLotAtt09() {
		return lotAtt09;
	}

	public void setLotAtt09(String lotAtt09) {
		this.lotAtt09 = lotAtt09;
	}
	
	@ExcelField(title="批次属性10", align=2, sort=30)
	public String getLotAtt10() {
		return lotAtt10;
	}

	public void setLotAtt10(String lotAtt10) {
		this.lotAtt10 = lotAtt10;
	}
	
	@ExcelField(title="批次属性11", align=2, sort=31)
	public String getLotAtt11() {
		return lotAtt11;
	}

	public void setLotAtt11(String lotAtt11) {
		this.lotAtt11 = lotAtt11;
	}
	
	@ExcelField(title="批次属性12", align=2, sort=32)
	public String getLotAtt12() {
		return lotAtt12;
	}

	public void setLotAtt12(String lotAtt12) {
		this.lotAtt12 = lotAtt12;
	}
	
	@ExcelField(title="数据来源（MANUAL手工输入、SYSTEM系统生成）", align=2, sort=33)
	public String getDataSource() {
		return dataSource;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	@ExcelField(title="分公司", align=2, sort=41)
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

    public String getIsSerial() {
        return isSerial;
    }

    public void setIsSerial(String isSerial) {
        this.isSerial = isSerial;
    }
}