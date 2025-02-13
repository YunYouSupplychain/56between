package com.yunyou.modules.tms.basic.service;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.sys.entity.Area;
import com.yunyou.modules.sys.entity.Office;
import com.yunyou.modules.sys.service.AreaService;
import com.yunyou.modules.sys.utils.UserUtils;
import com.yunyou.modules.tms.basic.entity.TmCarrierRouteRelation;
import com.yunyou.modules.tms.basic.entity.extend.TmCarrierRouteRelationEntity;
import com.yunyou.modules.tms.basic.mapper.TmCarrierRouteRelationMapper;
import com.yunyou.modules.tms.common.TmsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 承运商路由信息Service
 *
 * @author liujianhua
 * @version 2020-02-20
 */
@Service
@Transactional(readOnly = true)
public class TmCarrierRouteRelationService extends CrudService<TmCarrierRouteRelationMapper, TmCarrierRouteRelation> {
    @Autowired
    private AreaService areaService;

    public void saveValidator(TmCarrierRouteRelation tmCarrierRouteRelation) {
        if (StringUtils.isBlank(tmCarrierRouteRelation.getOriginId())) {
            throw new TmsException("起始地不能为空");
        }
        if (StringUtils.isBlank(tmCarrierRouteRelation.getDestinationId())) {
            throw new TmsException("目的地不能为空");
        }
        if (tmCarrierRouteRelation.getOriginId().equals(tmCarrierRouteRelation.getDestinationId())) {
            throw new TmsException("起始地/目的地不能相同");
        }
        Office organizationCenter = UserUtils.getOrgCenter(tmCarrierRouteRelation.getOrgId());
        if (StringUtils.isBlank(organizationCenter.getId())) {
            throw new TmsException("找不到归属组织中心，不能操作");
        }
        List<TmCarrierRouteRelation> list = findList(new TmCarrierRouteRelation(tmCarrierRouteRelation.getCode(), tmCarrierRouteRelation.getOrgId()));
        if (CollectionUtil.isNotEmpty(list)) {
            if (list.stream().anyMatch(o -> !o.getId().equals(tmCarrierRouteRelation.getId()))) {
                throw new TmsException("编码[" + tmCarrierRouteRelation.getCode() + "]已存在");
            }
        }
        List<TmCarrierRouteRelation> list1 = findList(new TmCarrierRouteRelation(tmCarrierRouteRelation.getCarrierCode(), tmCarrierRouteRelation.getOriginId(), tmCarrierRouteRelation.getDestinationId(), tmCarrierRouteRelation.getOrgId()));
        if (CollectionUtil.isNotEmpty(list1)) {
            long count = list1.stream().filter(o -> !o.getId().equals(tmCarrierRouteRelation.getId())).count();
            if (count > 0) {
                throw new TmsException("承运商路由已存在");
            }
        }
        Area startArea = areaService.get(tmCarrierRouteRelation.getOriginId());
        if (startArea == null) {
            throw new TmsException("起始地不存在");
        }
        Area endArea = areaService.get(tmCarrierRouteRelation.getDestinationId());
        if (endArea == null) {
            throw new TmsException("目的地不存在");
        }
    }

    public TmCarrierRouteRelationEntity getEntity(String id) {
        TmCarrierRouteRelationEntity entity = mapper.getEntity(id);
        if (entity != null) {
            entity.setOrigin(areaService.getFullName(entity.getOriginId()));
            entity.setDestination(areaService.getFullName(entity.getDestinationId()));
        }
        return entity;
    }

    public Page<TmCarrierRouteRelationEntity> findPage(Page page, TmCarrierRouteRelation tmCarrierRouteRelation) {
        dataRuleFilter(tmCarrierRouteRelation);
        tmCarrierRouteRelation.setPage(page);
        List<TmCarrierRouteRelationEntity> list = mapper.findPage(tmCarrierRouteRelation);
        for (TmCarrierRouteRelationEntity entity : list) {
            entity.setOrigin(areaService.getFullName(entity.getOriginId()));
            entity.setDestination(areaService.getFullName(entity.getDestinationId()));
        }
        page.setList(list);
        return page;
    }

    public Page<TmCarrierRouteRelationEntity> findGrid(Page page, TmCarrierRouteRelationEntity entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        List<TmCarrierRouteRelationEntity> list = mapper.findGrid(entity);
        for (TmCarrierRouteRelationEntity o : list) {
            o.setOrigin(areaService.getFullName(o.getOriginId()));
            o.setDestination(areaService.getFullName(o.getDestinationId()));
        }
        page.setList(list);
        return page;
    }

    public TmCarrierRouteRelation getByCode(String carrierCode, String code, String orgId) {
        return mapper.getByCode(carrierCode, code, orgId);
    }

    @Transactional
    public void remove(String carrierCode, String code, String orgId) {
        mapper.remove(carrierCode, code, orgId);
    }
}