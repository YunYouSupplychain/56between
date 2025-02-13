package com.yunyou.modules.tms.order.manager;

import com.yunyou.common.config.Global;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.service.BaseService;
import com.yunyou.modules.sys.SysParamConstants;
import com.yunyou.modules.sys.utils.SysControlParamsUtils;
import com.yunyou.modules.tms.basic.entity.TmItem;
import com.yunyou.modules.tms.basic.service.TmItemService;
import com.yunyou.modules.tms.common.TmsConstants;
import com.yunyou.modules.tms.order.entity.*;
import com.yunyou.modules.tms.order.service.*;
import com.yunyou.common.utils.number.BigDecimalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TmTotalManager extends BaseService {
    @Autowired
    private TmTransportOrderHeaderService tmTransportOrderHeaderService;
    @Autowired
    private TmTransportOrderDeliveryService tmTransportOrderDeliveryService;
    @Autowired
    private TmTransportOrderSkuService tmTransportOrderSkuService;
    @Autowired
    private TmDispatchOrderHeaderService tmDispatchOrderHeaderService;
    @Autowired
    private TmDispatchOrderLabelService tmDispatchOrderLabelService;
    @Autowired
    private TmItemService tmItemService;

    /**
     * 描述：统计数量、重量、体积
     */
    @Transactional
    public void totalByTransport(String transportNo) {
        TmTransportOrderHeader orderHeader = tmTransportOrderHeaderService.getByNo(transportNo);
        TmTransportOrderDelivery orderDelivery = tmTransportOrderDeliveryService.getByNo(transportNo, orderHeader.getOrgId());

        double totalEa = 0L;
        double totalWeight = 0D, totalCubic = 0D;
        List<TmTransportOrderSku> orderSkus = tmTransportOrderSkuService.findList(new TmTransportOrderSku(orderHeader.getTransportNo(), orderHeader.getOrgId()));
        for (TmTransportOrderSku orderSku : orderSkus) {
            totalEa = BigDecimalUtil.add(totalEa, orderSku.getQty());
            totalWeight = BigDecimalUtil.add(totalWeight, (orderSku.getWeight() == null ? 0D : orderSku.getWeight()));
            totalCubic = BigDecimalUtil.add(totalCubic, (orderSku.getCubic() == null ? 0D : orderSku.getCubic()));
        }
        orderDelivery.setTotalEaQty(totalEa);
        orderDelivery.setTotalWeight(totalWeight);
        orderDelivery.setTotalCubic(totalCubic);
        tmTransportOrderDeliveryService.save(orderDelivery);
    }

    /**
     * 描述：统计数量、重量、体积
     */
    @Transactional
    public void totalByDispatch(String dispatchNo) {
        TmDispatchOrderHeader orderHeader = tmDispatchOrderHeaderService.getByNo(dispatchNo);
        if (orderHeader == null) {
            return;
        }
        TmDispatchOrderLabel qEntity = new TmDispatchOrderLabel();
        qEntity.setDispatchNo(dispatchNo);
        qEntity.setReceiveShip(TmsConstants.RECEIVE);
        qEntity.setOrgId(orderHeader.getOrgId());
        List<TmDispatchOrderLabel> list = tmDispatchOrderLabelService.findList(qEntity);
        double totalSkuQty = 0D, totalWeight = 0D, totalCubic = 0D;
        // TMS参数：派车单按商品统计重量、体积（Y：是 N：否） 注：按商品统计指的是取商品基础数据的毛重*数量，体积同上；N：否，默认按标签统计
        final String DISPATCH_TOTAL_BY_SKU = SysControlParamsUtils.getValue(SysParamConstants.DISPATCH_TOTAL_BY_SKU, orderHeader.getOrgId());
        if (CollectionUtil.isNotEmpty(list)) {
            for (TmDispatchOrderLabel o : list) {
                double qty = o.getQty() == null ? 0D : o.getQty();
                double weight = 0D, cubic = 0D;

                if (Global.Y.equals(DISPATCH_TOTAL_BY_SKU)) {
                    if (StringUtils.isNotBlank(o.getOwnerCode()) && StringUtils.isNotBlank(o.getSkuCode())) {
                        TmItem tmItem = tmItemService.getByOwnerAndSku(o.getOwnerCode(), o.getSkuCode(), o.getBaseOrgId());
                        if (tmItem != null) {
                            weight = tmItem.getGrossweight() == null ? 0D : tmItem.getGrossweight();
                            cubic = tmItem.getCubic() == null ? 0D : tmItem.getCubic();
                        }
                    }
                } else {
                    weight = o.getWeight() == null ? 0D : o.getWeight();
                    cubic = o.getCubic() == null ? 0D : o.getCubic();
                }
                totalSkuQty = BigDecimalUtil.add(totalSkuQty, qty);
                totalWeight = BigDecimalUtil.add(totalWeight, weight);
                totalCubic = BigDecimalUtil.add(totalCubic, cubic);
            }
        }
        orderHeader.setTotalQty(list.size());
        orderHeader.setTotalSkuQty(totalSkuQty);
        orderHeader.setTotalWeight(totalWeight);
        orderHeader.setTotalCubic(totalCubic);
        tmDispatchOrderHeaderService.save(orderHeader);
    }
}
