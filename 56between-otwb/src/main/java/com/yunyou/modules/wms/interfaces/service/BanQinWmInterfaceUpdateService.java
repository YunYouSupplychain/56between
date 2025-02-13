package com.yunyou.modules.wms.interfaces.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.modules.interfaces.weigh.entity.BanQinWeighingConstant;
import com.yunyou.modules.wms.common.entity.ProcessByCode;
import com.yunyou.modules.wms.common.entity.WmsCodeMaster;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoAlloc;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoAllocEntity;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoEntity;
import com.yunyou.modules.wms.outbound.service.BanQinOutboundBatchShipmentAction;
import com.yunyou.modules.wms.outbound.service.BanQinWmSoAllocService;
import com.yunyou.modules.wms.outbound.service.BanQinWmSoHeaderService;
import com.yunyou.modules.wms.weigh.entity.BanQinWmMiddleWeigh;
import com.yunyou.modules.wms.weigh.service.BanQinWmMiddleWeighService;
import com.yunyou.modules.wms.weigh.service.BanQinWmWeighHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BanQinWmInterfaceUpdateService {
    @Autowired
    private BanQinWmSoAllocService banQinWmSoAllocService;
    @Autowired
    private BanQinWmMiddleWeighService banQinWmMiddleWeighService;
    @Autowired
    @Lazy
    private BanQinOutboundBatchShipmentAction outboundShipmentAction;
    @Autowired
    private BanQinWmWeighHistoryService banQinWmWeighHistoryService;
    @Autowired
    private BanQinWmSoHeaderService banQinWmSoHeaderService;

    /**
     * 称重信息更新-称重中间表
     */
    @Transactional
    public ResultMessage updateWeighInfoByMiddle(BanQinWmMiddleWeigh weighInfo) {
        ResultMessage msg = new ResultMessage();
        BanQinWmSoAllocEntity condition = new BanQinWmSoAllocEntity();
        condition.setTrackingNo(weighInfo.getBoxNum());
        condition.setOrgId(weighInfo.getOrgId());
        List<BanQinWmSoAllocEntity> allocList = banQinWmSoAllocService.findByEntity(condition);
        if (CollectionUtil.isEmpty(allocList)) {
            msg.setSuccess(false);
            msg.setMessage("面单号不存在");
        } else {
            String soNo = allocList.get(0).getSoNo();
            BanQinWmSoEntity soEntity = banQinWmSoHeaderService.findEntityBySoNo(soNo, weighInfo.getOrgId());
            for (BanQinWmSoAllocEntity updateItem : allocList) {
                updateItem.setPackWeight(weighInfo.getWeighQty());
                updateItem.setUpdateDate(new Date());
                updateItem.setUpdateBy(BanQinWeighingConstant.WEIGH_TIMER_USER);
                banQinWmSoAllocService.save(updateItem);
                banQinWmWeighHistoryService.saveHistory(weighInfo, updateItem, soEntity, BanQinWeighingConstant.ACTION_WEIGH_SAVE, "操作成功", BanQinWeighingConstant.WEIGH_TIMER_USER);
            }
            msg.setSuccess(true);
            msg.setMessage("操作成功！");
        }
        return msg;
    }

    @Transactional
    public void updateWeighInfo(BanQinWmMiddleWeigh weighInfo) {
        weighInfo.setUpdateBy(BanQinWeighingConstant.WEIGH_TIMER_USER);
        weighInfo.setUpdateDate(new Date());
        banQinWmMiddleWeighService.update(weighInfo);
    }

    /**
     * 自动发货
     */
    public void autoShipByMiddle(BanQinWmMiddleWeigh weighInfo) {
        ResultMessage msg = new ResultMessage();
        BanQinWmSoAllocEntity condition = new BanQinWmSoAllocEntity();
        condition.setTrackingNo(weighInfo.getBoxNum());
        // condition.setStatus(WmsCodeMaster.SO_FULL_PICKING.getCode());
        condition.setOrgId(weighInfo.getOrgId());
        List<BanQinWmSoAllocEntity> allocList = banQinWmSoAllocService.findByEntity(condition);
        if (CollectionUtil.isEmpty(allocList)) {
            msg.setSuccess(false);
            msg.setMessage("面单号不存在");
        } else {
            List<BanQinWmSoAllocEntity> pickList = allocList.stream().filter(t -> WmsCodeMaster.SO_FULL_PICKING.getCode().equals(t.getStatus())).collect(Collectors.toList());
            if (CollectionUtil.isEmpty(pickList)) {
                String soNo = allocList.get(0).getSoNo();
                BanQinWmSoEntity soEntity = banQinWmSoHeaderService.findEntityBySoNo(soNo, weighInfo.getOrgId());
                for (BanQinWmSoAllocEntity entity : allocList) {
                    banQinWmWeighHistoryService.saveHistory(weighInfo, entity, soEntity, BanQinWeighingConstant.ACTION_AUTO_SHIP, "不是完全拣货状态，不进行自动发货！", BanQinWeighingConstant.WEIGH_TIMER_USER);
                }
                msg.setSuccess(true);
            } else {
                List<String> allocIdList = pickList.stream().map(BanQinWmSoAlloc::getAllocId).collect(Collectors.toList());
                // 校验状态
                msg = checkStatus(ProcessByCode.BY_ALLOC.getCode(), allocIdList, weighInfo.getOrgId());
                if (msg.isSuccess()) {
                    // 按分配明细批量发运
                    msg = outboundShipmentAction.outboundBatchShipment(pickList);
                    // 查找发货成功的数据
                    BanQinWmSoAllocEntity shipCondition = new BanQinWmSoAllocEntity();
                    shipCondition.setTrackingNo(weighInfo.getBoxNum());
                    shipCondition.setStatus(WmsCodeMaster.SO_FULL_SHIPPING.getCode());
                    shipCondition.setOrgId(weighInfo.getOrgId());
                    List<BanQinWmSoAllocEntity> shipAllocList = banQinWmSoAllocService.findByEntity(shipCondition);
                    // 过滤本次操作数据，插入称重历史表
                    if (CollectionUtil.isNotEmpty(shipAllocList)) {
                        String soNo = pickList.get(0).getSoNo();
                        BanQinWmSoEntity soEntity = banQinWmSoHeaderService.findEntityBySoNo(soNo, weighInfo.getOrgId());
                        for (BanQinWmSoAllocEntity entity : shipAllocList) {
                            if (allocIdList.contains(entity.getAllocId())) {
                                banQinWmWeighHistoryService.saveHistory(weighInfo, entity, soEntity, BanQinWeighingConstant.ACTION_AUTO_SHIP, "操作成功", BanQinWeighingConstant.WEIGH_TIMER_USER);
                            }
                        }
                    }
                }
            }
        }
        if (msg.isSuccess()) {
            banQinWmMiddleWeighService.delete(weighInfo);
        } else {
            weighInfo.setShipStatus(BanQinWeighingConstant.STATUS_N);
            weighInfo.setShipMsg(msg.getMessage());
            updateWeighInfo(weighInfo);
        }
    }

    private ResultMessage checkStatus(String processByCode, List<String> noList, String orgId) {
        // 拦截状态校验
        ResultMessage message = banQinWmSoHeaderService.checkBatchInterceptStatus(processByCode, noList, orgId);
        if (!message.isSuccess()) {
            return message;
        }
        // 冻结状态校验
        return banQinWmSoHeaderService.checkBatchHoldStatus(processByCode, noList, orgId);
    }
}
