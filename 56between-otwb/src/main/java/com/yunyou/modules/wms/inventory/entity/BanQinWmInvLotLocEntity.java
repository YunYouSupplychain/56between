package com.yunyou.modules.wms.inventory.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * 获取批次库位库存 入参entity
 * @author WMJ
 * @version 2019/03/05
 */
public class BanQinWmInvLotLocEntity extends BanQinWmInvLotLoc {
	// 批次属性01
	private Date lotAtt01;
	// 批次属性02
	private Date lotAtt02;
	// 批次属性03
	private Date lotAtt03;
	// 批次属性04
	private String lotAtt04;
	// 批次属性05
	private String lotAtt05;
	// 批次属性06
	private String lotAtt06;
	// 批次属性07
	private String lotAtt07;
	// 批次属性08
	private String lotAtt08;
	// 批次属性09
	private String lotAtt09;
	// 批次属性10
	private String lotAtt10;
	// 批次属性11
	private String lotAtt11;
	// 批次属性06
	private String lotAtt12;
	// 可用数
	private Double qtyAvailable;
	// 货主名称
	private String ownerName;
	// 商品名
	private String skuName;
    // 包装编码
    private String packCode;
    // 包装规格描述
    private String packDesc;
    // 打印单位
    private String printUom;
	// 毛重
	private Double grossWeight;
    // 单位描述
    private String uomDesc;
    // 包装数量
    private Double uomQty;
    // 是否装箱
    private String cdprIsPackBox;
    // 是否是序列号管理
	private String isSerial;
	// 出库时间
	private Date outboundTime;
	// 承运商编码
	private String carrierCode;
	// 承运商名称
	private String carrierName;
	// 车牌号
	private String vehicleNo;
	// 驾驶员
	private String driver;
	// 驾驶员电话
	private String driverTel;
	// 收货人地址
	private String consigneeAddr;
	// 客户单号
	private String customerNo;
	// 订单日期
	private Date soOrderTime;

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

	public String getSkuName() {
		return skuName;
	}

	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}

    public Double getQtyAvailable() {
        return qtyAvailable;
    }

    public void setQtyAvailable(Double qtyAvailable) {
        this.qtyAvailable = qtyAvailable;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getPackCode() {
        return packCode;
    }

    public void setPackCode(String packCode) {
        this.packCode = packCode;
    }

    public String getPackDesc() {
        return packDesc;
    }

    public void setPackDesc(String packDesc) {
        this.packDesc = packDesc;
    }

    public String getPrintUom() {
        return printUom;
    }

    public void setPrintUom(String printUom) {
        this.printUom = printUom;
    }

	public Double getGrossWeight() {
		return grossWeight;
	}

	public void setGrossWeight(Double grossWeight) {
		this.grossWeight = grossWeight;
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

    public String getCdprIsPackBox() {
        return cdprIsPackBox;
    }

    public void setCdprIsPackBox(String cdprIsPackBox) {
        this.cdprIsPackBox = cdprIsPackBox;
    }

	public String getIsSerial() {
		return isSerial;
	}

	public void setIsSerial(String isSerial) {
		this.isSerial = isSerial;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getOutboundTime() {
		return outboundTime;
	}

	public void setOutboundTime(Date outboundTime) {
		this.outboundTime = outboundTime;
	}

	public String getCarrierCode() {
		return carrierCode;
	}

	public void setCarrierCode(String carrierCode) {
		this.carrierCode = carrierCode;
	}

	public String getCarrierName() {
		return carrierName;
	}

	public void setCarrierName(String carrierName) {
		this.carrierName = carrierName;
	}

	public String getVehicleNo() {
		return vehicleNo;
	}

	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getDriverTel() {
		return driverTel;
	}

	public void setDriverTel(String driverTel) {
		this.driverTel = driverTel;
	}

	public String getConsigneeAddr() {
		return consigneeAddr;
	}

	public void setConsigneeAddr(String consigneeAddr) {
		this.consigneeAddr = consigneeAddr;
	}

	public String getCustomerNo() {
		return customerNo;
	}

	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getSoOrderTime() {
		return soOrderTime;
	}

	public void setSoOrderTime(Date soOrderTime) {
		this.soOrderTime = soOrderTime;
	}
}