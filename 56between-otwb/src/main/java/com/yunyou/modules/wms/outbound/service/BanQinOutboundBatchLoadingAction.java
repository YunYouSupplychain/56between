package com.yunyou.modules.wms.outbound.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.modules.wms.common.entity.WarehouseException;
import com.yunyou.modules.wms.common.service.BanQinWmsCommonService;
import com.yunyou.modules.wms.outbound.entity.BanQinWmLdDetail;
import com.yunyou.modules.wms.outbound.entity.BanQinWmLdDetailEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 批量装载确认
 * @author WMJ
 * @version 2019/02/20
 */
@Service
public class BanQinOutboundBatchLoadingAction {
	// 装载确认
	@Autowired
	protected BanQinOutboundLoadingService outboundLoadingService;

    /**
     * 按装车明细实体Entity批量装载
     * @param ldDetailEntitys
     * @return
     */
	public ResultMessage outboundBatchLoading(List<BanQinWmLdDetailEntity> ldDetailEntitys) {
        ResultMessage msg = new ResultMessage();
		for (BanQinWmLdDetailEntity ldDetailEntity : ldDetailEntitys) {
			try {
				outboundLoadingService.outboundLoading(ldDetailEntity);
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

    /**
     * 按装车明细Model批量装载
     * @param ldDetailModels
     * @return
     */
	public ResultMessage outboundBatchLoadingByLdDetails(List<BanQinWmLdDetail> ldDetailModels) {
        ResultMessage msg = new ResultMessage();
		for (BanQinWmLdDetail ldDetailModel : ldDetailModels) {
			try {
				outboundLoadingService.outboundLoading(ldDetailModel);
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

    /**
     * 按订单批量装载
     * @param ldNo
     * @param soNos
     * @return
     */
	public ResultMessage outboundBatchLoadingBySoNo(String ldNo, String[] soNos, String orgId) {
        ResultMessage msg = new ResultMessage();
		for (String soNo : soNos) {
			try {
				outboundLoadingService.outboundLoadingBySoNo(ldNo, soNo, orgId);
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

    /**
     * 按订单批量装载
     * @param ldNo
     * @param traceIds
     * @return
     */
	public ResultMessage outboundBatchLoadingByTraceId(String ldNo, String[] traceIds, String orgId) {
        ResultMessage msg = new ResultMessage();
		for (String traceId : traceIds) {
			try {
				outboundLoadingService.outboundLoadingByTraceId(ldNo, traceId, orgId);
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