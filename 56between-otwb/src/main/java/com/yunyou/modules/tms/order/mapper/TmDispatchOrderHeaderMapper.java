package com.yunyou.modules.tms.order.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.tms.order.entity.TmDispatchOrderHeader;
import org.apache.ibatis.annotations.Param;

/**
 * 派车单MAPPER接口
 *
 * @author liujianhua
 * @version 2020-03-11
 */
@MyBatisMapper
public interface TmDispatchOrderHeaderMapper extends BaseMapper<TmDispatchOrderHeader> {

    TmDispatchOrderHeader getByNo(@Param("dispatchNo") String dispatchNo);
}