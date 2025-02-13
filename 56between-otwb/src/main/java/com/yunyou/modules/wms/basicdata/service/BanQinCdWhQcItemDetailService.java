package com.yunyou.modules.wms.basicdata.service;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhQcItemDetail;
import com.yunyou.modules.wms.basicdata.mapper.BanQinCdWhQcItemDetailMapper;
import com.yunyou.modules.wms.common.service.WmsUtil;
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
public class BanQinCdWhQcItemDetailService extends CrudService<BanQinCdWhQcItemDetailMapper, BanQinCdWhQcItemDetail> {
    @Autowired
    private WmsUtil wmsUtil;

	public Page<BanQinCdWhQcItemDetail> findPage(Page<BanQinCdWhQcItemDetail> page, BanQinCdWhQcItemDetail banQinCdWhQcItemDetail) {
		dataRuleFilter(banQinCdWhQcItemDetail);
		banQinCdWhQcItemDetail.setPage(page);
		page.setList(mapper.findPage(banQinCdWhQcItemDetail));
		return page;
	}

	@Override
	@Transactional
	public void save(BanQinCdWhQcItemDetail banQinCdWhQcItemDetail) {
	    if (StringUtils.isEmpty(banQinCdWhQcItemDetail.getId())) {
	        banQinCdWhQcItemDetail.setLineNo(wmsUtil.getMaxLineNo("cd_wh_qc_item_detail", "header_id", banQinCdWhQcItemDetail.getHeaderId()));
        }
		super.save(banQinCdWhQcItemDetail);
	}

	@Transactional
    public void deleteByHeaderId(String headerId) {
	    mapper.deleteByHeaderId(headerId);
    }

	@Transactional
    public void remove(String itemGroupCode, String orgId) {
		mapper.remove(itemGroupCode, orgId);
    }
}