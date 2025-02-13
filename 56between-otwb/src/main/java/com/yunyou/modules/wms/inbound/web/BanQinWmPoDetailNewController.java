package com.yunyou.modules.wms.inbound.web;

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
import com.yunyou.modules.wms.inbound.entity.BanQinWmPoDetailNew;
import com.yunyou.modules.wms.inbound.service.BanQinWmPoDetailNewService;

/**
 * 采购单明细Controller
 * @author WMJ
 * @version 2019-01-26
 */
@Controller
@RequestMapping(value = "${adminPath}/inbound/banQinWmPoDetailNew")
public class BanQinWmPoDetailNewController extends BaseController {

	@Autowired
	private BanQinWmPoDetailNewService banQinWmPoDetailNewService;
	
	@ModelAttribute
	public BanQinWmPoDetailNew get(@RequestParam(required=false) String id) {
		BanQinWmPoDetailNew entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = banQinWmPoDetailNewService.get(id);
		}
		if (entity == null){
			entity = new BanQinWmPoDetailNew();
		}
		return entity;
	}
	
	/**
	 * 采购单明细列表页面
	 */
	@RequiresPermissions("inbound:banQinWmPoDetailNew:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "wms/inbound/banQinWmPoDetailNewList";
	}
	
		/**
	 * 采购单明细列表数据
	 */
	@ResponseBody
	@RequiresPermissions("inbound:banQinWmPoDetailNew:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(BanQinWmPoDetailNew banQinWmPoDetailNew, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<BanQinWmPoDetailNew> page = banQinWmPoDetailNewService.findPage(new Page<BanQinWmPoDetailNew>(request, response), banQinWmPoDetailNew); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑采购单明细表单页面
	 */
	@RequiresPermissions(value={"inbound:banQinWmPoDetailNew:view","inbound:banQinWmPoDetailNew:add","inbound:banQinWmPoDetailNew:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(BanQinWmPoDetailNew banQinWmPoDetailNew, Model model) {
		model.addAttribute("banQinWmPoDetailNew", banQinWmPoDetailNew);
		if(StringUtils.isBlank(banQinWmPoDetailNew.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "wms/inbound/banQinWmPoDetailNewForm";
	}

	/**
	 * 保存采购单明细
	 */
	@RequiresPermissions(value={"inbound:banQinWmPoDetailNew:add","inbound:banQinWmPoDetailNew:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(BanQinWmPoDetailNew banQinWmPoDetailNew, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, banQinWmPoDetailNew)){
			return form(banQinWmPoDetailNew, model);
		}
		//新增或编辑表单保存
		banQinWmPoDetailNewService.save(banQinWmPoDetailNew);//保存
		addMessage(redirectAttributes, "保存采购单明细成功");
		return "redirect:"+Global.getAdminPath()+"/inbound/banQinWmPoDetailNew/?repage";
	}
	
	/**
	 * 删除采购单明细
	 */
	@ResponseBody
	@RequiresPermissions("inbound:banQinWmPoDetailNew:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(BanQinWmPoDetailNew banQinWmPoDetailNew, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		banQinWmPoDetailNewService.delete(banQinWmPoDetailNew);
		j.setMsg("删除采购单明细成功");
		return j;
	}
	
	/**
	 * 批量删除采购单明细
	 */
	@ResponseBody
	@RequiresPermissions("inbound:banQinWmPoDetailNew:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			banQinWmPoDetailNewService.delete(banQinWmPoDetailNewService.get(id));
		}
		j.setMsg("删除采购单明细成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("inbound:banQinWmPoDetailNew:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(BanQinWmPoDetailNew banQinWmPoDetailNew, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "采购单明细"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<BanQinWmPoDetailNew> page = banQinWmPoDetailNewService.findPage(new Page<BanQinWmPoDetailNew>(request, response, -1), banQinWmPoDetailNew);
    		new ExportExcel("采购单明细", BanQinWmPoDetailNew.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出采购单明细记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("inbound:banQinWmPoDetailNew:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<BanQinWmPoDetailNew> list = ei.getDataList(BanQinWmPoDetailNew.class);
			for (BanQinWmPoDetailNew banQinWmPoDetailNew : list){
				try{
					banQinWmPoDetailNewService.save(banQinWmPoDetailNew);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条采购单明细记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条采购单明细记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入采购单明细失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/inbound/banQinWmPoDetailNew/?repage";
    }
	
	/**
	 * 下载导入采购单明细数据模板
	 */
	@RequiresPermissions("inbound:banQinWmPoDetailNew:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "采购单明细数据导入模板.xlsx";
    		List<BanQinWmPoDetailNew> list = Lists.newArrayList(); 
    		new ExportExcel("采购单明细数据", BanQinWmPoDetailNew.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/inbound/banQinWmPoDetailNew/?repage";
    }

}