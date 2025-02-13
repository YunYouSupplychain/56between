package com.yunyou.modules.sys.common.entity;

import com.google.common.collect.Lists;
import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.core.persistence.DataEntity;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 包装Entity
 */
public class SysWmsPackage extends DataEntity<SysWmsPackage> {

    private static final long serialVersionUID = 1L;
    private String cdpaCode;// 包装代码
    private String cdpaType;// 包装类型
    private String cdpaFormat;// 包装规格
    private String cdpaDesc;// 描述
    private String cdpaIsUse;// 可用标识,N为不可用,Y为可用,默认为Y
    private String cdpaFormatEn;// 包装规格(英文)
    private String cdpaWhCode;// 仓库
    private String dataSet;// 数据套
    private String pmCode;// 代码(平台级别)
    private List<SysWmsPackageRelation> packageDetailList = Lists.newArrayList();
    private String unitCode;// 包装单位编码
    private String unitDesc;// 包装单位名称

    public SysWmsPackage() {
        super();
    }

    public SysWmsPackage(String id) {
        super(id);
    }

    public SysWmsPackage(String id, String dataSet) {
        super(id);
        this.dataSet = dataSet;
    }

    @NotNull(message = "包装代码不能为空")
    @ExcelField(title = "包装代码", align = 2, sort = 1)
    public String getCdpaCode() {
        return cdpaCode;
    }

    public void setCdpaCode(String cdpaCode) {
        this.cdpaCode = cdpaCode;
    }

    @ExcelField(title = "包装类型", align = 2, sort = 2)
    public String getCdpaType() {
        return cdpaType;
    }

    public void setCdpaType(String cdpaType) {
        this.cdpaType = cdpaType;
    }

    @NotNull(message = "包装规格不能为空")
    @ExcelField(title = "包装规格", align = 2, sort = 3)
    public String getCdpaFormat() {
        return cdpaFormat;
    }

    public void setCdpaFormat(String cdpaFormat) {
        this.cdpaFormat = cdpaFormat;
    }

    @ExcelField(title = "描述", align = 2, sort = 4)
    public String getCdpaDesc() {
        return cdpaDesc;
    }

    public void setCdpaDesc(String cdpaDesc) {
        this.cdpaDesc = cdpaDesc;
    }

    @ExcelField(title = "可用标识,N为不可用,Y为可用,默认为Y", align = 2, sort = 5)
    public String getCdpaIsUse() {
        return cdpaIsUse;
    }

    public void setCdpaIsUse(String cdpaIsUse) {
        this.cdpaIsUse = cdpaIsUse;
    }

    @ExcelField(title = "包装规格(英文)", align = 2, sort = 6)
    public String getCdpaFormatEn() {
        return cdpaFormatEn;
    }

    public void setCdpaFormatEn(String cdpaFormatEn) {
        this.cdpaFormatEn = cdpaFormatEn;
    }

    @ExcelField(title = "仓库", align = 2, sort = 7)
    public String getCdpaWhCode() {
        return cdpaWhCode;
    }

    public void setCdpaWhCode(String cdpaWhCode) {
        this.cdpaWhCode = cdpaWhCode;
    }

    public String getDataSet() {
        return dataSet;
    }

    public void setDataSet(String dataSet) {
        this.dataSet = dataSet;
    }

    @ExcelField(title = "代码(平台级别)", align = 2, sort = 16)
    public String getPmCode() {
        return pmCode;
    }

    public void setPmCode(String pmCode) {
        this.pmCode = pmCode;
    }

    public List<SysWmsPackageRelation> getPackageDetailList() {
        return packageDetailList;
    }

    public void setPackageDetailList(List<SysWmsPackageRelation> packageDetailList) {
        this.packageDetailList = packageDetailList;
    }

    public String getCodeAndName() {
        return codeAndName;
    }

    public void setCodeAndName(String codeAndName) {
        this.codeAndName = codeAndName;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public String getUnitDesc() {
        return unitDesc;
    }

    public void setUnitDesc(String unitDesc) {
        this.unitDesc = unitDesc;
    }
}