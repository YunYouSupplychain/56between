package com.yunyou.modules.bms.report.web;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.bms.report.entity.*;
import com.yunyou.modules.bms.report.service.BmsReportAccountReceivableService;
import com.yunyou.modules.bms.common.BmsConstants;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "${adminPath}/bms/report/accountReceivable")
public class BmsReportAccountReceivableController extends BaseController {
    @Autowired
    private BmsReportAccountReceivableService bmsReportAccountReceivableService;

    @RequiresPermissions("bms:report:accountReceivable:list")
    @RequestMapping("list")
    public String list(Model model) {
        model.addAttribute("bmsReportAccountReceivableQuery", new BmsReportAccountReceivableQuery());
        return "modules/bms/report/bmsReportAccountReceivableList";
    }

    @ResponseBody
    @RequiresPermissions("bms:report:accountReceivable:list")
    @RequestMapping("data")
    public Map<String, Object> data(BmsReportAccountReceivableQuery query, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(bmsReportAccountReceivableService.findPage(new Page<>(request, response), query));
    }

    @RequiresPermissions(value = "bms:report:accountReceivable:export")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public void exportExcel(@RequestBody BmsReportAccountReceivableQuery query, HttpServletRequest request, HttpServletResponse response) {
        try {
            Page<BmsReportAccountReceivableEntity> page = bmsReportAccountReceivableService.findPage(new Page<>(request, response, -1), query);
            new ExportExcel(null, BmsReportAccountReceivableEntity.class).setDataList(page.getList()).write(response, "应收账款结算单.xlsx").dispose();
        } catch (IOException e) {
            logger.error("应收账款结算单导出Excel失败", e);
        }
    }

