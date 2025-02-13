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
import com.yunyou.modules.wms.basicdata.entity.BanQinCdRuleRotationHeader;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdRuleRotationHeaderEntity;
import com.yunyou.modules.wms.basicdata.service.BanQinCdRuleRotationHeaderService;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 库存周转规则Controller
 * @author WMJ
 * @version 2019-01-25
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/basicdata/banQinCdRuleRotationHeader")
public class BanQinCdRuleRotationHeaderController extends BaseController {

	@Autowired
	private BanQinCdRuleRotationHeaderService banQinCdRuleRotationHeaderService;
	
	@ModelAttribute
	public BanQinCdRuleRotationHeaderEntity get(@RequestParam(required=false) String id) {
		BanQinCdRuleRotationHeaderEntity entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = banQinCdRuleRotationHeaderService.getEntity(id);
		}
		if (entity == null){
			entity = new BanQinCdRuleRotationHeaderEntity();
		}
		return entity;
	}
	
	/**
	 * 库存周转规则列表页面
	 */
	@RequiresPermissions("basicdata:banQinCdRuleRotationHeader:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/wms/basicdata/banQinCdRuleRotationHeaderList";
	}
	
    /**
	 * 库存周转规则列表数据
	 */
	@ResponseBody
	@RequiresPermissions("basicdata:banQinCdRuleRotationHeader:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(BanQinCdRuleRotationHeaderEntity banQinCdRuleRotationHeader, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<BanQinCdRuleRotationHeaderEntity> page = banQinCdRuleRotationHeaderService.findPage(new Page<BanQinCdRuleRotationHeaderEntity>(request, response), banQinCdRuleRotationHeader);
		return getBootstrapData(page);
	}

	/**
	 * 库存周转规则弹出框数据
	 */
	@ResponseBody
	@RequestMapping(value = "grid")
	public Map<String, Object> grid(BanQinCdRuleRotationHeaderEntity banQinCdRuleRotationHeader, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<BanQinCdRuleRotationHeaderEntity> page = banQinCdRuleRotationHeaderService.findPage(new Page<BanQinCdRuleRotationHeaderEntity>(request, response), banQinCdRuleRotationHeader);
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑库存周转规则表单页面
	 */
	@RequiresPermissions(value={"basicdata:banQinCdRuleRotationHeader:view","basicdata:banQinCdRuleRotationHeader:add","basicdata:banQinCdRuleRotationHeader:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(BanQinCdRuleRotationHeaderEntity banQinCdRuleRotationHeaderEntity, Model model) {
		model.addAttribute("banQinCdRuleRotationHeaderEntity", banQinCdRuleRotationHeaderEntity);
		if(StringUtils.isBlank(banQinCdRuleRotationHeaderEntity.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "modules/wms/basicdata/banQinCdRuleRotationHeaderForm";
	}

	/**
	 * 保存库存周转规则
	 */
	@RequiresPermissions(value={"basicdata:banQinCdRuleRotationHeader:add","basicdata:banQinCdRuleRotationHeader:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
    @ResponseBody
	public AjaxJson save(BanQinCdRuleRotationHeaderEntity banQinCdRuleRotationHeaderEntity, Model model, RedirectAttributes redirectAttributes) throws Exception{
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, banQinCdRuleRotationHeaderEntity)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            BanQinCdRuleRotationHeader entity = banQinCdRuleRotationHeaderService.saveEntity(banQinCdRuleRotationHeaderEntity);

            LinkedHashMap<String, Object> map = new LinkedHashMap<>();
            map.put("entity", entity);
            j.setBody(map);
        } catch (DuplicateKeyException e) {
            j.setSuccess(false);
            j.setMsg("规则编码重复!");
        } catch (Exception e) {
			j.setSuccess(false);
			j.setMsg(e.getMessage());
		}
        
        return j;
	}
	
	/**
	 * 批量删除库存周转规则
	 */
	@ResponseBody
	@RequiresPermissions("basicdata:banQinCdRuleRotationHeader:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			banQinCdRuleRotationHeaderService.delete(banQinCdRuleRotationHeaderService.get(id));
		}
		j.setMsg("删除库存周转规则成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("basicdata:banQinCdRuleRotationHeader:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(BanQinCdRuleRotationHeader banQinCdRuleRotationHeader, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "库存周转规则"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<BanQinCdRuleRotationHeader> page = banQinCdRuleRotationHeaderService.findPage(new Page<BanQinCdRuleRotationHeader>(request, response, -1), banQinCdRuleRotationHeader);
    		new ExportExcel("库存周转规则", BanQinCdRuleRotationHeader.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出库存周转规则记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("basicdata:banQinCdRuleRotationHeader:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<BanQinCdRuleRotationHeader> list = ei.getDataList(BanQinCdRuleRotationHeader.class);
			for (BanQinCdRuleRotationHeader banQinCdRuleRotationHeader : list){
				try{
					banQinCdRuleRotationHeaderService.save(banQinCdRuleRotationHeader);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条库存周转规则记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条库存周转规则记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入库存周转规则失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/wms/basicdata/banQinCdRuleRotationHeader/?repage";
    }
	
	/**
	 * 下载导入库存周转规则数据模板
	 */
	@RequiresPermissions("basicdata:banQinCdRuleRotationHeader:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "库存周转规则数据导入模板.xlsx";
    		List<BanQinCdRuleRotationHeader> list = Lists.newArrayList(); 
    		new ExportExcel("库存周转规则数据", BanQinCdRuleRotationHeader.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/wms/basicdata/banQinCdRuleRotationHeader/?repage";
    }

}