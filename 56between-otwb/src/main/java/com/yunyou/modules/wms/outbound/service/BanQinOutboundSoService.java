package com.yunyou.modules.wms.outbound.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.time.DateUtils;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.common.utils.time.DateUtil;
import com.yunyou.modules.wms.basicdata.entity.*;
import com.yunyou.modules.wms.basicdata.service.BanQinCdRuleWvGroupDetailService;
import com.yunyou.modules.wms.basicdata.service.BanQinCdRuleWvGroupHeaderService;
import com.yunyou.modules.wms.common.entity.BanQinCdWhPackageRelationEntity;
import com.yunyou.modules.wms.common.entity.WarehouseException;
import com.yunyou.modules.wms.common.entity.WmsCodeMaster;
import com.yunyou.modules.wms.common.entity.WmsConstants;
import com.yunyou.modules.wms.common.service.BanQinWmsCommonService;
import com.yunyou.modules.wms.inventory.entity.BanQinWmInvSerial;
import com.yunyou.modules.wms.inventory.service.BanQinWmInvSerialService;
import com.yunyou.modules.wms.outbound.entity.*;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 出库单操作方法类
 *
 * @author WMJ
 * @version 2019/02/26
 */
@Service
public class BanQinOutboundSoService {
    // 公共方法
    @Autowired
    protected BanQinWmsCommonService wmCommon;
    // 出库单manager
    @Autowired
    protected BanQinWmSoHeaderService wmSoHeaderService;
    // 出库单明细
    @Autowired
    protected BanQinWmSoDetailService wmSoDetailService;
    // 生成波次
    @Autowired
    protected BanQinOutboundCreateWaveService outboundCreateWaveService;
    // 波次单
    @Autowired
    protected BanQinWmWvHeaderService wmWvHeaderService;
    // 预配明细
    @Autowired
    protected BanQinWmSoPreallocService wmSoPreallocService;
    // 分配明细
    @Autowired
    protected BanQinWmSoAllocService wmSoAllocService;
    // 删除
    @Autowired
    protected BanQinOutboundRemoveService outboundRemoveService;
    // 复制
    @Autowired
    protected BanQinOutboundDuplicateService outboundDuplicateService;
    // 预配
    @Autowired
    @Lazy
    protected BanQinOutboundBatchPreallocAction outboundBatchPreallocAction;
    // 两步分配
    @Autowired
    @Lazy
    protected BanQinOutboundBatchAllocTwoStepAction outboundBatchAllocTwoStepAction;
    // 一步分配
    @Autowired
    @Lazy
    protected BanQinOutboundBatchAllocOneStepAction outboundBatchAllocOneStepAction;
    // 拣货
    @Autowired
    @Lazy
    protected BanQinOutboundBatchPickingAction outboundBatchPickingAction;
    // 发货
    @Autowired
    @Lazy
    protected BanQinOutboundBatchShipmentAction outboundBatchShipmentAction;
    // 关闭、取消
    @Autowired
    protected BanQinOutboundCloseOrCancelService outboundCloseOrCancelService;
    // 取消预配
    @Autowired
    @Lazy
    protected BanQinOutboundBatchCancelPreallocAction outboundBatchCancelPreallocAction;
    // 取消分配
    @Autowired
    @Lazy
    protected BanQinOutboundBatchCancelAllocAction outboundBatchCancelAllocAction;
    // 取消拣货
    @Autowired
    @Lazy
    protected BanQinOutboundBatchCancelPickingAction outboundBatchCancelPickingAction;
    // 手工预配
    @Autowired
    protected BanQinOutboundManualPreallocService outboundManualPreallocService;
    // 手工分配
    @Autowired
    protected BanQinOutboundManualAllocService outboundManualAllocService;
    // 复核
    @Autowired
    protected BanQinOutboundCheckService outboundCheckService;
    // 复核
    @Autowired
    @Lazy
    protected BanQinOutboundCheckAction outboundCheckAction;
    // 打包
    @Autowired
    protected BanQinOutboundPackService outboundPackService;
    // 打包
    @Autowired
    @Lazy
    protected BanQinOutboundPackAction outboundPackAction;
    // 取消预配明细
    @Autowired
    protected BanQinWmDelPreallocService wmDelPreallocService;
    // 取消分配明细
    @Autowired
    protected BanQinWmDelAllocService wmDelAllocService;
    // 拦截订单
    @Autowired
    @Lazy
    protected BanQinOutboundBatchInterceptAction outboundBatchInterceptAction;
    // 出库序列号
    @Autowired
    protected BanQinWmSoSerialService wmSoSerialService;
    // 库存序列号
    @Autowired
    protected BanQinWmInvSerialService wmInvSerialService;
    // 装车单
    @Autowired
    protected BanQinWmLdHeaderService wmLdHeaderService;
    // 装车单明细
    @Autowired
    protected BanQinWmLdDetailService wmLdDetailService;
    // 出库公共
    @Autowired
    protected BanQinOutboundCommonService outboundCommon;
    // 取消发货
    @Autowired
    @Lazy
    protected BanQinOutboundBatchCancelShipmentAction outboundBatchCancelShipmentAction;
    // 波次规则组
    @Autowired
    private BanQinCdRuleWvGroupHeaderService cdRuleWvGroupHeaderService;
    // 波次规则组明细
    @Autowired
    private BanQinCdRuleWvGroupDetailService cdRuleWvGroupDetailService;
    // 生成拣货单
    @Autowired
    protected BanQinOutboundCreatePickService banQinOutboundCreatePickService;

    /**
     * 根据发运订单号获取发运订单信息
     *
     * @param soNo  出库单号
     * @param orgId 机构ID
     */
    public BanQinWmSoEntity getSoEntityBySoNo(String soNo, String orgId) {
        // 获取出库单
        BanQinWmSoEntity soEntity = wmSoHeaderService.findEntityBySoNo(soNo, orgId);
        // 获取商品明细
        soEntity.setDetailList(wmSoDetailService.findEntityBySoNo(soNo, orgId));
        // 获取预配明细
        soEntity.setPreallocList(wmSoPreallocService.getEntityBySoNo(soNo, orgId));
        // 获取分配明细
        soEntity.setAllocList(wmSoAllocService.getEntityBySoNo(soNo, orgId));
        return soEntity;
    }

