package com.yunyou.modules.wms.inbound.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.modules.sys.utils.SysControlParamsUtils;
import com.yunyou.modules.sys.utils.UserUtils;
import com.yunyou.modules.wms.basicdata.entity.*;
import com.yunyou.modules.wms.basicdata.mapper.BanQinCdWhLocMapper;
import com.yunyou.modules.wms.basicdata.service.*;
import com.yunyou.modules.wms.common.entity.*;
import com.yunyou.modules.wms.common.service.BanQinWmsCommonService;
import com.yunyou.modules.wms.inbound.entity.BanQinWmAsnDetailReceive;
import com.yunyou.modules.wms.inbound.entity.BanQinWmAsnDetailReceiveEntity;
import com.yunyou.modules.wms.inbound.entity.BanQinWmAsnHeader;
import com.yunyou.modules.wms.inventory.entity.BanQinWmInvLotAtt;
import com.yunyou.modules.wms.inventory.entity.BanQinWmInvLotLoc;
import com.yunyou.modules.wms.inventory.service.BanQinInventoryService;
import com.yunyou.modules.wms.inventory.service.BanQinWmInvLotAttService;
import com.yunyou.modules.wms.inventory.service.BanQinWmInvLotLocService;
import com.yunyou.modules.wms.qc.entity.*;
import com.yunyou.modules.wms.qc.service.BanQinWmQcDetailService;
import com.yunyou.modules.wms.qc.service.BanQinWmQcHeaderService;
import com.yunyou.modules.wms.qc.service.BanQinWmQcSkuService;
import com.yunyou.modules.wms.task.entity.BanQinWmTaskPa;
import com.yunyou.modules.wms.task.entity.BanQinWmTaskPaEntity;
import com.yunyou.modules.wms.task.service.BanQinWmTaskPaService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 描述：入库上架操作Service
 */
@Service
public class BanQinInboundPaOperationService {
    @Autowired
    private BanQinCdRulePaHeaderService banQinCdRulePaHeaderService;
    @Autowired
    private BanQinCdRulePaDetailService banQinCdRulePaDetailService;
    @Autowired
    private BanQinWmInvLotLocService banQinWmInvLotLocService;
    @Autowired
    private BanQinWmInvLotAttService banQinWmInvLotAttService;
    @Autowired
    private BanQinCdWhLocService banQinCdWhLocService;
    @Autowired
    private BanQinCdWhLocMapper banQinCdWhLocMapper;
    @Autowired
    private BanQinCdWhSkuService banQinCdWhSkuService;
    @Autowired
    private BanQinCdWhSkuLocService banQinCdWhSkuLocService;
    @Autowired
    private BanQinCdWhPackageService banQinCdWhPackageService;
    @Autowired
    private BanQinInventoryService banQinInventoryService;
    @Autowired
    private BanQinWmsCommonService banQinWmsCommonService;
    @Autowired
    private BanQinWmTaskPaService banQinWmTaskPaService;
    @Autowired
    private BanQinInboundOperationService banQinInboundOperationService;
    @Autowired
    private BanQinWmAsnHeaderService banQinWmAsnHeaderService;
    @Autowired
    private BanQinWmQcHeaderService banQinWmQcHeaderService;
    @Autowired
    private BanQinWmQcSkuService banQinWmQcSkuService;
    @Autowired
    private BanQinWmQcDetailService banQinWmQcDetailService;
    @Autowired
    private BanQinWmAsnDetailReceiveService banQinWmAsnDetailReceiveService;

    public Object locker = new Object();

    /**
     * 描述： 批量生成上架任务：只对于收货后的明细，配置为上架时计算库位，才能生成上架任务
     */
    public ResultMessage inboundBatchCreateTaskPa(List<BanQinWmAsnDetailReceiveEntity> wmAsnDetailReceiveEntities) {
        ResultMessage msg = new ResultMessage();
        msg.setSuccess(false);
        // key值为跟踪号
        Map<String, List<BanQinWmAsnDetailReceiveEntity>> map = Maps.newHashMap();
        List<BanQinWmAsnDetailReceiveEntity> receiveGroup;

        // 收货上架参数：生成上架任务是否进行单进程操作（Y：单进程；N：不是单进程）
        String RCV_PA_TASK_QUEUE = SysControlParamsUtils.getValue(ControlParamCode.RCV_PA_TASK_QUEUE.getCode());
        for (BanQinWmAsnDetailReceiveEntity wmAsnDetailReceiveEntity : wmAsnDetailReceiveEntities) {
            BanQinWmAsnDetailReceiveEntity checkEntity = banQinWmAsnDetailReceiveService.getEntityByAsnNoAndLineNo(wmAsnDetailReceiveEntity.getAsnNo(), wmAsnDetailReceiveEntity.getLineNo(), wmAsnDetailReceiveEntity.getOrgId());
            if (!WmsCodeMaster.ASN_FULL_RECEIVING.getCode().equals(checkEntity.getStatus())) {
                msg.addMessage("单据号" + checkEntity.getAsnNo() + "行号" + checkEntity.getLineNo() + "未收货，不能操作");
                continue;
            }
            // 质检校验
            if ((StringUtils.isNotEmpty(wmAsnDetailReceiveEntity.getQcStatus()) && WmsCodeMaster.QC_PHASE_B_PA.getCode().equals(wmAsnDetailReceiveEntity.getQcPhase()))
                    || WmsCodeMaster.QC_NEW.getCode().equals(wmAsnDetailReceiveEntity.getQcStatus())) {
                msg.addMessage("单据号" + wmAsnDetailReceiveEntity.getAsnNo() + "行号" + wmAsnDetailReceiveEntity.getLineNo() + "已生成质检单，不能操作");
                continue;
            } else if (StringUtils.isEmpty(wmAsnDetailReceiveEntity.getQcStatus()) && WmsConstants.YES.equals(wmAsnDetailReceiveEntity.getIsQc())) {
                msg.addMessage("单据号" + wmAsnDetailReceiveEntity.getAsnNo() + "行号" + wmAsnDetailReceiveEntity.getLineNo() + "商品需要质检，不能操作");
                continue;
            }
            // 如果不是上架时计算库位，不能生成上架任务
            if (!WmsCodeMaster.RESERVE_PA.getCode().equals(wmAsnDetailReceiveEntity.getReserveCode())) {
                msg.addMessage("单据号" + wmAsnDetailReceiveEntity.getAsnNo() + "行号" + wmAsnDetailReceiveEntity.getLineNo() + "上架库位指定规则需为上架时计算库位");
                continue;
            }
            // ASN单头
            BanQinWmAsnHeader wmAsnHeaderModel = banQinWmAsnHeaderService.getByAsnNo(wmAsnDetailReceiveEntity.getAsnNo(), wmAsnDetailReceiveEntity.getOrgId());
            wmAsnDetailReceiveEntity.setOrderType(wmAsnHeaderModel.getAsnType());
            // 1.忽略跟踪号，一个收货明细生成一个上架任务
            if (WmsConstants.TRACE_ID.equals(wmAsnDetailReceiveEntity.getToId())) {
                BanQinWmTaskPaEntity entity = new BanQinWmTaskPaEntity();
                entity.setOrderNo(wmAsnDetailReceiveEntity.getAsnNo());
                entity.setAsnType(wmAsnDetailReceiveEntity.getOrderType());
                entity.setOrderType(WmsCodeMaster.ORDER_ASN.getCode());
                entity.setOwnerCode(wmAsnDetailReceiveEntity.getOwnerCode());
                entity.setSkuCode(wmAsnDetailReceiveEntity.getSkuCode());
                entity.setPackCode(wmAsnDetailReceiveEntity.getPackCode());
                entity.setLotNum(wmAsnDetailReceiveEntity.getLotNum());
                entity.setFromId(wmAsnDetailReceiveEntity.getToId());
                entity.setFromLoc(wmAsnDetailReceiveEntity.getToLoc());
                entity.setNewPaRule(wmAsnDetailReceiveEntity.getPaRule());
                entity.setNewReserveCode(wmAsnDetailReceiveEntity.getReserveCode());
                entity.setPaIdRcv(wmAsnDetailReceiveEntity.getPaId());
                entity.setCubic(wmAsnDetailReceiveEntity.getCubic());
                entity.setGrossWeight(wmAsnDetailReceiveEntity.getGrossWeight());
                entity.setOrgId(wmAsnDetailReceiveEntity.getOrgId());
                entity.setUom(wmAsnDetailReceiveEntity.getUom());
                if (StringUtils.isNotEmpty(wmAsnDetailReceiveEntity.getPaId())) {
                    // 已生成上架数
                    Double qtyPa = banQinWmTaskPaService.getQtyPaByPaId(wmAsnDetailReceiveEntity.getPaId(), wmAsnDetailReceiveEntity.getOrgId());
                    entity.setQtyPaEa(wmAsnDetailReceiveEntity.getQtyRcvEa() - qtyPa);
                } else {
                    entity.setQtyPaEa(wmAsnDetailReceiveEntity.getQtyRcvEa());
                }
                if (entity.getQtyPaEa() > 0D) {
                    try {
                        // 生成上架任务单进程控制
                        if (WmsConstants.YES.equals(RCV_PA_TASK_QUEUE)) {
                            synchronized (locker) {
                                msg = this.inboundCreatePaTask(entity);
                            }
                        } else {
                            msg = this.inboundCreatePaTask(entity);
                        }
                        if (msg.isSuccess()) {
                            BanQinWmTaskPaEntity param = (BanQinWmTaskPaEntity) msg.getData();
                            String paId = param.getPaId();
                            wmAsnDetailReceiveEntity.setPaId(paId);
                            banQinInboundOperationService.saveAsnDetailReceiveEntity(wmAsnDetailReceiveEntity);
                        }
                    } catch (WarehouseException e) {
                        msg.addMessage("单据号" + wmAsnDetailReceiveEntity.getAsnNo() + "行号" + wmAsnDetailReceiveEntity.getLineNo() + e.getMessage());
                    }
                }
            } else {
                // 2.如果存在跟踪号，则分组汇总
                String key = wmAsnDetailReceiveEntity.getToId();
                receiveGroup = !map.containsKey(key) ? Lists.newArrayList() : map.get(key);
                receiveGroup.add(wmAsnDetailReceiveEntity);
                map.put(key, receiveGroup);
            }
        }

        // 存在跟踪号时，分组构造生成上架任务的参数
        Set<String> keySet = map.keySet();
        for (String key : keySet) {
            // 同个traceId在不同收货明细行，如果收货信息有不同，则只取第一条
            BanQinWmAsnDetailReceiveEntity receiveEntity = map.get(key).get(0);
            BanQinWmTaskPaEntity entity = new BanQinWmTaskPaEntity();
            entity.setOrderNo(receiveEntity.getAsnNo());
            entity.setAsnType(receiveEntity.getOrderType());
            entity.setOrderType(WmsCodeMaster.ORDER_ASN.getCode());
            entity.setOwnerCode(receiveEntity.getOwnerCode());
            entity.setSkuCode(receiveEntity.getSkuCode());
            entity.setPackCode(receiveEntity.getPackCode());
            entity.setLotNum(receiveEntity.getLotNum());
            entity.setFromId(receiveEntity.getToId());
            entity.setFromLoc(receiveEntity.getToLoc());
            entity.setNewPaRule(receiveEntity.getPaRule());
            entity.setPaIdRcv(receiveEntity.getPaId());
            entity.setNewReserveCode(receiveEntity.getReserveCode());
            entity.setCubic(receiveEntity.getCubic());
            entity.setGrossWeight(receiveEntity.getGrossWeight());
            entity.setOrgId(receiveEntity.getOrgId());
            entity.setUom(receiveEntity.getUom());
            try {
                // 生成上架任务单进程控制
                if (WmsConstants.YES.equals(RCV_PA_TASK_QUEUE)) {
                    synchronized (locker) {
                        msg = this.inboundCreatePaTask(entity);
                    }
                } else {
                    msg = this.inboundCreatePaTask(entity);
                }
                if (msg.isSuccess()) {
                    BanQinWmTaskPaEntity param = (BanQinWmTaskPaEntity) msg.getData();
                    String paId = param.getPaId();
                    // 更新相同跟踪号的收货明细行的PaID
                    List<BanQinWmAsnDetailReceive> receiveList = banQinWmAsnDetailReceiveService.getByAsnNoAndTraceId(receiveEntity.getAsnNo(), key, WmsCodeMaster.ASN_FULL_RECEIVING.getCode(), receiveEntity.getOrgId());
                    for (BanQinWmAsnDetailReceive wmAsnDetailReceiveModel : receiveList) {
                        wmAsnDetailReceiveModel.setReserveCode(entity.getNewReserveCode());
                        wmAsnDetailReceiveModel.setPaRule(entity.getNewPaRule());
                        wmAsnDetailReceiveModel.setPaId(paId);
                        banQinWmAsnDetailReceiveService.save(wmAsnDetailReceiveModel);
                    }
                }
            } catch (WarehouseException e) {
                msg.addMessage("单据号" + receiveEntity.getAsnNo() + "行号" + receiveEntity.getLineNo() + e.getMessage());
            }
        }
        if (StringUtils.isEmpty(msg.getMessage())) {
            msg.setSuccess(true);
            msg.addMessage("操作成功");
        }
        return msg;
    }

