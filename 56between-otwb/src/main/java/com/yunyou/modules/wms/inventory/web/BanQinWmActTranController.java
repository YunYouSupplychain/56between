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
import com.yunyou.modules.wms.inventory.entity.BanQinWmActTran;
import com.yunyou.modules.wms.inventory.entity.extend.BanQinWmActTranExportEntity;
import com.yunyou.modules.wms.inventory.service.BanQinWmActTranService;
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
 * 库存交易Controller
 * @author WMJ
 * @version 2019-01-26
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/inventory/banQinWmActTran")
public class BanQinWmActTranController extends BaseController {

	@Autowired
	private BanQinWmActTranService banQinWmActTranService;
	
	@ModelAttribute
	public BanQinWmActTran get(@RequestParam(required=false) String id) {
		BanQinWmActTran entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = banQinWmActTranService.get(id);
		}
		if (entity == null){
			entity = new BanQinWmActTran();
		}
		return entity;
	}
	
	/**
	 * 库存交易列表页面
	 */
	@RequiresPermissions("inventory:banQinWmActTran:list")
	@RequestMapping(value = {"list", ""})
	public String list(BanQinWmActTran banQinWmActTran, Model model) {
        model.addAttribute("banQinWmActTran", banQinWmActTran);
		return "modules/wms/inventory/banQinWmActTranList";
	}
	
    /**
	 * 库存交易列表数据
	 */
	@ResponseBody
	@RequiresPermissions("inventory:banQinWmActTran:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(BanQinWmActTran banQinWmActTran, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<BanQinWmActTran> page = banQinWmActTranService.findPage(new Page<BanQinWmActTran>(request, response), banQinWmActTran); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑库存交易表单页面
	 */
	@RequiresPermissions(value={"inventory:banQinWmActTran:view","inventory:banQinWmActTran:add","inventory:banQinWmActTran:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(BanQinWmActTran banQinWmActTran, Model model) {
		model.addAttribute("banQinWmActTran", banQinWmActTran);
		if(StringUtils.isBlank(banQinWmActTran.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "modules/wms/inventory/banQinWmActTranForm";
	}

	/**
	 * 保存库存交易
	 */
	@RequiresPermissions(value={"inventory:banQinWmActTran:add","inventory:banQinWmActTran:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(BanQinWmActTran banQinWmActTran, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, banQinWmActTran)){
			return form(banQinWmActTran, model);
		}
		//新增或编辑表单保存
		banQinWmActTranService.save(banQinWmActTran);//保存
		addMessage(redirectAttributes, "保存库存交易成功");
		return "redirect:"+Global.getAdminPath()+"/wms/inventory/banQinWmActTran/?repage";
	}
	
	/**
	 * 删除库存交易
	 */
	@ResponseBody
	@RequiresPermissions("inventory:banQinWmActTran:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(BanQinWmActTran banQinWmActTran, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		banQinWmActTranService.delete(banQinWmActTran);
		j.setMsg("删除库存交易成功");
		return j;
	}
	
	/**
	 * 批量删除库存交易
	 */
	@ResponseBody
	@RequiresPermissions("inventory:banQinWmActTran:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			banQinWmActTranService.delete(banQinWmActTranService.get(id));
		}
		j.setMsg("删除库存交易成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("inventory:banQinWmActTran:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(BanQinWmActTran banQinWmActTran, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "库存交易"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<BanQinWmActTran> page = banQinWmActTranService.findPage(new Page<BanQinWmActTran>(request, response, -1), banQinWmActTran);
    		new ExportExcel("", BanQinWmActTranExportEntity.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出库存交易记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("inventory:banQinWmActTran:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<BanQinWmActTran> list = ei.getDataList(BanQinWmActTran.class);
			for (BanQinWmActTran banQinWmActTran : list){
				try{
					banQinWmActTranService.save(banQinWmActTran);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条库存交易记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条库存交易记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入库存交易失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/wms/inventory/banQinWmActTran/?repage";
    }
	
	/**
	 * 下载导入库存交易数据模板
	 */
	@RequiresPermissions("inventory:banQinWmActTran:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "库存交易数据导入模板.xlsx";
    		List<BanQinWmActTran> list = Lists.newArrayList(); 
    		new ExportExcel("库存交易数据", BanQinWmActTran.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/wms/inventory/banQinWmActTran/?repage";
    }

}