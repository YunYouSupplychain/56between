package com.yunyou.modules.tms.basic.service;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.sys.entity.Office;
import com.yunyou.modules.sys.utils.UserUtils;
import com.yunyou.modules.tms.basic.entity.TmVehicle;
import com.yunyou.modules.tms.basic.entity.extend.TmVehicleEntity;
import com.yunyou.modules.tms.basic.mapper.TmVehicleMapper;
import com.yunyou.modules.tms.common.TmsConstants;
import com.yunyou.modules.tms.common.TmsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 车辆信息Service
 *
 * @author liujianhua
 * @version 2020-02-20
 */
@Service
@Transactional(readOnly = true)
public class TmVehicleService extends CrudService<TmVehicleMapper, TmVehicle> {

    @Autowired
    private TmVehiclePartService tmVehiclePartService;
    @Autowired
    private TmVehicleQualificationService tmVehicleQualificationService;

    @Transactional
    public void save(TmVehicle tmVehicle) {
        if (StringUtils.isBlank(tmVehicle.getId())) {
            tmVehicle.setStatus(TmsConstants.VEHICLE_STATUS_00);
        }
        super.save(tmVehicle);
    }

    @Transactional
    public void delete(TmVehicle tmVehicle) {
        // 删除车辆配件
        tmVehiclePartService.deleteByCar(tmVehicle.getCarNo(), tmVehicle.getOrgId());
        // 删除车辆资质
        tmVehicleQualificationService.deleteByCar(tmVehicle.getCarNo(), tmVehicle.getOrgId());
        super.delete(tmVehicle);
    }

    public void saveValidator(TmVehicle tmVehicle) {
        if (StringUtils.isBlank(tmVehicle.getCarNo())) {
            throw new TmsException("车牌号不能为空");
        }
        Office organizationCenter = UserUtils.getOrgCenter(tmVehicle.getOrgId());
        if (StringUtils.isBlank(organizationCenter.getId())) {
            throw new TmsException("找不到归属组织中心，不能操作");
        }
        List<TmVehicle> list = findList(new TmVehicle(tmVehicle.getCarNo(), tmVehicle.getOrgId()));
        if (CollectionUtil.isNotEmpty(list)) {
            if (list.stream().anyMatch(o -> !o.getId().equals(tmVehicle.getId()))) {
                throw new TmsException("车牌号[" + tmVehicle.getCarNo() + "]已存在");
            }
        }
    }

    public TmVehicleEntity getEntity(String id) {
        return mapper.getEntity(id);
    }

    public TmVehicleEntity getEntity(String carNo, String orgId) {
        return mapper.getEntityByNo(carNo, orgId);
    }

    @SuppressWarnings("unchecked")
    public Page<TmVehicleEntity> findPage(Page page, TmVehicle qEntity) {
        dataRuleFilter(qEntity);
        qEntity.setPage(page);
        page.setList(mapper.findPage(qEntity));
        return page;
    }

    @SuppressWarnings("unchecked")
    public Page<TmVehicleEntity> findGrid(Page page, TmVehicleEntity qEntity) {
        dataRuleFilter(qEntity);
        qEntity.setPage(page);
        page.setList(mapper.findGrid(qEntity));
        return page;
    }

    public TmVehicle getByNo(String carNo, String orgId) {
        return mapper.getByNo(carNo, orgId);
    }

    public List<TmVehicle> getEnableList(String orgId) {
        TmVehicle condition = new TmVehicle();
        condition.setStatus("00");
        condition.setOrgId(orgId);
        return this.findList(condition);
    }

    @Transactional
    public void remove(String carNo, String orgId) {
        mapper.remove(carNo, orgId);
    }
}