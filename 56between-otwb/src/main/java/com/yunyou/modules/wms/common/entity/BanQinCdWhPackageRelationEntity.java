package com.yunyou.modules.wms.common.entity;

/**
 * 包装维护Entity
 * @author WMJ
 * @version 2019/03/01
 */
public class BanQinCdWhPackageRelationEntity {
	/**
	 * 包装代码
	 */
	private String cdpaCode;
	/**
	 * 单位
	 */
	private String cdprUnitLevel;
	/**
	 * 单位数量
	 */
	private Integer cdprQuantity;
	/**
	 * 是否主换算单位
	 */
	private String cdprIsMain;
	/**
	 * 描述
	 */
	private String cdprDesc;
	/**
	 * 装箱标识
	 */
	private String cdprIsPackBox;
	/**
	 * 入库标签
	 */
	private String cdprIsLableIn;
	/**
	 * 出库标签
	 */
	private String cdprIsLableOut;
	/**
	 * EA数量/换算数量=单位数量
	 */
	private Double qtyUom;

	public String getCdpaCode() {
		return cdpaCode;
	}

	public void setCdpaCode(String cdpaCode) {
		this.cdpaCode = cdpaCode;
	}

	public String getCdprUnitLevel() {
		return cdprUnitLevel;
	}

	public void setCdprUnitLevel(String cdprUnitLevel) {
		this.cdprUnitLevel = cdprUnitLevel;
	}

	public Integer getCdprQuantity() {
		return cdprQuantity;
	}

	public void setCdprQuantity(Integer cdprQuantity) {
		this.cdprQuantity = cdprQuantity;
	}

	public String getCdprIsMain() {
		return cdprIsMain;
	}

	public void setCdprIsMain(String cdprIsMain) {
		this.cdprIsMain = cdprIsMain;
	}

	public String getCdprDesc() {
		return cdprDesc;
	}

	public void setCdprDesc(String cdprDesc) {
		this.cdprDesc = cdprDesc;
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

	public Double getQtyUom() {
		return qtyUom;
	}

	public void setQtyUom(Double qtyUom) {
		this.qtyUom = qtyUom;
	}

}