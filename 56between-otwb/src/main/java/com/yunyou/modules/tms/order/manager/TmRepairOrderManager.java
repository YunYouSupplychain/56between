package com.yunyou.modules.tms.order.manager;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.BaseService;
import com.yunyou.modules.sys.GenNoType;
import com.yunyou.modules.sys.service.SynchronizedNoService;
import com.yunyou.modules.tms.authority.TmAuthorityRule;
import com.yunyou.modules.tms.authority.TmAuthorityTable;
import com.yunyou.modules.tms.common.TmsConstants;
import com.yunyou.modules.tms.order.entity.*;
import com.yunyou.modules.tms.order.entity.extend.TmRepairOrderDetailEntity;
import com.yunyou.modules.tms.order.entity.extend.TmRepairOrderEntity;
import com.yunyou.modules.tms.order.entity.extend.TmRepairOrderInboundInfoEntity;
import com.yunyou.modules.tms.order.entity.extend.TmRepairOrderOutboundInfoEntity;
import com.yunyou.modules.tms.order.manager.mapper.TmRepairOrderMapper;
import com.yunyou.modules.tms.order.service.*;
import com.yunyou.common.utils.number.BigDecimalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TmRepairOrderManager extends BaseService {
    @Autowired
    private TmRepairOrderMapper mapper;
    @Autowired
    private TmRepairOrderHeaderService tmRepairOrderHeaderService;
    @Autowired
    private TmRepairOrderDetailService tmRepairOrderDetailService;
    @Autowired
    private TmAuthorityManager tmAuthorityManager;
    @Autowired
    private SynchronizedNoService noService;
    @Autowired
    private TmAttachementDetailService tmAttachementDetailService;// 照片附件处理类
    @Autowired
    private TmRepairOrderInboundInfoService tmRepairOrderInboundInfoService;
    @Autowired
    private TmRepairOrderOutboundInfoService tmRepairOrderOutboundInfoService;

    public TmRepairOrderHeader get(String id) {
        return tmRepairOrderHeaderService.get(id);
    }

    public TmRepairOrderEntity getEntity(String id) {
        return mapper.getEntity(id);
    }

    @SuppressWarnings("unchecked")
    public Page<TmRepairOrderEntity> findPage(Page page, TmRepairOrderEntity qEntity) {
        dataRuleFilter(qEntity);
        String authorityScope = TmAuthorityRule.dataRule(TmAuthorityTable.TM_REPAIR_ORDER_HEADER.getValue(), qEntity.getOrgId());
        qEntity.setDataScope(StringUtils.isNotBlank(qEntity.getDataScope()) ? qEntity.getDataScope() + authorityScope : authorityScope);
        qEntity.setPage(page);
        page.setList(mapper.findPage(qEntity));
        return page;
    }

    public List<TmRepairOrderEntity> findEntityList(TmRepairOrderEntity qEntity) {
        return mapper.findEntityList(qEntity);
    }

    public List<TmRepairOrderDetailEntity> findDetailList(TmRepairOrderDetailEntity qEntity) {
        return mapper.findDetailList(qEntity);
    }

    public List<TmRepairOrderInboundInfoEntity> findInboundInfoList(TmRepairOrderInboundInfoEntity qEntity) {
        return mapper.findInboundInfoList(qEntity);
    }

    public List<TmRepairOrderOutboundInfoEntity> findOutboundInfoList(TmRepairOrderOutboundInfoEntity qEntity) {
        return mapper.findOutboundInfoList(qEntity);
    }

    public TmRepairOrderEntity getByNo(String repairNo, String orgId) {
        return mapper.getByNo(repairNo, orgId);
    }

    @Transactional
    public String saveEntity(TmRepairOrderEntity entity) {
        if (StringUtils.isBlank(entity.getId())) {
            entity.setRepairNo(noService.getDocumentNo(GenNoType.TM_REPAIR_NO.name()));
            entity.setStatus(TmsConstants.REPAIR_ORDER_STATUS_00);
        }
        tmRepairOrderHeaderService.save(entity);
        // 生成授权数据
        tmAuthorityManager.genAuthorityData(entity);
        return entity.getId();
    }

    @Transactional
    public void removeEntity(TmRepairOrderHeader entity) {
        // 删除授权数据信息
        tmAuthorityManager.remove(TmAuthorityTable.TM_REPAIR_ORDER_HEADER.getValue(), entity.getId());
        tmRepairOrderDetailService.delete(new TmRepairOrderDetail(entity.getRepairNo(), entity.getOrgId()));
        tmRepairOrderHeaderService.delete(entity);
    }

    /**
     * 描述：保存司机报修信息
     */
    @Transactional
    public String saveForUnRepair(TmRepairOrderEntity entity) {
        if (StringUtils.isNotBlank(entity.getId())) {
            TmRepairOrderHeader old = this.get(entity.getId());
            if (old != null) {
                old.setRepairNo(entity.getRepairNo());
                old.setOrderTime(entity.getOrderTime());
                old.setStatus(entity.getStatus());
                old.setOwnerCode(entity.getOwnerCode());
                old.setCarNo(entity.getCarNo());
                old.setDriver(entity.getDriver());
                old.setNeedRepairItem(entity.getNeedRepairItem());
                tmRepairOrderHeaderService.save(old);
                return old.getId();
            }
        }
        return this.saveEntity(entity);
    }

    /**
     * 描述：保存维修厂维修信息
     */
    @Transactional
    public String saveForRepair(TmRepairOrderEntity entity) {
        if (StringUtils.isNotBlank(entity.getId())) {
            TmRepairOrderHeader old = this.get(entity.getId());
            if (old != null) {
                entity.setRepairNo(old.getRepairNo());
                entity.setOrderTime(old.getOrderTime());
                entity.setStatus(old.getStatus());
                entity.setOwnerCode(old.getOwnerCode());
                entity.setCarNo(old.getCarNo());
                entity.setDriver(old.getDriver());
                entity.setNeedRepairItem(old.getNeedRepairItem());
                entity.setRecVer(old.getRecVer());
                entity.setOrgId(old.getOrgId());
                entity.setBaseOrgId(old.getBaseOrgId());
            }
        }
        return this.saveEntity(entity);
    }

    @Transactional
    public void saveDetail(TmRepairOrderDetailEntity entity) {
        tmRepairOrderDetailService.save(entity);
        this.total(entity.getRepairNo());
    }

    @Transactional
    public void removeDetail(String repairNo, String detailId) {
        tmRepairOrderDetailService.delete(new TmRepairOrderDetail(detailId));
        this.total(repairNo);
    }

    @Transactional
    public void total(String repairNo) {
        TmRepairOrderHeader orderHeader = tmRepairOrderHeaderService.getByNo(repairNo);
        if (orderHeader != null) {
            double amount = 0, workHour = 0, workHourCost = 0, totalAmount = 0;
            List<TmRepairOrderDetail> list = tmRepairOrderDetailService.findList(new TmRepairOrderDetail(orderHeader.getRepairNo(), orderHeader.getOrgId()));
            for (TmRepairOrderDetail o : list) {
                amount = BigDecimalUtil.add(amount, o.getAmount() == null ? 0 : o.getAmount());
                workHour = BigDecimalUtil.add(workHour, o.getWorkHour() == null ? 0 : o.getWorkHour());
                workHourCost = BigDecimalUtil.add(workHourCost, o.getWorkHourCost() == null ? 0 : o.getWorkHourCost());
                totalAmount = BigDecimalUtil.add(totalAmount, o.getTotalAmount() == null ? 0 : o.getTotalAmount());
            }
            orderHeader.setAmount(amount);
            orderHeader.setWorkHour(workHour);
            orderHeader.setWorkHourCost(workHourCost);
            orderHeader.setTotalAmount(totalAmount);
            tmRepairOrderHeaderService.save(orderHeader);
        }
    }

    /**
     * 描述：分页查询图片信息
     */
    @SuppressWarnings("unchecked")
    public Page<TmAttachementDetail> findImgPage(Page page, TmAttachementDetail qEntity) {
        dataRuleFilter(qEntity);
        qEntity.setPage(page);
        page.setList(tmAttachementDetailService.findList(qEntity));
        return page;
    }

    @Transactional
    public void saveInboundInfo(TmRepairOrderInboundInfoEntity entity) {
        tmRepairOrderInboundInfoService.save(entity);
    }

    @Transactional
    public void saveOutboundInfo(TmRepairOrderOutboundInfoEntity entity) {
        tmRepairOrderOutboundInfoService.save(entity);
    }

    @Transactional
    public void deleteInboundInfo(String id) {
        tmRepairOrderInboundInfoService.delete(new TmRepairOrderInboundInfo(id));
    }

    @Transactional
    public void deleteOutboundInfo(String id) {
        tmRepairOrderOutboundInfoService.delete(new TmRepairOrderOutboundInfo(id));
    }
}
