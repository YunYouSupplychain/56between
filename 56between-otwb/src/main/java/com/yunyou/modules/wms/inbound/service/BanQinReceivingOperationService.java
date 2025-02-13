package com.yunyou.modules.wms.inbound.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhLoc;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhSku;
import com.yunyou.modules.wms.basicdata.service.BanQinCdWhLocService;
import com.yunyou.modules.wms.basicdata.service.BanQinCdWhSkuService;
import com.yunyou.modules.wms.common.entity.*;
import com.yunyou.modules.wms.common.service.BanQinWmsCommonService;
import com.yunyou.modules.wms.inbound.entity.*;
import com.yunyou.modules.wms.inventory.entity.BanQinWmInvLotAtt;
import com.yunyou.modules.wms.inventory.entity.BanQinWmInvLotLoc;
import com.yunyou.modules.wms.inventory.entity.BanQinWmInvSerialEntity;
import com.yunyou.modules.wms.inventory.service.BanQinInventoryService;
import com.yunyou.modules.wms.inventory.service.BanQinWmInvLotLocService;
import com.yunyou.modules.wms.inventory.service.BanQinWmInvSerialService;
import com.yunyou.modules.wms.task.entity.BanQinWmTaskPa;
import com.yunyou.modules.wms.task.entity.BanQinWmTaskPaEntity;
import com.yunyou.modules.wms.task.service.BanQinWmTaskPaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 描述： 入库收货操作Service
 *
 * @auther: Jianhua on 2019/1/28
 */
@Service
public class BanQinReceivingOperationService {
    @Autowired
    private BanQinWmAsnDetailService banQinWmAsnDetailService;
    @Autowired
    private BanQinWmAsnDetailReceiveService banQinWmAsnDetailReceiveService;
    @Autowired
    private BanQinInventoryService banQinInventoryService;
    @Autowired
    private BanQinWmAsnSerialService banQinWmAsnSerialService;
    @Autowired
    private BanQinCdWhSkuService banQinCdWhSkuService;
    @Autowired
    private BanQinWmInvSerialService banQinWmInvSerialService;
    @Autowired
    private BanQinCdWhLocService banQinCdWhLocService;
    @Autowired
    private BanQinInboundOperationService banQinInboundOperationService;
    @Autowired
    private BanQinWmsCommonService banQinWmsCommonService;
    @Autowired
    private BanQinWmInvLotLocService banQinWmInvLotLocService;
    @Autowired
    private BanQinWmTaskPaService banQinWmTaskPaService;
    @Autowired
    private BanQinWmPoDetailService banQinWmPoDetailService;
    @Autowired
    private BanQinInboundPaOperationService banQinInboundPaOperationService;

    public Object locker = new Object();

