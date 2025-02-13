package com.yunyou.modules.tms.order.action;

import com.yunyou.core.action.BaseAction;
import com.yunyou.core.persistence.Page;
import com.yunyou.modules.tms.order.entity.TmLabelLog;
import com.yunyou.modules.tms.order.entity.extend.TmLabelLogEntity;
import com.yunyou.modules.tms.order.service.TmLabelLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 标签履历Action
 *
 * @author 刘剑华
 * @since 2023/11/28 10:17
 */
@Service
public class TmLabelLogAction extends BaseAction {
    @Autowired
    private TmLabelLogService tmLabelLogService;

    public Page<TmLabelLog> findPage(Page<TmLabelLog> page, TmLabelLogEntity entity) {
        return tmLabelLogService.findPage(page, entity);
    }
}
