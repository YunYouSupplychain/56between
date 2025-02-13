package com.yunyou.modules.sys.common.entity;

import com.yunyou.core.persistence.DataEntity;

import javax.validation.constraints.NotNull;

/**
 * 商品拣货位Entity
 */
public class SysWmsSkuLoc extends DataEntity<SysWmsSkuLoc> {
    private static final long serialVersionUID = 1L;
    @NotNull(message = "货主编码不能为空")
    private String ownerCode;// 货主编码
    @NotNull(message = "商品编码不能为空")
    private String skuCode;// 商品编码
    @NotNull(message = "库位编码不能为空")
    private String locCode;// 库位编码
    @NotNull(message = "商品拣货位类型不能为空")
    private String skuLocType;// 商品拣货位类型（CS、EA、PC）
    private Double minRp;// 最小补货数
    private String rpUom;// 补货单位（PL、CS、EA）
    private Double maxLimit;// 库存上限
    private Double minLimit;// 库存下限
    private String isOverAlloc;// 是否超量分配
    private String isAutoRp;// 是否自动生成补货任务（预留扩展）
    private String isRpAlloc;// 是否补被占用库存
    private String isOverRp;// 是否超量补货
    private String isFmRs;// 是否从存储位补货
    private String isFmCs;// 是否从箱拣货位补货
    private String dataSet;// 数据套
    private String headerId;// 商品Id

    public SysWmsSkuLoc() {
        super();
    }

    public SysWmsSkuLoc(String id) {
        super(id);
    }

    public SysWmsSkuLoc(String id, String headerId, String dataSet) {
        super(id);
        this.headerId = headerId;
        this.dataSet = dataSet;
    }

    public String getOwnerCode() {
        return ownerCode;
    }

    public void setOwnerCode(String ownerCode) {
        this.ownerCode = ownerCode;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public String getLocCode() {
        return locCode;
    }

    public void setLocCode(String locCode) {
        this.locCode = locCode;
    }

    public String getSkuLocType() {
        return skuLocType;
    }

    public void setSkuLocType(String skuLocType) {
        this.skuLocType = skuLocType;
    }

    public Double getMinRp() {
        return minRp;
    }

    public void setMinRp(Double minRp) {
        this.minRp = minRp;
    }

    public String getRpUom() {
        return rpUom;
    }

    public void setRpUom(String rpUom) {
        this.rpUom = rpUom;
    }

    public Double getMaxLimit() {
        return maxLimit;
    }

    public void setMaxLimit(Double maxLimit) {
        this.maxLimit = maxLimit;
    }

    public Double getMinLimit() {
        return minLimit;
    }

    public void setMinLimit(Double minLimit) {
        this.minLimit = minLimit;
    }

    public String getIsOverAlloc() {
        return isOverAlloc;
    }

    public void setIsOverAlloc(String isOverAlloc) {
        this.isOverAlloc = isOverAlloc;
    }

    public String getIsAutoRp() {
        return isAutoRp;
    }

    public void setIsAutoRp(String isAutoRp) {
        this.isAutoRp = isAutoRp;
    }

    public String getIsRpAlloc() {
        return isRpAlloc;
    }

    public void setIsRpAlloc(String isRpAlloc) {
        this.isRpAlloc = isRpAlloc;
    }

    public String getIsOverRp() {
        return isOverRp;
    }

    public void setIsOverRp(String isOverRp) {
        this.isOverRp = isOverRp;
    }

    public String getIsFmRs() {
        return isFmRs;
    }

    public void setIsFmRs(String isFmRs) {
        this.isFmRs = isFmRs;
    }

    public String getIsFmCs() {
        return isFmCs;
    }

    public void setIsFmCs(String isFmCs) {
        this.isFmCs = isFmCs;
    }

    public String getDataSet() {
        return dataSet;
    }

    public void setDataSet(String dataSet) {
        this.dataSet = dataSet;
    }

    public String getHeaderId() {
        return headerId;
    }

    public void setHeaderId(String headerId) {
        this.headerId = headerId;
    }

}