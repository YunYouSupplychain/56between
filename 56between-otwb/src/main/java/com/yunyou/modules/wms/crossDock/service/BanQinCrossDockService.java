package com.yunyou.modules.wms.crossDock.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.modules.wms.crossDock.entity.*;
import com.yunyou.modules.wms.crossDock.mapper.BanQinWmCrossDockMapper;
import com.yunyou.modules.wms.inbound.entity.BanQinWmAsnDetailReceive;
import com.yunyou.modules.wms.inbound.entity.BanQinWmAsnDetailReceiveEntity;
import com.yunyou.modules.wms.inbound.service.BanQinWmAsnDetailReceiveService;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoAllocEntity;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoDetailEntity;
import com.yunyou.modules.wms.outbound.service.BanQinWmSoAllocService;
import com.yunyou.modules.wms.outbound.service.BanQinWmSoDetailService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 越库
 * @author WMJ
 * @version 2020-02-17
 */
@Service
public class BanQinCrossDockService {
    @Autowired
    BanQinCrossDockBatchCreateTaskAction crossDockBatchCreateTaskAction;
    @Autowired
    BanQinWmSoAllocService wmSoAllocService;
    @Autowired
    BanQinWmCrossDockListService wmCrossDockListService;
    @Autowired
    BanQinWmAsnDetailReceiveService wmAsnDetailReceiveService;
    @Autowired
    BanQinWmSoDetailService wmSoDetailService;
    @Autowired
    BanQinWmCrossDockMapper wmCrossDockMapper;

    public Page<BanQinWmCrossDockEntity> getEntityByQueryInfo(Page page, BanQinGetCrossDockQueryEntity queryInfo) {
        return wmCrossDockListService.getEntityByQueryInfo(page, queryInfo);
    }

    /**
     * 单头生成越库任务
     */
    public ResultMessage crossDockCreateTask(List<BanQinWmCrossDockEntity> wmCrossDockEntity) {
        ResultMessage msg = new ResultMessage();
        boolean havEx = false;
        for (BanQinWmCrossDockEntity item : wmCrossDockEntity) {
            ResultMessage msg1 = crossDockBatchCreateTaskAction.crossDockBatchCreateTaskBySku(item.getOwnerCode(), item.getSkuCode(), item);
            if (!msg1.isSuccess()) {
                msg.addMessage(msg1.getMessage());
                havEx = true;
            }
        }
        if (havEx) {
            msg.setSuccess(false);
            return msg;
        } else {
            msg.setSuccess(true);
            msg.addMessage("操作成功");
            return msg;
        }
    }

    /**
     * 直接越库，按明细行生成越库任务
     */
    public ResultMessage crossDockCreateTaskDetail(List<BanQinWmAsnDetailReceiveEntity> asnReceiveEntitys, List<BanQinWmSoDetailEntity> soDetailEntitys, String orgId) {
        return crossDockBatchCreateTaskAction.crossDockBatchCreateTaskByDirect(asnReceiveEntitys, soDetailEntitys, orgId);
    }

    public ResultMessage createTaskByInDirect(List<BanQinWmAsnDetailReceiveEntity> asnReceiveEntitys, List<BanQinWmSoDetailEntity> soDetailEntitys, String orgId) {
        return crossDockBatchCreateTaskAction.crossDockBatchCreateTaskByInDirect(asnReceiveEntitys, soDetailEntitys, orgId);
    }

    /**
     * 分配明细
     */
    public List<BanQinWmSoAllocEntity> getSoAlloc(BanQinWmAsnDetailReceiveQueryEntity entity) {
        // 获取分配明细
        return wmSoAllocService.getEntityByCd(entity.getAsnNo(), entity.getRcvLineNo(), null, null, entity.getOrgId());
    }

