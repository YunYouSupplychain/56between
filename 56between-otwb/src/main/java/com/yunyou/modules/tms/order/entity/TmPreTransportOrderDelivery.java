package com.yunyou.modules.tms.order.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.core.persistence.DataEntity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 运输订单配送信息Entity
 *
 * @author liujianhua
 * @version 2020-03-04
 */
public class TmPreTransportOrderDelivery extends DataEntity<TmPreTransportOrderDelivery> {

    private static final long serialVersionUID = 1L;
    private String transportNo;// 运输单号
    private String carrierCode;// 承运商编码
    private Double totalEaQty;// 件数
    private Double totalPlQty;// 托盘数
    private Double totalWeight;// 重量
    private Double totalCubic;// 体积
    private BigDecimal paidAmount;// 到付金额
    private Double actualShipWeight;// 实际发货重量
    private String virtualBillingStatus;// 虚拟开单状态
    private Date planArriveTime;// 预计到达时间
    private String isException;// 是否异常
    private String signStatus;// 签收状态
    private Date signTime;// 签收时间
    private String signBy;// 签收人
    private String signRemarks;// 签收备注
    private String receiptStatus;// 回单状态
    private Date receiptTime;// 回单时间
    private String receiptBy;// 回单人
    private String receiptRemarks;// 回单备注
    private Integer receiptCount;// 回单份数
    private String orgId;// 机构ID
    private String baseOrgId;// 基础数据机构ID

    public TmPreTransportOrderDelivery() {
        super();
    }

    public TmPreTransportOrderDelivery(String id) {
        super(id);
    }

    public TmPreTransportOrderDelivery(String transportNo, String orgId) {
        this.transportNo = transportNo;
        this.orgId = orgId;
    }

    @ExcelField(title = "运输单号", align = 2, sort = 7)
    public String getTransportNo() {
        return transportNo;
    }

    public void setTransportNo(String transportNo) {
        this.transportNo = transportNo;
    }

    @ExcelField(title = "承运商编码", align = 2, sort = 8)
    public String getCarrierCode() {
        return carrierCode;
    }

    public void setCarrierCode(String carrierCode) {
        this.carrierCode = carrierCode;
    }

    @ExcelField(title = "件数", align = 2, sort = 9)
    public Double getTotalEaQty() {
        return totalEaQty;
    }

    public void setTotalEaQty(Double totalEaQty) {
        this.totalEaQty = totalEaQty;
    }

    @ExcelField(title = "托盘数", align = 2, sort = 10)
    public Double getTotalPlQty() {
        return totalPlQty;
    }

    public void setTotalPlQty(Double totalPlQty) {
        this.totalPlQty = totalPlQty;
    }

    @ExcelField(title = "重量", align = 2, sort = 11)
    public Double getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(Double totalWeight) {
        this.totalWeight = totalWeight;
    }

    @ExcelField(title = "体积", align = 2, sort = 12)
    public Double getTotalCubic() {
        return totalCubic;
    }

    public void setTotalCubic(Double totalCubic) {
        this.totalCubic = totalCubic;
    }

    @ExcelField(title = "到付金额", align = 2, sort = 13)
    public BigDecimal getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(BigDecimal paidAmount) {
        this.paidAmount = paidAmount;
    }

    @ExcelField(title = "实际发货重量", align = 2, sort = 14)
    public Double getActualShipWeight() {
        return actualShipWeight;
    }

    public void setActualShipWeight(Double actualShipWeight) {
        this.actualShipWeight = actualShipWeight;
    }

    @ExcelField(title = "虚拟开单状态", dictType = "SYS_YES_NO", align = 2, sort = 15)
    public String getVirtualBillingStatus() {
        return virtualBillingStatus;
    }

    public void setVirtualBillingStatus(String virtualBillingStatus) {
        this.virtualBillingStatus = virtualBillingStatus;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelField(title = "预计到达时间", align = 2, sort = 16)
    public Date getPlanArriveTime() {
        return planArriveTime;
    }

    public void setPlanArriveTime(Date planArriveTime) {
        this.planArriveTime = planArriveTime;
    }

    public String getIsException() {
        return isException;
    }

    public void setIsException(String isException) {
        this.isException = isException;
    }

    @ExcelField(title = "签收状态", dictType = "SYS_YES_NO", align = 2, sort = 17)
    public String getSignStatus() {
        return signStatus;
    }

    public void setSignStatus(String signStatus) {
        this.signStatus = signStatus;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelField(title = "签收时间", align = 2, sort = 18)
    public Date getSignTime() {
        return signTime;
    }

    public void setSignTime(Date signTime) {
        this.signTime = signTime;
    }

    @ExcelField(title = "签收人", align = 2, sort = 19)
    public String getSignBy() {
        return signBy;
    }

    public void setSignBy(String signBy) {
        this.signBy = signBy;
    }

    @ExcelField(title = "签收备注", align = 2, sort = 20)
    public String getSignRemarks() {
        return signRemarks;
    }

    public void setSignRemarks(String signRemarks) {
        this.signRemarks = signRemarks;
    }

    @ExcelField(title = "回单状态", dictType = "SYS_YES_NO", align = 2, sort = 21)
    public String getReceiptStatus() {
        return receiptStatus;
    }

    public void setReceiptStatus(String receiptStatus) {
        this.receiptStatus = receiptStatus;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelField(title = "回单时间", align = 2, sort = 22)
    public Date getReceiptTime() {
        return receiptTime;
    }

    public void setReceiptTime(Date receiptTime) {
        this.receiptTime = receiptTime;
    }

    @ExcelField(title = "回单人", align = 2, sort = 23)
    public String getReceiptBy() {
        return receiptBy;
    }

    public void setReceiptBy(String receiptBy) {
        this.receiptBy = receiptBy;
    }

    @ExcelField(title = "回单备注", align = 2, sort = 24)
    public String getReceiptRemarks() {
        return receiptRemarks;
    }

    public void setReceiptRemarks(String receiptRemarks) {
        this.receiptRemarks = receiptRemarks;
    }

    @ExcelField(title = "机构ID", align = 2, sort = 25)
    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getBaseOrgId() {
        return baseOrgId;
    }

    public void setBaseOrgId(String baseOrgId) {
        this.baseOrgId = baseOrgId;
    }

    public Integer getReceiptCount() {
        return receiptCount;
    }

    public void setReceiptCount(Integer receiptCount) {
        this.receiptCount = receiptCount;
    }
}