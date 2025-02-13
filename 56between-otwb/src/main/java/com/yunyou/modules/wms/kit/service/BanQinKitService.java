package com.yunyou.modules.wms.kit.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.modules.wms.common.entity.WarehouseException;
import com.yunyou.modules.wms.common.entity.WmsCodeMaster;
import com.yunyou.modules.wms.common.service.WmsUtil;
import com.yunyou.modules.wms.inbound.service.BanQinInboundPaOperationService;
import com.yunyou.modules.wms.kit.entity.BanQinCdWhBomDetail;
import com.yunyou.modules.wms.kit.entity.BanQinCdWhBomHeader;
import com.yunyou.modules.wms.kit.entity.BanQinCdWhBomStep;
import com.yunyou.modules.wms.kit.entity.BanQinWmKitHeader;
import com.yunyou.modules.wms.kit.entity.extend.*;
import com.yunyou.modules.wms.outbound.entity.BanQinWmDelAllocEntity;
import com.yunyou.modules.wms.outbound.service.BanQinWmDelAllocService;
import com.yunyou.modules.wms.task.entity.BanQinWmTaskPaEntity;
import com.yunyou.modules.wms.task.service.BanQinWmTaskPaService;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 描述：加工管理- 前后交互方法接口类
 * <p>
 * create by Jianhua on 2019/8/25
 */
@Service
public class BanQinKitService {
    @Autowired
    protected BanQinCdWhBomHeaderService banQinCdWhBomHeaderService;
    @Autowired
    protected BanQinCdWhBomDetailService banQinCdWhBomDetailService;
    @Autowired
    protected BanQinCdWhBomStepService banQinCdWhBomStepService;
    @Autowired
    protected BanQinKitRemoveService banQinKitRemoveService;
    @Autowired
    protected BanQinWmKitHeaderService banQinWmKitHeaderService;
    @Autowired
    protected BanQinWmKitParentDetailService banQinWmKitParentDetailService;
    @Autowired
    protected BanQinWmKitSubDetailService banQinWmKitSubDetailService;
    @Autowired
    protected BanQinWmTaskKitService banQinWmTaskKitService;
    @Autowired
    protected BanQinWmKitResultDetailService banQinWmKitResultDetailService;
    @Autowired
    protected BanQinWmKitStepService banQinWmKitStepService;
    @Autowired
    protected BanQinKitDuplicateService banQinKitDuplicateService;
    @Autowired
    protected BanQinKitCloseOrCancelService banQinKitCloseOrCancelService;
    @Autowired
    protected BanQinKitGenerateSubService banQinKitGenerateSubService;
    @Autowired
    protected BanQinKitManualAllocService banQinKitManualAllocService;
    @Autowired
    protected BanQinKitBatchAllocService banQinKitBatchAllocService;
    @Autowired
    protected BanQinKitBatchCancelAllocService banQinKitBatchCancelAllocService;
    @Autowired
    protected BanQinKitBatchPickingService banQinKitBatchPickingService;
    @Autowired
    protected BanQinKitBatchCancelPickingService banQinKitBatchCancelPickingService;
    @Autowired
    protected BanQinKitBatchConfirmService banQinKitBatchConfirmService;
    @Autowired
    protected BanQinKitCancelConfirmService banQinKitCancelConfirmService;
    @Autowired
    protected BanQinWmDelAllocService banQinWmDelAllocService;
    @Autowired
    protected BanQinKitBatchCreateTaskPaService banQinKitBatchCreateTaskPaService;
    @Autowired
    protected BanQinKitRemoveTaskPaService banQinKitRemoveTaskPaService;
    @Autowired
    protected BanQinInboundPaOperationService banQinInboundPaOperationService;
    @Autowired
    protected BanQinWmTaskPaService banQinWmTaskPaService;
    @Autowired
    protected WmsUtil wmsUtil;

