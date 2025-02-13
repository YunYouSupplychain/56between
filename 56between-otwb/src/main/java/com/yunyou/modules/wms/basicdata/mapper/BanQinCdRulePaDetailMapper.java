package com.yunyou.modules.wms.basicdata.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdRulePaDetail;
import org.apache.ibatis.annotations.Param;

/**
 * 上架规则明细MAPPER接口
 *
 * @author WMJ
 * @version 2019-01-25
 */
@MyBatisMapper
public interface BanQinCdRulePaDetailMapper extends BaseMapper<BanQinCdRulePaDetail> {

    void deleteByHeaderId(String headerId);

    void remove(@Param("ruleCode") String ruleCode, @Param("orgId") String orgId);
}