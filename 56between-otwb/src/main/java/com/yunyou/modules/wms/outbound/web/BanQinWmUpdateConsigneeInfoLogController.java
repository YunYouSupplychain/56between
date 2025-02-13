package com.yunyou.modules.wms.outbound.web;

import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.wms.outbound.entity.BanQinWmUpdateConsigneeInfoLogEntity;
import com.yunyou.modules.wms.outbound.service.BanQinWmUpdateConsigneeInfoLogService;
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
 * 更新收货信息日志Controller
 * @author WMJ
 * @version 2020-03-19
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/outbound/banQinWmUpdateConsigneeInfoLog")
public class BanQinWmUpdateConsigneeInfoLogController extends BaseController {
	@Autowired
	private BanQinWmUpdateConsigneeInfoLogService wmUpdateConsigneeInfoLogService;

	@ModelAttribute
	public BanQinWmUpdateConsigneeInfoLogEntity init() {
		return new BanQinWmUpdateConsigneeInfoLogEntity();
	}

	@RequiresPermissions("outbound:banQinWmUpdateConsigneeInfoLog:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/wms/report/banQinWmUpdateConsigneeInfoLogList";
	}

	@ResponseBody
	@RequestMapping(value = "data")
	public Map<String, Object> data(BanQinWmUpdateConsigneeInfoLogEntity entity, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<BanQinWmUpdateConsigneeInfoLogEntity> page = wmUpdateConsigneeInfoLogService.findPage(new Page<BanQinWmUpdateConsigneeInfoLogEntity>(request, response), entity);
		return getBootstrapData(page);
	}

}