    @RequiresPermissions(value = "bms:report:accountReceivable:print")
    @RequestMapping(value = "/print")
    public String print(BmsReportAccountReceivableQuery query, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<BmsReportAccountReceivableEntity> page = bmsReportAccountReceivableService.findPage(new Page<>(request, response, -1), query);

        Map<String, BmsReportAccountReceivable> map = Maps.newHashMap();
        for (BmsReportAccountReceivableEntity entity : page.getList()) {
            // 第一步：按费用单号+开票对象+快行开票对象+机构分組
            String key = entity.getBillNo() + entity.getInvoiceObjectCode() + entity.getKxtxCode() + entity.getOrgId();

            // 各费用明细
            BmsReportAccountReceivableSub1 receivableSub1 = new BmsReportAccountReceivableSub1();
            BeanUtils.copyProperties(entity, receivableSub1);
            receivableSub1.setCost(entity.getCost());
            // 各税率费用
            BmsReportAccountReceivableSub2 receivableSub2 = new BmsReportAccountReceivableSub2();
            receivableSub2.setTaxRate(entity.getTaxRate() == null ? 0D : entity.getTaxRate());
            receivableSub2.setAmountIncludeTax(entity.getCost() == null ? BigDecimal.ZERO : entity.getCost());

            if (map.containsKey(key)) {
                BmsReportAccountReceivable receivable = map.get(key);
                receivable.getSub1List().add(receivableSub1);
                receivable.getSub2List().add(receivableSub2);
            } else {
                BmsReportAccountReceivable receivable = new BmsReportAccountReceivable();
                List<BmsReportAccountReceivableSub1> sub1List = Lists.newArrayList();
                List<BmsReportAccountReceivableSub2> sub2List = Lists.newArrayList();

                BeanUtils.copyProperties(entity, receivable);
                sub1List.add(receivableSub1);
                sub2List.add(receivableSub2);
                receivable.setSub1List(sub1List);
                receivable.setSub2List(sub2List);
                map.put(key, receivable);
            }
        }

        // 第二步：主数据按费用单号、开票对象排序
        List<BmsReportAccountReceivable> rsList = map.values().stream().sorted(Comparator.comparing(BmsReportAccountReceivable::getBillNo).reversed()
                .thenComparing(BmsReportAccountReceivable::getInvoiceObjectCode)).collect(Collectors.toList());

        for (BmsReportAccountReceivable receivable : rsList) {
            // 第三步：按仓别+税率分组做小计且分组后按仓别、计费科目、结算对象排序
            List<BmsReportAccountReceivableSub1> sub1List = Lists.newArrayList();
            Map<String, List<BmsReportAccountReceivableSub1>> sub1Map = receivable.getSub1List().stream().collect(Collectors.groupingBy(o -> o.getWarehouseCode() + o.getTaxRate()));
            List<String> keys = sub1Map.keySet().stream().sorted().collect(Collectors.toList());
            for (String key : keys) {
                List<BmsReportAccountReceivableSub1> sub1s = sub1Map.get(key);
                BmsReportAccountReceivableSub1 subtotalSub2 = new BmsReportAccountReceivableSub1();
                subtotalSub2.setWarehouseCode(sub1s.get(0).getWarehouseCode());
                subtotalSub2.setWarehouseName(sub1s.get(0).getWarehouseName());
                subtotalSub2.setSettleObjectCode("小计");
                for (BmsReportAccountReceivableSub1 sub1 : sub1s) {
                    subtotalSub2.setOccurrenceQty(subtotalSub2.getOccurrenceQty().add(sub1.getOccurrenceQty()));
                    subtotalSub2.setBillQty(subtotalSub2.getBillQty().add(sub1.getBillQty()));
                    subtotalSub2.setCost(subtotalSub2.getCost().add(sub1.getCost()));
                }
                sub1s.sort(Comparator.comparing(BmsReportAccountReceivableSub1::getWarehouseCode)
                        .thenComparing(BmsReportAccountReceivableSub1::getBillSubjectName)
                        .thenComparing(BmsReportAccountReceivableSub1::getSettleObjectCode)
                        .thenComparing(BmsReportAccountReceivableSub1::getBillStandard, Comparator.nullsLast(BigDecimal::compareTo))
                        .thenComparing(BmsReportAccountReceivableSub1::getCostRemarks, Comparator.nullsLast(String::compareTo)));
                sub1List.addAll(sub1s);
                sub1List.add(subtotalSub2);
            }
            receivable.setSub1List(sub1List);

            // 第四步：各税率费用相同税率合计，同时增加一条总合计记录
            List<BmsReportAccountReceivableSub2> sub2List = Lists.newArrayList();
            // 各税率费用相同税率合计
            Map<Double, BmsReportAccountReceivableSub2> sub2Map = Maps.newHashMap();
            for (BmsReportAccountReceivableSub2 receivableSub2 : receivable.getSub2List()) {
                if (sub2Map.containsKey(receivableSub2.getTaxRate())) {
                    BmsReportAccountReceivableSub2 sub2 = sub2Map.get(receivableSub2.getTaxRate());
                    BigDecimal amountIncludeTax = sub2.getAmountIncludeTax().add(receivableSub2.getAmountIncludeTax());
                    sub2.setAmountIncludeTax(amountIncludeTax);
                } else {
                    BmsReportAccountReceivableSub2 sub2 = new BmsReportAccountReceivableSub2();
                    BeanUtils.copyProperties(receivableSub2, sub2);
                    sub2Map.put(receivableSub2.getTaxRate(), sub2);
                }
            }
            // 总含税金额、总未税金额、总税额
            BigDecimal totalAmountIncludeTax = BigDecimal.ZERO, totalAmount = BigDecimal.ZERO, totalTaxAmount = BigDecimal.ZERO;
            // 按税率升序
            List<BmsReportAccountReceivableSub2> sub2s = sub2Map.values().stream().sorted(Comparator.comparingDouble(BmsReportAccountReceivableSub2::getTaxRate)).collect(Collectors.toList());
            for (int i = 0; i < sub2s.size(); i++) {
                BmsReportAccountReceivableSub2 sub2 = sub2s.get(i);
                BigDecimal amountIncludeTax = sub2.getAmountIncludeTax().setScale(BmsConstants.BILL_RETAIN_DECIMAL, BigDecimal.ROUND_HALF_UP);
                BigDecimal amount = sub2.getAmountIncludeTax().divide(BigDecimal.ONE.add(BigDecimal.valueOf(sub2.getTaxRate())), BmsConstants.BILL_RETAIN_DECIMAL, BigDecimal.ROUND_HALF_UP);
                BigDecimal taxAmount = amountIncludeTax.subtract(amount);
                sub2.setLineNo((i + 1) + "");
                sub2.setAmount(amount);
                sub2.setTaxAmount(taxAmount);
                sub2List.add(sub2);

                totalAmountIncludeTax = totalAmountIncludeTax.add(amountIncludeTax);
                totalAmount = totalAmount.add(amount);
                totalTaxAmount = totalTaxAmount.add(taxAmount);
            }
            // 总合计记录
            BmsReportAccountReceivableSub2 totalSub2 = new BmsReportAccountReceivableSub2();
            totalSub2.setLineNo("总计");
            totalSub2.setAmountIncludeTax(totalAmountIncludeTax);
            totalSub2.setAmount(totalAmount);
            totalSub2.setTaxAmount(totalTaxAmount);
            sub2List.add(totalSub2);

            receivable.setSub2List(sub2List);
        }

        // 报表数据源
        JRDataSource jrDataSource = new JRBeanCollectionDataSource(rsList);
        // 动态指定报表模板url
        model.addAttribute("url", "classpath:/jasper/billAccountReceivable2.jasper");
        model.addAttribute("format", "pdf");
        model.addAttribute("jrMainDataSource", jrDataSource);
        return "iReportView";
    }
}
