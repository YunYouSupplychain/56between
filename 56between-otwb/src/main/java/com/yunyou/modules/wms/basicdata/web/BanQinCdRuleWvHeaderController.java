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
import com.yunyou.modules.wms.basicdata.entity.BanQinCdRuleWvHeader;
import com.yunyou.modules.wms.basicdata.service.BanQinCdRuleWvHeaderService;
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
 * 波次规则Controller
 *
 * @author WMJ
 * @version 2019-01-25
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/basicdata/banQinCdRuleWvHeader")
public class BanQinCdRuleWvHeaderController extends BaseController {

    @Autowired
    private BanQinCdRuleWvHeaderService banQinCdRuleWvHeaderService;

    @ModelAttribute
    public BanQinCdRuleWvHeader get(@RequestParam(required = false) String id) {
        BanQinCdRuleWvHeader entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = banQinCdRuleWvHeaderService.get(id);
        }
        if (entity == null) {
            entity = new BanQinCdRuleWvHeader();
        }
        return entity;
    }

    /**
     * 波次规则列表页面
     */
    @RequiresPermissions("basicdata:banQinCdRuleWvHeader:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/wms/basicdata/banQinCdRuleWvHeaderList";
    }

    /**
     * 波次规则列表数据
     */
    @ResponseBody
    @RequiresPermissions("basicdata:banQinCdRuleWvHeader:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(BanQinCdRuleWvHeader banQinCdRuleWvHeader, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<BanQinCdRuleWvHeader> page = banQinCdRuleWvHeaderService.findPage(new Page<BanQinCdRuleWvHeader>(request, response), banQinCdRuleWvHeader);
        return getBootstrapData(page);
    }

    /**
     * 波次规则列表数据
     */
    @ResponseBody
    @RequestMapping(value = "grid")
    public Map<String, Object> grid(BanQinCdRuleWvHeader banQinCdRuleWvHeader, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<BanQinCdRuleWvHeader> page = banQinCdRuleWvHeaderService.findPage(new Page<BanQinCdRuleWvHeader>(request, response), banQinCdRuleWvHeader);
        return getBootstrapData(page);
    }

    /**
     * 查看，增加，编辑波次规则表单页面
     */
    @RequiresPermissions(value = {"basicdata:banQinCdRuleWvHeader:view", "basicdata:banQinCdRuleWvHeader:add", "basicdata:banQinCdRuleWvHeader:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(BanQinCdRuleWvHeader banQinCdRuleWvHeader, Model model) {
        model.addAttribute("banQinCdRuleWvHeader", banQinCdRuleWvHeader);
        if (StringUtils.isBlank(banQinCdRuleWvHeader.getId())) {//如果ID是空为添加
            model.addAttribute("isAdd", true);
        }
        return "modules/wms/basicdata/banQinCdRuleWvHeaderForm";
    }

    /**
     * 保存波次规则
     */
    @ResponseBody
    @RequiresPermissions(value = {"basicdata:banQinCdRuleWvHeader:add", "basicdata:banQinCdRuleWvHeader:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(BanQinCdRuleWvHeader entity, Model model, RedirectAttributes redirectAttributes) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, entity)) {
            j.setSuccess(false);
            j.setMsg("非法数据!");
            return j;
        }
        try {
            banQinCdRuleWvHeaderService.save(entity);
            j.put("entity", entity);
        } catch (DuplicateKeyException e) {
            j.setSuccess(false);
            j.setMsg("波次规则编码已存在!");
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }

        return j;
    }

    /**
     * 批量删除波次规则
     */
    @ResponseBody
    @RequiresPermissions("basicdata:banQinCdRuleWvHeader:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        String idArray[] = ids.split(",");
        for (String id : idArray) {
            banQinCdRuleWvHeaderService.delete(new BanQinCdRuleWvHeader(id));
        }
        j.setMsg("删除波次规则成功");
        return j;
    }

    /**
     * 导出excel文件
     */
    @ResponseBody
    @RequiresPermissions("basicdata:banQinCdRuleWvHeader:export")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public AjaxJson exportFile(BanQinCdRuleWvHeader banQinCdRuleWvHeader, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        try {
            String fileName = "波次规则" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            Page<BanQinCdRuleWvHeader> page = banQinCdRuleWvHeaderService.findPage(new Page<BanQinCdRuleWvHeader>(request, response, -1), banQinCdRuleWvHeader);
            new ExportExcel("波次规则", BanQinCdRuleWvHeader.class).setDataList(page.getList()).write(response, fileName).dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出波次规则记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 导入Excel数据
     */
    @RequiresPermissions("basicdata:banQinCdRuleWvHeader:import")
    @RequestMapping(value = "import", method = RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            int successNum = 0;
            int failureNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<BanQinCdRuleWvHeader> list = ei.getDataList(BanQinCdRuleWvHeader.class);
            for (BanQinCdRuleWvHeader banQinCdRuleWvHeader : list) {
                try {
                    banQinCdRuleWvHeaderService.save(banQinCdRuleWvHeader);
                    successNum++;
                } catch (ConstraintViolationException ex) {
                    failureNum++;
                } catch (Exception ex) {
                    failureNum++;
                }
            }
            if (failureNum > 0) {
                failureMsg.insert(0, "，失败 " + failureNum + " 条波次规则记录。");
            }
            addMessage(redirectAttributes, "已成功导入 " + successNum + " 条波次规则记录" + failureMsg);
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入波次规则失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/wms/basicdata/banQinCdRuleWvHeader/?repage";
    }

    /**
     * 下载导入波次规则数据模板
     */
    @RequiresPermissions("basicdata:banQinCdRuleWvHeader:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            String fileName = "波次规则数据导入模板.xlsx";
            List<BanQinCdRuleWvHeader> list = Lists.newArrayList();
            new ExportExcel("波次规则数据", BanQinCdRuleWvHeader.class, 1).setDataList(list).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/wms/basicdata/banQinCdRuleWvHeader/?repage";
    }

}