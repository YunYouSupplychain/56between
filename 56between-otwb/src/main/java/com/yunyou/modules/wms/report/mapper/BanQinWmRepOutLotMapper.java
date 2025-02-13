package com.yunyou.modules.wms.report.mapper;

import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.report.entity.WmRepOutLotEntity;

import java.util.List;

@MyBatisMapper
public interface BanQinWmRepOutLotMapper {
    List<WmRepOutLotEntity> findPage(WmRepOutLotEntity entity);
}
