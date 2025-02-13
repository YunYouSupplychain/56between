package com.yunyou.modules.oms.basic.web;

import com.google.common.collect.Lists;
import com.yunyou.common.config.Global;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.time.DateUtils;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.common.utils.excel.ImportExcel;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.oms.basic.entity.OmPackage;
import com.yunyou.modules.oms.basic.service.OmPackageService;
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
 * 包装Controller
 *
 * @author WMJ
 * @version 2019-04-19
 */
@Controller
@RequestMapping(value = "${adminPath}/oms/basic/omPackage")
public class OmPackageController extends BaseController {

    @Autowired
    private OmPackageService omPackageService;

    @ModelAttribute
    public OmPackage get(@RequestParam(required = false) String id) {
        OmPackage entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = omPackageService.get(id);
        }
        if (entity == null) {
            entity = new OmPackage();
        }
        return entity;
    }

    /**
     * 包装列表页面
     */
    @RequiresPermissions("basic:omPackage:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/oms/basic/omPackageList";
    }

    /**
     * 包装列表数据
     */
    @ResponseBody
    @RequiresPermissions("basic:omPackage:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(OmPackage omPackage, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<OmPackage> page = omPackageService.findPage(new Page<OmPackage>(request, response), omPackage);
        return getBootstrapData(page);
    }

    /**
     * 包装列表数据(弹出框)
     */
    @ResponseBody
    @RequestMapping(value = "popData")
    public Map<String, Object> popData(OmPackage omPackage, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<OmPackage> page = omPackageService.findPage(new Page<OmPackage>(request, response), omPackage);
        return getBootstrapData(page);
    }

    /**
     * 查看，增加，编辑包装表单页面
     */
    @RequiresPermissions(value = {"basic:omPackage:view", "basic:omPackage:add", "basic:omPackage:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(OmPackage omPackage, Model model) {
        model.addAttribute("omPackage", omPackage);
        if (StringUtils.isBlank(omPackage.getId())) {//如果ID是空为添加
            model.addAttribute("isAdd", true);
        }
        return "modules/oms/basic/omPackageForm";
    }

    /**
     * 保存包装
     */
    @RequiresPermissions(value = {"basic:omPackage:add", "basic:omPackage:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    @ResponseBody
    public AjaxJson save(OmPackage omPackage, Model model, RedirectAttributes redirectAttributes) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, omPackage)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            omPackageService.save(omPackage);
            j.setMsg("保存成功！");
        } catch (DuplicateKeyException e) {
            j.setSuccess(false);
            j.setMsg("包装编码重复！");
        }
        return j;
    }

    /**
     * 批量删除包装
     */
    @ResponseBody
    @RequiresPermissions("basic:omPackage:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        String idArray[] = ids.split(",");
        for (String id : idArray) {
            omPackageService.delete(omPackageService.get(id));
        }
        j.setMsg("删除包装成功");
        return j;
    }

    /**
     * 导出excel文件
     */
    @ResponseBody
    @RequiresPermissions("basic:omPackage:export")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public AjaxJson exportFile(OmPackage omPackage, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        try {
            String fileName = "包装" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            Page<OmPackage> page = omPackageService.findPage(new Page<OmPackage>(request, response, -1), omPackage);
            new ExportExcel("包装", OmPackage.class).setDataList(page.getList()).write(response, fileName).dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出包装记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 导入Excel数据
     */
    @RequiresPermissions("basic:omPackage:import")
    @RequestMapping(value = "import", method = RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            int successNum = 0;
            int failureNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<OmPackage> list = ei.getDataList(OmPackage.class);
            for (OmPackage omPackage : list) {
                try {
                    omPackageService.save(omPackage);
                    successNum++;
                } catch (ConstraintViolationException ex) {
                    failureNum++;
                } catch (Exception ex) {
                    failureNum++;
                }
            }
            if (failureNum > 0) {
                failureMsg.insert(0, "，失败 " + failureNum + " 条包装记录。");
            }
            addMessage(redirectAttributes, "已成功导入 " + successNum + " 条包装记录" + failureMsg);
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入包装失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/oms/basic/omPackage/?repage";
    }

    /**
     * 下载导入包装数据模板
     */
    @RequiresPermissions("basic:omPackage:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            String fileName = "包装数据导入模板.xlsx";
            List<OmPackage> list = Lists.newArrayList();
            new ExportExcel("包装数据", OmPackage.class, 1).setDataList(list).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/oms/basic/omPackage/?repage";
    }

}