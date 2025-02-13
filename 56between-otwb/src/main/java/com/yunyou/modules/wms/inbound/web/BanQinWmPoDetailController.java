package com.yunyou.modules.wms.inbound.web;

import com.google.common.collect.Lists;
import com.yunyou.common.ResultMessage;
import com.yunyou.common.config.Global;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.time.DateUtils;
import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.common.utils.excel.ImportExcel;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.wms.inbound.entity.BanQinWmPoDetail;
import com.yunyou.modules.wms.inbound.entity.BanQinWmPoDetailEntity;
import com.yunyou.modules.wms.inbound.service.BanQinInboundPoService;
import com.yunyou.modules.wms.inbound.service.BanQinWmPoDetailService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
 * 采购单明细Controller
 *
 * @author WMJ
 * @version 2019-01-26
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/inbound/banQinWmPoDetail")
public class BanQinWmPoDetailController extends BaseController {
    @Autowired
    private BanQinWmPoDetailService banQinWmPoDetailService;
    @Autowired
    private BanQinInboundPoService inboundPoService;

    /**
     * 采购单明细列表数据
     */
    @ResponseBody
    @RequestMapping(value = "data")
    public Map<String, Object> data(BanQinWmPoDetailEntity banQinWmPoDetailEntity, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<BanQinWmPoDetailEntity> page = banQinWmPoDetailService.findPage(new Page<BanQinWmPoDetailEntity>(request, response), banQinWmPoDetailEntity);
        return getBootstrapData(page);
    }

    /**
     * 保存采购单明细
     */
    @RequestMapping(value = "save")
    @ResponseBody
    public AjaxJson save(BanQinWmPoDetailEntity banQinWmPoDetailEntity, Model model, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, banQinWmPoDetailEntity)) {
            j.setSuccess(false);
            j.setMsg("非法数据");
            return j;
        }
        try {
            ResultMessage msg = inboundPoService.savePoDetailEntity(banQinWmPoDetailEntity);
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 批量删除采购单明细
     */
    @ResponseBody
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(BanQinWmPoDetailEntity banQinWmPoDetailEntity, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        ResultMessage msg = inboundPoService.removePoDetailEntity(banQinWmPoDetailEntity.getPoNo(), banQinWmPoDetailEntity.getLineNo().split(","), banQinWmPoDetailEntity.getOrgId());
        j.setSuccess(msg.isSuccess());
        j.setMsg(msg.getMessage());
        return j;
    }

    /**
     * 导出excel文件
     */
    @ResponseBody
    @RequiresPermissions("inbound:banQinWmPoDetail:export")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public AjaxJson exportFile(BanQinWmPoDetail banQinWmPoDetail, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        try {
            String fileName = "采购单明细" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            Page<BanQinWmPoDetail> page = banQinWmPoDetailService.findPage(new Page<BanQinWmPoDetail>(request, response, -1), banQinWmPoDetail);
            new ExportExcel("采购单明细", BanQinWmPoDetail.class).setDataList(page.getList()).write(response, fileName).dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出采购单明细记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 导入Excel数据
     */
    @RequiresPermissions("inbound:banQinWmPoDetail:import")
    @RequestMapping(value = "import", method = RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            int successNum = 0;
            int failureNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<BanQinWmPoDetail> list = ei.getDataList(BanQinWmPoDetail.class);
            for (BanQinWmPoDetail banQinWmPoDetail : list) {
                try {
                    banQinWmPoDetailService.save(banQinWmPoDetail);
                    successNum++;
                } catch (ConstraintViolationException ex) {
                    failureNum++;
                } catch (Exception ex) {
                    failureNum++;
                }
            }
            if (failureNum > 0) {
                failureMsg.insert(0, "，失败 " + failureNum + " 条采购单明细记录。");
            }
            addMessage(redirectAttributes, "已成功导入 " + successNum + " 条采购单明细记录" + failureMsg);
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入采购单明细失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/inbound/banQinWmPoDetail/?repage";
    }

    /**
     * 下载导入采购单明细数据模板
     */
    @RequiresPermissions("inbound:banQinWmPoDetail:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            String fileName = "采购单明细数据导入模板.xlsx";
            List<BanQinWmPoDetail> list = Lists.newArrayList();
            new ExportExcel("采购单明细数据", BanQinWmPoDetail.class, 1).setDataList(list).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/inbound/banQinWmPoDetail/?repage";
    }

    /**
     * 取消订单行
     */
    @ResponseBody
    @RequestMapping(value = "cancel")
    public AjaxJson cancel(BanQinWmPoDetailEntity banQinWmPoDetailEntity, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        ResultMessage msg = inboundPoService.cancelPoDetail(banQinWmPoDetailEntity.getPoNo(), banQinWmPoDetailEntity.getLineNo().split(","), banQinWmPoDetailEntity.getOrgId());
        j.setSuccess(msg.isSuccess());
        j.setMsg(msg.getMessage());
        return j;
    }

}