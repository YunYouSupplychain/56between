package com.yunyou.modules.tms.order.manager;

import com.yunyou.core.service.BaseService;
import com.yunyou.modules.tms.common.TmsConstants;
import com.yunyou.modules.tms.order.entity.*;
import com.yunyou.modules.tms.order.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 标签履历Manager
 *
 * @author 刘剑华
 * @since 2023/11/27 16:58
 */
@Service
@Transactional(readOnly = true)
public class TmLabelLogManager extends BaseService {
    @Autowired
    private TmLabelLogService tmLabelLogService;
    @Autowired
    private TmTransportOrderHeaderService tmTransportOrderHeaderService;
    @Autowired
    private TmTransportOrderLabelService tmTransportOrderLabelService;
    @Autowired
    private TmTransportOrderRouteService tmTransportOrderRouteService;
    @Autowired
    private TmDispatchOrderLabelService tmDispatchOrderLabelService;

    /**
     * 插入网点收货标签履历记录
     */
    @Transactional
    public void saveOutletReceiveLog(String transportNo, String labelNo, String orgId) {
        TmTransportOrderLabel tmTransportOrderLabel =
                tmTransportOrderLabelService.getByTransportNoAndLabelNo(transportNo, labelNo, orgId);
        TmTransportOrderRoute tmTransportOrderRoute =
                tmTransportOrderRouteService.getByTransportNoAndLabelNo(transportNo, labelNo, tmTransportOrderLabel.getBaseOrgId());

        String ownerCode = tmTransportOrderLabel.getOwnerCode();
        String skuCode = tmTransportOrderLabel.getSkuCode();
        BigDecimal qty = tmTransportOrderLabel.getQty() == null ? null : BigDecimal.valueOf(tmTransportOrderLabel.getQty());
        BigDecimal weight = tmTransportOrderLabel.getWeight() == null ? null : BigDecimal.valueOf(tmTransportOrderLabel.getWeight());
        BigDecimal cubic = tmTransportOrderLabel.getCubic() == null ? null : BigDecimal.valueOf(tmTransportOrderLabel.getCubic());

        TmLabelLog tmLabelLog = new TmLabelLog();
        tmLabelLog.setOpTime(new Date());
        tmLabelLog.setOpType(TmsConstants.OP_TYPE_00);
        tmLabelLog.setLabelNo(labelNo);
        tmLabelLog.setTransportNo(transportNo);
        tmLabelLog.setLineNo(tmTransportOrderLabel.getLineNo());
        tmLabelLog.setDispatchNo(tmTransportOrderRoute.getDispatchNo());
        tmLabelLog.setOwnerCode(ownerCode);
        tmLabelLog.setSkuCode(skuCode);
        tmLabelLog.setQty(qty);
        tmLabelLog.setWeight(weight);
        tmLabelLog.setVolume(cubic);
        tmLabelLog.setPreOutletCode(tmTransportOrderRoute.getPreOutletCode());
        tmLabelLog.setNowOutletCode(tmTransportOrderRoute.getNowOutletCode());
        tmLabelLog.setNextOutletCode(tmTransportOrderRoute.getNextOutletCode());
        tmLabelLog.setBaseOrgId(tmTransportOrderLabel.getBaseOrgId());
        tmLabelLog.setOrgId(tmTransportOrderLabel.getOrgId());
        tmLabelLogService.save(tmLabelLog);
    }

    /**
     * 插入网点取消收货标签履历记录
     */
    @Transactional
    public void saveOutletCancelReceiveLog(String transportNo, String labelNo, String orgId) {
        TmTransportOrderLabel tmTransportOrderLabel =
                tmTransportOrderLabelService.getByTransportNoAndLabelNo(transportNo, labelNo, orgId);
        TmTransportOrderRoute tmTransportOrderRoute =
                tmTransportOrderRouteService.getByTransportNoAndLabelNo(transportNo, labelNo, tmTransportOrderLabel.getBaseOrgId());

        String ownerCode = tmTransportOrderLabel.getOwnerCode();
        String skuCode = tmTransportOrderLabel.getSkuCode();
        BigDecimal qty = tmTransportOrderLabel.getQty() == null ? null : BigDecimal.valueOf(tmTransportOrderLabel.getQty());
        BigDecimal weight = tmTransportOrderLabel.getWeight() == null ? null : BigDecimal.valueOf(tmTransportOrderLabel.getWeight());
        BigDecimal cubic = tmTransportOrderLabel.getCubic() == null ? null : BigDecimal.valueOf(tmTransportOrderLabel.getCubic());

        TmLabelLog tmLabelLog = new TmLabelLog();
        tmLabelLog.setOpTime(new Date());
        tmLabelLog.setOpType(TmsConstants.OP_TYPE_10);
        tmLabelLog.setLabelNo(labelNo);
        tmLabelLog.setTransportNo(transportNo);
        tmLabelLog.setLineNo(tmTransportOrderLabel.getLineNo());
        tmLabelLog.setDispatchNo(tmTransportOrderRoute.getDispatchNo());
        tmLabelLog.setOwnerCode(ownerCode);
        tmLabelLog.setSkuCode(skuCode);
        tmLabelLog.setQty(qty);
        tmLabelLog.setWeight(weight);
        tmLabelLog.setVolume(cubic);
        tmLabelLog.setPreOutletCode(tmTransportOrderRoute.getPreOutletCode());
        tmLabelLog.setNowOutletCode(tmTransportOrderRoute.getNowOutletCode());
        tmLabelLog.setNextOutletCode(tmTransportOrderRoute.getNextOutletCode());
        tmLabelLog.setBaseOrgId(tmTransportOrderLabel.getBaseOrgId());
        tmLabelLog.setOrgId(tmTransportOrderLabel.getOrgId());
        tmLabelLogService.save(tmLabelLog);
    }

