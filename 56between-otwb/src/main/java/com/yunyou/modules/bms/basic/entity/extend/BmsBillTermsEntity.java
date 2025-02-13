package com.yunyou.modules.bms.basic.entity.extend;

import com.google.common.collect.Lists;
import com.yunyou.modules.bms.basic.entity.BmsBillTerms;
import com.yunyou.modules.bms.basic.entity.BmsBillTermsParameter;

import java.util.List;

/**
 * 描述：计费条款扩展实体
 *
 * @author Jianhua
 * @version 2019/5/27
 */
public class BmsBillTermsEntity extends BmsBillTerms {

    private static final long serialVersionUID = -8913638276492782302L;
    // 计费条款参数
    private List<BmsBillTermsParameter> parameters = Lists.newArrayList();

    public List<BmsBillTermsParameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<BmsBillTermsParameter> parameters) {
        this.parameters = parameters;
    }
}
