package com.yunyou.modules.tms.spare.entity.extend;

import com.yunyou.modules.tms.spare.entity.TmSparePoScanInfo;

import java.util.Date;

public class TmSparePoScanInfoEntity extends TmSparePoScanInfo {
    private static final long serialVersionUID = -1109556807983170048L;

    private String fittingName;
    private String fittingModel;
    private String supplierName;
    private Date operateTimeFm;
    private Date operateTimeTo;

    public TmSparePoScanInfoEntity() {
    }

    public TmSparePoScanInfoEntity(String id) {
        super(id);
    }

    public TmSparePoScanInfoEntity(String sparePoNo, String orgId) {
        super(sparePoNo, orgId);
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

    public Date getOperateTimeFm() {
        return operateTimeFm;
    }

    public void setOperateTimeFm(Date operateTimeFm) {
        this.operateTimeFm = operateTimeFm;
    }

    public Date getOperateTimeTo() {
        return operateTimeTo;
    }

    public void setOperateTimeTo(Date operateTimeTo) {
        this.operateTimeTo = operateTimeTo;
    }
}
