package com.yunyou.modules.wms.basicdata.service;

import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdRuleQcHeader;
import com.yunyou.modules.wms.basicdata.mapper.BanQinCdRuleQcHeaderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 质检规则Service
 * @author WMJ
 * @version 2019-01-25
 */
@Service
@Transactional(readOnly = true)
public class BanQinCdRuleQcHeaderService extends CrudService<BanQinCdRuleQcHeaderMapper, BanQinCdRuleQcHeader> {
    @Autowired
    private BanQinCdRuleQcClassService cdRuleQcClassService;
    @Autowired
    private BanQinCdRuleQcDetailService cdRuleQcDetailService;

	public Page<BanQinCdRuleQcHeader> findPage(Page<BanQinCdRuleQcHeader> page, BanQinCdRuleQcHeader banQinCdRuleQcHeader) {
		dataRuleFilter(banQinCdRuleQcHeader);
		banQinCdRuleQcHeader.setPage(page);
		page.setList(mapper.findPage(banQinCdRuleQcHeader));
		return page;
	}

    @Override
	@Transactional
	public void delete(BanQinCdRuleQcHeader banQinCdRuleQcHeader) {
		cdRuleQcClassService.deleteByHeaderId(banQinCdRuleQcHeader.getId());
		cdRuleQcDetailService.deleteByHeaderId(banQinCdRuleQcHeader.getId());
		super.delete(banQinCdRuleQcHeader);
	}
	
	public BanQinCdRuleQcHeader findFirst(BanQinCdRuleQcHeader banQinCdRuleQcHeader) {
        List<BanQinCdRuleQcHeader> list = this.findList(banQinCdRuleQcHeader);
        return CollectionUtil.isNotEmpty(list) ? list.get(0) : null;
    }

    /**
     * 查询质检规则
     */
    public BanQinCdRuleQcHeader getByRuleCode(String ruleCode, String orgId) {
        BanQinCdRuleQcHeader model = new BanQinCdRuleQcHeader();
        model.setRuleCode(ruleCode);
        model.setOrgId(orgId);
        return this.findFirst(model);
    }

    @Transactional
    public void remove(String ruleCode, String orgId) {
    	cdRuleQcClassService.remove(ruleCode, orgId);
    	cdRuleQcDetailService.remove(ruleCode, orgId);
    	mapper.remove(ruleCode, orgId);
    }
}