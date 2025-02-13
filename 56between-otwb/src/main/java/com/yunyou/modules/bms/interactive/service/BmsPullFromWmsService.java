package com.yunyou.modules.bms.interactive.service;

import com.yunyou.core.service.BaseService;
import com.yunyou.modules.bms.business.entity.BmsInboundData;
import com.yunyou.modules.bms.business.entity.BmsInventoryData;
import com.yunyou.modules.bms.business.entity.BmsOutboundData;
import com.yunyou.modules.bms.interactive.BmsPullDataCondition;
import com.yunyou.modules.bms.interactive.mapper.BmsPullFromWmsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 描述：BMS拉取WMS数据Service
 *
 * @auther: Jianhua on 2019/6/3
 */
@Service
@Transactional(readOnly = true)
public class BmsPullFromWmsService extends BaseService {
    @Autowired
    private BmsPullFromWmsMapper mapper;

    /**
     * 描述：拉取入库数据
     */
    public List<BmsInboundData> pullInboundData(BmsPullDataCondition condition) {
        return mapper.pullInboundData(condition);
    }

    /**
     * 描述：拉取出库数据
     */
    public List<BmsOutboundData> pullOutboundData(BmsPullDataCondition condition) {
        return mapper.pullOutboundData(condition);
    }

    /**
     * 描述：拉取库存数据
     */
    public List<BmsInventoryData> pullInventoryData(BmsPullDataCondition condition) {
        return mapper.pullInventoryData(condition);
    }

}
