package com.yunyou.modules.bms.basic.service;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.bms.basic.entity.BmsBillTerms;
import com.yunyou.modules.bms.basic.entity.BmsBillTermsParameter;
import com.yunyou.modules.bms.basic.entity.extend.BmsBillTermsEntity;
import com.yunyou.modules.bms.basic.mapper.BmsBillTermsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 计费条款Service
 *
 * @author Jianhua Liu
 * @version 2019-05-27
 */
@Service
@Transactional(readOnly = true)
public class BmsBillTermsService extends CrudService<BmsBillTermsMapper, BmsBillTerms> {
    @Autowired
    private BmsBillTermsParameterService bmsBillTermsParameterService;

    public Page<BmsBillTerms> findPage(Page<BmsBillTerms> page, BmsBillTermsEntity entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    public BmsBillTermsEntity getEntity(String id) {
        BmsBillTermsEntity entity = mapper.getEntity(id);
        if (entity != null) {
            entity.setParameters(bmsBillTermsParameterService.findByBillTermsCode(entity.getBillTermsCode()));
        }
        return entity;
    }

    public BmsBillTerms getByCode(String termsCode) {
        return mapper.getByCode(termsCode);
    }

    public BmsBillTermsEntity duplicate(BmsBillTermsEntity entity) {
        if (entity != null) {
            entity.setId(null);
            entity.setBillTermsCode(null);
            entity.setBillTermsDesc(null);
            if (CollectionUtil.isNotEmpty(entity.getParameters())) {
                for (BmsBillTermsParameter parameter : entity.getParameters()) {
                    parameter.setId(null);
                    parameter.setBillTermsCode(null);
                }
            }
        }
        return entity;
    }

    @Transactional
    public BmsBillTermsEntity saveEntity(BmsBillTermsEntity entity) {
        this.save(entity);
        for (BmsBillTermsParameter parameter : entity.getParameters()) {
            if (parameter.getId() == null) {
                continue;
            }
            if (BmsBillTermsParameter.DEL_FLAG_NORMAL.equals(parameter.getDelFlag())) {
                parameter.setBillTermsCode(entity.getBillTermsCode());
                parameter.setIsEnable(StringUtils.isBlank(parameter.getIsEnable()) ? "N" : parameter.getIsEnable());
                parameter.setIsShow(StringUtils.isBlank(parameter.getIsShow()) ? "N" : parameter.getIsShow());
                bmsBillTermsParameterService.save(parameter);
            } else {
                bmsBillTermsParameterService.delete(parameter);
            }
        }
        return entity;
    }

    @Override
    @Transactional
    public void delete(BmsBillTerms entity) {
        bmsBillTermsParameterService.remove(entity.getBillTermsCode());
        super.delete(entity);
    }

    @Transactional
    public void remove(String billTermsCode) {
        bmsBillTermsParameterService.remove(billTermsCode);
        mapper.remove(billTermsCode);
    }
}