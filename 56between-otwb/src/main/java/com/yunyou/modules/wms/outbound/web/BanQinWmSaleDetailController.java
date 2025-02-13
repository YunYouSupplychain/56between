package com.yunyou.modules.wms.outbound.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.yunyou.common.utils.time.DateUtils;
import com.yunyou.common.config.Global;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.common.utils.excel.ImportExcel;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSaleDetail;
import com.yunyou.modules.wms.outbound.service.BanQinWmSaleDetailService;

/**
 * 销售单明细Controller
 * @author WMJ
 * @version 2019-02-14
 */
@Controller
@RequestMapping(value = "${adminPath}/outbound/banQinWmSaleDetail")
public class BanQinWmSaleDetailController extends BaseController {

	@Autowired
	private BanQinWmSaleDetailService banQinWmSaleDetailService;
	
	@ModelAttribute
	public BanQinWmSaleDetail get(@RequestParam(required=false) String id) {
		BanQinWmSaleDetail entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = banQinWmSaleDetailService.get(id);
		}
		if (entity == null){
			entity = new BanQinWmSaleDetail();
		}
		return entity;
	}
	
	/**
	 * 销售单明细列表页面
	 */
	@RequiresPermissions("outbound:banQinWmSaleDetail:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "wms/outbound/banQinWmSaleDetailList";
	}
	
		/**
	 * 销售单明细列表数据
	 */
	@ResponseBody
	@RequiresPermissions("outbound:banQinWmSaleDetail:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(BanQinWmSaleDetail banQinWmSaleDetail, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<BanQinWmSaleDetail> page = banQinWmSaleDetailService.findPage(new Page<BanQinWmSaleDetail>(request, response), banQinWmSaleDetail); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑销售单明细表单页面
	 */
	@RequiresPermissions(value={"outbound:banQinWmSaleDetail:view","outbound:banQinWmSaleDetail:add","outbound:banQinWmSaleDetail:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(BanQinWmSaleDetail banQinWmSaleDetail, Model model) {
		model.addAttribute("banQinWmSaleDetail", banQinWmSaleDetail);
		if(StringUtils.isBlank(banQinWmSaleDetail.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "wms/outbound/banQinWmSaleDetailForm";
	}

	/**
	 * 保存销售单明细
	 */
	@RequiresPermissions(value={"outbound:banQinWmSaleDetail:add","outbound:banQinWmSaleDetail:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(BanQinWmSaleDetail banQinWmSaleDetail, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, banQinWmSaleDetail)){
			return form(banQinWmSaleDetail, model);
		}
		//新增或编辑表单保存
		banQinWmSaleDetailService.save(banQinWmSaleDetail);//保存
		addMessage(redirectAttributes, "保存销售单明细成功");
		return "redirect:"+Global.getAdminPath()+"/outbound/banQinWmSaleDetail/?repage";
	}
	
	/**
	 * 删除销售单明细
	 */
	@ResponseBody
	@RequiresPermissions("outbound:banQinWmSaleDetail:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(BanQinWmSaleDetail banQinWmSaleDetail, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		banQinWmSaleDetailService.delete(banQinWmSaleDetail);
		j.setMsg("删除销售单明细成功");
		return j;
	}
	
	/**
	 * 批量删除销售单明细
	 */
	@ResponseBody
	@RequiresPermissions("outbound:banQinWmSaleDetail:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			banQinWmSaleDetailService.delete(banQinWmSaleDetailService.get(id));
		}
		j.setMsg("删除销售单明细成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("outbound:banQinWmSaleDetail:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(BanQinWmSaleDetail banQinWmSaleDetail, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "销售单明细"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<BanQinWmSaleDetail> page = banQinWmSaleDetailService.findPage(new Page<BanQinWmSaleDetail>(request, response, -1), banQinWmSaleDetail);
    		new ExportExcel("销售单明细", BanQinWmSaleDetail.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出销售单明细记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("outbound:banQinWmSaleDetail:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<BanQinWmSaleDetail> list = ei.getDataList(BanQinWmSaleDetail.class);
			for (BanQinWmSaleDetail banQinWmSaleDetail : list){
				try{
					banQinWmSaleDetailService.save(banQinWmSaleDetail);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条销售单明细记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条销售单明细记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入销售单明细失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/outbound/banQinWmSaleDetail/?repage";
    }
	
	/**
	 * 下载导入销售单明细数据模板
	 */
	@RequiresPermissions("outbound:banQinWmSaleDetail:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "销售单明细数据导入模板.xlsx";
    		List<BanQinWmSaleDetail> list = Lists.newArrayList(); 
    		new ExportExcel("销售单明细数据", BanQinWmSaleDetail.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/outbound/banQinWmSaleDetail/?repage";
    }

}