    /**
     * 描述：根据货主、组合件编码、加工类型获取bom entity
     * <p>
     * create by Jianhua on 2019/8/25
     */
    public BanQinCdWhBomEntity getBomEntity(String ownerCode, String parentSkuCode, String kitType, String orgId) {
        // 组合件
        BanQinCdWhBomEntity entity = banQinCdWhBomHeaderService.getEntityByOwnerAndParentSkuAndKitType(ownerCode, parentSkuCode, kitType, orgId);
        // 组合件明细
        List<BanQinCdWhBomDetailEntity> bomDetailEntities = banQinCdWhBomDetailService.getEntityByOwnerAndParentSkuAndKitType(ownerCode, parentSkuCode, kitType, orgId);
        // 组合件加工工序
        List<BanQinCdWhBomStep> bomStepModels = banQinCdWhBomStepService.getByOwnerAndParentSkuAndKitType(ownerCode, parentSkuCode, kitType, orgId);
        // 设置对象
        entity.setBomDetailEntitys(bomDetailEntities);
        entity.setBomStepModels(bomStepModels);
        return entity;
    }

    /**
     * 描述：保存组合件
     * <p>
     * create by Jianhua on 2019/8/25
     */
    @Transactional
    public ResultMessage saveBomHeaderEntity(BanQinCdWhBomEntity entity) {
        ResultMessage msg = new ResultMessage();
        banQinCdWhBomHeaderService.checkSaveBefore(entity);
        banQinCdWhBomHeaderService.saveBomHeader(entity);

        msg.setSuccess(true);
        msg.addMessage("操作成功");
        msg.setData(entity);
        return msg;
    }

    /**
     * 描述：删除组合件，包含明细表删除
     * <p>
     * create by Jianhua on 2019/8/25
     */
    @Transactional
    public void removeBomEntity(String[] ids) {
        for (String id : ids) {
            BanQinCdWhBomHeader entity = banQinCdWhBomHeaderService.get(id);
            banQinKitRemoveService.removeBomEntity(entity.getOwnerCode(), entity.getParentSkuCode(), entity.getKitType(), entity.getOrgId());
        }
    }

    /**
     * 描述：根据货主、组合件编码、加工类型、行号获取bom detail entity
     * <p>
     * create by Jianhua on 2019/8/25
     */
    public BanQinCdWhBomDetailEntity getBomDetailEntity(String ownerCode, String parentSkuCode, String kitType, String lineNo, String orgId) {
        return banQinCdWhBomDetailService.getEntityByOwnerAndParentSkuAndKitTypeAndLineNo(ownerCode, parentSkuCode, kitType, lineNo, orgId);
    }

    /**
     * 描述：保存组合件明细
     * <p>
     * create by Jianhua on 2019/8/25
     */
    @Transactional
    public void saveBomDetailEntity(BanQinCdWhBomDetailEntity entity) {
        if (StringUtils.isBlank(entity.getId())) {
            entity.setLineNo(wmsUtil.getMaxLineNo("cd_wh_bom_detail", "header_id", entity.getHeaderId()));
        }
        banQinCdWhBomDetailService.saveBomDetail(entity);
    }

    /**
     * 描述：删除组合件明细
     * <p>
     * create by Jianhua on 2019/8/25
     */
    @Transactional
    public void removeBomDetailEntity(String[] ids) {
        for (String id : ids) {
            BanQinCdWhBomDetail detail = banQinCdWhBomDetailService.get(id);
            this.banQinCdWhBomDetailService.removeByOwnerAndParentSkuAndKitTypeAndLineNo(detail.getOwnerCode(), detail.getParentSkuCode(), detail.getKitType(), detail.getLineNo(), detail.getOrgId());
        }
    }

    /**
     * 描述：保存加工工序
     * <p>
     * create by Jianhua on 2019/8/25
     */
    @Transactional
    public ResultMessage saveBomStep(BanQinCdWhBomStep cdWhBomStep) {
        ResultMessage msg = new ResultMessage();
        if (StringUtils.isBlank(cdWhBomStep.getId())) {
            cdWhBomStep.setLineNo(banQinCdWhBomStepService.getNewLineNo(cdWhBomStep.getOwnerCode(), cdWhBomStep.getParentSkuCode(), cdWhBomStep.getKitType(), cdWhBomStep.getOrgId()));
        }
        this.banQinCdWhBomStepService.save(cdWhBomStep);
        msg.setSuccess(true);
        msg.addMessage("操作成功");
        return msg;
    }

