package com.yunyou.modules.wms.inventory.service;

import com.yunyou.core.service.CrudService;
import com.yunyou.modules.wms.common.service.WmsUtil;
import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.modules.wms.common.entity.WarehouseException;
import com.yunyou.modules.wms.common.entity.WmsCodeMaster;
import com.yunyou.modules.wms.inventory.entity.BanQinWmMvDetail;
import com.yunyou.modules.wms.inventory.entity.BanQinWmMvHeader;
import com.yunyou.modules.wms.inventory.entity.extend.BanQinWmMvDetailEntity;
import com.yunyou.modules.wms.inventory.mapper.BanQinWmMvDetailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class BanQinWmMvDetailService extends CrudService<BanQinWmMvDetailMapper, BanQinWmMvDetail> {
    @Autowired
    private WmsUtil wmsUtil;
    @Autowired
    @Lazy
    private BanQinWmMvHeaderService wmMvHeaderService;
    @Autowired
    @Lazy
    private BanQinWmInvMvService wmInvMvService;

    public Page<BanQinWmMvDetailEntity> findPage(Page page, BanQinWmMvDetailEntity qEntity) {
        dataRuleFilter(qEntity);
        qEntity.setPage(page);
        page.setList(mapper.findPage(qEntity));
        return page;
    }

    public List<BanQinWmMvDetailEntity> findEntity(BanQinWmMvDetailEntity qEntity) {
        return mapper.findEntity(qEntity);
    }

    public List<BanQinWmMvDetailEntity> findEntityByNo(String mvNo, String orgId) {
        BanQinWmMvDetailEntity qEntity = new BanQinWmMvDetailEntity();
        qEntity.setMvNo(mvNo);
        qEntity.setOrgId(orgId);
        return this.findEntity(qEntity);
    }

    public void saveValidator(BanQinWmMvDetailEntity entity) {
        BanQinWmMvHeader wmMvHeader = wmMvHeaderService.get(entity.getHeaderId());
        if (wmMvHeader == null) {
            throw new WarehouseException(MessageFormat.format("订单[{0}]数据过期", entity.getMvNo()));
        }
        if (!WmsCodeMaster.MV_NEW.getCode().equals(wmMvHeader.getStatus())) {
            throw new WarehouseException(MessageFormat.format("订单[{0}]不是新建状态，无法操作", wmMvHeader.getMvNo()));
        }
        if (WmsCodeMaster.AUDIT_CLOSE.getCode().equals(wmMvHeader.getAuditStatus())) {
            throw new WarehouseException(MessageFormat.format("订单[{0}]已审核，无法操作", wmMvHeader.getMvNo()));
        }
        if (!WmsCodeMaster.MV_NEW.getCode().equals(entity.getStatus())) {
            throw new WarehouseException("订单[{0}]行号[{1}]不是新建状态，无法操作", entity.getMvNo(), entity.getLineNo());
        }
    }

    @Override
    @Transactional
    public void save(BanQinWmMvDetail entity) {
        if (StringUtils.isBlank(entity.getId())) {
            entity.setLineNo(wmsUtil.getMaxLineNo("wm_mv_detail", "header_id", entity.getHeaderId()));
        }
        super.save(entity);
    }

    @Transactional
    public void remove(String id) {
        BanQinWmMvDetail wmMvDetail = super.get(id);
        if (wmMvDetail == null) {
            throw new WarehouseException("数据已过期");
        }
        if (!WmsCodeMaster.MV_NEW.getCode().equals(wmMvDetail.getStatus())) {
            throw new WarehouseException(MessageFormat.format("行号[{0}]不是新建状态，无法操作", wmMvDetail.getLineNo()));
        }
        super.delete(wmMvDetail);
    }

    @Transactional
    public void executeMv(String id) {
        BanQinWmMvDetailEntity wmMvDetailEntity = mapper.getEntity(id);
        if (wmMvDetailEntity == null) {
            throw new WarehouseException("数据已过期");
        }
        BanQinWmMvHeader wmMvHeader = wmMvHeaderService.get(wmMvDetailEntity.getHeaderId());
        if (WmsCodeMaster.AUDIT_NEW.getCode().equals(wmMvHeader.getAuditStatus())) {
            throw new WarehouseException(MessageFormat.format("订单[{0}]未审核，无法操作", wmMvDetailEntity.getMvNo()));
        }
        if (WmsCodeMaster.MV_CLOSE.getCode().equals(wmMvHeader.getStatus())) {
            throw new WarehouseException(MessageFormat.format("订单[{0}]已关闭，无法操作", wmMvDetailEntity.getMvNo()));
        }
        if (WmsCodeMaster.MV_CANCEL.getCode().equals(wmMvHeader.getStatus())) {
            throw new WarehouseException(MessageFormat.format("订单[{0}]已取消，无法操作", wmMvDetailEntity.getMvNo()));
        }
        if (WmsCodeMaster.MV_CANCEL.getCode().equals(wmMvDetailEntity.getStatus())) {
            throw new WarehouseException(MessageFormat.format("订单[{0}]行号[{1}]已取消，无法操作", wmMvDetailEntity.getMvNo(), wmMvDetailEntity.getLineNo()));
        }
        if (WmsCodeMaster.MV_FULL.getCode().equals(wmMvDetailEntity.getStatus())) {
            throw new WarehouseException(MessageFormat.format("订单[{0}]行号[{1}]已移动完成，无法操作", wmMvDetailEntity.getMvNo(), wmMvDetailEntity.getLineNo()));
        }
        // 执行移动
        ResultMessage message = wmInvMvService.invMovementByOrder(wmMvDetailEntity);
        if (!message.isSuccess()) {
            throw new WarehouseException(MessageFormat.format("订单[{0}]行号[{1}] {2}", wmMvDetailEntity.getMvNo(), wmMvDetailEntity.getLineNo(), message.getMessage()));
        } else {
            // 移动成功，更新状态
            wmMvDetailEntity.setStatus(WmsCodeMaster.MV_FULL.getCode());
            this.save(wmMvDetailEntity);
            // 更新单头状态
            wmMvHeaderService.updateHeaderStatus(wmMvDetailEntity.getMvNo(), wmMvDetailEntity.getOrgId());
        }
    }

    @Transactional
    public void cancel(String id) {
        BanQinWmMvDetail wmMvDetail = super.get(id);
        if (wmMvDetail == null) {
            throw new WarehouseException("数据过期");
        }
        if (WmsCodeMaster.MV_NEW.getCode().equals(wmMvDetail.getStatus())) {
            wmMvDetail.setStatus(WmsCodeMaster.TF_CANCEL.getCode());
            this.save(wmMvDetail);
        } else {
            throw new WarehouseException(MessageFormat.format("行号[{0}]不是新建状态，无法操作", wmMvDetail.getLineNo()));
        }
    }
}
