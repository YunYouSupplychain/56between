package com.yunyou.modules.oms.order.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;
import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.core.persistence.DataEntity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 采购订单Entity
 *
 * @author WMJ
 * @version 2019-04-16
 */
public class OmPoHeader extends DataEntity<OmPoHeader> {

    private static final long serialVersionUID = 1L;
    private String poNo;        // 采购单号
    private Date orderDate;        // 订单日期
    private String owner;        // 货主
    private String supplierCode;        // 供应商编码
    private String status;        // 订单状态
    private String poType;        // 订单类型
    private Date validityPeriod;        // 有效期
    private Date arrivalTime;        // 到货时间
    private String project;        // 项目
    private String contractNo;        // 合同号
    private String consignee;        // 收货人
    private String consigneeTel;        // 收货人联系电话
    private String consigneeArea;        // 收货人区域
    private String consigneeAddress;        // 收货人地址
    private String shipper;        // 发货人
    private String shipperTel;        // 发货人联系电话
    private String shipperArea;        // 发货人区域
    private String shipperAddress;        // 发货人地址
    private String channel;        // 渠道
    private String customerNo;        // 客户订单号
    private String transportMode;        // 运输方式
    private String carrier;        // 承运商
    private String logisticsNo;        // 物流单号
    private String vehicleNo;        // 车牌号
    private String driver;        // 司机
    private String contactTel;        // 联系电话
    private String organization;        // 机构
    private String department;        // 部门
    private String businessBy;        // 业务人
    private String preparedBy;        // 制单人
    private String auditBy;        // 审核人
    private BigDecimal prepaidAmount;        // 预付金额
    private String settlement;        // 结算对象
    private String settleMode;        // 结算方式
    private String currency;        // 币种
    private BigDecimal exchangeRate;        // 汇率
    private BigDecimal totalAmount; // 合计金额
    private BigDecimal totalTax;    // 合计税额
    private BigDecimal totalTaxInAmount;    // 合计含税金额
    private String orgId;           // 平台编码
    private String subOrgId;        // 下发机构ID
    private BigDecimal freightCharge;    // 运费
    private String consigneeAddressArea;        // 收货人地址区域
    private String shipperAddressArea;        // 收货人地址区域
    private String saleOrderNos;    // 销售订单号
    private String purchaseMethod;  // 采购方式
    private Date deliveryDate;    // 发货时间
    private String payStatus;   // 支付状态
    private String payAccount;// 付款账户
    private String payNo;   // 付款单号
    private BigDecimal actualPaidAmount;// 实付金额
    private String cashierRemarks; // 出纳备注
    private String settlementContact;    // 结算联系人
    private String settlementContactTel; // 结算联系人电话
    private Date auditDate;    // 审核时间
    private String depositBank; // 开户银行
	private String dataSource;// 数据来源
    private String principal;// 委托方
    private List<OmPoDetail> omPoDetailList = Lists.newArrayList();        // 子表列表

    public OmPoHeader() {
        super();
    }

    public OmPoHeader(String id) {
        super(id);
    }

    @ExcelField(title = "采购单号", align = 2, sort = 7)
    public String getPoNo() {
        return poNo;
    }

