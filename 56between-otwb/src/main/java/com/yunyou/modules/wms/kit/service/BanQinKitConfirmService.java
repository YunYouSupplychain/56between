package com.yunyou.modules.wms.kit.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.IdGen;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.modules.sys.GenNoType;
import com.yunyou.modules.sys.service.SynchronizedNoService;
import com.yunyou.modules.sys.utils.UserUtils;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdRuleRotationDetail;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhSku;
import com.yunyou.modules.wms.basicdata.service.BanQinCdRuleRotationDetailService;
import com.yunyou.modules.wms.basicdata.service.BanQinCdWhSkuService;
import com.yunyou.modules.wms.common.entity.*;
import com.yunyou.modules.wms.common.service.BanQinWmsCommonService;
import com.yunyou.modules.wms.inbound.service.BanQinInboundPaOperationService;
import com.yunyou.modules.wms.inventory.entity.BanQinWmInvLotAtt;
import com.yunyou.modules.wms.inventory.entity.BanQinWmInvLotLoc;
import com.yunyou.modules.wms.inventory.service.BanQinInventoryService;
import com.yunyou.modules.wms.inventory.service.BanQinWmInvLotLocService;
import com.yunyou.modules.wms.kit.entity.*;
import com.yunyou.modules.wms.kit.entity.extend.BanQinWmKitResultDetailEntity;
import com.yunyou.modules.wms.kit.entity.extend.BanQinWmTaskKitEntity;
import com.yunyou.modules.wms.task.entity.BanQinWmTaskPa;
import com.yunyou.modules.wms.task.entity.BanQinWmTaskPaEntity;
import com.yunyou.modules.wms.task.service.BanQinWmTaskPaService;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 描述：加工管理-加工确认
 * <p>
 * create by Jianhua on 2019/8/23
 */
@Service
public class BanQinKitConfirmService {
    @Autowired
    protected BanQinWmsCommonService wmCommon;
    @Autowired
    protected SynchronizedNoService noService;
    @Autowired
    protected BanQinCdWhBomDetailService banQinCdWhBomDetailService;
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
    protected BanQinInventoryService banQinInventoryService;
    @Autowired
    protected BanQinCdRuleRotationDetailService banQinCdRuleRotationDetailService;
    @Autowired
    protected BanQinCdWhSkuService banQinCdWhSkuService;
    @Autowired
    protected BanQinInboundPaOperationService banQinInboundPaOperationService;
    @Autowired
    protected BanQinWmInvLotLocService banQinWmInvLotLocService;
    @Autowired
    protected BanQinWmTaskPaService banQinWmTaskPaService;

    /**
     * 描述：加工确认
     * <p>
     * create by Jianhua on 2019/8/23
     */
    @Transactional
    public void kitConfirm(BanQinWmKitResultDetailEntity entity, List<BanQinWmKitSubDetail> subModels, List<BanQinWmTaskKitEntity> taskEntitys) throws WarehouseException {
        // 已经加工
        BanQinWmKitResultDetail resultModel = banQinWmKitResultDetailService.getByKitNoAndKitLineNo(entity.getKitNo(), entity.getKitLineNo(), entity.getOrgId());
        if (resultModel.getStatus().equals(WmsCodeMaster.KIT_FULL_KIT.getCode())) {
            throw new WarehouseException("加工行号" + entity.getKitLineNo() + "为完全加工状态，不能操作");
        }
        // 获取加工类型
        BanQinWmKitHeader headerModel = banQinWmKitHeaderService.getByKitNo(entity.getKitNo(), entity.getOrgId());
        // 重新设置实际加工数=当前加工数
        entity.setQtyCompleteEa(entity.getQtyCurrentKitEa());// 当前需加工数
        entity.setQtyCompleteUom(entity.getQtyCurrentKitUom());// 当前需加工数
        if (headerModel.getKitType().equals(WmsCodeMaster.KIT_TYPE_COMBINE.getCode())) {
            // 按组合加工（Ａ=Ｂ+Ｃ）/COM
            resultModel = kitByCombine(entity, subModels, taskEntitys);
        } else if (headerModel.getKitType().equals(WmsCodeMaster.KIT_TYPE_ACCESSORY.getCode()) || headerModel.getKitType().equals(WmsCodeMaster.KIT_TYPE_VA.getCode())) {
            // 按辅料加工（Ａ=Ａ+辅料）/ACC， 按增值加工（A=A）/VA
            resultModel = kitByOther(entity, subModels, taskEntitys);
        }
        if (resultModel.getReserveCode() != null && resultModel.getReserveCode().equals(WmsCodeMaster.PARENT_KIT.getCode())) {
            // 加工确认时计算库位，生成上架任务
            ResultMessage resultMessage = createTaskPa(resultModel);
            if (resultMessage.isSuccess()) {
                BanQinWmTaskPaEntity wmTaskPaEntity = (BanQinWmTaskPaEntity) resultMessage.getData();
                String paId = wmTaskPaEntity.getPaId();
                resultModel.setPaId(paId);// 上架任务ID
            } else {
                throw new WarehouseException(resultMessage.getMessage());
            }
        }
        // 保存上架任务ID,跟踪号
        this.banQinWmKitResultDetailService.save(resultModel);
        // 父件状态、数量更新
        BanQinWmKitParentDetail parentModel = this.banQinWmKitParentDetailService.getByKitNoAndParentLineNo(entity.getKitNo(), entity.getParentLineNo(), entity.getOrgId());
        if (parentModel != null) {
            this.banQinWmKitParentDetailService.updateStatus(ActionCode.KIN, entity.getQtyCompleteEa(), parentModel);
        }
        // 更新加工单状态
        this.banQinWmKitHeaderService.updateStatus(entity.getKitNo(), entity.getOrgId());
    }

