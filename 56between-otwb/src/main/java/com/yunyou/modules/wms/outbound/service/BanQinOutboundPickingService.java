package com.yunyou.modules.wms.outbound.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.IdGen;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.modules.sys.GenNoType;
import com.yunyou.modules.sys.service.SynchronizedNoService;
import com.yunyou.modules.sys.utils.UserUtils;
import com.yunyou.modules.wms.common.entity.*;
import com.yunyou.modules.wms.common.service.BanQinWmsCommonService;
import com.yunyou.modules.wms.inventory.service.BanQinInventoryService;
import com.yunyou.modules.wms.outbound.entity.BanQinOutboundPickingEntity;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoAlloc;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoAllocEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 拣货确认
 *
 * @author WMJ
 * @version 2019/02/20
 */
@Service
@Transactional(readOnly = true)
public class BanQinOutboundPickingService {
    // 公共方法
    @Autowired
    protected BanQinWmsCommonService wmCommon;
    // 出库公共
    @Autowired
    protected BanQinOutboundCommonService outboundCommon;
    // 分配明细
    @Autowired
    protected BanQinWmSoAllocService wmSoAllocService;
    // 编码生成器
    @Autowired
    protected SynchronizedNoService noService;
    // 库存更新
    @Autowired
    protected BanQinInventoryService inventoryService;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 拣货
     *
     * @param wmSoAllocEntity
     * @return
     * @throws WarehouseException
     */
    @Transactional
    public ResultMessage outboundPicking(BanQinWmSoAllocEntity wmSoAllocEntity) throws WarehouseException {
        ResultMessage msg = new ResultMessage();
        String userName = StringUtils.isEmpty(wmSoAllocEntity.getPickOp()) ? UserUtils.getUser().getName() : wmSoAllocEntity.getPickOp();
        // 1、如果目标跟踪号不为"*"，或者不为空，那么校验箱号混收货人、混货主
        // @@@@目标跟踪号值获取----前台/原跟踪号字段?
        if (wmSoAllocEntity.getToId() != null && (!wmSoAllocEntity.getToId().equals(WmsConstants.TRACE_ID))) {
            // 1、校验箱号(TO_ID)不允许混收货人、不允许混货主
            // 不允许混货主
            List<Map<String, Object>> toIdMixOwnerMap = jdbcTemplate.queryForList("select 1 AS num from WM_SO_ALLOC a where a.owner_code != '" + wmSoAllocEntity.getOwnerCode()
                    + "' and a.to_id='" + wmSoAllocEntity.getToId() + "' and a.org_id ='" + wmSoAllocEntity.getOrgId() + "' limit 1");
            long count = toIdMixOwnerMap.size() == 0 ? 0 : Long.parseLong(toIdMixOwnerMap.get(0).get("num").toString());
            if (count > 0) {
                // 箱号{0}不允许混货主
                throw new WarehouseException("箱号" + wmSoAllocEntity.getToId() + "不允许混货主");
            }
            // 不允许混收货人
//            List<Map<String, Object>> toIdMixConsigneeMap = jdbcTemplate.queryForList("select 1 AS num from WM_SO_ALLOC a where a.consignee_code != '" + wmSoAllocEntity.getOwnerCode()
//                    + "' and a.to_id='" + wmSoAllocEntity.getToId() + "' and a.org_id ='" + wmSoAllocEntity.getOrgId() + "' limit 1");
//            count = toIdMixConsigneeMap.size() == 0 ? 0 : Long.valueOf(toIdMixConsigneeMap.get(0).get("num").toString());
//            if (count > 0) {
//                // 箱号{0}不允许混收货人
//                throw new WarehouseException("箱号" + wmSoAllocEntity.getToId() + "不允许混收货人");
//            }
        }
        // 2、校验拣货位空值，为空，提示信息
        if (StringUtils.isEmpty(wmSoAllocEntity.getToLoc())) {
            // 拣货库位不能为空
            throw new WarehouseException("拣货库位不能为空");
        }
        if (wmSoAllocEntity.getQtyEa() <= 0) {
            throw new WarehouseException(wmSoAllocEntity.getAllocId() + "拣货数必须大于0");
        }
        // 分配明细获取
        BanQinWmSoAlloc wmSoAllocModel = wmSoAllocService.getByAllocId(wmSoAllocEntity.getAllocId(), wmSoAllocEntity.getOrgId());
        // 状态不符合，数据过期
        if (!wmSoAllocModel.getStatus().equals(WmsCodeMaster.ALLOC_FULL_ALLOC.getCode())) {
            // 分配明细不是完全分配状态
            throw new WarehouseException(wmSoAllocEntity.getAllocId() + "分配明细不是完全分配状态");
        }
        // 分配数量版本号不一致，数据过期
        if (wmSoAllocModel.getRecVer() != wmSoAllocEntity.getRecVer()) {
            throw new WarehouseException(wmSoAllocEntity.getAllocId() + "分配数量版本号不一致，数据过期");
        }
        // 部分拣货、完全拣货标记，true : 完全拣货，false:部分拣货
        boolean diffQtyFlag = true;
        if (wmSoAllocEntity.getQtyPkEa() != null && wmSoAllocEntity.getQtyPkEa() != 0 && (!wmSoAllocEntity.getQtyPkEa().equals(wmSoAllocModel.getQtyEa()))) {
            diffQtyFlag = false;
        } else {
            wmSoAllocEntity.setQtyPkEa(wmSoAllocModel.getQtyEa());// 完全拣货，拣货数量=分配数量
        }
        /* 4拣货任务单位修改，是否需要进行单位换算 */
        // 是否换单位，true : 单位不变，false:单位改变
        boolean diffUomFlag = true;
        if (!wmSoAllocEntity.getUom().equals(wmSoAllocModel.getUom())) {
            // 源单位与新单位比较，不相等，换单位拣货
            diffUomFlag = false;
        }
        // 4、获取包装单位，确定复核状态
        // 复核状态初始值获取修改
        // 复核打包参数：是否一定要复核才可以发运（Y：需要复核；N：不用复核）
        String shipNeedCheck = "N";
        BanQinCdWhPackageRelationEntity packageEntity = wmCommon.getPackageRelationAndQtyUom(wmSoAllocEntity.getPackCode(), wmSoAllocEntity.getUom(), wmSoAllocEntity.getQtyPkEa(), wmSoAllocEntity.getOrgId());
        // 包装装箱标记--确定复核状态
        String isPackBox = packageEntity.getCdprIsPackBox();
        String checkStatus = null;
        // 如果Y,那么复核状态全部为【未复核】，不考虑包装维护的装箱标记
        if (shipNeedCheck.equals(WmsConstants.YES)) {
            checkStatus = WmsCodeMaster.CHECK_NEW.getCode();
        } else {
            // 如果N,那么复核状态根据包装维护的是否装箱标记，装箱标记为Y,状态为【未复核】，装箱标记为N,状态为【不复核】
            // 如果装箱标记为Y,那么复核状态为"00"，需要复核，并且需拣货并且复核后才可发货
            // 如果装箱标记为N,那么复核状态为"90"，不需要复核，拣货后直接发货
            if (WmsConstants.YES.equals(isPackBox)) {
                checkStatus = WmsCodeMaster.CHECK_NEW.getCode();
            } else {
                checkStatus = WmsCodeMaster.CHECK_NOT.getCode();
            }
        }
        // 5、更新分配明细
        // 5.1完全拣货
        if (diffQtyFlag) {
            // 如果换单位，那么换算数量
            if (!diffUomFlag) {
                wmSoAllocModel.setUom(wmSoAllocEntity.getUom());// 新拣货单位
                wmSoAllocModel.setQtyUom(packageEntity.getQtyUom());// 新拣货单位数量
            }
            wmSoAllocModel.setStatus(WmsCodeMaster.ALLOC_FULL_PICKING.getCode());// 完全拣货
            wmSoAllocModel.setToLoc(wmSoAllocEntity.getToLoc());// 目标拣货位
            wmSoAllocModel.setToId(wmSoAllocEntity.getToId());// 目标跟踪号
            wmSoAllocModel.setCheckStatus(checkStatus);// 复核状态
            wmSoAllocModel.setPickOp(userName);// 拣货人
            wmSoAllocModel.setPickTime(new Date());// 拣货时间，时区时间
             this.wmSoAllocService.save(wmSoAllocModel);
        } else {
            // 5.2部分拣货
            // 剩余拣货数量
            Double newQtyPkEa = 0D;
            Double newQtyPkUom = 0D;
            String newUom = null;// 新单位
            // 如果换单位，那么换算数量
            if (!diffUomFlag) {
                BanQinCdWhPackageRelationEntity oldPackageEntity = wmCommon.getPackageRelationAndQtyUom(wmSoAllocModel.getPackCode(), wmSoAllocModel.getUom(), wmSoAllocModel.getOrgId());
                // 如果源单位>新单位，那么向下拆分单位
                if (oldPackageEntity.getCdprQuantity() > packageEntity.getCdprQuantity()) {
                    newQtyPkEa = wmSoAllocModel.getQtyEa() - wmSoAllocEntity.getQtyPkEa();
                    newQtyPkUom = wmCommon.doubleDivide(newQtyPkEa, packageEntity.getCdprQuantity().doubleValue());
                    newUom = packageEntity.getCdprUnitLevel();
                } else {
                    // 如果源单位<新单位，那么新分配记录，不拆分单位
                    newQtyPkEa = wmSoAllocModel.getQtyEa() - wmSoAllocEntity.getQtyPkEa();
                    newQtyPkUom = wmCommon.doubleDivide(newQtyPkEa, oldPackageEntity.getCdprQuantity().doubleValue());
                    newUom = oldPackageEntity.getCdprUnitLevel();
                }

                wmSoAllocModel.setUom(wmSoAllocEntity.getUom());// 新换算单位
            } else {
                // 不换单位
                newQtyPkEa = wmSoAllocModel.getQtyEa() - wmSoAllocEntity.getQtyPkEa();
                newQtyPkUom = wmCommon.doubleDivide(newQtyPkEa, packageEntity.getCdprQuantity().doubleValue());
                newUom = packageEntity.getCdprUnitLevel();
            }

            // 源分配记录
            wmSoAllocModel.setQtyEa(wmSoAllocEntity.getQtyPkEa());// 拣货数量EA
            wmSoAllocModel.setQtyUom(packageEntity.getQtyUom());// 拣货单位数量
            wmSoAllocModel.setStatus(WmsCodeMaster.ALLOC_FULL_PICKING.getCode());// 完全拣货
            wmSoAllocModel.setCheckStatus(checkStatus);
            wmSoAllocModel.setPickOp(userName);
            wmSoAllocModel.setPickTime(new Date());

            // 新增分配记录
            BanQinWmSoAlloc newSoAllocModel = new BanQinWmSoAlloc();
            BeanUtils.copyProperties(wmSoAllocModel, newSoAllocModel);
            newSoAllocModel.setIsNewRecord(true);
            newSoAllocModel.setId(IdGen.uuid());
            newSoAllocModel.setStatus(WmsCodeMaster.ALLOC_FULL_ALLOC.getCode());// 完全分配
            // -
            // 新记录未拣货
            newSoAllocModel.setQtyEa(newQtyPkEa);
            newSoAllocModel.setQtyUom(newQtyPkUom);
            newSoAllocModel.setUom(newUom);
            newSoAllocModel.setPickOp(null);
            newSoAllocModel.setPickTime(null);
            // 新分配记录的分配ID
            String allocId = noService.getDocumentNo(GenNoType.WM_ALLOC_ID.name());
            newSoAllocModel.setAllocId(allocId);

            // 源分配记录，目标拣货位，目标跟踪号
            wmSoAllocModel.setToLoc(wmSoAllocEntity.getToLoc());// 目标拣货位
            wmSoAllocModel.setToId(wmSoAllocEntity.getToId());// 目标跟踪号
            this.wmSoAllocService.save(wmSoAllocModel);
            // 新分配记录保存
            this.wmSoAllocService.save(newSoAllocModel);
        }

        // 6、更新库存、交易(操作数量=实际拣货数量)
        BanQinInventoryEntity invEntity = updateInventory(wmSoAllocModel);
        // 7、更新出库单明细、波次单明细、出库单状态、波次单状态
        outboundCommon.updateOrder(ActionCode.PICKING, wmSoAllocModel.getQtyEa(), wmSoAllocModel.getSoNo(), wmSoAllocModel.getLineNo(), wmSoAllocEntity.getWaveNo(), wmSoAllocModel.getOrgId());
        // 出参 数据对象
        BanQinOutboundPickingEntity entity = new BanQinOutboundPickingEntity();
        entity.setWmSoAllocModel(wmSoAllocModel);// 分配拣货记录
        entity.setTranId(invEntity.getTranId());// 交易事务ID
        // 信息返回
        msg.setSuccess(true);
        msg.setData(entity);
        return msg;
    }

