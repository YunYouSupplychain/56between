package com.yunyou.modules.tms.basic.service;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.sys.entity.Office;
import com.yunyou.modules.sys.utils.UserUtils;
import com.yunyou.modules.tms.basic.entity.TmTransportObjScope;
import com.yunyou.modules.tms.basic.entity.extend.TmTransportObjScopeEntity;
import com.yunyou.modules.tms.basic.mapper.TmTransportObjScopeMapper;
import com.yunyou.modules.tms.common.TmsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 业务对象服务范围Service
 *
 * @author liujianhua
 * @version 2020-02-20
 */
@Service
@Transactional(readOnly = true)
public class TmTransportObjScopeService extends CrudService<TmTransportObjScopeMapper, TmTransportObjScope> {

    public void saveValidator(TmTransportObjScope tmTransportObjScope) {
        if (StringUtils.isBlank(tmTransportObjScope.getTransportObjCode())) {
            throw new TmsException("业务对象不能为空");
        }
        if (StringUtils.isBlank(tmTransportObjScope.getTransportScopeCode())) {
            throw new TmsException("业务服务范围不能为空");
        }
        if (StringUtils.isBlank(tmTransportObjScope.getTransportScopeType())) {
            throw new TmsException("服务范围类型不能为空");
        }
        Office organizationCenter = UserUtils.getOrgCenter(tmTransportObjScope.getOrgId());
        if (StringUtils.isBlank(organizationCenter.getId())) {
            throw new TmsException("找不到归属组织中心，不能操作");
        }
        List<TmTransportObjScope> list = findList(new TmTransportObjScope(tmTransportObjScope.getTransportScopeCode(), tmTransportObjScope.getTransportObjCode(), tmTransportObjScope.getTransportScopeType(), tmTransportObjScope.getOrgId()));
        if (CollectionUtil.isNotEmpty(list)) {
            if (list.stream().anyMatch(o -> !o.getId().equals(tmTransportObjScope.getId()))) {
                throw new TmsException("业务对象[" + tmTransportObjScope.getTransportObjCode() + "]业务服务范围[" + tmTransportObjScope.getTransportScopeCode() + "]服务范围类型[" + tmTransportObjScope.getTransportScopeType() + "]已存在");
            }
        }
    }

    public TmTransportObjScopeEntity getEntity(String id) {
        return mapper.getEntity(id);
    }

    public Page<TmTransportObjScopeEntity> findPage(Page page, TmTransportObjScope tmTransportObjScope) {
        dataRuleFilter(tmTransportObjScope);
        tmTransportObjScope.setPage(page);
        page.setList(mapper.findPage(tmTransportObjScope));
        return page;
    }

    public List<TmTransportObjScope> findCarrierScope(String transportObjCode, String transportScopeType, String orgId) {
        TmTransportObjScope tmTransportObjScope = new TmTransportObjScope();
        tmTransportObjScope.setTransportObjCode(transportObjCode);
        tmTransportObjScope.setTransportScopeType(transportScopeType);
        tmTransportObjScope.setOrgId(orgId);
        return mapper.findCarrierScope(tmTransportObjScope);
    }

    @Transactional
    public void remove(String transportObjCode, String transportScopeCode, String transportScopeType, String orgId) {
        mapper.remove(transportObjCode, transportScopeCode, transportScopeType, orgId);
    }
}