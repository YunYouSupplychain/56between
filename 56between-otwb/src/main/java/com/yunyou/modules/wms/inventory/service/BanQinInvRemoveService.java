package com.yunyou.modules.wms.inventory.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.modules.wms.common.entity.WarehouseException;
import com.yunyou.modules.wms.common.entity.WmsCodeMaster;
import com.yunyou.modules.wms.inventory.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 库存管理删除实现类
 * @author WMJ
 * @version 2019/03/05
 */
@Service
public class BanQinInvRemoveService {
	/**
	 * 调整单
	 */
	@Autowired
	protected BanQinWmAdHeaderService wmAdHeaderService;
	/**
	 * 调整单明细
	 */
	@Autowired
	protected BanQinWmAdDetailService wmAdDetailService;
	/**
	 * 盘点单
	 */
	@Autowired
	protected BanQinWmCountHeaderService wmCountHeaderService;
	/**
	 * 盘点任务
	 */
	@Autowired
	protected BanQinWmTaskCountService wmTaskCountService;
	/**
	 * 转移单
	 */
	@Autowired
	protected BanQinWmTfHeaderService wmTfHeaderService;
	/**
	 * 转移单明细
	 */
	@Autowired
	protected BanQinWmTfDetailService wmTfDetailService;

	@Autowired
	protected BanQinWmTfSerialService wmTfSerialService;

    /**
     * 删除调整单
     * @param id
     * @return
     * @throws WarehouseException
     */
    @Transactional
	public ResultMessage removeAd(String id) throws WarehouseException {
        ResultMessage msg = new ResultMessage();
		// 获取要删除的model
        BanQinWmAdHeader wmAdHeaderModel = wmAdHeaderService.get(id);
		if (wmAdHeaderModel == null) {
			msg.setData("数据过期");
			msg.setSuccess(false);
			return msg;
		}
		// 当订单状态不为创建的时候，不能进行删除
		if (!WmsCodeMaster.AD_NEW.getCode().equals(wmAdHeaderModel.getStatus())) {
			msg.setData(wmAdHeaderModel.getAdNo() + "订单状态不是创建状态");
			msg.setSuccess(false);
		} else if (WmsCodeMaster.AUDIT_CLOSE.getCode().equals(wmAdHeaderModel.getAuditStatus())) {
			msg.setData(wmAdHeaderModel.getAdNo() + "订单已审核");
			msg.setSuccess(false);
		} else {
			BanQinWmAdDetail detailExample = new BanQinWmAdDetail();
			detailExample.setAdNo(wmAdHeaderModel.getAdNo());
            detailExample.setOrgId(wmAdHeaderModel.getOrgId());
			List<BanQinWmAdDetail> wmAdDetailModels = wmAdDetailService.findList(detailExample);
			for (BanQinWmAdDetail wmAdDetailModel : wmAdDetailModels) {
				if (!WmsCodeMaster.AD_NEW.getCode().equals(wmAdDetailModel.getStatus())) {
					throw new WarehouseException(wmAdHeaderModel.getAdNo());
				}
				wmAdDetailService.delete(wmAdDetailModel);
			}
			wmAdHeaderService.delete(wmAdHeaderModel);
			msg.setSuccess(true);
		}
		return msg;
	}

    /**
     * 删除盘点单
     * @param countNo
     * @return
     * @throws WarehouseException
     */
    @Transactional
	public ResultMessage removeCount(String countNo, String orgId) throws WarehouseException {
        ResultMessage msg = new ResultMessage();
		// 获取要删除的model
		BanQinWmCountHeader example = new BanQinWmCountHeader();
		example.setCountNo(countNo);
		example.setOrgId(orgId);
        BanQinWmCountHeader wmCountHeaderModel = wmCountHeaderService.findFirst(example);
		// 当订单状态不为创建的时候，不能进行删除
		if (wmCountHeaderModel == null || !WmsCodeMaster.CC_NEW.getCode().equals(wmCountHeaderModel.getStatus())) {
			msg.setSuccess(false);
			return msg;
		} else {
			BanQinWmTaskCount taskCountExample = new BanQinWmTaskCount();
			taskCountExample.setCountNo(countNo);
            taskCountExample.setOrgId(orgId);
			List<BanQinWmTaskCount> wmTaskCountModels = wmTaskCountService.findList(taskCountExample);
			for (BanQinWmTaskCount wmTaskCountModel : wmTaskCountModels) {
				if (!WmsCodeMaster.CC_NEW.getCode().equals(wmTaskCountModel.getStatus())) {
					throw new WarehouseException("[" + wmTaskCountModel.getCountId() + "]不是新建状态");
				}
				wmTaskCountService.delete(wmTaskCountModel);
			}
			wmCountHeaderService.delete(wmCountHeaderModel);
			msg.setSuccess(true);
		}
		return msg;
	}

