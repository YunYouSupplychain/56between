package com.yunyou.modules.sys.common.service;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.interfaces.interactive.action.SyncPlatformDataToWmsAction;
import com.yunyou.modules.sys.SysParamConstants;
import com.yunyou.modules.sys.common.entity.SysWmsQcItemDetail;
import com.yunyou.modules.sys.common.entity.SysWmsQcItemHeader;
import com.yunyou.modules.sys.common.mapper.SysWmsQcItemDetailMapper;
import com.yunyou.modules.sys.utils.SysControlParamsUtils;
import com.yunyou.modules.wms.common.service.WmsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 质检项Service
 */
@Service
@Transactional(readOnly = true)
public class SysWmsQcItemDetailService extends CrudService<SysWmsQcItemDetailMapper, SysWmsQcItemDetail> {
    @Autowired
    private WmsUtil wmsUtil;
    @Autowired
    @Lazy
    private SysWmsQcItemHeaderService sysWmsQcItemHeaderService;
    @Autowired
    @Lazy
    private SyncPlatformDataToWmsAction syncPlatformDataToWmsAction;

    public Page<SysWmsQcItemDetail> findPage(Page<SysWmsQcItemDetail> page, SysWmsQcItemDetail entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    @Override
    @Transactional
    public void save(SysWmsQcItemDetail entity) {
        if (StringUtils.isBlank(entity.getId())) {
            entity.setLineNo(wmsUtil.getMaxLineNo("sys_wms_qc_item_detail", "header_id", entity.getHeaderId()));
        }
        super.save(entity);
        final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
        if ("Y".equals(IS_SAVING_PUSH)) {
            sysWmsQcItemHeaderService.findSync(new SysWmsQcItemHeader(entity.getHeaderId())).forEach(syncPlatformDataToWmsAction::sync);
        }
    }

    @Override
    @Transactional
    public void delete(SysWmsQcItemDetail entity) {
        super.delete(entity);
        final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
        if ("Y".equals(IS_SAVING_PUSH)) {
            sysWmsQcItemHeaderService.findSync(new SysWmsQcItemHeader(entity.getHeaderId())).forEach(syncPlatformDataToWmsAction::sync);
        }
    }

    @Transactional
    public void deleteByHeaderId(String headerId) {
        mapper.deleteByHeaderId(headerId);
    }
}