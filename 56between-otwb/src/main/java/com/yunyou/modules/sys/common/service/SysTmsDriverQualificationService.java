package com.yunyou.modules.sys.common.service;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.interfaces.interactive.action.SyncPlatformDataToTmsAction;
import com.yunyou.modules.sys.SysParamConstants;
import com.yunyou.modules.sys.common.entity.SysTmsDriver;
import com.yunyou.modules.sys.common.entity.SysTmsDriverQualification;
import com.yunyou.modules.sys.common.mapper.SysTmsDriverQualificationMapper;
import com.yunyou.modules.sys.utils.SysControlParamsUtils;
import com.yunyou.modules.tms.common.TmsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 运输人员资质信息Service
 */
@Service
@Transactional(readOnly = true)
public class SysTmsDriverQualificationService extends CrudService<SysTmsDriverQualificationMapper, SysTmsDriverQualification> {
    @Autowired
    @Lazy
    private SysTmsDriverService sysTmsDriverService;
    @Autowired
    @Lazy
    private SyncPlatformDataToTmsAction syncPlatformDataToTmsAction;

    public void saveValidator(SysTmsDriverQualification sysTmsDriverQualification) {
        if (StringUtils.isBlank(sysTmsDriverQualification.getDriverCode())) {
            throw new TmsException("所属运输人员编码不能为空");
        }
        if (StringUtils.isBlank(sysTmsDriverQualification.getQualificationCode())) {
            throw new TmsException("资质编码不能为空");
        }
        List<SysTmsDriverQualification> list = findList(new SysTmsDriverQualification(sysTmsDriverQualification.getDriverCode(), sysTmsDriverQualification.getQualificationCode(), sysTmsDriverQualification.getDataSet()));
        if (CollectionUtil.isNotEmpty(list)) {
            if (list.stream().anyMatch(o -> !o.getId().equals(sysTmsDriverQualification.getId()))) {
                throw new TmsException("资质编码[" + sysTmsDriverQualification.getQualificationCode() + "]已存在");
            }
        }
    }

    @Override
    @Transactional
    public void save(SysTmsDriverQualification entity) {
        super.save(entity);
        final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
        if ("Y".equals(IS_SAVING_PUSH)) {
            sysTmsDriverService.findSync(new SysTmsDriver(entity.getDriverCode(), entity.getDataSet())).forEach(syncPlatformDataToTmsAction::sync);
        }
    }

    @Override
    @Transactional
    public void delete(SysTmsDriverQualification entity) {
        super.delete(entity);
        final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
        if ("Y".equals(IS_SAVING_PUSH)) {
            sysTmsDriverService.findSync(new SysTmsDriver(entity.getDriverCode(), entity.getDataSet())).forEach(syncPlatformDataToTmsAction::sync);
        }
    }

    @Transactional
    public void deleteByDriver(String driverCode, String orgId) {
        mapper.deleteByDriver(driverCode, orgId);
    }

}