package com.yunyou.modules.wms.outbound.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.List;

/**
 * 描述：分配拣货明细扩展Entity
 *
 * @auther: Jianhua on 2019/2/14
 */
public class BanQinWmSoAllocEntity extends BanQinWmSoAlloc {
    // 拣货数量
    private Double qtyPkEa;
    // 拣货包装数量
    private Double qtyPkUom;
    // 货主名称
    private String ownerName;
    // 商品名称
    private String skuName;
    // 商品快速录入码
    private String skuQuickCode;
    // 包装规格
    private String packDesc;
    // 包装单位描述
    private String uomDesc;
    // 包装换算数量
    private Double uomQty;
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
    // 是否序列号管理
    private String isSerial;
    // 商品单体积
    private Double cubic;
    // 商品单净重
    private Double netWeight;
    // 商品单毛重
    private Double grossWeight;
    // 商品上架规则
    private String paRule;
    // 商品上架库位指定规则
    private String reserveCode;
    // 是否生成上架任务
    private String isTaskPa;
    // 上架任务是否推荐分配库位
    private String isAllocLoc;
    // 库区
    private String zoneCode;
    //
    private String orgName;
    // 手工拣货库位
    private String pickLoc;
    // 手工拣货批次
    private String pickLotNum;
    // 手工拣货跟踪号
    private String pickTraceId;
    // 手工目标库位
    private String pickToLoc;
    // 手工目标跟踪号
    private String pickToId;
    private Double qtyCheckUom;
    private Double qtyCheckEa;
    private String isPack;
    private Double qtyPackUom;
    private Double qtyPackEa;
    // 波次顺序
    private String seq;
    // 条码
    private String barcode;
    // 订单数量
    private Double qtySoEa;    
	// 是否非库存序列号管理
    private String isUnSerial;
    // 订单时间
    private Date orderTime;
    // 规格型号
    private String skuSpec;
    // 数量单位
    private String qtyUnit;

    // 商品明细自定义
    private String def1;		// 自定义1（温层）
    private String def2;		// 自定义2（课别）
    private String def3;		// 自定义3
    private String def4;		// 自定义4
    private String def5;		// 自定义5
    private String def6;		// 自定义6
    private String def7;		// 自定义7
    private String def8;		// 自定义8
    private String def9;		// 自定义9
    private String def10;		// 自定义10

    // 包装查询用
    private String carrierCode;
    private String line;
    private String consigneeName;
    private String consigneeTel;
    private String consigneeAddr;
    private String contactName;
    private String contactTel;
    private String contactAddr;
    
    private List<String> waveNos; // 查询用
    private List<String> soNos; // 查询用
    private String lineNos; // 查询用
    private List<String> allocIds; // 查询用
    private List<String> ToIds; // 查询用
    private List<String> cdTypeList;
    private List<String> statusList;
    private String statuss;
    private String soTrackingNo;
    private String isPacked = "N";// 查询用 是否打包(Y/N)
    private Date beginOrderTime;	// 开始 订单时间
    private Date endOrderTime;		// 结束 订单时间

    public Double getQtyPkEa() {
        return qtyPkEa;
    }

    public void setQtyPkEa(Double qtyPkEa) {
        this.qtyPkEa = qtyPkEa;
    }

    public Double getQtyPkUom() {
        return qtyPkUom;
    }

