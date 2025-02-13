package com.yunyou.modules.oms.order.entity.extend;

import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.modules.oms.order.entity.OmChainHeader;

import java.math.BigDecimal;
import java.util.Date;

public class OmChainImportEntity extends OmChainHeader {
    private String skuCode;
    private BigDecimal qty;

    @ExcelField(title = "订单时间**必填 日期格式: yyyy-MM-dd hh:mm:ss", align = 2, sort = 1)
    @Override
    public Date getOrderDate() {
        return super.getOrderDate();
    }

    @Override
    public void setOrderDate(Date orderDate) {
        super.setOrderDate(orderDate);
    }

    @ExcelField(title = "订单类型**必填 填写序号\n1:正常采购 2:正常销售\n3:销售退货 4:采购退货\n5:调拨入库 6:领用\n7:赠送 8:折扣销售 9:调拨出库", align = 2, sort = 2)
    @Override
    public String getBusinessOrderType() {
        return super.getBusinessOrderType();
    }

    @Override
    public void setBusinessOrderType(String businessOrderType) {
        super.setBusinessOrderType(businessOrderType);
    }

    @ExcelField(title = "商流订单号", align = 2, sort = 3)
    @Override
    public String getBusinessNo() {
        return super.getBusinessNo();
    }

    @Override
    public void setBusinessNo(String businessNo) {
        super.setBusinessNo(businessNo);
    }

    @ExcelField(title = "货主编码**必填", align = 2, sort = 4)
    @Override
    public String getOwner() {
        return super.getOwner();
    }

    @Override
    public void setOwner(String owner) {
        super.setOwner(owner);
    }

    @ExcelField(title = "客户编码", align = 2, sort = 5)
    @Override
    public String getCustomer() {
        return super.getCustomer();
    }

    @Override
    public void setCustomer(String customer) {
        super.setCustomer(customer);
    }

    @ExcelField(title = "供应商编码", align = 2, sort = 6)
    @Override
    public String getSupplierCode() {
        return super.getSupplierCode();
    }

    @ExcelField(title = "委托方编码", align = 2, sort = 6)
    @Override
    public String getPrincipal() {
        return super.getPrincipal();
    }

    @Override
    public void setPrincipal(String principal) {
        super.setPrincipal(principal);
    }

    @Override
    public void setSupplierCode(String supplierCode) {
        super.setSupplierCode(supplierCode);
    }

    @ExcelField(title = "下发机构**必填", align = 2, sort = 7)
    @Override
    public String getWarehouse() {
        return super.getWarehouse();
    }

    @Override
    public void setWarehouse(String warehouse) {
        super.setWarehouse(warehouse);
    }

    @ExcelField(title = "有效期至", align = 2, sort = 8)
    @Override
    public Date getValidityPeriod() {
        return super.getValidityPeriod();
    }

    @Override
    public void setValidityPeriod(Date validityPeriod) {
        super.setValidityPeriod(validityPeriod);
    }

    @ExcelField(title = "项目", align = 2, sort = 9)
    @Override
    public String getProject() {
        return super.getProject();
    }

    @Override
    public void setProject(String project) {
        super.setProject(project);
    }

    @ExcelField(title = "合同号", align = 2, sort = 10)
    @Override
    public String getContractNo() {
        return super.getContractNo();
    }

    @Override
    public void setContractNo(String contractNo) {
        super.setContractNo(contractNo);
    }

    @ExcelField(title = "客户订单号**必填 根据该字段合并生成订单", align = 2, sort = 11)
    @Override
    public String getCustomerNo() {
        return super.getCustomerNo();
    }

    @Override
    public void setCustomerNo(String customerNo) {
        super.setCustomerNo(customerNo);
    }

    @ExcelField(title = "店铺", align = 2, sort = 12)
    @Override
    public String getShop() {
        return super.getShop();
    }

    @Override
    public void setShop(String shop) {
        super.setShop(shop);
    }

    @ExcelField(title = "业务员", align = 2, sort = 13)
    @Override
    public String getClerkCode() {
        return super.getClerkCode();
    }

    @Override
    public void setClerkCode(String clerkCode) {
        super.setClerkCode(clerkCode);
    }

    @ExcelField(title = "会员级别", align = 2, sort = 14)
    @Override
    public String getVipStatus() {
        return super.getVipStatus();
    }

    @Override
    public void setVipStatus(String vipStatus) {
        super.setVipStatus(vipStatus);
    }

    @ExcelField(title = "校验库存充足**Y/N", align = 2, sort = 15)
    @Override
    public String getIsAvailableStock() {
        return super.getIsAvailableStock();
    }

    @Override
    public void setIsAvailableStock(String isAvailableStock) {
        super.setIsAvailableStock(isAvailableStock);
    }

    @ExcelField(title = "收货人", align = 2, sort = 16)
    @Override
    public String getConsignee() {
        return super.getConsignee();
    }

    @Override
    public void setConsignee(String consignee) {
        super.setConsignee(consignee);
    }

