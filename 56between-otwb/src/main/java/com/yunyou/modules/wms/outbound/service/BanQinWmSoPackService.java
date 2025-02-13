package com.yunyou.modules.wms.outbound.service;

import com.yunyou.common.config.GlobalDataRule;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoPack;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoPackEntity;
import com.yunyou.modules.wms.outbound.mapper.BanQinWmSoPackMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 出库单打包记录Service
 * @author WMJ
 * @version 2019-12-16
 */
@Service
@Transactional(readOnly = true)
public class BanQinWmSoPackService extends CrudService<BanQinWmSoPackMapper, BanQinWmSoPack> {

	public BanQinWmSoPack get(String id) {
		return super.get(id);
	}
	
	public List<BanQinWmSoPack> findList(BanQinWmSoPack banQinWmSoPack) {
		return super.findList(banQinWmSoPack);
	}
	
	public Page<BanQinWmSoPackEntity> findPage(Page page, BanQinWmSoPackEntity entity) {
		entity.setDataScope(GlobalDataRule.getDataRuleSql("a.org_id", entity.getOrgId()));
		dataRuleFilter(entity);
		entity.setPage(page);
		page.setList(mapper.findPage(entity));
		return page;
	}
	
	@Transactional
	public void save(BanQinWmSoPack banQinWmSoPack) {
		super.save(banQinWmSoPack);
	}
	
	@Transactional
	public void delete(BanQinWmSoPack banQinWmSoPack) {
		super.delete(banQinWmSoPack);
	}

	public BanQinWmSoPack findLastByAllocId(String allocId, String orgId) {
		BanQinWmSoPack wmSoPack = new BanQinWmSoPack();
		BanQinWmSoPack condition = new BanQinWmSoPack();
		condition.setAllocId(allocId);
		condition.setOrgId(orgId);
		List<BanQinWmSoPack> list = this.findList(condition);
		if (CollectionUtil.isNotEmpty(list)) {
			List<BanQinWmSoPack> collect = list.stream().sorted(Comparator.comparing(BanQinWmSoPack::getCreateDate).reversed()).limit(1).collect(Collectors.toList());
			wmSoPack = collect.get(0);
		}
		return wmSoPack;
	}

	@Transactional
	public void updateTrackingNo(String allocId, String mailNo, String orgId) {
		BanQinWmSoPack wmSoPack = findLastByAllocId(allocId, orgId);
		wmSoPack.setTrackingNo(mailNo);
		this.save(wmSoPack);
	}

	/**
	 * 描述：出库交接清单报表查询
	 *
	 * @author Jianhua on 2020-1-12
	 */
	public Page<BanQinWmSoPackEntity> findOutHandoverPage(Page page, BanQinWmSoPackEntity query) {
		query.setDataScope(GlobalDataRule.getDataRuleSql("a.org_id", query.getOrgId()));
		dataRuleFilter(query);
		query.setPage(page);
		page.setList(mapper.findOutHandoverPage(query));
		return page;
	}

}