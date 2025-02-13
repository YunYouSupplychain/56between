package com.yunyou.modules.wms.outbound.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoPack;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoPackEntity;

import java.util.List;

/**
 * 出库单打包记录MAPPER接口
 * @author WMJ
 * @version 2019-12-16
 */
@MyBatisMapper
public interface BanQinWmSoPackMapper extends BaseMapper<BanQinWmSoPack> {
    List<BanQinWmSoPackEntity> findPage(BanQinWmSoPackEntity entity);

    List<BanQinWmSoPackEntity> findOutHandoverPage(BanQinWmSoPackEntity query);
}