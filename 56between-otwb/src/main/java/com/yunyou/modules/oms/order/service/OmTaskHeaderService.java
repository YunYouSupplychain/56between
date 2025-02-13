package com.yunyou.modules.oms.order.service;

import com.yunyou.common.config.GlobalDataRule;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.oms.basic.entity.OmCarrierServiceScope;
import com.yunyou.modules.oms.basic.entity.OmItem;
import com.yunyou.modules.oms.basic.service.OmCarrierServiceScopeService;
import com.yunyou.modules.oms.basic.service.OmItemService;
import com.yunyou.modules.oms.common.OmsConstants;
import com.yunyou.modules.oms.common.OmsException;
import com.yunyou.modules.oms.order.entity.OmTaskDetail;
import com.yunyou.modules.oms.order.entity.OmTaskHeader;
import com.yunyou.modules.oms.order.entity.OmTaskHeaderEntity;
import com.yunyou.modules.oms.order.mapper.OmTaskHeaderMapper;
import com.yunyou.modules.sys.SysParamConstants;
import com.yunyou.modules.sys.utils.SysControlParamsUtils;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * 供应链作业任务Service
 *
 * @author WMJ
 * @version 2019-04-21
 */
@Service
@Transactional(readOnly = true)
public class OmTaskHeaderService extends CrudService<OmTaskHeaderMapper, OmTaskHeader> {
    @Autowired
    private OmTaskDetailService omTaskDetailService;
    @Autowired
    private OmChainHeaderService omChainHeaderService;
    @Autowired
    private OmCarrierServiceScopeService omCarrierServiceScopeService;
    @Autowired
    private OmItemService omItemService;

    /**
     * 描述：保存任务及明细
     */
    @Override
    @Transactional
    public void save(OmTaskHeader omTaskHeader) {
        if (StringUtils.isBlank(omTaskHeader.getHandleStatus())) {
            omTaskHeader.setHandleStatus(OmsConstants.HANDLE_STATUS_N);
        }
        super.save(omTaskHeader);
        for (OmTaskDetail omTaskDetail : omTaskHeader.getOmTaskDetailList()) {
            if (omTaskDetail.getId() == null) {
                continue;
            }
            omTaskDetail.setHeaderId(omTaskHeader.getId());
            omTaskDetail.setOrgId(omTaskHeader.getOrgId());
            omTaskDetailService.save(omTaskDetail);
        }
    }

    /**
     * 描述：删除任务及明细
     */
    @Override
    @Transactional
    public void delete(OmTaskHeader omTaskHeader) {
        omTaskDetailService.delete(new OmTaskDetail(null, omTaskHeader.getId()));
        mapper.delete(omTaskHeader);
    }

    /**
     * 描述：更新作业任务关联信息
     */
    @Transactional
    public void updateAssociatedTask(String taskId, String associatedTaskId, String delFlag) {
        mapper.updateAssociatedTask(taskId, associatedTaskId, delFlag);
    }

