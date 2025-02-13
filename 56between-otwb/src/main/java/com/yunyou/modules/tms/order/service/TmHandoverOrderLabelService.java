package com.yunyou.modules.tms.order.service;

import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunyou.common.utils.IdGen;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.sys.entity.User;
import com.yunyou.modules.tms.order.entity.TmHandoverOrderLabel;
import com.yunyou.modules.tms.order.mapper.TmHandoverOrderLabelMapper;

/**
 * 交接单标签Service
 * @author zyf
 * @version 2020-03-30
 */
@Service
@Transactional(readOnly = true)
public class TmHandoverOrderLabelService extends CrudService<TmHandoverOrderLabelMapper, TmHandoverOrderLabel> {

	@Transactional
	public void saveByUser(TmHandoverOrderLabel entity, User user) {
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