package com.yunyou.modules.bms.basic.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.bms.basic.entity.BmsBillFormulaParameter;
import org.apache.ibatis.annotations.Param;

/**
 * 公式参数MAPPER接口
 *
 * @author Jianhua Liu
 * @version 2019-06-10
 */
@MyBatisMapper
public interface BmsBillFormulaParameterMapper extends BaseMapper<BmsBillFormulaParameter> {

    void remove(@Param("formulaCode") String formulaCode);
}