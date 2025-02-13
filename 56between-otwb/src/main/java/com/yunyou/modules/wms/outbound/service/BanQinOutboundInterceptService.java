package com.yunyou.modules.wms.outbound.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.modules.wms.common.entity.WarehouseException;
import com.yunyou.modules.wms.common.entity.WmsCodeMaster;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 拦截
 *
 * @author WMJ
 * @version 2019/02/22
 */
@Service
@Transactional(readOnly = true)
public class BanQinOutboundInterceptService {
    // 出库单
    @Autowired
    protected BanQinWmSoHeaderService wmSoHeaderService;

    /**
     * 拦截指令 出库单拦截状态更新
     * @param soNo
     * @return
     * @throws WarehouseException
     */
    @Transactional
    public ResultMessage interceptOrder(String soNo, String orgId) throws WarehouseException {
        ResultMessage msg = new ResultMessage();
        BanQinWmSoHeader wmSoHeaderModel = wmSoHeaderService.findBySoNo(soNo, orgId);
        if (wmSoHeaderModel == null) {
            // 查询不到出库单
            throw new WarehouseException("查询不到出库单", soNo);
        }
        // 如果状态为部分发运/完全发运，那么拦截指令为90(拦截失败)
        if (wmSoHeaderModel.getStatus().equals(WmsCodeMaster.SO_PART_SHIPPING.getCode()) || wmSoHeaderModel.getStatus().equals(WmsCodeMaster.SO_FULL_SHIPPING.getCode())) {
            wmSoHeaderModel.setInterceptStatus(WmsCodeMaster.ITC_FAIL.getCode());
            throw new WarehouseException(soNo + "已发运，不能操作");
        }
        // 如果状态为创建状态，那么拦截指令为99，订单状态改为90(执行取消)
        else if (wmSoHeaderModel.getStatus().equals(WmsCodeMaster.SO_NEW.getCode())) {
            // 拦截状态=99(拦截成功)
            wmSoHeaderModel.setInterceptStatus(WmsCodeMaster.ITC_SUCCESS.getCode());
            // 订单状态取消
            wmSoHeaderModel.setStatus(WmsCodeMaster.SO_CANCEL.getCode());
        }
        // 其他状态，那么指令为10(拦截指令完成)，订单状态
        else {
            // 拦截状态=10(拦截)
            wmSoHeaderModel.setInterceptStatus(WmsCodeMaster.ITC_INTERCEPT.getCode());
        }
        wmSoHeaderService.save(wmSoHeaderModel);

        msg.setSuccess(true);
        return msg;
    }

}