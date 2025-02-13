package com.yunyou.modules.bms.basic.web;

import com.google.common.collect.Lists;
import com.yunyou.common.config.Global;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.common.utils.excel.ImportExcel;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.bms.basic.entity.BmsInvoiceObject;
import com.yunyou.modules.bms.basic.service.BmsInvoiceObjectService;
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
 * 开票对象Controller
 *
 * @author zqs
 * @version 2019-02-18
 */
@Controller
@RequestMapping(value = "${adminPath}/bms/basic/bmsInvoiceObject")
public class BmsInvoiceObjectController extends BaseController {
    @Autowired
    private BmsInvoiceObjectService bmsInvoiceObjectService;

    @ModelAttribute
    public BmsInvoiceObject get(@RequestParam(required = false) String id) {
        BmsInvoiceObject entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = bmsInvoiceObjectService.get(id);
        }
        if (entity == null) {
            entity = new BmsInvoiceObject();
        }
        return entity;
    }

    /**
     * 开票对象列表页面
     */
    @RequiresPermissions("bms:bmsInvoiceObject:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/bms/basic/bmsInvoiceObjectList";
    }

    /**
     * 开票对象列表数据
     */
    @ResponseBody
    @RequiresPermissions("bms:bmsInvoiceObject:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(BmsInvoiceObject bmsInvoiceObject, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(bmsInvoiceObjectService.findPage(new Page<>(request, response), bmsInvoiceObject));
    }

    /**
     * 查看，增加，编辑开票对象表单页面
     */
    @RequiresPermissions(value = {"bms:bmsInvoiceObject:view", "bms:bmsInvoiceObject:add", "bms:bmsInvoiceObject:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(BmsInvoiceObject bmsInvoiceObject, Model model) {
        model.addAttribute("bmsInvoiceObject", bmsInvoiceObject);
        return "modules/bms/basic/bmsInvoiceObjectForm";
    }

    /**
     * 保存开票对象
     */
    @ResponseBody
    @RequiresPermissions(value = {"bms:bmsInvoiceObject:add", "bms:bmsInvoiceObject:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(BmsInvoiceObject bmsInvoiceObject, Model model) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, bmsInvoiceObject)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        return bmsInvoiceObjectService.checkSaveBefore(bmsInvoiceObject);
    }

    /**
     * 删除开票对象
     */
    @ResponseBody
    @RequiresPermissions("bms:bmsInvoiceObject:del")
    @RequestMapping(value = "delete")
    public AjaxJson delete(BmsInvoiceObject bmsInvoiceObject) {
        AjaxJson j = new AjaxJson();
        bmsInvoiceObjectService.delete(bmsInvoiceObject);
        j.setMsg("删除开票对象成功");
        return j;
    }

    /**
     * 批量删除开票对象
     */
    @ResponseBody
    @RequiresPermissions("bms:bmsInvoiceObject:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            bmsInvoiceObjectService.delete(bmsInvoiceObjectService.get(id));
        }
        j.setMsg("删除开票对象成功");
        return j;
    }

    /**
     * 导出excel文件
     */
    @ResponseBody
    @RequiresPermissions("bms:bmsInvoiceObject:export")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public AjaxJson exportFile(BmsInvoiceObject bmsInvoiceObject, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            Page<BmsInvoiceObject> page = bmsInvoiceObjectService.findPage(new Page<>(request, response, -1), bmsInvoiceObject);
            new ExportExcel("开票对象", BmsInvoiceObject.class).setDataList(page.getList()).write(response, "开票对象.xlsx").dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出开票对象记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    @ResponseBody
    @RequestMapping(value = "detail")
    public BmsInvoiceObject detail(String id) {
        return bmsInvoiceObjectService.get(id);
    }

    /**
     * 导入Excel数据
     */
    @RequiresPermissions("bms:bmsInvoiceObject:import")
    @RequestMapping(value = "import", method = RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            int successNum = 0;
            int failureNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<BmsInvoiceObject> list = ei.getDataList(BmsInvoiceObject.class);
            for (BmsInvoiceObject bmsInvoiceObject : list) {
                try {
                    bmsInvoiceObjectService.save(bmsInvoiceObject);
                    successNum++;
                } catch (Exception ex) {
                    failureNum++;
                }
            }
            if (failureNum > 0) {
                failureMsg.insert(0, "，失败 " + failureNum + " 条开票对象记录。");
            }
            addMessage(redirectAttributes, "已成功导入 " + successNum + " 条开票对象记录" + failureMsg);
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入开票对象失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/bms/basic/bmsInvoiceObject/?repage";
    }

    /**
     * 下载导入开票对象数据模板
     */
    @RequiresPermissions("bms:bmsInvoiceObject:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            List<BmsInvoiceObject> list = Lists.newArrayList();
            new ExportExcel("开票对象数据", BmsInvoiceObject.class, 1).setDataList(list).write(response, "开票对象数据导入模板.xlsx").dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/bms/basic/bmsInvoiceObject/?repage";
    }

    /**
     * 开票对象弹出窗数据
     */
    @ResponseBody
    @RequestMapping(value = "grid")
    public Map<String, Object> grid(BmsInvoiceObject bmsInvoiceObject, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(bmsInvoiceObjectService.findPage(new Page<>(request, response), bmsInvoiceObject));
    }

}