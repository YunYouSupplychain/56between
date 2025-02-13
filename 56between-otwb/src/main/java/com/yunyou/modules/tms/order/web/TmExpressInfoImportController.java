package com.yunyou.modules.tms.order.web;

import com.yunyou.common.config.Global;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.common.utils.excel.ImportExcel;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.tms.common.TmsException;
import com.yunyou.modules.tms.order.entity.TmExpressInfoImport;
import com.yunyou.modules.tms.order.entity.TmExpressInfoImportDetail;
import com.yunyou.modules.tms.order.service.TmExpressInfoImportDetailService;
import com.yunyou.modules.tms.order.service.TmExpressInfoImportService;
import com.google.common.collect.Lists;
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
 * 快递单号导入更新Controller
 *
 * @author zyf
 * @version 2020-04-13
 */
@Controller
@RequestMapping(value = "${adminPath}/tms/order/tmExpressInfoImport")
public class TmExpressInfoImportController extends BaseController {
    @Autowired
    private TmExpressInfoImportService tmExpressInfoImportService;
    @Autowired
    private TmExpressInfoImportDetailService tmExpressInfoImportDetailService;

    @ModelAttribute
    public TmExpressInfoImport get(@RequestParam(required = false) String id) {
        TmExpressInfoImport entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = tmExpressInfoImportService.get(id);
        }
        if (entity == null) {
            entity = new TmExpressInfoImport();
        }
        return entity;
    }

    /**
     * 快递单号导入更新列表页面
     */
    @RequiresPermissions("tms:order:tmExpressInfoImport:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/tms/order/tmExpressInfoImportList";
    }

    /**
     * 快递单号导入更新列表数据
     */
    @ResponseBody
    @RequiresPermissions("tms:order:tmExpressInfoImport:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(TmExpressInfoImport tmExpressInfoImport, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(tmExpressInfoImportService.findPage(new Page<>(request, response), tmExpressInfoImport));
    }

    /**
     * 快递单号导入更新明细列表数据
     */
    @ResponseBody
    @RequiresPermissions("tms:order:tmExpressInfoImport:list")
    @RequestMapping(value = "detail/data")
    public Map<String, Object> detailData(TmExpressInfoImportDetail tmExpressInfoImportDetail, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(tmExpressInfoImportDetailService.findPage(new Page<>(request, response), tmExpressInfoImportDetail));
    }

    /**
     * 查看，增加，编辑快递单号导入更新表单页面
     */
    @RequiresPermissions(value = {"tms:order:tmExpressInfoImport:view", "tms:order:tmExpressInfoImport:add", "tms:order:tmExpressInfoImport:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(TmExpressInfoImport tmExpressInfoImport, Model model) {
        model.addAttribute("tmExpressInfoImport", tmExpressInfoImport);
        return "modules/tms/order/tmExpressInfoImportForm";
    }

    /**
     * 保存快递单号导入更新
     */
    @ResponseBody
    @RequiresPermissions(value = {"tms:order:tmExpressInfoImport:add", "tms:order:tmExpressInfoImport:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(TmExpressInfoImport tmExpressInfoImport, Model model) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, tmExpressInfoImport)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        tmExpressInfoImportService.save(tmExpressInfoImport);
        j.setSuccess(true);
        j.setMsg("保存快递单号导入更新成功");
        return j;
    }

    /**
     * 删除快递单号导入更新
     */
    @ResponseBody
    @RequiresPermissions("tms:order:tmExpressInfoImport:del")
    @RequestMapping(value = "delete")
    public AjaxJson delete(TmExpressInfoImport tmExpressInfoImport) {
        AjaxJson j = new AjaxJson();
        tmExpressInfoImportService.delete(tmExpressInfoImport);
        j.setMsg("删除快递单号导入更新成功");
        return j;
    }

    /**
     * 批量删除快递单号导入更新
     */
    @ResponseBody
    @RequiresPermissions("tms:order:tmExpressInfoImport:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            tmExpressInfoImportService.delete(tmExpressInfoImportService.get(id));
        }
        j.setMsg("删除快递单号导入更新成功");
        return j;
    }

    /**
     * 导出excel文件
     */
    @ResponseBody
    @RequiresPermissions("tms:order:tmExpressInfoImport:export")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public AjaxJson exportFile(TmExpressInfoImport tmExpressInfoImport, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            String fileName = "快递单号导入更新.xlsx";
            Page<TmExpressInfoImport> page = tmExpressInfoImportService.findPage(new Page<>(request, response, -1), tmExpressInfoImport);
            new ExportExcel("快递单号导入更新", TmExpressInfoImport.class).setDataList(page.getList()).write(response, fileName).dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出快递单号导入更新记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 导入Excel数据
     */
    @RequiresPermissions("tms:order:tmExpressInfoImport:import")
    @RequestMapping(value = "import", method = RequestMethod.POST)
    @ResponseBody
    public AjaxJson importFile(MultipartFile file, String importReason, String orgId) {
        AjaxJson j = new AjaxJson();
        try {
            String fileName = file.getOriginalFilename();
            ImportExcel ei = new ImportExcel(file, 1, 0);
            int lineNo = 1;
            boolean hasError = false;
            StringBuilder failureMsg = new StringBuilder();
            List<TmExpressInfoImportDetail> list = ei.getDataList(TmExpressInfoImportDetail.class);
            List<TmExpressInfoImportDetail> saveDetails = Lists.newArrayList();
            if (CollectionUtil.isNotEmpty(list)) {
                for (TmExpressInfoImportDetail detail : list) {
                    lineNo++;
                    if (StringUtils.isBlank(detail.getCustomerNo())) {
                        continue;
                    }
                    try {
                        tmExpressInfoImportService.checkImportFile(detail, orgId);
                        saveDetails.add(detail);
                    } catch (TmsException e) {
                        failureMsg.append("第").append(lineNo).append("行导入异常:").append(e.getMessage());
                        failureMsg.append(System.getProperty("line.separator"));
                        hasError = true;
                    }
                }
                if (hasError) {
                    j.setSuccess(false);
                    j.setMsg(failureMsg.toString());
                } else {
                    tmExpressInfoImportService.importFile(saveDetails, fileName, importReason, orgId);
                }
            } else {
                j.setSuccess(false);
                j.setMsg("EXCEL数据为空！");
            }
        } catch (Exception e) {
            logger.error("导入异常", e);
            j.setSuccess(false);
            j.setMsg("导入异常:" + e.getMessage());
        }
        return j;
    }

    /**
     * 下载导入快递单号导入更新数据模板
     */
    @RequiresPermissions("tms:order:tmExpressInfoImport:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            String fileName = "快递单号更新导入模板.xlsx";
            List<TmExpressInfoImportDetail> list = Lists.newArrayList();
            new ExportExcel("", TmExpressInfoImportDetail.class, 2).setDataList(list).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/tms/order/tmExpressInfoImport/?repage";
    }

}