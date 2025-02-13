package com.yunyou.modules.wms.basicdata.entity;

import com.yunyou.core.persistence.DataEntity;
import com.yunyou.common.utils.excel.annotation.ExcelField;

import javax.validation.constraints.NotNull;

/**
 * 批次属性Entity
 * @author WMJ
 * @version 2019-01-25
 */
public class BanQinCdWhLotDetail extends DataEntity<BanQinCdWhLotDetail> {
	
	private static final long serialVersionUID = 1L;
	private String lotCode;		// 批次属性编码
	private String lotAtt;		// 批次属性
	private String title;		// 批次标签
	private String foreignTitle;		// 批次标签（外语）
	private String inputControl;		// 输入控制（R必输、O可选、F禁用）
	private String fieldType;		// 字段显示类型（字符串、数字、日期、日期时间、弹出框、下拉框）
	private String key;		// 属性选项（Key值，适用于弹出框和下拉框）
	private String orgId;		// 分公司
	private String headerId;		// 头表Id
	private String codeAndName; // 模糊查询字段

	public BanQinCdWhLotDetail() {
		super();
		this.recVer = 0;
	}

	public BanQinCdWhLotDetail(String id){
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

	@NotNull(message="批次属性不能为空")
	@ExcelField(title="批次属性", align=2, sort=3)
	public String getLotAtt() {
		return lotAtt;
	}

	public void setLotAtt(String lotAtt) {
		this.lotAtt = lotAtt;
	}

	@NotNull(message="批次标签不能为空")
	@ExcelField(title="批次标签", align=2, sort=4)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@ExcelField(title="批次标签（外语）", align=2, sort=5)
	public String getForeignTitle() {
		return foreignTitle;
	}

	public void setForeignTitle(String foreignTitle) {
		this.foreignTitle = foreignTitle;
	}

	@NotNull(message="输入控制不能为空")
	@ExcelField(title="输入控制", align=2, sort=6)
	public String getInputControl() {
		return inputControl;
	}

	public void setInputControl(String inputControl) {
		this.inputControl = inputControl;
	}
	
	@ExcelField(title="字段显示类型（字符串、数字、日期、日期时间、弹出框、下拉框）", align=2, sort=7)
	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}
	
	@ExcelField(title="属性选项（Key值，适用于弹出框和下拉框）", align=2, sort=8)
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@ExcelField(title="分公司", align=2, sort=16)
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
	@ExcelField(title="头表Id", align=2, sort=17)
	public String getHeaderId() {
		return headerId;
	}

	public void setHeaderId(String headerId) {
		this.headerId = headerId;
	}

	public String getCodeAndName() {
		return codeAndName;
	}

	public void setCodeAndName(String codeAndName) {
		this.codeAndName = codeAndName;
	}
}