    /**
     * 根据发运订单号、行号获取发运订单信息
     *
     * @param soNo    出库单号
     * @param lineNos 出库单号明细行号
     * @param orgId   机构ID
     */
    public BanQinWmSoEntity getSoEntityBySoNoAndLineNos(String soNo, String[] lineNos, String orgId) {
        // 获取出库单
        BanQinWmSoEntity soEntity = wmSoHeaderService.findEntityBySoNo(soNo, orgId);
        if (lineNos.length == 1) {
            soEntity.setDetailEntity(wmSoDetailService.findEntityBySoNoAndLineNo(soNo, lineNos[0], orgId));
        }
        // 获取商品明细
        soEntity.setDetailList(wmSoDetailService.findEntityBySoNo(soNo, orgId));
        // 获取预配明细
        soEntity.setPreallocList(wmSoPreallocService.getEntityBySoNo(soNo, orgId));
        // 获取分配明细
        soEntity.setAllocList(wmSoAllocService.getEntityBySoNo(soNo, orgId));
        return soEntity;
    }

    /**
     * 根据订单号、订单行号获取发运订单明细行信息
     *
     * @param soNo   出库单号
     * @param lineNo 出库单号明细行号
     * @param orgId  机构ID
     */
    public BanQinWmSoDetailEntity getSoDetailEntityBySoLineNo(String soNo, String lineNo, String orgId) {
        return wmSoDetailService.findEntityBySoNoAndLineNo(soNo, lineNo, orgId);
    }

    /**
     * 根据分配ID，获取分配明细信息
     *
     * @param allocId 分配ID
     * @param orgId   机构ID
     */
    public BanQinWmSoAllocEntity getSoAllocEntityByAllocId(String allocId, String orgId) {
        return wmSoAllocService.getEntityByAllocId(allocId, orgId);
    }

    /**
     * 根据发运单号，获取分配明细信息
     *
     * @param soNo  出库单号
     * @param orgId 机构ID
     */
    public List<BanQinWmSoAllocEntity> getSoAllocEntity(String soNo, String orgId) {
        return wmSoAllocService.getEntityBySoNo(soNo, orgId);
    }

    /**
     * 发运订单头保存
     *
     * @param soEntity 出库单实体
     */
    public ResultMessage saveSoEntity(BanQinWmSoEntity soEntity) {
        ResultMessage msg = new ResultMessage();
        BanQinWmSoHeader wmSoHeader;
        try {
            wmSoHeader = wmSoHeaderService.saveSoHeader(soEntity);
        } catch (WarehouseException e) {
            msg.addMessage(e.getMessage());
            msg.setSuccess(false);
            return msg;
        }
        msg.setSuccess(true);
        msg.addMessage("保存成功");
        msg.setData(wmSoHeader);
        return msg;
    }

    /**
     * 删除发运订单头
     *
     * @param soNos 出库单号
     * @param orgId 机构ID
     */
    public ResultMessage removeSoEntity(String[] soNos, String orgId) {
        ResultMessage msg = new ResultMessage();
        for (String soNo : soNos) {
            try {
                outboundRemoveService.removeSoEntity(soNo, orgId);
            } catch (WarehouseException e) {
                msg.addMessage(e.getMessage());
            }
        }
        if (StringUtils.isEmpty(msg.getMessage())) {
            msg.setSuccess(true);
            msg.addMessage("操作成功");
        }
        return msg;
    }

    /**
     * 审核
     *
     * @param soNos 出库单号
     * @param orgId 机构ID
     */
    public ResultMessage audit(String[] soNos, String orgId) {
        ResultMessage msg = new ResultMessage();
        for (String soNo : soNos) {
            try {
                wmSoHeaderService.audit(soNo, orgId);
            } catch (WarehouseException e) {
                msg.addMessage(e.getMessage());
            }
        }
        if (StringUtils.isEmpty(msg.getMessage())) {
            msg.setSuccess(true);
            msg.addMessage("操作成功");
        }
        return msg;
    }

    /**
     * 取消审核
     *
     * @param soNos 出库单号
     * @param orgId 机构ID
     */
    public ResultMessage cancelAudit(String[] soNos, String orgId) {
        ResultMessage msg = new ResultMessage();
        for (String soNo : soNos) {
            try {
                wmSoHeaderService.cancelAudit(soNo, orgId);
            } catch (WarehouseException e) {
                msg.addMessage(e.getMessage());
            }
        }
        if (StringUtils.isEmpty(msg.getMessage())) {
            msg.setSuccess(true);
            msg.addMessage("操作成功");
        }
        return msg;
    }

    /**
     * 冻结发运单
     *
     * @param soNos 出库单号
     * @param orgId 机构ID
     */
    public ResultMessage hold(String[] soNos, String orgId) {
        ResultMessage msg = new ResultMessage();
        for (String soNo : soNos) {
            try {
                wmSoHeaderService.hold(soNo, orgId);
            } catch (WarehouseException e) {
                msg.addMessage(e.getMessage());
            }
        }
        // 消息
        if (StringUtils.isEmpty(msg.getMessage())) {
            msg.setSuccess(true);
            msg.addMessage("操作成功");
        }
        return msg;
    }

    /**
     * 取消冻结发运单
     *
     * @param soNos 出库单号
     * @param orgId 机构ID
     */
    public ResultMessage cancelHold(String[] soNos, String orgId) {
        ResultMessage msg = new ResultMessage();
        for (String soNo : soNos) {
            try {
                wmSoHeaderService.cancelHold(soNo, orgId);
            } catch (WarehouseException e) {
                msg.addMessage(e.getMessage());
            }
        }
        if (StringUtils.isEmpty(msg.getMessage())) {
            msg.setSuccess(true);
            msg.addMessage("操作成功");
        }
        return msg;
    }

