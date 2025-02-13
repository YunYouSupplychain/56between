package com.yunyou.modules.oms.order.manager;

import com.yunyou.common.enums.SystemAliases;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.service.BaseService;
import com.yunyou.modules.oms.basic.entity.OmBusinessOrderTypeRelation;
import com.yunyou.modules.oms.basic.service.OmBusinessOrderTypeRelationService;
import com.yunyou.modules.oms.common.OmsConstants;
import com.yunyou.modules.oms.common.OmsException;
import com.yunyou.modules.oms.common.OmsUtils;
import com.yunyou.modules.oms.inv.service.OmSaleInventoryService;
import com.yunyou.modules.oms.order.entity.*;
import com.yunyou.modules.oms.order.entity.extend.OmRequisitionDetailEntity;
import com.yunyou.modules.oms.order.entity.extend.OmRequisitionEntity;
import com.yunyou.modules.oms.order.service.*;
import com.yunyou.modules.sys.GenNoType;
import com.yunyou.modules.sys.SysParamConstants;
import com.yunyou.modules.sys.service.SynchronizedNoService;
import com.yunyou.modules.sys.utils.SysControlParamsUtils;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
/**
 * 作业任务生成Service
 *
 * @author liujianhua
 * @version 2022.7.29
 */
@Service
@Transactional(readOnly = true)
public class OmTaskGenManager extends BaseService {
    @Autowired
    private OmChainHeaderService omChainHeaderService;
    @Autowired
    private OmChainDetailService omChainDetailService;
    @Autowired
    private OmRequisitionHeaderService omRequisitionHeaderService;
    @Autowired
    private OmRequisitionDetailService omRequisitionDetailService;
    @Autowired
    private OmBusinessOrderTypeRelationService omBusinessOrderTypeRelationService;
    @Autowired
    private OmTaskHeaderService omTaskHeaderService;
    @Autowired
    private OmSaleInventoryService omSaleInventoryService;
    @Autowired
    private SynchronizedNoService noService;

    /**
     * 生成作业任务
     *
     * @param entity 供应链订单数据
     */
    @Transactional
    public void genTask(OmChainHeader entity) {
        OmChainHeader omChainHeader = omChainHeaderService.get(entity.getId());
        if (omChainHeader == null) {
            throw new OmsException(MessageFormat.format("订单{0}数据已过期!", entity.getChainNo()));
        }
        if (StringUtils.isBlank(omChainHeader.getWarehouse())) {
            throw new OmsException(MessageFormat.format("订单{0}下发机构为空!", entity.getChainNo()));
        }
        List<OmChainDetail> omChainDetailList = entity.getOmChainDetailList().stream().filter(o -> o.getPlanTaskQty() != null && o.getPlanTaskQty().compareTo(BigDecimal.ZERO) > 0).collect(Collectors.toList());
        if (CollectionUtil.isEmpty(omChainDetailList)) {
            throw new OmsException(MessageFormat.format("订单{0}未指定计划任务量!", entity.getChainNo()));
        }
        if (!(OmsConstants.OMS_CO_STATUS_30.equals(omChainHeader.getStatus()) || OmsConstants.OMS_CO_STATUS_35.equals(omChainHeader.getStatus()))) {
            throw new OmsException(MessageFormat.format("订单{0}非审核、部分生成作业任务状态，无法操作!", entity.getChainNo()));
        }
        List<OmBusinessOrderTypeRelation> relations = omBusinessOrderTypeRelationService.getByBusinessOrderType(entity.getBusinessOrderType(), entity.getOrgId());
        if (CollectionUtil.isEmpty(relations)) {
            throw new OmsException("未维护订单类型关系，无法生成作业任务!");
        }
        // 获取该订单的最新任务批次号
        String lotNum = omTaskHeaderService.getMaxLotNumBySourceNo(omChainHeader.getChainNo(), omChainHeader.getOrgId());

        // 生成作业任务
        for (OmBusinessOrderTypeRelation relation : relations) {
            this.genTask(entity, omChainDetailList, relation.getOrderType(), lotNum, relation.getPushSystem(), relation.getPushOrderType(), entity.getWarehouse());
        }

        // 执行订单明细任务数更新
        for (OmChainDetail o : omChainDetailList) {
            OmChainDetail omChainDetail = omChainDetailService.get(o.getId());
            omChainDetail.setTaskQty(omChainDetail.getTaskQty() == null ? o.getPlanTaskQty() : omChainDetail.getTaskQty().add(o.getPlanTaskQty()));
            omChainDetailService.save(omChainDetail);
        }
        // 执行订单状态更新
        List<OmChainDetail> list = omChainDetailService.getDetails(omChainHeader.getId());
        BigDecimal totalQty = list.stream().map(OmChainDetail::getQty).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalTaskQty = list.stream().map(OmChainDetail::getTaskQty).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
        if (totalQty.compareTo(totalTaskQty) == 0) {
            omChainHeader.setStatus(OmsConstants.OMS_CO_STATUS_40);
            omChainHeader.setHandleStatus(OmsConstants.HANDLE_STATUS_Y);
            omChainHeader.setOmChainDetailList(Lists.newArrayList());// 去除明细避免二次无用更新
            omChainHeaderService.save(omChainHeader);
        } else if (totalTaskQty.compareTo(BigDecimal.ZERO) == 0) {
            omChainHeader.setStatus(OmsConstants.OMS_CO_STATUS_30);
            omChainHeader.setHandleStatus(OmsConstants.HANDLE_STATUS_Y);
            omChainHeader.setOmChainDetailList(Lists.newArrayList());// 去除明细避免二次无用更新
            omChainHeaderService.save(omChainHeader);
        } else {
            omChainHeader.setStatus(OmsConstants.OMS_CO_STATUS_35);
            omChainHeader.setHandleStatus(OmsConstants.HANDLE_STATUS_Y);
            omChainHeader.setOmChainDetailList(Lists.newArrayList());// 去除明细避免二次无用更新
            omChainHeaderService.save(omChainHeader);
        }
    }

