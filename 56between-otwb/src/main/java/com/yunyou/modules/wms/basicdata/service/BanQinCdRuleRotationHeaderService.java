package com.yunyou.modules.wms.basicdata.service;

import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdRuleRotationHeader;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdRuleRotationHeaderEntity;
import com.yunyou.modules.wms.basicdata.mapper.BanQinCdRuleRotationHeaderMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
public class BanQinCdRuleRotationHeaderService extends CrudService<BanQinCdRuleRotationHeaderMapper, BanQinCdRuleRotationHeader> {
    @Autowired
    private BanQinCdRuleRotationDetailService cdRuleRotationDetailService;

	public BanQinCdRuleRotationHeader get(String id) {
		return super.get(id);
	}

    public BanQinCdRuleRotationHeaderEntity getEntity(String id) {
        return mapper.getEntity(id);
    }
	
	public List<BanQinCdRuleRotationHeader> findList(BanQinCdRuleRotationHeader banQinCdRuleRotationHeader) {
		return super.findList(banQinCdRuleRotationHeader);
	}
	
	public Page<BanQinCdRuleRotationHeaderEntity> findPage(Page page, BanQinCdRuleRotationHeaderEntity entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
		return page;
	}
	
	@Transactional
	public void save(BanQinCdRuleRotationHeader banQinCdRuleRotationHeader) {
		super.save(banQinCdRuleRotationHeader);
	}

    @Transactional
    public BanQinCdRuleRotationHeader saveEntity(BanQinCdRuleRotationHeader banQinCdRuleRotationHeader) {
        this.save(banQinCdRuleRotationHeader);
        return banQinCdRuleRotationHeader;
    }
	
	@Transactional
	public void delete(BanQinCdRuleRotationHeader banQinCdRuleRotationHeader) {
		super.delete(banQinCdRuleRotationHeader);
        cdRuleRotationDetailService.deleteByHeaderId(banQinCdRuleRotationHeader.getId());
	}
	
	public BanQinCdRuleRotationHeader findFirst(BanQinCdRuleRotationHeader banQinCdRuleRotationHeader) {
        List<BanQinCdRuleRotationHeader> list = this.findList(banQinCdRuleRotationHeader);
        return CollectionUtil.isNotEmpty(list) ? list.get(0) : null;
    } 
	
	public BanQinCdRuleRotationHeader findByRuleCode(String ruleCode, String orgId) {
        BanQinCdRuleRotationHeader model = new BanQinCdRuleRotationHeader();
        model.setRuleCode(ruleCode);
        model.setOrgId(orgId);
        return this.findFirst(model);
    }

    @Transactional
    public void remove(String ruleCode, String orgId) {
	    cdRuleRotationDetailService.remove(ruleCode, orgId);
	    mapper.remove(ruleCode, orgId);
    }
}