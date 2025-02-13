package com.yunyou.modules.wms.basicdata.service;

import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.wms.basicdata.entity.BanQinWmCarrierTypeRelation;
import com.yunyou.modules.wms.basicdata.entity.BanQinWmCarrierTypeRelationEntity;
import com.yunyou.modules.wms.basicdata.mapper.BanQinWmCarrierTypeRelationMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 承运商类型关系Service
 * @author zyf
 * @version 2020-01-07
 */
@Service
@Transactional(readOnly = true)
public class BanQinWmCarrierTypeRelationService extends CrudService<BanQinWmCarrierTypeRelationMapper, BanQinWmCarrierTypeRelation> {

	public BanQinWmCarrierTypeRelationEntity getEntity(String id) {
		return mapper.getEntity(id);
	}

	@SuppressWarnings("unchecked")
	public Page<BanQinWmCarrierTypeRelationEntity> findPage(Page page, BanQinWmCarrierTypeRelationEntity entity) {
		dataRuleFilter(entity);
		entity.setPage(page);
		page.setList(mapper.findPage(entity));
		return page;
	}

	public BanQinWmCarrierTypeRelationEntity getByCarrierCode(String carrierCode, String orgId) {
		BanQinWmCarrierTypeRelationEntity condition = new BanQinWmCarrierTypeRelationEntity();
		condition.setCarrierCode(carrierCode);
		condition.setOrgId(orgId);
		List<BanQinWmCarrierTypeRelationEntity> list = mapper.findEntity(condition);
		return CollectionUtil.isNotEmpty(list) ? list.get(0) : null;
	}

	@Transactional
    public void remove(String carrierCode, String orgId) {
		mapper.remove(carrierCode, orgId);
    }
}