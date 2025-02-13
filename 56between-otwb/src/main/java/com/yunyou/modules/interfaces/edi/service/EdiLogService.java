package com.yunyou.modules.interfaces.edi.service;

import com.yunyou.core.persistence.Page;
import com.yunyou.modules.interfaces.edi.entity.EdiLog;
import com.yunyou.modules.interfaces.edi.mapper.EdiLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class EdiLogService {
    @Autowired
    private EdiLogMapper mapper;

    @Transactional
    public void saveLog(EdiLog log) {
        log.preInsert();
        mapper.save(log);
    }

    public Page<EdiLog> findPage(Page<EdiLog> page, EdiLog entity) {
        entity.setPage(page);
        page.setList(mapper.findList(entity));
        return page;
    }
}
