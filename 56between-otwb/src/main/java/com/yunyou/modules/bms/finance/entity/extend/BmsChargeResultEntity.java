package com.yunyou.modules.bms.finance.entity.extend;

import com.yunyou.modules.bms.finance.entity.BmsChargeResult;

import java.util.List;

/**
 * 费用结果Entity
 *
 * @author liujianhua
 * @version 2022.8.11
 */
public class BmsChargeResultEntity extends BmsChargeResult {

    /**
     * 费用明细ID
     */
    private List<String> chargeDetailIds;

    public List<String> getChargeDetailIds() {
        return chargeDetailIds;
    }

    public void setChargeDetailIds(List<String> chargeDetailIds) {
        this.chargeDetailIds = chargeDetailIds;
    }
}
