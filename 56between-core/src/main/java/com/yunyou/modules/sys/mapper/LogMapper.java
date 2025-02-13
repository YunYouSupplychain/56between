package com.yunyou.modules.sys.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.sys.entity.Log;

/**
 * 日志MAPPER接口
 * @author yunyou
 * @version 2017-05-16
 */
@MyBatisMapper
public interface LogMapper extends BaseMapper<Log> {

	void empty();
}