    @ExcelField(title = "收货人联系电话", align = 2, sort = 17)
    @Override
    public String getConsigneeTel() {
        return super.getConsigneeTel();
    }

    @Override
    public void setConsigneeTel(String consigneeTel) {
        super.setConsigneeTel(consigneeTel);
    }

    @ExcelField(title = "收货人区域", align = 2, sort = 18)
    @Override
    public String getConsigneeArea() {
        return super.getConsigneeArea();
    }

    @Override
    public void setConsigneeArea(String consigneeArea) {
        super.setConsigneeArea(consigneeArea);
    }

    @ExcelField(title = "收货人地址", align = 2, sort = 19)
    @Override
    public String getConsigneeAddress() {
        return super.getConsigneeAddress();
    }

    @Override
    public void setConsigneeAddress(String consigneeAddress) {
        super.setConsigneeAddress(consigneeAddress);
    }

    @ExcelField(title = "收货人地址区域", align = 2, sort = 20)
    @Override
    public String getConsigneeAddressArea() {
        return super.getConsigneeAddressArea();
    }

    @Override
    public void setConsigneeAddressArea(String consigneeAddressArea) {
        super.setConsigneeAddressArea(consigneeAddressArea);
    }

    @ExcelField(title = "发货人", align = 2, sort = 21)
    @Override
    public String getShipper() {
        return super.getShipper();
    }

    @Override
    public void setShipper(String shipper) {
        super.setShipper(shipper);
    }

    @ExcelField(title = "发货人联系电话", align = 2, sort = 22)
    @Override
    public String getShipperTel() {
        return super.getShipperTel();
    }

    @Override
    public void setShipperTel(String shipperTel) {
        super.setShipperTel(shipperTel);
    }

    @ExcelField(title = "发货人区域", align = 2, sort = 23)
    @Override
    public String getShipperArea() {
        return super.getShipperArea();
    }

    @Override
    public void setShipperArea(String shipperArea) {
        super.setShipperArea(shipperArea);
    }

    @ExcelField(title = "发货人地址", align = 2, sort = 24)
    @Override
    public String getShipperAddress() {
        return super.getShipperAddress();
    }

    @Override
    public void setShipperAddress(String shipperAddress) {
        super.setShipperAddress(shipperAddress);
    }

    @ExcelField(title = "发货人地址区域", align = 2, sort = 25)
    @Override
    public String getShipperAddressArea() {
        return super.getShipperAddressArea();
    }

    @Override
    public void setShipperAddressArea(String shipperAddressArea) {
        super.setShipperAddressArea(shipperAddressArea);
    }

    @ExcelField(title = "发货时间", align = 2, sort = 26)
    @Override
    public Date getDeliveryDate() {
        return super.getDeliveryDate();
    }

    @Override
    public void setDeliveryDate(Date deliveryDate) {
        super.setDeliveryDate(deliveryDate);
    }

    @ExcelField(title = "预计到货时间", align = 2, sort = 27)
    @Override
    public Date getArrivalTime() {
        return super.getArrivalTime();
    }

    @Override
    public void setArrivalTime(Date arrivalTime) {
        super.setArrivalTime(arrivalTime);
    }

    @ExcelField(title = "运输方式**1:铁路 2:航空 3:陆运\n4:快递 5:船运", align = 2, sort = 28)
    @Override
    public String getTransportMode() {
        return super.getTransportMode();
    }

    @Override
    public void setTransportMode(String transportMode) {
        super.setTransportMode(transportMode);
    }

    @ExcelField(title = "承运商", align = 2, sort = 29)
    @Override
    public String getCarrier() {
        return super.getCarrier();
    }

    @Override
    public void setCarrier(String carrier) {
        super.setCarrier(carrier);
    }

    @ExcelField(title = "物流单号", align = 2, sort = 30)
    @Override
    public String getLogisticsNo() {
        return super.getLogisticsNo();
    }

    @Override
    public void setLogisticsNo(String logisticsNo) {
        super.setLogisticsNo(logisticsNo);
    }

    @ExcelField(title = "车牌号", align = 2, sort = 31)
    @Override
    public String getVehicleNo() {
        return super.getVehicleNo();
    }

    @Override
    public void setVehicleNo(String vehicleNo) {
        super.setVehicleNo(vehicleNo);
    }

    @ExcelField(title = "司机", align = 2, sort = 32)
    @Override
    public String getDriver() {
        return super.getDriver();
    }

    @Override
    public void setDriver(String driver) {
        super.setDriver(driver);
    }

    @ExcelField(title = "司机联系电话", align = 2, sort = 33)
    @Override
    public String getContactTel() {
        return super.getContactTel();
    }

    @Override
    public void setContactTel(String contactTel) {
        super.setContactTel(contactTel);
    }

    @ExcelField(title = "运费金额", align = 2, sort = 34)
    @Override
    public BigDecimal getFreightCharge() {
        return super.getFreightCharge();
    }

    @Override
    public void setFreightCharge(BigDecimal freightCharge) {
        super.setFreightCharge(freightCharge);
    }

    @ExcelField(title = "业务人", align = 2, sort = 35)
    @Override
    public String getBusinessBy() {
        return super.getBusinessBy();
    }

