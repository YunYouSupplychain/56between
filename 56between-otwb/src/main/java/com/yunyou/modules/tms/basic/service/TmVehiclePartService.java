package com.yunyou.modules.tms.basic.service;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.sys.entity.Office;
import com.yunyou.modules.sys.utils.UserUtils;
import com.yunyou.modules.tms.basic.entity.TmVehiclePart;
import com.yunyou.modules.tms.basic.mapper.TmVehiclePartMapper;
import com.yunyou.modules.tms.common.TmsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 车辆配件Service
 *
 * @author liujianhua
 * @version 2020-02-20
 */
@Service
@Transactional(readOnly = true)
public class TmVehiclePartService extends CrudService<TmVehiclePartMapper, TmVehiclePart> {

    public TmVehiclePart get(String id) {
        return super.get(id);
    }

    public List<TmVehiclePart> findList(TmVehiclePart tmVehiclePart) {
        return super.findList(tmVehiclePart);
    }

    public Page<TmVehiclePart> findPage(Page<TmVehiclePart> page, TmVehiclePart tmVehiclePart) {
        return super.findPage(page, tmVehiclePart);
    }

    @Transactional
    public void save(TmVehiclePart tmVehiclePart) {
        super.save(tmVehiclePart);
    }

    @Transactional
    public void delete(TmVehiclePart tmVehiclePart) {
        super.delete(tmVehiclePart);
    }

    @Transactional
    public void deleteByCar(String carNo, String orgId) {
        mapper.deleteByCar(carNo, orgId);
    }

    public void saveValidator(TmVehiclePart tmVehiclePart) {
        if (StringUtils.isBlank(tmVehiclePart.getCarNo())) {
            throw new TmsException("所属车辆车牌号不能为空");
        }
        if (StringUtils.isBlank(tmVehiclePart.getPartNo())) {
            throw new TmsException("配件编号不能为空");
        }
        Office organizationCenter = UserUtils.getOrgCenter(tmVehiclePart.getOrgId());
        if (StringUtils.isBlank(organizationCenter.getId())) {
            throw new TmsException("找不到归属组织中心，不能操作");
        }
        List<TmVehiclePart> list = findList(new TmVehiclePart(tmVehiclePart.getCarNo(), tmVehiclePart.getPartNo(), tmVehiclePart.getOrgId()));
        if (CollectionUtil.isNotEmpty(list)) {
            if (list.stream().anyMatch(o -> !o.getId().equals(tmVehiclePart.getId()))) {
                throw new TmsException("配件编号[" + tmVehiclePart.getPartNo() + "]已存在");
            }
        }
    }
}