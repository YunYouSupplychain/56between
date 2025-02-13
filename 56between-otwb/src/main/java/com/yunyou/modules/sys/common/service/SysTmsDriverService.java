package com.yunyou.modules.sys.common.service;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.interfaces.interactive.action.SyncPlatformDataToTmsAction;
import com.yunyou.modules.sys.SysParamConstants;
import com.yunyou.modules.sys.common.entity.SysTmsDriver;
import com.yunyou.modules.sys.common.entity.SysTmsDriverQualification;
import com.yunyou.modules.sys.common.entity.extend.SysTmsDriverEntity;
import com.yunyou.modules.sys.common.mapper.SysTmsDriverMapper;
import com.yunyou.modules.sys.utils.SysControlParamsUtils;
import com.yunyou.modules.tms.common.TmsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 运输人员信息Service
 *
 * @author liujianhua
 * @version 2020-02-20
 */
@Service
@Transactional(readOnly = true)
public class SysTmsDriverService extends CrudService<SysTmsDriverMapper, SysTmsDriver> {
    @Autowired
    private SysTmsDriverQualificationService sysTmsDriverQualificationService;
    @Autowired
    @Lazy
    private SyncPlatformDataToTmsAction syncPlatformDataToTmsAction;

    @SuppressWarnings("unchecked")
    public Page<SysTmsDriverEntity> findPage(Page page, SysTmsDriverEntity entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    @SuppressWarnings("unchecked")
    public Page<SysTmsDriverEntity> findGrid(Page page, SysTmsDriverEntity entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findGrid(entity));
        return page;
    }

    public List<SysTmsDriver> findSync(SysTmsDriver entity) {
        List<SysTmsDriver> list = mapper.findSync(entity);
        list.forEach(o -> o.setDriverQualificationList(sysTmsDriverQualificationService.findList(new SysTmsDriverQualification(o.getCode(), o.getDataSet()))));
        return list;
    }

    @Override
    public SysTmsDriver get(String id) {
        SysTmsDriver entity = super.get(id);
        if (entity != null) {
            entity.setDriverQualificationList(sysTmsDriverQualificationService.findList(new SysTmsDriverQualification(null, entity.getDataSet())));
        }
        return entity;
    }

    public SysTmsDriverEntity getEntity(String id) {
        SysTmsDriverEntity entity = mapper.getEntity(id);
        if (entity != null) {
            entity.setDriverQualificationList(sysTmsDriverQualificationService.findList(new SysTmsDriverQualification(null, entity.getDataSet())));
        }
        return entity;
    }

    public void saveValidator(SysTmsDriver sysTmsDriver) {
        if (StringUtils.isBlank(sysTmsDriver.getCode())) {
            throw new TmsException("编码不能为空");
        }
        if (StringUtils.isBlank(sysTmsDriver.getName())) {
            throw new TmsException("姓名不能为空");
        }
        if (StringUtils.isBlank(sysTmsDriver.getCarrierCode())) {
            throw new TmsException("承运商不能为空");
        }
        if (StringUtils.isBlank(sysTmsDriver.getPhone())) {
            throw new TmsException("手机号不能为空");
        }
        if (StringUtils.isBlank(sysTmsDriver.getIdCard())) {
            throw new TmsException("身份证不能为空");
        }
        List<SysTmsDriver> list = findList(new SysTmsDriver(sysTmsDriver.getCode(), sysTmsDriver.getDataSet()));
        if (CollectionUtil.isNotEmpty(list)) {
            if (list.stream().anyMatch(o -> !o.getId().equals(sysTmsDriver.getId()))) {
                throw new TmsException("编码[" + sysTmsDriver.getCode() + "]已存在");
            }
        }
    }

    @Override
    @Transactional
    public void save(SysTmsDriver entity) {
        super.save(entity);
        final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
        if ("Y".equals(IS_SAVING_PUSH)) {
            this.findSync(entity).forEach(syncPlatformDataToTmsAction::sync);
        }
    }

    @Transactional
    public void delete(SysTmsDriver sysTmsDriver) {
        sysTmsDriverQualificationService.deleteByDriver(sysTmsDriver.getCode(), sysTmsDriver.getDataSet());
        super.delete(sysTmsDriver);
        final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
        if ("Y".equals(IS_SAVING_PUSH)) {
            syncPlatformDataToTmsAction.removeDriver(sysTmsDriver.getCode(), sysTmsDriver.getDataSet());
        }
    }

}