package com.yunyou.modules.oms.order.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;
import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.core.persistence.DataEntity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 销售订单Entity
 *
 * @author WMJ
 * @version 2019-04-17
 */
public class OmSaleHeader extends DataEntity<OmSaleHeader> {

    private static final long serialVersionUID = 1L;
    private String saleNo;        // 销售单号
    private Date orderDate;        // 订单日期
    private String status;        // 订单状态
    private String saleType;        // 订单类型
    private String outWarhouse;        // 出库仓库
    private String owner;            // 货主
    private String customer;        // 客户
    private String shop;        // 店铺(机构)
    private String principal;        // 委托方
    private String invoice;        // 发票抬头
    private String taxNo;        // 税号
    private String project;        // 项目
    private String contractNo;        // 合同号
    private String saleBy;        // 销售人
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
    private BigDecimal totalTax;        // 合计税额
    private BigDecimal totalAmount;        // 合计金额
    private BigDecimal totalTaxInAmount;        // 合计含税金额
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
    private String orgId;           // 平台编码
    private String isAvailableStock = "N";    // 是否校验可用库存(Y/N)
    private String consigneeAddressArea;        // 收货人地址区域
    private String shipperAddressArea;        // 收货人地址区域
    /*开具发票*/
    private String invoiceType;        // 发票类型
    private String companyName;     // 公司名称
    private String taxpayerIdentityNumber;  // 纳税人识别号
    private String depositBank;     // 开户银行名称
    private String depositBankAccount;  // 开户银行账号
    private String registeredArea;  // 注册地区
    private String registeredTelephone; // 注册电话
    private String ticketCollectorPhone;// 收票人手机
    private String ticketCollectorAddress;// 收票人地址

    private BigDecimal freightCharge;    // 运费
    private BigDecimal couponAmount;    // 优惠券金额
    private String payWay;            // 支付方式(废弃)
    private String payStatus;        // 支付状态
    private String vipNo;            // 会员编号
    private String sendOrderNo;        // 发货单号
    private String appAuditStatus;    // app审核状态
    private Date payDate;            // 付款时间
    private String vipStatus;   // vip状态
    private String saleMethod;  // 销售方式
    private String clerkCode;    // 业务员代码
    private String ticketCollectorName; // 收票人名称
    private String appAuditBy;  // 审核人(APP审核信息)
    private Date appAuditDate;  // 审核时间(APP审核信息)
    private String appAnnex;    // 附件路径(APP审核信息)
    private String auditOrgId;  // 审核机构
    private String auditDepartment; // 审核部门
    private Date auditDate;   // 审核时间
    private String annex;       // 附件路径
    private String auditRemarks;    // 审核备注
    private String updateOrgId;     // 更新人机构
    private String updateDepartment; // 更新人部门
    private String updateContent;   // 更新内容
    private String preparedRemarks;// 制单备注
    private BigDecimal discountRate;// 整单折扣率
    private String receiptAccount;// 收款账户
    private String receiptNo;   // 收款单号
    private BigDecimal actualReceivedAmount;// 实收金额
    private String cashierRemarks; // 出纳备注
    private String poNo;// 采购单号
    private BigDecimal orderSettleAmount;   // 订单结算金额
    private String receiptFlag; // 收款标识，金蝶接口反馈
    private BigDecimal skuSettleAmount; // 货物结算金额
    private String dataSource;// 数据来源
    private List<OmSaleDetail> omSaleDetailList = Lists.newArrayList();        // 子表列表

    public OmSaleHeader() {
        super();
    }

    public OmSaleHeader(String id) {
        super(id);
    }

    @ExcelField(title = "销售单号", align = 2, sort = 7)
    public String getSaleNo() {
        return saleNo;
    }

