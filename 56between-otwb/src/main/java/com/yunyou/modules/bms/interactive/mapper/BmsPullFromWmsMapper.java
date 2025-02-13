package com.yunyou.modules.bms.interactive.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.bms.business.entity.BmsInboundData;
import com.yunyou.modules.bms.business.entity.BmsInventoryData;
import com.yunyou.modules.bms.business.entity.BmsOutboundData;
import com.yunyou.modules.bms.interactive.BmsPullDataCondition;

import java.util.List;

/**
 * 描述：拉取WMS数据信息Mapper
 *
 * @author liujianhua
 * @since 2021.12.14
 */
@MyBatisMapper
public interface BmsPullFromWmsMapper extends BaseMapper<BmsInboundData> {
    /**
     * 描述：拉取WMS入库数据信息
     *
     * @param condition 条件参数
     * @return 转换后的BMS入库业务数据
     */
    List<BmsInboundData> pullInboundData(BmsPullDataCondition condition);

    /**
     * 描述：拉取WMS出库数据信息
     *
     * @param condition 条件参数
     * @return 转换后的BMS出库业务数据
     */
    List<BmsOutboundData> pullOutboundData(BmsPullDataCondition condition);

    /**
     * 描述：拉取WMS库存数据信息
     *
     * @param condition 条件参数
     * @return 转换后的BMS库存业务数据
     */
    List<BmsInventoryData> pullInventoryData(BmsPullDataCondition condition);
}
