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
import com.yunyou.modules.wms.inventory.entity.BanQinWmTfSerial;
import com.yunyou.modules.wms.inventory.service.BanQinWmTfSerialService;

/**
 * 序列号转移Controller
 * @author WMJ
 * @version 2019-01-28
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/inventory/banQinWmTfSerial")
public class BanQinWmTfSerialController extends BaseController {

	@Autowired
	private BanQinWmTfSerialService banQinWmTfSerialService;
	
	@ModelAttribute
	public BanQinWmTfSerial get(@RequestParam(required=false) String id) {
		BanQinWmTfSerial entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = banQinWmTfSerialService.get(id);
		}
		if (entity == null){
			entity = new BanQinWmTfSerial();
		}
		return entity;
	}
	
	/**
	 * 序列号转移列表页面
	 */
	@RequiresPermissions("inventory:banQinWmTfSerial:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/wms/inventory/banQinWmTfSerialList";
	}
	
		/**
	 * 序列号转移列表数据
	 */
	@ResponseBody
	@RequiresPermissions("inventory:banQinWmTfSerial:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(BanQinWmTfSerial banQinWmTfSerial, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<BanQinWmTfSerial> page = banQinWmTfSerialService.findPage(new Page<BanQinWmTfSerial>(request, response), banQinWmTfSerial); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑序列号转移表单页面
	 */
	@RequiresPermissions(value={"inventory:banQinWmTfSerial:view","inventory:banQinWmTfSerial:add","inventory:banQinWmTfSerial:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(BanQinWmTfSerial banQinWmTfSerial, Model model) {
		model.addAttribute("banQinWmTfSerial", banQinWmTfSerial);
		if(StringUtils.isBlank(banQinWmTfSerial.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "modules/wms/inventory/banQinWmTfSerialForm";
	}

	/**
	 * 保存序列号转移
	 */
	@RequiresPermissions(value={"inventory:banQinWmTfSerial:add","inventory:banQinWmTfSerial:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(BanQinWmTfSerial banQinWmTfSerial, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, banQinWmTfSerial)){
			return form(banQinWmTfSerial, model);
		}
		//新增或编辑表单保存
		banQinWmTfSerialService.save(banQinWmTfSerial);//保存
		addMessage(redirectAttributes, "保存序列号转移成功");
		return "redirect:"+Global.getAdminPath()+"/wms/inventory/banQinWmTfSerial/?repage";
	}
	
	/**
	 * 删除序列号转移
	 */
	@ResponseBody
	@RequiresPermissions("inventory:banQinWmTfSerial:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(BanQinWmTfSerial banQinWmTfSerial, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		banQinWmTfSerialService.delete(banQinWmTfSerial);
		j.setMsg("删除序列号转移成功");
		return j;
	}
	
	/**
	 * 批量删除序列号转移
	 */
	@ResponseBody
	@RequiresPermissions("inventory:banQinWmTfSerial:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			banQinWmTfSerialService.delete(banQinWmTfSerialService.get(id));
		}
		j.setMsg("删除序列号转移成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("inventory:banQinWmTfSerial:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(BanQinWmTfSerial banQinWmTfSerial, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "序列号转移"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<BanQinWmTfSerial> page = banQinWmTfSerialService.findPage(new Page<BanQinWmTfSerial>(request, response, -1), banQinWmTfSerial);
    		new ExportExcel("序列号转移", BanQinWmTfSerial.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出序列号转移记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("inventory:banQinWmTfSerial:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<BanQinWmTfSerial> list = ei.getDataList(BanQinWmTfSerial.class);
			for (BanQinWmTfSerial banQinWmTfSerial : list){
				try{
					banQinWmTfSerialService.save(banQinWmTfSerial);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条序列号转移记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条序列号转移记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入序列号转移失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/wms/inventory/banQinWmTfSerial/?repage";
    }
	
	/**
	 * 下载导入序列号转移数据模板
	 */
	@RequiresPermissions("inventory:banQinWmTfSerial:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "序列号转移数据导入模板.xlsx";
    		List<BanQinWmTfSerial> list = Lists.newArrayList(); 
    		new ExportExcel("序列号转移数据", BanQinWmTfSerial.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/wms/inventory/banQinWmTfSerial/?repage";
    }

}