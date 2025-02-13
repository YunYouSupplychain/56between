package com.yunyou.modules.wms.basicdata.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdRuleWvDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 波次规则MAPPER接口
 *
 * @author WMJ
 * @version 2019-01-25
 */
@MyBatisMapper
public interface BanQinCdRuleWvDetailMapper extends BaseMapper<BanQinCdRuleWvDetail> {
    void deleteByHeaderId(String headerId);

    List<BanQinCdRuleWvDetail> getByHeaderId(String headerId);

    void remove(@Param("ruleCode") String ruleCode, @Param("orgId") String orgId);
}