package com.yunyou.modules.wms.basicdata.web;

import com.yunyou.common.json.AjaxJson;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdRuleQcClass;
import com.yunyou.modules.wms.basicdata.service.BanQinCdRuleQcClassService;
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
 * 质检规则级差Controller
 * @author WMJ
 * @version 2019-01-25
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/basicdata/banQinCdRuleQcClass")
public class BanQinCdRuleQcClassController extends BaseController {

	@Autowired
	private BanQinCdRuleQcClassService banQinCdRuleQcClassService;
	
    /**
	 * 级差明细列表数据
	 */
	@ResponseBody
	@RequestMapping(value = "data")
	public Map<String, Object> data(BanQinCdRuleQcClass banQinCdRuleQcClass, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<BanQinCdRuleQcClass> page = banQinCdRuleQcClassService.findPage(new Page<BanQinCdRuleQcClass>(request, response), banQinCdRuleQcClass); 
		return getBootstrapData(page);
	}

	/**
	 * 保存级差明细
	 */
    @ResponseBody
	@RequiresPermissions("basicdata:banQinCdRuleQcClass:saveDetail")
	@RequestMapping(value = "save")
	public AjaxJson save(BanQinCdRuleQcClass banQinCdRuleQcClass, Model model, RedirectAttributes redirectAttributes) throws Exception{
        AjaxJson j = new AjaxJson();
        try {
			banQinCdRuleQcClassService.save(banQinCdRuleQcClass);
			j.setMsg("保存成功!");
        } catch (Exception e) {
			j.setSuccess(false);
			j.setMsg(e.getMessage());
		}
		return j;
	}
	
	/**
	 * 批量删除级差明细
	 */
	@ResponseBody
	@RequiresPermissions("basicdata:banQinCdRuleQcClass:removeDetail")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			banQinCdRuleQcClassService.delete(banQinCdRuleQcClassService.get(id));
		}
		j.setMsg("删除级差明细成功");
		return j;
	}
	
}