    /**
     * 明细操作完成获取头信息
     */
    public ResultMessage getEntity(BanQinWmCrossDockEntity wmCrossDockEntity) {
        ResultMessage msg = new ResultMessage();
        BanQinWmCrossDockEntity entity = wmCrossDockListService.getEntity(wmCrossDockEntity);
        msg.setData(entity);
        return msg;
    }

    /**
     * 单头生成越库标记
     */
    public ResultMessage crossDocCreateTaskBySkuInDirect(List<BanQinWmCrossDockEntity> wmCrossDockEntity) {
        ResultMessage msg = new ResultMessage();
        boolean havEx = false;
        for (BanQinWmCrossDockEntity item : wmCrossDockEntity) {
            ResultMessage msg1 = crossDockBatchCreateTaskAction.crossDockBatchCreateTaskBySkuInDirect(item.getOwnerCode(), item.getSkuCode(), item);
            if (!msg1.isSuccess()) {
                msg.addMessage(msg1.getMessage());
                havEx = true;
            }
        }
        if (havEx) {
            msg.setSuccess(false);
        } else {
            msg.setSuccess(true);
            msg.addMessage("操作成功");
        }
        return msg;
    }

    /**
     * 单击查询收货明细
     */
    public ResultMessage getWmAsnReceiveEntity(BanQinWmAsnDetailReceiveQueryEntity wmAsnDetailReceive) {
        ResultMessage msg = new ResultMessage();
        BanQinWmReceiveEntity wmReceiveEntity = new BanQinWmReceiveEntity();
        if (StringUtils.isNotEmpty(wmAsnDetailReceive.getAsnNo())) {
            BanQinWmAsnDetailReceive item = wmCrossDockListService.getWmAsnReceiveEntity(wmAsnDetailReceive.getAsnNo(), wmAsnDetailReceive.getRcvLineNo(), wmAsnDetailReceive.getOrgId());
            if (null == item) {
                msg.setSuccess(false);
                msg.addMessage("数据已过期");
                return msg;
            }

            BeanUtils.copyProperties(item, wmReceiveEntity);
            List<BanQinWmSoAllocEntity> wmSoAllocEntity = wmSoAllocService.getEntityByCd(wmAsnDetailReceive.getAsnNo(), wmAsnDetailReceive.getRcvLineNo(), null, null, wmAsnDetailReceive.getOrgId());
            wmReceiveEntity.setWmSoAllocEntity(wmSoAllocEntity);
        }
        List<BanQinWmAsnDetailReceiveEntity> wmAsnDetailReceiveEntity = wmAsnDetailReceiveService.getEntityByCrossDock(wmAsnDetailReceive);
        List<BanQinWmSoDetailEntity> wmSoDetailEntity = wmSoDetailService.getEntityByCrossDock(wmAsnDetailReceive);
        wmReceiveEntity.setWmAsnDetailReceiveEntity(wmAsnDetailReceiveEntity);
        wmReceiveEntity.setWmSoDetailEntity(wmSoDetailEntity);
        msg.setSuccess(true);
        msg.setData(wmReceiveEntity);
        return msg;
    }

    /**
     * 越库任务高级查询
     */
    public Page<BanQinWmAsnDetailReceiveQueryEntity> getTaskItemByQueryInfo(Page page, BanQinWmTaskCdByDirectQueryEntity queryInfo) {
        return wmCrossDockListService.getTaskItemByQueryInfo(page, queryInfo);
    }

    public List<BanQinGetCdAllocStatusAndOutStepQueryEntity> getCdAllocStatusAndOutStepQueryList(BanQinGetCdAllocStatusAndOutStepQueryEntity entity) {
        return wmCrossDockMapper.getCdAllocStatusAndOutStepQueryList(entity);
    }

    public List<BanQinGetCrossDockAsnDetailReceiveQueryEntity> getCrossDockAsnDetailReceiveQueryList(BanQinGetCrossDockAsnDetailReceiveQueryEntity entity) {
        return wmCrossDockMapper.getCrossDockAsnDetailReceiveQueryList(entity);
    }

}
