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
import com.yunyou.modules.wms.inventory.entity.BanQinWmAdDetail;
import com.yunyou.modules.wms.inventory.entity.BanQinWmAdDetailEntity;
import com.yunyou.modules.wms.inventory.service.BanQinWmAdDetailService;
import com.yunyou.modules.wms.inventory.service.BanQinWmAdHeaderService;
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
 * 调整单明细Controller
 *
 * @author WMJ
 * @version 2019-01-28
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/inventory/banQinWmAdDetail")
public class BanQinWmAdDetailController extends BaseController {
    @Autowired
    private BanQinWmAdDetailService banQinWmAdDetailService;
    @Autowired
    private BanQinWmAdHeaderService banQinWmAdHeaderService;

    /**
     * 调整单明细列表数据
     */
    @ResponseBody
    @RequestMapping(value = "data")
    public Map<String, Object> data(BanQinWmAdDetailEntity banQinWmAdDetailEntity, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<BanQinWmAdDetailEntity> page = banQinWmAdDetailService.findPage(new Page<BanQinWmAdDetailEntity>(request, response), banQinWmAdDetailEntity);
        return getBootstrapData(page);
    }

    /**
     * 保存调整单明细
     */
    @ResponseBody
    @RequiresPermissions("inventory:banQinWmAdDetail:saveDetail")
    @RequestMapping(value = "save")
    public AjaxJson save(@RequestBody BanQinWmAdDetailEntity banQinWmAdDetailEntity, Model model, RedirectAttributes redirectAttributes) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, banQinWmAdDetailEntity)) {
            j.setSuccess(false);
            j.setMsg("非法数据！");
            return j;
        }
        try {
            ResultMessage message = banQinWmAdDetailService.saveEntity(banQinWmAdDetailEntity);
            j.setSuccess(message.isSuccess());
            j.setMsg(message.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 批量删除调整单明细
     */
    @ResponseBody
    @RequiresPermissions("inventory:banQinWmAdDetail:removeDetail")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(BanQinWmAdDetailEntity banQinWmAdDetailEntity, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinWmAdHeaderService.removeDetail(banQinWmAdDetailEntity.getHeaderId(), banQinWmAdDetailEntity.getId());
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
    @RequiresPermissions("inventory:banQinWmAdDetail:export")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public AjaxJson exportFile(BanQinWmAdDetail banQinWmAdDetail, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        try {
            String fileName = "调整单明细" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            Page<BanQinWmAdDetail> page = banQinWmAdDetailService.findPage(new Page<BanQinWmAdDetail>(request, response, -1), banQinWmAdDetail);
            new ExportExcel("调整单明细", BanQinWmAdDetail.class).setDataList(page.getList()).write(response, fileName).dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出调整单明细记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 导入Excel数据
     */
    @RequiresPermissions("inventory:banQinWmAdDetail:import")
    @RequestMapping(value = "import", method = RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            int successNum = 0;
            int failureNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<BanQinWmAdDetail> list = ei.getDataList(BanQinWmAdDetail.class);
            for (BanQinWmAdDetail banQinWmAdDetail : list) {
                try {
                    banQinWmAdDetailService.save(banQinWmAdDetail);
                    successNum++;
                } catch (ConstraintViolationException ex) {
                    failureNum++;
                } catch (Exception ex) {
                    failureNum++;
                }
            }
            if (failureNum > 0) {
                failureMsg.insert(0, "，失败 " + failureNum + " 条调整单明细记录。");
            }
            addMessage(redirectAttributes, "已成功导入 " + successNum + " 条调整单明细记录" + failureMsg);
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入调整单明细失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/wms/inventory/banQinWmAdDetail/?repage";
    }

    /**
     * 下载导入调整单明细数据模板
     */
    @RequiresPermissions("inventory:banQinWmAdDetail:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            String fileName = "调整单明细数据导入模板.xlsx";
            List<BanQinWmAdDetail> list = Lists.newArrayList();
            new ExportExcel("调整单明细数据", BanQinWmAdDetail.class, 1).setDataList(list).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/wms/inventory/banQinWmAdDetail/?repage";
    }

    /**
     * 明细执行调整
     */
    @ResponseBody
    @RequiresPermissions("inventory:banQinWmAdDetail:adjustDetail")
    @RequestMapping(value = "adjustDetail")
    public AjaxJson adjustDetail(String ids, String headerId) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage message = banQinWmAdHeaderService.adjustDetail(headerId, ids);
            j.setSuccess(message.isSuccess());
            j.setMsg(message.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 明细取消
     */
    @ResponseBody
    @RequiresPermissions("inventory:banQinWmAdDetail:cancelDetail")
    @RequestMapping(value = "cancelDetail")
    public AjaxJson cancelDetail(String ids, String headerId) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage message = banQinWmAdHeaderService.cancelDetail(headerId, ids);
            j.setSuccess(message.isSuccess());
            j.setMsg(message.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("取消调整异常" + e.getMessage());
        }
        return j;
    }

}