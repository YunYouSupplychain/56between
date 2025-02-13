package com.yunyou.modules.bms.finance.web;

import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.collection.MapUtil;
import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.bms.finance.entity.BmsBillStatistics;
import com.yunyou.modules.bms.finance.entity.extend.BmsBillStatisticsEntity;
import com.yunyou.modules.bms.finance.service.BmsBillStatisticsService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Map;

/**
 * 费用统计Controller
 *
 * @author Jianhua Liu
 * @version 2019-05-29
 */
@Controller
@RequestMapping(value = "${adminPath}/bms/finance/bmsBillStatistics")
public class BmsBillStatisticsController extends BaseController {
    @Autowired
    private BmsBillStatisticsService bmsBillStatisticsService;

    /**
     * 应收列表页面
     */
    @RequiresPermissions("bms:finance:bmsBillStatistics:list:receivable")
    @RequestMapping("/receivable/list")
    public String receivableList(Model model) {
        model.addAttribute("bmsBillStatisticsEntity", new BmsBillStatisticsEntity());
        return "modules/bms/finance/bmsBillReceivableStatisticsList";
    }

    /**
     * 应付列表页面
     */
    @RequiresPermissions("bms:finance:bmsBillStatistics:list:payable")
    @RequestMapping("/payable/list")
    public String payableList(Model model) {
        model.addAttribute("bmsBillStatisticsEntity", new BmsBillStatisticsEntity());
        return "modules/bms/finance/bmsBillPayableStatisticsList";
    }

    @ResponseBody
    @RequestMapping(value = "data")
    public Map<String, Object> data(BmsBillStatisticsEntity entity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(bmsBillStatisticsService.findPage(new Page<>(request, response), entity));
    }

    /**
     * 描述：查询统计数量
     */
    @ResponseBody
    @RequestMapping("getTotal")
    public AjaxJson getTotal(BmsBillStatisticsEntity entity) {
        AjaxJson j = new AjaxJson();
        Map<String, BigDecimal> map = bmsBillStatisticsService.getTotal(entity);
        double sumOccurrenceQty = 0D, sumBillQty = 0D, sumCost = 0D;
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
     * 导出excel文件
     */
    @ResponseBody
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public AjaxJson exportFile(@RequestBody BmsBillStatistics bmsBillStatistics, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            Page<BmsBillStatistics> page = bmsBillStatisticsService.findPage(new Page<>(request, response, -1), bmsBillStatistics);
            new ExportExcel(null, BmsBillStatistics.class).setDataList(page.getList()).write(response, ".xlsx").dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }
}