package com.yunyou.modules.tms.applet.entity.extend;

import java.io.Serializable;
import java.math.BigDecimal;

public class TmDispatchTankInfoEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String remarks;

    private String dispatchNo;				// 派车单号
    private String outletCode;		        // 网点
    private String transportNo;				// 运输订单号
    private BigDecimal offloadingQty;		// 卸油数
    private BigDecimal tankQty;				// 装罐数
    private String orgId;					// 机构ID
    private Long recVer = 0L;        		// 版本号
    private String baseOrgId;   			// 基础数据机构ID

    private String outletName;              // 网点名称
    private String carrierCode;             // 承运商编码
    private String carrierName;             // 承运商名称
    private String carNo;                   // 车牌号
    private String customerCode;            // 客户编码
    private String customerName;            // 客户名称

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

    public String getDispatchNo() {
        return dispatchNo;
    }

    public void setDispatchNo(String dispatchNo) {
        this.dispatchNo = dispatchNo;
    }

    public String getOutletCode() {
        return outletCode;
    }

    public void setOutletCode(String outletCode) {
        this.outletCode = outletCode;
    }

    public String getTransportNo() {
        return transportNo;
    }

    public void setTransportNo(String transportNo) {
        this.transportNo = transportNo;
    }

    public BigDecimal getOffloadingQty() {
        return offloadingQty;
    }

    public void setOffloadingQty(BigDecimal offloadingQty) {
        this.offloadingQty = offloadingQty;
    }

    public BigDecimal getTankQty() {
        return tankQty;
    }

    public void setTankQty(BigDecimal tankQty) {
        this.tankQty = tankQty;
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

    public String getOutletName() {
        return outletName;
    }

    public void setOutletName(String outletName) {
        this.outletName = outletName;
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

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}
