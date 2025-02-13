package com.yunyou.modules.tms.order.entity.extend;

import com.yunyou.modules.tms.order.entity.TmDispatchPlanVehicle;

public class TmDispatchPlanVehicleEntity extends TmDispatchPlanVehicle {

    /*多字段匹配查询*/
    private String codeAndName;
    // 车辆类型
    private String vehicleType;
    private String vehicleTypeDesc;
    // 设备类型
    private String equipmentTypeCode;
    private String equipmentTypeName;

    public String getCodeAndName() {
        return codeAndName;
    }

    public void setCodeAndName(String codeAndName) {
        this.codeAndName = codeAndName;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getVehicleTypeDesc() {
        return vehicleTypeDesc;
    }

    public void setVehicleTypeDesc(String vehicleTypeDesc) {
        this.vehicleTypeDesc = vehicleTypeDesc;
    }

    public String getEquipmentTypeCode() {
        return equipmentTypeCode;
    }

    public void setEquipmentTypeCode(String equipmentTypeCode) {
        this.equipmentTypeCode = equipmentTypeCode;
    }

    public String getEquipmentTypeName() {
        return equipmentTypeName;
    }

    public void setEquipmentTypeName(String equipmentTypeName) {
        this.equipmentTypeName = equipmentTypeName;
    }
}
