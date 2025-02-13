package com.yunyou.modules.wms.inventory.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.inventory.entity.BanQinWmActLog;
import com.yunyou.modules.wms.inventory.entity.BanQinWmActLogEntity;

import java.util.List;

/**
 * 库存交易日志MAPPER接口
 * @author WMJ
 * @version 2020-04-14
 */
@MyBatisMapper
public interface BanQinWmActLogMapper extends BaseMapper<BanQinWmActLog> {
    List<BanQinWmActLogEntity> findLast(BanQinWmActLogEntity entity);
    List<BanQinWmActLogEntity> findEntity(BanQinWmActLogEntity entity);
    List<BanQinWmActLogEntity> findInOutData(BanQinWmActLogEntity entity);
    List<BanQinWmActLogEntity> findAsnData(BanQinWmActLogEntity entity);
    List<BanQinWmActLogEntity> findSoData(BanQinWmActLogEntity entity);
    List<BanQinWmActLogEntity> findAdData(BanQinWmActLogEntity entity);
    List<BanQinWmActLogEntity> findTfData(BanQinWmActLogEntity entity);
}