package com.yunyou.modules.wms.outbound.web;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.wms.outbound.entity.BanQinDispatchPkRuleEntity;
import com.yunyou.modules.wms.outbound.entity.BanQinWmWvHeader;
import com.yunyou.modules.wms.outbound.entity.BanQinWmWvHeaderEntity;
import com.yunyou.modules.wms.outbound.service.BanQinOutboundWaveService;
import com.yunyou.modules.wms.outbound.service.BanQinWmWvHeaderService;
import com.yunyou.modules.wms.report.entity.PackingListLabel;
import com.yunyou.modules.wms.report.entity.WaveCombinePickingLabel;
import com.yunyou.modules.wms.report.entity.WaveSortingLabel;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 波次单Controller
 *
 * @author WMJ
 * @version 2019-02-14
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/outbound/banQinWmWvHeader")
public class BanQinWmWvHeaderController extends BaseController {
    @Autowired
    private BanQinWmWvHeaderService banQinWmWvHeaderService;
    @Autowired
    private BanQinOutboundWaveService banQinOutboundWaveService;

    @ModelAttribute
    public BanQinWmWvHeaderEntity get(@RequestParam(required = false) String id) {
        BanQinWmWvHeaderEntity entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = banQinWmWvHeaderService.getEntity(id);
        }
        if (entity == null) {
            entity = new BanQinWmWvHeaderEntity();
        }
        return entity;
    }

    /**
     * 波次单列表页面
     */
    @RequiresPermissions("outbound:banQinWmWvHeader:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/wms/outbound/banQinWmWvHeaderList";
    }

    /**
     * 波次单列表数据
     */
    @ResponseBody
    @RequiresPermissions("outbound:banQinWmWvHeader:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(BanQinWmWvHeaderEntity entity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(banQinWmWvHeaderService.findPage(new Page<BanQinWmWvHeaderEntity>(request, response), entity));
    }

    /**
     * 查看，增加，编辑波次单表单页面
     */
    @RequiresPermissions(value = {"outbound:banQinWmWvHeader:view", "outbound:banQinWmWvHeader:add", "outbound:banQinWmWvHeader:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(BanQinWmWvHeader banQinWmWvHeader, Model model) {
        model.addAttribute("banQinWmWvHeader", banQinWmWvHeader);
        if (StringUtils.isBlank(banQinWmWvHeader.getId())) {//如果ID是空为添加
            model.addAttribute("isAdd", true);
        }
        return "modules/wms/outbound/banQinWmWvHeaderForm";
    }

    /**
     * 保存波次单
     */
    @RequiresPermissions(value = {"outbound:banQinWmWvHeader:add", "outbound:banQinWmWvHeader:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    @ResponseBody
    public AjaxJson save(BanQinWmWvHeaderEntity entity, Model model) {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, entity)) {
            j.setSuccess(false);
            j.setMsg("非法数据");
            return j;
        }
        try {
            ResultMessage msg = banQinOutboundWaveService.saveWvEntity(entity);
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
            BanQinWmWvHeader wmWvHeader = (BanQinWmWvHeader) msg.getData();

            LinkedHashMap<String, Object> map = new LinkedHashMap<>();
            map.put("entity", wmWvHeader.getId());
            j.setBody(map);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 批量删除波次单
     */
    @ResponseBody
    @RequiresPermissions("outbound:banQinWmWvHeader:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(BanQinWmWvHeader banQinWmWvHeader) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinOutboundWaveService.removeWvEntity(banQinWmWvHeader.getWaveNo().split(","), banQinWmWvHeader.getOrgId());
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
    @RequiresPermissions("outbound:banQinWmWvHeader:export")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public AjaxJson exportFile(BanQinWmWvHeader banQinWmWvHeader, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            Page<BanQinWmWvHeader> page = banQinWmWvHeaderService.findPage(new Page<>(request, response, -1), banQinWmWvHeader);
            new ExportExcel("波次单", BanQinWmWvHeader.class).setDataList(page.getList()).write(response, "波次单.xlsx").dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出波次单记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 分配
     */
    @ResponseBody
    @RequiresPermissions("outbound:banQinWmWvHeader:alloc")
    @RequestMapping(value = "alloc")
    public AjaxJson alloc(BanQinWmWvHeader banQinWmWvHeader) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinOutboundWaveService.allocByWave("BY_WAVE", Arrays.asList(banQinWmWvHeader.getWaveNo().split(",")), banQinWmWvHeader.getOrgId());
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 分派拣货
     */
    @ResponseBody
    @RequiresPermissions("outbound:banQinWmWvHeader:dispathPk")
    @RequestMapping(value = "dispathPk")
    public AjaxJson dispathPk(BanQinDispatchPkRuleEntity entity) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinOutboundWaveService.dispatchPicking(Arrays.asList(entity.getWaveNos().split(",")), entity, entity.getOrgId());
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 拣货确认
     */
    @ResponseBody
    @RequiresPermissions("outbound:banQinWmWvHeader:picking")
    @RequestMapping(value = "picking")
    public AjaxJson picking(BanQinWmWvHeader banQinWmWvHeader) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinOutboundWaveService.pickingByWave("BY_WAVE", Arrays.asList(banQinWmWvHeader.getWaveNo().split(",")), banQinWmWvHeader.getOrgId());
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 发货确认
     */
    @ResponseBody
    @RequiresPermissions("outbound:banQinWmWvHeader:shipment")
    @RequestMapping(value = "shipment")
    public AjaxJson shipment(BanQinWmWvHeader banQinWmWvHeader) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinOutboundWaveService.shipmentByWave("BY_WAVE", Arrays.asList(banQinWmWvHeader.getWaveNo().split(",")), banQinWmWvHeader.getOrgId());
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 取消分配
     */
    @ResponseBody
    @RequiresPermissions("outbound:banQinWmWvHeader:cancelAlloc")
    @RequestMapping(value = "cancelAlloc")
    public AjaxJson cancelAlloc(BanQinWmWvHeader banQinWmWvHeader) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinOutboundWaveService.cancelAllocByWave("BY_WAVE", Arrays.asList(banQinWmWvHeader.getWaveNo().split(",")), banQinWmWvHeader.getOrgId());
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 取消拣货
     */
    @ResponseBody
    @RequiresPermissions("outbound:banQinWmWvHeader:cancelPicking")
    @RequestMapping(value = "cancelPicking")
    public AjaxJson cancelPicking(BanQinWmWvHeader banQinWmWvHeader) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinOutboundWaveService.cancelPickingByWave("BY_WAVE", Arrays.asList(banQinWmWvHeader.getWaveNo().split(",")), banQinWmWvHeader.getOrgId());
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 取消发货
     */
    @ResponseBody
    @RequiresPermissions("outbound:banQinWmWvHeader:cancelShipment")
    @RequestMapping(value = "cancelShipment")
    public AjaxJson cancelShipment(BanQinWmWvHeader banQinWmWvHeader) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinOutboundWaveService.cancelShipmentByWave("BY_WAVE", Arrays.asList(banQinWmWvHeader.getWaveNo().split(",")), banQinWmWvHeader.getOrgId());
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 打印波次分拣单
     * @param model
     * @return
     */
    @RequestMapping(value = "/printWaveSorting")
    public String printWaveSorting(Model model, String ids) {
        List<WaveSortingLabel> result = banQinWmWvHeaderService.getWaveSorting(Arrays.asList(ids.split(",")));
        // 报表数据源
        JRDataSource jrDataSource = new JRBeanCollectionDataSource(result);
        // 动态指定报表模板url
        model.addAttribute("url", "classpath:/jasper/waveSorting.jasper");
        model.addAttribute("format", "pdf");
        model.addAttribute("jrMainDataSource", jrDataSource);

        return "iReportView";
    }

    /**
     * 打印合拣单
     * @param model
     * @return
     */
    @RequestMapping(value = "/printWaveCombinePicking")
    public String printWaveCombinePicking(Model model, String ids) {
        List<WaveCombinePickingLabel> result = banQinWmWvHeaderService.getWaveCombinePicking(Arrays.asList(ids.split(",")));
        // 报表数据源
        JRDataSource jrDataSource = new JRBeanCollectionDataSource(result);
        // 动态指定报表模板url
        model.addAttribute("url", "classpath:/jasper/waveCombinePicking.jasper");
        model.addAttribute("format", "pdf");
        model.addAttribute("jrMainDataSource", jrDataSource);

        return "iReportView";
    }

    /**
     * 打印合拣单（横版）
     * @param model
     * @return
     */
    @RequestMapping(value = "/printWaveCombinePickingLandscape")
    public String printWaveCombinePickingLandscape(Model model, String ids) {
        List<WaveCombinePickingLabel> result = banQinWmWvHeaderService.getWaveCombinePicking(Arrays.asList(ids.split(",")));
        // 报表数据源
        JRDataSource jrDataSource = new JRBeanCollectionDataSource(result);
        // 动态指定报表模板url
        model.addAttribute("url", "classpath:/jasper/waveCombinePickingLandscape.jasper");
        model.addAttribute("format", "pdf");
        model.addAttribute("jrMainDataSource", jrDataSource);

        return "iReportView";
    }

    /**
     * 打印分拣清单（装箱清单）
     * @param model
     * @return
     */
    @RequestMapping(value = "/printPickingList")
    public String printPickingList(Model model, String ids) {
        List<PackingListLabel> result = banQinWmWvHeaderService.getPacingList(Arrays.asList(ids.split(",")));
        // 报表数据源
        JRDataSource jrDataSource = new JRBeanCollectionDataSource(result);
        // 动态指定报表模板url
        model.addAttribute("url", "classpath:/jasper/packingList.jasper");
        model.addAttribute("format", "pdf");
        model.addAttribute("jrMainDataSource", jrDataSource);

        return "iReportView";
    }

    /**
     * 获取面单
     */
    @ResponseBody
    @RequiresPermissions("outbound:banQinWmWvHeader:getWaybill")
    @RequestMapping(value = "getWaybill")
    public AjaxJson getWaybill(BanQinWmWvHeader banQinWmWvHeader) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinOutboundWaveService.getWaybill(Arrays.asList(banQinWmWvHeader.getWaveNo().split(",")), banQinWmWvHeader.getOrgId());
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 打印面单
     */
    @ResponseBody
    @RequiresPermissions("outbound:banQinWmWvHeader:printWaybill")
    @RequestMapping(value = "printWaybill")
    public AjaxJson printWaybill(BanQinWmWvHeader banQinWmWvHeader) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinOutboundWaveService.printWaybill(Arrays.asList(banQinWmWvHeader.getWaveNo().split(",")), banQinWmWvHeader.getOrgId());
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
            j.put("imageList", msg.getData());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 获取面单并打印
     */
    @ResponseBody
    @RequiresPermissions("outbound:banQinWmWvHeader:getAndPrintWaybill")
    @RequestMapping(value = "getAndPrintWaybill")
    public AjaxJson getAndPrintWaybill(BanQinWmWvHeader banQinWmWvHeader) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinOutboundWaveService.getAndPrintWaybill(Arrays.asList(banQinWmWvHeader.getWaveNo().split(",")), banQinWmWvHeader.getOrgId());
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
            j.put("imageList", msg.getData());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

}