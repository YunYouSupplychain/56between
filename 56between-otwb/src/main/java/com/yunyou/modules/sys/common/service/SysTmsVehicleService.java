package com.yunyou.modules.sys.common.service;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.interfaces.interactive.action.SyncPlatformDataToTmsAction;
import com.yunyou.modules.sys.SysParamConstants;
import com.yunyou.modules.sys.common.entity.SysTmsVehicle;
import com.yunyou.modules.sys.common.entity.SysTmsVehiclePart;
import com.yunyou.modules.sys.common.entity.SysTmsVehicleQualification;
import com.yunyou.modules.sys.common.entity.extend.SysTmsVehicleEntity;
import com.yunyou.modules.sys.common.mapper.SysTmsVehicleMapper;
import com.yunyou.modules.sys.utils.SysControlParamsUtils;
import com.yunyou.modules.tms.common.TmsConstants;
import com.yunyou.modules.tms.common.TmsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 车辆信息Service
 */
@Service
@Transactional(readOnly = true)
public class SysTmsVehicleService extends CrudService<SysTmsVehicleMapper, SysTmsVehicle> {
    @Autowired
    private SysTmsVehiclePartService sysTmsVehiclePartService;
    @Autowired
    private SysTmsVehicleQualificationService sysTmsVehicleQualificationService;
    @Autowired
    @Lazy
    private SyncPlatformDataToTmsAction syncPlatformDataToTmsAction;

    @SuppressWarnings("unchecked")
    public Page<SysTmsVehicleEntity> findPage(Page page, SysTmsVehicleEntity qEntity) {
        dataRuleFilter(qEntity);
        qEntity.setPage(page);
        page.setList(mapper.findPage(qEntity));
        return page;
    }

    @SuppressWarnings("unchecked")
    public Page<SysTmsVehicleEntity> findGrid(Page page, SysTmsVehicleEntity qEntity) {
        dataRuleFilter(qEntity);
        qEntity.setPage(page);
        page.setList(mapper.findGrid(qEntity));
        return page;
    }

    public List<SysTmsVehicle> findSync(SysTmsVehicle entity) {
        List<SysTmsVehicle> list = mapper.findSync(entity);
        list.forEach(o -> {
            o.setVehiclePartList(sysTmsVehiclePartService.findList(new SysTmsVehiclePart(o.getCarNo(), o.getDataSet())));
            o.setVehicleQualificationList(sysTmsVehicleQualificationService.findList(new SysTmsVehicleQualification(o.getCarNo(), o.getDataSet())));
        });
        return list;
    }

    public SysTmsVehicleEntity getEntity(String id) {
        SysTmsVehicleEntity entity = mapper.getEntity(id);
        if (entity != null) {
            entity.setVehiclePartList(sysTmsVehiclePartService.findList(new SysTmsVehiclePart(entity.getCarNo(), entity.getDataSet())));
            entity.setVehicleQualificationList(sysTmsVehicleQualificationService.findList(new SysTmsVehicleQualification(entity.getCarNo(), entity.getDataSet())));
        }
        return entity;
    }

    public SysTmsVehicleEntity getEntity(String carNo, String dataSet) {
        return mapper.getEntityByNo(carNo, dataSet);
    }

    public SysTmsVehicle getByNo(String carNo, String dataSet) {
        return mapper.getByNo(carNo, dataSet);
    }

    public void saveValidator(SysTmsVehicle sysTmsVehicle) {
        if (StringUtils.isBlank(sysTmsVehicle.getCarNo())) {
            throw new TmsException("车牌号不能为空");
        }
        List<SysTmsVehicle> list = findList(new SysTmsVehicle(sysTmsVehicle.getCarNo(), sysTmsVehicle.getDataSet()));
        if (CollectionUtil.isNotEmpty(list)) {
            if (list.stream().anyMatch(o -> !o.getId().equals(sysTmsVehicle.getId()))) {
                throw new TmsException("车牌号[" + sysTmsVehicle.getCarNo() + "]已存在");
            }
        }
    }

    @Override
    @Transactional
    public void save(SysTmsVehicle sysTmsVehicle) {
        if (StringUtils.isBlank(sysTmsVehicle.getId())) {
            sysTmsVehicle.setStatus(TmsConstants.VEHICLE_STATUS_00);
        }
        super.save(sysTmsVehicle);
        final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
        if ("Y".equals(IS_SAVING_PUSH)) {
            this.findSync(sysTmsVehicle).forEach(syncPlatformDataToTmsAction::sync);
        }
    }

    @Transactional
    public void delete(SysTmsVehicle entity) {
        sysTmsVehiclePartService.deleteByCar(entity.getCarNo(), entity.getDataSet());
        sysTmsVehicleQualificationService.deleteByCar(entity.getCarNo(), entity.getDataSet());
        super.delete(entity);
        final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
        if ("Y".equals(IS_SAVING_PUSH)) {
            syncPlatformDataToTmsAction.removeVehicle(entity.getCarNo(), entity.getDataSet());
        }
    }

}