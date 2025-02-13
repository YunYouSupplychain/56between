package com.yunyou.modules.wms.kit.service;

import com.google.common.collect.Lists;
import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.modules.wms.basicdata.service.BanQinCdWhLocService;
import com.yunyou.modules.wms.common.entity.WarehouseException;
import com.yunyou.modules.wms.common.entity.WmsCodeMaster;
import com.yunyou.modules.wms.common.entity.WmsConstants;
import com.yunyou.modules.wms.common.service.BanQinWmsCommonService;
import com.yunyou.modules.wms.kit.entity.BanQinWmKitResultDetail;
import com.yunyou.modules.wms.kit.entity.extend.BanQinWmKitResultDetailEntity;
import com.yunyou.modules.wms.kit.entity.extend.BanQinWmTaskKitEntity;
import com.yunyou.modules.wms.task.entity.BanQinWmTaskPa;
import com.yunyou.modules.wms.task.entity.BanQinWmTaskPaEntity;
import com.yunyou.modules.wms.task.service.BanQinWmTaskPaService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 描述：加工管理-生成上架任务
 * <p>
 * create by Jianhua on 2019/8/25
 */
@Service
public class BanQinKitBatchCreateTaskPaService {
    @Autowired
    protected BanQinWmTaskPaService banQinWmTaskPaService;
    @Autowired
    protected BanQinKitCreateTaskPaService banQinKitCreateTaskPaService;
    @Autowired
    protected BanQinCdWhLocService banQinCdWhLocService;
    @Autowired
    protected BanQinWmsCommonService wmCommon;
    @Autowired
    protected BanQinWmKitResultDetailService banQinWmKitResultDetailService;
    public Object locker = new Object();