    /**
     * 描述： 入库收货
     *
     * @param receiveEntity
     * @author Jianhua on 2019/1/28
     */
    @Transactional
    public ResultMessage inboundReceiving(BanQinWmAsnDetailReceiveEntity receiveEntity) throws WarehouseException {
        ResultMessage msg = new ResultMessage();
        /*
         * 收货前校验 a.校验数据是否过期 b.订单状态的校验 c.校验是否允许超收 d.生产日期、失效日期的逻辑校验
         * e.超过商品有效期控制天数,拒收
         */
        BanQinCdWhSku skuModel = banQinCdWhSkuService.getByOwnerAndSkuCode(receiveEntity.getOwnerCode(), receiveEntity.getSkuCode(), receiveEntity.getOrgId());
        receiveEntity = this.checkBeforeReceiving(receiveEntity, skuModel);
        // 参数
        ActionCode action = receiveEntity.getActionCode();
        String ownerCode = receiveEntity.getOwnerCode();
        String skuCode = receiveEntity.getSkuCode();
        String toLoc = receiveEntity.getToLoc();
        String asnNo = receiveEntity.getAsnNo();
        String lineNo = receiveEntity.getLineNo();
        String paRule = receiveEntity.getPaRule();
        String reserveCode = receiveEntity.getReserveCode();
        Double currentQtyRcvEa = receiveEntity.getCurrentQtyRcvEa();// 当前收货数
        Double qtyPlanEa = receiveEntity.getQtyPlanEa();
        String packCode = receiveEntity.getPackCode();
        String uom = receiveEntity.getUom();
        Double rcvQtyUom = receiveEntity.getCurrentQtyRcvUom();
        String asnType = receiveEntity.getOrderType();
        String planPaLoc = receiveEntity.getPlanPaLoc();
        // 收货跟踪号
        String toId = StringUtils.isEmpty(receiveEntity.getToId()) ? WmsConstants.TRACE_ID : receiveEntity.getToId();

        /*
         * 收货时一个ID多次收货时： 1.获取目标库位的信息，校验是否混放SKU、是否混批 2.校验traceID不能混SKU，混批次。
         * 3.traceId存在库存，校验当前ASN单的收货明细中，是否存在同一个traceID的已收货明细，如果不存在，该库存可能来自其他ASN，不收货。
         * 4.如果traceId已经生成上架任务，不能重复收
         */
        // 生成批次号
        BanQinWmInvLotAtt wmInvLotAttModel = new BanQinWmInvLotAtt();
        wmInvLotAttModel.setOwnerCode(ownerCode);
        wmInvLotAttModel.setSkuCode(skuCode);
        wmInvLotAttModel.setOrgId(receiveEntity.getOrgId());
        wmInvLotAttModel.setLotAtt01(receiveEntity.getLotAtt01());
        wmInvLotAttModel.setLotAtt02(receiveEntity.getLotAtt02());
        wmInvLotAttModel.setLotAtt03(receiveEntity.getLotAtt03());
        wmInvLotAttModel.setLotAtt04(receiveEntity.getLotAtt04());
        wmInvLotAttModel.setLotAtt05(receiveEntity.getLotAtt05());
        wmInvLotAttModel.setLotAtt06(receiveEntity.getLotAtt06());
        wmInvLotAttModel.setLotAtt07(receiveEntity.getLotAtt07());
        wmInvLotAttModel.setLotAtt08(receiveEntity.getLotAtt08());
        wmInvLotAttModel.setLotAtt09(receiveEntity.getLotAtt09());
        wmInvLotAttModel.setLotAtt10(receiveEntity.getLotAtt10());
        wmInvLotAttModel.setLotAtt11(receiveEntity.getLotAtt11());
        wmInvLotAttModel.setLotAtt12(receiveEntity.getLotAtt12());
        String lotnum = this.banQinInventoryService.createInvLotNum(wmInvLotAttModel);

        // 扫描序列号收货，同收货一样。
        if (ActionCode.SCAN_RECEIVING.getCode().equals(receiveEntity.getActionCode().getCode())) {
            action = ActionCode.RECEIVING;
        }

        // 收货前计算库位，收货动作改变。
        if (WmsCodeMaster.RESERVE_B_RCV_TWO.getCode().equals(reserveCode)) {
            action = ActionCode.PLAN_RECEIVING;
        } else if (WmsCodeMaster.RESERVE_B_RCV_ONE.getCode().equals(reserveCode)) {
            action = ActionCode.ONESTEP_PLAN_RECEIVING;
        }

        // 计划收货
        BanQinInventoryEntity fmEntity = new BanQinInventoryEntity();
        fmEntity.setAction(action);
        fmEntity.setOrderNo(asnNo);
        fmEntity.setLineNo(lineNo);
        fmEntity.setOwnerCode(ownerCode);
        fmEntity.setSkuCode(skuCode);
        fmEntity.setLocCode(planPaLoc);// 计划上架库位
        fmEntity.setLotNum(receiveEntity.getLotNum());// 安排库位时产生的批次号
        fmEntity.setTraceId(receiveEntity.getPlanId());// 码盘跟踪号
        fmEntity.setPackCode(packCode);
        fmEntity.setUom(uom);
        fmEntity.setQtyUom(rcvQtyUom);
        fmEntity.setQtyEaOp(currentQtyRcvEa);// 实际收货数
        fmEntity.setPlanRecQty(qtyPlanEa);// 计划预约数
        fmEntity.setOrgId(receiveEntity.getOrgId());

        // 实际收货
        BanQinInventoryEntity toEntity = new BanQinInventoryEntity();
        toEntity.setAction(action);
        toEntity.setOrderNo(asnNo);
        toEntity.setLineNo(lineNo);
        toEntity.setOwnerCode(ownerCode);
        toEntity.setSkuCode(skuCode);
        toEntity.setLocCode(toLoc);// 实际收货库位
        toEntity.setLotNum(lotnum);// 实际收货批号
        toEntity.setTraceId(toId);// 收货跟踪号
        toEntity.setPackCode(packCode);
        toEntity.setUom(uom);
        toEntity.setQtyUom(rcvQtyUom);
        toEntity.setQtyEaOp(currentQtyRcvEa);// 实际收货数
        toEntity.setPlanRecQty(qtyPlanEa);// 计划预约数
        toEntity.setOrgId(receiveEntity.getOrgId());

        // 库存更新，返回收货后的库存信息
        BanQinInventoryEntity updatedInv;
        // 如果收货前计算库位，并且 计划上架库位不为空（预约到库位），则使用预约收货
        if (StringUtils.isNotEmpty(planPaLoc) && (WmsCodeMaster.RESERVE_B_RCV_ONE.getCode().equals(reserveCode) || WmsCodeMaster.RESERVE_B_RCV_TWO.getCode().equals(reserveCode))) {
            updatedInv = banQinInventoryService.updateInventory(fmEntity, toEntity);
        } else {
            // 校验traceID不能混SKU，混批次，混ASN
            this.checkTraceId(asnNo, toId, skuCode, lotnum, receiveEntity.getOrgId());
            updatedInv = banQinInventoryService.updateInventory(toEntity);
        }
        lotnum = updatedInv.getLotNum();
        receiveEntity.setLotNum(lotnum);

        // 保存序列号
        // 序列号信息
        List<BanQinWmAsnSerialEntity> serialList = receiveEntity.getSerialList();
        if (null != serialList && serialList.size() > 0) {
            for (BanQinWmAsnSerialEntity wmAsnSerialEntity : serialList) {
                // 保存序列号到入库序列号表
                banQinWmAsnSerialService.saveRcvSerial(asnNo, ownerCode, skuCode, lotnum, wmAsnSerialEntity);
                // 收货已扫描的序列号
                if (!wmAsnSerialEntity.getStatus().equals(WmsCodeMaster.SERIAL_PLAN_NOT_SCAN.getCode()) && null != wmAsnSerialEntity.getRcvLineNo()) {
                    BanQinWmInvSerialEntity serialEntity = new BanQinWmInvSerialEntity();
                    serialEntity.setOrderNo(asnNo);
                    serialEntity.setOrderType(WmsCodeMaster.ORDER_ASN.getCode());
                    serialEntity.setSerialTranType(WmsCodeMaster.RCV.getCode());
                    serialEntity.setLineNo(lineNo);
                    serialEntity.setTranId(updatedInv.getTranId());
                    serialEntity.setOwnerCode(wmAsnSerialEntity.getOwnerCode());
                    serialEntity.setSkuCode(wmAsnSerialEntity.getSkuCode());
                    serialEntity.setSerialNo(wmAsnSerialEntity.getSerialNo());
                    serialEntity.setLotNum(lotnum);
                    banQinWmInvSerialService.updateInventorySerial(serialEntity);
                }
            }
        }

        /*
         * 部分收货：预收数>实收数，拆分收货明细。 更新当前收货明细状态，回填ASN明细的收货数，PO单的收货数。 更新ASN单和PO单状态
         */

        // 库位为忽略ID时，更新完库存，跟踪号置*
        toId = updatedInv.getTraceId();
        receiveEntity.setToId(toId);
        if (null == receiveEntity.getRcvTime()) {
            receiveEntity.setRcvTime(new Date());
        }

        // 更新当前收货明细状态和PO明细状态，回填ASN明细的收货数，PO单的收货数。
        receiveEntity = banQinInboundOperationService.updateReceivingStatus(receiveEntity);
        if (!receiveEntity.getLotNum().equals(updatedInv.getLotNum())) {
            throw new WarehouseException("并发时，批次号异常：收货明细" + receiveEntity.getLotNum() + "库存" + updatedInv.getLotNum());
        }
        // 更新ASN单和PO单状态
        banQinInboundOperationService.updateAsnStatus(asnNo, receiveEntity.getOrgId());
        String poNo = receiveEntity.getPoNo();
        if (StringUtils.isNotEmpty(poNo)) {
            banQinInboundOperationService.updatePoStatus(poNo, receiveEntity.getOrgId());
        }

        // 部分收货：预收数>实收数，拆分收货明细。
        // 如果是收货前计算库位（两步收货），新的收货明细，上架库位指定规则取【收货时计算库位】，计划上架库位置空
        banQinWmAsnDetailReceiveService.splitAsnDetailReceive(receiveEntity, currentQtyRcvEa, qtyPlanEa);

        // 首次收货，更新商品上次盘点时间
        boolean isSave = false;
        if (null == skuModel.getFirstInTime()) {
            skuModel.setFirstInTime(receiveEntity.getRcvTime());
            isSave = true;
        }
        if (null == skuModel.getLastCountTime()) {
            skuModel.setLastCountTime(receiveEntity.getRcvTime());
            isSave = true;
        }
        if (isSave) {
            banQinCdWhSkuService.save(skuModel);
        }
        /*
         * 判断是否生成上架任务
         */
        // 上架库位指定规则为收货时计算库位 并且 收货库位是过渡库位
        // 收货前计算库位（一步收货）即使收到过渡库位，也不产生上架任务
        // 收货前计算库位（两步收货）并且 计划上架库位不为空（预约到库位），生成上架任务，推荐库位=计划上架库位
        if (WmsCodeMaster.RESERVE_RCV.getCode().equals(reserveCode) || (StringUtils.isNotEmpty(planPaLoc) && WmsCodeMaster.RESERVE_B_RCV_TWO.getCode().equals(reserveCode))) {
            // ASN单头
            BanQinWmTaskPaEntity taskPaEntity = new BanQinWmTaskPaEntity();
            taskPaEntity.setOrderNo(asnNo);
            taskPaEntity.setAsnType(asnType);
            taskPaEntity.setOrderType(WmsCodeMaster.ORDER_ASN.getCode());
            taskPaEntity.setOwnerCode(ownerCode);
            taskPaEntity.setSkuCode(skuCode);
            taskPaEntity.setLotNum(lotnum);
            taskPaEntity.setFromLoc(toLoc);
            taskPaEntity.setFromId(toId);
            taskPaEntity.setNewPaRule(paRule);
            taskPaEntity.setNewReserveCode(reserveCode);
            taskPaEntity.setPlanPaLoc(planPaLoc);
            taskPaEntity.setPackCode(packCode);
            taskPaEntity.setQtyPaEa(currentQtyRcvEa);
            taskPaEntity.setCubic(skuModel.getCubic());
            taskPaEntity.setGrossWeight(skuModel.getGrossWeight());
            taskPaEntity.setTraceId(receiveEntity.getId());
            taskPaEntity.setRealCaseId(receiveEntity.getLotAtt07());
            taskPaEntity.setOrgId(receiveEntity.getOrgId());
            taskPaEntity.setUom(receiveEntity.getUom());
            // 收货上架参数：生成上架任务是否进行单进程操作（Y：单进程；N：不是单进程）
            String RCV_PA_TASK_QUEUE = WmsConstants.NO;/*banQinWmsCommonService.getSysControlParam(ControlParamCode.RCV_PA_TASK_QUEUE.getCode());*/
            // 生成上架任务单进程控制
            if (WmsConstants.YES.equals(RCV_PA_TASK_QUEUE)) {
                synchronized (locker) {
                    msg = banQinInboundPaOperationService.inboundCreatePaTask(taskPaEntity);
                }
            } else {
                msg = banQinInboundPaOperationService.inboundCreatePaTask(taskPaEntity);
            }
            if (msg.isSuccess()) {
                BanQinWmTaskPaEntity param = (BanQinWmTaskPaEntity) msg.getData();
                String paId = param.getPaId();
                String traceId = param.getTraceId();
                // 跟踪号为“*”，每个收货明细生成一条上架任务
                if (WmsConstants.TRACE_ID.equals(receiveEntity.getToId())) {
                    receiveEntity.setPaId(paId);
                    receiveEntity = banQinInboundOperationService.saveAsnDetailReceiveEntity(receiveEntity);
                } else {// 更新相同跟踪号的收货明细行的PaID
//					List<WmAsnDetailReceiveModel> receiveList = wmAsnDetailReceiveManager.getByAsnNoAndTraceId(receiveEntity.getAsnNo(), receiveEntity.getToId(), WmsCodeMaster.ASN_FULL_RECEIVING
//							.getCode());
//					for (WmAsnDetailReceiveModel wmAsnDetailReceiveModel : receiveList) {
//						wmAsnDetailReceiveModel.setReserveCode(reserveCode);
//						wmAsnDetailReceiveModel.setPaRule(paRule);
//						wmAsnDetailReceiveModel.setPaId(paId);
//					}
//					wmAsnDetailReceiveManager.saveAll(receiveList);

                    BanQinWmAsnDetailReceive receiveModel = banQinWmAsnDetailReceiveService.get(traceId);
                    receiveModel.setReserveCode(reserveCode);
                    receiveModel.setPaRule(paRule);
                    receiveModel.setPaId(paId);
                    banQinWmAsnDetailReceiveService.save(receiveModel);
                }
            }
        }
        /*
         * 构造返回的参数
         */
        /*ReceivingReturnParam returnParam = new ReceivingReturnParam();
        returnParam.setWmAsnDetailReceiveEntity(receiveEntity);
        returnParam.setTranId(updatedInv.getTranId());
        msg.setData(returnParam);*/
        receiveEntity.setTransactionId(updatedInv.getTaskId());
        msg.setData(receiveEntity);
        msg.setSuccess(true);
        return msg;

    }

