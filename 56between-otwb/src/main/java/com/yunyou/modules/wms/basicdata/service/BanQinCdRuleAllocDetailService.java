package com.yunyou.modules.wms.basicdata.service;

import com.yunyou.core.service.CrudService;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdRuleAllocDetail;
import com.yunyou.modules.wms.basicdata.mapper.BanQinCdRuleAllocDetailMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

/**
 * 分配规则Service
 *
 * @author WMJ
 * @version 2019-01-25
 */
@Service
@Transactional(readOnly = true)
public class BanQinCdRuleAllocDetailService extends CrudService<BanQinCdRuleAllocDetailMapper, BanQinCdRuleAllocDetail> {

    @Transactional
    public void deleteByHeaderId(String headerId) {
        mapper.deleteByHeaderId(headerId);
    }

    /**
     * 根据分配规则代码，获取分配明细
     */
    public List<BanQinCdRuleAllocDetail> getByRuleCode(String ruleCode, String orgId) {
        BanQinCdRuleAllocDetail model = new BanQinCdRuleAllocDetail();
        model.setRuleCode(ruleCode);
        model.setOrgId(orgId);
        List<BanQinCdRuleAllocDetail> list = this.findList(model);
        list.sort(Comparator.comparing(BanQinCdRuleAllocDetail::getLineNo));// 按行号升序
        return list;
    }

    @Transactional
    public void remove(String ruleCode, String orgId) {
        mapper.remove(ruleCode, orgId);
    }
}