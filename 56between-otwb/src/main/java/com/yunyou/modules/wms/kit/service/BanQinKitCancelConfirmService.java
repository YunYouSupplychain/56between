package com.yunyou.modules.wms.kit.service;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.modules.wms.common.entity.ActionCode;
import com.yunyou.modules.wms.common.entity.BanQinInventoryEntity;
import com.yunyou.modules.wms.common.entity.WarehouseException;
import com.yunyou.modules.wms.common.entity.WmsCodeMaster;
import com.yunyou.modules.wms.common.service.BanQinWmsCommonService;
import com.yunyou.modules.wms.inventory.service.BanQinInventoryService;
import com.yunyou.modules.wms.kit.entity.*;
import com.yunyou.modules.wms.kit.entity.extend.BanQinWmKitResultDetailEntity;
import com.yunyou.modules.wms.kit.entity.extend.BanQinWmTaskKitEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 描述：加工管理-取消加工
 * <p>
 * create by Jianhua on 2019/8/23
 */
@Service
public class BanQinKitCancelConfirmService {
    @Autowired
    protected BanQinWmsCommonService wmCommon;
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

    /**
     * 描述：取消加工
     * <p>
     * create by Jianhua on 2019/8/23
     */
    @Transactional
    public void kitCancelConfirm(BanQinWmKitResultDetailEntity entity) throws WarehouseException {
        // 加工单，父件更新时，需校验加工类型
        BanQinWmKitHeader model = banQinWmKitHeaderService.getByKitNo(entity.getKitNo(), entity.getOrgId());
        if (model.getStatus().equals(WmsCodeMaster.KIT_CLOSE.getCode())) {
            throw new WarehouseException(entity.getKitNo() + "已关闭，不能操作");
        }
        // 加工结果
        BanQinWmKitResultDetail resultModel = this.banQinWmKitResultDetailService.getByKitNoAndKitLineNo(entity.getKitNo(), entity.getKitLineNo(), entity.getOrgId());
        if (!resultModel.getStatus().equals(WmsCodeMaster.KIT_FULL_KIT.getCode())) {
            throw new WarehouseException("加工行号" + entity.getKitLineNo() + "未加工，不能操作");
        }
        if (StringUtils.isNotEmpty(resultModel.getPaId())) {
            throw new WarehouseException(entity.getKitLineNo() + "已经生成上架任务，不能操作");
        }
        // 更新子件加工任务
        List<BanQinWmTaskKitEntity> taskEntities = this.banQinWmTaskKitService.getEntityByKitNoAndKitLineNo(entity.getKitNo(), entity.getKitLineNo(), entity.getOrgId());
        if (CollectionUtil.isEmpty(taskEntities)) {
            throw new WarehouseException("加工行号" + entity.getKitLineNo() + "加工任务数据错误");
        }
        // 重新设置实际加工数，防止界面传入数据错误
        entity.setQtyCompleteEa(entity.getQtyPlanEa());
        entity.setQtyCompleteUom(entity.getQtyPlanUom());

        BanQinInventoryEntity parentInvEntity = new BanQinInventoryEntity();
        for (BanQinWmTaskKitEntity taskEntity : taskEntities) {
            BanQinWmTaskKit taskModel = new BanQinWmTaskKit();
            BeanUtils.copyProperties(taskEntity, taskModel);
            taskModel.setStatus(WmsCodeMaster.SUB_KIT_FULL_PICKING.getCode());// 完全拣货
            taskModel.setKitOp(null);// 加工人
            taskModel.setKitTime(null);// 加工时间
            taskModel.setKitLineNo(null);// 加工行号清空
            banQinWmTaskKitService.save(taskModel);
            // 组合件加工
            if (model.getKitType().equals(WmsCodeMaster.KIT_TYPE_COMBINE.getCode())) {
                // 子件更新库存、交易
                BanQinInventoryEntity subInvEntity = updateInvByTask(taskModel);
            }
            // 辅料加工/增值加工
            else if (model.getKitType().equals(WmsCodeMaster.KIT_TYPE_ACCESSORY.getCode()) || model.getKitType().equals(WmsCodeMaster.KIT_TYPE_VA.getCode())) {
                // 取消转移
                parentInvEntity = this.updateInvByResultCktf(taskModel, entity);
            }
        }
        // 更新子件
        List<BanQinWmKitSubDetail> subModels = this.banQinWmKitSubDetailService.getByKitNoAndParentLineNo(entity.getKitNo(), entity.getParentLineNo(), entity.getOrgId());
        for (BanQinWmKitSubDetail subModel : subModels) {
            this.banQinWmKitSubDetailService.updateStatus(ActionCode.CANCEL_KOUT, (entity.getQtyCompleteEa() * subModel.getQtyBomEa()), subModel);
        }

        // 组合件加工 更新父件库存、交易
        if (model.getKitType().equals(WmsCodeMaster.KIT_TYPE_COMBINE.getCode())) {
            // 取消入库
            parentInvEntity = this.updateInvByResultCkin(entity);
        }
        // 更新加工结果
        resultModel.setQtyCompleteEa(0D);// 实际加工数清0
        resultModel.setStatus(WmsCodeMaster.KIT_NEW.getCode());// 创建状态
        resultModel.setLotNum(null);// 批次号清空
        this.banQinWmKitResultDetailService.save(resultModel);
        // 更新加工工序，删除再重新新增记录
        this.banQinWmKitStepService.saveEntityByResult(entity);

        // 更新父件
        BanQinWmKitParentDetail parentModel = this.banQinWmKitParentDetailService.getByKitNoAndParentLineNo(entity.getKitNo(), entity.getParentLineNo(), entity.getOrgId());
        if (parentModel != null) {
            this.banQinWmKitParentDetailService.updateStatus(ActionCode.CANCEL_KIN, entity.getQtyCompleteEa(), parentModel);
        }
        // 更新加工单状态
        this.banQinWmKitHeaderService.updateStatus(entity.getKitNo(), entity.getOrgId());
    }

