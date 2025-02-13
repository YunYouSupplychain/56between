package com.yunyou.modules.sys.common.entity;

import com.google.common.collect.Lists;
import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.core.persistence.DataEntity;

import java.util.List;

/**
 * 包装Entity
 */
public class SysOmsPackage extends DataEntity<SysOmsPackage> {

    private static final long serialVersionUID = 1L;
    private String cdpaCode;        // 包装代码
    private String cdpaType;        // 包装类型
    private String cdpaFormat;        // 包装规格
    private String cdpaDesc;        // 描述
    private String cdpaIsUse;        // 可用标识,N为不可用,Y为可用,默认为Y
    private String cdpaFormatEn;        // 包装规格(英文)
    private String cdpaWhCode;        // 仓库
    private String timeZone;        // 时区
    private String dataSet;        // 数据套
    private String pmCode;        // 代码(平台级别)
    private List<SysOmsPackageRelation> packageDetailList = Lists.newArrayList();;

    public SysOmsPackage() {
        super();
    }

    public SysOmsPackage(String id) {
        super(id);
    }

    public SysOmsPackage(String id, String dataSet) {
        super(id);
        this.dataSet = dataSet;
    }

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

    @ExcelField(title = "时区", align = 2, sort = 14)
    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
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

    public List<SysOmsPackageRelation> getPackageDetailList() {
        return packageDetailList;
    }

    public void setPackageDetailList(List<SysOmsPackageRelation> packageDetailList) {
        this.packageDetailList = packageDetailList;
    }

    public String getCodeAndName() {
        return codeAndName;
    }

    public void setCodeAndName(String codeAndName) {
        this.codeAndName = codeAndName;
    }
}