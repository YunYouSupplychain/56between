package com.yunyou.modules.bms.basic.service;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.bms.basic.entity.BmsTransportSteppedPrice;
import com.yunyou.modules.bms.basic.entity.extend.BmsContractSteppedPrice;
import com.yunyou.modules.bms.basic.mapper.BmsTransportSteppedPriceMapper;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 描述：运输阶梯价格Service
 *
 * @author liujianhua created on 2019-11-14
 */
@Service
@Transactional(readOnly = true)
public class BmsTransportSteppedPriceService extends CrudService<BmsTransportSteppedPriceMapper, BmsTransportSteppedPrice> {

    /**
     * 描述：根据外键ID查询阶梯价格
     */
    public List<BmsTransportSteppedPrice> findByFkId(String fkId) {
        if (StringUtils.isBlank(fkId)) {
            return Lists.newArrayList();
        }
        return mapper.findByFkId(fkId);
    }

    /**
     * 描述：根据外键ID删除阶梯价格
     */
    @Transactional
    public void deleteByFkId(String fkId) {
        if (StringUtils.isBlank(fkId)) {
            return;
        }
        mapper.deleteByFkId(fkId);
    }

    /**
     * 描述：根据外键ID查询阶梯价格
     */
    public List<BmsContractSteppedPrice> findSteppedPriceByFkId(String fkId) {
        if (StringUtils.isBlank(fkId)) {
            return Lists.newArrayList();
        }
        return mapper.findSteppedPriceByFkId(fkId);
    }
}