package com.yunyou.modules.wms.basicdata.web;

import com.google.common.collect.Lists;
import com.yunyou.common.config.Global;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.time.DateUtils;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.common.utils.excel.ImportExcel;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhPackage;
import com.yunyou.modules.wms.basicdata.service.BanQinCdWhPackageService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
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
 * 包装Controller
 * @author WMJ
 * @version 2019-01-25
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/basicdata/banQinCdWhPackage")
public class BanQinCdWhPackageController extends BaseController {

	@Autowired
	private BanQinCdWhPackageService banQinCdWhPackageService;
	
	@ModelAttribute
	public BanQinCdWhPackage get(@RequestParam(required=false) String id) {
		BanQinCdWhPackage entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = banQinCdWhPackageService.get(id);
		}
		if (entity == null){
			entity = new BanQinCdWhPackage();
		}
		return entity;
	}
	
	/**
	 * 包装列表页面
	 */
	@RequiresPermissions("basicdata:banQinCdWhPackage:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/wms/basicdata/banQinCdWhPackageList";
	}
	
    /**
	 * 包装列表数据
	 */
	@ResponseBody
	@RequiresPermissions("basicdata:banQinCdWhPackage:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(BanQinCdWhPackage banQinCdWhPackage, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<BanQinCdWhPackage> page = banQinCdWhPackageService.findPage(new Page<BanQinCdWhPackage>(request, response), banQinCdWhPackage); 
		return getBootstrapData(page);
	}

	/**
	 * 包装列表数据
	 */
	@ResponseBody
	@RequestMapping(value = "grid")
	public Map<String, Object> grid(BanQinCdWhPackage banQinCdWhPackage, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<BanQinCdWhPackage> page = banQinCdWhPackageService.findPage(new Page<BanQinCdWhPackage>(request, response), banQinCdWhPackage);
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑包装表单页面
	 */
	@RequiresPermissions(value={"basicdata:banQinCdWhPackage:view","basicdata:banQinCdWhPackage:add","basicdata:banQinCdWhPackage:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(BanQinCdWhPackage banQinCdWhPackage, Model model) {
		model.addAttribute("banQinCdWhPackage", banQinCdWhPackage);
		if(StringUtils.isBlank(banQinCdWhPackage.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "modules/wms/basicdata/banQinCdWhPackageForm";
	}

	/**
	 * 保存包装
	 */
	@RequiresPermissions(value={"basicdata:banQinCdWhPackage:add","basicdata:banQinCdWhPackage:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
    @ResponseBody
	public AjaxJson save(BanQinCdWhPackage banQinCdWhPackage, Model model, RedirectAttributes redirectAttributes) throws Exception{
        AjaxJson j = new AjaxJson();
	    if (!beanValidator(model, banQinCdWhPackage)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		try {
		    banQinCdWhPackageService.save(banQinCdWhPackage);
		    j.setMsg("保存成功！");
	    } catch (DuplicateKeyException e) {
	        j.setSuccess(false);
	        j.setMsg("包装编码重复！");
        } catch (Exception e) {
			j.setSuccess(false);
			j.setMsg(e.getMessage());
		}

	    return j;
	}
	
	/**
	 * 批量删除包装
	 */
	@ResponseBody
	@RequiresPermissions("basicdata:banQinCdWhPackage:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			banQinCdWhPackageService.delete(banQinCdWhPackageService.get(id));
		}
		j.setMsg("删除包装成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("basicdata:banQinCdWhPackage:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(BanQinCdWhPackage banQinCdWhPackage, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "包装"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<BanQinCdWhPackage> page = banQinCdWhPackageService.findPage(new Page<BanQinCdWhPackage>(request, response, -1), banQinCdWhPackage);
    		new ExportExcel("包装", BanQinCdWhPackage.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出包装记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("basicdata:banQinCdWhPackage:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<BanQinCdWhPackage> list = ei.getDataList(BanQinCdWhPackage.class);
			for (BanQinCdWhPackage banQinCdWhPackage : list){
				try{
					banQinCdWhPackageService.save(banQinCdWhPackage);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条包装记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条包装记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入包装失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/wms/basicdata/banQinCdWhPackage/?repage";
    }
	
	/**
	 * 下载导入包装数据模板
	 */
	@RequiresPermissions("basicdata:banQinCdWhPackage:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "包装数据导入模板.xlsx";
    		List<BanQinCdWhPackage> list = Lists.newArrayList(); 
    		new ExportExcel("包装数据", BanQinCdWhPackage.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/wms/basicdata/banQinCdWhPackage/?repage";
    }

}