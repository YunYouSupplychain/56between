package com.yunyou.modules.wms.basicdata.service;

import com.yunyou.core.service.CrudService;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdRuleWvGroupDetail;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdRuleWvGroupDetailEntity;
import com.yunyou.modules.wms.basicdata.mapper.BanQinCdRuleWvGroupDetailMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 波次规则组明细Service
 * @author WMJ
 * @version 2020-02-09
 */
@Service
@Transactional(readOnly = true)
public class BanQinCdRuleWvGroupDetailService extends CrudService<BanQinCdRuleWvGroupDetailMapper, BanQinCdRuleWvGroupDetail> {

	public List<BanQinCdRuleWvGroupDetailEntity> findGrid(BanQinCdRuleWvGroupDetail banQinCdRuleWvGroupDetail) {
		return mapper.findGrid(banQinCdRuleWvGroupDetail);
	}

	@Transactional
	public void saveEntity(List<BanQinCdRuleWvGroupDetail> list) {
		for (BanQinCdRuleWvGroupDetail detail : list) {
			this.save(detail);
		}
	}

	public List<BanQinCdRuleWvGroupDetail> findByGroupCode(String groupCode, String orgId) {
		BanQinCdRuleWvGroupDetail condition = new BanQinCdRuleWvGroupDetail();
		condition.setGroupCode(groupCode);
		condition.setOrgId(orgId);
		return this.findList(condition);
	}

	@Transactional
    public void remove(String groupCode, String orgId) {
		mapper.remove(groupCode, orgId);
    }
}