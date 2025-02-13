package com.yunyou.modules.tms.basic.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.yunyou.core.persistence.DataEntity;
import com.yunyou.common.utils.excel.annotation.ExcelField;

/**
 * 车辆资质信息Entity
 *
 * @author liujianhua
 * @version 2020-02-20
 */
public class TmVehicleQualification extends DataEntity<TmVehicleQualification> {

    private static final long serialVersionUID = 1L;
    private String carNo;        // 车牌号
    private String qualificationCode;        // 资质编码
    private String qualificationNameCn;        // 中文名称
    private String qualificationNameEn;        // 英文名称
    private String qualificationShortName;        // 资质简称
    private Date effectiveDate;        // 生效日期
    private Date expireDate;        // 失效日期
    private String orgId;        // 机构ID

    public TmVehicleQualification() {
        super();
    }

    public TmVehicleQualification(String id) {
        super(id);
    }

    public TmVehicleQualification(String carNo, String orgId) {
        this.carNo = carNo;
        this.orgId = orgId;
    }

    public TmVehicleQualification(String carNo, String qualificationCode, String orgId) {
        this.carNo = carNo;
        this.qualificationCode = qualificationCode;
        this.orgId = orgId;
    }

    @ExcelField(title = "车牌号", align = 2, sort = 7)
    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    @ExcelField(title = "资质编码", align = 2, sort = 8)
    public String getQualificationCode() {
        return qualificationCode;
    }

    public void setQualificationCode(String qualificationCode) {
        this.qualificationCode = qualificationCode;
    }

    @ExcelField(title = "中文名称", align = 2, sort = 9)
    public String getQualificationNameCn() {
        return qualificationNameCn;
    }

    public void setQualificationNameCn(String qualificationNameCn) {
        this.qualificationNameCn = qualificationNameCn;
    }

    @ExcelField(title = "英文名称", align = 2, sort = 10)
    public String getQualificationNameEn() {
        return qualificationNameEn;
    }

    public void setQualificationNameEn(String qualificationNameEn) {
        this.qualificationNameEn = qualificationNameEn;
    }

    @ExcelField(title = "资质简称", align = 2, sort = 11)
    public String getQualificationShortName() {
        return qualificationShortName;
    }

    public void setQualificationShortName(String qualificationShortName) {
        this.qualificationShortName = qualificationShortName;
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    @ExcelField(title = "生效日期", align = 2, sort = 12)
    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    @ExcelField(title = "失效日期", align = 2, sort = 13)
    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    @ExcelField(title = "机构ID", align = 2, sort = 14)
    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
}