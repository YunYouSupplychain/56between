package com.yunyou.modules.tms.rf.entity;

import java.io.Serializable;

@SuppressWarnings("serial")
public class TMSRF_Inbound_Query_Request implements Serializable {
	// 备件入库单号
	private String sparePoNo;
	// 经办人
	private String operator;

	public String getSparePoNo() {
		return sparePoNo;
	}

	public void setSparePoNo(String sparePoNo) {
		this.sparePoNo = sparePoNo;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}
}
