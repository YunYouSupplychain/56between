package com.yunyou.modules.wms.basicdata.service;

import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdRuleWvDetailWv;
import com.yunyou.modules.wms.basicdata.mapper.BanQinCdRuleWvDetailWvMapper;
import org.apache.commons.lang3.StringEscapeUtils;
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
public class BanQinCdRuleWvDetailWvService extends CrudService<BanQinCdRuleWvDetailWvMapper, BanQinCdRuleWvDetailWv> {

    @Transactional
    public void saveEntity(List<BanQinCdRuleWvDetailWv> list) {
        if (CollectionUtil.isNotEmpty(list)) {
            for (BanQinCdRuleWvDetailWv detailWv : list) {
                detailWv.setOperator(StringEscapeUtils.unescapeHtml3(detailWv.getOperator()));
                this.save(detailWv);
            }
        }
    }

    @Transactional
    public void deleteByHeaderId(String headerId) {
        mapper.deleteByHeaderId(headerId);
    }

    @Transactional
    public BanQinCdRuleWvDetailWv findFirst(BanQinCdRuleWvDetailWv cdRuleWvDetailWv) {
        List<BanQinCdRuleWvDetailWv> list = this.findList(cdRuleWvDetailWv);
        return CollectionUtil.isEmpty(list) ? null : list.get(0);
    }

    /**
     * 根据波次明细规则代码获取波次限制
     */
    public BanQinCdRuleWvDetailWv getByRuleCodeAndLineNo(String ruleCode, String lineNo, String orgId) {
        BanQinCdRuleWvDetailWv wvRuleDetailWv = new BanQinCdRuleWvDetailWv();
        wvRuleDetailWv.setRuleCode(ruleCode);
        wvRuleDetailWv.setLineNo(lineNo);
        wvRuleDetailWv.setOrgId(orgId);
        return this.findFirst(wvRuleDetailWv);
    }

    /**
     * 根据波次明细规则代码,行号和限制条件获取波次限制
     */
    public List<BanQinCdRuleWvDetailWv> getByRuleCodeAndLineNoAndCondition(String ruleCode, String lineNo, String condition, String orgId) {
        BanQinCdRuleWvDetailWv wvRuleDetailWv = new BanQinCdRuleWvDetailWv();
        wvRuleDetailWv.setRuleCode(ruleCode);
        wvRuleDetailWv.setLineNo(lineNo);
        wvRuleDetailWv.setCondition(condition);
        wvRuleDetailWv.setOrgId(orgId);
        return this.findList(wvRuleDetailWv);
    }

    @Transactional
    public void updateLineNo(String headerId, String lineNo) {
        mapper.execUpdateSql("update cd_rule_wv_detail_wv set line_no = '" + lineNo + "' where header_id = '" + headerId + "'");
    }

    @Transactional
    public void remove(String ruleCode, String orgId) {
        mapper.remove(ruleCode, orgId);
    }
}