package com.yunyou.modules.wms.basicdata.service;

import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdRuleRotationDetail;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdRuleRotationDetailEntity;
import com.yunyou.modules.wms.basicdata.mapper.BanQinCdRuleRotationDetailMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 库存周转规则Service
 * @author WMJ
 * @version 2019-01-25
 */
@Service
@Transactional(readOnly = true)
public class BanQinCdRuleRotationDetailService extends CrudService<BanQinCdRuleRotationDetailMapper, BanQinCdRuleRotationDetail> {

	public Page<BanQinCdRuleRotationDetailEntity> findPage(Page page, BanQinCdRuleRotationDetailEntity entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
	}

    @Transactional
    public void deleteByHeaderId(String headerId) {
	    mapper.deleteByHeaderId(headerId);
    }

    /**
     * 根据周转规则代码，获取周转规则明细
     */
    public List<BanQinCdRuleRotationDetail> getByRuleCode(String ruleCode, String orgId) {
        BanQinCdRuleRotationDetail model = new BanQinCdRuleRotationDetail();
        model.setRuleCode(ruleCode);
        model.setOrgId(orgId);
        return this.findList(model);
    }

	@Transactional
    public void remove(String ruleCode, String orgId) {
    	mapper.remove(ruleCode, orgId);
    }
}