package com.yunyou.modules.wms.inbound.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.inbound.entity.BanQinWmAsnDetailReceives;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 收货箱明细MAPPER接口
 *
 * @author WMJ
 * @version 2019-01-25
 */
@MyBatisMapper
public interface BanQinWmAsnDetailReceivesMapper extends BaseMapper<BanQinWmAsnDetailReceives> {

    void removeByReceiveId(@Param("receiveIds") List<String> receiveIds);
}