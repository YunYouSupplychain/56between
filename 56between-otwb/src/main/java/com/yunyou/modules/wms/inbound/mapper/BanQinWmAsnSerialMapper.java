package com.yunyou.modules.wms.inbound.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.inbound.entity.BanQinWmAsnSerial;
import com.yunyou.modules.wms.inbound.entity.BanQinWmAsnSerialEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 入库序列MAPPER接口
 *
 * @author WMJ
 * @version 2019-01-25
 */
@MyBatisMapper
public interface BanQinWmAsnSerialMapper extends BaseMapper<BanQinWmAsnSerial> {
    List<BanQinWmAsnSerialEntity> findPage(BanQinWmAsnSerialEntity entity);

    void removeByAsnNo(@Param("asnNo") String asnNo, @Param("orgId") String orgId);

    void removeByAsnNoAndRcvLineNo(@Param("asnNo") String asnNo, @Param("lineNo") String lineNo, @Param("orgId") String orgId);

    void updateSerialForCancelRcv(@Param("asnNo") String asnNo, @Param("lineNo") String lineNo, @Param("orgId") String orgId);

    List<BanQinWmAsnSerial> checkAsnSerialQuery(@Param("asnNo") String asnNo, @Param("orgId") String orgId, @Param("serialNoAndSkuCode") String serialNoAndSkuCode);
}