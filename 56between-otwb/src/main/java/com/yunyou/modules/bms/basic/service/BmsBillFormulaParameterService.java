package com.yunyou.modules.bms.basic.service;

import com.yunyou.core.service.CrudService;
import com.yunyou.modules.bms.basic.entity.BmsBillFormulaParameter;
import com.yunyou.modules.bms.basic.mapper.BmsBillFormulaParameterMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 公式参数Service
 *
 * @author Jianhua Liu
 * @version 2019-06-10
 */
@Service
@Transactional(readOnly = true)
public class BmsBillFormulaParameterService extends CrudService<BmsBillFormulaParameterMapper, BmsBillFormulaParameter> {

    public List<BmsBillFormulaParameter> findByFormulaCode(String formulaCode) {
        BmsBillFormulaParameter parameter = new BmsBillFormulaParameter();
        parameter.setFormulaCode(formulaCode);
        return findList(parameter);
    }

    @Transactional
    public void remove(String formulaCode) {
        mapper.remove(formulaCode);
    }
}