    /**
     * 描述：手动生成上架任务
     * <p>
     * create by Jianhua on 2019/8/25
     */
    public ResultMessage kitBatchCreateTaskPa(List<BanQinWmKitResultDetailEntity> resultEntitys) {
        ResultMessage msg = new ResultMessage();
        // 收货上架参数：生成上架任务是否进行单进程操作（Y：单进程；N：不是单进程）
        String RCV_PA_TASK_QUEUE = WmsConstants.YES/*wmCommon.getSysControlParam(ControlParamCode.RCV_PA_TASK_QUEUE.getCode())*/;
        msg.setSuccess(false);
        Map<String, List<BanQinWmKitResultDetailEntity>> map = new HashMap<>();
        List<BanQinWmKitResultDetailEntity> resultGroup;
        // 循环加工结果
        for (BanQinWmKitResultDetailEntity resultEntity : resultEntitys) {
            // 获取加工结果
            BanQinWmKitResultDetailEntity checkEntity = banQinWmKitResultDetailService.getEntityByKitNoAndKitLineNo(resultEntity.getKitNo(), resultEntity.getKitLineNo(), resultEntity.getOrgId());
            // 校验状态
            if (!WmsCodeMaster.KIT_FULL_KIT.getCode().equals(checkEntity.getStatus())) {
                // 加工行号{0}未加工，不能操作
                msg.addMessage("加工行号" + resultEntity.getKitLineNo() + "未加工，不能操作");
                continue;
            }
            if (StringUtils.isNotEmpty(resultEntity.getPaId())) {
                // 加工行号{0}已经生成上架任务，不能操作
                msg.addMessage("加工行号" + resultEntity.getKitLineNo() + "已经生成上架任务，不能操作");
                continue;
            }
            // 不为空，不能生成上架任务
            if (StringUtils.isNotEmpty(resultEntity.getReserveCode())) {
                // 上架库位指定规则错误
                msg.addMessage("订单号" + resultEntity.getKitNo() + "行号" + resultEntity.getKitLineNo() + "上架库位指定规则错误");
                continue;
            }
            // 1.忽略跟踪号，一个加工明细生成一个上架任务
            if (WmsConstants.TRACE_ID.equals(resultEntity.getKitTraceId())) {
                BanQinWmTaskPaEntity entity = new BanQinWmTaskPaEntity();
                entity.setOrderNo(resultEntity.getKitNo());
                entity.setOrderType(WmsCodeMaster.ORDER_KIT.getCode());// 加工单
                entity.setOwnerCode(resultEntity.getOwnerCode());
                entity.setSkuCode(resultEntity.getParentSkuCode());
                entity.setPackCode(resultEntity.getPackCode());
                entity.setUom(resultEntity.getUom());
                entity.setLotNum(resultEntity.getLotNum());
                entity.setFromId(resultEntity.getKitTraceId());
                entity.setFromLoc(resultEntity.getKitLoc());
                entity.setNewPaRule(resultEntity.getPaRule());
                entity.setNewReserveCode(resultEntity.getReserveCode());
                entity.setPaIdRcv(resultEntity.getPaId());
                entity.setCubic(resultEntity.getCubic());
                entity.setGrossWeight(resultEntity.getGrossWeight());
                entity.setOrgId(resultEntity.getOrgId());
                if (StringUtils.isNotEmpty(resultEntity.getPaId())) {
                    Double qtyPa = banQinWmTaskPaService.getQtyPaByPaId(resultEntity.getPaId(), resultEntity.getOrgId());// 已生成上架数
                    entity.setQtyEa(resultEntity.getQtyCompleteEa() - qtyPa);
                } else {
                    entity.setQtyEa(resultEntity.getQtyCompleteEa());
                }
                if (entity.getQtyEa() > 0D) {
                    try {
                        // 生成上架任务单进程控制
                        if (WmsConstants.YES.equals(RCV_PA_TASK_QUEUE)) {
                            synchronized (locker) {
                                msg = banQinKitCreateTaskPaService.kitCreateTaskPa(entity);
                            }
                        } else {
                            msg = banQinKitCreateTaskPaService.kitCreateTaskPa(entity);
                        }
                        if (msg.isSuccess()) {
                            BanQinWmTaskPa wmTaskPa = (BanQinWmTaskPa) msg.getData();
                            String paId = wmTaskPa.getPaId();
                            resultEntity.setPaId(paId);
                            BanQinWmKitResultDetail resultModel = new BanQinWmKitResultDetail();
                            BeanUtils.copyProperties(resultEntity, resultModel);
                            banQinWmKitResultDetailService.save(resultModel);
                        }
                    } catch (WarehouseException e) {
                        msg.addMessage("订单号" + resultEntity.getKitNo() + "行号" + resultEntity.getKitLineNo() + e.getMessage());
                    }
                }
            } else {
                // 2.如果存在跟踪号，则分组汇总
                String key = resultEntity.getKitTraceId();
                if (!map.containsKey(key)) {
                    resultGroup = Lists.newArrayList();
                } else {
                    resultGroup = map.get(key);
                }
                resultGroup.add(resultEntity);
                map.put(key, resultGroup);
            }
        }

        // 存在跟踪号时，分组构造生成上架任务的参数
        Set<String> keySet = map.keySet();
        for (String key : keySet) {
            // 同个traceId在不同收货明细行，如果收货信息有不同，则只取第一条
            BanQinWmKitResultDetailEntity resultEntity = map.get(key).get(0);
            BanQinWmTaskPaEntity entity = new BanQinWmTaskPaEntity();
            entity.setOrderNo(resultEntity.getKitNo());
            entity.setOrderType(WmsCodeMaster.ORDER_KIT.getCode());// 加工单
            entity.setOwnerCode(resultEntity.getOwnerCode());
            entity.setSkuCode(resultEntity.getParentSkuCode());
            entity.setPackCode(resultEntity.getPackCode());
            entity.setUom(resultEntity.getUom());
            entity.setLotNum(resultEntity.getLotNum());
            entity.setFromId(resultEntity.getKitTraceId());
            entity.setFromLoc(resultEntity.getKitLoc());
            entity.setNewPaRule(resultEntity.getPaRule());
            entity.setPaIdRcv(resultEntity.getPaId());
            entity.setNewReserveCode(resultEntity.getReserveCode());
            entity.setCubic(resultEntity.getCubic());
            entity.setGrossWeight(resultEntity.getGrossWeight());
            entity.setOrgId(resultEntity.getOrgId());
            try {
                // 生成上架任务单进程控制
                if (WmsConstants.YES.equals(RCV_PA_TASK_QUEUE)) {
                    synchronized (locker) {
                        msg = banQinKitCreateTaskPaService.kitCreateTaskPa(entity);
                    }
                } else {
                    msg = banQinKitCreateTaskPaService.kitCreateTaskPa(entity);
                }
                if (msg.isSuccess()) {
                    BanQinWmTaskPa wmTaskPa = (BanQinWmTaskPa) msg.getData();
                    String paId = wmTaskPa.getPaId();
                    // 更新相同跟踪号的加工结果明细行的PaID
                    List<BanQinWmKitResultDetail> resultModels = banQinWmKitResultDetailService.getByKitNoAndTraceId(resultEntity.getKitNo(), key, WmsCodeMaster.KIT_FULL_KIT.getCode(), resultEntity.getOrgId());
                    for (BanQinWmKitResultDetail resultModel : resultModels) {
                        resultModel.setReserveCode(entity.getNewReserveCode());
                        resultModel.setPaRule(entity.getNewPaRule());
                        resultModel.setPaId(paId);
                        banQinWmKitResultDetailService.save(resultModel);
                    }
                }
            } catch (WarehouseException e) {
                msg.addMessage("订单号" + resultEntity.getKitNo() + "行号" + resultEntity.getKitLineNo() + e.getMessage());
            }
        }
        if (StringUtils.isEmpty(msg.getMessage())) {
            msg.setSuccess(true);
            msg.addMessage("操作成功");
        }
        return msg;
    }

    /**
     * 描述：生成上架任务 - 取消分配日志
     * <p>
     * create by Jianhua on 2019/8/25
     */
    public ResultMessage kitBatchCreateTaskPaByDelAlloc(List<BanQinWmTaskKitEntity> taskKitEntitys) {
        ResultMessage msg = new ResultMessage();
        for (BanQinWmTaskKitEntity taskKitEntity : taskKitEntitys) {
            try {
                banQinKitCreateTaskPaService.kitCreateTaskPaByAlloc(taskKitEntity);
            } catch (WarehouseException e) {
                msg.addMessage(e.getMessage());
                msg.setSuccess(false);
            }
        }
        // 消息处理
        if (StringUtils.isBlank(msg.getMessage())) {
            msg.addMessage("操作成功");
            msg.setSuccess(true);
        }
        return msg;
    }
}
