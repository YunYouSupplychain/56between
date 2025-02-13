package com.yunyou.modules.sys.common.entity;

import com.google.common.collect.Lists;
import com.yunyou.core.persistence.DataEntity;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 批次属性Entity
 */
public class SysWmsLotHeader extends DataEntity<SysWmsLotHeader> {

    private static final long serialVersionUID = 1L;
    @NotNull(message = "批次属性编码不能为空")
    private String lotCode;// 批次属性编码
    @NotNull(message = "批次属性名称不能为空")
    private String lotName;// 批次属性名称
    @NotNull(message = "数据套不能为空")
    private String dataSet;// 数据套
    private List<SysWmsLotDetail> cdWhLotDetailList = Lists.newArrayList();

    private String dataSetName;// 数据套名称

    public SysWmsLotHeader() {
        super();
    }

    public SysWmsLotHeader(String id) {
        super(id);
    }

    public SysWmsLotHeader(String id, String dataSet) {
        super(id);
        this.dataSet = dataSet;
    }

    public String getLotCode() {
        return lotCode;
    }

    public void setLotCode(String lotCode) {
        this.lotCode = lotCode;
    }

    public String getLotName() {
        return lotName;
    }

    public void setLotName(String lotName) {
        this.lotName = lotName;
    }

    public String getDataSet() {
        return dataSet;
    }

    public void setDataSet(String dataSet) {
        this.dataSet = dataSet;
    }

    public List<SysWmsLotDetail> getCdWhLotDetailList() {
        return cdWhLotDetailList;
    }

    public void setCdWhLotDetailList(List<SysWmsLotDetail> cdWhLotDetailList) {
        this.cdWhLotDetailList = cdWhLotDetailList;
    }

    public String getDataSetName() {
        return dataSetName;
    }

    public void setDataSetName(String dataSetName) {
        this.dataSetName = dataSetName;
    }
}