package com.yunyou.modules.wms.outbound.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.yunyou.common.utils.time.DateUtils;
import com.yunyou.common.config.Global;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.common.utils.excel.ImportExcel;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoPrealloc;
import com.yunyou.modules.wms.outbound.service.BanQinWmSoPreallocService;

/**
 * 预配明细Controller
 *
 * @author WMJ
 * @version 2019-02-14
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/outbound/banQinWmSoPrealloc")
public class BanQinWmSoPreallocController extends BaseController {

    @Autowired
    private BanQinWmSoPreallocService banQinWmSoPreallocService;

    /**
     * 预配明细列表数据
     */
    @ResponseBody
    @RequestMapping(value = "data")
    public Map<String, Object> data(BanQinWmSoPrealloc banQinWmSoPrealloc, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<BanQinWmSoPrealloc> page = banQinWmSoPreallocService.findPage(new Page<BanQinWmSoPrealloc>(request, response), banQinWmSoPrealloc);
        return getBootstrapData(page);
    }

    /**
     * 保存预配明细
     */
    @RequestMapping(value = "save")
    public AjaxJson save(BanQinWmSoPrealloc banQinWmSoPrealloc, Model model, RedirectAttributes redirectAttributes) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, banQinWmSoPrealloc)) {
            j.setSuccess(false);
            j.setMsg("非法数据");
        }
        banQinWmSoPreallocService.save(banQinWmSoPrealloc);
        j.setMsg("保存成功");
        return j;
    }

    /**
     * 批量删除预配明细
     */
    @ResponseBody
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        String idArray[] = ids.split(",");
        for (String id : idArray) {
            banQinWmSoPreallocService.delete(banQinWmSoPreallocService.get(id));
        }
        j.setMsg("删除预配明细成功");
        return j;
    }

    /**
     * 导出excel文件
     */
    @ResponseBody
    @RequiresPermissions("outbound:banQinWmSoPrealloc:export")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public AjaxJson exportFile(BanQinWmSoPrealloc banQinWmSoPrealloc, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        try {
            String fileName = "预配明细" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            Page<BanQinWmSoPrealloc> page = banQinWmSoPreallocService.findPage(new Page<BanQinWmSoPrealloc>(request, response, -1), banQinWmSoPrealloc);
            new ExportExcel("预配明细", BanQinWmSoPrealloc.class).setDataList(page.getList()).write(response, fileName).dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出预配明细记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 导入Excel数据
     */
    @RequiresPermissions("outbound:banQinWmSoPrealloc:import")
    @RequestMapping(value = "import", method = RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            int successNum = 0;
            int failureNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<BanQinWmSoPrealloc> list = ei.getDataList(BanQinWmSoPrealloc.class);
            for (BanQinWmSoPrealloc banQinWmSoPrealloc : list) {
                try {
                    banQinWmSoPreallocService.save(banQinWmSoPrealloc);
                    successNum++;
                } catch (ConstraintViolationException ex) {
                    failureNum++;
                } catch (Exception ex) {
                    failureNum++;
                }
            }
            if (failureNum > 0) {
                failureMsg.insert(0, "，失败 " + failureNum + " 条预配明细记录。");
            }
            addMessage(redirectAttributes, "已成功导入 " + successNum + " 条预配明细记录" + failureMsg);
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入预配明细失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/wms/outbound/banQinWmSoPrealloc/?repage";
    }

    /**
     * 下载导入预配明细数据模板
     */
    @RequiresPermissions("outbound:banQinWmSoPrealloc:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            String fileName = "预配明细数据导入模板.xlsx";
            List<BanQinWmSoPrealloc> list = Lists.newArrayList();
            new ExportExcel("预配明细数据", BanQinWmSoPrealloc.class, 1).setDataList(list).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/wms/outbound/banQinWmSoPrealloc/?repage";
    }

}