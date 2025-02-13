package com.yunyou.modules.tms.order.service;

import com.yunyou.common.config.GlobalDataRule;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.sys.entity.User;
import com.yunyou.modules.tms.basic.entity.extend.TmTransportObjEntity;
import com.yunyou.modules.tms.basic.service.TmTransportObjService;
import com.yunyou.modules.tms.common.TmsConstants;
import com.yunyou.modules.tms.order.entity.TmDispatchOrderHeader;
import com.yunyou.modules.tms.order.entity.TmTransportOrderHeader;
import com.yunyou.modules.tms.order.entity.TmTransportOrderRoute;
import com.yunyou.modules.tms.order.entity.TmTransportOrderTrack;
import com.yunyou.modules.tms.order.mapper.TmTransportOrderTrackMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

/**
 * 运输订单跟踪信息表Service
 *
 * @author zyf
 * @version 2020-04-10
 */
@Service
@Transactional(readOnly = true)
public class TmTransportOrderTrackService extends CrudService<TmTransportOrderTrackMapper, TmTransportOrderTrack> {
    @Autowired
    private TmTransportOrderHeaderService tmTransportOrderHeaderService;
    @Autowired
    private TmTransportOrderRouteService tmTransportOrderRouteService;
    @Autowired
    private TmDispatchOrderHeaderService tmDispatchOrderHeaderService;
    @Autowired
    private TmTransportObjService tmTransportObjService;

    @Override
    public Page<TmTransportOrderTrack> findPage(Page page, TmTransportOrderTrack tmTransportOrderTrack) {
        tmTransportOrderTrack.setDataScope(GlobalDataRule.getDataRuleSql("a.org_id", tmTransportOrderTrack.getOrgId()));
        dataRuleFilter(tmTransportOrderTrack);
        tmTransportOrderTrack.setPage(page);
        page.setList(mapper.findPage(tmTransportOrderTrack));
        return page;
    }

    public List<TmTransportOrderTrack> getSignList(String transportNo, String orgId) {
        return mapper.findSignList(transportNo, orgId);
    }

    @Transactional
    public void saveTrackNode(String transportNo, String labelNo, String opNode, User user) {
        TmTransportOrderHeader transportOrder = tmTransportOrderHeaderService.getByNo(transportNo);

        String baseOrgId = transportOrder.getBaseOrgId();
        String dispatchNo = null;
        String driver = null;
        String phone = null;

        TmTransportOrderRoute orderRoute = tmTransportOrderRouteService.getByTransportNoAndLabelNo(transportNo, labelNo, baseOrgId);
        TmTransportObjEntity nowOutlet = tmTransportObjService.getEntity(orderRoute.getNowOutletCode(), baseOrgId);

        if (StringUtils.isNotBlank(orderRoute.getDispatchNo()) && !TmsConstants.NULL.equals(orderRoute.getDispatchNo())) {
            TmDispatchOrderHeader dispatchOrder = tmDispatchOrderHeaderService.getByNo(orderRoute.getDispatchNo());
            if (dispatchOrder != null) {
                dispatchNo = dispatchOrder.getDispatchNo();
                driver = dispatchOrder.getDriver();
                phone = dispatchOrder.getDriverTel();
            }
        }

        TmTransportOrderTrack track = new TmTransportOrderTrack();
        track.setTransportNo(transportNo);
        track.setLabelNo(labelNo);
        track.setOpPerson(user.getName());
        track.setOpNode(opNode);
        track.setOpTime(new Date());
        track.setDispatchNo(dispatchNo);
        track.setDriver(driver);
        track.setPhone(phone);
        track.setOrgId(transportOrder.getOrgId());
        track.setBaseOrgId(transportOrder.getBaseOrgId());
        switch (opNode) {
            case TmsConstants.TRANSPORT_TRACK_NODE_RECEIVE:
                track.setReceiveOutletCode(nowOutlet.getTransportObjCode());
                track.setReceiveOutletName(nowOutlet.getTransportObjName());
                track.setOperation(MessageFormat.format("快件[{0}] {1} 已收件", labelNo, nowOutlet.getTransportObjName()));
                break;
            case TmsConstants.TRANSPORT_TRACK_NODE_SHIP:
                TmTransportObjEntity nextOutlet = tmTransportObjService.getEntity(orderRoute.getNextOutletCode(), baseOrgId);
                if (nextOutlet == null) {
                    nextOutlet = new TmTransportObjEntity();
                }
                track.setDeliverOutletCode(nowOutlet.getTransportObjCode());
                track.setDeliverOutletName(nowOutlet.getTransportObjName());
                track.setReceiveOutletCode(nextOutlet.getTransportObjCode());
                track.setReceiveOutletName(nextOutlet.getTransportObjName());
                track.setOperation(MessageFormat.format("快件[{0}] {1} 已发出,下一站 {2}", labelNo, nowOutlet.getTransportObjName(), nextOutlet.getTransportObjName()));
                break;
            case TmsConstants.TRANSPORT_TRACK_NODE_ARRIVE:
                track.setReceiveOutletCode(nowOutlet.getTransportObjCode());
                track.setReceiveOutletName(nowOutlet.getTransportObjName());
                track.setOperation(MessageFormat.format("快件[{0}] {1} 已收入", labelNo, nowOutlet.getTransportObjName()));
                break;
            case TmsConstants.TRANSPORT_TRACK_NODE_DELIVER:
                track.setDeliverOutletCode(nowOutlet.getTransportObjCode());
                track.setDeliverOutletName(nowOutlet.getTransportObjName());
                track.setOperation(MessageFormat.format("快件[{0}] {1} 派件员：{2} 电话：{3} 当前正在为您派件", labelNo, nowOutlet.getTransportObjName(), driver, phone));
                break;
            case TmsConstants.TRANSPORT_TRACK_NODE_SIGN:
                track.setOperation(MessageFormat.format("快件[{0}] 已签收", labelNo));
                break;
            default:
        }
        super.save(track);
    }

    @Transactional
    public void removeReceiveTrackNode(String transportNo, String labelNo, String opNode, String outletCode) {
        mapper.removeReceiveTrackNode(transportNo, labelNo, opNode, outletCode);
    }

    @Transactional
    public void removeByTransportNo(String transportNo, String orgId) {
        mapper.removeByTransportNo(transportNo, orgId);
    }

    @Transactional
    public void removeByTransportNoAndLabelNo(String transportNo, String labelNo, String orgId) {
        mapper.removeByTransportNoAndLabelNo(transportNo, labelNo, orgId);
    }
}