package com.yunyou.modules.wms.kit.service;

import com.yunyou.common.utils.IdGen;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.modules.sys.GenNoType;
import com.yunyou.modules.sys.service.SynchronizedNoService;
import com.yunyou.modules.wms.basicdata.service.BanQinCdWhSkuLocService;
import com.yunyou.modules.wms.basicdata.service.BanQinCdWhSkuService;
import com.yunyou.modules.wms.common.entity.*;
import com.yunyou.modules.wms.common.service.BanQinWmsCommonService;
import com.yunyou.modules.wms.inventory.service.BanQinInventoryService;
import com.yunyou.modules.wms.inventory.service.BanQinWmInvLotLocService;
import com.yunyou.modules.wms.kit.entity.BanQinWmKitSubDetail;
import com.yunyou.modules.wms.kit.entity.BanQinWmTaskKit;
import com.yunyou.modules.wms.kit.entity.extend.BanQinWmTaskKitEntity;
import com.yunyou.modules.wms.outbound.entity.BanQinAllocInvLotLocEntity;
import com.yunyou.modules.wms.outbound.service.BanQinOutboundCommonService;
import com.yunyou.modules.wms.outbound.service.BanQinOutboundManualAllocService;
import com.yunyou.modules.wms.outbound.service.BanQinWmDelAllocService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 描述：加工管理- 子件手工分配
 * <p>
 * create by Jianhua on 2019/8/25
 */
@Service
public class BanQinKitManualAllocService {
    @Autowired
    protected BanQinWmsCommonService wmCommon;
    @Autowired
    protected BanQinOutboundCommonService outboundCommon;
    @Autowired
    protected BanQinOutboundManualAllocService outboundManualAlloc;
    @Autowired
    protected BanQinWmKitSubDetailService banQinWmKitSubDetailService;
    @Autowired
    protected BanQinWmTaskKitService banQinWmTaskKitService;
    @Autowired
    protected BanQinWmInvLotLocService banQinWmInvLotLocService;
    @Autowired
    protected BanQinCdWhSkuService banQinCdWhSkuService;
    @Autowired
    protected SynchronizedNoService noService;
    @Autowired
    protected BanQinInventoryService banQinInventoryService;
    @Autowired
    protected BanQinCdWhSkuLocService banQinCdWhSkuLocService;
    @Autowired
    protected BanQinWmDelAllocService banQinWmDelAllocService;

    /**
     * 描述：手工分配
     * <p>
     * create by Jianhua on 2019/8/25
     */
    @Transactional
    public BanQinWmTaskKitEntity kitManualAlloc(BanQinWmTaskKitEntity entity) throws WarehouseException {
        BanQinWmKitSubDetail subModel = this.banQinWmKitSubDetailService.getByKitNoAndSubLineNo(entity.getKitNo(), entity.getSubLineNo(), entity.getOrgId());

        // 3.1 手工分配新增保存
        if (StringUtils.isEmpty(entity.getId())) {
            entity = insertTaskKit(entity, subModel);
        }
        // 3.2 手工分配修改保存
        else {
            entity = modifyTaskKit(entity, subModel);
        }
        return entity;
    }

