package com.yunyou.modules.sys.common.service;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.interfaces.interactive.action.SyncPlatformDataToTmsAction;
import com.yunyou.modules.sys.SysParamConstants;
import com.yunyou.modules.sys.common.entity.SysTmsFitting;
import com.yunyou.modules.sys.common.entity.extend.SysTmsFittingEntity;
import com.yunyou.modules.sys.common.mapper.SysTmsFittingMapper;
import com.yunyou.modules.sys.utils.SysControlParamsUtils;
import com.yunyou.modules.tms.common.TmsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class SysTmsFittingService extends CrudService<SysTmsFittingMapper, SysTmsFitting> {
    @Autowired
    private SyncPlatformDataToTmsAction syncPlatformDataToTmsAction;

    @SuppressWarnings("unchecked")
    public Page<SysTmsFittingEntity> findPage(Page page, SysTmsFittingEntity qEntity) {
        dataRuleFilter(qEntity);
        qEntity.setPage(page);
        page.setList(mapper.findPage(qEntity));
        return page;
    }

    @SuppressWarnings("unchecked")
    public Page<SysTmsFittingEntity> findGrid(Page page, SysTmsFittingEntity qEntity) {
        dataRuleFilter(qEntity);
        qEntity.setPage(page);
        page.setList(mapper.findGrid(qEntity));
        return page;
    }

    public SysTmsFittingEntity getEntity(String id) {
        return mapper.getEntity(id);
    }

    public void saveValidator(SysTmsFitting sysTmsFitting) {
        if (StringUtils.isBlank(sysTmsFitting.getFittingCode())) {
            throw new TmsException("配件编码不能为空");
        }
        if (StringUtils.isBlank(sysTmsFitting.getFittingName())) {
            throw new TmsException("配件名称不能为空");
        }
        List<SysTmsFitting> list = findList(new SysTmsFitting(sysTmsFitting.getFittingCode(), sysTmsFitting.getDataSet()));
        if (CollectionUtil.isNotEmpty(list)) {
            if (list.stream().anyMatch(o -> !o.getId().equals(sysTmsFitting.getId()))) {
                throw new TmsException(MessageFormat.format("配件[{0}]已存在", sysTmsFitting.getFittingCode()));
            }
        }
    }

    @Override
    @Transactional
    public void save(SysTmsFitting entity) {
        super.save(entity);
        final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
        if ("Y".equals(IS_SAVING_PUSH)) {
            syncPlatformDataToTmsAction.sync(entity);
        }
    }

    @Override
    @Transactional
    public void delete(SysTmsFitting entity) {
        super.delete(entity);
        final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
        if ("Y".equals(IS_SAVING_PUSH)) {
            syncPlatformDataToTmsAction.removeFitting(entity.getFittingCode(), entity.getDataSet());
        }
    }
}