    /**
     * 插入网点发货标签履历记录
     */
    @Transactional
    public void saveOutletDeliveryLog(String transportNo, String labelNo, String orgId) {
        TmTransportOrderLabel tmTransportOrderLabel =
                tmTransportOrderLabelService.getByTransportNoAndLabelNo(transportNo, labelNo, orgId);
        TmTransportOrderRoute tmTransportOrderRoute =
                tmTransportOrderRouteService.getByTransportNoAndLabelNo(transportNo, labelNo, tmTransportOrderLabel.getBaseOrgId());

        String ownerCode = tmTransportOrderLabel.getOwnerCode();
        String skuCode = tmTransportOrderLabel.getSkuCode();
        BigDecimal qty = tmTransportOrderLabel.getQty() == null ? null : BigDecimal.valueOf(tmTransportOrderLabel.getQty());
        BigDecimal weight = tmTransportOrderLabel.getWeight() == null ? null : BigDecimal.valueOf(tmTransportOrderLabel.getWeight());
        BigDecimal cubic = tmTransportOrderLabel.getCubic() == null ? null : BigDecimal.valueOf(tmTransportOrderLabel.getCubic());

        TmLabelLog tmLabelLog = new TmLabelLog();
        tmLabelLog.setOpTime(new Date());
        tmLabelLog.setOpType(TmsConstants.OP_TYPE_20);
        tmLabelLog.setLabelNo(labelNo);
        tmLabelLog.setTransportNo(transportNo);
        tmLabelLog.setLineNo(tmTransportOrderLabel.getLineNo());
        tmLabelLog.setDispatchNo(tmTransportOrderRoute.getDispatchNo());
        tmLabelLog.setOwnerCode(ownerCode);
        tmLabelLog.setSkuCode(skuCode);
        tmLabelLog.setQty(qty);
        tmLabelLog.setWeight(weight);
        tmLabelLog.setVolume(cubic);
        tmLabelLog.setPreOutletCode(tmTransportOrderRoute.getPreOutletCode());
        tmLabelLog.setNowOutletCode(tmTransportOrderRoute.getNowOutletCode());
        tmLabelLog.setNextOutletCode(tmTransportOrderRoute.getNextOutletCode());
        tmLabelLog.setBaseOrgId(tmTransportOrderLabel.getBaseOrgId());
        tmLabelLog.setOrgId(tmTransportOrderLabel.getOrgId());
        tmLabelLogService.save(tmLabelLog);
    }

