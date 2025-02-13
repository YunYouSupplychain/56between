package com.yunyou.modules.sys.common.entity;

import com.google.common.collect.Lists;
import com.yunyou.core.persistence.DataEntity;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 包装Entity
 */
public class SysCommonPackage extends DataEntity<SysCommonPackage> {

    private static final long serialVersionUID = 1L;
    @NotNull(message = "包装代码不能为空")
    private String cdpaCode;// 包装代码
    @NotNull(message = "包装类型不能为空")
    private String cdpaType;// 包装类型
    @NotNull(message = "包装规格不能为空")
    private String cdpaFormat;// 包装规格
    private String cdpaDesc;// 描述
    private String cdpaFormatEn;// 包装规格(英文)
    private String cdpaIsUse;// 可用标识,N为不可用,Y为可用,默认为Y
    private String cdpaWhCode;// 仓库
    private String timeZone;// 时区
    private String pmCode;// 代码(平台级别)
    @NotNull(message = "数据套不能为空")
    private String dataSet;// 数据套

    // 扩展字段
    private String dataSetName;// 数据套名称
    private List<SysCommonPackageRelation> packageDetailList = Lists.newArrayList();
    private List<String> ids;

    public SysCommonPackage() {
        super();
    }

    public SysCommonPackage(String dataSet) {
        super();
        this.dataSet = dataSet;
    }

    public String getCdpaCode() {
        return cdpaCode;
    }

    public void setCdpaCode(String cdpaCode) {
        this.cdpaCode = cdpaCode;
    }

    public String getCdpaType() {
        return cdpaType;
    }

    public void setCdpaType(String cdpaType) {
        this.cdpaType = cdpaType;
    }

    public String getCdpaFormat() {
        return cdpaFormat;
    }

    public void setCdpaFormat(String cdpaFormat) {
        this.cdpaFormat = cdpaFormat;
    }

    public String getCdpaDesc() {
        return cdpaDesc;
    }

    public void setCdpaDesc(String cdpaDesc) {
        this.cdpaDesc = cdpaDesc;
    }

    public String getCdpaIsUse() {
        return cdpaIsUse;
    }

    public void setCdpaIsUse(String cdpaIsUse) {
        this.cdpaIsUse = cdpaIsUse;
    }

    public String getCdpaFormatEn() {
        return cdpaFormatEn;
    }

    public void setCdpaFormatEn(String cdpaFormatEn) {
        this.cdpaFormatEn = cdpaFormatEn;
    }

    public String getCdpaWhCode() {
        return cdpaWhCode;
    }

    public void setCdpaWhCode(String cdpaWhCode) {
        this.cdpaWhCode = cdpaWhCode;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getPmCode() {
        return pmCode;
    }

    public void setPmCode(String pmCode) {
        this.pmCode = pmCode;
    }

    public String getDataSet() {
        return dataSet;
    }

    public void setDataSet(String dataSet) {
        this.dataSet = dataSet;
    }

    public List<SysCommonPackageRelation> getPackageDetailList() {
        return packageDetailList;
    }

    public void setPackageDetailList(List<SysCommonPackageRelation> packageDetailList) {
        this.packageDetailList = packageDetailList;
    }

    public String getDataSetName() {
        return dataSetName;
    }

    public void setDataSetName(String dataSetName) {
        this.dataSetName = dataSetName;
    }

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }
}