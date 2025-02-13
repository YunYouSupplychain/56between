package com.yunyou.modules.bms.finance.web;

import com.yunyou.common.config.Global;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.MapUtil;
import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.common.utils.excel.ImportExcel;
import com.yunyou.common.utils.time.DateUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.bms.common.BmsConstants;
import com.yunyou.modules.bms.common.BmsException;
import com.yunyou.modules.bms.finance.entity.BmsBill;
import com.yunyou.modules.bms.finance.entity.BmsBillDetail;
import com.yunyou.modules.bms.finance.entity.extend.BmsBillDetailEntity;
import com.yunyou.modules.bms.finance.entity.extend.BmsBillDetailImport;
import com.yunyou.modules.bms.finance.service.BmsBillDetailService;
import com.yunyou.modules.bms.finance.service.BmsBillService;
import com.yunyou.modules.bms.finance.service.BmsBillStatisticsService;
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
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 费用明细Controller
 *
 * @author Jianhua Liu
 * @version 2019-06-19
 */
@Controller
@RequestMapping(value = "${adminPath}/bms/finance/bmsBillDetail")
public class BmsBillDetailController extends BaseController {
    @Autowired
    private BmsBillDetailService bmsBillDetailService;
    @Autowired
    private BmsBillStatisticsService bmsBillStatisticsService;
    @Autowired
    private BmsBillService bmsBillService;

