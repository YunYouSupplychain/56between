package com.yunyou.modules.wms.crossDock.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.modules.wms.common.entity.WarehouseException;
import com.yunyou.modules.wms.common.entity.WmsCodeMaster;
import com.yunyou.modules.wms.crossDock.entity.BanQinWmAsnDetailReceiveQueryEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 批量执行取消越库
 *
 * @author WMJ
 * @version 2020-02-17
 */
@Service
public class BanQinCrossDockBatchCancelConfirmAction {
    @Autowired
    protected BanQinCrossDockCancelConfirmService crossDockCancelConfirmService;

    public ResultMessage crossDockCancelConfirm(List<BanQinWmAsnDetailReceiveQueryEntity> wmAsnDetailReceiveQueryEntityList) {
        ResultMessage msg = new ResultMessage();
        for (BanQinWmAsnDetailReceiveQueryEntity item : wmAsnDetailReceiveQueryEntityList) {
            try {
                if (item.getStatus().equals(WmsCodeMaster.ASN_NEW.getCode())) {
                    if (item.getCdType().equals(WmsCodeMaster.CD_TYPE_DIRECT.getCode())) {
                        msg.addMessage("ASN单号[" + item.getAsnNo() + "]行号[" + item.getRcvLineNo() + "]不是完全收货状态不能操作");
                    } else {
                        msg.addMessage("货主[" + item.getOwnerName() + "]商品[" + item.getSkuName() + "]不是完全收货状态不能操作");
                    }
                } else {
                    crossDockCancelConfirmService.crossDockCancelConfirm(item.getAsnNo(), item.getRcvLineNo(), item.getOrgId());
                }
            } catch (WarehouseException e) {
                msg.addMessage(e.getMessage());
                msg.setSuccess(false);
            }
        }
        if (StringUtils.isEmpty(msg.getMessage())) {
            msg.addMessage("操作成功");
            msg.setSuccess(true);
        }

        return msg;
    }

}