    /**
     * 拦截订单
     *
     * @param soNos 出库单号
     * @param orgId 机构ID
     */
    public ResultMessage intercept(String[] soNos, String orgId) {
        ResultMessage msg = outboundBatchInterceptAction.outboundBatchIntercept(soNos, orgId);
        if (StringUtils.isEmpty(msg.getMessage())) {
            msg.setSuccess(true);
            msg.addMessage("操作成功");
        }
        return msg;
    }

    /**
     * 生成波次计划
     *
     * @param soNos        出库单号
     * @param waveRuleCode 波次规则编码
     * @param orgId        机构ID
     */
    public ResultMessage createWave(List<String> soNos, String waveRuleCode, String orgId) {
        ResultMessage msg = new ResultMessage();
        if (CollectionUtil.isEmpty(soNos)) {
            msg.addMessage("已生成波次，无法重复生成");
            msg.setSuccess(false);
            return msg;
        }
        try {
            Collections.sort(soNos);
            soNos = Lists.newArrayList(soNos);
            if (StringUtils.isEmpty(waveRuleCode)) {
                // 勾选直接生成波次
                outboundCreateWaveService.createWave(soNos, orgId);
            } else {
                // 根据波次规则生成波次
                outboundCreateWaveService.createWaveNew(soNos, waveRuleCode, orgId);
            }
        } catch (WarehouseException e) {
            msg.addMessage(e.getMessage());
            msg.setSuccess(false);
            return msg;
        }
        msg.setSuccess(true);
        msg.addMessage("操作成功");
        return msg;
    }

    /**
     * 生成波次计划-按波次组
     *
     * @param waveGroupId 波次规则组ID
     */
    public ResultMessage createWaveByGroup(String waveGroupId) {
        ResultMessage msg = new ResultMessage();
        try {
            BanQinCdRuleWvGroupHeader groupHeader = cdRuleWvGroupHeaderService.get(waveGroupId);
            if (null == groupHeader) {
                msg.addMessage("波次规则组不存在");
                msg.setSuccess(false);
                return msg;
            }
            // 查询符合条件的so
            Date orderDateF = new Date();
            orderDateF = DateUtil.setHours(orderDateF, Integer.parseInt(DateUtils.getHours(groupHeader.getOrderDateFm())));
            orderDateF = DateUtil.setMinutes(orderDateF, Integer.parseInt(DateUtils.getMinus(groupHeader.getOrderDateFm())));
            orderDateF = DateUtil.setSeconds(orderDateF, Integer.parseInt(DateUtils.getSeconds(groupHeader.getOrderDateFm())));
            groupHeader.setOrderDateFm(orderDateF);

            Date orderDateT = new Date();
            orderDateT = DateUtil.setHours(orderDateT, Integer.parseInt(DateUtils.getHours(groupHeader.getOrderDateTo())));
            orderDateT = DateUtil.setMinutes(orderDateT, Integer.parseInt(DateUtils.getMinus(groupHeader.getOrderDateTo())));
            orderDateT = DateUtil.setSeconds(orderDateT, Integer.parseInt(DateUtils.getSeconds(groupHeader.getOrderDateTo())));
            groupHeader.setOrderDateTo(orderDateT);

            BanQinCdRuleWvGroupHeaderEntity entity = new BanQinCdRuleWvGroupHeaderEntity();
            BeanUtils.copyProperties(groupHeader, entity);
            entity.setOwnerCodes(StringUtils.isEmpty(groupHeader.getOwnerCode()) ? null : groupHeader.getOwnerCode().split(","));
            entity.setSkuCodes(StringUtils.isEmpty(groupHeader.getSkuCode()) ? null : groupHeader.getSkuCode().split(","));
            List<String> soNos = wmSoHeaderService.findSoNosByWaveGroup(entity);
            if (CollectionUtil.isEmpty(soNos)) {
                msg.addMessage("没有符合条件的SO");
                msg.setSuccess(false);
                return msg;
            }
            List<BanQinCdRuleWvGroupDetail> groupDetails = cdRuleWvGroupDetailService.findByGroupCode(groupHeader.getGroupCode(), groupHeader.getOrgId());
            if (CollectionUtil.isEmpty(groupDetails)) {
                msg.addMessage("波次规则组明细不存在");
                msg.setSuccess(false);
                return msg;
            }
            for (BanQinCdRuleWvGroupDetail detail : groupDetails) {
                outboundCreateWaveService.createWaveNew(soNos, detail.getRuleCode(), detail.getOrgId());
            }
        } catch (DuplicateKeyException e) {
            msg.setSuccess(false);
            msg.addMessage("数据已过期");
            return msg;
        } catch (WarehouseException e) {
            msg.addMessage(e.getMessage());
            msg.setSuccess(false);
            return msg;
        }
        msg.setSuccess(true);
        msg.addMessage("操作成功");
        return msg;
    }

    /**
     * 生成波次计划-按波次组-so
     */
    public ResultMessage createWaveByGroupSo(BanQinCdRuleWvGroupHeader groupHeader) {
        ResultMessage msg = new ResultMessage();
        try {
            BanQinCdRuleWvGroupHeaderEntity entity = new BanQinCdRuleWvGroupHeaderEntity();
            BeanUtils.copyProperties(groupHeader, entity);
            entity.setOwnerCodes(StringUtils.isEmpty(groupHeader.getOwnerCode()) ? null : groupHeader.getOwnerCode().split(","));
            entity.setSkuCodes(StringUtils.isEmpty(groupHeader.getSkuCode()) ? null : groupHeader.getSkuCode().split(","));
            List<String> soNos = wmSoHeaderService.findSoNosByWaveGroup(entity);
            if (CollectionUtil.isEmpty(soNos)) {
                msg.addMessage("没有符合条件的SO");
                msg.setSuccess(false);
                return msg;
            }
            List<BanQinCdRuleWvGroupDetail> groupDetails = cdRuleWvGroupDetailService.findByGroupCode(groupHeader.getGroupCode(), groupHeader.getOrgId());
            if (CollectionUtil.isEmpty(groupDetails)) {
                msg.addMessage("波次规则组明细不存在");
                msg.setSuccess(false);
                return msg;
            }
            for (BanQinCdRuleWvGroupDetail detail : groupDetails) {
                outboundCreateWaveService.createWaveNew(soNos, detail.getRuleCode(), detail.getOrgId());
            }
        } catch (WarehouseException e) {
            msg.addMessage(e.getMessage());
            msg.setSuccess(false);
            return msg;
        }
        msg.setSuccess(true);
        msg.addMessage("操作成功");
        return msg;
    }

