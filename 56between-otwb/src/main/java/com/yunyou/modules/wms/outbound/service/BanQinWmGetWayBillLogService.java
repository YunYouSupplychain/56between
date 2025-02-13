package com.yunyou.modules.wms.outbound.service;

import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.wms.outbound.entity.BanQinWmGetWayBillLog;
import com.yunyou.modules.wms.outbound.mapper.BanQinWmGetWayBillLogMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 面单获取日志Service
 * @author WMJ
 * @version 2020-03-23
 */
@Service
@Transactional(readOnly = true)
public class BanQinWmGetWayBillLogService extends CrudService<BanQinWmGetWayBillLogMapper, BanQinWmGetWayBillLog> {

	public BanQinWmGetWayBillLog findFirst(BanQinWmGetWayBillLog wmGetWayBillLog) {
		List<BanQinWmGetWayBillLog> list = this.findList(wmGetWayBillLog);
		return CollectionUtil.isNotEmpty(list) ? list.get(0) : null;
	}

	@Transactional
	public void saveInfo(String waveNo, String soNo, String trackingNo, String caseNo, String type, String response, String orgId, String def1, String def2) {
		BanQinWmGetWayBillLog log = new BanQinWmGetWayBillLog();
		log.setWaveNo(waveNo);
		log.setSoNo(soNo);
		log.setTrackingNo(trackingNo);
		log.setCaseNo(caseNo);
		log.setType(type);
		log.setResponse(response);
		log.setOrgId(orgId);
		log.setDef1(def1);
		log.setDef2(def2);
		this.save(log);
	}

	public List<BanQinWmGetWayBillLog> findByUniKey(String soNo, String trackingNo, String caseNo, String orgId) {
		BanQinWmGetWayBillLog log = new BanQinWmGetWayBillLog();
		log.setSoNo(soNo);
		log.setTrackingNo(trackingNo);
		log.setCaseNo(caseNo);
		log.setOrgId(orgId);
		return this.findList(log);
	}

}