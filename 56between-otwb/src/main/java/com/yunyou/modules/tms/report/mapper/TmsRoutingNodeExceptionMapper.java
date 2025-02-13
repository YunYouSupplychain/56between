package com.yunyou.modules.tms.report.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.tms.report.entity.TmsRoutingNodeException;

import java.util.List;

/**
 * 路由节点异常Entity
 * @author WMJ
 * @version 2020-03-23
 */
@MyBatisMapper
public interface TmsRoutingNodeExceptionMapper extends BaseMapper<TmsRoutingNodeException> {
	List<TmsRoutingNodeException> findPage(TmsRoutingNodeException exception);
}