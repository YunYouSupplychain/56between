package com.yunyou.modules.tms.order.entity;

import java.util.Date;

import com.yunyou.core.persistence.DataEntity;

public class TmCarrierFreight extends DataEntity<TmCarrierFreight> {
    private static final long serialVersionUID = -8916240895419721525L;
    private String orgId;               // 机构
    private String status;              // 账单状态
    private String billNo;              // 费用单号
    private String billModule;          // 费用模块
    private String billSubjectCategory; // 费用类别
    private String billTermsCode;       // 计费条款代码
    private String billTermsDesc;       // 计费条款说明
    private String outputObjects;       // 输出对象
    private String warehouseCode;       // 仓库编码
    private String warehouseName;       // 仓库名称
    private String billSubjectCode;     // 费用科目编码
    private String billSubjectName;     // 费用科目名称
    private String sysOrderNo;          // 派车单号
    private String def1;                // 运输单号
    private String settlementObjectCode;// 结算对象编码
    private String settlementObjectName;// 结算对象名称
    private Date businessDate;          // 业务日期
    private String receivablePayable;   // 应收应付
    private Double billStandard;        // 计费标准
    private Double billQty;             // 计费量
    private Double cost;                // 费用
    private String remarks;             // 备注

    private String dispatchNo;          // 派车单号
    private String transportNo;         // 运输单号
    private String customerNo;          // 客户订单号
    private String customerCode;        // 客户编码
    private String customerName;        // 客户名称
    private String principalCode;       // 委托方编码
    private String principalName;       // 委托方名称
    private String consigneeCode;       // 收货方编码
    private String consigneeName;       // 收货方名称
    private String consigneeCityId;     // 收货城市ID
    private String consigneeCity;       // 收货城市
    private String consignee;           // 收货人
    private String consigneeTel;        // 收货人联系电话
    private String consigneeAddress;    // 收货地址

    private Double totalQty;            // 总数量
    private Double totalWeight;         // 总重量

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getBillModule() {
        return billModule;
    }

    public void setBillModule(String billModule) {
        this.billModule = billModule;
    }

    public String getBillSubjectCategory() {
        return billSubjectCategory;
    }

    public void setBillSubjectCategory(String billSubjectCategory) {
        this.billSubjectCategory = billSubjectCategory;
    }

    public String getBillTermsCode() {
        return billTermsCode;
    }

    public void setBillTermsCode(String billTermsCode) {
        this.billTermsCode = billTermsCode;
    }

    public String getBillTermsDesc() {
        return billTermsDesc;
    }

    public void setBillTermsDesc(String billTermsDesc) {
        this.billTermsDesc = billTermsDesc;
    }

    public String getOutputObjects() {
        return outputObjects;
    }

    public void setOutputObjects(String outputObjects) {
        this.outputObjects = outputObjects;
    }

    public String getWarehouseCode() {
        return warehouseCode;
    }

    public void setWarehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public String getBillSubjectCode() {
        return billSubjectCode;
    }

    public void setBillSubjectCode(String billSubjectCode) {
        this.billSubjectCode = billSubjectCode;
    }

    public String getBillSubjectName() {
        return billSubjectName;
    }

    public void setBillSubjectName(String billSubjectName) {
        this.billSubjectName = billSubjectName;
    }

    public String getSysOrderNo() {
        return sysOrderNo;
    }

    public void setSysOrderNo(String sysOrderNo) {
        this.sysOrderNo = sysOrderNo;
    }

    public String getDef1() {
        return def1;
    }

    public void setDef1(String def1) {
        this.def1 = def1;
    }

    public String getSettlementObjectCode() {
        return settlementObjectCode;
    }

    public void setSettlementObjectCode(String settlementObjectCode) {
        this.settlementObjectCode = settlementObjectCode;
    }

    public String getSettlementObjectName() {
        return settlementObjectName;
    }

    public void setSettlementObjectName(String settlementObjectName) {
        this.settlementObjectName = settlementObjectName;
    }

    public Date getBusinessDate() {
        return businessDate;
    }

    public void setBusinessDate(Date businessDate) {
        this.businessDate = businessDate;
    }

    public String getReceivablePayable() {
        return receivablePayable;
    }

    public void setReceivablePayable(String receivablePayable) {
        this.receivablePayable = receivablePayable;
    }

    public Double getBillStandard() {
        return billStandard;
    }

    public void setBillStandard(Double billStandard) {
        this.billStandard = billStandard;
    }

    public Double getBillQty() {
        return billQty;
    }

    public void setBillQty(Double billQty) {
        this.billQty = billQty;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
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

    public String getTransportNo() {
        return transportNo;
    }

    public void setTransportNo(String transportNo) {
        this.transportNo = transportNo;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
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

    public String getPrincipalCode() {
        return principalCode;
    }

    public void setPrincipalCode(String principalCode) {
        this.principalCode = principalCode;
    }

    public String getPrincipalName() {
        return principalName;
    }

    public void setPrincipalName(String principalName) {
        this.principalName = principalName;
    }

    public String getConsigneeCode() {
        return consigneeCode;
    }

    public void setConsigneeCode(String consigneeCode) {
        this.consigneeCode = consigneeCode;
    }

    public String getConsigneeName() {
        return consigneeName;
    }

    public void setConsigneeName(String consigneeName) {
        this.consigneeName = consigneeName;
    }

    public String getConsigneeCityId() {
        return consigneeCityId;
    }

    public void setConsigneeCityId(String consigneeCityId) {
        this.consigneeCityId = consigneeCityId;
    }

    public String getConsigneeCity() {
        return consigneeCity;
    }

    public void setConsigneeCity(String consigneeCity) {
        this.consigneeCity = consigneeCity;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getConsigneeTel() {
        return consigneeTel;
    }

    public void setConsigneeTel(String consigneeTel) {
        this.consigneeTel = consigneeTel;
    }

    public String getConsigneeAddress() {
        return consigneeAddress;
    }

    public void setConsigneeAddress(String consigneeAddress) {
        this.consigneeAddress = consigneeAddress;
    }

    public Double getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(Double totalQty) {
        this.totalQty = totalQty;
    }

    public Double getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(Double totalWeight) {
        this.totalWeight = totalWeight;
    }
}
