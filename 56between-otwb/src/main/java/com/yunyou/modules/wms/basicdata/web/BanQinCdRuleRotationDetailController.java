package com.yunyou.modules.wms.basicdata.web;

import com.yunyou.common.json.AjaxJson;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdRuleRotationDetail;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdRuleRotationDetailEntity;
import com.yunyou.modules.wms.basicdata.service.BanQinCdRuleRotationDetailService;
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
 * 库存周转规则Controller
 * @author WMJ
 * @version 2019-01-25
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/basicdata/banQinCdRuleRotationDetail")
public class BanQinCdRuleRotationDetailController extends BaseController {
	@Autowired
	private BanQinCdRuleRotationDetailService banQinCdRuleRotationDetailService;
	
    /**
	 * 库存周转规则列表数据
	 */
	@ResponseBody
	@RequestMapping(value = "data")
	public Map<String, Object> data(BanQinCdRuleRotationDetailEntity banQinCdRuleRotationDetailEntity, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<BanQinCdRuleRotationDetailEntity> page = banQinCdRuleRotationDetailService.findPage(new Page<BanQinCdRuleRotationDetailEntity>(request, response), banQinCdRuleRotationDetailEntity); 
		return getBootstrapData(page);
	}

	/**
	 * 保存库存周转规则
	 */
	@ResponseBody
	@RequiresPermissions("basicdata:banQinCdRuleRotationDetail:saveDetail")
	@RequestMapping(value = "save")
	public AjaxJson save(BanQinCdRuleRotationDetail banQinCdRuleRotationDetail, Model model, RedirectAttributes redirectAttributes) throws Exception{
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, banQinCdRuleRotationDetail)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
			banQinCdRuleRotationDetailService.save(banQinCdRuleRotationDetail);
			j.setMsg("保存成功!");
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg(e.getMessage());
		}

		return j;
	}
	
	/**
	 * 批量删除库存周转规则
	 */
	@ResponseBody
	@RequiresPermissions("basicdata:banQinCdRuleRotationDetail:removeDetail")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			banQinCdRuleRotationDetailService.delete(banQinCdRuleRotationDetailService.get(id));
		}
		j.setMsg("删除库存周转规则成功");
		return j;
	}

}