package com.yunyou.modules.tms.basic.service;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.sys.entity.Office;
import com.yunyou.modules.sys.utils.UserUtils;
import com.yunyou.modules.tms.basic.entity.TmVehicleQualification;
import com.yunyou.modules.tms.basic.mapper.TmVehicleQualificationMapper;
import com.yunyou.modules.tms.common.TmsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 车辆资质信息Service
 *
 * @author liujianhua
 * @version 2020-02-20
 */
@Service
@Transactional(readOnly = true)
public class TmVehicleQualificationService extends CrudService<TmVehicleQualificationMapper, TmVehicleQualification> {

    public TmVehicleQualification get(String id) {
        return super.get(id);
    }

    public List<TmVehicleQualification> findList(TmVehicleQualification tmVehicleQualification) {
        return super.findList(tmVehicleQualification);
    }

    public Page<TmVehicleQualification> findPage(Page<TmVehicleQualification> page, TmVehicleQualification tmVehicleQualification) {
        return super.findPage(page, tmVehicleQualification);
    }

    @Transactional
    public void save(TmVehicleQualification tmVehicleQualification) {
        super.save(tmVehicleQualification);
    }

    @Transactional
    public void delete(TmVehicleQualification tmVehicleQualification) {
        super.delete(tmVehicleQualification);
    }

    @Transactional
    public void deleteByCar(String carNo, String orgId) {
        mapper.deleteByCar(carNo, orgId);
    }

    public void saveValidator(TmVehicleQualification tmVehicleQualification) {
        if (StringUtils.isBlank(tmVehicleQualification.getCarNo())) {
            throw new TmsException("所属车辆车牌号不能为空");
        }
        if (StringUtils.isBlank(tmVehicleQualification.getQualificationCode())) {
            throw new TmsException("资质编码不能为空");
        }
        Office organizationCenter = UserUtils.getOrgCenter(tmVehicleQualification.getOrgId());
        if (StringUtils.isBlank(organizationCenter.getId())) {
            throw new TmsException("找不到归属组织中心，不能操作");
        }
        List<TmVehicleQualification> list = findList(new TmVehicleQualification(tmVehicleQualification.getCarNo(), tmVehicleQualification.getQualificationCode(), tmVehicleQualification.getOrgId()));
        if (CollectionUtil.isNotEmpty(list)) {
            if (list.stream().anyMatch(o -> !o.getId().equals(tmVehicleQualification.getId()))) {
                throw new TmsException("资质编码[" + tmVehicleQualification.getQualificationCode() + "]已存在");
            }
        }
    }
}