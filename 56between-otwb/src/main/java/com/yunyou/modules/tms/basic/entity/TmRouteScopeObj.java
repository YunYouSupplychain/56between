package com.yunyou.modules.tms.basic.entity;

import com.yunyou.core.persistence.DataEntity;

/**
 * 路由范围对象Entity
 *
 * @author liujianhua
 * @version 2021-10-15
 */
public class TmRouteScopeObj extends DataEntity<TmRouteScopeObj> {

    private static final long serialVersionUID = 1L;
    private String routeScopeCode;// 路由范围编码
    private String transportObjCode;// 业务对象编码
    private String scopeType;// 范围类型
    private String orgId;// 组织中心ID

    public TmRouteScopeObj() {
        super();
    }

    public TmRouteScopeObj(String id) {
        super(id);
    }

    public TmRouteScopeObj(String routeScopeCode, String orgId) {
        this.routeScopeCode = routeScopeCode;
        this.orgId = orgId;
    }

    public TmRouteScopeObj(String routeScopeCode, String scopeType, String orgId) {
        this(routeScopeCode, orgId);
        this.scopeType = scopeType;
    }

    public String getRouteScopeCode() {
        return routeScopeCode;
    }

    public void setRouteScopeCode(String routeScopeCode) {
        this.routeScopeCode = routeScopeCode;
    }

    public String getTransportObjCode() {
        return transportObjCode;
    }

    public void setTransportObjCode(String transportObjCode) {
        this.transportObjCode = transportObjCode;
    }

    public String getScopeType() {
        return scopeType;
    }

    public void setScopeType(String scopeType) {
        this.scopeType = scopeType;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
}