    /**
     * 描述：加工确认 按组合加工（Ａ=Ｂ+Ｃ）/COM
     * <p>
     * create by Jianhua on 2019/8/23
     */
    @Transactional
    public BanQinWmKitResultDetail kitByCombine(BanQinWmKitResultDetailEntity entity, List<BanQinWmKitSubDetail> subModels, List<BanQinWmTaskKitEntity> taskKitEntities) throws WarehouseException {
        // 循环基础子件，子件更新
        for (BanQinWmKitSubDetail subModel : subModels) {
            Double subKitEa = subModel.getQtyBomEa() * entity.getQtyCompleteEa();// 需加工数
            // 如果是普通，那么获取加工任务,更新任务、子件等信息
            if (subModel.getSubSkuType().equals(WmsCodeMaster.SUB_SKU_TYPE_COMMON.getCode())) {
                // 循环加工任务，库存更新
                Double taskKitEa = subKitEa;
                for (BanQinWmTaskKitEntity taskEntity : taskKitEntities) {
                    // 是否是当前子件的加工任务
                    if (taskEntity.getSubLineNo().equals(subModel.getSubLineNo())) {
                        if (taskKitEa <= 0) {
                            break;
                        }
                        // 包装
                        BanQinCdWhPackageRelationEntity packageEntity = wmCommon.getPackageRelationAndQtyUom(taskEntity.getPackCode(), taskEntity.getUom(), taskEntity.getOrgId());
                        // 更新加工任务，或拆分
                        BanQinWmTaskKit taskModel = updateTaskKit(entity.getKitLineNo(), taskEntity, taskKitEa, packageEntity);
                        // 子件出库
                        updateInvByTask(taskModel, packageEntity.getCdprQuantity().doubleValue());
                        taskKitEa -= taskModel.getQtyEa();// 剩余需操作数
                    }
                }
                // 如果需加工数未加工完成，则无法加工完成
                if (taskKitEa > 0) {
                    throw new WarehouseException("加工行号" + entity.getKitLineNo() + "可加工子件不足,不能操作");
                }
            }
            // 子件数量、状态更新(如果是辅料，直接更新子件)
            banQinWmKitSubDetailService.updateStatus(ActionCode.KOUT, subKitEa, subModel);

        }
        // 父件加工结果更新
        BanQinWmKitResultDetail resultModel = this.updateResult(entity);
        // 父件加工结果库存更新
        BanQinInventoryEntity invEntity = this.updateInvByResultKin(resultModel);
        resultModel.setKitTraceId(invEntity.getTraceId());// 如果有忽略ID，那么需要回填加工跟踪号
        return resultModel;
    }

