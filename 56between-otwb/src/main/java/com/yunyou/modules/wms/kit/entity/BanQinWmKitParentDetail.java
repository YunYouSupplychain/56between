package com.yunyou.modules.wms.kit.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.core.persistence.DataEntity;

import java.util.Date;

/**
 * 加工单父件明细Entity
 * @author Jianhua Liu
 * @version 2019-08-20
 */
public class BanQinWmKitParentDetail extends DataEntity<BanQinWmKitParentDetail> {
	
	private static final long serialVersionUID = 1L;
	private String kitNo;		// 加工单号
	private String parentLineNo;		// 父件明细行号
	private String status;		// 状态
	private String logisticNo;		// 物流单号
	private String logisticLineNo;		// 物流单行号
	private String ownerCode;		// 货主编码
	private String parentSkuCode;		// 父件编码
	private String packCode;		// 包装编码
	private String uom;		// 包装单位
	private Double qtyPlanEa = 0D;		// 计划加工数EA
	private Double qtyKitEa = 0D;		// 实际加工数EA
	private String planKitLoc;		// 计划加工台
	private String paRule;		// 上架规则
	private String isCreateSub;		// 是否已生成子件
	private Date lotAtt01;		// 批次属性1(生产日期)
	private Date lotAtt02;		// 批次属性2(失效日期)
	private Date lotAtt03;		// 批次属性3(入库日期)
	private String lotAtt04;		// 批次属性4
	private String lotAtt05;		// 批次属性5
	private String lotAtt06;		// 批次属性6
	private String lotAtt07;		// 批次属性7
	private String lotAtt08;		// 批次属性8
	private String lotAtt09;		// 批次属性9
	private String lotAtt10;		// 批次属性10
	private String lotAtt11;		// 批次属性11
	private String lotAtt12;		// 批次属性12
	private String def1;		// 自定义1
	private String def2;		// 自定义2
	private String def3;		// 自定义3
	private String def4;		// 自定义4
	private String def5;		// 自定义5
	private String orgId;		// 机构ID
	private String headerId;	// 头Id
	
	public BanQinWmKitParentDetail() {
		super();
		this.recVer = 0;
	}

	public BanQinWmKitParentDetail(String id){
		super(id);
	}

	@ExcelField(title="加工单号", align=2, sort=2)
	public String getKitNo() {
		return kitNo;
	}

	public void setKitNo(String kitNo) {
		this.kitNo = kitNo;
	}
	
	@ExcelField(title="父件明细行号", align=2, sort=3)
	public String getParentLineNo() {
		return parentLineNo;
	}

	public void setParentLineNo(String parentLineNo) {
		this.parentLineNo = parentLineNo;
	}
	
	@ExcelField(title="状态", dictType="SYS_WM_KIT_STATUS", align=2, sort=4)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@ExcelField(title="物流单号", align=2, sort=5)
	public String getLogisticNo() {
		return logisticNo;
	}

	public void setLogisticNo(String logisticNo) {
		this.logisticNo = logisticNo;
	}
	
	@ExcelField(title="物流单行号", align=2, sort=6)
	public String getLogisticLineNo() {
		return logisticLineNo;
	}

	public void setLogisticLineNo(String logisticLineNo) {
		this.logisticLineNo = logisticLineNo;
	}
	
	@ExcelField(title="货主编码", align=2, sort=7)
	public String getOwnerCode() {
		return ownerCode;
	}

	public void setOwnerCode(String ownerCode) {
		this.ownerCode = ownerCode;
	}
	
	@ExcelField(title="父件编码", align=2, sort=8)
	public String getParentSkuCode() {
		return parentSkuCode;
	}

	public void setParentSkuCode(String parentSkuCode) {
		this.parentSkuCode = parentSkuCode;
	}
	
	@ExcelField(title="包装编码", align=2, sort=9)
	public String getPackCode() {
		return packCode;
	}

	public void setPackCode(String packCode) {
		this.packCode = packCode;
	}
	
	@ExcelField(title="包装单位", align=2, sort=10)
	public String getUom() {
		return uom;
	}

	public void setUom(String uom) {
		this.uom = uom;
	}
	
	@ExcelField(title="计划加工数EA", align=2, sort=11)
	public Double getQtyPlanEa() {
		return qtyPlanEa;
	}

	public void setQtyPlanEa(Double qtyPlanEa) {
		this.qtyPlanEa = qtyPlanEa;
	}
	
	@ExcelField(title="实际加工数EA", align=2, sort=12)
	public Double getQtyKitEa() {
		return qtyKitEa;
	}

	public void setQtyKitEa(Double qtyKitEa) {
		this.qtyKitEa = qtyKitEa;
	}
	