    /**
     * 描述： 批量生成上架任务（质检单）
     */
    public ResultMessage inboundBatchCreateTaskPaFmQc(List<BanQinWmQcSkuEntity> wmQcSkuEntities) {
        ResultMessage msg = new ResultMessage();
        msg.setSuccess(false);

        BanQinWmQcSkuEntity skuEntity = wmQcSkuEntities.get(0);
        BanQinWmQcEntity wmQcEntity = banQinWmQcHeaderService.findEntityByQcNo(skuEntity.getQcNo(), skuEntity.getOrgId());
        if (!WmsCodeMaster.QC_PHASE_B_PA.getCode().equals(wmQcEntity.getQcPhase())) {
            msg.addMessage("质检阶段需为收货后上架前质检");
            return msg;
        }

        // 收货上架参数：生成上架任务是否进行单进程操作（Y：单进程；N：不是单进程）
        final String RCV_PA_TASK_QUEUE = SysControlParamsUtils.getValue(ControlParamCode.RCV_PA_TASK_QUEUE.getCode());
        for (BanQinWmQcSkuEntity wmQcSkuEntity : wmQcSkuEntities) {
            String paRule = wmQcSkuEntity.getPaRule();
            BanQinWmQcSku wmQcSkuModel = banQinWmQcSkuService.findByQcNoAndLineNo(wmQcSkuEntity.getQcNo(), wmQcSkuEntity.getLineNo(), wmQcSkuEntity.getOrgId());
            if (StringUtils.isEmpty(wmQcSkuModel.getPaRule()) && StringUtils.isEmpty(paRule)) {
                msg.addMessage("行号" + wmQcSkuEntity.getLineNo() + "上架规则不能为空");
                continue;
            }
            if (!WmsCodeMaster.QC_FULL_QC.getCode().equals(wmQcSkuModel.getStatus())) {
                msg.addMessage("行号" + wmQcSkuEntity.getLineNo() + "不是完全质检，不能操作");
                continue;
            }
            wmQcSkuModel.setPaRule(paRule);
            banQinWmQcSkuService.saveQcSkuModel(wmQcSkuModel);
            // key值为跟踪号
            Map<String, List<BanQinWmQcDetailEntity>> quaMap = Maps.newHashMap();
            Map<String, List<BanQinWmQcDetailEntity>> unQuaMap = Maps.newHashMap();
            List<BanQinWmQcDetailEntity> quaGroup;
            List<BanQinWmQcDetailEntity> unquaGroup;
            List<BanQinWmQcDetailEntity> wmQcDetailEntities = banQinWmQcDetailService.findEntityByQcNoAndQcLineNo(wmQcSkuEntity.getQcNo(), wmQcSkuEntity.getLineNo(), wmQcSkuEntity.getOrgId());
            for (BanQinWmQcDetailEntity wmQcDetailEntity : wmQcDetailEntities) {
                // 良品，不良品生成两个上架任务。
                Double qtyQuaEa = wmQcDetailEntity.getQtyQuaEa() == null ? 0D : wmQcDetailEntity.getQtyQuaEa();
                Double qtyUnquaEa = wmQcDetailEntity.getQtyUnquaEa() == null ? 0D : wmQcDetailEntity.getQtyUnquaEa();
                if (qtyQuaEa > 0D) {
                    // 1.忽略跟踪号，一个质检明细生成一个上架任务
                    if (WmsConstants.TRACE_ID.equals(wmQcDetailEntity.getQuaTraceId())) {
                        BanQinWmTaskPaEntity entity = createQuaTaskPaEntity(wmQcDetailEntity, paRule);
                        if (StringUtils.isNotEmpty(wmQcDetailEntity.getQuaPaId())) {
                            // 已生成上架数
                            Double qtyPa = banQinWmTaskPaService.getQtyPaByPaId(wmQcDetailEntity.getQuaPaId(), wmQcDetailEntity.getOrgId());
                            entity.setQtyPaEa(qtyQuaEa - qtyPa);
                        } else {
                            entity.setQtyPaEa(qtyQuaEa);
                        }
                        if (entity.getQtyPaEa() > 0D) {
                            try {
                                // 生成上架任务单进程控制
                                if (WmsConstants.YES.equals(RCV_PA_TASK_QUEUE)) {
                                    synchronized (locker) {
                                        msg = this.inboundCreatePaTask(entity);
                                    }
                                } else {
                                    msg = this.inboundCreatePaTask(entity);
                                }
                                if (msg.isSuccess()) {
                                    BanQinWmTaskPaEntity param = (BanQinWmTaskPaEntity) msg.getData();
                                    String paId = param.getPaId();
                                    wmQcDetailEntity.setQuaPaId(paId);
                                    wmQcDetailEntity = banQinWmQcDetailService.saveQcDetailEntity(wmQcDetailEntity);
                                }
                            } catch (WarehouseException e) {
                                msg.addMessage("质检明细行号" + wmQcDetailEntity.getLineNo() + e.getMessage());
                            }
                        }
                    } else {
                        // 2.如果存在跟踪号，则分组汇总
                        String key = wmQcDetailEntity.getQuaTraceId();
                        if (!quaMap.containsKey(key)) {
                            quaGroup = Lists.newArrayList();
                        } else {
                            quaGroup = quaMap.get(key);
                        }
                        quaGroup.add(wmQcDetailEntity);
                        quaMap.put(key, quaGroup);
                    }
                }
                if (qtyUnquaEa > 0D) {
                    // 1.忽略跟踪号，一个质检明细生成一个上架任务
                    if (WmsConstants.TRACE_ID.equals(wmQcDetailEntity.getUnquaTraceId())) {
                        BanQinWmTaskPaEntity entity = createUnQuaTaskPaEntity(wmQcDetailEntity, paRule);
                        if (StringUtils.isNotEmpty(wmQcDetailEntity.getUnquaPaId())) {
                            // 已生成上架数
                            Double qtyPa = banQinWmTaskPaService.getQtyPaByPaId(wmQcDetailEntity.getUnquaPaId(), wmQcDetailEntity.getOrgId());
                            entity.setQtyPaEa(qtyUnquaEa - qtyPa);
                        } else {
                            entity.setQtyPaEa(qtyUnquaEa);
                        }
                        if (entity.getQtyPaEa() > 0D) {
                            try {
                                // 生成上架任务单进程控制
                                if (WmsConstants.YES.equals(RCV_PA_TASK_QUEUE)) {
                                    synchronized (locker) {
                                        msg = this.inboundCreatePaTask(entity);
                                    }
                                } else {
                                    msg = this.inboundCreatePaTask(entity);
                                }
                                if (msg.isSuccess()) {
                                    BanQinWmTaskPaEntity param = (BanQinWmTaskPaEntity) msg.getData();
                                    String unquaPaId = param.getPaId();
                                    wmQcDetailEntity.setUnquaPaId(unquaPaId);
                                    wmQcDetailEntity = banQinWmQcDetailService.saveQcDetailEntity(wmQcDetailEntity);
                                }
                            } catch (WarehouseException e) {
                                msg.addMessage("质检明细行号" + wmQcDetailEntity.getLineNo() + e.getMessage());
                            }
                        }
                    } else {
                        // 2.如果存在跟踪号，则分组汇总
                        String key = wmQcDetailEntity.getUnquaTraceId();
                        if (!unQuaMap.containsKey(key)) {
                            unquaGroup = Lists.newArrayList();
                        } else {
                            unquaGroup = unQuaMap.get(key);
                        }
                        unquaGroup.add(wmQcDetailEntity);
                        unQuaMap.put(key, unquaGroup);
                    }
                }
            }
            // 质检合格：存在跟踪号时，分组构造生成上架任务的参数
            Set<String> keySet = quaMap.keySet();
            for (String key : keySet) {
                // 同个traceId在不同明细行，如果信息有不同，则只取第一条
                BanQinWmQcDetailEntity detailEntity = quaMap.get(key).get(0);
                BanQinWmTaskPaEntity entity = createQuaTaskPaEntity(detailEntity, paRule);
                try {
                    // 生成上架任务单进程控制
                    if (WmsConstants.YES.equals(RCV_PA_TASK_QUEUE)) {
                        synchronized (locker) {
                            msg = this.inboundCreatePaTask(entity);
                        }
                    } else {
                        msg = this.inboundCreatePaTask(entity);
                    }
                    if (msg.isSuccess()) {
                        BanQinWmTaskPaEntity param = (BanQinWmTaskPaEntity) msg.getData();
                        String quaPaId = param.getPaId();
                        // 更新相同跟踪号的明细行的PaID
                        BanQinWmQcDetail example = new BanQinWmQcDetail();
                        example.setQcNo(detailEntity.getQcNo());
                        example.setQuaTraceId(key);
                        example.setStatus(WmsCodeMaster.QC_FULL_QC.getCode());
                        List<BanQinWmQcDetail> queryList = banQinWmQcDetailService.findList(example);
                        for (BanQinWmQcDetail wmQcDetailModel : queryList) {
                            wmQcDetailModel.setQuaPaId(quaPaId);
                            banQinWmQcDetailService.save(wmQcDetailModel);
                        }
                    }
                } catch (WarehouseException e) {
                    msg.addMessage("质检明细行号" + detailEntity.getLineNo() + e.getMessage());
                }
            }
            // 质检不合格：存在跟踪号时，分组构造生成上架任务的参数
            Set<String> unkeySet = unQuaMap.keySet();
            for (String key : unkeySet) {
                // 同个traceId在不同明细行，如果信息有不同，则只取第一条
                BanQinWmQcDetailEntity detailEntity = unQuaMap.get(key).get(0);
                BanQinWmTaskPaEntity entity = createUnQuaTaskPaEntity(detailEntity, paRule);
                try {
                    // 生成上架任务单进程控制
                    if (WmsConstants.YES.equals(RCV_PA_TASK_QUEUE)) {
                        synchronized (locker) {
                            msg = this.inboundCreatePaTask(entity);
                        }
                    } else {
                        msg = this.inboundCreatePaTask(entity);
                    }
                    if (msg.isSuccess()) {
                        BanQinWmTaskPaEntity param = (BanQinWmTaskPaEntity) msg.getData();
                        String unquaPaId = param.getPaId();
                        // 更新相同跟踪号的明细行的PaID
                        BanQinWmQcDetail example = new BanQinWmQcDetail();
                        example.setQcNo(detailEntity.getQcNo());
                        example.setUnquaTraceId(key);
                        example.setStatus(WmsCodeMaster.QC_FULL_QC.getCode());
                        example.setOrgId(param.getOrgId());
                        List<BanQinWmQcDetail> queryList = banQinWmQcDetailService.findList(example);
                        for (BanQinWmQcDetail wmQcDetailModel : queryList) {
                            wmQcDetailModel.setUnquaPaId(unquaPaId);
                            banQinWmQcDetailService.save(wmQcDetailModel);
                        }
                    }
                } catch (WarehouseException e) {
                    msg.addMessage("质检明细行号" + detailEntity.getLineNo() + e.getMessage());
                }
            }
        }
        if (StringUtils.isEmpty(msg.getMessage())) {
            msg.setSuccess(true);
            msg.addMessage("操作成功");
        }
        return msg;
    }