    /**
     * 预配- 按订单号
     */
    public ResultMessage preallocBySo(String processByCode, List<String> noList, String orgId) {
        return outboundBatchPreallocAction.outboundBatchPrealloc(processByCode, noList, orgId);
    }

    /**
     * 分配-按订单
     */
    public ResultMessage allocBySo(String processByCode, List<String> noList, String orgId) {
        ResultMessage msg;
        // 两步分配控制参数
        String twoStep = WmsConstants.NO;
        if (twoStep.equals(WmsConstants.YES)) {
            // 执行两步分配
            msg = outboundBatchAllocTwoStepAction.outboundBatchAllocTwoStep(processByCode, noList, orgId);
        } else {
            // 执行一步分配
            msg = outboundBatchAllocOneStepAction.outboundBatchAllocOneStep(processByCode, noList, orgId);
        }

        return msg;
    }

    /**
     * 拣货确认- 按订单
     */
    public ResultMessage pickingBySoHandler(String processByCode, List<String> noList, String orgId) {
        return outboundBatchPickingAction.outboundBatchPicking(processByCode, noList, orgId);
    }

    /**
     * 取消预配-按订单
     */
    public ResultMessage cancelPreallocBySo(String processByCode, List<String> noList, String orgId) {
        return outboundBatchCancelPreallocAction.outboundBatchCancelPrealloc(processByCode, noList, orgId);
    }

    /**
     * 取消分配-按订单
     */
    public ResultMessage cancelAllocBySo(String processByCode, List<String> noList, String orgId) {
        return outboundBatchCancelAllocAction.outboundBatchCancelAlloc(processByCode, noList, orgId);
    }

    /**
     * 取消拣货-按订单
     */
    public ResultMessage cancelPickingBySo(String processByCode, List<String> noList, String orgId) {
        return outboundBatchCancelPickingAction.outboundBatchCancelPicking(processByCode, noList, orgId);
    }

    /**
     * 发运确认- 按订单
     */
    public ResultMessage shipmentBySo(String processByCode, List<String> noList, String orgId) {
        return outboundBatchShipmentAction.outboundBatchShipment(processByCode, noList, WmsConstants.YES, orgId);
    }

    /**
     * 取消发货-按订单
     */
    public ResultMessage cancelShipmentBySo(String processByCode, List<String> noList, String orgId) {
        return outboundBatchCancelShipmentAction.outboundBatchCancelShipment(processByCode, noList, orgId);
    }

    /**
     * 关闭订单
     *
     * @param soNos 出库单号
     * @param orgId 机构ID
     */
    public ResultMessage close(String[] soNos, String orgId) {
        ResultMessage msg = new ResultMessage();
        for (String soNo : soNos) {
            try {
                // 订单关闭
                outboundCloseOrCancelService.close(soNo, orgId);
            } catch (WarehouseException e) {
                msg.addMessage(e.getMessage());
            }
        }
        // 消息
        if (StringUtils.isEmpty(msg.getMessage())) {
            msg.setSuccess(true);
            msg.addMessage("操作成功");
        }
        return msg;
    }

    /**
     * 取消订单
     *
     * @param soNos 出库单号
     * @param orgId 机构ID
     */
    public ResultMessage cancelSo(String[] soNos, String orgId) {
        ResultMessage msg = new ResultMessage();
        for (String soNo : soNos) {
            try {
                outboundCloseOrCancelService.cancelSo(soNo, orgId);
            } catch (WarehouseException e) {
                msg.addMessage(e.getMessage());
            }
        }
        // 消息
        if (StringUtils.isEmpty(msg.getMessage())) {
            msg.setSuccess(true);
            msg.addMessage("操作成功");
        }
        return msg;
    }

    /**
     * 选择发运订单 生成装车单
     *
     * @param soNos 出库单号
     * @param orgId 机构ID
     */
    public ResultMessage generateLd(List<String> soNos, String orgId) {
        ResultMessage msg = new ResultMessage();

        if (CollectionUtil.isEmpty(soNos)) {
            msg.setSuccess(false);
            msg.addMessage("无可操作的订单");
            return msg;
        }
        // 勾选发运订单生成装车单，获取不符合条件的发运订单 ,发运订单已经生成装车单(完全或者部分)，不可再生成
        List<String> errorSoNos = wmLdDetailService.checkIsGenLdBySoNos(soNos, null, orgId);
        if (CollectionUtil.isNotEmpty(errorSoNos)) {
            soNos.removeAll(errorSoNos);
            if (CollectionUtil.isEmpty(soNos)) {
                msg.setSuccess(false);
                msg.addMessage("已经生成装车单，不能操作");
                return msg;
            }
        }
        /* 勾选发运订单生成装车单，获取不符合条件的发运订单,拣货明细未复核，不可生成 */
        errorSoNos = wmSoAllocService.checkNoFullCheckBySoNOs(soNos, orgId);
        if (CollectionUtil.isNotEmpty(errorSoNos)) {
            soNos.removeAll(errorSoNos);
            if (CollectionUtil.isEmpty(soNos)) {
                msg.setSuccess(false);
                msg.addMessage("未完全复核，不能操作");
                return msg;
            }
        }
        // 生成装车单
        // 装车单号
        String ldNo = null;
        try {
            ldNo = this.wmLdHeaderService.generateLdEntity(soNos.toArray(new String[0]), orgId);
        } catch (WarehouseException e) {
            msg.setSuccess(false);
            msg.addMessage(e.getMessage());
        }
        if (ldNo != null && StringUtils.isEmpty(msg.getMessage())) {
            msg.setSuccess(true);
            msg.addMessage("操作成功，生成的装车单号" + ldNo);
        }
        return msg;
    }