    /**
     * 描述：加工确认 按辅料加工（Ａ=Ａ+辅料）/ACC 按增值加工（A=A）/VA
     * <p>
     * create by Jianhua on 2019/8/23
     */
    @Transactional
    public BanQinWmKitResultDetail kitByOther(BanQinWmKitResultDetailEntity entity, List<BanQinWmKitSubDetail> subModels, List<BanQinWmTaskKitEntity> taskEntitys) throws WarehouseException {
        BanQinWmKitResultDetail resultModel = null;
        // 循环基础子件，子件更新
        for (BanQinWmKitSubDetail subModel : subModels) {
            Double subKitEa = subModel.getQtyBomEa() * entity.getQtyCompleteEa();// 需加工数
            if (subKitEa <= 0) {
                continue;
            }
            // 如果是普通，那么获取加工任务,更新任务、子件等信息
            if (subModel.getSubSkuType().equals(WmsCodeMaster.SUB_SKU_TYPE_COMMON.getCode())) {
                if (entity.getSubSelectCode().equals(WmsCodeMaster.SUB_KIT_RT.getCode())) {
                    // 获取子件加工任务
                    taskEntitys = getTaskKitEntitysByRotation(subModel);
                }
                // 循环加工任务，库存更新
                Double taskKitEa = subKitEa;
                for (BanQinWmTaskKitEntity taskEntity : taskEntitys) {
                    // 是否是当前子件的加工任务
                    if (taskEntity.getSubLineNo().equals(subModel.getSubLineNo())) {
                        if (taskKitEa <= 0) {
                            break;
                        }
                        // 包装
                        BanQinCdWhPackageRelationEntity packageEntity = wmCommon.getPackageRelationAndQtyUom(taskEntity.getPackCode(), taskEntity.getUom(), taskEntity.getOrgId());
                        // 更新加工任务，或拆分
                        BanQinWmTaskKit taskModel = updateTaskKit(entity.getKitLineNo(), taskEntity, taskKitEa, packageEntity);

                        taskKitEa -= taskModel.getQtyEa();// 剩余需操作数
                        // 父件加工结果更新
                        resultModel = this.updateResult(entity);
                        // 父件加工结果库存更新
                        BanQinInventoryEntity invEntity = this.updateInvByResultKtf(taskModel, resultModel);
                        resultModel.setKitTraceId(invEntity.getTraceId());// 如果有忽略ID，那么需要回填加工跟踪号
                    }
                }
            }
            // 子件数量、状态更新(如果是辅料，直接更新子件)
            banQinWmKitSubDetailService.updateStatus(ActionCode.KOUT, subKitEa, subModel);
        }
        return resultModel;
    }

    /**
     * 描述：校验同一个traceID，是否允许重复收货 收货时，traceId不允许混SKU，混lotnum，混ASN存在
     * <p>
     * create by Jianhua on 2019/8/23
     */
    protected void checkTraceId(String kitNo, String traceId, String skuCode, String lotnum, String orgId) throws WarehouseException {
        if (!WmsConstants.TRACE_ID.equals(traceId)) {
            List<BanQinWmInvLotLoc> wmInvLotLocList = banQinWmInvLotLocService.getByTraceId(traceId, orgId);
            if (null != wmInvLotLocList && wmInvLotLocList.size() > 0) {
                for (BanQinWmInvLotLoc wmInvLotLocModel : wmInvLotLocList) {
                    if (!wmInvLotLocModel.getSkuCode().equals(skuCode) || !wmInvLotLocModel.getLotNum().equals(lotnum)) {
                        throw new WarehouseException("跟踪号" + traceId + "不能混商品、混批次");// 跟踪号{0}不能混商品、混批次
                    }
                }
                // 如果traceId已经生成上架任务，不能重复收
                BanQinWmTaskPa wmTaskPa = new BanQinWmTaskPa();
                wmTaskPa.setOrgId(orgId);
                wmTaskPa.setOrderNo(kitNo);
                wmTaskPa.setFmId(traceId);
                List<BanQinWmTaskPa> wmTaskPaList = banQinWmTaskPaService.findList(wmTaskPa);
                if (CollectionUtil.isNotEmpty(wmTaskPaList)) {
                    throw new WarehouseException("跟踪号" + traceId + "已经生成上架任务，不能操作");// 跟踪号{0}已经生成上架任务，不能操作
                }
            }
        }
    }

