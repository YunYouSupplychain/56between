package com.yunyou.modules.wms.basicdata.service;

import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhLotDetail;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhLotHeader;
import com.yunyou.modules.wms.basicdata.mapper.BanQinCdWhLotHeaderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 批次属性Service
 * @author WMJ
 * @version 2019-01-25
 */
@Service
@Transactional(readOnly = true)
public class BanQinCdWhLotHeaderService extends CrudService<BanQinCdWhLotHeaderMapper, BanQinCdWhLotHeader> {
    @Autowired
    private BanQinCdWhLotDetailService cdWhLotDetailService;
    
	public Page<BanQinCdWhLotHeader> findPage(Page<BanQinCdWhLotHeader> page, BanQinCdWhLotHeader banQinCdWhLotHeader) {
		dataRuleFilter(banQinCdWhLotHeader);
		banQinCdWhLotHeader.setPage(page);
		page.setList(mapper.findPage(banQinCdWhLotHeader));
		return page;
	}

	@Override
	@Transactional
	public void save(BanQinCdWhLotHeader banQinCdWhLotHeader) {
		super.save(banQinCdWhLotHeader);
		if (CollectionUtil.isNotEmpty(banQinCdWhLotHeader.getCdWhLotDetailList())) {
		    for (BanQinCdWhLotDetail detail : banQinCdWhLotHeader.getCdWhLotDetailList()) {
                detail.setLotCode(banQinCdWhLotHeader.getLotCode());
                detail.setHeaderId(banQinCdWhLotHeader.getId());
				detail.setOrgId(banQinCdWhLotHeader.getOrgId());
                cdWhLotDetailService.save(detail);
            }
        }
	}

	@Override
	@Transactional
	public void delete(BanQinCdWhLotHeader banQinCdWhLotHeader) {
		cdWhLotDetailService.deleteByHeaderId(banQinCdWhLotHeader.getId());
		super.delete(banQinCdWhLotHeader);
	}

	public BanQinCdWhLotHeader getByCode(String lotCode, String orgId) {
		return mapper.getByCode(lotCode, orgId);
	}

	@Transactional
	public void remove(String lotCode, String orgId) {
		cdWhLotDetailService.remove(lotCode, orgId);
		mapper.remove(lotCode, orgId);
	}
}