package com.yunyou.modules.wms.basicdata.entity;

import java.util.List;

/**
 * 商品Entity
 * @author WMJ
 * @version 2019-01-25
 */
public class BanQinCdWhSkuEntity extends BanQinCdWhSku {
	
	private static final long serialVersionUID = 1L;
    // 货主名称
    private String ownerName;
    // 包装规格描述
    private String cdpaFormat;
    // 批次属性名称
    private String lotName;
    // 上架规则名称
    private String paRuleName;
    // 上架库区名称
    private String paZoneName;
    // 库存周转规则名称
    private String rotationRuleName;
    // 预配规则名称
    private String preallocRuleName;
    // 分配规则名称
    private String allocRuleName;
    // 循环级别
    private String cycleName;
    // 缺省收货单位名称
    private String rcvUomName;
    // 缺省收货单位数量
    private Double rcvUomQty;
    // 缺省发货单位名称
    private String shipUomName;
    // 缺省发货单位数量
    private Double shipUomQty;
    // 缺省打印单位名称
    private String printUomName;
    private Double uomQty;
    // 质检规则名称
    private String qcRuleName;
    // 质检项组名称
    private String itemGroupName;
    // 商品分类名称
    private String typeName;
    // 模糊查询
    private String skuCodeAndName;
    // 货主编码组
    private String ownerCodes;
    // 条码信息
    private List<BanQinCdWhSkuBarcode> barcodeList;

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getCdpaFormat() {
        return cdpaFormat;
    }

    public void setCdpaFormat(String cdpaFormat) {
        this.cdpaFormat = cdpaFormat;
    }

    public String getLotName() {
        return lotName;
    }

    public void setLotName(String lotName) {
        this.lotName = lotName;
    }

    public String getPaRuleName() {
        return paRuleName;
    }

    public void setPaRuleName(String paRuleName) {
        this.paRuleName = paRuleName;
    }

    public String getPaZoneName() {
        return paZoneName;
    }

    public void setPaZoneName(String paZoneName) {
        this.paZoneName = paZoneName;
    }

    public String getRotationRuleName() {
        return rotationRuleName;
    }

    public void setRotationRuleName(String rotationRuleName) {
        this.rotationRuleName = rotationRuleName;
    }

    public String getPreallocRuleName() {
        return preallocRuleName;
    }

    public void setPreallocRuleName(String preallocRuleName) {
        this.preallocRuleName = preallocRuleName;
    }

    public String getAllocRuleName() {
        return allocRuleName;
    }

    public void setAllocRuleName(String allocRuleName) {
        this.allocRuleName = allocRuleName;
    }

    public String getCycleName() {
        return cycleName;
    }

    public void setCycleName(String cycleName) {
        this.cycleName = cycleName;
    }

    public String getRcvUomName() {
        return rcvUomName;
    }

    public void setRcvUomName(String rcvUomName) {
        this.rcvUomName = rcvUomName;
    }

    public String getShipUomName() {
        return shipUomName;
    }

    public void setShipUomName(String shipUomName) {
        this.shipUomName = shipUomName;
    }

    public String getPrintUomName() {
        return printUomName;
    }

    public void setPrintUomName(String printUomName) {
        this.printUomName = printUomName;
    }

    public String getQcRuleName() {
        return qcRuleName;
    }

    public void setQcRuleName(String qcRuleName) {
        this.qcRuleName = qcRuleName;
    }

    public String getItemGroupName() {
        return itemGroupName;
    }

    public void setItemGroupName(String itemGroupName) {
        this.itemGroupName = itemGroupName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Double getRcvUomQty() {
        return rcvUomQty;
    }

    public void setRcvUomQty(Double rcvUomQty) {
        this.rcvUomQty = rcvUomQty;
    }

    public Double getShipUomQty() {
        return shipUomQty;
    }

    public void setShipUomQty(Double shipUomQty) {
        this.shipUomQty = shipUomQty;
    }

    public Double getUomQty() {
        return uomQty;
    }

    public void setUomQty(Double uomQty) {
        this.uomQty = uomQty;
    }

    public String getSkuCodeAndName() {
        return skuCodeAndName;
    }

    public void setSkuCodeAndName(String skuCodeAndName) {
        this.skuCodeAndName = skuCodeAndName;
    }

    public List<BanQinCdWhSkuBarcode> getBarcodeList() {
        return barcodeList;
    }

    public void setBarcodeList(List<BanQinCdWhSkuBarcode> barcodeList) {
        this.barcodeList = barcodeList;
    }

    public String getOwnerCodes() {
        return ownerCodes;
    }

    public void setOwnerCodes(String ownerCodes) {
        this.ownerCodes = ownerCodes;
    }
}