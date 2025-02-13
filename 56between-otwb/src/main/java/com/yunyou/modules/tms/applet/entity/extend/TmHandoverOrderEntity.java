package com.yunyou.modules.tms.applet.entity.extend;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class TmHandoverOrderEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String remarks;
    private String handoverNo;		// 交接单号
    private String dispatchNo;		// 派车单号
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dispatchTime;		// 派车时间
    private String status;		// 交接单状态
    private String dispatchOutletCode;		// 派车网点
    private String deliveryOutletCode;		// 配送网点
    private String carrierCode;		// 承运商编码
    private String carNo;		// 车牌号
    private String driver;		// 司机
    private String driverTel;		// 联系电话
    private Long receivableQty;		// 应收件数
    private Long actualQty;		// 实收件数
    private String orgId;		// 机构ID
    private Long recVer = 0L;        // 版本号
    private String baseOrgId;   // 基础数据机构ID
    private String handoverPerson;	// 交接人
    private String trip;            // 车次

    private Date dispatchTimeFm;
    private Date dispatchTimeTo;

    private String carrierName; // 承运商名称
    private String driverName; // 司机姓名
    private String dispatchOutletName;     // 派车网点名称
    private String deliveryOutletName;     // 配送网点名称

    private List<TmHandoverOrderLabelEntity> labelList = Lists.newArrayList(); // 标签信息
    private List<TmHandoverOrderSkuEntity> skuList = Lists.newArrayList();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getHandoverNo() {
        return handoverNo;
    }

    public void setHandoverNo(String handoverNo) {
        this.handoverNo = handoverNo;
    }

    public String getDispatchNo() {
        return dispatchNo;
    }

    public void setDispatchNo(String dispatchNo) {
        this.dispatchNo = dispatchNo;
    }

    public Date getDispatchTime() {
        return dispatchTime;
    }

    public void setDispatchTime(Date dispatchTime) {
        this.dispatchTime = dispatchTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDispatchOutletCode() {
        return dispatchOutletCode;
    }

    public void setDispatchOutletCode(String dispatchOutletCode) {
        this.dispatchOutletCode = dispatchOutletCode;
    }

    public String getDeliveryOutletCode() {
        return deliveryOutletCode;
    }

    public void setDeliveryOutletCode(String deliveryOutletCode) {
        this.deliveryOutletCode = deliveryOutletCode;
    }

    public String getCarrierCode() {
        return carrierCode;
    }

    public void setCarrierCode(String carrierCode) {
        this.carrierCode = carrierCode;
    }

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getDriverTel() {
        return driverTel;
    }

    public void setDriverTel(String driverTel) {
        this.driverTel = driverTel;
    }

    public Long getReceivableQty() {
        return receivableQty;
    }

    public void setReceivableQty(Long receivableQty) {
        this.receivableQty = receivableQty;
    }

    public Long getActualQty() {
        return actualQty;
    }

    public void setActualQty(Long actualQty) {
        this.actualQty = actualQty;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public Long getRecVer() {
        return recVer;
    }

    public void setRecVer(Long recVer) {
        this.recVer = recVer;
    }

    public String getBaseOrgId() {
        return baseOrgId;
    }

    public void setBaseOrgId(String baseOrgId) {
        this.baseOrgId = baseOrgId;
    }

    public String getHandoverPerson() {
        return handoverPerson;
    }

    public void setHandoverPerson(String handoverPerson) {
        this.handoverPerson = handoverPerson;
    }

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

    public List<TmHandoverOrderLabelEntity> getLabelList() {
        return labelList;
    }

    public void setLabelList(List<TmHandoverOrderLabelEntity> labelList) {
        this.labelList = labelList;
    }

    public List<TmHandoverOrderSkuEntity> getSkuList() {
        return skuList;
    }

    public void setSkuList(List<TmHandoverOrderSkuEntity> skuList) {
        this.skuList = skuList;
    }

    public String getTrip() {
        return trip;
    }

    public void setTrip(String trip) {
        this.trip = trip;
    }
}
