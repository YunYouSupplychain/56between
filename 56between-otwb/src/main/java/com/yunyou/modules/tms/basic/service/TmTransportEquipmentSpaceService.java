package com.yunyou.modules.tms.basic.service;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.sys.entity.Office;
import com.yunyou.modules.sys.utils.UserUtils;
import com.yunyou.modules.tms.basic.entity.TmTransportEquipmentSpace;
import com.yunyou.modules.tms.basic.mapper.TmTransportEquipmentSpaceMapper;
import com.yunyou.modules.tms.common.TmsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 运输设备空间信息Service
 *
 * @author liujianhua
 * @version 2020-02-20
 */
@Service
@Transactional(readOnly = true)
public class TmTransportEquipmentSpaceService extends CrudService<TmTransportEquipmentSpaceMapper, TmTransportEquipmentSpace> {

    @Transactional
    public void remove(String transportEquipmentTypeCode, String orgId) {
        mapper.remove(transportEquipmentTypeCode, orgId);
    }

    public void saveValidator(TmTransportEquipmentSpace tmTransportEquipmentSpace) {
        if (StringUtils.isBlank(tmTransportEquipmentSpace.getTransportEquipmentTypeCode())) {
            throw new TmsException("所属设备类型编码不能为空");
        }
        if (StringUtils.isBlank(tmTransportEquipmentSpace.getTransportEquipmentNo())) {
            throw new TmsException("设备编号不能为空");
        }
        if (StringUtils.isBlank(tmTransportEquipmentSpace.getTransportEquipmentNo())) {
            throw new TmsException("设备编号不能为空");
        }
        Office organizationCenter = UserUtils.getOrgCenter(tmTransportEquipmentSpace.getOrgId());
        if (StringUtils.isBlank(organizationCenter.getId())) {
            throw new TmsException("找不到归属组织中心，不能操作");
        }
        List<TmTransportEquipmentSpace> list = findList(new TmTransportEquipmentSpace(tmTransportEquipmentSpace.getTransportEquipmentTypeCode(), tmTransportEquipmentSpace.getTransportEquipmentNo(), tmTransportEquipmentSpace.getOrgId()));
        if (CollectionUtil.isNotEmpty(list)) {
            if (list.stream().anyMatch(o -> !o.getId().equals(tmTransportEquipmentSpace.getId()))) {
                throw new TmsException("设备编号[" + tmTransportEquipmentSpace.getTransportEquipmentNo() + "]已存在");
            }
        }
    }
}