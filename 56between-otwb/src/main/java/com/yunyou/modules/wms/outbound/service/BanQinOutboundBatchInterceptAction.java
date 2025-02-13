package com.yunyou.modules.wms.outbound.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.modules.wms.common.entity.ProcessByCode;
import com.yunyou.modules.wms.common.entity.WarehouseException;
import com.yunyou.modules.wms.common.entity.WmsCodeMaster;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoAllocEntity;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoDetail;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoPreallocEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 出库订单拦截
 * @author WMJ
 * @version 2019/02/20
 */
@Service
public class BanQinOutboundBatchInterceptAction {
	// 出库单
	@Autowired
	protected BanQinWmSoHeaderService wmSoHeaderService;
	// 出库单明细
	@Autowired
	protected BanQinWmSoDetailService wmSoDetailService;
	// 预配明细
	@Autowired
	protected BanQinWmSoPreallocService wmSoPreallocService;
	// 分配明细
	@Autowired
	protected BanQinWmSoAllocService wmSoAllocService;
	// 取消拣货
	@Autowired
	protected BanQinOutboundBatchCancelPickingAction outboundBatchCancelPickingAction;
	// 取消分配
	@Autowired
	protected BanQinOutboundBatchCancelAllocAction outboundBatchCancelAllocAction;
	// 取消预配
	@Autowired
	protected BanQinOutboundBatchCancelPreallocAction outboundBatchCancelPreallocAction;

    /**
     * 批量拦截
     * @param soNos
     * @return
     */
	public ResultMessage outboundBatchIntercept(String[] soNos, String orgId) {
        ResultMessage msg = new ResultMessage();
		// 获取出库单明细行
		for (String soNo : soNos) {
			try {
				// 获取出库单明细，过滤00、99
				List<BanQinWmSoDetail> items = wmSoDetailService.findInterceptBySoNo(new String[]{soNo}, orgId);
				// 按出库单明细
                ResultMessage message = outboundBatchInterceptForSoDetail(items);
				if (!message.isSuccess()) {
					msg.addMessage(message.getMessage());
					msg.setSuccess(false);
				}
				// 出库单状态更新，取消状态、拦截状态更新
				wmSoHeaderService.cancelByIntercept(soNo, orgId);
			} catch (WarehouseException e) {
				msg.addMessage(e.getMessage());
				msg.setSuccess(false);
			}
		}
		return msg;
	}

    /**
     * 批量拦截 - 按出库单明细
     * @param items
     * @return
     */
	public ResultMessage outboundBatchInterceptForSoDetail(List<BanQinWmSoDetail> items) {
        ResultMessage msg = new ResultMessage();
		// 循环出库单明细
		for (BanQinWmSoDetail item : items) {
			List<String> noList = new ArrayList<>();
			noList.add("('" + item.getSoNo() + "','" + item.getLineNo() + "')");
			// 如果拣货数量>0,那么取消拣货
			if (item.getQtyPkEa() > 0) {
				// 按出库单号+行号 获取分配明细
				List<BanQinWmSoAllocEntity> wmSoAllocEntityList = wmSoAllocService.getEntityByIntercept(ProcessByCode.BY_SO_LINE.getCode(), noList, WmsCodeMaster.ALLOC_FULL_PICKING.getCode(), item.getOrgId());
                ResultMessage message = outboundBatchCancelPickingAction.outboundBatchCancelPicking(wmSoAllocEntityList);
				if (!message.isSuccess()) {
					msg.addMessage(message.getMessage());
				}
			}
			// 如果分配数量>0,那么取消分配
			if (item.getQtyAllocEa() > 0) {
				// 按出库单号+行号 获取分配明细
				List<BanQinWmSoAllocEntity> wmSoAllocEntityList = wmSoAllocService.getEntityByIntercept(ProcessByCode.BY_SO_LINE.getCode(), noList, WmsCodeMaster.ALLOC_FULL_ALLOC.getCode(), item.getOrgId());
                ResultMessage message = outboundBatchCancelAllocAction.outboundBatchCancelAlloc(wmSoAllocEntityList);
				if (!message.isSuccess()) {
					msg.addMessage(message.getMessage());
				}
			}
			// 如果预配数量>0,那么取消预配
			if (item.getQtyPreallocEa() > 0) {
				// 2、获取预配明细
				List<BanQinWmSoPreallocEntity> wmSoPreallocEntityList = wmSoPreallocService.getEntityByProcessByCode(ProcessByCode.BY_SO_LINE.getCode(), noList, item.getOrgId());
                ResultMessage message = outboundBatchCancelPreallocAction.outboundBatchCancelPrealloc(wmSoPreallocEntityList);
				if (!message.isSuccess()) {
					msg.addMessage(message.getMessage());
				}
			}
		}
		return msg;
	}
}