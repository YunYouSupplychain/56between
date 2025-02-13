package com.yunyou.modules.tms.order.manager;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.BaseService;
import com.yunyou.modules.sys.GenNoType;
import com.yunyou.modules.sys.entity.User;
import com.yunyou.modules.sys.service.SynchronizedNoService;
import com.yunyou.modules.tms.common.TmsConstants;
import com.yunyou.modules.tms.order.entity.TmAttachementDetail;
import com.yunyou.modules.tms.order.entity.extend.TmExceptionHandleBillDetailEntity;
import com.yunyou.modules.tms.order.entity.extend.TmExceptionHandleBillEntity;
import com.yunyou.modules.tms.order.entity.extend.TmExceptionHandleBillFeeEntity;
import com.yunyou.modules.tms.order.manager.mapper.TmExceptionHandleBillEntityMapper;
import com.yunyou.modules.tms.order.service.TmAttachementDetailService;
import com.yunyou.modules.tms.order.service.TmExceptionHandleBillDetailService;
import com.yunyou.modules.tms.order.service.TmExceptionHandleBillFeeService;
import com.yunyou.modules.tms.order.service.TmExceptionHandleBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 异常处理单处理类(事务管理)
 */
@Service
@Transactional(readOnly = true)
public class TmExceptionHandleBillManager extends BaseService {
    @Autowired
    private TmExceptionHandleBillEntityMapper mapper;
    @Autowired
    private TmExceptionHandleBillService tmExceptionHandleBillService;
    @Autowired
    private TmExceptionHandleBillDetailService tmExceptionHandleBillDetailService;
    @Autowired
    private TmExceptionHandleBillFeeService tmExceptionHandleBillFeeService;
    @Autowired
    private TmAttachementDetailService tmAttachementDetailService;
    @Autowired
    private SynchronizedNoService noService;

    public TmExceptionHandleBillEntity getEntity(String id) {
        TmExceptionHandleBillEntity entity = mapper.getEntity(id);
        if (entity != null) {
            entity.setTmExceptionHandleBillDetailList(mapper.findDetailList(new TmExceptionHandleBillDetailEntity(entity.getBillNo(), entity.getOrgId())));
            entity.setTmExceptionHandleBillFeeList(mapper.findFeeList(new TmExceptionHandleBillFeeEntity(entity.getBillNo(), entity.getOrgId())));

        }
        return entity;
    }

    public Page<TmExceptionHandleBillEntity> findPage(Page page, TmExceptionHandleBillEntity entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        List<TmExceptionHandleBillEntity> list = mapper.findPage(entity);
        page.setList(list);
        return page;
    }

    @Transactional
    public TmExceptionHandleBillEntity saveEntity(TmExceptionHandleBillEntity entity) {
        if (StringUtils.isBlank(entity.getBillNo())) {
            entity.setBillNo(noService.getDocumentNo(GenNoType.TM_EXCEPTION_NO.name()));
            entity.setBillStatus(TmsConstants.EXCEPTION_ORDER_STATUS_00);
        }
        tmExceptionHandleBillService.save(entity);

        return getEntity(entity.getId());
    }

    @Transactional
    public void removeEntity(TmExceptionHandleBillEntity entity) {
        // 删除主体信息
        tmExceptionHandleBillService.delete(entity);
        // 删除明细信息
        tmExceptionHandleBillDetailService.deleteDetail(entity.getBillNo(), entity.getOrgId());
        // 删除费用信息
        tmExceptionHandleBillFeeService.deleteDetail(entity.getBillNo(), entity.getOrgId());
    }

    public List<TmExceptionHandleBillDetailEntity> findDetailList(TmExceptionHandleBillDetailEntity entity) {
        return mapper.findDetailList(entity);
    }

    public List<TmExceptionHandleBillFeeEntity> findFeeDetailList(TmExceptionHandleBillFeeEntity entity) {
        return mapper.findFeeList(entity);
    }

    @Transactional
    public void saveDetailList(List<TmExceptionHandleBillDetailEntity> entities) {
        for (TmExceptionHandleBillDetailEntity entity : entities) {
            if (entity.getId() == null) continue;
            tmExceptionHandleBillDetailService.save(entity);
        }
    }

    @Transactional
    public void removeDetail(String detailId) {
        // 删除明细信息
        tmExceptionHandleBillDetailService.delete(new TmExceptionHandleBillDetailEntity(detailId));
    }

    @Transactional
    public void saveFeeDetailList(List<TmExceptionHandleBillFeeEntity> entities) {
        for (TmExceptionHandleBillFeeEntity entity : entities) {
            if (entity.getId() == null) continue;
            tmExceptionHandleBillFeeService.save(entity);
        }
    }

    @Transactional
    public void removeFeeDetail(String detailId) {
        // 删除明细信息
        tmExceptionHandleBillFeeService.delete(new TmExceptionHandleBillFeeEntity(detailId));
    }

    @Transactional
    public void saveImgDetail(String billNo, String orgId, String fileName, String imgFilePath, String imgUrl, User user) {
        tmAttachementDetailService.saveAppImgDetail(user, billNo, TmsConstants.IMP_UPLOAD_TYPE_EXCEPTION, null, orgId, fileName, imgFilePath, imgUrl);
    }

    public Page<TmAttachementDetail> findPicPage(Page page, TmAttachementDetail entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(tmAttachementDetailService.findList(entity));
        return page;
    }
}