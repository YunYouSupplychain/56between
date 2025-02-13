package com.yunyou.modules.bms.business.service;

import com.yunyou.common.config.GlobalDataRule;
import com.yunyou.common.exception.GlobalException;
import com.yunyou.common.utils.IdGen;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.DataEntity;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.bms.business.entity.BmsDispatchOrderData;
import com.yunyou.modules.bms.business.entity.BmsDispatchOrderSiteData;
import com.yunyou.modules.bms.business.entity.excel.BmsDispatchOrderDataExcel;
import com.yunyou.modules.bms.business.entity.extend.BmsDispatchOrderDataEntity;
import com.yunyou.modules.bms.business.mapper.BmsDispatchOrderDataMapper;
import com.yunyou.modules.bms.business.mapper.BmsDispatchOrderSiteDataMapper;
import com.yunyou.modules.bms.calculate.BmsCalculateUtils;
import com.yunyou.modules.bms.common.BmsConstants;
import com.yunyou.modules.bms.finance.entity.extend.BmsCalcTermsParams;
import com.yunyou.modules.bms.interactive.BmsPullDataCondition;
import com.yunyou.modules.sys.entity.Area;
import com.yunyou.modules.sys.service.AreaService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 描述：派车单数据Service
 */
@Service
@Transactional(readOnly = true)
public class BmsDispatchOrderDataService extends CrudService<BmsDispatchOrderDataMapper, BmsDispatchOrderData> {
    @Autowired
    private BmsDispatchOrderSiteDataMapper bmsDispatchOrderSiteDataMapper;
    @Autowired
    private AreaService areaService;

    public Page<BmsDispatchOrderData> findPage(Page<BmsDispatchOrderData> page, BmsDispatchOrderDataEntity entity) {
        entity.setDataScope(GlobalDataRule.getDataRuleSql("a.org_id", entity.getOrgId()));
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    public BmsDispatchOrderDataEntity getEntity(String id) {
        return mapper.getEntity(id);
    }

    public Page<BmsDispatchOrderSiteData> findSitePage(Page<BmsDispatchOrderSiteData> page, BmsDispatchOrderSiteData bmsDispatchOrderSiteData) {
        dataRuleFilter(bmsDispatchOrderSiteData);
        bmsDispatchOrderSiteData.setPage(page);
        page.setList(bmsDispatchOrderSiteDataMapper.findList(bmsDispatchOrderSiteData));
        return page;
    }

    public List<BmsDispatchOrderSiteData> findSiteData(String headerId, String orgId) {
        return bmsDispatchOrderSiteDataMapper.findByHeaderId(headerId, orgId);
    }

    @Transactional
    public void batchInsert(List<BmsDispatchOrderData> data) {
        for (BmsDispatchOrderData bmsDispatchOrderData : data) {
            this.save(bmsDispatchOrderData);
            for (BmsDispatchOrderSiteData bmsDispatchOrderSiteData : bmsDispatchOrderData.getSiteList()) {
                bmsDispatchOrderSiteData.setId(IdGen.uuid());
                bmsDispatchOrderSiteData.preInsert();
                bmsDispatchOrderSiteData.setHeaderId(bmsDispatchOrderData.getId());
                bmsDispatchOrderSiteData.setOrderNo(bmsDispatchOrderData.getOrderNo());
                bmsDispatchOrderSiteData.setOrgId(bmsDispatchOrderData.getOrgId());
                bmsDispatchOrderSiteData.setDataSources(bmsDispatchOrderData.getDataSources());
                bmsDispatchOrderSiteDataMapper.insert(bmsDispatchOrderSiteData);
            }
        }
    }

    @Transactional
    public void deleteByCondition(BmsPullDataCondition condition) {
        bmsDispatchOrderSiteDataMapper.deleteByCondition(condition);
        mapper.deleteByCondition(condition);
    }

    @Transactional
    public void importFile(List<BmsDispatchOrderDataExcel> importList, String orgId) {
        if (CollectionUtil.isEmpty(importList)) {
            return;
        }
        Map<String, String> checkMap = Maps.newHashMap();
        int size = importList.size();
        for (int i = 0; i < size; i++) {
            BmsDispatchOrderDataExcel data = importList.get(i);
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
            if (StringUtils.isBlank(data.getDriverCode())) {
                throw new GlobalException("第" + (i + 1) + "行，司机不能为空！");
            }
            if (StringUtils.isBlank(data.getDispatchSeq())) {
                throw new GlobalException("第" + (i + 1) + "行，配送顺序不能为空！");
            }
            if (checkMap.containsKey(data.getOrderNo() + data.getDispatchSeq())) {
                throw new GlobalException("第" + (i + 1) + "行，订单号" + data.getOrderNo() + "配送顺序重复！");
            } else {
                checkMap.put(data.getOrderNo() + data.getDispatchSeq(), data.getOrderNo() + data.getDispatchSeq());
            }
            if (StringUtils.isBlank(data.getAreaCode())) {
                throw new GlobalException("第" + (i + 1) + "行，网点所属城市编码不能为空！");
            }
            Area area = areaService.getByCode(data.getAreaCode());
            if (area == null) {
                throw new GlobalException("第" + (i + 1) + "行，网点所属城市编码不存在！");
            }
            data.setAreaId(area.getId());
        }
        List<BmsDispatchOrderData> rsList = Lists.newArrayList();
        Collection<List<BmsDispatchOrderDataExcel>> values = importList.stream().collect(Collectors.groupingBy(BmsDispatchOrderDataExcel::getOrderNo)).values();
        for (List<BmsDispatchOrderDataExcel> list : values) {
            BmsDispatchOrderData data = new BmsDispatchOrderData();
            BeanUtils.copyProperties(list.get(0), data);

            if (StringUtils.isBlank(data.getOrderType())) {
                data.setOrderType("1");
            }
            if (StringUtils.isBlank(data.getTranType())) {
                data.setTranType("3");
            }
            List<BmsDispatchOrderSiteData> siteDataList = list.stream().map(o -> {
                BmsDispatchOrderSiteData siteData = new BmsDispatchOrderSiteData();
                siteData.setDispatchSeq(o.getDispatchSeq());
                siteData.setOutletCode(o.getOutletCode());
                siteData.setOutletName(o.getOutletName());
                siteData.setAreaId(o.getAreaId());
                return siteData;
            }).collect(Collectors.toList());
            data.setSiteList(siteDataList);
            data.setDataSources(BmsConstants.BUSINESS_DATA_SOURCE_IMPORT);
            data.setIsFee(BmsConstants.YES);
            data.setOrgId(orgId);
            rsList.add(data);
        }
        this.batchInsert(rsList);
    }

    public List<BmsDispatchOrderData> findCalcData(List<BmsCalcTermsParams> includeParams, List<BmsCalcTermsParams> excludeParams) {
        List<BmsDispatchOrderData> rsList = Lists.newArrayList();
        String includeSql = BmsCalculateUtils.toWhereSql(includeParams);
        if (StringUtils.isBlank(includeSql)) {
            return rsList;
        }
        String excludeSql = BmsCalculateUtils.toWhereSql(excludeParams);
        List<BmsDispatchOrderData> includeData = mapper.findCalcData(includeSql);
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
