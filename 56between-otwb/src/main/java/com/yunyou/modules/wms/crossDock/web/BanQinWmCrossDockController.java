package com.yunyou.modules.wms.crossDock.web;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.wms.crossDock.entity.*;
import com.yunyou.modules.wms.crossDock.service.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 越库Controller
 * @author WMJ
 * @version 2020-02-21
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/crossDock/banQinWmCrossDock")
public class BanQinWmCrossDockController extends BaseController {
	@Autowired
	private BanQinCrossDockService crossDockService;
	@Autowired
	private BanQinWmCrossDockListService wmCrossDockListService;
	@Autowired
	private BanQinCrossDockBatchConfirmAction crossDockBatchConfirmAction;
	@Autowired
	private BanQinCrossDockBatchCancelConfirmAction crossDockBatchCancelConfirmAction;
	@Autowired
	private BanQinCrossDockBatchRemoveAction crossDockBatchRemoveAction;

	@ModelAttribute
	public BanQinWmCrossDockEntity init() {
		return new BanQinWmCrossDockEntity();
	}

	/**
	 * 越库列表页面
	 */
    @RequiresPermissions("crossDock:banQinWmCrossDock:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/wms/inbound/banQinWmCrossDockList";
	}
	
	/**
	 * 越库任务1列表数据
	 */
	@ResponseBody
	@RequestMapping(value = "data1")
	public Map<String, Object> data1(@RequestBody BanQinWmCrossDockEntity wmCrossDockEntity, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<BanQinWmCrossDockEntity> page = crossDockService.getEntityByQueryInfo(new Page(request, response), wmCrossDockEntity);
		return getBootstrapData(page);
	}

	/**
	 * 越库任务2列表数据
	 */
	@ResponseBody
	@RequestMapping(value = "data2")
	public Map<String, Object> data2(@RequestBody BanQinWmTaskCdByDirectQueryEntity queryEntity, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<BanQinWmAsnDetailReceiveQueryEntity> page = crossDockService.getTaskItemByQueryInfo(new Page(request, response), queryEntity);
		return getBootstrapData(page);
	}

	/**
	 * 越库入库单、出库单列表查询
	 */
	@ResponseBody
	@RequestMapping(value = "getWmCrossDockDetailEntity")
	public AjaxJson getWmCrossDockDetailEntity(@RequestBody BanQinWmCrossDockEntity entity, Model model) {
		AjaxJson j = new AjaxJson();
		try {
			ResultMessage msg = wmCrossDockListService.getCrossDockDetailByQuery(entity);
			j.setSuccess(msg.isSuccess());
			j.setMsg(msg.getMessage());
			LinkedHashMap<String, Object> map = new LinkedHashMap<>();
			BanQinWmCrossDockDetailEntity data = (BanQinWmCrossDockDetailEntity) msg.getData();
			Page asnPage = new Page();
			asnPage.setList(data.getWmAsnDetailReceiveEntity());
			map.put("asnList", getBootstrapData(asnPage));
			Page soPage = new Page();
			soPage.setList(data.getWmSoDetailEntity());
			map.put("soList", getBootstrapData(soPage));
			j.setBody(map);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg(e.getMessage());
		}
		return j;
	}

	/**
	 * 越库入库单、出库单列表查询
	 */
	@ResponseBody
	@RequestMapping(value = "getWmAsnReceiveEntity")
	public AjaxJson getWmAsnReceiveEntity(@RequestBody BanQinWmAsnDetailReceiveQueryEntity entity, Model model) {
		AjaxJson j = new AjaxJson();
		try {
			ResultMessage msg = crossDockService.getWmAsnReceiveEntity(entity);
			j.setSuccess(msg.isSuccess());
			j.setMsg(msg.getMessage());
			LinkedHashMap<String, Object> map = new LinkedHashMap<>();
			BanQinWmReceiveEntity data = (BanQinWmReceiveEntity) msg.getData();
			Page asnPage = new Page();
			asnPage.setList(data.getWmAsnDetailReceiveEntity());
			map.put("asnList", getBootstrapData(asnPage));
			Page soPage = new Page();
			soPage.setList(data.getWmSoDetailEntity());
			map.put("soList", getBootstrapData(soPage));
			Page allocPage = new Page();
			allocPage.setList(data.getWmSoAllocEntity());
			map.put("allocList", getBootstrapData(allocPage));
			j.setBody(map);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg(e.getMessage());
		}
		return j;
	}

	/**
	 * 生成越库任务(直接)
	 */
	@ResponseBody
	@RequestMapping(value = "crossDockCreateTask")
	public AjaxJson crossDockCreateTask(@RequestBody List<BanQinWmCrossDockEntity> list) {
		AjaxJson j = new AjaxJson();
		try {
			ResultMessage msg = crossDockService.crossDockCreateTask(list);
			j.setSuccess(msg.isSuccess());
			j.setMsg(msg.getMessage());
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg(e.getMessage());
		}
		return j;
	}

