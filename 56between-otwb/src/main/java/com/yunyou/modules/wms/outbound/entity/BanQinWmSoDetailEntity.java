package com.yunyou.modules.wms.outbound.entity;

import java.util.Date;
import java.util.List;

/**
 * 描述：出库单明细扩展Entity
 *
 * @auther: Jianhua on 2019/2/14
 */
public class BanQinWmSoDetailEntity extends BanQinWmSoDetail {
    // 波次单号
    private String waveNo;
    // 货主名称
    private String ownerName;
    // 商品名称
    private String skuName;
    // 商品快速录入码
    private String skuQuickCode;
    // 包装描述、规格
    private String packDesc;
    // 单位描述
    private String uomDesc;
    // 单位换算
    private Double uomQty;
    // 预配规则名称
    private String preallocRuleName;
    // 分配规则名称
    private String allocRuleName;
    // 周转规则名称
    private String rotationRuleName;
    // 拣货区名称
    private String zoneName;
    // 区域名称
    private String areaName;
    // 单位数量
    private Double qtySoUom;
    // 预配数
    private Double qtyPreallocUom;
    // 分配数
    private Double qtyAllocUom;
    // 拣货数
    private Double qtyPkUom;
    // 发货数
    private Double qtyShipUom;
    // 出库单类型
    private String soType;
    // 收货人编码
    private String consigneeCode;
    // 收货人名称
    private String consigneeName;
    // 客户订单号
    private String customerOrderNo;

    private List<BanQinWmSoDetailEntity> detailEntityList;

    // 查询条件
    private List<String> soNos;
    private List<String> soLineNos;
    private List<String> waveNos;
    private String isIntercept;// 是否拦截
    private String isAudit;// 是否审核
    private String holdStatus;
    private Date beginOrderTime;	// 开始 订单时间
    private Date endOrderTime;		// 结束 订单时间

    public String getWaveNo() {
        return waveNo;
    }

    public void setWaveNo(String waveNo) {
        this.waveNo = waveNo;
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

    public String getRotationRuleName() {
        return rotationRuleName;
    }

    public void setRotationRuleName(String rotationRuleName) {
        this.rotationRuleName = rotationRuleName;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public Double getQtySoUom() {
        return qtySoUom;
    }

    public void setQtySoUom(Double qtySoUom) {
        this.qtySoUom = qtySoUom;
    }

    public Double getQtyPreallocUom() {
        return qtyPreallocUom;
    }

    public void setQtyPreallocUom(Double qtyPreallocUom) {
        this.qtyPreallocUom = qtyPreallocUom;
    }

    public Double getQtyAllocUom() {
        return qtyAllocUom;
    }

    public void setQtyAllocUom(Double qtyAllocUom) {
        this.qtyAllocUom = qtyAllocUom;
    }

    public Double getQtyPkUom() {
        return qtyPkUom;
    }

    public void setQtyPkUom(Double qtyPkUom) {
        this.qtyPkUom = qtyPkUom;
    }

    public Double getQtyShipUom() {
        return qtyShipUom;
    }

    public void setQtyShipUom(Double qtyShipUom) {
        this.qtyShipUom = qtyShipUom;
    }

    public String getSoType() {
        return soType;
    }

    public void setSoType(String soType) {
        this.soType = soType;
    }

    public List<String> getSoNos() {
        return soNos;
    }

    public void setSoNos(List<String> soNos) {
        this.soNos = soNos;
    }

    public List<String> getSoLineNos() {
        return soLineNos;
    }

    public void setSoLineNos(List<String> soLineNos) {
        this.soLineNos = soLineNos;
    }

    public List<String> getWaveNos() {
        return waveNos;
    }

    public void setWaveNos(List<String> waveNos) {
        this.waveNos = waveNos;
    }

    public String getIsIntercept() {
        return isIntercept;
    }

    public void setIsIntercept(String isIntercept) {
        this.isIntercept = isIntercept;
    }

    public String getIsAudit() {
        return isAudit;
    }

    public void setIsAudit(String isAudit) {
        this.isAudit = isAudit;
    }

    public String getHoldStatus() {
        return holdStatus;
    }

    public void setHoldStatus(String holdStatus) {
        this.holdStatus = holdStatus;
    }

    public List<BanQinWmSoDetailEntity> getDetailEntityList() {
        return detailEntityList;
    }

    public void setDetailEntityList(List<BanQinWmSoDetailEntity> detailEntityList) {
        this.detailEntityList = detailEntityList;
    }

    public String getConsigneeCode() {
        return consigneeCode;
    }

    public void setConsigneeCode(String consigneeCode) {
        this.consigneeCode = consigneeCode;
    }

    public String getConsigneeName() {
        return consigneeName;
    }

    public void setConsigneeName(String consigneeName) {
        this.consigneeName = consigneeName;
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

    public String getCustomerOrderNo() {
        return customerOrderNo;
    }

    public void setCustomerOrderNo(String customerOrderNo) {
        this.customerOrderNo = customerOrderNo;
    }
}
