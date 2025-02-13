package com.yunyou.modules.wms.rf.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.modules.sys.entity.User;
import com.yunyou.modules.sys.utils.UserUtils;
import com.yunyou.modules.wms.common.entity.WmsCodeMaster;
import com.yunyou.modules.wms.inbound.entity.BanQinWmAsnDetailReceive;
import com.yunyou.modules.wms.inbound.entity.BanQinWmAsnDetailReceiveEntity;
import com.yunyou.modules.wms.inbound.service.BanQinInboundPaOperationService;
import com.yunyou.modules.wms.inbound.service.BanQinInboundPutawayService;
import com.yunyou.modules.wms.inbound.service.BanQinWmAsnDetailReceiveService;
import com.yunyou.modules.wms.rf.entity.WMSRF_PA_QueryPutAwayTaskByTaskNo_Request;
import com.yunyou.modules.wms.rf.entity.WMSRF_PA_QueryPutAwayTaskByTaskNo_Response;
import com.yunyou.modules.wms.rf.entity.WMSRF_PA_QueryPutAwayTaskByTraceID_Request;
import com.yunyou.modules.wms.rf.entity.WMSRF_PA_SavePutAwayByTask_Request;
import com.yunyou.modules.wms.task.entity.BanQinWmTaskPaEntity;
import com.yunyou.modules.wms.task.service.BanQinWmTaskPaService;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 上架确认
 * @author WMJ
 * @version 2019/07/09
 */
@Service
public class RfPutawayService {
    @Autowired
    private BanQinWmAsnDetailReceiveService wmAsnDetailReceiveService;
    @Autowired
    private BanQinWmTaskPaService wmTaskPaService;
    @Autowired
    private BanQinInboundPutawayService inboundPutawayService;
    @Autowired
    private BanQinInboundPaOperationService inboundBatchCreateTaskPa;
    @Autowired
    private RfPackageConfigService rfPackageConfigService;

    public ResultMessage queryPutAwayTaskByTraceId(WMSRF_PA_QueryPutAwayTaskByTraceID_Request request) {
        ResultMessage msg = new ResultMessage();
        User user = UserUtils.getUser();
        String orgId = user.getOffice().getId();
        // 1.根据托盘号，判断是否产生上架任务
        List<BanQinWmAsnDetailReceive> detailReceives = wmAsnDetailReceiveService.rfPaCheckCreatePATaskByToIDQuery(request.getToId(), orgId);
        if (CollectionUtil.isNotEmpty(detailReceives)) {
            // 如果存在上架任务ID，则直接调用WMSRF_PA_QueryPutAwayTaskByTaskNo
            if (null != detailReceives.get(0).getPaId()) {
                List<WMSRF_PA_QueryPutAwayTaskByTaskNo_Response> taskResponse = Lists.newArrayList();
                for (BanQinWmAsnDetailReceive detailReceive : detailReceives) {
                    WMSRF_PA_QueryPutAwayTaskByTaskNo_Request paIdRequest = new WMSRF_PA_QueryPutAwayTaskByTaskNo_Request();
                    paIdRequest.setPaId(detailReceive.getPaId());
                    paIdRequest.setUserId(request.getUserId());
                    ResultMessage resultMessage = this.queryPutAwayTaskByTaskNo(paIdRequest);
                    if (null != resultMessage.getData()) {
                        taskResponse.addAll((List<WMSRF_PA_QueryPutAwayTaskByTaskNo_Response>) resultMessage.getData());
                    }
                }
                if (taskResponse.size() > 0) {
                    msg.setData(taskResponse);
                } else {
                    msg.setSuccess(false);
                    msg.setMessage("查询结果为空");
                }
                return msg;
            }
            // 一个跟踪号存在多个上架任务
            // 不存在上架任务，且商品配置为“PA”,则先产生上架任务
            if (null != detailReceives.get(0).getReserveCode() && WmsCodeMaster.RESERVE_PA.getCode().equals(detailReceives.get(0).getReserveCode())) {
                // 1 、查找对应收货明细的 entitys 对象
                List<BanQinWmAsnDetailReceiveEntity> wmAsnDetailReceiveEntitys = Lists.newArrayList();
                BanQinWmAsnDetailReceiveEntity entity = wmAsnDetailReceiveService.getEntityByAsnNoAndLineNo(detailReceives.get(0).getAsnNo(), detailReceives.get(0).getLineNo(), detailReceives.get(0).getOrgId());
                wmAsnDetailReceiveEntitys.add(entity);
                // 2、存在明细，调用产生上架任务接口，生成上架任务
                ResultMessage message = inboundBatchCreateTaskPa.inboundBatchCreateTaskPa(wmAsnDetailReceiveEntitys);
                if (message.isSuccess()) {
                    // 3、查找对应的上架任务明细并返回
                    entity = wmAsnDetailReceiveService.getEntityByAsnNoAndLineNo(detailReceives.get(0).getAsnNo(), detailReceives.get(0).getLineNo(), detailReceives.get(0).getOrgId());
                    if (entity != null && entity.getPaId() != null) {
                        WMSRF_PA_QueryPutAwayTaskByTaskNo_Request paIdRequest = new WMSRF_PA_QueryPutAwayTaskByTaskNo_Request();
                        paIdRequest.setPaId(entity.getPaId());
                        paIdRequest.setUserId(request.getUserId());
                        return this.queryPutAwayTaskByTaskNo(paIdRequest);
                    } else {
                        msg.setSuccess(false);
                        msg.setMessage("查询结果为空");
                    }
                } else {
                    msg.setSuccess(false);
                    msg.setMessage(message.getMessage());
                }
            } else {
                msg.setSuccess(false);
                msg.setMessage("查询结果为空");
            }
        } else {
            msg.setSuccess(false);
            msg.setMessage("查询结果为空");
        }

        return msg;
    }

