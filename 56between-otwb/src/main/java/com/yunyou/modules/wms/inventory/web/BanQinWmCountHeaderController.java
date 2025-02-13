package com.yunyou.modules.wms.inventory.web;

import com.google.common.collect.Lists;
import com.yunyou.common.ResultMessage;
import com.yunyou.common.config.Global;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.time.DateUtils;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.common.utils.excel.ImportExcel;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.wms.inventory.entity.BanQinWmCountHeader;
import com.yunyou.modules.wms.inventory.entity.BanQinWmCountHeaderEntity;
import com.yunyou.modules.wms.inventory.entity.extend.BanQinWmCountHeaderExportEntity;
import com.yunyou.modules.wms.inventory.service.BanQinWmCountHeaderService;
import com.yunyou.modules.wms.report.entity.CountTaskLabel;
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
import javax.validation.ConstraintViolationException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 库存盘点Controller
 *
 * @author WMJ
 * @version 2019-01-28
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/inventory/banQinWmCountHeader")
public class BanQinWmCountHeaderController extends BaseController {

    @Autowired
    private BanQinWmCountHeaderService banQinWmCountHeaderService;

    @ModelAttribute
    public BanQinWmCountHeaderEntity get(@RequestParam(required = false) String id) {
        BanQinWmCountHeaderEntity entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = banQinWmCountHeaderService.getEntity(id);
        }
        if (entity == null) {
            entity = new BanQinWmCountHeaderEntity();
        }
        return entity;
    }

    /**
     * 库存盘点列表页面
     */
    @RequiresPermissions("inventory:banQinWmCountHeader:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/wms/inventory/banQinWmCountHeaderList";
    }

    /**
     * 库存盘点列表数据
     */
    @ResponseBody
    @RequiresPermissions("inventory:banQinWmCountHeader:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(BanQinWmCountHeaderEntity banQinWmCountHeaderEntity, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<BanQinWmCountHeaderEntity> page = banQinWmCountHeaderService.findPage(new Page<BanQinWmCountHeaderEntity>(request, response), banQinWmCountHeaderEntity);
        return getBootstrapData(page);
    }

    /**
     * 查看，增加，编辑库存盘点表单页面
     */
    @RequiresPermissions(value = {"inventory:banQinWmCountHeader:view", "inventory:banQinWmCountHeader:add", "inventory:banQinWmCountHeader:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(BanQinWmCountHeaderEntity banQinWmCountHeaderEntity, Model model) {
        model.addAttribute("banQinWmCountHeaderEntity", banQinWmCountHeaderEntity);
        if (StringUtils.isBlank(banQinWmCountHeaderEntity.getId())) {//如果ID是空为添加
            model.addAttribute("isAdd", true);
        }
        return "modules/wms/inventory/banQinWmCountHeaderForm";
    }

    /**
     * 保存库存盘点
     */
    @ResponseBody
    @RequiresPermissions(value = {"inventory:banQinWmCountHeader:add", "inventory:banQinWmCountHeader:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(BanQinWmCountHeaderEntity banQinWmCountHeaderEntity, Model model, RedirectAttributes redirectAttributes) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, banQinWmCountHeaderEntity)) {
            j.setSuccess(false);
            j.setMsg("非法数据");
            return j;
        }
        try {
            BanQinWmCountHeaderEntity entity = banQinWmCountHeaderService.saveEntity(banQinWmCountHeaderEntity);
            j.setMsg("保存成功");

            LinkedHashMap<String, Object> map = new LinkedHashMap<>();
            map.put("entity", entity);
            j.setBody(map);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 批量删除库存盘点
     */
    @ResponseBody
    @RequiresPermissions("inventory:banQinWmCountHeader:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinWmCountHeaderService.removeCountEntity(ids.split(","));
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
    @RequiresPermissions("inventory:banQinWmCountHeader:export")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public AjaxJson exportFile(BanQinWmCountHeaderEntity entity, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        try {
            String fileName = "库存盘点" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            Page<BanQinWmCountHeaderEntity> page = banQinWmCountHeaderService.findPage(new Page<BanQinWmCountHeaderEntity>(request, response, -1), entity);
            new ExportExcel("", BanQinWmCountHeaderExportEntity.class).setDataList(page.getList()).write(response, fileName).dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出库存盘点记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 导入Excel数据
     */
    @RequiresPermissions("inventory:banQinWmCountHeader:import")
    @RequestMapping(value = "import", method = RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            int successNum = 0;
            int failureNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<BanQinWmCountHeader> list = ei.getDataList(BanQinWmCountHeader.class);
            for (BanQinWmCountHeader banQinWmCountHeader : list) {
                try {
                    banQinWmCountHeaderService.save(banQinWmCountHeader);
                    successNum++;
                } catch (ConstraintViolationException ex) {
                    failureNum++;
                } catch (Exception ex) {
                    failureNum++;
                }
            }
            if (failureNum > 0) {
                failureMsg.insert(0, "，失败 " + failureNum + " 条库存盘点记录。");
            }
            addMessage(redirectAttributes, "已成功导入 " + successNum + " 条库存盘点记录" + failureMsg);
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入库存盘点失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/wms/inventory/banQinWmCountHeader/?repage";
    }

    /**
     * 下载导入库存盘点数据模板
     */
    @RequiresPermissions("inventory:banQinWmCountHeader:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            String fileName = "库存盘点数据导入模板.xlsx";
            List<BanQinWmCountHeader> list = Lists.newArrayList();
            new ExportExcel("库存盘点数据", BanQinWmCountHeader.class, 1).setDataList(list).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/wms/inventory/banQinWmCountHeader/?repage";
    }

    /**
     * 生成普通盘点任务
     *
     * @param ids
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "createCount")
    public AjaxJson createCount(String ids) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinWmCountHeaderService.generateOrdinaryCountTask(ids);
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("生成盘点任务异常:" + e.getMessage());
        }
        return j;
    }

    /**
     * 生成复盘任务
     *
     * @param ids
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "createRecount")
    public AjaxJson createRecount(String ids) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinWmCountHeaderService.generateCompoundTask(ids);
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("生成复盘任务异常:" + e.getMessage());
        }
        return j;
    }

    /**
     * 取消盘点任务
     *
     * @param ids
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "cancelCountTask")
    public AjaxJson cancelCountTask(String ids) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinWmCountHeaderService.cancelCountTask(ids);
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("取消盘点任务异常:" + e.getMessage());
        }
        return j;
    }

    /**
     * 关闭盘点
     *
     * @param ids
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "closeCount")
    public AjaxJson closeCount(String ids) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinWmCountHeaderService.closeCount(ids);
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("关闭盘点异常:" + e.getMessage());
        }
        return j;
    }

    /**
     * 取消盘点
     *
     * @param ids
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "cancelCount")
    public AjaxJson cancelCount(String ids) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinWmCountHeaderService.cancelCount(ids);
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("取消盘点异常:" + e.getMessage());
        }
        return j;
    }

    /**
     * 生成调整单
     *
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "createAd")
    public AjaxJson createAd(String id) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinWmCountHeaderService.generateAdOrder(id);
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("生成调整单异常:" + e.getMessage());
        }
        return j;
    }

    /**
     * 打印盘点任务
     * @param model
     * @return
     */
    @RequestMapping(value = "/printCountTask")
    public String printCountTask(Model model, String ids) {
        List<CountTaskLabel> result = banQinWmCountHeaderService.getCountTaskLabel(Arrays.asList(ids.split(",")));
        // 报表数据源
        JRDataSource jrDataSource = new JRBeanCollectionDataSource(result);
        // 动态指定报表模板url
        model.addAttribute("url", "classpath:/jasper/countTask.jasper");
        model.addAttribute("format", "pdf");
        model.addAttribute("jrMainDataSource", jrDataSource);

        return "iReportView";
    }

}