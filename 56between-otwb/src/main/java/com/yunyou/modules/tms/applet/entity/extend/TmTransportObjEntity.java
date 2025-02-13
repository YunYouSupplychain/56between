package com.yunyou.modules.tms.applet.entity.extend;

import com.yunyou.modules.tms.basic.entity.TmTransportObj;

import java.util.Date;

public class TmTransportObjEntity extends TmTransportObj {
    private static final long serialVersionUID = 773928936668397429L;

    /*多字段匹配查询*/
    private String codeAndName;

    private String area; // 所属城市
    private String carrierMatchedOrg; // 承运商对应机构
    private String outletMatchedOrg; // 承运商对应机构

    private String carNo;

    private String driver;
    private Date searchDateFm;
    private Date searchDateTo;

    public String getCodeAndName() {
        return codeAndName;
    }

    public void setCodeAndName(String codeAndName) {
        this.codeAndName = codeAndName;
    }

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

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public Date getSearchDateFm() {
        return searchDateFm;
    }

    public void setSearchDateFm(Date searchDateFm) {
        this.searchDateFm = searchDateFm;
    }

    public Date getSearchDateTo() {
        return searchDateTo;
    }

    public void setSearchDateTo(Date searchDateTo) {
        this.searchDateTo = searchDateTo;
    }

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }
}