    public void setQtyPkUom(Double qtyPkUom) {
        this.qtyPkUom = qtyPkUom;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getSkuQuickCode() {
        return skuQuickCode;
    }

    public void setSkuQuickCode(String skuQuickCode) {
        this.skuQuickCode = skuQuickCode;
    }

    public String getPackDesc() {
        return packDesc;
    }

    public void setPackDesc(String packDesc) {
        this.packDesc = packDesc;
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

    public String getIsSerial() {
        return isSerial;
    }

    public void setIsSerial(String isSerial) {
        this.isSerial = isSerial;
    }

    public Double getCubic() {
        return cubic;
    }

    public void setCubic(Double cubic) {
        this.cubic = cubic;
    }

    public Double getNetWeight() {
        return netWeight;
    }

    public void setNetWeight(Double netWeight) {
        this.netWeight = netWeight;
    }

    public Double getGrossWeight() {
        return grossWeight;
    }

    public void setGrossWeight(Double grossWeight) {
        this.grossWeight = grossWeight;
    }

    public String getPaRule() {
        return paRule;
    }

    public void setPaRule(String paRule) {
        this.paRule = paRule;
    }

    public String getReserveCode() {
        return reserveCode;
    }

    public void setReserveCode(String reserveCode) {
        this.reserveCode = reserveCode;
    }

    public String getIsTaskPa() {
        return isTaskPa;
    }

    public void setIsTaskPa(String isTaskPa) {
        this.isTaskPa = isTaskPa;
    }

    public String getIsAllocLoc() {
        return isAllocLoc;
    }

    public void setIsAllocLoc(String isAllocLoc) {
        this.isAllocLoc = isAllocLoc;
    }

    public List<String> getWaveNos() {
        return waveNos;
    }

    public void setWaveNos(List<String> waveNos) {
        this.waveNos = waveNos;
    }

    public List<String> getSoNos() {
        return soNos;
    }

    public void setSoNos(List<String> soNos) {
        this.soNos = soNos;
    }

    public String getLineNos() {
        return lineNos;
    }

    public void setLineNos(String lineNos) {
        this.lineNos = lineNos;
    }

    public List<String> getAllocIds() {
        return allocIds;
    }

    public void setAllocIds(List<String> allocIds) {
        this.allocIds = allocIds;
    }

    public List<String> getToIds() {
        return ToIds;
    }

    public void setToIds(List<String> toIds) {
        ToIds = toIds;
    }

    public String getZoneCode() {
        return zoneCode;
    }

    public void setZoneCode(String zoneCode) {
        this.zoneCode = zoneCode;
    }

    public String getConsigneeName() {
        return consigneeName;
    }

    public void setConsigneeName(String consigneeName) {
        this.consigneeName = consigneeName;
    }

    public String getConsigneeTel() {
        return consigneeTel;
    }

    public void setConsigneeTel(String consigneeTel) {
        this.consigneeTel = consigneeTel;
    }

    public String getConsigneeAddr() {
        return consigneeAddr;
    }

    public void setConsigneeAddr(String consigneeAddr) {
        this.consigneeAddr = consigneeAddr;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactTel() {
        return contactTel;
    }

    public void setContactTel(String contactTel) {
        this.contactTel = contactTel;
    }

    public String getContactAddr() {
        return contactAddr;
    }

    public void setContactAddr(String contactAddr) {
        this.contactAddr = contactAddr;
    }

    public String getCarrierCode() {
        return carrierCode;
    }

    public void setCarrierCode(String carrierCode) {
        this.carrierCode = carrierCode;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public List<String> getCdTypeList() {
        return cdTypeList;
    }

    public void setCdTypeList(List<String> cdTypeList) {
        this.cdTypeList = cdTypeList;
    }

    public List<String> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<String> statusList) {
        this.statusList = statusList;
    }

    public String getSoTrackingNo() {
        return soTrackingNo;
    }

    public void setSoTrackingNo(String soTrackingNo) {
        this.soTrackingNo = soTrackingNo;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getPickLoc() {
        return pickLoc;
    }

    public void setPickLoc(String pickLoc) {
        this.pickLoc = pickLoc;
    }

    public String getPickLotNum() {
        return pickLotNum;
    }

    public void setPickLotNum(String pickLotNum) {
        this.pickLotNum = pickLotNum;
    }

    public String getPickTraceId() {
        return pickTraceId;
    }

    public void setPickTraceId(String pickTraceId) {
        this.pickTraceId = pickTraceId;
    }

    public String getPickToLoc() {
        return pickToLoc;
    }

    public void setPickToLoc(String pickToLoc) {
        this.pickToLoc = pickToLoc;
    }

    public String getPickToId() {
        return pickToId;
    }

    public void setPickToId(String pickToId) {
        this.pickToId = pickToId;
    }

    public Double getQtyCheckUom() {
        return qtyCheckUom;
    }

    public void setQtyCheckUom(Double qtyCheckUom) {
        this.qtyCheckUom = qtyCheckUom;
    }

    public Double getQtyCheckEa() {
        return qtyCheckEa;
    }

    public void setQtyCheckEa(Double qtyCheckEa) {
        this.qtyCheckEa = qtyCheckEa;
    }

    public String getIsPack() {
        return isPack;
    }

    public void setIsPack(String isPack) {
        this.isPack = isPack;
    }

    public Double getQtyPackUom() {
        return qtyPackUom;
    }

    public void setQtyPackUom(Double qtyPackUom) {
        this.qtyPackUom = qtyPackUom;
    }

    public Double getQtyPackEa() {
        return qtyPackEa;
    }

    public void setQtyPackEa(Double qtyPackEa) {
        this.qtyPackEa = qtyPackEa;
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

    public Double getQtySoEa() {
        return qtySoEa;
    }

    public void setQtySoEa(Double qtySoEa) {
        this.qtySoEa = qtySoEa;
    }

    public String getIsPacked() {
        return isPacked;
    }

    public void setIsPacked(String isPacked) {
        this.isPacked = isPacked;
    }

    public String getIsUnSerial() {
        return isUnSerial;
    }

    public void setIsUnSerial(String isUnSerial) {
        this.isUnSerial = isUnSerial;
    }

    public Date getBeginOrderTime() {
        return beginOrderTime;
    }

    public void setBeginOrderTime(Date beginOrderTime) {
        this.beginOrderTime = beginOrderTime;
    }

    public Date getEndOrderTime() {
        return endOrderTime;
    }

    public void setEndOrderTime(Date endOrderTime) {
        this.endOrderTime = endOrderTime;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public String getStatuss() {
        return statuss;
    }

    public void setStatuss(String statuss) {
        this.statuss = statuss;
    }

    public String getSkuSpec() {
        return skuSpec;
    }

    public void setSkuSpec(String skuSpec) {
        this.skuSpec = skuSpec;
    }

    public String getQtyUnit() {
        return qtyUnit;
    }

    public void setQtyUnit(String qtyUnit) {
        this.qtyUnit = qtyUnit;
    }

    public String getDef1() {
        return def1;
    }

    public void setDef1(String def1) {
        this.def1 = def1;
    }

    public String getDef2() {
        return def2;
    }

    public void setDef2(String def2) {
        this.def2 = def2;
    }

    public String getDef3() {
        return def3;
    }

    public void setDef3(String def3) {
        this.def3 = def3;
    }

    public String getDef4() {
        return def4;
    }

    public void setDef4(String def4) {
        this.def4 = def4;
    }

    public String getDef5() {
        return def5;
    }

    public void setDef5(String def5) {
        this.def5 = def5;
    }

    public String getDef6() {
        return def6;
    }

    public void setDef6(String def6) {
        this.def6 = def6;
    }

    public String getDef7() {
        return def7;
    }

    public void setDef7(String def7) {
        this.def7 = def7;
    }

    public String getDef8() {
        return def8;
    }

    public void setDef8(String def8) {
        this.def8 = def8;
    }

    public String getDef9() {
        return def9;
    }

    public void setDef9(String def9) {
        this.def9 = def9;
    }

    public String getDef10() {
        return def10;
    }

    public void setDef10(String def10) {
        this.def10 = def10;
    }
}
