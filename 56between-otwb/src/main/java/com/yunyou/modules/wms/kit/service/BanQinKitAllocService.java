package com.yunyou.modules.wms.kit.service;

import com.yunyou.common.utils.IdGen;
import com.yunyou.modules.sys.GenNoType;
import com.yunyou.modules.sys.service.SynchronizedNoService;
import com.yunyou.modules.wms.common.entity.*;
import com.yunyou.modules.wms.common.service.BanQinWmsCommonService;
import com.yunyou.modules.wms.inventory.entity.BanQinWmInvLotLoc;
import com.yunyou.modules.wms.inventory.service.BanQinInventoryService;
import com.yunyou.modules.wms.inventory.service.BanQinWmInvLotLocService;
import com.yunyou.modules.wms.kit.entity.BanQinWmKitSubDetail;
import com.yunyou.modules.wms.kit.entity.BanQinWmTaskKit;
import com.yunyou.modules.wms.kit.entity.extend.BanQinKitAllocUpdateEntity;
import com.yunyou.modules.wms.outbound.entity.BanQinAllocInvLotLocEntity;
import com.yunyou.modules.wms.outbound.service.BanQinOutboundCommonService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 描述：加工管理-子件分配(一步分配)
 * <p>
 * create by Jianhua on 2019/8/23
 */
@Service
public class BanQinKitAllocService {
    @Autowired
    protected BanQinWmsCommonService wmCommon;
    @Autowired
    protected BanQinOutboundCommonService outboundCommon;
    @Autowired
    protected BanQinWmKitSubDetailService banQinWmKitSubDetailService;
    @Autowired
    protected BanQinWmTaskKitService banQinWmTaskKitService;
    @Autowired
    protected SynchronizedNoService noService;
    @Autowired
    protected BanQinWmInvLotLocService banQinWmInvLotLocService;
    @Autowired
    protected BanQinInventoryService banQinInventoryService;

    /**
     * 一步分配更新
     *
     * @param updateEntity
     * @return
     * @throws WarehouseException
     */
    @Transactional
    public Double updateAlloc(BanQinKitAllocUpdateEntity updateEntity) throws WarehouseException {
        BanQinAllocInvLotLocEntity invLotLocEntity = updateEntity.getInvLotLocEntity();
        // 1、单位数量计算 - 更新数量
        Double qtyEaOp = getQtyAllocEaOp(updateEntity);
        invLotLocEntity.setQtyEaOp(qtyEaOp);// 操作数量EA
        // 操作单位数量
        invLotLocEntity.setQtyUomOp(wmCommon.doubleDivide(qtyEaOp, updateEntity.getPackageEntity().getCdprQuantity().doubleValue()));

        // 2、更新分配
        updateTaskKitBySplit(updateEntity);

        // 3、库存更新
        updateInventory(updateEntity);

        // 并发控制
        BanQinWmKitSubDetail subModel = banQinWmKitSubDetailService.getByKitNoAndSubLineNo(updateEntity.getKitNo(), updateEntity.getSubLineNo(), updateEntity.getOrgId());
        // 最新获取到的可分配数
        Double qtyAllocEaNew = subModel.getQtyPlanEa() - subModel.getQtyAllocEa() - subModel.getQtyPkEa() - subModel.getQtyKitEa();
        // 可分配数前后不一致，数据过期
        if (!qtyAllocEaNew.equals(updateEntity.getQtyAllocEa())) {
            throw new WarehouseException(updateEntity.getKitNo() + "-" + updateEntity.getSubLineNo() + "数据已过期");
        }
        // 4、更新子件行状态
        subModel.setQtyAllocEa(subModel.getQtyAllocEa() + qtyEaOp);
        subModel.setStatus(banQinWmKitSubDetailService.getStatus(subModel));
        this.banQinWmKitSubDetailService.save(subModel);

        // 返回剩余可分配数量
        Double qtyAllocEa = updateEntity.getQtyAllocEa() - qtyEaOp;
        return qtyAllocEa;
    }

    /**
     * 描述：分配更新数量计算
     * <p>
     * create by Jianhua on 2019/8/23
     */
    protected Double getQtyAllocEaOp(BanQinKitAllocUpdateEntity updateEntity) throws WarehouseException {
        // 可分配数计算
        BanQinWmInvLotLoc invLotLocModel = new BanQinWmInvLotLoc();
        BeanUtils.copyProperties(updateEntity.getInvLotLocEntity(), invLotLocModel);
        Double qtyInv = banQinWmInvLotLocService.getQtyAllocAvailable(invLotLocModel);
        // 单位数量计算 - 更新数量
        Double qtyEaOp = outboundCommon.qtyEaOpByUom(updateEntity.getQtyAllocEa(), qtyInv, updateEntity.getPackageEntity().getCdprQuantity());
        return qtyEaOp;
    }

