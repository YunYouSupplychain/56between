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
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhCycle;
import com.yunyou.modules.wms.basicdata.service.BanQinCdWhCycleService;
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
 * 循环级别Controller
 *
 * @author WMJ
 * @version 2019-01-25
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/basicdata/banQinCdWhCycle")
public class BanQinCdWhCycleController extends BaseController {

    @Autowired
    private BanQinCdWhCycleService banQinCdWhCycleService;

    @ModelAttribute
    public BanQinCdWhCycle get(@RequestParam(required = false) String id) {
        BanQinCdWhCycle entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = banQinCdWhCycleService.get(id);
        }
        if (entity == null) {
            entity = new BanQinCdWhCycle();
        }
        return entity;
    }

    /**
     * 循环级别列表页面
     */
    @RequiresPermissions("basicdata:banQinCdWhCycle:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/wms/basicdata/banQinCdWhCycleList";
    }

    /**
     * 循环级别列表数据
     */
    @ResponseBody
    @RequiresPermissions("basicdata:banQinCdWhCycle:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(BanQinCdWhCycle banQinCdWhCycle, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<BanQinCdWhCycle> page = banQinCdWhCycleService.findPage(new Page<BanQinCdWhCycle>(request, response), banQinCdWhCycle);
        return getBootstrapData(page);
    }

    /**
     * 循环级别弹出框数据
     */
    @ResponseBody
    @RequestMapping(value = "grid")
    public Map<String, Object> grid(BanQinCdWhCycle banQinCdWhCycle, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<BanQinCdWhCycle> page = banQinCdWhCycleService.findPage(new Page<BanQinCdWhCycle>(request, response), banQinCdWhCycle);
        return getBootstrapData(page);
    }

    /**
     * 查看，增加，编辑循环级别表单页面
     */
    @RequiresPermissions(value = {"basicdata:banQinCdWhCycle:view", "basicdata:banQinCdWhCycle:add", "basicdata:banQinCdWhCycle:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(BanQinCdWhCycle banQinCdWhCycle, Model model) {
        model.addAttribute("banQinCdWhCycle", banQinCdWhCycle);
        if (StringUtils.isBlank(banQinCdWhCycle.getId())) {//如果ID是空为添加
            model.addAttribute("isAdd", true);
        }
        return "modules/wms/basicdata/banQinCdWhCycleForm";
    }

    /**
     * 保存循环级别
     */
    @RequiresPermissions(value = {"basicdata:banQinCdWhCycle:add", "basicdata:banQinCdWhCycle:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    @ResponseBody
    public AjaxJson save(BanQinCdWhCycle banQinCdWhCycle, Model model, RedirectAttributes redirectAttributes) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, banQinCdWhCycle)) {
            j.setSuccess(false);
            j.setMsg("非法数据！");
            return j;
        }
        try {
            banQinCdWhCycleService.save(banQinCdWhCycle);
        } catch (DuplicateKeyException e) {
            j.setSuccess(false);
            j.setMsg("编码已存在!");
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }

        return j;
    }

    /**
     * 批量删除循环级别
     */
    @ResponseBody
    @RequiresPermissions("basicdata:banQinCdWhCycle:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        String idArray[] = ids.split(",");
        for (String id : idArray) {
            banQinCdWhCycleService.delete(new BanQinCdWhCycle(id));
        }
        j.setMsg("删除循环级别成功");
        return j;
    }

    /**
     * 导出excel文件
     */
    @ResponseBody
    @RequiresPermissions("basicdata:banQinCdWhCycle:export")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public AjaxJson exportFile(BanQinCdWhCycle banQinCdWhCycle, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        try {
            String fileName = "循环级别" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            Page<BanQinCdWhCycle> page = banQinCdWhCycleService.findPage(new Page<BanQinCdWhCycle>(request, response, -1), banQinCdWhCycle);
            new ExportExcel("循环级别", BanQinCdWhCycle.class).setDataList(page.getList()).write(response, fileName).dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出循环级别记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 导入Excel数据
     */
    @RequiresPermissions("basicdata:banQinCdWhCycle:import")
    @RequestMapping(value = "import", method = RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            int successNum = 0;
            int failureNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<BanQinCdWhCycle> list = ei.getDataList(BanQinCdWhCycle.class);
            for (BanQinCdWhCycle banQinCdWhCycle : list) {
                try {
                    banQinCdWhCycleService.save(banQinCdWhCycle);
                    successNum++;
                } catch (ConstraintViolationException ex) {
                    failureNum++;
                } catch (Exception ex) {
                    failureNum++;
                }
            }
            if (failureNum > 0) {
                failureMsg.insert(0, "，失败 " + failureNum + " 条循环级别记录。");
            }
            addMessage(redirectAttributes, "已成功导入 " + successNum + " 条循环级别记录" + failureMsg);
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入循环级别失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/wms/basicdata/banQinCdWhCycle/?repage";
    }

    /**
     * 下载导入循环级别数据模板
     */
    @RequiresPermissions("basicdata:banQinCdWhCycle:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            String fileName = "循环级别数据导入模板.xlsx";
            List<BanQinCdWhCycle> list = Lists.newArrayList();
            new ExportExcel("循环级别数据", BanQinCdWhCycle.class, 1).setDataList(list).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/wms/basicdata/banQinCdWhCycle/?repage";
    }

}