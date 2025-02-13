package com.yunyou.modules.sys.common.entity.extend;

import com.yunyou.modules.sys.common.entity.SysTmsCarrierRouteRelation;

public class SysTmsCarrierRouteRelationEntity extends SysTmsCarrierRouteRelation {
    private static final long serialVersionUID = 3320986936435564469L;

    private String carrierName; // 承运商名称
    private String origin; // 起始地
    private String destination; // 目的地

    public String getCarrierName() {
        return carrierName;
    }

    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

}