	/**
	 * 标记越库(分拨)
	 */
	@ResponseBody
	@RequestMapping(value = "crossDocCreateTaskBySkuInDirect")
	public AjaxJson crossDocCreateTaskBySkuInDirect(@RequestBody List<BanQinWmCrossDockEntity> list) {
		AjaxJson j = new AjaxJson();
		try {
			ResultMessage msg = crossDockService.crossDocCreateTaskBySkuInDirect(list);
			j.setSuccess(msg.isSuccess());
			j.setMsg(msg.getMessage());
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg(e.getMessage());
		}
		return j;
	}

	/**
	 * 生成越库任务(直接)
	 */
	@ResponseBody
	@RequestMapping(value = "crossDockCreateTaskDetail")
	public AjaxJson crossDockCreateTaskDetail(@RequestBody BanQinWmReceiveEntity entity) {
		AjaxJson j = new AjaxJson();
		try {
			ResultMessage msg = crossDockService.crossDockCreateTaskDetail(entity.getWmAsnDetailReceiveEntity(), entity.getWmSoDetailEntity(), entity.getOrgId());
			j.setSuccess(msg.isSuccess());
			j.setMsg(msg.getMessage());
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg(e.getMessage());
		}
		return j;
	}

	/**
	 * 标记越库(分拨)
	 */
	@ResponseBody
	@RequestMapping(value = "createTaskByInDirect")
	public AjaxJson createTaskByInDirect(@RequestBody BanQinWmReceiveEntity entity) {
		AjaxJson j = new AjaxJson();
		try {
			ResultMessage msg = crossDockService.createTaskByInDirect(entity.getWmAsnDetailReceiveEntity(), entity.getWmSoDetailEntity(), entity.getOrgId());
			j.setSuccess(msg.isSuccess());
			j.setMsg(msg.getMessage());
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg(e.getMessage());
		}
		return j;
	}

	/**
	 * 批量执行越库
	 */
	@ResponseBody
	@RequestMapping(value = "crossDockBatchConfirm")
	public AjaxJson crossDockBatchConfirm(@RequestBody List<BanQinWmAsnDetailReceiveQueryEntity> list) {
		AjaxJson j = new AjaxJson();
		try {
			ResultMessage msg = crossDockBatchConfirmAction.crossDockBatchConfirm(list);
			j.setSuccess(msg.isSuccess());
			j.setMsg(msg.getMessage());
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg(e.getMessage());
		}
		return j;
	}

	/**
	 * 批量取消越库
	 */
	@ResponseBody
	@RequestMapping(value = "crossDockCancelConfirm")
	public AjaxJson crossDockCancelConfirm(@RequestBody List<BanQinWmAsnDetailReceiveQueryEntity> list) {
		AjaxJson j = new AjaxJson();
		try {
			ResultMessage msg = crossDockBatchCancelConfirmAction.crossDockCancelConfirm(list);
			j.setSuccess(msg.isSuccess());
			j.setMsg(msg.getMessage());
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg(e.getMessage());
		}
		return j;
	}

	/**
	 * 删除越库任务
	 */
	@ResponseBody
	@RequestMapping(value = "crossDockRemoveByDirect")
	public AjaxJson crossDockRemoveByDirect(@RequestBody List<BanQinWmAsnDetailReceiveQueryEntity> list) {
		AjaxJson j = new AjaxJson();
		try {
			ResultMessage msg = crossDockBatchRemoveAction.crossDockRemoveByDirect(list);
			j.setSuccess(msg.isSuccess());
			j.setMsg(msg.getMessage());
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg(e.getMessage());
		}
		return j;
	}

	/**
	 * 删除越库任务
	 */
	@ResponseBody
	@RequestMapping(value = "crossDockRemove")
	public AjaxJson crossDockRemove(@RequestBody List<BanQinWmAsnDetailReceiveQueryEntity> list) {
		AjaxJson j = new AjaxJson();
		try {
			ResultMessage msg = crossDockBatchRemoveAction.crossDockRemove(list);
			j.setSuccess(msg.isSuccess());
			j.setMsg(msg.getMessage());
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg(e.getMessage());
		}
		return j;
	}

	/**
	 * 明细-执行越库
	 */
	@ResponseBody
	@RequestMapping(value = "crossDockConfirmDetail")
	public AjaxJson crossDockConfirmDetail(@RequestBody BanQinWmReceiveEntity entity) {
		AjaxJson j = new AjaxJson();
		try {
			ResultMessage msg = crossDockBatchConfirmAction.crossDockConfirmDetail(entity.getWmAsnDetailReceiveEntity(), entity.getWmSoDetailEntity());
			j.setSuccess(msg.isSuccess());
			j.setMsg(msg.getMessage());
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg(e.getMessage());
		}
		return j;
	}

	/**
	 * 明细-取消越库任务
	 */
	@ResponseBody
	@RequestMapping(value = "cancelRemarkDetail")
	public AjaxJson cancelRemarkDetail(@RequestBody BanQinWmReceiveEntity entity) {
		AjaxJson j = new AjaxJson();
		try {
			ResultMessage msg = crossDockBatchRemoveAction.cancelRemarkDetail(entity.getWmAsnDetailReceiveEntity(), entity.getWmSoDetailEntity());
			j.setSuccess(msg.isSuccess());
			j.setMsg(msg.getMessage());
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg(e.getMessage());
		}
		return j;
	}

}