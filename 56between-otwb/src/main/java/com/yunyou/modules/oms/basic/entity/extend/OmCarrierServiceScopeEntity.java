package com.yunyou.modules.oms.basic.entity.extend;

import com.yunyou.modules.oms.basic.entity.OmCarrierServiceScope;

public class OmCarrierServiceScopeEntity extends OmCarrierServiceScope {
    // 承运商名称
    private String carrierName;
    // 业务服务范围名称
    private String groupName;
    // 机构名称
    private String orgName;

    public String getCarrierName() {
        return carrierName;
    }

    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }
}
