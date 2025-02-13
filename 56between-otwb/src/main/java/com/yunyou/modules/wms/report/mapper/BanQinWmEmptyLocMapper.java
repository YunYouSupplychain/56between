package com.yunyou.modules.wms.report.mapper;

import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhLoc;

import java.util.List;

@MyBatisMapper
public interface BanQinWmEmptyLocMapper {
    List<BanQinCdWhLoc> findPage(BanQinCdWhLoc entity);
}
