package com.yunyou.modules.tms.report.service;

import com.google.common.collect.Lists;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.time.DateFormatUtil;
import com.yunyou.common.utils.time.DateUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.BaseService;
import com.yunyou.modules.tms.authority.TmAuthorityRule;
import com.yunyou.modules.tms.authority.TmAuthorityTable;
import com.yunyou.modules.tms.report.entity.TmGasOilStatistics;
import com.yunyou.modules.tms.report.mapper.TmGasOilStatisticsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TmGasOilStatisticsService extends BaseService {
    @Autowired
    private TmGasOilStatisticsMapper mapper;

    public List<TmGasOilStatistics> findList(TmGasOilStatistics qEntity) {
        List<TmGasOilStatistics> rsList = Lists.newArrayList();
        dataRuleFilter(qEntity);
        String authorityScope = TmAuthorityRule.dataRule(TmAuthorityTable.TM_HANDOVER_ORDER_HEADER.getValue(), qEntity.getOrgId());
        qEntity.setDataScope(StringUtils.isNotBlank(qEntity.getDataScope()) ? qEntity.getDataScope() + authorityScope : authorityScope);
        List<TmGasOilStatistics> list;
        if ("DAY".equals(qEntity.getTimeDimension())) {
            qEntity.setFmDate(DateUtil.beginOfDate(DateUtil.subDays(new Date(), 29)));// 最近30天
            qEntity.setToDate(DateUtil.endOfDate(new Date()));
            list = mapper.getGasOilDailyReceivedQty(qEntity);
        } else if ("MONTH".equals(qEntity.getTimeDimension())) {
            qEntity.setFmDate(DateUtil.beginOfDate(DateUtil.subMonths(new Date(), 11)));// 最近1年
            qEntity.setToDate(DateUtil.endOfDate(new Date()));
            list = mapper.getGasOilMonthReceivedQty(qEntity);
        } else {
            list = Lists.newArrayList();
        }
        List<TmGasOilStatistics> gasOilList = mapper.getGasOil(qEntity.getGasCode(), qEntity.getBaseOrgId());

        Map<String, TmGasOilStatistics> map = list.stream().collect(Collectors.toMap(k -> k.getGasCode() + k.getOilCode() + k.getDate(), v -> v, (v1, v2) -> v1));
        Map<String, TmGasOilStatistics> gasSkuMap = gasOilList.stream().collect(Collectors.toMap(k -> k.getGasCode() + k.getOilCode(), v -> v, (v1, v2) -> v1));

        if ("DAY".equals(qEntity.getTimeDimension()) || "MONTH".equals(qEntity.getTimeDimension())) {
            Date curDate = qEntity.getFmDate();
            while (qEntity.getToDate().after(curDate)) {
                String date;
                if ("DAY".equals(qEntity.getTimeDimension())) {
                    date = DateFormatUtil.formatDate("yyyy-MM-dd", curDate);
                    curDate = DateUtil.addDays(curDate, 1);
                } else {
                    date = DateFormatUtil.formatDate("yyyy-MM", curDate);
                    curDate = DateUtil.addMonths(curDate, 1);
                }
                for (Map.Entry<String, TmGasOilStatistics> entry : gasSkuMap.entrySet()) {
                    TmGasOilStatistics gasSku = entry.getValue();

                    TmGasOilStatistics tmGasOilStatistics = new TmGasOilStatistics();
                    tmGasOilStatistics.setDate(date);
                    tmGasOilStatistics.setGasCode(gasSku.getGasCode());
                    tmGasOilStatistics.setGasName(gasSku.getGasName());
                    tmGasOilStatistics.setOilCode(gasSku.getOilCode());
                    tmGasOilStatistics.setOilName(gasSku.getOilName());
                    if (map.containsKey(entry.getKey() + date)) {
                        tmGasOilStatistics.setQty(map.get(entry.getKey() + date).getQty());
                    } else {
                        tmGasOilStatistics.setQty(0D);
                    }
                    rsList.add(tmGasOilStatistics);
                }
            }
        }
        return rsList.stream().sorted(Comparator.comparing(TmGasOilStatistics::getDate, Comparator.nullsLast(String::compareTo))
                .thenComparing(TmGasOilStatistics::getGasCode, Comparator.nullsLast(String::compareTo))
                .thenComparing(TmGasOilStatistics::getOilCode, Comparator.nullsLast(String::compareTo)))
                .collect(Collectors.toList());
    }

    public Page<TmGasOilStatistics> getGas(Page<TmGasOilStatistics> page, TmGasOilStatistics qEntity) {
        qEntity.setPage(page);
        page.setList(mapper.getGas(qEntity));
        return page;
    }
}
