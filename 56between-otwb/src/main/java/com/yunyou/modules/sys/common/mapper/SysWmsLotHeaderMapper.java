package com.yunyou.modules.sys.common.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.sys.common.entity.SysWmsLotHeader;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 批次属性MAPPER接口
 */
@MyBatisMapper
public interface SysWmsLotHeaderMapper extends BaseMapper<SysWmsLotHeader> {
    List<SysWmsLotHeader> findPage(SysWmsLotHeader entity);

    List<SysWmsLotHeader> findGrid(SysWmsLotHeader entity);

    List<SysWmsLotHeader> findSync(SysWmsLotHeader entity);

    SysWmsLotHeader getByCode(@Param("lotCode") String lotCode, @Param("dataSet") String dataSet);
}