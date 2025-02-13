package com.yunyou.modules.wms.inventory.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.sys.GenNoType;
import com.yunyou.modules.sys.service.SynchronizedNoService;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhSku;
import com.yunyou.modules.wms.basicdata.service.BanQinCdWhSkuService;
import com.yunyou.modules.wms.common.entity.WarehouseException;
import com.yunyou.modules.wms.common.entity.WmsCodeMaster;
import com.yunyou.modules.wms.inventory.entity.*;
import com.yunyou.modules.wms.inventory.mapper.BanQinWmTaskCountSerialMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 序列号盘点任务Service
 * @author WMJ
 * @version 2019-01-28
 */
@Service
@Transactional(readOnly = true)
public class BanQinWmTaskCountSerialService extends CrudService<BanQinWmTaskCountSerialMapper, BanQinWmTaskCountSerial> {
	@Autowired
	private BanQinInventoryService inventoryService;
	@Autowired
	@Lazy
	private BanQinWmCountHeaderService wmCountHeaderService;
	@Autowired
	private SynchronizedNoService noService;
	@Autowired
	private BanQinWmTaskCountService wmTaskCountService;
	@Autowired
	private BanQinCdWhSkuService cdWhSkuService;
	@Autowired
	private BanQinWmInvSerialService wmInvSerialService;

	public BanQinWmTaskCountSerial get(String id) {
		return super.get(id);
	}
	
	public List<BanQinWmTaskCountSerial> findList(BanQinWmTaskCountSerial banQinWmTaskCountSerial) {
		return super.findList(banQinWmTaskCountSerial);
	}
	
	public Page<BanQinWmTaskCountSerialEntity> findPage(Page page, BanQinWmTaskCountSerialEntity entity) {
		dataRuleFilter(entity);
		entity.setPage(page);
		page.setList(mapper.findPage(entity));
		return page;
	}
	
	@Transactional
	public void save(BanQinWmTaskCountSerial banQinWmTaskCountSerial) {
		super.save(banQinWmTaskCountSerial);
	}
	
	@Transactional
	public void delete(BanQinWmTaskCountSerial banQinWmTaskCountSerial) {
		super.delete(banQinWmTaskCountSerial);
	}

	public BanQinWmTaskCountSerial findFirst(BanQinWmTaskCountSerial model) {
		List<BanQinWmTaskCountSerial> list = this.findList(model);
		return CollectionUtil.isEmpty(list) ? null : list.get(0);
	}

	/**
	 * 序列号盘点，扫描序列号,盘点确认
	 */
	@Transactional
	public ResultMessage scanSerialNo(BanQinWmTaskCountSerialEntity wmTaskCountSerialEntity) {
		ResultMessage msg = new ResultMessage();
		// 当批次号为空时，根据批次属性获取批次号
		if (StringUtils.isEmpty(wmTaskCountSerialEntity.getLotNum())) {
			BanQinWmInvLotAtt wmInvLotAttModel = new BanQinWmInvLotAtt();
			BeanUtils.copyProperties(wmTaskCountSerialEntity, wmInvLotAttModel);
			String lotNum = inventoryService.createInvLotNum(wmInvLotAttModel);
			wmTaskCountSerialEntity.setLotNum(lotNum);
		}
		ResultMessage rstMsg = this.scanSerialNo_(wmTaskCountSerialEntity);
		if (rstMsg.isSuccess()) {
			msg.addMessage("操作成功");
			msg.setSuccess(true);
		} else {
			msg.addMessage(rstMsg.getMessage());
			msg.setSuccess(false);
		}
		return msg;
	}

