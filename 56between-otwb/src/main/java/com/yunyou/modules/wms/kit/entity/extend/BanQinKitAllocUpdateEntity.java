package com.yunyou.modules.wms.kit.entity.extend;

import com.yunyou.modules.wms.common.entity.ActionCode;
import com.yunyou.modules.wms.common.entity.BanQinCdWhPackageRelationEntity;
import com.yunyou.modules.wms.outbound.entity.BanQinAllocInvLotLocEntity;

/**
 * @author WMJ
 * @version 2019/08/29
 */
public class BanQinKitAllocUpdateEntity {
	// 库存更新类型
	private ActionCode actionCode;
	// 加工单号
	private String kitNo;
	// 加工单父件行号
	private String parentLineNo;
	// 加工单子件行号
	private String subLineNo;
	// 目标拣货库位
	private String planKitLoc;
	// 可分配数量
	private Double qtyAllocEa;
	// 包装单位换算
	private BanQinCdWhPackageRelationEntity packageEntity;
	// 批次库位库存
	private BanQinAllocInvLotLocEntity invLotLocEntity;
	// 机构
	private String orgId;
	//
	private String kitId;

	public ActionCode getActionCode() {
		return actionCode;
	}

	public void setActionCode(ActionCode actionCode) {
		this.actionCode = actionCode;
	}

	public String getKitNo() {
		return kitNo;
	}

	public void setKitNo(String kitNo) {
		this.kitNo = kitNo;
	}

	public String getParentLineNo() {
		return parentLineNo;
	}

	public void setParentLineNo(String parentLineNo) {
		this.parentLineNo = parentLineNo;
	}

	public String getSubLineNo() {
		return subLineNo;
	}

	public void setSubLineNo(String subLineNo) {
		this.subLineNo = subLineNo;
	}

	public String getPlanKitLoc() {
		return planKitLoc;
	}

	public void setPlanKitLoc(String planKitLoc) {
		this.planKitLoc = planKitLoc;
	}

	public Double getQtyAllocEa() {
		return qtyAllocEa;
	}

	public void setQtyAllocEa(Double qtyAllocEa) {
		this.qtyAllocEa = qtyAllocEa;
	}

	public BanQinCdWhPackageRelationEntity getPackageEntity() {
		return packageEntity;
	}

	public void setPackageEntity(BanQinCdWhPackageRelationEntity packageEntity) {
		this.packageEntity = packageEntity;
	}

	public BanQinAllocInvLotLocEntity getInvLotLocEntity() {
		return invLotLocEntity;
	}

	public void setInvLotLocEntity(BanQinAllocInvLotLocEntity invLotLocEntity) {
		this.invLotLocEntity = invLotLocEntity;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getKitId() {
		return kitId;
	}

	public void setKitId(String kitId) {
		this.kitId = kitId;
	}
}