    @ModelAttribute
    public BmsBillDetailEntity get(@RequestParam(required = false) String id) {
        BmsBillDetailEntity entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = bmsBillDetailService.getEntity(id);
        }
        if (entity == null) {
            entity = new BmsBillDetailEntity();
        }
        return entity;
    }

    /**
     * 列表页面
     */
    @RequiresPermissions("bms:finance:bmsBillDetail:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/bms/finance/bmsBillDetailList";
    }

    /**
     * 列表数据
     */
    @ResponseBody
    @RequiresPermissions("bms:finance:bmsBillDetail:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(BmsBillDetailEntity entity, HttpServletRequest request, HttpServletResponse response) {
        if (entity.getBusinessDateFm() != null) {
            entity.setBusinessDateFm(DateUtil.beginOfDate(entity.getBusinessDateFm()));
        }
        if (entity.getBusinessDateTo() != null) {
            entity.setBusinessDateTo(DateUtil.endOfDate(entity.getBusinessDateTo()));
        }
        return getBootstrapData(bmsBillDetailService.findPage(new Page<>(request, response), entity));
    }

    /**
     * 查看，增加，编辑表单页面
     */
    @RequiresPermissions(value = {"bms:finance:bmsBillDetail:view", "bms:finance:bmsBillDetail:add", "bms:finance:bmsBillDetail:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(BmsBillDetailEntity bmsBillDetailEntity, Model model) {
        model.addAttribute("bmsBillDetailEntity", bmsBillDetailEntity);
        return "modules/bms/finance/bmsBillDetailForm";
    }

    /**
     * 保存
     */
    @ResponseBody
    @RequiresPermissions(value = {"bms:finance:bmsBillDetail:add", "bms:finance:bmsBillDetail:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(BmsBillDetail bmsBillDetail, Model model) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, bmsBillDetail)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        bmsBillDetailService.save(bmsBillDetail);
        j.setSuccess(true);
        j.setMsg("保存成功");
        return j;
    }

    /**
     * 批量删除
     */
    @ResponseBody
    @RequiresPermissions("bms:finance:bmsBillDetail:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();
        try {
            bmsBillDetailService.deleteAll(Arrays.stream(ids.split(",")).map(bmsBillDetailService::get).collect(Collectors.toList()));
            j.setMsg("删除成功");
        } catch (BmsException e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 删除(按费用单号)
     */
    @ResponseBody
    @RequiresPermissions("bms:finance:bmsBillDetail:del")
    @RequestMapping(value = "deleteByBillNo")
    public AjaxJson deleteByBillNo(String billNo, String orgId, String receivablePayable) {
        AjaxJson j = new AjaxJson();
        bmsBillDetailService.deleteByBillNo(billNo, orgId, receivablePayable);
        j.setMsg("删除成功");
        return j;
    }

    /**
     * 导出excel文件
     */
    @ResponseBody
    @RequiresPermissions("bms:finance:bmsBillDetail:export")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public AjaxJson exportFile(@RequestBody BmsBillDetailEntity entity, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            List<BmsBillDetail> list = bmsBillDetailService.findPage(new Page<>(request, response, -1), entity).getList();
            new ExportExcel(null, BmsBillDetail.class).setDataList(list).write(response, "费用明细.xlsx").dispose();
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
    @RequiresPermissions("bms:finance:bmsBillDetail:import")
    @RequestMapping(value = "import", method = RequestMethod.POST)
    public String importFile(MultipartFile file, String orgId, RedirectAttributes redirectAttributes) {
        try {
            bmsBillDetailService.importFile(new ImportExcel(file, 1, 0).getDataList(BmsBillDetailImport.class), orgId);
            addMessage(redirectAttributes, "导入成功");
        } catch (Exception e) {
            logger.error("导入失败", e);
            addMessage(redirectAttributes, "导入失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/bms/finance/bmsBillDetail/?repage";
    }

    /**
     * 下载导入数据模板
     */
    @RequiresPermissions("bms:finance:bmsBillDetail:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            new ExportExcel(null, BmsBillDetailImport.class, 1).setDataList(Lists.newArrayList()).write(response, "数据导入模板.xlsx").dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/bms/finance/bmsBillDetail/?repage";
    }

    /**
     * 描述：统计发生量、计费量、费用
     */
    @ResponseBody
    @RequestMapping("getTotal")
    public AjaxJson getTotal(BmsBillDetailEntity entity) {
        AjaxJson j = new AjaxJson();
        j.setSuccess(true);
        if (entity.getBusinessDateFm() != null) {
            entity.setBusinessDateFm(DateUtil.beginOfDate(entity.getBusinessDateFm()));
        }
        if (entity.getBusinessDateTo() != null) {
            entity.setBusinessDateTo(DateUtil.endOfDate(entity.getBusinessDateTo()));
        }
        Map<String, BigDecimal> map = bmsBillDetailService.getTotal(entity);
        double sumOccurrenceQty = 0, sumBillQty = 0, sumCost = 0;
        if (MapUtil.isNotEmpty(map)) {
            if (map.containsKey("sumOccurrenceQty")) {
                sumOccurrenceQty = map.get("sumOccurrenceQty").doubleValue();
            }
            if (map.containsKey("sumBillQty")) {
                sumBillQty = map.get("sumBillQty").doubleValue();
            }
            if (map.containsKey("sumCost")) {
                sumCost = map.get("sumCost").doubleValue();
            }
        }
        j.put("sumOccurrenceQty", sumOccurrenceQty);
        j.put("sumBillQty", sumBillQty);
        j.put("sumCost", sumCost);
        return j;
    }

    /**
     * 描述：生成账单
     */
    @ResponseBody
    @RequiresPermissions("bms:finance:bmsBillDetail:genBill")
    @RequestMapping(value = "genBill")
    public AjaxJson genBill(BmsBillDetailEntity entity) {
        AjaxJson j = new AjaxJson();
        try {
            entity.setStatus(BmsConstants.BILL_STATUS_01);
            List<BmsBillDetail> list = bmsBillDetailService.findPage(new Page<>(), entity).getList();

            Map<String, List<BmsBillDetail>> map = list.stream().filter(o -> BmsConstants.BILL_STATUS_01.equals(o.getStatus())).collect(Collectors.groupingBy(o -> o.getSettleObjectCode() + o.getOrgId()));
            for (Map.Entry<String, List<BmsBillDetail>> entry : map.entrySet()) {
                List<BmsBillDetail> bmsBillDetails = entry.getValue();

                BmsBill bmsBill = new BmsBill();
                bmsBill.setSettleObjCode(bmsBillDetails.get(0).getSettleObjectCode());
                bmsBill.setSettleObjName(bmsBillDetails.get(0).getSettleObjectName());
                bmsBill.setAmount(bmsBillDetails.stream().map(BmsBillDetail::getCost).reduce(BigDecimal.ZERO, BigDecimal::add).doubleValue());
                bmsBill.setOrgId(bmsBillDetails.get(0).getOrgId());
                bmsBillService.save(bmsBill);
                // 绑定账单号
                for (BmsBillDetail bmsBillDetail : bmsBillDetails) {
                    bmsBillDetail.setConfirmNo(bmsBill.getConfirmNo());
                    bmsBillDetail.setStatus(BmsConstants.BILL_STATUS_02);
                    bmsBillDetailService.save(bmsBillDetail);
                }
                // 生成费用统计
                bmsBillStatisticsService.reStatistics(bmsBill.getConfirmNo(), bmsBill.getOrgId());
            }
        } catch (BmsException e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

}