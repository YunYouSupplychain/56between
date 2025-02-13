package com.yunyou.modules.wms.rf.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.enums.CustomerType;
import com.yunyou.common.utils.time.DateUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.modules.sys.entity.User;
import com.yunyou.modules.sys.utils.UserUtils;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhSkuBarcode;
import com.yunyou.modules.wms.basicdata.service.BanQinCdWhSkuBarcodeService;
import com.yunyou.modules.wms.common.entity.WmsCodeMaster;
import com.yunyou.modules.wms.customer.entity.BanQinEbCustomer;
import com.yunyou.modules.wms.customer.service.BanQinEbCustomerService;
import com.yunyou.modules.wms.inventory.entity.BanQinWmInvLotLoc;
import com.yunyou.modules.wms.inventory.entity.BanQinWmTaskCountEntity;
import com.yunyou.modules.wms.inventory.service.BanQinWmCountHeaderService;
import com.yunyou.modules.wms.inventory.service.BanQinWmInvLotAttService;
import com.yunyou.modules.wms.inventory.service.BanQinWmInvLotLocService;
import com.yunyou.modules.wms.inventory.service.BanQinWmTaskCountService;
import com.yunyou.modules.wms.rf.entity.*;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * RF盘点
 * @author WMJ
 * @version 2019/07/11
 */
@Service
public class RfTaskCountService {
    @Autowired
    private RfPackageConfigService rfPackageConfigService;
    @Autowired
    private RfLotAttrConfigService rfLotAttrConfigService;
    @Autowired
    private BanQinWmTaskCountService wmTaskCountService;
    @Autowired
    private BanQinWmCountHeaderService wmCountHeaderService;
    @Autowired
    private BanQinWmInvLotLocService wmInvLotLocService;
    @Autowired
    private BanQinEbCustomerService ebCustomerService;
    @Autowired
    private BanQinWmInvLotAttService wmInvLotAttService;
    @Autowired
    private BanQinCdWhSkuBarcodeService cdWhSkuBarcodeService;

