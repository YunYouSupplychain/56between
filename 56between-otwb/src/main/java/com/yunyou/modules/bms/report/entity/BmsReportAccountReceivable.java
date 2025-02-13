package com.yunyou.modules.bms.report.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;
import com.yunyou.common.utils.time.DateFormatUtil;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 描述：应收账款结算单
 *
 * @author liujianhua created on 2019-12-9
 */
public class BmsReportAccountReceivable implements Serializable {
    private static final long serialVersionUID = 2892801225997695052L;

    // 费用单号
    private String billNo;
    // 结算日期从
    private Date settleDateFm;
    // 结算日期到
    private Date settleDateTo;
    // 开票对象编码
    private String invoiceObjectCode;
    // 开票对象名称
    private String invoiceObjectName;
    // 开户行
    private String bank;
    // 银行账户
    private String bankAccount;
    // 备注
    private String remarks;
    // 快行天下
    private String kxtxCode;
    private String kxtxName;
    private String kxtxBank;
    private String kxtxBankAccount;
    private String kxtxRemarks;
    // 机构ID
    private String orgId;
    // 子数据源1
    private List<BmsReportAccountReceivableSub1> sub1List = Lists.newArrayList();
    // 子数据源2
    private List<BmsReportAccountReceivableSub2> sub2List = Lists.newArrayList();

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public Date getSettleDateFm() {
        return settleDateFm;
    }

    public void setSettleDateFm(Date settleDateFm) {
        this.settleDateFm = settleDateFm;
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public Date getSettleDateTo() {
        return settleDateTo;
    }

    public void setSettleDateTo(Date settleDateTo) {
        this.settleDateTo = settleDateTo;
    }

    public String getSettleCycle() {
        if (this.settleDateFm != null && this.settleDateTo != null) {
            return DateFormatUtil.formatDate("yyyy.MM.dd", this.settleDateFm)
                    + "-" + DateFormatUtil.formatDate("MM.dd", this.settleDateTo);
        }
        return "";
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

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getKxtxCode() {
        return kxtxCode;
    }

    public void setKxtxCode(String kxtxCode) {
        this.kxtxCode = kxtxCode;
    }

    public String getKxtxName() {
        return kxtxName;
    }

    public void setKxtxName(String kxtxName) {
        this.kxtxName = kxtxName;
    }

    public String getKxtxBank() {
        return kxtxBank;
    }

    public void setKxtxBank(String kxtxBank) {
        this.kxtxBank = kxtxBank;
    }

    public String getKxtxBankAccount() {
        return kxtxBankAccount;
    }

    public void setKxtxBankAccount(String kxtxBankAccount) {
        this.kxtxBankAccount = kxtxBankAccount;
    }

    public String getKxtxRemarks() {
        return kxtxRemarks;
    }

    public void setKxtxRemarks(String kxtxRemarks) {
        this.kxtxRemarks = kxtxRemarks;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public List<BmsReportAccountReceivableSub1> getSub1List() {
        return sub1List;
    }

    public void setSub1List(List<BmsReportAccountReceivableSub1> sub1List) {
        this.sub1List = sub1List;
    }

    public List<BmsReportAccountReceivableSub2> getSub2List() {
        return sub2List;
    }

    public void setSub2List(List<BmsReportAccountReceivableSub2> sub2List) {
        this.sub2List = sub2List;
    }
}
