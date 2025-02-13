package com.yunyou.modules.wms.basicdata.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdRuleRotationDetail;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdRuleRotationDetailEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 库存周转规则MAPPER接口
 *
 * @author WMJ
 * @version 2019-01-25
 */
@MyBatisMapper
public interface BanQinCdRuleRotationDetailMapper extends BaseMapper<BanQinCdRuleRotationDetail> {

    void deleteByHeaderId(String headerId);

    List<BanQinCdRuleRotationDetailEntity> findPage(BanQinCdRuleRotationDetailEntity entity);

    void remove(@Param("ruleCode") String ruleCode, @Param("orgId") String orgId);
}