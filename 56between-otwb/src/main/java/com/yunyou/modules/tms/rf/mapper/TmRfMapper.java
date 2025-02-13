package com.yunyou.modules.tms.rf.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.tms.order.entity.extend.TmDispatchOrderLabelEntity;
import com.yunyou.modules.tms.order.entity.extend.TmTransportOrderLabelEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@MyBatisMapper
public interface TmRfMapper extends BaseMapper<TmRfMapper> {

    /*根据栈板号查找标签*/
    List<TmTransportOrderLabelEntity> findCanLoadLabelByStackBarCode(@Param("stackBarCodes") List<String> stackBarCodes, @Param("orgId") String orgId);

    /*根据栈板号查找标签*/
    List<TmDispatchOrderLabelEntity> findDispatchLabelByStackBarCode(@Param("stackBarCode") String stackBarCode, @Param("orgId") String orgId);
}
