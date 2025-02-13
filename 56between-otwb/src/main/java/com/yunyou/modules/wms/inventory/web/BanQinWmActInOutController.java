package com.yunyou.modules.wms.inventory.web;

import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.wms.inventory.entity.BanQinWmActInOutEntity;
import com.yunyou.modules.wms.inventory.entity.BanQinWmActLogEntity;
import com.yunyou.modules.wms.inventory.service.BanQinWmActLogService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 进出存Controller
 * @author WMJ
 * @version 2020-04-23
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/inventory/banQinWmActInOut")
public class BanQinWmActInOutController extends BaseController {
	@Autowired
	private BanQinWmActLogService banQinWmActLogService;
	
	@ModelAttribute
	public BanQinWmActLogEntity init() {
		return new BanQinWmActLogEntity();
	}
	
	/**
	 * 业务库存列表页面
	 */
	@RequiresPermissions("inventory:banQinWmActInOut:list")
	@RequestMapping(value = {"list", ""})
	public String list(Model model) {
		return "modules/wms/inventory/banQinWmActInOutList";
	}
	
    /**
	 * 业务库存列表数据
	 */
	@ResponseBody
	@RequiresPermissions("inventory:banQinWmActInOut:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(BanQinWmActLogEntity entity, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<BanQinWmActInOutEntity> page = banQinWmActLogService.findInOutData(new Page<BanQinWmActInOutEntity>(request, response), entity);
		return getBootstrapData(page);
	}

}