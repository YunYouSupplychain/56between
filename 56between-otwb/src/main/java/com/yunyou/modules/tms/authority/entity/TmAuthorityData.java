package com.yunyou.modules.tms.authority.entity;

import com.yunyou.modules.sys.OfficeType;

import java.io.Serializable;

public class TmAuthorityData implements Serializable {

    private static final long serialVersionUID = 1234145250056269809L;
    // 身份类型
    private OfficeType type;
    // 表名
    private String tableName;
    // 业务主键ID
    private String businessId;
    // 身份关联ID
    private String relationId;

    public TmAuthorityData() {
    }

    public TmAuthorityData(OfficeType officeType, String tableName) {
        this.type = officeType;
        this.tableName = tableName;
    }

    public TmAuthorityData(String tableName, String businessId) {
        this.tableName = tableName;
        this.businessId = businessId;
    }

    public TmAuthorityData(OfficeType officeType, String tableName, String businessId, String relationId) {
        this.type = officeType;
        this.tableName = tableName;
        this.businessId = businessId;
        this.relationId = relationId;
    }

    public OfficeType getType() {
        return type;
    }

    public void setType(OfficeType type) {
        this.type = type;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getRelationId() {
        return relationId;
    }

    public void setRelationId(String relationId) {
        this.relationId = relationId;
    }
}
