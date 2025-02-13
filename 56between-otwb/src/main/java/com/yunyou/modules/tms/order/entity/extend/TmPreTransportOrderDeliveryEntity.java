package com.yunyou.modules.tms.order.entity.extend;

import com.yunyou.modules.tms.order.entity.TmPreTransportOrderDelivery;

public class TmPreTransportOrderDeliveryEntity extends TmPreTransportOrderDelivery {
    private static final long serialVersionUID = -7070783159457403636L;
    private String carrierName;// 承运商

    public String getCarrierName() {
        return carrierName;
    }

    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
    }
}
