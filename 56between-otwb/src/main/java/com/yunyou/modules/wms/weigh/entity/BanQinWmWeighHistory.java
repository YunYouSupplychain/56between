package com.yunyou.modules.wms.weigh.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.core.persistence.DataEntity;

import java.util.Date;

/**
 * 称重履历表Entity
 * @author zyf
 * @version 2020-01-08
 */
public class BanQinWmWeighHistory extends DataEntity<BanQinWmWeighHistory> {
	
	private static final long serialVersionUID = 1L;
	private String boxNum;				// 称重条码
	private String orgId;				// 机构
	private Double tareQty;				// 皮重
	private Double weighQty;			// 重量
	private Date weighTime;				// 称重时间
	private String deviceNo;			// 设备号
	private String handleAction;		// 处理动作
	private String handleMsg;			// 处理信息
	private String orderNo;				// 单据号
	private String allocId;				// 分配明细id
	private String customerOrderNo;		// 客户订单号
	private String businessNo;			// 商流单号
	private String chainNo;				// 供应链订单号
	private String taskNo;				// 供应链任务号
	private String externalNo;			// 外部单号
	private String waveNo;				// 波次单号
	private String ownerCode;			// 货主编码
	private String ownerName;			// 货主名称
	private String skuCode;				// 商品编码
	private String skuName;				// 商品名称
	private String caseNo;				// 箱号

	public BanQinWmWeighHistory() {
		super();
		this.recVer = 0;
	}

	public BanQinWmWeighHistory(String id){
		super(id);
	}

	@ExcelField(title="称重条码", align=2, sort=7)
	public String getBoxNum() {
		return boxNum;
	}

	public void setBoxNum(String boxNum) {
		this.boxNum = boxNum;
	}
	
	@ExcelField(title="机构", align=2, sort=8)
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
	@ExcelField(title="皮重", align=2, sort=9)
	public Double getTareQty() {
		return tareQty;
	}

	public void setTareQty(Double tareQty) {
		this.tareQty = tareQty;
	}
	
	@ExcelField(title="重量", align=2, sort=10)
	public Double getWeighQty() {
		return weighQty;
	}

	public void setWeighQty(Double weighQty) {
		this.weighQty = weighQty;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="称重时间", align=2, sort=11)
	public Date getWeighTime() {
		return weighTime;
	}

	public void setWeighTime(Date weighTime) {
		this.weighTime = weighTime;
	}
	
	@ExcelField(title="设备号", align=2, sort=12)
	public String getDeviceNo() {
		return deviceNo;
	}

	public void setDeviceNo(String deviceNo) {
		this.deviceNo = deviceNo;
	}
	
	@ExcelField(title="处理动作", align=2, sort=13)
	public String getHandleAction() {
		return handleAction;
	}

	public void setHandleAction(String handleAction) {
		this.handleAction = handleAction;
	}
	
	@ExcelField(title="处理信息", align=2, sort=14)
	public String getHandleMsg() {
		return handleMsg;
	}

	public void setHandleMsg(String handleMsg) {
		this.handleMsg = handleMsg;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getAllocId() {
		return allocId;
	}

	public void setAllocId(String allocId) {
		this.allocId = allocId;
	}

	public String getCustomerOrderNo() {
		return customerOrderNo;
	}

	public void setCustomerOrderNo(String customerOrderNo) {
		this.customerOrderNo = customerOrderNo;
	}

	public String getBusinessNo() {
		return businessNo;
	}

	public void setBusinessNo(String businessNo) {
		this.businessNo = businessNo;
	}

	public String getChainNo() {
		return chainNo;
	}

	public void setChainNo(String chainNo) {
		this.chainNo = chainNo;
	}

	public String getTaskNo() {
		return taskNo;
	}

	public void setTaskNo(String taskNo) {
		this.taskNo = taskNo;
	}

	public String getExternalNo() {
		return externalNo;
	}

	public void setExternalNo(String externalNo) {
		this.externalNo = externalNo;
	}

	public String getWaveNo() {
		return waveNo;
	}

	public void setWaveNo(String waveNo) {
		this.waveNo = waveNo;
	}

	public String getOwnerCode() {
		return ownerCode;
	}

	public void setOwnerCode(String ownerCode) {
		this.ownerCode = ownerCode;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

	public String getSkuName() {
		return skuName;
	}

	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}

	public String getCaseNo() {
		return caseNo;
	}

	public void setCaseNo(String caseNo) {
		this.caseNo = caseNo;
	}
}