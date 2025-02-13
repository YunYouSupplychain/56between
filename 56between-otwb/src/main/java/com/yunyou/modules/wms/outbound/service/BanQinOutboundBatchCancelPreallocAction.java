package com.yunyou.modules.wms.outbound.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.modules.wms.common.entity.WarehouseException;
import com.yunyou.modules.wms.common.service.BanQinWmsCommonService;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoPreallocEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 取消预配-批量
 * @author WMJ
 * @version 2019/02/20
 */
@Service
public class BanQinOutboundBatchCancelPreallocAction {
	// 取消预配
	@Autowired
	protected BanQinOutboundCancelPreallocService outboundCancelPreallocService;
	// 预配明细
	@Autowired
	protected BanQinWmSoPreallocService wmSoPreallocService;
	// 出库单
	@Autowired
	protected BanQinWmSoHeaderService wmSoHeaderService;

    /**
     * 批量取消预配
     * @param processByCode
     * @param noList
     * @return
     */
	public ResultMessage outboundBatchCancelPrealloc(String processByCode, List<String> noList, String orgId) {
        ResultMessage msg = new ResultMessage();
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
		// 2、获取预配明细
		List<BanQinWmSoPreallocEntity> wmSoPreallocEntityList = wmSoPreallocService.getEntityByProcessByCode(processByCode, noList, orgId);
		// 按预配明细
		message = outboundBatchCancelPrealloc(wmSoPreallocEntityList);
		// 消息处理
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
     * 批量取消预配 按预配明细
     * @param entityList
     * @return
     */
	public ResultMessage outboundBatchCancelPrealloc(List<BanQinWmSoPreallocEntity> entityList) {
        ResultMessage msg = new ResultMessage();
		for (BanQinWmSoPreallocEntity wmSoPreallocEntity : entityList) {
			try {
				// 按预配明细 取消预配
				outboundCancelPreallocService.outboundCancelPrealloc(wmSoPreallocEntity);
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