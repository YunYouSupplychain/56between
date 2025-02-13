package com.yunyou.modules.tms.basic.web;

import com.google.common.collect.Lists;
import com.yunyou.common.exception.GlobalException;
import com.yunyou.common.config.Global;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.time.DateUtils;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.common.utils.excel.ImportExcel;
import com.yunyou.core.persistence.BaseEntity;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.sys.utils.UserUtils;
import com.yunyou.modules.tms.basic.entity.TmTransportEquipmentType;
import com.yunyou.modules.tms.basic.entity.extend.TmTransportEquipmentTypeEntity;
import com.yunyou.modules.tms.basic.service.TmTransportEquipmentTypeService;
import com.yunyou.modules.tms.common.TmsException;
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
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 运输设备类型Controller
 *
 * @author liujianhua
 * @version 2020-02-20
 */
@Controller
@RequestMapping(value = "${adminPath}/tms/basic/tmTransportEquipmentType")
public class TmTransportEquipmentTypeController extends BaseController {

    @Autowired
    private TmTransportEquipmentTypeService tmTransportEquipmentTypeService;

    @ModelAttribute
    public TmTransportEquipmentTypeEntity get(@RequestParam(required = false) String id) {
        TmTransportEquipmentTypeEntity entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = tmTransportEquipmentTypeService.getEntity(id);
        }
        if (entity == null) {
            entity = new TmTransportEquipmentTypeEntity();
        }
        return entity;
    }

    /**
     * 运输设备类型列表页面
     */
    @RequiresPermissions("basic:tmTransportEquipmentType:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/tms/basic/tmTransportEquipmentTypeList";
    }

    /**
     * 运输设备类型列表数据
     */
    @ResponseBody
    @RequiresPermissions("basic:tmTransportEquipmentType:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(TmTransportEquipmentType tmTransportEquipmentType, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<TmTransportEquipmentTypeEntity> page = tmTransportEquipmentTypeService.findPage(new Page<TmTransportEquipmentTypeEntity>(request, response), tmTransportEquipmentType);
        return getBootstrapData(page);
    }

    /**
     * 查看，增加，编辑运输设备类型表单页面
     */
    @RequiresPermissions(value = {"basic:tmTransportEquipmentType:view", "basic:tmTransportEquipmentType:add", "basic:tmTransportEquipmentType:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(TmTransportEquipmentTypeEntity tmTransportEquipmentTypeEntity, Model model) {
        model.addAttribute("tmTransportEquipmentTypeEntity", tmTransportEquipmentTypeEntity);
        return "modules/tms/basic/tmTransportEquipmentTypeForm";
    }

    /**
     * 保存运输设备类型
     */
    @ResponseBody
    @RequiresPermissions(value = {"basic:tmTransportEquipmentType:add", "basic:tmTransportEquipmentType:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(TmTransportEquipmentType tmTransportEquipmentType, Model model, RedirectAttributes redirectAttributes) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, tmTransportEquipmentType)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            tmTransportEquipmentTypeService.saveValidator(tmTransportEquipmentType);
            tmTransportEquipmentTypeService.save(tmTransportEquipmentType);
            j.put("entity", tmTransportEquipmentType);
        } catch (TmsException e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 删除运输设备类型
     */
    @ResponseBody
    @RequiresPermissions("basic:tmTransportEquipmentType:del")
    @RequestMapping(value = "delete")
    public AjaxJson delete(TmTransportEquipmentType tmTransportEquipmentType, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();

        try {
            tmTransportEquipmentTypeService.delete(tmTransportEquipmentType);
        } catch (Exception e) {
            logger.error("删除运输设备类型id=[" + tmTransportEquipmentType.getId() + "]", e);
            j.setSuccess(false);
            j.setMsg("操作失败" + e.getMessage());
        }
        return j;
    }

    /**
     * 批量删除运输设备类型
     */
    @ResponseBody
    @RequiresPermissions("basic:tmTransportEquipmentType:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();

        StringBuilder errMsg = new StringBuilder();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            TmTransportEquipmentType tmTransportEquipmentType = tmTransportEquipmentTypeService.get(id);
            try {
                tmTransportEquipmentTypeService.delete(tmTransportEquipmentType);
            } catch (Exception e) {
                logger.error("删除运输设备类型id=[" + id + "]", e);
                errMsg.append("<br>").append("运输设备类型编码[").append(tmTransportEquipmentType.getTransportEquipmentTypeCode()).append("]删除失败");
            }
        }
        if (StringUtils.isNotBlank(errMsg)) {
            j.setMsg("操作成功，其中" + errMsg.toString());
        }
        return j;
    }

    /**
     * 导出excel文件
     */
    @ResponseBody
    @RequiresPermissions("basic:tmTransportEquipmentType:export")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public AjaxJson exportFile(TmTransportEquipmentType tmTransportEquipmentType, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        try {
            String fileName = "运输设备类型" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            Page<TmTransportEquipmentTypeEntity> page = tmTransportEquipmentTypeService.findPage(new Page<TmTransportEquipmentTypeEntity>(request, response, -1), tmTransportEquipmentType);
            new ExportExcel("运输设备类型", TmTransportEquipmentType.class).setDataList(page.getList()).write(response, fileName).dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出运输设备类型记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 导入Excel数据
     */
    @RequiresPermissions("basic:tmTransportEquipmentType:import")
    @RequestMapping(value = "import", method = RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            int successNum = 0;
            int failureNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<TmTransportEquipmentType> list = ei.getDataList(TmTransportEquipmentType.class);
            for (TmTransportEquipmentType tmTransportEquipmentType : list) {
                try {
                    tmTransportEquipmentTypeService.save(tmTransportEquipmentType);
                    successNum++;
                } catch (ConstraintViolationException ex) {
                    failureNum++;
                } catch (Exception ex) {
                    failureNum++;
                }
            }
            if (failureNum > 0) {
                failureMsg.insert(0, "，失败 " + failureNum + " 条运输设备类型记录。");
            }
            addMessage(redirectAttributes, "已成功导入 " + successNum + " 条运输设备类型记录" + failureMsg);
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入运输设备类型失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/tms/basic/tmTransportEquipmentType/?repage";
    }

    /**
     * 下载导入运输设备类型数据模板
     */
    @RequiresPermissions("basic:tmTransportEquipmentType:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            String fileName = "运输设备类型数据导入模板.xlsx";
            List<TmTransportEquipmentType> list = Lists.newArrayList();
            new ExportExcel("运输设备类型数据", TmTransportEquipmentType.class, 1).setDataList(list).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/tms/basic/tmTransportEquipmentType/?repage";
    }

    /**
     * 启用/停用运输设备类型
     */
    @ResponseBody
    @RequiresPermissions(value = {"basic:tmTransportEquipmentType:enable", "basic:tmTransportEquipmentType:unable"}, logical = Logical.OR)
    @RequestMapping(value = "enable")
    public AjaxJson enable(@RequestParam String ids, @RequestParam String flag) {
        AjaxJson j = new AjaxJson();

        StringBuilder errMsg = new StringBuilder();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            TmTransportEquipmentType tmTransportEquipmentType = tmTransportEquipmentTypeService.get(id);
            try {
                tmTransportEquipmentType.setDelFlag("0".equals(flag) ? BaseEntity.DEL_FLAG_NORMAL : BaseEntity.DEL_FLAG_DELETE);
                tmTransportEquipmentType.setUpdateBy(UserUtils.getUser());
                tmTransportEquipmentType.setUpdateDate(new Date());
                tmTransportEquipmentTypeService.save(tmTransportEquipmentType);
            } catch (GlobalException e){
                errMsg.append("<br>").append("运输设备类型[").append(tmTransportEquipmentType.getTransportEquipmentTypeCode()).append("]").append(e.getMessage());
            } catch (Exception e) {
                logger.error("启用/停用运输设备类型id=[" + id + "]", e);
                errMsg.append("<br>").append("运输设备类型[").append(tmTransportEquipmentType.getTransportEquipmentTypeCode()).append("]操作失败");
            }
        }
        if (StringUtils.isNotBlank(errMsg)) {
            j.setMsg("操作成功，其中" + errMsg.toString());
        }
        return j;
    }

    /**
     * 运输设备类型Grid列表数据
     */
    @ResponseBody
    @RequestMapping(value = "grid")
    public Map<String, Object> grid(TmTransportEquipmentTypeEntity entity, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<TmTransportEquipmentTypeEntity> page = tmTransportEquipmentTypeService.findGrid(new Page<TmTransportEquipmentTypeEntity>(request, response), entity);
        return getBootstrapData(page);
    }

}