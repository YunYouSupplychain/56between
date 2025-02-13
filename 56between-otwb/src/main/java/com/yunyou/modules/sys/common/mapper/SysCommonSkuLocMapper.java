package com.yunyou.modules.sys.common.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.sys.common.entity.SysCommonSkuLoc;
import com.yunyou.modules.sys.common.entity.extend.SysCommonSkuLocEntity;

import java.util.List;

/**
 * 商品拣货位MAPPER接口
 */
@MyBatisMapper
public interface SysCommonSkuLocMapper extends BaseMapper<SysCommonSkuLoc> {

    List<SysCommonSkuLocEntity> findPage(SysCommonSkuLocEntity entity);
}