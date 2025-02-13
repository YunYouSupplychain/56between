package com.yunyou.modules.sys.common.entity;

import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.core.persistence.DataEntity;

import javax.validation.constraints.NotNull;

/**
 * 商品供应商Entity
 */
public class SysCommonSkuSupplier extends DataEntity<SysCommonSkuSupplier> {

    private static final long serialVersionUID = 1L;
    private SysCommonSku sku;// 外键 父类
    private String supplierCode;// 供应商代码
    private String dataSet;// 数据套
    private String projectId;// 项目
    private String isDefault;// 是否默认

    // 扩展字段
    private String supplierName;// 供应商名称

    public SysCommonSkuSupplier() {
        super();
    }

    public SysCommonSkuSupplier(String id) {
        super(id);
    }

    public SysCommonSkuSupplier(SysCommonSku sku) {
        this.sku = sku;
        this.dataSet = sku.getDataSet();
    }

    @NotNull(message = "外键不能为空")
    public SysCommonSku getSku() {
        return sku;
    }

    public void setSku(SysCommonSku sku) {
        this.sku = sku;
    }

    @ExcelField(title = "供应商代码", align = 2, sort = 8)
    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    @ExcelField(title = "供应商名称", align = 2, sort = 9)
    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getDataSet() {
        return dataSet;
    }

    public void setDataSet(String dataSet) {
        this.dataSet = dataSet;
    }

    @ExcelField(title = "项目", align = 2, sort = 11)
    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }
}