    /**
     * 描述：更新加工任务，或拆分
     * <p>
     * create by Jianhua on 2019/8/23
     */
    @Transactional
    public BanQinWmTaskKit updateTaskKit(String kitLineNo, BanQinWmTaskKitEntity taskEntity, Double taskKitEa, BanQinCdWhPackageRelationEntity packageEntity) throws WarehouseException {
        // 操作数
        Double qtyOpEa = 0D;
        Double qtyOpUom = 0D;
        if (taskEntity.getQtyEa() > taskKitEa) {
            // 如果任务拣货数>需加工数
            qtyOpEa = taskKitEa;
            qtyOpUom = wmCommon.doubleDivide(taskKitEa, packageEntity.getCdprQuantity().doubleValue());

            // 拆分，新的加工任务
            BanQinWmTaskKit newTaskModel = new BanQinWmTaskKit();
            BeanUtils.copyProperties(taskEntity, newTaskModel);
            newTaskModel.setId(IdGen.uuid());
            newTaskModel.setIsNewRecord(true);
            String kitTaskId = noService.getDocumentNo(GenNoType.WM_KIT_ID.name());
            newTaskModel.setKitTaskId(kitTaskId);// 任务ID
            newTaskModel.setQtyEa(newTaskModel.getQtyEa() - qtyOpEa);// 数量
            newTaskModel.setQtyUom(newTaskModel.getQtyUom() - qtyOpUom);
            // 保存
            banQinWmTaskKitService.save(newTaskModel);
        } else {
            // 如果任务拣货数<需加工数
            qtyOpEa = taskEntity.getQtyEa();
            qtyOpUom = wmCommon.doubleDivide(taskEntity.getQtyEa(), packageEntity.getCdprQuantity().doubleValue());

        }
        // 原加工任务
        BanQinWmTaskKit taskModel = new BanQinWmTaskKit();
        BeanUtils.copyProperties(taskEntity, taskModel);
        taskModel.setStatus(WmsCodeMaster.SUB_KIT_FULL_KIT.getCode());// 完全加工
        taskModel.setQtyEa(qtyOpEa);
        taskModel.setQtyUom(qtyOpUom);
        taskModel.setKitOp(UserUtils.getUser().getName());// 加工人
        taskModel.setKitTime(new Date());// 加工时间
        taskModel.setKitLineNo(kitLineNo);// 加工行号
        // 保存
        banQinWmTaskKitService.save(taskModel);
        return taskModel;
    }