    /**
     * 描述：子件库存更新，出库
     * <p>
     * create by Jianhua on 2019/8/23
     */
    @Transactional
    public BanQinInventoryEntity updateInvByTask(BanQinWmTaskKit taskModel) throws WarehouseException {
        // 5、更新库存、交易(操作数量=分配数量)
        // 库存更新
        BanQinInventoryEntity invEntity = new BanQinInventoryEntity();
        invEntity.setAction(ActionCode.CANCEL_KOUT);// ACTION_CODE 子件取消出库
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
        invEntity.setQtyUom(taskModel.getQtyUom());// 包装数量
        invEntity.setOrgId(taskModel.getOrgId());
        return banQinInventoryService.updateInventory(invEntity);
    }

    /**
     * 描述：父件库存更新，入库
     * <p>
     * create by Jianhua on 2019/8/23
     */
    @Transactional
    public BanQinInventoryEntity updateInvByResultCkin(BanQinWmKitResultDetailEntity resultEntity) throws WarehouseException {
        // 5、更新库存、交易(操作数量=分配数量)
        // 库存更新
        BanQinInventoryEntity invEntity = new BanQinInventoryEntity();
        invEntity.setAction(ActionCode.CANCEL_KIN);// ACTION_CODE 父件取消入库
        invEntity.setOwnerCode(resultEntity.getOwnerCode());// 货主
        invEntity.setSkuCode(resultEntity.getParentSkuCode());// 商品
        invEntity.setLotNum(resultEntity.getLotNum());// 批次号
        invEntity.setLocCode(resultEntity.getKitLoc());// 加工库位
        invEntity.setTraceId(resultEntity.getKitTraceId());// 加工跟踪号
        invEntity.setQtyEaOp(resultEntity.getQtyCompleteEa());// 加工数
        invEntity.setOrderNo(resultEntity.getKitNo());// 加工单号
        invEntity.setLineNo(resultEntity.getKitLineNo());// 加工单加工结果行号
        invEntity.setPackCode(resultEntity.getPackCode());// 包装代码
        invEntity.setUom(resultEntity.getUom());// 包装单位
        invEntity.setQtyUom(resultEntity.getQtyCompleteEa() / resultEntity.getUomQty().doubleValue());// 包装单位换算数量
        invEntity.setOrgId(resultEntity.getOrgId());

        return banQinInventoryService.updateInventory(invEntity);
    }

    /**
     * 描述：父件库存更新，转移
     * <p>
     * create by Jianhua on 2019/8/23
     */
    @Transactional
    public BanQinInventoryEntity updateInvByResultCktf(BanQinWmTaskKit taskModel, BanQinWmKitResultDetailEntity resultEntity) throws WarehouseException {
        // 5、更新库存、交易(操作数量=分配数量)
        // 源
        BanQinInventoryEntity fminvm = new BanQinInventoryEntity();
        fminvm.setAction(ActionCode.CANCEL_KTF);
        fminvm.setOwnerCode(resultEntity.getOwnerCode());// 货主
        fminvm.setSkuCode(resultEntity.getParentSkuCode());// 商品
        fminvm.setLotNum(resultEntity.getLotNum());// 批次号
        fminvm.setLocCode(resultEntity.getKitLoc());// 加工库位
        fminvm.setTraceId(resultEntity.getKitTraceId());// 加工跟踪号
        fminvm.setOrderNo(resultEntity.getKitNo());// 加工单号
        fminvm.setLineNo(resultEntity.getKitLineNo());// 加工单加工结果行号
        fminvm.setPackCode(resultEntity.getPackCode());// 包装代码
        fminvm.setUom(resultEntity.getUom());// 包装单位
        fminvm.setQtyEaOp(taskModel.getQtyEa());// 加工数
        fminvm.setQtyUom(taskModel.getQtyUom());// 包装换算单位
        fminvm.setOrgId(taskModel.getOrgId());
        // 目标
        BanQinInventoryEntity toinvm = new BanQinInventoryEntity();
        toinvm.setAction(ActionCode.CANCEL_KTF);
        toinvm.setOwnerCode(taskModel.getOwnerCode());
        toinvm.setSkuCode(taskModel.getSubSkuCode());
        toinvm.setLotNum(taskModel.getLotNum());
        toinvm.setLocCode(taskModel.getToLoc());
        toinvm.setTraceId(taskModel.getToId());
        toinvm.setOrderNo(taskModel.getKitNo());
        toinvm.setLineNo(taskModel.getSubLineNo());
        toinvm.setTaskId(taskModel.getKitTaskId());
        toinvm.setPackCode(taskModel.getPackCode());
        toinvm.setUom(taskModel.getUom());
        toinvm.setQtyEaOp(taskModel.getQtyEa());
        toinvm.setQtyUom(taskModel.getQtyUom());// 包装换算单位
        toinvm.setOrgId(taskModel.getOrgId());
        return banQinInventoryService.updateInventory(fminvm, toinvm);
    }

}
