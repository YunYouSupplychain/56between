package com.yunyou.modules.wms.kit.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.core.persistence.DataEntity;

import java.util.Date;

/**
 * 加工工序Entity
 * @author Jianhua Liu
 * @version 2019-08-20
 */
public class BanQinWmKitStep extends DataEntity<BanQinWmKitStep> {
	
	private static final long serialVersionUID = 1L;
	private String kitNo;		// 加工单号
	private String kitLineNo;		// 加工明细行号
	private String stepLineNo;		// 加工工序行号
	private String ownerCode;		// 货主编码
	private String parentSkuCode;		// 父件编码
	private String step;		// 工序描述
	private String packCode;		// 包装编码
	private String uom;		// 包装单位
	private Double qtyKit = 0D;		// 加工数量
	private Double qtyKitEa = 0D;		// 加工数量EA
	private String kitOp;		// 加工人
	private Date kitTime;		// 加工时间
	private String def1;		// 自定义1
	private String def2;		// 自定义2
	private String def3;		// 自定义3
	private String def4;		// 自定义4
	private String def5;		// 自定义5
	private String orgId;		// 机构ID
	private String headerId;		// 头Id

	public BanQinWmKitStep() {
		super();
		this.recVer = 0;
	}

	public BanQinWmKitStep(String id){
		super(id);
	}

	@ExcelField(title="加工单号", align=2, sort=2)
	public String getKitNo() {
		return kitNo;
	}

	public void setKitNo(String kitNo) {
		this.kitNo = kitNo;
	}
	
	@ExcelField(title="加工明细行号", align=2, sort=3)
	public String getKitLineNo() {
		return kitLineNo;
	}

	public void setKitLineNo(String kitLineNo) {
		this.kitLineNo = kitLineNo;
	}
	
	@ExcelField(title="加工工序行号", align=2, sort=4)
	public String getStepLineNo() {
		return stepLineNo;
	}

	public void setStepLineNo(String stepLineNo) {
		this.stepLineNo = stepLineNo;
	}
	
	@ExcelField(title="货主编码", align=2, sort=5)
	public String getOwnerCode() {
		return ownerCode;
	}

	public void setOwnerCode(String ownerCode) {
		this.ownerCode = ownerCode;
	}
	
	@ExcelField(title="父件编码", align=2, sort=6)
	public String getParentSkuCode() {
		return parentSkuCode;
	}

	public void setParentSkuCode(String parentSkuCode) {
		this.parentSkuCode = parentSkuCode;
	}
	
	@ExcelField(title="工序描述", align=2, sort=7)
	public String getStep() {
		return step;
	}

	public void setStep(String step) {
		this.step = step;
	}
	
	@ExcelField(title="包装编码", align=2, sort=8)
	public String getPackCode() {
		return packCode;
	}

	public void setPackCode(String packCode) {
		this.packCode = packCode;
	}
	
	@ExcelField(title="包装单位", align=2, sort=9)
	public String getUom() {
		return uom;
	}

	public void setUom(String uom) {
		this.uom = uom;
	}
	
	@ExcelField(title="加工数量", align=2, sort=10)
	public Double getQtyKit() {
		return qtyKit;
	}

	public void setQtyKit(Double qtyKit) {
		this.qtyKit = qtyKit;
	}
	
	@ExcelField(title="加工数量EA", align=2, sort=11)
	public Double getQtyKitEa() {
		return qtyKitEa;
	}

	public void setQtyKitEa(Double qtyKitEa) {
		this.qtyKitEa = qtyKitEa;
	}
	
	@ExcelField(title="加工人", align=2, sort=12)
	public String getKitOp() {
		return kitOp;
	}

	public void setKitOp(String kitOp) {
		this.kitOp = kitOp;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="加工时间", align=2, sort=13)
	public Date getKitTime() {
		return kitTime;
	}

	public void setKitTime(Date kitTime) {
		this.kitTime = kitTime;
	}
	
	@ExcelField(title="自定义1", align=2, sort=14)
	public String getDef1() {
		return def1;
	}

	public void setDef1(String def1) {
		this.def1 = def1;
	}
	
	@ExcelField(title="自定义2", align=2, sort=15)
	public String getDef2() {
		return def2;
	}

	public void setDef2(String def2) {
		this.def2 = def2;
	}
	
	@ExcelField(title="自定义3", align=2, sort=16)
	public String getDef3() {
		return def3;
	}

	public void setDef3(String def3) {
		this.def3 = def3;
	}
	
	@ExcelField(title="自定义4", align=2, sort=17)
	public String getDef4() {
		return def4;
	}

	public void setDef4(String def4) {
		this.def4 = def4;
	}
	
	@ExcelField(title="自定义5", align=2, sort=18)
	public String getDef5() {
		return def5;
	}

	public void setDef5(String def5) {
		this.def5 = def5;
	}

	@ExcelField(title="机构ID", align=2, sort=27)
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