    /**
     * 描述：删除加工工序
     * <p>
     * create by Jianhua on 2019/8/25
     */
    @Transactional
    public ResultMessage removeBomStep(String[] ids) {
        ResultMessage msg = new ResultMessage();
        for (String id : ids) {
            this.banQinCdWhBomStepService.delete(new BanQinCdWhBomStep(id));
        }

        msg.setSuccess(true);
        msg.addMessage("操作成功");
        return msg;

    }

    /**
     * 描述：根据kitNo获取加工单(包括子表信息)
     * <p>
     * create by Jianhua on 2019/8/25
     */
    public BanQinWmKitEntity getKitEntity(String kitNo, String orgId) {
        // 加工单entity
        BanQinWmKitEntity entity = banQinWmKitHeaderService.getEntityByKitNo(kitNo, orgId);
        // 父件明细etntiys
        List<BanQinWmKitParentDetailEntity> parentEntities = banQinWmKitParentDetailService.getEntityByKitNo(kitNo, orgId);
        // 子件明细Entitys
        List<BanQinWmKitSubDetailEntity> subEntities = banQinWmKitSubDetailService.getEntityByKitNo(kitNo, orgId);
        // 加工任务entitys
        List<BanQinWmTaskKitEntity> taskEntities = banQinWmTaskKitService.getEntityByKitNo(kitNo, orgId);
        // 加工结果entitys
        List<BanQinWmKitResultDetailEntity> resultEntities = banQinWmKitResultDetailService.getEntityByKitNo(kitNo, orgId);
        // entity设置
        entity.setParentEntitys(parentEntities);
        entity.setSubEntitys(subEntities);
        entity.setTaskEntitys(taskEntities);
        entity.setResultEntitys(resultEntities);
        return entity;
    }

    /**
     * 描述：根据kitNo获取加工单(包括子表信息)
     * <p>
     * create by Jianhua on 2019/8/25
     */
    public BanQinWmKitEntity getKitEntity(String kitNo, String parentLineNo, String subLineNo, String kitTaskId, String kitLineNo, String orgId) {
        // 加工单entity
        BanQinWmKitEntity entity = banQinWmKitHeaderService.getEntityByKitNo(kitNo, orgId);
        // 父件明细entities
        List<BanQinWmKitParentDetailEntity> parentEntities = banQinWmKitParentDetailService.getEntityByKitNo(kitNo, orgId);
        // 单行操作
        if (parentLineNo != null) {
            entity.setParentEntity(banQinWmKitParentDetailService.getEntityByKitNoAndParentLineNo(kitNo, parentLineNo, orgId));
        }
        // 子件明细Entities
        List<BanQinWmKitSubDetailEntity> subEntities = banQinWmKitSubDetailService.getEntityByKitNo(kitNo, orgId);
        // 单行操作
        if (subLineNo != null) {
            entity.setSubEntity(banQinWmKitSubDetailService.getEntityByKitNoAndSubLineNo(kitNo, subLineNo, orgId));
        }
        // 加工任务entities
        List<BanQinWmTaskKitEntity> taskEntities = banQinWmTaskKitService.getEntityByKitNo(kitNo, orgId);
        // 单行操作
        if (kitTaskId != null) {
            entity.setTaskEntity(banQinWmTaskKitService.getEntityByKitTaskId(kitTaskId, orgId));
        }

        // 加工结果entities
        List<BanQinWmKitResultDetailEntity> resultEntities = banQinWmKitResultDetailService.getEntityByKitNo(kitNo, orgId);
        // 单行操作
        if (kitLineNo != null) {
            BanQinWmKitResultDetailEntity resultEntity = banQinWmKitResultDetailService.getEntityByKitNoAndKitLineNo(kitNo, kitLineNo, orgId);
            // 获取加工工序
            List<BanQinWmKitStepEntity> stepEntities = banQinWmKitStepService.getEntityByKitNoAndKitLineNo(kitNo, new String[]{kitLineNo}, orgId);
            resultEntity.setStepEntitys(stepEntities);
            entity.setResultEntity(resultEntity);
        }
        // entity设置
        entity.setParentEntitys(parentEntities);
        entity.setSubEntitys(subEntities);
        entity.setTaskEntitys(taskEntities);
        entity.setResultEntitys(resultEntities);
        return entity;
    }

