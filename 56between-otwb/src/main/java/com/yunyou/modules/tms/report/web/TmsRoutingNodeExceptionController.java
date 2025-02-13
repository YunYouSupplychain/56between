package com.yunyou.modules.tms.report.web;

import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.tms.report.entity.TmsRoutingNodeException;
import com.yunyou.modules.tms.report.service.TmsRoutingNodeExceptionService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 路由节点异常Controller
 * @author WMJ
 * @version 2020-03-23
 */
@Controller
@RequestMapping(value = "${adminPath}/tms/report/tmsRoutingNodeException")
public class TmsRoutingNodeExceptionController extends BaseController {
	@Autowired
	private TmsRoutingNodeExceptionService tmsRoutingNodeExceptionService;
	
	@ModelAttribute
	public TmsRoutingNodeException get(@RequestParam(required=false) String id) {
		return new TmsRoutingNodeException();
	}
	
	/**
	 * 路由节点异常列表页面
	 */
	@RequiresPermissions("tms:tmsRoutingNodeException:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/tms/tmsRoutingNodeExceptionList";
	}
	
	/**
	 *路由节点异常列表数据
	 */
	@ResponseBody
	@RequiresPermissions("tms:tmsRoutingNodeException:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(TmsRoutingNodeException exception, HttpServletRequest request, HttpServletResponse response) {
		return getBootstrapData(tmsRoutingNodeExceptionService.findPage(new Page<>(request, response), exception));
	}

}