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
import com.yunyou.modules.oms.basic.entity.OmClerk;
import com.yunyou.modules.oms.basic.service.OmClerkService;
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
 * 业务员Controller
 *
 * @author Jianhua Liu
 * @version 2019-07-29
 */
@Controller
@RequestMapping(value = "${adminPath}/oms/basic/omClerk")
public class OmClerkController extends BaseController {

    @Autowired
    private OmClerkService omClerkService;

    @ModelAttribute
    public OmClerk get(@RequestParam(required = false) String id) {
        OmClerk entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = omClerkService.get(id);
        }
        if (entity == null) {
            entity = new OmClerk();
        }
        return entity;
    }

    /**
     * 业务员列表页面
     */
    @RequiresPermissions("basic:omClerk:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/oms/basic/omClerkList";
    }

    /**
     * 业务员列表数据
     */
    @ResponseBody
    @RequiresPermissions("basic:omClerk:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(OmClerk omClerk, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<OmClerk> page = omClerkService.findPage(new Page<OmClerk>(request, response), omClerk);
        return getBootstrapData(page);
    }

    /**
     * 查看，增加，编辑业务员表单页面
     */
    @RequiresPermissions(value = {"basic:omClerk:view", "basic:omClerk:add", "basic:omClerk:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(OmClerk omClerk, Model model) {
        model.addAttribute("omClerk", omClerk);
        return "modules/oms/basic/omClerkForm";
    }

    /**
     * 保存业务员
     */
    @ResponseBody
    @RequiresPermissions(value = {"basic:omClerk:add", "basic:omClerk:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(OmClerk omClerk, Model model, RedirectAttributes redirectAttributes) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, omClerk)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        omClerkService.save(omClerk);//新建或者编辑保存
        j.setSuccess(true);
        j.setMsg("保存业务员成功");
        return j;
    }

    /**
     * 删除业务员
     */
    @ResponseBody
    @RequiresPermissions("basic:omClerk:del")
    @RequestMapping(value = "delete")
    public AjaxJson delete(OmClerk omClerk, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        omClerkService.delete(omClerk);
        j.setMsg("删除业务员成功");
        return j;
    }

    /**
     * 批量删除业务员
     */
    @ResponseBody
    @RequiresPermissions("basic:omClerk:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        String idArray[] = ids.split(",");
        for (String id : idArray) {
            omClerkService.delete(omClerkService.get(id));
        }
        j.setMsg("删除业务员成功");
        return j;
    }

    /**
     * 导出excel文件
     */
    @ResponseBody
    @RequiresPermissions("basic:omClerk:export")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public AjaxJson exportFile(OmClerk omClerk, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        try {
            String fileName = "业务员" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            Page<OmClerk> page = omClerkService.findPage(new Page<OmClerk>(request, response, -1), omClerk);
            new ExportExcel("业务员", OmClerk.class).setDataList(page.getList()).write(response, fileName).dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出业务员记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 导入Excel数据
     */
    @RequiresPermissions("basic:omClerk:import")
    @RequestMapping(value = "import", method = RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            int successNum = 0;
            int failureNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<OmClerk> list = ei.getDataList(OmClerk.class);
            for (OmClerk omClerk : list) {
                try {
                    omClerkService.save(omClerk);
                    successNum++;
                } catch (ConstraintViolationException ex) {
                    failureNum++;
                } catch (Exception ex) {
                    failureNum++;
                }
            }
            if (failureNum > 0) {
                failureMsg.insert(0, "，失败 " + failureNum + " 条业务员记录。");
            }
            addMessage(redirectAttributes, "已成功导入 " + successNum + " 条业务员记录" + failureMsg);
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入业务员失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/oms/basic/omClerk/?repage";
    }

    /**
     * 下载导入业务员数据模板
     */
    @RequiresPermissions("basic:omClerk:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            String fileName = "业务员数据导入模板.xlsx";
            List<OmClerk> list = Lists.newArrayList();
            new ExportExcel("业务员数据", OmClerk.class, 1).setDataList(list).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/oms/basic/omClerk/?repage";
    }

    /**
     * 弹出窗数据
     */
    @ResponseBody
    @RequestMapping(value = "popData")
    public Map<String, Object> popData(OmClerk omClerk, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<OmClerk> page = omClerkService.popData(new Page<OmClerk>(request, response), omClerk);
        return getBootstrapData(page);
    }

}