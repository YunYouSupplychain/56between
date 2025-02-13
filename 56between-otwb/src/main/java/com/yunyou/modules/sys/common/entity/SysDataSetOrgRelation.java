package com.yunyou.modules.sys.common.entity;

import com.yunyou.core.persistence.DataEntity;

import javax.validation.constraints.NotNull;

/**
 * 数据套机构关系Entity
 */
public class SysDataSetOrgRelation extends DataEntity<SysDataSetOrgRelation> {
    private static final long serialVersionUID = 1L;
    @NotNull(message = "数据套不能为空")
    private String dataSet;// 数据套
    @NotNull(message = "机构不能为空")
    private String orgId;// 机构ID

    public SysDataSetOrgRelation() {
        super();
    }

    public SysDataSetOrgRelation(String id) {
        super(id);
    }

    public SysDataSetOrgRelation(String id, String dataSet, String orgId) {
        super(id);
        this.dataSet = dataSet;
        this.orgId = orgId;
    }

    public String getDataSet() {
        return dataSet;
    }

    public void setDataSet(String dataSet) {
        this.dataSet = dataSet;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

}