package com.yunyou.modules.wms.basicdata.entity;

import com.yunyou.core.persistence.DataEntity;
import com.yunyou.common.utils.excel.annotation.ExcelField;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 包装Entity
 * @author WMJ
 * @version 2019-01-25
 */
public class BanQinCdWhPackage extends DataEntity<BanQinCdWhPackage> {
	
	private static final long serialVersionUID = 1L;
	private String cdpaCode;		// 包装代码
	private String cdpaType;		// 包装类型
	private String cdpaFormat;		// 包装规格
	private String cdpaDesc;		// 描述
	private String cdpaIsUse;		// 可用标识,N为不可用,Y为可用,默认为Y
	private String cdpaFormatEn;		// 包装规格(英文)
	private String cdpaWhCode;		// 仓库
	private String orgId;		// 分公司
	private String pmCode;		// 代码(平台级别)
    private List<BanQinCdWhPackageRelation> packageDetailList;
    private String codeAndName; // 模糊查询字段
	private String unitCode;	// 包装单位编码
	private String unitDesc;	// 包装单位名称
	
	public BanQinCdWhPackage() {
		super();
		this.recVer = 0;
	}

	public BanQinCdWhPackage(String id){
		super(id);
	}

	@NotNull(message="包装代码不能为空")
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

	@NotNull(message="包装规格不能为空")
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

    public List<BanQinCdWhPackageRelation> getPackageDetailList() {
        return packageDetailList;
    }

    public void setPackageDetailList(List<BanQinCdWhPackageRelation> packageDetailList) {
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