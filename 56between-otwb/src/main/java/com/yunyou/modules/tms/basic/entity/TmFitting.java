package com.yunyou.modules.tms.basic.entity;

import com.yunyou.core.persistence.DataEntity;

/**
 * 配件信息Entity
 * @author liujianhua
 * @version 2020-02-20
 */
public class TmFitting extends DataEntity<TmFitting> {

	private static final long serialVersionUID = 1L;
	private String fittingCode;		// 配件编码
	private String fittingName;		// 配件名称
	private String fittingModel;    // 配件型号
	private Double price;			// 单价
	private String orgId;			// 机构ID
    private String def1;
    private String def2;
    private String def3;
    private String def4;
    private String def5;

	public TmFitting() {
		super();
	}

	public TmFitting(String id){
		super(id);
	}

	public TmFitting(String fittingCode, String orgId) {
		this.fittingCode = fittingCode;
		this.orgId = orgId;
	}

	public String getFittingCode() {
		return fittingCode;
	}

	public void setFittingCode(String fittingCode) {
		this.fittingCode = fittingCode;
	}

	public String getFittingName() {
		return fittingName;
	}

	public void setFittingName(String fittingName) {
		this.fittingName = fittingName;
	}

	public String getFittingModel() {
		return fittingModel;
	}

	public void setFittingModel(String fittingModel) {
		this.fittingModel = fittingModel;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

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