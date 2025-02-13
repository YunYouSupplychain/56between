package com.yunyou.modules.wms.outbound.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.modules.wms.common.entity.WarehouseException;
import com.yunyou.modules.wms.common.entity.WmsCodeMaster;
import com.yunyou.modules.wms.common.service.BanQinWmsCommonService;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoAllocEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 批量取消分配
 * @author WMJ
 * @version 2019/02/20
 */
@Service
public class BanQinOutboundBatchCancelAllocAction {
	// 取消分配
	@Autowired
	protected BanQinOutboundCancelAllocService outboundCancelAllocService;
	// 分配明细
	@Autowired
	protected BanQinWmSoAllocService wmSoAllocService;
	// 出库单
	@Autowired
	protected BanQinWmSoHeaderService wmSoHeaderService;

    /**
     * 批量取消分配
     * @param processByCode
     * @param noList
     * @return
     */
	public ResultMessage outboundBatchCancelAlloc(String processByCode, List<String> noList, String orgId) {
        ResultMessage msg = new ResultMessage();
		List<BanQinWmSoAllocEntity> wmSoAllocEntityList = null;
		// 1、拦截、冻结状态校验
		// 拦截状态校验
        ResultMessage message = wmSoHeaderService.checkBatchInterceptStatus(processByCode, noList, orgId);
		if (!message.isSuccess()) {
			msg.addMessage(message.getMessage());
		}
		// 冻结状态校验
		message = wmSoHeaderService.checkBatchHoldStatus(processByCode, noList, orgId);
		if (!message.isSuccess()) {
			msg.addMessage(message.getMessage());
		}
		// 2、按波次单号 获取分配明细
		wmSoAllocEntityList = wmSoAllocService.getEntityByProcessByCode(processByCode, noList, WmsCodeMaster.ALLOC_FULL_ALLOC.getCode(), orgId);
		if (wmSoAllocEntityList.size() == 0) {
			// 没有可以操作的分配拣货明细
			msg.addMessage("没有可以操作的分配拣货明细");
			msg.setSuccess(false);
			return msg;
		}
		// 按分配明细拣货
		message = outboundBatchCancelAlloc(wmSoAllocEntityList);
		if (!message.isSuccess()) {
			msg.addMessage(message.getMessage());
			msg.setSuccess(false);
		}
		if (StringUtils.isEmpty(msg.getMessage())) {
			msg.addMessage("操作成功");
			msg.setSuccess(true);
		}
		return msg;
	}

    /**
     * 批量取消分配 按分配明细
     * @param entityList
     * @return
     */
	public ResultMessage outboundBatchCancelAlloc(List<BanQinWmSoAllocEntity> entityList) {
        ResultMessage msg = new ResultMessage();
		for (BanQinWmSoAllocEntity wmSoAllocEntity : entityList) {
			if (StringUtils.isNotEmpty(wmSoAllocEntity.getCdType())) {
				// 只能在越库任务界面执行出库，[{0}]属于越库，不能操作
				msg.addMessage("只能在越库任务界面执行出库" + wmSoAllocEntity.getAllocId());
				msg.setSuccess(false);
				continue;
			}
			try {
				outboundCancelAllocService.OutboundCancelAlloc(wmSoAllocEntity);
			} catch (WarehouseException e) {
				msg.addMessage(e.getMessage());
				msg.setSuccess(false);
			}
		}
		// 消息处理
		msg.setMessage(BanQinWmsCommonService.getResultMessage(msg.getMessage()));
		if (StringUtils.isEmpty(msg.getMessage())) {
			msg.addMessage("操作成功");
			msg.setSuccess(true);
		}
		return msg;
	}
}