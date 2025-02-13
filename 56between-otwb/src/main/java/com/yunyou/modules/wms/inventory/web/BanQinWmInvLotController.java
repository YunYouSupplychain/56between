package com.yunyou.modules.wms.inventory.web;

import com.google.common.collect.Lists;
import com.yunyou.common.config.Global;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.time.DateUtils;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.common.utils.excel.ImportExcel;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.wms.inventory.entity.BanQinWmInvLot;
import com.yunyou.modules.wms.inventory.service.BanQinWmInvLotService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Map;

/**
 * 批次库存表Controller
 * @author WMJ
 * @version 2019-01-25
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/inventory/banQinWmInvLot")
public class BanQinWmInvLotController extends BaseController {

	@Autowired
	private BanQinWmInvLotService banQinWmInvLotService;
	
	@ModelAttribute
	public BanQinWmInvLot get(@RequestParam(required=false) String id) {
		BanQinWmInvLot entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = banQinWmInvLotService.get(id);
		}
		if (entity == null){
			entity = new BanQinWmInvLot();
		}
		return entity;
	}
	
	/**
	 * 批次库存表列表页面
	 */
	@RequiresPermissions("inventory:banQinWmInvLot:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/wms/inventory/banQinWmInvLotList";
	}
	
	/**
	 * 批次库存表列表数据
	 */
	@ResponseBody
	@RequiresPermissions("inventory:banQinWmInvLot:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(BanQinWmInvLot banQinWmInvLot, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<BanQinWmInvLot> page = banQinWmInvLotService.findPage(new Page<BanQinWmInvLot>(request, response), banQinWmInvLot); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑批次库存表表单页面
	 */
	@RequiresPermissions(value={"inventory:banQinWmInvLot:view","inventory:banQinWmInvLot:add","inventory:banQinWmInvLot:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(BanQinWmInvLot banQinWmInvLot, Model model) {
		model.addAttribute("banQinWmInvLot", banQinWmInvLot);
		if(StringUtils.isBlank(banQinWmInvLot.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "modules/wms/inventory/banQinWmInvLotForm";
	}

	/**
	 * 保存批次库存表
	 */
	@RequiresPermissions(value={"inventory:banQinWmInvLot:add","inventory:banQinWmInvLot:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(BanQinWmInvLot banQinWmInvLot, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, banQinWmInvLot)){
			return form(banQinWmInvLot, model);
		}
		//新增或编辑表单保存
		banQinWmInvLotService.save(banQinWmInvLot);//保存
		addMessage(redirectAttributes, "保存批次库存表成功");
		return "redirect:"+Global.getAdminPath()+"/wms/inventory/banQinWmInvLot/?repage";
	}
	
	/**
	 * 删除批次库存表
	 */
	@ResponseBody
	@RequiresPermissions("inventory:banQinWmInvLot:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(BanQinWmInvLot banQinWmInvLot, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		banQinWmInvLotService.delete(banQinWmInvLot);
		j.setMsg("删除批次库存表成功");
		return j;
	}
	
	/**
	 * 批量删除批次库存表
	 */
	@ResponseBody
	@RequiresPermissions("inventory:banQinWmInvLot:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			banQinWmInvLotService.delete(banQinWmInvLotService.get(id));
		}
		j.setMsg("删除批次库存表成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("inventory:banQinWmInvLot:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(BanQinWmInvLot banQinWmInvLot, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "批次库存表"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<BanQinWmInvLot> page = banQinWmInvLotService.findPage(new Page<BanQinWmInvLot>(request, response, -1), banQinWmInvLot);
    		new ExportExcel("批次库存表", BanQinWmInvLot.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出批次库存表记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("inventory:banQinWmInvLot:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<BanQinWmInvLot> list = ei.getDataList(BanQinWmInvLot.class);
			for (BanQinWmInvLot banQinWmInvLot : list){
				try{
					banQinWmInvLotService.save(banQinWmInvLot);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条批次库存表记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条批次库存表记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入批次库存表失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/wms/inventory/banQinWmInvLot/?repage";
    }
	
	/**
	 * 下载导入批次库存表数据模板
	 */
	@RequiresPermissions("inventory:banQinWmInvLot:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "批次库存表数据导入模板.xlsx";
    		List<BanQinWmInvLot> list = Lists.newArrayList(); 
    		new ExportExcel("批次库存表数据", BanQinWmInvLot.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/wms/inventory/banQinWmInvLot/?repage";
    }

}