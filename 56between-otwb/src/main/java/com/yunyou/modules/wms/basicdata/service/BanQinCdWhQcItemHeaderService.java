package com.yunyou.modules.wms.basicdata.service;

import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhQcItemHeader;
import com.yunyou.modules.wms.basicdata.mapper.BanQinCdWhQcItemHeaderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 质检项Service
 * @author WMJ
 * @version 2019-01-25
 */
@Service
@Transactional(readOnly = true)
public class BanQinCdWhQcItemHeaderService extends CrudService<BanQinCdWhQcItemHeaderMapper, BanQinCdWhQcItemHeader> {
    @Autowired
    private BanQinCdWhQcItemDetailService cdWhQcItemDetailService;
    
	public Page<BanQinCdWhQcItemHeader> findPage(Page<BanQinCdWhQcItemHeader> page, BanQinCdWhQcItemHeader banQinCdWhQcItemHeader) {
		dataRuleFilter(banQinCdWhQcItemHeader);
		banQinCdWhQcItemHeader.setPage(page);
		page.setList(mapper.findPage(banQinCdWhQcItemHeader));
		return page;
	}

	@Transactional
	public void delete(BanQinCdWhQcItemHeader banQinCdWhQcItemHeader) {
		cdWhQcItemDetailService.deleteByHeaderId(banQinCdWhQcItemHeader.getId());
		super.delete(banQinCdWhQcItemHeader);
	}

    public BanQinCdWhQcItemHeader getByCode(String itemGroupCode, String orgId) {
		return mapper.getByCode(itemGroupCode, orgId);
    }

    @Transactional
	public void remove(String itemGroupCode, String orgId) {
		cdWhQcItemDetailService.remove(itemGroupCode, orgId);
		mapper.remove(itemGroupCode, orgId);
	}
}