package com.yunyou.modules.wms.report.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.report.entity.WmRepCombineEntity;

import java.util.List;

@MyBatisMapper
public interface BanQinWmRepCombineMapper extends BaseMapper {
    List<WmRepCombineEntity> findPage(WmRepCombineEntity entity);
}
