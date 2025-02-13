package com.yunyou.modules.wms.basicdata.service;

import com.yunyou.core.service.CrudService;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdRulePaDetail;
import com.yunyou.modules.wms.basicdata.mapper.BanQinCdRulePaDetailMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 上架规则明细Service
 *
 * @author WMJ
 * @version 2019-01-25
 */
@Service
@Transactional(readOnly = true)
public class BanQinCdRulePaDetailService extends CrudService<BanQinCdRulePaDetailMapper, BanQinCdRulePaDetail> {

    @Transactional
    public void deleteByHeaderId(String headerId) {
        mapper.deleteByHeaderId(headerId);
    }

    /**
     * 描述：根据上架规则代码获取规则明细
     *
     * @author Jianhua on 2019/1/28
     */
    public List<BanQinCdRulePaDetail> getByRuleCode(String ruleCode, String orgId) {
        BanQinCdRulePaDetail rulePaDetail = new BanQinCdRulePaDetail();
        rulePaDetail.setRuleCode(ruleCode);
        rulePaDetail.setOrgId(orgId);
        return findList(rulePaDetail);
    }

    @Transactional
    public void remove(String ruleCode, String orgId) {
        mapper.remove(ruleCode, orgId);
    }
}