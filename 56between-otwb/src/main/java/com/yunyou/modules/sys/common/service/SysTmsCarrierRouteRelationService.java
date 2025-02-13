package com.yunyou.modules.sys.common.service;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.interfaces.interactive.action.SyncPlatformDataToBmsAction;
import com.yunyou.modules.interfaces.interactive.action.SyncPlatformDataToTmsAction;
import com.yunyou.modules.sys.SysParamConstants;
import com.yunyou.modules.sys.common.entity.SysTmsCarrierRouteRelation;
import com.yunyou.modules.sys.common.entity.extend.SysTmsCarrierRouteRelationEntity;
import com.yunyou.modules.sys.common.mapper.SysTmsCarrierRouteRelationMapper;
import com.yunyou.modules.sys.entity.Area;
import com.yunyou.modules.sys.service.AreaService;
import com.yunyou.modules.sys.utils.SysControlParamsUtils;
import com.yunyou.modules.tms.common.TmsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 承运商路由信息Service
 */
@Service
@Transactional(readOnly = true)
public class SysTmsCarrierRouteRelationService extends CrudService<SysTmsCarrierRouteRelationMapper, SysTmsCarrierRouteRelation> {
    @Autowired
    private AreaService areaService;
    @Autowired
    @Lazy
    private SyncPlatformDataToTmsAction syncPlatformDataToTmsAction;
    @Autowired
    @Lazy
    private SyncPlatformDataToBmsAction syncPlatformDataToBmsAction;

    @SuppressWarnings("unchecked")
    public Page<SysTmsCarrierRouteRelationEntity> findPage(Page page, SysTmsCarrierRouteRelationEntity sysTmsCarrierRouteRelationEntity) {
        dataRuleFilter(sysTmsCarrierRouteRelationEntity);
        sysTmsCarrierRouteRelationEntity.setPage(page);
        List<SysTmsCarrierRouteRelationEntity> list = mapper.findPage(sysTmsCarrierRouteRelationEntity);
        for (SysTmsCarrierRouteRelationEntity entity : list) {
            entity.setOrigin(areaService.getFullName(entity.getOriginId()));
            entity.setDestination(areaService.getFullName(entity.getDestinationId()));
        }
        page.setList(list);
        return page;
    }

    @SuppressWarnings("unchecked")
    public Page<SysTmsCarrierRouteRelationEntity> findGrid(Page page, SysTmsCarrierRouteRelationEntity entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        List<SysTmsCarrierRouteRelationEntity> list = mapper.findGrid(entity);
        for (SysTmsCarrierRouteRelationEntity o : list) {
            o.setOrigin(areaService.getFullName(o.getOriginId()));
            o.setDestination(areaService.getFullName(o.getDestinationId()));
        }
        page.setList(list);
        return page;
    }

    public SysTmsCarrierRouteRelationEntity getEntity(String id) {
        SysTmsCarrierRouteRelationEntity entity = mapper.getEntity(id);
        if (entity != null) {
            entity.setOrigin(areaService.getFullName(entity.getOriginId()));
            entity.setDestination(areaService.getFullName(entity.getDestinationId()));
        }
        return entity;
    }

    public void saveValidator(SysTmsCarrierRouteRelation sysTmsCarrierRouteRelation) {
        if (StringUtils.isBlank(sysTmsCarrierRouteRelation.getOriginId())) {
            throw new TmsException("起始地不能为空");
        }
        if (StringUtils.isBlank(sysTmsCarrierRouteRelation.getDestinationId())) {
            throw new TmsException("目的地不能为空");
        }
        if (sysTmsCarrierRouteRelation.getOriginId().equals(sysTmsCarrierRouteRelation.getDestinationId())) {
            throw new TmsException("起始地/目的地不能相同");
        }
        List<SysTmsCarrierRouteRelation> list = findList(new SysTmsCarrierRouteRelation(sysTmsCarrierRouteRelation.getCode(), sysTmsCarrierRouteRelation.getDataSet()));
        if (CollectionUtil.isNotEmpty(list)) {
            if (list.stream().anyMatch(o -> !o.getId().equals(sysTmsCarrierRouteRelation.getId()))) {
                throw new TmsException("编码[" + sysTmsCarrierRouteRelation.getCode() + "]已存在");
            }
        }
        List<SysTmsCarrierRouteRelation> list1 = findList(new SysTmsCarrierRouteRelation(sysTmsCarrierRouteRelation.getCarrierCode(), sysTmsCarrierRouteRelation.getOriginId(), sysTmsCarrierRouteRelation.getDestinationId(), sysTmsCarrierRouteRelation.getDataSet()));
        if (CollectionUtil.isNotEmpty(list1)) {
            long count = list1.stream().filter(o -> !o.getId().equals(sysTmsCarrierRouteRelation.getId())).count();
            if (count > 0) {
                throw new TmsException("承运商路由已存在");
            }
        }
        Area startArea = areaService.get(sysTmsCarrierRouteRelation.getOriginId());
        if (startArea == null) {
            throw new TmsException("起始地不存在");
        }
        Area endArea = areaService.get(sysTmsCarrierRouteRelation.getDestinationId());
        if (endArea == null) {
            throw new TmsException("目的地不存在");
        }
    }

    @Override
    @Transactional
    public void save(SysTmsCarrierRouteRelation entity) {
        super.save(entity);
        final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
        if ("Y".equals(IS_SAVING_PUSH)) {
            syncPlatformDataToTmsAction.sync(entity);
            syncPlatformDataToBmsAction.sync(entity);
        }
    }

    @Override
    @Transactional
    public void delete(SysTmsCarrierRouteRelation entity) {
        super.delete(entity);
        final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
        if ("Y".equals(IS_SAVING_PUSH)) {
            syncPlatformDataToTmsAction.removeCarrierRouteRelation(entity.getCarrierCode(), entity.getCode(), entity.getDataSet());
            syncPlatformDataToBmsAction.removeRoute(entity.getCarrierCode(), entity.getCode(), entity.getDataSet());
        }
    }
}