	/**
	 * 序列号盘点，扫描序列号
	 */
	@Transactional
	public ResultMessage scanSerialNo_(BanQinWmTaskCountSerialEntity wmTaskCountSerialEntity) {
		ResultMessage msg = new ResultMessage();
		// 查找序列号盘点任务
		BanQinWmTaskCountSerial example = new BanQinWmTaskCountSerial();
		example.setCountNo(wmTaskCountSerialEntity.getCountNo());
		example.setOwnerCode(wmTaskCountSerialEntity.getOwnerCode());
		example.setSkuCode(wmTaskCountSerialEntity.getSkuCode());
		example.setLotNum(wmTaskCountSerialEntity.getLotNum());
		example.setSerialNo(wmTaskCountSerialEntity.getSerialNo());
		example.setOrgId(wmTaskCountSerialEntity.getOrgId());
		BanQinWmTaskCountSerial wmTaskCountSerial = this.findFirst(example);
		if (wmTaskCountSerial != null) {
			// 当序列号盘点任务状态为创建,且盘点结果为空，则更新盘点结果为匹配
			if (WmsCodeMaster.TSK_NEW.getCode().equals(wmTaskCountSerial.getStatus()) && wmTaskCountSerial.getCountResult() == null) {
				wmTaskCountSerial.setCountResult(WmsCodeMaster.MATCH.getCode());
				wmTaskCountSerial.setStatus(WmsCodeMaster.TSK_COMPLETE.getCode());
				wmTaskCountSerial.setLocCode(wmTaskCountSerialEntity.getLocCode());
				wmTaskCountSerial.setTraceId(wmTaskCountSerialEntity.getTraceId());
				this.save(wmTaskCountSerial);
				updateCountTask(wmTaskCountSerialEntity);
			} else {
				msg.setSuccess(false);
				msg.addMessage("不是新建状态");
				return msg;
			}
		} else {
			// 当序列号盘点任务为空时，插入一条序列盘点任务
			wmTaskCountSerial = new BanQinWmTaskCountSerial();
			wmTaskCountSerial.setCountNo(wmTaskCountSerialEntity.getCountNo());
			wmTaskCountSerial.setCountResult(WmsCodeMaster.PROFIT.getCode());
			wmTaskCountSerial.setLocCode(wmTaskCountSerialEntity.getLocCode());
			wmTaskCountSerial.setLotNum(wmTaskCountSerialEntity.getLotNum());
			wmTaskCountSerial.setOwnerCode(wmTaskCountSerialEntity.getOwnerCode());
			wmTaskCountSerial.setSerialNo(wmTaskCountSerialEntity.getSerialNo());
			wmTaskCountSerial.setSkuCode(wmTaskCountSerialEntity.getSkuCode());
			wmTaskCountSerial.setStatus(WmsCodeMaster.TSK_COMPLETE.getCode());
			wmTaskCountSerial.setTraceId(wmTaskCountSerialEntity.getTraceId());
			wmTaskCountSerial.setOrgId(wmTaskCountSerialEntity.getOrgId());
			wmTaskCountSerial.setHeaderId(wmTaskCountSerialEntity.getHeaderId());
			this.save(wmTaskCountSerial);
			updateCountTask(wmTaskCountSerialEntity);
		}
		BanQinWmTaskCountSerial condition = new BanQinWmTaskCountSerial();
		condition.setCountNo(wmTaskCountSerialEntity.getCountNo());
		condition.setStatus(WmsCodeMaster.TSK_NEW.getCode());
		condition.setOrgId(wmTaskCountSerialEntity.getOrgId());
		List<BanQinWmTaskCountSerial> items = this.findList(condition);
		if (items.size() > 0) {
			// 将盘点单更新为部分盘点
			wmCountHeaderService.updateStatusByCountNo(wmTaskCountSerialEntity.getCountNo(), WmsCodeMaster.CC_PART_COUNT.getCode(), wmTaskCountSerialEntity.getOrgId());
		} else {
			// 更新状态
			wmCountHeaderService.updateCountStatus(wmTaskCountSerialEntity.getCountNo(), wmTaskCountSerialEntity.getOrgId());
		}
		BeanUtils.copyProperties(wmTaskCountSerialEntity, wmTaskCountSerial);
		msg.setSuccess(true);
		return msg;
	}

	/**
	 * 序列号盘点，更新普通盘点任务
	 */
	@Transactional
	public double updateCountTask(BanQinWmTaskCountSerialEntity entity) {
		BanQinWmTaskCount wmTaskCountModel = wmTaskCountService.getTask(entity.getCountNo(), entity.getOwnerCode(), entity.getSkuCode(), entity.getLotNum(), entity.getLocCode(), entity.getTraceId(), entity.getOrgId());
		double countEa = 0d;
		// 当普通盘点任务不为空，则将普通盘点数量+1
		if (wmTaskCountModel != null) {
			wmTaskCountModel.setQtyCountEa(wmTaskCountModel.getQtyCountEa() + 1.0);
			wmTaskCountService.save(wmTaskCountModel);
			countEa = wmTaskCountModel.getQtyCountEa();
		} else {
			// 创建新的盘点任务
			BanQinWmTaskCount newTaskCountModel = new BanQinWmTaskCount();
			newTaskCountModel.setCountNo(entity.getCountNo());
			newTaskCountModel.setCountId(noService.getDocumentNo(GenNoType.WM_COUNT_ID.name()));
			newTaskCountModel.setLocCode(entity.getLocCode());
			newTaskCountModel.setLotNum(entity.getLotNum());
			newTaskCountModel.setOwnerCode(entity.getOwnerCode());
			newTaskCountModel.setQty(0.0);
			newTaskCountModel.setQtyCountEa(1.0);
			newTaskCountModel.setQtyDiff(0.0);
			newTaskCountModel.setSkuCode(entity.getSkuCode());
			newTaskCountModel.setStatus(WmsCodeMaster.TSK_NEW.getCode());
			newTaskCountModel.setTraceId(entity.getTraceId());
			newTaskCountModel.setOrgId(entity.getOrgId());
			newTaskCountModel.setHeaderId(entity.getHeaderId());
			// 获取商品的包装代码
			BanQinCdWhSku cdWhSkuModel = cdWhSkuService.getByOwnerAndSkuCode(entity.getOwnerCode(), entity.getSkuCode(), entity.getOrgId());
			newTaskCountModel.setPackCode(cdWhSkuModel.getPackCode());
			this.wmTaskCountService.save(newTaskCountModel);
			countEa = newTaskCountModel.getQtyCountEa();
		}
		return countEa;
	}

