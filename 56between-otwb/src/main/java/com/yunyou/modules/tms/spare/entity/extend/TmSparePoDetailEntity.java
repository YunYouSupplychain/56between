package com.yunyou.modules.tms.spare.entity.extend;

import com.yunyou.modules.tms.spare.entity.TmSparePoDetail;

public class TmSparePoDetailEntity extends TmSparePoDetail {
    private static final long serialVersionUID = 1450219619231665958L;

    private String fittingName;
    private String fittingModel;
    private String supplierName;

    public String getFittingName() {
        return fittingName;
    }

    public void setFittingName(String fittingName) {
        this.fittingName = fittingName;
    }

    public String getFittingModel() {
        return fittingModel;
    }

    public void setFittingModel(String fittingModel) {
        this.fittingModel = fittingModel;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }
}
