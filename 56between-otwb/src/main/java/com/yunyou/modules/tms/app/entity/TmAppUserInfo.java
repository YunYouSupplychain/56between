
package com.yunyou.modules.tms.app.entity;

import com.yunyou.core.persistence.DataEntity;

/**
 * app用户信息Entity
 * @author zyf
 * @version 2020-06-29
 */
public class TmAppUserInfo extends DataEntity<TmAppUserInfo> {
	
	private static final long serialVersionUID = 1L;
	private String loginName;				// 登录名
	private String password;				// 密码
	private String name;					// 姓名
	private String status;					// 状态（00未审核， 10已审核）
	private String userId;					// 用户id
	private String isCustomer;				// 是否客户
	private String transportObjCode;		// 业务对象编码
	private String isDriver;				// 是否运输人员
	private String driver;					// 运输人员编码
	private String orgId;					// 机构ID
	private String def1;					// 自定义1(微信登录 openid)
	private String def2;					// 自定义2(所属公司 用户自填)
	private String def3;					// 自定义3
	private String def4;					// 自定义4
	private String def5;					// 自定义5
	private String baseOrgId;				// 基础数据机构ID
	private String isSafetyChecker;			// 是否安检员
	private String isWarehouseMan;			// 是否仓管
	
	public TmAppUserInfo() {
		super();
	}

	public TmAppUserInfo(String id){
		super(id);
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIsCustomer() {
		return isCustomer;
	}

	public void setIsCustomer(String isCustomer) {
		this.isCustomer = isCustomer;
	}

	public String getIsDriver() {
		return isDriver;
	}

	public void setIsDriver(String isDriver) {
		this.isDriver = isDriver;
	}

	public String getTransportObjCode() {
		return transportObjCode;
	}

	public void setTransportObjCode(String transportObjCode) {
		this.transportObjCode = transportObjCode;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
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

	public String getBaseOrgId() {
		return baseOrgId;
	}

	public void setBaseOrgId(String baseOrgId) {
		this.baseOrgId = baseOrgId;
	}

	public String getIsSafetyChecker() {
		return isSafetyChecker;
	}

	public void setIsSafetyChecker(String isSafetyChecker) {
		this.isSafetyChecker = isSafetyChecker;
	}

	public String getIsWarehouseMan() {
		return isWarehouseMan;
	}

	public void setIsWarehouseMan(String isWarehouseMan) {
		this.isWarehouseMan = isWarehouseMan;
	}
}