    /**
     * 描述： 良品上架参数
     */
    private BanQinWmTaskPaEntity createQuaTaskPaEntity(BanQinWmQcDetailEntity wmQcDetailEntity, String paRule) {
        BanQinWmTaskPaEntity entity = new BanQinWmTaskPaEntity();
        entity.setOrderNo(wmQcDetailEntity.getQcNo());
        entity.setOrderType(WmsCodeMaster.ORDER_QC.getCode());
        entity.setOwnerCode(wmQcDetailEntity.getOwnerCode());
        entity.setSkuCode(wmQcDetailEntity.getSkuCode());
        entity.setPackCode(wmQcDetailEntity.getPackCode());
        entity.setLotNum(wmQcDetailEntity.getQuaLotNum());
        entity.setFromId(wmQcDetailEntity.getQuaTraceId());
        entity.setFromLoc(wmQcDetailEntity.getQuaLoc());
        entity.setNewPaRule(paRule);
        entity.setPaIdRcv(wmQcDetailEntity.getQuaPaId());
        entity.setCubic(wmQcDetailEntity.getCubic());
        entity.setGrossWeight(wmQcDetailEntity.getGrossWeight());
        entity.setOrgId(wmQcDetailEntity.getOrgId());
        entity.setUom(wmQcDetailEntity.getUom());
        return entity;
    }

    /**
     * 描述： 不良品上架参数
     */
    private BanQinWmTaskPaEntity createUnQuaTaskPaEntity(BanQinWmQcDetailEntity wmQcDetailEntity, String paRule) {
        BanQinWmTaskPaEntity entity = new BanQinWmTaskPaEntity();
        entity.setOrderNo(wmQcDetailEntity.getQcNo());
        entity.setOrderType(WmsCodeMaster.ORDER_QC.getCode());
        entity.setOwnerCode(wmQcDetailEntity.getOwnerCode());
        entity.setSkuCode(wmQcDetailEntity.getSkuCode());
        entity.setPackCode(wmQcDetailEntity.getPackCode());
        entity.setLotNum(wmQcDetailEntity.getUnquaLotNum());
        entity.setFromId(wmQcDetailEntity.getUnquaTraceId());
        entity.setFromLoc(wmQcDetailEntity.getUnquaLoc());
        entity.setNewPaRule(paRule);
        entity.setPaIdRcv(wmQcDetailEntity.getUnquaPaId());
        entity.setCubic(wmQcDetailEntity.getCubic());
        entity.setGrossWeight(wmQcDetailEntity.getGrossWeight());
        entity.setOrgId(wmQcDetailEntity.getOrgId());
        entity.setUom(wmQcDetailEntity.getUom());
        return entity;
    }

    /**
     * 描述： 计算上架库位，并生成上架任务
     * <p>
     * 收货时根据商品属性，库位指定规则，判断是否进行上架。
     * a.人工指定库位（HH）
     * 收货时，填写计划收货库位，直接收货到该库位。不生成上架任务。库位库存为可用QTY。
     * 如果是过渡库位&TraceID不为“*”，可以人工生成上架任务。生成任务后，产生原库位上架待出数。计划库位的上架待入数。
     * b.收货时计算库位（IN）
     * 收货时，直接调用生成上架任务方法。
     * c.上架时计算库位（PA）
     * 待RF扫描tranceID时触发生成上架任务。
     */
    @Transactional
    public ResultMessage inboundCreatePaTask(BanQinWmTaskPaEntity taskPaEntity) {
        ResultMessage msg = new ResultMessage();
        // 单据类型
        String orderType = taskPaEntity.getOrderType();
        // 单据的订单类型
        String asnType = taskPaEntity.getAsnType();
        // 单据号
        String orderNo = taskPaEntity.getOrderNo();
        // 货主
        String ownerCode = taskPaEntity.getOwnerCode();
        // 商品
        String skuCode = taskPaEntity.getSkuCode();
        // 批次号
        String lotNum = taskPaEntity.getLotNum();
        // 收货库位
        String fromLoc = taskPaEntity.getFromLoc();
        // 收货traceID
        String fromId = taskPaEntity.getFromId();
        // 上架EA数量
        Double currentQtyPa = taskPaEntity.getQtyPaEa();
        // traceId实际可用库存
        Double qtyAvailable;
        // 实际生成上架任务数
        Double qtyPa;
        // 实际生成上架单位数
        Double qtyPaUom;
        // 单位(默认EA)
        String uom = taskPaEntity.getUom();
        // 包装
        String packCode = taskPaEntity.getPackCode();
        // 指定上架规则
        String newPaRule = taskPaEntity.getNewPaRule();
        // 上架库位指定规则
        String newReserveCode = taskPaEntity.getNewReserveCode();
        // 推荐库位
        String planToLoc;
        // 毛重
        double grossWeight = taskPaEntity.getGrossWeight() == null ? 0 : taskPaEntity.getGrossWeight();
        // 体积
        double cubic = taskPaEntity.getCubic() == null ? 0 : taskPaEntity.getCubic();

        // ASN收货库位为过渡库位，才能生成上架任务。
        BanQinCdWhLoc locModel = banQinCdWhLocService.findByLocCode(fromLoc, taskPaEntity.getOrgId());
        if (!WmsCodeMaster.LOC_USE_ST.getCode().equals(locModel.getLocUseType()) && orderType.equals(WmsCodeMaster.ORDER_ASN.getCode())) {
            if (WmsCodeMaster.RESERVE_RCV.getCode().equals(newReserveCode)) {
                msg.setSuccess(false);
                return msg;
            }
            throw new WarehouseException("收货库位需为过渡库位");
        }

        /*
         * 获取上架数
         */
        // 收货前计算库位（两步收货），上架数=收货数
        if (WmsCodeMaster.RESERVE_B_RCV_TWO.getCode().equals(newReserveCode)) {
            qtyPa = currentQtyPa;
        } else {
            if (WmsConstants.TRACE_ID.equals(fromId)) {
                // 实例化查询库存参数
                BanQinInventoryEntity checkEntity = new BanQinInventoryEntity();
                checkEntity.setOwnerCode(ownerCode);
                checkEntity.setSkuCode(skuCode);
                checkEntity.setLotNum(lotNum);
                checkEntity.setLocCode(fromLoc);
                checkEntity.setTraceId(fromId);
                checkEntity.setOrgId(taskPaEntity.getOrgId());
                // 根据owner,sku,loc,lotNum,traceId查询wm_inv_lot_loc中可用库存，如果不存在可用库存，则不能生成上架任务。
                qtyAvailable = this.banQinInventoryService.getAvailableQty(checkEntity);
                if (qtyAvailable == 0D) {
                    if (WmsCodeMaster.RESERVE_RCV.getCode().equals(newReserveCode)) {
                        msg.setSuccess(false);
                        return msg;
                    }
                    throw new WarehouseException("有效库存不足，批次[" + lotNum + "]库位[" + fromLoc + "]跟踪号[" + fromId + "]，不能操作");
                }
                if (currentQtyPa > qtyAvailable) {
                    qtyPa = qtyAvailable;
                } else {
                    qtyPa = currentQtyPa;
                }
            } else {
                // 合并上架任务：判断是否已存在同单同品同批次同库位同托盘的上架任务，存在则删除
                BanQinWmTaskPa con = new BanQinWmTaskPa();
                con.setOrderNo(orderNo);
                con.setStatus(WmsCodeMaster.TSK_NEW.getCode());
                con.setSkuCode(skuCode);
                con.setLotNum(lotNum);
                con.setFmLoc(fromLoc);
                con.setFmId(fromId);
                con.setOrgId(taskPaEntity.getOrgId());
                List<BanQinWmTaskPa> list = banQinWmTaskPaService.findList(con);
                for (BanQinWmTaskPa wmTaskPa : list) {
                    BanQinWmTaskPaEntity wmTaskPaEntity = new BanQinWmTaskPaEntity();
                    BeanUtils.copyProperties(wmTaskPa, wmTaskPaEntity);
                    this.inboundRemovePutaway(wmTaskPaEntity);
                }
                // 实例化查询库存参数
                BanQinInventoryEntity checkEntity = new BanQinInventoryEntity();
                checkEntity.setOwnerCode(ownerCode);
                checkEntity.setSkuCode(skuCode);
                checkEntity.setLotNum(lotNum);
                checkEntity.setLocCode(fromLoc);
                checkEntity.setTraceId(fromId);
                checkEntity.setOrgId(taskPaEntity.getOrgId());
                // 根据owner,sku,loc,lotNum,traceId查询wm_inv_lot_loc中可用库存，如果不存在可用库存，则不能生成上架任务。
                qtyAvailable = this.banQinInventoryService.getAvailableQty(checkEntity);
                if (qtyAvailable == 0D) {
                    if (WmsCodeMaster.RESERVE_RCV.getCode().equals(newReserveCode)) {
                        msg.setSuccess(false);
                        return msg;
                    }
                    throw new WarehouseException("有效库存不足，批次[" + lotNum + "]库位[" + fromLoc + "]跟踪号[" + fromId + "]，不能操作");
                }
                BanQinWmInvLotAtt wmInvLotAttModel = banQinWmInvLotAttService.getByLotNum(lotNum, taskPaEntity.getOrgId());
                if (WmsCodeMaster.QC_ATT_TO_QC.getCode().equals(wmInvLotAttModel.getLotAtt04())) {
                    if (WmsCodeMaster.RESERVE_RCV.getCode().equals(newReserveCode)) {
                        msg.setSuccess(false);
                        return msg;
                    }
                    throw new WarehouseException("商品需要质检，不能操作");
                }
                // 上架时计算库位，扫描跟踪号，上架数取有效库存。
                qtyPa = qtyAvailable;
            }
        }

        BanQinCdWhPackageRelationEntity packageRelationAndQtyUom = banQinWmsCommonService.getPackageRelationAndQtyUom(packCode, taskPaEntity.getUom(), taskPaEntity.getOrgId());
        Integer unitQty = packageRelationAndQtyUom.getCdprQuantity();
        qtyPaUom = banQinWmsCommonService.doubleDivide(qtyPa, unitQty.doubleValue());

        // 收货前计算库位（两步收货）并且 计划上架库位不为空（预约到库位），生成上架任务，推荐库位=计划上架库位
        if (WmsCodeMaster.RESERVE_B_RCV_TWO.getCode().equals(newReserveCode)) {
            planToLoc = taskPaEntity.getPlanPaLoc();
            if (StringUtils.isEmpty(planToLoc)) {
                msg.setSuccess(false);
                return msg;
            }
        } else {
            // 计算上架库位
            BanQinWmTaskPaEntity entity = new BanQinWmTaskPaEntity();
            entity.setOwnerCode(ownerCode);
            entity.setSkuCode(skuCode);
            entity.setFromLoc(fromLoc);
            entity.setFromId(fromId);
            entity.setLotNum(lotNum);
            entity.setOrderType(asnType);
            entity.setPackCode(packCode);
            entity.setPaRule(newPaRule);
            entity.setUom(uom);
            entity.setQtyPa(qtyPa);
            entity.setQtyPaUom(qtyPaUom);
            entity.setGrossWeight(grossWeight * qtyPa);
            entity.setCubic(cubic * qtyPa);
            entity.setTraceId(taskPaEntity.getTraceId());
            entity.setRealCaseId(taskPaEntity.getRealCaseId());
            entity.setOrgId(taskPaEntity.getOrgId());

            if (!WmsConstants.TRACE_ID.equals(fromId) && StringUtils.isNotEmpty(fromId)) {
                List<BanQinWmInvLotLoc> lotLocModels = banQinWmInvLotLocService.getByTraceId(fromId, taskPaEntity.getOrgId());
                // 如果跟踪号在库存记录中存在 且 库位不等于收货库位，则不计算上架库位
                if (lotLocModels.stream().map(BanQinWmInvLotLoc::getLocCode).anyMatch(locCode -> !fromLoc.equals(locCode))) {
                    planToLoc = null;
                } else {
                    planToLoc = this.putawayCalculation(entity);
                }
            } else {
                planToLoc = this.putawayCalculation(entity);
            }

            // 实例化上架更新库存参数
            BanQinInventoryEntity fmEntity = new BanQinInventoryEntity();
            fmEntity.setAction(ActionCode.CREATE_PA_TASK);
            fmEntity.setOrderNo(orderNo);
            fmEntity.setOwnerCode(ownerCode);
            fmEntity.setSkuCode(skuCode);
            fmEntity.setPackCode(packCode);
            fmEntity.setUom(uom);
            fmEntity.setLotNum(lotNum);
            fmEntity.setTraceId(fromId);
            fmEntity.setLocCode(fromLoc);
            fmEntity.setQtyUom(qtyPaUom);
            fmEntity.setQtyEaOp(qtyPa);
            fmEntity.setOrgId(taskPaEntity.getOrgId());

            BanQinInventoryEntity toEntity = new BanQinInventoryEntity();
            toEntity.setAction(ActionCode.CREATE_PA_TASK);
            toEntity.setOrderNo(orderNo);
            toEntity.setOwnerCode(ownerCode);
            toEntity.setSkuCode(skuCode);
            toEntity.setPackCode(packCode);
            toEntity.setUom(uom);
            toEntity.setLotNum(lotNum);
            toEntity.setTraceId(fromId);
            toEntity.setLocCode(planToLoc);
            toEntity.setPlanLocCode(planToLoc);
            toEntity.setQtyUom(qtyPaUom);
            toEntity.setQtyEaOp(qtyPa);
            toEntity.setOrgId(taskPaEntity.getOrgId());

            // 执行上架更新，更新收货库位待出数，计划库位待入数
            banQinInventoryService.updateInventory(fmEntity, toEntity);
        }
        // 生成上架任务
        BanQinWmTaskPa wmTaskPaModel = banQinWmTaskPaService.saveTaskPa(taskPaEntity, planToLoc, qtyPa, uom, qtyPaUom);
        BanQinWmTaskPaEntity wmTaskPaEntity = new BanQinWmTaskPaEntity();
        BeanUtils.copyProperties(wmTaskPaModel, wmTaskPaEntity);
        // 构造返回的参数
        msg.setData(wmTaskPaEntity);
        msg.setSuccess(true);
        return msg;
    }

