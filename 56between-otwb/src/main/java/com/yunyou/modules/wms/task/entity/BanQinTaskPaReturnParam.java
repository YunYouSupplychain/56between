package com.yunyou.modules.wms.task.entity;

/**
 * 上架出参
 * @author WMJ
 * @version 2019/02/20
 */
public class BanQinTaskPaReturnParam {
	// 上架任务
	private BanQinWmTaskPa wmTaskPaModel;
	// 交易ID
	private String tranId;

    public BanQinWmTaskPa getWmTaskPaModel() {
        return wmTaskPaModel;
    }

    public void setWmTaskPaModel(BanQinWmTaskPa wmTaskPaModel) {
        this.wmTaskPaModel = wmTaskPaModel;
    }

    public String getTranId() {
		return tranId;
	}

	public void setTranId(String tranId) {
		this.tranId = tranId;
	}

}
