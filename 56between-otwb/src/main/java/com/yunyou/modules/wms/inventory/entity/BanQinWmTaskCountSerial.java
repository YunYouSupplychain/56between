package com.yunyou.modules.wms.inventory.entity;

import com.yunyou.core.persistence.DataEntity;
import com.yunyou.common.utils.excel.annotation.ExcelField;

/**
 * 序列号盘点任务Entity
 * @author WMJ
 * @version 2019-01-28
 */
public class BanQinWmTaskCountSerial extends DataEntity<BanQinWmTaskCountSerial> {
	
	private static final long serialVersionUID = 1L;
	private String countNo;		// 盘点单号
	private String ownerCode;		// 货主编码
	private String skuCode;		// 商品编码
	private String serialNo;		// 序列号
	private String status;		// 盘点状态（创建，完成）
	private String countResult;		// 盘点结果（匹配，盘盈，盘亏）
	private String lotNum;		// 批次号
	private String locCode;		// 库位编码
	private String traceId;		// 跟踪号
	private String orgId;		// 分公司
	private String headerId;    // 头Id
	
	public BanQinWmTaskCountSerial() {
		super();
		this.recVer = 0;
	}

	public BanQinWmTaskCountSerial(String id){
		super(id);
	}

	@ExcelField(title="盘点单号", align=2, sort=2)
	public String getCountNo() {
		return countNo;
	}

	public void setCountNo(String countNo) {
		this.countNo = countNo;
	}
	
	@ExcelField(title="货主编码", align=2, sort=3)
	public String getOwnerCode() {
		return ownerCode;
	}

	public void setOwnerCode(String ownerCode) {
		this.ownerCode = ownerCode;
	}
	
	@ExcelField(title="商品编码", align=2, sort=4)
	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}
	
	@ExcelField(title="序列号", align=2, sort=5)
	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
	
	@ExcelField(title="盘点状态（创建，完成）", align=2, sort=6)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@ExcelField(title="盘点结果（匹配，盘盈，盘亏）", align=2, sort=7)
	public String getCountResult() {
		return countResult;
	}

	public void setCountResult(String countResult) {
		this.countResult = countResult;
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

	@ExcelField(title="分公司", align=2, sort=18)
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