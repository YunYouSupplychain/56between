package com.yunyou.modules.wms.kit.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.modules.wms.basicdata.entity.*;
import com.yunyou.modules.wms.basicdata.service.*;
import com.yunyou.modules.wms.common.entity.*;
import com.yunyou.modules.wms.common.service.BanQinWmsCommonService;
import com.yunyou.modules.wms.inventory.entity.BanQinWmInvLot;
import com.yunyou.modules.wms.kit.entity.extend.BanQinKitAllocForInvEntity;
import com.yunyou.modules.wms.kit.entity.extend.BanQinKitAllocForRuleEntity;
import com.yunyou.modules.wms.kit.entity.extend.BanQinKitAllocUpdateEntity;
import com.yunyou.modules.wms.kit.entity.extend.BanQinWmKitSubDetailEntity;
import com.yunyou.modules.wms.outbound.entity.BanQinAllocInvLotLocEntity;
import com.yunyou.modules.wms.outbound.entity.BanQinAllocInvLotLocSqlParamlEntity;
import com.yunyou.modules.wms.outbound.service.BanQinOutboundCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 描述：加工管理-子件分配(一步分配)- 批量
 * <p>
 * create by Jianhua on 2019/8/25
 */
@Service
public class BanQinKitBatchAllocService {
    @Autowired
    protected BanQinWmsCommonService wmCommon;
    @Autowired
    protected BanQinOutboundCommonService outboundCommon;
    @Autowired
    protected BanQinWmKitSubDetailService banQinWmKitSubDetailService;
    @Autowired
    protected BanQinCdRuleAllocDetailService banQinCdRuleAllocDetailService;
    @Autowired
    protected BanQinCdRuleRotationHeaderService banQinCdRuleRotationHeaderService;
    @Autowired
    protected BanQinCdRuleRotationDetailService banQinCdRuleRotationDetailService;
    @Autowired
    protected BanQinCdWhSkuService banQinCdWhSkuService;
    @Autowired
    protected BanQinCdWhSkuLocService banQinCdWhSkuLocService;
    @Autowired
    protected BanQinKitAllocService banQinKitAllocService;

    /**
     * 描述：子件一步分配
     * <p>
     * create by Jianhua on 2019/8/25
     */
    public ResultMessage kitBatchAlloc(String kitNo, List<String> subLineNos, String orgId) {
        ResultMessage msg = new ResultMessage();
        // 获取子件明细，过滤掉子件类型为辅料的信息
        List<BanQinWmKitSubDetailEntity> kitSubDetailEntities = getAvailableSubDetail(kitNo, subLineNos, orgId);
        if (CollectionUtil.isEmpty(kitSubDetailEntities)) {
            msg.addMessage("没有可以操作的子件明细行");
            msg.setSuccess(false);
            return msg;
        }
        try {
            // 循环子件明细
            for (BanQinWmKitSubDetailEntity subEntity : kitSubDetailEntities) {
                try {
                    // 1.2、分配规则明细获取
                    List<BanQinCdRuleAllocDetail> ruleAllocDetailList = banQinCdRuleAllocDetailService.getByRuleCode(subEntity.getAllocRule(), orgId);
                    if (CollectionUtil.isEmpty(ruleAllocDetailList)) {
                        msg.addMessage("查询不到分配规则[" + subEntity.getAllocRule() + "]明细");
                        msg.setSuccess(false);
                        continue;
                    }

                    // 1.3、获取库存周转规则明细
                    List<BanQinCdRuleRotationDetail> ruleRotationDetailList = banQinCdRuleRotationDetailService.getByRuleCode(subEntity.getRotationRule(), subEntity.getOrgId());

                    // 1.4、包装单位换算信息获取
                    BanQinCdWhPackageEntity cdWhPackageEntity = wmCommon.getCdWhPackageRelation(subEntity.getPackCode(), orgId);

                    // 1.5、商品信息获取
                    BanQinCdWhSku cdWhSkuModel = banQinCdWhSkuService.getByOwnerAndSkuCode(subEntity.getOwnerCode(), subEntity.getSubSkuCode(), subEntity.getOrgId());
                    // 参数设置
                    BanQinKitAllocForRuleEntity entity = new BanQinKitAllocForRuleEntity();
                    entity.setCdWhSkuModel(cdWhSkuModel);// 商品明细
                    entity.setSubDetailEntity(subEntity);// 子件
                    entity.setPackageRelationList(cdWhPackageEntity.getCdWhPackageRelations());// 包装
                    entity.setRuleAllocDetailList(ruleAllocDetailList);// 分配明细
                    entity.setRuleRotationDetailList(ruleRotationDetailList);// 周转规则明细
                    // 2、库存周转规则header - 需要区分是按库存周转优先，还是按包装优先
                    BanQinCdRuleRotationHeader cdRuleRotationHeaderModel = banQinCdRuleRotationHeaderService.findByRuleCode(subEntity.getRotationRule(), orgId);
                    if (cdRuleRotationHeaderModel == null) {
                        msg.addMessage("查询不到库存周转规则[" + subEntity.getRotationRule() + "]");
                        msg.setSuccess(false);
                        continue;
                    }
                    // 2.1、库存周转优先
                    if (cdRuleRotationHeaderModel.getRotationType().equals(WmsCodeMaster.ROTATION.getCode())) {
                        ResultMessage message = allocByRotation(entity);
                        if(!message.isSuccess()){
                            msg.setSuccess(false);
                            msg.addMessage(message.getMessage());
                        }
                    }
                    // 2.2、包装优先
                    else if (cdRuleRotationHeaderModel.getRotationType().equals(WmsCodeMaster.PACKAGE.getCode())) {
                        ResultMessage message = allocByPackage(entity);
                        if(!message.isSuccess()){
                            msg.setSuccess(false);
                            msg.addMessage(message.getMessage());
                        }
                    }
                } catch (WarehouseException e) {
                    msg.setSuccess(false);
                    msg.addMessage(e.getMessage());
                }
            }
        } catch (Exception e) {
            // 框架控制，如数据过期，抛出提示
            throw new RuntimeException(e.getMessage());
        } finally {
            if (StringUtils.isBlank(msg.getMessage())) {
                msg.addMessage("操作成功");
            }
        }
        return msg;
    }