    /**
     * 未生成装车单的发运订单，可直接进行装车交接
     *
     * @param soNos 出库单号
     * @param orgId 机构ID
     */
    public ResultMessage deliverBySoNos(List<String> soNos, String orgId) {
        ResultMessage msg = new ResultMessage();
        String errorMsg = "";
        // 勾选发运订单生成装车单，获取不符合条件的发运订单 ,发运订单已经生成装车单(完全或者部分)，不可进行装车交接
        List<String> errorSoNos = wmLdDetailService.checkIsGenLdBySoNos(soNos, null, orgId);
        if (errorSoNos.size() > 0) {
            soNos.removeAll(errorSoNos);
            errorMsg = "已经生成装车单，不能操作的发运订单";
        }
        // 更新装车状态 - 完全交接
        for (String soNo : soNos) {
            try {
                outboundCommon.updateLdStatusByNoLd(soNo, WmsCodeMaster.LD_FULL_DELIVERY.getCode(), orgId);
            } catch (WarehouseException e) {
                msg.setSuccess(false);
                msg.addMessage(e.getMessage());
            }
        }
        if (StringUtils.isNotEmpty(errorMsg)) {
            msg.addMessage(errorMsg);
        }
        if (StringUtils.isEmpty(msg.getMessage())) {
            msg.setSuccess(true);
            msg.addMessage("操作成功");
        }
        return msg;
    }

    /**
     * 取消交接(未生成装车单的发运订单，直接进行装车交接)
     *
     * @param soNos 出库单号
     * @param orgId 机构ID
     */
    public ResultMessage cancelDeliverBySoNos(List<String> soNos, String orgId) {
        ResultMessage msg = new ResultMessage();
        String errorMsg = "";
        // 勾选发运订单生成装车单，获取不符合条件的发运订单 ,发运订单已经生成装车单(完全或者部分)，不可在出库单界面进行取消交接
        List<String> errorSoNos = wmLdDetailService.checkIsGenLdBySoNos(soNos, null, orgId);
        if (errorSoNos.size() > 0) {
            soNos.removeAll(errorSoNos);
            errorMsg = "已经生成装车单，不能操作的发运订单";
        }
        // 更新装车状态 - 取消交接
        for (String soNo : soNos) {
            try {
                outboundCommon.updateLdStatusByNoLd(soNo, null, orgId);// 装车状态清空
            } catch (WarehouseException e) {
                msg.setSuccess(false);
                msg.addMessage(e.getMessage());
            }
        }
        if (StringUtils.isNotEmpty(errorMsg)) {
            msg.addMessage(errorMsg);
        }
        if (StringUtils.isEmpty(msg.getMessage())) {
            msg.setSuccess(true);
            msg.addMessage("操作成功");
        }
        return msg;
    }

    /**
     * SO 明细行保存
     */
    public ResultMessage saveSoDetailEntity(BanQinWmSoDetailEntity soDetailEntity) {
        ResultMessage msg = new ResultMessage();
        try {
            // 校验单据状态，是否可修改(并发控制)
            wmSoHeaderService.checkStatus(soDetailEntity.getSoNo(), null, soDetailEntity.getOrgId());
            wmSoDetailService.saveSoDetail(soDetailEntity);
        } catch (WarehouseException e) {
            msg.addMessage(e.getMessage());
            msg.setSuccess(false);
            return msg;
        }
        msg.addMessage("操作成功");
        msg.setSuccess(true);
        return msg;
    }

    /**
     * SO 明细行删除
     *
     * @param soNo    出库单号
     * @param lineNos 出库单明细行号
     * @param orgId   机构ID
     */
    public ResultMessage removeSoDetailEntity(String soNo, String[] lineNos, String orgId) {
        ResultMessage msg = new ResultMessage();
        try {
            // 校验单据状态，是否可修改(并发控制)
            wmSoHeaderService.checkStatus(soNo, null, orgId);
            wmSoDetailService.removeBySoNoAndLineNos(soNo, lineNos, orgId);
        } catch (WarehouseException e) {
            msg.addMessage(e.getMessage());
            msg.setSuccess(false);
            return msg;
        }
        msg.addMessage("操作成功");
        msg.setSuccess(true);
        return msg;
    }

    /**
     * 预配-按订单行
     */
    public ResultMessage preallocBySoLine(String processByCode, String soNo, String[] lineNos, String orgId) {
        List<String> noList = new ArrayList<>();
        for (String lineNo : lineNos) {
            noList.add("('" + soNo + "','" + lineNo + "')");
        }
        return outboundBatchPreallocAction.outboundBatchPrealloc(processByCode, noList, orgId);
    }

    /**
     * 分配-按订单行
     */
    public ResultMessage allocBySoLine(String processByCode, String soNo, String[] lineNos, String orgId) {
        List<String> noList = new ArrayList<>();
        for (String lineNo : lineNos) {
            noList.add("('" + soNo + "','" + lineNo + "')");
        }
        // 两步分配控制参数
        String twoStep = WmsConstants.NO;
        if (twoStep.equals(WmsConstants.YES)) {
            // 执行两步分配
            return outboundBatchAllocTwoStepAction.outboundBatchAllocTwoStep(processByCode, noList, orgId);
        } else {
            // 执行一步分配
            return outboundBatchAllocOneStepAction.outboundBatchAllocOneStep(processByCode, noList, orgId);
        }
    }

