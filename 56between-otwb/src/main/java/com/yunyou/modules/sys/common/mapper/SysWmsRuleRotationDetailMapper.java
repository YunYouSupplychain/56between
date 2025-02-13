package com.yunyou.modules.sys.common.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.sys.common.entity.SysWmsRuleRotationDetail;

import java.util.List;

/**
 * 库存周转规则MAPPER接口
 */
@MyBatisMapper
public interface SysWmsRuleRotationDetailMapper extends BaseMapper<SysWmsRuleRotationDetail> {
    List<SysWmsRuleRotationDetail> findPage(SysWmsRuleRotationDetail entity);

    void deleteByHeaderId(String headerId);
}