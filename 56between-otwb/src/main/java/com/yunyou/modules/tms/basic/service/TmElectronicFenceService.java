package com.yunyou.modules.tms.basic.service;

import com.yunyou.common.utils.IdGen;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.tms.basic.entity.TmElectronicFence;
import com.yunyou.modules.tms.basic.entity.TmElectronicFencePoint;
import com.yunyou.modules.tms.basic.mapper.TmElectronicFenceMapper;
import com.yunyou.modules.tms.basic.mapper.TmElectronicFencePointMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TmElectronicFenceService extends CrudService<TmElectronicFenceMapper, TmElectronicFence> {
    @Autowired
    private TmElectronicFencePointMapper tmElectronicFencePointMapper;

    @Override
    public Page<TmElectronicFence> findPage(Page<TmElectronicFence> page, TmElectronicFence entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    public Page<TmElectronicFence> findGrid(Page<TmElectronicFence> page, TmElectronicFence entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findGrid(entity));
        return page;
    }

    public TmElectronicFence getByCode(String fenceCode, String orgId) {
        TmElectronicFence tmElectronicFence = mapper.getByCode(fenceCode, orgId);
        if (tmElectronicFence != null) {
            tmElectronicFence.setPointList(this.findPoints(fenceCode, orgId));
        }
        return tmElectronicFence;
    }

    public List<TmElectronicFencePoint> findPoints(String fenceCode, String orgId) {
        return tmElectronicFencePointMapper.findList(new TmElectronicFencePoint(fenceCode, orgId));
    }

    @Override
    @Transactional
    public void save(TmElectronicFence entity) {
        if (StringUtils.isBlank(entity.getFenceCode())) {
            entity.setFenceCode(StringUtils.upperCase(IdGen.uuid()));
        }
        super.save(entity);
        tmElectronicFencePointMapper.delete(new TmElectronicFencePoint(entity.getFenceCode(), entity.getOrgId()));
        if (CollectionUtil.isEmpty(entity.getPointList())) {
            return;
        }
        for (TmElectronicFencePoint point : entity.getPointList()) {
            point.setFenceCode(entity.getFenceCode());
            point.setOrgId(entity.getOrgId());
            tmElectronicFencePointMapper.insert(point);
        }
    }

    @Override
    @Transactional
    public void delete(TmElectronicFence entity) {
        tmElectronicFencePointMapper.delete(new TmElectronicFencePoint(entity.getFenceCode(), entity.getOrgId()));
        super.delete(entity);
    }
}