    /**
     * 描述： 根据上架规则计算上架库位
     *
     * @param entity 上架任务
     */
    public String putawayCalculation(BanQinWmTaskPaEntity entity) {
        String orgId = entity.getOrgId();
        String paRule = entity.getPaRule();
        if (StringUtils.isNotEmpty(paRule)) {
            BanQinCdRulePaHeader paHeader = this.banQinCdRulePaHeaderService.getByRuleCode(paRule, orgId);
            if (paHeader != null) {
                List<BanQinCdRulePaDetail> paDetails = this.banQinCdRulePaDetailService.getByRuleCode(paRule, orgId);
                for (BanQinCdRulePaDetail paDetail : paDetails) {
                    String SQL = " select cwl.LOC_code,cwl.lane,cwl.seq,cwl.floor " + " from cd_wh_loc cwl " + " where 1=1 " + " and cwl.loc_code not in('SORTATION','STAGE') "
                            + " and cwl.IS_ENABLE='Y' " + " and cwl.Status='00' " + " and cwl.org_id='" + orgId + "' ";
                    String mainCode = paDetail.getMainCode();
                    // 是否包装限制
                    if (this.checkPackageRestrict(paDetail, entity)) {
                        continue;
                    }
                    // 是否订单类型限制
                    if (this.checkAsnTypeRestrict(paDetail, entity)) {
                        continue;
                    }
                    // 是否批次属性限制
                    if (this.checkLotAttRestrict(paDetail, entity)) {
                        continue;
                    }
                    if (WmsCodeMaster.PA_RULE_A01.getCode().equals(mainCode)) {
                        if (paDetail.getToLoc() != null) {
                            return paDetail.getToLoc();
                        }
                    } else if (WmsCodeMaster.PA_RULE_A02.getCode().equals(mainCode)) {
                        BanQinCdWhSku skuModel = this.checkSkuByOwnerAndSku(entity.getOwnerCode(), entity.getSkuCode(), entity.getOrgId());
                        if (skuModel.getPaLoc() != null) {
                            return skuModel.getPaLoc();
                        }
                    } else if (WmsCodeMaster.PA_RULE_A03.getCode().equals(mainCode)) {
                        BanQinCdWhSkuLoc skuLoc = this.banQinCdWhSkuLocService.getCdWhSkuLocType(entity.getOwnerCode(), entity.getSkuCode(), WmsCodeMaster.LOC_USE_EA.getCode(), entity.getOrgId());
                        if (skuLoc != null && skuLoc.getLocCode() != null) {
                            return skuLoc.getLocCode();
                        }
                    } else if (WmsCodeMaster.PA_RULE_A04.getCode().equals(mainCode)) {
                        BanQinCdWhSkuLoc skuLoc = this.banQinCdWhSkuLocService.getCdWhSkuLocType(entity.getOwnerCode(), entity.getSkuCode(), WmsCodeMaster.LOC_USE_CS.getCode(), entity.getOrgId());
                        if (skuLoc != null && skuLoc.getLocCode() != null) {
                            return skuLoc.getLocCode();
                        }
                    } else if (WmsCodeMaster.PA_RULE_A05.getCode().equals(mainCode)) {
                        BanQinCdWhSkuLoc skuLoc = this.banQinCdWhSkuLocService.getCdWhSkuLocType(entity.getOwnerCode(), entity.getSkuCode(), WmsCodeMaster.LOC_USE_PC.getCode(), entity.getOrgId());
                        if (skuLoc != null && skuLoc.getLocCode() != null) {
                            return skuLoc.getLocCode();
                        }
                    } else if (WmsCodeMaster.PA_RULE_A06.getCode().equals(mainCode)) {
                        String fromLoc = entity.getFmLoc();
                        if (StringUtils.isEmpty(fromLoc)) {
                            throw new WarehouseException("收货库位不能为空");
                        }
                        String fmLoc = paDetail.getFmLoc();
                        if (fromLoc.equals(fmLoc) && paDetail.getToLoc() != null) {
                            return paDetail.getToLoc();
                        }
                    } else if (WmsCodeMaster.PA_RULE_A07.getCode().equals(mainCode)) {
                        String locCode = paDetail.getToLoc();
                        locCode = this.checkSpaceRestrict(locCode, paDetail, entity);
                        if (locCode != null) {
                            boolean isFlag = this.checkInvMixSkuMixLot(locCode, entity);
                            if (!isFlag) {
                                continue;
                            }
                            SQL = SQL + " and cwl.loc_code='" + locCode + "' ";
                            String locAttrRestrict = checkLocAttrRestrict(paDetail);
                            SQL = SQL + locAttrRestrict;
                            String orderBy = " order by cwl.pa_seq ";
                            SQL = SQL + orderBy;
                            Object[] locCodes = this.getLocBySql(SQL);
                            if (locCodes.length > 0) {
                                return locCode;
                            }
                        }
                    } else if (WmsCodeMaster.PA_RULE_B01.getCode().equals(mainCode)) {
                        String zone = paDetail.getToZone();
                        SQL = SQL + " and cwl.zone_code='" + zone + "' ";
                        String locAttrRestrict = checkLocAttrRestrict(paDetail);
                        SQL = SQL + locAttrRestrict;
                        String orderBy = " order by cwl.pa_seq ";
                        SQL = SQL + orderBy;
                        Object[] locCodes = this.getLocBySql(SQL);
                        String planToLoc = this.findEmptyLoc(locCodes, paDetail, entity);
                        if (planToLoc != null) {
                            return planToLoc;
                        }
                    } else if (WmsCodeMaster.PA_RULE_B02.getCode().equals(mainCode)) {
                        BanQinCdWhSku skuModel = this.checkSkuByOwnerAndSku(entity.getOwnerCode(), entity.getSkuCode(), entity.getOrgId());
                        String zone = skuModel.getPaZone();
                        SQL = SQL + " and cwl.zone_code='" + zone + "' ";
                        String locAttrRestrict = checkLocAttrRestrict(paDetail);
                        SQL = SQL + locAttrRestrict;
                        String orderBy = " order by cwl.pa_seq ";
                        SQL = SQL + orderBy;
                        Object[] locCodes = this.getLocBySql(SQL);
                        String planToLoc = this.findEmptyLoc(locCodes, paDetail, entity);
                        if (planToLoc != null) {
                            return planToLoc;
                        }
                    } else if (WmsCodeMaster.PA_RULE_B03.getCode().equals(mainCode)) {
                        BanQinCdWhSkuLoc skuLoc = this.banQinCdWhSkuLocService.getCdWhSkuLocType(entity.getOwnerCode(), entity.getSkuCode(), WmsCodeMaster.LOC_USE_EA.getCode(), entity.getOrgId());
                        if (skuLoc != null) {
                            String eaLocCode = skuLoc.getLocCode();
                            SQL = SQL + " and cwl.loc_code!='" + eaLocCode + "' ";
                            String locAttrRestrict = checkLocAttrRestrict(paDetail);
                            SQL = SQL + locAttrRestrict;
                            /* 同边优先于对面边 */
                            BanQinCdWhLoc eaLoc = this.banQinCdWhLocService.findByLocCode(eaLocCode, entity.getOrgId());
                            String orderBy = this.orderBy(eaLoc);
                            SQL = SQL + orderBy;
                            Object[] locCodes = this.getLocBySql(SQL);
                            String planToLoc = this.findEmptyLoc(locCodes, paDetail, entity);
                            if (planToLoc != null) {
                                return planToLoc;
                            }
                        }
                    } else if (WmsCodeMaster.PA_RULE_B04.getCode().equals(mainCode)) {
                        BanQinCdWhSkuLoc skuLoc = this.banQinCdWhSkuLocService.getCdWhSkuLocType(entity.getOwnerCode(), entity.getSkuCode(), WmsCodeMaster.LOC_USE_CS.getCode(), entity.getOrgId());
                        if (skuLoc != null) {
                            String csLocCode = skuLoc.getLocCode();
                            SQL = SQL + " and cwl.loc_code!='" + csLocCode + "' ";
                            String locAttrRestrict = checkLocAttrRestrict(paDetail);
                            SQL = SQL + locAttrRestrict;
                            /* 同边优先于对面边 */
                            BanQinCdWhLoc csLoc = this.banQinCdWhLocService.findByLocCode(csLocCode, entity.getOrgId());
                            String orderBy = this.orderBy(csLoc);
                            SQL = SQL + orderBy;
                            Object[] locCodes = this.getLocBySql(SQL);
                            String planToLoc = this.findEmptyLoc(locCodes, paDetail, entity);
                            if (planToLoc != null) {
                                return planToLoc;
                            }
                        }
                    } else if (WmsCodeMaster.PA_RULE_B05.getCode().equals(mainCode)) {
                        BanQinCdWhSkuLoc skuLoc = this.banQinCdWhSkuLocService.getCdWhSkuLocType(entity.getOwnerCode(), entity.getSkuCode(), WmsCodeMaster.LOC_USE_PC.getCode(), entity.getOrgId());
                        if (skuLoc != null) {
                            String pcLocCode = skuLoc.getLocCode();
                            SQL = SQL + " and cwl.loc_code!='" + pcLocCode + "' ";
                            String locAttrRestrict = checkLocAttrRestrict(paDetail);
                            SQL = SQL + locAttrRestrict;
                            /* 同边优先于对面边 */
                            BanQinCdWhLoc pcLoc = this.banQinCdWhLocService.findByLocCode(pcLocCode, entity.getOrgId());
                            String orderBy = this.orderBy(pcLoc);
                            SQL = SQL + orderBy;
                            Object[] locCodes = this.getLocBySql(SQL);
                            String planToLoc = this.findEmptyLoc(locCodes, paDetail, entity);
                            if (planToLoc != null) {
                                return planToLoc;
                            }
                        }
                    } else if (WmsCodeMaster.PA_RULE_C01.getCode().equals(mainCode)) {
                        String zone = paDetail.getToZone();
                        SQL = SQL + " and cwl.zone_code='" + zone + "' ";
                        String locAttrRestrict = checkLocAttrRestrict(paDetail);
                        SQL = SQL + locAttrRestrict;
                        String orderBy = " order by cwl.pa_seq ";
                        SQL = SQL + orderBy;
                        Object[] locCodes = this.getLocBySql(SQL);
                        String planToLoc = this.findNotEmptyLoc(locCodes, paDetail, entity);
                        if (planToLoc != null) {
                            return planToLoc;
                        }
                    } else if (WmsCodeMaster.PA_RULE_C02.getCode().equals(mainCode)) {
                        BanQinCdWhSku skuModel = this.checkSkuByOwnerAndSku(entity.getOwnerCode(), entity.getSkuCode(), entity.getOrgId());
                        String zone = skuModel.getPaZone();
                        SQL = SQL + " and cwl.zone_code='" + zone + "' ";
                        String locAttrRestrict = checkLocAttrRestrict(paDetail);
                        SQL = SQL + locAttrRestrict;
                        String orderBy = " order by cwl.pa_seq ";
                        SQL = SQL + orderBy;
                        Object[] locCodes = this.getLocBySql(SQL);
                        String planToLoc = this.findNotEmptyLoc(locCodes, paDetail, entity);
                        if (planToLoc != null) {
                            return planToLoc;
                        }
                    } else if (WmsCodeMaster.PA_RULE_C03.getCode().equals(mainCode)) {
                        BanQinCdWhSkuLoc skuLoc = this.banQinCdWhSkuLocService.getCdWhSkuLocType(entity.getOwnerCode(), entity.getSkuCode(), WmsCodeMaster.LOC_USE_EA.getCode(), entity.getOrgId());
                        if (skuLoc != null) {
                            String eaLocCode = skuLoc.getLocCode();
                            SQL = SQL + " and cwl.loc_code!='" + eaLocCode + "' ";
                            String locAttrRestrict = checkLocAttrRestrict(paDetail);
                            SQL = SQL + locAttrRestrict;
                            /* 同边优先于对面边 */
                            BanQinCdWhLoc eaLoc = this.banQinCdWhLocService.findByLocCode(eaLocCode, entity.getOrgId());
                            String orderBy = this.orderBy(eaLoc);
                            SQL = SQL + orderBy;
                            Object[] locCodes = this.getLocBySql(SQL);
                            String planToLoc = this.findNotEmptyLoc(locCodes, paDetail, entity);
                            if (planToLoc != null) {
                                return planToLoc;
                            }
                        }
                    } else if (WmsCodeMaster.PA_RULE_C04.getCode().equals(mainCode)) {
                        BanQinCdWhSkuLoc skuLoc = this.banQinCdWhSkuLocService.getCdWhSkuLocType(entity.getOwnerCode(), entity.getSkuCode(), WmsCodeMaster.LOC_USE_CS.getCode(), entity.getOrgId());
                        if (skuLoc != null) {
                            String csLocCode = skuLoc.getLocCode();
                            SQL = SQL + " and cwl.loc_code!='" + csLocCode + "' ";
                            String locAttrRestrict = checkLocAttrRestrict(paDetail);
                            SQL = SQL + locAttrRestrict;
                            /* 同边优先于对面边 */
                            BanQinCdWhLoc csLoc = this.banQinCdWhLocService.findByLocCode(csLocCode, entity.getOrgId());
                            String orderBy = this.orderBy(csLoc);
                            SQL = SQL + orderBy;
                            Object[] locCodes = this.getLocBySql(SQL);
                            String planToLoc = this.findNotEmptyLoc(locCodes, paDetail, entity);
                            if (planToLoc != null) {
                                return planToLoc;
                            }
                        }
                    } else if (WmsCodeMaster.PA_RULE_C05.getCode().equals(mainCode)) {
                        BanQinCdWhSkuLoc skuLoc = this.banQinCdWhSkuLocService.getCdWhSkuLocType(entity.getOwnerCode(), entity.getSkuCode(), WmsCodeMaster.LOC_USE_PC.getCode(), entity.getOrgId());
                        if (skuLoc != null) {
                            String pcLocCode = skuLoc.getLocCode();
                            SQL = SQL + " and cwl.loc_code!='" + pcLocCode + "' ";
                            String locAttrRestrict = checkLocAttrRestrict(paDetail);
                            SQL = SQL + locAttrRestrict;
                            /* 同边优先于对面边 */
                            BanQinCdWhLoc pcLoc = this.banQinCdWhLocService.findByLocCode(pcLocCode, entity.getOrgId());
                            String orderBy = this.orderBy(pcLoc);
                            SQL = SQL + orderBy;
                            Object[] locCodes = this.getLocBySql(SQL);
                            String planToLoc = this.findNotEmptyLoc(locCodes, paDetail, entity);
                            if (planToLoc != null) {
                                return planToLoc;
                            }
                        }
                    } else if (WmsCodeMaster.PA_RULE_D01.getCode().equals(mainCode)) {
                        String zone = paDetail.getToZone();
                        SQL = SQL + " and cwl.zone_code='" + zone + "' ";
                        String locAttrRestrict = checkLocAttrRestrict(paDetail);
                        SQL = SQL + locAttrRestrict;
                        String orderBy = " order by cwl.pa_seq ";
                        SQL = SQL + orderBy;
                        Object[] locCodes = this.getLocBySql(SQL);
                        String planToLoc = this.findHaveSameSkuLoc(locCodes, paDetail, entity);
                        if (planToLoc != null) {
                            return planToLoc;
                        }
                    } else if (WmsCodeMaster.PA_RULE_D02.getCode().equals(mainCode)) {
                        BanQinCdWhSku skuModel = this.checkSkuByOwnerAndSku(entity.getOwnerCode(), entity.getSkuCode(), entity.getOrgId());
                        String zone = skuModel.getPaZone();
                        SQL = SQL + " and cwl.zone_code='" + zone + "' ";
                        String locAttrRestrict = checkLocAttrRestrict(paDetail);
                        SQL = SQL + locAttrRestrict;
                        String orderBy = " order by cwl.pa_seq ";
                        SQL = SQL + orderBy;
                        Object[] locCodes = this.getLocBySql(SQL);
                        String planToLoc = this.findHaveSameSkuLoc(locCodes, paDetail, entity);
                        if (planToLoc != null) {
                            return planToLoc;
                        }
                    } else if (WmsCodeMaster.PA_RULE_D03.getCode().equals(mainCode)) {
                        BanQinCdWhSkuLoc skuLoc = this.banQinCdWhSkuLocService.getCdWhSkuLocType(entity.getOwnerCode(), entity.getSkuCode(), WmsCodeMaster.LOC_USE_EA.getCode(), entity.getOrgId());
                        if (skuLoc != null) {
                            String eaLocCode = skuLoc.getLocCode();
                            SQL = SQL + " and cwl.loc_code!='" + eaLocCode + "' ";
                            String locAttrRestrict = checkLocAttrRestrict(paDetail);
                            SQL = SQL + locAttrRestrict;
                            /* 同边优先于对面边 */
                            BanQinCdWhLoc eaLoc = this.banQinCdWhLocService.findByLocCode(eaLocCode, entity.getOrgId());
                            String orderBy = this.orderBy(eaLoc);
                            SQL = SQL + orderBy;
                            Object[] locCodes = this.getLocBySql(SQL);
                            String planToLoc = this.findHaveSameSkuLoc(locCodes, paDetail, entity);
                            if (planToLoc != null) {
                                return planToLoc;
                            }
                        }
                    } else if (WmsCodeMaster.PA_RULE_D04.getCode().equals(mainCode)) {
                        BanQinCdWhSkuLoc skuLoc = this.banQinCdWhSkuLocService.getCdWhSkuLocType(entity.getOwnerCode(), entity.getSkuCode(), WmsCodeMaster.LOC_USE_CS.getCode(), entity.getOrgId());
                        if (skuLoc != null) {
                            String csLocCode = skuLoc.getLocCode();
                            SQL = SQL + " and cwl.loc_code!='" + csLocCode + "' ";
                            String locAttrRestrict = checkLocAttrRestrict(paDetail);
                            SQL = SQL + locAttrRestrict;
                            /* 同边优先于对面边 */
                            BanQinCdWhLoc csLoc = this.banQinCdWhLocService.findByLocCode(csLocCode, entity.getOrgId());
                            String orderBy = this.orderBy(csLoc);
                            SQL = SQL + orderBy;
                            Object[] locCodes = this.getLocBySql(SQL);
                            String planToLoc = this.findHaveSameSkuLoc(locCodes, paDetail, entity);
                            if (planToLoc != null) {
                                return planToLoc;
                            }
                        }
                    } else if (WmsCodeMaster.PA_RULE_D05.getCode().equals(mainCode)) {
                        BanQinCdWhSkuLoc skuLoc = this.banQinCdWhSkuLocService.getCdWhSkuLocType(entity.getOwnerCode(), entity.getSkuCode(), WmsCodeMaster.LOC_USE_PC.getCode(), entity.getOrgId());
                        if (skuLoc != null) {
                            String pcLocCode = skuLoc.getLocCode();
                            SQL = SQL + " and cwl.loc_code!='" + pcLocCode + "' ";
                            String locAttrRestrict = checkLocAttrRestrict(paDetail);
                            SQL = SQL + locAttrRestrict;
                            /* 同边优先于对面边 */
                            BanQinCdWhLoc pcLoc = this.banQinCdWhLocService.findByLocCode(pcLocCode, entity.getOrgId());
                            String orderBy = this.orderBy(pcLoc);
                            SQL = SQL + orderBy;
                            Object[] locCodes = this.getLocBySql(SQL);
                            String planToLoc = this.findHaveSameSkuLoc(locCodes, paDetail, entity);
                            if (planToLoc != null) {
                                return planToLoc;
                            }
                        }
                    } else if (WmsCodeMaster.PA_RULE_E01.getCode().equals(mainCode)) {
                        String traceLoc = this.findSkuSameTraceLoc(entity.getRealCaseId());
                        SQL = SQL + traceLoc;
                        Object[] locCodes = this.getLocBySql(SQL);
                        if (locCodes.length > 0) {
                            return locCodes[0].toString();
                        }
                    }
                }
            } else {
                throw new WarehouseException("上架规则" + paRule + "不存在或配置错误");
            }
        } else {
            throw new WarehouseException("上架规则不能为空");
        }
        return null;
    }

