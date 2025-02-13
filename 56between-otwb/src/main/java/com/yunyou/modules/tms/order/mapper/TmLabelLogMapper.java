package com.yunyou.modules.tms.order.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.tms.order.entity.TmLabelLog;
import com.yunyou.modules.tms.order.entity.extend.TmLabelLogEntity;

import java.util.List;

/**
 * 标签履历Mapper
 *
 * @author 刘剑华
 * @since 2023/11/16 14:04
 */
@MyBatisMapper
public interface TmLabelLogMapper extends BaseMapper<TmLabelLog> {

    List<TmLabelLog> findPage(TmLabelLogEntity entity);
}
