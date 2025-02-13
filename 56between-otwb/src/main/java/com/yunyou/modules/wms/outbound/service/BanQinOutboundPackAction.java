package com.yunyou.modules.wms.outbound.service;

import com.google.common.collect.Lists;
import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.modules.interfaces.interactive.service.SyncLogisticsNoService;
import com.yunyou.modules.wms.common.entity.ProcessByCode;
import com.yunyou.modules.wms.common.entity.WarehouseException;
import com.yunyou.modules.wms.common.entity.WmsConstants;
import com.yunyou.modules.wms.common.service.BanQinWmsCommonService;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoAllocEntity;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoEntity;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoSerialEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 打包更换箱号并且发货、打印
 *
 * @author WMJ
 * @version 2019/02/20
 */
@Service
public class BanQinOutboundPackAction {
    @Autowired
    protected BanQinOutboundPackService outboundPackService;
    @Autowired
    protected BanQinWmsCommonService wmCommon;
    @Autowired
    protected BanQinOutboundBatchShipmentAction outboundBatchShipmentAction;
    @Autowired
    private BanQinWmSoHeaderService wmSoHeaderService;
    @Autowired
    private SyncLogisticsNoService syncLogisticsNoService;

    /**
     * 打包
     *
     * @param --目标包装单位，目标箱号
     * @param toId             目标箱号
     * @param soSerialList     出库序列号
     * @param trackingNo       快递单号
     * @param isCheck          是否复核
     * @param isPrintContainer 是否打印装箱清单
     * @param isPrintLabel     是否打印装箱标签
     * @return
     */
    public ResultMessage updateTraceId(List<BanQinWmSoAllocEntity> allocItems, String toId, List<BanQinWmSoSerialEntity> soSerialList, String trackingNo, String isCheck, String isPrintContainer, String isPrintLabel, String orgId) {
        ResultMessage msg = new ResultMessage();
        // 是否自动打包装箱标签
        try {
            String soNo = allocItems.get(0).getSoNo();
            BanQinWmSoEntity wmSoEntity = wmSoHeaderService.findEntityBySoNo(soNo, orgId);
            if (isPrintLabel.equals(WmsConstants.YES)) {
                msg = outboundPackService.packAndPrint(allocItems, toId, soSerialList, isCheck, wmSoEntity);
                // 同步更新到OMS、TMS
                new Thread(() -> syncLogisticsNoService.synchroWayBillNo(wmSoEntity.getDef3(), soNo, orgId)).start();
            } else {
                outboundPackService.updateTraceId(allocItems, toId, soSerialList, trackingNo, isCheck, wmSoEntity);
            }
        } catch (WarehouseException e) {
            msg.setSuccess(false);
            msg.addMessage(e.getMessage());
            return msg;
        }
        // 打包参数：打包后是否自动做发货确认（Y:复核后自动发货，N:复核后不自动发货）
        String packAutoShip = "N";
        if (WmsConstants.YES.equals(packAutoShip)) {
            List<String> noList = Lists.newArrayList();
            noList.add(toId);
            ResultMessage shipMsg = outboundBatchShipmentAction.outboundBatchShipment(ProcessByCode.BY_TOID.getCode(), noList, WmsConstants.YES, orgId);
            if (!shipMsg.isSuccess()) {
                msg.addMessage(shipMsg.getMessage());
                msg.setSuccess(false);
            }
        }
        msg.setSuccess(true);
        msg.addMessage("操作成功");
        return msg;
    }

    /**
     * 取消打包
     *
     * @param allocIds      分配ID
     * @param isCancelCheck 是否同步取消复核
     * @param orgId         机构ID
     * @return
     */
    public ResultMessage cancelPack(List<String> allocIds, String isCancelCheck, String orgId) {
        ResultMessage msg = new ResultMessage();

        StringBuilder errMsg = new StringBuilder();
        for (String allocId : allocIds) {
            try {
                outboundPackService.cancelPack(allocId, WmsConstants.YES.equals(isCancelCheck), orgId);
            } catch (Exception e) {
                errMsg.append(e.getMessage()).append("<br>");
            }
        }
        msg.setMessage(StringUtils.isNotBlank(errMsg) ? errMsg.toString() : "操作成功");
        return msg;
    }
}