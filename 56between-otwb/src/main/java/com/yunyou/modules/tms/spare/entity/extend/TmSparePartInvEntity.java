package com.yunyou.modules.tms.spare.entity.extend;

import com.yunyou.modules.tms.spare.entity.TmSparePartInv;

import java.util.Date;

public class TmSparePartInvEntity extends TmSparePartInv {
    private static final long serialVersionUID = -7356974595382225631L;

    private String fittingName;
    private String fittingModel;    // 规格型号
    private String supplierName;
    private Date inboundTimeFm;
    private Date inboundTimeTo;

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

    public Date getInboundTimeFm() {
        return inboundTimeFm;
    }

    public void setInboundTimeFm(Date inboundTimeFm) {
        this.inboundTimeFm = inboundTimeFm;
    }

    public Date getInboundTimeTo() {
        return inboundTimeTo;
    }

    public void setInboundTimeTo(Date inboundTimeTo) {
        this.inboundTimeTo = inboundTimeTo;
    }
}
