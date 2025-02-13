package com.yunyou.modules.bms.finance.entity;

import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.common.utils.time.DateFormatUtil;
import com.yunyou.core.persistence.DataEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 描述：费用明细Entity
 *
 * @author Jianhua
 * @version 2019/7/13
 */
public class BmsBillDetail extends DataEntity<BmsBillDetail> {

    private static final long serialVersionUID = 1L;
    @ExcelField(title = "费用单号")
    private String billNo;
    // 状态(新建/已生成对账单/已核销)
    private String status;
    // 帐单号
    private String confirmNo;
    @ExcelField(title = "模型编码")
    private String settleModelCode;
    @ExcelField(title = "结算对象编码")
    private String settleObjectCode;
    @ExcelField(title = "结算对象名称")
    private String settleObjectName;
    // 结算方式
    private String settleMethod;
    // 结算类别
    private String settleCategory;
    @ExcelField(title = "系统合同编号")
    private String sysContractNo;
    @ExcelField(title = "客户合同编号")
    private String contractNo;
    @ExcelField(title = "子合同编号")
    private String subcontractNo;
    @ExcelField(title = "费用模块")
    private String billModule;
    @ExcelField(title = "费用科目编码")
    private String billSubjectCode;
    @ExcelField(title = "费用科目名称")
    private String billSubjectName;
    @ExcelField(title = "费用类别")
    private String billCategory;
    @ExcelField(title = "应收应付")
    private String receivablePayable;
    @ExcelField(title = "计费条款编码")
    private String billTermsCode;
    @ExcelField(title = "计费条款说明")
    private String billTermsDesc;
    @ExcelField(title = "输出对象")
    private String outputObjects;
    @ExcelField(title = "公式")
    private String formulaName;
    @ExcelField(title = "公式参数值描述")
    private String formulaParamsDesc;
    @ExcelField(title = "计费标准")
    private BigDecimal billStandard;
    @ExcelField(title = "计费量")
    private BigDecimal billQty;
    @ExcelField(title = "发生量")
    private BigDecimal occurrenceQty;
    @ExcelField(title = "费用")
    private BigDecimal cost;
    // 未税单价
    private BigDecimal price;
    // 含税单价
    private BigDecimal taxPrice;
    // 合同单价
    private BigDecimal contractPrice;
    // 物流点数
    private BigDecimal logisticsPoints;

    @ExcelField(title = "日期范围从", format = DateFormatUtil.PATTERN_ISO_ON_DATE)
    @JsonFormat(pattern = DateFormatUtil.PATTERN_ISO_ON_DATE)
    private Date dateFm;
    @ExcelField(title = "日期范围到", format = DateFormatUtil.PATTERN_ISO_ON_DATE)
    @JsonFormat(pattern = DateFormatUtil.PATTERN_ISO_ON_DATE)
    private Date dateTo;
    @ExcelField(title = "业务日期", format = DateFormatUtil.PATTERN_ISO_ON_DATE)
    @JsonFormat(pattern = DateFormatUtil.PATTERN_ISO_ON_DATE)
    private Date businessDate;
    // 业务类型
    private String businessType;
    @ExcelField(title = "系统订单号")
    private String sysOrderNo;
    @ExcelField(title = "客户订单号")
    private String customerOrderNo;
    // 业务机构编码
    @ExcelField(title = "仓库编码")
    private String warehouseCode;
    // 业务机构名称
    @ExcelField(title = "仓库名称")
    private String warehouseName;
    @ExcelField(title = "供应商编码")
    private String supplierCode;
    @ExcelField(title = "供应商名称")
    private String supplierName;
    @ExcelField(title = "货主编码")
    private String ownerCode;
    @ExcelField(title = "货主名称")
    private String ownerName;
    @ExcelField(title = "收货方编码")
    private String consigneeCode;
    @ExcelField(title = "收货方名称")
    private String consigneeName;
    @ExcelField(title = "承运商编码")
    private String carrierCode;
    @ExcelField(title = "承运商名称")
    private String carrierName;
    @ExcelField(title = "商品编码")
    private String skuCode;
    @ExcelField(title = "商品名称")
    private String skuName;
    @ExcelField(title = "车型")
    private String carType;
    @ExcelField(title = "车牌号")
    private String carNo;
    @ExcelField(title = "司机")
    private String driver;
    @ExcelField(title = "检疫证类型")
    private String quarantineType;
    // 路线
    private String route;
    // 机构ID
    private String orgId;

