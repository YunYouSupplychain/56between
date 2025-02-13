package com.yunyou.modules.sys.common.service;

import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.bms.common.BmsException;
import com.yunyou.modules.interfaces.interactive.action.SyncPlatformDataToBmsAction;
import com.yunyou.modules.sys.SysParamConstants;
import com.yunyou.modules.sys.common.entity.SysBmsInvoiceObject;
import com.yunyou.modules.sys.common.entity.SysBmsInvoiceObjectDetail;
import com.yunyou.modules.sys.common.mapper.SysBmsInvoiceObjectMapper;
import com.yunyou.modules.sys.utils.SysControlParamsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 开票对象Service
 */
@Service
@Transactional(readOnly = true)
public class SysBmsInvoiceObjectService extends CrudService<SysBmsInvoiceObjectMapper, SysBmsInvoiceObject> {
    @Autowired
    private SysBmsInvoiceObjectDetailService sysBmsInvoiceObjectDetailService;
    @Autowired
    private SyncPlatformDataToBmsAction syncPlatformDataToBmsAction;

    @Override
    public Page<SysBmsInvoiceObject> findPage(Page<SysBmsInvoiceObject> page, SysBmsInvoiceObject entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    public Page<SysBmsInvoiceObject> findGrid(Page<SysBmsInvoiceObject> page, SysBmsInvoiceObject entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findGrid(entity));
        return page;
    }

    public List<SysBmsInvoiceObject> findSync(SysBmsInvoiceObject entity) {
        List<SysBmsInvoiceObject> list = mapper.findSync(entity);
        list.forEach(o -> o.setSysBmsInvoiceObjectDetailList(sysBmsInvoiceObjectDetailService.findList(new SysBmsInvoiceObjectDetail(o))));
        return list;
    }

    @Override
    public SysBmsInvoiceObject get(String id) {
        SysBmsInvoiceObject sysBmsInvoiceObject = super.get(id);
        if (sysBmsInvoiceObject != null) {
            sysBmsInvoiceObject.setSysBmsInvoiceObjectDetailList(sysBmsInvoiceObjectDetailService.findList(new SysBmsInvoiceObjectDetail(sysBmsInvoiceObject)));
        }
        return sysBmsInvoiceObject;
    }

    @Override
    @Transactional
    public void save(SysBmsInvoiceObject entity) {
        super.save(entity);
        for (SysBmsInvoiceObjectDetail sysBmsInvoiceObjectDetail : entity.getSysBmsInvoiceObjectDetailList()) {
            if (sysBmsInvoiceObjectDetail.getId() == null) {
                continue;
            }
            if (SysBmsInvoiceObjectDetail.DEL_FLAG_NORMAL.equals(sysBmsInvoiceObjectDetail.getDelFlag())) {
                SysBmsInvoiceObjectDetail invoiceObjectDetail = sysBmsInvoiceObjectDetailService.getByCode(sysBmsInvoiceObjectDetail.getCode(), sysBmsInvoiceObjectDetail.getDataSet());
                if (invoiceObjectDetail != null && !invoiceObjectDetail.getId().equals(sysBmsInvoiceObjectDetail.getId())) {
                    throw new BmsException("编码：" + sysBmsInvoiceObjectDetail.getCode() + "开票对象明细重复！");
                }
                sysBmsInvoiceObjectDetail.setSysBmsInvoiceObject(entity);
                sysBmsInvoiceObjectDetailService.save(sysBmsInvoiceObjectDetail);
            } else {
                sysBmsInvoiceObjectDetailService.delete(sysBmsInvoiceObjectDetail);
            }
        }
        final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
        if ("Y".equals(IS_SAVING_PUSH)) {
            syncPlatformDataToBmsAction.sync(entity);
        }
    }

    @Transactional
    public void delete(SysBmsInvoiceObject entity) {
        sysBmsInvoiceObjectDetailService.delete(new SysBmsInvoiceObjectDetail(entity));
        super.delete(entity);
        final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
        if ("Y".equals(IS_SAVING_PUSH)) {
            syncPlatformDataToBmsAction.removeInvoiceObject(entity.getCode(), entity.getDataSet());
        }
    }

}