	/**
	 * 批量盘点确认
	 */
	@Transactional
	public ResultMessage batchComfirmCount(List<BanQinWmTaskCountSerialEntity> wmTaskCountSerialEntitys) {
		ResultMessage msg = new ResultMessage();
		for (BanQinWmTaskCountSerialEntity entity : wmTaskCountSerialEntitys) {
			try {
				ResultMessage rstMsg = this.scanSerialNo(entity);
				if (!rstMsg.isSuccess()) {
					msg.addMessage("[" + entity.getOwnerCode() + "][" + entity.getSkuCode() + "][" + entity.getSerialNo() + "]" + rstMsg.getMessage());
				}
			} catch (WarehouseException e) {
				msg.setSuccess(false);
				msg.addMessage(e.getMessage());
			}
		}
		if (StringUtils.isEmpty(msg.getMessage())) {
			msg.setSuccess(true);
			msg.addMessage("操作成功");
		} else {
			throw new RuntimeException(msg.getMessage());
		}
		return msg;
	}

	/**
	 * 序列号盘点 盘亏确认
	 */
	@Transactional
	public ResultMessage comfirmLoss(BanQinWmTaskCountSerialEntity wmTaskCountSerialEntity) {
		ResultMessage msg = new ResultMessage();
		ResultMessage rstMsg = this.comfirmLoss_(wmTaskCountSerialEntity);
		if (rstMsg.isSuccess()) {
			msg.addMessage("操作成功");
			msg.setSuccess(true);
		} else {
			msg.addMessage(rstMsg.getMessage());
			msg.setSuccess(false);
		}
		return msg;
	}

	/**
	 * 序列号盘点，扫描序列号
	 */
	@Transactional
	public ResultMessage comfirmLoss_(BanQinWmTaskCountSerialEntity entity) {
		ResultMessage msg = new ResultMessage();
		// 查找序列号盘点任务
		BanQinWmTaskCountSerial example = new BanQinWmTaskCountSerial();
		example.setCountNo(entity.getCountNo());
		example.setOwnerCode(entity.getOwnerCode());
		example.setSkuCode(entity.getSkuCode());
		example.setLotNum(entity.getLotNum());
		example.setSerialNo(entity.getSerialNo());
		example.setOrgId(entity.getOrgId());
		BanQinWmTaskCountSerial wmTaskCountSerial = this.findFirst(example);
		if (wmTaskCountSerial != null) {
			// 当序列号盘点任务状态为创建,且盘点结果为空，则更新盘点结果为匹配
			if (WmsCodeMaster.TSK_NEW.getCode().equals(wmTaskCountSerial.getStatus()) && wmTaskCountSerial.getCountResult() == null) {
				wmTaskCountSerial.setCountResult(WmsCodeMaster.LOSS.getCode());
				wmTaskCountSerial.setStatus(WmsCodeMaster.TSK_COMPLETE.getCode());
				this.save(wmTaskCountSerial);
			} else {
				msg.setSuccess(false);
				msg.addMessage("状态不是新建状态");
				return msg;
			}
		} else {
			msg.setSuccess(false);
			msg.addMessage("数据过期");
			return msg;
		}
		BanQinWmTaskCountSerial condition = new BanQinWmTaskCountSerial();
		condition.setCountNo(entity.getCountNo());
		condition.setStatus(WmsCodeMaster.TSK_NEW.getCode());
		condition.setOrgId(entity.getOrgId());
		List<BanQinWmTaskCountSerial> items = this.findList(condition);
		if (items.size() > 0) {
			// 将盘点单更新为部分盘点
			wmCountHeaderService.updateStatusByCountNo(entity.getCountNo(), WmsCodeMaster.CC_PART_COUNT.getCode(), entity.getOrgId());
		} else {
			// 更新状态
			wmCountHeaderService.updateCountStatus(entity.getCountNo(), entity.getOrgId());
		}
		msg.setSuccess(true);
		return msg;
	}

