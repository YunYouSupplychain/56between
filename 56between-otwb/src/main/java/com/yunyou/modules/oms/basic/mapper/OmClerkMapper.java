package com.yunyou.modules.oms.basic.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.oms.basic.entity.OmClerk;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 业务员MAPPER接口
 *
 * @author Jianhua Liu
 * @version 2019-07-29
 */
@MyBatisMapper
public interface OmClerkMapper extends BaseMapper<OmClerk> {

    List<OmClerk> findPage(OmClerk entity);

    List<OmClerk> popData(OmClerk entity);

    void remove(@Param("clerkCode") String clerkCode, @Param("orgId") String orgId);
}