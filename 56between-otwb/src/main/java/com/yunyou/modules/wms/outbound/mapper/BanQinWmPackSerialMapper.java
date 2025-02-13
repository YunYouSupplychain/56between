package com.yunyou.modules.wms.outbound.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.outbound.entity.BanQinWmPackSerial;
import com.yunyou.modules.wms.outbound.entity.BanQinWmPackSerialEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 打包序列号MAPPER接口
 *
 * @author WMJ
 * @version 2020-05-13
 */
@MyBatisMapper
public interface BanQinWmPackSerialMapper extends BaseMapper<BanQinWmPackSerial> {
    void removeByAllocId(@Param("allocId") String alloc, @Param("orgId") String orgId);
    List<BanQinWmPackSerialEntity> findPage(BanQinWmPackSerialEntity entity);
}