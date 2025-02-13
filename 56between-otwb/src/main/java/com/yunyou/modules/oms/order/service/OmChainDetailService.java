package com.yunyou.modules.oms.order.service;

import com.yunyou.core.service.CrudService;
import com.yunyou.modules.oms.order.entity.OmChainDetail;
import com.yunyou.modules.oms.order.mapper.OmChainDetailMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 供应链订单明细Service
 *
 * @author WMJ
 * @version 2019.04.17
 */
@Service
@Transactional(readOnly = true)
public class OmChainDetailService extends CrudService<OmChainDetailMapper, OmChainDetail> {

    public OmChainDetail get(String chainNo, String lineNo, String orgId) {
        return mapper.getByLineNo(chainNo, lineNo, orgId);
    }

    public List<OmChainDetail> getDetails(String chainId) {
        return mapper.findList(new OmChainDetail(null, chainId));
    }

    public String getNewLineNo(String chainId, String orgId) {
        Integer maxLineNo = mapper.getMaxLineNo(chainId, orgId);
        if (maxLineNo == null) {
            maxLineNo = 0;
        }
        return String.format("%04d", maxLineNo + 1);
    }
}
