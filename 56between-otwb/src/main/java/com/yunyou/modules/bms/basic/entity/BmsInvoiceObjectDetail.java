package com.yunyou.modules.bms.basic.entity;

import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.core.persistence.DataEntity;

/**
 * 开票对象明细Entity
 * @author zqs
 * @version 2019-02-18
 */
public class BmsInvoiceObjectDetail extends DataEntity<BmsInvoiceObjectDetail> {

    private static final long serialVersionUID = 1L;
    private BmsInvoiceObject baseInvoiceObject;		// 开票对象ID 父类
    private String code;		// 客户编码
    private String name;		// 客户名称
    private String orgId;		// 机构ID

    public BmsInvoiceObjectDetail() {
        super();
        this.setIdType(IDTYPE_AUTO);
    }

    public BmsInvoiceObjectDetail(String id){
        super(id);
    }

    public BmsInvoiceObjectDetail(BmsInvoiceObject baseInvoiceObject){
        this.baseInvoiceObject = baseInvoiceObject;
    }

    public BmsInvoiceObject getBaseInvoiceObject() {
        return baseInvoiceObject;
    }

    public void setBaseInvoiceObject(BmsInvoiceObject baseInvoiceObject) {
        this.baseInvoiceObject = baseInvoiceObject;
    }

    @ExcelField(title="客户编码", align=2, sort=8)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @ExcelField(title="客户名称", align=2, sort=9)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
}