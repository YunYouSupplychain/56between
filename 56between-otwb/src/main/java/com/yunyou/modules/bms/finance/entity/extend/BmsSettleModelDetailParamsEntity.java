package com.yunyou.modules.bms.finance.entity.extend;

import com.yunyou.modules.bms.finance.entity.BmsSettleModelDetailParams;
import com.yunyou.modules.sys.entity.DictValue;

import java.util.List;

/**
 * 描述：结算模型明细参数
 *
 * @author Jianhua
 */
public class BmsSettleModelDetailParamsEntity extends BmsSettleModelDetailParams {

    private List<DictValue> dictValueList;

    public List<DictValue> getDictValueList() {
        return dictValueList;
    }

    public void setDictValueList(List<DictValue> dictValueList) {
        this.dictValueList = dictValueList;
    }
}
