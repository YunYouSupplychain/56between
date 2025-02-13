package com.yunyou.modules.tms.order.entity.extend;

import com.yunyou.modules.tms.order.entity.TmTransportOrderDelivery;

public class TmTransportOrderDeliveryEntity extends TmTransportOrderDelivery {
    private static final long serialVersionUID = -7070783159457403636L;
    private String carrierName;// 承运商
    private String carTypeName;// 车型

    public String getCarrierName() {
        return carrierName;
    }

    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
    }

    public String getCarTypeName() {
        return carTypeName;
    }

    public void setCarTypeName(String carTypeName) {
        this.carTypeName = carTypeName;
    }
}
