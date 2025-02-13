package com.yunyou.modules.sys.common.service;

import com.yunyou.core.service.CrudService;
import com.yunyou.modules.interfaces.interactive.action.SyncPlatformDataToWmsAction;
import com.yunyou.modules.sys.SysParamConstants;
import com.yunyou.modules.sys.common.entity.SysWmsRuleWvDetail;
import com.yunyou.modules.sys.common.entity.SysWmsRuleWvHeader;
import com.yunyou.modules.sys.common.mapper.SysWmsRuleWvDetailMapper;
import com.yunyou.modules.sys.utils.SysControlParamsUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 波次规则Service
 */
@Service
@Transactional(readOnly = true)
public class SysWmsRuleWvDetailService extends CrudService<SysWmsRuleWvDetailMapper, SysWmsRuleWvDetail> {
    @Autowired
    @Lazy
    private SysWmsRuleWvHeaderService sysWmsRuleWvHeaderService;
    @Autowired
    private SysWmsRuleWvDetailWvService sysWmsRuleWvDetailWvService;
    @Autowired
    private SysWmsRuleWvDetailOrderService sysWmsRuleWvDetailOrderService;
    @Autowired
    @Lazy
    private SyncPlatformDataToWmsAction syncPlatformDataToWmsAction;

    @Override
    @Transactional
    public void save(SysWmsRuleWvDetail entity) {
        entity.setSql(StringEscapeUtils.unescapeHtml3(entity.getSql()));
        sysWmsRuleWvDetailWvService.updateLineNo(entity.getId(), entity.getLineNo());
        sysWmsRuleWvDetailOrderService.updateLineNo(entity.getId(), entity.getLineNo());
        super.save(entity);
        final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
        if ("Y".equals(IS_SAVING_PUSH)) {
            sysWmsRuleWvHeaderService.findSync(new SysWmsRuleWvHeader(entity.getHeaderId())).forEach(syncPlatformDataToWmsAction::sync);
        }
    }

    @Override
    @Transactional
    public void delete(SysWmsRuleWvDetail entity) {
        sysWmsRuleWvDetailWvService.deleteByHeaderId(entity.getId());
        sysWmsRuleWvDetailOrderService.deleteByHeaderId(entity.getId());
        super.delete(entity);
        final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
        if ("Y".equals(IS_SAVING_PUSH)) {
            sysWmsRuleWvHeaderService.findSync(new SysWmsRuleWvHeader(entity.getHeaderId())).forEach(syncPlatformDataToWmsAction::sync);
        }
    }

    @Transactional
    public void deleteByHeaderId(String headerId) {
        List<SysWmsRuleWvDetail> list = mapper.getByHeaderId(headerId);
        for (SysWmsRuleWvDetail detail : list) {
            sysWmsRuleWvDetailWvService.deleteByHeaderId(detail.getId());
            sysWmsRuleWvDetailOrderService.deleteByHeaderId(detail.getId());
        }
        mapper.deleteByHeaderId(headerId);
    }

    @Transactional
    public void updateSQL(String id, String sql) {
        SysWmsRuleWvDetail sysWmsRuleWvDetail = this.get(id);
        sysWmsRuleWvDetail.setSql(sql);
        super.save(sysWmsRuleWvDetail);
        final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
        if ("Y".equals(IS_SAVING_PUSH)) {
            sysWmsRuleWvHeaderService.findSync(new SysWmsRuleWvHeader(sysWmsRuleWvDetail.getHeaderId())).forEach(syncPlatformDataToWmsAction::sync);
        }
    }

    public List<SysWmsRuleWvDetail> findByHeaderId(String headerId) {
        List<SysWmsRuleWvDetail> list = mapper.getByHeaderId(headerId);
        for (SysWmsRuleWvDetail detail : list) {
            detail.setRuleWvDetailWvList(sysWmsRuleWvDetailWvService.findByHeaderId(detail.getId(), detail.getDataSet()));
            detail.setRuleWvDetailOrderList(sysWmsRuleWvDetailOrderService.findByHeaderId(detail.getId(), detail.getDataSet()));
        }
        return list;
    }
}