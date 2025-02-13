package com.yunyou.modules.tms.basic.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.sys.entity.Office;
import com.yunyou.modules.sys.utils.UserUtils;
import com.yunyou.modules.tms.basic.entity.TmTransportEquipmentType;
import com.yunyou.modules.tms.basic.entity.extend.TmTransportEquipmentTypeEntity;
import com.yunyou.modules.tms.basic.mapper.TmTransportEquipmentTypeMapper;
import com.yunyou.modules.tms.common.TmsException;

/**
 * 运输设备类型Service
 *
 * @author liujianhua
 * @version 2020-02-20
 */
@Service
@Transactional(readOnly = true)
public class TmTransportEquipmentTypeService extends CrudService<TmTransportEquipmentTypeMapper, TmTransportEquipmentType> {
    @Autowired
    private TmTransportEquipmentSpaceService tmTransportEquipmentSpaceService;

    @Override
    @Transactional
    public void delete(TmTransportEquipmentType tmTransportEquipmentType) {
        // 删除设备空间
        tmTransportEquipmentSpaceService.remove(tmTransportEquipmentType.getTransportEquipmentTypeCode(), tmTransportEquipmentType.getOrgId());
        super.delete(tmTransportEquipmentType);
    }

    public void saveValidator(TmTransportEquipmentType tmTransportEquipmentType) {
        if (StringUtils.isBlank(tmTransportEquipmentType.getTransportEquipmentTypeCode())) {
            throw new TmsException("运输设备类型编码不能为空");
        }
        Office organizationCenter = UserUtils.getOrgCenter(tmTransportEquipmentType.getOrgId());
        if (StringUtils.isBlank(organizationCenter.getId())) {
            throw new TmsException("找不到归属组织中心，不能操作");
        }
        List<TmTransportEquipmentType> list = this.findList(new TmTransportEquipmentType(tmTransportEquipmentType.getTransportEquipmentTypeCode(), tmTransportEquipmentType.getOrgId()));
        if (CollectionUtil.isNotEmpty(list)) {
            if (list.stream().anyMatch(o -> !o.getId().equals(tmTransportEquipmentType.getId()))) {
                throw new TmsException("运输设备类型编码[" + tmTransportEquipmentType.getTransportEquipmentTypeCode() + "]已存在");
            }
        }
    }

    public TmTransportEquipmentTypeEntity getEntity(String id) {
        return mapper.getEntity(id);
    }

    public Page<TmTransportEquipmentTypeEntity> findPage(Page page, TmTransportEquipmentType tmTransportEquipmentType) {
        dataRuleFilter(tmTransportEquipmentType);
        tmTransportEquipmentType.setPage(page);
        page.setList(mapper.findPage(tmTransportEquipmentType));
        return page;
    }

    public Page<TmTransportEquipmentTypeEntity> findGrid(Page page, TmTransportEquipmentTypeEntity entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findGrid(entity));
        return page;
    }

    public TmTransportEquipmentType getByCode(String transportEquipmentTypeCode, String orgId) {
        return mapper.getByCode(transportEquipmentTypeCode, orgId);
    }

    @Transactional
    public void remove(String transportEquipmentTypeCode, String orgId) {
        tmTransportEquipmentSpaceService.remove(transportEquipmentTypeCode, orgId);
        mapper.remove(transportEquipmentTypeCode, orgId);
    }
}