package com.yunyou.modules.tms.order.mapper;

import org.apache.ibatis.annotations.Param;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.tms.order.entity.TmRepairOrderHeader;

@MyBatisMapper
public interface TmRepairOrderHeaderMapper extends BaseMapper<TmRepairOrderHeader> {
    TmRepairOrderHeader getByNo(@Param("repairNo") String repairNo);
}
