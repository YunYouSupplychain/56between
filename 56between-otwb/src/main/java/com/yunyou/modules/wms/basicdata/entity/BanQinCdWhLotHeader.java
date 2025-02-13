package com.yunyou.modules.wms.basicdata.entity;

import com.yunyou.core.persistence.DataEntity;
import com.yunyou.common.utils.excel.annotation.ExcelField;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 批次属性Entity
 * @author WMJ
 * @version 2019-01-25
 */
public class BanQinCdWhLotHeader extends DataEntity<BanQinCdWhLotHeader> {
	
	private static final long serialVersionUID = 1L;
	private String lotCode;		// 批次属性编码
	private String lotName;		// 批次属性名称
	private String remark;		// 备注
	private String orgId;		// 分公司
    private List<BanQinCdWhLotDetail> cdWhLotDetailList;
    private String lotCodeAndName; // 模糊查询字段
	
	public BanQinCdWhLotHeader() {
		super();
		this.recVer = 0;
	}

	public BanQinCdWhLotHeader(String id){
		super(id);
	}

	@NotNull(message="批次属性编码不能为空")
	@ExcelField(title="批次属性编码", align=2, sort=2)
	public String getLotCode() {
		return lotCode;
	}

	public void setLotCode(String lotCode) {
		this.lotCode = lotCode;
	}

	@NotNull(message="批次属性名称不能为空")
	@ExcelField(title="批次属性名称", align=2, sort=3)
	public String getLotName() {
		return lotName;
	}

	public void setLotName(String lotName) {
		this.lotName = lotName;
	}
	
	@ExcelField(title="备注", align=2, sort=4)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@ExcelField(title="分公司", align=2, sort=12)
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

    public List<BanQinCdWhLotDetail> getCdWhLotDetailList() {
        return cdWhLotDetailList;
    }

    public void setCdWhLotDetailList(List<BanQinCdWhLotDetail> cdWhLotDetailList) {
        this.cdWhLotDetailList = cdWhLotDetailList;
    }

	public String getLotCodeAndName() {
		return lotCodeAndName;
	}

	public void setLotCodeAndName(String lotCodeAndName) {
		this.lotCodeAndName = lotCodeAndName;
	}
}