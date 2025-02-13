package com.yunyou.modules.wms.basicdata.web;

import com.yunyou.common.json.AjaxJson;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdRuleAllocDetail;
import com.yunyou.modules.wms.basicdata.service.BanQinCdRuleAllocDetailService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 分配规则明细Controller
 * @author WMJ
 * @version 2019-01-25
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/basicdata/banQinCdRuleAllocDetail")
public class BanQinCdRuleAllocDetailController extends BaseController {

	@Autowired
	private BanQinCdRuleAllocDetailService banQinCdRuleAllocDetailService;
	
    /**
	 * 分配规则列表数据
	 */
	@ResponseBody
	@RequestMapping(value = "data")
	public Map<String, Object> data(BanQinCdRuleAllocDetail banQinCdRuleAllocDetail, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<BanQinCdRuleAllocDetail> page = banQinCdRuleAllocDetailService.findPage(new Page<BanQinCdRuleAllocDetail>(request, response), banQinCdRuleAllocDetail); 
		return getBootstrapData(page);
	}

	/**
	 * 保存分配规则
	 */
	@ResponseBody
	@RequiresPermissions("basicdata:banQinCdRuleAllocDetail:saveDetail")
	@RequestMapping(value = "save")
	public AjaxJson save(BanQinCdRuleAllocDetail banQinCdRuleAllocDetail, Model model, RedirectAttributes redirectAttributes) throws Exception{
        AjaxJson j = new AjaxJson();
	    if (!beanValidator(model, banQinCdRuleAllocDetail)){
			j.setSuccess(false);
			j.setMsg("非法数据!");
		}
		try {
            banQinCdRuleAllocDetailService.save(banQinCdRuleAllocDetail);
            j.setMsg("保存成功!");
        } catch (DuplicateKeyException e) {
	        j.setSuccess(false);
	        j.setMsg("行号不能重复！");
        } catch (Exception e) {
			j.setSuccess(false);
			j.setMsg(e.getMessage());
		}
        return j;
	}
	
	/**
	 * 批量删除分配规则
	 */
	@ResponseBody
	@RequiresPermissions("basicdata:banQinCdRuleAllocDetail:removeDetail")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			banQinCdRuleAllocDetailService.delete(banQinCdRuleAllocDetailService.get(id));
		}
		j.setMsg("删除分配规则明细成功");
		return j;
	}

}