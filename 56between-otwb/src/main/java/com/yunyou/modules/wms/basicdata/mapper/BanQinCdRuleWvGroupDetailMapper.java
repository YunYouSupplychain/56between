package com.yunyou.modules.wms.basicdata.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdRuleWvGroupDetail;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdRuleWvGroupDetailEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 波次规则组明细MAPPER接口
 *
 * @author WMJ
 * @version 2020-02-09
 */
@MyBatisMapper
public interface BanQinCdRuleWvGroupDetailMapper extends BaseMapper<BanQinCdRuleWvGroupDetail> {
    List<BanQinCdRuleWvGroupDetailEntity> findGrid(BanQinCdRuleWvGroupDetail detail);

    void remove(@Param("groupCode") String groupCode, @Param("orgId") String orgId);
}