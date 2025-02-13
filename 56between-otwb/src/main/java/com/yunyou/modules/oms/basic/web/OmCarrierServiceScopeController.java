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
import com.yunyou.modules.oms.basic.entity.OmCarrierServiceScope;
import com.yunyou.modules.oms.basic.entity.extend.OmCarrierServiceScopeEntity;
import com.yunyou.modules.oms.basic.service.OmCarrierServiceScopeService;
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
 * 承运商服务范围Controller
 *
 * @author Jianhua Liu
 * @version 2019-10-23
 */
@Controller
@RequestMapping(value = "${adminPath}/oms/basic/omCarrierServiceScope")
public class OmCarrierServiceScopeController extends BaseController {

    @Autowired
    private OmCarrierServiceScopeService omCarrierServiceScopeService;

    @ModelAttribute
    public OmCarrierServiceScopeEntity get(@RequestParam(required = false) String id) {
        OmCarrierServiceScopeEntity entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = omCarrierServiceScopeService.getEntity(id);
        }
        if (entity == null) {
            entity = new OmCarrierServiceScopeEntity();
        }
        return entity;
    }

    /**
     * 承运商服务范围列表页面
     */
    @RequiresPermissions("basic:omCarrierServiceScope:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/oms/basic/omCarrierServiceScopeList";
    }

    /**
     * 承运商服务范围列表数据
     */
    @ResponseBody
    @RequiresPermissions("basic:omCarrierServiceScope:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(OmCarrierServiceScopeEntity entity, HttpServletRequest request, HttpServletResponse response, Model model) {
        return getBootstrapData(omCarrierServiceScopeService.findPage(new Page<OmCarrierServiceScopeEntity>(request, response), entity));
    }

    /**
     * 查看，增加，编辑承运商服务范围表单页面
     */
    @RequiresPermissions(value = {"basic:omCarrierServiceScope:view", "basic:omCarrierServiceScope:add", "basic:omCarrierServiceScope:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(OmCarrierServiceScopeEntity entity, Model model) {
        model.addAttribute("omCarrierServiceScopeEntity", entity);
        return "modules/oms/basic/omCarrierServiceScopeForm";
    }

    /**
     * 保存承运商服务范围
     */
    @ResponseBody
    @RequiresPermissions(value = {"basic:omCarrierServiceScope:add", "basic:omCarrierServiceScope:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(OmCarrierServiceScope omCarrierServiceScope, Model model, RedirectAttributes redirectAttributes) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, omCarrierServiceScope)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            omCarrierServiceScopeService.save(omCarrierServiceScope);
            j.setSuccess(true);
            j.setMsg("保存承运商服务范围成功");
        } catch (Exception e) {
            logger.error("【承运商服务范围】", e);
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 删除承运商服务范围
     */
    @ResponseBody
    @RequiresPermissions("basic:omCarrierServiceScope:del")
    @RequestMapping(value = "delete")
    public AjaxJson delete(OmCarrierServiceScope omCarrierServiceScope, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        omCarrierServiceScopeService.delete(omCarrierServiceScope);
        j.setMsg("删除承运商服务范围成功");
        return j;
    }

    /**
     * 批量删除承运商服务范围
     */
    @ResponseBody
    @RequiresPermissions("basic:omCarrierServiceScope:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            omCarrierServiceScopeService.delete(omCarrierServiceScopeService.get(id));
        }
        j.setMsg("删除承运商服务范围成功");
        return j;
    }

    /**
     * 导出excel文件
     */
    @ResponseBody
    @RequiresPermissions("basic:omCarrierServiceScope:export")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public AjaxJson exportFile(OmCarrierServiceScope omCarrierServiceScope, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        try {
            String fileName = "承运商服务范围" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            Page<OmCarrierServiceScope> page = omCarrierServiceScopeService.findPage(new Page<OmCarrierServiceScope>(request, response, -1), omCarrierServiceScope);
            new ExportExcel("承运商服务范围", OmCarrierServiceScope.class).setDataList(page.getList()).write(response, fileName).dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出承运商服务范围记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 导入Excel数据
     */
    @RequiresPermissions("basic:omCarrierServiceScope:import")
    @RequestMapping(value = "import", method = RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            int successNum = 0;
            int failureNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<OmCarrierServiceScope> list = ei.getDataList(OmCarrierServiceScope.class);
            for (OmCarrierServiceScope omCarrierServiceScope : list) {
                try {
                    omCarrierServiceScopeService.save(omCarrierServiceScope);
                    successNum++;
                } catch (Exception ex) {
                    failureNum++;
                }
            }
            if (failureNum > 0) {
                failureMsg.insert(0, "，失败 " + failureNum + " 条承运商服务范围记录。");
            }
            addMessage(redirectAttributes, "已成功导入 " + successNum + " 条承运商服务范围记录" + failureMsg);
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入承运商服务范围失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/oms/basic/omCarrierServiceScope/?repage";
    }

    /**
     * 下载导入承运商服务范围数据模板
     */
    @RequiresPermissions("basic:omCarrierServiceScope:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            String fileName = "承运商服务范围数据导入模板.xlsx";
            List<OmCarrierServiceScope> list = Lists.newArrayList();
            new ExportExcel("承运商服务范围数据", OmCarrierServiceScope.class, 1).setDataList(list).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/oms/basic/omCarrierServiceScope/?repage";
    }

}