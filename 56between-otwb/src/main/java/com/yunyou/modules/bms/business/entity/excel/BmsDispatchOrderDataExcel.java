package com.yunyou.modules.bms.business.entity.excel;

import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.common.utils.time.DateFormatUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * 描述：入库数据验收金额导入
 *
 * @author Jianhua
 * @version 2019/6/24
 */
public class BmsDispatchOrderDataExcel implements Serializable {

    private static final long serialVersionUID = -6691112563435527018L;
    @ExcelField(title = "派车单号")
    private String orderNo;
    @ExcelField(title = "派车单类型", dictType = "TMS_DISPATCH_TYPE")
    private String orderType;
    @ExcelField(title = "订单日期", format = DateFormatUtil.PATTERN_ISO_ON_DATE)
    private Date orderDate;
    @ExcelField(title = "派车时间")
    private Date dispatchTime;
    @ExcelField(title = "运输方式", dictType = "TMS_TRANSPORT_METHOD")
    private String tranType;
    @ExcelField(title = "承运商编码")
    private String carrierCode;
    @ExcelField(title = "承运商名称")
    private String carrierName;
    @ExcelField(title = "车型", dictType = "TMS_CAR_TYPE")
    private String carType;
    @ExcelField(title = "车牌号")
    private String vehicleNo;
    @ExcelField(title = "驾驶员编码")
    private String driverCode;
    @ExcelField(title = "驾驶员名称")
    private String driverName;
    @ExcelField(title = "调度员")
    private String dispatcher;
    @ExcelField(title = "总EA数")
    private Double totalQtyEa;
    @ExcelField(title = "总框数")
    private Double totalQtyFrame;
    @ExcelField(title = "签收人")
    private String signBy;
    @ExcelField(title = "签收时间")
    private Date signTime;
    @ExcelField(title = "配送顺序")
    private String dispatchSeq;
    @ExcelField(title = "网点编码")
    private String outletCode;
    @ExcelField(title = "网点名称")
    private String outletName;
    @ExcelField(title = "网点所属城市编码")
    private String areaCode;
    // 网点所属城市ID
    private String areaId;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Date getDispatchTime() {
        return dispatchTime;
    }

    public void setDispatchTime(Date dispatchTime) {
        this.dispatchTime = dispatchTime;
    }

    public String getCarrierCode() {
        return carrierCode;
    }

    public void setCarrierCode(String carrierCode) {
        this.carrierCode = carrierCode;
    }

    public String getCarrierName() {
        return carrierName;
    }

    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getDriverCode() {
        return driverCode;
    }

    public void setDriverCode(String driverCode) {
        this.driverCode = driverCode;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDispatcher() {
        return dispatcher;
    }

    public void setDispatcher(String dispatcher) {
        this.dispatcher = dispatcher;
    }

    public String getTranType() {
        return tranType;
    }

    public void setTranType(String tranType) {
        this.tranType = tranType;
    }

    public Double getTotalQtyEa() {
        return totalQtyEa;
    }

    public void setTotalQtyEa(Double totalQtyEa) {
        this.totalQtyEa = totalQtyEa;
    }

    public Double getTotalQtyFrame() {
        return totalQtyFrame;
    }

    public void setTotalQtyFrame(Double totalQtyFrame) {
        this.totalQtyFrame = totalQtyFrame;
    }

    public String getSignBy() {
        return signBy;
    }

    public void setSignBy(String signBy) {
        this.signBy = signBy;
    }

    public Date getSignTime() {
        return signTime;
    }

    public void setSignTime(Date signTime) {
        this.signTime = signTime;
    }

    public String getDispatchSeq() {
        return dispatchSeq;
    }

    public void setDispatchSeq(String dispatchSeq) {
        this.dispatchSeq = dispatchSeq;
    }

    public String getOutletCode() {
        return outletCode;
    }

    public void setOutletCode(String outletCode) {
        this.outletCode = outletCode;
    }

    public String getOutletName() {
        return outletName;
    }

    public void setOutletName(String outletName) {
        this.outletName = outletName;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }
}
