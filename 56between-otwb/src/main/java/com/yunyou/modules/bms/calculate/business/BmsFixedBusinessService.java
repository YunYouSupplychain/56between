package com.yunyou.modules.bms.calculate.business;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.common.utils.time.DateUtil;
import com.yunyou.core.service.BaseService;
import com.yunyou.modules.bms.common.BmsConstants;
import com.yunyou.modules.bms.finance.entity.extend.BmsCalcTermsParams;
import com.yunyou.modules.sys.entity.Office;
import com.yunyou.modules.sys.utils.UserUtils;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 业务数据-固定Service
 *
 * @author liujianhua
 * @version 2022.8.11
 */
@Service
public class BmsFixedBusinessService extends BaseService {

    /**
     * 查找业务数据
     *
     * @param outputObject 条款输出对象
     * @param periodType   周期类型
     * @param settleDateFm 结算日期从
     * @param settleDateTo 结算日期到
     * @return 业务数据
     */
    public List<BmsCalcBusinessData> findForCalcFixed(String outputObject, String periodType, Date settleDateFm, Date settleDateTo) {
        List<BmsCalcBusinessData> rsList = Lists.newArrayList();
        if (BmsConstants.OUTPUT_OBJECT_999.equals(outputObject)) {
            Date date;
            if ("Daily".equals(periodType)) {
                date = DateUtil.beginOfDate(DateUtil.endOfDate(settleDateFm));
            } else if ("Monthly".equals(periodType)) {
                date = DateUtil.beginOfDate(DateUtil.endOfMonth(settleDateFm));
            } else if ("Yearly".equals(periodType)) {
                date = DateUtil.beginOfDate(DateUtil.endOfYear(settleDateFm));
            } else {
                return rsList;
            }
            while (!date.after(settleDateTo)) {
                BmsCalcBusinessData bmsBusinessParams = new BmsCalcBusinessData();
                bmsBusinessParams.setBusinessDate(date);
                bmsBusinessParams.setOutputValue(BigDecimal.ONE);
                rsList.add(bmsBusinessParams);
                if ("Daily".equals(periodType)) {
                    date = DateUtil.addDays(date, 1);
                } else if ("Monthly".equals(periodType)) {
                    date = DateUtil.addMonths(date, 1);
                } else {
                    date = DateUtil.addMonths(date, 12);
                }
            }
        }
        return rsList;
    }

    /**
     * 查找业务数据
     *
     * @param outputObject  条款输出对象
     * @param periodType    周期类型
     * @param settleDateFm  结算日期从
     * @param settleDateTo  结算日期到
     * @param includeParams 条款包含参数
     * @param excludeParams 条款排除参数
     * @return 业务数据
     */
    public List<BmsCalcBusinessData> findForCalcFixed(String outputObject, String periodType, Date settleDateFm, Date settleDateTo, List<BmsCalcTermsParams> includeParams, List<BmsCalcTermsParams> excludeParams) {
        List<BmsCalcBusinessData> rsList = Lists.newArrayList();
        if (BmsConstants.OUTPUT_OBJECT_999.equals(outputObject)) {
            Date date;
            if ("Daily".equals(periodType)) {
                date = DateUtil.beginOfDate(DateUtil.endOfDate(settleDateFm));
            } else if ("Monthly".equals(periodType)) {
                date = DateUtil.beginOfDate(DateUtil.endOfMonth(settleDateFm));
            } else if ("Yearly".equals(periodType)) {
                date = DateUtil.beginOfDate(DateUtil.endOfYear(settleDateFm));
            } else {
                return rsList;
            }

            Set<String> orgCodes = includeParams.stream().filter(o -> BmsConstants.YES.equals(o.getIsEnable()) && BmsConstants.TERMS_PARAM_TYPE_TEXT.equals(o.getType()) && "org_id".equals(o.getField())).map(BmsCalcTermsParams::getFieldValue).filter(StringUtils::isNotBlank).collect(Collectors.toSet());
            orgCodes.removeAll(excludeParams.stream().filter(o -> BmsConstants.YES.equals(o.getIsEnable()) && BmsConstants.TERMS_PARAM_TYPE_TEXT.equals(o.getType()) && "org_id".equals(o.getField())).map(BmsCalcTermsParams::getFieldValue).filter(StringUtils::isNotBlank).collect(Collectors.toSet()));
            if (CollectionUtil.isEmpty(orgCodes)) {
                return this.findForCalcFixed(outputObject, periodType, settleDateFm, settleDateTo);
            }
            List<Office> orgList = orgCodes.stream().map(s -> Arrays.stream(s.split(",")).map(UserUtils::getOfficeByCode).filter(Objects::nonNull).collect(Collectors.toList())).collect(ArrayList::new, ArrayList::addAll, ArrayList::addAll);

            while (!date.after(settleDateTo)) {
                for (Office office : orgList) {
                    BmsCalcBusinessData bmsCalcBusinessParams = new BmsCalcBusinessData();
                    bmsCalcBusinessParams.setBusinessDate(date);
                    bmsCalcBusinessParams.setOutputValue(BigDecimal.ONE);
                    bmsCalcBusinessParams.setBusinessOrgId(office.getId());
                    bmsCalcBusinessParams.setBusinessOrgCode(office.getCode());
                    bmsCalcBusinessParams.setBusinessOrgName(office.getName());
                    rsList.add(bmsCalcBusinessParams);
                }
                if ("Daily".equals(periodType)) {
                    date = DateUtil.addDays(date, 1);
                } else if ("Monthly".equals(periodType)) {
                    date = DateUtil.addMonths(date, 1);
                } else {
                    date = DateUtil.addMonths(date, 12);
                }
            }
        }
        return rsList;
    }
}
