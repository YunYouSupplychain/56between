package com.yunyou.modules.wms.customer.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
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
import com.yunyou.modules.wms.customer.entity.BanQinEbCustomer;
import com.yunyou.modules.wms.customer.service.BanQinEbCustomerService;

/**
 * 客户Controller
 * @author WMJ
 * @version 2019-01-25
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/customer/banQinEbCustomer")
public class BanQinEbCustomerController extends BaseController {

	@Autowired
	private BanQinEbCustomerService banQinEbCustomerService;
	
	@ModelAttribute
	public BanQinEbCustomer get(@RequestParam(required=false) String id) {
		BanQinEbCustomer entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = banQinEbCustomerService.get(id);
		}
		if (entity == null){
			entity = new BanQinEbCustomer();
		}
		return entity;
	}
	
	/**
	 * 客户列表页面
	 */
	@RequiresPermissions("customer:banQinEbCustomer:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/wms/customer/banQinEbCustomerList";
	}
	
    /**
	 * 客户列表数据
	 */
	@ResponseBody
	@RequiresPermissions("customer:banQinEbCustomer:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(BanQinEbCustomer banQinEbCustomer, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<BanQinEbCustomer> page = banQinEbCustomerService.findPage(new Page<BanQinEbCustomer>(request, response), banQinEbCustomer); 
		return getBootstrapData(page);
	}

	/**
	 * 客户弹出框数据
	 */
	@ResponseBody
	@RequestMapping(value = "grid")
	public Map<String, Object> grid(BanQinEbCustomer banQinEbCustomer, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<BanQinEbCustomer> page = banQinEbCustomerService.findPage(new Page<BanQinEbCustomer>(request, response), banQinEbCustomer);
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑客户表单页面
	 */
	@RequiresPermissions(value={"customer:banQinEbCustomer:view","customer:banQinEbCustomer:add","customer:banQinEbCustomer:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(BanQinEbCustomer banQinEbCustomer, Model model) {
		model.addAttribute("banQinEbCustomer", banQinEbCustomer);
		if(StringUtils.isBlank(banQinEbCustomer.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "modules/wms/customer/banQinEbCustomerForm";
	}

	/**
	 * 保存客户
	 */
    @ResponseBody
	@RequiresPermissions(value={"customer:banQinEbCustomer:add","customer:banQinEbCustomer:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(BanQinEbCustomer banQinEbCustomer, Model model, RedirectAttributes redirectAttributes) throws Exception{
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, banQinEbCustomer)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            banQinEbCustomerService.save(banQinEbCustomer);
            j.setMsg("保存客户成功");
        } catch (DuplicateKeyException e) {
            j.setSuccess(false);
            j.setMsg("客户编码重复！");
        }
        return j;
	}
	
	/**
	 * 批量删除客户
	 */
	@ResponseBody
	@RequiresPermissions("customer:banQinEbCustomer:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			banQinEbCustomerService.delete(banQinEbCustomerService.get(id));
		}
		j.setMsg("删除客户成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("customer:banQinEbCustomer:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(BanQinEbCustomer banQinEbCustomer, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "客户"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<BanQinEbCustomer> page = banQinEbCustomerService.findPage(new Page<BanQinEbCustomer>(request, response, -1), banQinEbCustomer);
    		new ExportExcel("客户", BanQinEbCustomer.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出客户记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("customer:banQinEbCustomer:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<BanQinEbCustomer> list = ei.getDataList(BanQinEbCustomer.class);
			for (BanQinEbCustomer banQinEbCustomer : list){
				try{
					banQinEbCustomerService.save(banQinEbCustomer);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条客户记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条客户记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入客户失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/wms/customer/banQinEbCustomer/?repage";
    }
	
	/**
	 * 下载导入客户数据模板
	 */
	@RequiresPermissions("customer:banQinEbCustomer:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "客户数据导入模板.xlsx";
    		List<BanQinEbCustomer> list = Lists.newArrayList(); 
    		new ExportExcel("客户数据", BanQinEbCustomer.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/wms/customer/banQinEbCustomer/?repage";
    }

}