    /**
     * 拣货确认-按订单行
     */
    public ResultMessage pickingBySoLine(String processByCode, String soNo, String[] lineNos, String orgId) {
        List<String> noList = new ArrayList<>();
        for (String lineNo : lineNos) {
            noList.add("('" + soNo + "','" + lineNo + "')");
        }
        return outboundBatchPickingAction.outboundBatchPicking(processByCode, noList, orgId);
    }

    /**
     * 取消预配-按订单行
     */
    public ResultMessage cancelPreallocBySoLine(String processByCode, String soNo, String[] lineNos, String orgId) {
        List<String> noList = new ArrayList<>();
        for (String lineNo : lineNos) {
            noList.add("('" + soNo + "','" + lineNo + "')");
        }
        return outboundBatchCancelPreallocAction.outboundBatchCancelPrealloc(processByCode, noList, orgId);
    }

    /**
     * 取消分配-按订单行
     */
    public ResultMessage cancelAllocBySoLine(String processByCode, String soNo, String[] lineNos, String orgId) {
        List<String> noList = new ArrayList<>();
        for (String lineNo : lineNos) {
            noList.add("('" + soNo + "','" + lineNo + "')");
        }
        return outboundBatchCancelAllocAction.outboundBatchCancelAlloc(processByCode, noList, orgId);
    }

    /**
     * 取消拣货-按订单行
     */
    public ResultMessage cancelPickingBySoLine(String processByCode, String soNo, String[] lineNos, String orgId) {
        List<String> noList = new ArrayList<>();
        for (String lineNo : lineNos) {
            noList.add("('" + soNo + "','" + lineNo + "')");
        }
        return outboundBatchCancelPickingAction.outboundBatchCancelPicking(processByCode, noList, orgId);
    }

    /**
     * 订单行取消
     */
    public ResultMessage cancelSoDetail(String soNo, String[] lineNos, String orgId) {
        ResultMessage msg = new ResultMessage();
        for (String lineNo : lineNos) {
            try {
                outboundCloseOrCancelService.cancelSoDetail(soNo, lineNo, orgId);
            } catch (WarehouseException e) {
                msg.addMessage(e.getMessage());
            }
        }
        // 消息
        if (StringUtils.isEmpty(msg.getMessage())) {
            msg.setSuccess(true);
            msg.addMessage("操作成功");
        }
        return msg;
    }

    /**
     * 根据发运订单号、行号获取发运订单信息
     */
    protected BanQinWmSoEntity getSoEntityByAlloc(BanQinWmSoAllocEntity entity) {
        // 获取出库单
        BanQinWmSoEntity soEntity = wmSoHeaderService.findEntityBySoNo(entity.getSoNo(), entity.getOrgId());
        // 获取商品明细
        soEntity.setDetailList(wmSoDetailService.findEntityBySoNo(entity.getSoNo(), entity.getOrgId()));
        // 获取预配明细
        soEntity.setPreallocList(wmSoPreallocService.getEntityBySoNo(entity.getSoNo(), entity.getOrgId()));
        // 分配当前记录
        if (entity.getAllocId() != null) {
            soEntity.setAllocEntity(wmSoAllocService.getEntityByAllocId(entity.getAllocId(), entity.getOrgId()));
        }
        // 获取分配明细
        soEntity.setAllocList(wmSoAllocService.getEntityBySoNo(entity.getSoNo(), entity.getOrgId()));
        return soEntity;
    }

    /**
     * 手工分配
     */
    public ResultMessage manualAlloc(BanQinWmSoAllocEntity entity) {
        ResultMessage msg = new ResultMessage();
        try {
            outboundManualAllocService.outboundManualAlloc(entity);
        } catch (WarehouseException e) {
            msg.setSuccess(false);
            msg.addMessage(e.getMessage());
            return msg;
        }
        msg.setSuccess(true);
        msg.addMessage("操作成功");
        return msg;
    }

    /**
     * 拣货确认 -- 分配明细
     */
    public ResultMessage pickingByAlloc(List<BanQinWmSoAllocEntity> entityList) {
        return outboundBatchPickingAction.outboundBatchPicking(entityList);
    }

    /**
     * 手工拣货确认
     */
    public ResultMessage pickingByManual(BanQinWmSoAllocEntity entity) {
        return outboundBatchPickingAction.pickingByManual(entity);
    }

    /**
     * 发运确认 -- 分配明细
     */
    public ResultMessage shipmentByAlloc(List<BanQinWmSoAllocEntity> entityList) {
        return outboundBatchShipmentAction.outboundBatchShipment(entityList);
    }

    /**
     * 取消分配 -- 分配明细
     */
    public ResultMessage cancelAllocByAlloc(List<BanQinWmSoAllocEntity> entityList) {
        return outboundBatchCancelAllocAction.outboundBatchCancelAlloc(entityList);
    }

    /**
     * 取消拣货 -- 分配明细
     */
    public ResultMessage cancelPickingByAlloc(List<BanQinWmSoAllocEntity> entityList) {
        return outboundBatchCancelPickingAction.outboundBatchCancelPicking(entityList);
    }

    /**
     * 取消发货 -- 分配明细
     */
    public ResultMessage cancelShipmentByAlloc(List<BanQinWmSoAllocEntity> entityList) {
        return outboundBatchCancelShipmentAction.outboundBatchCancelShipment(entityList);
    }

    /**
     * 拣货确认 -- 拣货任务
     */
    public ResultMessage picking(List<BanQinWmSoAllocEntity> entityList) {
        return outboundBatchPickingAction.outboundBatchPicking(entityList);
    }

    /**
     * 发运确认 -- 拣货任务
     */
    public ResultMessage shipment(List<BanQinWmSoAllocEntity> entityList) {
        return outboundBatchShipmentAction.outboundBatchShipment(entityList);
    }

