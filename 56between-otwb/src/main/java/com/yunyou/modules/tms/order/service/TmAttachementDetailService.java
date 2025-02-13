package com.yunyou.modules.tms.order.service;

import com.yunyou.modules.sys.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunyou.core.service.CrudService;
import com.yunyou.modules.tms.order.entity.TmAttachementDetail;
import com.yunyou.modules.tms.order.mapper.TmAttachementDetailMapper;

import java.util.Date;

/**
 * 附件信息Service
 * @author zyf
 * @version 2020-04-07
 */
@Service
@Transactional(readOnly = true)
public class TmAttachementDetailService extends CrudService<TmAttachementDetailMapper, TmAttachementDetail> {

	public void deleteByOrderNo(String orderNo, String orderType, String orgId) {
		mapper.deleteByOrderNo(orderNo, orderType, orgId);
	}

	/**
	 * 保存APP上传的照片信息
	 */
	@Transactional
	public void saveAppImgDetail(User user, String orderNo, String orderType, String labelNo, String orgId, String fileName, String imgFilePath, String imgUrl) {
		TmAttachementDetail entity = new TmAttachementDetail();
		entity.setOrderNo(orderNo);
		entity.setOrderType(orderType);
		entity.setLabelNo(labelNo);
		entity.setUploadPerson(user.getLoginName());
		entity.setUploadTime(new Date());
		entity.setUploadPath(imgFilePath);
		entity.setFileName(fileName);
		entity.setFileUrl(imgUrl + imgFilePath);
		entity.setOrgId(orgId);
		entity.setCreateBy(user);
		entity.setUpdateBy(user);
		super.save(entity);
	}
}