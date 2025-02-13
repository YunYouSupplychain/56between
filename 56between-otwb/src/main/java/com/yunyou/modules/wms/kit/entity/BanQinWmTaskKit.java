package com.yunyou.modules.wms.kit.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.core.persistence.DataEntity;

import java.util.Date;

/**
 * 加工任务Entity
 * @author Jianhua Liu
 * @version 2019-08-21
 */
public class BanQinWmTaskKit extends DataEntity<BanQinWmTaskKit> {
	
	private static final long serialVersionUID = 1L;
	private String kitTaskId;		// 加工任务ID
	private String kitNo;		// 加工单号
	private String subLineNo;		// 子件明细行号
	private String parentLineNo;		// 父件明细行号
	private String ownerCode;		// 货主编码
	private String subSkuCode;		// 子件编码
	private String lotNum;		// 批次号
	private String locCode;		// 库位编码
	private String traceId;		// 跟踪号
	private String status;		// 状态
	private String packCode;		// 包装编码
	private String uom;		// 包装单位
	private Double qtyUom = 0D;		// 包装数量
	private Double qtyEa = 0D;		// EA数量
	private String toLoc;		// 目标库位编码
	private String toId;		// 目标跟踪号
	private String pickOp;		// 拣货人
	private Date pickTime;		// 拣货时间
	private String kitOp;		// 加工人
	private Date kitTime;		// 加工时间
	private String kitLineNo;		// 加工操作行号
	private String def1;		// 自定义1
	private String def2;		// 自定义2
	private String def3;		// 自定义3
	private String def4;		// 自定义4
	private String def5;		// 自定义5
	private String orgId;		// 机构ID
	private String headerId;    // 头Id
	
	public BanQinWmTaskKit() {
		super();
		this.recVer = 0;
	}

	public BanQinWmTaskKit(String id){
		super(id);
	}

	@ExcelField(title="加工任务ID", align=2, sort=2)
	public String getKitTaskId() {
		return kitTaskId;
	}

	public void setKitTaskId(String kitTaskId) {
		this.kitTaskId = kitTaskId;
	}
	
	@ExcelField(title="加工单号", align=2, sort=3)
	public String getKitNo() {
		return kitNo;
	}

	public void setKitNo(String kitNo) {
		this.kitNo = kitNo;
	}
	
	@ExcelField(title="子件明细行号", align=2, sort=4)
	public String getSubLineNo() {
		return subLineNo;
	}

	public void setSubLineNo(String subLineNo) {
		this.subLineNo = subLineNo;
	}
	
	@ExcelField(title="父件明细行号", align=2, sort=5)
	public String getParentLineNo() {
		return parentLineNo;
	}

	public void setParentLineNo(String parentLineNo) {
		this.parentLineNo = parentLineNo;
	}
	
	@ExcelField(title="货主编码", align=2, sort=6)
	public String getOwnerCode() {
		return ownerCode;
	}

	public void setOwnerCode(String ownerCode) {
		this.ownerCode = ownerCode;
	}
	
	@ExcelField(title="子件编码", align=2, sort=7)
	public String getSubSkuCode() {
		return subSkuCode;
	}

	public void setSubSkuCode(String subSkuCode) {
		this.subSkuCode = subSkuCode;
	}
	
	@ExcelField(title="批次号", align=2, sort=8)
	public String getLotNum() {
		return lotNum;
	}

	public void setLotNum(String lotNum) {
		this.lotNum = lotNum;
	}
	
	@ExcelField(title="库位编码", align=2, sort=9)
	public String getLocCode() {
		return locCode;
	}

	public void setLocCode(String locCode) {
		this.locCode = locCode;
	}
	
	@ExcelField(title="跟踪号", align=2, sort=10)
	public String getTraceId() {
		return traceId;
	}

	public void setTraceId(String traceId) {
		this.traceId = traceId;
	}
	
	@ExcelField(title="状态", dictType="SYS_WM_SUB_KIT_STATUS", align=2, sort=11)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@ExcelField(title="包装编码", align=2, sort=12)
	public String getPackCode() {
		return packCode;
	}

	public void setPackCode(String packCode) {
		this.packCode = packCode;
	}
	
	@ExcelField(title="包装单位", align=2, sort=13)
	public String getUom() {
		return uom;
	}

	public void setUom(String uom) {
		this.uom = uom;
	}
	
	@ExcelField(title="包装数量", align=2, sort=14)
	public Double getQtyUom() {
		return qtyUom;
	}

	public void setQtyUom(Double qtyUom) {
		this.qtyUom = qtyUom;
	}
	
	@ExcelField(title="EA数量", align=2, sort=15)
	public Double getQtyEa() {
		return qtyEa;
	}

	public void setQtyEa(Double qtyEa) {
		this.qtyEa = qtyEa;
	}
	
	@ExcelField(title="目标库位编码", align=2, sort=16)
	public String getToLoc() {
		return toLoc;
	}

	public void setToLoc(String toLoc) {
		this.toLoc = toLoc;
	}
	
	@ExcelField(title="目标跟踪号", align=2, sort=17)
	public String getToId() {
		return toId;
	}

	public void setToId(String toId) {
		this.toId = toId;
	}
	
	@ExcelField(title="拣货人", align=2, sort=18)
	public String getPickOp() {
		return pickOp;
	}

	public void setPickOp(String pickOp) {
		this.pickOp = pickOp;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="拣货时间", align=2, sort=19)
	public Date getPickTime() {
		return pickTime;
	}

	public void setPickTime(Date pickTime) {
		this.pickTime = pickTime;
	}
	
	@ExcelField(title="加工人", align=2, sort=20)
	public String getKitOp() {
		return kitOp;
	}

	public void setKitOp(String kitOp) {
		this.kitOp = kitOp;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="加工时间", align=2, sort=21)
	public Date getKitTime() {
		return kitTime;
	}

	public void setKitTime(Date kitTime) {
		this.kitTime = kitTime;
	}
	
	@ExcelField(title="加工操作行号", align=2, sort=22)
	public String getKitLineNo() {
		return kitLineNo;
	}

	public void setKitLineNo(String kitLineNo) {
		this.kitLineNo = kitLineNo;
	}
	
	@ExcelField(title="自定义1", align=2, sort=23)
	public String getDef1() {
		return def1;
	}

	public void setDef1(String def1) {
		this.def1 = def1;
	}
	
	@ExcelField(title="自定义2", align=2, sort=24)
	public String getDef2() {
		return def2;
	}

	public void setDef2(String def2) {
		this.def2 = def2;
	}
	
	@ExcelField(title="自定义3", align=2, sort=25)
	public String getDef3() {
		return def3;
	}

	public void setDef3(String def3) {
		this.def3 = def3;
	}
	
	@ExcelField(title="自定义4", align=2, sort=26)
	public String getDef4() {
		return def4;
	}

	public void setDef4(String def4) {
		this.def4 = def4;
	}
	
	@ExcelField(title="自定义5", align=2, sort=27)
	public String getDef5() {
		return def5;
	}

	public void setDef5(String def5) {
		this.def5 = def5;
	}

	@ExcelField(title="机构ID", align=2, sort=36)
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