    /**
     * 生成作业任务
     *
     * @param entity        供应链订单数据
     * @param lotNum        任务批次
     * @param taskType      指定作业任务类型
     * @param pushSystem    指定下发系统
     * @param pushOrderType 指定下发订单类型
     * @param pushWarehouse 指定下发仓库
     */
    @Transactional
    public void genTask(OmChainHeader entity, List<OmChainDetail> omChainDetailList, String taskType, String lotNum, String pushSystem, String pushOrderType, String pushWarehouse) {
        if (omChainHeaderService.isIntercepted(entity.getChainNo(), entity.getOrgId())) {
            throw new OmsException(MessageFormat.format("订单{0}已拦截，无法操作", entity.getChainNo()));
        }
        // 系统控制参数：订单生成任务时忽略库存校验
        final String CHAIN_GEN_TASK_IGNORE_CHECK_INV = SysControlParamsUtils.getValue(SysParamConstants.CHAIN_GEN_TASK_IGNORE_CHECK_INV, entity.getOrgId());
        // 是否校验库存
        boolean isCheckInv = OmsConstants.OMS_TASK_TYPE_01.equals(taskType) && SystemAliases.WMS.getCode().equals(pushSystem)
                && OmsConstants.OMS_Y.equals(entity.getIsAvailableStock()) && !OmsConstants.OMS_Y.equals(CHAIN_GEN_TASK_IGNORE_CHECK_INV);
        // 作业任务类型为出库，默认分配状态为“未分配”，否则为“不分配”
        String allocStatus = OmsConstants.OMS_TASK_TYPE_01.equals(taskType) && SystemAliases.WMS.getCode().equals(pushSystem) ? OmsConstants.OMS_TK_ALLOC_STATUS_00 : OmsConstants.OMS_TK_ALLOC_STATUS_90;
        BigDecimal totalAmount = BigDecimal.ZERO, totalTax = BigDecimal.ZERO, totalTaxInAmount = BigDecimal.ZERO;

        List<OmTaskDetail> omTaskDetails = Lists.newArrayList();
        for (OmChainDetail detail : omChainDetailList) {
            BigDecimal planTaskQty = detail.getPlanTaskQty();
            if (planTaskQty == null || planTaskQty.compareTo(BigDecimal.ZERO) <= 0) {
                throw new OmsException(MessageFormat.format("订单{0}商品{1}计划任务数必须大于0", entity.getChainNo(), detail.getSkuCode()));
            }
            OmChainDetail omChainDetail = omChainDetailService.get(detail.getId());
            BigDecimal taskQty = detail.getTaskQty() == null ? planTaskQty : detail.getTaskQty().add(planTaskQty);
            if (taskQty.compareTo(detail.getQty()) > 0) {
                throw new OmsException(MessageFormat.format("订单{0}商品{1}计划任务数超出订单数", entity.getChainNo(), omChainDetail.getSkuCode()));
            }
            if (isCheckInv) {
                double availableQty = omSaleInventoryService.getAvailableQty(entity.getOwner(), omChainDetail.getSkuCode(), pushWarehouse);
                if (planTaskQty.doubleValue() > availableQty) {
                    throw new OmsException(MessageFormat.format("订单{0}商品{1}库存不足，无法分配", entity.getChainNo(), omChainDetail.getSkuCode()));
                }
            }
            // 辅助单位数量
            BigDecimal auxiliaryQty = omChainDetail.getAuxiliaryQty();
            // BigDecimal auxiliaryQty = OmsUtils.calcAuxiliaryQty(omChainDetail.getRatio(), planTaskQty);
            // 单价
            BigDecimal price = omChainDetail.getPrice();
            // 税率
            BigDecimal taxRate = omChainDetail.getTaxRate() == null ? BigDecimal.ZERO : BigDecimal.valueOf(omChainDetail.getTaxRate());
            // 含税金额
            BigDecimal taxAmount = OmsUtils.calcTaxAmount(omChainDetail.getTaxPrice(), planTaskQty);
            // 税金
            BigDecimal taxMoney = OmsUtils.calcTaxMoney(omChainDetail.getTaxPrice(), planTaskQty, taxRate);
            // 金额
            BigDecimal amount = OmsUtils.calcAmount(omChainDetail.getTaxPrice(), planTaskQty, taxRate);
            // 成交价税合计
            BigDecimal sumTransactionPriceTax = OmsUtils.calcSumTransactionPriceTax(omChainDetail.getTaxPrice(), planTaskQty, omChainDetail.getDiscount());
            // 成交税金
            BigDecimal transactionTax = OmsUtils.calcTransactionTax(omChainDetail.getTaxPrice(), planTaskQty, taxRate, omChainDetail.getDiscount());
            // 成交金额
            BigDecimal turnover = OmsUtils.calcTurnover(omChainDetail.getTaxPrice(), planTaskQty, taxRate, omChainDetail.getDiscount());

            totalAmount = totalAmount.add(amount);
            totalTax = totalTax.add(taxMoney);
            totalTaxInAmount = totalTaxInAmount.add(taxAmount);

            OmTaskDetail omTaskDetail = new OmTaskDetail();
            BeanUtils.copyProperties(omChainDetail, omTaskDetail);
            omTaskDetail.setId("");
            omTaskDetail.setHeaderId(null);
            omTaskDetail.setAllocStatus(allocStatus);
            omTaskDetail.setQty(planTaskQty);
            omTaskDetail.setChainNo(entity.getChainNo());
            omTaskDetail.setCustomerNo(entity.getCustomerNo());
            omTaskDetail.setDataSource(entity.getDataSource());
            omTaskDetail.setOrgId(entity.getOrgId());
            omTaskDetail.setAuxiliaryQty(auxiliaryQty);
            omTaskDetail.setPrice(price);
            omTaskDetail.setTaxAmount(taxAmount);
            omTaskDetail.setTaxMoney(taxMoney);
            omTaskDetail.setAmount(amount);
            omTaskDetail.setSumTransactionPriceTax(sumTransactionPriceTax);
            omTaskDetail.setTransactionTax(transactionTax);
            omTaskDetail.setTurnover(turnover);
            omTaskDetails.add(omTaskDetail);
        }

        OmTaskHeader omTaskHeader = new OmTaskHeader();
        BeanUtils.copyProperties(entity, omTaskHeader);
        omTaskHeader.setId(null);
        omTaskHeader.setTaskNo(noService.getDocumentNo(GenNoType.OM_TASK_NO.name()));
        omTaskHeader.setTaskType(taskType);
        omTaskHeader.setStatus(OmsConstants.OMS_TASK_STATUS_20);
        omTaskHeader.setChainNo(entity.getChainNo());
        omTaskHeader.setTaskSource(OmsConstants.TASK_SOURCE_CO);
        omTaskHeader.setLotNum(lotNum);
        omTaskHeader.setWarehouse(pushWarehouse);
        omTaskHeader.setPushSystem(pushSystem);
        omTaskHeader.setPushOrderType(pushOrderType);

        omTaskHeader.setHandleStatus(OmsConstants.HANDLE_STATUS_N);
        omTaskHeader.setOrgId(entity.getOrgId());
        omTaskHeader.setTotalAmount(totalAmount);
        omTaskHeader.setTotalTax(totalTax);
        omTaskHeader.setTotalTaxInAmount(totalTaxInAmount);
        omTaskHeader.setOmTaskDetailList(omTaskDetails);
        omTaskHeaderService.save(omTaskHeader);
    }

