package com.yunyou.modules.wms.report.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoAlloc;

import java.util.Date;

public class WmShipDataEntity extends BanQinWmSoAlloc {
    private String soType;
    private Date orderTime;
    private String ownerName;
    private String deliveryName;
    private String deliveryTel;
    private String deliveryZip;
    private String deliveryArea;
    private String deliveryAddress;
    private String consignee;
    private String consigneeTel;
    private String consigneeAddress;
    private String consigneeZip;
    private String consigneeArea;
    private String businessNo;
    private String chainNo;
    private String taskNo;
    private String customerOrderNo;
    private String externalNo;
    private String skuName;
    private String carrierCode;
    private String carrierName;
    private String orgName;

    private Date shipTimeFm;
    private Date shipTimeTo;
    private Date orderTimeFm;
    private Date orderTimeTo;
    private String isTracking;

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getShipTimeFm() {
        return shipTimeFm;
    }

    public void setShipTimeFm(Date shipTimeFm) {
        this.shipTimeFm = shipTimeFm;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getShipTimeTo() {
        return shipTimeTo;
    }

    public void setShipTimeTo(Date shipTimeTo) {
        this.shipTimeTo = shipTimeTo;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getOrderTimeFm() {
        return orderTimeFm;
    }

    public void setOrderTimeFm(Date orderTimeFm) {
        this.orderTimeFm = orderTimeFm;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getOrderTimeTo() {
        return orderTimeTo;
    }

    public void setOrderTimeTo(Date orderTimeTo) {
        this.orderTimeTo = orderTimeTo;
    }

    public String getIsTracking() {
        return isTracking;
    }

    public void setIsTracking(String isTracking) {
        this.isTracking = isTracking;
    }

    @ExcelField(title = "出库单号", align = 2, sort = 7)
    public String getSoNo() {
        return super.getSoNo();
    }

    @ExcelField(title = "出库单类型", align = 2, sort = 8)
    public String getSoType() {
        return soType;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelField(title = "订单时间", align = 2, sort = 9)
    public Date getOrderTime() {
        return orderTime;
    }

    @ExcelField(title = "货主编码", align = 2, sort = 10)
    public String getOwnerCode() {
        return super.getOwnerCode();
    }

    @ExcelField(title = "货主名称", align = 2, sort = 11)
    public String getOwnerName() {
        return ownerName;
    }

    @ExcelField(title = "发货人", align = 2, sort = 12)
    public String getDeliveryName() {
        return deliveryName;
    }

    @ExcelField(title = "发货人电话", align = 2, sort = 13)
    public String getDeliveryTel() {
        return deliveryTel;
    }

    @ExcelField(title = "发货人邮编", align = 2, sort = 14)
    public String getDeliveryZip() {
        return deliveryZip;
    }

    @ExcelField(title = "发货人区域", align = 2, sort = 15)
    public String getDeliveryArea() {
        return deliveryArea;
    }

    @ExcelField(title = "发货人地址", align = 2, sort = 15)
    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    @ExcelField(title = "收货人", align = 2, sort = 16)
    public String getConsignee() {
        return consignee;
    }

    @ExcelField(title = "收货人电话", align = 2, sort = 17)
    public String getConsigneeTel() {
        return consigneeTel;
    }

    @ExcelField(title = "收货人地址", align = 2, sort = 18)
    public String getConsigneeAddress() {
        return consigneeAddress;
    }

    @ExcelField(title = "收货人邮编", align = 2, sort = 19)
    public String getConsigneeZip() {
        return consigneeZip;
    }

    @ExcelField(title = "收货人区域", align = 2, sort = 20)
    public String getConsigneeArea() {
        return consigneeArea;
    }

    @ExcelField(title = "商流订单号", align = 2, sort = 21)
    public String getBusinessNo() {
        return businessNo;
    }

    @ExcelField(title = "供应链订单号", align = 2, sort = 22)
    public String getChainNo() {
        return chainNo;
    }

    @ExcelField(title = "供应链任务号", align = 2, sort = 23)
    public String getTaskNo() {
        return taskNo;
    }

    @ExcelField(title = "客户订单号", align = 2, sort = 24)
    public String getCustomerOrderNo() {
        return customerOrderNo;
    }

    @ExcelField(title = "外部单号", align = 2, sort = 25)
    public String getExternalNo() {
        return externalNo;
    }

    @ExcelField(title = "分配明细Id", align = 2, sort = 26)
    public String getAllocId() {
        return super.getAllocId();
    }

    @ExcelField(title = "波次单号", align = 2, sort = 27)
    public String getWaveNo() {
        return super.getWaveNo();
    }

    @ExcelField(title = "商品编码", align = 2, sort = 28)
    public String getSkuCode() {
        return super.getSkuCode();
    }

    @ExcelField(title = "商品名称", align = 2, sort = 29)
    public String getSkuName() {
        return skuName;
    }

    @ExcelField(title = "库位编码", align = 2, sort = 30)
    public String getLocCode() {
        return super.getLocCode();
    }

    @ExcelField(title = "跟踪号", align = 2, sort = 31)
    public String getTraceId() {
        return super.getTraceId();
    }

    @ExcelField(title = "数量", align = 2, sort = 32)
    public Double getQtyEa() {
        return super.getQtyEa();
    }

    @ExcelField(title = "目标库位编码", align = 2, sort = 33)
    public String getToLoc() {
        return super.getToLoc();
    }

    @ExcelField(title = "目标跟踪号", align = 2, sort = 34)
    public String getToId() {
        return super.getToId();
    }

    @ExcelField(title = "拣货人", align = 2, sort = 35)
    public String getPickOp() {
        return super.getPickOp();
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelField(title = "拣货时间", align = 2, sort = 36)
    public Date getPickTime() {
        return super.getPickTime();
    }

    @ExcelField(title = "复核人", align = 2, sort = 37)
    public String getCheckOp() {
        return super.getCheckOp();
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelField(title = "复核时间", align = 2, sort = 38)
    public Date getCheckTime() {
        return super.getCheckTime();
    }

    @ExcelField(title = "打包人", align = 2, sort = 39)
    public String getPackOp() {
        return super.getPackOp();
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelField(title = "打包时间", align = 2, sort = 40)
    public Date getPackTime() {
        return super.getPackTime();
    }

    @ExcelField(title = "发货人", align = 2, sort = 41)
    public String getShipOp() {
        return super.getShipOp();
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelField(title = "发货时间时间", align = 2, sort = 42)
    public Date getShipTime() {
        return super.getShipTime();
    }

    @ExcelField(title = "快递单号", align = 2, sort = 43)
    public String getTrackingNo() {
        return super.getTrackingNo();
    }

    @ExcelField(title = "打包箱号", align = 2, sort = 44)
    public String getCaseNo() {
        return super.getCaseNo();
    }

    @ExcelField(title = "承运商编码", align = 2, sort = 45)
    public String getCarrierCode() {
        return carrierCode;
    }

    @ExcelField(title = "承运商名称", align = 2, sort = 46)
    public String getCarrierName() {
        return carrierName;
    }

}
