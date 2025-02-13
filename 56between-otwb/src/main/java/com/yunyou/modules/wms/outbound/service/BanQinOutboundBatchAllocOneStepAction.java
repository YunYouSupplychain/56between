package com.yunyou.modules.wms.outbound.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.modules.wms.basicdata.entity.*;
import com.yunyou.modules.wms.basicdata.service.*;
import com.yunyou.modules.wms.common.entity.*;
import com.yunyou.modules.wms.common.service.BanQinWmsCommonService;
import com.yunyou.modules.wms.inventory.entity.BanQinWmInvLot;
import com.yunyou.modules.wms.inventory.service.BanQinInvReplenishmentService;
import com.yunyou.modules.wms.outbound.entity.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 一步分配  按波次号、按出库单号、按出库单+行号
 *
 * @author WMJ
 * @version 2019/02/20
 */
@Service
public class BanQinOutboundBatchAllocOneStepAction {
    // 分配
    @Autowired
    protected BanQinOutboundAllocService outboundAllocService;
    // 公共方法
    @Autowired
    protected BanQinWmsCommonService wmCommon;
    // 出库公共
    @Autowired
    protected BanQinOutboundCommonService outboundCommon;
    // 出库单
    @Autowired
    protected BanQinWmSoHeaderService wmSoHeaderService;
    // 出库单行
    @Autowired
    protected BanQinWmSoDetailService wmSoDetailService;
    // 分配规则
    @Autowired
    protected BanQinCdRuleAllocDetailService cdRuleAllocDetailService;
    // 库存周转规则header
    @Autowired
    protected BanQinCdRuleRotationHeaderService cdRuleRotationHeaderService;
    // 库存周转规则Detail
    @Autowired
    protected BanQinCdRuleRotationDetailService cdRuleRotationDetailService;
    // 商品
    @Autowired
    protected BanQinCdWhSkuService cdWhSkuService;
    // 商品拣货区
    @Autowired
    protected BanQinCdWhSkuLocService cdWhSkuLocService;
    // 补货
    @Autowired
    protected BanQinInvReplenishmentService invReplenishment;

