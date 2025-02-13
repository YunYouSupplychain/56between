package com.yunyou.modules.tms.basic.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.sys.entity.Office;
import com.yunyou.modules.sys.utils.UserUtils;
import com.yunyou.modules.tms.basic.entity.TmDriver;
import com.yunyou.modules.tms.basic.entity.extend.TmDriverEntity;
import com.yunyou.modules.tms.basic.mapper.TmDriverMapper;
import com.yunyou.modules.tms.common.TmsException;

/**
 * 运输人员信息Service
 *
 * @author liujianhua
 * @version 2020-02-20
 */
@Service
@Transactional(readOnly = true)
public class TmDriverService extends CrudService<TmDriverMapper, TmDriver> {
    @Autowired
    private TmDriverQualificationService tmDriverQualificationService;

    @Override
    @Transactional
    public void delete(TmDriver tmDriver) {
        // 删除资质信息
        tmDriverQualificationService.remove(tmDriver.getCode(), tmDriver.getOrgId());
        super.delete(tmDriver);
    }

    public void saveValidator(TmDriver tmDriver) {
        if (StringUtils.isBlank(tmDriver.getCode())) {
            throw new TmsException("编码不能为空");
        }
        if (StringUtils.isBlank(tmDriver.getName())) {
            throw new TmsException("姓名不能为空");
        }
        if (StringUtils.isBlank(tmDriver.getCarrierCode())) {
            throw new TmsException("承运商不能为空");
        }
        if (StringUtils.isBlank(tmDriver.getPhone())) {
            throw new TmsException("手机号不能为空");
        }
        Office organizationCenter = UserUtils.getOrgCenter(tmDriver.getOrgId());
        if (StringUtils.isBlank(organizationCenter.getId())) {
            throw new TmsException("找不到归属组织中心，不能操作");
        }
        List<TmDriver> list = findList(new TmDriver(tmDriver.getCode(), tmDriver.getOrgId()));
        if (CollectionUtil.isNotEmpty(list)) {
            if (list.stream().anyMatch(o -> !o.getId().equals(tmDriver.getId()))) {
                throw new TmsException("编码[" + tmDriver.getCode() + "]已存在");
            }
        }
    }

    public TmDriverEntity getEntity(String id) {
        return mapper.getEntity(id);
    }

    public Page<TmDriverEntity> findPage(Page page, TmDriver tmDriver) {
        dataRuleFilter(tmDriver);
        tmDriver.setPage(page);
        page.setList(mapper.findPage(tmDriver));
        return page;
    }

    public Page<TmDriverEntity> findGrid(Page page, TmDriverEntity entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findGrid(entity));
        return page;
    }

    @Transactional
    public TmDriver getByCode(String code, String orgId) {
        return mapper.getByCode(code, orgId);
    }

    @Transactional
    public void remove(String code, String orgId) {
        tmDriverQualificationService.remove(code, orgId);
        mapper.remove(code, orgId);
    }
}