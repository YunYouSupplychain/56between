package com.yunyou.modules.tms.order.service;

import com.yunyou.core.service.CrudService;
import com.yunyou.modules.tms.order.entity.TmDemandPlanDetail;
import com.yunyou.modules.tms.order.mapper.TmDemandPlanDetailMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 需求计划明细Service
 * @author WMJ
 * @version 2020-07-07
 */
@Service
@Transactional(readOnly = true)
public class TmDemandPlanDetailService extends CrudService<TmDemandPlanDetailMapper, TmDemandPlanDetail> {

    @Transactional
    public void deleteDetail(String planOrderNo, String orgId, String skuCode) {
        mapper.deleteDetail(planOrderNo, orgId, skuCode);
    }

}