    /**
     * 一步分配
     *
     * @param processByCode
     * @param noList
     * @return
     */
    public ResultMessage outboundBatchAllocOneStep(String processByCode, List<String> noList, String orgId) {
        ResultMessage msg = new ResultMessage();
        // 1、拦截、冻结状态校验
        // 拦截状态校验
        ResultMessage message = wmSoHeaderService.checkBatchInterceptStatus(processByCode, noList, orgId);
        if (!message.isSuccess()) {
            msg.addMessage(message.getMessage());
        }
        // 冻结状态校验
        message = wmSoHeaderService.checkBatchHoldStatus(processByCode, noList, orgId);
        if (!message.isSuccess()) {
            msg.addMessage(message.getMessage());
        }
        // 2、获取出库单行，过滤出库单行取消状态，出库单的取消、关闭状态、拦截、冻结状态
        List<BanQinWmSoDetailEntity> soDetailList = wmSoDetailService.findEntityByProcessCode(processByCode, noList, orgId);
        if (CollectionUtil.isEmpty(soDetailList)) {
            // 没有可以操作的出库单行
            msg.addMessage("没有可以操作的出库单行");
            msg.setSuccess(false);
            return msg;
        }
        try {
            // 循环出库单明细行
            for (BanQinWmSoDetailEntity soDetailItem : soDetailList) {
                try {
                    if (StringUtils.isNotEmpty(soDetailItem.getCdType())) {
                        // 只能在越库任务界面执行出库，[{0}][{1}]属于越库，不能操作
                        msg.addMessage("只能在越库任务界面执行出库," + soDetailItem.getSoNo() + soDetailItem.getLineNo() + "属于越库，不能操作");
                        msg.setSuccess(false);
                        continue;
                    }
                    // 1、获取基本数据
                    // 1.1、获取出库单
                    BanQinWmSoHeader wmSoHeaderModel = wmSoHeaderService.findBySoNo(soDetailItem.getSoNo(), soDetailItem.getOrgId());
                    // 1.2、分配规则明细获取
                    List<BanQinCdRuleAllocDetail> ruleAllocDetailList = cdRuleAllocDetailService.getByRuleCode(soDetailItem.getAllocRule(), soDetailItem.getOrgId());
                    if (ruleAllocDetailList.size() == 0) {
                        // 查询不到分配规则{0}明细
                        msg.addMessage("查询不到分配规则" + soDetailItem.getAllocRule());
                        msg.setSuccess(false);
                        continue;
                    }
                    // 1.3、获取库存周转规则明细
                    List<BanQinCdRuleRotationDetail> ruleRotationDetailList = cdRuleRotationDetailService.getByRuleCode(soDetailItem.getRotationRule(), soDetailItem.getOrgId());
                    // 1.4、包装单位换算信息获取
                    BanQinCdWhPackageEntity packageEntity = wmCommon.getCdWhPackageRelation(soDetailItem.getPackCode(), soDetailItem.getOrgId());
                    // 1.5、商品信息获取
                    BanQinCdWhSku cdWhSkuModel = cdWhSkuService.getByOwnerAndSkuCode(soDetailItem.getOwnerCode(), soDetailItem.getSkuCode(), soDetailItem.getOrgId());
                    // 分配前基本数据
                    BanQinAllocForAllocRuleEntity entity = new BanQinAllocForAllocRuleEntity();
                    entity.setCdWhSkuModel(cdWhSkuModel);// 商品
                    entity.setRuleAllocDetailList(ruleAllocDetailList);// 分配规则明细
                    entity.setRuleRotationDetailList(ruleRotationDetailList);// 库存周转规则明细
                    entity.setConsigneeCode(wmSoHeaderModel.getConsigneeCode());// 收货人
                    entity.setTrackingNo(wmSoHeaderModel.getTrackingNo());// 快递单号
                    entity.setWmSoDetailItem(soDetailItem);// 出库单明细行
                    entity.setPackageRelationList(packageEntity.getCdWhPackageRelations());// 包装
                    // 2、库存周转规则header - 需要区分是按库存周转优先，还是按包装优先
                    BanQinCdRuleRotationHeader cdRuleRotationHeaderModel = cdRuleRotationHeaderService.findByRuleCode(soDetailItem.getRotationRule(), soDetailItem.getOrgId());
                    if (cdRuleRotationHeaderModel == null) {
                        // 查询不到库存周转规则
                        msg.addMessage("查询不到库存周转规则" + soDetailItem.getRotationRule());
                        msg.setSuccess(false);
                        continue;
                    }
                    // 2.1、库存周转优先
                    if (cdRuleRotationHeaderModel.getRotationType().equals(WmsCodeMaster.ROTATION.getCode())) {
                        allocByRotation(entity);
                    }
                    // 2.2、包装优先
                    else if (cdRuleRotationHeaderModel.getRotationType().equals(WmsCodeMaster.PACKAGE.getCode())) {
                        allocByPackage(entity);
                    }
                } catch (WarehouseException e) {
                    msg.addMessage(e.getMessage());
                    msg.setSuccess(false);
                }
            }
        } catch (Throwable e) {
            // 框架控制，如数据过期，抛出提示
            throw new RuntimeException(e.getMessage());
        } finally {
            if (StringUtils.isEmpty(msg.getMessage())) {
                msg.addMessage("操作成功");
            }
        }
        return msg;
    }

