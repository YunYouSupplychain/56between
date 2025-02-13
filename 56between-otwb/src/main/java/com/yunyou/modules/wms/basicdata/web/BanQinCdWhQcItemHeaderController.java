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
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhQcItemHeader;
import com.yunyou.modules.wms.basicdata.service.BanQinCdWhQcItemHeaderService;
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
 * 质检项Controller
 *
 * @author WMJ
 * @version 2019-01-25
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/basicdata/banQinCdWhQcItemHeader")
public class BanQinCdWhQcItemHeaderController extends BaseController {

    @Autowired
    private BanQinCdWhQcItemHeaderService banQinCdWhQcItemHeaderService;

    @ModelAttribute
    public BanQinCdWhQcItemHeader get(@RequestParam(required = false) String id) {
        BanQinCdWhQcItemHeader entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = banQinCdWhQcItemHeaderService.get(id);
        }
        if (entity == null) {
            entity = new BanQinCdWhQcItemHeader();
        }
        return entity;
    }

    /**
     * 质检项列表页面
     */
    @RequiresPermissions("basicdata:banQinCdWhQcItemHeader:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/wms/basicdata/banQinCdWhQcItemHeaderList";
    }

    /**
     * 质检项列表数据
     */
    @ResponseBody
    @RequiresPermissions("basicdata:banQinCdWhQcItemHeader:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(BanQinCdWhQcItemHeader banQinCdWhQcItemHeader, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<BanQinCdWhQcItemHeader> page = banQinCdWhQcItemHeaderService.findPage(new Page<BanQinCdWhQcItemHeader>(request, response), banQinCdWhQcItemHeader);
        return getBootstrapData(page);
    }

    /**
     * 质检项列表数据
     */
    @ResponseBody
    @RequestMapping(value = "grid")
    public Map<String, Object> grid(BanQinCdWhQcItemHeader banQinCdWhQcItemHeader, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<BanQinCdWhQcItemHeader> page = banQinCdWhQcItemHeaderService.findPage(new Page<BanQinCdWhQcItemHeader>(request, response), banQinCdWhQcItemHeader);
        return getBootstrapData(page);
    }

    /**
     * 查看，增加，编辑质检项表单页面
     */
    @RequiresPermissions(value = {"basicdata:banQinCdWhQcItemHeader:view", "basicdata:banQinCdWhQcItemHeader:add", "basicdata:banQinCdWhQcItemHeader:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(BanQinCdWhQcItemHeader banQinCdWhQcItemHeader, Model model) {
        model.addAttribute("banQinCdWhQcItemHeader", banQinCdWhQcItemHeader);
        if (StringUtils.isBlank(banQinCdWhQcItemHeader.getId())) {//如果ID是空为添加
            model.addAttribute("isAdd", true);
        }
        return "modules/wms/basicdata/banQinCdWhQcItemHeaderForm";
    }

    /**
     * 保存质检项
     */
    @RequiresPermissions(value = {"basicdata:banQinCdWhQcItemHeader:add", "basicdata:banQinCdWhQcItemHeader:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    @ResponseBody
    public AjaxJson save(BanQinCdWhQcItemHeader banQinCdWhQcItemHeader, Model model, RedirectAttributes redirectAttributes) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, banQinCdWhQcItemHeader)) {
            j.setSuccess(false);
            j.setMsg("非法数据!");
            return j;
        }
        try {
            banQinCdWhQcItemHeaderService.save(banQinCdWhQcItemHeader);
            j.put("entity", banQinCdWhQcItemHeader);
        } catch (DuplicateKeyException e) {
            j.setSuccess(false);
            j.setMsg("质检项组编码重复!");
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }

        return j;
    }

    /**
     * 批量删除质检项
     */
    @ResponseBody
    @RequiresPermissions("basicdata:banQinCdWhQcItemHeader:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        String idArray[] = ids.split(",");
        for (String id : idArray) {
            banQinCdWhQcItemHeaderService.delete(new BanQinCdWhQcItemHeader(id));
        }
        j.setMsg("删除质检项成功");
        return j;
    }

    /**
     * 导出excel文件
     */
    @ResponseBody
    @RequiresPermissions("basicdata:banQinCdWhQcItemHeader:export")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public AjaxJson exportFile(BanQinCdWhQcItemHeader banQinCdWhQcItemHeader, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        try {
            String fileName = "质检项" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            Page<BanQinCdWhQcItemHeader> page = banQinCdWhQcItemHeaderService.findPage(new Page<BanQinCdWhQcItemHeader>(request, response, -1), banQinCdWhQcItemHeader);
            new ExportExcel("质检项", BanQinCdWhQcItemHeader.class).setDataList(page.getList()).write(response, fileName).dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出质检项记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 导入Excel数据
     */
    @RequiresPermissions("basicdata:banQinCdWhQcItemHeader:import")
    @RequestMapping(value = "import", method = RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            int successNum = 0;
            int failureNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<BanQinCdWhQcItemHeader> list = ei.getDataList(BanQinCdWhQcItemHeader.class);
            for (BanQinCdWhQcItemHeader banQinCdWhQcItemHeader : list) {
                try {
                    banQinCdWhQcItemHeaderService.save(banQinCdWhQcItemHeader);
                    successNum++;
                } catch (ConstraintViolationException ex) {
                    failureNum++;
                } catch (Exception ex) {
                    failureNum++;
                }
            }
            if (failureNum > 0) {
                failureMsg.insert(0, "，失败 " + failureNum + " 条质检项记录。");
            }
            addMessage(redirectAttributes, "已成功导入 " + successNum + " 条质检项记录" + failureMsg);
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入质检项失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/wms/basicdata/banQinCdWhQcItemHeader/?repage";
    }

    /**
     * 下载导入质检项数据模板
     */
    @RequiresPermissions("basicdata:banQinCdWhQcItemHeader:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            String fileName = "质检项数据导入模板.xlsx";
            List<BanQinCdWhQcItemHeader> list = Lists.newArrayList();
            new ExportExcel("质检项数据", BanQinCdWhQcItemHeader.class, 1).setDataList(list).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/wms/basicdata/banQinCdWhQcItemHeader/?repage";
    }

}