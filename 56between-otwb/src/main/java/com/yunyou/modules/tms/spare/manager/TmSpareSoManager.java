package com.yunyou.modules.tms.spare.manager;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.BaseService;
import com.yunyou.modules.sys.GenNoType;
import com.yunyou.modules.sys.service.SynchronizedNoService;
import com.yunyou.modules.tms.common.TmsConstants;
import com.yunyou.modules.tms.common.TmsException;
import com.yunyou.modules.tms.spare.entity.TmSpareSoDetail;
import com.yunyou.modules.tms.spare.entity.TmSpareSoHeader;
import com.yunyou.modules.tms.spare.entity.extend.TmSpareSoDetailEntity;
import com.yunyou.modules.tms.spare.entity.extend.TmSpareSoEntity;
import com.yunyou.modules.tms.spare.entity.extend.TmSpareSoScanInfoEntity;
import com.yunyou.modules.tms.spare.mapper.TmSpareSoMapper;
import com.yunyou.modules.tms.spare.service.TmSpareSoDetailService;
import com.yunyou.modules.tms.spare.service.TmSpareSoHeaderService;
import com.yunyou.modules.tms.spare.service.TmSpareSoScanInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class TmSpareSoManager extends BaseService {
    @Autowired
    private TmSpareSoMapper mapper;
    @Autowired
    private SynchronizedNoService noService;
    @Autowired
    private TmSpareSoHeaderService tmSpareSoHeaderService;
    @Autowired
    private TmSpareSoDetailService tmSpareSoDetailService;
    @Autowired
    private TmSpareSoScanInfoService tmSpareSoScanInfoService;

    public TmSpareSoEntity getEntity(String id) {
        return mapper.getEntity(id);
    }

    public TmSpareSoDetailEntity getDetailEntity(String id) {
        return mapper.getDetailEntity(id);
    }

    public TmSpareSoScanInfoEntity getScanInfoEntity(String id) {
        return mapper.getScanInfoEntity(id);
    }

    public List<TmSpareSoEntity> findList(TmSpareSoEntity qEntity) {
        return mapper.findSoPage(qEntity);
    }

    public List<TmSpareSoDetailEntity> findList(TmSpareSoDetailEntity qEntity) {
        return mapper.findDetailList(qEntity);
    }

    public List<TmSpareSoScanInfoEntity> findList(TmSpareSoScanInfoEntity qEntity) {
        return mapper.findScanInfoList(qEntity);
    }

    @SuppressWarnings("unchecked")
    public Page<TmSpareSoEntity> findPage(Page page, TmSpareSoEntity qEntity) {
        dataRuleFilter(qEntity);
        qEntity.setPage(page);
        page.setList(mapper.findSoPage(qEntity));
        return page;
    }

    @SuppressWarnings("unchecked")
    public Page<TmSpareSoDetailEntity> findPage(Page page, TmSpareSoDetailEntity qEntity) {
        dataRuleFilter(qEntity);
        qEntity.setPage(page);
        page.setList(mapper.findDetailPage(qEntity));
        return page;
    }

    @SuppressWarnings("unchecked")
    public Page<TmSpareSoScanInfoEntity> findPage(Page page, TmSpareSoScanInfoEntity qEntity) {
        dataRuleFilter(qEntity);
        qEntity.setPage(page);
        page.setList(mapper.findScanInfoPage(qEntity));
        return page;
    }

    public TmSpareSoEntity getByNo(String spareSoNo, String orgId) {
        return mapper.getByNo(spareSoNo, orgId);
    }

    public TmSpareSoDetailEntity getDetailByNoAndFitting(String spareSoNo, String fittingCode, String orgId) {
        return mapper.getDetailByNoAndFitting(spareSoNo, fittingCode, orgId);
    }

    @Transactional
    public TmSpareSoEntity saveHeader(TmSpareSoEntity entity) {
        if (StringUtils.isBlank(entity.getId())) {
            entity.setSpareSoNo(noService.getDocumentNo(GenNoType.TM_SPARE_SO_NO.name()));
            if (StringUtils.isBlank(entity.getOrderStatus())) {
                entity.setOrderStatus(TmsConstants.SPARE_SO_STATUS_00);
            }
            if (entity.getOrderTime() == null) {
                entity.setOrderTime(new Date());
            }
            if (StringUtils.isBlank(entity.getOrderType())) {
                entity.setOrderType(TmsConstants.SPARE_SO_TYPE_1);
            }
        }
        tmSpareSoHeaderService.save(entity);
        return getEntity(entity.getId());
    }

    @Transactional
    public TmSpareSoDetailEntity saveDetail(TmSpareSoDetailEntity entity) {
        if (StringUtils.isBlank(entity.getSpareSoNo())) {
            throw new TmsException("出库单号不能为空");
        }
        if (StringUtils.isBlank(entity.getFittingCode())) {
            throw new TmsException("备件不能为空");
        }
        if (entity.getSoQty() == null) {
            throw new TmsException("订单数量不能为空");
        }
        tmSpareSoDetailService.save(entity);
        return getDetailEntity(entity.getId());
    }

    @Transactional
    public TmSpareSoScanInfoEntity saveScanInfo(TmSpareSoScanInfoEntity entity) {
        if (StringUtils.isBlank(entity.getSpareSoNo())) {
            throw new TmsException("出库单号不能为空");
        }
        if (StringUtils.isBlank(entity.getFittingCode())) {
            throw new TmsException("备件不能为空");
        }
        if (entity.getBarcode() == null) {
            throw new TmsException("备件条码不能为空");
        }
        tmSpareSoScanInfoService.save(entity);
        return getScanInfoEntity(entity.getId());
    }

    @Transactional
    public void remove(String id) {
        TmSpareSoHeader tmSpareSoHeader = tmSpareSoHeaderService.get(id);
        List<TmSpareSoScanInfoEntity> scanInfoList = this.findList(new TmSpareSoScanInfoEntity(tmSpareSoHeader.getSpareSoNo(), tmSpareSoHeader.getOrgId()));
        if (CollectionUtil.isNotEmpty(scanInfoList)) {
            throw new TmsException(MessageFormat.format("订单【{0}】已扫描入库，无法操作", tmSpareSoHeader.getSpareSoNo()));
        }
        tmSpareSoHeaderService.delete(tmSpareSoHeader);
        mapper.removeDetailByNo(tmSpareSoHeader.getSpareSoNo(), tmSpareSoHeader.getOrgId());
    }

    @Transactional
    public void removeDetail(String id) {
        tmSpareSoDetailService.delete(new TmSpareSoDetail(id));
    }
}
