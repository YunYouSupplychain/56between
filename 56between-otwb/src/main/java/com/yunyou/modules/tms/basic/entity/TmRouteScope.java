package com.yunyou.modules.tms.basic.entity;

import com.yunyou.core.persistence.DataEntity;

/**
 * 路由范围Entity
 *
 * @author liujianhua
 * @version 2021-10-15
 */
public class TmRouteScope extends DataEntity<TmRouteScope> {

    private static final long serialVersionUID = 1L;
    private String code;// 编码
    private String name;// 名称
    private String carrierCode;// 承运商编码
    private String orgId;// 组织中心ID

    public TmRouteScope() {
        super();
    }

    public TmRouteScope(String id) {
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

    public String getCarrierCode() {
        return carrierCode;
    }

    public void setCarrierCode(String carrierCode) {
        this.carrierCode = carrierCode;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
}