package com.yunyou.modules.wms.task.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.config.GlobalDataRule;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.sys.utils.UserUtils;
import com.yunyou.modules.wms.common.entity.WmsCodeMaster;
import com.yunyou.modules.wms.inventory.service.BanQinInvReplenishmentService;
import com.yunyou.modules.wms.task.entity.BanQinWmTaskRp;
import com.yunyou.modules.wms.task.entity.BanQinWmTaskRpEntity;
import com.yunyou.modules.wms.task.mapper.BanQinWmTaskRpMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 补货任务Service
 * @author WMJ
 * @version 2019-01-26
 */
@Service
@Transactional(readOnly = true)
public class BanQinWmTaskRpService extends CrudService<BanQinWmTaskRpMapper, BanQinWmTaskRp> {
	@Autowired
	@Lazy
	private BanQinInvReplenishmentService invReplenishmentService;

	public BanQinWmTaskRp get(String id) {
		return super.get(id);
	}

	public BanQinWmTaskRpEntity getEntity(String id) {
		return mapper.getEntity(id);
	}

	public List<BanQinWmTaskRp> findList(BanQinWmTaskRp banQinWmTaskRp) {
		return super.findList(banQinWmTaskRp);
	}
	
	public Page<BanQinWmTaskRpEntity> findPage(Page page, BanQinWmTaskRpEntity entity) {
		entity.setDataScope(GlobalDataRule.getDataRuleSql("a.org_id", entity.getOrgId()));
		dataRuleFilter(entity);
		entity.setPage(page);
		page.setList(mapper.findPage(entity));
		return page;
	}
	
	@Transactional
	public void save(BanQinWmTaskRp banQinWmTaskRp) {
		super.save(banQinWmTaskRp);
	}
	
	@Transactional
	public void delete(BanQinWmTaskRp banQinWmTaskRp) {
		super.delete(banQinWmTaskRp);
	}

	@Transactional
	public ResultMessage createTask(String ownerCode, String skuCode, String orgId) {
		return invReplenishmentService.createRpTask(ownerCode, skuCode, orgId);
	}

	@Transactional
	public ResultMessage cancelTask(List<String> ids) {
		ResultMessage msg = new ResultMessage();
		ResultMessage message = new ResultMessage();
		for (String id : ids) {
			message = cancelTask(id);
		}
		if (ids.size() == 1) {
			msg = message;
		} else {
			msg.setMessage("操作成功");
		}
		return msg;
	}

	@Transactional
	public ResultMessage cancelTask(String id) {
		ResultMessage msg = new ResultMessage();
		BanQinWmTaskRp wmTaskRpModel = get(id);
		if (wmTaskRpModel == null || WmsCodeMaster.TSK_COMPLETE.getCode().equals(wmTaskRpModel.getStatus())) {
			msg.setSuccess(false);
			msg.addMessage("任务不是新建状态");
		} else {
			invReplenishmentService.cancelRpTask(wmTaskRpModel);
			msg.setMessage("操作成功");
		}
		return msg;
	}

	@Transactional
	public ResultMessage confirmTask(List<String> ids) {
		ResultMessage msg = new ResultMessage();
		ResultMessage message = new ResultMessage();
		for (String id : ids) {
			message = confirmTask(id);
		}
		if (ids.size() == 1) {
			msg = message;
		} else {
			msg.setMessage("操作成功");
		}
		return msg;
	}

	@Transactional
	public ResultMessage confirmTask(String id) {
		ResultMessage msg = new ResultMessage();
		BanQinWmTaskRp wmTaskRpModel = get(id);
		wmTaskRpModel.setRpTime(new Date());
		wmTaskRpModel.setRpOp(UserUtils.getUser().getName());
		// 当任务为完成状态不能操作
		if (WmsCodeMaster.TSK_COMPLETE.getCode().equals(wmTaskRpModel.getStatus())) {
			msg.setSuccess(false);
			msg.setMessage("任务不是新建状态");
		} else {
			invReplenishmentService.completeRpTask(wmTaskRpModel);
			msg.setMessage("操作成功");
		}
		return msg;
	}

	public List<BanQinWmTaskRpEntity> rfRPGetRpDetailQuery(String rpId, String orgId) {
		return mapper.rfRPGetRpDetailQuery(rpId, orgId);
	}
	
}