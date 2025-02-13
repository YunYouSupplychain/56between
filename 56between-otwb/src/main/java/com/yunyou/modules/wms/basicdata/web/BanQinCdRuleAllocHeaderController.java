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
import com.yunyou.modules.wms.basicdata.entity.BanQinCdRuleAllocHeader;
import com.yunyou.modules.wms.basicdata.service.BanQinCdRuleAllocHeaderService;
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
 * 分配规则Controller
 * @author WMJ
 * @version 2019-01-25
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/basicdata/banQinCdRuleAllocHeader")
public class BanQinCdRuleAllocHeaderController extends BaseController {

	@Autowired
	private BanQinCdRuleAllocHeaderService banQinCdRuleAllocHeaderService;
	
	@ModelAttribute
	public BanQinCdRuleAllocHeader get(@RequestParam(required=false) String id) {
		BanQinCdRuleAllocHeader entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = banQinCdRuleAllocHeaderService.get(id);
		}
		if (entity == null){
			entity = new BanQinCdRuleAllocHeader();
		}
		return entity;
	}
	
	/**
	 * 分配规则列表页面
	 */
	@RequiresPermissions("basicdata:banQinCdRuleAllocHeader:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/wms/basicdata/banQinCdRuleAllocHeaderList";
	}
	
    /**
	 * 分配规则列表数据
	 */
	@ResponseBody
	@RequiresPermissions("basicdata:banQinCdRuleAllocHeader:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(BanQinCdRuleAllocHeader banQinCdRuleAllocHeader, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<BanQinCdRuleAllocHeader> page = banQinCdRuleAllocHeaderService.findPage(new Page<BanQinCdRuleAllocHeader>(request, response), banQinCdRuleAllocHeader); 
		return getBootstrapData(page);
	}

	/**
	 * 分配规则弹出框数据
	 */
	@ResponseBody
	@RequestMapping(value = "grid")
	public Map<String, Object> grid(BanQinCdRuleAllocHeader banQinCdRuleAllocHeader, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<BanQinCdRuleAllocHeader> page = banQinCdRuleAllocHeaderService.findPage(new Page<BanQinCdRuleAllocHeader>(request, response), banQinCdRuleAllocHeader);
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑分配规则表单页面
	 */
	@RequiresPermissions(value={"basicdata:banQinCdRuleAllocHeader:view","basicdata:banQinCdRuleAllocHeader:add","basicdata:banQinCdRuleAllocHeader:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(BanQinCdRuleAllocHeader banQinCdRuleAllocHeader, Model model) {
		model.addAttribute("banQinCdRuleAllocHeader", banQinCdRuleAllocHeader);
		if(StringUtils.isBlank(banQinCdRuleAllocHeader.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "modules/wms/basicdata/banQinCdRuleAllocHeaderForm";
	}

	/**
	 * 保存分配规则
	 */
	@RequiresPermissions(value={"basicdata:banQinCdRuleAllocHeader:add","basicdata:banQinCdRuleAllocHeader:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
    @ResponseBody
	public AjaxJson save(BanQinCdRuleAllocHeader entity, Model model, RedirectAttributes redirectAttributes) throws Exception{
        AjaxJson j = new AjaxJson();
	    if (!beanValidator(model, entity)){
	        j.setSuccess(false);
	        j.setMsg("非法数据!");
	        return j;
		}
		try {
            banQinCdRuleAllocHeaderService.save(entity);
            j.put("entity", entity);
        } catch (DuplicateKeyException e) {
	        j.setSuccess(false);
	        j.setMsg("规则编码已存在!");
        } catch (Exception e) {
			j.setSuccess(false);
			j.setMsg(e.getMessage());
		}

        return j;
	}
	
	/**
	 * 删除分配规则
	 */
	@ResponseBody
	@RequiresPermissions("basicdata:banQinCdRuleAllocHeader:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(BanQinCdRuleAllocHeader banQinCdRuleAllocHeader, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		banQinCdRuleAllocHeaderService.delete(banQinCdRuleAllocHeader);
		j.setMsg("删除分配规则成功");
		return j;
	}
	
	/**
	 * 批量删除分配规则
	 */
	@ResponseBody
	@RequiresPermissions("basicdata:banQinCdRuleAllocHeader:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			banQinCdRuleAllocHeaderService.delete(banQinCdRuleAllocHeaderService.get(id));
		}
		j.setMsg("删除分配规则成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("basicdata:banQinCdRuleAllocHeader:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(BanQinCdRuleAllocHeader banQinCdRuleAllocHeader, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "分配规则"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<BanQinCdRuleAllocHeader> page = banQinCdRuleAllocHeaderService.findPage(new Page<BanQinCdRuleAllocHeader>(request, response, -1), banQinCdRuleAllocHeader);
    		new ExportExcel("分配规则", BanQinCdRuleAllocHeader.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出分配规则记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("basicdata:banQinCdRuleAllocHeader:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<BanQinCdRuleAllocHeader> list = ei.getDataList(BanQinCdRuleAllocHeader.class);
			for (BanQinCdRuleAllocHeader banQinCdRuleAllocHeader : list){
				try{
					banQinCdRuleAllocHeaderService.save(banQinCdRuleAllocHeader);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条分配规则记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条分配规则记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入分配规则失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/wms/basicdata/banQinCdRuleAllocHeader/?repage";
    }
	
	/**
	 * 下载导入分配规则数据模板
	 */
	@RequiresPermissions("basicdata:banQinCdRuleAllocHeader:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "分配规则数据导入模板.xlsx";
    		List<BanQinCdRuleAllocHeader> list = Lists.newArrayList(); 
    		new ExportExcel("分配规则数据", BanQinCdRuleAllocHeader.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/wms/basicdata/banQinCdRuleAllocHeader/?repage";
    }

}