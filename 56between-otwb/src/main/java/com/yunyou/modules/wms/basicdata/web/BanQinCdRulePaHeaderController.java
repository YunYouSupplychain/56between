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
import com.yunyou.modules.wms.basicdata.entity.BanQinCdRulePaHeader;
import com.yunyou.modules.wms.basicdata.service.BanQinCdRulePaHeaderService;
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
 * 上架规则Controller
 * @author WMJ
 * @version 2019-01-25
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/basicdata/banQinCdRulePaHeader")
public class BanQinCdRulePaHeaderController extends BaseController {

	@Autowired
	private BanQinCdRulePaHeaderService banQinCdRulePaHeaderService;
	
	@ModelAttribute
	public BanQinCdRulePaHeader get(@RequestParam(required=false) String id) {
		BanQinCdRulePaHeader entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = banQinCdRulePaHeaderService.get(id);
		}
		if (entity == null){
			entity = new BanQinCdRulePaHeader();
		}
		return entity;
	}
	
	/**
	 * 上架规则列表页面
	 */
	@RequiresPermissions("basicdata:banQinCdRulePaHeader:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/wms/basicdata/banQinCdRulePaHeaderList";
	}
	
    /**
	 * 上架规则列表数据
	 */
	@ResponseBody
	@RequiresPermissions("basicdata:banQinCdRulePaHeader:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(BanQinCdRulePaHeader banQinCdRulePaHeader, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<BanQinCdRulePaHeader> page = banQinCdRulePaHeaderService.findPage(new Page<BanQinCdRulePaHeader>(request, response), banQinCdRulePaHeader); 
		return getBootstrapData(page);
	}

	/**
	 * 上架规则弹出框数据
	 */
	@ResponseBody
	@RequestMapping(value = "grid")
	public Map<String, Object> grid(BanQinCdRulePaHeader banQinCdRulePaHeader, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<BanQinCdRulePaHeader> page = banQinCdRulePaHeaderService.findPage(new Page<BanQinCdRulePaHeader>(request, response), banQinCdRulePaHeader);
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑上架规则表单页面
	 */
	@RequiresPermissions(value={"basicdata:banQinCdRulePaHeader:view","basicdata:banQinCdRulePaHeader:add","basicdata:banQinCdRulePaHeader:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(BanQinCdRulePaHeader banQinCdRulePaHeader, Model model) {
		model.addAttribute("banQinCdRulePaHeader", banQinCdRulePaHeader);
		if(StringUtils.isBlank(banQinCdRulePaHeader.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "modules/wms/basicdata/banQinCdRulePaHeaderForm";
	}

	/**
	 * 保存上架规则
	 */
    @ResponseBody
	@RequiresPermissions(value={"basicdata:banQinCdRulePaHeader:add","basicdata:banQinCdRulePaHeader:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(BanQinCdRulePaHeader entity, Model model, RedirectAttributes redirectAttributes) throws Exception{
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, entity)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
			banQinCdRulePaHeaderService.save(entity);
            j.put("entity", entity);
        } catch (DuplicateKeyException e) {
            j.setSuccess(false);
            j.setMsg("规则编码已存在！");
        } catch (Exception e) {
        	j.setSuccess(false);
        	j.setMsg(e.getMessage());
		}

        return j;
	}
	
	/**
	 * 批量删除上架规则
	 */
	@ResponseBody
	@RequiresPermissions("basicdata:banQinCdRulePaHeader:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			banQinCdRulePaHeaderService.delete(banQinCdRulePaHeaderService.get(id));
		}
		j.setMsg("删除上架规则成功！");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("basicdata:banQinCdRulePaHeader:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(BanQinCdRulePaHeader banQinCdRulePaHeader, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "上架规则"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<BanQinCdRulePaHeader> page = banQinCdRulePaHeaderService.findPage(new Page<BanQinCdRulePaHeader>(request, response, -1), banQinCdRulePaHeader);
    		new ExportExcel("上架规则", BanQinCdRulePaHeader.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出上架规则记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("basicdata:banQinCdRulePaHeader:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<BanQinCdRulePaHeader> list = ei.getDataList(BanQinCdRulePaHeader.class);
			for (BanQinCdRulePaHeader banQinCdRulePaHeader : list){
				try{
					banQinCdRulePaHeaderService.save(banQinCdRulePaHeader);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条上架规则记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条上架规则记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入上架规则失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/wms/basicdata/banQinCdRulePaHeader/?repage";
    }
	
	/**
	 * 下载导入上架规则数据模板
	 */
	@RequiresPermissions("basicdata:banQinCdRulePaHeader:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "上架规则数据导入模板.xlsx";
    		List<BanQinCdRulePaHeader> list = Lists.newArrayList(); 
    		new ExportExcel("上架规则数据", BanQinCdRulePaHeader.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/wms/basicdata/banQinCdRulePaHeader/?repage";
    }

}