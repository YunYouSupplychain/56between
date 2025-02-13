package com.yunyou.modules.wms.basicdata.service;

import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdRuleWvDetail;
import com.yunyou.modules.wms.basicdata.mapper.BanQinCdRuleWvDetailMapper;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 波次规则Service
 * @author WMJ
 * @version 2019-01-25
 */
@Service
@Transactional(readOnly = true)
public class BanQinCdRuleWvDetailService extends CrudService<BanQinCdRuleWvDetailMapper, BanQinCdRuleWvDetail> {
    @Autowired
    private BanQinCdRuleWvDetailWvService cdRuleWvDetailWvService;
    @Autowired
    private BanQinCdRuleWvDetailOrderService cdRuleWvDetailOrderService;

	@Transactional
    public void saveEntity(BanQinCdRuleWvDetail banQinCdRuleWvDetail) {
        banQinCdRuleWvDetail.setSql(StringEscapeUtils.unescapeHtml3(banQinCdRuleWvDetail.getSql()));
        this.save(banQinCdRuleWvDetail);
        cdRuleWvDetailWvService.updateLineNo(banQinCdRuleWvDetail.getId(), banQinCdRuleWvDetail.getLineNo());
        cdRuleWvDetailOrderService.updateLineNo(banQinCdRuleWvDetail.getId(), banQinCdRuleWvDetail.getLineNo());
    }
	
	@Transactional
	public void delete(BanQinCdRuleWvDetail banQinCdRuleWvDetail) {
        cdRuleWvDetailWvService.deleteByHeaderId(banQinCdRuleWvDetail.getId());
        cdRuleWvDetailOrderService.deleteByHeaderId(banQinCdRuleWvDetail.getId());
        super.delete(banQinCdRuleWvDetail);
	}
	
	@Transactional
    public void deleteByHeaderId(String headerId) {
        List<BanQinCdRuleWvDetail> list = mapper.getByHeaderId(headerId);
        for (BanQinCdRuleWvDetail detail : list) {
            cdRuleWvDetailWvService.deleteByHeaderId(detail.getId());
            cdRuleWvDetailOrderService.deleteByHeaderId(detail.getId());
        }
        mapper.deleteByHeaderId(headerId);
    }
	
	public BanQinCdRuleWvDetail findFirst(BanQinCdRuleWvDetail banQinCdRuleWvDetail) {
        List<BanQinCdRuleWvDetail> list = this.findList(banQinCdRuleWvDetail);
        return CollectionUtil.isNotEmpty(list) ? list.get(0) : null;
    }

    /**
     * 根据规则编码获取波次明细行记录
     *
     * @return List<CdRuleWvDetailModel>
     */
    public List<BanQinCdRuleWvDetail> getByRuleCodeAndIsEnable(String ruleCode, String orgId) {
        BanQinCdRuleWvDetail wvRuleDetail = new BanQinCdRuleWvDetail();
        wvRuleDetail.setRuleCode(ruleCode);
        wvRuleDetail.setIsEnable("Y");
        wvRuleDetail.setOrgId(orgId);
        return this.findList(wvRuleDetail);
    }

    /**
     * 根据规则编码获取波次明细行记录
     */
    public List<BanQinCdRuleWvDetail> getByRuleCode(String ruleCode, String orgId) {
        BanQinCdRuleWvDetail model = new BanQinCdRuleWvDetail();
        model.setRuleCode(ruleCode);
        model.setOrgId(orgId);
        return this.findList(model);
    }

    /**
     * 根据ruleCode和LineNo获取波次规则明细
     */
    public BanQinCdRuleWvDetail getByRuleCodeAndLineNo(String ruleCode, String lineNo, String orgId) {
        BanQinCdRuleWvDetail example = new BanQinCdRuleWvDetail();
        example.setRuleCode(ruleCode);
        example.setLineNo(lineNo);
        example.setOrgId(orgId);
        return this.findFirst(example);
    }

    @Transactional
    public void updateSQL(String headerId, String sql) {
        mapper.execUpdateSql("update cd_rule_wv_detail set `sql` = \"" + sql + "\" where id = '" + headerId + "'");
    }

    @Transactional
    public void remove(String ruleCode, String orgId) {
        cdRuleWvDetailWvService.remove(ruleCode, orgId);
        cdRuleWvDetailOrderService.remove(ruleCode, orgId);
        mapper.remove(ruleCode, orgId);
    }
}