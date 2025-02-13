package com.yunyou.modules.wms.basicdata.service;

import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdRulePreallocHeader;
import com.yunyou.modules.wms.basicdata.mapper.BanQinCdRulePreallocHeaderMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 预配规则Service
 * @author WMJ
 * @version 2019-01-25
 */
@Service
@Transactional(readOnly = true)
public class BanQinCdRulePreallocHeaderService extends CrudService<BanQinCdRulePreallocHeaderMapper, BanQinCdRulePreallocHeader> {

	public BanQinCdRulePreallocHeader get(String id) {
		return super.get(id);
	}
	
	public List<BanQinCdRulePreallocHeader> findList(BanQinCdRulePreallocHeader banQinCdRulePreallocHeader) {
		return super.findList(banQinCdRulePreallocHeader);
	}
	
	public Page<BanQinCdRulePreallocHeader> findPage(Page<BanQinCdRulePreallocHeader> page, BanQinCdRulePreallocHeader banQinCdRulePreallocHeader) {
		dataRuleFilter(banQinCdRulePreallocHeader);
		banQinCdRulePreallocHeader.setPage(page);
		page.setList(mapper.findPage(banQinCdRulePreallocHeader));
		return page;
	}
	
	@Transactional
	public void save(BanQinCdRulePreallocHeader banQinCdRulePreallocHeader) {
		super.save(banQinCdRulePreallocHeader);
	}
	
	@Transactional
	public void delete(BanQinCdRulePreallocHeader banQinCdRulePreallocHeader) {
		super.delete(banQinCdRulePreallocHeader);
	}
	
	public BanQinCdRulePreallocHeader findFirst(BanQinCdRulePreallocHeader banQinCdRulePreallocHeader) {
        List<BanQinCdRulePreallocHeader> list = this.findList(banQinCdRulePreallocHeader);
        return CollectionUtil.isNotEmpty(list) ? list.get(0) : null;
    }

}