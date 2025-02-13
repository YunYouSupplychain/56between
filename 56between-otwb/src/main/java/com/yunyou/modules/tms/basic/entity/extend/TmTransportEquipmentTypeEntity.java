package com.yunyou.modules.tms.basic.entity.extend;

import com.yunyou.modules.tms.basic.entity.TmTransportEquipmentType;

public class TmTransportEquipmentTypeEntity extends TmTransportEquipmentType {
    private static final long serialVersionUID = -1439261380151518470L;

    private String supplierName;

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }
}
