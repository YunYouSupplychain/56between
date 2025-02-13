package com.yunyou.modules.tms.order.manager;

import com.google.common.collect.Lists;
import com.yunyou.common.config.Global;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.sys.OfficeType;
import com.yunyou.modules.sys.entity.Office;
import com.yunyou.modules.sys.service.AreaService;
import com.yunyou.modules.sys.service.OfficeService;
import com.yunyou.modules.sys.utils.UserUtils;
import com.yunyou.modules.tms.authority.TmAuthorityRule;
import com.yunyou.modules.tms.authority.TmAuthorityTable;
import com.yunyou.modules.tms.basic.entity.TmTransportObj;
import com.yunyou.modules.tms.basic.service.TmOutletRelationService;
import com.yunyou.modules.tms.basic.service.TmTransportObjService;
import com.yunyou.modules.tms.common.TmsConstants;
import com.yunyou.modules.tms.common.TmsException;
import com.yunyou.modules.tms.order.entity.TmReceiveLabel;
import com.yunyou.modules.tms.order.entity.TmTransportOrderHeader;
import com.yunyou.modules.tms.order.entity.TmTransportOrderLabel;
import com.yunyou.modules.tms.order.entity.TmTransportOrderRoute;
import com.yunyou.modules.tms.order.entity.extend.TmReceiveEntity;
import com.yunyou.modules.tms.order.manager.mapper.TmReceiveMapper;
import com.yunyou.modules.tms.order.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 描述：网点收货业务处理类(事务管理)
 */
@Service
@Transactional(readOnly = true)
public class TmReceiveManager extends CrudService<TmReceiveMapper, TmReceiveLabel> {
    @Autowired
    private TmReceiveLabelService tmReceiveLabelService;
    @Autowired
    private TmTransportOrderHeaderService tmTransportOrderHeaderService;
    @Autowired
    private TmTransportOrderLabelService tmTransportOrderLabelService;
    @Autowired
    private TmTransportOrderRouteService tmTransportOrderRouteService;
    @Autowired
    private TmDispatchOrderLabelService tmDispatchOrderLabelService;
    @Autowired
    private TmOutletRelationService tmOutletRelationService;
    @Autowired
    private TmTransportObjService tmTransportObjService;
    @Autowired
    private OfficeService officeService;
    @Autowired
    private AreaService areaService;
    @Autowired
    private TmUpdateStatusManager tmUpdateStatusManager;
    @Autowired
    private TmAuthorityManager tmAuthorityManager;
    @Autowired
    private TmLabelLogManager tmLabelLogManager;

    /**
     * 描述：网点收货List分页查询
     */
    @SuppressWarnings("unchecked")
    public Page<TmReceiveEntity> findPage(Page page, TmReceiveEntity qEntity) {
        dataRuleFilter(qEntity);
        String authorityScope = TmAuthorityRule.dataRule(TmAuthorityTable.TM_TRANSPORT_ORDER_ROUTE.getValue(), qEntity.getOrgId());
        qEntity.setDataScope(StringUtils.isNotBlank(qEntity.getDataScope()) ? qEntity.getDataScope() + authorityScope : authorityScope);
        /*qEntity.setReceiveOutletCodes(this.getReceiveOutletCondition(qEntity.getOrgId()));*/
        qEntity.setPage(page);
        List<TmReceiveEntity> list = mapper.findPage(qEntity);
        for (TmReceiveEntity entity : list) {
            entity.setShipCity(areaService.getFullName(entity.getShipCityId()));
            entity.setConsigneeCity(areaService.getFullName(entity.getConsigneeCityId()));
        }
        page.setList(list);
        return page;
    }

    private List<String> getReceiveOutletCondition(String opOrgId) {
        List<String> receiveOutletCodes = Lists.newArrayList();
        Office office = officeService.get(opOrgId);
        if (office != null) {
            // 如果当前操作的机构是“网点”，那么找出对应的业务对象编码作为网点筛选条件
            if (OfficeType.OUTLET.getValue().equals(office.getType())) {
                Office organizationCenter = UserUtils.getOrgCenter(opOrgId);
                if (StringUtils.isNotBlank(organizationCenter.getId())) {
                    List<TmTransportObj> list = tmTransportObjService.findOutletMatchObjByOrgId(opOrgId, organizationCenter.getId());
                    if (CollectionUtil.isNotEmpty(list)) {
                        receiveOutletCodes = list.stream().map(TmTransportObj::getTransportObjCode).collect(Collectors.toList());
                    }
                }
                // 如果无对应业务对象，采用一个不存在的作为网点筛选条件过滤
                if (CollectionUtil.isEmpty(receiveOutletCodes)) {
                    receiveOutletCodes = Lists.newArrayList("*");
                }
            } else if (OfficeType.CARRIER.getValue().equals(office.getType())) {
                // 如果当前操作的机构是“网点”，采用一个不存在的作为网点筛选条件过滤
                receiveOutletCodes = Lists.newArrayList("*");
            }
        }
        return receiveOutletCodes;
    }

