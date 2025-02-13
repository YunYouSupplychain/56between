package com.yunyou.modules.wms.report.mapper;

import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.report.entity.WmRepInvValidityEntity;

import java.util.List;

@MyBatisMapper
public interface BanQinWmRepInvValidityMapper {
    List<WmRepInvValidityEntity> findPage(WmRepInvValidityEntity entity);
}
