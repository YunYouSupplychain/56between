package com.yunyou.modules.wms.outbound.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.modules.wms.common.entity.WarehouseException;
import com.yunyou.modules.wms.common.service.BanQinWmsCommonService;
import com.yunyou.modules.wms.outbound.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 拣货单操作类
 * @author ZYF
 * @version 2020/05/14
 */
@Service
public class BanQinOutboundPickOrderService {
	// 公共方法
	@Autowired
	protected BanQinWmsCommonService wmCommon;
	// 分配明细
	@Autowired
	protected BanQinWmSoAllocService wmSoAllocService;
	// 删除
	@Autowired
	protected BanQinOutboundRemoveService outboundRemoveService;
	// 拣货单
	protected BanQinWmPickHeaderService pickHeaderService;
	// 拣货单明细
	protected BanQinWmPickDetailService pickDetailService;

    /**
     * 保存拣货单
     * @return
     */
	public ResultMessage savePickEntity(BanQinWmPickHeaderEntity pickEntity) {
		ResultMessage msg = new ResultMessage();
		BanQinWmPickHeader banQinWmPickHeader = new BanQinWmPickHeader();
		try {
			banQinWmPickHeader = pickHeaderService.savePickHeader(pickEntity);
		} catch (WarehouseException e) {
			msg.addMessage(e.getMessage());
			msg.setSuccess(false);
		}
		msg.addMessage("操作成功");
		msg.setSuccess(true);
		msg.setData(banQinWmPickHeader);
		return msg;
	}

    /**
     * 删除拣货单
     * @return
     */
	public ResultMessage removePickEntity(String[] pickNos, String orgId) {
		ResultMessage msg = new ResultMessage();
		// 循环出库单号
		for (String pickNo : pickNos) {
			try {
				outboundRemoveService.removePickEntity(pickNo, orgId);
			} catch (WarehouseException e) {
				msg.addMessage(e.getMessage());
			}
		}
		if (StringUtils.isEmpty(msg.getMessage())) {
			msg.setSuccess(true);
			msg.addMessage("操作成功");
		}
		return msg;
	}
}