package com.yunyou.modules.bms.business.service;

import com.yunyou.common.config.GlobalDataRule;
import com.yunyou.common.exception.GlobalException;
import com.yunyou.common.utils.IdGen;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.DataEntity;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.bms.business.entity.BmsDispatchData;
import com.yunyou.modules.bms.business.entity.extend.BmsDispatchDataEntity;
import com.yunyou.modules.bms.business.mapper.BmsDispatchDataMapper;
import com.yunyou.modules.bms.calculate.BmsCalculateUtils;
import com.yunyou.modules.bms.common.BmsConstants;
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
 * 派车配载数据Service
 *
 * @author Jianhua Liu
 * @version 2019-07-16
 */
@Service
@Transactional(readOnly = true)
public class BmsDispatchDataService extends CrudService<BmsDispatchDataMapper, BmsDispatchData> {

    public Page<BmsDispatchData> findPage(Page<BmsDispatchData> page, BmsDispatchDataEntity entity) {
        entity.setDataScope(GlobalDataRule.getDataRuleSql("a.org_id", entity.getOrgId()));
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    @Transactional
    public void batchInsert(List<BmsDispatchData> data) {
        if (CollectionUtil.isEmpty(data)) {
            return;
        }
        Date date = new Date();
        User user = UserUtils.getUser();
        for (BmsDispatchData o : data) {
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
    public void importFile(List<BmsDispatchData> importList, String orgId) {
        if (CollectionUtil.isEmpty(importList)) {
            return;
        }
        int size = importList.size();
        for (int i = 0; i < size; i++) {
            BmsDispatchData data = importList.get(i);
            if (StringUtils.isBlank(data.getOrderNo())) {
                throw new GlobalException("第" + (i + 1) + "行，订单号不能为空！");
            }
            if (null == data.getOrderDate()) {
                throw new GlobalException("第" + (i + 1) + "行，订单日期不能为空！");
            }
            if (null == data.getDispatchTime()) {
                throw new GlobalException("第" + (i + 1) + "行，派车时间不能为空！");
            }
            if (StringUtils.isBlank(data.getCarrierCode())) {
                throw new GlobalException("第" + (i + 1) + "行，承运商不能为空！");
            }
            if (StringUtils.isBlank(data.getCarType())) {
                throw new GlobalException("第" + (i + 1) + "行，车型不能为空！");
            }
            if (StringUtils.isBlank(data.getVehicleNo())) {
                throw new GlobalException("第" + (i + 1) + "行，车牌号不能为空！");
            }
            if (StringUtils.isBlank(data.getShipCode())) {
                throw new GlobalException("第" + (i + 1) + "行，发货方不能为空！");
            }
            if (StringUtils.isBlank(data.getShipCityId())) {
                throw new GlobalException("第" + (i + 1) + "行，发货城市不能为空！");
            }
            if (StringUtils.isBlank(data.getConsigneeCode())) {
                throw new GlobalException("第" + (i + 1) + "行，收货方不能为空！");
            }
            if (StringUtils.isBlank(data.getConsigneeCityId())) {
                throw new GlobalException("第" + (i + 1) + "行，目的地城市不能为空！");
            }
            if (StringUtils.isBlank(data.getOwnerCode())) {
                throw new GlobalException("第" + (i + 1) + "行，货主不能为空！");
            }
            if (StringUtils.isBlank(data.getSkuCode())) {
                throw new GlobalException("第" + (i + 1) + "行，商品不能为空！");
            }
            if (null == data.getQty()) {
                throw new GlobalException("第" + (i + 1) + "行，数量不能为空！");
            }
            if (StringUtils.isBlank(data.getTransportMethod())) {
                data.setTransportMethod("3");
            }
            data.setDataSources(BmsConstants.BUSINESS_DATA_SOURCE_IMPORT);
            data.setIsFee(BmsConstants.YES);
            data.setOrgId(orgId);
        }
        this.batchInsert(importList);
    }

    public List<BmsDispatchData> findCalcData(List<BmsCalcTermsParams> includeParams, List<BmsCalcTermsParams> excludeParams) {
        List<BmsDispatchData> rsList = Lists.newArrayList();
        String includeSql = BmsCalculateUtils.toWhereSql(includeParams);
        if (StringUtils.isBlank(includeSql)) {
            return rsList;
        }
        String excludeSql = BmsCalculateUtils.toWhereSql(excludeParams);
        List<BmsDispatchData> includeData = mapper.findCalcData(includeSql);
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