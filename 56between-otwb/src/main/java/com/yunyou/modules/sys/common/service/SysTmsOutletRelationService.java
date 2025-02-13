package com.yunyou.modules.sys.common.service;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.BaseEntity;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.TreeService;
import com.yunyou.modules.interfaces.interactive.action.SyncPlatformDataToTmsAction;
import com.yunyou.modules.sys.SysParamConstants;
import com.yunyou.modules.sys.common.entity.SysTmsOutletRelation;
import com.yunyou.modules.sys.common.entity.extend.SysTmsOutletRelationEntity;
import com.yunyou.modules.sys.common.entity.extend.SysTmsTransportObjEntity;
import com.yunyou.modules.sys.common.mapper.SysTmsOutletRelationMapper;
import com.yunyou.modules.sys.utils.SysControlParamsUtils;
import com.yunyou.modules.tms.common.TmsException;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 网点拓扑图Service
 *
 * @author liujianhua
 * @version 2020-03-02
 */
@Service
@Transactional(readOnly = true)
public class SysTmsOutletRelationService extends TreeService<SysTmsOutletRelationMapper, SysTmsOutletRelation> {
    @Autowired
    private SysTmsTransportObjService sysTmsTransportObjService;
    @Autowired
    @Lazy
    private SyncPlatformDataToTmsAction syncPlatformDataToTmsAction;

    @SuppressWarnings("unchecked")
    public Page<SysTmsOutletRelationEntity> findRoute(Page page, SysTmsOutletRelationEntity qEntity) {
        dataRuleFilter(qEntity);
        qEntity.setPage(page);
        List<SysTmsOutletRelationEntity> list = mapper.findRoute(qEntity);
        list.forEach(o -> o.setRoute(this.getRoute(o.getId())));
        page.setList(list);
        return page;
    }

    public List<SysTmsOutletRelation> findSync(String dataSet) {
        return mapper.findSync(dataSet);
    }

    @Override
    public List<SysTmsOutletRelation> findList(SysTmsOutletRelation sysTmsOutletRelation) {
        if (StringUtils.isNotBlank(sysTmsOutletRelation.getParentIds())) {
            sysTmsOutletRelation.setParentIds("," + sysTmsOutletRelation.getParentIds() + ",");
        }
        return super.findList(sysTmsOutletRelation);
    }

    public List<SysTmsOutletRelation> findChildren(String parentId, String dataSet) {
        return mapper.findChildren(parentId, dataSet);
    }

    public String getRoute(String id) {
        if (StringUtils.isBlank(id)) {
            return "";
        }
        SysTmsOutletRelation o = this.get(id);
        if (o == null || !BaseEntity.DEL_FLAG_NORMAL.equals(o.getDelFlag())) {
            return "";
        }
        SysTmsTransportObjEntity entity = sysTmsTransportObjService.getEntity(o.getCode(), o.getDataSet());
        if (entity == null) {
            return "";
        }
        return this.getRoute(id, entity.getTransportObjName());
    }

    private String getRoute(String parentId, String route) {
        if (route == null) {
            route = "";
        }
        List<SysTmsOutletRelation> children = mapper.getChildren(parentId);
        if (CollectionUtil.isEmpty(children)) {
            return route;
        }
        SysTmsOutletRelation o = children.get(0);
        if (BaseEntity.DEL_FLAG_NORMAL.equals(o.getDelFlag())) {
            SysTmsTransportObjEntity entity = sysTmsTransportObjService.getEntity(o.getCode(), o.getDataSet());
            if (StringUtils.isBlank(route)) {
                route += entity.getTransportObjName();
            } else {
                route += " -> " + entity.getTransportObjName();
            }
        }
        return this.getRoute(o.getId(), route);
    }

    public String getParentCodes(String parentIds) {
        if (StringUtils.isBlank(parentIds)) {
            return "";
        }
        StringBuilder parentCodes = new StringBuilder();
        String[] parentIdArr = parentIds.split(",");
        for (String parentId : parentIdArr) {
            if ("0".equals(parentId) || StringUtils.isBlank(parentId)) {
                continue;
            }
            SysTmsOutletRelation parent = get(parentId);
            parentCodes.append(parent.getCode()).append(",");
        }
        return parentCodes.toString();
    }

    public void saveValidator(SysTmsOutletRelation sysTmsOutletRelation) {
        if (sysTmsOutletRelation.getParent() != null && StringUtils.isNotBlank(sysTmsOutletRelation.getParent().getId())) {
            List<SysTmsOutletRelation> list = Lists.newArrayList(sysTmsOutletRelation);

            SysTmsOutletRelation parent = this.get(sysTmsOutletRelation.getParent().getId());
            String[] parentIds = (parent.getParentIds() + "," + parent.getId()).split(",");
            for (String parentId : parentIds) {
                SysTmsOutletRelation outletRelation = this.get(parentId);
                if (outletRelation != null) {
                    list.add(outletRelation);
                }
            }
            if (StringUtils.isNotBlank(sysTmsOutletRelation.getId())) {
                List<SysTmsOutletRelation> children = mapper.findChildren(sysTmsOutletRelation.getId(), sysTmsOutletRelation.getDataSet());
                if (CollectionUtil.isNotEmpty(children)) {
                    list.addAll(children);
                }
            }
            if (list.stream().collect(Collectors.groupingBy(SysTmsOutletRelation::getCode)).values().stream().anyMatch(o -> o.size() > 1)) {
                throw new TmsException("当前路线中已存在网点[" + sysTmsOutletRelation.getName() + "]");
            }
        }
    }

    @Override
    @Transactional
    public void save(SysTmsOutletRelation entity) {
        super.save(entity);
        final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
        if ("Y".equals(IS_SAVING_PUSH)) {
            syncPlatformDataToTmsAction.sync(entity);
        }
    }

    @Override
    @Transactional
    public void delete(SysTmsOutletRelation entity) {
        super.delete(entity);
        final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
        if ("Y".equals(IS_SAVING_PUSH)) {
            syncPlatformDataToTmsAction.removeOutletRelation(entity.getCode(), entity.getParentIds(), entity.getDataSet());
        }
    }
}