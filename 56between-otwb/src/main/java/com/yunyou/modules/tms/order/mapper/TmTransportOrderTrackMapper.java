package com.yunyou.modules.tms.order.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.tms.order.entity.TmTransportOrderTrack;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 运输订单跟踪信息表MAPPER接口
 *
 * @author zyf
 * @version 2020-04-10
 */
@MyBatisMapper
public interface TmTransportOrderTrackMapper extends BaseMapper<TmTransportOrderTrack> {

    List<TmTransportOrderTrack> findPage(TmTransportOrderTrack entity);

    void removeReceiveTrackNode(@Param("transportNo") String transportNo, @Param("labelNo") String labelNo, @Param("opNode") String opNode, @Param("outletCode") String outletCode);

    void removeByTransportNo(@Param("transportNo") String transportNo, @Param("orgId") String orgId);

    void removeByTransportNoAndLabelNo(@Param("transportNo") String transportNo, @Param("labelNo") String labelNo, @Param("orgId") String orgId);

    List<TmTransportOrderTrack> findSignList(@Param("transportNo") String transportNo, @Param("orgId") String orgId);
}