package com.yunyou.modules.wms.rf.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.time.DateUtils;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.modules.sys.entity.User;
import com.yunyou.modules.sys.utils.UserUtils;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhSku;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhSkuBarcode;
import com.yunyou.modules.wms.basicdata.service.BanQinCdWhSkuBarcodeService;
import com.yunyou.modules.wms.basicdata.service.BanQinCdWhSkuService;
import com.yunyou.modules.wms.common.entity.ActionCode;
import com.yunyou.modules.wms.common.entity.WmsCodeMaster;
import com.yunyou.modules.wms.common.entity.WmsConstants;
import com.yunyou.modules.wms.inbound.entity.BanQinWmAsnDetailReceiveEntity;
import com.yunyou.modules.wms.inbound.entity.BanQinWmAsnHeader;
import com.yunyou.modules.wms.inbound.service.BanQinReceivingOperationService;
import com.yunyou.modules.wms.inbound.service.BanQinWmAsnDetailReceiveService;
import com.yunyou.modules.wms.inbound.service.BanQinWmAsnHeaderService;
import com.yunyou.modules.wms.rf.entity.WMSRF_RC_CheckAsnIsPalletize_Request;
import com.yunyou.modules.wms.rf.entity.WMSRF_RC_QueryAsnDetailByTraceIDOrSku_Request;
import com.yunyou.modules.wms.rf.entity.WMSRF_RC_QueryAsnDetailByTraceIDOrSku_Response;
import com.yunyou.modules.wms.rf.entity.WMSRF_RC_SaveAsnDetailByTraceID_Request;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * RF收货service
 *
 * @author WMJ
 * @version 2019/07/08
 */
@Service
public class RfReceiveService {
    @Autowired
    private BanQinWmAsnHeaderService wmAsnHeaderService;
    @Autowired
    private BanQinWmAsnDetailReceiveService wmAsnDetailReceiveService;
    @Autowired
    private BanQinCdWhSkuService cdWhSkuService;
    @Autowired
    private BanQinReceivingOperationService receivingOperationService;
    @Autowired
    private RfLotAttrConfigService rfLotAttrConfigService;
    @Autowired
    private RfPackageConfigService rfPackageConfigService;
    @Autowired
    private BanQinCdWhSkuBarcodeService banQinCdWhSkuBarcodeService;

    public ResultMessage checkAsnIsPalletize(WMSRF_RC_CheckAsnIsPalletize_Request request) {
        ResultMessage msg = new ResultMessage();
        User user = UserUtils.getUser();
        List<Map<String, Long>> list = wmAsnHeaderService.rfRcCheckAsnIsPalletizeQuery(request.getAsnNo(), user.getOffice().getId());
        if (CollectionUtil.isNotEmpty(list)) {
            Map<String, Long> map = list.get(0);
            // 扫描托盘收货
            if (1 == request.getFunType()) {
                if (map.get("total") <= 0 || map.get("isPal") == 0 || map.get("neverPal").longValue() == map.get("total").longValue()) {
                    msg.setSuccess(false);
                    msg.setMessage("查询结果为空");
                }
            } else {// 扫描商品收货
                if (map.get("total") <= 0 || map.get("isPal").intValue() == map.get("total").intValue()) {
                    msg.setSuccess(false);
                    msg.setMessage("查询结果为空");
                }
            }
        } else {
            msg.setSuccess(false);
            msg.setMessage("查询结果为空");
        }

        return msg;
    }

