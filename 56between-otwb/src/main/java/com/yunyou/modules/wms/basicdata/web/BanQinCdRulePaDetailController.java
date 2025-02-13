package com.yunyou.modules.wms.basicdata.web;

import com.yunyou.common.json.AjaxJson;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdRulePaDetail;
import com.yunyou.modules.wms.basicdata.service.BanQinCdRulePaDetailService;
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
 * 上架规则明细Controller
 * @author WMJ
 * @version 2019-01-25
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/basicdata/banQinCdRulePaDetail")
public class BanQinCdRulePaDetailController extends BaseController {
	@Autowired
	private BanQinCdRulePaDetailService banQinCdRulePaDetailService;
	
    /**
	 * 上架规则明细列表数据
	 */
	@ResponseBody
	@RequestMapping(value = "data")
	public Map<String, Object> data(BanQinCdRulePaDetail banQinCdRulePaDetail, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<BanQinCdRulePaDetail> page = banQinCdRulePaDetailService.findPage(new Page<BanQinCdRulePaDetail>(request, response), banQinCdRulePaDetail); 
		return getBootstrapData(page);
	}

	/**
	 * 保存上架规则明细
	 */
	@RequestMapping(value = "save")
	@RequiresPermissions("basicdata:banQinCdRulePaDetail:saveDetail")
    @ResponseBody
	public AjaxJson save(BanQinCdRulePaDetail banQinCdRulePaDetail, Model model, RedirectAttributes redirectAttributes) throws Exception{
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, banQinCdRulePaDetail)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            banQinCdRulePaDetailService.save(banQinCdRulePaDetail);
            j.setMsg("保存成功！");
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
	 * 批量删除上架规则明细
	 */
	@ResponseBody
	@RequiresPermissions("basicdata:banQinCdRulePaDetail:removeDetail")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			banQinCdRulePaDetailService.delete(new BanQinCdRulePaDetail(id));
		}
		j.setMsg("删除成功！");
		return j;
	}
	
}