package com.yunyou.modules.bms.basic.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.bms.basic.entity.BmsBillFormula;
import com.yunyou.modules.bms.basic.entity.extend.BmsBillFormulaEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 计费公式MAPPER接口
 *
 * @author Jianhua Liu
 * @version 2019-05-29
 */
@MyBatisMapper
public interface BmsBillFormulaMapper extends BaseMapper<BmsBillFormula> {

    BmsBillFormulaEntity getEntity(String id);

    BmsBillFormulaEntity getByCode(@Param("formulaCode") String formulaCode);

    List<BmsBillFormula> findPage(BmsBillFormulaEntity entity);

    void remove(@Param("formulaCode") String formulaCode);
}