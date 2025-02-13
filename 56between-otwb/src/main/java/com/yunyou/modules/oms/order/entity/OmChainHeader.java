package com.yunyou.modules.oms.order.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;
import com.yunyou.core.persistence.DataEntity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 供应链订单Entity
 *
 * @author WMJ
 * @version 2019-04-17
 */
public class OmChainHeader extends DataEntity<OmChainHeader> {
    private static final long serialVersionUID = 1L;

    private String chainNo;// 供应链订单号
    private String businessOrderType;// 业务订单类型
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date orderDate;// 订单时间
    private String status;// 订单状态

    private String customerNo;// 客户订单号
    private String businessNo;// 商流订单号
    private String sourceOrderNo;// 来源订单号
    private String sourceOrderType;// 来源订单类型(采购订单OR销售订单)
    private String sourceOrderId;// 来源订单ID
    private String orderSource;// 订单来源（区分手工和接口）
    private String dataSource;// 数据来源

    private String owner;// 货主编码
    private String customer;// 客户编码
    private String supplierCode;// 供应商编码
    private String principal;// 委托方编码

    private String shipperCode;// 发货方编码
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date deliveryDate;// 预计发货时间
    private String shipper;// 发货人
    private String shipperTel;// 发货人联系电话
    private String shipperArea;// 发货人区域
    private String shipperAddress;// 发货人地址
    private String shipperAddressArea;// 发货人地址区域

    private String consigneeCode;// 收货方编码
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date arrivalTime;// 预计到货时间
    private String consignee;// 收货人
    private String consigneeTel;// 收货人联系电话
    private String consigneeArea;// 收货人区域
    private String consigneeAddress;// 收货人地址
    private String consigneeAddressArea;// 收货人地址区域

    private String carrier;// 承运商编码
    private String logisticsNo;// 物流单号
    private String transportMode;// 运输方式
    private String vehicleNo;// 车牌号
    private String driver;// 司机
    private String contactTel;// 联系电话

    private String settlement;// 结算对象编码
    private String contractNo;// 合同号
    private String settleMode;// 结算方式
    private BigDecimal exchangeRate;// 汇率
    private String currency;// 币种
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date payDate;// 付款时间
    private String payStatus;// 支付状态
    private BigDecimal prepaidAmount;// 预付金额
    private BigDecimal freightCharge;// 运费金额
    private BigDecimal couponAmount;// 优惠券金额
    private BigDecimal totalTax;// 合计税额
    private BigDecimal totalAmount;// 合计金额
    private BigDecimal totalTaxInAmount;// 合计含税金额

    private String taxNo;// 税号
    private String invoice;// 发票抬头
    private String invoiceType;// 发票类型
    private String companyName;// 公司名称
    private String depositBank;// 开户银行名称
    private String depositBankAccount;// 开户银行账号
    private String registeredArea;// 注册地区
    private String registeredTelephone;// 注册电话
    private String taxpayerIdentityNumber;// 纳税人识别号
    private String ticketCollectorName;// 收票人名称
    private String ticketCollectorPhone;// 收票人手机
    private String ticketCollectorAddress;// 收票人地址

    private String preSaleNo;// 预售订单号
    private String sendOrderNo;// 发货单号
    private String preparedBy;// 制单人
    private String businessBy;// 业务人
    private String saleBy;// 销售人
    private String project;// 项目
    private String channel;// 渠道
    private String shop;// 店铺
    private String vipNo;// 会员编号
    private String vipStatus;// 会员级别
    private String clerkCode;// 业务员代码

    private String isAvailableStock = "N";// 是否校验可用库存(Y/N)
    private String warehouse;// 下发机构
    private String auditBy;// 审核人
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date auditDate;// 审核时间
    private String interceptStatus;// 拦截状态
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date interceptTime;  // 拦截时间
    private String handleStatus;// 处理状态(生成作业任务)
    private String orgId;// 机构ID

    private String pushSystem;// 下发系统
    private String serviceMode;// 服务模式
    @Deprecated
    private String payWay;// 支付方式(废弃)
    @Deprecated
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date validityPeriod;// 有效期
    @Deprecated
    private String chainType;// 订单类型
    private List<OmChainDetail> omChainDetailList = Lists.newArrayList();// 明细列表
    private String def1;
    private String def2;
    private String def3;
    private String def4;
    private String def5;
    private String def6;
    private String def7;
    private String def8;
    private String def9;
    private String def10;

    public OmChainHeader() {
        super();
    }

    public OmChainHeader(String id) {
        super(id);
    }

    public String getChainNo() {
        return chainNo;
    }

    public void setChainNo(String chainNo) {
        this.chainNo = chainNo;
    }

    public String getBusinessOrderType() {
        return businessOrderType;
    }

