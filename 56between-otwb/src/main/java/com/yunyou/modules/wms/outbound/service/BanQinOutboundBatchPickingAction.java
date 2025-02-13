package com.yunyou.modules.wms.outbound.service;

import com.google.common.collect.Lists;
import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.modules.wms.common.entity.WarehouseException;
import com.yunyou.modules.wms.common.entity.WmsCodeMaster;
import com.yunyou.modules.wms.common.service.BanQinWmsCommonService;
import com.yunyou.modules.wms.outbound.entity.BanQinOutboundPickingEntity;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoAlloc;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoAllocEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 批量拣货确认
 * @author WMJ
 * @version 2019/02/20
 */
@Service
public class BanQinOutboundBatchPickingAction {
	// 单分配明细拣货
	@Autowired
	protected BanQinOutboundPickingService outboundPickingService;
	// 分配明细
	@Autowired
	protected BanQinWmSoAllocService wmSoAllocService;
	// 出库单
	@Autowired
	protected BanQinWmSoHeaderService wmSoHeaderService;
	@Autowired BanQinOutboundManualAllocService outboundManualAllocService;

    /**
     * 批量拣货确认
     * @param processByCode
     * @param noList
     * @return
     */
	public ResultMessage outboundBatchPicking(String processByCode, List<String> noList, String orgId) {
        ResultMessage msg = new ResultMessage();
		List<BanQinWmSoAllocEntity> wmSoAllocEntityList = Lists.newArrayList();
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
			msg.addMessage("没有可以操作的分配拣货明细");
			msg.setSuccess(false);
			return msg;
		}
		// 按分配明细拣货
		message = outboundBatchPicking(wmSoAllocEntityList);
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
     * 批量拣货确认 按分配明细
     * @param entityList
     * @return
     */
	public ResultMessage outboundBatchPicking(List<BanQinWmSoAllocEntity> entityList) {
        ResultMessage msg = new ResultMessage();
		for (BanQinWmSoAllocEntity wmSoAllocEntity : entityList) {
			try {
                ResultMessage pickMsg = outboundPickingService.outboundPicking(wmSoAllocEntity);
				// 信息返回成功，开始计算费用
				if (pickMsg.isSuccess()) {
					BanQinOutboundPickingEntity entity = (BanQinOutboundPickingEntity) pickMsg.getData();
					if (entity != null) {
						BanQinWmSoAlloc model = entity.getWmSoAllocModel();
//						try {
//							generalCharge.generalChargeBoundImmTran(model.getOwnerCode(), model.getSoNo(), model.getLineNo(), WmsCodeMaster.ORDER_SO.getCode(), entity.getTranId());
//						} catch (WarehouseException e) {
//							e.printStackTrace();// 打印日志，信息不返回界面
//						}
					}
				}
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

	/**
	 * 手工拣货确认
	 * @param entity
	 * @return
	 */
	public ResultMessage pickingByManual(BanQinWmSoAllocEntity entity) {
		ResultMessage msg = new ResultMessage();
		try {
			msg = outboundManualAllocService.pickingByManual(entity);
		} catch (WarehouseException e) {
			msg.setSuccess(false);
			msg.setMessage(e.getMessage());
			return msg;
		}
		msg.setMessage("操作成功");
		return msg;
	}

}