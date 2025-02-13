package com.yunyou.modules.oms.order.entity.extend;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.modules.oms.order.entity.OmChainHeaderEntity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 供应链订单导出Entity
 * @author WMJ
 * @version 2020-04-07
 */
public class OmChainHeaderExportEntity extends OmChainHeaderEntity {

    @Override
    @ExcelField(title = "供应链订单号", align = 2, sort = 1)
    public String getChainNo() {
        return super.getChainNo();
    }

    @Override
    @ExcelField(title = "客户订单号", align = 2, sort = 2)
    public String getCustomerNo() {
        return super.getCustomerNo();
    }

    @Override
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelField(title = "订单日期", align = 2, sort = 3)
    public Date getOrderDate() {
        return super.getOrderDate();
    }

    @Override
    @ExcelField(title = "下发机构", align = 2, sort = 4)
    public String getWarehouseName() {
        return super.getWarehouseName();
    }

    @Override
    @ExcelField(title = "货主", align = 2, sort = 5)
    public String getOwnerName() {
        return super.getOwnerName();
    }

    @Override
    @ExcelField(title = "供应商", align = 2, sort = 6)
    public String getSupplierName() {
        return super.getSupplierName();
    }

    @Override
    @ExcelField(title = "客户", align = 2, sort = 7)
    public String getCustomerName() {
        return super.getCustomerName();
    }

    @Override
    @ExcelField(title = "订单状态", align = 2, sort = 8, dictType = "OMS_CHAIN_STATUS")
    public String getStatus() {
        return super.getStatus();
    }

    @Override
    @ExcelField(title = "业务订单类型", align = 2, sort = 9, dictType = "OMS_BUSINESS_ORDER_TYPE")
    public String getBusinessOrderType() {
        return super.getBusinessOrderType();
    }

    @Override
    @ExcelField(title = "拦截状态", align = 2, sort = 10, dictType = "OMS_INTERCEPT_STATUS")
    public String getInterceptStatus() {
        return super.getInterceptStatus();
    }

    @Override
    @ExcelField(title = "商流订单号", align = 2, sort = 11)
    public String getBusinessNo() {
        return super.getBusinessNo();
    }

    @Override
    @ExcelField(title = "外部单号", align = 2, sort = 12)
    public String getDef1() {
        return super.getDef1();
    }

    @Override
    @ExcelField(title = "机构", align = 2, sort = 13)
    public String getOrgName() {
        return super.getOrgName();
    }

    @Override
    @ExcelField(title = "订单来源", align = 2, sort = 14, dictType = "SYS_ORDER_SOURCE")
    public String getOrderSource() {
        return super.getOrderSource();
    }

    @Override
    @ExcelField(title = "收货人", align = 2, sort = 15)
    public String getConsignee() {
        return super.getConsignee();
    }

    @Override
    @ExcelField(title = "收货人联系电话", align = 2, sort = 16)
    public String getConsigneeTel() {
        return super.getConsigneeTel();
    }

    @Override
    @ExcelField(title = "收货人区域", align = 2, sort = 17)
    public String getConsigneeAreaName() {
        return super.getConsigneeAreaName();
    }

    @Override
    @ExcelField(title = "收货人地址", align = 2, sort = 18)
    public String getConsigneeAddress() {
        return super.getConsigneeAddress();
    }

    @Override
    @ExcelField(title = "发货人", align = 2, sort = 19)
    public String getShipper() {
        return super.getShipper();
    }

    @Override
    @ExcelField(title = "发货人联系电话", align = 2, sort = 20)
    public String getShipperTel() {
        return super.getShipperTel();
    }

    @Override
    @ExcelField(title = "发货人区域", align = 2, sort = 21)
    public String getShipperAreaName() {
        return super.getShipperAreaName();
    }

    @Override
    @ExcelField(title = "发货人地址", align = 2, sort = 22)
    public String getShipperAddress() {
        return super.getShipperAddress();
    }

    @Override
    @ExcelField(title = "委托方", align = 2, sort = 23)
    public String getPrincipalName() {
        return super.getPrincipalName();
    }

    @Override
    @ExcelField(title = "承运商", align = 2, sort = 24)
    public String getCarrierName() {
        return super.getCarrierName();
    }

    @Override
    @ExcelField(title = "物流单号", align = 2, sort = 25)
    public String getLogisticsNo() {
        return super.getLogisticsNo();
    }

    @Override
    @ExcelField(title = "运输方式", align = 2, sort = 26, dictType = "TMS_TRANSPORT_METHOD")
    public String getTransportMode() {
        return super.getTransportMode();
    }

    @Override
    @ExcelField(title = "车牌号", align = 2, sort = 27)
    public String getVehicleNo() {
        return super.getVehicleNo();
    }

    @Override
    @ExcelField(title = "司机", align = 2, sort = 28)
    public String getDriver() {
        return super.getDriver();
    }

    @Override
    @ExcelField(title = "联系电话", align = 2, sort = 29)
    public String getContactTel() {
        return super.getContactTel();
    }

    @Override
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelField(title = "有效期", align = 2, sort = 30)
    public Date getValidityPeriod() {
        return super.getValidityPeriod();
    }

    @Override
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelField(title = "到货时间", align = 2, sort = 31)
    public Date getArrivalTime() {
        return super.getArrivalTime();
    }

    @Override
    @ExcelField(title = "合同号", align = 2, sort = 32)
    public String getContractNo() {
        return super.getContractNo();
    }

    @Override
    @ExcelField(title = "渠道", align = 2, sort = 33, dictType = "OMS_CHANNEL")
    public String getChannel() {
        return super.getChannel();
    }

    @Override
    @ExcelField(title = "业务人", align = 2, sort = 35)
    public String getBusinessBy() {
        return super.getBusinessBy();
    }

    @Override
    @ExcelField(title = "制单人", align = 2, sort = 36)
    public String getPreparedBy() {
        return super.getPreparedBy();
    }

    @Override
    @ExcelField(title = "审核人", align = 2, sort = 37)
    public String getAuditBy() {
        return super.getAuditBy();
    }

    @Override
    @ExcelField(title = "预付金额", align = 2, sort = 38)
    public BigDecimal getPrepaidAmount() {
        return super.getPrepaidAmount();
    }

    @Override
    @ExcelField(title = "结算对象", align = 2, sort = 39)
    public String getSettlementName() {
        return super.getSettlementName();
    }

    @Override
    @ExcelField(title = "结算方式", align = 2, sort = 40)
    public String getSettleMode() {
        return super.getSettleMode();
    }

    @Override
    @ExcelField(title = "币种", align = 2, sort = 41)
    public String getCurrency() {
        return super.getCurrency();
    }

    @Override
    @ExcelField(title = "汇率", align = 2, sort = 42)
    public BigDecimal getExchangeRate() {
        return super.getExchangeRate();
    }

    @Override
    @ExcelField(title = "汇率", align = 2, sort = 43)
    public String getShop() {
        return super.getShop();
    }

    @Override
    @ExcelField(title = "发票抬头", align = 2, sort = 44)
    public String getInvoice() {
        return super.getInvoice();
    }

    @Override
    @ExcelField(title = "税号", align = 2, sort = 45)
    public String getTaxNo() {
        return super.getTaxNo();
    }

    @Override
    @ExcelField(title = "预售订单号", align = 2, sort = 46)
    public String getPreSaleNo() {
        return super.getPreSaleNo();
    }
}
