package com.yunyou.modules.tms.print.mapper;

import java.util.List;

import com.yunyou.modules.tms.print.entity.*;
import org.apache.ibatis.annotations.Param;

import com.yunyou.core.persistence.annotation.MyBatisMapper;

@MyBatisMapper
public interface TmPrintMapper {

    List<RepairOrder> getPrintRepairOrder(@Param("ids") String[] ids);

    List<TransportEnvelope> getPrintTransportEnvelope(@Param("ids") String[] ids);

    List<TransportAgreement> getPrintTransportAgreement(@Param("ids") String[] ids);

    List<TransportAgreement> getTransportAgreement(String transportNo);

    List<DispatchGoodsList> getPrintGoodsList(@Param("ids") String[] ids);

    List<DispatchGoodsList> getGoodsList(String transport);

    List<StoreDelivery> getPrintStoreDelivery(@Param("ids") String[] ids);

    List<StoreCheckAcceptOrderReport> getPrintStoreCheckAcceptOrder(@Param("ids") String[] ids, @Param("types") String[] types);

    List<StoreCheckAcceptOrderReport> getPrintStoreCheckAcceptOrderPAL(@Param("ids") String[] ids);

    List<TicketBook> getPrintTicketBook(@Param("ids") String[] ids);

    List<DispatchHandoverReport> getPrintDispatchHandover(@Param("ids") String[] ids);

    List<StoreCheckAcceptOrderReport> getPrintStoreCheckAcceptOrderWms(@Param("ids") String[] ids);

    List<TransportOrderReport> getPrintTransportOrder(@Param("ids") String[] ids);

    List<DispatchOrderReport> getPrintDispatchOrder(@Param("ids") String[] ids);
}