    /**
     * 描述：一步分配 库存周转优先
     * <p>
     * create by Jianhua on 2019/8/25
     */
    @Transactional
    public ResultMessage allocByRotation(BanQinKitAllocForRuleEntity entity) {
        ResultMessage msg = new ResultMessage();

        BanQinWmKitSubDetailEntity item = entity.getSubDetailEntity();
        // 分配明细
        List<BanQinCdRuleAllocDetail> ruleAllocDetailList = entity.getRuleAllocDetailList();
        // 库存周转规则查询语句条件设置
        BanQinCdRuleRotationSqlParamEntity sqlParamEntity = new BanQinCdRuleRotationSqlParamEntity();
        // 库存周转规则明细
        sqlParamEntity.setRuleRotationDetailList(entity.getRuleRotationDetailList());
        // 出库单明细行12个批次属性
        sqlParamEntity.setLotAtt01(item.getLotAtt01());
        sqlParamEntity.setLotAtt02(item.getLotAtt02());
        sqlParamEntity.setLotAtt03(item.getLotAtt03());
        sqlParamEntity.setLotAtt04(item.getLotAtt04());
        sqlParamEntity.setLotAtt05(item.getLotAtt05());
        sqlParamEntity.setLotAtt06(item.getLotAtt06());
        sqlParamEntity.setLotAtt07(item.getLotAtt07());
        sqlParamEntity.setLotAtt08(item.getLotAtt08());
        sqlParamEntity.setLotAtt09(item.getLotAtt09());
        sqlParamEntity.setLotAtt10(item.getLotAtt10());
        sqlParamEntity.setLotAtt11(item.getLotAtt11());
        sqlParamEntity.setLotAtt12(item.getLotAtt12());
        // 1、获取批次库存记录
        List<BanQinWmInvLot> invLotList = outboundCommon.getInvLotByRuleRotation(item.getOwnerCode(), item.getSubSkuCode(), item.getOrgId(), sqlParamEntity);
        if (invLotList.size() == 0) {
            msg.addMessage("[" + item.getKitNo() + "][" + item.getSubLineNo() + "]没有分配记录");
            msg.setSuccess(false);
            return msg;

        }
        // 可分配数量 = 计划数-分配数-拣货数-加工数
        Double qtyAllocEa = item.getQtyPlanEa() - item.getQtyAllocEa() - item.getQtyPkEa() - item.getQtyKitEa();
        // 无可分配数量
        if (qtyAllocEa <= 0D) {
            return msg;
        }

        // 2、循环批次号
        for (BanQinWmInvLot invLotModel : invLotList) {
            // 3、循环分配明细
            for (BanQinCdRuleAllocDetail ruleAllocDetailModel : ruleAllocDetailList) {
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
                invSqlParamEntity.setOwnerCode(item.getOwnerCode());// 货主
                invSqlParamEntity.setSkuCode(item.getSubSkuCode());// 商品
                invSqlParamEntity.setAreaCode(item.getAreaCode());// 区域
                invSqlParamEntity.setZoneCode(item.getZoneCode());// 库区
                invSqlParamEntity.setLocCode(item.getLocCode());// 库位
                invSqlParamEntity.setTraceId(item.getTraceId());// 跟踪号
                invSqlParamEntity.setLocUseType(ruleAllocDetailModel.getLocUseType());// 库位使用类型
                invSqlParamEntity.setIsClearFirst(ruleAllocDetailModel.getIsClearFirst());// 清仓优先
                invSqlParamEntity.setLotNum(invLotModel.getLotNum());// 批次号
                invSqlParamEntity.setQtyPackUom(packageEntity.getCdprQuantity());// 单位换算数量
                // 商品固定拣货位
                BanQinCdWhSkuLoc cdWhSkuLocModel = null;
                if (StringUtils.isNotEmpty(ruleAllocDetailModel.getSkuLocType())) {
                    // 商品拣货位
                    cdWhSkuLocModel = banQinCdWhSkuLocService.getCdWhSkuLocType(item.getOwnerCode(), item.getSubSkuCode(), ruleAllocDetailModel.getSkuLocType(), item.getOrgId());
                    if (cdWhSkuLocModel == null) {
                        // 分配规则配置商品固定拣货位，但商品未配置商品拣货位，则继续执行下一条分配规则明细
                        continue;
                    }
                    invSqlParamEntity.setSkuLocCode(cdWhSkuLocModel.getLocCode());// 商品固定拣货位
                }

                // 1、获取批次库位库存
                List<BanQinAllocInvLotLocEntity> invLotLocList = outboundCommon.getInvLotLocByRotation(invSqlParamEntity, item.getOrgId());

                // 2、循环批次库位库存更新
                BanQinKitAllocForInvEntity forInvEntity = new BanQinKitAllocForInvEntity();
                forInvEntity.setRuleAllocDetailModel(ruleAllocDetailModel);// 分配规则明细
                forInvEntity.setInvLotLocList(invLotLocList);// 批次库位库存
                forInvEntity.setCdWhSkuLocModel(cdWhSkuLocModel);// 商品固定拣货位
                forInvEntity.setKitSubDetailEntity(item);// 子件明细行
                forInvEntity.setQtyAllocEa(qtyAllocEa);// 可分配数量
                forInvEntity.setPackageEntity(packageEntity);// 包装

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
                BanQinAllocInvLotLocEntity invLotLocEntity = outboundCommon.getOverInvLot(qtyAllocUom, packageEntity.getCdprQuantity().doubleValue(), cdWhSkuLocModel.getLocCode(),
                        item.getOwnerCode(), item.getSubSkuCode(), invLotModel.getLotNum(), item.getOrgId());
                // 更新库存
                if (invLotLocEntity == null) {
                    continue;
                }
                // 分配更新
                BanQinKitAllocUpdateEntity updateEntity = new BanQinKitAllocUpdateEntity();
                updateEntity.setActionCode(ActionCode.ONESTEP_OVER_ALLOCATION);// 两步超量分配\一步超量分配
                updateEntity.setKitNo(item.getKitNo());// 加工单
                updateEntity.setParentLineNo(item.getParentLineNo());// 加工父件行号
                updateEntity.setSubLineNo(item.getSubLineNo());// 加工单子件行号
                updateEntity.setPlanKitLoc(item.getPlanKitLoc());// 计划加工台
                updateEntity.setPackageEntity(packageEntity);// 包装
                updateEntity.setInvLotLocEntity(invLotLocEntity);// 批次库位库存
                updateEntity.setQtyAllocEa(qtyAllocEa);// 分配数量
                updateEntity.setOrgId(item.getOrgId());
                updateEntity.setKitId(item.getHeaderId());
                // 一步分配更新
                qtyAllocEa = banQinKitAllocService.updateAlloc(updateEntity);
            }
        }
        return msg;
    }

