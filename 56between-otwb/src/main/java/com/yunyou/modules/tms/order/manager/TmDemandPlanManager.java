package com.yunyou.modules.tms.order.manager;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.BaseService;
import com.yunyou.modules.sys.GenNoType;
import com.yunyou.modules.sys.service.SynchronizedNoService;
import com.yunyou.modules.tms.authority.TmAuthorityRule;
import com.yunyou.modules.tms.authority.TmAuthorityTable;
import com.yunyou.modules.tms.order.entity.extend.TmDemandPlanDetailEntity;
import com.yunyou.modules.tms.order.entity.extend.TmDemandPlanEntity;
import com.yunyou.modules.tms.order.manager.mapper.TmDemandPlanMapper;
import com.yunyou.modules.tms.order.service.TmDemandPlanDetailService;
import com.yunyou.modules.tms.order.service.TmDemandPlanHeaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 需求计划业务处理类(事务管理)
 */
@Service
@Transactional(readOnly = true)
public class TmDemandPlanManager extends BaseService {
    @Autowired
    private TmDemandPlanMapper mapper;
    @Autowired
    private TmDemandPlanHeaderService tmDemandPlanHeaderService;
    @Autowired
    private TmDemandPlanDetailService tmDemandPlanDetailService;
    @Autowired
    private TmAuthorityManager tmAuthorityManager;
    @Autowired
    private SynchronizedNoService noService;

    public TmDemandPlanEntity getEntity(String id) {
        TmDemandPlanEntity entity = mapper.getEntity(id);
        if (entity != null) {
            entity.setTmDemandPlanDetailList(mapper.findDetailList(new TmDemandPlanDetailEntity(entity.getPlanOrderNo(), entity.getOrgId())));
        }
        return entity;
    }

    @SuppressWarnings("unchecked")
    public Page<TmDemandPlanEntity> findPage(Page page, TmDemandPlanEntity qEntity) {
        dataRuleFilter(qEntity);
        String authorityScope = TmAuthorityRule.dataRule(TmAuthorityTable.TM_DEMAND_PLAN_HEADER.getValue(), qEntity.getOrgId());
        qEntity.setDataScope(StringUtils.isNotBlank(qEntity.getDataScope()) ? qEntity.getDataScope() + authorityScope : authorityScope);
        qEntity.setPage(page);
        page.setList(mapper.findPage(qEntity));
        return page;
    }

    @Transactional
    public TmDemandPlanEntity saveEntity(TmDemandPlanEntity entity) {
        if (StringUtils.isBlank(entity.getPlanOrderNo())) {
            entity.setPlanOrderNo(noService.getDocumentNo(GenNoType.TM_DEMAND_PLAN_NO.name()));
        }
        tmDemandPlanHeaderService.save(entity);
        // 生成授权数据
        tmAuthorityManager.genAuthorityData(entity);
        return getEntity(entity.getId());
    }

    @Transactional
    public void removeEntity(TmDemandPlanEntity entity) {
        // 删除授权数据
        tmAuthorityManager.remove(TmAuthorityTable.TM_DEMAND_PLAN_HEADER.getValue(), entity.getId());
        // 删除主体信息
        tmDemandPlanHeaderService.delete(entity);
        // 删除明细信息
        tmDemandPlanDetailService.deleteDetail(entity.getPlanOrderNo(), entity.getOrgId(), null);
    }

    public List<TmDemandPlanEntity> findHeaderList(TmDemandPlanEntity entity) {
        return mapper.findHeaderList(entity);
    }

    public List<TmDemandPlanDetailEntity> findDetailList(TmDemandPlanDetailEntity entity) {
        return mapper.findDetailList(entity);
    }

    public List<TmDemandPlanDetailEntity> findDetailByPlanNo(String planNo, String orgId) {
        TmDemandPlanDetailEntity entity = new TmDemandPlanDetailEntity();
        entity.setPlanOrderNo(planNo);
        entity.setOrgId(orgId);
        return this.findDetailList(entity);
    }

    @Transactional
    public void saveDetailList(List<TmDemandPlanDetailEntity> entities) {
        for (TmDemandPlanDetailEntity entity : entities) {
            if (entity.getId() == null) {
                continue;
            }
            tmDemandPlanDetailService.save(entity);
        }
    }

    @Transactional
    public void removeDetail(String detailId) {
        // 删除明细信息
        tmDemandPlanDetailService.delete(new TmDemandPlanDetailEntity(detailId));
    }

}