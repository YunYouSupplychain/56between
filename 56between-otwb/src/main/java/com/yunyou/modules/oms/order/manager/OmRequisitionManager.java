package com.yunyou.modules.oms.order.manager;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.BaseService;
import com.yunyou.modules.oms.common.OmsConstants;
import com.yunyou.modules.oms.common.OmsException;
import com.yunyou.modules.oms.inv.service.OmSaleInventoryService;
import com.yunyou.modules.oms.order.entity.OmRequisitionHeader;
import com.yunyou.modules.oms.order.entity.extend.OmRequisitionDetailEntity;
import com.yunyou.modules.oms.order.entity.extend.OmRequisitionEntity;
import com.yunyou.modules.oms.order.mapper.OmRequisitionMapper;
import com.yunyou.modules.oms.order.service.OmRequisitionDetailService;
import com.yunyou.modules.oms.order.service.OmRequisitionHeaderService;
import com.yunyou.modules.sys.GenNoType;
import com.yunyou.modules.sys.service.SynchronizedNoService;
import com.yunyou.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class OmRequisitionManager extends BaseService {
    @Autowired
    private OmRequisitionMapper mapper;
    @Autowired
    private OmRequisitionHeaderService omRequisitionHeaderService;
    @Autowired
    private OmRequisitionDetailService omRequisitionDetailService;
    @Autowired
    private SynchronizedNoService noService;
    @Autowired
    private OmSaleInventoryService omSaleInventoryService;

    public OmRequisitionEntity getEntity(String id) {
        OmRequisitionEntity entity = mapper.getEntity(id);
        if (entity != null) {
            List<OmRequisitionDetailEntity> omRequisitionDetailList = this.findList(new OmRequisitionDetailEntity(entity.getReqNo(), entity.getOrgId()));
            omRequisitionDetailList.forEach(o -> {
                double availableQty = omSaleInventoryService.getAvailableQty(entity.getOwnerCode(), o.getSkuCode(), entity.getFmOrgId());
                o.setPlanTaskQty(o.getQty().subtract(o.getTaskQty()));
                o.setInvQty(BigDecimal.valueOf(availableQty));
            });
            entity.setOmRequisitionDetailList(omRequisitionDetailList);
        }
        return entity;
    }

    public OmRequisitionEntity getEntity(String reqNo, String orgId) {
        return mapper.getEntityByNo(reqNo, orgId);
    }

    public OmRequisitionDetailEntity getDetailEntity(String id) {
        return mapper.getDetailEntity(id);
    }

    public OmRequisitionDetailEntity getDetailEntity(String reqNo, String lineNo, String orgId) {
        return mapper.getDetailEntityByNoAndLineNo(reqNo, lineNo, orgId);
    }

    public List<OmRequisitionDetailEntity> findList(OmRequisitionDetailEntity qEntity) {
        return mapper.findDetailList(qEntity);
    }

    @SuppressWarnings("unchecked")
    public Page<OmRequisitionEntity> findPage(Page page, OmRequisitionEntity qEntity) {
        dataRuleFilter(qEntity);
        qEntity.setPage(page);
        page.setList(mapper.findPage(qEntity));
        return page;
    }

    @Transactional
    public OmRequisitionEntity save(OmRequisitionEntity entity) {
        if (StringUtils.isBlank(entity.getOrderType())) {
            throw new OmsException("单据类型不能为空");
        }
        if (StringUtils.isBlank(entity.getOwnerCode())) {
            throw new OmsException("货主不能为空");
        }
        if (StringUtils.isBlank(entity.getFmOrgId())) {
            throw new OmsException("源仓库不能为空");
        }
        if (StringUtils.isBlank(entity.getToOrgId())) {
            throw new OmsException("目标仓库不能为空");
        }
        if (StringUtils.isBlank(entity.getId())) {
            entity.setReqNo(noService.getDocumentNo(GenNoType.OM_REQUISITION_NO.name()));
            entity.setStatus(OmsConstants.OMS_RO_STATUS_10);
            entity.setRecVer(0);
        }
        omRequisitionHeaderService.save(entity);
        return getEntity(entity.getId());
    }

    @Transactional
    public void remove(String id) {
        OmRequisitionHeader omRequisitionHeader = omRequisitionHeaderService.get(id);
        if (omRequisitionHeader == null) {
            throw new OmsException("数据已过期");
        }
        if (!OmsConstants.OMS_RO_STATUS_10.equals(omRequisitionHeader.getStatus())) {
            throw new OmsException(MessageFormat.format("订单【{0}】不是新建状态，无法操作", omRequisitionHeader.getReqNo()));
        }
        omRequisitionHeaderService.delete(omRequisitionHeader);
        mapper.removeByNo(omRequisitionHeader.getReqNo(), omRequisitionHeader.getOrgId());
    }

    @Transactional
    public void audit(String id) {
        OmRequisitionHeader omRequisitionHeader = omRequisitionHeaderService.get(id);
        if (omRequisitionHeader == null) {
            throw new OmsException("数据已过期");
        }
        if (!OmsConstants.OMS_RO_STATUS_10.equals(omRequisitionHeader.getStatus())) {
            throw new OmsException(MessageFormat.format("订单【{0}】不是新建状态，无法操作", omRequisitionHeader.getReqNo()));
        }
        omRequisitionHeader.setStatus(OmsConstants.OMS_RO_STATUS_20);
        omRequisitionHeader.setAuditBy(UserUtils.getUser());
        omRequisitionHeaderService.save(omRequisitionHeader);
    }

    @Transactional
    public void cancelAudit(String id) {
        OmRequisitionHeader omRequisitionHeader = omRequisitionHeaderService.get(id);
        if (omRequisitionHeader == null) {
            throw new OmsException("数据已过期");
        }
        if (!OmsConstants.OMS_RO_STATUS_20.equals(omRequisitionHeader.getStatus())) {
            throw new OmsException(MessageFormat.format("订单【{0}】不是已审核状态，无法操作", omRequisitionHeader.getReqNo()));
        }
        omRequisitionHeader.setStatus(OmsConstants.OMS_RO_STATUS_10);
        omRequisitionHeader.setAuditBy(null);
        omRequisitionHeaderService.save(omRequisitionHeader);
    }

    @Transactional
    public void close(String id) {
        OmRequisitionHeader omRequisitionHeader = omRequisitionHeaderService.get(id);
        if (omRequisitionHeader == null) {
            throw new OmsException("数据已过期");
        }
        if (!(OmsConstants.OMS_RO_STATUS_35.equals(omRequisitionHeader.getStatus())
                || OmsConstants.OMS_RO_STATUS_40.equals(omRequisitionHeader.getStatus()))) {
            throw new OmsException(MessageFormat.format("订单【{0}】不是已生成任务状态，无法操作", omRequisitionHeader.getReqNo()));
        }
        omRequisitionHeader.setStatus(OmsConstants.OMS_RO_STATUS_99);
        omRequisitionHeaderService.save(omRequisitionHeader);
    }

    @Transactional
    public void cancel(String id) {
        OmRequisitionHeader omRequisitionHeader = omRequisitionHeaderService.get(id);
        if (omRequisitionHeader == null) {
            throw new OmsException("数据已过期");
        }
        if (!OmsConstants.OMS_RO_STATUS_10.equals(omRequisitionHeader.getStatus())) {
            throw new OmsException(MessageFormat.format("订单【{0}】不是新建状态，无法操作", omRequisitionHeader.getReqNo()));
        }
        omRequisitionHeader.setStatus(OmsConstants.OMS_RO_STATUS_90);
        omRequisitionHeaderService.save(omRequisitionHeader);
    }

    @Transactional
    public OmRequisitionEntity saveDetail(String id, List<OmRequisitionDetailEntity> omRequisitionDetailList) {
        for (OmRequisitionDetailEntity entity : omRequisitionDetailList) {
            if (entity.getId() == null) {
                continue;
            }
            if (StringUtils.isBlank(entity.getId())) {
                entity.setRecVer(0);
            }
            if (StringUtils.isBlank(entity.getSkuCode())) {
                throw new OmsException(MessageFormat.format("行号【{0}】，商品不能为空", entity.getLineNo()));
            }
            if (entity.getQty() == null) {
                throw new OmsException(MessageFormat.format("行号【{0}】，数量不能为空", entity.getLineNo()));
            }
            omRequisitionDetailService.save(entity);
        }
        return getEntity(id);
    }

    @Transactional
    public void removeDetail(String[] ids) {
        for (String id : ids) {
            omRequisitionDetailService.delete(omRequisitionDetailService.get(id));
        }
    }

}