    /**
     * 描述：新增加工任务
     * <p>
     * create by Jianhua on 2019/8/25
     */
    @Transactional
    protected BanQinWmTaskKitEntity insertTaskKit(BanQinWmTaskKitEntity entity, BanQinWmKitSubDetail subModel) throws WarehouseException {
        // 1、校验分配数量 小于等于 可分配数量
        // 出库明细行可分配数量 = 订货数 - 已分配数 - 已拣货数 - 已加工数
        Double qtyAlloc = subModel.getQtyPlanEa() - subModel.getQtyAllocEa() - subModel.getQtyPkEa() - subModel.getQtyKitEa();
        // 如果可分配数量 < 当前分配数量，不能手工分配，订货数不足
        if (qtyAlloc < entity.getQtyEa()) {
            throw new WarehouseException("分配数量大于计划加工数，不能操作");
        }

        // 2、获取包装单位装箱标记
        BanQinCdWhPackageRelationEntity packageRelationEntity = wmCommon.getPackageRelationAndQtyUom(entity.getPackCode(), entity.getUom(), entity.getQtyEa(), entity.getOrgId());

        // 3、根据界面传入的批次号、库位、跟踪号 获取批次库位库存 可分配数量，校验可用库存数量与分配数量大小
        // 校验效期：如果商品配置开启效期控制，那么校验批次库存是否符合效期期, 不符合，提示信息 校验可分配数量 大于等于
        // 分配数量(分配明细输入的分配操作数)，固定拣货位，超量分配，超量补货
        BanQinAllocInvLotLocEntity allocInvEntity = new BanQinAllocInvLotLocEntity();
        allocInvEntity.setOwnerCode(entity.getOwnerCode());
        allocInvEntity.setSkuCode(entity.getSubSkuCode());
        allocInvEntity.setLotNum(entity.getLotNum());
        allocInvEntity.setLocCode(entity.getLocCode());
        allocInvEntity.setTraceId(entity.getTraceId());
        allocInvEntity.setLotAtt01(subModel.getLotAtt01());
        allocInvEntity.setLotAtt02(subModel.getLotAtt02());
        allocInvEntity.setOrgId(entity.getOrgId());
        BanQinInventoryEntity invEntity = outboundManualAlloc.getInvLotLoc(allocInvEntity);
        // 库存可分配数量 = 可超量分配数量+ 库存总数量-冻结数量-分配数量-已拣货数量-上架待出数量-补货待出数量-移动待出数量
        Double qtyAllocInv = banQinInventoryService.getAvailableAllocQty(invEntity);

        // 超量分配数量
        allocInvEntity.setQtyEaOp(qtyAlloc);// 操作数=需要分配的数量
        allocInvEntity.setQtyUomOp(entity.getQtyUom());
        Double qtyOverAlloc = outboundManualAlloc.getOverAlloc(allocInvEntity, invEntity, packageRelationEntity.getCdprQuantity());
        // 库存更新类型
        ActionCode actionCode = ActionCode.ONESTEP_ALLOCATION;// 默认一步分配
        if (qtyOverAlloc != 0) {
            qtyAllocInv += qtyOverAlloc;
            actionCode = ActionCode.ONESTEP_OVER_ALLOCATION;// 一步超量分配
        }
        // 如果库存总可分配数量<将分配数量,库存不足
        if (qtyAllocInv < entity.getQtyEa()) {
            throw new WarehouseException("有效库存不足,批次[" + entity.getLotNum() + "]库位[" + entity.getToLoc() + "]跟踪号[" + entity.getToId() + "]");
        }

        // 5、新增分配明细：生成分配编号， 波次单号
        // 新增：生成分配编号， 波次单号，预配单位数量换算
        // 生成TO_ID(跟踪号)
        if (entity.getToId() == null || StringUtils.isEmpty(entity.getToId())) {
            // 目标跟踪号
            String toId = null;
            // 分配参数：分配时是否自动产生跟踪号（Y：自动产生；N：默认为*）
            String allocTraceId = WmsConstants.NO/*wmCommon.getSysControlParam(ControlParamCode.ALLOC_TRACE_ID.getCode())*/;
            if (allocTraceId.equals(WmsConstants.YES)) {
                toId = noService.getDocumentNo(GenNoType.WM_TRACE_ID.name());
            } else {
                toId = WmsConstants.TRACE_ID;
            }
            entity.setToId(toId);
        }

        entity.setOrgId(entity.getOrgId());
        // 分配ID生成
        String kitTaskId = noService.getDocumentNo(GenNoType.WM_KIT_ID.name());
        entity.setKitTaskId(kitTaskId);
        // 6、统一更新
        return updateTaskKit(actionCode, entity.getQtyEa(), null, entity, subModel);
    }