    /**
     * 描述： 校验商品信息
     */
    private BanQinCdWhSku checkSkuByOwnerAndSku(String ownerCode, String skuCode, String orgId) {
        if (StringUtils.isNotEmpty(ownerCode) && StringUtils.isNotEmpty(skuCode)) {
            BanQinCdWhSku skuModel = this.banQinCdWhSkuService.getByOwnerAndSkuCode(ownerCode, skuCode, orgId);
            if (skuModel == null) {
                throw new WarehouseException("货主" + ownerCode + "的商品" + skuCode + "信息不存在或错误");
            } else {
                return skuModel;
            }
        } else {
            throw new WarehouseException("货主编码和商品编码都不能为空");
        }
    }

    /**
     * 描述： 查找最近库位
     */
    private String orderBy(BanQinCdWhLoc loc) {
        String orderBy;
        String lane = loc.getLane();
        Long seq = loc.getSeq();
        Long floor = loc.getFloor();
        if (StringUtils.isNotEmpty(lane) && seq != null && floor != null) {
            // 同边优先于对面边计算
            orderBy = " order by abs (to_number(cwl.lane) - to_number('" + lane + "'))," + " abs(mod(cwl.seq - " + seq + ",2))," + " abs(to_number(cwl.seq) - " + seq + "),"
                    + " power(to_number(cwl.seq) - " + seq + ",2)+power(to_number(cwl.floor) - " + floor + ",2),cwl.seq ";
        } else {
            orderBy = " order by cwl.pa_seq ";
        }
        return orderBy;
    }

    /**
     * 查找空库位
     */
    private String findEmptyLoc(Object[] locs, BanQinCdRulePaDetail paDetail, BanQinWmTaskPaEntity entity) {
        for (Object locCode : locs) {
            boolean isEmpty = this.banQinWmInvLotLocService.isEmptyLoc(locCode.toString(), entity.getOrgId());
            if (isEmpty) {
                boolean isFlag = this.checkInvMixSkuMixLot(locCode.toString(), entity);
                if (!isFlag) {
                    continue;
                }
                String retLocCode = this.checkSpaceRestrict(locCode.toString(), paDetail, entity);
                if (retLocCode != null) {
                    return retLocCode;
                }
            }
        }
        return null;

    }

