package com.yunyou.modules.tms.basic.web;

import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.time.DateUtils;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.tms.basic.entity.TmTransportEquipmentSpace;
import com.yunyou.modules.tms.basic.service.TmTransportEquipmentSpaceService;
import com.yunyou.modules.tms.common.TmsException;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 运输设备空间信息Controller
 * @author liujianhua
 * @version 2020-02-20
 */
@Controller
@RequestMapping(value = "${adminPath}/tms/basic/tmTransportEquipmentSpace")
public class TmTransportEquipmentSpaceController extends BaseController {
	@Autowired
	private TmTransportEquipmentSpaceService tmTransportEquipmentSpaceService;
	
	@ModelAttribute
	public TmTransportEquipmentSpace get(@RequestParam(required=false) String id) {
		TmTransportEquipmentSpace entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = tmTransportEquipmentSpaceService.get(id);
		}
		if (entity == null){
			entity = new TmTransportEquipmentSpace();
		}
		return entity;
	}

	/**
	 * 运输设备空间信息列表数据
	 */
	@ResponseBody
	@RequiresPermissions("basic:tmTransportEquipmentSpace:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(TmTransportEquipmentSpace tmTransportEquipmentSpace, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<TmTransportEquipmentSpace> page = tmTransportEquipmentSpaceService.findPage(new Page<TmTransportEquipmentSpace>(request, response), tmTransportEquipmentSpace); 
		return getBootstrapData(page);
	}

	/**
	 * 保存运输设备空间信息
	 */
	@ResponseBody
	@RequiresPermissions(value={"basic:tmTransportEquipmentSpace:add","basic:tmTransportEquipmentSpace:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(TmTransportEquipmentSpace tmTransportEquipmentSpace, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, tmTransportEquipmentSpace)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		try {
			tmTransportEquipmentSpaceService.saveValidator(tmTransportEquipmentSpace);
			tmTransportEquipmentSpaceService.save(tmTransportEquipmentSpace);
		} catch (TmsException e) {
			j.setSuccess(false);
			j.setMsg(e.getMessage());
		}
		return j;
	}
	
	/**
	 * 删除运输设备空间信息
	 */
	@ResponseBody
	@RequiresPermissions("basic:tmTransportEquipmentSpace:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(TmTransportEquipmentSpace tmTransportEquipmentSpace, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		tmTransportEquipmentSpaceService.delete(tmTransportEquipmentSpace);
		j.setMsg("删除运输设备空间信息成功");
		return j;
	}
	
	/**
	 * 批量删除运输设备空间信息
	 */
	@ResponseBody
	@RequiresPermissions("basic:tmTransportEquipmentSpace:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			tmTransportEquipmentSpaceService.delete(tmTransportEquipmentSpaceService.get(id));
		}
		j.setMsg("删除运输设备空间信息成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("basic:tmTransportEquipmentSpace:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(TmTransportEquipmentSpace tmTransportEquipmentSpace, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "运输设备空间信息"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<TmTransportEquipmentSpace> page = tmTransportEquipmentSpaceService.findPage(new Page<TmTransportEquipmentSpace>(request, response, -1), tmTransportEquipmentSpace);
    		new ExportExcel("运输设备空间信息", TmTransportEquipmentSpace.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出运输设备空间信息记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

}