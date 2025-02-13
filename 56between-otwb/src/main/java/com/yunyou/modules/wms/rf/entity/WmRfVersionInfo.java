package com.yunyou.modules.wms.rf.entity;

import com.yunyou.core.persistence.DataEntity;

/**
 * RF版本信息Entity
 * @author WMJ
 * @version 2019-08-15
 */
public class WmRfVersionInfo extends DataEntity<WmRfVersionInfo> {
	private static final long serialVersionUID = 1L;
	private String versionName;   	// 版本号
	private String versionInfo;		// 版本信息
	private String downloadAddress;	// 下载地址
	private String def1;
	private String def2;
	private String def3;

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public String getVersionInfo() {
		return versionInfo;
	}

	public void setVersionInfo(String versionInfo) {
		this.versionInfo = versionInfo;
	}

	public String getDownloadAddress() {
		return downloadAddress;
	}

	public void setDownloadAddress(String downloadAddress) {
		this.downloadAddress = downloadAddress;
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
}