package com.yunyou.modules.wms.basicdata.entity;

import com.yunyou.core.persistence.DataEntity;

/**
 * 商品分类
 */
public class CdWhSkuClassification extends DataEntity<CdWhSkuClassification> {

    private String code;
    private String name;
    private String orgId;

    public CdWhSkuClassification() {
    }

    public CdWhSkuClassification(String id) {
        super(id);
    }

    public CdWhSkuClassification(String code, String orgId) {
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