    /**
     * 一步分配 库存周转优先
     * @param entity
     * @return
     */
    public ResultMessage allocByRotation(BanQinAllocForAllocRuleEntity entity) {
        ResultMessage msg = new ResultMessage();
        // 分配明细
        List<BanQinCdRuleAllocDetail> ruleAllocDetailList = entity.getRuleAllocDetailList();
        // 出库单行
        BanQinWmSoDetailEntity soDetailItem = entity.getWmSoDetailItem();
        // 库存周转规则查询语句条件设置
        BanQinCdRuleRotationSqlParamEntity sqlParamEntity = new BanQinCdRuleRotationSqlParamEntity();
        // 库存周转规则明细
        sqlParamEntity.setRuleRotationDetailList(entity.getRuleRotationDetailList());
        // 出库单明细行12个批次属性
        sqlParamEntity.setLotAtt01(soDetailItem.getLotAtt01());
        sqlParamEntity.setLotAtt02(soDetailItem.getLotAtt02());
        sqlParamEntity.setLotAtt03(soDetailItem.getLotAtt03());
        sqlParamEntity.setLotAtt04(soDetailItem.getLotAtt04());
        sqlParamEntity.setLotAtt05(soDetailItem.getLotAtt05());
        sqlParamEntity.setLotAtt06(soDetailItem.getLotAtt06());
        sqlParamEntity.setLotAtt07(soDetailItem.getLotAtt07());
        sqlParamEntity.setLotAtt08(soDetailItem.getLotAtt08());
        sqlParamEntity.setLotAtt09(soDetailItem.getLotAtt09());
        sqlParamEntity.setLotAtt10(soDetailItem.getLotAtt10());
        sqlParamEntity.setLotAtt11(soDetailItem.getLotAtt11());
        sqlParamEntity.setLotAtt12(soDetailItem.getLotAtt12());
        try {
            // 1、获取批次库存记录
            List<BanQinWmInvLot> invLotList = outboundCommon.getInvLotByRuleRotation(soDetailItem.getOwnerCode(), soDetailItem.getSkuCode(), soDetailItem.getOrgId(), sqlParamEntity);
            if (invLotList.size() == 0) {
                // 出库单行{0}没有分配记录([{0}][{1}]没有分配记录)
                msg.addMessage("出库单行" + soDetailItem.getSoNo() + "没有分配记录" + soDetailItem.getLineNo());
                msg.setSuccess(false);
                return msg;
            }
            // 可分配数量
            BanQinWmSoDetail wmSoDetailModel = new BanQinWmSoDetail();
            BeanUtils.copyProperties(soDetailItem, wmSoDetailModel);
            Double qtyAllocEa = wmSoDetailService.getAvailableQtySoEa(wmSoDetailModel);// 可分配数量
            // 无可分配数量
            if (qtyAllocEa <= 0D) {
                return msg;
            }
            // 2、循环批次号
            for (BanQinWmInvLot invLotModel : invLotList) {
                // 3、循环分配明细
                for (BanQinCdRuleAllocDetail ruleAllocDetailModel : ruleAllocDetailList) {
                    try {
                        if (qtyAllocEa <= 0D) {
                            return msg;
                        }
                        // 包装单位
                        BanQinCdWhPackageRelationEntity packageEntity = null;
                        try {
                            packageEntity = wmCommon.getPackageRelationAndQtyUom(entity.getPackageRelationList(), ruleAllocDetailModel.getUom(), qtyAllocEa);
                        } catch (WarehouseException e) {
                            // 查询不到包装单位，不抛提示，接着往下执行
                            continue;
                        }
                        // 分配单位数量 = 可分配数量EA/分配明细单位换算数量 => 可分配数量EA 根据分配规则明细单位
                        // 换算单位数量
                        Double qtyAllocUom = wmCommon.doubleDivide(qtyAllocEa, packageEntity.getCdprQuantity().doubleValue());
                        // 不及一单位数量，循环下一个分配单位
                        if (qtyAllocUom < 1D) {
                            continue;
                        }
                        // 获取批次库位库存 入参
                        BanQinAllocInvLotLocSqlParamlEntity invSqlParamEntity = new BanQinAllocInvLotLocSqlParamlEntity();
                        invSqlParamEntity.setCdWhSkuModel(entity.getCdWhSkuModel());// 商品，主要用于校验出库效期
                        invSqlParamEntity.setOwnerCode(wmSoDetailModel.getOwnerCode());// 货主
                        invSqlParamEntity.setSkuCode(wmSoDetailModel.getSkuCode());// 商品
                        invSqlParamEntity.setAreaCode(wmSoDetailModel.getAreaCode());// 出库单行区域
                        invSqlParamEntity.setZoneCode(wmSoDetailModel.getZoneCode());// 出库单行拣货区
                        invSqlParamEntity.setLocCode(wmSoDetailModel.getLocCode());// 出库单行拣货位
                        invSqlParamEntity.setTraceId(wmSoDetailModel.getTraceId());// 出库单行跟踪号
                        invSqlParamEntity.setLocUseType(ruleAllocDetailModel.getLocUseType());// 库位使用类型
                        invSqlParamEntity.setIsClearFirst(ruleAllocDetailModel.getIsClearFirst());// 清仓优先
                        invSqlParamEntity.setLotNum(invLotModel.getLotNum());// 批次号
                        invSqlParamEntity.setQtyPackUom(packageEntity.getCdprQuantity());// 单位换算数量
                        // 商品固定拣货位
                        BanQinCdWhSkuLoc cdWhSkuLocModel = null;
                        if (StringUtils.isNotEmpty(ruleAllocDetailModel.getSkuLocType())) {
                            // 商品拣货位
                            cdWhSkuLocModel = cdWhSkuLocService.getCdWhSkuLocType(wmSoDetailModel.getOwnerCode(), wmSoDetailModel.getSkuCode(), ruleAllocDetailModel.getSkuLocType(), ruleAllocDetailModel.getOrgId());
                            if (cdWhSkuLocModel == null) {
                                // 分配规则配置商品固定拣货位，但商品未配置商品拣货位，则继续执行下一条分配规则明细
                                continue;
                            }
                            invSqlParamEntity.setSkuLocCode(cdWhSkuLocModel.getLocCode());// 商品固定拣货位
                        }
                        // 1、获取批次库位库存
                        List<BanQinAllocInvLotLocEntity> invLotLocList = outboundCommon.getInvLotLocByRotation(invSqlParamEntity, wmSoDetailModel.getOrgId());
                        // 2、循环批次库位库存更新
                        BanQinAllocForInvLotLocEntity forInvEntity = new BanQinAllocForInvLotLocEntity();
                        forInvEntity.setRuleAllocDetailModel(ruleAllocDetailModel);// 分配规则明细
                        forInvEntity.setInvLotLocList(invLotLocList);// 批次库位库存
                        forInvEntity.setCdWhSkuLocModel(cdWhSkuLocModel);// 商品固定拣货位
                        forInvEntity.setWmSoDetailItem(soDetailItem);// 出库单明细行
                        forInvEntity.setQtyAllocEa(qtyAllocEa);// 可分配数量
                        forInvEntity.setPackageEntity(packageEntity);// 包装
                        forInvEntity.setConsigneeCode(entity.getConsigneeCode());// 收货人
                        forInvEntity.setTrackingNo(entity.getTrackingNo());// 快递单号
                        // 3、调用循环批次库位库存更新，超量分配
                        ResultMessage message = allocForInvLotLoc(forInvEntity);
                        if (!message.isSuccess()) {
                            msg.addMessage(message.getMessage());
                            msg.setSuccess(false);
                            continue;
                        }
                        // 剩余可分配数量
                        qtyAllocEa = (Double) message.getData();

                        /*******************************************************
                         * 超量分配逻辑开始
                         ******************************************************/
                        // 如果没有配置固定拣货位，那么不能进行超量分配
                        if (ruleAllocDetailModel.getSkuLocType() == null) {
                            continue;
                        }
                        // 如果不允许超量分配，那么退出
                        if (cdWhSkuLocModel == null || cdWhSkuLocModel.getIsOverAlloc().equals(WmsConstants.NO)) {
                            continue;
                        }
                        // 如果无剩余可分配数量，那么退出
                        if (qtyAllocEa <= 0) {
                            return msg;
                        }
                        // 剩余可分配单位数量
                        qtyAllocUom = wmCommon.doubleDivide(qtyAllocEa, packageEntity.getCdprQuantity().doubleValue());
                        // 超量分配，分配规则固定拣货位不为空，并且超量分配，可分配单位数量
                        // 如果剩余可分配单位数量<1，那么退出
                        if (qtyAllocUom < 1) {
                            continue;
                        }
                        // 库存获取,根据库存周转规则排序，获取批次库存可用数
                        BanQinAllocInvLotLocEntity invLotLocEntity = outboundCommon.getOverInvLot(qtyAllocUom, packageEntity.getCdprQuantity().doubleValue(),
                                cdWhSkuLocModel.getLocCode(), wmSoDetailModel.getOwnerCode(), wmSoDetailModel.getSkuCode(), invLotModel.getLotNum(), invLotModel.getOrgId());
                        // 更新库存
                        if (invLotLocEntity == null) {
                            continue;
                        }
                        // 分配更新
                        BanQinAllocUpdateEntity updateEntity = new BanQinAllocUpdateEntity();
                        updateEntity.setActionCode(ActionCode.ONESTEP_OVER_ALLOCATION);// 两步超量分配\一步超量分配
                        updateEntity.setSoNo(soDetailItem.getSoNo());// 出库单
                        updateEntity.setLineNo(soDetailItem.getLineNo());// 出库单行号
                        updateEntity.setWaveNo(soDetailItem.getWaveNo());// 波次号
                        updateEntity.setPackageEntity(packageEntity);// 包装
                        updateEntity.setConsigneeCode(entity.getConsigneeCode());// 收货人
                        updateEntity.setTrackingNo(entity.getTrackingNo());// 快递单号
                        updateEntity.setInvLotLocEntity(invLotLocEntity);// 批次库位库存
                        updateEntity.setQtyAllocEa(qtyAllocEa);// 分配数量
                        // 一步分配更新
                        qtyAllocEa = outboundAllocService.updateAllocOneStep(updateEntity);
                    } catch (WarehouseException e) {
                        msg.addMessage(e.getMessage());
                        msg.setSuccess(false);
                    }
                }
            }
        } catch (WarehouseException e) {
            msg.addMessage(e.getMessage());
            msg.setSuccess(false);
        }
        return msg;
    }