    public void setSaleNo(String saleNo) {
        this.saleNo = saleNo;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelField(title = "订单日期", align = 2, sort = 8)
    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    @ExcelField(title = "订单状态", align = 2, sort = 9)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @ExcelField(title = "订单类型", align = 2, sort = 10)
    public String getSaleType() {
        return saleType;
    }

    public void setSaleType(String saleType) {
        this.saleType = saleType;
    }

    @ExcelField(title = "出库仓库", align = 2, sort = 11)
    public String getOutWarhouse() {
        return outWarhouse;
    }

    public void setOutWarhouse(String outWarhouse) {
        this.outWarhouse = outWarhouse;
    }

    @ExcelField(title = "客户", align = 2, sort = 12)
    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    @ExcelField(title = "店铺(机构)", align = 2, sort = 13)
    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    @ExcelField(title = "委托方", align = 2, sort = 14)
    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    @ExcelField(title = "发票抬头", align = 2, sort = 15)
    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    @ExcelField(title = "税号", align = 2, sort = 16)
    public String getTaxNo() {
        return taxNo;
    }

    public void setTaxNo(String taxNo) {
        this.taxNo = taxNo;
    }

    @ExcelField(title = "项目", align = 2, sort = 17)
    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    @ExcelField(title = "合同号", align = 2, sort = 18)
    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    @ExcelField(title = "销售人", align = 2, sort = 19)
    public String getSaleBy() {
        return saleBy;
    }

    public void setSaleBy(String saleBy) {
        this.saleBy = saleBy;
    }

    @ExcelField(title = "收货人", align = 2, sort = 20)
    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    @ExcelField(title = "收货人联系电话", align = 2, sort = 21)
    public String getConsigneeTel() {
        return consigneeTel;
    }

    public void setConsigneeTel(String consigneeTel) {
        this.consigneeTel = consigneeTel;
    }

    @ExcelField(title = "收货人区域", align = 2, sort = 22)
    public String getConsigneeArea() {
        return consigneeArea;
    }

    public void setConsigneeArea(String consigneeArea) {
        this.consigneeArea = consigneeArea;
    }

    @ExcelField(title = "收货人地址", align = 2, sort = 23)
    public String getConsigneeAddress() {
        return consigneeAddress;
    }

    public void setConsigneeAddress(String consigneeAddress) {
        this.consigneeAddress = consigneeAddress;
    }

    @ExcelField(title = "发货人", align = 2, sort = 24)
    public String getShipper() {
        return shipper;
    }

    public void setShipper(String shipper) {
        this.shipper = shipper;
    }

    @ExcelField(title = "发货人联系电话", align = 2, sort = 25)
    public String getShipperTel() {
        return shipperTel;
    }

    public void setShipperTel(String shipperTel) {
        this.shipperTel = shipperTel;
    }

    @ExcelField(title = "发货人区域", align = 2, sort = 26)
    public String getShipperArea() {
        return shipperArea;
    }

    public void setShipperArea(String shipperArea) {
        this.shipperArea = shipperArea;
    }

    @ExcelField(title = "发货人地址", align = 2, sort = 27)
    public String getShipperAddress() {
        return shipperAddress;
    }

    public void setShipperAddress(String shipperAddress) {
        this.shipperAddress = shipperAddress;
    }

    @ExcelField(title = "渠道", align = 2, sort = 28)
    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    @ExcelField(title = "客户订单号", align = 2, sort = 29)
    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    @ExcelField(title = "合计税额", align = 2, sort = 30)
    public BigDecimal getTotalTax() {
        return totalTax;
    }

    public void setTotalTax(BigDecimal totalTax) {
        this.totalTax = totalTax;
    }


    @ExcelField(title = "合计金额", align = 2, sort = 31)
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    @ExcelField(title = "合计含税金额", align = 2, sort = 32)
    public BigDecimal getTotalTaxInAmount() {
        return totalTaxInAmount;
    }

    public void setTotalTaxInAmount(BigDecimal totalTaxInAmount) {
        this.totalTaxInAmount = totalTaxInAmount;
    }

    @ExcelField(title = "运输方式", align = 2, sort = 33)
    public String getTransportMode() {
        return transportMode;
    }

    public void setTransportMode(String transportMode) {
        this.transportMode = transportMode;
    }

    @ExcelField(title = "承运商", align = 2, sort = 34)
    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    @ExcelField(title = "物流单号", align = 2, sort = 35)
    public String getLogisticsNo() {
        return logisticsNo;
    }

    public void setLogisticsNo(String logisticsNo) {
        this.logisticsNo = logisticsNo;
    }

    @ExcelField(title = "车牌号", align = 2, sort = 36)
    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    @ExcelField(title = "司机", align = 2, sort = 37)
    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    @ExcelField(title = "联系电话", align = 2, sort = 38)
    public String getContactTel() {
        return contactTel;
    }

    public void setContactTel(String contactTel) {
        this.contactTel = contactTel;
    }

    @ExcelField(title = "机构", align = 2, sort = 39)
    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    @ExcelField(title = "部门", align = 2, sort = 40)
    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @ExcelField(title = "业务人", align = 2, sort = 41)
    public String getBusinessBy() {
        return businessBy;
    }

    public void setBusinessBy(String businessBy) {
        this.businessBy = businessBy;
    }

    @ExcelField(title = "制单人", align = 2, sort = 42)
    public String getPreparedBy() {
        return preparedBy;
    }

    public void setPreparedBy(String preparedBy) {
        this.preparedBy = preparedBy;
    }

    @ExcelField(title = "审核人", align = 2, sort = 43)
    public String getAuditBy() {
        return auditBy;
    }

    public void setAuditBy(String auditBy) {
        this.auditBy = auditBy;
    }

    @ExcelField(title = "预付金额", align = 2, sort = 44)
    public BigDecimal getPrepaidAmount() {
        return prepaidAmount;
    }

    public void setPrepaidAmount(BigDecimal prepaidAmount) {
        this.prepaidAmount = prepaidAmount;
    }

    @ExcelField(title = "结算对象", align = 2, sort = 45)
    public String getSettlement() {
        return settlement;
    }

    public void setSettlement(String settlement) {
        this.settlement = settlement;
    }

    @ExcelField(title = "结算方式", align = 2, sort = 46)
    public String getSettleMode() {
        return settleMode;
    }

    public void setSettleMode(String settleMode) {
        this.settleMode = settleMode;
    }

    @ExcelField(title = "币种", align = 2, sort = 47)
    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @ExcelField(title = "汇率", align = 2, sort = 48)
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

    public List<OmSaleDetail> getOmSaleDetailList() {
        return omSaleDetailList;
    }

    public void setOmSaleDetailList(List<OmSaleDetail> omSaleDetailList) {
        this.omSaleDetailList = omSaleDetailList;
    }

    public String getIsAvailableStock() {
        return isAvailableStock;
    }

    public void setIsAvailableStock(String isAvailableStock) {
        this.isAvailableStock = isAvailableStock;
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

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
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

    public String getVipStatus() {
        return vipStatus;
    }

    public void setVipStatus(String vipStatus) {
        this.vipStatus = vipStatus;
    }

    public String getSaleMethod() {
        return saleMethod;
    }

    public void setSaleMethod(String saleMethod) {
        this.saleMethod = saleMethod;
    }

    public String getClerkCode() {
        return clerkCode;
    }

    public void setClerkCode(String clerkCode) {
        this.clerkCode = clerkCode;
    }

    public String getTicketCollectorName() {
        return ticketCollectorName;
    }

    public void setTicketCollectorName(String ticketCollectorName) {
        this.ticketCollectorName = ticketCollectorName;
    }

    public String getAppAuditBy() {
        return appAuditBy;
    }

    public void setAppAuditBy(String appAuditBy) {
        this.appAuditBy = appAuditBy;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getAppAuditDate() {
        return appAuditDate;
    }

    public void setAppAuditDate(Date appAuditDate) {
        this.appAuditDate = appAuditDate;
    }

    public String getAppAnnex() {
        return appAnnex;
    }

    public void setAppAnnex(String appAnnex) {
        this.appAnnex = appAnnex;
    }

    public String getAuditOrgId() {
        return auditOrgId;
    }

    public void setAuditOrgId(String auditOrgId) {
        this.auditOrgId = auditOrgId;
    }

    public String getAuditDepartment() {
        return auditDepartment;
    }

    public void setAuditDepartment(String auditDepartment) {
        this.auditDepartment = auditDepartment;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getAuditDate() {
        return auditDate;
    }

    public void setAuditDate(Date auditDate) {
        this.auditDate = auditDate;
    }

    public String getAnnex() {
        return annex;
    }

    public void setAnnex(String annex) {
        this.annex = annex;
    }

    public String getAuditRemarks() {
        return auditRemarks;
    }

    public void setAuditRemarks(String auditRemarks) {
        this.auditRemarks = auditRemarks;
    }

    public String getUpdateOrgId() {
        return updateOrgId;
    }

    public void setUpdateOrgId(String updateOrgId) {
        this.updateOrgId = updateOrgId;
    }

    public String getUpdateDepartment() {
        return updateDepartment;
    }

    public void setUpdateDepartment(String updateDepartment) {
        this.updateDepartment = updateDepartment;
    }

    public String getUpdateContent() {
        return updateContent;
    }

    public void setUpdateContent(String updateContent) {
        this.updateContent = updateContent;
    }

    public String getPayWay() {
        return payWay;
    }

    public void setPayWay(String payWay) {
        this.payWay = payWay;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public String getVipNo() {
        return vipNo;
    }

    public void setVipNo(String vipNo) {
        this.vipNo = vipNo;
    }

    public String getSendOrderNo() {
        return sendOrderNo;
    }

    public void setSendOrderNo(String sendOrderNo) {
        this.sendOrderNo = sendOrderNo;
    }

    public String getAppAuditStatus() {
        return appAuditStatus;
    }

    public void setAppAuditStatus(String appAuditStatus) {
        this.appAuditStatus = appAuditStatus;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getPayDate() {
        return payDate;
    }

    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }

    public String getPreparedRemarks() {
        return preparedRemarks;
    }

    public void setPreparedRemarks(String preparedRemarks) {
        this.preparedRemarks = preparedRemarks;
    }

    public BigDecimal getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(BigDecimal discountRate) {
        this.discountRate = discountRate;
    }

    public String getReceiptAccount() {
        return receiptAccount;
    }

    public void setReceiptAccount(String receiptAccount) {
        this.receiptAccount = receiptAccount;
    }

    public String getReceiptNo() {
        return receiptNo;
    }

    public void setReceiptNo(String receiptNo) {
        this.receiptNo = receiptNo;
    }

    public BigDecimal getActualReceivedAmount() {
        return actualReceivedAmount;
    }

    public void setActualReceivedAmount(BigDecimal actualReceivedAmount) {
        this.actualReceivedAmount = actualReceivedAmount;
    }

    public String getCashierRemarks() {
        return cashierRemarks;
    }

    public void setCashierRemarks(String cashierRemarks) {
        this.cashierRemarks = cashierRemarks;
    }

    public String getPoNo() {
        return poNo;
    }

    public void setPoNo(String poNo) {
        this.poNo = poNo;
    }

    public BigDecimal getOrderSettleAmount() {
        return orderSettleAmount;
    }

    public void setOrderSettleAmount(BigDecimal orderSettleAmount) {
        this.orderSettleAmount = orderSettleAmount;
    }

    public String getReceiptFlag() {
        return receiptFlag;
    }

    public void setReceiptFlag(String receiptFlag) {
        this.receiptFlag = receiptFlag;
    }

    public BigDecimal getSkuSettleAmount() {
        return skuSettleAmount;
    }

    public void setSkuSettleAmount(BigDecimal skuSettleAmount) {
        this.skuSettleAmount = skuSettleAmount;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }
}
