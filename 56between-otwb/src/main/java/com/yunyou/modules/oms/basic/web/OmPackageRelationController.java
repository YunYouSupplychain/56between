package com.yunyou.modules.oms.basic.web;

import com.yunyou.common.json.AjaxJson;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.oms.basic.entity.OmPackage;
import com.yunyou.modules.oms.basic.entity.OmPackageRelation;
import com.yunyou.modules.oms.basic.service.OmPackageRelationService;
import com.yunyou.modules.oms.basic.service.OmPackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 包装明细Controller
 * @author WMJ
 * @version 2019-04-19
 */
@Controller
@RequestMapping(value = "${adminPath}/oms/basic/omPackageRelation")
public class OmPackageRelationController extends BaseController {
	@Autowired
	private OmPackageRelationService omPackageRelationService;
	@Autowired
    private OmPackageService omPackageService;
	
    /**
     * 包装明细列表数据
     */
    @ResponseBody
    @RequestMapping(value = "data")
	public List<OmPackageRelation> data(OmPackageRelation omPackageRelation) {
	    return omPackageRelationService.findByPackage(omPackageRelation.getCdprCdpaPmCode(), omPackageRelation.getOrgId());
    }

    /**
     * 包装明细弹出框
     */
    @ResponseBody
    @RequestMapping(value = "grid")
    public Map<String, Object> grid(OmPackageRelation omPackageRelation, HttpServletRequest request, HttpServletResponse response, Model model) {
        OmPackage cdWhPackage = omPackageService.findByPackCode(omPackageRelation.getPackCode(), omPackageRelation.getOrgId());
        omPackageRelation.setCdprCdpaPmCode(null != cdWhPackage ? cdWhPackage.getPmCode() : "#");
        Page<OmPackageRelation> page = omPackageRelationService.findPage(new Page<>(request, response), omPackageRelation);
        return getBootstrapData(page);
    }
	
	/**
	 * 删除包装明细
	 */
	@ResponseBody
	@RequestMapping(value = "delete")
	public AjaxJson delete(OmPackageRelation omPackageRelation, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		omPackageRelationService.delete(omPackageRelation);
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
			omPackageRelationService.delete(omPackageRelationService.get(id));
		}
		j.setMsg("删除包装明细成功");
		return j;
	}

    /**
     * 包装规格明细初始化
     */
    @ResponseBody
    @RequestMapping(value = "initialList")
    public List<OmPackageRelation> initialList(){
        return omPackageRelationService.initialList();
    }
	
}