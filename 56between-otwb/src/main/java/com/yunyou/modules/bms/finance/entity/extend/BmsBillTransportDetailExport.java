package com.yunyou.modules.bms.finance.entity.extend;

import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.common.utils.time.DateFormatUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * 描述：运输费用导出模板
 *
 * @author liujianhua created on 2019-12-5
 */
public class BmsBillTransportDetailExport implements Serializable {
    private static final long serialVersionUID = 6154212236632671710L;

    @ExcelField(title = "费用单号", align = 1, sort = 1)
    private String billNo;              // 费用单号
    @ExcelField(title = "日期范围", align = 1, sort = 2)
    private String dateRange;           // 日期范围
    @ExcelField(title = "仓库编码", align = 1, sort = 3)
    private String warehouseCode;       // 仓库编码
    @ExcelField(title = "仓库名称", align = 1, sort = 4)
    private String warehouseName;       // 仓库名称
    @ExcelField(title = "结算模型编码", align = 1, sort = 5)
    private String settleModelCode; // 结算模型编码
    @ExcelField(title = "客户合同编号", align = 1, sort = 6)
    private String contractNo;          // 客户合同编号
    @ExcelField(title = "结算对象编码", align = 1, sort = 7)
    private String settleObjectCode;// 结算对象代码
    @ExcelField(title = "结算对象名称", align = 1, sort = 8)
    private String settleObjectName;// 结算对象名称
    @ExcelField(title = "应收应付", dictType = "BMS_RECEIVABLE_PAYABLE", align = 1, sort = 9)
    private String receivablePayable;   // 应收应付
    @ExcelField(title = "业务日期", format = DateFormatUtil.PATTERN_ISO_ON_DATE, align = 1, sort = 10)
    private Date businessDate;          // 业务日期
    @ExcelField(title = "系统订单号", align = 1, sort = 11)
    private String sysOrderNo;          // 系统订单号
    @ExcelField(title = "承运商编码", align = 1, sort = 12)
    private String carrierCode;         // 承运商编码
    @ExcelField(title = "承运商名称", align = 1, sort = 13)
    private String carrierName;         // 承运商名称
    @ExcelField(title = "车牌号", align = 1, sort = 14)
    private String carNo;               // 车牌号
    @ExcelField(title = "司机", align = 1, sort = 15)
    private String driver;              // 司机
    @ExcelField(title = "车型", dictType = "TMS_CAR_TYPE", align = 1, sort = 16)
    private String carType;             // 车型
    @ExcelField(title = "路线", align = 1, sort = 17)
    private String route;               // 路线
    @ExcelField(title = "费用科目代码", align = 1, sort = 18)
    private String billSubjectCode;     // 费用科目代码
    @ExcelField(title = "费用科目名称", align = 1, sort = 19)
    private String billSubjectName;     // 费用科目名称
    @ExcelField(title = "计费标准", align = 1, sort = 20)
    private Double billStandard;        // 计费标准
    @ExcelField(title = "发生量", align = 1, sort = 21)
    private Double occurrenceQty;       // 发生量
    @ExcelField(title = "计费量", align = 1, sort = 22)
    private Double billQty;             // 计费量
    @ExcelField(title = "费用", align = 1, sort = 23)
    private Double cost;                // 费用
    @ExcelField(title = "系统合同号", align = 1, sort = 24)
    private String sysContractNo;       // 系统合同编号
    @ExcelField(title = "子合同号", align = 1, sort = 25)
    private String subcontractNo;       // 子合同编号
    @ExcelField(title = "公式", align = 1, sort = 26)
    private String formula;             // 公式(名称)
    @ExcelField(title = "机构", align = 1, sort = 27)
    private String orgName;             // 机构
    @ExcelField(title = "备注", align = 1, sort = 28)
    private String remarks;             // 备注

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getDateRange() {
        return dateRange;
    }

    public void setDateRange(String dateRange) {
        this.dateRange = dateRange;
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

    public String getSettleModelCode() {
        return settleModelCode;
    }

    public void setSettleModelCode(String settleModelCode) {
        this.settleModelCode = settleModelCode;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
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

    public String getReceivablePayable() {
        return receivablePayable;
    }

    public void setReceivablePayable(String receivablePayable) {
        this.receivablePayable = receivablePayable;
    }

    public Date getBusinessDate() {
        return businessDate;
    }

    public void setBusinessDate(Date businessDate) {
        this.businessDate = businessDate;
    }

    public String getSysOrderNo() {
        return sysOrderNo;
    }

    public void setSysOrderNo(String sysOrderNo) {
        this.sysOrderNo = sysOrderNo;
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

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
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

    public Double getBillStandard() {
        return billStandard;
    }

    public void setBillStandard(Double billStandard) {
        this.billStandard = billStandard;
    }

    public Double getOccurrenceQty() {
        return occurrenceQty;
    }

    public void setOccurrenceQty(Double occurrenceQty) {
        this.occurrenceQty = occurrenceQty;
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

    public String getSysContractNo() {
        return sysContractNo;
    }

    public void setSysContractNo(String sysContractNo) {
        this.sysContractNo = sysContractNo;
    }

    public String getSubcontractNo() {
        return subcontractNo;
    }

    public void setSubcontractNo(String subcontractNo) {
        this.subcontractNo = subcontractNo;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }


    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
