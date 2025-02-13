package com.yunyou.modules.wms.basicdata.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdRuleRotationHeader;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdRuleRotationHeaderEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 库存周转规则MAPPER接口
 *
 * @author WMJ
 * @version 2019-01-25
 */
@MyBatisMapper
public interface BanQinCdRuleRotationHeaderMapper extends BaseMapper<BanQinCdRuleRotationHeader> {
    BanQinCdRuleRotationHeaderEntity getEntity(String id);

    List<BanQinCdRuleRotationHeaderEntity> findPage(BanQinCdRuleRotationHeaderEntity entity);

    void remove(@Param("ruleCode") String ruleCode, @Param("orgId") String orgId);
}