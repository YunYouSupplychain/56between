package com.yunyou.modules.wms.inventory.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.List;

/**
 * 转移单明细entity
 * @author WMJ
 * @version 2019/03/07
 */
public class BanQinWmTfDetailEntity extends BanQinWmTfDetail {
	private String fmOwnerName;
	private String toOwnerName;
	private String fmSkuName;
	private String toSkuName;
	private String fmPackDesc;
	private String fmUomDesc;
	private String fmCdprQuantity;
	private String toPackDesc;
	private String toUomDesc;
	private String toCdprQuantity;
	private Integer availableNum;
	private Double qty;
	private Double qtyHold;
	private Double qtyAlloc;
	private Double qtyPk;
	private Double qtyPaOut;
	private Double qtyRpOut;
	private String isAllowTf;
	private Date fmLotAtt01;
	private Date fmLotAtt02;
	private Date fmLotAtt03;
	private String fmLotAtt04;
	private String fmLotAtt05;
	private String fmLotAtt06;
	private String fmLotAtt07;
	private String fmLotAtt08;
	private String fmLotAtt09;
	private String fmLotAtt10;
	private String fmLotAtt11;
	private String fmLotAtt12;
	// 批次属性1(生产日期)
	private Date lotAtt01;
	// 批次属性2(失效日期)
	private Date lotAtt02;
	// 批次属性3(入库日期)
	private Date lotAtt03;
	// 批次属性4
	private String lotAtt04;
	// 批次属性5
	private String lotAtt05;
	// 批次属性6
	private String lotAtt06;
	// 批次属性7
	private String lotAtt07;
	// 批次属性8
	private String lotAtt08;
	// 批次属性9
	private String lotAtt09;
	// 批次属性10
	private String lotAtt10;
	// 批次属性11
	private String lotAtt11;
	// 批次属性12
	private String lotAtt12;
	// 源商品是否序列号
	private String fmIsSerial;
	// 目标商品是否序列号
	private String toIsSerial;
	// 序列号转移明细
	private List<BanQinWmTfSerialEntity> wmTfSerialEntitys;
    
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

	public String getIsAllowTf() {
		return isAllowTf;
	}

	public void setIsAllowTf(String isAllowTf) {
		this.isAllowTf = isAllowTf;
	}

	public Integer getAvailableNum() {
		return availableNum;
	}

	public void setAvailableNum(Integer availableNum) {
		this.availableNum = availableNum;
	}

	public Double getQty() {
		return qty;
	}

	public void setQty(Double qty) {
		this.qty = qty;
	}

	public Double getQtyHold() {
		return qtyHold;
	}

	public void setQtyHold(Double qtyHold) {
		this.qtyHold = qtyHold;
	}

	public Double getQtyAlloc() {
		return qtyAlloc;
	}

	public void setQtyAlloc(Double qtyAlloc) {
		this.qtyAlloc = qtyAlloc;
	}

	public Double getQtyPk() {
		return qtyPk;
	}

	public void setQtyPk(Double qtyPk) {
		this.qtyPk = qtyPk;
	}

	public Double getQtyPaOut() {
		return qtyPaOut;
	}

	public void setQtyPaOut(Double qtyPaOut) {
		this.qtyPaOut = qtyPaOut;
	}

	public Double getQtyRpOut() {
		return qtyRpOut;
	}

	public void setQtyRpOut(Double qtyRpOut) {
		this.qtyRpOut = qtyRpOut;
	}

	public String getFmSkuName() {
		return fmSkuName;
	}

	public void setFmSkuName(String fmSkuName) {
		this.fmSkuName = fmSkuName;
	}

	public String getToSkuName() {
		return toSkuName;
	}

	public void setToSkuName(String toSkuName) {
		this.toSkuName = toSkuName;
	}

	public String getFmCdprQuantity() {
		return fmCdprQuantity;
	}

	public void setFmCdprQuantity(String fmCdprQuantity) {
		this.fmCdprQuantity = fmCdprQuantity;
	}

	public String getToCdprQuantity() {
		return toCdprQuantity;
	}

	public void setToCdprQuantity(String toCdprQuantity) {
		this.toCdprQuantity = toCdprQuantity;
	}

	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getFmLotAtt01() {
		return fmLotAtt01;
	}

