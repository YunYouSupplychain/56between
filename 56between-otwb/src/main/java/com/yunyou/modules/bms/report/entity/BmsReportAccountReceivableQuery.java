package com.yunyou.modules.bms.report.entity;

import com.yunyou.core.persistence.DataEntity;

import java.util.Date;

/**
 * 描述：应收账款查询实体
 *
 * @author liujianhua created on 2019-12-9
 */
public class BmsReportAccountReceivableQuery extends DataEntity<BmsReportAccountReceivableQuery> {
    private static final long serialVersionUID = -8859558756262600900L;

    // 费用单号
    private String billNo;
    // 状态
    private String status;
    // 开票对象编码
    private String invoiceObjectCode;
    // 开票对象名称
    private String invoiceObjectName;
    // 仓库编码
    private String warehouseCode;
    // 仓库名称
    private String warehouseName;
    // 结算对象编码
    private String settleObjectCode;
    // 结算对象名称
    private String settleObjectName;
    // 结算日期从
    private Date settleDateFm;
    // 结算日期到
    private Date settleDateTo;
    // 机构ID
    private String orgId;

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInvoiceObjectCode() {
        return invoiceObjectCode;
    }

    public void setInvoiceObjectCode(String invoiceObjectCode) {
        this.invoiceObjectCode = invoiceObjectCode;
    }

    public String getInvoiceObjectName() {
        return invoiceObjectName;
    }

    public void setInvoiceObjectName(String invoiceObjectName) {
        this.invoiceObjectName = invoiceObjectName;
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

    public String getSettleObjectCode() {
        return settleObjectCode;
    }

    public void setSettleObjectCode(String settleObjectCode) {
        this.settleObjectCode = settleObjectCode;
    }

    public String getSettleObjectName() {
        return settleObjectName;
    }

    public void setSettleObjectName(String settleObjectName) {
        this.settleObjectName = settleObjectName;
    }

    public Date getSettleDateFm() {
        return settleDateFm;
    }

    public void setSettleDateFm(Date settleDateFm) {
        this.settleDateFm = settleDateFm;
    }

    public Date getSettleDateTo() {
        return settleDateTo;
    }

    public void setSettleDateTo(Date settleDateTo) {
        this.settleDateTo = settleDateTo;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
}
