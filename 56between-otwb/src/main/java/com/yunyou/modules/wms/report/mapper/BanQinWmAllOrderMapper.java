package com.yunyou.modules.wms.report.mapper;

import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.report.entity.WmAllOrderEntity;

import java.util.List;

@MyBatisMapper
public interface BanQinWmAllOrderMapper {
    List<WmAllOrderEntity> findPage(WmAllOrderEntity entity);
}
