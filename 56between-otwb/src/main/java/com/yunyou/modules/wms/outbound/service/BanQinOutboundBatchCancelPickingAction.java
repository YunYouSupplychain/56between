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
 * 批量取消拣货
 * @author WMJ
 * @version 2019/02/20
 */
@Service
public class BanQinOutboundBatchCancelPickingAction {
	// 拣货
	@Autowired
	protected BanQinOutboundCancelPickingService outboundCancelPickingService;
	// 分配明细
	@Autowired
	protected BanQinWmSoAllocService wmSoAllocService;
	// 出库单
	@Autowired
	protected BanQinWmSoHeaderService wmSoHeaderService;
	// 取消拣货 生成上架任务
	@Autowired
	protected BanQinOutboundCreateTaskPaService outboundCreateTaskPaService;

    /**
     * 批量取消拣货
     * @param processByCode
     * @param noList
     * @return
     */
	public ResultMessage outboundBatchCancelPicking(String processByCode, List<String> noList, String orgId) {
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
		// 2、按波次单号、按出库单号+行号 获取分配明细
		wmSoAllocEntityList = wmSoAllocService.getEntityByProcessByCode(processByCode, noList, WmsCodeMaster.ALLOC_FULL_PICKING.getCode(), orgId);
		if (wmSoAllocEntityList.size() == 0) {
			// 没有可以操作的分配拣货明细
			msg.addMessage("没有可以操作的分配拣货明细");
			msg.setSuccess(false);
			return msg;
		}
		// 按分配明细拣货
		message = outboundBatchCancelPicking(wmSoAllocEntityList);
		if (noList.size() > 1) {
			message.setMessage(BanQinWmsCommonService.getResultMessage(msg.getMessage()));
		}
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
     * 批量取消拣货 按分配明细
     * @param wmSoAllocEntityList
     * @return
     */
	public ResultMessage outboundBatchCancelPicking(List<BanQinWmSoAllocEntity> wmSoAllocEntityList) {
        ResultMessage msg = new ResultMessage();
		for (BanQinWmSoAllocEntity wmSoAllocEntity : wmSoAllocEntityList) {
			// 如果是直接越库匹配，并且操作步骤是收货+分配+拣货，则提示
			if (StringUtils.isNotEmpty(wmSoAllocEntity.getCdType()) && wmSoAllocEntity.getCdType().equals(WmsCodeMaster.CD_TYPE_DIRECT.getCode())
					&& wmSoAllocEntity.getCdOutStep().equals(WmsCodeMaster.RCV_ALLOC_PK.getCode())) {
				// {0}越库执行同时做了拣货，只能做取消越库
				msg.addMessage(wmSoAllocEntity.getAllocId() + "越库执行同时做了拣货，只能做取消越库");
				msg.setSuccess(false);
				continue;
			}
			try {
				outboundCancelPickingService.outboundCancelPicking(wmSoAllocEntity);
			} catch (WarehouseException e) {
				msg.addMessage(e.getMessage());
				msg.setSuccess(false);
			}
		}
		// 消息处理
		if (StringUtils.isEmpty(msg.getMessage())) {
			msg.addMessage("操作成功");
			msg.setSuccess(true);
		}
		return msg;
	}

}