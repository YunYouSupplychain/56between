package com.yunyou.modules.tms.basic.entity;

import com.yunyou.core.persistence.TreeEntity;

/**
 * 网点拓扑图Entity
 *
 * @author liujianhua
 * @version 2020-03-02
 */
public class TmOutletRelation extends TreeEntity<TmOutletRelation> {

    private static final long serialVersionUID = 1L;
    private String code;        // 网点编码
    private String orgId;       // 机构ID

    public TmOutletRelation() {
        super();
    }

    public TmOutletRelation(String id) {
        super(id);
    }

    public TmOutletRelation(String code, String orgId) {
        this.code = code;
        this.orgId = orgId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public TmOutletRelation getParent() {
        return parent;
    }

    @Override
    public void setParent(TmOutletRelation parent) {
        this.parent = parent;

    }

    @Override
    public String getParentId() {
        return parent != null && parent.getId() != null ? parent.getId() : "0";
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
}