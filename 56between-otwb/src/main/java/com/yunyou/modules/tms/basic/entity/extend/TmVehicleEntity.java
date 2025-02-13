package com.yunyou.modules.tms.basic.entity.extend;

import com.yunyou.modules.tms.basic.entity.TmVehicle;

public class TmVehicleEntity extends TmVehicle {
    private static final long serialVersionUID = 909582494279328887L;

    private String dispatchCenterId;// 调度中心ID
    private String opOrgId;         // 操作机构ID

    private String transportEquipmentTypeName; // 运输设备类型名称
    private String carrierName;     // 承运商名称
    private String supplierName;    // 供应商名称
    private String mainDriverName;  // 主驾驶
    private String mainDriverTel;   // 主驾驶电话
    private String copilotName;     // 副驾驶
    private String dispatchBaseName;// 调度基地名称
    private String vehicleTypeDesc; // 车辆类型
    private String vehicleTypeName; // 车型名称

    public String getDispatchCenterId() {
        return dispatchCenterId;
    }

    public void setDispatchCenterId(String dispatchCenterId) {
        this.dispatchCenterId = dispatchCenterId;
    }

    public String getOpOrgId() {
        return opOrgId;
    }

    public void setOpOrgId(String opOrgId) {
        this.opOrgId = opOrgId;
    }

    public String getTransportEquipmentTypeName() {
        return transportEquipmentTypeName;
    }

    public void setTransportEquipmentTypeName(String transportEquipmentTypeName) {
        this.transportEquipmentTypeName = transportEquipmentTypeName;
    }

    public String getCarrierName() {
        return carrierName;
    }

    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getMainDriverName() {
        return mainDriverName;
    }

    public void setMainDriverName(String mainDriverName) {
        this.mainDriverName = mainDriverName;
    }

    public String getCopilotName() {
        return copilotName;
    }

    public void setCopilotName(String copilotName) {
        this.copilotName = copilotName;
    }

    public String getDispatchBaseName() {
        return dispatchBaseName;
    }

    public void setDispatchBaseName(String dispatchBaseName) {
        this.dispatchBaseName = dispatchBaseName;
    }

    public String getMainDriverTel() {
        return mainDriverTel;
    }

    public void setMainDriverTel(String mainDriverTel) {
        this.mainDriverTel = mainDriverTel;
    }

    public String getVehicleTypeDesc() {
        return vehicleTypeDesc;
    }

    public void setVehicleTypeDesc(String vehicleTypeDesc) {
        this.vehicleTypeDesc = vehicleTypeDesc;
    }

    public String getVehicleTypeName() {
        return vehicleTypeName;
    }

    public void setVehicleTypeName(String vehicleTypeName) {
        this.vehicleTypeName = vehicleTypeName;
    }
}
