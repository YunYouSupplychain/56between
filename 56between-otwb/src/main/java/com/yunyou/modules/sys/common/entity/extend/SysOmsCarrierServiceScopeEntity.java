package com.yunyou.modules.sys.common.entity.extend;

import com.yunyou.modules.sys.common.entity.SysOmsCarrierServiceScope;

public class SysOmsCarrierServiceScopeEntity extends SysOmsCarrierServiceScope {
    // 承运商名称
    private String carrierName;
    // 业务服务范围名称
    private String groupName;
    // 货主名称
    private String ownerName;

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

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
}
