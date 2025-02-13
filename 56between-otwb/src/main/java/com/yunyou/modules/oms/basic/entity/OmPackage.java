package com.yunyou.modules.oms.basic.entity;

import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.core.persistence.DataEntity;

import java.util.List;

/**
 * 包装Entity
 * @author WMJ
 * @version 2019-04-19
 */
public class OmPackage extends DataEntity<OmPackage> {
	
	private static final long serialVersionUID = 1L;
	private String cdpaCode;		// 包装代码
	private String cdpaType;		// 包装类型
	private String cdpaFormat;		// 包装规格
	private String cdpaDesc;		// 描述
	private String cdpaIsUse;		// 可用标识,N为不可用,Y为可用,默认为Y
	private String cdpaFormatEn;		// 包装规格(英文)
	private String cdpaWhCode;		// 仓库
	private String timeZone;		// 时区
	private String orgId;		// 分公司
	private String pmCode;		// 代码(平台级别)
    private List<OmPackageRelation> packageDetailList;

    // 查询条件
	private String codeAndName;
	
	public OmPackage() {
		super();
	}

	public OmPackage(String id){
		super(id);
	}

	@ExcelField(title="包装代码", align=2, sort=1)
	public String getCdpaCode() {
		return cdpaCode;
	}

	public void setCdpaCode(String cdpaCode) {
		this.cdpaCode = cdpaCode;
	}
	
	@ExcelField(title="包装类型", align=2, sort=2)
	public String getCdpaType() {
		return cdpaType;
	}

	public void setCdpaType(String cdpaType) {
		this.cdpaType = cdpaType;
	}
	
	@ExcelField(title="包装规格", align=2, sort=3)
	public String getCdpaFormat() {
		return cdpaFormat;
	}

	public void setCdpaFormat(String cdpaFormat) {
		this.cdpaFormat = cdpaFormat;
	}
	
	@ExcelField(title="描述", align=2, sort=4)
	public String getCdpaDesc() {
		return cdpaDesc;
	}

	public void setCdpaDesc(String cdpaDesc) {
		this.cdpaDesc = cdpaDesc;
	}
	
	@ExcelField(title="可用标识,N为不可用,Y为可用,默认为Y", align=2, sort=5)
	public String getCdpaIsUse() {
		return cdpaIsUse;
	}

	public void setCdpaIsUse(String cdpaIsUse) {
		this.cdpaIsUse = cdpaIsUse;
	}
	
	@ExcelField(title="包装规格(英文)", align=2, sort=6)
	public String getCdpaFormatEn() {
		return cdpaFormatEn;
	}

	public void setCdpaFormatEn(String cdpaFormatEn) {
		this.cdpaFormatEn = cdpaFormatEn;
	}
	
	@ExcelField(title="仓库", align=2, sort=7)
	public String getCdpaWhCode() {
		return cdpaWhCode;
	}

	public void setCdpaWhCode(String cdpaWhCode) {
		this.cdpaWhCode = cdpaWhCode;
	}

	@ExcelField(title="时区", align=2, sort=14)
	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}
	
	@ExcelField(title="分公司", align=2, sort=15)
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
	@ExcelField(title="代码(平台级别)", align=2, sort=16)
	public String getPmCode() {
		return pmCode;
	}

	public void setPmCode(String pmCode) {
		this.pmCode = pmCode;
	}

    public List<OmPackageRelation> getPackageDetailList() {
        return packageDetailList;
    }

    public void setPackageDetailList(List<OmPackageRelation> packageDetailList) {
        this.packageDetailList = packageDetailList;
    }

	public String getCodeAndName() {
		return codeAndName;
	}

	public void setCodeAndName(String codeAndName) {
		this.codeAndName = codeAndName;
	}
}