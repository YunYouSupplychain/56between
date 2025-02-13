package com.yunyou.modules.wms.outbound.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.modules.wms.common.entity.WmsConstants;
import com.yunyou.modules.wms.common.service.BanQinWmsCommonService;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoSerialEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 复核确认并且发货、打印
 * @author WMj
 * @version 2019/02/20
 */
@Service
public class BanQinOutboundCheckAction {
	// 公共方法
	@Autowired
	protected BanQinWmsCommonService wmCommon;
	// 复核
	@Autowired
	protected BanQinOutboundCheckService outboundCheckService;
	// 发运确认
	@Autowired
	protected BanQinOutboundBatchShipmentAction outboundBatchShipmentAction;

    /**
     * 复核确认后，并且自动发货、打印
     * @param allocIds
     * @param soSerialList
     * @param trackingNo
     * @param noList
     * @param processByCode
     * @return
     */
	public ResultMessage checkConfirm(String[] allocIds, List<BanQinWmSoSerialEntity> soSerialList, String trackingNo, List<String> noList, String processByCode, String orgId) {
        ResultMessage msg = new ResultMessage();
		outboundCheckService.checkConfirm(allocIds, soSerialList, trackingNo, orgId);
		// 复核参数：复核后是否自动做发货确认（Y:复核后自动发货，N:复核后不自动发货）
		String checkAutoShip = "N";
		if (checkAutoShip.equals(WmsConstants.YES)) {
            ResultMessage shipMsg = outboundBatchShipmentAction.outboundBatchShipment(processByCode, noList, WmsConstants.YES, orgId);
			if (!shipMsg.isSuccess()) {
				msg.addMessage(shipMsg.getMessage());
				msg.setSuccess(false);
				return msg;
			}
		}
		msg.addMessage("操作成功");
		msg.setSuccess(true);
		return msg;
	}

}