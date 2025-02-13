package com.yunyou.modules.wms.inventory.service;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.sys.GenNoType;
import com.yunyou.modules.sys.service.SynchronizedNoService;
import com.yunyou.modules.sys.utils.UserUtils;
import com.yunyou.modules.wms.common.entity.WarehouseException;
import com.yunyou.modules.wms.common.entity.WmsCodeMaster;
import com.yunyou.modules.wms.inventory.entity.BanQinWmMvHeader;
import com.yunyou.modules.wms.inventory.entity.extend.BanQinWmMvDetailEntity;
import com.yunyou.modules.wms.inventory.entity.extend.BanQinWmMvEntity;
import com.yunyou.modules.wms.inventory.entity.extend.PrintMvOrder;
import com.yunyou.modules.wms.inventory.mapper.BanQinWmMvHeaderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

/**
 * 库存移动单Service
 */
@Service
@Transactional(readOnly = true)
public class BanQinWmMvHeaderService extends CrudService<BanQinWmMvHeaderMapper, BanQinWmMvHeader> {
    @Autowired
    private SynchronizedNoService noService;
    @Autowired
    private BanQinWmMvDetailService wmMvDetailService;

    public BanQinWmMvEntity getEntity(String id) {
        return mapper.getEntity(id);
    }

    public Page<BanQinWmMvHeader> findPage(Page page, BanQinWmMvEntity qEntity) {
        dataRuleFilter(qEntity);
        qEntity.setPage(page);
        page.setList(mapper.findPage(qEntity));
        return page;
    }

    public void saveValidator(BanQinWmMvHeader wmMvHeader) {
        if (!WmsCodeMaster.MV_NEW.getCode().equals(wmMvHeader.getStatus())) {
            throw new WarehouseException(MessageFormat.format("订单[{0}]不是新建状态，无法操作", wmMvHeader.getMvNo()));
        }
        if (WmsCodeMaster.AUDIT_CLOSE.getCode().equals(wmMvHeader.getAuditStatus())) {
            throw new WarehouseException(MessageFormat.format("订单[{0}]已审核，无法操作", wmMvHeader.getMvNo()));
        }
        if (StringUtils.isBlank(wmMvHeader.getOwnerCode())) {
            throw new WarehouseException("货主不能为空");
        }
    }

    @Transactional
    public String saveHeader(BanQinWmMvHeader wmMvHeader) {
        if (StringUtils.isBlank(wmMvHeader.getMvNo())) {
            wmMvHeader.setMvNo(noService.getDocumentNo(GenNoType.WM_MV_NO.name()));
        }
        super.save(wmMvHeader);
        return wmMvHeader.getId();
    }

    @Transactional
    public void remove(String id) {
        BanQinWmMvHeader wmMvHeader = super.get(id);
        if (wmMvHeader == null) {
            throw new WarehouseException("数据已过期");
        }
        if (WmsCodeMaster.AUDIT_CLOSE.getCode().equals(wmMvHeader.getAuditStatus())) {
            throw new WarehouseException(MessageFormat.format("订单[{0}]已审核，无法操作", wmMvHeader.getMvNo()));
        }
        if (!WmsCodeMaster.MV_NEW.getCode().equals(wmMvHeader.getAuditStatus())) {
            throw new WarehouseException(MessageFormat.format("订单[{0}]不是新建状态，无法操作", wmMvHeader.getMvNo()));
        }
        List<BanQinWmMvDetailEntity> wmMvDetailEntities = wmMvDetailService.findEntityByNo(wmMvHeader.getMvNo(), wmMvHeader.getOrgId());
        for (BanQinWmMvDetailEntity wmMvDetailEntity : wmMvDetailEntities) {
            wmMvDetailService.delete(wmMvDetailEntity);
        }
        super.delete(wmMvHeader);
    }

    @Transactional
    public void audit(String id) {
        BanQinWmMvHeader wmMvHeader = super.get(id);
        if (wmMvHeader == null) {
            throw new WarehouseException("数据已过期");
        }
        if (WmsCodeMaster.AUDIT_CLOSE.getCode().equals(wmMvHeader.getAuditStatus())) {
            throw new WarehouseException(MessageFormat.format("订单[{0}]已审核，无法操作", wmMvHeader.getMvNo()));
        }
        if (!WmsCodeMaster.MV_NEW.getCode().equals(wmMvHeader.getStatus())) {
            throw new WarehouseException(MessageFormat.format("订单[{0}]不是新建状态，无法操作", wmMvHeader.getMvNo()));
        }
        wmMvHeader.setAuditStatus(WmsCodeMaster.AUDIT_CLOSE.getCode());
        wmMvHeader.setAuditTime(new Date());
        wmMvHeader.setAuditOp(UserUtils.getUser().getName());
        this.saveHeader(wmMvHeader);
    }