    public ResultMessage queryTaskCountDetail(WMSRF_TC_QueryTaskCountDetail_Request request) {
        ResultMessage msg = new ResultMessage();
        User user = UserUtils.getUser();
        List<BanQinWmTaskCountEntity> items = wmTaskCountService.rfTCGetTaskCountDetailQuery(request.getZoneCode(), request.getLane(), request.getLocCode(), request.getCountNo(), user.getOffice().getId());
        if (CollectionUtil.isNotEmpty(items)) {
            List<WMSRF_TC_TaskContDetail_Response> result = Lists.newArrayList();
            for (BanQinWmTaskCountEntity entity : items) {
                WMSRF_TC_TaskContDetail_Response response = new WMSRF_TC_TaskContDetail_Response();
                BeanUtils.copyProperties(entity, response);
                if (WmsCodeMaster.COUNT_METHOD_D.getCode().equals(entity.getCountMethod())) {
                    // 如果是动态盘点，需要抓取当前最新库存数
                    BanQinWmInvLotLoc wmInvLotLoc = wmInvLotLocService.getByLotNumAndLocationAndTraceId(entity.getLotNum(), entity.getLocCode(), entity.getTraceId(), entity.getOrgId());
                    if (null != wmInvLotLoc) {
                        response.setQty(wmInvLotLoc.getQty());
                    }
                }
                if (null != entity.getLotAtt01()) {
                    response.setLotAtt01(DateUtils.formatDate(entity.getLotAtt01(), "yyyy-MM-dd"));
                }
                if (null != entity.getLotAtt02()) {
                    response.setLotAtt03(DateUtils.formatDate(entity.getLotAtt02(), "yyyy-MM-dd"));
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
    public ResultMessage saveTaskCount(WMSRF_TC_SaveTaskCount_Request request) {
        ResultMessage msg = new ResultMessage();
        User user = UserUtils.getUser();
        BanQinWmTaskCountEntity taskEntity = wmTaskCountService.getEntityByCountId(request.getCountId(), user.getOffice().getId());
        if (null == taskEntity) {
            msg.setSuccess(false);
            msg.setMessage("查询结果为空");
            return msg;
        }
        if (!taskEntity.getStatus().equals(WmsCodeMaster.TSK_NEW.getCode())) {
            msg.setSuccess(false);
            msg.setMessage("状态不是创建状态");
            return msg;
        }
        if (WmsCodeMaster.COUNT_METHOD_D.getCode().equals(taskEntity.getCountMethod())) {
            // 如果是动态盘点，需要抓取当前最新库存数并更新
            BanQinWmInvLotLoc wmInvLotLocModel = wmInvLotLocService.getByLotNumAndLocationAndTraceId(taskEntity.getLotNum(), taskEntity.getLocCode(), taskEntity.getTraceId(), taskEntity.getOrgId());
            if (null != wmInvLotLocModel) {
                taskEntity.setQty(wmInvLotLocModel.getQty());
            }
        }
        if ("N".equals(request.getIsSerial())) {
            taskEntity.setQtyCountEa(request.getQtyCountEa());
        }
        taskEntity.setQtyDiff(request.getQtyCountEa() - taskEntity.getQty());
        taskEntity.setCountOp(user.getLoginName());
        return wmCountHeaderService.confirmCount(taskEntity);
    }

    public ResultMessage checkTaskCount(WMSRF_TC_CheckTaskCount_Request request) {
        ResultMessage msg = new ResultMessage();
        User user = UserUtils.getUser();
        List<BanQinWmTaskCountEntity> items = wmTaskCountService.rfTCGetTaskCountDetailQuery(null, null, null, request.getCountNo(), user.getOffice().getId());
        if (CollectionUtil.isEmpty(items)) {
            msg.setSuccess(false);
            msg.setMessage("查询结果为空");
        }
        return msg;
    }

    @Transactional
    public ResultMessage addNewTaskCount(WMSRF_TC_AddNewTaskCount_Request request) {
        List<BanQinCdWhSkuBarcode> barcode = cdWhSkuBarcodeService.findByBarcode(request.getOwnerCode(), null, request.getSkuCode(), request.getOrgId());
        if (CollectionUtil.isEmpty(barcode)) {
            request.setSkuCode(barcode.get(0).getSkuCode());
        } else {
            throw new RuntimeException("条码信息不匹配或不存在");
        }

        BanQinWmTaskCountEntity wmTaskCountEntity = new BanQinWmTaskCountEntity();
        BeanUtils.copyProperties(request, wmTaskCountEntity);
        wmTaskCountEntity.setRecVer(0);
        wmTaskCountEntity.setLotAtt01(DateUtils.parseDate(request.getLotAtt01()));
        wmTaskCountEntity.setLotAtt02(DateUtils.parseDate(request.getLotAtt02()));
        wmTaskCountEntity.setLotAtt03(DateUtils.parseDate(request.getLotAtt03()));
        // 默认的的盘点数等于库存数
        wmTaskCountEntity.setQty(0D);
        wmTaskCountEntity.setQtyCountEa(request.getQty());
        wmTaskCountEntity.setQtyCountUom(0D);
        wmTaskCountEntity.setQtyDiff(request.getQty());
        wmTaskCountEntity.setStatus(WmsCodeMaster.TSK_COMPLETE.getCode());

        return wmCountHeaderService.addConfirmCount(wmTaskCountEntity);
    }

    public ResultMessage queryOwner(WMSRF_TC_QueryOwner_Request request) {
        ResultMessage msg = new ResultMessage();
        User user = UserUtils.getUser();
        BanQinEbCustomer ebCustomer = new BanQinEbCustomer();
        ebCustomer.setEbcuCustomerNo(request.getOwnerCode());
        ebCustomer.setOrgId(user.getOffice().getId());
        ebCustomer.setEbcuType(CustomerType.OWNER.getCode());
        List<BanQinEbCustomer> list = ebCustomerService.findList(ebCustomer);
        if (CollectionUtil.isNotEmpty(list)) {
            List<WMSRF_TC_OwnerDetail_Response> responses = Lists.newArrayList();
            for (BanQinEbCustomer customer : list) {
                WMSRF_TC_OwnerDetail_Response response = new WMSRF_TC_OwnerDetail_Response();
                response.setOwnerCode(customer.getEbcuCustomerNo());
                response.setOwnerName(customer.getEbcuNameCn());
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
