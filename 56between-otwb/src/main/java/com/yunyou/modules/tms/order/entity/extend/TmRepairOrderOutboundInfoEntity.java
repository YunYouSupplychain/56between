package com.yunyou.modules.tms.order.entity.extend;

import com.yunyou.modules.tms.order.entity.TmRepairOrderOutboundInfo;

public class TmRepairOrderOutboundInfoEntity extends TmRepairOrderOutboundInfo {
    private static final long serialVersionUID = 1L;
    private String fittingName;     // 备件名称
    private String fittingModel;    // 规格型号
    private String supplierName;    // 供应商名称

    public TmRepairOrderOutboundInfoEntity() {
    }

    public TmRepairOrderOutboundInfoEntity(String id) {
        super(id);
    }

    public String getFittingModel() {
        return fittingModel;
    }

    public void setFittingModel(String fittingModel) {
        this.fittingModel = fittingModel;
    }

    public String getFittingName() {
        return fittingName;
    }

    public void setFittingName(String fittingName) {
        this.fittingName = fittingName;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }
}
