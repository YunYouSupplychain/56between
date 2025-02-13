package com.yunyou.modules.tms.order.service;

import com.yunyou.common.utils.IdGen;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.sys.entity.User;
import com.yunyou.modules.tms.order.entity.TmHandoverOrderSku;
import com.yunyou.modules.tms.order.mapper.TmHandoverOrderSkuMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 交接单标签Service
 * @author zyf
 * @version 2020-03-30
 */
@Service
@Transactional(readOnly = true)
public class TmHandoverOrderSkuService extends CrudService<TmHandoverOrderSkuMapper, TmHandoverOrderSku> {

	@Transactional
	public void saveByUser(TmHandoverOrderSku entity, User user) {
		if (entity.getIsNewRecord()){
			entity.setId(IdGen.uuid());
			entity.setCreateBy(user);
			entity.setCreateDate(new Date());
			entity.setUpdateBy(user);
			entity.setUpdateDate(new Date());
			mapper.insert(entity);
		}else{
			entity.setUpdateBy(user);
			entity.setUpdateDate(new Date());
			mapper.update(entity);
		}
	}
}