    /**
     * 插入已发车标签履历记录
     */
    @Transactional
    public void saveDepartureLog(String dispatchNo, String orgId) {
        TmDispatchOrderLabel qEntity = new TmDispatchOrderLabel();
        qEntity.setDispatchNo(dispatchNo);
        qEntity.setReceiveShip(TmsConstants.RECEIVE);
        qEntity.setOrgId(orgId);
        List<TmDispatchOrderLabel> orderLabels = tmDispatchOrderLabelService.findList(qEntity);
        for (TmDispatchOrderLabel orderLabel : orderLabels) {
            TmTransportOrderHeader tmTransportOrderHeader =
                    tmTransportOrderHeaderService.getByNo(orderLabel.getTransportNo());
            TmTransportOrderLabel tmTransportOrderLabel =
                    tmTransportOrderLabelService.getByTransportNoAndLabelNo(tmTransportOrderHeader.getTransportNo(), orderLabel.getLabelNo(), tmTransportOrderHeader.getOrgId());
            TmTransportOrderRoute tmTransportOrderRoute =
                    tmTransportOrderRouteService.getByTransportNoAndLabelNo(tmTransportOrderHeader.getTransportNo(), orderLabel.getLabelNo(), tmTransportOrderHeader.getBaseOrgId());

            String ownerCode = tmTransportOrderLabel.getOwnerCode();
            String skuCode = tmTransportOrderLabel.getSkuCode();
            BigDecimal qty = tmTransportOrderLabel.getQty() == null ? null : BigDecimal.valueOf(tmTransportOrderLabel.getQty());
            BigDecimal weight = tmTransportOrderLabel.getWeight() == null ? null : BigDecimal.valueOf(tmTransportOrderLabel.getWeight());
            BigDecimal cubic = tmTransportOrderLabel.getCubic() == null ? null : BigDecimal.valueOf(tmTransportOrderLabel.getCubic());

            TmLabelLog tmLabelLog = new TmLabelLog();
            tmLabelLog.setOpTime(new Date());
            tmLabelLog.setOpType(TmsConstants.OP_TYPE_30);
            tmLabelLog.setLabelNo(orderLabel.getLabelNo());
            tmLabelLog.setTransportNo(tmTransportOrderLabel.getTransportNo());
            tmLabelLog.setLineNo(tmTransportOrderLabel.getLineNo());
            tmLabelLog.setDispatchNo(tmTransportOrderRoute.getDispatchNo());
            tmLabelLog.setOwnerCode(ownerCode);
            tmLabelLog.setSkuCode(skuCode);
            tmLabelLog.setQty(qty);
            tmLabelLog.setWeight(weight);
            tmLabelLog.setVolume(cubic);
            tmLabelLog.setPreOutletCode(tmTransportOrderRoute.getPreOutletCode());
            tmLabelLog.setNowOutletCode(tmTransportOrderRoute.getNowOutletCode());
            tmLabelLog.setNextOutletCode(tmTransportOrderRoute.getNextOutletCode());
            tmLabelLog.setBaseOrgId(tmTransportOrderLabel.getBaseOrgId());
            tmLabelLog.setOrgId(tmTransportOrderLabel.getOrgId());
            tmLabelLogService.save(tmLabelLog);
        }
    }

    /**
     * 插入已签收标签履历记录
     */
    @Transactional
    public void saveSignLog(String transportNo, String labelNo, String orgId) {
        TmTransportOrderLabel tmTransportOrderLabel =
                tmTransportOrderLabelService.getByTransportNoAndLabelNo(transportNo, labelNo, orgId);
        TmTransportOrderRoute tmTransportOrderRoute =
                tmTransportOrderRouteService.getByTransportNoAndLabelNo(transportNo, labelNo, tmTransportOrderLabel.getBaseOrgId());

        String ownerCode = tmTransportOrderLabel.getOwnerCode();
        String skuCode = tmTransportOrderLabel.getSkuCode();
        BigDecimal qty = tmTransportOrderLabel.getQty() == null ? null : BigDecimal.valueOf(tmTransportOrderLabel.getQty());
        BigDecimal weight = tmTransportOrderLabel.getWeight() == null ? null : BigDecimal.valueOf(tmTransportOrderLabel.getWeight());
        BigDecimal cubic = tmTransportOrderLabel.getCubic() == null ? null : BigDecimal.valueOf(tmTransportOrderLabel.getCubic());

        TmLabelLog tmLabelLog = new TmLabelLog();
        tmLabelLog.setOpTime(new Date());
        tmLabelLog.setOpType(TmsConstants.OP_TYPE_40);
        tmLabelLog.setLabelNo(labelNo);
        tmLabelLog.setTransportNo(transportNo);
        tmLabelLog.setLineNo(tmTransportOrderLabel.getLineNo());
        tmLabelLog.setDispatchNo(tmTransportOrderRoute.getDispatchNo());
        tmLabelLog.setOwnerCode(ownerCode);
        tmLabelLog.setSkuCode(skuCode);
        tmLabelLog.setQty(qty);
        tmLabelLog.setWeight(weight);
        tmLabelLog.setVolume(cubic);
        tmLabelLog.setPreOutletCode(tmTransportOrderRoute.getPreOutletCode());
        tmLabelLog.setNowOutletCode(tmTransportOrderRoute.getNowOutletCode());
        tmLabelLog.setNextOutletCode(tmTransportOrderRoute.getNextOutletCode());
        tmLabelLog.setBaseOrgId(tmTransportOrderLabel.getBaseOrgId());
        tmLabelLog.setOrgId(tmTransportOrderLabel.getOrgId());
        tmLabelLogService.save(tmLabelLog);
    }
}
