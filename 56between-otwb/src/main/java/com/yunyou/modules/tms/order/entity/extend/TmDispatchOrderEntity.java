package com.yunyou.modules.tms.order.entity.extend;

import com.google.common.collect.Lists;
import com.yunyou.modules.tms.order.entity.TmDispatchOrderHeader;

import java.util.Date;
import java.util.List;

public class TmDispatchOrderEntity extends TmDispatchOrderHeader {
    private static final long serialVersionUID = -5516472048084033640L;

    private Date dispatchTimeFm;
    private Date dispatchTimeTo;
    private Date departureTimeFm;
    private Date departureTimeTo;

    private String transportNo;
    private String carrierName; // 承运商名称
    private String driverName; // 司机姓名
    private String copilotName; // 副驾驶名称
    private String dispatchOutletName; // 派车网点名称
    /**
     * 起始地区域名称
     */
    private String startAreaName;
    /**
     * 目的地区域名称
     */
    private String endAreaName;
    private String orgName;
    private List<TmDispatchOrderSiteEntity> tmDispatchOrderSiteList = Lists.newArrayList(); // 配送点信息
    private List<TmDispatchOrderLabelEntity> tmDispatchOrderLabelList = Lists.newArrayList(); // 标签信息
    private List<String> dispatchNoList = Lists.newArrayList();
    private List<String> statusList = Lists.newArrayList();

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

    public Date getDepartureTimeFm() {
        return departureTimeFm;
    }

    public void setDepartureTimeFm(Date departureTimeFm) {
        this.departureTimeFm = departureTimeFm;
    }

    public Date getDepartureTimeTo() {
        return departureTimeTo;
    }

    public void setDepartureTimeTo(Date departureTimeTo) {
        this.departureTimeTo = departureTimeTo;
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

    public List<TmDispatchOrderSiteEntity> getTmDispatchOrderSiteList() {
        return tmDispatchOrderSiteList;
    }

    public void setTmDispatchOrderSiteList(List<TmDispatchOrderSiteEntity> tmDispatchOrderSiteList) {
        this.tmDispatchOrderSiteList = tmDispatchOrderSiteList;
    }

    public List<TmDispatchOrderLabelEntity> getTmDispatchOrderLabelList() {
        return tmDispatchOrderLabelList;
    }

    public void setTmDispatchOrderLabelList(List<TmDispatchOrderLabelEntity> tmDispatchOrderLabelList) {
        this.tmDispatchOrderLabelList = tmDispatchOrderLabelList;
    }

    public String getStartAreaName() {
        return startAreaName;
    }

    public void setStartAreaName(String startAreaName) {
        this.startAreaName = startAreaName;
    }

    public String getEndAreaName() {
        return endAreaName;
    }

    public void setEndAreaName(String endAreaName) {
        this.endAreaName = endAreaName;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getCopilotName() {
        return copilotName;
    }

    public void setCopilotName(String copilotName) {
        this.copilotName = copilotName;
    }

    public String getTransportNo() {
        return transportNo;
    }

    public void setTransportNo(String transportNo) {
        this.transportNo = transportNo;
    }

    public List<String> getDispatchNoList() {
        return dispatchNoList;
    }

    public void setDispatchNoList(List<String> dispatchNoList) {
        this.dispatchNoList = dispatchNoList;
    }

    public List<String> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<String> statusList) {
        this.statusList = statusList;
    }
}