	/**
	 * 批量盘亏确认
	 */
	@Transactional
	public ResultMessage batchComfirmLoss(List<BanQinWmTaskCountSerialEntity> wmTaskCountSerialEntitys) {
		ResultMessage msg = new ResultMessage();
		for (BanQinWmTaskCountSerialEntity entity : wmTaskCountSerialEntitys) {
			ResultMessage rstMsg = this.comfirmLoss(entity);
			if (!rstMsg.isSuccess()) {
				msg.addMessage("[" + entity.getOwnerCode() + "][" + entity.getSkuCode() + "][" + entity.getSerialNo() + "]" + rstMsg.getMessage());
			}
		}
		if (StringUtils.isEmpty(msg.getMessage())) {
			msg.setSuccess(true);
			msg.addMessage("操作成功");
		}
		return msg;
	}

	/**
	 * 序列号盘点，删除序列号盘点任务
	 */
	@Transactional
	public ResultMessage deleteSerialTask(List<BanQinWmTaskCountSerialEntity> wmTaskCountSerialEntitys) {
		ResultMessage msg = new ResultMessage();
		for (BanQinWmTaskCountSerialEntity wmTaskCountSerialEntity : wmTaskCountSerialEntitys) {
			ResultMessage rstMsg = this.deleteSerTask_(wmTaskCountSerialEntity);
			if (!rstMsg.isSuccess()) {
				msg.addMessage(rstMsg.getMessage());
			}
		}
		if (StringUtils.isEmpty(msg.getMessage())) {
			msg.setSuccess(true);
			msg.addMessage("操作成功");
		}
		return msg;
	}

	/**
	 * 序列号盘点，删除序列号盘点任务
	 */
	@Transactional
	public ResultMessage deleteSerTask_(BanQinWmTaskCountSerialEntity entity) {
		ResultMessage msg = new ResultMessage();
		// 序列号库存中存在的序列号不允许删除
		BanQinWmInvSerial wmInvSerialModel = wmInvSerialService.getByOwnerCodeAndSkuCodeAndSerialNo(entity.getOwnerCode(), entity.getSkuCode(), entity.getSerialNo(), entity.getOrgId());
		BanQinWmTaskCountSerial example = new BanQinWmTaskCountSerial();
		example.setCountNo(entity.getCountNo());
		example.setOwnerCode(entity.getOwnerCode());
		example.setSkuCode(entity.getSkuCode());
		example.setLotNum(entity.getLotNum());
		example.setSerialNo(entity.getSerialNo());
		example.setOrgId(entity.getOrgId());
		BanQinWmTaskCountSerial wmTaskCountSerial = this.findFirst(example);
		if (wmTaskCountSerial == null) {
			msg.setSuccess(false);
			msg.addMessage("序列号不存在");
			return msg;
		}
		// 当序列号盘点任务为完成，对应的普通盘点任务的盘点数量也要-1
		if (WmsCodeMaster.TSK_COMPLETE.getCode().equals(wmTaskCountSerial.getStatus()) && !WmsCodeMaster.LOSS.getCode().equals(wmTaskCountSerial.getCountResult())) {
			BanQinWmTaskCount wmTaskCountModel = wmTaskCountService.getTask(entity.getCountNo(), entity.getOwnerCode(), entity.getSkuCode(), entity.getLotNum(), entity.getLocCode(), entity.getTraceId(), entity.getOrgId());
			wmTaskCountModel.setQtyCountEa(wmTaskCountModel.getQtyCountEa() - 1);
			if (WmsCodeMaster.TSK_COMPLETE.getCode().equals(wmTaskCountModel.getStatus())) {
				wmTaskCountModel.setQtyDiff(wmTaskCountModel.getQtyDiff() - 1);
			}
			wmTaskCountService.save(wmTaskCountModel);
		}

		if (wmInvSerialModel != null) {
			wmTaskCountSerial.setStatus(WmsCodeMaster.TSK_NEW.getCode());
			wmTaskCountSerial.setCountResult(null);
			wmTaskCountSerial.setLocCode(null);
			this.save(wmTaskCountSerial);
		} else {
			// 删除序列号盘点任务
			this.delete(wmTaskCountSerial);
		}
		BanQinWmTaskCountSerial condition = new BanQinWmTaskCountSerial();
		condition.setCountNo(entity.getCountNo());
		condition.setStatus(WmsCodeMaster.TSK_COMPLETE.getCode());
		condition.setOrgId(entity.getOrgId());
		List<BanQinWmTaskCountSerial> items = this.findList(condition);
		if (items.size() == 0) {
			// 更新状态
			wmCountHeaderService.updateCountStatus(entity.getCountNo(), entity.getOrgId());
		}

		return msg;
	}

}