    /**
     * 库存交易更新
     *
     * @param wmSoAllocModel
     * @return
     * @throws WarehouseException
     */
    @Transactional
    public BanQinInventoryEntity updateInventory(BanQinWmSoAlloc wmSoAllocModel) throws WarehouseException {
        // 库存更新FM对象-分配
        BanQinInventoryEntity fromInvEntity = new BanQinInventoryEntity();
        fromInvEntity.setAction(ActionCode.PICKING);// ACTION_CODE 拣货
        fromInvEntity.setOwnerCode(wmSoAllocModel.getOwnerCode());// 货主
        fromInvEntity.setSkuCode(wmSoAllocModel.getSkuCode());// 商品
        fromInvEntity.setLotNum(wmSoAllocModel.getLotNum());// 批次号
        fromInvEntity.setLocCode(wmSoAllocModel.getLocCode());// 分配库位
        fromInvEntity.setTraceId(wmSoAllocModel.getTraceId());// 分配库位跟踪号
        fromInvEntity.setOrderNo(wmSoAllocModel.getSoNo());// 出库单号
        fromInvEntity.setLineNo(wmSoAllocModel.getLineNo());// 出库单行号
        fromInvEntity.setTaskId(wmSoAllocModel.getAllocId());// 任务号-分配号
        fromInvEntity.setPackCode(wmSoAllocModel.getPackCode());// 包装代码
        fromInvEntity.setUom(wmSoAllocModel.getUom());// 包装单位
        fromInvEntity.setQtyUom(wmSoAllocModel.getQtyUom());// 包装单位=分配数量UOM
        fromInvEntity.setQtyEaOp(wmSoAllocModel.getQtyEa());// 拣货数量=分配数量EA
        fromInvEntity.setOrgId(wmSoAllocModel.getOrgId());
        // 库存更新TO对象-目标拣货
        BanQinInventoryEntity toInvEntity = new BanQinInventoryEntity();
        toInvEntity.setAction(ActionCode.PICKING);// ACTION_CODE 拣货
        toInvEntity.setOwnerCode(wmSoAllocModel.getOwnerCode());// 货主
        toInvEntity.setSkuCode(wmSoAllocModel.getSkuCode());// 商品
        toInvEntity.setLotNum(wmSoAllocModel.getLotNum());// 批次号
        toInvEntity.setLocCode(wmSoAllocModel.getToLoc());// 拣货目标库位
        toInvEntity.setTraceId(wmSoAllocModel.getToId());// 拣货目标跟踪号
        toInvEntity.setOrderNo(wmSoAllocModel.getSoNo());// 出库单号
        toInvEntity.setLineNo(wmSoAllocModel.getLineNo());// 出库单行号
        toInvEntity.setTaskId(wmSoAllocModel.getAllocId());// 任务号-分配号
        toInvEntity.setPackCode(wmSoAllocModel.getPackCode());// 包装代码
        toInvEntity.setUom(wmSoAllocModel.getUom());// 包装单位
        toInvEntity.setQtyEaOp(wmSoAllocModel.getQtyEa());// 拣货数量EA
        toInvEntity.setQtyUom(wmSoAllocModel.getQtyUom());// 包装数=拣货数量UOM
        toInvEntity.setOrgId(wmSoAllocModel.getOrgId());

        return inventoryService.updateInventory(fromInvEntity, toInvEntity);
    }
}