package com.yunyou.modules.wms.inventory.web;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.common.utils.excel.ImportExcel;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.wms.inventory.entity.BanQinWmInvQuery;
import com.yunyou.modules.wms.inventory.entity.extend.BanQinWmInvExportEntity;
import com.yunyou.modules.wms.inventory.entity.extend.BanQinWmInvImportEntity;
import com.yunyou.modules.wms.inventory.service.BanQinWmInvQueryService;
import com.yunyou.modules.wms.report.entity.TraceLabel;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 库存查询Controller
 *
 * @author WMJ
 * @version 2019-01-25
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/inventory/banQinWmInvQuery")
public class BanQinWmInvQueryController extends BaseController {
    @Autowired
    private BanQinWmInvQueryService wmInvQueryService;

    @ModelAttribute
    public BanQinWmInvQuery get(@RequestParam(required = false) String id) {
        return new BanQinWmInvQuery();
    }

    /**
     * 库存查询列表页面
     */
    @RequiresPermissions("inventory:banQinWmInvQuery:list")
    @RequestMapping(value = {"list", ""})
    public String list(BanQinWmInvQuery wmInvQuery, Model model) {
        model.addAttribute("banQinWmInvQuery", wmInvQuery);
        return "modules/wms/inventory/banQinWmInvQuery";
    }

    /**
     * 库存查询页面数据源（按货主查询）
     */
    @ResponseBody
    @RequestMapping(value = "byOwner")
    public Map<String, Object> byOwner(BanQinWmInvQuery wmInvQuery, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(wmInvQueryService.findPageByOwner(new Page<>(request, response), wmInvQuery));
    }

    /**
     * 库存查询页面数据源（按商品查询）
     */
    @ResponseBody
    @RequestMapping(value = "bySku")
    public Map<String, Object> bySku(BanQinWmInvQuery wmInvQuery, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(wmInvQueryService.findPageBySku(new Page<>(request, response), wmInvQuery));
    }

    /**
     * 库存查询页面数据源（按批次查询）
     */
    @ResponseBody
    @RequestMapping(value = "byLot")
    public Map<String, Object> byLot(BanQinWmInvQuery wmInvQuery, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(wmInvQueryService.findPageByLot(new Page<>(request, response), wmInvQuery));
    }

    /**
     * 库存查询页面数据源（按库位查询）
     */
    @ResponseBody
    @RequestMapping(value = "byLoc")
    public Map<String, Object> byLoc(BanQinWmInvQuery wmInvQuery, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(wmInvQueryService.findPageByLoc(new Page<>(request, response), wmInvQuery));
    }

    /**
     * 库存查询页面数据源（按商品/库位查询）
     */
    @ResponseBody
    @RequestMapping(value = "bySkuLoc")
    public Map<String, Object> data5(BanQinWmInvQuery wmInvQuery, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(wmInvQueryService.findPageBySkuAndLoc(new Page<>(request, response), wmInvQuery));
    }

    /**
     * 库存查询页面数据源（按批次/库位/跟踪号查询）
     */
    @ResponseBody
    @RequestMapping(value = "byLotLocTraceId")
    public Map<String, Object> byLotLocTraceId(BanQinWmInvQuery wmInvQuery, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(wmInvQueryService.findPageByTraceId(new Page<>(request, response), wmInvQuery));
    }

    /**
     * 导入Excel数据
     */
    @ResponseBody
    @RequiresPermissions("inventory:banQinWmInvQuery:import")
    @RequestMapping(value = "import", method = RequestMethod.POST)
    public AjaxJson importFile(MultipartFile file, String orgId) {
        AjaxJson j = new AjaxJson();
        try {
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<BanQinWmInvImportEntity> list = ei.getDataList(BanQinWmInvImportEntity.class);
            ResultMessage msg = wmInvQueryService.importInventory(list, orgId);
            if (msg.isSuccess()) {
                j.setSuccess(true);
                j.setMsg("导入成功");
            } else {
                j.setSuccess(false);
                j.setMsg(msg.getMessage());
            }
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("", e);
            }
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    @ResponseBody
    @RequiresPermissions("inventory:banQinWmInvQuery:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(BanQinWmInvExportEntity entity, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            Page<BanQinWmInvExportEntity> page = wmInvQueryService.findExportInfo(new Page<>(request, response, -1), entity);
            new ExportExcel("", BanQinWmInvExportEntity.class).setDataList(page.getList()).write(response, "库存.xlsx").dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出库存记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 打印托盘标签
     */
    @RequestMapping(value = "/printTraceLabel")
    @RequiresPermissions("inventory:banQinWmInvQuery:printTraceLabel")
    public String printTraceLabel(Model model, String ids) {
        List<TraceLabel> result = wmInvQueryService.getTraceLabel(Arrays.asList(ids.split(",")));

        model.addAttribute("url", "classpath:/jasper/traceLabel.jasper");
        model.addAttribute("format", "pdf");
        model.addAttribute("jrMainDataSource", new JRBeanCollectionDataSource(result));
        return "iReportView";
    }

    /**
     * 打印托盘标签（二维码）
     */
    @RequestMapping(value = "/printTraceLabelQrCode")
    @RequiresPermissions("inventory:banQinWmInvQuery:printTraceLabelQrCode")
    public String printTraceLabelQrCode(Model model, String ids) {
        List<TraceLabel> result = wmInvQueryService.getTraceLabel(Arrays.asList(ids.split(",")));

        model.addAttribute("url", "classpath:/jasper/traceLabel_qrcode.jasper");
        model.addAttribute("format", "pdf");
        model.addAttribute("jrMainDataSource", new JRBeanCollectionDataSource(result));
        return "iReportView";
    }

}