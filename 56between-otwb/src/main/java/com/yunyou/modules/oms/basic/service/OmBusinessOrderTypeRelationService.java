package com.yunyou.modules.oms.basic.service;

import com.yunyou.core.service.CrudService;
import com.yunyou.modules.oms.basic.entity.OmBusinessOrderTypeRelation;
import com.yunyou.modules.oms.basic.mapper.OmBusinessOrderTypeRelationMapper;
import com.yunyou.modules.oms.common.OmsConstants;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 业务类型-订单类型关联关系Service
 *
 * @author zyf
 * @version 2019-07-03
 */
@Service
@Transactional(readOnly = true)
public class OmBusinessOrderTypeRelationService extends CrudService<OmBusinessOrderTypeRelationMapper, OmBusinessOrderTypeRelation> {

    public List<OmBusinessOrderTypeRelation> getByBusinessOrderType(String businessOrderType, String orgId) {
        OmBusinessOrderTypeRelation qEntity = new OmBusinessOrderTypeRelation();
        qEntity.setBusinessOrderType(businessOrderType);
        qEntity.setOrgId(orgId);
        return findList(qEntity);
    }

    public boolean isOnlyTransport(String businessOrderType, String orgId) {
        List<OmBusinessOrderTypeRelation> relations = this.getByBusinessOrderType(businessOrderType, orgId);
        return relations.stream().allMatch(o -> OmsConstants.OMS_TASK_TYPE_03.equals(o.getOrderType()));
    }

    @Transactional
    public void remove(String businessOrderType, String taskType, String pushSystem, String orgId) {
        mapper.remove(businessOrderType, taskType, pushSystem, orgId);
    }
}