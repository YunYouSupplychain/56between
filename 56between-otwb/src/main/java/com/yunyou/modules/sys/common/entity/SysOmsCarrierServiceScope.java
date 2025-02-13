package com.yunyou.modules.sys.common.entity;

import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.core.persistence.DataEntity;

/**
 * 承运商服务范围Entity
 */
public class SysOmsCarrierServiceScope extends DataEntity<SysOmsCarrierServiceScope> {

    private static final long serialVersionUID = 1L;
    private String ownerCode;// 货主编码
    private String carrierCode;// 承运商编码
    private String groupCode;// 业务服务范围编码
    private Double maxWeight;// 最大重量
    private Double maxVolume;// 最大体积
    private Double maxCost;// 最大费用
    private String dataSet;// 数据套

    // 扩展字段
    private String dataSetName;// 数据套名称
    private String oldOwnerCode;// 修改前货主编码
    private String oldCarrierCode;// 修改前承运商编码
    private String oldGroupCode;// 修改前业务服务范围编码

    public SysOmsCarrierServiceScope() {
        super();
    }

    public SysOmsCarrierServiceScope(String id) {
        super(id);
    }

    public SysOmsCarrierServiceScope(String id, String dataSet) {
        super(id);
        this.dataSet = dataSet;
    }

    @ExcelField(title = "承运商编码", align = 2, sort = 7)
    public String getCarrierCode() {
        return carrierCode;
    }

    public void setCarrierCode(String carrierCode) {
        this.carrierCode = carrierCode;
    }

    @ExcelField(title = "业务服务范围编码", align = 2, sort = 8)
    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    @ExcelField(title = "最大重量", align = 2, sort = 9)
    public Double getMaxWeight() {
        return maxWeight;
    }

    public void setMaxWeight(Double maxWeight) {
        this.maxWeight = maxWeight;
    }

    @ExcelField(title = "最大体积", align = 2, sort = 10)
    public Double getMaxVolume() {
        return maxVolume;
    }

    public void setMaxVolume(Double maxVolume) {
        this.maxVolume = maxVolume;
    }

    @ExcelField(title = "最大费用", align = 2, sort = 11)
    public Double getMaxCost() {
        return maxCost;
    }

    public void setMaxCost(Double maxCost) {
        this.maxCost = maxCost;
    }

    public String getDataSet() {
        return dataSet;
    }

    public void setDataSet(String dataSet) {
        this.dataSet = dataSet;
    }

    public String getOwnerCode() {
        return ownerCode;
    }

    public void setOwnerCode(String ownerCode) {
        this.ownerCode = ownerCode;
    }

    public String getDataSetName() {
        return dataSetName;
    }

    public void setDataSetName(String dataSetName) {
        this.dataSetName = dataSetName;
    }

    public String getOldOwnerCode() {
        return oldOwnerCode;
    }

    public void setOldOwnerCode(String oldOwnerCode) {
        this.oldOwnerCode = oldOwnerCode;
    }

    public String getOldCarrierCode() {
        return oldCarrierCode;
    }

    public void setOldCarrierCode(String oldCarrierCode) {
        this.oldCarrierCode = oldCarrierCode;
    }

    public String getOldGroupCode() {
        return oldGroupCode;
    }

    public void setOldGroupCode(String oldGroupCode) {
        this.oldGroupCode = oldGroupCode;
    }
}