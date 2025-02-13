package com.yunyou.modules.tms.basic.service;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.sys.entity.Office;
import com.yunyou.modules.sys.utils.UserUtils;
import com.yunyou.modules.tms.basic.entity.TmDriverQualification;
import com.yunyou.modules.tms.basic.mapper.TmDriverQualificationMapper;
import com.yunyou.modules.tms.common.TmsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 运输人员资质信息Service
 *
 * @author liujianhua
 * @version 2020-02-20
 */
@Service
@Transactional(readOnly = true)
public class TmDriverQualificationService extends CrudService<TmDriverQualificationMapper, TmDriverQualification> {

    public TmDriverQualification get(String id) {
        return super.get(id);
    }

    public List<TmDriverQualification> findList(TmDriverQualification tmDriverQualification) {
        return super.findList(tmDriverQualification);
    }

    public Page<TmDriverQualification> findPage(Page<TmDriverQualification> page, TmDriverQualification tmDriverQualification) {
        return super.findPage(page, tmDriverQualification);
    }

    @Transactional
    public void save(TmDriverQualification tmDriverQualification) {
        super.save(tmDriverQualification);
    }

    @Transactional
    public void delete(TmDriverQualification tmDriverQualification) {
        super.delete(tmDriverQualification);
    }

    @Transactional
    public void remove(String driverCode, String orgId) {
        mapper.remove(driverCode, orgId);
    }

    public void saveValidator(TmDriverQualification tmDriverQualification) {
        if (StringUtils.isBlank(tmDriverQualification.getDriverCode())) {
            throw new TmsException("所属运输人员编码不能为空");
        }
        if (StringUtils.isBlank(tmDriverQualification.getQualificationCode())) {
            throw new TmsException("资质编码不能为空");
        }
        Office organizationCenter = UserUtils.getOrgCenter(tmDriverQualification.getOrgId());
        if (StringUtils.isBlank(organizationCenter.getId())) {
            throw new TmsException("找不到归属组织中心，不能操作");
        }
        List<TmDriverQualification> list = findList(new TmDriverQualification(tmDriverQualification.getDriverCode(), tmDriverQualification.getQualificationCode(), tmDriverQualification.getOrgId()));
        if (CollectionUtil.isNotEmpty(list)) {
            if (list.stream().anyMatch(o -> !o.getId().equals(tmDriverQualification.getId()))) {
                throw new TmsException("资质编码[" + tmDriverQualification.getQualificationCode() + "]已存在");
            }
        }
    }
}