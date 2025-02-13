package com.yunyou.modules.sys.common.service;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.interfaces.interactive.action.SyncPlatformDataToTmsAction;
import com.yunyou.modules.sys.SysParamConstants;
import com.yunyou.modules.sys.common.entity.SysTmsVehicle;
import com.yunyou.modules.sys.common.entity.SysTmsVehicleQualification;
import com.yunyou.modules.sys.common.mapper.SysTmsVehicleQualificationMapper;
import com.yunyou.modules.sys.utils.SysControlParamsUtils;
import com.yunyou.modules.tms.common.TmsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 车辆资质信息Service
 */
@Service
@Transactional(readOnly = true)
public class SysTmsVehicleQualificationService extends CrudService<SysTmsVehicleQualificationMapper, SysTmsVehicleQualification> {
    @Autowired
    @Lazy
    private SysTmsVehicleService sysTmsVehicleService;
    @Autowired
    @Lazy
    private SyncPlatformDataToTmsAction syncPlatformDataToTmsAction;

    public void saveValidator(SysTmsVehicleQualification sysTmsVehicleQualification) {
        if (StringUtils.isBlank(sysTmsVehicleQualification.getCarNo())) {
            throw new TmsException("所属车辆车牌号不能为空");
        }
        if (StringUtils.isBlank(sysTmsVehicleQualification.getQualificationCode())) {
            throw new TmsException("资质编码不能为空");
        }
        List<SysTmsVehicleQualification> list = findList(new SysTmsVehicleQualification(sysTmsVehicleQualification.getCarNo(), sysTmsVehicleQualification.getQualificationCode(), sysTmsVehicleQualification.getDataSet()));
        if (CollectionUtil.isNotEmpty(list)) {
            if (list.stream().anyMatch(o -> !o.getId().equals(sysTmsVehicleQualification.getId()))) {
                throw new TmsException("资质编码[" + sysTmsVehicleQualification.getQualificationCode() + "]已存在");
            }
        }
    }

    @Override
    @Transactional
    public void save(SysTmsVehicleQualification entity) {
        super.save(entity);
        final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
        if ("Y".equals(IS_SAVING_PUSH)) {
            sysTmsVehicleService.findSync(new SysTmsVehicle(entity.getCarNo(), entity.getDataSet())).forEach(syncPlatformDataToTmsAction::sync);
        }
    }

    @Override
    @Transactional
    public void delete(SysTmsVehicleQualification entity) {
        super.delete(entity);
        final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
        if ("Y".equals(IS_SAVING_PUSH)) {
            sysTmsVehicleService.findSync(new SysTmsVehicle(entity.getCarNo(), entity.getDataSet())).forEach(syncPlatformDataToTmsAction::sync);
        }
    }

    @Transactional
    public void deleteByCar(String carNo, String dataSet) {
        mapper.deleteByCar(carNo, dataSet);
    }
}