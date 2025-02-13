package com.yunyou.modules.wms.outbound.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoSerial;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoSerialEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 出库序列号MAPPER接口
 *
 * @author WMJ
 * @version 2019-02-14
 */
@MyBatisMapper
public interface BanQinWmSoSerialMapper extends BaseMapper<BanQinWmSoSerial> {
    List<BanQinWmSoSerialEntity> findPage(BanQinWmSoSerialEntity entity);

    void removeByAllocId(@Param("allocId") String allocId, @Param("orgId") String orgId);

    void removeByAllocIdAndNotPack(@Param("allocId") String allocId, @Param("orgId") String orgId);
    
    List<BanQinWmSoSerialEntity> findByAllocIds(@Param("allocIds") List<String> allocIds, @Param("orgId") String orgId);
}