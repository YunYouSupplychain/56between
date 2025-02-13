package com.yunyou.modules.wms.basicdata.service;

import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdRuleWvGroupDetail;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdRuleWvGroupHeader;
import com.yunyou.modules.wms.basicdata.mapper.BanQinCdRuleWvGroupHeaderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 波次规则组Service
 * @author WMJ
 * @version 2020-02-09
 */
@Service
@Transactional(readOnly = true)
public class BanQinCdRuleWvGroupHeaderService extends CrudService<BanQinCdRuleWvGroupHeaderMapper, BanQinCdRuleWvGroupHeader> {
	@Autowired
	private BanQinCdRuleWvGroupDetailService cdRuleWvGroupDetailService;

	public Page<BanQinCdRuleWvGroupHeader> findPage(Page<BanQinCdRuleWvGroupHeader> page, BanQinCdRuleWvGroupHeader banQinCdRuleWvGroupHeader) {
		dataRuleFilter(banQinCdRuleWvGroupHeader);
		banQinCdRuleWvGroupHeader.setPage(page);
		page.setList(mapper.findPage(banQinCdRuleWvGroupHeader));
		return page;
	}

	@Transactional
	public void deleteEntity(String[] ids) {
		for (String id : ids) {
			BanQinCdRuleWvGroupHeader header = this.get(id);
			if (null != header) {
				this.delete(header);
				List<BanQinCdRuleWvGroupDetail> details = cdRuleWvGroupDetailService.findByGroupCode(header.getGroupCode(), header.getOrgId());
				if (CollectionUtil.isNotEmpty(details)) {
					for (BanQinCdRuleWvGroupDetail detail : details) {
						cdRuleWvGroupDetailService.delete(detail);
					}
				}
			}
		}
	}

	public BanQinCdRuleWvGroupHeader getByGroupCode(String groupCode, String orgId) {
		return mapper.getByGroupCode(groupCode, orgId);
	}

	@Transactional
    public void remove(String groupCode, String orgId) {
		cdRuleWvGroupDetailService.remove(groupCode, orgId);
		mapper.remove(groupCode, orgId);
    }
}