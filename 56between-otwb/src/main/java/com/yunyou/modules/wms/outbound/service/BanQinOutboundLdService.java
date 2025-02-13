package com.yunyou.modules.wms.outbound.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.modules.sys.utils.SysControlParamsUtils;
import com.yunyou.modules.wms.common.entity.ControlParamCode;
import com.yunyou.modules.wms.common.entity.WarehouseException;
import com.yunyou.modules.wms.common.entity.WmsCodeMaster;
import com.yunyou.modules.wms.common.entity.WmsConstants;
import com.yunyou.modules.wms.common.service.BanQinWmsCommonService;
import com.yunyou.modules.wms.outbound.entity.BanQinWmLdDetail;
import com.yunyou.modules.wms.outbound.entity.BanQinWmLdDetailEntity;
import com.yunyou.modules.wms.outbound.entity.BanQinWmLdEntity;
import com.yunyou.modules.wms.outbound.entity.BanQinWmLdHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * 装车单操作方法类
 *
 * @author WMJ
 * @version 2019/02/20
 */
@Service
public class BanQinOutboundLdService {
    // 装车明细
    @Autowired
    protected BanQinWmLdDetailService wmLdDetailService;
    // 装车单
    @Autowired
    protected BanQinWmLdHeaderService wmLdHeaderService;
    // 批量装载确认
    @Autowired
    protected BanQinOutboundBatchLoadingAction outboundBatchLoading;
    // 装载确认
    @Autowired
    protected BanQinOutboundLoadingService outboundLoading;
    // 发货
    @Autowired
    protected BanQinOutboundBatchShipmentAction outboundBatchShipmentAction;
    // 发运确认转交接，取消交接，取消发货
    @Autowired
    protected BanQinOutboundLdDeliveryService outboundLdDeliveryService;
    // 公共接口
    @Autowired
    protected BanQinWmsCommonService wmCommon;