    /**
     * 描述：修改分配明细
     * <p>
     * create by Jianhua on 2019/8/25
     */
    @Transactional
    protected BanQinWmTaskKitEntity modifyTaskKit(BanQinWmTaskKitEntity entity, BanQinWmKitSubDetail subModel) throws WarehouseException {
        // 1、校验预配数量+分配数量+拣货数量+发货数量 小于等于 订货数量
        // 源分配明细获取
        BanQinWmTaskKit model = banQinWmTaskKitService.getByKitTaskId(entity.getKitTaskId(), entity.getOrgId());

        // 2、获取包装维护，装箱标记
        BanQinCdWhPackageRelationEntity packageRelationEntity = wmCommon.getPackageRelationAndQtyUom(entity.getPackCode(), entity.getUom(), entity.getOrgId());
        // 包装单位换算数量
        Integer uomQty = packageRelationEntity.getCdprQuantity();

        // 3、根据界面传入的批次号、库位、跟踪号 获取批次库位库存 可分配数量， 校验可用库存数量与分配数量大小
        // 校验效期：如果商品配置开启效期控制，那么校验批次库存是否符合效期期，不符合，提示信息 校验可分配数量 大于等于
        // 分配数量(分配明细输入的分配操作数)，否，提示信息
        // 超量分配计算、补货：如果该商品拣货位配置该拣货位，那么校验是否超量分配，获取可超量分配的库存
        // 获取批次库位库存
        BanQinAllocInvLotLocEntity allocInvEntity = new BanQinAllocInvLotLocEntity();
        allocInvEntity.setOwnerCode(entity.getOwnerCode());
        allocInvEntity.setSkuCode(entity.getSubSkuCode());
        allocInvEntity.setLotNum(entity.getLotNum());
        allocInvEntity.setLocCode(entity.getLocCode());
        allocInvEntity.setTraceId(entity.getTraceId());
        allocInvEntity.setLotAtt01(subModel.getLotAtt01());
        allocInvEntity.setLotAtt02(subModel.getLotAtt02());
        allocInvEntity.setOrgId(entity.getOrgId());
        BanQinInventoryEntity invEntity = outboundManualAlloc.getInvLotLoc(allocInvEntity);
        // 4、数量修改处理
        Double qtyAllocDiff = 0D; // 差异数量
        ActionCode actionCode = null;

        // 4.1、数量未更改 ，原分配数量=新分配数量
        if (model.getQtyEa().equals(entity.getQtyEa())) {
            throw new WarehouseException("操作成功");
        }

        // 4.2、数量改小，取消分配
        else if (model.getQtyEa() > entity.getQtyEa()) {
            // 数量改小，比较 差异数量 与 库存记录分配数量, 原分配数量>新增分配数量，取消分配
            qtyAllocDiff = model.getQtyEa() - entity.getQtyEa();
            // 1、校验取消分配数量与出库单行分配数量，是否足够取消分配
            if (subModel.getQtyAllocEa() < qtyAllocDiff) {
                throw new WarehouseException("没有足够的分配数取消");
            }
            // 2、比较库存分配数量与分配数 大小，是否足够取消分配
            if (invEntity.getQtyAlloc() < qtyAllocDiff) {
                throw new WarehouseException("有效库存不足,批次" + entity.getLotNum() + " 库位" + entity.getToLoc() + " 跟踪号" + entity.getToId());
            }
            // 3、库存更新ACTION_CODE设置
            actionCode = ActionCode.CANCEL_ALLOCATION;// 取消分配
        }
        // 4.3、数量改大，一步分配
        else if (model.getQtyEa() < entity.getQtyEa()) {
            // 差异数量,原分配数量<新增分配数量 , 新分配数量-原分配数量
            qtyAllocDiff = entity.getQtyEa() - model.getQtyEa();
            // 一步分配
            // 出库明细行可分配数量 = 订货数 - 已预配数 - 已分配数 - 已拣货数 - 已发货数
            Double qtyAlloc = subModel.getQtyPlanEa() - subModel.getQtyAllocEa() - subModel.getQtyPkEa() - subModel.getQtyKitEa();
            if (qtyAlloc < qtyAllocDiff) {
                throw new WarehouseException("分配数量大于待加工数");
            }
            // 库存可分配数量 = 可超量分配 + 库存总数量-冻结数量-分配数量-已拣货数量-上架待出数量-补货待出数量-移动待出数量
            Double qtyAllocInv = banQinInventoryService.getAvailableAllocQty(invEntity);
            allocInvEntity.setQtyEaOp(qtyAllocDiff);// 操作数=需要分配的数量
            allocInvEntity.setQtyUomOp(entity.getQtyUom());

            Double qtyOverAlloc = outboundManualAlloc.getOverAlloc(allocInvEntity, invEntity, uomQty);
            // 总可分配数量
            qtyAllocInv += qtyOverAlloc;

            // 4、比较可用库存与分配数 大小，是否足够分配
            if (qtyAllocInv < qtyAllocDiff) {
                throw new WarehouseException("有效库存不足,批次" + entity.getLotNum() + " 库位" + entity.getToLoc() + " 跟踪号" + entity.getToId());
            }
            // 一步分配
            // actionCode设置,
            if (qtyOverAlloc > 0D) {
                actionCode = ActionCode.ONESTEP_OVER_ALLOCATION;// 一步超量分配
            } else {
                actionCode = ActionCode.ONESTEP_ALLOCATION;
            }
        }
        if (actionCode == null) {
            throw new WarehouseException("操作代码不能为空");
        }
        // 统一更新
        return updateTaskKit(actionCode, qtyAllocDiff, uomQty.doubleValue(), entity, subModel);
    }

