package com.yunyou.modules.sys.common.entity;

import com.yunyou.core.persistence.DataEntity;

import javax.validation.constraints.NotNull;

/**
 * 区域Entity
 */
public class SysCommonRegionPlace extends DataEntity<SysCommonRegionPlace> {

    private static final long serialVersionUID = 1L;
    @NotNull(message = "区域编码不能为空")
    private String code;// 区域编码
    @NotNull(message = "区域名称不能为空")
    private String placeId;// 地点ID
    @NotNull(message = "数据套不能为空")
    private String dataSet;// 数据套

    private String dataSetName;// 数据套名称

    public SysCommonRegionPlace() {
        super();
    }

    public SysCommonRegionPlace(String id) {
        super(id);
    }

    public SysCommonRegionPlace(String id, String dataSet) {
        super(id);
        this.dataSet = dataSet;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getDataSet() {
        return dataSet;
    }

    public void setDataSet(String dataSet) {
        this.dataSet = dataSet;
    }

    public String getDataSetName() {
        return dataSetName;
    }

    public void setDataSetName(String dataSetName) {
        this.dataSetName = dataSetName;
    }
}