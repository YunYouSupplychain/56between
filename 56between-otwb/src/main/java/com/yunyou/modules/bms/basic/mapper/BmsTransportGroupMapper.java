package com.yunyou.modules.bms.basic.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.bms.basic.entity.BmsTransportGroup;
import com.yunyou.modules.bms.basic.entity.extend.BmsTransportGroupEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 描述：运输体系Mapper
 *
 * @author liujianhua created on 2019-11-14
 */
@MyBatisMapper
public interface BmsTransportGroupMapper extends BaseMapper<BmsTransportGroup> {

    BmsTransportGroupEntity getEntity(String id);

    BmsTransportGroupEntity getByCode(@Param("transportGroupCode") String transportGroupCode, @Param("orgId") String orgId);

    List<BmsTransportGroup> findPage(BmsTransportGroupEntity entity);
}
