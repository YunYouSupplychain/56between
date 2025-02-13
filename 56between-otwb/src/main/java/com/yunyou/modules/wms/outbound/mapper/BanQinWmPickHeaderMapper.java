package com.yunyou.modules.wms.outbound.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.outbound.entity.BanQinWmPickHeader;
import com.yunyou.modules.wms.outbound.entity.BanQinWmPickHeaderEntity;
import com.yunyou.modules.wms.report.entity.WmPickOrder;

import java.util.List;

/**
 * 拣货单MAPPER接口
 * @author ZYF
 * @version 2020-05-13
 */
@MyBatisMapper
public interface BanQinWmPickHeaderMapper extends BaseMapper<BanQinWmPickHeader> {

    BanQinWmPickHeaderEntity getEntity(String id);

    List<BanQinWmPickHeaderEntity> findPage(BanQinWmPickHeaderEntity entity);

    List<WmPickOrder> getPickOrder(List<String> list);
}