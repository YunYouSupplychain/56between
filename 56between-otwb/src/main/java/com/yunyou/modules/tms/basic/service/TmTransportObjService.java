package com.yunyou.modules.tms.basic.service;

import com.yunyou.common.enums.CustomerType;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.sys.SysParamConstants;
import com.yunyou.modules.sys.entity.Area;
import com.yunyou.modules.sys.entity.Office;
import com.yunyou.modules.sys.service.AreaService;
import com.yunyou.modules.sys.utils.SysControlParamsUtils;
import com.yunyou.modules.sys.utils.UserUtils;
import com.yunyou.modules.tms.basic.entity.TmBusinessRoute;
import com.yunyou.modules.tms.basic.entity.TmTransportObj;
import com.yunyou.modules.tms.basic.entity.excel.TmTransportObjImport;
import com.yunyou.modules.tms.basic.entity.extend.TmTransportObjEntity;
import com.yunyou.modules.tms.basic.mapper.TmTransportObjMapper;
import com.yunyou.modules.tms.common.TmsConstants;
import com.yunyou.modules.tms.common.TmsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 业务对象信息Service
 *
 * @author liujianhua
 * @version 2020-02-20
 */
@Service
@Transactional(readOnly = true)
public class TmTransportObjService extends CrudService<TmTransportObjMapper, TmTransportObj> {
    @Autowired
    private AreaService areaService;
    @Autowired
    private TmBusinessRouteService tmBusinessRouteService;

    public void saveValidator(TmTransportObj tmTransportObj) {
        if (StringUtils.isBlank(tmTransportObj.getTransportObjCode())) {
            throw new TmsException("业务对象编码不能为空");
        }
        if (StringUtils.isBlank(tmTransportObj.getTransportObjName())) {
            throw new TmsException("业务对象名称不能为空");
        }
        if (StringUtils.isBlank(tmTransportObj.getTransportObjType())) {
            throw new TmsException("业务对象类型不能为空");
        }
        Office organizationCenter = UserUtils.getOrgCenter(tmTransportObj.getOrgId());
        if (StringUtils.isBlank(organizationCenter.getId())) {
            throw new TmsException("找不到归属组织中心，不能操作");
        }
        List<TmTransportObj> list = findList(new TmTransportObj(tmTransportObj.getTransportObjCode(), tmTransportObj.getOrgId()));
        if (CollectionUtil.isNotEmpty(list)) {
            if (list.stream().anyMatch(o -> !o.getId().equals(tmTransportObj.getId()))) {
                throw new TmsException("业务对象[" + tmTransportObj.getTransportObjCode() + "]已存在");
            }
        }
    }

    public TmTransportObjEntity getEntity(String id) {
        TmTransportObjEntity entity = mapper.getEntity(new TmTransportObj(id));
        if (entity != null) {
            if (StringUtils.isNotBlank(entity.getAreaId())) {
                entity.setArea(areaService.getFullName(entity.getAreaId()));
            }
            if (StringUtils.isNotBlank(entity.getRouteId())) {
                TmBusinessRoute tmBusinessRoute = tmBusinessRouteService.getByCode(entity.getRouteId(), entity.getOrgId());
                if (tmBusinessRoute != null) {
                    entity.setRoute(tmBusinessRoute.getName());
                }
            }
        }
        return entity;
    }

    public TmTransportObj getByCode(String tmTransportObjCode, String orgId) {
        return mapper.getEntity(new TmTransportObj(tmTransportObjCode, orgId));
    }

    public TmTransportObjEntity getEntity(String tmTransportObjCode, String orgId) {
        TmTransportObjEntity entity = mapper.getEntity(new TmTransportObj(tmTransportObjCode, orgId));
        if (entity != null) {
            entity.setArea(areaService.getFullName(entity.getAreaId()));
            if (StringUtils.isNotBlank(entity.getRouteId())) {
                TmBusinessRoute tmBusinessRoute = tmBusinessRouteService.getByCode(entity.getRouteId(), entity.getOrgId());
                if (tmBusinessRoute != null) {
                    entity.setRoute(tmBusinessRoute.getName());
                }
            }
        }
        return entity;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Page<TmTransportObjEntity> findPage(Page page, TmTransportObj tmTransportObj) {
        dataRuleFilter(tmTransportObj);
        tmTransportObj.setPage(page);
        List<TmTransportObjEntity> list = mapper.findPage(tmTransportObj);
        list.forEach(o -> o.setArea(areaService.getFullName(o.getAreaId())));
        page.setList(list);
        return page;
    }

    @SuppressWarnings("unchecked")
    public Page<TmTransportObjEntity> findGrid(Page page, TmTransportObjEntity entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        List<TmTransportObjEntity> list = mapper.findGrid(entity);
        list.forEach(o -> o.setArea(areaService.getFullName(o.getAreaId())));
        page.setList(list);
        return page;
    }

    @SuppressWarnings("unchecked")
    public Page<TmTransportObjEntity> findSettleGrid(Page page, TmTransportObjEntity entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findSettleGrid(entity));
        return page;
    }

    @SuppressWarnings("unchecked")
    public Page<TmTransportObjEntity> findOutletGrid(Page page, TmTransportObjEntity entity) {
        entity.setIsCarrierServiceScope(SysControlParamsUtils.getValue(SysParamConstants.IS_CARRIER_SERVICE_SCOPE_PATTERN, entity.getOrgId()));
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findOutletGrid(entity));
        return page;
    }

    public List<TmTransportObj> findOutletMatchObjByOrgId(String orgId, String baseOrgId) {
        TmTransportObj con = new TmTransportObj();
        con.setOutletMatchedOrgId(orgId);
        con.setOrgId(baseOrgId);
        return this.findList(con);
    }

    public List<TmTransportObj> findCarrierMatchObjByOrgId(String orgId, String baseOrgId) {
        TmTransportObj con = new TmTransportObj();
        con.setCarrierMatchedOrgId(orgId);
        con.setOrgId(baseOrgId);
        return this.findList(con);
    }

    public List<TmTransportObj> findCustomerMatchObjByLine(String line, String baseOrgId) {
        TmTransportObj con = new TmTransportObj();
        con.setRouteId(line);
        con.setOrgId(baseOrgId);
        con.setTransportObjType("OWNER");
        return this.findList(con);
    }

    @Override
    @Transactional
    public void save(TmTransportObj entity) {
        if (StringUtils.isBlank(entity.getAreaId()) && entity.getTransportObjType().contains(CustomerType.OUTLET.getCode())) {
            Area area = areaService.getByCode(entity.getTransportObjCode());
            Area businessArea = areaService.getByCode(TmsConstants.BUSINESS_AREA_CODE);
            if (area == null && businessArea != null) {
                area = new Area();
                area.setParent(businessArea);
                area.setType("4");
                area.setCode(entity.getTransportObjCode());
                area.setName(entity.getTransportObjName());
                areaService.save(area);
            }
            if (area != null) {
                entity.setAreaId(area.getId());
            }
        }
        super.save(entity);
    }

    @Transactional
    public void importFile(TmTransportObjImport objImport) {

    }

    @Transactional
    public void remove(String transportObjCode, String orgId) {
        mapper.remove(transportObjCode, orgId);
    }

    @Transactional
    public void batchInsert(List<TmTransportObj> list) {
        for (int i = 0; i < list.size(); i += 999) {
            if (list.size() - i < 999) {
                mapper.batchInsert(list.subList(i, list.size()));
            } else {
                mapper.batchInsert(list.subList(i, i + 999));
            }
        }
    }
}