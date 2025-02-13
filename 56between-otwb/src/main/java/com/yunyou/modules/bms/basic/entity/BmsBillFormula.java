package com.yunyou.modules.bms.basic.entity;

import com.yunyou.core.persistence.DataEntity;

/**
 * 计费公式Entity
 *
 * @author Jianhua Liu
 * @version 2019-05-29
 */
public class BmsBillFormula extends DataEntity<BmsBillFormula> {

    private static final long serialVersionUID = 1L;
    // 公式编码
    private String formulaCode;
    // 公式名称
    private String formulaName;
    // 公式
    private String formula;

    public BmsBillFormula() {
        super();
    }

    public BmsBillFormula(String id) {
        super(id);
    }

    public String getFormulaCode() {
        return formulaCode;
    }

    public void setFormulaCode(String formulaCode) {
        this.formulaCode = formulaCode;
    }

    public String getFormulaName() {
        return formulaName;
    }

    public void setFormulaName(String formulaName) {
        this.formulaName = formulaName;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

}