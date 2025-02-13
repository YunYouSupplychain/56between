package com.yunyou.modules.bms.basic.service;

import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.bms.basic.entity.BmsInvoiceObject;
import com.yunyou.modules.bms.basic.entity.BmsInvoiceObjectDetail;
import com.yunyou.modules.bms.basic.mapper.BmsInvoiceObjectDetailMapper;
import com.yunyou.modules.bms.basic.mapper.BmsInvoiceObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 开票对象Service
 *
 * @author zqs
 * @version 2019-02-18
 */
@Service
@Transactional(readOnly = true)
public class BmsInvoiceObjectService extends CrudService<BmsInvoiceObjectMapper, BmsInvoiceObject> {
    @Autowired
    private BmsInvoiceObjectDetailMapper baseInvoiceObjectDetailMapper;

    @Override
    public BmsInvoiceObject get(String id) {
        BmsInvoiceObject baseInvoiceObject = super.get(id);
        if (baseInvoiceObject != null) {
            baseInvoiceObject.setBmsInvoiceObjectDetailList(baseInvoiceObjectDetailMapper.findList(new BmsInvoiceObjectDetail(baseInvoiceObject)));
        }
        return baseInvoiceObject;
    }

    public BmsInvoiceObject getByCode(String code, String orgId) {
        return mapper.getByCode(code, orgId);
    }

    @Transactional
    public AjaxJson checkSaveBefore(BmsInvoiceObject baseInvoiceObject) {
        AjaxJson j = new AjaxJson();
        for (BmsInvoiceObjectDetail baseInvoiceObjectDetail : baseInvoiceObject.getBmsInvoiceObjectDetailList()) {
            if (BmsInvoiceObjectDetail.DEL_FLAG_NORMAL.equals(baseInvoiceObjectDetail.getDelFlag())) {
                if (StringUtils.isBlank(baseInvoiceObjectDetail.getId())) {
                    BmsInvoiceObjectDetail invoiceObjectDetail = baseInvoiceObjectDetailMapper.getByCode(baseInvoiceObjectDetail.getCode(), baseInvoiceObjectDetail.getOrgId());
                    if (invoiceObjectDetail != null) {
                        j.setSuccess(false);
                        j.setMsg("编码：" + baseInvoiceObjectDetail.getCode() + "开票对象明细重复！");
                        return j;
                    }
                }
            }
        }
        this.save(baseInvoiceObject);
        j.setSuccess(true);
        j.setMsg("保存开票对象成功");
        return j;
    }

    @Override
    @Transactional
    public void save(BmsInvoiceObject baseInvoiceObject) {
        super.save(baseInvoiceObject);
        for (BmsInvoiceObjectDetail baseInvoiceObjectDetail : baseInvoiceObject.getBmsInvoiceObjectDetailList()) {
            if (baseInvoiceObjectDetail.getId() == null) {
                continue;
            }
            if (BmsInvoiceObjectDetail.DEL_FLAG_NORMAL.equals(baseInvoiceObjectDetail.getDelFlag())) {
                if (StringUtils.isBlank(baseInvoiceObjectDetail.getId())) {
                    baseInvoiceObjectDetail.setBaseInvoiceObject(baseInvoiceObject);
                    baseInvoiceObjectDetail.preInsert();
                    baseInvoiceObjectDetailMapper.insert(baseInvoiceObjectDetail);
                } else {
                    baseInvoiceObjectDetail.preUpdate();
                    baseInvoiceObjectDetailMapper.update(baseInvoiceObjectDetail);
                }
            } else {
                baseInvoiceObjectDetailMapper.delete(baseInvoiceObjectDetail);
            }
        }
    }

    @Override
    @Transactional
    public void delete(BmsInvoiceObject baseInvoiceObject) {
        baseInvoiceObjectDetailMapper.delete(new BmsInvoiceObjectDetail(baseInvoiceObject));
        super.delete(baseInvoiceObject);
    }

    @Transactional
    public void remove(String code, String orgId) {
        mapper.remove(code, orgId);
    }
}