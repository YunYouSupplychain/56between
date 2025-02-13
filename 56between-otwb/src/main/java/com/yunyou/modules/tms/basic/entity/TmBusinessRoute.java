package com.yunyou.modules.tms.basic.entity;

import com.yunyou.core.persistence.DataEntity;

public class TmBusinessRoute extends DataEntity<TmBusinessRoute> {

    private static final long serialVersionUID = -9168407398587249646L;
    private String code;
    private String name;
    private String orgId;

    public TmBusinessRoute() {
    }

    public TmBusinessRoute(String id) {
        super(id);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

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
