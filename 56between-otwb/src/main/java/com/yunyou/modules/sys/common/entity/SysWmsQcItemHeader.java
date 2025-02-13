package com.yunyou.modules.sys.common.entity;

import com.google.common.collect.Lists;
import com.yunyou.core.persistence.DataEntity;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 质检项Entity
 */
public class SysWmsQcItemHeader extends DataEntity<SysWmsQcItemHeader> {

    private static final long serialVersionUID = 1L;
    @NotNull(message = "质检项组编码不能为空")
    private String itemGroupCode;// 质检项组编码
    @NotNull(message = "质检项组名称不能为空")
    private String itemGroupName;// 质检项组名称
    @NotNull(message = "数据套不能为空")
    private String dataSet;// 数据套

    private List<SysWmsQcItemDetail> qcItemDetailList = Lists.newArrayList();
    private String dataSetName;// 数据套名称

    public SysWmsQcItemHeader() {
        super();
    }

    public SysWmsQcItemHeader(String id) {
        super(id);
    }

    public SysWmsQcItemHeader(String id, String dataSet) {
        super(id);
        this.dataSet = dataSet;
    }

    public String getItemGroupCode() {
        return itemGroupCode;
    }

    public void setItemGroupCode(String itemGroupCode) {
        this.itemGroupCode = itemGroupCode;
    }

    public String getItemGroupName() {
        return itemGroupName;
    }

    public void setItemGroupName(String itemGroupName) {
        this.itemGroupName = itemGroupName;
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

    public List<SysWmsQcItemDetail> getQcItemDetailList() {
        return qcItemDetailList;
    }

    public void setQcItemDetailList(List<SysWmsQcItemDetail> qcItemDetailList) {
        this.qcItemDetailList = qcItemDetailList;
    }
}