    /**
     * 按单位拆分 更新分配明细 例如：单位PL，库存可分配单位数量超过1PL
     * 那么分配时，分配明细结果显示为：1PL显示一条分配记录
     */
    @Transactional
    public void updateTaskKitBySplit(BanQinKitAllocUpdateEntity updateEntity) throws WarehouseException {
        BanQinAllocInvLotLocEntity invLotLocEntity = updateEntity.getInvLotLocEntity();// 批次库位库存
        // 库存更新数
        Double qtyEa = invLotLocEntity.getQtyEaOp();// 缓存
        BanQinCdWhPackageRelationEntity packageEntity = updateEntity.getPackageEntity();// 包装
        // 是否拆单位标记,N:不拆分，Y:拆分
        String flag = WmsConstants.NO;
        // 校验是否拆单位更新分配明细
        // 如果不拆分,那么直接更新分配明细
        if (flag.equals(WmsConstants.NO)) {
            // 更新分配明细
            updateTaskKit(updateEntity);
        } else {
            // 拆单位更新
            int size = invLotLocEntity.getQtyUomOp().intValue();
            Double qtyEaOp = invLotLocEntity.getQtyEaOp();
            for (int i = 0; i < size; i++) {
                if (qtyEaOp <= 0) {
                    break;
                }
                // 写保存分配明细
                invLotLocEntity.setQtyEaOp(packageEntity.getCdprQuantity().doubleValue());
                updateTaskKit(updateEntity);
                // 剩余库存可分配数量
                qtyEaOp -= packageEntity.getCdprQuantity().doubleValue();
            }
            invLotLocEntity.setQtyEaOp(qtyEa);// 值回归
        }

    }

    /**
     * Description : 新增加工任务
     *
     * @param updateEntity
     * @return
     * @throws WarehouseException
     * @Author: Morice
     * @Create Date: 2014-7-2
     */
    @Transactional
    public void updateTaskKit(BanQinKitAllocUpdateEntity updateEntity) throws WarehouseException {
        BanQinAllocInvLotLocEntity invLotLocEntity = updateEntity.getInvLotLocEntity();// 批次库位库存
        BanQinCdWhPackageRelationEntity packageEntity = updateEntity.getPackageEntity();// 包装

        // 分配EA数量
        Double qtyEa = packageEntity.getCdprQuantity().doubleValue();
        // 新增加工任务
        BanQinWmTaskKit model = new BanQinWmTaskKit();
        model.setId(IdGen.uuid());
        model.setIsNewRecord(true);
        model.setHeaderId(updateEntity.getKitId());
        model.setOrgId(updateEntity.getOrgId());// 分公司
        // 分配ID
        String kitTaskId = noService.getDocumentNo(GenNoType.WM_KIT_ID.name());
        model.setKitTaskId(kitTaskId);
        model.setKitNo(updateEntity.getKitNo());// 加工单号
        model.setSubLineNo(updateEntity.getSubLineNo());// 子件行号
        model.setParentLineNo(updateEntity.getParentLineNo());// 子件行号
        model.setOwnerCode(invLotLocEntity.getOwnerCode());// 货主编码
        model.setSubSkuCode(invLotLocEntity.getSkuCode());// 商品编码
        model.setLotNum(invLotLocEntity.getLotNum());// 批次号
        model.setLocCode(invLotLocEntity.getLocCode());// 库位编码
        model.setTraceId(invLotLocEntity.getTraceId());// 分配库位跟踪号
        model.setStatus(WmsCodeMaster.SUB_KIT_FULL_ALLOC.getCode());// 完全分配状态

        model.setPackCode(packageEntity.getCdpaCode());// 包装编码
        model.setUom(packageEntity.getCdprUnitLevel());// 包装单位
        model.setQtyEa(invLotLocEntity.getQtyEaOp());// 操作数量
        model.setQtyUom(wmCommon.doubleDivide(invLotLocEntity.getQtyEaOp(), packageEntity.getCdprQuantity().doubleValue()));// 包装数量
        model.setToLoc(updateEntity.getPlanKitLoc());// 目标库位编码 = 计划加工台
        // 目标跟踪号
        String toId = null;
        // 分配参数：分配时是否自动产生跟踪号（Y：自动产生；N：默认为*）
        String allocTraceId = "N";/*wmCommon.getSysControlParam(ControlParamCode.ALLOC_TRACE_ID.getCode())*/
        if (allocTraceId.equals(WmsConstants.YES)) {
            toId = noService.getDocumentNo(GenNoType.WM_TRACE_ID.name());
        } else {
            toId = WmsConstants.TRACE_ID;
        }
        model.setToId(toId);// 目标跟踪号
        model.setPickOp(null);// 拣货人员
        model.setPickTime(null);// 拣货时间
        model.setKitOp(null);// 加工人员
        model.setKitTime(null);// 加工时间
        this.banQinWmTaskKitService.save(model);
    }

    /**
     * Description :库存更新
     *
     * @param updateEntity
     * @throws WarehouseException
     * @Author: Morice
     * @Create Date: 2014-7-2
     */
    @Transactional
    protected void updateInventory(BanQinKitAllocUpdateEntity updateEntity) throws WarehouseException {
        BanQinAllocInvLotLocEntity invLotLocEntity = updateEntity.getInvLotLocEntity();
        // 库存更新
        BanQinInventoryEntity invEntity = new BanQinInventoryEntity();
        invEntity.setAction(updateEntity.getActionCode());// 分配
        invEntity.setOwnerCode(invLotLocEntity.getOwnerCode());// 货主
        invEntity.setSkuCode(invLotLocEntity.getSkuCode());// 商品
        invEntity.setLotNum(invLotLocEntity.getLotNum());// 批次号
        invEntity.setLocCode(invLotLocEntity.getLocCode());// 库位
        invEntity.setTraceId(invLotLocEntity.getTraceId());// 跟踪号
        invEntity.setQtyEaOp(invLotLocEntity.getQtyEaOp());// 分配操作数量
        invEntity.setOrgId(invLotLocEntity.getOrgId());
        banQinInventoryService.updateInventory(invEntity);
    }
}
