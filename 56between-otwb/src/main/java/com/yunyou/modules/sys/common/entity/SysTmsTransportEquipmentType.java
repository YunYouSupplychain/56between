package com.yunyou.modules.sys.common.entity;

import com.google.common.collect.Lists;
import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.core.persistence.DataEntity;

import java.util.List;

/**
 * 运输设备类型Entity
 */
public class SysTmsTransportEquipmentType extends DataEntity<SysTmsTransportEquipmentType> {

    private static final long serialVersionUID = 1L;
    private String transportEquipmentTypeCode;// 运输设备类型编码
    private String transportEquipmentTypeNameCn;// 中文名称
    private String transportEquipmentTypeNameEn;// 英文名称
    private String temperatureType;// 温度类别
    private String isTemperatureControl;// 是否温度控制
    private Double minAllowTemperature;// 最低允许温度
    private Double maxAllowTemperature;// 最高允许温度
    private String isHumidityControl;// 是否湿度控制
    private Double minAllowHumidity;// 最低允许湿度
    private Double maxAllowHumidity;// 最高允许湿度
    private String isContainer;// 是否集装箱
    private String onlyAllowLastInLastOut;// 只允许后进后出
    private String isFixedEquipmentSpace;// 设备空间是否固定
    private Double internalLength;// 内部长度
    private Double internalWidth;// 内部宽度
    private Double internalHeight;// 内部高度
    private Double externalLength;// 外部长度
    private Double externalWidth;// 外部宽度
    private Double externalHeight;// 外部高度
    private Double doorWidth;// 门宽
    private Double doorHeight;// 门高
    private Double minLoadWeight;// 最小载重量
    private Double maxLoadWeight;// 最大载重量
    private Double leftLimitMaxLoadWeight;// 左侧限制最大载重
    private Double rightLimitMaxLoadWeight;// 右侧限制最大载重
    private Double leftRightDiffLimitMaxLoadWeight;// 左右差异限制最大载重
    private Double minLoadCubic;// 最小装载容积
    private Double maxLoadCubic;// 最大装载容积
    private Double allowOverweightRate;// 允许超重比例
    private String dataSet;// 数据套
    // 运输设备空间信息列表
    private List<SysTmsTransportEquipmentSpace> transportEquipmentSpaceList = Lists.newArrayList();;

    private String dataSetName;// 数据套名称

    public SysTmsTransportEquipmentType() {
        super();
    }

    public SysTmsTransportEquipmentType(String id) {
        super(id);
    }

    public SysTmsTransportEquipmentType(String transportEquipmentTypeCode, String dataSet) {
        this.transportEquipmentTypeCode = transportEquipmentTypeCode;
        this.dataSet = dataSet;
    }

    @ExcelField(title = "运输设备类型编码", align = 2, sort = 7)
    public String getTransportEquipmentTypeCode() {
        return transportEquipmentTypeCode;
    }

    public void setTransportEquipmentTypeCode(String transportEquipmentTypeCode) {
        this.transportEquipmentTypeCode = transportEquipmentTypeCode;
    }

    @ExcelField(title = "中文名称", align = 2, sort = 8)
    public String getTransportEquipmentTypeNameCn() {
        return transportEquipmentTypeNameCn;
    }

    public void setTransportEquipmentTypeNameCn(String transportEquipmentTypeNameCn) {
        this.transportEquipmentTypeNameCn = transportEquipmentTypeNameCn;
    }

    @ExcelField(title = "英文名称", align = 2, sort = 9)
    public String getTransportEquipmentTypeNameEn() {
        return transportEquipmentTypeNameEn;
    }

    public void setTransportEquipmentTypeNameEn(String transportEquipmentTypeNameEn) {
        this.transportEquipmentTypeNameEn = transportEquipmentTypeNameEn;
    }

    @ExcelField(title = "温度类别", dictType = "TMS_TEMPERATURE_TYPE", align = 2, sort = 10)
    public String getTemperatureType() {
        return temperatureType;
    }

    public void setTemperatureType(String temperatureType) {
        this.temperatureType = temperatureType;
    }

