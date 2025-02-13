package com.yunyou.modules.sys.common.entity;

import com.yunyou.core.persistence.TreeEntity;

/**
 * 网点拓扑图Entity
 *
 * @author liujianhua
 * @version 2020-03-02
 */
public class SysTmsOutletRelation extends TreeEntity<SysTmsOutletRelation> {

    private static final long serialVersionUID = 1L;
    private String code;// 网点编码
    private String dataSet;// 数据套

    // 扩展字段
    private String dataSetName;// 数据套名称
    private String oldParentIds;// 修改前所有父级编号
    private String oldCode;// 修改器网点编码

    public SysTmsOutletRelation() {
        super();
    }

    public SysTmsOutletRelation(String id) {
        super(id);
    }

    public SysTmsOutletRelation(String code, String dataSet) {
        this.code = code;
        this.dataSet = dataSet;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public SysTmsOutletRelation getParent() {
        return parent;
    }

    @Override
    public void setParent(SysTmsOutletRelation parent) {
        this.parent = parent;
    }

    @Override
    public String getParentId() {
        return parent != null && parent.getId() != null ? parent.getId() : "0";
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

    public String getOldParentIds() {
        return oldParentIds;
    }

    public void setOldParentIds(String oldParentIds) {
        this.oldParentIds = oldParentIds;
    }

    public String getOldCode() {
        return oldCode;
    }

    public void setOldCode(String oldCode) {
        this.oldCode = oldCode;
    }
}