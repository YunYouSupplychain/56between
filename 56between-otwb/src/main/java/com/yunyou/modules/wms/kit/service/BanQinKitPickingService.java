package com.yunyou.modules.wms.kit.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.modules.sys.GenNoType;
import com.yunyou.modules.sys.service.SynchronizedNoService;
import com.yunyou.modules.sys.utils.UserUtils;
import com.yunyou.modules.wms.common.entity.*;
import com.yunyou.modules.wms.common.service.BanQinWmsCommonService;
import com.yunyou.modules.wms.inventory.service.BanQinInventoryService;
import com.yunyou.modules.wms.kit.entity.BanQinWmKitSubDetail;
import com.yunyou.modules.wms.kit.entity.BanQinWmTaskKit;
import com.yunyou.modules.wms.kit.entity.extend.BanQinWmTaskKitEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 描述：加工管理- 子件拣货确认 
 * <p>
 * create by Jianhua on 2019/8/25
 */
@Service
public class BanQinKitPickingService {
    @Autowired
    protected BanQinWmsCommonService wmCommon;
    @Autowired
    protected SynchronizedNoService noService;
    @Autowired
    protected BanQinWmKitSubDetailService banQinWmKitSubDetailService;
    @Autowired
    protected BanQinWmTaskKitService banQinWmTaskKitService;
    @Autowired
    protected BanQinInventoryService banQinInventoryService;

    /**
     * 描述：拣货
     * <p>
     * create by Jianhua on 2019/8/25
     */
    @Transactional
    public ResultMessage kitPicking(BanQinWmTaskKitEntity entity) throws WarehouseException {
        ResultMessage msg = new ResultMessage();
        // 1、校验拣货位空值，为空，提示信息
        if (entity.getToLoc() == null || StringUtils.isEmpty(entity.getToLoc())) {
            // 拣货库位不能为空
            throw new WarehouseException(entity.getKitTaskId() + "拣货库位不能为空");
        }
        if (entity.getQtyEa() == 0) {
            throw new WarehouseException(entity.getKitTaskId() + "拣货数必须大于0");
        }

        // 2、加工任务数据过期校验
        BanQinWmTaskKit model = banQinWmTaskKitService.getByKitTaskId(entity.getKitTaskId(), entity.getOrgId());
        // 状态不符合，数据过期
        if (!model.getStatus().equals(WmsCodeMaster.SUB_KIT_FULL_ALLOC.getCode())) {
            throw new WarehouseException("[" + entity.getKitTaskId() + "]不是完全分配状态，不能操作");
        }
        // 分配数量版本号不一致，数据过期

        // 3、校验分配数量与拣货数量， 如果拣货数量=0/null,那么拣货数量=分配数量 如果不为空，分配数量>拣货数量(判断完全/部分拣货),
        // 计算 ？(前台界面换算？)=>单位换算分配单位数量与拣货单位数量
        // 部分拣货、完全拣货标记，true : 完全拣货，false:部分拣货
        boolean diffQtyFlag = true;
        if (entity.getQtyPkEa() != null && entity.getQtyPkEa() != 0 && (!entity.getQtyPkEa().equals(model.getQtyEa()))) {
            diffQtyFlag = false;
        } else {
            entity.setQtyPkEa(model.getQtyEa());// 完全拣货，拣货数量=分配数量
        }

        /* 4拣货任务单位修改，是否需要进行单位换算 */
        // 是否换单位，true : 单位不变，false:单位改变
        boolean diffUomFlag = true;
        if (!entity.getUom().equals(model.getUom())) {
            // 源单位与新单位比较，不相等，换单位拣货
            diffUomFlag = false;
        }

        // 4、获取包装单位
        BanQinCdWhPackageRelationEntity packageEntity = wmCommon.getPackageRelationAndQtyUom(entity.getPackCode(), entity.getUom(), entity.getQtyPkEa(), entity.getOrgId());

        // 5、更新分配明细
        // 5.1完全拣货
        if (diffQtyFlag) {
            // 如果换单位，那么换算数量
            if (!diffUomFlag) {
                model.setUom(entity.getUom());// 新拣货单位
                model.setQtyUom(packageEntity.getQtyUom());// 新拣货单位数量
            }
            model.setStatus(WmsCodeMaster.SUB_KIT_FULL_PICKING.getCode());// 完全拣货
            model.setToLoc(entity.getToLoc());// 目标拣货位
            model.setToId(entity.getToId());// 目标跟踪号
            model.setPickOp(UserUtils.getUser().getName());// 拣货人
            model.setPickTime(new Date());// 拣货时间，时区时间
            this.banQinWmTaskKitService.save(model);

        } else {
            // 5.2部分拣货
            // 剩余拣货数量
            Double newQtyPkEa = 0D;
            Double newQtyPkUom = 0D;
            String newUom = null;// 新单位
            // 如果换单位，那么换算数量
            if (!diffUomFlag) {
                BanQinCdWhPackageRelationEntity oldPackageEntity = wmCommon.getPackageRelationAndQtyUom(model.getPackCode(), model.getUom(), model.getOrgId());
                // 如果源单位>新单位，那么向下拆分单位
                if (oldPackageEntity.getCdprQuantity() > packageEntity.getCdprQuantity()) {
                    newQtyPkEa = model.getQtyEa() - entity.getQtyPkEa();
                    newQtyPkUom = wmCommon.doubleDivide(newQtyPkEa, packageEntity.getCdprQuantity().doubleValue());
                    newUom = packageEntity.getCdprUnitLevel();
                } else {
                    // 如果源单位<新单位，那么新分配记录，不拆分单位
                    newQtyPkEa = model.getQtyEa() - entity.getQtyPkEa();
                    newQtyPkUom = wmCommon.doubleDivide(newQtyPkEa, oldPackageEntity.getCdprQuantity().doubleValue());
                    newUom = oldPackageEntity.getCdprUnitLevel();
                }

                model.setUom(entity.getUom());// 新换算单位
            } else {
                // 不换单位
                newQtyPkEa = model.getQtyEa() - entity.getQtyPkEa();
                newQtyPkUom = wmCommon.doubleDivide(newQtyPkEa, packageEntity.getCdprQuantity().doubleValue());
                newUom = packageEntity.getCdprUnitLevel();
            }

            // 源分配记录
            model.setQtyEa(entity.getQtyPkEa());// 拣货数量EA
            model.setQtyUom(packageEntity.getQtyUom());// 拣货单位数量
            model.setStatus(WmsCodeMaster.SUB_KIT_FULL_PICKING.getCode());// 完全拣货
            model.setPickOp(UserUtils.getUser().getName());
            model.setPickTime(new Date());

            // 新增分配记录
            BanQinWmTaskKit newModel = new BanQinWmTaskKit();
            BeanUtils.copyProperties(model, newModel);
            newModel.setId(null);
            newModel.setStatus(WmsCodeMaster.SUB_KIT_FULL_ALLOC.getCode());// 完全分配
            // -
            // 新记录未拣货
            newModel.setQtyEa(newQtyPkEa);
            newModel.setQtyUom(newQtyPkUom);
            newModel.setUom(newUom);
            newModel.setPickOp(null);
            newModel.setPickTime(null);
            // 新分配记录的分配ID
            String kitTaskId = noService.getDocumentNo(GenNoType.WM_KIT_ID.name());
            newModel.setKitTaskId(kitTaskId);

            // 源分配记录，目标拣货位，目标跟踪号
            model.setToLoc(entity.getToLoc());// 目标拣货位
            model.setToId(entity.getToId());// 目标跟踪号
            this.banQinWmTaskKitService.save(model);
            // 新分配记录保存
            this.banQinWmTaskKitService.save(newModel);
        }

        // 6、更新库存、交易(操作数量=实际拣货数量)
        BanQinInventoryEntity invEntity = updateInventory(model);

        // 7、更新状态
        BanQinWmKitSubDetail subModel = this.banQinWmKitSubDetailService.getByKitNoAndSubLineNo(entity.getKitNo(), entity.getSubLineNo(), entity.getOrgId());
        if (subModel != null) {
            this.banQinWmKitSubDetailService.updateStatus(ActionCode.PICKING, entity.getQtyEa(), subModel);
        }

        // 出参 数据对象
        // 信息返回
        msg.setSuccess(true);
        return msg;
    }

