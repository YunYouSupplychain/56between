package com.yunyou.modules.sys.common.entity;

import com.yunyou.core.persistence.DataEntity;

/**
 * 包装明细Entity
 */
public class SysCommonPackageRelation extends DataEntity<SysCommonPackageRelation> {

    private static final long serialVersionUID = 1L;
    private String cdprUnit;// 单位
    private String cdprUnitRate;// 换算比例
    private String cdprIsMain;// 是否主换算单位
    private String cdprSequencesNo;// 序号
    private String cdprUnitLevel;// 单位级别
    private Double cdprQuantity;// 数量
    private String cdprDesc;// 包装描述
    private String cdprMaterial;// 包装材料
    private String cdprIsPackBox;// 装箱标识
    private String cdprIsLableIn;// 入库标签
    private String cdprIsLableOut;// 出库标签
    private Double cdprLength;// 长度
    private Double cdprWidth;// 宽度
    private Double cdprHeight;// 高度
    private Double cdprVolume;// 体积
    private Double cdprWeight;// 重量
    private String cdprWhCode;// 仓库
    private String timeZone;// 时区
    private Double cdprTi;// 托盘每层可堆放箱数
    private Double cdprHi;// 托盘可堆放层数
    private String pmCode;// 代码
    private String cdprCdpaPmCode;// 包装代码(平台级别)
    private String cdprPaId;// 包装外键
    private String cdprIsDefault;// 是否默认
    private String cdprIsReview;// 是否需要复核
    private String packCode;// 包装代码用作查询
    private String dataSet;// 数据套

    public SysCommonPackageRelation() {
        super();
        this.cdprLength = 0.00d;
        this.cdprWidth = 0.00d;
        this.cdprHeight = 0.00d;
        this.cdprVolume = 0.00d;
        this.cdprWeight = 0.00d;
        this.cdprTi = 0d;
        this.cdprHi = 0d;
    }

    public SysCommonPackageRelation(String id) {
        super(id);
    }

    public String getCdprUnit() {
        return cdprUnit;
    }

    public void setCdprUnit(String cdprUnit) {
        this.cdprUnit = cdprUnit;
    }

    public String getCdprUnitRate() {
        return cdprUnitRate;
    }

    public void setCdprUnitRate(String cdprUnitRate) {
        this.cdprUnitRate = cdprUnitRate;
    }

    public String getCdprIsMain() {
        return cdprIsMain;
    }

    public void setCdprIsMain(String cdprIsMain) {
        this.cdprIsMain = cdprIsMain;
    }

    public String getCdprSequencesNo() {
        return cdprSequencesNo;
    }

    public void setCdprSequencesNo(String cdprSequencesNo) {
        this.cdprSequencesNo = cdprSequencesNo;
    }

    public String getCdprUnitLevel() {
        return cdprUnitLevel;
    }

    public void setCdprUnitLevel(String cdprUnitLevel) {
        this.cdprUnitLevel = cdprUnitLevel;
    }

    public Double getCdprQuantity() {
        return cdprQuantity;
    }

    public void setCdprQuantity(Double cdprQuantity) {
        this.cdprQuantity = cdprQuantity;
    }

    public String getCdprDesc() {
        return cdprDesc;
    }

    public void setCdprDesc(String cdprDesc) {
        this.cdprDesc = cdprDesc;
    }

    public String getCdprMaterial() {
        return cdprMaterial;
    }

    public void setCdprMaterial(String cdprMaterial) {
        this.cdprMaterial = cdprMaterial;
    }

    public String getCdprIsPackBox() {
        return cdprIsPackBox;
    }

    public void setCdprIsPackBox(String cdprIsPackBox) {
        this.cdprIsPackBox = cdprIsPackBox;
    }

    public String getCdprIsLableIn() {
        return cdprIsLableIn;
    }

    public void setCdprIsLableIn(String cdprIsLableIn) {
        this.cdprIsLableIn = cdprIsLableIn;
    }

    public String getCdprIsLableOut() {
        return cdprIsLableOut;
    }

    public void setCdprIsLableOut(String cdprIsLableOut) {
        this.cdprIsLableOut = cdprIsLableOut;
    }

    public Double getCdprLength() {
        return cdprLength;
    }

    public void setCdprLength(Double cdprLength) {
        this.cdprLength = cdprLength;
    }

    public Double getCdprWidth() {
        return cdprWidth;
    }

    public void setCdprWidth(Double cdprWidth) {
        this.cdprWidth = cdprWidth;
    }

    public Double getCdprHeight() {
        return cdprHeight;
    }

    public void setCdprHeight(Double cdprHeight) {
        this.cdprHeight = cdprHeight;
    }

    public Double getCdprVolume() {
        return cdprVolume;
    }

    public void setCdprVolume(Double cdprVolume) {
        this.cdprVolume = cdprVolume;
    }

    public Double getCdprWeight() {
        return cdprWeight;
    }

    public void setCdprWeight(Double cdprWeight) {
        this.cdprWeight = cdprWeight;
    }

    public String getCdprWhCode() {
        return cdprWhCode;
    }

    public void setCdprWhCode(String cdprWhCode) {
        this.cdprWhCode = cdprWhCode;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public Double getCdprTi() {
        return cdprTi;
    }

    public void setCdprTi(Double cdprTi) {
        this.cdprTi = cdprTi;
    }

    public Double getCdprHi() {
        return cdprHi;
    }

    public void setCdprHi(Double cdprHi) {
        this.cdprHi = cdprHi;
    }

    public String getPmCode() {
        return pmCode;
    }

    public void setPmCode(String pmCode) {
        this.pmCode = pmCode;
    }

    public String getCdprCdpaPmCode() {
        return cdprCdpaPmCode;
    }

    public void setCdprCdpaPmCode(String cdprCdpaPmCode) {
        this.cdprCdpaPmCode = cdprCdpaPmCode;
    }

    public String getCdprPaId() {
        return cdprPaId;
    }

    public void setCdprPaId(String cdprPaId) {
        this.cdprPaId = cdprPaId;
    }

    public String getCdprIsDefault() {
        return cdprIsDefault;
    }

    public void setCdprIsDefault(String cdprIsDefault) {
        this.cdprIsDefault = cdprIsDefault;
    }

    public String getCdprIsReview() {
        return cdprIsReview;
    }

    public void setCdprIsReview(String cdprIsReview) {
        this.cdprIsReview = cdprIsReview;
    }

    public String getPackCode() {
        return packCode;
    }

    public void setPackCode(String packCode) {
        this.packCode = packCode;
    }

    public String getDataSet() {
        return dataSet;
    }

    public void setDataSet(String dataSet) {
        this.dataSet = dataSet;
    }
}