    public void setBusinessOrderType(String businessOrderType) {
        this.businessOrderType = businessOrderType;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public String getBusinessNo() {
        return businessNo;
    }

    public void setBusinessNo(String businessNo) {
        this.businessNo = businessNo;
    }

    public String getSourceOrderNo() {
        return sourceOrderNo;
    }

    public void setSourceOrderNo(String sourceOrderNo) {
        this.sourceOrderNo = sourceOrderNo;
    }

    public String getSourceOrderType() {
        return sourceOrderType;
    }

    public void setSourceOrderType(String sourceOrderType) {
        this.sourceOrderType = sourceOrderType;
    }

    public String getSourceOrderId() {
        return sourceOrderId;
    }

    public void setSourceOrderId(String sourceOrderId) {
        this.sourceOrderId = sourceOrderId;
    }

    public String getOrderSource() {
        return orderSource;
    }

    public void setOrderSource(String orderSource) {
        this.orderSource = orderSource;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getShipperCode() {
        return shipperCode;
    }

    public void setShipperCode(String shipperCode) {
        this.shipperCode = shipperCode;
    }

    public String getShipper() {
        return shipper;
    }

    public void setShipper(String shipper) {
        this.shipper = shipper;
    }

    public String getShipperTel() {
        return shipperTel;
    }

    public void setShipperTel(String shipperTel) {
        this.shipperTel = shipperTel;
    }

    public String getShipperArea() {
        return shipperArea;
    }

    public void setShipperArea(String shipperArea) {
        this.shipperArea = shipperArea;
    }

    public String getShipperAddress() {
        return shipperAddress;
    }

    public void setShipperAddress(String shipperAddress) {
        this.shipperAddress = shipperAddress;
    }

    public String getShipperAddressArea() {
        return shipperAddressArea;
    }

    public void setShipperAddressArea(String shipperAddressArea) {
        this.shipperAddressArea = shipperAddressArea;
    }

    public String getConsigneeCode() {
        return consigneeCode;
    }

    public void setConsigneeCode(String consigneeCode) {
        this.consigneeCode = consigneeCode;
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

    public String getConsigneeArea() {
        return consigneeArea;
    }

    public void setConsigneeArea(String consigneeArea) {
        this.consigneeArea = consigneeArea;
    }

    public String getConsigneeAddress() {
        return consigneeAddress;
    }

    public void setConsigneeAddress(String consigneeAddress) {
        this.consigneeAddress = consigneeAddress;
    }

    public String getConsigneeAddressArea() {
        return consigneeAddressArea;
    }

    public void setConsigneeAddressArea(String consigneeAddressArea) {
        this.consigneeAddressArea = consigneeAddressArea;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getLogisticsNo() {
        return logisticsNo;
    }

    public void setLogisticsNo(String logisticsNo) {
        this.logisticsNo = logisticsNo;
    }

    public String getTransportMode() {
        return transportMode;
    }

    public void setTransportMode(String transportMode) {
        this.transportMode = transportMode;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getContactTel() {
        return contactTel;
    }

    public void setContactTel(String contactTel) {
        this.contactTel = contactTel;
    }

    public String getPreSaleNo() {
        return preSaleNo;
    }

    public void setPreSaleNo(String preSaleNo) {
        this.preSaleNo = preSaleNo;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Date getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Date arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getSettlement() {
        return settlement;
    }

    public void setSettlement(String settlement) {
        this.settlement = settlement;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String getSettleMode() {
        return settleMode;
    }

    public void setSettleMode(String settleMode) {
        this.settleMode = settleMode;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getSendOrderNo() {
        return sendOrderNo;
    }

    public void setSendOrderNo(String sendOrderNo) {
        this.sendOrderNo = sendOrderNo;
    }

    public Date getPayDate() {
        return payDate;
    }

    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public BigDecimal getPrepaidAmount() {
        return prepaidAmount;
    }

    public void setPrepaidAmount(BigDecimal prepaidAmount) {
        this.prepaidAmount = prepaidAmount;
    }

    public BigDecimal getFreightCharge() {
        return freightCharge;
    }

    public void setFreightCharge(BigDecimal freightCharge) {
        this.freightCharge = freightCharge;
    }

    public BigDecimal getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(BigDecimal couponAmount) {
        this.couponAmount = couponAmount;
    }

    public BigDecimal getTotalTax() {
        return totalTax;
    }

    public void setTotalTax(BigDecimal totalTax) {
        this.totalTax = totalTax;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTotalTaxInAmount() {
        return totalTaxInAmount;
    }

    public void setTotalTaxInAmount(BigDecimal totalTaxInAmount) {
        this.totalTaxInAmount = totalTaxInAmount;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    public String getTaxNo() {
        return taxNo;
    }

    public void setTaxNo(String taxNo) {
        this.taxNo = taxNo;
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getTaxpayerIdentityNumber() {
        return taxpayerIdentityNumber;
    }

    public void setTaxpayerIdentityNumber(String taxpayerIdentityNumber) {
        this.taxpayerIdentityNumber = taxpayerIdentityNumber;
    }

    public String getDepositBank() {
        return depositBank;
    }

    public void setDepositBank(String depositBank) {
        this.depositBank = depositBank;
    }

    public String getDepositBankAccount() {
        return depositBankAccount;
    }

    public void setDepositBankAccount(String depositBankAccount) {
        this.depositBankAccount = depositBankAccount;
    }

    public String getRegisteredArea() {
        return registeredArea;
    }

    public void setRegisteredArea(String registeredArea) {
        this.registeredArea = registeredArea;
    }

    public String getRegisteredTelephone() {
        return registeredTelephone;
    }

    public void setRegisteredTelephone(String registeredTelephone) {
        this.registeredTelephone = registeredTelephone;
    }

    public String getTicketCollectorName() {
        return ticketCollectorName;
    }

    public void setTicketCollectorName(String ticketCollectorName) {
        this.ticketCollectorName = ticketCollectorName;
    }

    public String getTicketCollectorPhone() {
        return ticketCollectorPhone;
    }

    public void setTicketCollectorPhone(String ticketCollectorPhone) {
        this.ticketCollectorPhone = ticketCollectorPhone;
    }

    public String getTicketCollectorAddress() {
        return ticketCollectorAddress;
    }

    public void setTicketCollectorAddress(String ticketCollectorAddress) {
        this.ticketCollectorAddress = ticketCollectorAddress;
    }

    public String getPreparedBy() {
        return preparedBy;
    }

    public void setPreparedBy(String preparedBy) {
        this.preparedBy = preparedBy;
    }

    public String getSaleBy() {
        return saleBy;
    }

    public void setSaleBy(String saleBy) {
        this.saleBy = saleBy;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getVipNo() {
        return vipNo;
    }

    public void setVipNo(String vipNo) {
        this.vipNo = vipNo;
    }

    public String getVipStatus() {
        return vipStatus;
    }

    public void setVipStatus(String vipStatus) {
        this.vipStatus = vipStatus;
    }

    public String getClerkCode() {
        return clerkCode;
    }

    public void setClerkCode(String clerkCode) {
        this.clerkCode = clerkCode;
    }

    public String getIsAvailableStock() {
        return isAvailableStock;
    }

    public void setIsAvailableStock(String isAvailableStock) {
        this.isAvailableStock = isAvailableStock;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public String getAuditBy() {
        return auditBy;
    }

    public void setAuditBy(String auditBy) {
        this.auditBy = auditBy;
    }

    public Date getAuditDate() {
        return auditDate;
    }

    public void setAuditDate(Date auditDate) {
        this.auditDate = auditDate;
    }

    public String getInterceptStatus() {
        return interceptStatus;
    }

    public void setInterceptStatus(String interceptStatus) {
        this.interceptStatus = interceptStatus;
    }

    public Date getInterceptTime() {
        return interceptTime;
    }

    public void setInterceptTime(Date interceptTime) {
        this.interceptTime = interceptTime;
    }

    public String getHandleStatus() {
        return handleStatus;
    }

    public void setHandleStatus(String handleStatus) {
        this.handleStatus = handleStatus;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getPayWay() {
        return payWay;
    }

    public void setPayWay(String payWay) {
        this.payWay = payWay;
    }

    public Date getValidityPeriod() {
        return validityPeriod;
    }

    public void setValidityPeriod(Date validityPeriod) {
        this.validityPeriod = validityPeriod;
    }

    public String getChainType() {
        return chainType;
    }

    public void setChainType(String chainType) {
        this.chainType = chainType;
    }

    public String getPushSystem() {
        return pushSystem;
    }

    public void setPushSystem(String pushSystem) {
        this.pushSystem = pushSystem;
    }

    public String getServiceMode() {
        return serviceMode;
    }

    public void setServiceMode(String serviceMode) {
        this.serviceMode = serviceMode;
    }

    public String getBusinessBy() {
        return businessBy;
    }

    public void setBusinessBy(String businessBy) {
        this.businessBy = businessBy;
    }

    public List<OmChainDetail> getOmChainDetailList() {
        return omChainDetailList;
    }

    public void setOmChainDetailList(List<OmChainDetail> omChainDetailList) {
        this.omChainDetailList = omChainDetailList;
    }

    public String getDef1() {
        return def1;
    }

    public void setDef1(String def1) {
        this.def1 = def1;
    }

    public String getDef2() {
        return def2;
    }

    public void setDef2(String def2) {
        this.def2 = def2;
    }

    public String getDef3() {
        return def3;
    }

    public void setDef3(String def3) {
        this.def3 = def3;
    }

    public String getDef4() {
        return def4;
    }

    public void setDef4(String def4) {
        this.def4 = def4;
    }

    public String getDef5() {
        return def5;
    }

    public void setDef5(String def5) {
        this.def5 = def5;
    }

    public String getDef6() {
        return def6;
    }

    public void setDef6(String def6) {
        this.def6 = def6;
    }

    public String getDef7() {
        return def7;
    }

    public void setDef7(String def7) {
        this.def7 = def7;
    }

    public String getDef8() {
        return def8;
    }

    public void setDef8(String def8) {
        this.def8 = def8;
    }

    public String getDef9() {
        return def9;
    }

    public void setDef9(String def9) {
        this.def9 = def9;
    }

    public String getDef10() {
        return def10;
    }

    public void setDef10(String def10) {
        this.def10 = def10;
    }
}