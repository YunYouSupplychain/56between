package com.yunyou.modules.bms.basic.service;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.bms.basic.entity.BmsBillFormula;
import com.yunyou.modules.bms.basic.entity.BmsBillFormulaParameter;
import com.yunyou.modules.bms.basic.entity.extend.BmsBillFormulaEntity;
import com.yunyou.modules.bms.basic.mapper.BmsBillFormulaMapper;
import com.yunyou.modules.bms.common.BmsException;
import com.yunyou.modules.sys.utils.DictUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 计费公式Service
 *
 * @author Jianhua Liu
 * @version 2019-05-29
 */
@Service
@Transactional(readOnly = true)
public class BmsBillFormulaService extends CrudService<BmsBillFormulaMapper, BmsBillFormula> {
    @Autowired
    private BmsBillFormulaParameterService bmsBillFormulaParameterService;

    public BmsBillFormulaEntity getEntity(String id) {
        BmsBillFormulaEntity entity = mapper.getEntity(id);
        if (entity != null) {
            entity.setParameters(bmsBillFormulaParameterService.findByFormulaCode(entity.getFormulaCode()));
        }
        return entity;
    }

    public Page<BmsBillFormula> findPage(Page<BmsBillFormula> page, BmsBillFormulaEntity entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    public BmsBillFormulaEntity getByCode(String formulaCode) {
        BmsBillFormulaEntity entity = mapper.getByCode(formulaCode);
        if (entity != null) {
            entity.setParameters(bmsBillFormulaParameterService.findByFormulaCode(formulaCode));
        }
        return entity;
    }

    public void checkSaveBefore(BmsBillFormulaEntity entity) {
        if (StringUtils.isBlank(entity.getFormulaCode())) {
            throw new BmsException("公式编码不能为空");
        }
        if (StringUtils.isBlank(entity.getFormula())) {
            throw new BmsException("公式不能为空");
        }
    }

    public String getFormulaName(BmsBillFormulaEntity entity) {
        String formula = entity.getFormula();
        for (BmsBillFormulaParameter o : entity.getParameters()) {
            formula = formula.replace(o.getParameterName(), DictUtils.getDictLabel(o.getParameterValue(), "BMS_BILL_FORMULA_PARAM", ""));
        }
        return formula;
    }

    @Transactional
    public void saveEntity(BmsBillFormulaEntity entity) {
        this.checkSaveBefore(entity);
        entity.setFormulaName(this.getFormulaName(entity));
        this.save(entity);

        bmsBillFormulaParameterService.remove(entity.getFormulaCode());
        for (BmsBillFormulaParameter parameter : entity.getParameters()) {
            if (parameter.getId() == null) {
                continue;
            }
            parameter.setId(null);
            parameter.setFormulaCode(entity.getFormulaCode());
            bmsBillFormulaParameterService.save(parameter);
        }
    }

    @Override
    @Transactional
    public void delete(BmsBillFormula entity) {
        bmsBillFormulaParameterService.remove(entity.getFormulaCode());
        super.delete(entity);
    }

    @Transactional
    public void remove(String formulaCode) {
        bmsBillFormulaParameterService.remove(formulaCode);
        mapper.remove(formulaCode);
    }
}