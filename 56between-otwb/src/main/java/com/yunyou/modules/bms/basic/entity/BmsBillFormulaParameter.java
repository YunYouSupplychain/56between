package com.yunyou.modules.bms.basic.entity;

import com.yunyou.core.persistence.DataEntity;

/**
 * 公式参数Entity
 *
 * @author Jianhua Liu
 * @version 2019-06-10
 */
public class BmsBillFormulaParameter extends DataEntity<BmsBillFormulaParameter> {

    private static final long serialVersionUID = 1L;
    // 公式编码
    private String formulaCode;
    // 参数名(固定以p开头后接数字，如：p1,p2,p3....)
    private String parameterName;
    // 参数值
    private String parameterValue;

    public BmsBillFormulaParameter() {
        super();
    }

    public BmsBillFormulaParameter(String id) {
        super(id);
    }

    public String getFormulaCode() {
        return formulaCode;
    }

    public void setFormulaCode(String formulaCode) {
        this.formulaCode = formulaCode;
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public String getParameterValue() {
        return parameterValue;
    }

    public void setParameterValue(String parameterValue) {
        this.parameterValue = parameterValue;
    }

}