    @Transactional
    public void cancelAudit(String id) {
        BanQinWmMvHeader wmMvHeader = super.get(id);
        if (wmMvHeader == null) {
            throw new WarehouseException("数据已过期");
        }
        if (WmsCodeMaster.AUDIT_NEW.getCode().equals(wmMvHeader.getAuditStatus())) {
            throw new WarehouseException(MessageFormat.format("订单[{0}]未审核，无法操作", wmMvHeader.getMvNo()));
        }
        if (!WmsCodeMaster.MV_NEW.getCode().equals(wmMvHeader.getStatus())) {
            throw new WarehouseException(MessageFormat.format("订单[{0}]不是新建状态，无法操作", wmMvHeader.getMvNo()));
        }
        wmMvHeader.setAuditStatus(WmsCodeMaster.AUDIT_NEW.getCode());
        wmMvHeader.setAuditTime(null);
        wmMvHeader.setAuditOp(null);
        this.saveHeader(wmMvHeader);
    }

    @Transactional
    public void executeMv(String id) {
        BanQinWmMvHeader wmMvHeader = super.get(id);
        if (wmMvHeader == null) {
            throw new WarehouseException("数据已过期");
        }
        List<BanQinWmMvDetailEntity> wmMvDetailEntities = wmMvDetailService.findEntityByNo(wmMvHeader.getMvNo(), wmMvHeader.getOrgId());
        for (BanQinWmMvDetailEntity wmMvDetailEntity : wmMvDetailEntities) {
            if (WmsCodeMaster.MV_NEW.getCode().equals(wmMvDetailEntity.getStatus())) {
                wmMvDetailService.executeMv(wmMvDetailEntity.getId());
            }
        }
    }

    @Transactional
    public void close(String id) {
        BanQinWmMvHeader wmMvHeader = super.get(id);
        if (wmMvHeader == null) {
            throw new WarehouseException("数据已过期");
        }
        if (!(WmsCodeMaster.MV_PART.getCode().equals(wmMvHeader.getStatus()) || WmsCodeMaster.MV_FULL.getCode().equals(wmMvHeader.getStatus()))) {
            throw new WarehouseException(MessageFormat.format("订单[{0}]仅部分移动、完全移动状态才能关闭", wmMvHeader.getMvNo()));
        }
        wmMvHeader.setStatus(WmsCodeMaster.MV_CLOSE.getCode());
        this.saveHeader(wmMvHeader);
    }

    @Transactional
    public void cancel(String id) {
        BanQinWmMvHeader wmMvHeader = super.get(id);
        if (wmMvHeader == null) {
            throw new WarehouseException("数据已过期");
        }
        if (!WmsCodeMaster.MV_NEW.getCode().equals(wmMvHeader.getStatus())) {
            throw new WarehouseException(MessageFormat.format("订单[{0}]不是新建状态，无法操作", wmMvHeader.getMvNo()));
        }
        wmMvHeader.setStatus(WmsCodeMaster.MV_CANCEL.getCode());
        this.saveHeader(wmMvHeader);
    }

    public BanQinWmMvHeader getByNo(String mvNo, String orgId) {
        return mapper.getByNo(mvNo, orgId);
    }

    @Transactional
    public void updateHeaderStatus(String mvNo, String orgId) {
        BanQinWmMvHeader wmMvHeader = this.getByNo(mvNo, orgId);

        List<BanQinWmMvDetailEntity> entities = wmMvDetailService.findEntityByNo(mvNo, orgId);
        boolean isNew = entities.stream().anyMatch(o -> WmsCodeMaster.MV_NEW.getCode().equals(o.getStatus()));
        boolean isFull = entities.stream().anyMatch(o -> WmsCodeMaster.MV_FULL.getCode().equals(o.getStatus()));
        if (isNew && isFull) {
            wmMvHeader.setStatus(WmsCodeMaster.MV_PART.getCode());
            this.saveHeader(wmMvHeader);
        } else if (isFull) {
            wmMvHeader.setStatus(WmsCodeMaster.MV_FULL.getCode());
            this.saveHeader(wmMvHeader);
        }
    }

    public List<PrintMvOrder> getMvOrder(String[] ids) {
        return mapper.getMvOrder(ids);
    }
}