    /**
     * 描述：库存交易更新
     * <p>
     * create by Jianhua on 2019/8/25
     */
    @Transactional
    protected BanQinInventoryEntity updateInventory(BanQinWmTaskKit model) throws WarehouseException {
        // 库存更新FM对象-分配
        BanQinInventoryEntity fromInvEntity = new BanQinInventoryEntity();
        fromInvEntity.setAction(ActionCode.PICKING);// ACTION_CODE 拣货
        fromInvEntity.setOwnerCode(model.getOwnerCode());// 货主
        fromInvEntity.setSkuCode(model.getSubSkuCode());// 商品
        fromInvEntity.setLotNum(model.getLotNum());// 批次号
        // 原库位跟踪号移动
        fromInvEntity.setLocCode(model.getLocCode());// 分配库位
        fromInvEntity.setTraceId(model.getTraceId());// 分配库位跟踪号

        fromInvEntity.setQtyEaOp(model.getQtyEa());// 拣货数量=分配数量
        fromInvEntity.setOrderNo(model.getKitNo());// 加工单号
        fromInvEntity.setLineNo(model.getSubLineNo());// 加工单子件行号
        fromInvEntity.setTaskId(model.getKitTaskId());// 任务ID
        fromInvEntity.setPackCode(model.getPackCode());// 包装代码
        fromInvEntity.setUom(model.getUom());// 包装单位
        fromInvEntity.setQtyUom(model.getQtyUom());// 包装单位换算数量
        fromInvEntity.setOrgId(model.getOrgId());
        // 库存更新TO对象-目标拣货
        BanQinInventoryEntity toInvEntity = new BanQinInventoryEntity();
        toInvEntity.setAction(ActionCode.PICKING);// ACTION_CODE 拣货
        toInvEntity.setOwnerCode(model.getOwnerCode());// 货主
        toInvEntity.setSkuCode(model.getSubSkuCode());// 商品
        toInvEntity.setLotNum(model.getLotNum());// 批次号
        // 移动到目标库位跟踪号
        toInvEntity.setLocCode(model.getToLoc());// 拣货目标库位
        toInvEntity.setTraceId(model.getToId());// 拣货目标跟踪号

        toInvEntity.setQtyEaOp(model.getQtyEa());// 拣货数量
        toInvEntity.setOrderNo(model.getKitNo());// 加工单号
        toInvEntity.setLineNo(model.getSubLineNo());// 加工单子件行号
        toInvEntity.setTaskId(model.getKitTaskId());// 任务ID
        toInvEntity.setPackCode(model.getPackCode());// 包装代码
        toInvEntity.setUom(model.getUom());// 包装单位
        toInvEntity.setQtyUom(model.getQtyUom());// 包装单位换算数量
        toInvEntity.setOrgId(model.getOrgId());
        return banQinInventoryService.updateInventory(fromInvEntity, toInvEntity);
    }
}
