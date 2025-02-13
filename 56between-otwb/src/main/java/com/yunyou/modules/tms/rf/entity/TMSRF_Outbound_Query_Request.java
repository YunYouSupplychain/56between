package com.yunyou.modules.tms.rf.entity;

import java.io.Serializable;

@SuppressWarnings("serial")
public class TMSRF_Outbound_Query_Request implements Serializable {
	// 备件出库单号
	private String spareSoNo;
	// 经办人
	private String operator;

	public String getSpareSoNo() {
		return spareSoNo;
	}

	public void setSpareSoNo(String spareSoNo) {
		this.spareSoNo = spareSoNo;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}
}
