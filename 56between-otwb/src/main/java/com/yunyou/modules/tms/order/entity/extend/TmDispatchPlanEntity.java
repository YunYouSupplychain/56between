package com.yunyou.modules.tms.order.entity.extend;

import com.yunyou.modules.tms.order.entity.TmDispatchPlanHeader;

import java.util.Date;
import java.util.List;

public class TmDispatchPlanEntity extends TmDispatchPlanHeader {
    private static final long serialVersionUID = 1L;
    // 调度配置
    private List<TmDispatchPlanConfigEntity> detailList;
    // 承运商名称
    private String carrierName;
    // 派车网点名称
    private String dispatchOutletName;
    // 调度时间从
    private Date dispatchTimeFm;
    // 调度时间到
    private Date dispatchTimeTo;
    //
    private String vehicleAndTrips;
    // 需求计划时间从
    private Date demandBeginTime;
    // 需求计划时间到
    private Date demandEndTime;
    // 创建时间从
    private Date createDateFm;
    // 创建时间到
    private Date createDateTo;
    // 需求计划单号
    private String demandPlanNos;
    // 车牌号
    private String carNos;

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

    public String getDispatchOutletName() {
        return dispatchOutletName;
    }

    public void setDispatchOutletName(String dispatchOutletName) {
        this.dispatchOutletName = dispatchOutletName;
    }

    public String getVehicleAndTrips() {
        return vehicleAndTrips;
    }

    public void setVehicleAndTrips(String vehicleAndTrips) {
        this.vehicleAndTrips = vehicleAndTrips;
    }

    public List<TmDispatchPlanConfigEntity> getDetailList() {
        return detailList;
    }

    public void setDetailList(List<TmDispatchPlanConfigEntity> detailList) {
        this.detailList = detailList;
    }

    public Date getDemandBeginTime() {
        return demandBeginTime;
    }

    public void setDemandBeginTime(Date demandBeginTime) {
        this.demandBeginTime = demandBeginTime;
    }

    public Date getDemandEndTime() {
        return demandEndTime;
    }

    public void setDemandEndTime(Date demandEndTime) {
        this.demandEndTime = demandEndTime;
    }

    public Date getCreateDateFm() {
        return createDateFm;
    }

    public void setCreateDateFm(Date createDateFm) {
        this.createDateFm = createDateFm;
    }

    public Date getCreateDateTo() {
        return createDateTo;
    }

    public void setCreateDateTo(Date createDateTo) {
        this.createDateTo = createDateTo;
    }

    public String getDemandPlanNos() {
        return demandPlanNos;
    }

    public void setDemandPlanNos(String demandPlanNos) {
        this.demandPlanNos = demandPlanNos;
    }

    public String getCarNos() {
        return carNos;
    }

    public void setCarNos(String carNos) {
        this.carNos = carNos;
    }
}