    /**
     * 根据装车单号删除装车单
     * @return
     */
    public ResultMessage removeByLdNo(String[] ldNos, String orgId) {
        ResultMessage msg = new ResultMessage();
        try {
            wmLdHeaderService.removeByLdNo(ldNos, orgId);
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
     * 根据装车单号获取装车实体对象
     * @param ldNo
     * @return
     */
    public ResultMessage getEntityByLdNo(String ldNo, String orgId) {
        ResultMessage msg = new ResultMessage();
        BanQinWmLdEntity ldEntity = wmLdHeaderService.getEntityByLdNo(ldNo, orgId);
        msg.addMessage("操作成功");
        msg.setSuccess(true);
        msg.setData(ldEntity);
        return msg;
    }

    /**
     * 根据装车单号交接确认
     * @param ldNos
     * @return
     */
    public ResultMessage deliveryByLdNo(String[] ldNos, String orgId) {
        ResultMessage msg = new ResultMessage();
        for (String ldNo : ldNos) {
            try {
                wmLdHeaderService.deliveryByLdNo(ldNo, orgId);
            } catch (WarehouseException e) {
                msg.addMessage(e.getMessage());
                msg.setSuccess(false);
            }
        }
        msg.addMessage("操作成功");
        msg.setSuccess(true);
        return msg;
    }

    /**
     * 装载BY装车实体对象ENTITY
     * @param ldDetailEntitys
     * @return
     */
    public ResultMessage outboundBatchLoading(List<BanQinWmLdDetailEntity> ldDetailEntitys) {
        return outboundBatchLoading.outboundBatchLoading(ldDetailEntitys);
    }

    /**
     * 装载BY装车明细Model
     * @param ldDetailModels
     * @return
     */
    public ResultMessage outboundLoadingByLdDetail(List<BanQinWmLdDetail> ldDetailModels) {
        return outboundBatchLoading.outboundBatchLoadingByLdDetails(ldDetailModels);
    }

    /**
     * 装载 by订单号SO
     * @param ldNo
     * @param soNos
     * @return
     */
    public ResultMessage outboundLoadingBySoNo(String ldNo, String[] soNos, String orgId) {
        return outboundBatchLoading.outboundBatchLoadingBySoNo(ldNo, soNos, orgId);
    }

    /**
     * 装载 by箱号TraceId
     * @param ldNo
     * @param traceIds
     * @return
     */
    public ResultMessage outboundLoadingByTraceId(String ldNo, String[] traceIds, String orgId) {
        return outboundBatchLoading.outboundBatchLoadingByTraceId(ldNo, traceIds, orgId);
    }

    /**
     * 装载 by装车单号LdNo
     * @param ldNo
     * @return
     */
    public ResultMessage outboundLoadingByLdNo(String ldNo, String orgId) {
        return outboundLoading.outboundLoadingByLdNo(ldNo, orgId);
    }

    /**
     * 发运by装车单号LdNo
     * @param ldNos
     * @return
     */
    public ResultMessage outboundShipmentByLdNo(String[] ldNos, String orgId) {
        ResultMessage msg = new ResultMessage();
        String loadAllocStatus = SysControlParamsUtils.getValue(ControlParamCode.LOAD_ALLOW_STATUS.getCode(), orgId);
        // 如果拣货后生成装车单，才可以发运确认
        if (WmsConstants.LOAD_ALLOW_STATUS_SP.equals(loadAllocStatus)) {
            msg.setSuccess(false);
            msg.addMessage("系统控制参数不是拣货后装车，不能操作！");
            return msg;
        }
        for (String ldNo : ldNos) {
            try {
                ResultMessage rtmsg = outboundLdDeliveryService.outboundShipment(ldNo, orgId);
                if (!rtmsg.isSuccess()) {
                    msg.addMessage(rtmsg.getMessage());
                    msg.setSuccess(false);
                }
            } catch (WarehouseException e) {
                msg.addMessage(e.getMessage());
                msg.setSuccess(false);
            }
        }
        if (StringUtils.isEmpty(msg.getMessage())) {
            msg.setSuccess(true);
            msg.addMessage("操作成功");
        }
        return msg;
    }

    /**
     * 保存装车单单头
     * @param wmLdEntity
     * @return
     */
    @Transactional
    public ResultMessage saveWmLdHeader(BanQinWmLdEntity wmLdEntity) {
        ResultMessage msg = new ResultMessage();
        BanQinWmLdHeader entity;
        try {
            entity = wmLdHeaderService.saveWmLdEntity(wmLdEntity);
        } catch (WarehouseException e) {
            msg.addMessage(e.getMessage());
            msg.setSuccess(false);
            return msg;
        }

        msg.addMessage("操作成功");
        msg.setData(entity);
        msg.setSuccess(true);
        return msg;
    }

    /**
     * 根据装车单号取消发货
     * @param ldNos
     * @return
     */
    public ResultMessage cancelShipmentByLdNo(String[] ldNos, String orgId) {
        ResultMessage msg = new ResultMessage();
        String loadAllocStatus = SysControlParamsUtils.getValue(ControlParamCode.LOAD_ALLOW_STATUS.getCode(), orgId);
        // 如果拣货后生成装车单，才可以取消发货
        if (WmsConstants.LOAD_ALLOW_STATUS_SP.equals(loadAllocStatus)) {
            msg.setSuccess(false);
            msg.addMessage("控制参数不是拣货后装车，不能操作");
            return msg;
        }
        for (String ldNo : ldNos) {
            try {
                ResultMessage rtmsg = outboundLdDeliveryService.outboundCancelShipment(ldNo, orgId);
                if (!rtmsg.isSuccess()) {
                    msg.addMessage(rtmsg.getMessage());
                    msg.setSuccess(false);
                }
            } catch (WarehouseException e) {
                msg.addMessage(e.getMessage());
                msg.setSuccess(false);
            }
        }
        if (StringUtils.isEmpty(msg.getMessage())) {
            msg.setSuccess(true);
            msg.addMessage("操作成功");
        }
        return msg;
    }

    /**
     * 根据装车单号取消交接
     * @param ldNos
     * @return
     */
    public ResultMessage cancelDeliveryByLdNo(String[] ldNos, String orgId) {
        ResultMessage msg = new ResultMessage();
        for (String ldNo : ldNos) {
            try {
                // 取消交接
                wmLdHeaderService.cancelDeliveryByLdNo(ldNo, orgId);
            } catch (WarehouseException e) {
                msg.addMessage(e.getMessage());
                msg.setSuccess(false);
                return msg;
            }
        }
        if (StringUtils.isEmpty(msg.getMessage())) {
            msg.setSuccess(true);
            msg.addMessage("操作成功");
        }
        return msg;
    }

    /**
     * 根据装车单号和行号取消装载确认
     * @param ldNo
     * @param lineNos
     * @return
     */
    public ResultMessage cancelByLdNoAndLineNo(String ldNo, String[] lineNos, String orgId) {
        ResultMessage msg = new ResultMessage();
        try {
            wmLdDetailService.cancelByLdNoAndLineNo(ldNo, lineNos, orgId);
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
     * 根据装车单号和行号删除装载明细
     * @param ldNo
     * @param lineNos
     * @return
     */
    public ResultMessage removeByLdNoAndLineNo(String ldNo, String[] lineNos, String orgId) {
        ResultMessage msg = new ResultMessage();
        try {
            wmLdDetailService.removeByLdNoAndLineNo(ldNo, lineNos, orgId);
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
     * 根据装车单号和SO号删除装载明细
     * @param ldNo
     * @param soNos
     * @return
     */
    public ResultMessage removeByLdNoAndSoNo(String ldNo, String[] soNos, String orgId) {
        ResultMessage msg = new ResultMessage();
        try {
            // 批量校验
            // 符合条件的结果
            List<String> rsSoNos = wmLdDetailService.checkIsGenLdBySoNos(Arrays.asList(soNos), WmsCodeMaster.LD_NEW.getCode(), orgId);
            // 获取集合差，不符合条件的结果
            Object[] errorSoNos = wmCommon.minus(soNos, rsSoNos.toArray());
            for (Object soNo : errorSoNos) {
                msg.addMessage(soNo + "的明细做了装载确认，不能操作");
            }
            wmLdDetailService.removeByLdNoAndSoNo(ldNo, rsSoNos.toArray(new String[]{}), orgId);
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
     * 根据装车单号和TraceId箱号删除装载明细
     * @param ldNo
     * @param traceIds
     * @return
     */
    public ResultMessage removeByLdNoAndTraceId(String ldNo, String[] traceIds, String orgId) {
        ResultMessage msg = new ResultMessage();
        try {
            // 批量校验
            // 符合条件的结果
            List<String> rsTraceIds = wmLdDetailService.checkIsGenLdByToIds(ldNo, Arrays.asList(traceIds), WmsCodeMaster.LD_NEW.getCode(), orgId);
            // 获取集合差，不符合条件的结果
            Object[] errorTraceIds = wmCommon.minus(traceIds, rsTraceIds.toArray());
            for (Object traceId : errorTraceIds) {
                msg.addMessage(traceId + "的明细做了装载确认，不能操作 ");
            }
            wmLdDetailService.removeByLdNoAndTraceId(ldNo, traceIds, orgId);
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
     * 未装车明细挑选弹窗口-确认
     * @param ldNo
     * @param allocIds
     * @return
     */
    public ResultMessage addLdDetailByAllocId(String ldNo, String[] allocIds, String orgId) {
        ResultMessage msg = new ResultMessage();
        try {
            wmLdDetailService.addLdDetailByAllocId(ldNo, allocIds, orgId);
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
     * 订单挑选弹窗口-确认
     * @param ldNo
     * @param soNos
     * @return
     */
    public ResultMessage addLdDetailBySoNo(String ldNo, String[] soNos, String orgId) {
        ResultMessage msg = new ResultMessage();
        try {
            wmLdDetailService.addLdDetailBySoNo(ldNo, soNos, orgId);
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
     * 包裹扫描弹窗口-确认
     * @param ldNo
     * @param traceIds
     * @return
     */
    public ResultMessage addLdDetailByTraceId(String ldNo, String[] traceIds, String orgId) {
        ResultMessage msg = new ResultMessage();
        try {
            wmLdDetailService.addLdDetailByTraceId(ldNo, traceIds, orgId);
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
     * 包裹扫描弹窗口-扫描包裹回车
     * @param traceId
     * @param wmLdEntity
     * @return
     */
    public ResultMessage enterByTraceId(String traceId, BanQinWmLdEntity wmLdEntity) {
        ResultMessage msg = new ResultMessage();
        try {
            msg.setData(wmLdDetailService.getTraceByTraceId(traceId, wmLdEntity));
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
     * 未装车完成的装车明细重新生成一个装车单
     * @param wmLdEntity
     * @param ldDetails
     * @return
     */
    public ResultMessage newLd(BanQinWmLdEntity wmLdEntity, List<BanQinWmLdDetailEntity> ldDetails) {
        ResultMessage msg = new ResultMessage();
        try {
            msg.setData(wmLdHeaderService.createNewWmLdEntity(wmLdEntity, ldDetails));
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
     * 根据装车单号关闭装车单
     * @param ldNos
     * @return
     */
    public ResultMessage closeOrder(String[] ldNos, String orgId) {
        ResultMessage msg = new ResultMessage();
        for (String ldNo : ldNos) {
            try {
                // 装车单关闭
                msg = wmLdHeaderService.closeLdOrder(ldNo, orgId);
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

}
