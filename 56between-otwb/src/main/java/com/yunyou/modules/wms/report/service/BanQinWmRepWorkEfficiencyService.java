package com.yunyou.modules.wms.report.service;

import com.google.common.collect.Lists;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.common.utils.time.DateFormatUtil;
import com.yunyou.core.service.BaseService;
import com.yunyou.modules.wms.report.entity.WmRepWorkEfficiencyEntity;
import com.yunyou.modules.wms.report.entity.WmRepWorkEfficiencyQuery;
import com.yunyou.modules.wms.report.entity.WmRepWorkEfficiencyReport;
import com.yunyou.modules.wms.report.mapper.BanQinWmRepWorkEfficiencyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BanQinWmRepWorkEfficiencyService extends BaseService {
    @Autowired
    private BanQinWmRepWorkEfficiencyMapper mapper;

    public List<WmRepWorkEfficiencyReport> findList(WmRepWorkEfficiencyQuery query) {
        List<WmRepWorkEfficiencyEntity> sourceList = Lists.newArrayList();
        if ("RCV".equals(query.getReportType())) {
            sourceList = mapper.findRcvList(query);
        } else if ("PK".equals(query.getReportType())) {
            sourceList = mapper.findPkList(query);
        } else if ("REVIEW".equals(query.getReportType())) {
            sourceList = mapper.findReviewList(query);
        }
        if (CollectionUtil.isEmpty(sourceList)) {
            return Lists.newArrayList();
        }

        List<WmRepWorkEfficiencyReport> rsList = Lists.newArrayList();
        long totalEa = 0, totalOrder = 0;
        double totalEaEfficiency = 0D, totalOrderEfficiency = 0D;
        // 按操作员+日期分组
        Map<String, List<WmRepWorkEfficiencyEntity>> map = sourceList.stream().collect(
                Collectors.groupingBy(o -> o.getOperator() + DateFormatUtil.formatDate("yyyyMMdd", o.getOperateDate())));
        for (List<WmRepWorkEfficiencyEntity> list : map.values()) {
            WmRepWorkEfficiencyEntity entity = list.get(0);
            // 总件数
            long totalEaNum = list.stream().mapToLong(WmRepWorkEfficiencyEntity::getOrderNum).sum();
            // 总订单数
            long totalOrderNum = list.stream().map(WmRepWorkEfficiencyEntity::getOrderNo).distinct().count();
            // 工作时长（H）
            Date maxDate = list.stream().map(WmRepWorkEfficiencyEntity::getOperateDate).max(Comparator.comparingLong(Date::getTime)).get();
            Date minDate = list.stream().map(WmRepWorkEfficiencyEntity::getOperateDate).min(Comparator.comparingLong(Date::getTime)).get();
            double workingTime = BigDecimal.valueOf(maxDate.getTime())
                    .subtract(BigDecimal.valueOf(minDate.getTime())).divide(BigDecimal.valueOf(3600000), 2, BigDecimal.ROUND_HALF_UP).doubleValue();
            // 单均件
            double averageEaNum = 0D;
            if (totalOrderNum > 0) {
                averageEaNum = BigDecimal.valueOf(totalEaNum).divide(BigDecimal.valueOf(totalOrderNum), 2, BigDecimal.ROUND_HALF_UP).doubleValue();
            }
            // 件效率（件/H）
            double eaEfficiency = 0D;
            // 单效率（单/H）
            double orderEfficiency = 0D;
            if (workingTime > 0) {
                eaEfficiency = BigDecimal.valueOf(totalEaNum).divide(BigDecimal.valueOf(workingTime), 2, BigDecimal.ROUND_HALF_UP).doubleValue();
                orderEfficiency = BigDecimal.valueOf(totalOrderNum).divide(BigDecimal.valueOf(workingTime), 2, BigDecimal.ROUND_HALF_UP).doubleValue();
            }

            // 合计
            totalEa += totalEaNum;
            totalOrder += totalOrderNum;
            totalEaEfficiency += eaEfficiency;
            totalOrderEfficiency += orderEfficiency;

            WmRepWorkEfficiencyReport report = new WmRepWorkEfficiencyReport();
            report.setOperateDate(DateFormatUtil.formatDate("MM月dd日", entity.getOperateDate()));
            report.setOperator(entity.getOperator());
            report.setTotalEaNum(totalEaNum);
            report.setTotalOrderNum(totalOrderNum);
            report.setAverageEaNum(averageEaNum);
            report.setWorkingTime(workingTime);
            report.setEaEfficiency(eaEfficiency);
            report.setOrderEfficiency(orderEfficiency);
            rsList.add(report);
        }
        // 按总件数降序
        rsList.sort(Comparator.comparingLong(WmRepWorkEfficiencyReport::getTotalEaNum).reversed());
        // 设置序号
        for (int i = 0; i < rsList.size(); i++) {
            rsList.get(i).setSerialNo(i + 1);
        }

        // 总计
        if (CollectionUtil.isNotEmpty(rsList)) {
            WmRepWorkEfficiencyReport total = new WmRepWorkEfficiencyReport();
            total.setOperator("总计");
            total.setTotalEaNum(totalEa);
            total.setTotalOrderNum(totalOrder);
            total.setEaEfficiency(BigDecimal.valueOf(totalEaEfficiency).divide(BigDecimal.valueOf(rsList.size()), 2, BigDecimal.ROUND_HALF_UP).doubleValue());
            total.setOrderEfficiency(BigDecimal.valueOf(totalOrderEfficiency).divide(BigDecimal.valueOf(rsList.size()), 2, BigDecimal.ROUND_HALF_UP).doubleValue());
            rsList.add(total);
        }
        return rsList;
    }
}
