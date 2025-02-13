package com.yunyou.modules.bms.basic.entity.template;

import com.yunyou.common.utils.excel.annotation.ExcelField;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 描述：合同商品价格导入模板
 *
 * @author Jianhua
 * @version 2019/7/4
 */
public class BmsContractSkuPriceImport implements Serializable {

    private static final long serialVersionUID = -4337759409235553171L;
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
    @ExcelField(title = "机构编码")
    private String orgCode;

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

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }
}
