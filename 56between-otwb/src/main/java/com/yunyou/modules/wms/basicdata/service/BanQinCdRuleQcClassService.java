package com.yunyou.modules.wms.basicdata.service;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdRuleQcClass;
import com.yunyou.modules.wms.basicdata.mapper.BanQinCdRuleQcClassMapper;
import com.yunyou.modules.wms.common.service.WmsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

/**
 * 质检规则Service
 * @author WMJ
 * @version 2019-01-25
 */
@Service
@Transactional(readOnly = true)
public class BanQinCdRuleQcClassService extends CrudService<BanQinCdRuleQcClassMapper, BanQinCdRuleQcClass> {
    @Autowired
    private WmsUtil wmsUtil;

	@Override
	@Transactional
	public void save(BanQinCdRuleQcClass banQinCdRuleQcClass) {
	    if (StringUtils.isEmpty(banQinCdRuleQcClass.getId())) {
            banQinCdRuleQcClass.setLineNo(wmsUtil.getMaxLineNo("cd_rule_qc_class", "header_id", banQinCdRuleQcClass.getHeaderId()));
        }
        super.save(banQinCdRuleQcClass);
	}

	@Transactional
    public void deleteByHeaderId(String headerId) {
	    mapper.deleteByHeaderId(headerId);
    }

    /**
     * 由规则编码查询
     */
    public Collection<BanQinCdRuleQcClass> getCdRuleQcClassByRuleCode(String ruleCode, String orgId) {
        BanQinCdRuleQcClass example = new BanQinCdRuleQcClass();
        example.setRuleCode(ruleCode);
        example.setOrgId(orgId);
        return this.findList(example);
    }

	@Transactional
    public void remove(String ruleCode, String orgId) {
    	mapper.remove(ruleCode, orgId);
    }
}