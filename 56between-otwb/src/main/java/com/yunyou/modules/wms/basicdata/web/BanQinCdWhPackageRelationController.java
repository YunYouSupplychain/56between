package com.yunyou.modules.wms.basicdata.web;

import java.util.List;
import java.util.Map;

import com.yunyou.core.persistence.Page;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhPackage;
import com.yunyou.modules.wms.basicdata.service.BanQinCdWhPackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.yunyou.common.json.AjaxJson;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhPackageRelation;
import com.yunyou.modules.wms.basicdata.service.BanQinCdWhPackageRelationService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 包装明细Controller
 * @author WMJ
 * @version 2019-01-25
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/basicdata/banQinCdWhPackageRelation")
public class BanQinCdWhPackageRelationController extends BaseController {
	@Autowired
	private BanQinCdWhPackageRelationService banQinCdWhPackageRelationService;
	@Autowired
    private BanQinCdWhPackageService banQinCdWhPackageService;
	
    /**
     * 包装明细列表数据
     */
    @ResponseBody
    @RequestMapping(value = "data")
	public List<BanQinCdWhPackageRelation> data(BanQinCdWhPackageRelation banQinCdWhPackageRelation) {
	    return banQinCdWhPackageRelationService.findByPackage(banQinCdWhPackageRelation.getCdprCdpaPmCode(), banQinCdWhPackageRelation.getOrgId());
    }

    /**
     * 包装明细弹出框
     */
    @ResponseBody
    @RequestMapping(value = "grid")
    public Map<String, Object> grid(BanQinCdWhPackageRelation banQinCdWhPackageRelation, HttpServletRequest request, HttpServletResponse response, Model model) {
        BanQinCdWhPackage cdWhPackage = banQinCdWhPackageService.findByPackCode(banQinCdWhPackageRelation.getPackCode(), banQinCdWhPackageRelation.getOrgId());
        banQinCdWhPackageRelation.setCdprCdpaPmCode(null != cdWhPackage ? cdWhPackage.getPmCode() : "#");
        Page<BanQinCdWhPackageRelation> page = banQinCdWhPackageRelationService.findPage(new Page<>(request, response), banQinCdWhPackageRelation);
        return getBootstrapData(page);
    }
	
	/**
	 * 删除包装明细
	 */
	@ResponseBody
	@RequestMapping(value = "delete")
	public AjaxJson delete(BanQinCdWhPackageRelation banQinCdWhPackageRelation, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		banQinCdWhPackageRelationService.delete(banQinCdWhPackageRelation);
		j.setMsg("删除包装明细成功");
		return j;
	}
	
	/**
	 * 批量删除包装明细
	 */
	@ResponseBody
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			banQinCdWhPackageRelationService.delete(banQinCdWhPackageRelationService.get(id));
		}
		j.setMsg("删除包装明细成功");
		return j;
	}

    /**
     * 包装规格明细初始化
     */
    @ResponseBody
    @RequestMapping(value = "initialList")
    public List<BanQinCdWhPackageRelation> initialList(){
        return banQinCdWhPackageRelationService.initialList();
    }
	
}