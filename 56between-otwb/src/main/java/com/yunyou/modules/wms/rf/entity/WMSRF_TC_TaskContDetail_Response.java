package com.yunyou.modules.wms.rf.entity;

import java.io.Serializable;
import java.util.List;

public class WMSRF_TC_TaskContDetail_Response implements Serializable {
    // 主键
    private String id;
    //
    private String headerId;
    // 盘点任务ID
    private String countId;
    // 盘点单号
    private String countNo;
    // 盘点状态
    private String status;
    // 盘点方法（静态盘点、动态盘点）
    private String countMethod;
    // 盘点方式（明盘、盲盘）
    private String countMode;
    // 区域编码
    private String areaCode;
    // 库区编码
    private String zoneCode;
    // 盘点人员
    private String countOp;
    // 上次盘点人员
    private String preCountOp;
    // 上次盘点单号
    private String preCountNo;
    // 上次盘点数量EA
    private Double preQtyCountEa;
    // 货主编码
    private String ownerCode;
    // 货主名称
    private String ownerName;
    // 商品编码
    private String skuCode;
    // 商品名称
    private String skuName;
    // 批次号
    private String lotNum;
    // 库位编码
    private String locCode;
    // 跟踪号
    private String traceId;
    // 库存数
    private Double qty;
    // 包装编码
    private String packCode;
    // 包装规格
    private String packDesc;
    // 单位
    private String uom;
    // 盘点库存
    private Double qtyCountUom;
    // 盘点库存EA
    private Double qtyCountEa;
    // 盘点差异数
    private Double qtyDiff;
    // 批次属性1(生产日期)
    private String lotAtt01;
    // 批次属性2(失效日期)
    private String lotAtt02;
    // 批次属性3(入库日期)
    private String lotAtt03;
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
    // 分公司
    private String orgId;
    private String isSerial;
    // 批次属性配置
    private List<WMSRF_CF_ProductionLotConfig> lotConfigs;
    // 包装规格
    private List<WMSRF_CF_ProductionPackageConfig> packageConfigs;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCountId() {
        return countId;
    }

    public void setCountId(String countId) {
        this.countId = countId;
    }

    public String getCountNo() {
        return countNo;
    }

    public void setCountNo(String countNo) {
        this.countNo = countNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCountMethod() {
        return countMethod;
    }

    public void setCountMethod(String countMethod) {
        this.countMethod = countMethod;
    }

    public String getCountMode() {
        return countMode;
    }

    public void setCountMode(String countMode) {
        this.countMode = countMode;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getZoneCode() {
        return zoneCode;
    }

    public void setZoneCode(String zoneCode) {
        this.zoneCode = zoneCode;
    }

    public String getCountOp() {
        return countOp;
    }

    public void setCountOp(String countOp) {
        this.countOp = countOp;
    }

    public String getPreCountOp() {
        return preCountOp;
    }

    public void setPreCountOp(String preCountOp) {
        this.preCountOp = preCountOp;
    }

    public String getPreCountNo() {
        return preCountNo;
    }

    public void setPreCountNo(String preCountNo) {
        this.preCountNo = preCountNo;
    }

    public Double getPreQtyCountEa() {
        return preQtyCountEa;
    }

    public void setPreQtyCountEa(Double preQtyCountEa) {
        this.preQtyCountEa = preQtyCountEa;
    }

    public String getOwnerCode() {
        return ownerCode;
    }

    public void setOwnerCode(String ownerCode) {
        this.ownerCode = ownerCode;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getLotNum() {
        return lotNum;
    }

    public void setLotNum(String lotNum) {
        this.lotNum = lotNum;
    }

    public String getLocCode() {
        return locCode;
    }

    public void setLocCode(String locCode) {
        this.locCode = locCode;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
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

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public Double getQtyCountUom() {
        return qtyCountUom;
    }

    public void setQtyCountUom(Double qtyCountUom) {
        this.qtyCountUom = qtyCountUom;
    }

    public Double getQtyCountEa() {
        return qtyCountEa;
    }

    public void setQtyCountEa(Double qtyCountEa) {
        this.qtyCountEa = qtyCountEa;
    }

    public Double getQtyDiff() {
        return qtyDiff;
    }

    public void setQtyDiff(Double qtyDiff) {
        this.qtyDiff = qtyDiff;
    }

    public String getLotAtt01() {
        return lotAtt01;
    }

    public void setLotAtt01(String lotAtt01) {
        this.lotAtt01 = lotAtt01;
    }

    public String getLotAtt02() {
        return lotAtt02;
    }

    public void setLotAtt02(String lotAtt02) {
        this.lotAtt02 = lotAtt02;
    }

    public String getLotAtt03() {
        return lotAtt03;
    }

    public void setLotAtt03(String lotAtt03) {
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

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getIsSerial() {
        return isSerial;
    }

    public void setIsSerial(String isSerial) {
        this.isSerial = isSerial;
    }

    public List<WMSRF_CF_ProductionLotConfig> getLotConfigs() {
        return lotConfigs;
    }

    public void setLotConfigs(List<WMSRF_CF_ProductionLotConfig> lotConfigs) {
        this.lotConfigs = lotConfigs;
    }

    public List<WMSRF_CF_ProductionPackageConfig> getPackageConfigs() {
        return packageConfigs;
    }

    public void setPackageConfigs(List<WMSRF_CF_ProductionPackageConfig> packageConfigs) {
        this.packageConfigs = packageConfigs;
    }

    public String getHeaderId() {
        return headerId;
    }

    public void setHeaderId(String headerId) {
        this.headerId = headerId;
    }
}