    private String dateRange;// 日期范围

    public BmsBillDetail() {
        super();
    }

    public BmsBillDetail(String id) {
        super(id);
    }

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

    public String getConfirmNo() {
        return confirmNo;
    }

    public void setConfirmNo(String confirmNo) {
        this.confirmNo = confirmNo;
    }

    public String getSettleModelCode() {
        return settleModelCode;
    }

    public void setSettleModelCode(String settleModelCode) {
        this.settleModelCode = settleModelCode;
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

    public String getSettleMethod() {
        return settleMethod;
    }

    public void setSettleMethod(String settleMethod) {
        this.settleMethod = settleMethod;
    }

    public String getSettleCategory() {
        return settleCategory;
    }

    public void setSettleCategory(String settleCategory) {
        this.settleCategory = settleCategory;
    }

    public String getSysContractNo() {
        return sysContractNo;
    }

    public void setSysContractNo(String sysContractNo) {
        this.sysContractNo = sysContractNo;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String getSubcontractNo() {
        return subcontractNo;
    }

    public void setSubcontractNo(String subcontractNo) {
        this.subcontractNo = subcontractNo;
    }

    public String getBillModule() {
        return billModule;
    }

    public void setBillModule(String billModule) {
        this.billModule = billModule;
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

    public String getBillCategory() {
        return billCategory;
    }

    public void setBillCategory(String billCategory) {
        this.billCategory = billCategory;
    }

    public String getReceivablePayable() {
        return receivablePayable;
    }

    public void setReceivablePayable(String receivablePayable) {
        this.receivablePayable = receivablePayable;
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

    public String getFormulaName() {
        return formulaName;
    }

    public void setFormulaName(String formulaName) {
        this.formulaName = formulaName;
    }

    public String getFormulaParamsDesc() {
        return formulaParamsDesc;
    }

    public void setFormulaParamsDesc(String formulaParamsDesc) {
        this.formulaParamsDesc = formulaParamsDesc;
    }

    public BigDecimal getBillStandard() {
        return billStandard;
    }

    public void setBillStandard(BigDecimal billStandard) {
        this.billStandard = billStandard;
    }

    public BigDecimal getBillQty() {
        return billQty;
    }

    public void setBillQty(BigDecimal billQty) {
        this.billQty = billQty;
    }

    public BigDecimal getOccurrenceQty() {
        return occurrenceQty;
    }

    public void setOccurrenceQty(BigDecimal occurrenceQty) {
        this.occurrenceQty = occurrenceQty;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getTaxPrice() {
        return taxPrice;
    }

    public void setTaxPrice(BigDecimal taxPrice) {
        this.taxPrice = taxPrice;
    }

    public BigDecimal getContractPrice() {
        return contractPrice;
    }

    public void setContractPrice(BigDecimal contractPrice) {
        this.contractPrice = contractPrice;
    }

    public BigDecimal getLogisticsPoints() {
        return logisticsPoints;
    }

    public void setLogisticsPoints(BigDecimal logisticsPoints) {
        this.logisticsPoints = logisticsPoints;
    }

    public Date getDateFm() {
        return dateFm;
    }

    public void setDateFm(Date dateFm) {
        this.dateFm = dateFm;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public Date getBusinessDate() {
        return businessDate;
    }

    public void setBusinessDate(Date businessDate) {
        this.businessDate = businessDate;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getSysOrderNo() {
        return sysOrderNo;
    }

    public void setSysOrderNo(String sysOrderNo) {
        this.sysOrderNo = sysOrderNo;
    }

    public String getCustomerOrderNo() {
        return customerOrderNo;
    }

    public void setCustomerOrderNo(String customerOrderNo) {
        this.customerOrderNo = customerOrderNo;
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

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getOwnerCode() {
        return ownerCode;
    }

    public void setOwnerCode(String ownerCode) {
        this.ownerCode = ownerCode;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
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

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
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

    public String getQuarantineType() {
        return quarantineType;
    }

    public void setQuarantineType(String quarantineType) {
        this.quarantineType = quarantineType;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getDateRange() {
        if (this.dateFm != null && this.dateTo != null) {
            return DateFormatUtil.formatDate("yyyyMMdd", this.dateFm) + " ~ " + DateFormatUtil.formatDate("yyyyMMdd", this.dateTo);
        }
        return "";
    }

    @Override
    @ExcelField(title = "备注")
    public String getRemarks() {
        return super.getRemarks();
    }
}