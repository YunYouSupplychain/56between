package com.yunyou.modules.sys.common.service;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.bms.common.BmsException;
import com.yunyou.modules.sys.common.entity.SysBmsRoute;
import com.yunyou.modules.sys.common.entity.extend.SysBmsRouteTemplate;
import com.yunyou.modules.sys.common.mapper.SysBmsRouteMapper;
import com.yunyou.modules.sys.entity.Area;
import com.yunyou.modules.sys.service.AreaService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 路由Service
 */
@Service
@Transactional(readOnly = true)
public class SysBmsRouteService extends CrudService<SysBmsRouteMapper, SysBmsRoute> {
    @Autowired
    private AreaService areaService;

    public Page<SysBmsRoute> findPage(Page<SysBmsRoute> page, SysBmsRoute entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    public SysBmsRoute getByCode(String routeCode, String dataSet) {
        return mapper.getByCode(routeCode, dataSet);
    }

    public SysBmsRoute getByStartAndEndAreaId(String startAreaId, String endAreaId, String dataSet) {
        return mapper.getByStartAndEndAreaId(startAreaId, endAreaId, dataSet);
    }

    public SysBmsRoute getByStartAndEndAreaCode(String startAreaCode, String endAreaCode, String dataSet) {
        return mapper.getByStartAndEndAreaCode(startAreaCode, endAreaCode, dataSet);
    }

    public void checkSaveBefore(SysBmsRoute sysBmsRoute) {
        if (StringUtils.isBlank(sysBmsRoute.getStartAreaId())) {
            throw new BmsException("起始地不能为空");
        }
        if (StringUtils.isBlank(sysBmsRoute.getEndAreaId())) {
            throw new BmsException("目的地不能为空");
        }
        if (sysBmsRoute.getStartAreaId().equals(sysBmsRoute.getEndAreaId())) {
            throw new BmsException("起始地/目的地不能相同");
        }
        SysBmsRoute route = this.getByStartAndEndAreaId(sysBmsRoute.getStartAreaId(), sysBmsRoute.getEndAreaId(), sysBmsRoute.getDataSet());
        if (route != null && !route.getId().equals(sysBmsRoute.getId())) {
            throw new BmsException("路由已存在");
        }
        Area startArea = areaService.get(sysBmsRoute.getStartAreaId());
        if (startArea == null) {
            throw new BmsException("起始地不存在");
        }
        sysBmsRoute.setStartAreaCode(startArea.getCode());
        Area endArea = areaService.get(sysBmsRoute.getEndAreaId());
        if (endArea == null) {
            throw new BmsException("目的地不存在");
        }
        sysBmsRoute.setEndAreaCode(endArea.getCode());
    }

    @Override
    @Transactional
    public void save(SysBmsRoute entity) {
        this.checkSaveBefore(entity);
        if (StringUtils.isBlank(entity.getRouteCode())) {
            entity.setRouteCode(entity.getStartAreaCode() + "-" + entity.getEndAreaCode());
        }
        super.save(entity);
    }

    @Override
    @Transactional
    public void delete(SysBmsRoute entity) {
        super.delete(entity);
    }

    /**
     * 描述：导入
     *
     * @author liujianhua created on 2019-11-20
     */
    @Transactional
    public void importFile(SysBmsRouteTemplate data) {
        if (StringUtils.isBlank(data.getRouteName())) {
            throw new BmsException("路由名称为空");
        }
        if (StringUtils.isBlank(data.getStartAreaCode())) {
            throw new BmsException("起始地编码为空");
        }
        if (StringUtils.isBlank(data.getStartAreaName())) {
            throw new BmsException("起始地名称为空");
        }
        if (StringUtils.isBlank(data.getEndAreaCode())) {
            throw new BmsException("目的地编码为空");
        }
        if (StringUtils.isBlank(data.getEndAreaName())) {
            throw new BmsException("目的地名称为空");
        }
        if (data.getMileage() == null) {
            throw new BmsException("标准里程为空");
        }
        if (data.getStartAreaCode().equals(data.getEndAreaCode())) {
            throw new BmsException("起始地[" + data.getStartAreaCode() + "]/目的地[" + data.getEndAreaCode() + "]不能相同");
        }
        if (StringUtils.isBlank(data.getDataSet())) {
            throw new BmsException("数据套为空");
        }
        SysBmsRoute route = this.getByStartAndEndAreaCode(data.getStartAreaCode(), data.getEndAreaCode(), data.getDataSet());
        if (route != null) {
            throw new BmsException("[" + data.getStartAreaCode() + "] - [" + data.getEndAreaCode() + "]路由已存在");
        }

        SysBmsRoute sysBmsRoute = new SysBmsRoute();
        BeanUtils.copyProperties(data, sysBmsRoute);
        Area startArea = areaService.getByCode(data.getStartAreaCode());
        if (startArea == null) {
            throw new BmsException("起始地[" + data.getStartAreaCode() + "]不存在");
        } else {
            sysBmsRoute.setStartAreaId(startArea.getId());
            sysBmsRoute.setStartAreaName(startArea.getName());
        }
        Area endArea = areaService.getByCode(data.getEndAreaCode());
        if (endArea == null) {
            throw new BmsException("目的地[" + data.getEndAreaCode() + "]不存在");
        } else {
            sysBmsRoute.setEndAreaId(endArea.getId());
            sysBmsRoute.setEndAreaName(endArea.getName());
        }
        this.save(sysBmsRoute);
    }

}