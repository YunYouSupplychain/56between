package com.yunyou.modules.wms.basicdata.service;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdRuleQcDetail;
import com.yunyou.modules.wms.basicdata.mapper.BanQinCdRuleQcDetailMapper;
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
public class BanQinCdRuleQcDetailService extends CrudService<BanQinCdRuleQcDetailMapper, BanQinCdRuleQcDetail> {
    @Autowired
    private WmsUtil wmsUtil;

	@Override
	@Transactional
	public void save(BanQinCdRuleQcDetail banQinCdRuleQcDetail) {
	    if (StringUtils.isEmpty(banQinCdRuleQcDetail.getId())) {
	        banQinCdRuleQcDetail.setLineNo(wmsUtil.getMaxLineNo("cd_rule_qc_detail", "header_id", banQinCdRuleQcDetail.getHeaderId()));
        }
		super.save(banQinCdRuleQcDetail);
	}
	
	@Transactional
    public void deleteByHeaderId(String headerId) {
	    mapper.deleteByHeaderId(headerId);
    }

    /**
     * 查询明细
     */
    public Collection<BanQinCdRuleQcDetail> getCdRuleQcDetailByRuleCode(String ruleCode, String orgId) {
        BanQinCdRuleQcDetail example = new BanQinCdRuleQcDetail();
        example.setRuleCode(ruleCode);
        example.setOrgId(orgId);
        return this.findList(example);
    }

	@Transactional
    public void remove(String ruleCode, String orgId) {
		mapper.remove(ruleCode, orgId);
    }
}