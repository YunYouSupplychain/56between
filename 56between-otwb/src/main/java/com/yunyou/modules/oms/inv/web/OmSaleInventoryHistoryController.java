package com.yunyou.modules.oms.inv.web;

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
import com.yunyou.modules.oms.inv.entity.OmSaleInventoryHistory;
import com.yunyou.modules.oms.inv.service.OmSaleInventoryHistoryService;

/**
 * 销售库存履历Controller
 * @author zyf
 * @version 2020-01-06
 */
@Controller
@RequestMapping(value = "${adminPath}/oms/inv/omSaleInventoryHistory")
public class OmSaleInventoryHistoryController extends BaseController {

	@Autowired
	private OmSaleInventoryHistoryService omSaleInventoryHistoryService;
	
	@ModelAttribute
	public OmSaleInventoryHistory get(@RequestParam(required=false) String id) {
		OmSaleInventoryHistory entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = omSaleInventoryHistoryService.get(id);
		}
		if (entity == null){
			entity = new OmSaleInventoryHistory();
		}
		return entity;
	}
	
	/**
	 * 销售库存履历列表页面
	 */
	@RequiresPermissions("oms:inv:omSaleInventoryHistory:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/oms/inv/omSaleInventoryHistoryList";
	}
	
		/**
	 * 销售库存履历列表数据
	 */
	@ResponseBody
	@RequiresPermissions("oms:inv:omSaleInventoryHistory:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(OmSaleInventoryHistory omSaleInventoryHistory, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<OmSaleInventoryHistory> page = omSaleInventoryHistoryService.findPage(new Page<OmSaleInventoryHistory>(request, response), omSaleInventoryHistory); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑销售库存履历表单页面
	 */
	@RequiresPermissions(value={"oms:inv:omSaleInventoryHistory:view","oms:inv:omSaleInventoryHistory:add","oms:inv:omSaleInventoryHistory:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(OmSaleInventoryHistory omSaleInventoryHistory, Model model) {
		model.addAttribute("omSaleInventoryHistory", omSaleInventoryHistory);
		return "modules/oms/inv/omSaleInventoryHistoryForm";
	}

	/**
	 * 保存销售库存履历
	 */
	@ResponseBody
	@RequiresPermissions(value={"oms:inv:omSaleInventoryHistory:add","oms:inv:omSaleInventoryHistory:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(OmSaleInventoryHistory omSaleInventoryHistory, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, omSaleInventoryHistory)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		omSaleInventoryHistoryService.save(omSaleInventoryHistory);//新建或者编辑保存
		j.setSuccess(true);
		j.setMsg("保存销售库存履历成功");
		return j;
	}
	
	/**
	 * 删除销售库存履历
	 */
	@ResponseBody
	@RequiresPermissions("oms:inv:omSaleInventoryHistory:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(OmSaleInventoryHistory omSaleInventoryHistory, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		omSaleInventoryHistoryService.delete(omSaleInventoryHistory);
		j.setMsg("删除销售库存履历成功");
		return j;
	}
	
	/**
	 * 批量删除销售库存履历
	 */
	@ResponseBody
	@RequiresPermissions("oms:inv:omSaleInventoryHistory:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			omSaleInventoryHistoryService.delete(omSaleInventoryHistoryService.get(id));
		}
		j.setMsg("删除销售库存履历成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("oms:inv:omSaleInventoryHistory:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(OmSaleInventoryHistory omSaleInventoryHistory, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "销售库存履历"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<OmSaleInventoryHistory> page = omSaleInventoryHistoryService.findPage(new Page<OmSaleInventoryHistory>(request, response, -1), omSaleInventoryHistory);
    		new ExportExcel("销售库存履历", OmSaleInventoryHistory.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出销售库存履历记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("oms:inv:omSaleInventoryHistory:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<OmSaleInventoryHistory> list = ei.getDataList(OmSaleInventoryHistory.class);
			for (OmSaleInventoryHistory omSaleInventoryHistory : list){
				try{
					omSaleInventoryHistoryService.save(omSaleInventoryHistory);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条销售库存履历记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条销售库存履历记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入销售库存履历失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/oms/inv/omSaleInventoryHistory/?repage";
    }
	
	/**
	 * 下载导入销售库存履历数据模板
	 */
	@RequiresPermissions("oms:inv:omSaleInventoryHistory:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "销售库存履历数据导入模板.xlsx";
    		List<OmSaleInventoryHistory> list = Lists.newArrayList(); 
    		new ExportExcel("销售库存履历数据", OmSaleInventoryHistory.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/oms/inv/omSaleInventoryHistory/?repage";
    }

}