package com.yunyou.modules.wms.rf.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.modules.sys.entity.User;
import com.yunyou.modules.sys.utils.DictUtils;
import com.yunyou.modules.sys.utils.UserUtils;
import com.yunyou.modules.wms.common.entity.WarehouseException;
import com.yunyou.modules.wms.common.entity.WmsCodeMaster;
import com.yunyou.modules.wms.outbound.entity.BanQinWmLdDetailEntity;
import com.yunyou.modules.wms.outbound.entity.BanQinWmLdEntity;
import com.yunyou.modules.wms.outbound.entity.BanQinWmLdHeader;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoAllocEntity;
import com.yunyou.modules.wms.outbound.service.BanQinOutboundLoadingService;
import com.yunyou.modules.wms.outbound.service.BanQinWmLdDetailService;
import com.yunyou.modules.wms.outbound.service.BanQinWmLdHeaderService;
import com.yunyou.modules.wms.rf.entity.*;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * RF装车service
 * @author ZYF
 * @version 2020/05/18
 */
@Service
public class RfLoadService {
    @Autowired
    private BanQinWmLdHeaderService banQinWmLdHeaderService;
    @Autowired
    private BanQinWmLdDetailService banQinWmLdDetailService;
    @Autowired
    private BanQinOutboundLoadingService outboundLoadingService;

    public ResultMessage queryLdOrderInfo(WMSRF_LD_QueryLdInfo_Request request) {
        ResultMessage msg = new ResultMessage();
        User user = UserUtils.getUser();
        String orgId = user.getOffice().getId();
        if (StringUtils.isNotEmpty(request.getLdNo())) {
            BanQinWmLdHeader ldHeader = banQinWmLdHeaderService.getByLdNo(request.getLdNo(), orgId);
            ResultMessage checkMsg = checkLdOrder(ldHeader);
            if (!checkMsg.isSuccess()) {
                msg = checkMsg;
                return msg;
            }
            WMSRF_LD_QueryLdInfo_Response response = new WMSRF_LD_QueryLdInfo_Response();
            response.setLdNo(ldHeader.getLdNo());
            response.setVehicleNo(ldHeader.getVehicleNo());
            response.setVehicleType(DictUtils.getDictLabel(ldHeader.getVehicleType(), "TMS_CAR_TYPE", ldHeader.getVehicleType()));
            msg.setData(response);
        } else {
            msg.setSuccess(false);
            msg.setMessage("装车单号为空！");
        }
        return msg;
    }

    public ResultMessage queryLdOrderList(WMSRF_LD_QueryLdList_Request request) {
        ResultMessage msg = new ResultMessage();
        User user = UserUtils.getUser();
        String orgId = user.getOffice().getId();
        BanQinWmLdEntity condition = new BanQinWmLdEntity();
        List<String> statusList = Lists.newArrayList();
        statusList.add(WmsCodeMaster.LD_NEW.getCode());
        statusList.add(WmsCodeMaster.LD_PART_LOAD.getCode());
        statusList.add(WmsCodeMaster.LD_FULL_LOAD.getCode());
        condition.setStatusList(statusList);
        condition.setOrgId(orgId);
        List<BanQinWmLdEntity> ldEntityList = banQinWmLdHeaderService.findEntity(condition);
        if (CollectionUtil.isNotEmpty(ldEntityList)) {
            List<WMSRF_LD_QueryLdList_Response> responses = Lists.newArrayList();
            for (BanQinWmLdEntity ldEntity : ldEntityList) {
                WMSRF_LD_QueryLdList_Response response = new WMSRF_LD_QueryLdList_Response();
                response.setLdNo(ldEntity.getLdNo());
                response.setVehicleNo(ldEntity.getVehicleNo());
                response.setVehicleType(DictUtils.getDictLabel(ldEntity.getVehicleType(), "TMS_CAR_TYPE", ldEntity.getVehicleType()));
                responses.add(response);
            }
            msg.setData(responses);
        } else {
            msg.setSuccess(false);
            msg.setMessage("未找到装车单！");
        }
        return msg;
    }

    @Transactional
    public ResultMessage loadByTraceId(WMSRF_LD_LoadByTraceId_Request request) {
        ResultMessage msg = new ResultMessage();
        User user = UserUtils.getUser();
        String orgId = user.getOffice().getId();
        if (StringUtils.isNotEmpty(request.getLdNo()) && StringUtils.isNotEmpty(request.getTraceId())) {
            // 获取装车单
            BanQinWmLdEntity ldEntity = banQinWmLdHeaderService.getWmLdHeaderEntityQueryItemByLdNo(request.getLdNo(), orgId);
            ResultMessage checkMsg = checkLdOrder(ldEntity);
            if (!checkMsg.isSuccess()) {
                msg = checkMsg;
                return msg;
            }
            // 校验获取包裹信息
            BanQinWmSoAllocEntity traceEntity = banQinWmLdDetailService.getTraceByTraceId(request.getTraceId(), ldEntity);
            if (traceEntity == null) {
                msg.setSuccess(false);
                msg.setMessage("包裹信息不存在！");
                return msg;
            }
            try {
                // 根据包裹生成装车明细
                String[] traceIds = new String[1];
                traceIds[0] = request.getTraceId();
                banQinWmLdDetailService.addLdDetailByTraceId(request.getLdNo(), traceIds, orgId);
                // 根据箱号装载
                outboundLoadingService.outboundLoadingByTraceId(request.getLdNo(), request.getTraceId(), orgId);
                // 返回箱号
                List<BanQinWmLdDetailEntity> detailEntityList = banQinWmLdDetailService.getTraceIdByLdNo(request.getLdNo(), orgId);
                List<String> toIdList = detailEntityList.stream().map(BanQinWmLdDetailEntity::getToId).collect(Collectors.toList());
                msg.setData(toIdList);
            } catch (WarehouseException e) {
                msg.addMessage(e.getMessage());
                msg.setSuccess(false);
            }
        } else {
            msg.setSuccess(false);
            msg.setMessage("单号为空！");
        }
        return msg;
    }

    private ResultMessage checkLdOrder(BanQinWmLdHeader ldHeader){
        ResultMessage msg = new ResultMessage();
        if (ldHeader == null) {
            msg.setSuccess(false);
            msg.setMessage("装车单不存在！");
            return msg;
        }
        if (WmsCodeMaster.LD_PART_DELIVERY.getCode().equals(ldHeader.getStatus()) || WmsCodeMaster.LD_FULL_DELIVERY.getCode().equals(ldHeader.getStatus())) {
            msg.setSuccess(false);
            msg.setMessage("装车单已进行交接！");
            return msg;
        }
        return msg;
    }

}
