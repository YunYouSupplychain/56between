package com.yunyou.modules.sys.common.entity;

import com.yunyou.core.persistence.DataEntity;

import javax.validation.constraints.NotNull;

/**
 * 数据套Entity
 */
public class SysCommonDataSet extends DataEntity<SysCommonDataSet> {

    private static final long serialVersionUID = 1L;
    @NotNull(message = "编码不能为空")
    private String code;// 编码
    @NotNull(message = "名称不能为空")
    private String name;// 名称
    private String isDefault;// 是否默认

    public SysCommonDataSet() {
        super();
    }

    public SysCommonDataSet(String id) {
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

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

}