package com.yunyou.modules.tms.basic.entity.extend;

import com.google.common.collect.Lists;
import com.yunyou.modules.tms.basic.entity.TmRouteScope;

import java.util.List;

public class TmRouteScopeEntity extends TmRouteScope {

    private static final long serialVersionUID = 6079834706003105087L;
    private List<TmRouteScopeObjEntity> objList = Lists.newArrayList();
    private String orgName;
    private String carrierName;

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getCarrierName() {
        return carrierName;
    }

    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
    }

    public List<TmRouteScopeObjEntity> getObjList() {
        return objList;
    }

    public void setObjList(List<TmRouteScopeObjEntity> objList) {
        this.objList = objList;
    }
}
