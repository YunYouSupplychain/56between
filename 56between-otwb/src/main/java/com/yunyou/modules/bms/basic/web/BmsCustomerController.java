package com.yunyou.modules.bms.basic.web;

import com.google.common.collect.Lists;
import com.yunyou.common.config.Global;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.common.utils.excel.ImportExcel;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.bms.basic.entity.BmsCustomer;
import com.yunyou.modules.bms.basic.service.BmsCustomerService;
import com.yunyou.modules.bms.common.BmsException;
import com.yunyou.modules.sys.service.AreaService;
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
 * 客户Controller
 *
 * @author Jianhua Liu
 * @version 2019-06-11
 */
@Controller
@RequestMapping(value = "${adminPath}/bms/basic/bmsCustomer")
public class BmsCustomerController extends BaseController {
    @Autowired
    private BmsCustomerService bmsCustomerService;
    @Autowired
    private AreaService areaService;

    @ModelAttribute
    public BmsCustomer get(@RequestParam(required = false) String id) {
        BmsCustomer entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = bmsCustomerService.get(id);
        }
        if (entity == null) {
            entity = new BmsCustomer();
        }
        return entity;
    }

    /**
     * 客户列表页面
     */
    @RequiresPermissions("basic:bmsCustomer:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/bms/basic/bmsCustomerList";
    }

    /**
     * 客户列表数据
     */
    @ResponseBody
    @RequiresPermissions("basic:bmsCustomer:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(BmsCustomer bmsCustomer, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(bmsCustomerService.findPage(new Page<>(request, response), bmsCustomer));
    }

    /**
     * 查看，增加，编辑客户表单页面
     */
    @RequiresPermissions(value = {"basic:bmsCustomer:view", "basic:bmsCustomer:add", "basic:bmsCustomer:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(BmsCustomer bmsCustomer, Model model) {
        if (bmsCustomer != null && StringUtils.isBlank(bmsCustomer.getAreaName()) && StringUtils.isNotBlank(bmsCustomer.getAreaId())) {
            bmsCustomer.setAreaName(areaService.getFullName(bmsCustomer.getAreaId()));
        }
        model.addAttribute("bmsCustomer", bmsCustomer);
        return "modules/bms/basic/bmsCustomerForm";
    }

    /**
     * 保存客户
     */
    @ResponseBody
    @RequiresPermissions(value = {"basic:bmsCustomer:add", "basic:bmsCustomer:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(BmsCustomer bmsCustomer, Model model) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, bmsCustomer)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            bmsCustomerService.save(bmsCustomer);
            j.setSuccess(true);
            j.setMsg("保存客户成功");
            j.put("entity", bmsCustomer);
        } catch (BmsException e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } catch (DuplicateKeyException e) {
            j.setSuccess(false);
            j.setMsg("客户已存在");
        }
        return j;
    }

    /**
     * 删除客户
     */
    @ResponseBody
    @RequiresPermissions("basic:bmsCustomer:del")
    @RequestMapping(value = "delete")
    public AjaxJson delete(BmsCustomer bmsCustomer) {
        AjaxJson j = new AjaxJson();
        bmsCustomerService.delete(bmsCustomer);
        j.setMsg("删除客户成功");
        return j;
    }

    /**
     * 批量删除客户
     */
    @ResponseBody
    @RequiresPermissions("basic:bmsCustomer:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            bmsCustomerService.delete(bmsCustomerService.get(id));
        }
        j.setMsg("删除客户成功");
        return j;
    }

    /**
     * 导出excel文件
     */
    @ResponseBody
    @RequiresPermissions("basic:bmsCustomer:export")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public AjaxJson exportFile(BmsCustomer bmsCustomer, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            Page<BmsCustomer> page = bmsCustomerService.findPage(new Page<>(request, response, -1), bmsCustomer);
            new ExportExcel("客户", BmsCustomer.class).setDataList(page.getList()).write(response, "客户.xlsx").dispose();
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
    @RequiresPermissions("basic:bmsCustomer:import")
    @RequestMapping(value = "import", method = RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            int successNum = 0;
            int failureNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<BmsCustomer> list = ei.getDataList(BmsCustomer.class);
            for (BmsCustomer bmsCustomer : list) {
                try {
                    bmsCustomerService.save(bmsCustomer);
                    successNum++;
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
        return "redirect:" + Global.getAdminPath() + "/bms/basic/bmsCustomer/?repage";
    }

    /**
     * 下载导入客户数据模板
     */
    @RequiresPermissions("basic:bmsCustomer:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            String fileName = "客户数据导入模板.xlsx";
            List<BmsCustomer> list = Lists.newArrayList();
            new ExportExcel("客户数据", BmsCustomer.class, 1).setDataList(list).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/bms/basic/bmsCustomer/?repage";
    }

    /**
     * 弹出框
     */
    @ResponseBody
    @RequestMapping(value = "pop")
    public Map<String, Object> grid(BmsCustomer bmsCustomer, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(bmsCustomerService.findGrid(new Page<>(request, response), bmsCustomer));
    }
}