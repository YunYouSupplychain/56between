package com.yunyou.modules.wms.basicdata.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdTrackingInfo;

import java.util.List;

/**
 * 快递接口信息MAPPER接口
 * @author WMJ
 * @version 2020-05-06
 */
@MyBatisMapper
public interface BanQinCdTrackingInfoMapper extends BaseMapper<BanQinCdTrackingInfo> {
	List<BanQinCdTrackingInfo> findPage(BanQinCdTrackingInfo info);
}