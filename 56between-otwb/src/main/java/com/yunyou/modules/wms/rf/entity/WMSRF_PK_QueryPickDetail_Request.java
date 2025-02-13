package com.yunyou.modules.wms.rf.entity;

import java.io.Serializable;

@SuppressWarnings("serial")
public class WMSRF_PK_QueryPickDetail_Request implements Serializable {
	// 用户Id
	private String userId;
	// 发运订单号
	private String soNo;
	// 拣货单号
	private String pickNo;
	// 分配ID
	private String allocId;
	// 游标：获取指定记录
	private int currentIndex;
	// 波次号
	private String waveNo;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getCurrentIndex() {
		return currentIndex;
	}

	public void setCurrentIndex(int currentIndex) {
		this.currentIndex = currentIndex;
	}

	public String getSoNo() {
		return soNo;
	}

	public void setSoNo(String soNo) {
		this.soNo = soNo;
	}

	public String getPickNo() {
		return pickNo;
	}

	public void setPickNo(String pickNo) {
		this.pickNo = pickNo;
	}

	public String getAllocId() {
		return allocId;
	}

	public void setAllocId(String allocId) {
		this.allocId = allocId;
	}

	public String getWaveNo() {
		return waveNo;
	}

	public void setWaveNo(String waveNo) {
		this.waveNo = waveNo;
	}
}
