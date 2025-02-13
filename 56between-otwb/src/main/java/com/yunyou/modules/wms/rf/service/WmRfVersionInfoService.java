package com.yunyou.modules.wms.rf.service;

import com.yunyou.core.service.CrudService;
import com.yunyou.modules.wms.rf.entity.WmRfVersionInfo;
import com.yunyou.modules.wms.rf.mapper.WmRfVersionInfoMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * RF版本信息Service
 * @author WMJ
 * @version 2019-08-15
 */
@Service
@Transactional(readOnly = true)
public class WmRfVersionInfoService extends CrudService<WmRfVersionInfoMapper, WmRfVersionInfo> {

    public WmRfVersionInfo getLastVersionInfo() {
        return mapper.getLastVersionInfo();
    }

    public WmRfVersionInfo getLastVersionInfo(String appId) {
        return mapper.getLastByAppId(appId);
    }

}