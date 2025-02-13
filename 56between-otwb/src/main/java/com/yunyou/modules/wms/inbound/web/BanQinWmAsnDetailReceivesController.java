package com.yunyou.modules.wms.inbound.web;

import com.google.common.collect.Lists;
import com.yunyou.common.config.Global;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.time.DateUtils;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.common.utils.excel.ImportExcel;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.wms.inbound.entity.BanQinWmAsnDetailReceives;
import com.yunyou.modules.wms.inbound.service.BanQinWmAsnDetailReceivesService;
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
 * 收货箱明细Controller
 * @author WMJ
 * @version 2019-01-25
 */
@Controller
@RequestMapping(value = "${adminPath}/inbound/banQinWmAsnDetailReceives")
public class BanQinWmAsnDetailReceivesController extends BaseController {

	@Autowired
	private BanQinWmAsnDetailReceivesService banQinWmAsnDetailReceivesService;
	
	@ModelAttribute
	public BanQinWmAsnDetailReceives get(@RequestParam(required=false) String id) {
		BanQinWmAsnDetailReceives entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = banQinWmAsnDetailReceivesService.get(id);
		}
		if (entity == null){
			entity = new BanQinWmAsnDetailReceives();
		}
		return entity;
	}
	
	/**
	 * 收货箱明细列表页面
	 */
	@RequiresPermissions("inbound:banQinWmAsnDetailReceives:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "wms/inbound/banQinWmAsnDetailReceivesList";
	}
	
		/**
	 * 收货箱明细列表数据
	 */
	@ResponseBody
	@RequiresPermissions("inbound:banQinWmAsnDetailReceives:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(BanQinWmAsnDetailReceives banQinWmAsnDetailReceives, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<BanQinWmAsnDetailReceives> page = banQinWmAsnDetailReceivesService.findPage(new Page<BanQinWmAsnDetailReceives>(request, response), banQinWmAsnDetailReceives); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑收货箱明细表单页面
	 */
	@RequiresPermissions(value={"inbound:banQinWmAsnDetailReceives:view","inbound:banQinWmAsnDetailReceives:add","inbound:banQinWmAsnDetailReceives:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(BanQinWmAsnDetailReceives banQinWmAsnDetailReceives, Model model) {
		model.addAttribute("banQinWmAsnDetailReceives", banQinWmAsnDetailReceives);
		if(StringUtils.isBlank(banQinWmAsnDetailReceives.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "wms/inbound/banQinWmAsnDetailReceivesForm";
	}

	/**
	 * 保存收货箱明细
	 */
	@RequiresPermissions(value={"inbound:banQinWmAsnDetailReceives:add","inbound:banQinWmAsnDetailReceives:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(BanQinWmAsnDetailReceives banQinWmAsnDetailReceives, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, banQinWmAsnDetailReceives)){
			return form(banQinWmAsnDetailReceives, model);
		}
		//新增或编辑表单保存
		banQinWmAsnDetailReceivesService.save(banQinWmAsnDetailReceives);//保存
		addMessage(redirectAttributes, "保存收货箱明细成功");
		return "redirect:"+Global.getAdminPath()+"/inbound/banQinWmAsnDetailReceives/?repage";
	}
	
	/**
	 * 删除收货箱明细
	 */
	@ResponseBody
	@RequiresPermissions("inbound:banQinWmAsnDetailReceives:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(BanQinWmAsnDetailReceives banQinWmAsnDetailReceives, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		banQinWmAsnDetailReceivesService.delete(banQinWmAsnDetailReceives);
		j.setMsg("删除收货箱明细成功");
		return j;
	}
	
	/**
	 * 批量删除收货箱明细
	 */
	@ResponseBody
	@RequiresPermissions("inbound:banQinWmAsnDetailReceives:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			banQinWmAsnDetailReceivesService.delete(banQinWmAsnDetailReceivesService.get(id));
		}
		j.setMsg("删除收货箱明细成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("inbound:banQinWmAsnDetailReceives:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(BanQinWmAsnDetailReceives banQinWmAsnDetailReceives, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "收货箱明细"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<BanQinWmAsnDetailReceives> page = banQinWmAsnDetailReceivesService.findPage(new Page<BanQinWmAsnDetailReceives>(request, response, -1), banQinWmAsnDetailReceives);
    		new ExportExcel("收货箱明细", BanQinWmAsnDetailReceives.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出收货箱明细记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("inbound:banQinWmAsnDetailReceives:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<BanQinWmAsnDetailReceives> list = ei.getDataList(BanQinWmAsnDetailReceives.class);
			for (BanQinWmAsnDetailReceives banQinWmAsnDetailReceives : list){
				try{
					banQinWmAsnDetailReceivesService.save(banQinWmAsnDetailReceives);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条收货箱明细记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条收货箱明细记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入收货箱明细失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/inbound/banQinWmAsnDetailReceives/?repage";
    }
	
	/**
	 * 下载导入收货箱明细数据模板
	 */
	@RequiresPermissions("inbound:banQinWmAsnDetailReceives:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "收货箱明细数据导入模板.xlsx";
    		List<BanQinWmAsnDetailReceives> list = Lists.newArrayList(); 
    		new ExportExcel("收货箱明细数据", BanQinWmAsnDetailReceives.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/inbound/banQinWmAsnDetailReceives/?repage";
    }

}