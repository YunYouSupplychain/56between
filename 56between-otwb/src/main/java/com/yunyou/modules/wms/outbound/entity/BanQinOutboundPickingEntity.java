package com.yunyou.modules.wms.outbound.entity;

/**
 * 拣货确认 出参数据对象
 * @author WMJ
 * @version 2019/02/20
 */
public class BanQinOutboundPickingEntity {

	// 分配拣货记录表
	private BanQinWmSoAlloc wmSoAllocModel;
	// 交易事务ID
	private String tranId;

    public BanQinWmSoAlloc getWmSoAllocModel() {
        return wmSoAllocModel;
    }

    public void setWmSoAllocModel(BanQinWmSoAlloc wmSoAllocModel) {
        this.wmSoAllocModel = wmSoAllocModel;
    }

    public String getTranId() {
		return tranId;
	}

	public void setTranId(String tranId) {
		this.tranId = tranId;
	}

}