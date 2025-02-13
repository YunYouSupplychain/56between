package com.yunyou.modules.wms.inventory.web;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.time.DateUtils;
import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.wms.inventory.entity.BanQinWmBusinessInvEntity;
import com.yunyou.modules.wms.inventory.entity.extend.BanQinWmBusinessInvExportEntity;
import com.yunyou.modules.wms.inventory.service.BanQinWmBusinessInvService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 业务库存Controller
 * @author WMJ
 * @version 2020-04-26
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/inventory/banQinWmBusinessInv")
public class BanQinWmBusinessInvController extends BaseController {
	@Autowired
	private BanQinWmBusinessInvService wmBusinessInvService;
	
	@ModelAttribute
	public BanQinWmBusinessInvEntity init() {
		return new BanQinWmBusinessInvEntity();
	}
	
	/**
	 * 业务库存列表页面
	 */
	@RequiresPermissions("inventory:banQinWmBusinessInv:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/wms/inventory/banQinWmBusinessInvList";
	}
	
    /**
	 * 业务库存列表数据
	 */
	@ResponseBody
	@RequiresPermissions("inventory:banQinWmBusinessInv:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(BanQinWmBusinessInvEntity entity, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<BanQinWmBusinessInvEntity> page = wmBusinessInvService.findPage(new Page<BanQinWmBusinessInvEntity>(request, response), entity);
		return getBootstrapData(page);
	}

	/**
	 * 业务库存列表数据统计
	 */
	@ResponseBody
	@RequestMapping(value = "count")
	public BanQinWmBusinessInvEntity count(BanQinWmBusinessInvEntity entity, HttpServletRequest request, HttpServletResponse response, Model model) {
		return wmBusinessInvService.count(entity);
	}

	/**
	 * 结算
	 */
	@ResponseBody
	@RequiresPermissions("inventory:banQinWmBusinessInv:settle")
	@RequestMapping(value = "settle")
	public AjaxJson settle(String settleMonth, String orgId, HttpServletRequest request, HttpServletResponse response, Model model) {
		AjaxJson j = new AjaxJson();
		try {
			ResultMessage msg = wmBusinessInvService.settle(settleMonth, orgId);
			j.setSuccess(msg.isSuccess());
			j.setMsg(msg.getMessage());
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg(e.getMessage());
		}

		return j;
	}

	/**
	 * 重新计算并结算
	 */
	@ResponseBody
	@RequiresPermissions("inventory:banQinWmBusinessInv:reCalcAndSettle")
	@RequestMapping(value = "reCalcAndSettle")
	public AjaxJson reCalcAndSettle(BanQinWmBusinessInvEntity entity, HttpServletRequest request, HttpServletResponse response, Model model) {
		AjaxJson j = new AjaxJson();
		try {
			ResultMessage msg = wmBusinessInvService.reCalcAndSettle(entity);
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
	@RequiresPermissions("inventory:banQinWmBusinessInv:export")
	@RequestMapping(value = "export", method = RequestMethod.POST)
	public AjaxJson exportFile(BanQinWmBusinessInvEntity entity, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
			String fileName = "业务库存" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			Page<BanQinWmBusinessInvEntity> page = wmBusinessInvService.findPage(new Page<BanQinWmBusinessInvEntity>(request, response, -1), entity);
			new ExportExcel("", BanQinWmBusinessInvExportEntity.class).setDataList(page.getList()).write(response, fileName).dispose();
			j.setSuccess(true);
			j.setMsg("导出成功！");
			return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出业务库存记录失败！失败信息：" + e.getMessage());
		}
		return j;
	}

}