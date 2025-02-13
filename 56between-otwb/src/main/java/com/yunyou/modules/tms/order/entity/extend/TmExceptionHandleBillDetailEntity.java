package com.yunyou.modules.tms.order.entity.extend;

import com.yunyou.modules.tms.order.entity.TmExceptionHandleBillDetail;

public class TmExceptionHandleBillDetailEntity extends TmExceptionHandleBillDetail {
    private static final long serialVersionUID = 1L;

    public TmExceptionHandleBillDetailEntity() {
        super();
    }

    public TmExceptionHandleBillDetailEntity(String id) {
        super(id);
    }


    public TmExceptionHandleBillDetailEntity(String billNo, String orgId) {
        super(billNo, orgId);
    }


}
