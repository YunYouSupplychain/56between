package com.yunyou.modules.wms.outbound.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.modules.wms.common.entity.WarehouseException;
import com.yunyou.modules.wms.common.entity.WmsCodeMaster;
import com.yunyou.modules.wms.common.service.BanQinWmsCommonService;
import com.yunyou.modules.wms.inventory.service.BanQinInventoryService;
import com.yunyou.modules.wms.outbound.entity.BanQinWmLdHeader;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoAllocEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 装车交接、取消交接
 *
 * @author WMJ
 * @version 2019/02/20
 */
@Service
public class BanQinOutboundLdDeliveryService {
    // 公共接口
    @Autowired
    protected BanQinWmsCommonService wmCommon;
    // 分配明细
    @Autowired
    protected BanQinWmSoAllocService wmSoAllocService;
    // 库存更新
    @Autowired
    protected BanQinInventoryService inventoryService;
    // 装车明细
    @Autowired
    protected BanQinWmLdDetailService wmLdDetailService;
    // 装车单
    @Autowired
    protected BanQinWmLdHeaderService wmLdHeaderService;
    // 发运确认
    @Autowired
    protected BanQinOutboundShipmentService outboundShipmentService;
    // 取消发货
    @Autowired
    @Lazy
    protected BanQinOutboundCancelShipmentService outboundCancelShipmentService;

    /**
     * 发运确认
     *
     * @param ldNo  装车单号
     * @param orgId 机构ID
     */
    public ResultMessage outboundShipment(String ldNo, String orgId) {
        ResultMessage msg = new ResultMessage();
        StringBuilder str = new StringBuilder();
        BanQinWmLdHeader model = wmLdHeaderService.getByLdNo(ldNo, orgId);
        if (null != model && WmsCodeMaster.LD_FULL_LOAD.getCode().equals(model.getStatus()) && WmsCodeMaster.ALLOC_FULL_PICKING.getCode().equals(model.getSoStatus())) {
            model.setSoStatus(WmsCodeMaster.ALLOC_FULL_SHIPPING.getCode());
        } else {
            // 非完全装车或非完全拣货，不能操作
            str.append(ldNo).append("非完全装车或非完全拣货，不能操作").append("\n");
            msg.addMessage(str.toString());
            msg.setSuccess(false);
            return msg;
        }
        // 发运
        List<BanQinWmSoAllocEntity> soAllocEntities = wmLdDetailService.getWmSoAllocEntityByLdNo(new String[]{ldNo}, orgId);
        // 复核状态发运修改(控制参数在分配、拣货时取值控制)
        // 2、校验是否需要复核（90）/是否已经复核(99)， 如果未复核(00)，那么提示未复核不能发运(不复核(90),可直接发运)
        for (BanQinWmSoAllocEntity soAllocEntity : soAllocEntities) {
            if (WmsCodeMaster.CHECK_NEW.getCode().equals(soAllocEntity.getCheckStatus())) {
                str.append(soAllocEntity.getAllocId()).append("分配明细未复核不能发运").append("\n");
            }
        }
        if (StringUtils.isNotBlank(str)) {
            msg.addMessage(str.toString());
            msg.setSuccess(false);
            return msg;
        }
        for (BanQinWmSoAllocEntity wmSoAllocEntity : soAllocEntities) {
            try {
                // 发运确认
                outboundShipmentService.outboundShipment(wmSoAllocEntity);
            } catch (WarehouseException e) {
                msg.addMessage(e.getMessage());
                msg.setSuccess(false);
                return msg;
            }
        }
        wmLdHeaderService.save(model);
        msg.setSuccess(true);
        msg.addMessage("操作成功");
        return msg;
    }

    /**
     * 取消发货
     *
     * @param ldNo  装车单号
     * @param orgId 机构ID
     */
    public ResultMessage outboundCancelShipment(String ldNo, String orgId) {
        ResultMessage msg = new ResultMessage();
        String str = "";
        BanQinWmLdHeader model = wmLdHeaderService.getByLdNo(ldNo, orgId);
        if (null != model && WmsCodeMaster.LD_FULL_LOAD.getCode().equals(model.getStatus()) && WmsCodeMaster.ALLOC_FULL_SHIPPING.getCode().equals(model.getSoStatus())) {
            model.setSoStatus(WmsCodeMaster.ALLOC_FULL_PICKING.getCode());
        } else {
            str = str + ldNo + "非完全装车或非完全发运，不能操" + "\n";
            msg.addMessage(str);
            msg.setSuccess(false);
            return msg;
        }
        List<BanQinWmSoAllocEntity> soAllocEntities = wmLdDetailService.getWmSoAllocEntityByLdNo(new String[]{ldNo}, orgId);
        // 分配明细，取消发货
        for (BanQinWmSoAllocEntity wmSoAllocEntity : soAllocEntities) {
            try {
                // 取消发货
                outboundCancelShipmentService.outboundCancelShipment(wmSoAllocEntity);
            } catch (WarehouseException e) {
                msg.addMessage(e.getMessage());
                msg.setSuccess(false);
                return msg;
            }
        }
        wmLdHeaderService.save(model);
        msg.setSuccess(true);
        msg.addMessage("操作成功");
        return msg;
    }

}