    @ExcelField(title = "是否温度控制", dictType = "SYS_YES_NO", align = 2, sort = 11)
    public String getIsTemperatureControl() {
        return isTemperatureControl;
    }

    public void setIsTemperatureControl(String isTemperatureControl) {
        this.isTemperatureControl = isTemperatureControl;
    }

    @ExcelField(title = "最低允许温度", align = 2, sort = 12)
    public Double getMinAllowTemperature() {
        return minAllowTemperature;
    }

    public void setMinAllowTemperature(Double minAllowTemperature) {
        this.minAllowTemperature = minAllowTemperature;
    }

    @ExcelField(title = "最高允许温度", align = 2, sort = 13)
    public Double getMaxAllowTemperature() {
        return maxAllowTemperature;
    }

    public void setMaxAllowTemperature(Double maxAllowTemperature) {
        this.maxAllowTemperature = maxAllowTemperature;
    }

    @ExcelField(title = "是否湿度控制", dictType = "SYS_YES_NO", align = 2, sort = 14)
    public String getIsHumidityControl() {
        return isHumidityControl;
    }

    public void setIsHumidityControl(String isHumidityControl) {
        this.isHumidityControl = isHumidityControl;
    }

    @ExcelField(title = "最低允许湿度", align = 2, sort = 15)
    public Double getMinAllowHumidity() {
        return minAllowHumidity;
    }

    public void setMinAllowHumidity(Double minAllowHumidity) {
        this.minAllowHumidity = minAllowHumidity;
    }

    @ExcelField(title = "最高允许湿度", align = 2, sort = 16)
    public Double getMaxAllowHumidity() {
        return maxAllowHumidity;
    }

    public void setMaxAllowHumidity(Double maxAllowHumidity) {
        this.maxAllowHumidity = maxAllowHumidity;
    }

    @ExcelField(title = "是否集装箱", dictType = "SYS_YES_NO", align = 2, sort = 17)
    public String getIsContainer() {
        return isContainer;
    }

    public void setIsContainer(String isContainer) {
        this.isContainer = isContainer;
    }

    @ExcelField(title = "只允许后进后出", dictType = "SYS_YES_NO", align = 2, sort = 18)
    public String getOnlyAllowLastInLastOut() {
        return onlyAllowLastInLastOut;
    }

    public void setOnlyAllowLastInLastOut(String onlyAllowLastInLastOut) {
        this.onlyAllowLastInLastOut = onlyAllowLastInLastOut;
    }

    @ExcelField(title = "设备空间是否固定", dictType = "SYS_YES_NO", align = 2, sort = 19)
    public String getIsFixedEquipmentSpace() {
        return isFixedEquipmentSpace;
    }

    public void setIsFixedEquipmentSpace(String isFixedEquipmentSpace) {
        this.isFixedEquipmentSpace = isFixedEquipmentSpace;
    }

    @ExcelField(title = "内部长度", align = 2, sort = 20)
    public Double getInternalLength() {
        return internalLength;
    }

    public void setInternalLength(Double internalLength) {
        this.internalLength = internalLength;
    }

    @ExcelField(title = "内部宽度", align = 2, sort = 21)
    public Double getInternalWidth() {
        return internalWidth;
    }

    public void setInternalWidth(Double internalWidth) {
        this.internalWidth = internalWidth;
    }

    @ExcelField(title = "内部高度", align = 2, sort = 22)
    public Double getInternalHeight() {
        return internalHeight;
    }

    public void setInternalHeight(Double internalHeight) {
        this.internalHeight = internalHeight;
    }

    @ExcelField(title = "外部长度", align = 2, sort = 23)
    public Double getExternalLength() {
        return externalLength;
    }

    public void setExternalLength(Double externalLength) {
        this.externalLength = externalLength;
    }

    @ExcelField(title = "外部宽度", align = 2, sort = 24)
    public Double getExternalWidth() {
        return externalWidth;
    }

    public void setExternalWidth(Double externalWidth) {
        this.externalWidth = externalWidth;
    }

