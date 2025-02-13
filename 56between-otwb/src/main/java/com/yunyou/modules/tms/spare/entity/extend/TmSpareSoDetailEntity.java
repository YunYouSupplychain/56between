package com.yunyou.modules.tms.spare.entity.extend;

import com.yunyou.modules.tms.spare.entity.TmSpareSoDetail;

public class TmSpareSoDetailEntity extends TmSpareSoDetail {
    private static final long serialVersionUID = 326742712808062324L;

    private String fittingName;
    private String fittingModel;
    private String supplierName;

    public TmSpareSoDetailEntity() {
    }

    public TmSpareSoDetailEntity(String id) {
        super(id);
    }

    public TmSpareSoDetailEntity(String spareSoNo, String orgId) {
        super(spareSoNo, orgId);
    }

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
