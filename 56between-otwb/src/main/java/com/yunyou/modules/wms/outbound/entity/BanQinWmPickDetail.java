package com.yunyou.modules.wms.outbound.entity;

import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.core.persistence.DataEntity;

/**
 * 拣货单明细Entity
 * @author ZYF
 * @version 2020-05-13
 */
public class BanQinWmPickDetail extends DataEntity<BanQinWmPickDetail> {

	private static final long serialVersionUID = 1L;
	private String soNo;		// 出库单号
	private String allocId;		// 分配ID
	private String pickNo;		// 拣货单号
	private String orgId;		// 分公司
	private String def1;		// 自定义1
	private String def2;		// 自定义2
	private String def3;		// 自定义3
	private String def4;		// 自定义4
	private String def5;		// 自定义5

	public BanQinWmPickDetail() {
		super();
		this.recVer = 0;
	}

	public BanQinWmPickDetail(String id){
		super(id);
	}

	@ExcelField(title="出库单号", align=2, sort=2)
	public String getSoNo() {
		return soNo;
	}

	public void setSoNo(String soNo) {
		this.soNo = soNo;
	}

	public String getAllocId() {
		return allocId;
	}

	public void setAllocId(String allocId) {
		this.allocId = allocId;
	}

	public String getPickNo() {
		return pickNo;
	}

	public void setPickNo(String pickNo) {
		this.pickNo = pickNo;
	}

	@ExcelField(title="分公司", align=2, sort=12)
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getDef1() {
		return def1;
	}

	public void setDef1(String def1) {
		this.def1 = def1;
	}

	public String getDef2() {
		return def2;
	}

	public void setDef2(String def2) {
		this.def2 = def2;
	}

	public String getDef3() {
		return def3;
	}

	public void setDef3(String def3) {
		this.def3 = def3;
	}

	public String getDef4() {
		return def4;
	}

	public void setDef4(String def4) {
		this.def4 = def4;
	}

	public String getDef5() {
		return def5;
	}

	public void setDef5(String def5) {
		this.def5 = def5;
	}
}