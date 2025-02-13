package com.yunyou.modules.wms.kit.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.kit.entity.BanQinWmKitStep;
import com.yunyou.modules.wms.kit.entity.extend.BanQinWmKitStepEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 加工工序MAPPER接口
 *
 * @author Jianhua Liu
 * @version 2019-08-20
 */
@MyBatisMapper
public interface BanQinWmKitStepMapper extends BaseMapper<BanQinWmKitStep> {

    Long getMaxLineNo(@Param("kitNo") String kitNo, @Param("kitLineNo") String kitLineNo, @Param("orgId") String orgId);

    List<BanQinWmKitStepEntity> getEntityByKitNoAndKitLineNo(@Param("kitNo") String kitNo, @Param("kitLineNos") String[] kitLineNos, @Param("orgId") String orgId);
}