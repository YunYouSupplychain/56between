package com.yunyou.modules.tms.basic.service;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.sys.GenNoType;
import com.yunyou.modules.sys.entity.Office;
import com.yunyou.modules.sys.service.SynchronizedNoService;
import com.yunyou.modules.sys.utils.UserUtils;
import com.yunyou.modules.tms.basic.entity.TmVehicleType;
import com.yunyou.modules.tms.basic.mapper.TmVehicleTypeMapper;
import com.yunyou.modules.tms.common.TmsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 车型信息Service
 *
 * @author liujianhua
 * @version 2022-08-04
 */
@Service
@Transactional(readOnly = true)
public class TmVehicleTypeService extends CrudService<TmVehicleTypeMapper, TmVehicleType> {
    @Autowired
    private SynchronizedNoService noService;

    @Override
    public Page<TmVehicleType> findPage(Page<TmVehicleType> page, TmVehicleType qEntity) {
        dataRuleFilter(qEntity);
        qEntity.setPage(page);
        page.setList(mapper.findPage(qEntity));
        return page;
    }

    public Page<TmVehicleType> findGrid(Page<TmVehicleType> page, TmVehicleType qEntity) {
        dataRuleFilter(qEntity);
        qEntity.setPage(page);
        page.setList(mapper.findGrid(qEntity));
        return page;
    }

    public void saveValidator(TmVehicleType tmVehicleType) {
        if (StringUtils.isBlank(tmVehicleType.getName())) {
            throw new TmsException("名称不能为空");
        }
        Office organizationCenter = UserUtils.getOrgCenter(tmVehicleType.getOrgId());
        if (StringUtils.isBlank(organizationCenter.getId())) {
            throw new TmsException("找不到归属组织中心，不能操作");
        }
    }

    public TmVehicleType getByNo(String code, String orgId) {
        return mapper.getByNo(code, orgId);
    }

    @Override
    @Transactional
    public void save(TmVehicleType entity) {
        if(StringUtils.isBlank(entity.getCode())) {
            entity.setCode(noService.getDocumentNo(GenNoType.TM_VEHICLE_TYPE_NO.name()));
        }
        super.save(entity);
    }

    @Transactional
    public void removeByNo(String code, String orgId) {
        mapper.removeByNo(code, orgId);
    }
}