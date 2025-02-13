package com.yunyou.modules.wms.outbound.entity;

/**
 * 分派拣货生成拣货单号规则DispatchPkRuleEntity
 * @author WMJ
 * @version 2019/02/20
 */
public class BanQinDispatchPkRuleEntity {
	// 是否分库区
	private String isByZone;
	// 是否托盘任务
	private String isPlTask;
	// 是否箱任务
	private String isCsTask;
	// 是否零拣任务
	private String isEaTask;
	// 托盘任务表上限
	private Double plLimit;
	// 箱任务表上限
	private Double csLimit;
	// 零拣任务表上限
	private Double eaLimit;
	// 托盘任务表浮动值百分比%
	private Double plFloat;
	// 箱任务表浮动值百分比%
	private Double csFloat;
	// 零拣任务表浮动值百分比%
	private Double eaFloat;
	private String waveNos;
	private String orgId;

	public String getIsByZone() {
		return isByZone;
	}

	public void setIsByZone(String isByZone) {
		this.isByZone = isByZone;
	}

	public String getIsPlTask() {
		return isPlTask;
	}

	public void setIsPlTask(String isPlTask) {
		this.isPlTask = isPlTask;
	}

	public String getIsCsTask() {
		return isCsTask;
	}

	public void setIsCsTask(String isCsTask) {
		this.isCsTask = isCsTask;
	}

	public String getIsEaTask() {
		return isEaTask;
	}

	public void setIsEaTask(String isEaTask) {
		this.isEaTask = isEaTask;
	}

	public Double getPlLimit() {
		return plLimit;
	}

	public void setPlLimit(Double plLimit) {
		this.plLimit = plLimit;
	}

	public Double getCsLimit() {
		return csLimit;
	}

	public void setCsLimit(Double csLimit) {
		this.csLimit = csLimit;
	}

	public Double getEaLimit() {
		return eaLimit;
	}

	public void setEaLimit(Double eaLimit) {
		this.eaLimit = eaLimit;
	}

	public Double getPlFloat() {
		return plFloat;
	}

	public void setPlFloat(Double plFloat) {
		this.plFloat = plFloat;
	}

	public Double getCsFloat() {
		return csFloat;
	}

	public void setCsFloat(Double csFloat) {
		this.csFloat = csFloat;
	}

	public Double getEaFloat() {
		return eaFloat;
	}

	public void setEaFloat(Double eaFloat) {
		this.eaFloat = eaFloat;
	}

	public String getWaveNos() {
		return waveNos;
	}

	public void setWaveNos(String waveNos) {
		this.waveNos = waveNos;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
}