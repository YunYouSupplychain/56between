package com.yunyou.modules.tms.basic.service;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.BaseEntity;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.TreeService;
import com.yunyou.modules.sys.entity.Office;
import com.yunyou.modules.sys.utils.UserUtils;
import com.yunyou.modules.tms.basic.entity.TmOutletRelation;
import com.yunyou.modules.tms.basic.entity.extend.TmOutletRelationEntity;
import com.yunyou.modules.tms.basic.entity.extend.TmTransportObjEntity;
import com.yunyou.modules.tms.basic.mapper.TmOutletRelationMapper;
import com.yunyou.modules.tms.common.TmsException;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
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
public class TmOutletRelationService extends TreeService<TmOutletRelationMapper, TmOutletRelation> {
    @Autowired
    private TmTransportObjService tmTransportObjService;

    @Override
    public List<TmOutletRelation> findList(TmOutletRelation tmOutletRelation) {
        if (StringUtils.isNotBlank(tmOutletRelation.getParentIds())) {
            tmOutletRelation.setParentIds("," + tmOutletRelation.getParentIds() + ",");
        }
        return super.findList(tmOutletRelation);
    }

    public List<TmOutletRelation> findChildren(String parentId, String orgId) {
        return mapper.findChildren(parentId, orgId);
    }

    public void saveValidator(TmOutletRelation tmOutletRelation) {
        Office organizationCenter = UserUtils.getOrgCenter(tmOutletRelation.getOrgId());
        if (StringUtils.isBlank(organizationCenter.getId())) {
            throw new TmsException("找不到归属组织中心，不能操作");
        }
        if (tmOutletRelation.getParent() != null && StringUtils.isNotBlank(tmOutletRelation.getParent().getId())) {
            List<TmOutletRelation> list = Lists.newArrayList(tmOutletRelation);

            TmOutletRelation parent = this.get(tmOutletRelation.getParent().getId());
            String[] parentIds = (parent.getParentIds() + "," + parent.getId()).split(",");
            for (String parentId : parentIds) {
                TmOutletRelation outletRelation = this.get(parentId);
                if (outletRelation != null) {
                    list.add(outletRelation);
                }
            }
            if (StringUtils.isNotBlank(tmOutletRelation.getId())) {
                List<TmOutletRelation> children = mapper.findChildren(tmOutletRelation.getId(), tmOutletRelation.getOrgId());
                if (CollectionUtil.isNotEmpty(children)) {
                    list.addAll(children);
                }
            }
            if (list.stream().collect(Collectors.groupingBy(TmOutletRelation::getCode)).values().stream().anyMatch(o -> o.size() > 1)) {
                throw new TmsException("当前路线中已存在网点[" + tmOutletRelation.getName() + "]");
            }
        }
    }

    /**
     * 描述：根据起始网点编码与目的地网点编码获取推荐路线的下一个网点编码
     */
    public String getNextOutletForRecommendRoute(String originOutletCode, String destinationOutletCode, String orgId) {
        if (StringUtils.isBlank(originOutletCode) || StringUtils.isBlank(destinationOutletCode) || StringUtils.isBlank(orgId) || originOutletCode.equals(destinationOutletCode)) {
            return null;
        }
        List<String> recommendRouteList = Lists.newArrayList();
        List<TmOutletRelation> originList = findList(new TmOutletRelation(originOutletCode, orgId));
        List<TmOutletRelation> destinationList = findList(new TmOutletRelation(destinationOutletCode, orgId));
        for (TmOutletRelation destination : destinationList) {
            if (StringUtils.isBlank(destination.getParentId())) {
                continue;
            }
            String destinationR = destination.getParentIds() + destination.getId() + ",";
            for (TmOutletRelation origin : originList) {
                String originR = StringUtils.isNotBlank(origin.getParentId()) ? origin.getParentIds() + origin.getId() + "," : origin.getId() + ",";
                if (destinationR.startsWith(originR)) {
                    recommendRouteList.add(destinationR.replace(originR, ""));
                }
            }
        }
        String nextOutletCode = null;
        if (CollectionUtil.isNotEmpty(recommendRouteList)) {
            recommendRouteList.sort(Comparator.comparingLong(String::length));
            TmOutletRelation tmOutletRelation = this.get(recommendRouteList.get(0).substring(0, recommendRouteList.get(0).indexOf(",")));
            nextOutletCode = tmOutletRelation.getCode();
        }
        if (StringUtils.isBlank(nextOutletCode)) {
            nextOutletCode = destinationOutletCode;
        }
        return nextOutletCode;
    }

    public String getRoute(String id) {
        if (StringUtils.isBlank(id)) {
            return "";
        }

        TmOutletRelation o = this.get(id);
        if (o == null || !BaseEntity.DEL_FLAG_NORMAL.equals(o.getDelFlag())) {
            return "";
        }
        TmTransportObjEntity entity = tmTransportObjService.getEntity(o.getCode(), o.getOrgId());
        if (entity == null) {
            return "";
        }
        return this.getRoute(id, entity.getTransportObjName());
    }

    private String getRoute(String parentId, String route) {
        if (route == null) {
            route = "";
        }
        List<TmOutletRelation> children = mapper.getChildren(parentId);
        if (CollectionUtil.isEmpty(children)) {
            return route;
        }
        TmOutletRelation o = children.get(0);
        if (BaseEntity.DEL_FLAG_NORMAL.equals(o.getDelFlag())) {
            TmTransportObjEntity entity = tmTransportObjService.getEntity(o.getCode(), o.getOrgId());
            if (StringUtils.isBlank(route)) {
                route += entity.getTransportObjName();
            } else {
                route += " -> " + entity.getTransportObjName();
            }
        }
        return this.getRoute(o.getId(), route);
    }

    @SuppressWarnings("unchecked")
    public Page<TmOutletRelationEntity> findRoute(Page page, TmOutletRelationEntity qEntity) {
        dataRuleFilter(qEntity);
        qEntity.setPage(page);
        List<TmOutletRelationEntity> list = mapper.findRoute(qEntity);
        list.forEach(o -> o.setRoute(this.getRoute(o.getId())));
        page.setList(list);
        return page;
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
            TmOutletRelation parent = get(parentId);
            parentCodes.append(parent.getCode()).append(",");
        }
        return parentCodes.toString();
    }

    @Transactional
    public void removeAll(String orgId) {
        mapper.removeAll(orgId);
    }

    @Transactional
    public void batchInsert(List<TmOutletRelation> list) {
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