package com.yunyou.modules.interfaces.weigh.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.service.BaseService;
import com.yunyou.modules.wms.common.entity.ProcessByCode;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoAllocEntity;
import com.yunyou.modules.wms.outbound.service.BanQinWmSoHeaderService;
import com.yunyou.modules.wms.weigh.entity.BanQinWeighMachineInfoEntity;
import com.yunyou.modules.interfaces.weigh.entity.BanQinWeighingConstant;
import com.yunyou.modules.interfaces.weigh.entity.BanQinWeighingRequest;
import com.yunyou.modules.interfaces.weigh.entity.BanQinWeighingResultMessage;
import com.yunyou.modules.wms.outbound.service.BanQinWmSoAllocService;
import com.yunyou.modules.wms.weigh.service.BanQinWeighMachineInfoService;
import com.yunyou.modules.wms.weigh.service.BanQinWmMiddleWeighService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class BanQinWeighingInterfaceService extends BaseService {

    @Autowired
    private BanQinWeighMachineInfoService banQinWeighMachineInfoService;
    @Autowired
    private BanQinWmSoAllocService banQinWmSoAllocService;
    @Autowired
    private BanQinWmMiddleWeighService banQinWmMiddleWeighService;
    @Autowired
    private BanQinWmSoHeaderService banQinWmSoHeaderService;

    @Transactional
    public BanQinWeighingResultMessage banQinWeighingHandle(BanQinWeighingRequest request) {
        BanQinWeighingResultMessage msg = new BanQinWeighingResultMessage();
        String boxNum = request.getID();
        String deviceNo = request.getSN();
        String orderNo = request.getORDER();
        Double weighQty = request.getWEI();
        Double tareQty = request.getWEI_PI();
        Date weighTime = request.getTIME();
        if (StringUtils.isBlank(boxNum)) {
            msg.setMsgInfo("称重条码为空！");
            msg.setIsSuccess(false);
            return msg;
        }
        if (StringUtils.isBlank(deviceNo)) {
            msg.setMsgInfo("设备号为空！");
            msg.setIsSuccess(false);
            return msg;
        }
        BanQinWeighMachineInfoEntity weighMachine = banQinWeighMachineInfoService.getBySN(deviceNo);
        if (weighMachine == null) {
            msg.setMsgInfo("未知设备，请先维护！");
            msg.setIsSuccess(false);
            return msg;
        }
        String orgId = weighMachine.getOrgId();
        BanQinWmSoAllocEntity condition = new BanQinWmSoAllocEntity();
        condition.setTrackingNo(boxNum);
        condition.setOrgId(orgId);
        List<BanQinWmSoAllocEntity> allocList = banQinWmSoAllocService.findByEntity(condition);
        if (CollectionUtil.isEmpty(allocList)) {
            msg.setIsSuccess(false);
            msg.setMsgInfo("未记录的面单号");
            return msg;
        } else {
            List<String> allocIdList = allocList.stream().map(t -> t.getAllocId()).collect(Collectors.toList());
            // 拦截状态校验
            ResultMessage message = banQinWmSoHeaderService.checkBatchInterceptStatus(ProcessByCode.BY_ALLOC.getCode(), allocIdList, orgId);
            if (!message.isSuccess()) {
                msg.setIsSuccess(false);
                msg.setMsgInfo(message.getMessage());
                return msg;
            }
            // 冻结状态校验
            message = banQinWmSoHeaderService.checkBatchHoldStatus(ProcessByCode.BY_ALLOC.getCode(), allocIdList, orgId);
            if (!message.isSuccess()) {
                msg.setIsSuccess(false);
                msg.setMsgInfo(message.getMessage());
                return msg;
            }
        }
        new Thread(() -> banQinWmMiddleWeighService.saveWeighInfo(boxNum, deviceNo, orgId, tareQty, weighQty, weighTime, BanQinWeighingConstant.WEIGH_USER)).start();
        msg.setIsSuccess(true);
        msg.setMsgInfo("成功");
        return msg;
    }
}