    @ExcelField(title = "外部高度", align = 2, sort = 25)
    public Double getExternalHeight() {
        return externalHeight;
    }

    public void setExternalHeight(Double externalHeight) {
        this.externalHeight = externalHeight;
    }

    @ExcelField(title = "门宽", align = 2, sort = 26)
    public Double getDoorWidth() {
        return doorWidth;
    }

    public void setDoorWidth(Double doorWidth) {
        this.doorWidth = doorWidth;
    }

    @ExcelField(title = "门高", align = 2, sort = 27)
    public Double getDoorHeight() {
        return doorHeight;
    }

    public void setDoorHeight(Double doorHeight) {
        this.doorHeight = doorHeight;
    }

    @ExcelField(title = "最小载重量", align = 2, sort = 28)
    public Double getMinLoadWeight() {
        return minLoadWeight;
    }

    public void setMinLoadWeight(Double minLoadWeight) {
        this.minLoadWeight = minLoadWeight;
    }

    @ExcelField(title = "最大载重量", align = 2, sort = 29)
    public Double getMaxLoadWeight() {
        return maxLoadWeight;
    }

    public void setMaxLoadWeight(Double maxLoadWeight) {
        this.maxLoadWeight = maxLoadWeight;
    }

    @ExcelField(title = "左侧限制最大载重", align = 2, sort = 30)
    public Double getLeftLimitMaxLoadWeight() {
        return leftLimitMaxLoadWeight;
    }

    public void setLeftLimitMaxLoadWeight(Double leftLimitMaxLoadWeight) {
        this.leftLimitMaxLoadWeight = leftLimitMaxLoadWeight;
    }

    @ExcelField(title = "右侧限制最大载重", align = 2, sort = 31)
    public Double getRightLimitMaxLoadWeight() {
        return rightLimitMaxLoadWeight;
    }

    public void setRightLimitMaxLoadWeight(Double rightLimitMaxLoadWeight) {
        this.rightLimitMaxLoadWeight = rightLimitMaxLoadWeight;
    }

    @ExcelField(title = "左右差异限制最大载重", align = 2, sort = 32)
    public Double getLeftRightDiffLimitMaxLoadWeight() {
        return leftRightDiffLimitMaxLoadWeight;
    }

    public void setLeftRightDiffLimitMaxLoadWeight(Double leftRightDiffLimitMaxLoadWeight) {
        this.leftRightDiffLimitMaxLoadWeight = leftRightDiffLimitMaxLoadWeight;
    }

    @ExcelField(title = "最小装载容积", align = 2, sort = 33)
    public Double getMinLoadCubic() {
        return minLoadCubic;
    }

    public void setMinLoadCubic(Double minLoadCubic) {
        this.minLoadCubic = minLoadCubic;
    }

    @ExcelField(title = "最大装载容积", align = 2, sort = 34)
    public Double getMaxLoadCubic() {
        return maxLoadCubic;
    }

    public void setMaxLoadCubic(Double maxLoadCubic) {
        this.maxLoadCubic = maxLoadCubic;
    }

    @ExcelField(title = "允许超重比例(%)", align = 2, sort = 35)
    public Double getAllowOverweightRate() {
        return allowOverweightRate;
    }

    public void setAllowOverweightRate(Double allowOverweightRate) {
        this.allowOverweightRate = allowOverweightRate;
    }

    public String getDataSet() {
        return dataSet;
    }

    public void setDataSet(String dataSet) {
        this.dataSet = dataSet;
    }

    public List<SysTmsTransportEquipmentSpace> getTransportEquipmentSpaceList() {
        return transportEquipmentSpaceList;
    }

    public void setTransportEquipmentSpaceList(List<SysTmsTransportEquipmentSpace> transportEquipmentSpaceList) {
        this.transportEquipmentSpaceList = transportEquipmentSpaceList;
    }

    public String getDataSetName() {
        return dataSetName;
    }

    public void setDataSetName(String dataSetName) {
        this.dataSetName = dataSetName;
    }
}