    /**
     * 生成作业任务
     *
     * @param entity 调拨单数据
     */
    @Transactional
    public void genTask(OmRequisitionEntity entity) {
        OmRequisitionHeader omRequisitionHeader = omRequisitionHeaderService.get(entity.getId());
        if (omRequisitionHeader == null) {
            throw new OmsException(MessageFormat.format("订单{0}数据已过期!", entity.getReqNo()));
        }
        if (!(OmsConstants.OMS_RO_STATUS_20.equals(omRequisitionHeader.getStatus()) || OmsConstants.OMS_RO_STATUS_35.equals(omRequisitionHeader.getStatus()))) {
            throw new OmsException(MessageFormat.format("订单{0}非审核、部分生成作业任务状态，无法操作!", omRequisitionHeader.getReqNo()));
        }
        List<OmRequisitionDetailEntity> omRequisitionDetailList = entity.getOmRequisitionDetailList().stream().filter(o -> o.getPlanTaskQty() != null && o.getPlanTaskQty().compareTo(BigDecimal.ZERO) > 0).collect(Collectors.toList());
        if (CollectionUtil.isEmpty(omRequisitionDetailList)) {
            throw new OmsException(MessageFormat.format("订单{0}未指定计划任务量!", omRequisitionHeader.getReqNo()));
        }
        List<OmBusinessOrderTypeRelation> relations = omBusinessOrderTypeRelationService.getByBusinessOrderType(omRequisitionHeader.getOrderType(), omRequisitionHeader.getOrgId());
        if (CollectionUtil.isEmpty(relations)) {
            throw new OmsException("未维护订单类型关系，无法生成作业任务!");
        }
        // 获取该订单的最新任务批次号
        String lotNum = omTaskHeaderService.getMaxLotNumBySourceNo(omRequisitionHeader.getReqNo(), omRequisitionHeader.getOrgId());

        // 生成作业任务
        for (OmBusinessOrderTypeRelation relation : relations) {
            if (OmsConstants.OMS_TASK_TYPE_01.equals(relation.getOrderType())) {
                // 调出
                this.genTask(omRequisitionHeader, omRequisitionDetailList, OmsConstants.OMS_TASK_TYPE_01, lotNum, relation.getPushSystem(), relation.getPushOrderType(), omRequisitionHeader.getFmOrgId());
            } else if (OmsConstants.OMS_TASK_TYPE_02.equals(relation.getOrderType())) {
                // 调入
                this.genTask(omRequisitionHeader, omRequisitionDetailList, OmsConstants.OMS_TASK_TYPE_02, lotNum, relation.getPushSystem(), relation.getPushOrderType(), omRequisitionHeader.getToOrgId());
            } else if (OmsConstants.OMS_TASK_TYPE_03.equals(relation.getOrderType())) {
                // 调出
                this.genTask(omRequisitionHeader, omRequisitionDetailList, OmsConstants.OMS_TASK_TYPE_03, lotNum, relation.getPushSystem(), relation.getPushOrderType(), omRequisitionHeader.getFmOrgId());
            }
        }

        // 执行订单明细任务数更新
        for (OmRequisitionDetailEntity o : omRequisitionDetailList) {
            OmRequisitionDetail omRequisitionDetail = omRequisitionDetailService.get(o.getId());
            omRequisitionDetail.setTaskQty(omRequisitionDetail.getTaskQty() == null ? o.getPlanTaskQty() : omRequisitionDetail.getTaskQty().add(o.getPlanTaskQty()));
            omRequisitionDetailService.save(omRequisitionDetail);
        }
        // 执行订单状态更新
        List<OmRequisitionDetail> list = omRequisitionDetailService.findList(new OmRequisitionDetail(entity.getReqNo(), entity.getOrgId()));
        BigDecimal totalQty = list.stream().map(OmRequisitionDetail::getQty).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalTaskQty = list.stream().map(OmRequisitionDetail::getTaskQty).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
        if (totalQty.compareTo(totalTaskQty) == 0) {
            omRequisitionHeader.setStatus(OmsConstants.OMS_CO_STATUS_40);
            omRequisitionHeaderService.save(omRequisitionHeader);
        } else if (totalTaskQty.compareTo(BigDecimal.ZERO) == 0) {
            omRequisitionHeader.setStatus(OmsConstants.OMS_CO_STATUS_30);
            omRequisitionHeaderService.save(omRequisitionHeader);
        } else {
            omRequisitionHeader.setStatus(OmsConstants.OMS_CO_STATUS_35);
            omRequisitionHeaderService.save(omRequisitionHeader);
        }
    }

