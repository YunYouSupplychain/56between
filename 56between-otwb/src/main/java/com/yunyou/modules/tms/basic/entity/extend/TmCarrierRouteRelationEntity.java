package com.yunyou.modules.tms.basic.entity.extend;

import com.yunyou.modules.tms.basic.entity.TmCarrierRouteRelation;

public class TmCarrierRouteRelationEntity extends TmCarrierRouteRelation {
    private static final long serialVersionUID = 3320986936435564469L;

    /*多字段匹配查询*/
    private String codeAndName;

    private String carrierName; // 承运商名称
    private String origin; // 起始地
    private String destination; // 目的地

    public String getCodeAndName() {
        return codeAndName;
    }

    public void setCodeAndName(String codeAndName) {
        this.codeAndName = codeAndName;
    }

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