    /**
     * 描述：更新父件结果，或拆分
     * <p>
     * create by Jianhua on 2019/8/23
     */
    @Transactional
    public BanQinWmKitResultDetail updateResult(BanQinWmKitResultDetailEntity entity) throws WarehouseException {
        if (entity.getQtyPlanEa() > entity.getQtyCompleteEa()) {
            // 如果计划加工数>实际加工数，那么拆分结果明细
            // 拆分，新父件结果
            BanQinWmKitResultDetail newResultModel = new BanQinWmKitResultDetail();
            BeanUtils.copyProperties(entity, newResultModel);
            newResultModel.setId(IdGen.uuid());
            newResultModel.setIsNewRecord(true);
            String newKitLineNo = banQinWmKitResultDetailService.getNewLineNo(newResultModel.getKitNo(), newResultModel.getOrgId());
            newResultModel.setKitLineNo(newKitLineNo);// 行号
            newResultModel.setQtyPlanEa(entity.getQtyPlanEa() - entity.getQtyCompleteEa());// 计划加工数
            newResultModel.setQtyCompleteEa(0D);// 实际加工数0
            newResultModel.setSubSelectCode(WmsCodeMaster.SUB_KIT_RT.getCode());// 子件指定规则，默认按周转选择
            newResultModel.setReserveCode(null);// 上架库位指定规则，默认为空
            this.banQinWmKitResultDetailService.save(newResultModel);

            // 拆分加工工序
            List<BanQinWmKitStep> stepModels = banQinWmKitStepService.getByKitNoAndKitLineNo(entity.getKitNo(), entity.getKitLineNo(), entity.getOrgId());
            if (CollectionUtil.isNotEmpty(stepModels)) {
                for (BanQinWmKitStep stepModel : stepModels) {
                    // 新加工工序
                    BanQinWmKitStep newStepModel = new BanQinWmKitStep();
                    BeanUtils.copyProperties(stepModel, newStepModel);
                    newStepModel.setId(IdGen.uuid());
                    newStepModel.setIsNewRecord(true);
                    newStepModel.setKitLineNo(newKitLineNo);
                    newStepModel.setUom(entity.getUom());// 单位
                    newStepModel.setQtyKitEa(entity.getQtyPlanEa() - entity.getQtyCompleteEa());
                    newStepModel.setQtyKit(newStepModel.getQtyKitEa() / entity.getUomQty());
                    // 源加工工序
                    stepModel.setUom(entity.getUom());// 单位
                    stepModel.setQtyKitEa(entity.getQtyCompleteEa());
                    stepModel.setQtyKit(stepModel.getQtyKitEa() / entity.getUomQty());

                    this.banQinWmKitStepService.save(newStepModel);
                    this.banQinWmKitStepService.save(stepModel);
                }
            }
        }
        // 计划加工数=实际加工数
        BanQinWmKitResultDetail resultModel = new BanQinWmKitResultDetail();
        BeanUtils.copyProperties(entity, resultModel);
        resultModel.setQtyPlanEa(entity.getQtyCompleteEa());// 计划加工数=实际加工数
        resultModel.setStatus(WmsCodeMaster.KIT_FULL_KIT.getCode());// 状态，完全加工

        // 生成批次号
        BanQinWmInvLotAtt wmInvLotAttModel = new BanQinWmInvLotAtt();
        wmInvLotAttModel.setOwnerCode(entity.getOwnerCode());
        wmInvLotAttModel.setSkuCode(entity.getParentSkuCode());
        wmInvLotAttModel.setLotAtt01(entity.getLotAtt01());
        wmInvLotAttModel.setLotAtt02(entity.getLotAtt02());
        wmInvLotAttModel.setLotAtt03(entity.getLotAtt03());
        wmInvLotAttModel.setLotAtt04(entity.getLotAtt04());
        wmInvLotAttModel.setLotAtt05(entity.getLotAtt05());
        wmInvLotAttModel.setLotAtt06(entity.getLotAtt06());
        wmInvLotAttModel.setLotAtt07(entity.getLotAtt07());
        wmInvLotAttModel.setLotAtt08(entity.getLotAtt08());
        wmInvLotAttModel.setLotAtt09(entity.getLotAtt09());
        wmInvLotAttModel.setLotAtt10(entity.getLotAtt10());
        wmInvLotAttModel.setLotAtt11(entity.getLotAtt11());
        wmInvLotAttModel.setLotAtt12(entity.getLotAtt12());
        wmInvLotAttModel.setOrgId(entity.getOrgId());
        String lotNum = this.banQinInventoryService.createInvLotNum(wmInvLotAttModel);

        resultModel.setLotNum(lotNum);
        // 不能直接执行生成上架任务，辅料会生成多行任务
        this.banQinWmKitResultDetailService.save(resultModel);
        return banQinWmKitResultDetailService.get(resultModel.getId());
    }

    /**
     * 描述：子件库存更新，出库
     * <p>
     * create by Jianhua on 2019/8/23
     */
    @Transactional
    public BanQinInventoryEntity updateInvByTask(BanQinWmTaskKit taskModel, Double uomQty) throws WarehouseException {
        // 5、更新库存、交易(操作数量=分配数量)
        // 库存更新
        BanQinInventoryEntity invEntity = new BanQinInventoryEntity();
        invEntity.setAction(ActionCode.KOUT);// ACTION_CODE 子件出库
        invEntity.setOwnerCode(taskModel.getOwnerCode());// 货主
        invEntity.setSkuCode(taskModel.getSubSkuCode());// 商品
        invEntity.setLotNum(taskModel.getLotNum());// 批次号
        invEntity.setLocCode(taskModel.getToLoc());// 拣货库位
        invEntity.setTraceId(taskModel.getToId());// 拣货库位跟踪号
        invEntity.setQtyEaOp(taskModel.getQtyEa());// 加工数
        invEntity.setOrderNo(taskModel.getKitNo());// 加工单号
        invEntity.setLineNo(taskModel.getSubLineNo());// 加工单子件行号
        invEntity.setTaskId(taskModel.getKitTaskId());// 任务ID
        invEntity.setPackCode(taskModel.getPackCode());// 包装代码
        invEntity.setUom(taskModel.getUom());// 包装单位
        invEntity.setQtyUom(taskModel.getQtyUom());// 包装单位换算数量
        invEntity.setOrgId(taskModel.getOrgId());

        return banQinInventoryService.updateInventory(invEntity);
    }