	public void setFmLotAtt01(Date fmLotAtt01) {
		this.fmLotAtt01 = fmLotAtt01;
	}

	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getFmLotAtt02() {
		return fmLotAtt02;
	}

	public void setFmLotAtt02(Date fmLotAtt02) {
		this.fmLotAtt02 = fmLotAtt02;
	}

	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getFmLotAtt03() {
		return fmLotAtt03;
	}

	public void setFmLotAtt03(Date fmLotAtt03) {
		this.fmLotAtt03 = fmLotAtt03;
	}

	public String getFmLotAtt04() {
		return fmLotAtt04;
	}

	public void setFmLotAtt04(String fmLotAtt04) {
		this.fmLotAtt04 = fmLotAtt04;
	}

	public String getFmLotAtt05() {
		return fmLotAtt05;
	}

	public void setFmLotAtt05(String fmLotAtt05) {
		this.fmLotAtt05 = fmLotAtt05;
	}

	public String getFmLotAtt06() {
		return fmLotAtt06;
	}

	public void setFmLotAtt06(String fmLotAtt06) {
		this.fmLotAtt06 = fmLotAtt06;
	}

	public String getFmLotAtt07() {
		return fmLotAtt07;
	}

	public void setFmLotAtt07(String fmLotAtt07) {
		this.fmLotAtt07 = fmLotAtt07;
	}

	public String getFmLotAtt08() {
		return fmLotAtt08;
	}

	public void setFmLotAtt08(String fmLotAtt08) {
		this.fmLotAtt08 = fmLotAtt08;
	}

	public String getFmLotAtt09() {
		return fmLotAtt09;
	}

	public void setFmLotAtt09(String fmLotAtt09) {
		this.fmLotAtt09 = fmLotAtt09;
	}

	public String getFmLotAtt10() {
		return fmLotAtt10;
	}

	public void setFmLotAtt10(String fmLotAtt10) {
		this.fmLotAtt10 = fmLotAtt10;
	}

	public String getFmLotAtt11() {
		return fmLotAtt11;
	}

	public void setFmLotAtt11(String fmLotAtt11) {
		this.fmLotAtt11 = fmLotAtt11;
	}

	public String getFmLotAtt12() {
		return fmLotAtt12;
	}

	public void setFmLotAtt12(String fmLotAtt12) {
		this.fmLotAtt12 = fmLotAtt12;
	}

	public String getFmIsSerial() {
		return fmIsSerial;
	}

	public void setFmIsSerial(String fmIsSerial) {
		this.fmIsSerial = fmIsSerial;
	}

	public String getToIsSerial() {
		return toIsSerial;
	}

	public void setToIsSerial(String toIsSerial) {
		this.toIsSerial = toIsSerial;
	}

	public List<BanQinWmTfSerialEntity> getWmTfSerialEntitys() {
		return wmTfSerialEntitys;
	}

	public void setWmTfSerialEntitys(List<BanQinWmTfSerialEntity> wmTfSerialEntitys) {
		this.wmTfSerialEntitys = wmTfSerialEntitys;
	}

    public String getFmOwnerName() {
        return fmOwnerName;
    }

    public void setFmOwnerName(String fmOwnerName) {
        this.fmOwnerName = fmOwnerName;
    }

    public String getToOwnerName() {
        return toOwnerName;
    }

    public void setToOwnerName(String toOwnerName) {
        this.toOwnerName = toOwnerName;
    }

    public String getFmPackDesc() {
        return fmPackDesc;
    }

    public void setFmPackDesc(String fmPackDesc) {
        this.fmPackDesc = fmPackDesc;
    }

    public String getFmUomDesc() {
        return fmUomDesc;
    }

    public void setFmUomDesc(String fmUomDesc) {
        this.fmUomDesc = fmUomDesc;
    }

    public String getToPackDesc() {
        return toPackDesc;
    }

    public void setToPackDesc(String toPackDesc) {
        this.toPackDesc = toPackDesc;
    }

    public String getToUomDesc() {
        return toUomDesc;
    }

    public void setToUomDesc(String toUomDesc) {
        this.toUomDesc = toUomDesc;
    }
}
