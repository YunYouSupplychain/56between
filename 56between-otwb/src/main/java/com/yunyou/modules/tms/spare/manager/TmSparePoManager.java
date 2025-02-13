package com.yunyou.modules.tms.spare.manager;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.BaseService;
import com.yunyou.modules.sys.GenNoType;
import com.yunyou.modules.sys.service.SynchronizedNoService;
import com.yunyou.modules.tms.common.TmsConstants;
import com.yunyou.modules.tms.common.TmsException;
import com.yunyou.modules.tms.spare.entity.TmSparePoDetail;
import com.yunyou.modules.tms.spare.entity.TmSparePoHeader;
import com.yunyou.modules.tms.spare.entity.TmSparePoScanInfo;
import com.yunyou.modules.tms.spare.entity.extend.TmSparePoDetailEntity;
import com.yunyou.modules.tms.spare.entity.extend.TmSparePoEntity;
import com.yunyou.modules.tms.spare.entity.extend.TmSparePoScanInfoEntity;
import com.yunyou.modules.tms.spare.mapper.TmSparePoMapper;
import com.yunyou.modules.tms.spare.service.TmSparePoDetailService;
import com.yunyou.modules.tms.spare.service.TmSparePoHeaderService;
import com.yunyou.modules.tms.spare.service.TmSparePoScanInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class TmSparePoManager extends BaseService {
    @Autowired
    private TmSparePoMapper mapper;
    @Autowired
    private SynchronizedNoService noService;
    @Autowired
    private TmSparePoHeaderService tmSparePoHeaderService;
    @Autowired
    private TmSparePoDetailService tmSparePoDetailService;
    @Autowired
    private TmSparePoScanInfoService tmSparePoScanInfoService;

    public TmSparePoEntity getEntity(String id) {
        return mapper.getEntity(id);
    }

    public TmSparePoDetailEntity getDetailEntity(String id) {
        return mapper.getDetailEntity(id);
    }

    public TmSparePoScanInfoEntity getScanInfoEntity(String id) {
        return mapper.getScanInfoEntity(id);
    }

    public List<TmSparePoEntity> findList(TmSparePoEntity qEntity) {
        return mapper.findPoPage(qEntity);
    }

    public List<TmSparePoDetailEntity> findList(TmSparePoDetailEntity qEntity) {
        return mapper.findDetailList(qEntity);
    }

    public List<TmSparePoScanInfoEntity> findList(TmSparePoScanInfoEntity qEntity) {
        return mapper.findScanInfoList(qEntity);
    }


    @SuppressWarnings("unchecked")
    public Page<TmSparePoEntity> findPage(Page page, TmSparePoEntity qEntity) {
        dataRuleFilter(qEntity);
        qEntity.setPage(page);
        page.setList(mapper.findPoPage(qEntity));
        return page;
    }

    @SuppressWarnings("unchecked")
    public Page<TmSparePoDetailEntity> findPage(Page page, TmSparePoDetailEntity qEntity) {
        dataRuleFilter(qEntity);
        qEntity.setPage(page);
        page.setList(mapper.findDetailPage(qEntity));
        return page;
    }

    @SuppressWarnings("unchecked")
    public Page<TmSparePoScanInfoEntity> findPage(Page page, TmSparePoScanInfoEntity qEntity) {
        dataRuleFilter(qEntity);
        qEntity.setPage(page);
        page.setList(mapper.findScanInfoPage(qEntity));
        return page;
    }

    public TmSparePoEntity getByNo(String sparePoNo, String orgId) {
        return mapper.getByNo(sparePoNo, orgId);
    }

    public TmSparePoDetailEntity getDetailByNoAndFitting(String sparePoNo, String fittingCode, String orgId) {
        return mapper.getDetailByNoAndFitting(sparePoNo, fittingCode, orgId);
    }

    @Transactional
    public TmSparePoEntity saveHeader(TmSparePoEntity entity) {
        if (StringUtils.isBlank(entity.getId())) {
            entity.setSparePoNo(noService.getDocumentNo(GenNoType.TM_SPARE_ASN_NO.name()));
            if (StringUtils.isBlank(entity.getOrderStatus())) {
                entity.setOrderStatus(TmsConstants.SPARE_ASN_STATUS_00);
            }
            if (entity.getOrderTime() == null) {
                entity.setOrderTime(new Date());
            }
            if (StringUtils.isBlank(entity.getOrderType())) {
                entity.setOrderType(TmsConstants.SPARE_ASN_TYPE_1);
            }
        }
        tmSparePoHeaderService.save(entity);
        return getEntity(entity.getId());
    }

    @Transactional
    public TmSparePoDetailEntity saveDetail(TmSparePoDetailEntity entity) {
        if (StringUtils.isBlank(entity.getSparePoNo())) {
            throw new TmsException("入库单号不能为空");
        }
        if (StringUtils.isBlank(entity.getFittingCode())) {
            throw new TmsException("备件不能为空");
        }
        if (entity.getPoQty() == null) {
            throw new TmsException("订单数量不能为空");
        }
        tmSparePoDetailService.save(entity);
        return getDetailEntity(entity.getId());
    }

    @Transactional
    public TmSparePoScanInfo saveScanInfo(TmSparePoScanInfo entity) {
        if (StringUtils.isBlank(entity.getSparePoNo())) {
            throw new TmsException("入库单号不能为空");
        }
        if (StringUtils.isBlank(entity.getFittingCode())) {
            throw new TmsException("备件不能为空");
        }
        if (entity.getBarcode() == null) {
            throw new TmsException("备件条码不能为空");
        }
        tmSparePoScanInfoService.save(entity);
        return getScanInfoEntity(entity.getId());
    }

    @Transactional
    public void remove(String id) {
        TmSparePoHeader tmSparePoHeader = tmSparePoHeaderService.get(id);
        List<TmSparePoScanInfoEntity> scanInfoList = this.findList(new TmSparePoScanInfoEntity(tmSparePoHeader.getSparePoNo(), tmSparePoHeader.getOrgId()));
        if (CollectionUtil.isNotEmpty(scanInfoList)) {
            throw new TmsException(MessageFormat.format("订单【{0}】已扫描入库，无法操作", tmSparePoHeader.getSparePoNo()));
        }
        tmSparePoHeaderService.delete(tmSparePoHeader);
        mapper.removeDetailByNo(tmSparePoHeader.getSparePoNo(), tmSparePoHeader.getOrgId());
    }

    @Transactional
    public void removeDetail(String id) {
        tmSparePoDetailService.delete(new TmSparePoDetail(id));
    }
}
