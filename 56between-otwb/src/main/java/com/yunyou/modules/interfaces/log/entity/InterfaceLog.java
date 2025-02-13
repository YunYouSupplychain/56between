package com.yunyou.modules.interfaces.log.entity;

import com.yunyou.core.persistence.DataEntity;

/**
 * 接口日志Entity
 * @author zyf
 * @version 2019-12-19
 */
public class InterfaceLog extends DataEntity<InterfaceLog> {

	private static final long serialVersionUID = 1L;
	private String interfaceType;			// 接口类型
	private String isSuccess;				// 处理状态
	private String message;					// 处理结果
	private String requestData;				// 请求数据
	private String responseData;			// 反馈数据
	private String searchNo;				// 检索单号
	private String handleDirection;			// 处理方向
	private String interfaceDirection;		// 接口方向
	private String def1;					// 自定义1
	private String def2;					// 自定义2
	private String def3;					// 自定义3
	private String def4;					// 自定义4
	private String def5;					// 自定义5

	public InterfaceLog() {
		super();
	}

	public InterfaceLog(String id){
		super(id);
	}

	public String getInterfaceType() {
		return interfaceType;
	}

	public void setInterfaceType(String interfaceType) {
		this.interfaceType = interfaceType;
	}

	public String getIsSuccess() {
		return isSuccess;
	}

	public void setIsSuccess(String isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getRequestData() {
		return requestData;
	}

	public void setRequestData(String requestData) {
		this.requestData = requestData;
	}

	public String getResponseData() {
		return responseData;
	}

	public void setResponseData(String responseData) {
		this.responseData = responseData;
	}

	public String getSearchNo() {
		return searchNo;
	}

	public void setSearchNo(String searchNo) {
		this.searchNo = searchNo;
	}

	public String getDef1() {
		return def1;
	}

	public void setDef1(String def1) {
		this.def1 = def1;
	}

	public String getDef2() {
		return def2;
	}

	public void setDef2(String def2) {
		this.def2 = def2;
	}

	public String getDef3() {
		return def3;
	}

	public void setDef3(String def3) {
		this.def3 = def3;
	}

	public String getDef4() {
		return def4;
	}

	public void setDef4(String def4) {
		this.def4 = def4;
	}

	public String getDef5() {
		return def5;
	}

	public void setDef5(String def5) {
		this.def5 = def5;
	}

	public String getHandleDirection() {
		return handleDirection;
	}

	public void setHandleDirection(String handleDirection) {
		this.handleDirection = handleDirection;
	}

	public String getInterfaceDirection() {
		return interfaceDirection;
	}

	public void setInterfaceDirection(String interfaceDirection) {
		this.interfaceDirection = interfaceDirection;
	}
}