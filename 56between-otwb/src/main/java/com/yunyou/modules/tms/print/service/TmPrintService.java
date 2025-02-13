package com.yunyou.modules.tms.print.service;

import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.service.BaseService;
import com.yunyou.modules.sys.utils.DictUtils;
import com.yunyou.modules.tms.print.entity.*;
import com.yunyou.modules.tms.print.mapper.TmPrintMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TmPrintService extends BaseService {
    @Autowired
    private TmPrintMapper mapper;

    public List<RepairOrder> getPrintRepairOrder(String[] ids) {
        return mapper.getPrintRepairOrder(ids);
    }

    public List<TransportEnvelope> getPrintTransportEnvelope(String[] ids) {
        return mapper.getPrintTransportEnvelope(ids);
    }

    public List<TransportAgreement> getPrintTransportAgreement(String[] ids) {
        List<TransportAgreement> rsList = mapper.getPrintTransportAgreement(ids);
        // 按派车单号+商品名称排序
        return rsList.stream().sorted(Comparator.comparing(TransportAgreement::getDispatchNo, Comparator.nullsLast(String::compareTo))
                .thenComparing(TransportAgreement::getTransportNo, Comparator.nullsLast(String::compareTo))
                .thenComparing(TransportAgreement::getSkuName, Comparator.nullsLast(String::compareTo)))
                .collect(Collectors.toList());
    }

    public List<DispatchGoodsList> getPrintGoodsList(String[] ids) {
        return mapper.getPrintGoodsList(ids);
    }

    public List<StoreDelivery> getPrintStoreDelivery(String[] ids) {
        List<StoreDelivery> list = mapper.getPrintStoreDelivery(ids);
        if (CollectionUtil.isNotEmpty(list)) {
            for (StoreDelivery entity : list) {
                entity.setAsnOrderType(DictUtils.getDictLabel(entity.getAsnOrderType(), "SMS_ASN_ORDER_TYPE", entity.getAsnOrderType()));
            }
        }
        return list;
    }

    public List<StoreCheckAcceptOrderReport> getPrintStoreCheckAcceptOrder(String[] ids) {
        String[] types = new String[]{"15"};
        return mapper.getPrintStoreCheckAcceptOrder(ids, types);
    }

    public List<StoreCheckAcceptOrderReport> getPrintStoreCheckAcceptOrderFresh(String[] ids) {
        String[] types = new String[]{"15", "17"};
        return mapper.getPrintStoreCheckAcceptOrder(ids, types);
    }

    public List<StoreCheckAcceptOrderReport> getPrintStoreCheckAcceptOrderPAL(String[] ids) {
        return mapper.getPrintStoreCheckAcceptOrderPAL(ids);
    }

    public List<StoreCheckAcceptOrderReport> getPrintStoreCheckAcceptOrderWms(String[] ids) {
        return mapper.getPrintStoreCheckAcceptOrderWms(ids);
    }

    public List<TicketBook> getPrintTicketBook(String[] ids) {
        return mapper.getPrintTicketBook(ids);
    }

    public List<DispatchHandoverReport> getPrintDispatchHandover(String[] ids) {
        List<DispatchHandoverReport> list = mapper.getPrintDispatchHandover(ids);
        if (CollectionUtil.isNotEmpty(list)) {
            for (DispatchHandoverReport entity : list) {
                entity.setCarModel(DictUtils.getDictLabel(entity.getCarModel(), "TMS_CAR_TYPE", entity.getCarModel()));
            }
        }
        return list;
    }

    public List<TransportOrderReport> getPrintTransportOrder(String[] ids) {
        return mapper.getPrintTransportOrder(ids);
    }

    public List<DispatchOrderReport> getPrintDispatchOrder(String[] ids) {
        return mapper.getPrintDispatchOrder(ids);
    }
}
