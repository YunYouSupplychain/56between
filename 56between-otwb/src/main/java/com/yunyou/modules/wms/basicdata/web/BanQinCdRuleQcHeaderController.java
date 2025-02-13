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
import com.yunyou.modules.wms.basicdata.entity.BanQinCdRuleQcHeader;
import com.yunyou.modules.wms.basicdata.service.BanQinCdRuleQcHeaderService;
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
 * 质检规则Controller
 *
 * @author WMJ
 * @version 2019-01-25
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/basicdata/banQinCdRuleQcHeader")
public class BanQinCdRuleQcHeaderController extends BaseController {

    @Autowired
    private BanQinCdRuleQcHeaderService banQinCdRuleQcHeaderService;

    @ModelAttribute
    public BanQinCdRuleQcHeader get(@RequestParam(required = false) String id) {
        BanQinCdRuleQcHeader entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = banQinCdRuleQcHeaderService.get(id);
        }
        if (entity == null) {
            entity = new BanQinCdRuleQcHeader();
        }
        return entity;
    }

    /**
     * 质检规则列表页面
     */
    @RequiresPermissions("basicdata:banQinCdRuleQcHeader:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/wms/basicdata/banQinCdRuleQcHeaderList";
    }

    /**
     * 质检规则列表数据
     */
    @ResponseBody
    @RequiresPermissions("basicdata:banQinCdRuleQcHeader:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(BanQinCdRuleQcHeader banQinCdRuleQcHeader, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<BanQinCdRuleQcHeader> page = banQinCdRuleQcHeaderService.findPage(new Page<BanQinCdRuleQcHeader>(request, response), banQinCdRuleQcHeader);
        return getBootstrapData(page);
    }

    /**
     * 质检规则弹出框数据
     */
    @ResponseBody
    @RequestMapping(value = "grid")
    public Map<String, Object> grid(BanQinCdRuleQcHeader banQinCdRuleQcHeader, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<BanQinCdRuleQcHeader> page = banQinCdRuleQcHeaderService.findPage(new Page<BanQinCdRuleQcHeader>(request, response), banQinCdRuleQcHeader);
        return getBootstrapData(page);
    }

    /**
     * 查看，增加，编辑质检规则表单页面
     */
    @RequiresPermissions(value = {"basicdata:banQinCdRuleQcHeader:view", "basicdata:banQinCdRuleQcHeader:add", "basicdata:banQinCdRuleQcHeader:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(BanQinCdRuleQcHeader banQinCdRuleQcHeader, Model model) {
        model.addAttribute("banQinCdRuleQcHeader", banQinCdRuleQcHeader);
        if (StringUtils.isBlank(banQinCdRuleQcHeader.getId())) {//如果ID是空为添加
            model.addAttribute("isAdd", true);
        }
        return "modules/wms/basicdata/banQinCdRuleQcHeaderForm";
    }

    /**
     * 保存质检规则
     */
    @ResponseBody
    @RequiresPermissions(value = {"basicdata:banQinCdRuleQcHeader:add", "basicdata:banQinCdRuleQcHeader:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(BanQinCdRuleQcHeader banQinCdRuleQcHeader, Model model, RedirectAttributes redirectAttributes) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, banQinCdRuleQcHeader)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            banQinCdRuleQcHeaderService.save(banQinCdRuleQcHeader);
            j.put("entity", banQinCdRuleQcHeader);
        } catch (DuplicateKeyException e) {
            j.setSuccess(false);
            j.setMsg("规则编码已存在!");
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }

        return j;
    }

    /**
     * 批量删除质检规则
     */
    @ResponseBody
    @RequiresPermissions("basicdata:banQinCdRuleQcHeader:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        String idArray[] = ids.split(",");
        for (String id : idArray) {
            banQinCdRuleQcHeaderService.delete(banQinCdRuleQcHeaderService.get(id));
        }
        j.setMsg("删除质检规则成功");
        return j;
    }

    /**
     * 导出excel文件
     */
    @ResponseBody
    @RequiresPermissions("basicdata:banQinCdRuleQcHeader:export")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public AjaxJson exportFile(BanQinCdRuleQcHeader banQinCdRuleQcHeader, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        try {
            String fileName = "质检规则" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            Page<BanQinCdRuleQcHeader> page = banQinCdRuleQcHeaderService.findPage(new Page<BanQinCdRuleQcHeader>(request, response, -1), banQinCdRuleQcHeader);
            new ExportExcel("质检规则", BanQinCdRuleQcHeader.class).setDataList(page.getList()).write(response, fileName).dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出质检规则记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 导入Excel数据
     */
    @RequiresPermissions("basicdata:banQinCdRuleQcHeader:import")
    @RequestMapping(value = "import", method = RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            int successNum = 0;
            int failureNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<BanQinCdRuleQcHeader> list = ei.getDataList(BanQinCdRuleQcHeader.class);
            for (BanQinCdRuleQcHeader banQinCdRuleQcHeader : list) {
                try {
                    banQinCdRuleQcHeaderService.save(banQinCdRuleQcHeader);
                    successNum++;
                } catch (ConstraintViolationException ex) {
                    failureNum++;
                } catch (Exception ex) {
                    failureNum++;
                }
            }
            if (failureNum > 0) {
                failureMsg.insert(0, "，失败 " + failureNum + " 条质检规则记录。");
            }
            addMessage(redirectAttributes, "已成功导入 " + successNum + " 条质检规则记录" + failureMsg);
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入质检规则失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/wms/basicdata/banQinCdRuleQcHeader/?repage";
    }

    /**
     * 下载导入质检规则数据模板
     */
    @RequiresPermissions("basicdata:banQinCdRuleQcHeader:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            String fileName = "质检规则数据导入模板.xlsx";
            List<BanQinCdRuleQcHeader> list = Lists.newArrayList();
            new ExportExcel("质检规则数据", BanQinCdRuleQcHeader.class, 1).setDataList(list).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/wms/basicdata/banQinCdRuleQcHeader/?repage";
    }

}