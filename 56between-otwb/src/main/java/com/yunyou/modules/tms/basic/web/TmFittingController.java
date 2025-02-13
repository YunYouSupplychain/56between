package com.yunyou.modules.tms.basic.web;

import com.google.common.collect.Lists;
import com.yunyou.common.exception.GlobalException;
import com.yunyou.common.config.Global;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.common.utils.excel.ImportExcel;
import com.yunyou.core.persistence.BaseEntity;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.sys.utils.UserUtils;
import com.yunyou.modules.tms.basic.entity.TmFitting;
import com.yunyou.modules.tms.basic.entity.extend.TmFittingEntity;
import com.yunyou.modules.tms.basic.service.TmFittingService;
import com.yunyou.modules.tms.common.TmsException;
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
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "${adminPath}/tms/basic/tmFitting")
public class TmFittingController extends BaseController {
    @Autowired
    private TmFittingService tmFittingService;

    @ModelAttribute
    public TmFittingEntity get(@RequestParam(required = false) String id) {
        TmFittingEntity entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = tmFittingService.getEntity(id);
        }
        if (entity == null) {
            entity = new TmFittingEntity();
        }
        return entity;
    }

    @RequiresPermissions("tms:basic:fitting:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/tms/basic/tmFittingList";
    }

    @ResponseBody
    @RequiresPermissions("tms:basic:fitting:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(TmFittingEntity qEntity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(tmFittingService.findPage(new Page<TmFittingEntity>(request, response), qEntity));
    }

    @RequiresPermissions(value = {"tms:basic:fitting:view", "tms:basic:fitting:add", "tms:basic:fitting:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(TmFittingEntity entity, Model model) {
        model.addAttribute("tmFittingEntity", entity);
        return "modules/tms/basic/tmFittingForm";
    }

    @ResponseBody
    @RequiresPermissions(value = {"tms:basic:fitting:add", "tms:basic:fitting:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(TmFitting tmFitting, Model model) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, tmFitting)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            tmFittingService.saveValidator(tmFitting);
            tmFittingService.save(tmFitting);
        } catch (TmsException e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    @ResponseBody
    @RequiresPermissions("tms:basic:fitting:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();

        StringBuilder errMsg = new StringBuilder();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            TmFitting tmFitting = tmFittingService.get(id);
            try {
                tmFittingService.delete(tmFitting);
            } catch (Exception e) {
                logger.error("删除配件id=[" + id + "]", e);
                errMsg.append("<br>").append("配件[").append(tmFitting.getFittingCode()).append("]删除失败");
            }
        }
        if (StringUtils.isNotBlank(errMsg)) {
            j.setMsg("操作成功，其中" + errMsg.toString());
        }
        return j;
    }

    @ResponseBody
    @RequiresPermissions("tms:basic:fitting:export")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public AjaxJson exportFile(TmFittingEntity qEntity, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            Page<TmFittingEntity> page = tmFittingService.findPage(new Page<TmFittingEntity>(request, response, -1), qEntity);
            new ExportExcel("配件信息", TmFitting.class).setDataList(page.getList()).write(response, "配件信息.xlsx").dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出配件信息记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    @RequiresPermissions("tms:basic:fitting:import")
    @RequestMapping(value = "import", method = RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            int successNum = 0;
            int failureNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<TmFitting> list = ei.getDataList(TmFitting.class);
            for (TmFitting tmFitting : list) {
                try {
                    tmFittingService.save(tmFitting);
                    successNum++;
                } catch (Exception ex) {
                    failureNum++;
                }
            }
            if (failureNum > 0) {
                failureMsg.insert(0, "，失败 " + failureNum + " 条配件信息记录。");
            }
            addMessage(redirectAttributes, "已成功导入 " + successNum + " 条配件信息记录" + failureMsg);
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入配件信息失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/tms/basic/tmFitting/?repage";
    }

    @RequiresPermissions("tms:basic:fitting:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            new ExportExcel("配件信息数据", TmFitting.class, 1).setDataList(Lists.newArrayList()).write(response, "数据导入模板.xlsx").dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/tms/basic/tmFitting/?repage";
    }

    @ResponseBody
    @RequiresPermissions(value = {"tms:basic:fitting:enable", "tms:basic:fitting:unable"}, logical = Logical.OR)
    @RequestMapping(value = "enable")
    public AjaxJson enable(@RequestParam String ids, @RequestParam String flag) {
        AjaxJson j = new AjaxJson();

        StringBuilder errMsg = new StringBuilder();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            TmFitting tmFitting = tmFittingService.get(id);
            try {
                tmFitting.setDelFlag("0".equals(flag) ? BaseEntity.DEL_FLAG_NORMAL : BaseEntity.DEL_FLAG_DELETE);
                tmFitting.setUpdateBy(UserUtils.getUser());
                tmFitting.setUpdateDate(new Date());
                tmFittingService.save(tmFitting);
            } catch (GlobalException e) {
                errMsg.append("<br>").append("配件[").append(tmFitting.getFittingCode()).append("]").append(e.getMessage());
            } catch (Exception e) {
                logger.error("启用/停用配件id=[" + id + "]", e);
                errMsg.append("<br>").append("配件[").append(tmFitting.getFittingCode()).append("]操作失败");
            }
        }
        if (StringUtils.isNotBlank(errMsg)) {
            j.setMsg("操作成功，其中" + errMsg.toString());
        }
        return j;
    }

    @ResponseBody
    @RequestMapping(value = "grid")
    public Map<String, Object> grid(TmFittingEntity entity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(tmFittingService.findGrid(new Page<TmFittingEntity>(request, response), entity));
    }

}