    public ResultMessage queryAsnDetailByTraceIDOrSku(WMSRF_RC_QueryAsnDetailByTraceIDOrSku_Request request) {
        ResultMessage msg = new ResultMessage();
        User user = UserUtils.getUser();
        if (StringUtils.isNotEmpty(request.getSkuCode())) {
            BanQinWmAsnHeader asnHeader = wmAsnHeaderService.getByAsnNo(request.getAsnNo(), user.getOffice().getId());
            if (null == asnHeader) {
                msg.setSuccess(false);
                msg.setMessage("查询结果为空");
                return msg;
            }
            List<BanQinCdWhSkuBarcode> byBarcode = banQinCdWhSkuBarcodeService.findByBarcode(asnHeader.getOwnerCode(), null, request.getSkuCode(), user.getOffice().getId());
            if (CollectionUtil.isNotEmpty(byBarcode)) {
                request.setSkuCode(byBarcode.get(0).getSkuCode());
            }
        }

        List<BanQinWmAsnDetailReceiveEntity> list = wmAsnHeaderService.rfRcGetAsnDetailByTraceIDOrSkuQuery(request.getAsnNo(), request.getPlanId(), request.getSkuCode(), request.getFunType(), user.getOffice().getId());
        if (CollectionUtil.isNotEmpty(list)) {
            List<WMSRF_RC_QueryAsnDetailByTraceIDOrSku_Response> result = Lists.newArrayList();
            for (BanQinWmAsnDetailReceiveEntity entity : list) {
                WMSRF_RC_QueryAsnDetailByTraceIDOrSku_Response response = new WMSRF_RC_QueryAsnDetailByTraceIDOrSku_Response();
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

    @Transactional
    public ResultMessage saveAsnDetailByTraceId(WMSRF_RC_SaveAsnDetailByTraceID_Request request) {
        ResultMessage msg = new ResultMessage();
        User user = UserUtils.getUser();
        String orgId = user.getOffice().getId();
        BanQinCdWhSku skuModel = cdWhSkuService.getByOwnerAndSkuCode(request.getOwnerCode(), request.getSkuCode(), orgId);
        // 获取原明细行数据
        BanQinWmAsnDetailReceiveEntity wmAsnDetailReceiveEntity = wmAsnDetailReceiveService.getEntityByAsnNoAndLineNo(request.getAsnNo(), request.getLineNo(), orgId);
        if (null == wmAsnDetailReceiveEntity) {
            msg.setSuccess(false);
            msg.setMessage("入库单号[" + request.getAsnNo() + "]行号[" + request.getLineNo() + "]不存在");
            return msg;
        }
        if (!wmAsnDetailReceiveEntity.getStatus().equals(WmsCodeMaster.ASN_NEW.getCode())) {
            msg.setSuccess(false);
            msg.setMessage("订单行不是新建状态");
            return msg;
        }
        // 收货明细
        wmAsnDetailReceiveEntity.setActionCode(WmsConstants.YES.equals(skuModel.getIsSerial()) ? ActionCode.SCAN_RECEIVING : ActionCode.RECEIVING);
        wmAsnDetailReceiveEntity.setUom(request.getUom());
        wmAsnDetailReceiveEntity.setToId(request.getToId());
        wmAsnDetailReceiveEntity.setToLoc(request.getToLoc());
        wmAsnDetailReceiveEntity.setPlanId(request.getPlanId());
        wmAsnDetailReceiveEntity.setCurrentQtyRcvEa(request.getCurrentQtyRcvEa());
        wmAsnDetailReceiveEntity.setLotAtt01(DateUtils.parseDate(request.getLotAtt01()));
        wmAsnDetailReceiveEntity.setLotAtt02(DateUtils.parseDate(request.getLotAtt02()));
        wmAsnDetailReceiveEntity.setLotAtt03(DateUtils.parseDate(request.getLotAtt03()));
        wmAsnDetailReceiveEntity.setLotAtt04(request.getLotAtt04());
        wmAsnDetailReceiveEntity.setLotAtt05(request.getLotAtt05());
        wmAsnDetailReceiveEntity.setLotAtt06(request.getLotAtt06());
        wmAsnDetailReceiveEntity.setLotAtt07(request.getLotAtt07());
        wmAsnDetailReceiveEntity.setLotAtt08(request.getLotAtt08());
        wmAsnDetailReceiveEntity.setLotAtt09(request.getLotAtt09());
        wmAsnDetailReceiveEntity.setLotAtt10(request.getLotAtt10());
        wmAsnDetailReceiveEntity.setLotAtt11(request.getLotAtt11());
        wmAsnDetailReceiveEntity.setLotAtt12(request.getLotAtt12());

        ResultMessage resultMessage = receivingOperationService.inboundReceiving(wmAsnDetailReceiveEntity);
        if (!resultMessage.isSuccess()) {
            msg.setSuccess(false);
            msg.setMessage(wmAsnDetailReceiveEntity.getLineNo() + resultMessage.getMessage());
        }
        return msg;
    }

}