    /**
     * 描述：父件库存更新，入库
     * <p>
     * create by Jianhua on 2019/8/23
     */
    @Transactional
    public BanQinInventoryEntity updateInvByResultKin(BanQinWmKitResultDetail resultModel) throws WarehouseException {
        // 5、更新库存、交易(操作数量=分配数量)
        // 库存更新
        BanQinInventoryEntity invEntity = new BanQinInventoryEntity();
        invEntity.setAction(ActionCode.KIN);// ACTION_CODE 父件入库
        invEntity.setOwnerCode(resultModel.getOwnerCode());// 货主
        invEntity.setSkuCode(resultModel.getParentSkuCode());// 商品
        invEntity.setLotNum(resultModel.getLotNum());// 批次号
        invEntity.setLocCode(resultModel.getKitLoc());// 加工库位
        invEntity.setTraceId(resultModel.getKitTraceId());// 加工跟踪号
        invEntity.setQtyEaOp(resultModel.getQtyCompleteEa());// 加工数EA
        invEntity.setOrderNo(resultModel.getKitNo());// 加工单号
        invEntity.setLineNo(resultModel.getKitLineNo());// 加工单加工结果行号
        invEntity.setPackCode(resultModel.getPackCode());// 包装代码
        invEntity.setUom(resultModel.getUom());// 包装单位
        invEntity.setOrgId(resultModel.getOrgId());
        // 包装单位换算数量
        BanQinCdWhPackageRelationEntity packageEntity = wmCommon.getPackageRelationAndQtyUom(resultModel.getPackCode(), resultModel.getUom(), resultModel.getOrgId());
        invEntity.setQtyUom(resultModel.getQtyCompleteEa() / packageEntity.getCdprQuantity().doubleValue());// 包装数量=加工数UOM

        return banQinInventoryService.updateInventory(invEntity);
    }

    /**
     * 描述：父件库存更新，转移
     * <p>
     * create by Jianhua on 2019/8/23
     */
    @Transactional
    public BanQinInventoryEntity updateInvByResultKtf(BanQinWmTaskKit taskModel, BanQinWmKitResultDetail resultModel) throws WarehouseException {
        // 源
        BanQinInventoryEntity fminvm = new BanQinInventoryEntity();
        fminvm.setAction(ActionCode.KTF);
        fminvm.setOwnerCode(taskModel.getOwnerCode());
        fminvm.setSkuCode(taskModel.getSubSkuCode());
        fminvm.setLotNum(taskModel.getLotNum());
        fminvm.setLocCode(taskModel.getToLoc());
        fminvm.setTraceId(taskModel.getToId());
        fminvm.setOrderNo(taskModel.getKitNo());
        fminvm.setLineNo(taskModel.getSubLineNo());
        fminvm.setTaskId(taskModel.getKitTaskId());
        fminvm.setPackCode(taskModel.getPackCode());
        fminvm.setUom(taskModel.getUom());
        fminvm.setQtyEaOp(taskModel.getQtyEa());// 加工数EA
        fminvm.setQtyUom(taskModel.getQtyUom());// 加工数UOM
        fminvm.setOrgId(taskModel.getOrgId());

        // 目标
        BanQinInventoryEntity toinvm = new BanQinInventoryEntity();
        toinvm.setAction(ActionCode.KTF);
        toinvm.setOwnerCode(resultModel.getOwnerCode());// 货主
        toinvm.setSkuCode(resultModel.getParentSkuCode());// 商品
        toinvm.setLotNum(resultModel.getLotNum());// 批次号
        toinvm.setLocCode(resultModel.getKitLoc());// 加工库位
        toinvm.setTraceId(resultModel.getKitTraceId());// 加工跟踪号
        toinvm.setOrderNo(resultModel.getKitNo());// 加工单号
        toinvm.setLineNo(resultModel.getKitLineNo());// 加工单加工结果行号
        toinvm.setPackCode(resultModel.getPackCode());// 包装代码
        toinvm.setUom(resultModel.getUom());// 包装单位
        toinvm.setQtyEaOp(taskModel.getQtyEa());// 加工数EA
        toinvm.setQtyUom(taskModel.getQtyUom());// 加工数UOM
        toinvm.setOrgId(taskModel.getOrgId());

        return banQinInventoryService.updateInventory(fminvm, toinvm);
    }

