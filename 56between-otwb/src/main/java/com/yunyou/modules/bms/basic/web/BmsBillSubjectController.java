package com.yunyou.modules.bms.basic.web;

import com.google.common.collect.Lists;
import com.yunyou.common.config.Global;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.common.utils.excel.ImportExcel;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.bms.basic.entity.BmsBillSubject;
import com.yunyou.modules.bms.basic.entity.extend.BmsBillSubjectEntity;
import com.yunyou.modules.bms.basic.service.BmsBillSubjectService;
import com.yunyou.modules.bms.common.BmsException;
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
import java.util.List;
import java.util.Map;

/**
 * 费用科目Controller
 *
 * @author Jianhua Liu
 * @version 2019-05-27
 */
@Controller
@RequestMapping(value = "${adminPath}/bms/basic/bmsBillSubject")
public class BmsBillSubjectController extends BaseController {
    @Autowired
    private BmsBillSubjectService bmsBillSubjectService;

    @ModelAttribute
    public BmsBillSubject get(@RequestParam(required = false) String id) {
        BmsBillSubject entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = bmsBillSubjectService.get(id);
        }
        if (entity == null) {
            entity = new BmsBillSubject();
        }
        return entity;
    }

    /**
     * 列表页面
     */
    @RequiresPermissions("bms:bmsBillSubject:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/bms/basic/bmsBillSubjectList";
    }

    /**
     * 列表数据
     */
    @ResponseBody
    @RequiresPermissions("bms:bmsBillSubject:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(BmsBillSubject entity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(bmsBillSubjectService.findPage(new Page<>(request, response), entity));
    }

    /**
     * 查看，增加，编辑表单页面
     */
    @RequiresPermissions(value = {"bms:bmsBillSubject:view", "bms:bmsBillSubject:add", "bms:bmsBillSubject:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(BmsBillSubject entity, Model model) {
        model.addAttribute("bmsBillSubject", entity);
        return "modules/bms/basic/bmsBillSubjectForm";
    }

    /**
     * 保存
     */
    @ResponseBody
    @RequiresPermissions(value = {"bms:bmsBillSubject:add", "bms:bmsBillSubject:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(BmsBillSubject entity, Model model) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, entity)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            bmsBillSubjectService.checkSaveBefore(entity);
            bmsBillSubjectService.save(entity);
            j.put("entity", entity);
        } catch (BmsException e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } catch (DuplicateKeyException e) {
            j.setSuccess(false);
            j.setMsg("代码已存在");
        }
        return j;
    }

    /**
     * 批量删除
     */
    @ResponseBody
    @RequiresPermissions("bms:bmsBillSubject:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            bmsBillSubjectService.delete(bmsBillSubjectService.get(id));
        }
        j.setMsg("删除成功");
        return j;
    }

    /**
     * 导出excel文件
     */
    @ResponseBody
    @RequiresPermissions("bms:bmsBillSubject:export")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public AjaxJson exportFile(BmsBillSubject entity, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            Page<BmsBillSubjectEntity> page = bmsBillSubjectService.findPage(new Page<BmsBillSubject>(request, response, -1), entity);
            new ExportExcel("", BmsBillSubject.class).setDataList(page.getList()).write(response, "费用科目.xlsx").dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 导入Excel数据
     */
    @RequiresPermissions("bms:bmsBillSubject:import")
    @RequestMapping(value = "import", method = RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            int successNum = 0;
            int failureNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<BmsBillSubject> list = ei.getDataList(BmsBillSubject.class);
            for (BmsBillSubject entity : list) {
                try {
                    bmsBillSubjectService.checkSaveBefore(entity);
                    bmsBillSubjectService.save(entity);
                    successNum++;
                } catch (Exception ex) {
                    failureNum++;
                }
            }
            if (failureNum > 0) {
                failureMsg.insert(0, "，失败 " + failureNum + " 条记录。");
            }
            addMessage(redirectAttributes, "已成功导入 " + successNum + " 条记录" + failureMsg);
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/bms/basic/bmsBillSubject/?repage";
    }

    /**
     * 下载导入数据模板
     */
    @RequiresPermissions("bms:bmsBillSubject:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            new ExportExcel(null, BmsBillSubject.class, 1).setDataList(Lists.newArrayList()).write(response, "费用科目数据导入模板.xlsx").dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/bms/basic/bmsBillSubject/?repage";
    }

    /**
     * 弹出窗数据
     */
    @ResponseBody
    @RequestMapping(value = "grid")
    public Map<String, Object> grid(BmsBillSubjectEntity entity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(bmsBillSubjectService.findGrid(new Page<>(request, response), entity));
    }

}