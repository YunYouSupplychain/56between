package com.yunyou.modules.bms.basic.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.bms.basic.entity.BmsBillTermsParameter;
import com.yunyou.modules.bms.basic.entity.extend.BmsContractDetailTermsParamsEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 计费条款参数MAPPER接口
 *
 * @author Jianhua Liu
 * @version 2019-05-27
 */
@MyBatisMapper
public interface BmsBillTermsParameterMapper extends BaseMapper<BmsBillTermsParameter> {

    List<BmsBillTermsParameter> findByBillTermsCode(@Param("billTermsCode") String billTermsCode);

    void remove(@Param("billTermsCode") String billTermsCode);

    List<BmsContractDetailTermsParamsEntity> getTermsParams(@Param("billTermsCode") String billTermsCode);
}