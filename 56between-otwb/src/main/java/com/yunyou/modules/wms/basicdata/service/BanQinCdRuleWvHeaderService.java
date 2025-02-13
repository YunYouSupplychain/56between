package com.yunyou.modules.wms.basicdata.service;

import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdRuleWvHeader;
import com.yunyou.modules.wms.basicdata.mapper.BanQinCdRuleWvHeaderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 波次规则Service
 *
 * @author WMJ
 * @version 2019-01-25
 */
@Service
@Transactional(readOnly = true)
public class BanQinCdRuleWvHeaderService extends CrudService<BanQinCdRuleWvHeaderMapper, BanQinCdRuleWvHeader> {
    @Autowired
    private BanQinCdRuleWvDetailService cdRuleWvDetailService;

    public Page<BanQinCdRuleWvHeader> findPage(Page<BanQinCdRuleWvHeader> page, BanQinCdRuleWvHeader banQinCdRuleWvHeader) {
        dataRuleFilter(banQinCdRuleWvHeader);
        banQinCdRuleWvHeader.setPage(page);
        page.setList(mapper.findPage(banQinCdRuleWvHeader));
        return page;
    }

    @Override
    @Transactional
    public void delete(BanQinCdRuleWvHeader banQinCdRuleWvHeader) {
        cdRuleWvDetailService.deleteByHeaderId(banQinCdRuleWvHeader.getId());
        super.delete(banQinCdRuleWvHeader);
    }

    /**
     * 根据规则代码获取规则记录
     */
    public BanQinCdRuleWvHeader getByCode(String ruleCode, String orgId) {
        BanQinCdRuleWvHeader wvRuleHeader = new BanQinCdRuleWvHeader();
        wvRuleHeader.setRuleCode(ruleCode);
        wvRuleHeader.setOrgId(orgId);
        List<BanQinCdRuleWvHeader> list = this.findList(wvRuleHeader);
        if (CollectionUtil.isNotEmpty(list)) {
            return list.get(0);
        }
        return CollectionUtil.isNotEmpty(list) ? list.get(0) : null;
    }

    @Transactional
    public void remove(String ruleCode, String orgId) {
        cdRuleWvDetailService.remove(ruleCode, orgId);
        mapper.remove(ruleCode, orgId);
    }
}