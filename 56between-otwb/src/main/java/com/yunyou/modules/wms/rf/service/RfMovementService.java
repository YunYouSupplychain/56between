package com.yunyou.modules.wms.rf.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.common.utils.time.DateFormatUtil;
import com.yunyou.modules.sys.entity.User;
import com.yunyou.modules.sys.utils.UserUtils;
import com.yunyou.modules.wms.inventory.entity.BanQinWmInvLotLoc;
import com.yunyou.modules.wms.inventory.entity.BanQinWmInvLotLocEntity;
import com.yunyou.modules.wms.inventory.entity.BanQinWmInvMvEntity;
import com.yunyou.modules.wms.inventory.service.BanQinWmInvLotLocService;
import com.yunyou.modules.wms.inventory.service.BanQinWmInvMvService;
import com.yunyou.modules.wms.rf.entity.WMSRF_INV_SkuInvDetail_Response;
import com.yunyou.modules.wms.rf.entity.WMSRF_MV_QueryMovement_Request;
import com.yunyou.modules.wms.rf.entity.WMSRF_MV_SaveMovement_Request;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * RF移动
 * @author WMJ
 * @version 2019/07/09
 */
@Service
public class RfMovementService {
    @Autowired
    private BanQinWmInvMvService wmInvMvService;
    @Autowired
    private BanQinWmInvLotLocService wmInvLotLocService;
    @Autowired
    private RfLotAttrConfigService rfLotAttrConfigService;

    @Transactional
    public ResultMessage saveMovement(List<WMSRF_MV_SaveMovement_Request> requests) {
        User user = UserUtils.getUser();
        ResultMessage msg = new ResultMessage();
        StringBuilder returnMsg = new StringBuilder();
        for (WMSRF_MV_SaveMovement_Request request : requests) {
            // 1、先查到源库存记录
            BanQinWmInvMvEntity wmInvMvEntity = new BanQinWmInvMvEntity();
            BanQinWmInvLotLoc wmInvLotLocModel = wmInvLotLocService.getByLotNumAndLocationAndTraceId(request.getLotNum(), request.getFmLoc(), request.getFmTraceId(), user.getOffice().getId());
            if (null == wmInvLotLocModel) {
                msg.setSuccess(false);
                returnMsg.append(wmInvMvEntity.getFmTraceId()).append(":库存信息不存在").append("\n");
                continue;
            }
            // 2、拷贝：从model 到 entity
            BeanUtils.copyProperties(wmInvLotLocModel, wmInvMvEntity);
            // 3、设置：将RF界面传入的值设置到entity对象
            wmInvMvEntity.setFmLoc(request.getFmLoc());
            wmInvMvEntity.setFmTraceId(request.getFmTraceId());
            wmInvMvEntity.setPackCode(request.getPackCode());
            wmInvMvEntity.setToLoc(request.getToLoc());
            wmInvMvEntity.setToTraceId(request.getToTraceId());
            wmInvMvEntity.setToQty(request.getToQty());
            wmInvMvEntity.setToUom(request.getToUom());
            wmInvMvEntity.setToQtyUom(request.getToQty() / request.getCdprQuantity());
            wmInvMvEntity.setIsRf("Y");
            // 4、执行移动：调用后台接口
            ResultMessage message = wmInvMvService.invMovement(wmInvMvEntity);
            if (message.isSuccess()) {
                returnMsg.append(wmInvMvEntity.getFmTraceId()).append(":").append("操作成功").append("\n");
            } else {
                msg.setSuccess(false);
                returnMsg.append(wmInvMvEntity.getFmTraceId()).append(":").append(msg.getMessage()).append("\n");
            }
        }
        msg.setMessage(returnMsg.toString());
        return msg;
    }

    @Transactional
    public ResultMessage queryMovement(WMSRF_MV_QueryMovement_Request request) {
        ResultMessage msg = new ResultMessage();
        User user = UserUtils.getUser();
        List<BanQinWmInvLotLocEntity> items = wmInvLotLocService.rfMVGetMovementDetailQuery(request.getLocationCode(), request.getTraceId(), request.getLotNum(), request.getSkuCode(), user.getOffice().getId());
        if (CollectionUtil.isNotEmpty(items)) {
            List<WMSRF_INV_SkuInvDetail_Response> result = Lists.newArrayList();
            for (BanQinWmInvLotLocEntity entity : items) {
                WMSRF_INV_SkuInvDetail_Response response = new WMSRF_INV_SkuInvDetail_Response();
                BeanUtils.copyProperties(entity, response);
                if (entity.getLotAtt01() != null) {
                    response.setLotAtt01(DateFormatUtil.formatDate("yyyy-MM-dd", entity.getLotAtt01()));
                }
                if (entity.getLotAtt02() != null) {
                    response.setLotAtt02(DateFormatUtil.formatDate("yyyy-MM-dd", entity.getLotAtt02()));
                }
                if (entity.getLotAtt03() != null) {
                    response.setLotAtt03(DateFormatUtil.formatDate("yyyy-MM-dd", entity.getLotAtt03()));
                }
                response.setPrintUom(entity.getPrintUom());
                response.setQtyUom(entity.getUomQty());
                // 获取批次属性配置
                response.setLotConfigs(rfLotAttrConfigService.getLotAttrConfigs(entity.getLotNum(), entity.getSkuCode(), entity.getOwnerCode(), user.getOffice().getId()));
                // 获取包装配置
                result.add(response);
            }
            msg.setData(result);
        } else {
            msg.setSuccess(false);
            msg.setMessage("查询结果为空");
        }
        return msg;
    }
}
