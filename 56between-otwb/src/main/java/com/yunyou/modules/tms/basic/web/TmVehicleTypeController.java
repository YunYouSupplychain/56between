package com.yunyou.modules.tms.basic.web;

import com.yunyou.common.config.Global;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.common.utils.excel.ImportExcel;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.sys.utils.UserUtils;
import com.yunyou.modules.tms.basic.entity.TmVehicleType;
import com.yunyou.modules.tms.basic.service.TmVehicleTypeService;
import com.yunyou.modules.tms.common.TmsException;
import com.google.common.collect.Lists;
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
import java.util.List;
import java.util.Map;

/**
 * 车型信息Controller
 *
 * @author liujianhua
 * @version 2022-08-04
 */
@Controller
@RequestMapping(value = "${adminPath}/tms/basic/tmVehicleType")
public class TmVehicleTypeController extends BaseController {
    @Autowired
    private TmVehicleTypeService tmVehicleTypeService;

    @ModelAttribute
    public TmVehicleType get(@RequestParam(required = false) String id) {
        TmVehicleType entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = tmVehicleTypeService.get(id);
        }
        if (entity == null) {
            entity = new TmVehicleType();
        }
        return entity;
    }

    /**
     * 车型信息列表页面
     */
    @RequiresPermissions("basic:tmVehicleType:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/tms/basic/tmVehicleTypeList";
    }

    /**
     * 车型信息列表数据
     */
    @ResponseBody
    @RequiresPermissions("basic:tmVehicleType:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(TmVehicleType tmVehicleType, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(tmVehicleTypeService.findPage(new Page<>(request, response), tmVehicleType));
    }

    /**
     * 查看，增加，编辑车型信息表单页面
     */
    @RequiresPermissions(value = {"basic:tmVehicleType:view", "basic:tmVehicleType:add", "basic:tmVehicleType:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(TmVehicleType tmVehicleEntity, Model model) {
        model.addAttribute("tmVehicleEntity", tmVehicleEntity);
        return "modules/tms/basic/tmVehicleTypeForm";
    }

    /**
     * 保存车型信息
     */
    @ResponseBody
    @RequiresPermissions(value = {"basic:tmVehicleType:add", "basic:tmVehicleType:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(TmVehicleType tmVehicleType, Model model) {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, tmVehicleType)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            tmVehicleTypeService.saveValidator(tmVehicleType);
            tmVehicleTypeService.save(tmVehicleType);
            j.put("entity", tmVehicleType);
        } catch (TmsException e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 批量删除车型信息
     */
    @ResponseBody
    @RequiresPermissions("basic:tmVehicleType:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();

        String[] idArray = ids.split(",");
        for (String id : idArray) {
            try {
                tmVehicleTypeService.delete(new TmVehicleType(id));
            } catch (Exception e) {
                logger.error("删除车型信息id=[" + id + "]", e);
            }
        }
        return j;
    }

    /**
     * 导出excel文件
     */
    @ResponseBody
    @RequiresPermissions("basic:tmVehicleType:export")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public AjaxJson exportFile(TmVehicleType tmVehicleType, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            String fileName = "车型信息.xlsx";
            Page<TmVehicleType> page = tmVehicleTypeService.findPage(new Page<>(request, response, -1), tmVehicleType);
            new ExportExcel("车型信息", TmVehicleType.class).setDataList(page.getList()).write(response, fileName).dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出车型信息记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 导入Excel数据
     */
    @RequiresPermissions("basic:tmVehicleType:import")
    @RequestMapping(value = "import", method = RequestMethod.POST)
    public String importFile(MultipartFile file, String orgId, RedirectAttributes redirectAttributes) {
        try {
            int successNum = 0;
            int failureNum = 0;
            orgId = UserUtils.getOrgCenter(orgId).getId();
            StringBuilder failureMsg = new StringBuilder();
            List<TmVehicleType> list = new ImportExcel(file, 1, 0).getDataList(TmVehicleType.class);
            for (TmVehicleType tmVehicleType : list) {
                try {
                    tmVehicleType.setOrgId(orgId);
                    tmVehicleTypeService.save(tmVehicleType);
                    successNum++;
                } catch (Exception ex) {
                    failureNum++;
                }
            }
            if (failureNum > 0) {
                failureMsg.insert(0, "，失败 " + failureNum + " 条车型信息记录。");
            }
            addMessage(redirectAttributes, "已成功导入 " + successNum + " 条车型信息记录" + failureMsg);
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入车型信息失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/tms/basic/tmVehicleType/?repage";
    }

    /**
     * 下载导入车型信息数据模板
     */
    @RequiresPermissions("basic:tmVehicleType:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            String fileName = "车型信息数据导入模板.xlsx";
            List<TmVehicleType> list = Lists.newArrayList();
            new ExportExcel(null, TmVehicleType.class, 1).setDataList(list).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/tms/basic/tmVehicleType/?repage";
    }

    /**
     * 车型信息Grid列表数据
     */
    @ResponseBody
    @RequestMapping(value = "grid")
    public Map<String, Object> grid(TmVehicleType qEntity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(tmVehicleTypeService.findGrid(new Page<>(request, response), qEntity));
    }

}