package com.yunyou.modules.bms.basic.entity.extend;

import com.yunyou.modules.bms.basic.entity.BmsContractDetailTermsParams;
import com.yunyou.modules.sys.entity.DictValue;

import java.util.List;

public class BmsContractDetailTermsParamsEntity extends BmsContractDetailTermsParams {

    private String methodName;
    private List<DictValue> dictValueList;

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public List<DictValue> getDictValueList() {
        return dictValueList;
    }

    public void setDictValueList(List<DictValue> dictValueList) {
        this.dictValueList = dictValueList;
    }
}
