package com.yunyou.modules.sys.common.service;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.interfaces.interactive.action.SyncPlatformDataToTmsAction;
import com.yunyou.modules.sys.SysParamConstants;
import com.yunyou.modules.sys.common.entity.SysTmsVehicle;
import com.yunyou.modules.sys.common.entity.SysTmsVehiclePart;
import com.yunyou.modules.sys.common.mapper.SysTmsVehiclePartMapper;
import com.yunyou.modules.sys.utils.SysControlParamsUtils;
import com.yunyou.modules.tms.common.TmsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 车辆配件Service
 */
@Service
@Transactional(readOnly = true)
public class SysTmsVehiclePartService extends CrudService<SysTmsVehiclePartMapper, SysTmsVehiclePart> {
    @Autowired
    @Lazy
    private SysTmsVehicleService sysTmsVehicleService;
    @Autowired
    @Lazy
    private SyncPlatformDataToTmsAction syncPlatformDataToTmsAction;

    public void saveValidator(SysTmsVehiclePart sysTmsVehiclePart) {
        if (StringUtils.isBlank(sysTmsVehiclePart.getCarNo())) {
            throw new TmsException("所属车辆车牌号不能为空");
        }
        if (StringUtils.isBlank(sysTmsVehiclePart.getPartNo())) {
            throw new TmsException("配件编号不能为空");
        }
        List<SysTmsVehiclePart> list = findList(new SysTmsVehiclePart(sysTmsVehiclePart.getCarNo(), sysTmsVehiclePart.getPartNo(), sysTmsVehiclePart.getDataSet()));
        if (CollectionUtil.isNotEmpty(list)) {
            if (list.stream().anyMatch(o -> !o.getId().equals(sysTmsVehiclePart.getId()))) {
                throw new TmsException("配件编号[" + sysTmsVehiclePart.getPartNo() + "]已存在");
            }
        }
    }

    @Override
    @Transactional
    public void save(SysTmsVehiclePart entity) {
        super.save(entity);
        final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
        if ("Y".equals(IS_SAVING_PUSH)) {
            sysTmsVehicleService.findSync(new SysTmsVehicle(entity.getCarNo(), entity.getDataSet())).forEach(syncPlatformDataToTmsAction::sync);
        }
    }

    @Override
    @Transactional
    public void delete(SysTmsVehiclePart entity) {
        super.delete(entity);
        final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
        if ("Y".equals(IS_SAVING_PUSH)) {
            sysTmsVehicleService.findSync(new SysTmsVehicle(entity.getCarNo(), entity.getDataSet())).forEach(syncPlatformDataToTmsAction::sync);
        }
    }

    @Transactional
    public void deleteByCar(String carNo, String orgId) {
        mapper.deleteByCar(carNo, orgId);
    }
}