package com.yunyou.modules.sys.service;

import com.yunyou.common.utils.RedisUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.common.config.CacheNames;
import com.yunyou.modules.sys.entity.SysControlParams;
import com.yunyou.modules.sys.mapper.SysControlParamsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 系统控制参数Service
 *
 * @author Jianhua Liu
 * @version 2019-08-12
 */
@Service
@Transactional(readOnly = true)
public class SysControlParamsService extends CrudService<SysControlParamsMapper, SysControlParams> {
    @Autowired
    private RedisUtils redisUtils;

    public Page<SysControlParams> findPage(Page<SysControlParams> page, SysControlParams entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    public String getByCode(String code) {
        return this.getByCode(code, null);
    }

    public String getByCode(String code, String orgId) {
        SysControlParams sysControlParams = mapper.getByCode(code, orgId);
        if (sysControlParams == null) {
            sysControlParams = mapper.getByOnlyCode(code);
        }
        return sysControlParams == null ? "" : sysControlParams.getValue();
    }

    @Transactional
    public void saveEntity(SysControlParams sysControlParams) {
        super.save(sysControlParams);
        redisUtils.delete(CacheNames.SYS_CACHE_CONTROL_PARAMS_MAP);
    }

    @Transactional
    public void deleteEntity(SysControlParams sysControlParams) {
        super.delete(sysControlParams);
        redisUtils.delete(CacheNames.SYS_CACHE_CONTROL_PARAMS_MAP);
    }

}