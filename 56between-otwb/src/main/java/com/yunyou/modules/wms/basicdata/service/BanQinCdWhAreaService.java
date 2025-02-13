package com.yunyou.modules.wms.basicdata.service;

import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhArea;
import com.yunyou.modules.wms.basicdata.mapper.BanQinCdWhAreaMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 区域Service
 * @author WMJ
 * @version 2019-01-25
 */
@Service
@Transactional(readOnly = true)
public class BanQinCdWhAreaService extends CrudService<BanQinCdWhAreaMapper, BanQinCdWhArea> {

	@Override
	public Page<BanQinCdWhArea> findPage(Page<BanQinCdWhArea> page, BanQinCdWhArea banQinCdWhArea) {
		dataRuleFilter(banQinCdWhArea);
		banQinCdWhArea.setPage(page);
		page.setList(mapper.findPage(banQinCdWhArea));
		return page;
	}

    public BanQinCdWhArea getByCode(String areaCode, String orgId) {
		return mapper.getByCode(areaCode, orgId);
    }

    @Transactional
    public void remove(String areaCode, String orgId) {
		mapper.remove(areaCode, orgId);
    }
}