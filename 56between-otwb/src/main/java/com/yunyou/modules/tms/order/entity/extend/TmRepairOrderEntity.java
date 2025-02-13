package com.yunyou.modules.tms.order.entity.extend;

import java.util.List;

import com.yunyou.modules.tms.order.entity.TmRepairOrderHeader;

public class TmRepairOrderEntity extends TmRepairOrderHeader {
    private static final long serialVersionUID = 7757542238506265988L;
    private String ownerName;    // 客户名称
    private String driverName;      // 司机名称
    private List<TmRepairOrderDetailEntity> tmRepairOrderDetailList;

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public List<TmRepairOrderDetailEntity> getTmRepairOrderDetailList() {
        return tmRepairOrderDetailList;
    }

    public void setTmRepairOrderDetailList(List<TmRepairOrderDetailEntity> tmRepairOrderDetailList) {
        this.tmRepairOrderDetailList = tmRepairOrderDetailList;
    }
}
