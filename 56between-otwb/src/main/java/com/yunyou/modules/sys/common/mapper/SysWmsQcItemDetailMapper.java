package com.yunyou.modules.sys.common.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.sys.common.entity.SysWmsQcItemDetail;

import java.util.List;

/**
 * 质检项MAPPER接口
 */
@MyBatisMapper
public interface SysWmsQcItemDetailMapper extends BaseMapper<SysWmsQcItemDetail> {
    List<SysWmsQcItemDetail> findPage(SysWmsQcItemDetail entity);

    void deleteByHeaderId(String headerId);
}