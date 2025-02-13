package com.yunyou.modules.sys.common.entity.extend;

import com.yunyou.modules.sys.common.entity.SysDataSetOrgRelation;

import java.util.List;

public class SysDataSetOrgRelationEntity extends SysDataSetOrgRelation {

    private String dataSetName;// 数据套名称
    private String orgCode;// 机构编码
    private String orgName;// 机构名称
    private List<String> orgIds;

    public SysDataSetOrgRelationEntity() {
    }

    public SysDataSetOrgRelationEntity(String id, String dataSet, String orgId) {
        super(id, dataSet, orgId);
    }

    public String getDataSetName() {
        return dataSetName;
    }

    public void setDataSetName(String dataSetName) {
        this.dataSetName = dataSetName;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public List<String> getOrgIds() {
        return orgIds;
    }

    public void setOrgIds(List<String> orgIds) {
        this.orgIds = orgIds;
    }
}
