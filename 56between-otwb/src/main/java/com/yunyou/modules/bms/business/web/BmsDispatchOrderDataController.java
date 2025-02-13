package com.yunyou.modules.bms.business.web;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.config.Global;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.common.utils.excel.ImportExcel;
import com.yunyou.common.utils.time.DateUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.bms.business.entity.BmsDispatchOrderData;
import com.yunyou.modules.bms.business.entity.BmsDispatchOrderSiteData;
import com.yunyou.modules.bms.business.entity.excel.BmsDispatchOrderDataExcel;
import com.yunyou.modules.bms.business.entity.extend.BmsDispatchOrderDataEntity;
import com.yunyou.modules.bms.business.service.BmsDispatchOrderDataService;
import com.yunyou.modules.bms.interactive.BmsPullDataCondition;
import com.yunyou.modules.bms.interactive.service.BmsPullDataHandle;
import com.yunyou.modules.sys.entity.Area;
import com.yunyou.modules.sys.service.AreaService;
import com.google.common.collect.Lists;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "${adminPath}/bms/business/bmsDispatchOrderData")
public class BmsDispatchOrderDataController extends BaseController {
    @Autowired
    private BmsDispatchOrderDataService bmsDispatchOrderDataService;
    @Autowired
    private BmsPullDataHandle bmsPullDataHandle;
    @Autowired
    private AreaService areaService;

    /**
     * 派车单数据列表页面
     */
    @RequiresPermissions("business:bmsDispatchOrderData:list")
    @RequestMapping(value = {"list", ""})
    public String list(Model model) {
        model.addAttribute("bmsDispatchOrderDataEntity", new BmsDispatchOrderDataEntity());
        return "modules/bms/business/bmsDispatchOrderDataList";
    }

    /**
     * 派车单数据列表数据
     */
    @ResponseBody
    @RequiresPermissions("business:bmsDispatchOrderData:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(BmsDispatchOrderDataEntity entity, HttpServletRequest request, HttpServletResponse response) {
        if (entity.getOrderDateFm() != null) {
            entity.setOrderDateFm(DateUtil.beginOfDate(entity.getOrderDateFm()));
        }
        if (entity.getOrderDateTo() != null) {
            entity.setOrderDateTo(DateUtil.endOfDate(entity.getOrderDateTo()));
        }
        if (entity.getDispatchDateFm() != null) {
            entity.setDispatchDateFm(DateUtil.beginOfDate(entity.getDispatchDateFm()));
        }
        if (entity.getDispatchDateTo() != null) {
            entity.setDispatchDateTo(DateUtil.endOfDate(entity.getDispatchDateTo()));
        }
        return getBootstrapData(bmsDispatchOrderDataService.findPage(new Page<>(request, response), entity));
    }

    /**
     * 派车单数据列表页面
     */
    @RequiresPermissions("business:bmsDispatchOrderData:list")
    @RequestMapping(value = "form")
    public String form(BmsDispatchOrderDataEntity entity, Model model) {
        if (StringUtils.isNotBlank(entity.getId())) {
            entity = bmsDispatchOrderDataService.getEntity(entity.getId());
        }
        if (entity == null) {
            entity = new BmsDispatchOrderDataEntity();
        }
        model.addAttribute("bmsDispatchOrderDataEntity", entity);
        return "modules/bms/business/bmsDispatchOrderDataForm";
    }

