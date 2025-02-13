package com.yunyou.modules.wms.basicdata.entity;

import com.yunyou.core.persistence.DataEntity;
import com.yunyou.common.utils.excel.annotation.ExcelField;

/**
 * 质检项Entity
 * @author WMJ
 * @version 2019-01-25
 */
public class BanQinCdWhQcItemDetail extends DataEntity<BanQinCdWhQcItemDetail> {
	
	private static final long serialVersionUID = 1L;
	private String itemGroupCode;		// 质检项组编码
	private String lineNo;		// 行号
	private String qcItem;		// 质检项名称
	private String qcRef;		// 质检参考标准
	private String qcWay;		// 质检方法
	private String def1;		// 自定义1
	private String def2;		// 自定义2
	private String def3;		// 自定义3
	private String def4;		// 自定义4
	private String def5;		// 自定义5
	private String orgId;		// 分公司
	private String headerId;		// 头表Id
	
	public BanQinCdWhQcItemDetail() {
		super();
		this.recVer = 0;
	}

	public BanQinCdWhQcItemDetail(String id){
		super(id);
	}

	@ExcelField(title="质检项组编码", align=2, sort=2)
	public String getItemGroupCode() {
		return itemGroupCode;
	}

	public void setItemGroupCode(String itemGroupCode) {
		this.itemGroupCode = itemGroupCode;
	}
	
	@ExcelField(title="行号", align=2, sort=3)
	public String getLineNo() {
		return lineNo;
	}

	public void setLineNo(String lineNo) {
		this.lineNo = lineNo;
	}
	
	@ExcelField(title="质检项名称", align=2, sort=4)
	public String getQcItem() {
		return qcItem;
	}

	public void setQcItem(String qcItem) {
		this.qcItem = qcItem;
	}
	
	@ExcelField(title="质检参考标准", align=2, sort=5)
	public String getQcRef() {
		return qcRef;
	}

	public void setQcRef(String qcRef) {
		this.qcRef = qcRef;
	}
	
	@ExcelField(title="质检方法", align=2, sort=6)
	public String getQcWay() {
		return qcWay;
	}

	public void setQcWay(String qcWay) {
		this.qcWay = qcWay;
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

	@ExcelField(title="分公司", align=2, sort=20)
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
	@ExcelField(title="头表Id", align=2, sort=21)
	public String getHeaderId() {
		return headerId;
	}

	public void setHeaderId(String headerId) {
		this.headerId = headerId;
	}
	
}