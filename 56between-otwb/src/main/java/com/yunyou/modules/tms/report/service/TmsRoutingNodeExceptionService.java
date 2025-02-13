package com.yunyou.modules.tms.report.service;

import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.tms.report.entity.TmsRoutingNodeException;
import com.yunyou.modules.tms.report.mapper.TmsRoutingNodeExceptionMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 路由节点异常Service
 * @author WMJ
 * @version 2020-03-23
 */
@Service
@Transactional(readOnly = true)
public class TmsRoutingNodeExceptionService extends CrudService<TmsRoutingNodeExceptionMapper, TmsRoutingNodeException> {

	public TmsRoutingNodeException get(String id) {
		return super.get(id);
	}
	
	public List<TmsRoutingNodeException> findList(TmsRoutingNodeException exception) {
		return super.findList(exception);
	}
	
	public Page<TmsRoutingNodeException> findPage(Page<TmsRoutingNodeException> page, TmsRoutingNodeException exception) {
		exception.setPage(page);
		page.setList(mapper.findPage(exception));
		return page;
	}
	
	@Transactional
	public void save(TmsRoutingNodeException exception) {
		super.save(exception);
	}
	
	@Transactional
	public void delete(TmsRoutingNodeException exception) {
		super.delete(exception);
	}

	public void saveInfo(String orderNo, String trackingNo, String type, String message) {
		TmsRoutingNodeException exception = new TmsRoutingNodeException();
		exception.setOrderNo(orderNo);
		exception.setTrackingNo(trackingNo);
		exception.setType(type);
		exception.setMessage(message);
		this.save(exception);
	}
	
}