    /**
     * 描述：承运商分配
     * <p>
     * create by Jianhua on 2019/10/25
     */
    @Transactional
    public void carrierAlloc(String id) {
        OmTaskHeaderEntity entity = this.getEntity(id);
        if (!(OmsConstants.OMS_TASK_TYPE_01.equals(entity.getTaskType())
            || OmsConstants.OMS_TASK_TYPE_03.equals(entity.getTaskType()))) {
            throw new OmsException("[" + entity.getTaskNo() + "]不是出库或运输任务，无法操作");
        }
        if (OmsConstants.OMS_TASK_STATUS_40.equals(entity.getStatus())
            || OmsConstants.OMS_TASK_STATUS_90.equals(entity.getStatus())) {
            throw new OmsException("[" + entity.getTaskNo() + "]任务已下发或取消，无法操作");
        }
        if (!OmsConstants.TASK_SOURCE_RO.equals(entity.getTaskSource())) {
            if (omChainHeaderService.isIntercepted(entity.getChainNo(), entity.getOrgId())) {
                throw new OmsException("[" + entity.getTaskNo() + "]任务订单已拦截，无法操作");
            }
        }
        String carrier = null;
        // 统计当前任务的总重量、总体积、总费用
        BigDecimal totalWeight = BigDecimal.ZERO, totalVolume = BigDecimal.ZERO, totalCost = entity.getFreightCharge() == null ? BigDecimal.ZERO : entity.getFreightCharge();

        for (OmTaskDetail omTaskDetail : entity.getOmTaskDetailList()) {
            OmItem omItem = omItemService.getByOwnerAndSku(entity.getOwner(), omTaskDetail.getSkuCode(), entity.getOrgId());
            BigDecimal weight = omItem.getGrossWeight() == null ? BigDecimal.ZERO : BigDecimal.valueOf(omItem.getGrossWeight());
            BigDecimal volume = omItem.getVolume() == null ? BigDecimal.ZERO : BigDecimal.valueOf(omItem.getVolume());
            totalWeight = totalWeight.add(weight.multiply(omTaskDetail.getQty()));
            totalVolume = totalVolume.add(volume.multiply(omTaskDetail.getQty()));
        }
        // 根据收货人区域查找含此区域服务范围的承运商
        List<OmCarrierServiceScope> omCarrierServiceScopes = omCarrierServiceScopeService.findByAreaId(entity.getOwner(), entity.getConsigneeArea(), entity.getOrgId());
        if (CollectionUtil.isNotEmpty(omCarrierServiceScopes)) {
            List<OmCarrierServiceScope> suitableList = Lists.newArrayList();
            for (OmCarrierServiceScope omCarrierServiceScope : omCarrierServiceScopes) {
                double maxWeight = omCarrierServiceScope.getMaxWeight() == null ? Double.MAX_VALUE : omCarrierServiceScope.getMaxWeight();
                double maxVolume = omCarrierServiceScope.getMaxVolume() == null ? Double.MAX_VALUE : omCarrierServiceScope.getMaxVolume();
                double maxCost = omCarrierServiceScope.getMaxCost() == null ? Double.MAX_VALUE : omCarrierServiceScope.getMaxCost();
                if (totalCost.doubleValue() > maxCost) {
                    continue;
                }
                if (totalWeight.doubleValue() > maxWeight) {
                    continue;
                }
                if (totalVolume.doubleValue() > maxVolume) {
                    continue;
                }
                suitableList.add(omCarrierServiceScope);
            }
            if (CollectionUtil.isNotEmpty(suitableList)) {
                // ①费用、②重量、③体积
                Optional<OmCarrierServiceScope> optional = suitableList.stream().min(Comparator.comparing(OmCarrierServiceScope::getMaxCost, Comparator.nullsLast(Double::compareTo))
                    .thenComparing(OmCarrierServiceScope::getMaxWeight, Comparator.nullsLast(Double::compareTo))
                    .thenComparing(OmCarrierServiceScope::getMaxVolume, Comparator.nullsLast(Double::compareTo)));
                if (optional.isPresent()) {
                    carrier = optional.get().getCarrierCode();
                }
            }
        }
        if (StringUtils.isNotBlank(carrier)) {
            entity.setCarrier(carrier);
            this.save(entity);
        }
    }

    /**
     * 描述：指定承运商
     *
     * @author Jianhua on 2020-1-16
     */
    @Transactional
    public void carrierDesignate(String id, String carrier) {
        if (StringUtils.isBlank(carrier)) {
            return;
        }
        OmTaskHeaderEntity entity = this.getEntity(id);
        if (!(OmsConstants.OMS_TASK_TYPE_01.equals(entity.getTaskType())
            || OmsConstants.OMS_TASK_TYPE_03.equals(entity.getTaskType()))) {
            throw new OmsException("[" + entity.getTaskNo() + "]不是出库或运输任务，无法操作");
        }
        if (OmsConstants.OMS_TASK_STATUS_40.equals(entity.getStatus())
            || OmsConstants.OMS_TASK_STATUS_90.equals(entity.getStatus())) {
            throw new OmsException("[" + entity.getTaskNo() + "]任务已下发或取消，无法操作");
        }
        if (omChainHeaderService.isIntercepted(entity.getChainNo(), entity.getOrgId())) {
            throw new OmsException("[" + entity.getTaskNo() + "]任务订单已拦截，无法操作");
        }

        entity.setCarrier(carrier);
        this.save(entity);
    }

