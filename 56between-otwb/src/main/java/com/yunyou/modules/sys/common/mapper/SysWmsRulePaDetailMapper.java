package com.yunyou.modules.sys.common.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.sys.common.entity.SysWmsRulePaDetail;

/**
 * 上架规则明细MAPPER接口
 */
@MyBatisMapper
public interface SysWmsRulePaDetailMapper extends BaseMapper<SysWmsRulePaDetail> {
    void deleteByHeaderId(String headerId);
}