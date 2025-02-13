package com.yunyou.modules.wms.task.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Maps;
import com.yunyou.common.ResultMessage;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.time.DateUtils;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.wms.task.entity.BanQinWmTaskRpEntity;
import com.yunyou.modules.wms.task.entity.extend.BanQinWmTaskRpExportEntity;
import com.yunyou.modules.wms.task.service.BanQinWmTaskRpService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;

/**
 * 补货任务Controller
 * @author WMJ
 * @version 2019-01-26
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/task/banQinWmTaskRp")
public class BanQinWmTaskRpController extends BaseController {
	@Autowired
	private BanQinWmTaskRpService banQinWmTaskRpService;
	
	@ModelAttribute
	public BanQinWmTaskRpEntity get(@RequestParam(required = false) String id) {
		BanQinWmTaskRpEntity entity = null;
		if (StringUtils.isNotBlank(id)) {
			entity = banQinWmTaskRpService.getEntity(id);
		}
		if (entity == null) {
			entity = new BanQinWmTaskRpEntity();
		}
		return entity;
	}
	
	/**
	 * 补货任务列表页面
	 */
	@RequiresPermissions("task:banQinWmTaskRp:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/wms/task/banQinWmTaskRpList";
	}
	
	/**
	 * 补货任务列表数据
	 */
	@ResponseBody
	@RequiresPermissions("task:banQinWmTaskRp:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(BanQinWmTaskRpEntity banQinWmTaskRpEntity, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<BanQinWmTaskRpEntity> page = banQinWmTaskRpService.findPage(new Page<BanQinWmTaskRpEntity>(request, response), banQinWmTaskRpEntity);
		return getBootstrapData(page);
	}

	/**
	 * 查看库存移动
	 */
	@RequiresPermissions("task:banQinWmTaskRp:view")
	@RequestMapping(value = "form")
	public String form(BanQinWmTaskRpEntity banQinWmTaskRpEntity, Model model) {
		model.addAttribute("banQinWmTaskRpEntity", banQinWmTaskRpEntity);
		Map<String, Object> map = Maps.newHashMap();
		map.put("lotAtt01", null != banQinWmTaskRpEntity.getLotAtt01() ? DateUtils.formatDate(banQinWmTaskRpEntity.getLotAtt01(), "yyyy-MM-dd") : "");
		map.put("lotAtt02", null != banQinWmTaskRpEntity.getLotAtt02() ? DateUtils.formatDate(banQinWmTaskRpEntity.getLotAtt02(), "yyyy-MM-dd") : "");
		map.put("lotAtt03", null != banQinWmTaskRpEntity.getLotAtt03() ? DateUtils.formatDate(banQinWmTaskRpEntity.getLotAtt03(), "yyyy-MM-dd") : "");
		map.put("lotAtt04", banQinWmTaskRpEntity.getLotAtt04());
		map.put("lotAtt05", banQinWmTaskRpEntity.getLotAtt05());
		map.put("lotAtt06", banQinWmTaskRpEntity.getLotAtt06());
		map.put("lotAtt07", banQinWmTaskRpEntity.getLotAtt07());
		map.put("lotAtt08", banQinWmTaskRpEntity.getLotAtt08());
		map.put("lotAtt09", banQinWmTaskRpEntity.getLotAtt09());
		map.put("lotAtt10", banQinWmTaskRpEntity.getLotAtt10());
		map.put("lotAtt11", banQinWmTaskRpEntity.getLotAtt11());
		map.put("lotAtt12", banQinWmTaskRpEntity.getLotAtt12());
		model.addAttribute("banQinWmTaskRp", JSON.toJSONString(map, SerializerFeature.WriteMapNullValue));

		return "modules/wms/task/banQinWmTaskRpForm";
	}

	/**
	 * 生成补货任务
	 */
	@ResponseBody
	@RequiresPermissions("task:banQinWmTaskRp:createTask")
	@RequestMapping(value = "createTask")
	public AjaxJson createTask(BanQinWmTaskRpEntity banQinWmTaskRpEntity) {
		AjaxJson j = new AjaxJson();
		try {
			ResultMessage msg = banQinWmTaskRpService.createTask(banQinWmTaskRpEntity.getOwnerCode(), banQinWmTaskRpEntity.getSkuCode(), banQinWmTaskRpEntity.getOrgId());
			j.setSuccess(msg.isSuccess());
			j.setMsg(msg.getMessage());
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg(e.getMessage());
		}
		return j;
	}

	/**
	 * 取消补货任务
	 */
	@ResponseBody
	@RequiresPermissions("task:banQinWmTaskRp:cancelTask")
	@RequestMapping(value = "cancelTask")
	public AjaxJson cancelTask(String ids) {
		AjaxJson j = new AjaxJson();
		try {
			ResultMessage msg = banQinWmTaskRpService.cancelTask(Arrays.asList(ids.split(",")));
			j.setSuccess(msg.isSuccess());
			j.setMsg(msg.getMessage());
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg(e.getMessage());
		}
		return j;
	}

	/**
	 * 补货确认
	 */
	@ResponseBody
	@RequiresPermissions("task:banQinWmTaskRp:confirmTask")
	@RequestMapping(value = "confirmTask")
	public AjaxJson confirmTask(String ids) {
		AjaxJson j = new AjaxJson();
		try {
			ResultMessage msg = banQinWmTaskRpService.confirmTask(Arrays.asList(ids.split(",")));
			j.setSuccess(msg.isSuccess());
			j.setMsg(msg.getMessage());
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg(e.getMessage());
		}
		return j;
	}

	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("task:banQinWmTaskRp:export")
	@RequestMapping(value = "export", method = RequestMethod.POST)
	public AjaxJson exportFile(BanQinWmTaskRpEntity entity, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
			String fileName = "补货任务" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			Page<BanQinWmTaskRpEntity> page = banQinWmTaskRpService.findPage(new Page<BanQinWmTaskRpEntity>(request, response, -1), entity);
			new ExportExcel("", BanQinWmTaskRpExportEntity.class).setDataList(page.getList()).write(response, fileName).dispose();
			j.setSuccess(true);
			j.setMsg("导出成功！");
			return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出补货任务记录失败！失败信息：" + e.getMessage());
		}
		return j;
	}

}