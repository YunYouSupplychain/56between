package com.yunyou.modules.sys.common.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.sys.common.entity.SysWmsLotDetail;

import java.util.List;

/**
 * 批次属性MAPPER接口
 */
@MyBatisMapper
public interface SysWmsLotDetailMapper extends BaseMapper<SysWmsLotDetail> {
    List<SysWmsLotDetail> findPage(SysWmsLotDetail entity);
    List<SysWmsLotDetail> findGrid(SysWmsLotDetail entity);

    void deleteByHeaderId(String headerId);
}