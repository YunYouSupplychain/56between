package com.yunyou.modules.wms.common.entity;

import com.yunyou.modules.wms.common.lock.ILockLotProvider;
import com.yunyou.modules.wms.common.lock.LockLotInfo;
import com.yunyou.modules.wms.inventory.entity.BanQinWmInvLotLoc;

/**
 * 库存更新参数对象
 * @author WMJ
 * @version 2019-01-26
 */
public class BanQinInventoryEntity extends BanQinWmInvLotLoc implements ILockLotProvider {
	// 动作
	private ActionCode action;
	// 单据号
	private String orderNo;
	// 行号
	private String lineNo;
	// 任务编号
	private String taskId;
	// 任务行号
	private String taskLineNo;
	// 包装编码
	private String packCode;
	// 单位
	private String uom;
	// 包装数
	private Double qtyUom;
	// 当前库存数量
	private Double qtyEaBefore;
	// 操作数
	private Double qtyEaOp;
	// 计划库位
	private String planLocCode;
	// 计划跟踪号
	private String planTraceId;
	// 预配数
	private Double qtyPrealloc;
	// 体积
	private Double cubic;
	// 重量
	private Double grossWeight;
	// 托盘数
	private Double palletQty;
	// 可用数
	private Double availableQty;
	// 计划预约数
	private Double planRecQty;
	// 事务Id
	private String tranId;

	public Double getQtyPrealloc() {
		return qtyPrealloc;
	}

	public void setQtyPrealloc(Double qtyPrealloc) {
		this.qtyPrealloc = qtyPrealloc;
	}

	public ActionCode getAction() {
		return action;
	}

	public void setAction(ActionCode action) {
		this.action = action;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getLineNo() {
		return lineNo;
	}

	public void setLineNo(String lineNo) {
		this.lineNo = lineNo;
	}

	public String getPackCode() {
		return packCode;
	}

	public void setPackCode(String packCode) {
		this.packCode = packCode;
	}

	public String getUom() {
		return uom;
	}

	public void setUom(String uom) {
		this.uom = uom;
	}

	public Double getQtyUom() {
		return qtyUom;
	}

	public void setQtyUom(Double qtyUom) {
		this.qtyUom = qtyUom;
	}

	public Double getQtyEaBefore() {
		return qtyEaBefore;
	}

	public void setQtyEaBefore(Double qtyEaBefore) {
		this.qtyEaBefore = qtyEaBefore;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getTaskLineNo() {
		return taskLineNo;
	}

	public void setTaskLineNo(String taskLineNo) {
		this.taskLineNo = taskLineNo;
	}

	public Double getQtyEaOp() {
		return qtyEaOp;
	}

	public void setQtyEaOp(Double qtyEaOp) {
		this.qtyEaOp = qtyEaOp;
	}

	public String getPlanLocCode() {
		return planLocCode;
	}

	public void setPlanLocCode(String planLocCode) {
		this.planLocCode = planLocCode;
	}

	public String getPlanTraceId() {
		return planTraceId;
	}

	public void setPlanTraceId(String planTraceId) {
		this.planTraceId = planTraceId;
	}

	public Double getCubic() {
		return cubic;
	}

	public void setCubic(Double cubic) {
		this.cubic = cubic;
	}

	public Double getGrossWeight() {
		return grossWeight;
	}

	public void setGrossWeight(Double grossWeight) {
		this.grossWeight = grossWeight;
	}

	public Double getPalletQty() {
		return palletQty;
	}

	public void setPalletQty(Double palletQty) {
		this.palletQty = palletQty;
	}

	public Double getAvailableQty() {
		return availableQty;
	}

	public void setAvailableQty(Double availableQty) {
		this.availableQty = availableQty;
	}

	public Double getPlanRecQty() {
		return planRecQty;
	}

	public void setPlanRecQty(Double planRecQty) {
		this.planRecQty = planRecQty;
	}

	public String getTranId() {
		return tranId;
	}

	public void setTranId(String tranId) {
		this.tranId = tranId;
	}

	/**
	 * 悲观锁
	 */
	@Override
    public LockLotInfo getNeedLockLotInfo() {
		return new LockLotInfo(this.getLotNum(), this.getOrgId());
	}

}
