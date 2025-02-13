package com.yunyou.modules.wms.rf.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.time.DateUtils;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.modules.sys.entity.User;
import com.yunyou.modules.sys.utils.UserUtils;
import com.yunyou.modules.wms.common.entity.ProcessByCode;
import com.yunyou.modules.wms.common.entity.WarehouseException;
import com.yunyou.modules.wms.common.entity.WmsCodeMaster;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoAllocEntity;
import com.yunyou.modules.wms.outbound.service.*;
import com.yunyou.modules.wms.rf.entity.WMSRF_PK_PickDetail_Response;
import com.yunyou.modules.wms.rf.entity.WMSRF_PK_QueryPickDetail_Request;
import com.yunyou.modules.wms.rf.entity.WMSRF_PK_SavePick_Request;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * RF拣货
 *
 * @author WMJ
 * @version 2019/07/09
 */
@Service
public class RfPickingService {
    @Autowired
    private BanQinOutboundBatchPickingAction outboundBatchPickingAction;
    @Autowired
    private BanQinWmSoAllocService wmSoAllocService;
    @Autowired
    private RfPackageConfigService rfPackageConfigService;
    @Autowired
    private RfLotAttrConfigService rfLotAttrConfigService;
    @Autowired
    private BanQinOutboundBatchCancelAllocAction outboundBatchCancelAllocAction;
    @Autowired
    private BanQinOutboundManualAllocService outboundManualAllocService;
    @Autowired
    private BanQinOutboundSoService outboundSoService;
    @Autowired
    private BanQinWmSoHeaderService wmSoHeaderService;