    /**
     * 描述：一步分配 包装优先
     * <p>
     * create by Jianhua on 2019/8/25
     */
    @Transactional
    public ResultMessage allocByPackage(BanQinKitAllocForRuleEntity entity) {
        ResultMessage msg = new ResultMessage();
        // 按分配规则明细循环分配
        // 分配明细
        List<BanQinCdRuleAllocDetail> ruleAllocDetailList = entity.getRuleAllocDetailList();
        // 出库单行
        BanQinWmKitSubDetailEntity item = entity.getSubDetailEntity();
        // 可分配数量 = 计划数-分配数-拣货数-加工数
        Double qtyAllocEa = item.getQtyPlanEa() - item.getQtyAllocEa() - item.getQtyPkEa() - item.getQtyKitEa();
        // 循环分配明细
        for (BanQinCdRuleAllocDetail ruleAllocDetailModel : ruleAllocDetailList) {
            if (qtyAllocEa <= 0) {
                break;
            }
            // 包装单位
            BanQinCdWhPackageRelationEntity packageEntity;
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
            invSqlParamEntity.setOwnerCode(item.getOwnerCode());// 货主
            invSqlParamEntity.setSkuCode(item.getSubSkuCode());// 商品
            invSqlParamEntity.setAreaCode(item.getAreaCode());// 区域
            invSqlParamEntity.setZoneCode(item.getZoneCode());// 库区
            invSqlParamEntity.setLocCode(item.getLocCode());// 库位
            invSqlParamEntity.setTraceId(item.getTraceId());// 跟踪号
            invSqlParamEntity.setLocUseType(ruleAllocDetailModel.getLocUseType());// 库位使用类型
            invSqlParamEntity.setIsClearFirst(ruleAllocDetailModel.getIsClearFirst());// 清仓优先
            invSqlParamEntity.setQtyPackUom(packageEntity.getCdprQuantity());// 单位换算数量
            // 商品固定拣货位
            BanQinCdWhSkuLoc cdWhSkuLocModel = null;
            if (ruleAllocDetailModel.getSkuLocType() != null) {
                // 商品拣货位
                cdWhSkuLocModel = banQinCdWhSkuLocService.getCdWhSkuLocType(item.getOwnerCode(), item.getSubSkuCode(), ruleAllocDetailModel.getSkuLocType(), item.getOrgId());
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
            ruleRotationSqlParamEntity.setLotAtt01(item.getLotAtt01());
            ruleRotationSqlParamEntity.setLotAtt02(item.getLotAtt02());
            ruleRotationSqlParamEntity.setLotAtt03(item.getLotAtt03());
            ruleRotationSqlParamEntity.setLotAtt04(item.getLotAtt04());
            ruleRotationSqlParamEntity.setLotAtt05(item.getLotAtt05());
            ruleRotationSqlParamEntity.setLotAtt06(item.getLotAtt06());
            ruleRotationSqlParamEntity.setLotAtt07(item.getLotAtt07());
            ruleRotationSqlParamEntity.setLotAtt08(item.getLotAtt08());
            ruleRotationSqlParamEntity.setLotAtt09(item.getLotAtt09());
            ruleRotationSqlParamEntity.setLotAtt10(item.getLotAtt10());
            ruleRotationSqlParamEntity.setLotAtt11(item.getLotAtt11());
            ruleRotationSqlParamEntity.setLotAtt12(item.getLotAtt12());
            invSqlParamEntity.setRuleRotationSqlParamEntity(ruleRotationSqlParamEntity);

            // 1、获取批次库位库存
            List<BanQinAllocInvLotLocEntity> invLotLocList = outboundCommon.getInvLotLocByPackage(invSqlParamEntity, item.getOrgId());

            // 2、循环批次库位库存更新
            BanQinKitAllocForInvEntity forInvEntity = new BanQinKitAllocForInvEntity();
            forInvEntity.setRuleAllocDetailModel(ruleAllocDetailModel);// 分配规则明细
            forInvEntity.setInvLotLocList(invLotLocList);// 批次库位库存
            forInvEntity.setCdWhSkuLocModel(cdWhSkuLocModel);// 商品固定拣货位
            forInvEntity.setKitSubDetailEntity(item);// 子件明细行
            forInvEntity.setQtyAllocEa(qtyAllocEa);// 可分配数量
            forInvEntity.setPackageEntity(packageEntity);// 包装
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
            List<BanQinAllocInvLotLocEntity> overInvLotLocList = outboundCommon.getOverInvLotLoc(packageEntity.getCdprQuantity().doubleValue(), cdWhSkuLocModel.getLocCode(), item.getOwnerCode(),
                    item.getSubSkuCode(), item.getOrgId(), ruleRotationSqlParamEntity, entity.getCdWhSkuModel());
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
                // 分配更新
                // 库存更新类型
                BanQinKitAllocUpdateEntity updateEntity = new BanQinKitAllocUpdateEntity();
                updateEntity.setActionCode(ActionCode.ONESTEP_OVER_ALLOCATION);// 两步超量分配\一步超量分配
                updateEntity.setKitNo(item.getKitNo());// 加工单
                updateEntity.setParentLineNo(item.getParentLineNo());// 加工父件行号
                updateEntity.setSubLineNo(item.getSubLineNo());// 加工单子件行号
                updateEntity.setPlanKitLoc(item.getPlanKitLoc());// 计划加工台
                updateEntity.setPackageEntity(packageEntity);// 包装
                updateEntity.setInvLotLocEntity(invLotLocEntity);// 批次库位库存
                updateEntity.setQtyAllocEa(qtyAllocEa);// 分配数量
                updateEntity.setOrgId(item.getOrgId());
                // 一步分配更新
                qtyAllocEa = banQinKitAllocService.updateAlloc(updateEntity);
            }
        }
        return msg;
    }

