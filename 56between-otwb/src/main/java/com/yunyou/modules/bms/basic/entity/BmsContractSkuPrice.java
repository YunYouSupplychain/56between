package com.yunyou.modules.bms.basic.entity;

import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.core.persistence.DataEntity;

import java.math.BigDecimal;

/**
 * 合同商品价格Entity
 *
 * @author Jianhua Liu
 * @version 2019-05-27
 */
public class BmsContractSkuPrice extends DataEntity<BmsContractSkuPrice> {

    private static final long serialVersionUID = 1L;
    @ExcelField(title = "系统合同编号")
    private String sysContractNo;
    @ExcelField(title = "商品编码")
    private String skuCode;
    @ExcelField(title = "商品名称")
    private String skuName;
    @ExcelField(title = "品类")
    private String skuClass;
    @ExcelField(title = "未税单价")
    private BigDecimal price;
    @ExcelField(title = "含税单价")
    private BigDecimal taxPrice;
    @ExcelField(title = "单位", dictType = "BMS_CONTRACT_UNIT")
    private String unit;
    // 机构ID
    private String orgId;

    public BmsContractSkuPrice() {
        super();
    }

    public BmsContractSkuPrice(String id) {
        super(id);
    }

    public String getSysContractNo() {
        return sysContractNo;
    }

    public void setSysContractNo(String sysContractNo) {
        this.sysContractNo = sysContractNo;
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

    public String getSkuClass() {
        return skuClass;
    }

    public void setSkuClass(String skuClass) {
        this.skuClass = skuClass;
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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
}