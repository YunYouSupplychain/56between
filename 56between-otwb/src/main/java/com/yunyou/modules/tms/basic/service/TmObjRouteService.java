package com.yunyou.modules.tms.basic.service;

import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.bms.basic.entity.BmsCarrierRoute;
import com.yunyou.modules.bms.basic.service.BmsCarrierRouteService;
import com.yunyou.modules.sys.entity.Area;
import com.yunyou.modules.sys.service.AreaService;
import com.yunyou.modules.tms.basic.entity.TmCarrierRouteRelation;
import com.yunyou.modules.tms.basic.entity.TmObjRoute;
import com.yunyou.modules.tms.basic.entity.TmTransportObj;
import com.yunyou.modules.tms.basic.entity.extend.TmObjRouteEntity;
import com.yunyou.modules.tms.basic.mapper.TmObjRouteMapper;
import com.yunyou.modules.tms.common.TmsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * 业务对象路由Service
 */
@Service
@Transactional(readOnly = true)
public class TmObjRouteService extends CrudService<TmObjRouteMapper, TmObjRoute> {
    @Autowired
    private TmCarrierRouteRelationService tmCarrierRouteRelationService;
    @Autowired
    private TmTransportObjService tmTransportObjService;
    @Autowired
    private BmsCarrierRouteService bmsCarrierRouteService;
    @Autowired
    private AreaService areaService;

    public TmObjRoute getByCode(String carrierCode, String startObjCode, String endObjCode, String orgId) {
        return mapper.getByCode(carrierCode, startObjCode, endObjCode, orgId);
    }

    public TmObjRouteEntity getEntity(String id) {
        return mapper.getEntity(id);
    }

    public Page<TmObjRoute> findPage(Page<TmObjRoute> page, TmObjRouteEntity entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    @Transactional
    public void audit(TmObjRoute tmObjRoute) {
        tmObjRoute.setAuditStatus("99");
        super.save(tmObjRoute);

        TmTransportObj startObj = tmTransportObjService.getByCode(tmObjRoute.getStartObjCode(), tmObjRoute.getOrgId());
        TmTransportObj endObj = tmTransportObjService.getByCode(tmObjRoute.getEndObjCode(), tmObjRoute.getOrgId());
        if (startObj == null) {
            throw new TmsException("起点对象不存在");
        }
        if (endObj == null) {
            throw new TmsException("终点对象不存在");
        }
        Area startArea = areaService.get(startObj.getAreaId());
        if (startArea == null) {
            throw new TmsException("起点对象所属城市不存在");
        }
        Area endArea = areaService.get(endObj.getAreaId());
        if (endArea == null) {
            throw new TmsException("终点对象所属城市不存在");
        }
        String routeCode = startArea.getCode() + "-" + endArea.getCode();
        String routeName = startArea.getName() + "-" + endArea.getName();

        List<TmCarrierRouteRelation> list = tmCarrierRouteRelationService.findList(new TmCarrierRouteRelation(tmObjRoute.getCarrierCode(), startObj.getAreaId(), endObj.getAreaId(), tmObjRoute.getOrgId()));
        TmCarrierRouteRelation tmCarrierRouteRelation;
        if (CollectionUtil.isNotEmpty(list)) {
            tmCarrierRouteRelation = list.get(0);
        } else {
            tmCarrierRouteRelation = new TmCarrierRouteRelation();
        }
        tmCarrierRouteRelation.setCarrierCode(tmObjRoute.getCarrierCode());
        tmCarrierRouteRelation.setCode(routeCode);
        tmCarrierRouteRelation.setName(routeName);
        tmCarrierRouteRelation.setOriginId(startObj.getAreaId());
        tmCarrierRouteRelation.setDestinationId(endObj.getAreaId());
        tmCarrierRouteRelation.setMileage(tmObjRoute.getMileage());
        tmCarrierRouteRelation.setOrgId(tmObjRoute.getOrgId());
        tmCarrierRouteRelationService.save(tmCarrierRouteRelation);

        BmsCarrierRoute bmsCarrierRoute = bmsCarrierRouteService.getByStartAndEndAreaId(tmObjRoute.getCarrierCode(), startObj.getAreaId(), endObj.getAreaId(), tmObjRoute.getOrgId());
        if (bmsCarrierRoute==null){
            bmsCarrierRoute = new BmsCarrierRoute();
        }
        bmsCarrierRoute.setCarrierCode(tmObjRoute.getCarrierCode());
        bmsCarrierRoute.setRouteCode(routeCode);
        bmsCarrierRoute.setRouteName(routeName);
        bmsCarrierRoute.setStartAreaId(startObj.getAreaId());
        bmsCarrierRoute.setEndAreaId(endObj.getAreaId());
        if(tmObjRoute.getMileage()!=null) {
            bmsCarrierRoute.setMileage(BigDecimal.valueOf(tmObjRoute.getMileage()));
        }
        bmsCarrierRoute.setOrgId(tmObjRoute.getOrgId());
        bmsCarrierRouteService.save(bmsCarrierRoute);
    }

    @Transactional
    public void cancelAudit(TmObjRoute tmObjRoute) {
        tmObjRoute.setAuditStatus("00");
        super.save(tmObjRoute);
    }
}