    /**
     * Description : 收货前校验
     *
     * @param receivingEntity
     * @throws WarehouseException
     * @Author: Ramona.Wang
     * @Create Date: 2014-6-18
     */
    protected BanQinWmAsnDetailReceiveEntity checkBeforeReceiving(BanQinWmAsnDetailReceiveEntity receivingEntity, BanQinCdWhSku skuModel) throws WarehouseException {
        /*
         * 校验数据是否过期
         */
        // 数据查询最新数据
        BanQinWmAsnDetailReceive querySku = banQinWmAsnDetailReceiveService.getByAsnNoAndLineNo(receivingEntity.getAsnNo(), receivingEntity.getLineNo(), receivingEntity.getOrgId());
        if (null == querySku) {
            throw new WarehouseException("不存在");// 不存在
        }
        // 如果传入的收货明细与数据库中的版本号小，则说明传入的明细已数据过期
        if (querySku.getRecVer() > receivingEntity.getRecVer()) {
            throw new WarehouseException("数据已过期");// 数据过期
        }
        /*
         * 收货明细状态的校验
         */
        if (WmsCodeMaster.ASN_FULL_RECEIVING.getCode().equals(querySku.getStatus())) {
            throw new WarehouseException("已收货，不能操作");// "已收货，不能操作"
        }
        if (WmsCodeMaster.ASN_CANCEL.getCode().equals(querySku.getStatus())) {
            throw new WarehouseException("已取消，不能操作");// "已取消，不能操作"
        }
        if (StringUtils.isEmpty(receivingEntity.getToLoc())) {
            throw new WarehouseException("收货库位不能为空");// 收货库位不能为空
        }
        // 如果是收货前计算库位，计划上架库位不能为空
        if (WmsCodeMaster.RESERVE_B_RCV_ONE.getCode().equals(receivingEntity.getReserveCode()) || WmsCodeMaster.RESERVE_B_RCV_TWO.getCode().equals(receivingEntity.getReserveCode())) {
            if (StringUtils.isEmpty(querySku.getPlanPaLoc())) {
                throw new WarehouseException("计划上架库位不能为空，请先安排库位");// "计划上架库位不能为空，请先安排库位"
            } else {
                receivingEntity.setPlanPaLoc(querySku.getPlanPaLoc());
            }
        } else if (StringUtils.isNotEmpty(querySku.getPlanPaLoc())) {
            throw new WarehouseException("已安排库位，上架库位指定规则需为收货前计算库位");// "已安排库位，上架库位指定规则需为收货前计算库位"
        }
        // 如果商品需要序列号管理，需到入库序列扫描收货
        String isSerial = skuModel.getIsSerial();
        if (WmsConstants.YES.equals(isSerial) && !ActionCode.SCAN_RECEIVING.getCode().equals(receivingEntity.getActionCode().getCode())) {
            throw new WarehouseException(skuModel.getSkuCode() + "商品需进行序列号扫描收货");// "商品需进行序列号扫描收货"
        }

        // 如果是收货后上架前质检，要求先收到过渡库位，批次04为待检。等待生成质检，进行质检确认后，可在质检单中生成上架任务。
        if (WmsCodeMaster.QC_PHASE_B_PA.getCode().equals(querySku.getQcPhase())) {
            BanQinCdWhLoc locModel = banQinCdWhLocService.findByLocCode(receivingEntity.getToLoc(), receivingEntity.getOrgId());
            if (!WmsCodeMaster.LOC_USE_ST.getCode().equals(locModel.getLocUseType())) {
                throw new WarehouseException("收货后上架前质检，收货库位需为过渡库位");// "收货后上架前质检，收货库位需为过渡库位"
            }
        }

        /*
         * 订单状态的校验
         */
        ResultMessage msg = banQinInboundOperationService.checkAsnIsOperateStatus(receivingEntity.getHeadId());
        if (!msg.isSuccess()) {
            throw new WarehouseException(msg.getMessage());
        }
        BanQinWmAsnHeader wmAsnHeaderModel = (BanQinWmAsnHeader) msg.getData();
        // 订单类型
        receivingEntity.setOrderType(wmAsnHeaderModel.getAsnType());

        /*
         * ASN SKU明细的校验
         */
        BanQinWmAsnDetail wmAsnDetailModel = banQinWmAsnDetailService.getByAsnNoAndLineNo(receivingEntity.getAsnNo(), receivingEntity.getAsnLineNo(), receivingEntity.getOrgId());
        /*
         * 如果ASN明细已码盘，跟踪号不能为"*"
         */
        String toId = StringUtils.isEmpty(receivingEntity.getToId()) ? WmsConstants.TRACE_ID : receivingEntity.getToId();
        if (WmsConstants.YES.equals(wmAsnDetailModel.getIsPalletize()) && WmsConstants.TRACE_ID.equals(toId)) {
            throw new WarehouseException("已码盘，跟踪号不能为*");// 已码盘，跟踪号不能为*
        }
        // 如果商品需要质检，收货时批次属性04需为“待检”；
        String lotAtt04 = receivingEntity.getLotAtt04();
        String isQc = receivingEntity.getIsQc();
        String qcStatus = querySku.getQcStatus();
        String skuIsQc = skuModel.getIsQc();
        if (WmsConstants.YES.equals(isQc)) {

            // 校验质检阶段为收货前质检时，上架库位指定规则为：人工指定、收货时计算、上架时计算；
            // 质检阶段为收货后上架前质检时，上架库位指定规则为：人工指定、上架时计算
            if (WmsCodeMaster.QC_PHASE_B_PA.getCode().equals(receivingEntity.getQcPhase())) {
                if (!(WmsCodeMaster.RESERVE_MAN.getCode().equals(receivingEntity.getReserveCode()) || WmsCodeMaster.RESERVE_PA.getCode().equals(receivingEntity.getReserveCode()))) {
                    throw new WarehouseException("收货后上架前质检，上架库位指定规则需为人工指定库位或上架时计算库位");
                    // "收货后上架前质检，上架库位指定规则需为人工指定库位或上架时计算库位");
                }
            } else {
                if (WmsCodeMaster.RESERVE_B_RCV_ONE.getCode().equals(receivingEntity.getReserveCode()) || WmsCodeMaster.RESERVE_B_RCV_TWO.getCode().equals(receivingEntity.getReserveCode())) {
                    throw new WarehouseException("收货前质检，上架库位指定规则不能为收货前计算库位");
                    // "收货前质检，上架库位指定规则不能为收货前计算库位");
                }
            }
            if (WmsCodeMaster.QC_PHASE_B_PA.getCode().equals(receivingEntity.getQcPhase())) {
                if (StringUtils.isEmpty(lotAtt04)) {
                    receivingEntity.setLotAtt04(WmsCodeMaster.QC_ATT_TO_QC.getCode());
                } else if (!WmsCodeMaster.QC_ATT_TO_QC.getCode().equals(lotAtt04)) {
                    throw new WarehouseException("收货后上架前质检，批次属性04需为待检");// "收货后上架前质检，批次属性04需为待检");
                }
            } else {
                // 收货前质检，生成质检单后，未完成质检，不能收货。
                if (null == qcStatus || qcStatus.equals(WmsCodeMaster.QC_NEW.getCode())) {
                    throw new WarehouseException("商品需要质检，不能操作"); // "商品需要质检，不能操作");
                }
                if (StringUtils.isEmpty(lotAtt04)) {
                    throw new WarehouseException("商品需要质检，批次属性04不能为空");// "商品需要质检，批次属性04不能为空");
                } else if (WmsCodeMaster.QC_ATT_TO_QC.getCode().equals(lotAtt04)) {
                    throw new WarehouseException("批次属性04不能为待检");// "批次属性04不能为待检");
                }
            }
        } else {
            if (WmsCodeMaster.QC_ATT_TO_QC.getCode().equals(lotAtt04)) {
                throw new WarehouseException("批次属性04不能为待检");// "批次属性04不能为待检");
            }
        }
        if (WmsConstants.YES.equals(skuIsQc) && StringUtils.isEmpty(receivingEntity.getLotAtt04())) {
            throw new WarehouseException("商品需要质检，批次属性04不能为空");// "商品需要质检，批次属性04不能为空");
        }
        /*
         * 校验是否允许超收
         */

        // 是否允许超收
        String isOverRcv = skuModel.getIsOverRcv();
        // 已收货
        Double receivedQtyEaAsn = wmAsnDetailModel.getQtyRcvEa();
        // 订货数
        Double expectedQtyEaAsn = wmAsnDetailModel.getQtyAsnEa();
        // 当前收货数
        Double rcvEa = receivingEntity.getCurrentQtyRcvEa() == null ? 0D : receivingEntity.getCurrentQtyRcvEa();
        // 超收比例
        Double overRcvQty = skuModel.getOverRcvPct() == null ? 0D : skuModel.getOverRcvPct();
        // 收货数不能为0或负数
        if (rcvEa <= 0D) {
            throw new WarehouseException("收货数必须大于零");// 收货数必须大于零
        }
        if (StringUtils.isNotEmpty(isOverRcv)) {
            // 不允许超收，校验是否超量, 通过已收货数量+当前收货数量 与 预收货数 比较
            if (isOverRcv.equals(WmsConstants.NO) && (receivedQtyEaAsn + rcvEa > expectedQtyEaAsn)) {
                throw new WarehouseException("不允许超收");// 不允许超收
            }
            // 允许超收且有超收限制，则不能超过超收数量：（已收货数量+当前收货数量-预收货数）/预收货数> 超收比例
            if (isOverRcv.equals(WmsConstants.YES)) {
                if (overRcvQty != 0D && (((receivedQtyEaAsn + rcvEa - expectedQtyEaAsn) / expectedQtyEaAsn) * 100 > overRcvQty)) {
                    throw new WarehouseException("收货数超过超收比例");// 收货数超过超收比例
                }

                // 存在采购订单,对采购订单也进行超收校验
                if (StringUtils.isNotEmpty(wmAsnDetailModel.getPoNo()) && StringUtils.isNotEmpty(wmAsnDetailModel.getPoLineNo())) {
                    BanQinWmPoDetail wmPoDetailModel = banQinWmPoDetailService.findByPoNoAndLineNo(wmAsnDetailModel.getPoNo(), wmAsnDetailModel.getPoLineNo(), wmAsnDetailModel.getOrgId());
                    if (null == wmPoDetailModel) {
                        throw new WarehouseException(wmAsnDetailModel.getPoNo() + "[" + wmAsnDetailModel.getPoLineNo() + "]不存在");
                    }
                    // 已收货数
                    Double receivedQtyEaPo = wmPoDetailModel.getQtyRcvEa();
                    // 订货数
                    Double expectedQtyEaPo = wmPoDetailModel.getQtyPoEa();
                    // 是否超收
                    if ((receivedQtyEaPo + rcvEa) > expectedQtyEaPo) {
                        throw new WarehouseException("收货数不能超过采购数");// 收货数不能超过采购数
                    }
                }
            }
        }

        /*
         * 生产日期、失效日期的逻辑校验
         */
        // 生产日期
        Date lotAtt01 = receivingEntity.getLotAtt01();
        // 失效日期
        Date lotAtt02 = receivingEntity.getLotAtt02();
        // 保质期
        Double shelfLife = null != skuModel.getShelfLife() ? skuModel.getShelfLife() : 0D;
        // 入库日期
        Date lotAtt03 = receivingEntity.getLotAtt03();
        // 校验日期正确性
        this.banQinWmsCommonService.checkProductAndExpiryDate(lotAtt01, lotAtt02, lotAtt03);
        // 收货时是否根据生产日期、保质期自动计算失效日期
        /*String ASN_CALC_EXP_DATE = banQinWmsCommonService.getSysControlParam(ControlParamCode.ASN_CALC_EXP_DATE.getCode());
        if (WmsConstants.YES.equals(ASN_CALC_EXP_DATE) && (null != lotAtt01 && null != shelfLife && null == lotAtt02 && shelfLife != 0D)) {*/
        if (null != lotAtt01 && null != shelfLife && shelfLife != 0D && lotAtt02 == null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                lotAtt02 = sdf.parse(sdf.format(banQinWmsCommonService.getNextDay(lotAtt01, shelfLife.intValue())));
                receivingEntity.setLotAtt02(lotAtt02);
            } catch (ParseException e) {
                throw new WarehouseException(e.getMessage());
            }

        }