    /**
     * 描述：保存加工单
     * <p>
     * create by Jianhua on 2019/8/25
     */
    @Transactional
    public ResultMessage saveKitEntity(BanQinWmKitEntity entity) {
        ResultMessage msg = new ResultMessage();
        try {
            entity = banQinWmKitHeaderService.saveEntity(entity);
        } catch (WarehouseException e) {
            msg.setSuccess(false);
            msg.addMessage(e.getMessage());
            return msg;
        }
        msg.setSuccess(true);
        msg.addMessage("操作成功");
        msg.setData(getKitEntity(entity.getKitNo(), entity.getOrgId()));
        return msg;
    }

    /**
     * 描述：复制加工单
     * <p>
     * create by Jianhua on 2019/8/25
     */
    public void duplicateKitEntity(BanQinWmKitEntity entity) {
        banQinKitDuplicateService.duplicateKitEntity(entity);
    }

    /**
     * 描述：加工单审核
     * <p>
     * create by Jianhua on 2019/8/26
     */
    @Transactional
    public void audit(String kitNo, String orgId) {
        banQinWmKitHeaderService.audit(kitNo, orgId);
    }

    /**
     * 描述：加工单 取消审核
     * <p>
     * create by Jianhua on 2019/8/26
     */
    @Transactional
    public void cancelAudit(String kitNo, String orgId) {
        banQinWmKitHeaderService.cancelAudit(kitNo, orgId);
    }

    /**
     * 描述：加工单关闭
     * <p>
     * create by Jianhua on 2019/8/26
     */
    @Transactional
    public void close(String kitNo, String orgId) {
        banQinKitCloseOrCancelService.close(kitNo, orgId);
    }

    /**
     * 描述：加工单取消
     * <p>
     * create by Jianhua on 2019/8/26
     */
    @Transactional
    public void cancelKit(String kitNo, String orgId) {
        banQinKitCloseOrCancelService.cancelKit(kitNo, orgId);
    }

    /**
     * 描述：获取父件明细
     * <p>
     * create by Jianhua on 2019/8/25
     */
    public BanQinWmKitParentDetailEntity getKitParentEntity(String kitNo, String parentLineNo, String orgId) {
        return banQinWmKitParentDetailService.getEntityByKitNoAndParentLineNo(kitNo, parentLineNo, orgId);
    }

    /**
     * 描述：保存父件明细
     * <p>
     * create by Jianhua on 2019/8/25
     */
    @Transactional
    public BanQinWmKitParentDetailEntity saveKitParentEntity(BanQinWmKitParentDetailEntity entity) {
        // 并发控制，加工单状态控制明细新增保存
        BanQinWmKitHeader headerModel = banQinWmKitHeaderService.getByKitNo(entity.getKitNo(), entity.getOrgId());
        if (!headerModel.getStatus().equals(WmsCodeMaster.KIT_NEW.getCode())) {
            throw new WarehouseException(entity.getKitNo() + "不是创建状态，不能操作");
        }
        if (headerModel.getAuditStatus().equals(WmsCodeMaster.AUDIT_CLOSE.getCode())) {
            throw new WarehouseException(entity.getKitNo() + "已审核，不能操作");
        }
        return banQinKitDuplicateService.saveKitParentEntity(entity);
    }

    /**
     * 描述：删除父件明细
     * <p>
     * create by Jianhua on 2019/8/25
     */
    @Transactional
    public void removeKitParentEntity(String kitNo, String[] parentLineNos, String orgId) {
        BanQinWmKitHeader model = banQinWmKitHeaderService.getByKitNo(kitNo, orgId);
        if (!model.getStatus().equals(WmsCodeMaster.KIT_NEW.getCode())) {
            throw new WarehouseException(model.getKitNo() + "不是创建状态，不能操作");
        }
        if (model.getAuditStatus().equals(WmsCodeMaster.AUDIT_CLOSE.getCode())) {
            throw new WarehouseException(model.getKitNo() + "已审核，不能操作");
        }
        for (String parentLineNo : parentLineNos) {
            banQinKitRemoveService.removeKitParent(kitNo, parentLineNo, orgId);
        }
    }

