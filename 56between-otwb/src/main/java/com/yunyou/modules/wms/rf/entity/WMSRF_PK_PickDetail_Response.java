package com.yunyou.modules.wms.rf.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class WMSRF_PK_PickDetail_Response implements Serializable {
    // 主键
    private String id;
    // 分配Id
    private String allocId;
    // 预配ID
    private String preallocId;
    // 波次单号
    private String waveNo;
    // 出库单号
    private String soNo;
    // 行号
    private String lineNo;
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
    // 收货人编码
    private String consigneeCode;
    // 状态
    private String status;
    // 复核状态(00未复核、99已复核、90不复核)
    private String checkStatus;
    // 包装编码
    private String packCode;
    // 包装规格
    private String packDesc;
    // 包装单位
    private String uom;
    private String uomDesc;
    // 包装数量
    private Double qtyUom;
    // EA数量
    private Double qtyEa;
    // 目标库位编码
    private String toLoc;
    // 目标跟踪号
    private String toId;
    // 拣货人
    private String pickOp;
    // 拣货时间
    private Date pickTime;
    // 复核人
    private String checkOp;
    // 复核时间
    private Date checkTime;
    // 发货人
    private String shipOp;
    // 发货时间
    private Date shipTime;
    // 拣货单号
    private String pickNo;
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
    // 批次属性配置
    private List<WMSRF_CF_ProductionLotConfig> lotConfigs;
    // 包装规格
    private List<WMSRF_CF_ProductionPackageConfig> packageConfigs;
    // 总任务数
    private Long totalNum;
    // 已完成拣货任务数
    private Long pickNum;
    // 波次s顺序
    private String seq;
    // 条码
    private String barcode;
    //
    private Integer recVer;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAllocId() {
        return allocId;
    }

    public void setAllocId(String allocId) {
        this.allocId = allocId;
    }

    public String getPreallocId() {
        return preallocId;
    }

    public void setPreallocId(String preallocId) {
        this.preallocId = preallocId;
    }

    public String getWaveNo() {
        return waveNo;
    }

    public void setWaveNo(String waveNo) {
        this.waveNo = waveNo;
    }

    public String getSoNo() {
        return soNo;
    }

    public void setSoNo(String soNo) {
        this.soNo = soNo;
    }

    public String getLineNo() {
        return lineNo;
    }

    public void setLineNo(String lineNo) {
        this.lineNo = lineNo;
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

    public String getConsigneeCode() {
        return consigneeCode;
    }

    public void setConsigneeCode(String consigneeCode) {
        this.consigneeCode = consigneeCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(String checkStatus) {
        this.checkStatus = checkStatus;
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

    public Double getQtyUom() {
        return qtyUom;
    }

    public void setQtyUom(Double qtyUom) {
        this.qtyUom = qtyUom;
    }

    public Double getQtyEa() {
        return qtyEa;
    }

    public void setQtyEa(Double qtyEa) {
        this.qtyEa = qtyEa;
    }

    public String getToLoc() {
        return toLoc;
    }

    public void setToLoc(String toLoc) {
        this.toLoc = toLoc;
    }

    public String getToId() {
        return toId;
    }

    public void setToId(String toId) {
        this.toId = toId;
    }

    public String getPickOp() {
        return pickOp;
    }

    public void setPickOp(String pickOp) {
        this.pickOp = pickOp;
    }

    public Date getPickTime() {
        return pickTime;
    }

    public void setPickTime(Date pickTime) {
        this.pickTime = pickTime;
    }

    public String getCheckOp() {
        return checkOp;
    }

    public void setCheckOp(String checkOp) {
        this.checkOp = checkOp;
    }

    public Date getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
    }

    public String getShipOp() {
        return shipOp;
    }

    public void setShipOp(String shipOp) {
        this.shipOp = shipOp;
    }

    public Date getShipTime() {
        return shipTime;
    }

    public void setShipTime(Date shipTime) {
        this.shipTime = shipTime;
    }

    public String getPickNo() {
        return pickNo;
    }

    public void setPickNo(String pickNo) {
        this.pickNo = pickNo;
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

    public Long getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Long totalNum) {
        this.totalNum = totalNum;
    }

    public Long getPickNum() {
        return pickNum;
    }

    public void setPickNum(Long pickNum) {
        this.pickNum = pickNum;
    }

    public List<WMSRF_CF_ProductionPackageConfig> getPackageConfigs() {
        return packageConfigs;
    }

    public void setPackageConfigs(List<WMSRF_CF_ProductionPackageConfig> packageConfigs) {
        this.packageConfigs = packageConfigs;
    }

    public List<WMSRF_CF_ProductionLotConfig> getLotConfigs() {
        return lotConfigs;
    }

    public void setLotConfigs(List<WMSRF_CF_ProductionLotConfig> lotConfigs) {
        this.lotConfigs = lotConfigs;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Integer getRecVer() {
        return recVer;
    }

    public void setRecVer(Integer recVer) {
        this.recVer = recVer;
    }

    public String getUomDesc() {
        return uomDesc;
    }

    public void setUomDesc(String uomDesc) {
        this.uomDesc = uomDesc;
    }
}
