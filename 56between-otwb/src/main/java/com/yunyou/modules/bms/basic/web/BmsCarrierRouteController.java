package com.yunyou.modules.bms.basic.web;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.yunyou.common.config.Global;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.common.utils.excel.ImportExcel;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.bms.basic.entity.BmsCarrierRoute;
import com.yunyou.modules.bms.basic.entity.extend.BmsCarrierRouteEntity;
import com.yunyou.modules.bms.basic.entity.template.BmsCarrierRouteTemplate;
import com.yunyou.modules.bms.basic.service.BmsCarrierRouteService;
import com.yunyou.modules.bms.common.BmsException;
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
 * 承运商路由Controller
 *
 * @author zqs
 * @version 2018-06-15
 */
@Controller
@RequestMapping(value = "${adminPath}/bms/basic/bmsCarrierRoute")
public class BmsCarrierRouteController extends BaseController {
    @Autowired
    private BmsCarrierRouteService bmsCarrierRouteService;

    @ModelAttribute
    public BmsCarrierRouteEntity get(@RequestParam(required = false) String id) {
        BmsCarrierRouteEntity entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = bmsCarrierRouteService.getEntity(id);
        }
        if (entity == null) {
            entity = new BmsCarrierRouteEntity();
        }
        return entity;
    }

    /**
     * 承运商路由列表页面
     */
    @RequiresPermissions("sys:sysRoute:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/bms/basic/bmsCarrierRouteList";
    }

    /**
     * 承运商路由列表数据
     */
    @ResponseBody
    @RequiresPermissions("sys:sysRoute:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(BmsCarrierRouteEntity entity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(bmsCarrierRouteService.findPage(new Page<>(request, response), entity));
    }

    /**
     * 查看，增加，编辑承运商路由表单页面
     */
    @RequiresPermissions(value = {"sys:sysRoute:view", "sys:sysRoute:add", "sys:sysRoute:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(BmsCarrierRouteEntity entity, Model model) {
        model.addAttribute("bmsCarrierRouteEntity", entity);
        return "modules/bms/basic/bmsCarrierRouteForm";
    }

    /**
     * 保存承运商路由
     */
    @ResponseBody
    @RequiresPermissions(value = {"sys:sysRoute:add", "sys:sysRoute:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(BmsCarrierRoute bmsCarrierRoute, Model model) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, bmsCarrierRoute)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            bmsCarrierRouteService.saveEntity(bmsCarrierRoute);
        } catch (BmsException e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 删除承运商路由
     */
    @ResponseBody
    @RequiresPermissions("sys:sysRoute:del")
    @RequestMapping(value = "delete")
    public AjaxJson delete(BmsCarrierRoute bmsCarrierRoute) {
        AjaxJson j = new AjaxJson();
        bmsCarrierRouteService.delete(bmsCarrierRoute);
        j.setMsg("删除承运商路由成功");
        return j;
    }

    /**
     * 批量删除承运商路由
     */
    @ResponseBody
    @RequiresPermissions("sys:sysRoute:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            bmsCarrierRouteService.delete(bmsCarrierRouteService.get(id));
        }
        j.setMsg("删除承运商路由成功");
        return j;
    }

    /**
     * 导出excel文件
     */
    @ResponseBody
    @RequiresPermissions("sys:sysRoute:export")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public AjaxJson exportFile(BmsCarrierRoute bmsCarrierRoute, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            Page<BmsCarrierRoute> page = bmsCarrierRouteService.findPage(new Page<>(request, response, -1), bmsCarrierRoute);
            new ExportExcel("承运商路由", BmsCarrierRoute.class).setDataList(page.getList()).write(response, "承运商路由.xlsx").dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出承运商路由记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 导入Excel数据
     */
    @RequiresPermissions("sys:sysRoute:import")
    @RequestMapping(value = "import", method = RequestMethod.POST)
    public String importFile(MultipartFile file, String orgId, RedirectAttributes redirectAttributes) {
        try {
            int successNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<BmsCarrierRouteTemplate> list = ei.getDataList(BmsCarrierRouteTemplate.class);
            for (int i = 0; i < list.size(); i++) {
                try {
                    bmsCarrierRouteService.importFile(list.get(i), orgId);
                    successNum++;
                } catch (BmsException e) {
                    failureMsg.append("第").append(i + 1).append("行失败，").append(e.getMessage()).append("<br>");
                } catch (Exception e) {
                    logger.error("承运商路由导入失败[" + JSON.toJSONString(list.get(i)) + "]", e);
                    failureMsg.append("第").append(i + 1).append("行失败，").append(e.getMessage()).append("<br>");
                }
            }
            addMessage(redirectAttributes, "成功导入 " + successNum + " 条承运商路由记录<br>" + failureMsg);
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入承运商路由失败！失败信息：<br>" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/bms/basic/bmsCarrierRoute/?repage";
    }

    /**
     * 下载导入承运商路由数据模板
     */
    @RequiresPermissions("sys:sysRoute:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            new ExportExcel(null, BmsCarrierRouteTemplate.class, 1).setDataList(Lists.newArrayList()).write(response, "承运商路由数据导入模板.xlsx").dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/bms/basic/bmsCarrierRoute/?repage";
    }

    /**
     * 弹出窗数据
     */
    @ResponseBody
    @RequestMapping(value = "grid")
    public Map<String, Object> grid(BmsCarrierRouteEntity entity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(bmsCarrierRouteService.findGrid(new Page<>(request, response), entity));
    }

}