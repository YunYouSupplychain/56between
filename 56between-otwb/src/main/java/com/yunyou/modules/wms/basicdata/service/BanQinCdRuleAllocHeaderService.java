package com.yunyou.modules.wms.basicdata.service;

import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdRuleAllocHeader;
import com.yunyou.modules.wms.basicdata.mapper.BanQinCdRuleAllocHeaderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 分配规则Service
 *
 * @author WMJ
 * @version 2019-01-25
 */
@Service
@Transactional(readOnly = true)
public class BanQinCdRuleAllocHeaderService extends CrudService<BanQinCdRuleAllocHeaderMapper, BanQinCdRuleAllocHeader> {
    @Autowired
    private BanQinCdRuleAllocDetailService cdRuleAllocDetailService;

    public Page<BanQinCdRuleAllocHeader> findPage(Page<BanQinCdRuleAllocHeader> page, BanQinCdRuleAllocHeader banQinCdRuleAllocHeader) {
        dataRuleFilter(banQinCdRuleAllocHeader);
        banQinCdRuleAllocHeader.setPage(page);
        page.setList(mapper.findPage(banQinCdRuleAllocHeader));
        return page;
    }

    public BanQinCdRuleAllocHeader findFirst(BanQinCdRuleAllocHeader banQinCdRuleAllocHeader) {
        List<BanQinCdRuleAllocHeader> list = this.findList(banQinCdRuleAllocHeader);
        return CollectionUtil.isNotEmpty(list) ? list.get(0) : null;
    }

    /**
     * 通过RuleCode获取分配规则表头
     */
    public BanQinCdRuleAllocHeader getByRuleCode(String ruleCode, String orgId) {
        BanQinCdRuleAllocHeader example = new BanQinCdRuleAllocHeader();
        example.setRuleCode(ruleCode);
        example.setOrgId(orgId);
        return this.findFirst(example);
    }

    @Transactional
    public void remove(String ruleCode, String orgId) {
        cdRuleAllocDetailService.remove(ruleCode, orgId);
        mapper.remove(ruleCode, orgId);
    }
}