    /**
     * 一步分配 包装优先
     * @param entity
     * @return
     */
    public ResultMessage allocByPackage(BanQinAllocForAllocRuleEntity entity) {
        ResultMessage msg = new ResultMessage();
        // 按分配规则明细循环分配
        // 分配明细
        List<BanQinCdRuleAllocDetail> ruleAllocDetailList = entity.getRuleAllocDetailList();
        // 出库单行
        BanQinWmSoDetailEntity soDetailItem = entity.getWmSoDetailItem();
        // 可分配数量
        BanQinWmSoDetail wmSoDetailModel = new BanQinWmSoDetail();
        BeanUtils.copyProperties(soDetailItem, wmSoDetailModel);
        Double qtyAllocEa = wmSoDetailService.getAvailableQtySoEa(wmSoDetailModel);// 可分配数量
        // 循环分配明细
        for (BanQinCdRuleAllocDetail ruleAllocDetailModel : ruleAllocDetailList) {
            try {
                if (qtyAllocEa <= 0) {
                    break;
                }
                // 包装单位
                BanQinCdWhPackageRelationEntity packageEntity = null;
                try {
                    packageEntity = wmCommon.getPackageRelationAndQtyUom(entity.getPackageRelationList(), ruleAllocDetailModel.getUom(), qtyAllocEa);
                } catch (WarehouseException e) {
                    // 查询不到包装单位，不抛提示，接着往下执行
                    continue;
                }
                // 分配单位数量 = 可分配数量EA/分配明细单位换算数量 => 可分配数量EA 根据分配规则明细单位 换算单位数量
                Double qtyAllocUom = wmCommon.doubleDivide(qtyAllocEa, packageEntity.getCdprQuantity().doubleValue());
                // 不及一单位数量，循环下一个分配单位
                if (qtyAllocUom < 1D) {
                    continue;
                }
                // 商品拣货位

                // 获取批次库位库存 入参
                BanQinAllocInvLotLocSqlParamlEntity invSqlParamEntity = new BanQinAllocInvLotLocSqlParamlEntity();
                invSqlParamEntity.setCdWhSkuModel(entity.getCdWhSkuModel());// 商品，主要用于校验出库效期
                invSqlParamEntity.setOwnerCode(soDetailItem.getOwnerCode());// 货主
                invSqlParamEntity.setSkuCode(soDetailItem.getSkuCode());// 商品
                invSqlParamEntity.setAreaCode(soDetailItem.getAreaCode());// 出库单行区域
                invSqlParamEntity.setZoneCode(soDetailItem.getZoneCode());// 出库单行拣货区
                invSqlParamEntity.setLocCode(soDetailItem.getLocCode());// 出库单行拣货位
                invSqlParamEntity.setTraceId(soDetailItem.getTraceId());// 出库单行跟踪号
                invSqlParamEntity.setLocUseType(ruleAllocDetailModel.getLocUseType());// 库位使用类型
                invSqlParamEntity.setIsClearFirst(ruleAllocDetailModel.getIsClearFirst());// 清仓优先
                invSqlParamEntity.setQtyPackUom(packageEntity.getCdprQuantity());// 单位换算数量
                // 商品固定拣货位
                BanQinCdWhSkuLoc cdWhSkuLocModel = null;
                if (ruleAllocDetailModel.getSkuLocType() != null) {
                    // 商品拣货位
                    cdWhSkuLocModel = cdWhSkuLocService.getCdWhSkuLocType(wmSoDetailModel.getOwnerCode(), wmSoDetailModel.getSkuCode(), ruleAllocDetailModel.getSkuLocType(), ruleAllocDetailModel.getOrgId());
                    if (cdWhSkuLocModel == null) {
                        // 分配规则配置商品固定拣货位，但商品未配置商品拣货位，则继续执行下一条分配规则明细
                        continue;
                    }
                    invSqlParamEntity.setSkuLocCode(cdWhSkuLocModel.getLocCode());// 商品固定拣货位
                }
                // 库存周转规则查询条件
                BanQinCdRuleRotationSqlParamEntity ruleRotationSqlParamEntity = new BanQinCdRuleRotationSqlParamEntity();
                ruleRotationSqlParamEntity.setRuleRotationDetailList(entity.getRuleRotationDetailList());
                // 出库单明细行12个批次属性值
                ruleRotationSqlParamEntity.setLotAtt01(soDetailItem.getLotAtt01());
                ruleRotationSqlParamEntity.setLotAtt02(soDetailItem.getLotAtt02());
                ruleRotationSqlParamEntity.setLotAtt03(soDetailItem.getLotAtt03());
                ruleRotationSqlParamEntity.setLotAtt04(soDetailItem.getLotAtt04());
                ruleRotationSqlParamEntity.setLotAtt05(soDetailItem.getLotAtt05());
                ruleRotationSqlParamEntity.setLotAtt06(soDetailItem.getLotAtt06());
                ruleRotationSqlParamEntity.setLotAtt07(soDetailItem.getLotAtt07());
                ruleRotationSqlParamEntity.setLotAtt08(soDetailItem.getLotAtt08());
                ruleRotationSqlParamEntity.setLotAtt09(soDetailItem.getLotAtt09());
                ruleRotationSqlParamEntity.setLotAtt10(soDetailItem.getLotAtt10());
                ruleRotationSqlParamEntity.setLotAtt11(soDetailItem.getLotAtt11());
                ruleRotationSqlParamEntity.setLotAtt12(soDetailItem.getLotAtt12());
                invSqlParamEntity.setRuleRotationSqlParamEntity(ruleRotationSqlParamEntity);
                // 1、获取批次库位库存
                List<BanQinAllocInvLotLocEntity> invLotLocList = outboundCommon.getInvLotLocByPackage(invSqlParamEntity, soDetailItem.getOrgId());
                // 2、循环批次库位库存更新
                BanQinAllocForInvLotLocEntity forInvEntity = new BanQinAllocForInvLotLocEntity();
                forInvEntity.setRuleAllocDetailModel(ruleAllocDetailModel);// 分配规则明细
                forInvEntity.setInvLotLocList(invLotLocList);// 批次库位库存
                forInvEntity.setCdWhSkuLocModel(cdWhSkuLocModel);// 商品固定拣货位
                forInvEntity.setWmSoDetailItem(soDetailItem);// 出库单明细行
                forInvEntity.setQtyAllocEa(qtyAllocEa);// 可分配数量
                forInvEntity.setPackageEntity(packageEntity);// 包装
                forInvEntity.setConsigneeCode(entity.getConsigneeCode());// 收货人
                forInvEntity.setTrackingNo(entity.getTrackingNo());// 快递单号
                forInvEntity.setRuleRotationSqlParamEntity(ruleRotationSqlParamEntity);// 库存周转规则条件
                forInvEntity.setCdWhSkuModel(entity.getCdWhSkuModel());// 商品基础信息
                // 3、调用循环批次库位库存更新，超量分配
                ResultMessage message = allocForInvLotLoc(forInvEntity);
                if (!message.isSuccess()) {
                    msg.addMessage(message.getMessage());
                    msg.setSuccess(false);
                    continue;
                }
                // 剩余可分配数量
                qtyAllocEa = (Double) message.getData();

                /***************************************************************
                 * 超量分配逻辑开始
                 **************************************************************/
                // 如果没有配置固定拣货位，那么不能进行超量分配
                if (ruleAllocDetailModel.getSkuLocType() == null) {
                    continue;
                }
                // 如果不允许超量分配，那么退出
                if (cdWhSkuLocModel == null || cdWhSkuLocModel.getIsOverAlloc().equals(WmsConstants.NO)) {
                    continue;
                }
                // 如果无剩余可分配数量，那么退出
                if (qtyAllocEa <= 0) {
                    break;
                }
                // 剩余可分配单位数量
                qtyAllocUom = wmCommon.doubleDivide(qtyAllocEa, packageEntity.getCdprQuantity().doubleValue());
                // 超量分配，分配规则固定拣货位不为空，并且超量分配，可分配单位数量
                // 如果剩余可分配单位数量<1，那么退出
                if (qtyAllocUom < 1) {
                    continue;
                }
                // 库存获取
                List<BanQinAllocInvLotLocEntity> overInvLotLocList = outboundCommon.getOverInvLotLoc(packageEntity.getCdprQuantity().doubleValue(), cdWhSkuLocModel.getLocCode(),
                        soDetailItem.getOwnerCode(), soDetailItem.getSkuCode(), soDetailItem.getOrgId(), ruleRotationSqlParamEntity, entity.getCdWhSkuModel());
                // 更新库存
                if (overInvLotLocList.size() == 0) {
                    continue;
                }
                for (BanQinAllocInvLotLocEntity invLotLocEntity : overInvLotLocList) {
                    if (qtyAllocEa <= 0) {
                        break;
                    }
                    // 剩余可分配单位数量
                    qtyAllocUom = wmCommon.doubleDivide(qtyAllocEa, packageEntity.getCdprQuantity().doubleValue());
                    // 超量分配，分配规则固定拣货位不为空，并且超量分配，可分配单位数量
                    // 如果剩余可分配单位数量<1，那么退出
                    if (qtyAllocUom < 1) {
                        break;
                    }
                    try {
                        // 分配更新
                        // 库存更新类型
                        BanQinAllocUpdateEntity updateEntity = new BanQinAllocUpdateEntity();
                        updateEntity.setActionCode(ActionCode.ONESTEP_OVER_ALLOCATION);// 两步超量分配\一步超量分配
                        updateEntity.setSoNo(soDetailItem.getSoNo());// 出库单
                        updateEntity.setLineNo(soDetailItem.getLineNo());// 出库单行号
                        updateEntity.setWaveNo(soDetailItem.getWaveNo());// 波次号
                        updateEntity.setPackageEntity(packageEntity);// 包装
                        updateEntity.setConsigneeCode(entity.getConsigneeCode());// 收货人
                        updateEntity.setTrackingNo(entity.getTrackingNo());// 快递单号
                        updateEntity.setInvLotLocEntity(invLotLocEntity);// 批次库位库存
                        updateEntity.setQtyAllocEa(qtyAllocEa);// 分配数量
                        // 一步分配更新
                        qtyAllocEa = outboundAllocService.updateAllocOneStep(updateEntity);
                    } catch (WarehouseException e) {
                        msg.addMessage(e.getMessage());
                        msg.setSuccess(false);
                    }
                }
                /***************************************************************
                 * 超量分配逻辑结束
                 **************************************************************/
            } catch (WarehouseException e) {
                msg.addMessage(e.getMessage());
                msg.setSuccess(false);
            }
        }
        return msg;
    }