    public ResultMessage queryPickDetail(WMSRF_PK_QueryPickDetail_Request request) {
        ResultMessage msg = new ResultMessage();
        User user = UserUtils.getUser();
        if (StringUtils.isBlank(request.getSoNo()) && StringUtils.isBlank(request.getAllocId()) && StringUtils.isBlank(request.getPickNo()) && StringUtils.isBlank(request.getWaveNo())) {
            msg.setSuccess(false);
            msg.setMessage("查询参数异常");
            return msg;
        }

        // 2.获取拣货总数目，已完成拣货数
        Long totalNum = 0L; // 总条数
        Long pickNum = 0L; // 已拣货数
        Map<String, Long> maps = wmSoAllocService.rfPKGetPickNumQuery(request.getSoNo(), request.getPickNo(), request.getAllocId(), user.getOffice().getId(), request.getWaveNo());
        if (null != maps) {
            totalNum = maps.get("totalNum");
            pickNum = maps.get("pickNum");
        }
        if (totalNum <= 0 || pickNum.equals(totalNum)) {
            msg.setSuccess(false);
            msg.setMessage("查询结果为空");
            return msg;
        }
        // 3.获取拣货明细
        List<BanQinWmSoAllocEntity> soAllocEntityList = wmSoAllocService.rfPKGetPickDetailQuery(request.getSoNo(), request.getPickNo(), request.getAllocId(), user.getOffice().getId(), request.getWaveNo());
        if (CollectionUtil.isNotEmpty(soAllocEntityList)) {
            List<WMSRF_PK_PickDetail_Response> result = Lists.newArrayList();
            if (2 == request.getCurrentIndex()) {
                soAllocEntityList = this.getAllocDetailByWave(soAllocEntityList);
            }
            for (BanQinWmSoAllocEntity entity : soAllocEntityList) {
                WMSRF_PK_PickDetail_Response response = new WMSRF_PK_PickDetail_Response();
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
                response.setTotalNum(totalNum);
                response.setPickNum(pickNum);
                // 获取批次信息
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

    public ResultMessage savePick(WMSRF_PK_SavePick_Request request) {
        ResultMessage msg = new ResultMessage();
        User user = UserUtils.getUser();
        if (StringUtils.isBlank(request.getAllocId())) {
            msg.setSuccess(false);
            msg.setMessage("请求异常，分配明细Id丢失，请重新扫描");
            return msg;
        }
        BanQinWmSoAllocEntity entity = wmSoAllocService.getEntityByAllocId(request.getAllocId(), user.getOffice().getId());
        if (null == entity) throw new WarehouseException("分配明细[" + request.getAllocId() + "]数据已过期");
        if (!entity.getStatus().equals(WmsCodeMaster.SO_FULL_ALLOC.getCode())) {
            msg.setSuccess(false);
            msg.setMessage("不是完全分配状态");
            return msg;
        }
        ResultMessage message = wmSoHeaderService.checkBatchInterceptStatus(ProcessByCode.BY_SO.getCode(), Lists.newArrayList(entity.getSoNo()), entity.getOrgId());
        if (!message.isSuccess()) {
            return message;
        }
        message = wmSoHeaderService.checkBatchHoldStatus(ProcessByCode.BY_SO.getCode(), Lists.newArrayList(entity.getSoNo()), entity.getOrgId());
        if (!message.isSuccess()) {
            return message;
        }
        entity.setQtyPkEa(request.getQtyPkEa());
        entity.setQtyPkUom(request.getQtyPkUom());
        entity.setToId(request.getToId());
        entity.setTraceId(request.getTraceId());
        entity.setToLoc(request.getToLoc());
        entity.setPickOp(user.getName());
        List<BanQinWmSoAllocEntity> entityList = Lists.newArrayList();
        entityList.add(entity);
        return outboundBatchPickingAction.outboundBatchPicking(entityList);
    }

    public ResultMessage savePickByTraceId(WMSRF_PK_SavePick_Request request) {
        ResultMessage msg = new ResultMessage();
        User user = UserUtils.getUser();
        // 1.根据标签查询出分配明细
        BanQinWmSoAllocEntity condition = new BanQinWmSoAllocEntity();
        condition.setToId(request.getToId());
        condition.setStatus(WmsCodeMaster.SO_FULL_ALLOC.getCode());
        condition.setOrgId(user.getOffice().getId());
        List<BanQinWmSoAllocEntity> items = wmSoAllocService.findByEntity(condition);
        if (CollectionUtil.isNotEmpty(items)) {
            List<String> collect = items.stream().map(BanQinWmSoAllocEntity::getSoNo).distinct().collect(Collectors.toList());
            BanQinWmSoAllocEntity entity = items.get(0);
            ResultMessage message = wmSoHeaderService.checkBatchInterceptStatus(ProcessByCode.BY_SO.getCode(), collect, entity.getOrgId());
            if (!message.isSuccess()) {
                return message;
            }
            message = wmSoHeaderService.checkBatchHoldStatus(ProcessByCode.BY_SO.getCode(), Lists.newArrayList(entity.getSoNo()), entity.getOrgId());
            if (!message.isSuccess()) {
                return message;
            }
            message = wmSoHeaderService.checkBatchHoldStatus(ProcessByCode.BY_SO.getCode(), Lists.newArrayList(entity.getSoNo()), entity.getOrgId());
            if (!message.isSuccess()) {
                return message;
            }
            msg = outboundBatchPickingAction.outboundBatchPicking(items);
        } else {
            msg.setSuccess(false);
            msg.setMessage(request.getToId() + "不存在");
        }
        return msg;
    }

    /**
     * 按照波次合拣维度合并分配明细
     */
    private List<BanQinWmSoAllocEntity> getAllocDetailByWave(List<BanQinWmSoAllocEntity> list) {
        List<BanQinWmSoAllocEntity> result = Lists.newArrayList();
        Map<String, List<BanQinWmSoAllocEntity>> collect = list.stream().collect(Collectors.groupingBy(s -> s.getLocCode() + s.getTraceId() + s.getSkuCode() + s.getLotNum()));
        collect.values().forEach(v -> {
            BanQinWmSoAllocEntity entity = v.get(0);
            entity.setAllocId(v.stream().map(BanQinWmSoAllocEntity::getAllocId).collect(Collectors.joining(",")));
            entity.setQtyEa(v.stream().mapToDouble(BanQinWmSoAllocEntity::getQtyEa).sum());
            result.add(entity);
        });

        return result;
    }

    public ResultMessage savePickByWave(WMSRF_PK_SavePick_Request request) {
        ResultMessage msg;
        User user = UserUtils.getUser();
        String[] allocIds = request.getAllocId().split(",");
        List<BanQinWmSoAllocEntity> entityList = Lists.newArrayList();
        boolean flag = true;
        for (String allocId : allocIds) {
            if (StringUtils.isBlank(request.getAllocId())) {
                flag = false;
                continue;
            }
            BanQinWmSoAllocEntity entity = wmSoAllocService.getEntityByAllocId(allocId, user.getOffice().getId());
            if (null == entity) throw new WarehouseException("查询不到分配明细[" + allocId + "]");
            if (!entity.getStatus().equals(WmsCodeMaster.SO_FULL_ALLOC.getCode())) {
                flag = false;
                continue;
            }
            entityList.add(entity);
        }
        if (!flag) {
            throw new WarehouseException("数据过期，存在不是完全分配的记录");
        }
        BanQinWmSoAllocEntity allocEntity = entityList.get(0);
        ResultMessage message = wmSoHeaderService.checkBatchInterceptStatus(ProcessByCode.BY_SO.getCode(), Lists.newArrayList(allocEntity.getSoNo()), allocEntity.getOrgId());
        if (!message.isSuccess()) {
            return message;
        }
        message = wmSoHeaderService.checkBatchHoldStatus(ProcessByCode.BY_SO.getCode(), Lists.newArrayList(allocEntity.getSoNo()), allocEntity.getOrgId());
        if (!message.isSuccess()) {
            return message;
        }
        // 取消分配
        msg = outboundBatchCancelAllocAction.outboundBatchCancelAlloc(entityList);
        // 手工分配
        if (msg.isSuccess()) {
            double totalQty = request.getQtyPkEa();
            for (BanQinWmSoAllocEntity entity : entityList) {
                if (totalQty == 0d) break;

                entity.setId(null);
                entity.setLocCode(request.getToLoc());
                if (totalQty < entity.getQtyEa()) {
                    entity.setQtyEa(totalQty);
                    entity.setQtyUom(totalQty / entity.getUomQty());
                    totalQty = 0d;
                } else {
                    totalQty -= entity.getQtyEa();
                }
                outboundManualAllocService.outboundManualAlloc(entity);
            }
        }
        return msg;
    }

    public ResultMessage savePickByManual(WMSRF_PK_SavePick_Request request) {
        ResultMessage msg = new ResultMessage();
        User user = UserUtils.getUser();
        if (StringUtils.isBlank(request.getAllocId())) {
            msg.setSuccess(false);
            msg.setMessage("请求异常，分配明细Id丢失，请重新扫描");
            return msg;
        }
        BanQinWmSoAllocEntity entity = wmSoAllocService.getEntityByAllocId(request.getAllocId(), user.getOffice().getId());
        if (!entity.getStatus().equals(WmsCodeMaster.SO_FULL_ALLOC.getCode())) {
            msg.setSuccess(false);
            msg.setMessage("不是完全分配状态");
            return msg;
        }
        ResultMessage message = wmSoHeaderService.checkBatchInterceptStatus(ProcessByCode.BY_SO.getCode(), Lists.newArrayList(entity.getSoNo()), entity.getOrgId());
        if (!message.isSuccess()) {
            return message;
        }
        message = wmSoHeaderService.checkBatchHoldStatus(ProcessByCode.BY_SO.getCode(), Lists.newArrayList(entity.getSoNo()), entity.getOrgId());
        if (!message.isSuccess()) {
            return message;
        }
        BeanUtils.copyProperties(request, entity);
        entity.setPickLoc(request.getToLoc());
        entity.setPickLotNum(entity.getLotNum());
        entity.setPickTraceId(request.getTraceId());
        entity.setPickToLoc(entity.getToLoc());
        entity.setPickToId(entity.getToId());
        return outboundSoService.pickingByManual(entity);
    }

}