    /**
     * 查找非空库位
     */
    private String findNotEmptyLoc(Object[] locs, BanQinCdRulePaDetail paDetail, BanQinWmTaskPaEntity entity) {
        for (Object locCode : locs) {
            boolean isNotEmpty = this.banQinWmInvLotLocService.isNotEmptyLoc(locCode.toString(), entity.getOrgId());
            if (isNotEmpty) {
                boolean isFlag = this.checkInvMixSkuMixLot(locCode.toString(), entity);
                if (!isFlag) {
                    continue;
                }
                String retLocCode = this.checkSpaceRestrict(locCode.toString(), paDetail, entity);
                if (retLocCode != null) {
                    return retLocCode;
                }
            }
        }
        return null;

    }

    /**
     * 查找同商品库位
     */
    private String findHaveSameSkuLoc(Object[] locs, BanQinCdRulePaDetail paDetail, BanQinWmTaskPaEntity entity) {
        for (Object locCode : locs) {
            boolean isHaveSameSku = this.banQinWmInvLotLocService.isHaveSameSkuLoc(entity.getOwnerCode(), entity.getSkuCode(), locCode.toString(), entity.getOrgId());
            if (isHaveSameSku) {
                boolean isFlag = this.checkInvMixSkuMixLot(locCode.toString(), entity);
                if (!isFlag) {
                    continue;
                }
                String retLocCode = this.checkSpaceRestrict(locCode.toString(), paDetail, entity);
                if (retLocCode != null) {
                    return retLocCode;
                }
            }
        }
        return null;
    }

    /**
     * 描述：查找托盘所在的库位
     */
    private String findSkuSameTraceLoc(String lotAtt07) {
        return " and exists" +
                " (" +
                " select 1 from wm_asn_detail_receive wmad" +
                " inner join wm_task_pa wmtp" +
                " on wmtp.to_id = wmad.to_id" +
                " and wmtp.org_id = wmad.org_id" +
                " where wmtp.to_loc = cwl.loc_code" +
                " and wmtp.to_id != '*'" +
                " and wmad.lot_att07 = '" + lotAtt07 + "'" +
                " and wmad.org_id = cwl.org_id" +
                " )";
    }

    /**
     * 描述： 根据字符串SQL脚本查询库位
     */
    private Object[] getLocBySql(String sql) {
        List<Object> lstResult = banQinCdWhLocMapper.execSelectSql(sql);
        if (lstResult != null && lstResult.size() > 0) {
            Object[] objs = new Object[lstResult.size()];
            for (int i = lstResult.size() - 1; i >= 0; i--) {
                objs[i] = lstResult.get(i);
            }
            return objs;
        } else {
            return new Object[]{};
        }
    }

    /**
     * 包装限制
     *
     * @param paDetail 上架规则明细
     * @param entity   上架任务
     */
    private boolean checkPackageRestrict(BanQinCdRulePaDetail paDetail, BanQinWmTaskPaEntity entity) {
        boolean isRestrict = false;
        if (WmsConstants.YES.equals(paDetail.getIsPackageRestrict())) {
            String isLessCs = paDetail.getIsLessCs();
            String isMoreCsLessPl = paDetail.getIsMoreCsLessPl();
            String isMorePl = paDetail.getIsMorePl();
            String packCode = entity.getPackCode();
            Double qtyPa = entity.getQtyPa();
            Double csUomQty = 0D;
            Double plUomQty = 0D;
            boolean isea = false;
            boolean iscs = false;
            boolean ispl = false;
            if (StringUtils.isNotEmpty(packCode)) {
                BanQinCdWhPackageEntity packageEntity = banQinCdWhPackageService.findByPackageCode(packCode, entity.getOrgId());
                for (BanQinCdWhPackageRelation item : packageEntity.getCdWhPackageRelations()) {
                    if (WmsConstants.UOM_CS.equals(item.getCdprUnitLevel())) {
                        csUomQty = item.getCdprQuantity();
                    } else if (WmsConstants.UOM_PL.equals(item.getCdprUnitLevel())) {
                        plUomQty = item.getCdprQuantity();
                    }
                }
            } else {
                throw new WarehouseException("包装代码" + packCode + "不存在或配置错误");
            }
            if (WmsConstants.YES.equals(isLessCs)) {
                if (qtyPa < csUomQty) {
                    isea = true;
                }
            }
            if (WmsConstants.YES.equals(isMoreCsLessPl)) {
                if (qtyPa < plUomQty && qtyPa >= csUomQty) {
                    iscs = true;
                }
            }
            if (WmsConstants.YES.equals(isMorePl)) {
                if (qtyPa >= plUomQty) {
                    ispl = true;
                }
            }
            isRestrict = !isea && !iscs && !ispl;
        }
        return isRestrict;
    }

    /**
     * 订单类型限制
     *
     * @param paDetail 上架规则明细
     * @param entity   上架任务
     */
    private boolean checkAsnTypeRestrict(BanQinCdRulePaDetail paDetail, BanQinWmTaskPaEntity entity) {
        boolean isRestrict = false;
        if (WmsConstants.YES.equals(paDetail.getIsAsnTypeRestrict())) {
            String iAsnType = paDetail.getIncludeAsnType();
            String eAsnType = paDetail.getExcludeAsnType();
            List<String> iAsnTypes = Lists.newArrayList();
            List<String> eAsnTypes = Lists.newArrayList();
            boolean isEmpty = false;
            if (StringUtils.isNotEmpty(iAsnType)) {
                String[] in = iAsnType.split(",");
                iAsnTypes.addAll(Arrays.asList(in));
            }
            if (StringUtils.isNotEmpty(eAsnType)) {
                String[] ex = eAsnType.split(",");
                eAsnTypes.addAll(Arrays.asList(ex));
            }
            if (iAsnTypes.size() < 1) {
                isEmpty = true;
            }
            if (!((iAsnTypes.contains(entity.getOrderType()) || isEmpty) && !eAsnTypes.contains(entity.getOrderType()))) {
                isRestrict = true;
            }
        }
        return isRestrict;
    }

    /**
     * 批次属性限制
     *
     * @param paDetail 上架规则明细
     * @param entity   上架任务
     */
    private boolean checkLotAttRestrict(BanQinCdRulePaDetail paDetail, BanQinWmTaskPaEntity entity) {
        boolean isRestrict = false;
        if (WmsConstants.YES.equals(paDetail.getIsLotAttRestrict())) {
            String lotAtt04Equal = paDetail.getLotAtt04Equal();
            String lotAtt05Equal = paDetail.getLotAtt05Equal();
            String lotAtt04Unequal = paDetail.getLotAtt04Unequal();
            String lotAtt05Unequal = paDetail.getLotAtt05Unequal();
            String lotAtt04;
            String lotAtt05;
            String lotNum = entity.getLotNum();

            if (StringUtils.isNotEmpty(lotNum)) {
                BanQinWmInvLotAtt lotAtt = banQinWmInvLotAttService.getByLotNum(lotNum, paDetail.getOrgId());
                if (lotAtt != null) {
                    lotAtt04 = lotAtt.getLotAtt04();
                    lotAtt05 = lotAtt.getLotAtt05();
                } else {
                    throw new WarehouseException("库存批次属性错误");
                }
            } else {
                throw new WarehouseException("批次号不能为空");
            }
            if ((StringUtils.isNotEmpty(lotAtt04Equal) && !lotAtt04Equal.equals(lotAtt04)) || (StringUtils.isNotEmpty(lotAtt04Unequal) && lotAtt04Unequal.equals(lotAtt04))) {
                isRestrict = true;
            }
            if ((StringUtils.isNotEmpty(lotAtt05Equal) && !lotAtt05Equal.equals(lotAtt05)) || (StringUtils.isNotEmpty(lotAtt05Unequal) && lotAtt05Unequal.equals(lotAtt05))) {
                isRestrict = true;
            }
        }
        return isRestrict;
    }

    /**
     * 库位空间限制
     *
     * @param locCode  库位编码
     * @param paDetail 上架规则明细
     * @param entity   上架任务
     */
    private String checkSpaceRestrict(String locCode, BanQinCdRulePaDetail paDetail, BanQinWmTaskPaEntity entity) {
        String returnLocCode;
        if (WmsConstants.YES.equals(paDetail.getIsSpaceRestrict())) {
            BanQinCdWhLoc loc = this.banQinCdWhLocService.findByLocCode(locCode, entity.getOrgId());
            if (WmsConstants.YES.equals(paDetail.getIsPlRestrict())) {
                returnLocCode = this.checkPalletRestrict(loc);
                if (returnLocCode == null) {
                    return null;
                }
            }
            if (WmsConstants.YES.equals(paDetail.getIsCubicRestrict())) {
                returnLocCode = this.checkCubicRestrict(loc, entity);
                if (returnLocCode == null) {
                    return null;
                }
            }
            if (WmsConstants.YES.equals(paDetail.getIsWeightRestrict())) {
                returnLocCode = this.checkWeightRestrict(loc, entity);
                if (returnLocCode == null) {
                    return null;
                }
            } else {
                returnLocCode = locCode;
            }
        } else {
            returnLocCode = locCode;
        }
        return returnLocCode;
    }

    /**
     * 库位空间限制-托盘数限制
     *
     * @param loc 库位编码
     */
    private String checkPalletRestrict(BanQinCdWhLoc loc) {
        String locCode = loc.getLocCode();
        String isLoseId = loc.getIsLoseId();
        Long invPl;
        if (WmsConstants.YES.equals(isLoseId)) {
            invPl = this.banQinWmInvLotLocService.getPalletQty1ByLocCode(locCode, loc.getOrgId());
        } else {
            invPl = this.banQinWmInvLotLocService.getPalletQtyByLocCode(locCode, loc.getOrgId());
        }
        // 默认1托盘
        if (loc.getMaxPl() != null && loc.getMaxPl() > 0) {
            if (invPl + 1 > loc.getMaxPl()) {
                return null;
            }
        }
        return locCode;
    }

    /**
     * 库位空间限制-体积限制
     *
     * @param loc    库位编码
     * @param entity 上架任务
     */
    private String checkCubicRestrict(BanQinCdWhLoc loc, BanQinWmTaskPaEntity entity) {
        String locCode = loc.getLocCode();
        double cubic = entity.getCubic();
        Double invCubic = this.banQinWmInvLotLocService.getCubicByLocCode(locCode, loc.getOrgId());
        if (loc.getMaxCubic() != null && loc.getMaxCubic() > 0.0) {
            if (cubic + invCubic > loc.getMaxCubic()) {
                return null;
            }
        }
        return locCode;

    }

    /**
     * 库位空间限制-重量限制
     *
     * @param loc    库位编码
     * @param entity 上架任务
     */
    private String checkWeightRestrict(BanQinCdWhLoc loc, BanQinWmTaskPaEntity entity) {
        String locCode = loc.getLocCode();
        double grossWeight = entity.getGrossWeight();
        Double invGrossWeight = this.banQinWmInvLotLocService.getGrossWeightByLocCode(locCode, loc.getOrgId());
        if (loc.getMaxWeight() != null && loc.getMaxWeight() > 0) {
            if (grossWeight + invGrossWeight > loc.getMaxWeight()) {
                return null;
            }
        }
        return locCode;
    }

