package com.yunyou.modules.wms.inventory.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.sys.GenNoType;
import com.yunyou.modules.sys.service.SynchronizedNoService;
import com.yunyou.modules.wms.inventory.entity.BanQinWmCountHeader;
import com.yunyou.modules.wms.inventory.entity.BanQinWmTaskCount;
import com.yunyou.modules.wms.inventory.entity.BanQinWmTaskCountEntity;
import com.yunyou.modules.wms.inventory.mapper.BanQinWmTaskCountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 盘点任务Service
 * @author WMJ
 * @version 2019-01-28
 */
@Service
@Transactional(readOnly = true)
public class BanQinWmTaskCountService extends CrudService<BanQinWmTaskCountMapper, BanQinWmTaskCount> {
    @Autowired
    private SynchronizedNoService noService;
    @Autowired
	@Lazy
	private BanQinWmCountHeaderService wmCountHeaderService;

	public BanQinWmTaskCountEntity getEntity(String id) {
		return mapper.getEntity(id);
	}

	public Page<BanQinWmTaskCountEntity> findPage(Page page, BanQinWmTaskCountEntity banQinWmTaskCountEntity) {
		dataRuleFilter(banQinWmTaskCountEntity);
		banQinWmTaskCountEntity.setPage(page);
		page.setList(mapper.findPage(banQinWmTaskCountEntity));
		return page;
	}
	
	@Transactional
	public void saveEntity(BanQinWmTaskCount banQinWmTaskCount) {
		if (StringUtils.isEmpty(banQinWmTaskCount.getId())) {
			banQinWmTaskCount.setCountId(noService.getDocumentNo(GenNoType.WM_COUNT_ID.name()));
		}
		this.save(banQinWmTaskCount);
	}

	@Transactional
	public ResultMessage removeTaskById(String[] ids, String headerId) {
		ResultMessage msg = new ResultMessage();
		BanQinWmCountHeader countHeader = wmCountHeaderService.get(headerId);
		for (String id : ids) {
			delete(new BanQinWmTaskCount(id));
		}
		wmCountHeaderService.updateCountStatus(countHeader.getCountNo(), countHeader.getOrgId());
		msg.setSuccess(true);
		return msg;

	}

    public List<BanQinWmTaskCountEntity> rfTCGetTaskCountDetailQuery(String zoneCode, String lane, String locCode, String countNo, String orgId) {
		return mapper.rfTCGetTaskCountDetailQuery(zoneCode, lane, locCode, countNo, orgId);
	}

	public BanQinWmTaskCountEntity getEntityByCountId(String countId, String orgId) {
		BanQinWmTaskCountEntity entity = new BanQinWmTaskCountEntity();
		entity.setCountId(countId);
		entity.setOrgId(orgId);
		List<BanQinWmTaskCountEntity> list = mapper.findPage(entity);
		return CollectionUtil.isNotEmpty(list) ? list.get(0) : null;
	}

	public List<BanQinWmTaskCount> getByCountNo(String countNo, String orgId) {
		BanQinWmTaskCount entity = new BanQinWmTaskCount();
		entity.setCountNo(countNo);
		entity.setOrgId(orgId);
		return mapper.findList(entity);
	}

	public BanQinWmTaskCount getTask(String countNo, String ownerCode, String skuCode, String lotNum, String locCode, String traceId, String orgId) {
		BanQinWmTaskCount condition = new BanQinWmTaskCount();
		condition.setCountNo(countNo);
		condition.setOwnerCode(ownerCode);
		condition.setSkuCode(skuCode);
		condition.setLotNum(lotNum);
		condition.setLocCode(locCode);
		condition.setTraceId(traceId);
		condition.setOrgId(orgId);
		List<BanQinWmTaskCount> list = this.findList(condition);
		return CollectionUtil.isNotEmpty(list) ? list.get(0) : null;
	}
}