	@ExcelField(title="计划加工台", align=2, sort=13)
	public String getPlanKitLoc() {
		return planKitLoc;
	}

	public void setPlanKitLoc(String planKitLoc) {
		this.planKitLoc = planKitLoc;
	}
	
	@ExcelField(title="上架规则", align=2, sort=14)
	public String getPaRule() {
		return paRule;
	}

	public void setPaRule(String paRule) {
		this.paRule = paRule;
	}
	
	@ExcelField(title="是否已生成子件", align=2, sort=15)
	public String getIsCreateSub() {
		return isCreateSub;
	}

	public void setIsCreateSub(String isCreateSub) {
		this.isCreateSub = isCreateSub;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	@ExcelField(title="批次属性1(生产日期)", align=2, sort=16)
	public Date getLotAtt01() {
		return lotAtt01;
	}

	public void setLotAtt01(Date lotAtt01) {
		this.lotAtt01 = lotAtt01;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	@ExcelField(title="批次属性2(失效日期)", align=2, sort=17)
	public Date getLotAtt02() {
		return lotAtt02;
	}

	public void setLotAtt02(Date lotAtt02) {
		this.lotAtt02 = lotAtt02;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	@ExcelField(title="批次属性3(入库日期)", align=2, sort=18)
	public Date getLotAtt03() {
		return lotAtt03;
	}

	public void setLotAtt03(Date lotAtt03) {
		this.lotAtt03 = lotAtt03;
	}
	
	@ExcelField(title="批次属性4", align=2, sort=19)
	public String getLotAtt04() {
		return lotAtt04;
	}

	public void setLotAtt04(String lotAtt04) {
		this.lotAtt04 = lotAtt04;
	}
	
	@ExcelField(title="批次属性5", align=2, sort=20)
	public String getLotAtt05() {
		return lotAtt05;
	}

	public void setLotAtt05(String lotAtt05) {
		this.lotAtt05 = lotAtt05;
	}
	
	@ExcelField(title="批次属性6", align=2, sort=21)
	public String getLotAtt06() {
		return lotAtt06;
	}

	public void setLotAtt06(String lotAtt06) {
		this.lotAtt06 = lotAtt06;
	}
	
	@ExcelField(title="批次属性7", align=2, sort=22)
	public String getLotAtt07() {
		return lotAtt07;
	}

	public void setLotAtt07(String lotAtt07) {
		this.lotAtt07 = lotAtt07;
	}
	
	@ExcelField(title="批次属性8", align=2, sort=23)
	public String getLotAtt08() {
		return lotAtt08;
	}

	public void setLotAtt08(String lotAtt08) {
		this.lotAtt08 = lotAtt08;
	}
	
	@ExcelField(title="批次属性9", align=2, sort=24)
	public String getLotAtt09() {
		return lotAtt09;
	}

	public void setLotAtt09(String lotAtt09) {
		this.lotAtt09 = lotAtt09;
	}
	
	@ExcelField(title="批次属性10", align=2, sort=25)
	public String getLotAtt10() {
		return lotAtt10;
	}

	public void setLotAtt10(String lotAtt10) {
		this.lotAtt10 = lotAtt10;
	}
	
	@ExcelField(title="批次属性11", align=2, sort=26)
	public String getLotAtt11() {
		return lotAtt11;
	}

	public void setLotAtt11(String lotAtt11) {
		this.lotAtt11 = lotAtt11;
	}
	
	@ExcelField(title="批次属性12", align=2, sort=27)
	public String getLotAtt12() {
		return lotAtt12;
	}

	public void setLotAtt12(String lotAtt12) {
		this.lotAtt12 = lotAtt12;
	}
	
	@ExcelField(title="自定义1", align=2, sort=28)
	public String getDef1() {
		return def1;
	}

	public void setDef1(String def1) {
		this.def1 = def1;
	}
	
	@ExcelField(title="自定义2", align=2, sort=29)
	public String getDef2() {
		return def2;
	}

	public void setDef2(String def2) {
		this.def2 = def2;
	}
	
	@ExcelField(title="自定义3", align=2, sort=30)
	public String getDef3() {
		return def3;
	}

	public void setDef3(String def3) {
		this.def3 = def3;
	}
	
	@ExcelField(title="自定义4", align=2, sort=31)
	public String getDef4() {
		return def4;
	}

	public void setDef4(String def4) {
		this.def4 = def4;
	}
	
	@ExcelField(title="自定义5", align=2, sort=32)
	public String getDef5() {
		return def5;
	}

	public void setDef5(String def5) {
		this.def5 = def5;
	}

	@ExcelField(title="机构ID", align=2, sort=41)
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