    /**
     * 描述：根据加工单号、子件行号、周转规则排序获取加工任务
     * <p>
     * create by Jianhua on 2019/8/23
     */
    public List<BanQinWmTaskKitEntity> getTaskKitEntitysByRotation(BanQinWmKitSubDetail subModel) {
        // 构造周转规则参数
        BanQinCdRuleRotationSqlParamEntity sqlParamEntity = new BanQinCdRuleRotationSqlParamEntity();
        sqlParamEntity.setLotAtt01(subModel.getLotAtt01());
        sqlParamEntity.setLotAtt02(subModel.getLotAtt02());
        sqlParamEntity.setLotAtt03(subModel.getLotAtt03());
        sqlParamEntity.setLotAtt04(subModel.getLotAtt04());
        sqlParamEntity.setLotAtt05(subModel.getLotAtt05());
        sqlParamEntity.setLotAtt06(subModel.getLotAtt06());
        sqlParamEntity.setLotAtt07(subModel.getLotAtt07());
        sqlParamEntity.setLotAtt08(subModel.getLotAtt08());
        sqlParamEntity.setLotAtt09(subModel.getLotAtt09());
        sqlParamEntity.setLotAtt10(subModel.getLotAtt10());
        sqlParamEntity.setLotAtt11(subModel.getLotAtt11());
        sqlParamEntity.setLotAtt12(subModel.getLotAtt12());
        List<BanQinCdRuleRotationDetail> ruleRotationDetailList = banQinCdRuleRotationDetailService.getByRuleCode(subModel.getRotationRule(), subModel.getOrgId());
        sqlParamEntity.setRuleRotationDetailList(ruleRotationDetailList);
        // 查询
        return banQinWmTaskKitService.getEntityByProcess(ProcessByCode.BY_KIT_SUB.getCode(), subModel.getKitNo(), Lists.newArrayList(subModel.getSubLineNo()), WmsCodeMaster.SUB_KIT_FULL_PICKING.getCode(), subModel.getOrgId());
    }

    /**
     * 描述：生成上架任务
     * <p>
     * create by Jianhua on 2019/8/23
     */
    @Transactional
    public ResultMessage createTaskPa(BanQinWmKitResultDetail resultModel) throws WarehouseException {
        BanQinWmTaskPaEntity taskPaEntity = new BanQinWmTaskPaEntity();
        taskPaEntity.setOrderNo(resultModel.getKitNo());
        taskPaEntity.setOrderType(WmsCodeMaster.ORDER_KIT.getCode());
        taskPaEntity.setOwnerCode(resultModel.getOwnerCode());
        taskPaEntity.setSkuCode(resultModel.getParentSkuCode());
        taskPaEntity.setLotNum(resultModel.getLotNum());
        taskPaEntity.setFromLoc(resultModel.getKitLoc());
        taskPaEntity.setFromId(resultModel.getKitTraceId());
        taskPaEntity.setNewPaRule(resultModel.getPaRule());
        taskPaEntity.setNewReserveCode(resultModel.getReserveCode());
        taskPaEntity.setPlanPaLoc(null);
        taskPaEntity.setPackCode(resultModel.getPackCode());
        taskPaEntity.setQtyEa(resultModel.getQtyCompleteEa());
        taskPaEntity.setOrgId(resultModel.getOrgId());
        taskPaEntity.setUom(resultModel.getUom());
        BanQinCdWhSku skuModel = this.banQinCdWhSkuService.getByOwnerAndSkuCode(resultModel.getOwnerCode(), resultModel.getParentSkuCode(), resultModel.getOrgId());
        if (skuModel != null) {
            taskPaEntity.setCubic(skuModel.getCubic());
            taskPaEntity.setGrossWeight(skuModel.getGrossWeight());
        }
        // 调用生成上架任务
        return banQinInboundPaOperationService.inboundCreatePaTask(taskPaEntity);
    }
}
