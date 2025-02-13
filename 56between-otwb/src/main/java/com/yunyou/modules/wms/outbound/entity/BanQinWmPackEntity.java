package com.yunyou.modules.wms.outbound.entity;

import java.util.List;

/**
 * 打包entity
 * @author WMJ
 * @version 2019/02/25
 */
public class BanQinWmPackEntity extends BanQinWmSoAlloc {

	// 出库单快递单号
	private String soTrackingNo;
	// 打包数量
	private Double qtyPackEa;
	// 包装数量
    private Double qtyPackUom;
    //
    private String isCheck;
    //
    private String isPrintContainer;
    //
    private String isPrintLabel;
	// 打包查询结果列表
	private List<BanQinWmSoAllocEntity> allocItems;
	//
    private List<BanQinWmSoSerialEntity> soSerialList;

	public String getSoTrackingNo() {
		return soTrackingNo;
	}

	public void setSoTrackingNo(String soTrackingNo) {
		this.soTrackingNo = soTrackingNo;
	}

    public Double getQtyPackEa() {
        return qtyPackEa;
    }

    public void setQtyPackEa(Double qtyPackEa) {
        this.qtyPackEa = qtyPackEa;
    }

    public Double getQtyPackUom() {
        return qtyPackUom;
    }

    public void setQtyPackUom(Double qtyPackUom) {
        this.qtyPackUom = qtyPackUom;
    }

    public String getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(String isCheck) {
        this.isCheck = isCheck;
    }

    public String getIsPrintContainer() {
        return isPrintContainer;
    }

    public void setIsPrintContainer(String isPrintContainer) {
        this.isPrintContainer = isPrintContainer;
    }

    public String getIsPrintLabel() {
        return isPrintLabel;
    }

    public void setIsPrintLabel(String isPrintLabel) {
        this.isPrintLabel = isPrintLabel;
    }

    public List<BanQinWmSoAllocEntity> getAllocItems() {
        return allocItems;
    }

    public void setAllocItems(List<BanQinWmSoAllocEntity> allocItems) {
        this.allocItems = allocItems;
    }

    public List<BanQinWmSoSerialEntity> getSoSerialList() {
        return soSerialList;
    }

    public void setSoSerialList(List<BanQinWmSoSerialEntity> soSerialList) {
        this.soSerialList = soSerialList;
    }
}