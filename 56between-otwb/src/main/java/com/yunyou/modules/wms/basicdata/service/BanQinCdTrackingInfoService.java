package com.yunyou.modules.wms.basicdata.service;

import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdTrackingInfo;
import com.yunyou.modules.wms.basicdata.mapper.BanQinCdTrackingInfoMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 快递接口信息Service
 * @author WMJ
 * @version 2020-05-06
 */
@Service
@Transactional(readOnly = true)
public class BanQinCdTrackingInfoService extends CrudService<BanQinCdTrackingInfoMapper, BanQinCdTrackingInfo> {

	public BanQinCdTrackingInfo get(String id) {
		return super.get(id);
	}
	
	public List<BanQinCdTrackingInfo> findList(BanQinCdTrackingInfo info) {
		return super.findList(info);
	}
	
	public Page<BanQinCdTrackingInfo> findPage(Page<BanQinCdTrackingInfo> page, BanQinCdTrackingInfo info) {
        dataRuleFilter(info);
        info.setPage(page);
        page.setList(mapper.findPage(info));
		return page;
	}
	
	@Transactional
	public void save(BanQinCdTrackingInfo info) {
		super.save(info);
	}

	@Transactional
	public void delete(BanQinCdTrackingInfo info) {
		super.delete(info);
	}

}