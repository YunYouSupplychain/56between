package com.yunyou.modules.oms.basic.service;

import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.oms.basic.entity.OmClerk;
import com.yunyou.modules.oms.basic.mapper.OmClerkMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 业务员Service
 *
 * @author Jianhua Liu
 * @version 2019-07-29
 */
@Service
@Transactional(readOnly = true)
public class OmClerkService extends CrudService<OmClerkMapper, OmClerk> {

    public Page<OmClerk> findPage(Page<OmClerk> page, OmClerk entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    public Page<OmClerk> popData(Page<OmClerk> page, OmClerk entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.popData(entity));
        return page;
    }

    public OmClerk findByCode(String code, String orgId) {
        OmClerk condition = new OmClerk();
        condition.setClerkCode(code);
        condition.setOrgId(orgId);
        List<OmClerk> list = mapper.findList(condition);
        if (CollectionUtil.isNotEmpty(list)) {
            return list.get(0);
        } else {
            return new OmClerk();
        }
    }

    @Transactional
    public void remove(String clerkCode, String orgId) {
        mapper.remove(clerkCode, orgId);
    }
}