        /*
         * 超过商品有效期控制天数,拒收
         */
        String isValidity = skuModel.getIsValidity();
        // 入库效期
        Double inLifeDays = skuModel.getInLifeDays();
        // 周期类型（生产日期、失效日期）
        String lifeType = skuModel.getLifeType();
        if (StringUtils.isNotEmpty(isValidity) && WmsConstants.YES.equals(isValidity) && inLifeDays != null && inLifeDays != 0D) {
            // 失效日期与入库效期的校验
            if (null != lotAtt02 && WmsCodeMaster.LIFE_TYPE_E.getCode().equals(lifeType)) {
                // 失效日期-当前日期<入库效期 ，则拒收
                if (banQinWmsCommonService.dateDiff(new Date(), lotAtt02).doubleValue() < inLifeDays) {
                    throw new WarehouseException("超过入库效期，不允许收货");// 超过入库效期，不允许收货
                }
            }
            // 生产日期与入库效期的校验
            if (null != lotAtt01 && WmsCodeMaster.LIFE_TYPE_P.getCode().equals(lifeType)) {
                // 当前日期-生产日期>入库效期 ，则拒收
                if (banQinWmsCommonService.dateDiff(lotAtt01, new Date()).doubleValue() > inLifeDays) {
                    throw new WarehouseException("超过入库效期，不允许收货");// 超过入库效期，不允许收货
                }
            }
        }
        return receivingEntity;
    }

    /**
     * Description : 校验同一个traceID，是否允许重复收货 收货时，traceId不允许混SKU，混lotnum，混ASN存在。
     *
     * @param asnNo
     * @param traceId
     * @param skuCode
     * @param lotnum
     * @throws WarehouseException
     * @Author: Ramona.Wang
     * @Create Date: 2014-6-18
     */
    protected void checkTraceId(String asnNo, String traceId, String skuCode, String lotnum, String orgId) throws WarehouseException {
        if (!WmsConstants.TRACE_ID.equals(traceId)) {

            List<BanQinWmInvLotLoc> wmInvLotLocList = banQinWmInvLotLocService.getByTraceId(traceId, orgId);
            if (null != wmInvLotLocList && wmInvLotLocList.size() > 0) {
                // 跟踪号是否混放，以库位的混放属性为准。故在调用库存方法中校验。
                // for (WmInvLotLocModel wmInvLotLocModel : wmInvLotLocList) {
                // if(!wmInvLotLocModel.getSkuCode().equals(skuCode)||!wmInvLotLocModel.getLotNum().equals(lotnum)){
                // throw new
                // WarehouseException(MsgUtils.getMessage("msg.wms.info.traceMixSkuLotPrm",
                // traceId));//跟踪号{0}不能混商品、混批次
                // }
                // }
                // traceId存在库存，校验当前ASN单的收货明细中，是否存在同一个traceID的已收货明细，如果不存在，该库存可能来自其他ASN，不收货。
                List<BanQinWmAsnDetailReceive> wmAsnDetailReceiveList = banQinWmAsnDetailReceiveService.getByAsnNoAndTraceId(asnNo, traceId, WmsCodeMaster.ASN_FULL_RECEIVING.getCode(), orgId);
                if (null == wmAsnDetailReceiveList || wmAsnDetailReceiveList.size() <= 0) {
                    throw new WarehouseException("跟踪号" + traceId + "已被占用");// 跟踪号{0}已被占用
                }
            }

            // 如果traceId已经生成上架任务，不能重复收
            BanQinWmTaskPa con = new BanQinWmTaskPa();
            con.setOrderNo(asnNo);
            con.setFmId(traceId);
            con.setOrgId(orgId);
            List<BanQinWmTaskPa> list = banQinWmTaskPaService.findList(con);
            if (null != list && list.size() > 0) {
                throw new WarehouseException("跟踪号" + traceId + "已经生成上架任务，不能操作");// 跟踪号{0}已经生成上架任务，不能操作
            }
        }
    }

}
