package com.yunyou.modules.wms.rf.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.rf.entity.WmRfVersionInfo;

/**
 * RF版本信息MAPPER接口
 * @author WMJ
 * @version 2019-08-15
 */
@MyBatisMapper
public interface WmRfVersionInfoMapper extends BaseMapper<WmRfVersionInfo> {
    WmRfVersionInfo getLastVersionInfo();
    WmRfVersionInfo getLastByAppId(String appId);
}