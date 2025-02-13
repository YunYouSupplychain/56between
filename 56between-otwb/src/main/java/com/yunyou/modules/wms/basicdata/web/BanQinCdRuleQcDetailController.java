package com.yunyou.modules.wms.basicdata.web;

import com.yunyou.common.json.AjaxJson;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdRuleQcDetail;
import com.yunyou.modules.wms.basicdata.service.BanQinCdRuleQcDetailService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 质检处理意见Controller
 * @author WMJ
 * @version 2019-01-25
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/basicdata/banQinCdRuleQcDetail")
public class BanQinCdRuleQcDetailController extends BaseController {

	@Autowired
	private BanQinCdRuleQcDetailService banQinCdRuleQcDetailService;
	
    /**
	 * 质检处理意见列表数据
	 */
	@ResponseBody
	@RequestMapping(value = "data")
	public Map<String, Object> data(BanQinCdRuleQcDetail banQinCdRuleQcDetail, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<BanQinCdRuleQcDetail> page = banQinCdRuleQcDetailService.findPage(new Page<BanQinCdRuleQcDetail>(request, response), banQinCdRuleQcDetail); 
		return getBootstrapData(page);
	}

	/**
	 * 保存质检规则
	 */
	@ResponseBody
	@RequiresPermissions("basicdata:banQinCdRuleQcDetail:saveDetail")
	@RequestMapping(value = "save")
	public AjaxJson save(BanQinCdRuleQcDetail banQinCdRuleQcDetail, Model model, RedirectAttributes redirectAttributes) throws Exception{
        AjaxJson j = new AjaxJson();
        try {
			banQinCdRuleQcDetailService.save(banQinCdRuleQcDetail);
			j.setMsg("保存成功！");
        } catch (Exception e) {
			j.setSuccess(false);
			j.setMsg(e.getMessage());
		}

        return j;
	}
	
	/**
	 * 批量删除质检规则
	 */
	@ResponseBody
	@RequiresPermissions("basicdata:banQinCdRuleQcDetail:removeDetail")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			banQinCdRuleQcDetailService.delete(banQinCdRuleQcDetailService.get(id));
		}
		j.setMsg("删除成功");
		return j;
	}

}