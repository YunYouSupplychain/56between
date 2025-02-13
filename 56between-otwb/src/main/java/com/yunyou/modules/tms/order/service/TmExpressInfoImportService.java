package com.yunyou.modules.tms.order.service;

import com.yunyou.common.enums.CustomerType;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.sys.GenNoType;
import com.yunyou.modules.sys.service.SynchronizedNoService;
import com.yunyou.modules.tms.basic.entity.TmTransportObj;
import com.yunyou.modules.tms.basic.service.TmTransportObjService;
import com.yunyou.modules.tms.common.TmsException;
import com.yunyou.modules.tms.order.entity.TmExpressInfoImport;
import com.yunyou.modules.tms.order.entity.TmExpressInfoImportDetail;
import com.yunyou.modules.tms.order.entity.TmTransportOrderDelivery;
import com.yunyou.modules.tms.order.entity.TmTransportOrderHeader;
import com.yunyou.modules.tms.order.mapper.TmExpressInfoImportMapper;
import com.yunyou.modules.wms.common.entity.WmsConstants;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 快递单号导入更新Service
 *
 * @author zyf
 * @version 2020-04-13
 */
@Service
@Transactional(readOnly = true)
public class TmExpressInfoImportService extends CrudService<TmExpressInfoImportMapper, TmExpressInfoImport> {
    @Autowired
    private SynchronizedNoService noService;
    @Autowired
    private TmExpressInfoImportDetailService tmExpressInfoImportDetailService;
    @Autowired
    private TmTransportOrderHeaderService tmTransportOrderHeaderService;
    @Autowired
    private TmTransportOrderDeliveryService tmTransportOrderDeliveryService;
    @Autowired
    private TmTransportOrderTrackService tmTransportOrderTrackService;
    @Autowired
    private TmTransportObjService tmTransportObjService;

    public void checkImportFile(TmExpressInfoImportDetail detail, String orgId) {
        if (StringUtils.isBlank(detail.getMailNo())) {
            throw new TmsException("客户订单号【" + detail.getCustomerNo() + "】的面单号为空！");
        }
        if (StringUtils.isBlank(detail.getCarrierCode())) {
            throw new TmsException("客户订单号【" + detail.getCustomerNo() + "】的承运商为空！");
        }
        TmTransportOrderHeader tmTransportOrderHeader = tmTransportOrderHeaderService.getByCustomerNo(detail.getCustomerNo(), orgId);
        if (tmTransportOrderHeader == null) {
            throw new TmsException("客户订单号【" + detail.getCustomerNo() + "】不存在！");
        }
        TmTransportObj tmTransportObj = tmTransportObjService.getByCode(detail.getCarrierCode(), tmTransportOrderHeader.getBaseOrgId());
        if (tmTransportObj == null || !tmTransportObj.getTransportObjType().contains(CustomerType.CARRIER.getCode())) {
            throw new TmsException("承运商【" + detail.getCarrierCode() + "】不存在！");
        }
    }

    @Transactional
    public void importFile(List<TmExpressInfoImportDetail> detailList, String fileName, String importReason, String orgId) {
        TmExpressInfoImport importInfo = new TmExpressInfoImport();
        importInfo.setImportNo(noService.getDocumentNo(GenNoType.TM_EXPRESS_IMPORT_NO.name()));
        importInfo.setImportReason(importReason);
        importInfo.setFileName(fileName);
        importInfo.setOrgId(orgId);
        this.save(importInfo);
        if (CollectionUtil.isEmpty(detailList)) {
            return;
        }
        for (TmExpressInfoImportDetail detail : detailList) {
            TmExpressInfoImportDetail saveDetail = new TmExpressInfoImportDetail();
            BeanUtils.copyProperties(detail, saveDetail);
            saveDetail.setIsNewRecord(false);
            saveDetail.setImportNo(importInfo.getImportNo());
            saveDetail.setOrgId(orgId);
            tmExpressInfoImportDetailService.save(saveDetail);

            TmTransportOrderHeader tmTransportOrderHeader = tmTransportOrderHeaderService.getByCustomerNo(detail.getCustomerNo(), orgId);
            if (tmTransportOrderHeader != null) {
                tmTransportOrderHeader.setTrackingNo(detail.getMailNo());
                tmTransportOrderHeaderService.save(tmTransportOrderHeader);

                TmTransportOrderDelivery tmTransportOrderDelivery = tmTransportOrderDeliveryService.getByNo(tmTransportOrderHeader.getTransportNo(), tmTransportOrderHeader.getOrgId());
                tmTransportOrderDelivery.setCarrierCode(detail.getCarrierCode());
                tmTransportOrderDeliveryService.save(tmTransportOrderDelivery);
                tmTransportOrderTrackService.removeByTransportNo(tmTransportOrderHeader.getTransportNo(), tmTransportOrderHeader.getOrgId());
            }
            updateFeedbackFlag(detail.getCustomerNo(), orgId);
        }
    }

    @Transactional
    public void updateFeedbackFlag(String customerNo, String orgId) {
        mapper.execUpdateSql("update wm_so_header set edi_send_time = null, is_edi_send = " + WmsConstants.EDI_FLAG_10 + " where def5 = '" + customerNo + "' and org_id = '" + orgId + "'");
    }
}