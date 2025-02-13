package com.yunyou.modules.wms.rf.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.time.DateUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.modules.sys.entity.User;
import com.yunyou.modules.sys.utils.UserUtils;
import com.yunyou.modules.wms.inventory.entity.BanQinWmInvLotLocEntity;
import com.yunyou.modules.wms.inventory.service.BanQinWmInvLotLocService;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoAllocEntity;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoEntity;
import com.yunyou.modules.wms.outbound.service.BanQinWmSoAllocService;
import com.yunyou.modules.wms.outbound.service.BanQinWmSoHeaderService;
import com.yunyou.modules.wms.rf.entity.*;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * RF库存操作
 * @author WMJ
 * @version 2019/07/11
 */
@Service
public class RfInventoryService {
    @Autowired
    private RfLotAttrConfigService rfLotAttrConfigService;
    @Autowired
    private RfPackageConfigService rfPackageConfigService;
    @Autowired
    private BanQinWmInvLotLocService wmInvLotLocService;
    @Autowired
    private BanQinWmSoHeaderService wmSoHeaderService;
    @Autowired
    private BanQinWmSoAllocService wmSoAllocService;

    public ResultMessage querySkuInv(WMSRF_INV_QuerySkuInv_Request request) {
        ResultMessage msg = new ResultMessage();
        User user = UserUtils.getUser();
        List<BanQinWmInvLotLocEntity> items = wmInvLotLocService.rfInvGetSkuDetailQuery(request.getLocationCode(), request.getTraceId(), request.getLotNum(), request.getSkuCode(), user.getOffice().getId());
        if (CollectionUtil.isNotEmpty(items)) {
            List<WMSRF_INV_SkuInvDetail_Response> result = Lists.newArrayList();
            for (BanQinWmInvLotLocEntity entity : items) {
                WMSRF_INV_SkuInvDetail_Response response = new WMSRF_INV_SkuInvDetail_Response();
                BeanUtils.copyProperties(entity, response);
                if (null != entity.getLotAtt01()) {
                    response.setLotAtt01(DateUtils.formatDate(entity.getLotAtt01(), "yyyy-MM-dd"));
                }
                if (null != entity.getLotAtt02()) {
                    response.setLotAtt02(DateUtils.formatDate(entity.getLotAtt02(), "yyyy-MM-dd"));
                }
                if (null != entity.getLotAtt03()) {
                    response.setLotAtt03(DateUtils.formatDate(entity.getLotAtt03(), "yyyy-MM-dd"));
                }
                // 获取批次属性配置
                response.setLotConfigs(rfLotAttrConfigService.getLotAttrConfigs(entity.getLotNum(), entity.getSkuCode(), entity.getOwnerCode(), entity.getOrgId()));
                // 获取包装配置
                response.setPackageConfigs(rfPackageConfigService.getPackageConfigs(entity.getPackCode(), entity.getOrgId()));
                result.add(response);
            }
            msg.setData(result);
        } else {
            msg.setSuccess(false);
            msg.setMessage("查询结果为空");
        }
        return msg;
    }

    public ResultMessage queryPickBoxDetail(WMSRF_INV_QueryPickBoxHeaderOrDetail_Request request) {
        ResultMessage msg = new ResultMessage();
        User user = UserUtils.getUser();
        List<BanQinWmSoAllocEntity> items = wmSoAllocService.rfInvGetPickBoxDetailQuery(request.getSoNo(), request.getToId(), user.getOffice().getId());
        if (CollectionUtil.isNotEmpty(items)) {
            List<WMSRF_INV_PickBoxDetail_Response> responses = Lists.newArrayList();
            for (BanQinWmSoAllocEntity entity : items) {
                WMSRF_INV_PickBoxDetail_Response response = new WMSRF_INV_PickBoxDetail_Response();
                BeanUtils.copyProperties(entity, response);
                responses.add(response);
            }
            msg.setData(responses);
        } else {
            msg.setSuccess(false);
            msg.setMessage("查询结果为空");
        }
        return msg;
    }

    public ResultMessage queryPickBoxHeader(WMSRF_INV_QueryPickBoxHeaderOrDetail_Request request) {
        ResultMessage msg = new ResultMessage();
        User user = UserUtils.getUser();
        List<BanQinWmSoEntity> items = wmSoHeaderService.rfInvGetPickBoxHeaderQuery(request.getToId(), user.getOffice().getId());
        if (CollectionUtil.isNotEmpty(items)) {
            List<WMSRF_INV_PickBoxHeader_Response> responses = Lists.newArrayList();
            for (BanQinWmSoEntity entity : items) {
                WMSRF_INV_PickBoxHeader_Response response = new WMSRF_INV_PickBoxHeader_Response();
                BeanUtils.copyProperties(entity, response);
                responses.add(response);
            }
            msg.setData(responses);
        } else {
            msg.setSuccess(false);
            msg.setMessage("查询结果为空");
        }
        return msg;
    }
}
