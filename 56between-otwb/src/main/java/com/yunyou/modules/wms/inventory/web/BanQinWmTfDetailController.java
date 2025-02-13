package com.yunyou.modules.wms.inventory.web;

import com.google.common.collect.Lists;
import com.yunyou.common.ResultMessage;
import com.yunyou.common.config.Global;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.time.DateUtils;
import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.common.utils.excel.ImportExcel;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.wms.inventory.entity.BanQinWmTfDetail;
import com.yunyou.modules.wms.inventory.entity.BanQinWmTfDetailEntity;
import com.yunyou.modules.wms.inventory.service.BanQinWmTfDetailService;
import com.yunyou.modules.wms.inventory.service.BanQinWmTfHeaderService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Map;

/**
 * 转移单明细Controller
 *
 * @author WMJ
 * @version 2019-01-28
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/inventory/banQinWmTfDetail")
public class BanQinWmTfDetailController extends BaseController {
    @Autowired
    private BanQinWmTfDetailService banQinWmTfDetailService;
    @Autowired
    private BanQinWmTfHeaderService banQinWmTfHeaderService;

    /**
     * 转移单明细列表数据
     */
    @ResponseBody
    @RequestMapping(value = "data")
    public Map<String, Object> data(BanQinWmTfDetailEntity banQinWmTfDetailEntity, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<BanQinWmTfDetailEntity> page = banQinWmTfDetailService.findPage(new Page<BanQinWmTfDetailEntity>(request, response), banQinWmTfDetailEntity);
        return getBootstrapData(page);
    }

    /**
     * 保存转移单明细
     */
    @RequiresPermissions("inventory:banQinWmTfDetail:saveDetail")
    @RequestMapping(value = "save")
    @ResponseBody
    public AjaxJson save(@RequestBody BanQinWmTfDetailEntity banQinWmTfDetailEntity, Model model, RedirectAttributes redirectAttributes) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, banQinWmTfDetailEntity)) {
            j.setSuccess(false);
            j.setMsg("非法数据");
            return j;
        }
        try {
            ResultMessage msg = banQinWmTfDetailService.saveTfDetailEntity(banQinWmTfDetailEntity);
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 批量删除转移单明细
     */
    @ResponseBody
    @RequiresPermissions("inventory:banQinWmTfDetail:removeDetail")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids, String headerId, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinWmTfDetailService.removeTfDetailEntity(headerId, ids.split(","));
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
    @RequiresPermissions("inventory:banQinWmTfDetail:export")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public AjaxJson exportFile(BanQinWmTfDetail banQinWmTfDetail, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        try {
            String fileName = "转移单明细" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            Page<BanQinWmTfDetail> page = banQinWmTfDetailService.findPage(new Page<BanQinWmTfDetail>(request, response, -1), banQinWmTfDetail);
            new ExportExcel("转移单明细", BanQinWmTfDetail.class).setDataList(page.getList()).write(response, fileName).dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出转移单明细记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 导入Excel数据
     */
    @RequiresPermissions("inventory:banQinWmTfDetail:import")
    @RequestMapping(value = "import", method = RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            int successNum = 0;
            int failureNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<BanQinWmTfDetail> list = ei.getDataList(BanQinWmTfDetail.class);
            for (BanQinWmTfDetail banQinWmTfDetail : list) {
                try {
                    banQinWmTfDetailService.save(banQinWmTfDetail);
                    successNum++;
                } catch (ConstraintViolationException ex) {
                    failureNum++;
                } catch (Exception ex) {
                    failureNum++;
                }
            }
            if (failureNum > 0) {
                failureMsg.insert(0, "，失败 " + failureNum + " 条转移单明细记录。");
            }
            addMessage(redirectAttributes, "已成功导入 " + successNum + " 条转移单明细记录" + failureMsg);
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入转移单明细失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/wms/inventory/banQinWmTfDetail/?repage";
    }

    /**
     * 下载导入转移单明细数据模板
     */
    @RequiresPermissions("inventory:banQinWmTfDetail:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            String fileName = "转移单明细数据导入模板.xlsx";
            List<BanQinWmTfDetail> list = Lists.newArrayList();
            new ExportExcel("转移单明细数据", BanQinWmTfDetail.class, 1).setDataList(list).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/wms/inventory/banQinWmTfDetail/?repage";
    }

    /**
     * 转移明细
     */
    @ResponseBody
    @RequiresPermissions("inventory:banQinWmTfDetail:transferDetail")
    @RequestMapping(value = "transferDetail")
    public AjaxJson transferDetail(String ids, String headerId) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinWmTfHeaderService.transferDetail(headerId, ids);
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 取消订单明细
     */
    @ResponseBody
    @RequiresPermissions("inventory:banQinWmTfDetail:cancelDetail")
    @RequestMapping(value = "cancelDetail")
    public AjaxJson cancelDetail(String ids, String headerId) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinWmTfHeaderService.cancelDetail(headerId, ids);
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

}