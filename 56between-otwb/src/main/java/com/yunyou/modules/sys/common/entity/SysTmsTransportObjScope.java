package com.yunyou.modules.sys.common.entity;

import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.core.persistence.DataEntity;

/**
 * 业务对象服务范围Entity
 */
public class SysTmsTransportObjScope extends DataEntity<SysTmsTransportObjScope> {

    private static final long serialVersionUID = 1L;
    private String transportScopeCode;// 业务服务范围编码
    private String transportObjCode;// 业务对象编码
    private String transportScopeType;// 服务范围类型
    private Double maxLoadWeight;// 最大装载重量
    private Double maxLoadCubic;// 最大装载体积
    private Double maxAmount;// 最大金额
    private String dataSet;// 数据套

    // 扩展字段
    private String dataSetName;// 数据套名称
    private String oldTransportScopeCode;
    private String oldTransportObjCode;
    private String oldTransportScopeType;

    public SysTmsTransportObjScope() {
        super();
    }

    public SysTmsTransportObjScope(String id) {
        super(id);
    }

    public SysTmsTransportObjScope(String id, String dataSet) {
        super(id);
        this.dataSet = dataSet;
    }

    public SysTmsTransportObjScope(String transportScopeCode, String transportObjCode, String transportScopeType, String dataSet) {
        this.transportScopeCode = transportScopeCode;
        this.transportObjCode = transportObjCode;
        this.transportScopeType = transportScopeType;
        this.dataSet = dataSet;
    }

    @ExcelField(title = "业务服务范围编码", align = 2, sort = 7)
    public String getTransportScopeCode() {
        return transportScopeCode;
    }

    public void setTransportScopeCode(String transportScopeCode) {
        this.transportScopeCode = transportScopeCode;
    }

    @ExcelField(title = "业务对象编码", align = 2, sort = 8)
    public String getTransportObjCode() {
        return transportObjCode;
    }

    public void setTransportObjCode(String transportObjCode) {
        this.transportObjCode = transportObjCode;
    }

    @ExcelField(title = "最大装载重量", align = 2, sort = 9)
    public Double getMaxLoadWeight() {
        return maxLoadWeight;
    }

    public void setMaxLoadWeight(Double maxLoadWeight) {
        this.maxLoadWeight = maxLoadWeight;
    }

    @ExcelField(title = "最大装载体积", align = 2, sort = 10)
    public Double getMaxLoadCubic() {
        return maxLoadCubic;
    }

    public void setMaxLoadCubic(Double maxLoadCubic) {
        this.maxLoadCubic = maxLoadCubic;
    }

    @ExcelField(title = "最大金额", align = 2, sort = 11)
    public Double getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(Double maxAmount) {
        this.maxAmount = maxAmount;
    }

    public String getDataSet() {
        return dataSet;
    }

    public void setDataSet(String dataSet) {
        this.dataSet = dataSet;
    }

    @ExcelField(title = "服务范围类型", dictType = "TMS_TRANSPORT_SCOPE_TYPE", align = 2, sort = 13)
    public String getTransportScopeType() {
        return transportScopeType;
    }

    public void setTransportScopeType(String transportScopeType) {
        this.transportScopeType = transportScopeType;
    }

    public String getDataSetName() {
        return dataSetName;
    }

    public void setDataSetName(String dataSetName) {
        this.dataSetName = dataSetName;
    }

    public String getOldTransportScopeCode() {
        return oldTransportScopeCode;
    }

    public void setOldTransportScopeCode(String oldTransportScopeCode) {
        this.oldTransportScopeCode = oldTransportScopeCode;
    }

    public String getOldTransportObjCode() {
        return oldTransportObjCode;
    }

    public void setOldTransportObjCode(String oldTransportObjCode) {
        this.oldTransportObjCode = oldTransportObjCode;
    }

    public String getOldTransportScopeType() {
        return oldTransportScopeType;
    }

    public void setOldTransportScopeType(String oldTransportScopeType) {
        this.oldTransportScopeType = oldTransportScopeType;
    }
}