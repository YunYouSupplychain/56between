package com.yunyou.modules.tms.basic.entity.extend;

import com.yunyou.modules.tms.basic.entity.TmFitting;

public class TmFittingEntity extends TmFitting {
    private static final long serialVersionUID = -4413834319888396327L;

    /*多字段匹配查询*/
    private String codeAndName;

    public String getCodeAndName() {
        return codeAndName;
    }

    public void setCodeAndName(String codeAndName) {
        this.codeAndName = codeAndName;
    }
}
