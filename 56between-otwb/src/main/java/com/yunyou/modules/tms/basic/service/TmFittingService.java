package com.yunyou.modules.tms.basic.service;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.sys.entity.Office;
import com.yunyou.modules.sys.utils.UserUtils;
import com.yunyou.modules.tms.basic.entity.TmFitting;
import com.yunyou.modules.tms.basic.entity.extend.TmFittingEntity;
import com.yunyou.modules.tms.basic.mapper.TmFittingMapper;
import com.yunyou.modules.tms.common.TmsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class TmFittingService extends CrudService<TmFittingMapper, TmFitting> {

    @SuppressWarnings("unchecked")
    public Page<TmFittingEntity> findPage(Page page, TmFittingEntity qEntity) {
        dataRuleFilter(qEntity);
        qEntity.setPage(page);
        page.setList(mapper.findPage(qEntity));
        return page;
    }

    @SuppressWarnings("unchecked")
    public Page<TmFittingEntity> findGrid(Page page, TmFittingEntity qEntity) {
        dataRuleFilter(qEntity);
        qEntity.setPage(page);
        page.setList(mapper.findGrid(qEntity));
        return page;
    }

    public TmFittingEntity getEntity(String id) {
        return mapper.getEntity(id);
    }

    public void saveValidator(TmFitting tmFitting) {
        if (StringUtils.isBlank(tmFitting.getFittingCode())) {
            throw new TmsException("配件编码不能为空");
        }
        if (StringUtils.isBlank(tmFitting.getFittingName())) {
            throw new TmsException("配件名称不能为空");
        }
        Office organizationCenter = UserUtils.getOrgCenter(tmFitting.getOrgId());
        if (StringUtils.isBlank(organizationCenter.getId())) {
            throw new TmsException("找不到归属组织中心，不能操作");
        }
        List<TmFitting> list = findList(new TmFitting(tmFitting.getFittingCode(), tmFitting.getOrgId()));
        if (CollectionUtil.isNotEmpty(list)) {
            if (list.stream().anyMatch(o -> !o.getId().equals(tmFitting.getId()))) {
                throw new TmsException(MessageFormat.format("配件[{0}]已存在", tmFitting.getFittingCode()));
            }
        }
    }

    public TmFitting getByCode(String fittingCode, String orgId) {
        return mapper.getByCode(fittingCode, orgId);
    }

    @Transactional
    public void remove(String fittingCode, String orgId) {
        mapper.remove(fittingCode, orgId);
    }
}
