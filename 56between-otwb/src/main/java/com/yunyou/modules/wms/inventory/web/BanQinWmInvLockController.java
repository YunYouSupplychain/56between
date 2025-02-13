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
import com.yunyou.modules.wms.inventory.entity.BanQinWmInvLock;
import com.yunyou.modules.wms.inventory.service.BanQinWmInvLockService;

/**
 * 库存操作悲观锁记录表Controller
 * @author WMJ
 * @version 2019-01-25
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/inventory/banQinWmInvLock")
public class BanQinWmInvLockController extends BaseController {

	@Autowired
	private BanQinWmInvLockService banQinWmInvLockService;
	
	@ModelAttribute
	public BanQinWmInvLock get(@RequestParam(required=false) String id) {
		BanQinWmInvLock entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = banQinWmInvLockService.get(id);
		}
		if (entity == null){
			entity = new BanQinWmInvLock();
		}
		return entity;
	}
	
	/**
	 * 库存操作悲观锁记录表列表页面
	 */
	@RequiresPermissions("inventory:banQinWmInvLock:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/wms/inventory/banQinWmInvLockList";
	}
	
		/**
	 * 库存操作悲观锁记录表列表数据
	 */
	@ResponseBody
	@RequiresPermissions("inventory:banQinWmInvLock:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(BanQinWmInvLock banQinWmInvLock, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<BanQinWmInvLock> page = banQinWmInvLockService.findPage(new Page<BanQinWmInvLock>(request, response), banQinWmInvLock); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑库存操作悲观锁记录表表单页面
	 */
	@RequiresPermissions(value={"inventory:banQinWmInvLock:view","inventory:banQinWmInvLock:add","inventory:banQinWmInvLock:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(BanQinWmInvLock banQinWmInvLock, Model model) {
		model.addAttribute("banQinWmInvLock", banQinWmInvLock);
		if(StringUtils.isBlank(banQinWmInvLock.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "modules/wms/inventory/banQinWmInvLockForm";
	}

	/**
	 * 保存库存操作悲观锁记录表
	 */
	@RequiresPermissions(value={"inventory:banQinWmInvLock:add","inventory:banQinWmInvLock:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(BanQinWmInvLock banQinWmInvLock, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, banQinWmInvLock)){
			return form(banQinWmInvLock, model);
		}
		//新增或编辑表单保存
		banQinWmInvLockService.save(banQinWmInvLock);//保存
		addMessage(redirectAttributes, "保存库存操作悲观锁记录表成功");
		return "redirect:"+Global.getAdminPath()+"/wms/inventory/banQinWmInvLock/?repage";
	}
	
	/**
	 * 删除库存操作悲观锁记录表
	 */
	@ResponseBody
	@RequiresPermissions("inventory:banQinWmInvLock:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(BanQinWmInvLock banQinWmInvLock, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		banQinWmInvLockService.delete(banQinWmInvLock);
		j.setMsg("删除库存操作悲观锁记录表成功");
		return j;
	}
	
	/**
	 * 批量删除库存操作悲观锁记录表
	 */
	@ResponseBody
	@RequiresPermissions("inventory:banQinWmInvLock:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			banQinWmInvLockService.delete(banQinWmInvLockService.get(id));
		}
		j.setMsg("删除库存操作悲观锁记录表成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("inventory:banQinWmInvLock:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(BanQinWmInvLock banQinWmInvLock, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "库存操作悲观锁记录表"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<BanQinWmInvLock> page = banQinWmInvLockService.findPage(new Page<BanQinWmInvLock>(request, response, -1), banQinWmInvLock);
    		new ExportExcel("库存操作悲观锁记录表", BanQinWmInvLock.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出库存操作悲观锁记录表记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("inventory:banQinWmInvLock:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<BanQinWmInvLock> list = ei.getDataList(BanQinWmInvLock.class);
			for (BanQinWmInvLock banQinWmInvLock : list){
				try{
					banQinWmInvLockService.save(banQinWmInvLock);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条库存操作悲观锁记录表记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条库存操作悲观锁记录表记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入库存操作悲观锁记录表失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/wms/inventory/banQinWmInvLock/?repage";
    }
	
	/**
	 * 下载导入库存操作悲观锁记录表数据模板
	 */
	@RequiresPermissions("inventory:banQinWmInvLock:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "库存操作悲观锁记录表数据导入模板.xlsx";
    		List<BanQinWmInvLock> list = Lists.newArrayList(); 
    		new ExportExcel("库存操作悲观锁记录表数据", BanQinWmInvLock.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/wms/inventory/banQinWmInvLock/?repage";
    }

}