    /**
     * 取消分配 -- 拣货任务
     */
    public ResultMessage cancelAlloc(List<BanQinWmSoAllocEntity> entityList) {
        return outboundBatchCancelAllocAction.outboundBatchCancelAlloc(entityList);
    }

    /**
     * 取消拣货 -- 拣货任务
     */
    public ResultMessage cancelPicking(List<BanQinWmSoAllocEntity> entityList) {
        return outboundBatchCancelPickingAction.outboundBatchCancelPicking(entityList);
    }

    /**
     * 取消发货 -- 拣货任务
     */
    public ResultMessage cancelShipment(List<BanQinWmSoAllocEntity> entityList) {
        return outboundBatchCancelShipmentAction.outboundBatchCancelShipment(entityList);
    }

    /**
     * 获取包装信息
     *
     * @param packCode 包装代码
     * @param uom      包装单位
     * @param orgId    机构ID
     */
    public ResultMessage getPackageRelationAndQtyUom(String packCode, String uom, String orgId) {
        ResultMessage msg = new ResultMessage();
        BanQinCdWhPackageRelationEntity packageEntity;
        try {
            packageEntity = wmCommon.getPackageRelationAndQtyUom(packCode, uom, orgId);
        } catch (WarehouseException e) {
            msg.addMessage(e.getMessage());
            msg.setSuccess(false);
            return msg;
        }
        msg.setSuccess(true);
        msg.setData(packageEntity);
        return msg;
    }

    /**
     * 复核/打包查询
     */
    public ResultMessage getCheckByCondition(BanQinWmSoAllocEntity condition) {
        ResultMessage msg = new ResultMessage();
        BanQinWmCheckEntity entity;
        try {
            entity = wmSoAllocService.getCheckByCondition(condition);
        } catch (WarehouseException e) {
            msg.addMessage(e.getMessage());
            msg.setSuccess(false);
            return msg;
        }
        msg.setSuccess(true);
        msg.setData(entity);
        return msg;
    }

    /**
     * 批量复核
     */
    public ResultMessage checkConfirm(String[] allocIds, List<BanQinWmSoSerialEntity> soSerialList, String trackingNo, List<String> noList, String processByCode, String orgId) {
        return outboundCheckAction.checkConfirm(allocIds, soSerialList, trackingNo, noList, processByCode, orgId);
    }

    /**
     * 取消复核
     */
    public ResultMessage cancelCheck(String[] allocIds, String orgId) {
        ResultMessage msg = new ResultMessage();
        try {
            outboundCheckService.checkCancel(allocIds, orgId);
        } catch (WarehouseException e) {
            msg.setSuccess(false);
            msg.addMessage(e.getMessage());
            return msg;
        }
        msg.setSuccess(true);
        msg.addMessage("操作成功");
        return msg;
    }

    /**
     * 打包查询
     */
    public ResultMessage getPackByCondition(BanQinWmSoAllocEntity condition) {
        ResultMessage msg = new ResultMessage();
        BanQinWmPackEntity entity;
        try {
            entity = wmSoAllocService.getPackByCondition(condition);
        } catch (WarehouseException e) {
            msg.addMessage(e.getMessage());
            msg.setSuccess(false);
            return msg;
        }
        msg.setSuccess(true);
        msg.setData(entity);
        return msg;
    }

    /**
     * 打包确认
     */
    public ResultMessage updateTraceId(List<BanQinWmSoAllocEntity> allocItems, String toId, List<BanQinWmSoSerialEntity> soSerialList, String trackingNo, String isCheck, String isPrintContainer, String isPrintLabel, String orgId) {
        return outboundPackAction.updateTraceId(allocItems, toId, soSerialList, trackingNo, isCheck, isPrintContainer, isPrintLabel, orgId);
    }

    /**
     * 获取新的包装单位及单位换算数量
     *
     * @param packCode 包装代码
     * @param uomQty   包装单位数量
     * @param qtyEa    包装单位EA数量
     * @param orgId    机构ID
     */
    public ResultMessage getCdWhPackageRelation(String packCode, Double uomQty, Double qtyEa, String orgId) {
        ResultMessage msg = new ResultMessage();
        BanQinCdWhPackageRelation item = new BanQinCdWhPackageRelation();
        BanQinCdWhPackageEntity items = wmCommon.getCdWhPackageRelation(packCode, orgId);
        for (BanQinCdWhPackageRelation itemTemp : items.getCdWhPackageRelations()) {
            // 如果包装单位换算<=当前行单位换算数量
            if (itemTemp.getCdprQuantity() > uomQty) {
                continue;
            }
            // 计算单位数量，校验是否是小数，是则继续获取下一级单位
            double qtyUom = qtyEa / itemTemp.getCdprQuantity();
            String qtyUomStr = Double.toString(qtyUom);
            if (qtyUomStr.indexOf(".") > 0) {
                qtyUomStr = qtyUomStr.replaceAll("0+?$", "");// 去掉多余的0
                qtyUomStr = qtyUomStr.replaceAll("[.]$", "");// 如最后一位是.则去掉
            }
            // 正则表达式判断是否为小数
            Pattern pattern = Pattern.compile("\\d+\\.\\d+$|-\\d+\\.\\d+$");// 判断是否为小数
            // 不是
            if (!pattern.matcher(qtyUomStr).matches()) {
                item = itemTemp;
                break;
            }
        }
        msg.setSuccess(true);
        msg.addMessage("操作成功");
        msg.setData(item);
        return msg;
    }

    /**
     * 根据目标跟踪号获取快递单号
     *
     * @param toId  目标跟踪号
     * @param orgId 机构ID
     */
    public String getTrackingNoByToId(String toId, String orgId) {
        return wmSoAllocService.getTrackingNoByToId(toId, orgId);
    }

    /**
     * 根据分配ID获取出库序列号
     *
     * @param allocIds 分配ID
     * @param orgId    机构ID
     */
    public List<BanQinWmSoSerialEntity> getSoSerialItemByAllocIds(String[] allocIds, String orgId) {
        return wmSoSerialService.findByAllocIds(Arrays.asList(allocIds), orgId);
    }

