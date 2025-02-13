package com.yunyou.modules.sys.common.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.yunyou.core.persistence.DataEntity;
import com.yunyou.common.utils.excel.annotation.ExcelField;

/**
 * 运输人员资质信息Entity
 */
public class SysTmsDriverQualification extends DataEntity<SysTmsDriverQualification> {
	
	private static final long serialVersionUID = 1L;
	private String driverCode;// 运输人员编码
	private String qualificationCode;// 资质编码
	private String qualificationNameCn;// 中文名称
	private String qualificationNameEn;// 英文名称
	private String qualificationShortName;// 简称
	private Date effectiveDate;// 生效日期
	private Date expireDate;// 失效日期
	private String dataSet;// 数据套

	public SysTmsDriverQualification() {
		super();
	}

	public SysTmsDriverQualification(String id){
		super(id);
	}

	public SysTmsDriverQualification(String driverCode, String dataSet) {
		this.driverCode = driverCode;
		this.dataSet = dataSet;
	}

	public SysTmsDriverQualification(String driverCode, String qualificationCode, String dataSet) {
		this.driverCode = driverCode;
		this.qualificationCode = qualificationCode;
		this.dataSet = dataSet;
	}

	@ExcelField(title="运输人员编码", align=2, sort=7)
	public String getDriverCode() {
		return driverCode;
	}

	public void setDriverCode(String driverCode) {
		this.driverCode = driverCode;
	}
	
	@ExcelField(title="资质编码", align=2, sort=8)
	public String getQualificationCode() {
		return qualificationCode;
	}

	public void setQualificationCode(String qualificationCode) {
		this.qualificationCode = qualificationCode;
	}
	
	@ExcelField(title="中文名称", align=2, sort=9)
	public String getQualificationNameCn() {
		return qualificationNameCn;
	}

	public void setQualificationNameCn(String qualificationNameCn) {
		this.qualificationNameCn = qualificationNameCn;
	}
	
	@ExcelField(title="英文名称", align=2, sort=10)
	public String getQualificationNameEn() {
		return qualificationNameEn;
	}

	public void setQualificationNameEn(String qualificationNameEn) {
		this.qualificationNameEn = qualificationNameEn;
	}
	
	@ExcelField(title="简称", align=2, sort=11)
	public String getQualificationShortName() {
		return qualificationShortName;
	}

	public void setQualificationShortName(String qualificationShortName) {
		this.qualificationShortName = qualificationShortName;
	}

	@JsonFormat(pattern = "yyyy-MM-dd")
	@ExcelField(title="生效日期", align=2, sort=12)
	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	@JsonFormat(pattern = "yyyy-MM-dd")
	@ExcelField(title="失效日期", align=2, sort=13)
	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	public String getDataSet() {
		return dataSet;
	}

	public void setDataSet(String dataSet) {
		this.dataSet = dataSet;
	}
}