package com.yunyou.modules.wms.outbound.entity;

/**
 * 订单关闭 出参数据对象 
 * @author WMJ
 * @version 2019/02/20
 */
public class BanQinOutboundCloseEntity {

	// 分配拣货记录表
	private BanQinWmSoHeader wmSoHeaderModel;

    public BanQinWmSoHeader getWmSoHeaderModel() {
        return wmSoHeaderModel;
    }

    public void setWmSoHeaderModel(BanQinWmSoHeader wmSoHeaderModel) {
        this.wmSoHeaderModel = wmSoHeaderModel;
    }
}