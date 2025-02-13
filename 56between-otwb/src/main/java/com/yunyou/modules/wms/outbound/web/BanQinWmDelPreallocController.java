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
import com.yunyou.modules.wms.outbound.entity.BanQinWmDelPrealloc;
import com.yunyou.modules.wms.outbound.service.BanQinWmDelPreallocService;

/**
 * 取消预配记录Controller
 * @author WMJ
 * @version 2019-02-14
 */
@Controller
@RequestMapping(value = "${adminPath}/outbound/banQinWmDelPrealloc")
public class BanQinWmDelPreallocController extends BaseController {

	@Autowired
	private BanQinWmDelPreallocService banQinWmDelPreallocService;
	
	@ModelAttribute
	public BanQinWmDelPrealloc get(@RequestParam(required=false) String id) {
		BanQinWmDelPrealloc entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = banQinWmDelPreallocService.get(id);
		}
		if (entity == null){
			entity = new BanQinWmDelPrealloc();
		}
		return entity;
	}
	
	/**
	 * 取消预配记录列表页面
	 */
	@RequiresPermissions("outbound:banQinWmDelPrealloc:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "wms/outbound/banQinWmDelPreallocList";
	}
	
		/**
	 * 取消预配记录列表数据
	 */
	@ResponseBody
	@RequiresPermissions("outbound:banQinWmDelPrealloc:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(BanQinWmDelPrealloc banQinWmDelPrealloc, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<BanQinWmDelPrealloc> page = banQinWmDelPreallocService.findPage(new Page<BanQinWmDelPrealloc>(request, response), banQinWmDelPrealloc); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑取消预配记录表单页面
	 */
	@RequiresPermissions(value={"outbound:banQinWmDelPrealloc:view","outbound:banQinWmDelPrealloc:add","outbound:banQinWmDelPrealloc:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(BanQinWmDelPrealloc banQinWmDelPrealloc, Model model) {
		model.addAttribute("banQinWmDelPrealloc", banQinWmDelPrealloc);
		if(StringUtils.isBlank(banQinWmDelPrealloc.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "wms/outbound/banQinWmDelPreallocForm";
	}

	/**
	 * 保存取消预配记录
	 */
	@RequiresPermissions(value={"outbound:banQinWmDelPrealloc:add","outbound:banQinWmDelPrealloc:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(BanQinWmDelPrealloc banQinWmDelPrealloc, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, banQinWmDelPrealloc)){
			return form(banQinWmDelPrealloc, model);
		}
		//新增或编辑表单保存
		banQinWmDelPreallocService.save(banQinWmDelPrealloc);//保存
		addMessage(redirectAttributes, "保存取消预配记录成功");
		return "redirect:"+Global.getAdminPath()+"/outbound/banQinWmDelPrealloc/?repage";
	}
	
	/**
	 * 删除取消预配记录
	 */
	@ResponseBody
	@RequiresPermissions("outbound:banQinWmDelPrealloc:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(BanQinWmDelPrealloc banQinWmDelPrealloc, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		banQinWmDelPreallocService.delete(banQinWmDelPrealloc);
		j.setMsg("删除取消预配记录成功");
		return j;
	}
	
	/**
	 * 批量删除取消预配记录
	 */
	@ResponseBody
	@RequiresPermissions("outbound:banQinWmDelPrealloc:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			banQinWmDelPreallocService.delete(banQinWmDelPreallocService.get(id));
		}
		j.setMsg("删除取消预配记录成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("outbound:banQinWmDelPrealloc:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(BanQinWmDelPrealloc banQinWmDelPrealloc, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "取消预配记录"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<BanQinWmDelPrealloc> page = banQinWmDelPreallocService.findPage(new Page<BanQinWmDelPrealloc>(request, response, -1), banQinWmDelPrealloc);
    		new ExportExcel("取消预配记录", BanQinWmDelPrealloc.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出取消预配记录记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("outbound:banQinWmDelPrealloc:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<BanQinWmDelPrealloc> list = ei.getDataList(BanQinWmDelPrealloc.class);
			for (BanQinWmDelPrealloc banQinWmDelPrealloc : list){
				try{
					banQinWmDelPreallocService.save(banQinWmDelPrealloc);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条取消预配记录记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条取消预配记录记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入取消预配记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/outbound/banQinWmDelPrealloc/?repage";
    }
	
	/**
	 * 下载导入取消预配记录数据模板
	 */
	@RequiresPermissions("outbound:banQinWmDelPrealloc:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "取消预配记录数据导入模板.xlsx";
    		List<BanQinWmDelPrealloc> list = Lists.newArrayList(); 
    		new ExportExcel("取消预配记录数据", BanQinWmDelPrealloc.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/outbound/banQinWmDelPrealloc/?repage";
    }

}