    /**
     * 描述：按库存循环更新
     * <p>
     * create by Jianhua on 2019/8/25
     */
    @Transactional
    public ResultMessage allocForInvLotLoc(BanQinKitAllocForInvEntity entity) {
        ResultMessage msg = new ResultMessage();
        List<BanQinAllocInvLotLocEntity> invLotLocList = entity.getInvLotLocList();
        BanQinCdWhPackageRelationEntity packageEntity = entity.getPackageEntity();
        BanQinWmKitSubDetailEntity item = entity.getKitSubDetailEntity();
        // 可分配数量
        Double qtyAllocEa = entity.getQtyAllocEa();
        // 1、循环库存更新
        for (BanQinAllocInvLotLocEntity invLotLocEntity : invLotLocList) {
            Double qtyAllocUom = wmCommon.doubleDivide(qtyAllocEa, packageEntity.getCdprQuantity().doubleValue());
            if (qtyAllocUom < 1D) {// 无可分配整单位数量
                break;
            }

            BanQinKitAllocUpdateEntity updateEntity = new BanQinKitAllocUpdateEntity();
            updateEntity.setActionCode(ActionCode.ONESTEP_ALLOCATION);// 一步分配
            updateEntity.setKitNo(item.getKitNo());// 加工单
            updateEntity.setParentLineNo(item.getParentLineNo());// 加工父件行号
            updateEntity.setParentLineNo(item.getParentLineNo());// 加工父件行号
            updateEntity.setSubLineNo(item.getSubLineNo());// 加工子件行号
            updateEntity.setPlanKitLoc(item.getPlanKitLoc());// 计划加工台
            updateEntity.setPackageEntity(packageEntity);// 包装
            updateEntity.setInvLotLocEntity(invLotLocEntity);// 批次库位库存
            updateEntity.setQtyAllocEa(qtyAllocEa);// 分配数量
            updateEntity.setOrgId(item.getOrgId());
            updateEntity.setKitId(item.getHeaderId());
            // 分配更新，并且返回剩余可分配数量
            qtyAllocEa = banQinKitAllocService.updateAlloc(updateEntity);
        }

        // 返回剩余可分配数量
        msg.setData(qtyAllocEa);
        return msg;
    }

    private List<BanQinWmKitSubDetailEntity> getAvailableSubDetail(String kitNo, List<String> subLineNos, String orgId) {
        List<BanQinWmKitSubDetailEntity> result;
        List<BanQinWmKitSubDetailEntity> list = banQinWmKitSubDetailService.getEntityByKitNoAndSubLineNos(kitNo, subLineNos, orgId);
        result = list.stream().filter(e -> !WmsCodeMaster.SUB_SKU_TYPE_ACC.getCode().equals(e.getSubSkuType()))
                .filter(e -> (e.getQtyPlanEa() - e.getQtyAllocEa() - e.getQtyPkEa() - e.getQtyKitEa()) > 0)
                .collect(Collectors.toList());

        return result;
    }
}