    /**
     * 描述：标签收货
     */
    @Transactional
    public void receiveByLabel(String transportNo, String labelNo, String receiveOutletCode) {
        if (StringUtils.isBlank(receiveOutletCode)) {
            throw new TmsException(MessageFormat.format("运输订单{0}标签号{1}未指定收货网点，无法操作", transportNo, labelNo));
        }
        TmTransportOrderHeader orderHeader = tmTransportOrderHeaderService.getByNo(transportNo);
        TmTransportOrderLabel orderLabel = tmTransportOrderLabelService.getByTransportNoAndLabelNo(transportNo, labelNo, orderHeader.getOrgId());
        if (TmsConstants.ORDER_LABEL_STATUS_20.equals(orderLabel.getStatus())) {
            throw new TmsException(MessageFormat.format("运输订单{0}标签号{1}已签收，无法操作", transportNo, labelNo));
        }
        if (TmsConstants.ORDER_LABEL_STATUS_30.equals(orderLabel.getStatus())) {
            throw new TmsException(MessageFormat.format("运输订单{0}标签号{1}已回单，无法操作", transportNo, labelNo));
        }
        // 获取标签运输网点路由信息
        TmTransportOrderRoute orderRoute = tmTransportOrderRouteService.getByTransportNoAndLabelNo(transportNo, labelNo, orderHeader.getBaseOrgId());

        String ownerCode = orderLabel.getOwnerCode();
        String skuCode = orderLabel.getSkuCode();
        String baseOrgId = orderHeader.getBaseOrgId();
        String orgId = orderHeader.getOrgId();
        String customerNo = orderLabel.getCustomerNo();
        String dispatchNo = orderRoute.getDispatchNo();
        String nowOutletCode = orderRoute.getNowOutletCode();
        String newNextOutletCode;// 推荐下一站配送点
        if (receiveOutletCode.equals(orderHeader.getOutletCode())) {
            // 如果当前网点就是目的地城市配送网点，则下一站将送往收货方
            newNextOutletCode = orderHeader.getConsigneeCode();
        } else {
            // 根据当前网点与目的地城市配送网点推荐路线的下一个路由网点(可能无值)
            newNextOutletCode = tmOutletRelationService.getNextOutletForRecommendRoute(receiveOutletCode, orderHeader.getOutletCode(), baseOrgId);
        }

        // 插入标签网点收货记录
        TmReceiveLabel receiveLabel = new TmReceiveLabel();
        receiveLabel.setReceiveOutletCode(receiveOutletCode);
        receiveLabel.setLabelNo(labelNo);
        receiveLabel.setTransportNo(transportNo);
        receiveLabel.setCustomerNo(customerNo);
        receiveLabel.setDispatchNo(dispatchNo);
        receiveLabel.setOwnerCode(ownerCode);
        receiveLabel.setSkuCode(skuCode);
        receiveLabel.setIsAppInput(Global.N);
        receiveLabel.setBaseOrgId(baseOrgId);
        receiveLabel.setOrgId(orgId);
        tmReceiveLabelService.save(receiveLabel);
        // 生成授权数据
        tmAuthorityManager.genAuthorityData(receiveLabel);

        // 更新标签网点路由信息，上一网点=当前网点，当前网点=收货后网点，下一网点=推荐网点
        orderRoute.setPreOutletCode(nowOutletCode);
        orderRoute.setNowOutletCode(receiveOutletCode);
        orderRoute.setNextOutletCode(newNextOutletCode);
        orderRoute.setDispatchNo(TmsConstants.NULL);// 释放派车单号
        orderRoute.setPreAllocDispatchNo(TmsConstants.NULL);// 释放预派车单号
        tmTransportOrderRouteService.save(orderRoute);
        // 标签网点路由信息加入收货网点授权信息
        tmAuthorityManager.addOutletAuthorityData(receiveOutletCode, baseOrgId, TmAuthorityTable.TM_TRANSPORT_ORDER_ROUTE.getValue(), orderRoute.getId());
        // 运输订单加入收货网点授权信息
        tmAuthorityManager.addTransportAuthorityData(receiveOutletCode, orderHeader.getId());

        // 更新运输订单标签状态
        orderLabel.setStatus(TmsConstants.ORDER_LABEL_STATUS_10);
        tmTransportOrderLabelService.save(orderLabel);
        // 更新运输订单状态
        tmUpdateStatusManager.updateTransport(transportNo);

        // 插入网点收货标签履历记录
        tmLabelLogManager.saveOutletReceiveLog(transportNo, labelNo, orgId);
    }

