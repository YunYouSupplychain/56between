package com.yunyou.modules.wms.inventory.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Maps;
import com.yunyou.common.ResultMessage;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.time.DateUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.wms.inventory.entity.BanQinWmInvMvEntity;
import com.yunyou.modules.wms.inventory.service.BanQinWmInvMvService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 库存移动Controller
 * @author WMJ
 * @version 2019-06-20
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/inventory/banQinWmInvMv")
public class BanQinWmInvMvController extends BaseController {
	@Autowired
	private BanQinWmInvMvService banQinWmInvMvService;

	@ModelAttribute
	public BanQinWmInvMvEntity init() {
		return new BanQinWmInvMvEntity();
	}

	/**
	 * 库存移动列表页面
	 */
	@RequiresPermissions("inventory:banQinWmInvMv:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/wms/inventory/banQinWmInvMvList";
	}
	
	/**
	 * 库存移动列表数据
	 */
	@ResponseBody
	@RequiresPermissions("inventory:banQinWmInvMv:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(BanQinWmInvMvEntity banQinWmInvMvEntity, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<BanQinWmInvMvEntity> page = banQinWmInvMvService.findPage(new Page<BanQinWmInvMvEntity>(request, response), banQinWmInvMvEntity);
		return getBootstrapData(page);
	}

	/**
	 * 查看库存移动
	 */
	@RequiresPermissions("inventory:banQinWmInvMv:view")
	@RequestMapping(value = "form")
	public String form(BanQinWmInvMvEntity banQinWmInvMvEntity, Model model) {
		model.addAttribute("banQinWmInvMvEntity", banQinWmInvMvEntity);
		Map<String, Object> map = Maps.newHashMap();
		map.put("lotAtt01", null != banQinWmInvMvEntity.getLotAtt01() ? DateUtils.formatDate(banQinWmInvMvEntity.getLotAtt01(), "yyyy-MM-dd") : "");
		map.put("lotAtt02", null != banQinWmInvMvEntity.getLotAtt02() ? DateUtils.formatDate(banQinWmInvMvEntity.getLotAtt02(), "yyyy-MM-dd") : "");
		map.put("lotAtt03", null != banQinWmInvMvEntity.getLotAtt03() ? DateUtils.formatDate(banQinWmInvMvEntity.getLotAtt03(), "yyyy-MM-dd") : "");
		map.put("lotAtt04", banQinWmInvMvEntity.getLotAtt04());
		map.put("lotAtt05", banQinWmInvMvEntity.getLotAtt05());
		map.put("lotAtt06", banQinWmInvMvEntity.getLotAtt06());
		map.put("lotAtt07", banQinWmInvMvEntity.getLotAtt07());
		map.put("lotAtt08", banQinWmInvMvEntity.getLotAtt08());
		map.put("lotAtt09", banQinWmInvMvEntity.getLotAtt09());
		map.put("lotAtt10", banQinWmInvMvEntity.getLotAtt10());
		map.put("lotAtt11", banQinWmInvMvEntity.getLotAtt11());
		map.put("lotAtt12", banQinWmInvMvEntity.getLotAtt12());
		model.addAttribute("banQinWmInvMv", JSON.toJSONString(map, SerializerFeature.WriteMapNullValue));

		return "modules/wms/inventory/banQinWmInvMvForm";
	}

	/**
	 * 执行移动
	 */
	@ResponseBody
	@RequiresPermissions("inventory:banQinWmInvMv:moveConfirm")
	@RequestMapping(value = "moveConfirm")
	public AjaxJson moveConfirm(BanQinWmInvMvEntity banQinWmInvMvEntity, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
			ResultMessage msg = banQinWmInvMvService.invMovement(banQinWmInvMvEntity);
			j.setSuccess(msg.isSuccess());
			j.setMsg(msg.getMessage());
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg(e.getMessage());
		}
		return j;
	}

}