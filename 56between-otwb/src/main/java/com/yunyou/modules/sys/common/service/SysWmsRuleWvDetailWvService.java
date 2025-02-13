package com.yunyou.modules.sys.common.service;

import com.yunyou.core.service.CrudService;
import com.yunyou.modules.interfaces.interactive.action.SyncPlatformDataToWmsAction;
import com.yunyou.modules.sys.SysParamConstants;
import com.yunyou.modules.sys.common.entity.SysWmsRuleWvDetail;
import com.yunyou.modules.sys.common.entity.SysWmsRuleWvDetailWv;
import com.yunyou.modules.sys.common.entity.SysWmsRuleWvHeader;
import com.yunyou.modules.sys.common.mapper.SysWmsRuleWvDetailWvMapper;
import com.yunyou.modules.sys.utils.SysControlParamsUtils;
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
public class SysWmsRuleWvDetailWvService extends CrudService<SysWmsRuleWvDetailWvMapper, SysWmsRuleWvDetailWv> {
    @Autowired
    @Lazy
    private SysWmsRuleWvHeaderService sysWmsRuleWvHeaderService;
    @Autowired
    @Lazy
    private SysWmsRuleWvDetailService sysWmsRuleWvDetailService;
    @Autowired
    @Lazy
    private SyncPlatformDataToWmsAction syncPlatformDataToWmsAction;

    @Override
    @Transactional
    public void save(SysWmsRuleWvDetailWv entity) {
        super.save(entity);
        this.sync(entity.getHeaderId());
    }

    @Override
    @Transactional
    public void delete(SysWmsRuleWvDetailWv entity) {
        super.delete(entity);
        this.sync(entity.getHeaderId());
    }

    public void sync(String detailId) {
        SysWmsRuleWvDetail sysWmsRuleWvDetail = sysWmsRuleWvDetailService.get(detailId);
        final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
        if ("Y".equals(IS_SAVING_PUSH)) {
            sysWmsRuleWvHeaderService.findSync(new SysWmsRuleWvHeader(sysWmsRuleWvDetail.getHeaderId())).forEach(syncPlatformDataToWmsAction::sync);
        }
    }

    @Transactional
    public void deleteByHeaderId(String headerId) {
        mapper.deleteByHeaderId(headerId);
    }

    @Transactional
    public void updateLineNo(String headerId, String lineNo) {
        mapper.execUpdateSql("update sys_wms_rule_wv_detail_wv set line_no = '" + lineNo + "' where header_id = '" + headerId + "'");
    }

    public List<SysWmsRuleWvDetailWv> findByHeaderId(String headerId, String dataSet) {
        return findList(new SysWmsRuleWvDetailWv(null, headerId, dataSet));
    }
}