    /**
     * 按库存循环更新
     * @param entity
     * @return
     */
    public ResultMessage allocForInvLotLoc(BanQinAllocForInvLotLocEntity entity) {
        ResultMessage msg = new ResultMessage();
        List<BanQinAllocInvLotLocEntity> invLotLocList = entity.getInvLotLocList();
        BanQinCdWhPackageRelationEntity packageEntity = entity.getPackageEntity();
        BanQinWmSoDetailEntity wmSoDetailItem = entity.getWmSoDetailItem();
        // 可分配数量
        Double qtyAllocEa = entity.getQtyAllocEa();
        // 1、循环库存更新
        for (BanQinAllocInvLotLocEntity invLotLocEntity : invLotLocList) {
            Double qtyAllocUom = wmCommon.doubleDivide(qtyAllocEa, packageEntity.getCdprQuantity().doubleValue());
            if (qtyAllocUom < 1D) {// 无可分配整单位数量
                break;
            }
            try {
                BanQinAllocUpdateEntity updateEntity = new BanQinAllocUpdateEntity();
                updateEntity.setActionCode(ActionCode.ONESTEP_ALLOCATION);// 一步分配
                updateEntity.setSoNo(wmSoDetailItem.getSoNo());// 出库单
                updateEntity.setLineNo(wmSoDetailItem.getLineNo());// 出库单行号
                updateEntity.setWaveNo(wmSoDetailItem.getWaveNo());// 波次号
                updateEntity.setPackageEntity(packageEntity);// 包装
                updateEntity.setConsigneeCode(entity.getConsigneeCode());// 收货人
                updateEntity.setTrackingNo(entity.getTrackingNo());// 快递单号
                updateEntity.setInvLotLocEntity(invLotLocEntity);// 批次库位库存
                updateEntity.setQtyAllocEa(qtyAllocEa);// 分配数量
                // 分配更新，并且返回剩余可分配数量
                qtyAllocEa = outboundAllocService.updateAllocOneStep(updateEntity);
            } catch (WarehouseException e) {
                msg.addMessage(e.getMessage());
                msg.setSuccess(false);
            }
        }
        // 返回剩余可分配数量
        msg.setData(qtyAllocEa);
        return msg;
    }

}