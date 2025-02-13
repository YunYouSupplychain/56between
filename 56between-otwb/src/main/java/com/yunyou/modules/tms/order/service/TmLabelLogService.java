package com.yunyou.modules.tms.order.service;

import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.tms.order.entity.TmLabelLog;
import com.yunyou.modules.tms.order.entity.extend.TmLabelLogEntity;
import com.yunyou.modules.tms.order.mapper.TmLabelLogMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 标签履历Service
 *
 * @author 刘剑华
 * @since 2023/11/16 14:05
 */
@Service
@Transactional(readOnly = true)
public class TmLabelLogService extends CrudService<TmLabelLogMapper, TmLabelLog> {

    public Page<TmLabelLog> findPage(Page<TmLabelLog> page, TmLabelLogEntity entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(this.mapper.findPage(entity));
        return page;
    }
}
