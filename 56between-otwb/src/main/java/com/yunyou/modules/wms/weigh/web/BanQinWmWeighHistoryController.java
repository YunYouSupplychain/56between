package com.yunyou.modules.wms.weigh.web;

import com.google.common.collect.Lists;
import com.yunyou.common.config.Global;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.time.DateUtils;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.common.utils.excel.ImportExcel;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.wms.weigh.entity.BanQinWmWeighHistory;
import com.yunyou.modules.wms.weigh.service.BanQinWmWeighHistoryService;
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
import java.util.List;
import java.util.Map;

/**
 * 称重履历表Controller
 *
 * @author zyf
 * @version 2020-01-08
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/weigh/wmWeighHistory")
public class BanQinWmWeighHistoryController extends BaseController {

    @Autowired
    private BanQinWmWeighHistoryService banQinWmWeighHistoryService;

    @ModelAttribute
    public BanQinWmWeighHistory get(@RequestParam(required = false) String id) {
        BanQinWmWeighHistory entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = banQinWmWeighHistoryService.get(id);
        }
        if (entity == null) {
            entity = new BanQinWmWeighHistory();
        }
        return entity;
    }

    /**
     * 称重履历表列表页面
     */
    @RequiresPermissions("wms:weigh:banQinWmWeighHistory:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/wms/weigh/wmWeighHistoryList";
    }

    /**
     * 称重履历表列表数据
     */
    @ResponseBody
    @RequiresPermissions("wms:weigh:banQinWmWeighHistory:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(BanQinWmWeighHistory banQinWmWeighHistory, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<BanQinWmWeighHistory> page = banQinWmWeighHistoryService.findPage(new Page<BanQinWmWeighHistory>(request, response), banQinWmWeighHistory);
        return getBootstrapData(page);
    }

    /**
     * 查看，增加，编辑称重履历表表单页面
     */
    @RequiresPermissions(value = {"wms:weigh:banQinWmWeighHistory:view", "wms:weigh:banQinWmWeighHistory:add", "wms:weigh:banQinWmWeighHistory:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(BanQinWmWeighHistory banQinWmWeighHistory, Model model) {
        model.addAttribute("wmWeighHistory", banQinWmWeighHistory);
        return "modules/wms/weigh/wmWeighHistoryForm";
    }

    /**
     * 保存称重履历表
     */
    @ResponseBody
    @RequiresPermissions(value = {"wms:weigh:banQinWmWeighHistory:add", "wms:weigh:banQinWmWeighHistory:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(BanQinWmWeighHistory banQinWmWeighHistory, Model model, RedirectAttributes redirectAttributes) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, banQinWmWeighHistory)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        banQinWmWeighHistoryService.save(banQinWmWeighHistory);//新建或者编辑保存
        j.setSuccess(true);
        j.setMsg("保存称重履历表成功");
        return j;
    }

    /**
     * 删除称重履历表
     */
    @ResponseBody
    @RequiresPermissions("wms:weigh:banQinWmWeighHistory:del")
    @RequestMapping(value = "delete")
    public AjaxJson delete(BanQinWmWeighHistory banQinWmWeighHistory, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        banQinWmWeighHistoryService.delete(banQinWmWeighHistory);
        j.setMsg("删除称重履历表成功");
        return j;
    }

    /**
     * 批量删除称重履历表
     */
    @ResponseBody
    @RequiresPermissions("wms:weigh:banQinWmWeighHistory:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        String idArray[] = ids.split(",");
        for (String id : idArray) {
            banQinWmWeighHistoryService.delete(banQinWmWeighHistoryService.get(id));
        }
        j.setMsg("删除称重履历表成功");
        return j;
    }

    /**
     * 导出excel文件
     */
    @ResponseBody
    @RequiresPermissions("wms:weigh:banQinWmWeighHistory:export")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public AjaxJson exportFile(BanQinWmWeighHistory banQinWmWeighHistory, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        try {
            String fileName = "称重履历表" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            Page<BanQinWmWeighHistory> page = banQinWmWeighHistoryService.findPage(new Page<BanQinWmWeighHistory>(request, response, -1), banQinWmWeighHistory);
            new ExportExcel("称重履历表", BanQinWmWeighHistory.class).setDataList(page.getList()).write(response, fileName).dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出称重履历表记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 导入Excel数据
     */
    @RequiresPermissions("wms:weigh:banQinWmWeighHistory:import")
    @RequestMapping(value = "import", method = RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            int successNum = 0;
            int failureNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<BanQinWmWeighHistory> list = ei.getDataList(BanQinWmWeighHistory.class);
            for (BanQinWmWeighHistory banQinWmWeighHistory : list) {
                try {
                    banQinWmWeighHistoryService.save(banQinWmWeighHistory);
                    successNum++;
                } catch (ConstraintViolationException ex) {
                    failureNum++;
                } catch (Exception ex) {
                    failureNum++;
                }
            }
            if (failureNum > 0) {
                failureMsg.insert(0, "，失败 " + failureNum + " 条称重履历表记录。");
            }
            addMessage(redirectAttributes, "已成功导入 " + successNum + " 条称重履历表记录" + failureMsg);
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入称重履历表失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/wms/weigh/banQinWmWeighHistory/?repage";
    }

    /**
     * 下载导入称重履历表数据模板
     */
    @RequiresPermissions("wms:weigh:banQinWmWeighHistory:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            String fileName = "称重履历表数据导入模板.xlsx";
            List<BanQinWmWeighHistory> list = Lists.newArrayList();
            new ExportExcel("称重履历表数据", BanQinWmWeighHistory.class, 1).setDataList(list).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/wms/weigh/banQinWmWeighHistory/?repage";
    }

}