    /**
     * 生成作业任务
     *
     * @param entity        供应链订单数据
     * @param lotNum        任务批次
     * @param taskType      指定作业任务类型
     * @param pushSystem    指定下发系统
     * @param pushOrderType 指定下发订单类型
     * @param pushWarehouse 指定下发仓库
     */
    @Transactional
    public void genTask(OmRequisitionHeader entity, List<OmRequisitionDetailEntity> omRequisitionDetailList, String taskType, String lotNum, String pushSystem, String pushOrderType, String pushWarehouse) {
        if (StringUtils.isBlank(pushWarehouse)) {
            throw new OmsException("订单[" + entity.getReqNo() + "]未指定下发机构，无法操作");
        }
        // 作业任务类型为出库，默认分配状态为“未分配”，否则为“不分配”
        String allocStatus = OmsConstants.OMS_TASK_TYPE_01.equals(taskType) && SystemAliases.WMS.getCode().equals(pushSystem) ? OmsConstants.OMS_TK_ALLOC_STATUS_00 : OmsConstants.OMS_TK_ALLOC_STATUS_90;
        // 系统控制参数：订单生成任务时忽略库存校验
        final String CHAIN_GEN_TASK_IGNORE_CHECK_INV = SysControlParamsUtils.getValue(SysParamConstants.CHAIN_GEN_TASK_IGNORE_CHECK_INV, entity.getOrgId());
        // 是否校验库存
        boolean isCheckInv = OmsConstants.OMS_TASK_TYPE_01.equals(taskType) && SystemAliases.WMS.getCode().equals(pushSystem) && !OmsConstants.OMS_Y.equals(CHAIN_GEN_TASK_IGNORE_CHECK_INV);

        List<OmTaskDetail> omTaskDetails = Lists.newArrayList();
        for (OmRequisitionDetailEntity omRequisitionDetail : omRequisitionDetailList) {
            BigDecimal planTaskQty = omRequisitionDetail.getPlanTaskQty();
            if (planTaskQty == null || planTaskQty.compareTo(BigDecimal.ZERO) <= 0) {
                throw new OmsException(MessageFormat.format("订单{0}行号{1}计划任务数必须大于0", entity.getReqNo(), omRequisitionDetail.getLineNo()));
            }
            OmRequisitionDetail detail = omRequisitionDetailService.get(omRequisitionDetail.getId());
            BigDecimal taskQty = detail.getTaskQty() == null ? planTaskQty : detail.getTaskQty().add(planTaskQty);
            if (taskQty.compareTo(detail.getQty()) > 0) {
                throw new OmsException(MessageFormat.format("订单{0}行号{1}，计划任务量超出订单量", entity.getReqNo(), omRequisitionDetail.getLineNo()));
            }
            if (isCheckInv) {
                double availableQty = omSaleInventoryService.getAvailableQty(entity.getOwnerCode(), omRequisitionDetail.getSkuCode(), pushWarehouse);
                if (planTaskQty.doubleValue() > availableQty) {
                    throw new OmsException(MessageFormat.format("货主{0}商品{1}库存不足，无法操作", entity.getOwnerCode(), omRequisitionDetail.getSkuCode()));
                }
            }

            OmTaskDetail omTaskDetail = new OmTaskDetail();
            omTaskDetail.setId("");
            omTaskDetail.setAllocStatus(allocStatus);
            omTaskDetail.setChainNo(entity.getReqNo());
            omTaskDetail.setCustomerNo(entity.getCustomerNo());
            omTaskDetail.setLineNo(omRequisitionDetail.getLineNo());
            omTaskDetail.setSkuCode(omRequisitionDetail.getSkuCode());
            omTaskDetail.setSkuName(omRequisitionDetail.getSkuName());
            omTaskDetail.setSpec(omRequisitionDetail.getSkuSpec());
            omTaskDetail.setQty(planTaskQty);
            omTaskDetail.setUnit(omRequisitionDetail.getUom());
            omTaskDetail.setAuxiliaryQty(omRequisitionDetail.getAuxiliaryQty());
            omTaskDetail.setAuxiliaryUnit(omRequisitionDetail.getAuxiliaryUom());
            omTaskDetail.setOrgId(entity.getOrgId());
            omTaskDetail.setDef1(omRequisitionDetail.getDef1());
            omTaskDetail.setDef2(omRequisitionDetail.getDef2());
            omTaskDetail.setDef3(omRequisitionDetail.getDef3());
            omTaskDetail.setDef4(omRequisitionDetail.getDef4());
            omTaskDetail.setDef5(omRequisitionDetail.getDef5());
            omTaskDetails.add(omTaskDetail);
        }

        OmTaskHeader omTaskHeader = new OmTaskHeader();
        omTaskHeader.setTaskNo(noService.getDocumentNo(GenNoType.OM_TASK_NO.name()));
        omTaskHeader.setStatus(OmsConstants.OMS_TASK_STATUS_20);
        omTaskHeader.setTaskType(taskType);
        omTaskHeader.setBusinessOrderType(entity.getOrderType());
        omTaskHeader.setChainNo(entity.getReqNo());
        omTaskHeader.setOrderDate(entity.getOrderTime());
        omTaskHeader.setOwner(entity.getOwnerCode());
        omTaskHeader.setConsignee(entity.getConsignee());
        omTaskHeader.setConsigneeTel(entity.getConsigneeTel());
        omTaskHeader.setConsigneeArea(entity.getConsigneeAreaId());
        omTaskHeader.setConsigneeAddressArea(entity.getConsigneeArea());
        omTaskHeader.setConsigneeAddress(entity.getConsigneeAddress());
        omTaskHeader.setShipper(entity.getShipper());
        omTaskHeader.setShipperTel(entity.getShipperTel());
        omTaskHeader.setShipperArea(entity.getShipArea());
        omTaskHeader.setShipperAddressArea(entity.getShipAreaId());
        omTaskHeader.setShipperAddress(entity.getShipAddress());
        omTaskHeader.setCustomerNo(entity.getCustomerNo());
        omTaskHeader.setTransportMode(entity.getTransportMode());
        omTaskHeader.setCarrier(entity.getCarrierCode());
        omTaskHeader.setLogisticsNo(entity.getLogisticsNo());
        omTaskHeader.setVehicleNo(entity.getVehicleNo());
        omTaskHeader.setDriver(entity.getDriver());
        omTaskHeader.setContactTel(entity.getDriverTel());
        omTaskHeader.setArrivalTime(entity.getPlanArrivalTime());
        omTaskHeader.setPushOrderType(pushOrderType);
        omTaskHeader.setPushOrderType(pushSystem);
        omTaskHeader.setWarehouse(pushWarehouse);
        omTaskHeader.setLotNum(lotNum);
        omTaskHeader.setTaskSource(OmsConstants.TASK_SOURCE_RO);
        omTaskHeader.setOrgId(entity.getOrgId());
        omTaskHeader.setOmTaskDetailList(omTaskDetails);
        omTaskHeader.setDef1(entity.getDef1());
        omTaskHeader.setDef2(entity.getDef2());
        omTaskHeader.setDef3(entity.getDef3());
        omTaskHeader.setDef4(entity.getDef4());
        omTaskHeader.setDef5(entity.getDef5());
        omTaskHeader.setOmTaskDetailList(omTaskDetails);
        omTaskHeaderService.save(omTaskHeader);
    }

}