    /**
     * 描述：复制父件明细
     * <p>
     * create by Jianhua on 2019/8/25
     */
    @Transactional
    public void duplicateKitParentEntity(BanQinWmKitParentDetailEntity entity) {
        banQinKitDuplicateService.duplicateKitParentEntity(entity);
    }

    /**
     * 描述：生成子件
     * <p>
     * create by Jianhua on 2019/8/25
     */
    public ResultMessage generateSubs(String kitNo, String[] parentLineNos, String orgId) {
        ResultMessage msg = new ResultMessage();
        for (String parentLineNo : parentLineNos) {
            try {
                banQinKitGenerateSubService.generateSub(kitNo, parentLineNo, orgId);
            } catch (WarehouseException e) {
                msg.addMessage(e.getMessage());
            }
        }
        if (StringUtils.isBlank(msg.getMessage())) {
            msg.setSuccess(true);
            msg.addMessage("操作成功");
        }
        return msg;
    }

    /**
     * 描述：取消生成子件
     * <p>
     * create by Jianhua on 2019/8/25
     */
    public ResultMessage cancelGenerateSubs(String kitNo, String[] parentLineNos, String orgId) {
        ResultMessage msg = new ResultMessage();
        for (String parentLineNo : parentLineNos) {
            try {
                banQinKitGenerateSubService.cancelGenerateSub(kitNo, parentLineNo, orgId);
            } catch (WarehouseException e) {
                msg.addMessage(e.getMessage());
            }
        }
        if (StringUtils.isBlank(msg.getMessage())) {
            msg.setSuccess(true);
            msg.addMessage("操作成功");
        }
        return msg;
    }

    /**
     * 描述：取消父件明细
     * <p>
     * create by Jianhua on 2019/8/25
     */
    @Transactional
    public void cancelKitParent(String kitNo, String[] parentLineNos, String orgId) {
        for (String parentLineNo : parentLineNos) {
            banQinKitCloseOrCancelService.cancelKitParent(kitNo, parentLineNo, orgId);
        }
    }

    /**
     * 描述：获取子件明细
     * <p>
     * create by Jianhua on 2019/8/25
     */
    public BanQinWmKitSubDetailEntity getKitSubEntity(String kitNo, String subLineNo, String orgId) {
        return banQinWmKitSubDetailService.getEntityByKitNoAndSubLineNo(kitNo, subLineNo, orgId);
    }

    /**
     * 描述：保存子件明细
     * <p>
     * create by Jianhua on 2019/8/25
     */
    @Transactional
    public BanQinWmKitSubDetailEntity saveKitSubEntity(BanQinWmKitSubDetailEntity entity) {
        return banQinWmKitSubDetailService.saveKitSub(entity);
    }

    /**
     * 描述：子件明细分配
     * <p>
     * create by Jianhua on 2019/8/25
     */
    public ResultMessage alloc(String kitNo, List<String> subLineNos, String orgId) {
        return banQinKitBatchAllocService.kitBatchAlloc(kitNo, subLineNos, orgId);
    }

    /**
     * 描述：子件明细取消分配
     * <p>
     * create by Jianhua on 2019/8/25
     */
    public ResultMessage cancelAllocBySub(String processByCode, String kitNo, List<Object> subLineNos, String orgId) {
        return banQinKitBatchCancelAllocService.kitBatchCancelAlloc(processByCode, kitNo, subLineNos, orgId);
    }

    /**
     * 描述：子件明细拣货确认
     * <p>
     * create by Jianhua on 2019/8/25
     */
    public ResultMessage pickingBySub(String processByCode, String kitNo, List<Object> subLineNos, String orgId) {
        return banQinKitBatchPickingService.kitBatchPicking(processByCode, kitNo, subLineNos, orgId);
    }

