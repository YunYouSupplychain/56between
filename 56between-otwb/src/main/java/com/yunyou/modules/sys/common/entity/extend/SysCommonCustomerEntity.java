package com.yunyou.modules.sys.common.entity.extend;

import com.google.common.collect.Lists;
import com.yunyou.modules.sys.common.entity.SysCommonCustomer;

import java.util.List;

public class SysCommonCustomerEntity extends SysCommonCustomer {

    private String areaName;// 所属城市
    private String dataSetName;// 数据套名称
    private String routeName;// 业务路线名称
    private String settleName;// 结算对象名称
    private String clerkName;// 业务员名称
    private String carrierMatchedOrg;// 承运商对应机构
    private String outletMatchedOrg;// 网点对应机构

    private List<String> ids;

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getDataSetName() {
        return dataSetName;
    }

    public void setDataSetName(String dataSetName) {
        this.dataSetName = dataSetName;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getSettleName() {
        return settleName;
    }

    public void setSettleName(String settleName) {
        this.settleName = settleName;
    }

    public String getClerkName() {
        return clerkName;
    }

    public void setClerkName(String clerkName) {
        this.clerkName = clerkName;
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

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }
}
