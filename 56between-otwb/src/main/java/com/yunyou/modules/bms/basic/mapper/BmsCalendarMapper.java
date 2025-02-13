package com.yunyou.modules.bms.basic.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.bms.basic.entity.BmsCalendar;
import com.yunyou.modules.bms.basic.entity.extend.BmsCalendarEntity;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@MyBatisMapper
public interface BmsCalendarMapper extends BaseMapper<BmsCalendar> {

    List<BmsCalendarEntity> findPage(BmsCalendarEntity qEntity);

    BmsCalendarEntity getEntity(@Param("id") String id);

    BmsCalendar getByDate(@Param("date") Date date, @Param("orgId") String orgId);
}
