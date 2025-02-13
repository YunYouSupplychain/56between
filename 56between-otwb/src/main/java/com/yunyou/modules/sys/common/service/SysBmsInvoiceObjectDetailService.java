package com.yunyou.modules.sys.common.service;

import com.yunyou.core.service.CrudService;
import com.yunyou.modules.sys.common.entity.SysBmsInvoiceObjectDetail;
import com.yunyou.modules.sys.common.mapper.SysBmsInvoiceObjectDetailMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class SysBmsInvoiceObjectDetailService extends CrudService<SysBmsInvoiceObjectDetailMapper, SysBmsInvoiceObjectDetail> {

    public SysBmsInvoiceObjectDetail getByCode(String code, String dataSet) {
        return mapper.getByCode(code, dataSet);
    }
}
