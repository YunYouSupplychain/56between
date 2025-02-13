package com.yunyou.modules.tms.basic.entity;

import com.yunyou.core.persistence.DataEntity;

/**
 * 业务对象路由
 */
public class TmObjRoute extends DataEntity<TmObjRoute> {
    private static final long serialVersionUID = -7791924268754309419L;

    private String carrierCode;// 承运商编码
    private String startObjCode;// 业务对象编码(起点)
    private String startObjAddress;// 业务对象地址(起点)
    private String endObjCode;// 业务对象编码(终点)
    private String endObjAddress;// 业务对象地址(终点)
    private String auditStatus;// 审核状态
    private Double mileage;// 里程
    private String orgId;// 组织中心ID

    public TmObjRoute() {
        super();
    }

    public TmObjRoute(String id) {
        super(id);
    }

    public TmObjRoute(String carrierCode, String startObjCode, String endObjCode, String orgId) {
        this.carrierCode = carrierCode;
        this.startObjCode = startObjCode;
        this.endObjCode = endObjCode;
        this.orgId = orgId;
    }

    public String getCarrierCode() {
        return carrierCode;
    }

    public void setCarrierCode(String carrierCode) {
        this.carrierCode = carrierCode;
    }

    public String getStartObjCode() {
        return startObjCode;
    }

    public void setStartObjCode(String startObjCode) {
        this.startObjCode = startObjCode;
    }

    public String getStartObjAddress() {
        return startObjAddress;
    }

    public void setStartObjAddress(String startObjAddress) {
        this.startObjAddress = startObjAddress;
    }

    public String getEndObjCode() {
        return endObjCode;
    }

    public void setEndObjCode(String endObjCode) {
        this.endObjCode = endObjCode;
    }

    public String getEndObjAddress() {
        return endObjAddress;
    }

    public void setEndObjAddress(String endObjAddress) {
        this.endObjAddress = endObjAddress;
    }

    public String getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }

    public Double getMileage() {
        return mileage;
    }

    public void setMileage(Double mileage) {
        this.mileage = mileage;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
}
