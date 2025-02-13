package com.yunyou.modules.oms.basic.entity;

import com.yunyou.core.persistence.DataEntity;

/**
 * 商品分类
 */
public class OmSkuClassification extends DataEntity<OmSkuClassification> {

    private String code;
    private String name;
    private String orgId;

    public OmSkuClassification() {
    }

    public OmSkuClassification(String id) {
        super(id);
    }

    public OmSkuClassification(String code, String orgId) {
        this.code = code;
        this.orgId = orgId;
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