    /**
     * 批量删除派车单数据
     */
    @ResponseBody
    @RequiresPermissions("business:bmsDispatchOrderData:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            bmsDispatchOrderDataService.delete(bmsDispatchOrderDataService.get(id));
        }
        j.setMsg("删除派车单数据成功");
        return j;
    }

    /**
     * 描述：同步数据
     */
    @ResponseBody
    @RequestMapping("pullData")
    public AjaxJson pullData(@RequestBody BmsPullDataCondition condition) {
        AjaxJson j = new AjaxJson();
        condition.setDateType(BmsPullDataCondition.DATA_TYPE_DISPATCH_ORDER);
        condition.setFmDate(DateUtil.beginOfDate(condition.getFmDate()));
        condition.setToDate(DateUtil.endOfDate(condition.getToDate()));
        ResultMessage message = bmsPullDataHandle.dataHandle(condition);
        if (!message.isSuccess()) {
            j.setSuccess(false);
            j.setMsg(message.getMessage());
        }
        return j;
    }

    /**
     * 描述：不计费/加入计费
     */
    @ResponseBody
    @RequiresPermissions(value = {"business:bmsDispatchOrderData:cancelFee", "business:bmsDispatchOrderData:addFee"}, logical = Logical.OR)
    @RequestMapping("isFee")
    public AjaxJson isFee(@RequestParam String ids, @RequestParam String isFee) {
        AjaxJson j = new AjaxJson();
        if (StringUtils.isBlank(ids)) {
            j.setSuccess(false);
            j.setMsg("请选择记录");
            return j;
        }
        try {
            String[] idArr = ids.split(",");
            for (String id : idArr) {
                BmsDispatchOrderData bmsDispatchOrderData = bmsDispatchOrderDataService.get(id);
                bmsDispatchOrderData.setIsFee(isFee);
                bmsDispatchOrderDataService.save(bmsDispatchOrderData);
            }
        } catch (Exception e) {
            logger.error("加入/取消计费异常", e);
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    @ResponseBody
    @RequestMapping("siteData")
    public Map<String, Object> siteData(BmsDispatchOrderSiteData bmsDispatchOrderSiteData, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(bmsDispatchOrderDataService.findSitePage(new Page<>(request, response), bmsDispatchOrderSiteData));
    }

    /**
     * 导出excel文件
     */
    @ResponseBody
    @RequiresPermissions("business:bmsDispatchOrderData:export")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public AjaxJson exportFile(@RequestBody BmsDispatchOrderDataEntity entity, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            if (entity.getOrderDateFm() != null) {
                entity.setOrderDateFm(DateUtil.beginOfDate(entity.getOrderDateFm()));
            }
            if (entity.getOrderDateTo() != null) {
                entity.setOrderDateTo(DateUtil.endOfDate(entity.getOrderDateTo()));
            }
            if (entity.getDispatchDateFm() != null) {
                entity.setDispatchDateFm(DateUtil.beginOfDate(entity.getDispatchDateFm()));
            }
            if (entity.getDispatchDateTo() != null) {
                entity.setDispatchDateTo(DateUtil.endOfDate(entity.getDispatchDateTo()));
            }
            List<BmsDispatchOrderDataExcel> list = bmsDispatchOrderDataService.findPage(new Page<>(request, response, -1), entity).getList()
                    .stream().map(o -> bmsDispatchOrderDataService.findSiteData(o.getId(), o.getOrgId()).stream().map(e -> {
                        BmsDispatchOrderDataExcel export = new BmsDispatchOrderDataExcel();
                        BeanUtils.copyProperties(o, export);
                        export.setDispatchSeq(e.getDispatchSeq());
                        export.setOutletCode(e.getOutletCode());
                        export.setOutletName(e.getOutletName());
                        Area area = areaService.get(e.getAreaId());
                        if (area != null) {
                            export.setAreaCode(area.getCode());
                        }
                        return export;
                    }).collect(Collectors.toList())).collect(ArrayList::new, ArrayList::addAll, ArrayList::addAll);
            new ExportExcel(null, BmsDispatchOrderDataExcel.class).setDataList(list).write(response, "派车单数据.xlsx").dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 导入Excel数据
     */
    @RequiresPermissions("business:bmsDispatchOrderData:import")
    @RequestMapping(value = "import", method = RequestMethod.POST)
    public String importFile(MultipartFile file, String orgId, RedirectAttributes redirectAttributes) {
        try {
            bmsDispatchOrderDataService.importFile(new ImportExcel(file, 1, 0).getDataList(BmsDispatchOrderDataExcel.class), orgId);
            addMessage(redirectAttributes, "导入成功");
        } catch (Exception e) {
            logger.error("导入失败", e);
            addMessage(redirectAttributes, "导入失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/bms/business/bmsDispatchOrderData/?repage";
    }

    /**
     * 下载导入数据模板
     */
    @RequiresPermissions("business:bmsDispatchOrderData:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            new ExportExcel(null, BmsDispatchOrderDataExcel.class, 1).setDataList(Lists.newArrayList()).write(response, "派车单数据模板.xlsx").dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/bms/business/bmsDispatchOrderData/?repage";
    }
}
