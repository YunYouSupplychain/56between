package com.yunyou.modules.sys.common.entity;

import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.core.persistence.DataEntity;

/**
 * 开票对象明细Entity
 */
public class SysBmsInvoiceObjectDetail extends DataEntity<SysBmsInvoiceObjectDetail> {

    private static final long serialVersionUID = 1L;
    private SysBmsInvoiceObject sysBmsInvoiceObject;// 开票对象ID 父类
    private String code;// 客户编码
    private String name;// 客户名称
    private String dataSet;// 数据套

    public SysBmsInvoiceObjectDetail() {
        super();
        this.setIdType(IDTYPE_AUTO);
    }

    public SysBmsInvoiceObjectDetail(String id) {
        super(id);
    }

    public SysBmsInvoiceObjectDetail(SysBmsInvoiceObject sysBmsInvoiceObject) {
        this.sysBmsInvoiceObject = sysBmsInvoiceObject;
    }

    public SysBmsInvoiceObject getSysBmsInvoiceObject() {
        return sysBmsInvoiceObject;
    }

    public void setSysBmsInvoiceObject(SysBmsInvoiceObject sysBmsInvoiceObject) {
        this.sysBmsInvoiceObject = sysBmsInvoiceObject;
    }

    @ExcelField(title = "客户编码", align = 2, sort = 8)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @ExcelField(title = "客户名称", align = 2, sort = 9)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDataSet() {
        return dataSet;
    }

    public void setDataSet(String dataSet) {
        this.dataSet = dataSet;
    }
}