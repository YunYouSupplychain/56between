package com.yunyou.modules.wms.outbound.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.modules.wms.outbound.entity.BanQinWmDelAllocEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.wms.outbound.entity.BanQinWmDelAlloc;
import com.yunyou.modules.wms.outbound.service.BanQinWmDelAllocService;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 取消分配拣货记录Controller
 * @author WMJ
 * @version 2019-02-14
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/outbound/banQinWmDelAlloc")
public class BanQinWmDelAllocController extends BaseController {
	@Autowired
	private BanQinWmDelAllocService banQinWmDelAllocService;

    /**
	 * 取消分配拣货记录列表数据
	 */
	@ResponseBody
	@RequestMapping(value = "data")
	public Map<String, Object> data(BanQinWmDelAlloc banQinWmDelAlloc, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<BanQinWmDelAlloc> page = banQinWmDelAllocService.findPage(new Page<BanQinWmDelAlloc>(request, response), banQinWmDelAlloc); 
		return getBootstrapData(page);
	}

	/**
	 * 取消拣货
	 * @param list
	 * @param redirectAttributes
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "cancelPick")
	public AjaxJson cancelPick(@RequestBody List<BanQinWmDelAllocEntity> list, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
			ResultMessage msg = banQinWmDelAllocService.cancelPickingByAlloc(list);
			j.setSuccess(msg.isSuccess());
			j.setMsg(msg.getMessage());
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg(e.getMessage());
		}
		return j;
	}
}