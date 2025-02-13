package com.yunyou.modules.wms.outbound.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.modules.wms.common.entity.WarehouseException;
import com.yunyou.modules.wms.common.service.BanQinWmsCommonService;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoAllocEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 批量生成上架任务
 * @author WMJ
 * @version 2019/02/20
 */
@Service
public class BanQinOutboundBatchCreateTaskPaAction {
	@Autowired
	protected BanQinOutboundCreateTaskPaService outboundCreateTaskPaService;

    /**
     * 批量取消拣货 按分配明细
     * @param wmSoAllocEntityList
     * @return
     */
	public ResultMessage outboundBatchCreateTaskPa(List<BanQinWmSoAllocEntity> wmSoAllocEntityList) {
        ResultMessage msg = new ResultMessage();
		for (BanQinWmSoAllocEntity wmSoAllocEntity : wmSoAllocEntityList) {
			try {
				outboundCreateTaskPaService.outboundCreateTaskPaByAlloc(wmSoAllocEntity);
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