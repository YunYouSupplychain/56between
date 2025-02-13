package com.yunyou.modules.sys.common.service;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.sys.common.entity.SysTmsBusinessRoute;
import com.yunyou.modules.sys.common.entity.SysTmsTransportObj;
import com.yunyou.modules.sys.common.entity.extend.SysTmsTransportObjEntity;
import com.yunyou.modules.sys.common.mapper.SysTmsTransportObjMapper;
import com.yunyou.modules.sys.service.AreaService;
import com.yunyou.modules.tms.common.TmsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 业务对象信息Service
 */
@Service
@Transactional(readOnly = true)
public class SysTmsTransportObjService extends CrudService<SysTmsTransportObjMapper, SysTmsTransportObj> {
    @Autowired
    private SysTmsBusinessRouteService sysTmsBusinessRouteService;
    @Autowired
    private AreaService areaService;

    @SuppressWarnings("unchecked")
    public Page<SysTmsTransportObjEntity> findPage(Page page, SysTmsTransportObjEntity entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        List<SysTmsTransportObjEntity> list = mapper.findPage(entity);
        list.forEach(o -> {
            o.setArea(areaService.getFullName(o.getAreaId()));
        });
        page.setList(list);
        return page;
    }

    @SuppressWarnings("unchecked")
    public Page<SysTmsTransportObjEntity> findGrid(Page page, SysTmsTransportObjEntity entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        List<SysTmsTransportObjEntity> list = mapper.findGrid(entity);
        list.forEach(o -> {
            o.setArea(areaService.getFullName(o.getAreaId()));
        });
        page.setList(list);
        return page;
    }

    @SuppressWarnings("unchecked")
    public Page<SysTmsTransportObjEntity> findSettleGrid(Page page, SysTmsTransportObjEntity entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findSettleGrid(entity));
        return page;
    }

    public SysTmsTransportObjEntity getEntity(String id) {
        SysTmsTransportObjEntity entity = mapper.getEntity(new SysTmsTransportObj(id));
        if (entity != null) {
            entity.setArea(areaService.getFullName(entity.getAreaId()));
            if (StringUtils.isNotBlank(entity.getRouteId())) {
                SysTmsBusinessRoute sysTmsBusinessRoute = sysTmsBusinessRouteService.getByCode(entity.getRouteId(), entity.getDataSet());
                if (sysTmsBusinessRoute != null) {
                    entity.setRoute(sysTmsBusinessRoute.getName());
                }
            }
        }
        return entity;
    }

    public SysTmsTransportObjEntity getEntity(String sysTmsTransportObjCode, String dataSet) {
        SysTmsTransportObjEntity entity = mapper.getEntity(new SysTmsTransportObj(sysTmsTransportObjCode, dataSet));
        if (entity != null) {
            entity.setArea(areaService.getFullName(entity.getAreaId()));
            if (StringUtils.isNotBlank(entity.getRouteId())) {
                SysTmsBusinessRoute sysTmsBusinessRoute = sysTmsBusinessRouteService.getByCode(entity.getRouteId(), entity.getDataSet());
                if (sysTmsBusinessRoute != null) {
                    entity.setRoute(sysTmsBusinessRoute.getName());
                }
            }
        }
        return entity;
    }

    public SysTmsTransportObj getByCode(String sysTmsTransportObjCode, String dataSet) {
        return mapper.getEntity(new SysTmsTransportObj(sysTmsTransportObjCode, dataSet));
    }

    public void saveValidator(SysTmsTransportObj sysTmsTransportObj) {
        if (StringUtils.isBlank(sysTmsTransportObj.getTransportObjCode())) {
            throw new TmsException("业务对象编码不能为空");
        }
        if (StringUtils.isBlank(sysTmsTransportObj.getTransportObjName())) {
            throw new TmsException("业务对象名称不能为空");
        }
        if (StringUtils.isBlank(sysTmsTransportObj.getTransportObjType())) {
            throw new TmsException("业务对象类型不能为空");
        }
        List<SysTmsTransportObj> list = findList(new SysTmsTransportObj(sysTmsTransportObj.getTransportObjCode(), sysTmsTransportObj.getDataSet()));
        if (CollectionUtil.isNotEmpty(list)) {
            if (list.stream().anyMatch(o -> !o.getId().equals(sysTmsTransportObj.getId()))) {
                throw new TmsException("业务对象[" + sysTmsTransportObj.getTransportObjCode() + "]已存在");
            }
        }
    }

    @Transactional
    public void remove(String customerNo, String dataSet) {
        mapper.remove(customerNo, dataSet);
    }

    @Transactional
    public void batchInsert(List<SysTmsTransportObj> list) {
        if (CollectionUtil.isEmpty(list)) return;
        for (int i = 0; i < list.size(); i += 999) {
            if (list.size() - i < 999) {
                mapper.batchInsert(list.subList(i, list.size()));
            } else {
                mapper.batchInsert(list.subList(i, i + 999));
            }
        }
    }
}