    /**
     * 库存校验：混商品混批次校验。
     *
     * @param locCode 库位编码
     * @param entity  上架任务
     */
    private boolean checkInvMixSkuMixLot(String locCode, BanQinWmTaskPaEntity entity) {
        String skuCode = entity.getSkuCode();
        String lotNum = entity.getLotNum();
        BanQinCdWhLoc loc = this.banQinCdWhLocService.findByLocCode(locCode, entity.getOrgId());
        String isMixSku = loc.getIsMixSku();
        String isMixLot = loc.getIsMixLot();

        long maxMixLot = loc.getMaxMixLot() == null ? 0 : loc.getMaxMixLot();
        long maxMixSku = loc.getMaxMixSku() == null ? 0 : loc.getMaxMixSku();

        if (WmsConstants.NO.equals(isMixSku) || (WmsConstants.YES.equals(isMixSku) && maxMixSku != 0)) {
            Map<String, List<String>> map = this.banQinWmInvLotLocService.getSkuQtyByLocCode(locCode, entity.getOrgId());
            List<String> sku = map.get("SKU");
            int skuqty = sku.size();
            if (WmsConstants.NO.equals(isMixSku) && skuqty > 0 && !sku.contains(skuCode)) {
                // 库位不允许混放商品
                return false;
            }
            if (!sku.contains(skuCode)) {
                skuqty = skuqty + 1;
            }
            if (WmsConstants.YES.equals(isMixSku) && maxMixSku > 0 && skuqty > maxMixSku) {
                // 库位超过最大的混放商品数
                return false;
            }
        }

        if (WmsConstants.NO.equals(isMixLot) || (WmsConstants.YES.equals(isMixLot) && maxMixLot != 0)) {
            Map<String, List<String>> map = this.banQinWmInvLotLocService.getLotNumQtyByLocCode(locCode, entity.getOrgId());
            List<String> lot = map.get("LOT");
            int lotqty = lot.size();
            if (WmsConstants.NO.equals(isMixLot) && lotqty > 0 && !lot.contains(lotNum)) {
                // 库位不允许混放批次
                return false;
            }
            if (!lot.contains(lotNum)) {
                lotqty = lotqty + 1;
            }
            if (WmsConstants.YES.equals(isMixLot) && maxMixLot > 0 && lotqty > maxMixLot) {
                // 库位超过最大的混放批次数
                return false;
            }
        }
        return true;
    }

    /**
     * 库位属性限制
     *
     * @param paDetail 上架规则明细
     */
    private String checkLocAttrRestrict(BanQinCdRulePaDetail paDetail) {
        StringBuffer category = this.checkCategoryRestrict(paDetail);
        StringBuffer useType = this.checkUseTypeRestrict(paDetail);
        StringBuffer abc = this.checkAbcRestrict(paDetail);
        return category.toString() + useType.toString() + abc.toString();
    }

    /**
     * 库位种类限制
     *
     * @param paDetail 上架规则明细
     */
    private StringBuffer checkCategoryRestrict(BanQinCdRulePaDetail paDetail) {
        StringBuffer categoryRestrictSql = new StringBuffer();
        if (WmsConstants.YES.equals(paDetail.getIsCategoryRestrict())) {
            String[] in = this.stringSplit(paDetail.getIncludeCategory());
            String[] ex = this.stringSplit(paDetail.getExcludeCategory());
            this.arrayToSql(categoryRestrictSql, in, "CATEGORY", "IN");
            this.arrayToSql(categoryRestrictSql, ex, "CATEGORY", "NOT IN");
        }
        return categoryRestrictSql;
    }

    /**
     * 库位使用类型限制
     *
     * @param paDetail 上架规则明细
     */
    private StringBuffer checkUseTypeRestrict(BanQinCdRulePaDetail paDetail) {
        StringBuffer useTypeRestrictSql = new StringBuffer();
        if (WmsConstants.YES.equals(paDetail.getIsUseTypeRestrict())) {
            String[] in = this.stringSplit(paDetail.getIncludeUseType());
            String[] ex = this.stringSplit(paDetail.getExcludeUseType());
            this.arrayToSql(useTypeRestrictSql, in, "LOC_USE_TYPE", "IN");
            this.arrayToSql(useTypeRestrictSql, ex, "LOC_USE_TYPE", "NOT IN");
        }
        return useTypeRestrictSql;
    }

    /**
     * 库位ABC限制
     *
     * @param paDetail 上架规则明细
     */
    private StringBuffer checkAbcRestrict(BanQinCdRulePaDetail paDetail) {
        StringBuffer abcRestrictSql = new StringBuffer();
        if (WmsConstants.YES.equals(paDetail.getIsAbcRestrict())) {
            String[] in = this.stringSplit(paDetail.getIncludeAbc());
            String[] ex = this.stringSplit(paDetail.getExcludeAbc());
            this.arrayToSql(abcRestrictSql, in, "ABC", "IN");
            this.arrayToSql(abcRestrictSql, ex, "ABC", "NOT IN");
        }
        return abcRestrictSql;
    }

    /**
     * 库位中字段限制使用数组IN 或者 NOT IN
     */
    private void arrayToSql(StringBuffer sql, String[] str, String name, String inOrNotIn) {
        if (str.length > 0) {
            sql.append(" and cwl.").append(name);
            sql.append(" ").append(inOrNotIn);
            sql.append(" (");
            for (String s : str) {
                sql.append("'").append(s).append("',");
            }
            sql.append("'****'");
            sql.append(") ");
        }
    }

    /**
     * 根据逗号拆分成数组
     */
    private String[] stringSplit(String str) {
        String[] array = new String[]{};
        if (StringUtils.isNotEmpty(str)) {
            array = str.split(",");
        }
        return array;
    }


    /**
     * 描述： 批量上架
     *
     * @param wmTaskPaEntities 上架任务
     */
    public ResultMessage inboundBatchPutaway(List<BanQinWmTaskPaEntity> wmTaskPaEntities) {
        ResultMessage msg = new ResultMessage();
        for (BanQinWmTaskPaEntity wmTaskPaEntity : wmTaskPaEntities) {
            BanQinWmTaskPaEntity taskPaEntity = new BanQinWmTaskPaEntity();
            BeanUtils.copyProperties(wmTaskPaEntity, taskPaEntity);
            try {
                msg = this.inboundPutaway(taskPaEntity);
                // banQinInboundOperationService.checkRcvAutoCloseAsn(wmTaskPaEntity.getOrderNo(), wmTaskPaEntity.getOrgId());
            } catch (WarehouseException e) {
                msg.setSuccess(false);
                msg.addMessage("上架ID[" + wmTaskPaEntity.getPaId() + "]行号[" + wmTaskPaEntity.getLineNo() + "]" + e.getMessage());
            }
        }
        if (StringUtils.isEmpty(msg.getMessage())) {
            msg.addMessage("操作成功");
        }
        return msg;
    }

    /**
     * 描述： 上架确认
     *
     * @param wmTaskPaEntity 上架任务，前台页面收集到的上架任务信息
     */
    @Transactional
    public ResultMessage inboundPutaway(BanQinWmTaskPaEntity wmTaskPaEntity) {
        ResultMessage msg = new ResultMessage();
        /*
         * 上架确认前校验
         * 1.校验上架任务状态，判断是否为新建
         * 2.校验目标数量不能超过任务数量
         * 3.上架库位不能和收货库位相同
         * 4.如果是部分上架时，一定需要上到不同的TraceID
         * 5.校验是否允许商品混放、是否允许批次混放
         */
        checkBeforePutaway(wmTaskPaEntity);
        /*
         * 更新库存记录，返回上架后的库存信息
         */
        // 实例化上架更新库存参数
        BanQinInventoryEntity fmEntity = new BanQinInventoryEntity();
        fmEntity.setAction(ActionCode.PUTAWAY);
        fmEntity.setTaskId(wmTaskPaEntity.getPaId());
        fmEntity.setOrderNo(wmTaskPaEntity.getOrderNo());
        fmEntity.setOwnerCode(wmTaskPaEntity.getOwnerCode());
        fmEntity.setSkuCode(wmTaskPaEntity.getSkuCode());
        fmEntity.setPackCode(wmTaskPaEntity.getPackCode());
        fmEntity.setUom(wmTaskPaEntity.getUom());
        fmEntity.setLotNum(wmTaskPaEntity.getLotNum());
        fmEntity.setTraceId(wmTaskPaEntity.getFmId());
        fmEntity.setLocCode(wmTaskPaEntity.getFmLoc());
        fmEntity.setQtyUom(wmTaskPaEntity.getQtyPaUom());
        fmEntity.setQtyEaOp(wmTaskPaEntity.getCurrentPaQtyEa());
        fmEntity.setOrgId(wmTaskPaEntity.getOrgId());

        BanQinInventoryEntity toEntity = new BanQinInventoryEntity();
        toEntity.setAction(ActionCode.PUTAWAY);
        toEntity.setTaskId(wmTaskPaEntity.getPaId());
        toEntity.setOrderNo(wmTaskPaEntity.getOrderNo());
        toEntity.setOwnerCode(wmTaskPaEntity.getOwnerCode());
        toEntity.setSkuCode(wmTaskPaEntity.getSkuCode());
        toEntity.setPackCode(wmTaskPaEntity.getPackCode());
        toEntity.setUom(wmTaskPaEntity.getUom());
        toEntity.setLotNum(wmTaskPaEntity.getLotNum());
        toEntity.setTraceId(wmTaskPaEntity.getToId());// 实际跟踪号
        toEntity.setLocCode(wmTaskPaEntity.getToLoc());// 实际库位
        toEntity.setQtyUom(wmTaskPaEntity.getQtyPaUom());
        toEntity.setQtyEaOp(wmTaskPaEntity.getCurrentPaQtyEa());
        toEntity.setPlanLocCode(wmTaskPaEntity.getSuggestLoc());// 计划库位
        toEntity.setPlanTraceId(wmTaskPaEntity.getFmId());// 计划跟踪号
        toEntity.setOrgId(wmTaskPaEntity.getOrgId());

        // 执行上架更新，更新收货库位待出数，计划库位待入数
        BanQinInventoryEntity updatedInv = banQinInventoryService.updateInventory(fmEntity, toEntity);
        // 库位为忽略ID时，更新完库存，跟踪号置*
        BanQinWmTaskPa wmTaskPaModel = new BanQinWmTaskPa();
        BeanUtils.copyProperties(wmTaskPaEntity, wmTaskPaModel);
        wmTaskPaModel.setToId(updatedInv.getTraceId());

        Double currentPaQtyEa = wmTaskPaEntity.getCurrentPaQtyEa();
        Double qtyPaEa = wmTaskPaEntity.getQtyPaEa();
        // 更新上架任务记录
        wmTaskPaModel.setQtyPaEa(currentPaQtyEa);
        wmTaskPaModel.setStatus(WmsCodeMaster.TSK_COMPLETE.getCode());
        if (StringUtils.isEmpty(wmTaskPaModel.getPaOp())) {
            wmTaskPaModel.setPaOp(UserUtils.getUser().getName());
        }
        if (null == wmTaskPaModel.getPaTime()) {
            wmTaskPaModel.setPaTime(new Date());
        }
        this.banQinWmTaskPaService.save(wmTaskPaModel);
        wmTaskPaModel = this.banQinWmTaskPaService.get(wmTaskPaModel.getId());

        // 部分上架时，新增一个上架任务
        if (currentPaQtyEa < qtyPaEa) {
            Integer cdprQuantity = banQinWmsCommonService.getPackageRelationAndQtyUom(wmTaskPaModel.getPackCode(), wmTaskPaModel.getUom(), wmTaskPaModel.getOrgId()).getCdprQuantity();
            Double newQtyPaEa = qtyPaEa - currentPaQtyEa;
            cdprQuantity = cdprQuantity == null || cdprQuantity == 0 ? 1 : cdprQuantity;

            BanQinWmTaskPa model = new BanQinWmTaskPa();
            BeanUtils.copyProperties(wmTaskPaModel, model);
            model.setId(null);
            model.setStatus(WmsCodeMaster.TSK_NEW.getCode());
            model.setLineNo(banQinWmTaskPaService.getNewPaLineNo(wmTaskPaEntity.getPaId(), wmTaskPaEntity.getOrgId()));
            model.setToId(wmTaskPaModel.getFmId());
            model.setToLoc(wmTaskPaModel.getSuggestLoc());
            model.setQtyPaEa(newQtyPaEa);
            model.setQtyPaUom(Math.ceil(newQtyPaEa / cdprQuantity));
            model.setPaOp(null);
            model.setPaTime(null);
            model.setPrintNum(0);
            banQinWmTaskPaService.save(model);
        }

        /*
         * 构造返回的参数
         */
        /*TaskPaReturnParam returnParam = new TaskPaReturnParam();
        returnParam.setTranId(updatedInv.getTranId());
        returnParam.setWmTaskPaModel(wmTaskPaModel);
        msg.setData(returnParam);*/
        BanQinWmTaskPaEntity entity = new BanQinWmTaskPaEntity();
        BeanUtils.copyProperties(wmTaskPaModel, entity);
        entity.setTransactionId(updatedInv.getTranId());
        msg.setSuccess(true);
        return msg;
    }

