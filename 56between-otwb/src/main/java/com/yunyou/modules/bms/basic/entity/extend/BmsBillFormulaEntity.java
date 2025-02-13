package com.yunyou.modules.bms.basic.entity.extend;

import com.google.common.collect.Lists;
import com.yunyou.modules.bms.basic.entity.BmsBillFormula;
import com.yunyou.modules.bms.basic.entity.BmsBillFormulaParameter;

import java.util.List;

/**
 * 描述：计费公式扩展实体
 *
 * @author Jianhua
 * @version 2019/6/10
 */
public class BmsBillFormulaEntity extends BmsBillFormula {

    private static final long serialVersionUID = -648404325729312597L;
    // 计费公式参数
    private List<BmsBillFormulaParameter> parameters = Lists.newArrayList();

    public List<BmsBillFormulaParameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<BmsBillFormulaParameter> parameters) {
        this.parameters = parameters;
    }

}