    /**
     * 描述：手工分配 修改分配明细 保存统一方法
     * <p>
     * create by Jianhua on 2019/8/25
     */
    @Transactional
    protected BanQinWmTaskKitEntity updateTaskKit(ActionCode actionCode, Double qtyAllocDiff, Double uomQty, BanQinWmTaskKitEntity entity, BanQinWmKitSubDetail subModel) throws WarehouseException {
        // 取消分配，写取消分配记录
        if (actionCode.equals(ActionCode.CANCEL_ALLOCATION)) {
            BanQinWmTaskKit model = new BanQinWmTaskKit();
            BeanUtils.copyProperties(entity, model);
            model.setQtyEa(qtyAllocDiff);
            model.setQtyUom(qtyAllocDiff / uomQty);
            banQinWmDelAllocService.saveDelAllocByTaskKit(model);
        }

        // 1、保存加工任务
        BanQinWmTaskKit model = new BanQinWmTaskKit();
        BeanUtils.copyProperties(entity, model);
        model.setId(IdGen.uuid());
        model.setIsNewRecord(true);
        this.banQinWmTaskKitService.save(model);

        BanQinInventoryEntity invEntity = new BanQinInventoryEntity();
        // 2、更新库存
        invEntity.setAction(actionCode);
        invEntity.setOwnerCode(entity.getOwnerCode());// 货主
        invEntity.setSkuCode(entity.getSubSkuCode());// 商品
        invEntity.setLotNum(entity.getLotNum());// 批次号
        invEntity.setLocCode(entity.getLocCode());// 分配库位
        invEntity.setTraceId(entity.getTraceId());// 分配库位跟踪号
        invEntity.setQtyEaOp(qtyAllocDiff);// 分配数量
        invEntity.setOrgId(entity.getOrgId());
        banQinInventoryService.updateInventory(invEntity);

        // 3、更新子件状态
        this.banQinWmKitSubDetailService.updateStatus(actionCode, qtyAllocDiff, subModel);
        return banQinWmTaskKitService.getEntityByKitTaskId(model.getKitTaskId(), model.getOrgId());
    }

}
