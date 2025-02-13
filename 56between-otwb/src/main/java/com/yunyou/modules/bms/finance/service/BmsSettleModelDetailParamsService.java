package com.yunyou.modules.bms.finance.service;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.bms.common.BmsConstants;
import com.yunyou.modules.bms.finance.entity.BmsSettleModelDetailParams;
import com.yunyou.modules.bms.finance.entity.extend.BmsSettleModelDetailParamsEntity;
import com.yunyou.modules.bms.finance.mapper.BmsSettleModelDetailParamsMapper;
import com.yunyou.modules.sys.utils.DictUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 描述：结算模型明细参数Service
 *
 * @author liujianhua
 * @version 2019-11-18
 */
@Service
@Transactional(readOnly = true)
public class BmsSettleModelDetailParamsService extends CrudService<BmsSettleModelDetailParamsMapper, BmsSettleModelDetailParams> {

    /**
     * 描述：根据模型明细ID删除模型明细参数
     *
     * @param fkId 模型明细ID
     */
    @Transactional
    public void deleteByFkId(String fkId) {
        mapper.deleteByFkId(fkId);
    }

    /**
     * 根据模型明细ID查询包含参数
     *
     * @param fkId 模型明细ID
     */
    public List<BmsSettleModelDetailParamsEntity> findIncludeByFkId(String fkId) {
        List<BmsSettleModelDetailParamsEntity> list = mapper.findByFkIdAndIOE(fkId, BmsConstants.INCLUDE);
        return list.stream().peek(o -> {
            if (StringUtils.isNotBlank(o.getFieldOption())) {
                o.setDictValueList(DictUtils.getDictList(o.getFieldOption()));
            }
        }).collect(Collectors.toList());
    }

    /**
     * 根据模型明细ID查询排除参数
     *
     * @param fkId 模型明细ID
     */
    public List<BmsSettleModelDetailParamsEntity> findExcludeByFkId(String fkId) {
        List<BmsSettleModelDetailParamsEntity> list = mapper.findByFkIdAndIOE(fkId, BmsConstants.EXCLUDE);
        return list.stream().peek(o -> {
            if (StringUtils.isNotBlank(o.getFieldOption())) {
                o.setDictValueList(DictUtils.getDictList(o.getFieldOption()));
            }
        }).collect(Collectors.toList());
    }
}
