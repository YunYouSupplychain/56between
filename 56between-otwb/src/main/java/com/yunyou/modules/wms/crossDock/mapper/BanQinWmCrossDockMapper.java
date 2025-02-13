package com.yunyou.modules.wms.crossDock.mapper;

import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.crossDock.entity.*;

import java.util.List;

/**
 * 越库MAPPER接口
 * @author WMJ
 * @version 2020-02-19
 */
@MyBatisMapper
public interface BanQinWmCrossDockMapper {
    List<BanQinGetCdAllocStatusAndOutStepQueryEntity> getCdAllocStatusAndOutStepQueryList(BanQinGetCdAllocStatusAndOutStepQueryEntity entity);
    List<BanQinGetCrossDockAsnDetailReceiveQueryEntity> getCrossDockAsnDetailReceiveQueryList(BanQinGetCrossDockAsnDetailReceiveQueryEntity entity);
    List<BanQinGetCrossDockQueryEntity> getCrossDockQuery(BanQinGetCrossDockQueryEntity entity);
    List<BanQinWmTaskCdByIndirectQueryEntity> wmTaskCdByIndirectQuery(BanQinWmTaskCdByIndirectQueryEntity entity);
    List<BanQinWmTaskCdSoDetailByIndirectQueryEntity> wmTaskCdSoDetailByIndirectQuery(BanQinWmTaskCdSoDetailByIndirectQueryEntity entity);
    List<BanQinGetCrossDockAsnDetaiCountQueryEntity> getCrossDockAsnDetailCountQuery(BanQinGetCrossDockAsnDetaiCountQueryEntity entity);
    List<BanQinGetCrossDockSoDetailCountQueryEntity> getCrossDockSoDetailCountQuery(BanQinGetCrossDockSoDetailCountQueryEntity entity);
    List<BanQinWmTaskCdByDirectQueryEntity> wmTaskCdByDirectQuery(BanQinWmTaskCdByDirectQueryEntity entity);

}