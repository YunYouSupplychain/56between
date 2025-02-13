package com.yunyou.modules.bms.business.service;

import com.yunyou.common.config.GlobalDataRule;
import com.yunyou.common.utils.IdGen;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.DataEntity;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.bms.business.entity.BmsInboundData;
import com.yunyou.modules.bms.business.entity.extend.BmsInboundDataEntity;
import com.yunyou.modules.bms.business.mapper.BmsInboundDataMapper;
import com.yunyou.modules.bms.calculate.BmsCalculateUtils;
import com.yunyou.modules.bms.common.BmsConstants;
import com.yunyou.modules.bms.common.BmsException;
import com.yunyou.modules.bms.finance.entity.extend.BmsCalcTermsParams;
import com.yunyou.modules.bms.interactive.BmsPullDataCondition;
import com.yunyou.modules.sys.entity.User;
import com.yunyou.modules.sys.utils.UserUtils;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 入库数据Service
 *
 * @author Jianhua Liu
 * @version 2019-06-03
 */
@Service
@Transactional(readOnly = true)
public class BmsInboundDataService extends CrudService<BmsInboundDataMapper, BmsInboundData> {

    public Page<BmsInboundData> findPage(Page<BmsInboundData> page, BmsInboundDataEntity entity) {
        entity.setDataScope(GlobalDataRule.getDataRuleSql("a.org_id", entity.getOrgId()));
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    @Transactional
    public void batchInsert(List<BmsInboundData> data) {
        if (CollectionUtil.isEmpty(data)) {
            return;
        }
        Date date = new Date();
        User user = UserUtils.getUser();
        for (BmsInboundData o : data) {
            if (StringUtils.isBlank(o.getId())) {
                o.setId(IdGen.uuid());
            }
            if (o.getCreateBy() == null || StringUtils.isBlank(o.getCreateBy().getId())) {
                o.setCreateBy(user);
            }
            if (o.getCreateDate() == null) {
                o.setCreateDate(date);
            }
            if (o.getUpdateBy() == null || StringUtils.isBlank(o.getUpdateBy().getId())) {
                o.setUpdateBy(user);
            }
            if (o.getUpdateDate() == null) {
                o.setUpdateDate(date);
            }
        }
        for (int i = 0; i < data.size(); i += 999) {
            if (data.size() - i < 999) {
                mapper.batchInsert(data.subList(i, data.size()));
            } else {
                mapper.batchInsert(data.subList(i, i + 999));
            }
        }
    }

    @Transactional
    public void deleteByCondition(BmsPullDataCondition condition) {
        mapper.deleteByCondition(condition);
    }

    @Transactional
    public void importFile(List<BmsInboundData> importList, String orgId) {
        if (CollectionUtil.isEmpty(importList)) {
            return;
        }
        int size = importList.size();
        for (int i = 0; i < size; i++) {
            BmsInboundData data = importList.get(i);
            if (StringUtils.isBlank(data.getOrderNo())) {
                throw new BmsException("第" + (i + 1) + "行，订单号不能为空！");
            }
            if (null == data.getOrderDate()) {
                throw new BmsException("第" + (i + 1) + "行，订单日期不能为空！");
            }
            if (StringUtils.isBlank(data.getOwnerCode())) {
                throw new BmsException("第" + (i + 1) + "行，货主不能为空！");
            }
            if (StringUtils.isBlank(data.getSkuCode())) {
                throw new BmsException("第" + (i + 1) + "行，商品不能为空！");
            }
            if (null == data.getReceiptQty()) {
                throw new BmsException("第" + (i + 1) + "行，实收数量不能为空！");
            }
            if (StringUtils.isBlank(data.getOrderType())) {
                data.setOrderType("10");
            }
            if (StringUtils.isBlank(data.getBusinessType())) {
                data.setBusinessType("SWMS");
            }
            data.setDataSources(BmsConstants.BUSINESS_DATA_SOURCE_IMPORT);
            data.setIsFee(BmsConstants.YES);
            data.setOrgId(orgId);
        }
        this.batchInsert(importList);
    }

    public List<BmsInboundData> findCalcData(List<BmsCalcTermsParams> includeParams, List<BmsCalcTermsParams> excludeParams) {
        List<BmsInboundData> rsList = Lists.newArrayList();
        String includeSql = BmsCalculateUtils.toWhereSql(includeParams);
        if (StringUtils.isBlank(includeSql)) {
            return rsList;
        }
        String excludeSql = BmsCalculateUtils.toWhereSql(excludeParams);
        List<BmsInboundData> includeData = mapper.findCalcData(includeSql);
        if (CollectionUtil.isEmpty(includeData)) {
            return rsList;
        }
        if (StringUtils.isNotBlank(excludeSql)) {
            Map<String, String> excludeIds = mapper.findCalcData(excludeSql).stream().map(DataEntity::getId).collect(Collectors.toMap(Function.identity(), Function.identity()));
            rsList = includeData.stream().filter(o -> !excludeIds.containsKey(o.getId())).collect(Collectors.toList());
        } else {
            rsList.addAll(includeData);
        }
        return rsList;
    }
}