    @Override
    public void setBusinessBy(String businessBy) {
        super.setBusinessBy(businessBy);
    }

    @ExcelField(title = "销售人", align = 2, sort = 36)
    @Override
    public String getSaleBy() {
        return super.getSaleBy();
    }

    @Override
    public void setSaleBy(String saleBy) {
        super.setSaleBy(saleBy);
    }

    @ExcelField(title = "发票类型", align = 2, sort = 37)
    @Override
    public String getInvoiceType() {
        return super.getInvoiceType();
    }

    @Override
    public void setInvoiceType(String invoiceType) {
        super.setInvoiceType(invoiceType);
    }

    @ExcelField(title = "公司名称", align = 2, sort = 38)
    @Override
    public String getCompanyName() {
        return super.getCompanyName();
    }

    @Override
    public void setCompanyName(String companyName) {
        super.setCompanyName(companyName);
    }

    @ExcelField(title = "纳税人识别号", align = 2, sort = 39)
    @Override
    public String getTaxpayerIdentityNumber() {
        return super.getTaxpayerIdentityNumber();
    }

    @Override
    public void setTaxpayerIdentityNumber(String taxpayerIdentityNumber) {
        super.setTaxpayerIdentityNumber(taxpayerIdentityNumber);
    }

    @ExcelField(title = "开户银行名称", align = 2, sort = 40)
    @Override
    public String getDepositBank() {
        return super.getDepositBank();
    }

    @Override
    public void setDepositBank(String depositBank) {
        super.setDepositBank(depositBank);
    }

    @ExcelField(title = "开户银行账号", align = 2, sort = 41)
    @Override
    public String getDepositBankAccount() {
        return super.getDepositBankAccount();
    }

    @Override
    public void setDepositBankAccount(String depositBankAccount) {
        super.setDepositBankAccount(depositBankAccount);
    }

    @ExcelField(title = "注册地址", align = 2, sort = 42)
    @Override
    public String getRegisteredArea() {
        return super.getRegisteredArea();
    }

    @Override
    public void setRegisteredArea(String registeredArea) {
        super.setRegisteredArea(registeredArea);
    }

    @ExcelField(title = "联系电话", align = 2, sort = 43)
    @Override
    public String getRegisteredTelephone() {
        return super.getRegisteredTelephone();
    }

    @Override
    public void setRegisteredTelephone(String registeredTelephone) {
        super.setRegisteredTelephone(registeredTelephone);
    }

    @ExcelField(title = "收票人名称", align = 2, sort = 44)
    @Override
    public String getTicketCollectorName() {
        return super.getTicketCollectorName();
    }

    @Override
    public void setTicketCollectorName(String ticketCollectorName) {
        super.setTicketCollectorName(ticketCollectorName);
    }

    @ExcelField(title = "收票人手机", align = 2, sort = 45)
    @Override
    public String getTicketCollectorPhone() {
        return super.getTicketCollectorPhone();
    }

    @Override
    public void setTicketCollectorPhone(String ticketCollectorPhone) {
        super.setTicketCollectorPhone(ticketCollectorPhone);
    }

    @ExcelField(title = "收票人地址", align = 2, sort = 46)
    @Override
    public String getTicketCollectorAddress() {
        return super.getTicketCollectorAddress();
    }

    @Override
    public void setTicketCollectorAddress(String ticketCollectorAddress) {
        super.setTicketCollectorAddress(ticketCollectorAddress);
    }

    @ExcelField(title = "预付金额", align = 2, sort = 47)
    @Override
    public BigDecimal getPrepaidAmount() {
        return super.getPrepaidAmount();
    }

    @Override
    public void setPrepaidAmount(BigDecimal prepaidAmount) {
        super.setPrepaidAmount(prepaidAmount);
    }

    @ExcelField(title = "预付金额", align = 2, sort = 47)
    @Override
    public String getSettlement() {
        return super.getSettlement();
    }

    @Override
    public void setSettlement(String settlement) {
        super.setSettlement(settlement);
    }

    @ExcelField(title = "结算方式", align = 2, sort = 48)
    @Override
    public String getSettleMode() {
        return super.getSettleMode();
    }

    @Override
    public void setSettleMode(String settleMode) {
        super.setSettleMode(settleMode);
    }

    @Override
    public String getChannel() {
        return super.getChannel();
    }

    @Override
    public void setChannel(String channel) {
        super.setChannel(channel);
    }

    @Override
    public String getCurrency() {
        return super.getCurrency();
    }

    @Override
    public void setCurrency(String currency) {
        super.setCurrency(currency);
    }

    @Override
    public BigDecimal getExchangeRate() {
        return super.getExchangeRate();
    }

    @Override
    public void setExchangeRate(BigDecimal exchangeRate) {
        super.setExchangeRate(exchangeRate);
    }

    @ExcelField(title = "商品编码**必填", align = 2, sort = 49)
    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    @ExcelField(title = "数量**必填 必须是数字类型", align = 2, sort = 50)
    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

}