    public void setPoNo(String poNo) {
        this.poNo = poNo;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelField(title = "订单日期", align = 2, sort = 8)
    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    @ExcelField(title = "供应商编码", align = 2, sort = 9)
    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    @ExcelField(title = "订单状态", align = 2, sort = 10)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @ExcelField(title = "订单类型", align = 2, sort = 11)
    public String getPoType() {
        return poType;
    }

    public void setPoType(String poType) {
        this.poType = poType;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelField(title = "有效期", align = 2, sort = 12)
    public Date getValidityPeriod() {
        return validityPeriod;
    }

    public void setValidityPeriod(Date validityPeriod) {
        this.validityPeriod = validityPeriod;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelField(title = "到货时间", align = 2, sort = 13)
    public Date getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Date arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    @ExcelField(title = "项目", align = 2, sort = 14)
    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    @ExcelField(title = "合同号", align = 2, sort = 15)
    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    @ExcelField(title = "收货人", align = 2, sort = 16)
    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    @ExcelField(title = "收货人联系电话", align = 2, sort = 17)
    public String getConsigneeTel() {
        return consigneeTel;
    }

    public void setConsigneeTel(String consigneeTel) {
        this.consigneeTel = consigneeTel;
    }

    @ExcelField(title = "收货人区域", align = 2, sort = 18)
    public String getConsigneeArea() {
        return consigneeArea;
    }

    public void setConsigneeArea(String consigneeArea) {
        this.consigneeArea = consigneeArea;
    }

    @ExcelField(title = "收货人地址", align = 2, sort = 19)
    public String getConsigneeAddress() {
        return consigneeAddress;
    }

    public void setConsigneeAddress(String consigneeAddress) {
        this.consigneeAddress = consigneeAddress;
    }

    @ExcelField(title = "发货人", align = 2, sort = 20)
    public String getShipper() {
        return shipper;
    }

    public void setShipper(String shipper) {
        this.shipper = shipper;
    }

    @ExcelField(title = "发货人联系电话", align = 2, sort = 21)
    public String getShipperTel() {
        return shipperTel;
    }

    public void setShipperTel(String shipperTel) {
        this.shipperTel = shipperTel;
    }

    @ExcelField(title = "发货人区域", align = 2, sort = 22)
    public String getShipperArea() {
        return shipperArea;
    }

    public void setShipperArea(String shipperArea) {
        this.shipperArea = shipperArea;
    }

    @ExcelField(title = "发货人地址", align = 2, sort = 23)
    public String getShipperAddress() {
        return shipperAddress;
    }

    public void setShipperAddress(String shipperAddress) {
        this.shipperAddress = shipperAddress;
    }

    @ExcelField(title = "渠道", align = 2, sort = 24)
    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    @ExcelField(title = "客户订单号", align = 2, sort = 25)
    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    @ExcelField(title = "运输方式", align = 2, sort = 26)
    public String getTransportMode() {
        return transportMode;
    }

    public void setTransportMode(String transportMode) {
        this.transportMode = transportMode;
    }

    @ExcelField(title = "承运商", align = 2, sort = 27)
    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    @ExcelField(title = "物流单号", align = 2, sort = 28)
    public String getLogisticsNo() {
        return logisticsNo;
    }

    public void setLogisticsNo(String logisticsNo) {
        this.logisticsNo = logisticsNo;
    }

    @ExcelField(title = "车牌号", align = 2, sort = 29)
    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    @ExcelField(title = "司机", align = 2, sort = 30)
    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    @ExcelField(title = "联系电话", align = 2, sort = 31)
    public String getContactTel() {
        return contactTel;
    }

    public void setContactTel(String contactTel) {
        this.contactTel = contactTel;
    }

    @ExcelField(title = "机构", align = 2, sort = 32)
    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    @ExcelField(title = "部门", align = 2, sort = 33)
    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @ExcelField(title = "业务人", align = 2, sort = 34)
    public String getBusinessBy() {
        return businessBy;
    }

    public void setBusinessBy(String businessBy) {
        this.businessBy = businessBy;
    }

    @ExcelField(title = "制单人", align = 2, sort = 35)
    public String getPreparedBy() {
        return preparedBy;
    }

    public void setPreparedBy(String preparedBy) {
        this.preparedBy = preparedBy;
    }

    @ExcelField(title = "审核人", align = 2, sort = 36)
    public String getAuditBy() {
        return auditBy;
    }

    public void setAuditBy(String auditBy) {
        this.auditBy = auditBy;
    }

    @ExcelField(title = "预付金额", align = 2, sort = 37)
    public BigDecimal getPrepaidAmount() {
        return prepaidAmount;
    }

    public void setPrepaidAmount(BigDecimal prepaidAmount) {
        this.prepaidAmount = prepaidAmount;
    }

    @ExcelField(title = "结算对象", align = 2, sort = 38)
    public String getSettlement() {
        return settlement;
    }

    public void setSettlement(String settlement) {
        this.settlement = settlement;
    }

    @ExcelField(title = "结算方式", align = 2, sort = 39)
    public String getSettleMode() {
        return settleMode;
    }

    public void setSettleMode(String settleMode) {
        this.settleMode = settleMode;
    }

    @ExcelField(title = "币种", align = 2, sort = 40)
    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @ExcelField(title = "汇率", align = 2, sort = 41)
    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTotalTax() {
        return totalTax;
    }

    public void setTotalTax(BigDecimal totalTax) {
        this.totalTax = totalTax;
    }

    public BigDecimal getTotalTaxInAmount() {
        return totalTaxInAmount;
    }

    public void setTotalTaxInAmount(BigDecimal totalTaxInAmount) {
        this.totalTaxInAmount = totalTaxInAmount;
    }

    public List<OmPoDetail> getOmPoDetailList() {
        return omPoDetailList;
    }

    public void setOmPoDetailList(List<OmPoDetail> omPoDetailList) {
        this.omPoDetailList = omPoDetailList;
    }

    public String getSubOrgId() {
        return subOrgId;
    }

    public void setSubOrgId(String subOrgId) {
        this.subOrgId = subOrgId;
    }

    public BigDecimal getFreightCharge() {
        return freightCharge;
    }

    public void setFreightCharge(BigDecimal freightCharge) {
        this.freightCharge = freightCharge;
    }

    public String getConsigneeAddressArea() {
        return consigneeAddressArea;
    }

    public void setConsigneeAddressArea(String consigneeAddressArea) {
        this.consigneeAddressArea = consigneeAddressArea;
    }

    public String getShipperAddressArea() {
        return shipperAddressArea;
    }

    public void setShipperAddressArea(String shipperAddressArea) {
        this.shipperAddressArea = shipperAddressArea;
    }

    public String getSaleOrderNos() {
        return saleOrderNos;
    }

    public void setSaleOrderNos(String saleOrderNos) {
        this.saleOrderNos = saleOrderNos;
    }

    public String getPurchaseMethod() {
        return purchaseMethod;
    }

    public void setPurchaseMethod(String purchaseMethod) {
        this.purchaseMethod = purchaseMethod;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public String getPayAccount() {
        return payAccount;
    }

    public void setPayAccount(String payAccount) {
        this.payAccount = payAccount;
    }

    public String getPayNo() {
        return payNo;
    }

    public void setPayNo(String payNo) {
        this.payNo = payNo;
    }

    public BigDecimal getActualPaidAmount() {
        return actualPaidAmount;
    }

    public void setActualPaidAmount(BigDecimal actualPaidAmount) {
        this.actualPaidAmount = actualPaidAmount;
    }

    public String getCashierRemarks() {
        return cashierRemarks;
    }

    public void setCashierRemarks(String cashierRemarks) {
        this.cashierRemarks = cashierRemarks;
    }

    public String getSettlementContact() {
        return settlementContact;
    }

    public void setSettlementContact(String settlementContact) {
        this.settlementContact = settlementContact;
    }

    public String getSettlementContactTel() {
        return settlementContactTel;
    }

    public void setSettlementContactTel(String settlementContactTel) {
        this.settlementContactTel = settlementContactTel;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getAuditDate() {
        return auditDate;
    }

    public void setAuditDate(Date auditDate) {
        this.auditDate = auditDate;
    }

    public String getDepositBank() {
        return depositBank;
    }

    public void setDepositBank(String depositBank) {
        this.depositBank = depositBank;
    }

	public String getDataSource() {
		return dataSource;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }
}