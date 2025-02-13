package com.yunyou.modules.tms.order.entity.extend;

import com.yunyou.modules.tms.order.entity.TmExceptionHandleBillFee;

public class TmExceptionHandleBillFeeEntity extends TmExceptionHandleBillFee {
    private static final long serialVersionUID = 1L;

    public TmExceptionHandleBillFeeEntity() {
        super();
    }

    public TmExceptionHandleBillFeeEntity(String id) {
        super(id);
    }


    public TmExceptionHandleBillFeeEntity(String billNo, String orgId) {
        super(billNo, orgId);
    }


}