    /**
     * 1、校验序列号是否存在 2、校验序列号是否唯一 3、校验序列号批次号与拣货记录的批次号是否一致
     *
     * @param soNo      出库单号
     * @param ownerCode 货主编码
     * @param skuCode   商品编码
     * @param serialNo  序列号
     * @param lotNums   批次号
     * @param allocIds  分配ID
     * @param orgId     机构ID
     */
    public ResultMessage checkSoSerial(String soNo, String ownerCode, String skuCode, String serialNo, List<String> lotNums, List<String> allocIds, String orgId) {
        ResultMessage msg = new ResultMessage();
        // 1、校验序列号是否存在
        BanQinWmInvSerial invSerialModel = wmInvSerialService.getByOwnerCodeAndSkuCodeAndSerialNo(ownerCode, skuCode, serialNo, orgId);
        if (invSerialModel == null) {
            msg.addMessage("查询不到货主[" + ownerCode + "]商品[" + skuCode + "]的序列号[" + serialNo + "]");
            msg.setSuccess(false);
            return msg;
        }
        // 校验重复
        List<BanQinWmSoSerial> soSerialModels = wmSoSerialService.findByOwnerCodeAndSkuCodeAndSerialNo(ownerCode, skuCode, serialNo, orgId);
        if (soSerialModels.size() > 0) {
            for (BanQinWmSoSerial model : soSerialModels) {
                BanQinWmSoAlloc soAllocModel = wmSoAllocService.getByAllocId(model.getAllocId(), model.getOrgId());
                // 未出库的序列号存在于另一SO单中
                if (!model.getSoNo().equals(soNo) && serialNo.equals(invSerialModel.getSerialNo()) && soAllocModel.getStatus().equals(WmsCodeMaster.ALLOC_FULL_PICKING.getCode())) {
                    msg.addMessage("序列号出现重复，不能操作");
                    msg.setSuccess(false);
                    return msg;
                }
            }
        }
        BanQinWmSoSerial soSerialModel = wmSoSerialService.findBySoNoAndSkuCodeAndSerialNo(soNo, skuCode, serialNo, orgId);
        if (soSerialModel != null) {
            // 校验序列号是否唯一(分配ID) 如果不存在于分配ID集中，那么不唯一
            if (allocIds.size() > 0 && (!allocIds.contains(soSerialModel.getAllocId()))) {
                msg.addMessage("同序列号存在于不同的拣货记录中，不能操作");
                msg.setSuccess(false);
                return msg;
            }
            // 校验序列号批次号与拣货记录的批次号是否一致 如果不存在于批次号数据集，那么批次号不一致
            if (lotNums.size() > 0 && (!lotNums.contains(soSerialModel.getLotNum()))) {
                msg.addMessage("相同序列号存在于不同的批次中，不能操作");
                msg.setSuccess(false);
                return msg;
            }
        } else {
            soSerialModel = new BanQinWmSoSerial();
            // 将库存序列号复制到出库序列号中，并且返回前台
            BeanUtils.copyProperties(invSerialModel, soSerialModel);
            soSerialModel.setId(null);
        }
        // 如果在出库序列号表中没有查找到记录，那么序列号在出库序列号表目前唯一
        msg.setSuccess(true);
        msg.setData(soSerialModel);
        msg.addMessage("操作成功");
        return msg;
    }

    /**
     * 保存出库序列号
     */
    public ResultMessage saveSoSerial(BanQinWmSoSerial soSerialModel) {
        ResultMessage msg = new ResultMessage();

        BanQinWmSoSerial model = new BanQinWmSoSerial();
        BeanUtils.copyProperties(soSerialModel, model);
        this.wmSoSerialService.save(model);

        msg.setSuccess(true);
        msg.addMessage("操作成功");
        return msg;
    }

    /**
     * 删除出库序列号
     */
    public ResultMessage removeSoSerial(BanQinWmSoSerial soSerialModel) {
        ResultMessage msg = new ResultMessage();
        this.wmSoSerialService.delete(soSerialModel);
        msg.setSuccess(true);
        msg.addMessage("操作成功");
        return msg;
    }

    /**
     * 根据发运单号获取取消分配记录
     *
     * @param soNo  出库单号
     * @param orgId 机构ID
     */
    public List<BanQinWmDelAllocEntity> getWmDelAllocEntityBySoNo(String soNo, String orgId) {
        return wmDelAllocService.getEntityByOrderNo(soNo, orgId);
    }

    /**
     * 根据发运单号获取取消预配记录
     *
     * @param soNo  出库单号
     * @param orgId 机构ID
     */
    public List<BanQinWmDelPreallocEntity> getWmDelPreallocEntityBySoNo(String soNo, String orgId) {
        return wmDelPreallocService.getEntityBySoNo(soNo, orgId);
    }

    /**
     * 取消打包
     *
     * @param allocIds      分配ID
     * @param isCancelCheck 是否同时取消复核
     * @param orgId         机构ID
     */
    public ResultMessage cancelPack(List<String> allocIds, String isCancelCheck, String orgId) {
        return outboundPackAction.cancelPack(allocIds, isCancelCheck, orgId);
    }

    /**
     * 生成拣货单
     */
    public ResultMessage createPick(List<BanQinWmSoAllocEntity> list) {
        ResultMessage msg = new ResultMessage();
        if (CollectionUtil.isEmpty(list)) {
            msg.addMessage("请选择数据！");
            msg.setSuccess(false);
            return msg;
        }
        try {
            banQinOutboundCreatePickService.createPick(list.stream().map(BanQinWmSoAlloc::getAllocId).collect(Collectors.toList()), list.get(0).getOrgId());
        } catch (WarehouseException e) {
            msg.addMessage(e.getMessage());
            msg.setSuccess(false);
            return msg;
        }
        msg.setSuccess(true);
        msg.addMessage("操作成功");
        return msg;
    }
}