    /**
     * 描述：子件明细取消拣货
     * <p>
     * create by Jianhua on 2019/8/25
     */
    public ResultMessage cancelPickingBySub(String processByCode, String kitNo, List<Object> subLineNos, String orgId) {
        return banQinKitBatchCancelPickingService.kitBatchCancelPicking(processByCode, kitNo, subLineNos, orgId);
    }

    /**
     * 描述：获取加工任务
     * <p>
     * create by Jianhua on 2019/8/25
     */
    public BanQinWmTaskKitEntity getTaskKitEntity(String kitTaskId, String orgId) {
        return banQinWmTaskKitService.getEntityByKitTaskId(kitTaskId, orgId);
    }

    /**
     * 描述：加工任务手工分配
     * <p>
     * create by Jianhua on 2019/8/25
     */
    @Transactional
    public ResultMessage manualAlloc(BanQinWmTaskKitEntity entity) {
        ResultMessage msg = new ResultMessage();
        banQinKitManualAllocService.kitManualAlloc(entity);
        msg.setSuccess(true);
        msg.addMessage("操作成功");
        return msg;
    }

    /**
     * 描述：加工任务拣货确认
     * <p>
     * create by Jianhua on 2019/8/25
     */
    public ResultMessage pickingByTask(List<BanQinWmTaskKitEntity> entities) {
        return banQinKitBatchPickingService.kitBatchPicking(entities);
    }

    /**
     * 描述：加工任务取消分配
     * <p>
     * create by Jianhua on 2019/8/25
     */
    public ResultMessage cancelAllocByTask(List<BanQinWmTaskKitEntity> entities) {
        return banQinKitBatchCancelAllocService.kitBatchCancelAlloc(entities);
    }

    /**
     * 描述：加工任务取消拣货
     * <p>
     * create by Jianhua on 2019/8/25
     */
    public ResultMessage cancelPickingByTask(List<BanQinWmTaskKitEntity> entities) {
        return banQinKitBatchCancelPickingService.kitBatchCancelPicking(entities);
    }

    /**
     * 描述：获取父件加工结果明细
     * <p>
     * create by Jianhua on 2019/8/25
     */
    public BanQinWmKitResultDetailEntity getKitResultEntity(String kitNo, String kitLineNo, String orgId) {
        BanQinWmKitResultDetailEntity entity = banQinWmKitResultDetailService.getEntityByKitNoAndKitLineNo(kitNo, kitLineNo, orgId);
        // 加工工序
        entity.setStepEntitys(banQinWmKitStepService.getEntityByKitNoAndKitLineNo(kitNo, new String[]{kitLineNo}, orgId));
        return entity;
    }

    /**
     * 描述：加工确认
     * <p>
     * create by Jianhua on 2019/8/25
     */
    public ResultMessage kitConfirm(List<BanQinWmKitResultDetailEntity> entities, List<BanQinWmTaskKitEntity> taskEntities, String subSelectCode) {
        return banQinKitBatchConfirmService.kitBatchConfirm(entities, taskEntities, subSelectCode);
    }

    /**
     * 描述：取消加工
     * <p>
     * create by Jianhua on 2019/8/25
     */
    public ResultMessage kitCancelConfirm(List<BanQinWmKitResultDetailEntity> entities) {
        ResultMessage msg = new ResultMessage();
        if (entities.size() > 0) {
            BanQinWmKitResultDetailEntity entity = entities.get(0);
            // 关闭后不能取消加工
            BanQinWmKitHeader model = banQinWmKitHeaderService.getByKitNo(entity.getKitNo(), entity.getOrgId());
            if (model.getStatus().equals(WmsCodeMaster.KIT_CLOSE.getCode())) {
                msg.addMessage(entity.getKitNo() + "已关闭，不能操作");
                msg.setSuccess(false);
                return msg;
            }
        }
        // 循环加工结果
        for (BanQinWmKitResultDetailEntity entity : entities) {
            try {
                banQinKitCancelConfirmService.kitCancelConfirm(entity);
            } catch (WarehouseException e) {
                msg.addMessage(e.getMessage());
            }
        }
        if (StringUtils.isBlank(msg.getMessage())) {
            msg.setSuccess(true);
            msg.addMessage("操作成功");
        }
        return msg;
    }

