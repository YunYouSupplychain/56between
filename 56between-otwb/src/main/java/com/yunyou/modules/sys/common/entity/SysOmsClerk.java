package com.yunyou.modules.sys.common.entity;

import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.core.persistence.DataEntity;

/**
 * 业务员Entity
 */
public class SysOmsClerk extends DataEntity<SysOmsClerk> {

    private static final long serialVersionUID = 1L;
    private String clerkCode;// 业务员代码
    private String clerkName;// 业务员名称
    private String phone;// 联系电话
    private String dataSet;// 数据套

    private String dataSetName;// 数据套名称

    public SysOmsClerk() {
        super();
    }

    public SysOmsClerk(String id) {
        super(id);
    }

    public SysOmsClerk(String id, String clerkCode, String dataSet) {
        super(id);
        this.clerkCode = clerkCode;
        this.dataSet = dataSet;
    }

    @ExcelField(title = "业务员代码", align = 2, sort = 7)
    public String getClerkCode() {
        return clerkCode;
    }

    public void setClerkCode(String clerkCode) {
        this.clerkCode = clerkCode;
    }

    @ExcelField(title = "业务员名称", align = 2, sort = 8)
    public String getClerkName() {
        return clerkName;
    }

    public void setClerkName(String clerkName) {
        this.clerkName = clerkName;
    }

    @ExcelField(title = "联系电话", align = 2, sort = 9)
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDataSet() {
        return dataSet;
    }

    public void setDataSet(String dataSet) {
        this.dataSet = dataSet;
    }

    public String getDataSetName() {
        return dataSetName;
    }

    public void setDataSetName(String dataSetName) {
        this.dataSetName = dataSetName;
    }
}