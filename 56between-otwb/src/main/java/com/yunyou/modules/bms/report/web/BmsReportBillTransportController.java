package com.yunyou.modules.bms.report.web;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.common.utils.collection.MapUtil;
import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.bms.finance.entity.BmsBillDetail;
import com.yunyou.modules.bms.finance.entity.extend.BmsBillDetailEntity;
import com.yunyou.modules.bms.finance.entity.extend.BmsBillTransportDetailExport;
import com.yunyou.modules.bms.finance.service.BmsBillDetailService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 运输费用Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/bms/report/billTransport")
public class BmsReportBillTransportController extends BaseController {
    @Autowired
    private BmsBillDetailService bmsBillDetailService;

    @RequiresPermissions("bms:report:billTransport:list")
    @RequestMapping(value = "list")
    public String list(Model model) {
        model.addAttribute("bmsBillSkuDetailEntity", new BmsBillDetailEntity());
        return "modules/bms/report/bmsReportBillTransportList";
    }

    @ResponseBody
    @RequiresPermissions("bms:report:billTransport:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(BmsBillDetailEntity bmsBillDetailEntity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(bmsBillDetailService.findTransportPage(new Page<>(request, response), bmsBillDetailEntity));
    }

    @ResponseBody
    @RequiresPermissions("bms:report:billTransport:list")
    @RequestMapping("getTotal")
    public AjaxJson getTotal(BmsBillDetailEntity entity) {
        AjaxJson j = new AjaxJson();
        j.setSuccess(true);
        Map<String, BigDecimal> map = bmsBillDetailService.getTotal(entity);
        double sumActualValue1 = 0D, sumOccurrenceQty = 0D, sumCost = 0D;
        if (MapUtil.isNotEmpty(map)) {
            if (map.containsKey("sumOccurrenceQty")) {
                sumOccurrenceQty = map.get("sumOccurrenceQty").doubleValue();
            }
            if (map.containsKey("sumActualValue1")) {
                sumActualValue1 = map.get("sumActualValue1").doubleValue();
            }
            if (map.containsKey("sumCost")) {
                sumCost = map.get("sumCost").doubleValue();
            }
        }
        LinkedHashMap<String, Object> rsMap = Maps.newLinkedHashMap();
        rsMap.put("sumOccurrenceQty", sumOccurrenceQty);
        rsMap.put("sumActualValue1", sumActualValue1);
        rsMap.put("sumCost", sumCost);

        j.setBody(rsMap);
        return j;
    }

    @RequiresPermissions("bms:report:billTransport:export")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public void exportExcel(BmsBillDetailEntity qEntity, HttpServletRequest request, HttpServletResponse response) {
        Page<BmsBillDetail> page = bmsBillDetailService.findPage(new Page<>(request, response, -1), qEntity);
        List<BmsBillTransportDetailExport> rsList = Lists.newArrayList();
        if (CollectionUtil.isNotEmpty(page.getList())) {
            for (BmsBillDetail entity : page.getList()) {
                BmsBillTransportDetailExport export = new BmsBillTransportDetailExport();
                BeanUtils.copyProperties(entity, export);
                rsList.add(export);
            }
        }
        try {
            new ExportExcel(null, BmsBillTransportDetailExport.class).setDataList(rsList).write(response, "运输费用.xlsx").dispose();
        } catch (IOException e) {
            logger.error("运输费用导出失败", e);
        }
    }
}
