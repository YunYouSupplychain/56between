package com.yunyou.modules.wms.task.web;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.config.Global;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.common.utils.excel.ImportExcel;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.wms.inbound.service.BanQinInboundPaOperationService;
import com.yunyou.modules.wms.report.entity.PutawayTaskLabel;
import com.yunyou.modules.wms.task.entity.BanQinWmTaskPa;
import com.yunyou.modules.wms.task.entity.BanQinWmTaskPaEntity;
import com.yunyou.modules.wms.task.entity.extend.BanQinWmTaskPaExportEntity;
import com.yunyou.modules.wms.task.service.BanQinWmTaskPaService;
import com.google.common.collect.Lists;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
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
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 上架任务Controller
 *
 * @author WMJ
 * @version 2019-01-26
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/task/banQinWmTaskPa")
public class BanQinWmTaskPaController extends BaseController {
    @Autowired
    private BanQinWmTaskPaService banQinWmTaskPaService;
    @Autowired
    private BanQinInboundPaOperationService banQinInboundPaOperationService;

    @ModelAttribute
    public BanQinWmTaskPaEntity get(@RequestParam(required = false) String id) {
        BanQinWmTaskPaEntity entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = banQinWmTaskPaService.getEntity(id);
        }
        if (entity == null) {
            entity = new BanQinWmTaskPaEntity();
        }
        return entity;
    }

    /**
     * 上架任务列表页面
     */
    @RequiresPermissions("task:banQinWmTaskPa:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/wms/task/banQinWmTaskPaList";
    }

    /**
     * 上架任务列表数据
     */
    @ResponseBody
    @RequiresPermissions("task:banQinWmTaskPa:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(BanQinWmTaskPaEntity banQinWmTaskPaEntity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(banQinWmTaskPaService.findPage(new Page<BanQinWmTaskPaEntity>(request, response), banQinWmTaskPaEntity));
    }

    /**
     * 上架任务列表数据
     */
    @ResponseBody
    @RequestMapping(value = "grid")
    public Map<String, Object> grid(BanQinWmTaskPaEntity banQinWmTaskPaEntity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(banQinWmTaskPaService.findGrid(new Page<BanQinWmTaskPaEntity>(request, response), banQinWmTaskPaEntity));
    }

    /**
     * 查看，增加，编辑上架任务表单页面
     */
    @RequiresPermissions(value = {"task:banQinWmTaskPa:view", "task:banQinWmTaskPa:add", "task:banQinWmTaskPa:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(BanQinWmTaskPaEntity banQinWmTaskPaEntity, Model model) {
        model.addAttribute("banQinWmTaskPaEntity", banQinWmTaskPaEntity);
        return "modules/wms/task/banQinWmTaskPaForm";
    }

    /**
     * 批量删除上架任务
     */
    @ResponseBody
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(@RequestBody List<BanQinWmTaskPaEntity> list) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinInboundPaOperationService.inboundBatchRemoveTaskPa(list);
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 批量上架确认
     */
    @ResponseBody
    @RequestMapping(value = "putAway")
    public AjaxJson putAway(@RequestBody List<BanQinWmTaskPaEntity> list) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinInboundPaOperationService.inboundBatchPutaway(list);
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 导出excel文件
     */
    @ResponseBody
    @RequiresPermissions("task:banQinWmTaskPa:export")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public AjaxJson exportFile(BanQinWmTaskPaEntity entity, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            String fileName = "上架任务.xlsx";
            Page<BanQinWmTaskPaEntity> page = banQinWmTaskPaService.findPage(new Page<BanQinWmTaskPaEntity>(request, response, -1), entity);
            new ExportExcel("", BanQinWmTaskPaExportEntity.class).setDataList(page.getList()).write(response, fileName).dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出上架任务记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 导入Excel数据
     */
    @RequiresPermissions("task:banQinWmTaskPa:import")
    @RequestMapping(value = "import", method = RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            int successNum = 0;
            int failureNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<BanQinWmTaskPa> list = ei.getDataList(BanQinWmTaskPa.class);
            for (BanQinWmTaskPa banQinWmTaskPa : list) {
                try {
                    banQinWmTaskPaService.save(banQinWmTaskPa);
                    successNum++;
                } catch (Exception ex) {
                    failureNum++;
                }
            }
            if (failureNum > 0) {
                failureMsg.insert(0, "，失败 " + failureNum + " 条上架任务记录。");
            }
            addMessage(redirectAttributes, "已成功导入 " + successNum + " 条上架任务记录" + failureMsg);
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入上架任务失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/task/banQinWmTaskPa/?repage";
    }

    /**
     * 下载导入上架任务数据模板
     */
    @RequiresPermissions("task:banQinWmTaskPa:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            String fileName = "上架任务数据导入模板.xlsx";
            List<BanQinWmTaskPa> list = Lists.newArrayList();
            new ExportExcel("上架任务数据", BanQinWmTaskPa.class, 1).setDataList(list).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/task/banQinWmTaskPa/?repage";
    }

    /**
     * 打印上架任务单
     */
    @RequestMapping(value = "/printPaTask")
    public String printPaTask(Model model, String ids) {
        List<PutawayTaskLabel> result = banQinWmTaskPaService.getPaTaskReport(Arrays.asList(ids.split(",")));
        // 报表数据源
        JRDataSource jrDataSource = new JRBeanCollectionDataSource(result);
        // 动态指定报表模板url
        model.addAttribute("url", "classpath:/jasper/putawayTask.jasper");
        model.addAttribute("format", "pdf");
        model.addAttribute("jrMainDataSource", jrDataSource);
        return "iReportView";
    }

}