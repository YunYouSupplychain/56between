package com.yunyou.modules.bms.basic.service;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.bms.basic.entity.BmsCarrierRoute;
import com.yunyou.modules.bms.basic.entity.extend.BmsCarrierRouteEntity;
import com.yunyou.modules.bms.basic.entity.template.BmsCarrierRouteTemplate;
import com.yunyou.modules.bms.basic.mapper.BmsCarrierRouteMapper;
import com.yunyou.modules.bms.common.BmsException;
import com.yunyou.modules.sys.entity.Area;
import com.yunyou.modules.sys.service.AreaService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 承运商路由Service
 *
 * @author zqs
 * @version 2018-06-15
 */
@Service
@Transactional(readOnly = true)
public class BmsCarrierRouteService extends CrudService<BmsCarrierRouteMapper, BmsCarrierRoute> {
    @Autowired
    private AreaService areaService;

    public Page<BmsCarrierRoute> findPage(Page<BmsCarrierRoute> page, BmsCarrierRouteEntity entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    public Page<BmsCarrierRoute> findGrid(Page<BmsCarrierRoute> page, BmsCarrierRouteEntity entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findGrid(entity));
        return page;
    }

    @Override
    @Transactional
    public void save(BmsCarrierRoute bmsCarrierRoute) {
        super.save(bmsCarrierRoute);
    }

    public BmsCarrierRouteEntity getEntity(String id) {
        return mapper.getEntity(id);
    }

    public BmsCarrierRoute getByCode(String carrierCode, String routeCode, String orgId) {
        return mapper.getByCode(carrierCode, routeCode, orgId);
    }

    public BmsCarrierRoute getByStartAndEndAreaId(String carrierCode, String startAreaId, String endAreaId, String orgId) {
        return mapper.getByStartAndEndAreaId(carrierCode, startAreaId, endAreaId, orgId);
    }

    public void checkSaveBefore(BmsCarrierRoute bmsCarrierRoute) {
        if (StringUtils.isBlank(bmsCarrierRoute.getCarrierCode())) {
            throw new BmsException("承运商不能为空");
        }
        if (StringUtils.isBlank(bmsCarrierRoute.getStartAreaId())) {
            throw new BmsException("起始地不能为空");
        }
        if (StringUtils.isBlank(bmsCarrierRoute.getEndAreaId())) {
            throw new BmsException("目的地不能为空");
        }
        if (bmsCarrierRoute.getStartAreaId().equals(bmsCarrierRoute.getEndAreaId())) {
            throw new BmsException("起始地/目的地不能相同");
        }
        BmsCarrierRoute route = this.getByStartAndEndAreaId(bmsCarrierRoute.getCarrierCode(), bmsCarrierRoute.getStartAreaId(), bmsCarrierRoute.getEndAreaId(), bmsCarrierRoute.getOrgId());
        if (route != null && !route.getId().equals(bmsCarrierRoute.getId())) {
            throw new BmsException("路由已存在");
        }
        Area startArea = areaService.get(bmsCarrierRoute.getStartAreaId());
        if (startArea == null) {
            throw new BmsException("起始地不存在");
        }
        Area endArea = areaService.get(bmsCarrierRoute.getEndAreaId());
        if (endArea == null) {
            throw new BmsException("目的地不存在");
        }
        if (StringUtils.isBlank(bmsCarrierRoute.getRouteCode())) {
            bmsCarrierRoute.setRouteCode(startArea.getCode() + "-" + endArea.getCode());
        }
    }

    @Transactional
    public void saveEntity(BmsCarrierRoute entity) {
        this.checkSaveBefore(entity);
        this.save(entity);
    }

    /**
     * 描述：导入
     */
    @Transactional
    public void importFile(BmsCarrierRouteTemplate data, String orgId) {
        if (StringUtils.isBlank(data.getCarrierCode())) {
            throw new BmsException("承运商编码为空");
        }
        if (StringUtils.isBlank(data.getRouteName())) {
            throw new BmsException("路由名称为空");
        }
        if (StringUtils.isBlank(data.getStartAreaCode())) {
            throw new BmsException("起始地编码为空");
        }
        if (StringUtils.isBlank(data.getEndAreaCode())) {
            throw new BmsException("目的地编码为空");
        }
        if (data.getMileage() == null) {
            throw new BmsException("标准里程为空");
        }
        if (data.getStartAreaCode().equals(data.getEndAreaCode())) {
            throw new BmsException("起始地[" + data.getStartAreaCode() + "]/目的地[" + data.getEndAreaCode() + "]不能相同");
        }
        Area startArea = areaService.getByCode(data.getStartAreaCode());
        if (startArea == null) {
            throw new BmsException("起始地[" + data.getStartAreaCode() + "]不存在");
        }
        Area endArea = areaService.getByCode(data.getEndAreaCode());
        if (endArea == null) {
            throw new BmsException("目的地[" + data.getEndAreaCode() + "]不存在");
        }
        BmsCarrierRoute route = this.getByStartAndEndAreaId(data.getCarrierCode(), startArea.getId(), endArea.getId(), orgId);
        if (route != null) {
            throw new BmsException("承运商[" + data.getCarrierCode() + "]路由[" + data.getStartAreaCode() + "] - [" + data.getEndAreaCode() + "]已存在");
        }

        BmsCarrierRoute bmsCarrierRoute = new BmsCarrierRoute();
        BeanUtils.copyProperties(data, bmsCarrierRoute);
        bmsCarrierRoute.setStartAreaId(startArea.getId());
        bmsCarrierRoute.setEndAreaId(endArea.getId());
        bmsCarrierRoute.setOrgId(orgId);
        this.save(bmsCarrierRoute);
    }

    @Transactional
    public void remove(String carrierCode, String routeCode, String orgId) {
        mapper.remove(carrierCode, routeCode, orgId);
    }
}