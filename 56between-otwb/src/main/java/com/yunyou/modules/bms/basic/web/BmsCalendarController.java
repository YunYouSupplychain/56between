package com.yunyou.modules.bms.basic.web;

import com.alibaba.fastjson.JSON;
import com.yunyou.modules.bms.common.BmsException;
import com.google.common.collect.Lists;
import com.yunyou.common.config.Global;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.common.utils.excel.ImportExcel;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.bms.basic.entity.extend.BmsCalendarEntity;
import com.yunyou.modules.bms.basic.entity.template.BmsCalendarTemplate;
import com.yunyou.modules.bms.basic.service.BmsCalendarService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "${adminPath}/bms/basic/calendar")
public class BmsCalendarController extends BaseController {
    @Autowired
    private BmsCalendarService bmsCalendarService;

    @ModelAttribute
    public BmsCalendarEntity get(String id) {
        BmsCalendarEntity entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = bmsCalendarService.getEntity(id);
        }
        if (entity == null) {
            entity = new BmsCalendarEntity();
        }
        return entity;
    }

    @RequiresPermissions(value = "bms:calendar:list")
    @RequestMapping(value = {"", "list"})
    public String list(Model model) {
        return "modules/bms/basic/bmsCalendarList";
    }

        @RequiresPermissions(value = {"bms:calendar:add", "bms:calendar:edit", "bms:calendar:view"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(BmsCalendarEntity entity, Model model) {
        model.addAttribute("bmsCalendarEntity", entity);
        return "modules/bms/basic/bmsCalendarForm";
    }

    @ResponseBody
    @RequiresPermissions(value = "bms:calendar:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(BmsCalendarEntity qEntity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(bmsCalendarService.findPage(new Page<BmsCalendarEntity>(request, response), qEntity));
    }

    @ResponseBody
    @RequiresPermissions(value = {"bms:calendar:add", "bms:calendar:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(BmsCalendarEntity entity, Model model) {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, entity)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            bmsCalendarService.saveEntity(entity);
        } catch (BmsException e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } catch (RuntimeException e) {
            logger.error("日历保存异常", e);
            j.setSuccess(false);
            j.setMsg("保存异常");
        }
        return j;
    }

    @ResponseBody
    @RequiresPermissions(value = "bms:calendar:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();

        StringBuilder errMsg = new StringBuilder();
        String[] idArr = StringUtils.split(ids, ",");
        for (String id : idArr) {
            try {
                bmsCalendarService.removeEntity(id);
            } catch (BmsException e) {
                errMsg.append(e.getMessage()).append("<br>");
            } catch (RuntimeException e) {
                logger.error("日历删除异常", e);
                errMsg.append(e.getMessage()).append("<br>");
            }
        }
        if (StringUtils.isNotBlank(errMsg)) {
            j.setSuccess(false);
            j.setMsg(errMsg.toString());
        }
        return j;
    }

        @RequiresPermissions("bms:calendar:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            new ExportExcel(null, BmsCalendarTemplate.class, 2).setDataList(Lists.newArrayList()).write(response, "导入模板.xlsx").dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/bms/basic/calendar/?repage";
    }

    @RequiresPermissions("bms:calendar:import")
    @RequestMapping(value = "import", method = RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            int successNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<BmsCalendarTemplate> list = ei.getDataList(BmsCalendarTemplate.class);
            for (int i = 0; i < list.size(); i++) {
                try {
                    bmsCalendarService.importFile(list.get(i));
                    successNum++;
                } catch (BmsException e) {
                    failureMsg.append("第").append(i + 1).append("行失败，").append(e.getMessage()).append("<br>");
                } catch (Exception e) {
                    logger.error("日历导入失败[" + JSON.toJSONString(list.get(i)) + "]", e);
                    failureMsg.append("第").append(i + 1).append("行失败，").append(e.getMessage()).append("<br>");
                }
            }
            addMessage(redirectAttributes, "成功导入 " + successNum + " 条记录<br>" + failureMsg);
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入失败！失败信息：<br>" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/bms/basic/calendar/?repage";
    }
}
