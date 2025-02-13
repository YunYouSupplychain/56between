package com.yunyou.modules.wms.inventory.web;

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
import com.yunyou.modules.wms.inventory.entity.BanQinWmInvDailyTemp;
import com.yunyou.modules.wms.inventory.service.BanQinWmInvDailyTempService;

/**
 * 库存临时表Controller
 * @author WMJ
 * @version 2019-01-25
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/inventory/banQinWmInvDailyTemp")
public class BanQinWmInvDailyTempController extends BaseController {

	@Autowired
	private BanQinWmInvDailyTempService banQinWmInvDailyTempService;
	
	@ModelAttribute
	public BanQinWmInvDailyTemp get(@RequestParam(required=false) String id) {
		BanQinWmInvDailyTemp entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = banQinWmInvDailyTempService.get(id);
		}
		if (entity == null){
			entity = new BanQinWmInvDailyTemp();
		}
		return entity;
	}
	
	/**
	 * 库存临时表列表页面
	 */
	@RequiresPermissions("inventory:banQinWmInvDailyTemp:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/wms/inventory/banQinWmInvDailyTempList";
	}
	
		/**
	 * 库存临时表列表数据
	 */
	@ResponseBody
	@RequiresPermissions("inventory:banQinWmInvDailyTemp:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(BanQinWmInvDailyTemp banQinWmInvDailyTemp, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<BanQinWmInvDailyTemp> page = banQinWmInvDailyTempService.findPage(new Page<BanQinWmInvDailyTemp>(request, response), banQinWmInvDailyTemp); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑库存临时表表单页面
	 */
	@RequiresPermissions(value={"inventory:banQinWmInvDailyTemp:view","inventory:banQinWmInvDailyTemp:add","inventory:banQinWmInvDailyTemp:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(BanQinWmInvDailyTemp banQinWmInvDailyTemp, Model model) {
		model.addAttribute("banQinWmInvDailyTemp", banQinWmInvDailyTemp);
		if(StringUtils.isBlank(banQinWmInvDailyTemp.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "modules/wms/inventory/banQinWmInvDailyTempForm";
	}

	/**
	 * 保存库存临时表
	 */
	@RequiresPermissions(value={"inventory:banQinWmInvDailyTemp:add","inventory:banQinWmInvDailyTemp:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(BanQinWmInvDailyTemp banQinWmInvDailyTemp, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, banQinWmInvDailyTemp)){
			return form(banQinWmInvDailyTemp, model);
		}
		//新增或编辑表单保存
		banQinWmInvDailyTempService.save(banQinWmInvDailyTemp);//保存
		addMessage(redirectAttributes, "保存库存临时表成功");
		return "redirect:"+Global.getAdminPath()+"/wms/inventory/banQinWmInvDailyTemp/?repage";
	}
	
	/**
	 * 删除库存临时表
	 */
	@ResponseBody
	@RequiresPermissions("inventory:banQinWmInvDailyTemp:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(BanQinWmInvDailyTemp banQinWmInvDailyTemp, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		banQinWmInvDailyTempService.delete(banQinWmInvDailyTemp);
		j.setMsg("删除库存临时表成功");
		return j;
	}
	
	/**
	 * 批量删除库存临时表
	 */
	@ResponseBody
	@RequiresPermissions("inventory:banQinWmInvDailyTemp:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			banQinWmInvDailyTempService.delete(banQinWmInvDailyTempService.get(id));
		}
		j.setMsg("删除库存临时表成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("inventory:banQinWmInvDailyTemp:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(BanQinWmInvDailyTemp banQinWmInvDailyTemp, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "库存临时表"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<BanQinWmInvDailyTemp> page = banQinWmInvDailyTempService.findPage(new Page<BanQinWmInvDailyTemp>(request, response, -1), banQinWmInvDailyTemp);
    		new ExportExcel("库存临时表", BanQinWmInvDailyTemp.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出库存临时表记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("inventory:banQinWmInvDailyTemp:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<BanQinWmInvDailyTemp> list = ei.getDataList(BanQinWmInvDailyTemp.class);
			for (BanQinWmInvDailyTemp banQinWmInvDailyTemp : list){
				try{
					banQinWmInvDailyTempService.save(banQinWmInvDailyTemp);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条库存临时表记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条库存临时表记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入库存临时表失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/wms/inventory/banQinWmInvDailyTemp/?repage";
    }
	
	/**
	 * 下载导入库存临时表数据模板
	 */
	@RequiresPermissions("inventory:banQinWmInvDailyTemp:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "库存临时表数据导入模板.xlsx";
    		List<BanQinWmInvDailyTemp> list = Lists.newArrayList(); 
    		new ExportExcel("库存临时表数据", BanQinWmInvDailyTemp.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/wms/inventory/banQinWmInvDailyTemp/?repage";
    }

}