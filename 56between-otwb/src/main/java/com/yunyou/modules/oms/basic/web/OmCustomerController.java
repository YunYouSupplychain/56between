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
import com.yunyou.modules.oms.basic.entity.OmCustomer;
import com.yunyou.modules.oms.basic.entity.extend.OmCustomerEntity;
import com.yunyou.modules.oms.basic.service.OmCustomerService;
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
 * 客户Controller
 *
 * @author WMJ
 * @version 2019-01-25
 */
@Controller
@RequestMapping(value = "${adminPath}/oms/basic/omCustomer")
public class OmCustomerController extends BaseController {
    @Autowired
    private OmCustomerService omCustomerService;

    @ModelAttribute
    public OmCustomerEntity get(@RequestParam(required = false) String id) {
        OmCustomerEntity entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = omCustomerService.getEntity(id);
        }
        if (entity == null) {
            entity = new OmCustomerEntity();
        }
        return entity;
    }

    /**
     * 客户列表页面
     */
    @RequiresPermissions("basic:omCustomer:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/oms/basic/omCustomerList";
    }

    /**
     * 客户列表数据
     */
    @ResponseBody
    @RequiresPermissions("basic:omCustomer:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(OmCustomerEntity entity, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<OmCustomerEntity> page = omCustomerService.findPage(new Page<OmCustomerEntity>(request, response), entity);
        return getBootstrapData(page);
    }

    /**
     * 查看，增加，编辑客户表单页面
     */
    @RequiresPermissions(value = {"basic:omCustomer:view", "basic:omCustomer:add", "basic:omCustomer:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(OmCustomerEntity entity, Model model) {
        model.addAttribute("omCustomer", entity);
        if (StringUtils.isBlank(entity.getId())) {//如果ID是空为添加
            model.addAttribute("isAdd", true);
        }
        return "modules/oms/basic/omCustomerForm";
    }

    /**
     * 保存客户
     */
    @ResponseBody
    @RequiresPermissions(value = {"basic:omCustomer:add", "basic:omCustomer:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(OmCustomer omCustomer, Model model, RedirectAttributes redirectAttributes) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, omCustomer)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            omCustomerService.save(omCustomer);
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
    @RequiresPermissions("basic:omCustomer:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        String idArray[] = ids.split(",");
        for (String id : idArray) {
            omCustomerService.delete(omCustomerService.get(id));
        }
        j.setMsg("删除客户成功");
        return j;
    }

    /**
     * 导出excel文件
     */
    @ResponseBody
    @RequiresPermissions("basic:omCustomer:export")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public AjaxJson exportFile(OmCustomer omCustomer, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        try {
            String fileName = "客户" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            Page<OmCustomer> page = omCustomerService.findPage(new Page<OmCustomer>(request, response, -1), omCustomer);
            new ExportExcel("客户", OmCustomer.class).setDataList(page.getList()).write(response, fileName).dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出客户记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 导入Excel数据
     */
    @RequiresPermissions("basic:omCustomer:import")
    @RequestMapping(value = "import", method = RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            int successNum = 0;
            int failureNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<OmCustomer> list = ei.getDataList(OmCustomer.class);
            for (OmCustomer omCustomer : list) {
                try {
                    omCustomerService.save(omCustomer);
                    successNum++;
                } catch (ConstraintViolationException ex) {
                    failureNum++;
                } catch (Exception ex) {
                    failureNum++;
                }
            }
            if (failureNum > 0) {
                failureMsg.insert(0, "，失败 " + failureNum + " 条客户记录。");
            }
            addMessage(redirectAttributes, "已成功导入 " + successNum + " 条客户记录" + failureMsg);
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入客户失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/oms/basic/omCustomer/?repage";
    }

    /**
     * 下载导入客户数据模板
     */
    @RequiresPermissions("basic:omCustomer:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            String fileName = "客户数据导入模板.xlsx";
            List<OmCustomer> list = Lists.newArrayList();
            new ExportExcel("客户数据", OmCustomer.class, 1).setDataList(list).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/oms/basic/omCustomer/?repage";
    }

    /**
     * 客户数据（弹出框）
     */
    @ResponseBody
    @RequestMapping(value = "popData")
    public Map<String, Object> popData(OmCustomerEntity entity, HttpServletRequest request, HttpServletResponse response, Model model) {
        return getBootstrapData(omCustomerService.findGrid(new Page<OmCustomerEntity>(request, response), entity));
    }

}