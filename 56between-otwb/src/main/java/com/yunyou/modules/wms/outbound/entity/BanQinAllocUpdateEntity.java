package com.yunyou.modules.wms.outbound.entity;

import com.yunyou.modules.wms.common.entity.ActionCode;
import com.yunyou.modules.wms.common.entity.BanQinCdWhPackageRelationEntity;

/**
 * 分配更新入参Entity(单事务)
 * @author WMJ
 * @version 2019/02/20
 */
public class BanQinAllocUpdateEntity {
	// 库存更新类型
	private ActionCode actionCode;
	// 出库单号
	private String soNo;
	// 出库单行号
	private String lineNo;
	// 预配ID
	private String preallocId;
	// 预配明细单位换算数量
	private Double qtyPackUom;
	// 波次号
	private String waveNo;
	// 可分配数量
	private Double qtyAllocEa;
	// 收货人- 从出库单获取
	private String consigneeCode;
	// 快递单号- 从出库单获取
	private String trackingNo;
	// 包装单位换算
	private BanQinCdWhPackageRelationEntity packageEntity;
	// 批次库位库存
	private BanQinAllocInvLotLocEntity invLotLocEntity;

	public ActionCode getActionCode() {
		return actionCode;
	}

	public void setActionCode(ActionCode actionCode) {
		this.actionCode = actionCode;
	}

	public String getSoNo() {
		return soNo;
	}

	public void setSoNo(String soNo) {
		this.soNo = soNo;
	}

	public String getLineNo() {
		return lineNo;
	}

	public void setLineNo(String lineNo) {
		this.lineNo = lineNo;
	}

	public String getPreallocId() {
		return preallocId;
	}

	public void setPreallocId(String preallocId) {
		this.preallocId = preallocId;
	}

	public Double getQtyPackUom() {
		return qtyPackUom;
	}

	public void setQtyPackUom(Double qtyPackUom) {
		this.qtyPackUom = qtyPackUom;
	}

	public String getWaveNo() {
		return waveNo;
	}

	public void setWaveNo(String waveNo) {
		this.waveNo = waveNo;
	}

	public Double getQtyAllocEa() {
		return qtyAllocEa;
	}

	public void setQtyAllocEa(Double qtyAllocEa) {
		this.qtyAllocEa = qtyAllocEa;
	}

	public String getConsigneeCode() {
		return consigneeCode;
	}

	public void setConsigneeCode(String consigneeCode) {
		this.consigneeCode = consigneeCode;
	}

	public String getTrackingNo() {
		return trackingNo;
	}

	public void setTrackingNo(String trackingNo) {
		this.trackingNo = trackingNo;
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
}