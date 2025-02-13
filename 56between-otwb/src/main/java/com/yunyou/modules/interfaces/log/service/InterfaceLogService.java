package com.yunyou.modules.interfaces.log.service;

import com.yunyou.common.utils.IdGen;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.interfaces.log.entity.InterfaceLog;
import com.yunyou.modules.interfaces.log.mapper.InterfaceLogMapper;
import com.yunyou.modules.sys.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 接口日志Service
 * @author zyf
 * @version 2019-12-19
 */
@Service
@Transactional(readOnly = true)
public class InterfaceLogService extends CrudService<InterfaceLogMapper, InterfaceLog> {

	@Transactional
	public void saveLog(String type, String isSuccess, String message, String requestData, String responseData, String searchNo, User user, String handleDir){
		InterfaceLog log = new InterfaceLog();
		log.setId(IdGen.uuid());
		log.setCreateBy(user);
		log.setCreateDate(new Date());
		log.setUpdateBy(user);
		log.setUpdateDate(new Date());
		log.setInterfaceType(type);
		log.setIsSuccess(isSuccess);
		log.setMessage(message);
		log.setRequestData(requestData);
		log.setResponseData(responseData);
		log.setSearchNo(searchNo);
		log.setHandleDirection(handleDir);
		mapper.insert(log);
	}

	@Transactional(propagation= Propagation.REQUIRES_NEW)
	public void saveLogNew(String type, String isSuccess, String message, String requestData, String responseData, String searchNo, User user, String handleDir){
		InterfaceLog log = new InterfaceLog();
		log.setId(IdGen.uuid());
		log.setCreateBy(user);
		log.setCreateDate(new Date());
		log.setUpdateBy(user);
		log.setUpdateDate(new Date());
		log.setInterfaceType(type);
		log.setIsSuccess(isSuccess);
		log.setMessage(message);
		log.setRequestData(requestData);
		log.setResponseData(responseData);
		log.setSearchNo(searchNo);
		log.setHandleDirection(handleDir);
		mapper.insert(log);
	}
}