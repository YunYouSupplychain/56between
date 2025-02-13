package com.yunyou.modules.wms.kit.entity;

import com.yunyou.core.persistence.DataEntity;
import com.yunyou.common.utils.excel.annotation.ExcelField;

/**
 * 组合件加工工序Entity
 * @author Jianhua Liu
 * @version 2019-08-19
 */
public class BanQinCdWhBomStep extends DataEntity<BanQinCdWhBomStep> {
	
	private static final long serialVersionUID = 1L;
	private String ownerCode;		// 货主编码
	private String parentSkuCode;	// 父件编码
	private String kitType;		// 加工类型
	private String lineNo;		// 行号
	private String step;		// 工序描述
	private String def1;		// 自定义1
	private String def2;		// 自定义2
	private String def3;		// 自定义3
	private String def4;		// 自定义4
	private String def5;		// 自定义5
	private String orgId;		// 机构ID
	private String headerId;	// 头Id

	public BanQinCdWhBomStep() {
		super();
		this.recVer = 0;
	}

	public BanQinCdWhBomStep(String id){
		super(id);
	}

	@ExcelField(title="货主编码", align=2, sort=2)
	public String getOwnerCode() {
		return ownerCode;
	}

	public void setOwnerCode(String ownerCode) {
		this.ownerCode = ownerCode;
	}
	
	@ExcelField(title="父件编码", align=2, sort=3)
	public String getParentSkuCode() {
		return parentSkuCode;
	}

	public void setParentSkuCode(String parentSkuCode) {
		this.parentSkuCode = parentSkuCode;
	}
	
	@ExcelField(title="加工类型", align=2, sort=4)
	public String getKitType() {
		return kitType;
	}

	public void setKitType(String kitType) {
		this.kitType = kitType;
	}
	
	@ExcelField(title="行号", align=2, sort=5)
	public String getLineNo() {
		return lineNo;
	}

	public void setLineNo(String lineNo) {
		this.lineNo = lineNo;
	}
	
	@ExcelField(title="工序描述", align=2, sort=6)
	public String getStep() {
		return step;
	}

	public void setStep(String step) {
		this.step = step;
	}
	
	@ExcelField(title="自定义1", align=2, sort=7)
	public String getDef1() {
		return def1;
	}

	public void setDef1(String def1) {
		this.def1 = def1;
	}
	
	@ExcelField(title="自定义2", align=2, sort=8)
	public String getDef2() {
		return def2;
	}

	public void setDef2(String def2) {
		this.def2 = def2;
	}
	
	@ExcelField(title="自定义3", align=2, sort=9)
	public String getDef3() {
		return def3;
	}

	public void setDef3(String def3) {
		this.def3 = def3;
	}
	
	@ExcelField(title="自定义4", align=2, sort=10)
	public String getDef4() {
		return def4;
	}

	public void setDef4(String def4) {
		this.def4 = def4;
	}
	
	@ExcelField(title="自定义5", align=2, sort=11)
	public String getDef5() {
		return def5;
	}

	public void setDef5(String def5) {
		this.def5 = def5;
	}

	@ExcelField(title="机构ID", align=2, sort=20)
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getHeaderId() {
		return headerId;
	}

	public void setHeaderId(String headerId) {
		this.headerId = headerId;
	}
}