    /**
     * 描述： 上架确认前校验
     * 1.校验上架任务状态，判断是否为新建
     * 2.校验目标数量不能超过任务数量
     * 3.上架库位不能和收货库位相同
     * 4.如果是部分上架时，一定需要上到不同的TraceID
     * 5.校验是否允许商品混放、是否允许批次混放
     *
     * @param wmTaskPaEntity 上架任务
     */
    protected void checkBeforePutaway(BanQinWmTaskPaEntity wmTaskPaEntity) {
        BanQinWmTaskPa queryPaModel = banQinWmTaskPaService.getByPaIdAndLineNo(wmTaskPaEntity.getPaId(), wmTaskPaEntity.getLineNo(), wmTaskPaEntity.getOrgId());
        // 校验上架任务状态，判断是否为新建
        if (null == queryPaModel) {
            throw new WarehouseException("不存在");
        } else {
            if (WmsCodeMaster.TSK_COMPLETE.getCode().equals(queryPaModel.getStatus())) {
                throw new WarehouseException("已上架，不能操作");
            }
        }
        String asnNo = "";
        if (WmsCodeMaster.ORDER_QC.getCode().equals(wmTaskPaEntity.getOrderType())) {
            BanQinWmQcEntity wmQcEntity = banQinWmQcHeaderService.findEntityByQcNo(wmTaskPaEntity.getOrderNo(), wmTaskPaEntity.getOrgId());
            asnNo = wmQcEntity.getOrderNo();
        }
        if (WmsCodeMaster.ORDER_ASN.getCode().equals(wmTaskPaEntity.getOrderType())) {
            asnNo = wmTaskPaEntity.getOrderNo();
        }
        if (StringUtils.isNotEmpty(asnNo)) {
            BanQinWmAsnHeader wmAsnHeaderModel = banQinWmAsnHeaderService.getByAsnNo(asnNo, wmTaskPaEntity.getOrgId());
            if (null != wmAsnHeaderModel && WmsCodeMaster.ODHL_HOLD.getCode().equals(wmAsnHeaderModel.getHoldStatus())) {
                throw new WarehouseException("订单已冻结，不能操作");
            }
        }
        // 校验目标数量不能超过任务数量
        if (null == wmTaskPaEntity.getCurrentPaQtyEa() || wmTaskPaEntity.getCurrentPaQtyEa() == 0D) {
            throw new WarehouseException("上架数不能为0");
        }
        // 实际页面收集到的上架数>数据库查询出的计划上架数
        if (wmTaskPaEntity.getCurrentPaQtyEa() > queryPaModel.getQtyPaEa()) {
            throw new WarehouseException("不能超量上架");
        }
        // 上架库位不能和收货库位相同
        if (StringUtils.isEmpty(wmTaskPaEntity.getToLoc())) {
            throw new WarehouseException("上架库位不能为空");
        }
        if (wmTaskPaEntity.getToLoc().equals(wmTaskPaEntity.getFmLoc())) {
            throw new WarehouseException("上架库位不能和收货库位相同");
        }
        // 如果是部分上架并且库位不忽略跟踪号，一定需要上到不同的TraceID
        if (StringUtils.isEmpty(wmTaskPaEntity.getToId())) {
            throw new WarehouseException("上架跟踪号不能为空");
        }
        BanQinCdWhLoc locModel = banQinCdWhLocService.findByLocCode(wmTaskPaEntity.getToLoc(), wmTaskPaEntity.getOrgId());
        if (locModel == null) {
            throw new WarehouseException(wmTaskPaEntity.getToLoc() + "上架库位不存在");
        }
        if (WmsConstants.NO.equals(locModel.getIsLoseId())
                && !WmsConstants.TRACE_ID.equals(wmTaskPaEntity.getToId())
                && wmTaskPaEntity.getCurrentPaQtyEa() < queryPaModel.getQtyPaEa()
                && wmTaskPaEntity.getFmId().equals(wmTaskPaEntity.getToId())) {
            throw new WarehouseException("请选择新的跟踪号");
        }
        BanQinWmInvLotAtt wmInvLotAttModel = banQinWmInvLotAttService.getByLotNum(wmTaskPaEntity.getLotNum(), wmTaskPaEntity.getOrgId());
        if (WmsCodeMaster.QC_ATT_TO_QC.getCode().equals(wmInvLotAttModel.getLotAtt04())) {
            throw new WarehouseException("商品需要质检，不能操作");
        }
        // 校验跟踪号不能混商品、混批次
        /*if (!WmsConstants.TRACE_ID.equals(wmTaskPaEntity.getToId())) {
            List<WmInvLotLocModel> wmInvLotLocList = wmInvLotLocManager.getByTraceId(wmTaskPaEntity.getToId());
            for (WmInvLotLocModel wmInvLotLocModel : wmInvLotLocList) {
                if (!wmInvLotLocModel.getSkuCode().equals(wmTaskPaEntity.getSkuCode()) || !wmInvLotLocModel.getLotNum().equals(wmTaskPaEntity.getLotNum())) {
                    throw new WarehouseException("跟踪号" + wmTaskPaEntity.getToId() + "不能混商品、混批次");
                }
            }
        }*/
    }

//***********************************************************************取消上架***********************************************************************************************//

    /**
     * 描述： 批量删除上架任务
     *
     * @param wmTaskPaEntities 上架任务
     */
    public ResultMessage inboundBatchRemoveTaskPa(List<BanQinWmTaskPaEntity> wmTaskPaEntities) {
        ResultMessage msg = new ResultMessage();
        for (BanQinWmTaskPaEntity banQinWmTaskPaEntity : wmTaskPaEntities) {
            BanQinWmTaskPaEntity wmTaskPaEntity = new BanQinWmTaskPaEntity();
            BeanUtils.copyProperties(banQinWmTaskPaEntity, wmTaskPaEntity);
            try {
                this.inboundRemovePutaway(wmTaskPaEntity);
            } catch (WarehouseException e) {
                msg.addMessage("上架任务ID[" + wmTaskPaEntity.getPaId() + "]行号[" + wmTaskPaEntity.getLineNo() + "]" + e.getMessage());
            }
        }
        if (StringUtils.isEmpty(msg.getMessage())) {
            msg.addMessage("操作成功");
        }
        return msg;
    }

    /**
     * 描述： 批量删除上架任务(质检单)
     *
     * @param wmTaskPaEntities 上架任务
     */
    public ResultMessage inboundBatchRemoveTaskPaFmQc(List<BanQinWmTaskPaEntity> wmTaskPaEntities) {
        ResultMessage msg = new ResultMessage();
        for (BanQinWmTaskPaEntity banQinWmTaskPaEntity : wmTaskPaEntities) {
            BanQinWmTaskPaEntity wmTaskPaEntity = new BanQinWmTaskPaEntity();
            BeanUtils.copyProperties(banQinWmTaskPaEntity, wmTaskPaEntity);
            try {
                this.inboundRemovePutawayFmQc(wmTaskPaEntity);
            } catch (WarehouseException e) {
                msg.addMessage("上架ID" + wmTaskPaEntity.getPaId() + "行号" + wmTaskPaEntity.getLineNo() + e.getMessage());
            }
        }
        if (StringUtils.isEmpty(msg.getMessage())) {
            msg.addMessage("操作成功");
        }
        return msg;
    }

    /**
     * 描述： 删除上架任务 删除新建状态的上架任务，扣减推荐库位的上架待入数
     *
     * @param wmTaskPaEntity 上架任务
     */
    @Transactional
    public void inboundRemovePutaway(BanQinWmTaskPaEntity wmTaskPaEntity) {
        // ASN单冻结
        BanQinWmAsnHeader wmAsnHeaderModel = banQinWmAsnHeaderService.getByAsnNo(wmTaskPaEntity.getOrderNo(), wmTaskPaEntity.getOrgId());
        if (null != wmAsnHeaderModel && WmsCodeMaster.ODHL_HOLD.getCode().equals(wmAsnHeaderModel.getHoldStatus())) {
            throw new WarehouseException("订单已冻结，不能操作");
        }
        // 删除上架任务
        removePutaway(wmTaskPaEntity);
        // 如果paId不存在，但在收货明细里有存在，则需置空
        banQinWmAsnDetailReceiveService.updatePaIdNull(wmTaskPaEntity.getPaId(), wmTaskPaEntity.getOrgId());
    }

    /**
     * Description :删除上架任务(质检单)
     *
     * @param wmTaskPaEntity 上架任务
     */
    @Transactional
    public void inboundRemovePutawayFmQc(BanQinWmTaskPaEntity wmTaskPaEntity) {
        BanQinWmQcEntity wmQcEntity = banQinWmQcHeaderService.findEntityByQcNo(wmTaskPaEntity.getOrderNo(), wmTaskPaEntity.getOrgId());
        // ASN单冻结
        BanQinWmAsnHeader wmAsnHeaderModel = banQinWmAsnHeaderService.getByAsnNo(wmQcEntity.getOrderNo(), wmQcEntity.getOrgId());
        if (null != wmAsnHeaderModel && WmsCodeMaster.ODHL_HOLD.getCode().equals(wmAsnHeaderModel.getHoldStatus())) {
            throw new WarehouseException("订单已冻结，不能操作");
        }
        // 删除上架任务
        removePutaway(wmTaskPaEntity);
        // 如果paId不存在，但在质检明细里有存在，则需置空
        banQinWmQcDetailService.updateQcQuaPaIdNull(wmTaskPaEntity.getPaId(), wmTaskPaEntity.getOrgId());
        banQinWmQcDetailService.updateQcUnquaPaIdNull(wmTaskPaEntity.getPaId(), wmTaskPaEntity.getOrgId());
    }

    /**
     * 描述： 删除上架任务 删除新建状态的上架任务，扣减推荐库位的上架待入数
     *
     * @param wmTaskPaEntity 上架任务
     */
    @Transactional
    public void removePutaway(BanQinWmTaskPaEntity wmTaskPaEntity) {
        // 上架ID
        String paId = wmTaskPaEntity.getPaId();
        // 上架序号
        String lineNo = wmTaskPaEntity.getLineNo();

        BanQinWmTaskPa paModel = banQinWmTaskPaService.getByPaIdAndLineNo(paId, lineNo, wmTaskPaEntity.getOrgId());
        // 校验上架任务状态，判断是否为新建
        if (null == paModel) {
            throw new WarehouseException("不存在");
        } else if (!WmsCodeMaster.TSK_NEW.getCode().equals(paModel.getStatus())) {
            throw new WarehouseException("已上架，不能操作");
        }

        // 实例化上架更新库存参数
        BanQinInventoryEntity fmEntity = new BanQinInventoryEntity();
        fmEntity.setAction(ActionCode.CANCEL_PA_TASK);
        fmEntity.setOwnerCode(paModel.getOwnerCode());
        fmEntity.setSkuCode(paModel.getSkuCode());
        fmEntity.setLotNum(paModel.getLotNum());
        fmEntity.setTraceId(paModel.getToId());
        fmEntity.setLocCode(paModel.getToLoc());
        fmEntity.setQtyEaOp(paModel.getQtyPaEa());
        fmEntity.setPlanLocCode(paModel.getSuggestLoc());
        fmEntity.setOrgId(paModel.getOrgId());

        BanQinInventoryEntity toEntity = new BanQinInventoryEntity();
        toEntity.setAction(ActionCode.CANCEL_PA_TASK);
        toEntity.setOwnerCode(paModel.getOwnerCode());
        toEntity.setSkuCode(paModel.getSkuCode());
        toEntity.setLotNum(paModel.getLotNum());
        toEntity.setTraceId(paModel.getFmId());
        toEntity.setLocCode(paModel.getFmLoc());
        toEntity.setQtyEaOp(paModel.getQtyPaEa());
        toEntity.setOrgId(paModel.getOrgId());

        // 执行上架更新，更新收货库位待出数，计划库位待入数
        banQinInventoryService.updateInventory(fmEntity, toEntity);
        // 删除上架任务
        banQinWmTaskPaService.delete(paModel);
    }

}
