package com.yunyou.modules.sys.common.entity;

import com.yunyou.core.persistence.DataEntity;

import javax.validation.constraints.NotNull;

/**
 * 质检项Entity
 */
public class SysWmsQcItemDetail extends DataEntity<SysWmsQcItemDetail> {

    private static final long serialVersionUID = 1L;
    private String itemGroupCode;// 质检项组编码
    private String lineNo;// 行号
    private String qcItem;// 质检项名称
    private String qcRef;// 质检参考标准
    private String qcWay;// 质检方法
    private String def1;// 自定义1
    private String def2;// 自定义2
    private String def3;// 自定义3
    private String def4;// 自定义4
    private String def5;// 自定义5
    private String headerId;// 头表Id
    @NotNull(message = "数据套不能为空")
    private String dataSet;// 数据套

    public SysWmsQcItemDetail() {
        super();
    }

    public SysWmsQcItemDetail(String id) {
        super(id);
    }

    public SysWmsQcItemDetail(String headerId, String dataSet) {
        this.headerId = headerId;
        this.dataSet = dataSet;
    }

    public String getItemGroupCode() {
        return itemGroupCode;
    }

    public void setItemGroupCode(String itemGroupCode) {
        this.itemGroupCode = itemGroupCode;
    }

    public String getLineNo() {
        return lineNo;
    }

    public void setLineNo(String lineNo) {
        this.lineNo = lineNo;
    }

    public String getQcItem() {
        return qcItem;
    }

    public void setQcItem(String qcItem) {
        this.qcItem = qcItem;
    }

    public String getQcRef() {
        return qcRef;
    }

    public void setQcRef(String qcRef) {
        this.qcRef = qcRef;
    }

    public String getQcWay() {
        return qcWay;
    }

    public void setQcWay(String qcWay) {
        this.qcWay = qcWay;
    }

    public String getDef1() {
        return def1;
    }

    public void setDef1(String def1) {
        this.def1 = def1;
    }

    public String getDef2() {
        return def2;
    }

    public void setDef2(String def2) {
        this.def2 = def2;
    }

    public String getDef3() {
        return def3;
    }

    public void setDef3(String def3) {
        this.def3 = def3;
    }

    public String getDef4() {
        return def4;
    }

    public void setDef4(String def4) {
        this.def4 = def4;
    }

    public String getDef5() {
        return def5;
    }

    public void setDef5(String def5) {
        this.def5 = def5;
    }

    public String getHeaderId() {
        return headerId;
    }

    public void setHeaderId(String headerId) {
        this.headerId = headerId;
    }

    public String getDataSet() {
        return dataSet;
    }

    public void setDataSet(String dataSet) {
        this.dataSet = dataSet;
    }
}