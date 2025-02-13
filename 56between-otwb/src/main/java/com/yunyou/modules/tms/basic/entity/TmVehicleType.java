package com.yunyou.modules.tms.basic.entity;

import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.core.persistence.DataEntity;

/**
 * 车型信息
 * @author liujianhua
 * @version 2022-8-4
 */
public class TmVehicleType extends DataEntity<TmVehicleType> {

    /**
     * 编码
     */
    @ExcelField(title = "编码")
    private String code;
    /**
     * 名称
     */
    @ExcelField(title = "名称")
    private String name;
    /**
     * 机构ID
     */
    private String orgId;

    public TmVehicleType() {
    }

    public TmVehicleType(String id) {
        super(id);
    }

    public TmVehicleType(String code, String orgId) {
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
