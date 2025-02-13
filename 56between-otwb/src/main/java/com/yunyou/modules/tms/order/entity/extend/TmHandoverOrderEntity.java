package com.yunyou.modules.tms.order.entity.extend;

import com.google.common.collect.Lists;
import com.yunyou.modules.tms.order.entity.TmHandoverOrderHeader;

import java.util.Date;
import java.util.List;

public class TmHandoverOrderEntity extends TmHandoverOrderHeader {
    private static final long serialVersionUID = 1L;

    private Date dispatchTimeFm;
    private Date dispatchTimeTo;

    private String carrierName; // 承运商名称
    private String driverName; // 司机姓名
    private String dispatchOutletName;     // 派车网点名称
    private String deliveryOutletName;     // 配送网点名称

    private List<TmHandoverOrderLabelEntity> tmHandoverOrderLabelList = Lists.newArrayList(); // 标签信息
    private List<TmHandoverOrderSkuEntity> tmHandoverOrderSkuList = Lists.newArrayList();

    public Date getDispatchTimeFm() {
        return dispatchTimeFm;
    }

    public void setDispatchTimeFm(Date dispatchTimeFm) {
        this.dispatchTimeFm = dispatchTimeFm;
    }

    public Date getDispatchTimeTo() {
        return dispatchTimeTo;
    }

    public void setDispatchTimeTo(Date dispatchTimeTo) {
        this.dispatchTimeTo = dispatchTimeTo;
    }

    public String getCarrierName() {
        return carrierName;
    }

    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDispatchOutletName() {
        return dispatchOutletName;
    }

    public void setDispatchOutletName(String dispatchOutletName) {
        this.dispatchOutletName = dispatchOutletName;
    }

    public String getDeliveryOutletName() {
        return deliveryOutletName;
    }

    public void setDeliveryOutletName(String deliveryOutletName) {
        this.deliveryOutletName = deliveryOutletName;
    }

    public List<TmHandoverOrderLabelEntity> getTmHandoverOrderLabelList() {
        return tmHandoverOrderLabelList;
    }

    public void setTmHandoverOrderLabelList(List<TmHandoverOrderLabelEntity> tmHandoverOrderLabelList) {
        this.tmHandoverOrderLabelList = tmHandoverOrderLabelList;
    }

    public List<TmHandoverOrderSkuEntity> getTmHandoverOrderSkuList() {
        return tmHandoverOrderSkuList;
    }

    public void setTmHandoverOrderSkuList(List<TmHandoverOrderSkuEntity> tmHandoverOrderSkuList) {
        this.tmHandoverOrderSkuList = tmHandoverOrderSkuList;
    }
}