    /**
     * 描述：生成上架任务
     * <p>
     * create by Jianhua on 2019/8/25
     */
    public ResultMessage createTaskPa(List<BanQinWmKitResultDetailEntity> entities) {
        return banQinKitBatchCreateTaskPaService.kitBatchCreateTaskPa(entities);
    }

    /**
     * 描述：加工确认弹出框查询， 根据父件行号、拣货状态获取可进行加工确认的子件加工任务
     * <p>
     * create by Jianhua on 2019/8/25
     */
    public List<BanQinWmTaskKitEntity> getTaskKitEntityByKitConfirm(String kitNo, String parentLineNo, String status, String orgId) {
        return this.banQinWmTaskKitService.getEntityByParentLineNoAndStatus(kitNo, parentLineNo, status, orgId);
    }

    /** *******************************************加工工序************************************************************** */

    /**
     * 描述：保存加工工序
     * <p>
     * create by Jianhua on 2019/8/25
     */
    @Transactional
    public ResultMessage saveStepEntity(List<BanQinWmKitStepEntity> stepEntities) {
        ResultMessage msg = new ResultMessage();
        List<BanQinWmKitStepEntity> resultEntities;
        try {
            resultEntities = banQinWmKitStepService.saveEntity(stepEntities);
        } catch (WarehouseException e) {
            msg.addMessage(e.getMessage());
            msg.setSuccess(false);
            return msg;
        }

        msg.setSuccess(true);
        msg.addMessage("操作成功");
        msg.setData(resultEntities);
        return msg;
    }

    /** *******************************************上架任务************************************************************** */

    /**
     * 描述：根据上架任务ID和上架任务行号查询上架任务Entity
     * <p>
     * create by Jianhua on 2019/8/25
     */
    @Transactional
    public BanQinWmTaskPaEntity getEntityByPaIdAndLineNo(String paId, String lineNo, String orgId) {
        return banQinWmTaskPaService.getEntityByPaIdAndLineNo(paId, lineNo, orgId);
    }

    /**
     * 描述：删除上架任务
     * <p>
     * create by Jianhua on 2019/8/25
     */
    public ResultMessage kitBatchRemoveTaskPa(List<BanQinWmTaskPaEntity> wmTaskPaEntities) {
        ResultMessage msg = new ResultMessage();
        for (BanQinWmTaskPaEntity entity : wmTaskPaEntities) {
            try {
                banQinKitRemoveTaskPaService.kitRemovePutaway(entity);
            } catch (WarehouseException e) {
                msg.addMessage("上架任务ID" + entity.getPaId() + "行号" + entity.getLineNo() + e.getMessage());
            }
        }
        if (StringUtils.isBlank(msg.getMessage())) {
            msg.setSuccess(true);
            msg.addMessage("操作成功");
        }
        return msg;
    }

    /**
     * 描述：上架确认
     * <p>
     * create by Jianhua on 2019/8/25
     */
    public ResultMessage kitBatchPutaway(List<BanQinWmTaskPaEntity> wmTaskPaEntities) {
        return banQinInboundPaOperationService.inboundBatchPutaway(wmTaskPaEntities);
    }

    /**
     * 描述：根据订单号获取取消分配记录
     * <p>
     * create by Jianhua on 2019/8/25
     */
    public List<BanQinWmDelAllocEntity> getWmDelAllocEntityByKitNo(String kitNo, String orgId) {
        return banQinWmDelAllocService.getEntityByOrderNo(kitNo, orgId);
    }

    public ResultMessage kitBatchCreateTaskPaByDelAlloc(List<BanQinWmDelAllocEntity> list) {
        List<BanQinWmTaskKitEntity> result = Lists.newArrayList();
        for (BanQinWmDelAllocEntity entity : list) {
            BanQinWmTaskKitEntity kitEntity = new BanQinWmTaskKitEntity();
            BeanUtils.copyProperties(entity, kitEntity);
            kitEntity.setKitNo(entity.getOrderNo());
            kitEntity.setSubSkuCode(entity.getSkuCode());
            result.add(kitEntity);
        }
        return banQinKitBatchCreateTaskPaService.kitBatchCreateTaskPaByDelAlloc(result);
    }

}