    /**
     * 删除转移单
     * @param id
     * @return
     */
    @Transactional
	public ResultMessage removeTf(String id) {
        ResultMessage msg = new ResultMessage();
		// 获取要删除的model
        BanQinWmTfHeader wmTfHeaderModel = wmTfHeaderService.get(id);
		if (null == wmTfHeaderModel) {
			msg.setSuccess(false);
			return msg;
		}
		// 当订单状态不为创建的时候，不能进行删除
		if (!WmsCodeMaster.TF_NEW.getCode().equals(wmTfHeaderModel.getStatus())) {
			msg.setMessage(wmTfHeaderModel.getTfNo() + "订单状态不是创建状态");
			msg.setSuccess(false);
		} else if (WmsCodeMaster.AUDIT_CLOSE.getCode().equals(wmTfHeaderModel.getAuditStatus())) {
			msg.setMessage(wmTfHeaderModel.getTfNo() + "订单已审核");
			msg.setSuccess(false);
		} else {
			BanQinWmTfDetail detailExample = new BanQinWmTfDetail();
			detailExample.setTfNo(wmTfHeaderModel.getTfNo());
            detailExample.setOrgId(wmTfHeaderModel.getOrgId());
			List<BanQinWmTfDetail> wmTfDetailModels = wmTfDetailService.findList(detailExample);
			for (BanQinWmTfDetail wmTfDetailModel : wmTfDetailModels) {
				wmTfDetailService.delete(wmTfDetailModel);
				// 删除序列号
				BanQinWmTfSerial wmTfSerialModel = new BanQinWmTfSerial();
				wmTfSerialModel.setTfNo(wmTfDetailModel.getTfNo());
				wmTfSerialModel.setLineNo(wmTfDetailModel.getLineNo());
                wmTfSerialModel.setOrgId(wmTfDetailModel.getOrgId());
				List<BanQinWmTfSerial> wmTfSerialModels = wmTfSerialService.findList(wmTfSerialModel);
				if (wmTfSerialModels != null) {
					wmTfSerialService.deleteAll(wmTfSerialModels);
				}
			}
			wmTfHeaderService.delete(wmTfHeaderModel);
			msg.setSuccess(true);
		}
		return msg;
	}

    /**
     * 删除转移单明细和序列号转移
     * @param id
     * @return
     */
    @Transactional
	public ResultMessage removeTfDetailById(String id) {
        ResultMessage msg = new ResultMessage();
        BanQinWmTfDetail wmTfDetailModel = this.wmTfDetailService.get(id);
		if (null == wmTfDetailModel) {
			msg.setSuccess(false);
			return msg;
		}
		if (WmsCodeMaster.TF_NEW.getCode().equals(wmTfDetailModel.getStatus())) {
			this.wmTfDetailService.delete(wmTfDetailModel);
			// 删除序列号
			BanQinWmTfSerial wmTfSerialModel = new BanQinWmTfSerial();
			wmTfSerialModel.setTfNo(wmTfDetailModel.getTfNo());
			wmTfSerialModel.setLineNo(wmTfDetailModel.getLineNo());
            wmTfSerialModel.setOrgId(wmTfDetailModel.getOrgId());
			List<BanQinWmTfSerial> wmTfSerialModels = wmTfSerialService.findList(wmTfSerialModel);
			if (CollectionUtil.isNotEmpty(wmTfSerialModels)) {
				wmTfSerialService.deleteAll(wmTfSerialModels);
			}
			msg.setSuccess(true);
		} else {
			msg.setSuccess(false);
		}
		return msg;
	}

}