    public ResultMessage queryPutAwayTaskByTaskNo(WMSRF_PA_QueryPutAwayTaskByTaskNo_Request request) {
        ResultMessage msg = new ResultMessage();
        User user = UserUtils.getUser();
        List<BanQinWmTaskPaEntity> items = wmTaskPaService.rfPaGetPATaskByTaskNoQuery(request.getPaId(), request.getOrderNo(), request.getZoneCode(), user.getOffice().getId());
        List<WMSRF_PA_QueryPutAwayTaskByTaskNo_Response> result = Lists.newArrayList();
        if (CollectionUtil.isNotEmpty(items)) {
            for (BanQinWmTaskPaEntity item : items) {
                WMSRF_PA_QueryPutAwayTaskByTaskNo_Response response = new WMSRF_PA_QueryPutAwayTaskByTaskNo_Response();
                BeanUtils.copyProperties(item, response);
                // 获取包装配置
                response.setPackageConfigs(rfPackageConfigService.getPackageConfigs(item.getPackCode(), item.getOrgId()));
                result.add(response);
            }
            msg.setData(result);
        } else {
            msg.setSuccess(false);
            msg.setMessage("查询结果为空");
        }
        return msg;
    }

    @Transactional
    public ResultMessage savePutAwayByTask(WMSRF_PA_SavePutAwayByTask_Request request) {
        ResultMessage msg = new ResultMessage();
        User user = UserUtils.getUser();
        BanQinWmTaskPaEntity wmTaskPaEntity = wmTaskPaService.getEntityByPaIdAndLineNo(request.getPaId(), request.getLineNo(), user.getOffice().getId());
        if (null == wmTaskPaEntity) {
            msg.setSuccess(false);
            msg.setMessage(request.getPaId() + "不存在");
            return msg;
        }
        if (!wmTaskPaEntity.getStatus().equals(WmsCodeMaster.TSK_NEW.getCode())) {
            msg.setSuccess(false);
            msg.setMessage("不是创建状态，不能操作");
            return msg;
        }
        wmTaskPaEntity.setCurrentPaQtyEa(request.getCurrentPaQtyEa());
        wmTaskPaEntity.setUomDesc(request.getUomDesc());
        wmTaskPaEntity.setPackDesc(request.getPackDesc());
        wmTaskPaEntity.setToId(request.getToId());
        wmTaskPaEntity.setToLoc(request.getToLoc());
        wmTaskPaEntity.setPaOp(user.getLoginName());
        wmTaskPaEntity.setPaTime(new Date());
        wmTaskPaEntity.setOwnerName(request.getOwnerName());
        wmTaskPaEntity.setSkuName(request.getSkuName());
        inboundPutawayService.inboundPutaway(wmTaskPaEntity);
        msg.setMessage("操作成功");
        return msg;
    }

}
