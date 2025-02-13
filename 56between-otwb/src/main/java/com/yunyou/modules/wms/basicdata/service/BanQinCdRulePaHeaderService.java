package com.yunyou.modules.wms.basicdata.service;

import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdRulePaHeader;
import com.yunyou.modules.wms.basicdata.mapper.BanQinCdRulePaHeaderMapper;
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
public class BanQinCdRulePaHeaderService extends CrudService<BanQinCdRulePaHeaderMapper, BanQinCdRulePaHeader> {
    @Autowired
    private BanQinCdRulePaDetailService cdRulePaDetailService;

	public Page<BanQinCdRulePaHeader> findPage(Page<BanQinCdRulePaHeader> page, BanQinCdRulePaHeader banQinCdRulePaHeader) {
        dataRuleFilter(banQinCdRulePaHeader);
        banQinCdRulePaHeader.setPage(page);
        page.setList(mapper.findPage(banQinCdRulePaHeader));
		return page;
	}

    @Override
	@Transactional
	public void delete(BanQinCdRulePaHeader banQinCdRulePaHeader) {
        cdRulePaDetailService.deleteByHeaderId(banQinCdRulePaHeader.getId());
        super.delete(banQinCdRulePaHeader);
	}
	
	public BanQinCdRulePaHeader findFirst(BanQinCdRulePaHeader banQinCdRulePaHeader) {
        List<BanQinCdRulePaHeader> list = this.findList(banQinCdRulePaHeader);
        return CollectionUtil.isNotEmpty(list) ? list.get(0) : null;
    }

    /**
     * 描述： 根据上架规则代码获取上架规则记录
     * @author Jianhua on 2019/1/28
     */
    public BanQinCdRulePaHeader getByRuleCode(String ruleCode, String orgId) {
        BanQinCdRulePaHeader cdRulePaHeader = new BanQinCdRulePaHeader();
        cdRulePaHeader.setRuleCode(ruleCode);
        cdRulePaHeader.setOrgId(orgId);
        return findFirst(cdRulePaHeader);
    }

    @Transactional
    public void remove(String ruleCode, String orgId) {
        cdRulePaDetailService.remove(ruleCode, orgId);
        mapper.remove(ruleCode, orgId);
    }
}