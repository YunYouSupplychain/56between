package com.yunyou.modules.oms.basic.service;

import com.google.common.collect.Lists;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.oms.basic.entity.OmBusinessServiceScope;
import com.yunyou.modules.oms.basic.entity.OmCarrierServiceScope;
import com.yunyou.modules.oms.basic.entity.extend.OmCarrierServiceScopeEntity;
import com.yunyou.modules.oms.basic.mapper.OmCarrierServiceScopeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 承运商服务范围Service
 *
 * @author Jianhua Liu
 * @version 2019-10-23
 */
@Service
@Transactional(readOnly = true)
public class OmCarrierServiceScopeService extends CrudService<OmCarrierServiceScopeMapper, OmCarrierServiceScope> {
    @Autowired
    private OmBusinessServiceScopeService omBusinessServiceScopeService;

    public Page<OmCarrierServiceScopeEntity> findPage(Page page, OmCarrierServiceScopeEntity entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    public OmCarrierServiceScopeEntity getEntity(String id) {
        return mapper.getEntity(id);
    }

    public List<OmCarrierServiceScope> findByAreaId(String ownerCode, String areaId, String orgId) {
        List<OmCarrierServiceScope> rsList = Lists.newArrayList();

        List<OmBusinessServiceScope> omBusinessServiceScopes = omBusinessServiceScopeService.findByAreaId(areaId, orgId);
        if (CollectionUtil.isNotEmpty(omBusinessServiceScopes)) {
            for (OmBusinessServiceScope scope : omBusinessServiceScopes) {
                List<OmCarrierServiceScope> scopes = this.findByGroup(scope.getGroupCode(), ownerCode, orgId);
                if (CollectionUtil.isNotEmpty(scopes)) {
                    rsList.addAll(scopes);
                }
            }
        }
        return rsList;
    }

    private List<OmCarrierServiceScope> findByGroup(String groupCode, String ownerCode, String orgId) {
        OmCarrierServiceScope scope = new OmCarrierServiceScope();
        scope.setGroupCode(groupCode);
        scope.setOwnerCode(ownerCode);
        scope.setOrgId(orgId);
        return mapper.findList(scope);
    }

    @Transactional
    public void remove(String ownerCode, String carrierCode, String groupCode, String orgId) {
        mapper.remove(ownerCode, carrierCode, groupCode, orgId);
    }
}