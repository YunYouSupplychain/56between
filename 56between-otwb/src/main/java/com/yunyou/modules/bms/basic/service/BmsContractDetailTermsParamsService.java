package com.yunyou.modules.bms.basic.service;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.bms.basic.entity.BmsContractDetailTermsParams;
import com.yunyou.modules.bms.basic.entity.extend.BmsContractDetailTermsParamsEntity;
import com.yunyou.modules.bms.basic.mapper.BmsContractDetailTermsParamsMapper;
import com.yunyou.modules.bms.common.BmsConstants;
import com.yunyou.modules.sys.utils.DictUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 合同明细参数Service
 *
 * @author liujianhua
 * @version 2022-04-28
 */
@Service
@Transactional(readOnly = true)
public class BmsContractDetailTermsParamsService extends CrudService<BmsContractDetailTermsParamsMapper, BmsContractDetailTermsParams> {

    /**
     * 根据合同明细ID删除合同明细条款参数
     *
     * @param fkId 合同明细ID
     */
    @Transactional
    public void deleteByFkId(String fkId) {
        mapper.deleteByFkId(fkId);
    }

    /**
     * 根据合同明细ID删除合同明细条款参数
     *
     * @param sysContractNo 合同号
     * @param orgId         机构ID
     */
    @Transactional
    public void deleteByContract(String sysContractNo, String orgId) {
        mapper.deleteByContract(sysContractNo, orgId);
    }

    /**
     * 根据合同明细ID查询包含参数
     *
     * @param fkId 合同明细ID
     */
    public List<BmsContractDetailTermsParamsEntity> findIncludeByFkId(String fkId) {
        List<BmsContractDetailTermsParamsEntity> list = mapper.findByFkIdAndIOE(fkId, BmsConstants.INCLUDE);
        return list.stream().peek(o -> {
            if (StringUtils.isNotBlank(o.getFieldOption())) {
                o.setDictValueList(DictUtils.getDictList(o.getFieldOption()));
            }
        }).collect(Collectors.toList());
    }

    /**
     * 根据合同明细ID查询排除参数
     *
     * @param fkId 合同明细ID
     */
    public List<BmsContractDetailTermsParamsEntity> findExcludeByFkId(String fkId) {
        List<BmsContractDetailTermsParamsEntity> list = mapper.findByFkIdAndIOE(fkId, BmsConstants.EXCLUDE);
        return list.stream().peek(o -> {
            if (StringUtils.isNotBlank(o.getFieldOption())) {
                o.setDictValueList(DictUtils.getDictList(o.getFieldOption()));
            }
        }).collect(Collectors.toList());
    }
}
