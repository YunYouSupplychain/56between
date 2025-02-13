package com.yunyou.modules.wms.rf.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.modules.sys.entity.User;
import com.yunyou.modules.sys.utils.UserUtils;
import com.yunyou.modules.wms.common.entity.ProcessByCode;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoAllocEntity;
import com.yunyou.modules.wms.outbound.service.BanQinOutboundBatchShipmentAction;
import com.yunyou.modules.wms.outbound.service.BanQinWmSoAllocService;
import com.yunyou.modules.wms.outbound.service.BanQinWmSoHeaderService;
import com.yunyou.modules.wms.rf.entity.WMSRF_SHIP_SaveShipment_Request;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * RF发货
 * @author WMJ
 * @version 2019/07/09
 */
@Service
public class RfShipmentService {
    @Autowired
    private BanQinOutboundBatchShipmentAction outboundShipmentAction;
    @Autowired
    private BanQinWmSoAllocService wmSoAllocService;
    @Autowired
    private BanQinWmSoHeaderService wmSoHeaderService;

    public ResultMessage saveShipment(WMSRF_SHIP_SaveShipment_Request request) {
        ResultMessage msg = new ResultMessage();
        User user = UserUtils.getUser();
        if (StringUtils.isNotBlank(request.getSoNo())) {
            msg = saveShipmentBySo(request.getSoNo(), user.getOffice().getId());
        } else if (StringUtils.isNotBlank(request.getToId())) {
            msg = saveShipmentByTraceId(request.getToId(), user.getOffice().getId());
        } else if (StringUtils.isNotBlank(request.getTrackingNo())) {
            msg = saveShipmentByTrackingNo(request.getTrackingNo(), user.getOffice().getId());
        }
        return msg;
    }

    public ResultMessage saveShipmentBySo(String soNo, String orgId) {
        ResultMessage msg = new ResultMessage();
        BanQinWmSoAllocEntity condition = new BanQinWmSoAllocEntity();
        condition.setSoNo(soNo);
        condition.setOrgId(orgId);
        List<BanQinWmSoAllocEntity> items = wmSoAllocService.findByEntity(condition);
        if (CollectionUtil.isNotEmpty(items)) {
            long hasAlloc = items.stream().filter(s -> "40".equals(s.getStatus())).count();
            if (hasAlloc == items.size()) {
                msg.setSuccess(false);
                msg.setMessage("[" + soNo + "]未拣货, 无法发运!");
                return msg;
            }
            long hasShip = items.stream().filter(s -> "80".equals(s.getStatus())).count();
            if (hasShip == items.size()) {
                msg.setSuccess(false);
                msg.setMessage("[" + soNo + "]已发运, 不能再发运!");
                return msg;
            }

            BanQinWmSoAllocEntity entity = items.get(0);
            ResultMessage message = wmSoHeaderService.checkBatchInterceptStatus(ProcessByCode.BY_SO.getCode(), Lists.newArrayList(entity.getSoNo()), entity.getOrgId());
            if (!message.isSuccess()) {
                return message;
            }
            message = wmSoHeaderService.checkBatchHoldStatus(ProcessByCode.BY_SO.getCode(), Lists.newArrayList(entity.getSoNo()), entity.getOrgId());
            if (!message.isSuccess()) {
                return message;
            }
            msg = outboundShipmentAction.outboundBatchShipment(items);
        } else {
            msg.setSuccess(false);
            msg.setMessage("[" + soNo + "]不存在分配拣货记录!");
        }
        return msg;
    }

    private ResultMessage saveShipmentByTraceId(String traceId, String orgId) {
        ResultMessage msg = new ResultMessage();
        BanQinWmSoAllocEntity condition = new BanQinWmSoAllocEntity();
        condition.setToId(traceId);
        condition.setOrgId(orgId);
        List<BanQinWmSoAllocEntity> items = wmSoAllocService.findByEntity(condition);
        if (CollectionUtil.isNotEmpty(items)) {
            Map<String, List<BanQinWmSoAllocEntity>> collect = items.stream().collect(Collectors.groupingBy(BanQinWmSoAllocEntity::getSoNo));
            for (Map.Entry<String, List<BanQinWmSoAllocEntity>> entry : collect.entrySet()) {
                long hasAlloc = entry.getValue().stream().filter(s -> "40".equals(s.getStatus())).count();
                if (hasAlloc == entry.getValue().size()) {
                    msg.setSuccess(false);
                    msg.setMessage("[" + traceId + "]未拣货, 无法发运!");
                    return msg;
                }
                long hasShip = entry.getValue().stream().filter(s -> "80".equals(s.getStatus())).count();
                if (hasShip == entry.getValue().size()) {
                    msg.setSuccess(false);
                    msg.setMessage("[" + traceId + "]已发运, 不能再发运!");
                    return msg;
                }

                BanQinWmSoAllocEntity entity = entry.getValue().get(0);
                ResultMessage message = wmSoHeaderService.checkBatchInterceptStatus(ProcessByCode.BY_SO.getCode(), Lists.newArrayList(entity.getSoNo()), entity.getOrgId());
                if (!message.isSuccess()) {
                    return message;
                }
                message = wmSoHeaderService.checkBatchHoldStatus(ProcessByCode.BY_SO.getCode(), Lists.newArrayList(entity.getSoNo()), entity.getOrgId());
                if (!message.isSuccess()) {
                    return message;
                }
            }
            msg = outboundShipmentAction.outboundBatchShipment(items);
        } else {
            msg.setSuccess(false);
            msg.setMessage("[" + traceId + "]不存在分配拣货记录!");
        }
        return msg;
    }

    private ResultMessage saveShipmentByTrackingNo(String trackingNo, String orgId) {
        ResultMessage msg = new ResultMessage();
        BanQinWmSoAllocEntity condition = new BanQinWmSoAllocEntity();
        condition.setTrackingNo(trackingNo);
        condition.setOrgId(orgId);
        List<BanQinWmSoAllocEntity> items = wmSoAllocService.findByEntity(condition);
        if (CollectionUtil.isNotEmpty(items)) {
            Map<String, List<BanQinWmSoAllocEntity>> collect = items.stream().collect(Collectors.groupingBy(BanQinWmSoAllocEntity::getSoNo));
            for (Map.Entry<String, List<BanQinWmSoAllocEntity>> entry : collect.entrySet()) {
                long hasShip = entry.getValue().stream().filter(s -> "80".equals(s.getStatus())).count();
                if (hasShip == entry.getValue().size()) {
                    msg.setSuccess(false);
                    msg.setMessage("[" + trackingNo + "]已发运, 不能再发运!");
                    return msg;
                }

                BanQinWmSoAllocEntity entity = entry.getValue().get(0);
                ResultMessage message = wmSoHeaderService.checkBatchInterceptStatus(ProcessByCode.BY_SO.getCode(), Lists.newArrayList(entity.getSoNo()), entity.getOrgId());
                if (!message.isSuccess()) {
                    return message;
                }
                message = wmSoHeaderService.checkBatchHoldStatus(ProcessByCode.BY_SO.getCode(), Lists.newArrayList(entity.getSoNo()), entity.getOrgId());
                if (!message.isSuccess()) {
                    return message;
                }
            }
            msg = outboundShipmentAction.outboundBatchShipment(items);
        } else {
            msg.setSuccess(false);
            msg.setMessage("[" + trackingNo + "]不存在!");
        }
        return msg;
    }

}
