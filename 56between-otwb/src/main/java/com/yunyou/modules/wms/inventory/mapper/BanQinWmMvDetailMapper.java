package com.yunyou.modules.wms.inventory.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.inventory.entity.BanQinWmMvDetail;
import com.yunyou.modules.wms.inventory.entity.extend.BanQinWmMvDetailEntity;

import java.util.List;

@MyBatisMapper
public interface BanQinWmMvDetailMapper extends BaseMapper<BanQinWmMvDetail> {

    List<BanQinWmMvDetailEntity> findPage(BanQinWmMvDetailEntity qEntity);

    List<BanQinWmMvDetailEntity> findEntity(BanQinWmMvDetailEntity qEntity);

    BanQinWmMvDetailEntity getEntity(String id);
}
