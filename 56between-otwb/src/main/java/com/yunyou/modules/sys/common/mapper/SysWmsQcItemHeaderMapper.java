package com.yunyou.modules.sys.common.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.sys.common.entity.SysWmsQcItemHeader;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 质检项MAPPER接口
 */
@MyBatisMapper
public interface SysWmsQcItemHeaderMapper extends BaseMapper<SysWmsQcItemHeader> {
    List<SysWmsQcItemHeader> findPage(SysWmsQcItemHeader entity);

    List<SysWmsQcItemHeader> findGrid(SysWmsQcItemHeader entity);

    List<SysWmsQcItemHeader> findSync(SysWmsQcItemHeader entity);

    SysWmsQcItemHeader getByCode(@Param("itemGroupCode") String itemGroupCode, @Param("dataSet") String dataSet);
}