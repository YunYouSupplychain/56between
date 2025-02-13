package com.yunyou.modules.tms.basic.entity.extend;

import com.yunyou.modules.tms.basic.entity.TmTransportObj;

public class TmTransportObjEntity extends TmTransportObj {
    private static final long serialVersionUID = 773928936668397429L;

    private String area; // 所属城市
    private String carrierMatchedOrg; // 承运商对应机构
    private String outletMatchedOrg; // 承运商对应机构
    private String route;       // 路线全名称
    private String orgName;     // 机构
    private String settleName;  // 结算对象名称
    private String isCarrierServiceScope;
    private String carrierCode;

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCarrierMatchedOrg() {
        return carrierMatchedOrg;
    }

    public void setCarrierMatchedOrg(String carrierMatchedOrg) {
        this.carrierMatchedOrg = carrierMatchedOrg;
    }

    public String getOutletMatchedOrg() {
        return outletMatchedOrg;
    }

    public void setOutletMatchedOrg(String outletMatchedOrg) {
        this.outletMatchedOrg = outletMatchedOrg;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getSettleName() {
        return settleName;
    }

    public void setSettleName(String settleName) {
        this.settleName = settleName;
    }

    public String getIsCarrierServiceScope() {
        return isCarrierServiceScope;
    }

    public void setIsCarrierServiceScope(String isCarrierServiceScope) {
        this.isCarrierServiceScope = isCarrierServiceScope;
    }

    public String getCarrierCode() {
        return carrierCode;
    }

    public void setCarrierCode(String carrierCode) {
        this.carrierCode = carrierCode;
    }
}