    /**
     * 描述：取消揽收
     */
    @Transactional
    public void cancelReceive(String transportNo, String labelNo) {
        TmTransportOrderHeader orderHeader = tmTransportOrderHeaderService.getByNo(transportNo);
        TmTransportOrderLabel orderLabel = tmTransportOrderLabelService.getByTransportNoAndLabelNo(transportNo, labelNo, orderHeader.getOrgId());
        if (TmsConstants.ORDER_LABEL_STATUS_00.equals(orderLabel.getStatus())) {
            throw new TmsException(MessageFormat.format("运输订单[{0}]标签[{1}]未收货，无法操作", orderLabel.getTransportNo(), orderLabel.getLabelNo()));
        }
        if (TmsConstants.ORDER_LABEL_STATUS_20.equals(orderLabel.getStatus())) {
            throw new TmsException(MessageFormat.format("运输订单[{0}]标签[{1}]已签收，无法操作", orderLabel.getTransportNo(), orderLabel.getLabelNo()));
        }
        if (TmsConstants.ORDER_LABEL_STATUS_30.equals(orderLabel.getStatus())) {
            throw new TmsException(MessageFormat.format("运输订单[{0}]标签[{1}]已回单，无法操作", orderLabel.getTransportNo(), orderLabel.getLabelNo()));
        }
        if (tmDispatchOrderLabelService.existTransportLabel(orderLabel.getTransportNo(), orderLabel.getLabelNo(), orderLabel.getBaseOrgId())) {
            throw new TmsException(MessageFormat.format("运输订单[{0}]标签[{1}]已配载，无法操作", orderLabel.getTransportNo(), orderLabel.getLabelNo()));
        }
        // 获取标签运输网点路由信息
        TmTransportOrderRoute orderRoute = tmTransportOrderRouteService.getByTransportNoAndLabelNo(transportNo, labelNo, orderHeader.getBaseOrgId());

        String baseOrgId = orderHeader.getBaseOrgId();
        String orgId = orderHeader.getOrgId();
        String preOutletCode = orderRoute.getPreOutletCode();
        String nowOutletCode = orderRoute.getNowOutletCode();

        // 回退到上一个网点
        orderRoute.setPreOutletCode(null);
        orderRoute.setNowOutletCode(preOutletCode);
        orderRoute.setNextOutletCode(nowOutletCode);
        tmTransportOrderRouteService.save(orderRoute);
        // 删除授权数据信息
        tmAuthorityManager.removeOutletAuthorityData(nowOutletCode, baseOrgId, TmAuthorityTable.TM_TRANSPORT_ORDER_ROUTE.getValue(), orderRoute.getId());

        // 删除揽收收货记录
        TmReceiveLabel receiveLabel = new TmReceiveLabel();
        receiveLabel.setTransportNo(transportNo);
        receiveLabel.setLabelNo(orderLabel.getLabelNo());
        receiveLabel.setReceiveOutletCode(nowOutletCode);
        receiveLabel.setBaseOrgId(baseOrgId);
        receiveLabel.setOrgId(orgId);
        List<TmReceiveLabel> tmReceiveLabels = tmReceiveLabelService.findList(receiveLabel);
        for (TmReceiveLabel tmReceiveLabel : tmReceiveLabels) {
            tmReceiveLabelService.delete(tmReceiveLabel);
            // 删除授权数据信息
            tmAuthorityManager.removeOutletAuthorityData(nowOutletCode, baseOrgId, TmAuthorityTable.TM_RECEIVE_LABEL.getValue(), tmReceiveLabel.getId());
        }

        // 运输订单标签状态回退到“新建”
        orderLabel.setStatus(TmsConstants.ORDER_LABEL_STATUS_00);
        tmTransportOrderLabelService.save(orderLabel);
        // 更新运输订单状态
        tmUpdateStatusManager.updateTransport(transportNo);

        // 插入网点取消收货标签履历记录
        tmLabelLogManager.saveOutletCancelReceiveLog(transportNo, labelNo, orgId);
    }

    /**
     * 描述：强制删除
     */
    @Transactional
    public void forceRemove(String transportNo, String orgId, String baseOrgId) {
        TmReceiveLabel qEntity = new TmReceiveLabel();
        qEntity.setTransportNo(transportNo);
        qEntity.setBaseOrgId(baseOrgId);
        List<TmReceiveLabel> receiveLabels = tmReceiveLabelService.findList(qEntity);
        for (TmReceiveLabel receiveLabel : receiveLabels) {
            tmReceiveLabelService.delete(receiveLabel);
            // 删除授权数据信息
            tmAuthorityManager.remove(TmAuthorityTable.TM_RECEIVE_LABEL.getValue(), receiveLabel.getId());
        }
    }
}