    /**
     * 描述：查询任务分页列表数据
     */
    @SuppressWarnings("unchekced")
    public Page<OmTaskHeaderEntity> findPage(Page page, OmTaskHeaderEntity entity) {
        entity.setDataScope(GlobalDataRule.getDataRuleSql("a.org_id", entity.getOrgId()));
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    /**
     * 描述：查询任务Entity及明细
     */
    public OmTaskHeaderEntity getEntity(String id) {
        OmTaskHeaderEntity entity = mapper.getEntity(id);
        if (entity != null) {
            entity.setOmTaskDetailList(omTaskDetailService.findList(new OmTaskDetail(null, id)));
        }
        return entity;
    }

    /**
     * 描述：根据供应链订单模糊查询（传进去的单号为完整单号）
     */
    public List<OmTaskHeader> getByChainNo(String chainNo, String orgId) {
        return mapper.getByChainNo(chainNo, orgId);
    }

    /**
     * 描述：查询指定任务类型指定批次的任务
     */
    public List<OmTaskHeader> getBatchTask(String taskType, List<String> lotNums, String orgId) {
        return mapper.getBatchTask(taskType, lotNums, orgId);
    }

    /**
     * 描述：查询合并后关联的任务
     */
    public List<OmTaskHeader> getByAssociatedTaskId(String id) {
        return mapper.getByAssociatedTaskId(id);
    }

    /**
     * 获取同一来源订单的最新批次号
     */
    public String getMaxLotNumBySourceNo(String sourceNo, String orgId) {
        String lotNum = mapper.getMaxLotNumBySourceNo(sourceNo, orgId);
        if (StringUtils.isNotBlank(lotNum) && lotNum.length() > 2) {
            int serialNum = Integer.parseInt(lotNum.substring(lotNum.length() - 2));
            lotNum = sourceNo + String.format("%02d", serialNum + 1);
        } else {
            lotNum = sourceNo + "01";
        }
        return lotNum;
    }

    /**
     * 描述：判断下发的SO单是否拦截成功
     *
     * @author Jianhua on 2020-2-13
     */
    public boolean isPushSoOrderInterceptionSuccess(String chainNo, String taskNo, String orgId) {
        String getSql = "select intercept_status from wm_so_header where def2 = '" + chainNo + "' and def3 = '" + taskNo + "' and org_id = '" + orgId + "'";
        List<Object> objects = mapper.execSelectSql(getSql);
        return CollectionUtil.isEmpty(objects) || "99".equals(objects.get(0).toString());
    }

    /**
     * 描述：根据供应链订单查询提供给定时器操作数据
     */
    public List<String> findTaskIdByChainIdForTimer(String chainNo, String orgId) {
        return mapper.findTaskIdByChainIdForTimer(chainNo, orgId);
    }

    /**
     * 描述：查询提供给任务下发定时器操作数据
     *
     * @author Jianhua on 2020-2-12
     */
    public List<String> findCanPushTaskIdForTimer() {
        List<String> rsList = Lists.newArrayList();
        // 入库任务
        List<String> inTaskIds = mapper.findCanPushTaskIdForTimer(OmsConstants.OMS_TASK_STATUS_20, OmsConstants.OMS_TASK_TYPE_02);
        if (CollectionUtil.isNotEmpty(inTaskIds)) {
            rsList.addAll(inTaskIds);
        }
        // 出库任务
        List<String> outTaskIds = mapper.findCanPushTaskIdForTimer(OmsConstants.OMS_TASK_STATUS_30, OmsConstants.OMS_TASK_TYPE_01);
        if (CollectionUtil.isNotEmpty(outTaskIds)) {
            rsList.addAll(outTaskIds);
        }
        // 如果任务下发时同时下发同批次的运输任务，则不获取运输任务；
        final String isSendBatchTransTask = SysControlParamsUtils.getValue(SysParamConstants.IS_SEND_BATCH_TRANS_TASK, null);
        if (!OmsConstants.OMS_Y.equals(isSendBatchTransTask)) {
            List<String> tranTaskIds = mapper.findCanPushTaskIdForTimer(OmsConstants.OMS_TASK_STATUS_20, OmsConstants.OMS_TASK_TYPE_03);
            if (CollectionUtil.isNotEmpty(tranTaskIds)) {
                rsList.addAll(tranTaskIds);
            }
        }
        return rsList;
    }

    public OmTaskHeader getByTaskNoAndWarehouse(String taskNo, String warehouse) {
        return mapper.getByTaskNoAndWarehouse(taskNo, warehouse);
    }

}