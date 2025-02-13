package com.yunyou.modules.wms.outbound.service;

import com.yunyou.common.utils.IdGen;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.modules.sys.entity.User;
import com.yunyou.modules.sys.utils.UserUtils;
import com.yunyou.modules.wms.common.entity.WarehouseException;
import com.yunyou.modules.wms.common.entity.WmsCodeMaster;
import com.yunyou.modules.wms.outbound.entity.BanQinCheckPackingTotalInfoEntity;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoAlloc;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoSerial;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoSerialEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 复核功能
 *
 * @author WMJ
 * @version 2019/02/20
 */
@Service
@Transactional(readOnly = true)
public class BanQinOutboundCheckService {
    // 分配明细表
    @Autowired
    protected BanQinWmSoAllocService wmSoAllocService;
    // 出库序列号
    @Autowired
    protected BanQinWmSoSerialService wmSoSerialService;
    // 装车单
    @Autowired
    protected BanQinWmLdDetailService wmLdDetailService;

    /**
     * 复核确认
     *
     * @param allocIds
     * @param soSerialList
     * @param trackingNo
     * @throws WarehouseException
     */
    @Transactional
    public void checkConfirm(String[] allocIds, List<BanQinWmSoSerialEntity> soSerialList, String trackingNo, String orgId) throws WarehouseException {
        User user = UserUtils.getUser();
        double qtyAllocEa = 0D;// 分配总数
        // 复核确认
        for (String allocId : allocIds) {
            BanQinWmSoAlloc allocModel = this.wmSoAllocService.getByAllocId(allocId, orgId);
            allocModel.setCheckTime(new Date());
            allocModel.setCheckOp(user.getLoginName());
            allocModel.setCheckStatus(WmsCodeMaster.CHECK_CLOSE.getCode());
            // 更新快递单号
            if (StringUtils.isNotEmpty(trackingNo)) {
                allocModel.setTrackingNo(trackingNo);
            }
            wmSoAllocService.save(allocModel);
            qtyAllocEa += allocModel.getQtyEa();
        }
        // 写保存出库序列号
        List<BanQinWmSoSerial> serialModels = new ArrayList<>();
        for (BanQinWmSoSerialEntity item : soSerialList) {
            if (StringUtils.isEmpty(item.getId())) {
                BanQinWmSoSerial model = new BanQinWmSoSerial();
                BeanUtils.copyProperties(item, model);
                model.setId(IdGen.uuid());
                model.setIsNewRecord(true);
                serialModels.add(model);
            }
        }
        // 校验出库序列号与分配ID的总数
//        if (soSerialList.size() > 0) {
//            List<BanQinWmSoSerial> countItem = wmSoSerialService.findByAllocIds(Arrays.asList(allocIds), orgId);
//            long count = countItem.stream().map(BanQinWmSoSerial::getSerialNo).distinct().count();
//            if (qtyAllocEa != serialModels.size() + count) {
//                // 复核数与出库序列号总数不一致，不能操作
//                throw new WarehouseException("复核数与出库序列号总数不一致，不能操作");
//            }
//        }
        for (BanQinWmSoSerial soSerial : serialModels) {
            wmSoSerialService.save(soSerial);
        }
    }

    /**
     * 取消复核
     * @param allocIds
     * @throws WarehouseException
     */
    @Transactional
    public void checkCancel(String[] allocIds, String orgId) throws WarehouseException {
        List<BanQinWmSoAlloc> allocModels = new ArrayList<>();
        List<String> allocIdList = Arrays.asList(allocIds);
        // 获取已经生成装车单的分配明细
        List<String> errorIds = wmLdDetailService.checkIsGenLdByAllocIds(allocIdList, null, orgId);
        if (errorIds.size() > 0) {
            // 已经生成装车单的记录，不允许取消复核
            throw new WarehouseException("已经生成装车单的记录，不允许取消复核");
        }
        for (String allocId : allocIds) {
            BanQinWmSoAlloc allocModel = this.wmSoAllocService.getByAllocId(allocId, orgId);
            allocModel.setCheckTime(null);
            allocModel.setCheckOp(null);
            allocModel.setCheckStatus(WmsCodeMaster.CHECK_NEW.getCode());
            allocModels.add(allocModel);
            wmSoAllocService.save(allocModel);

            // 删除出库序列号（已经打包的记录，不能删除）
            this.wmSoSerialService.removeByAllocIdAndNotPack(allocId, orgId);
        }
    }

    /**
     * 根据soNo订单号获取复核打包的商品数，单品数，总重量，总体积
     * @param soNo
     * @return
     * @throws WarehouseException
     */
    public BanQinCheckPackingTotalInfoEntity getTotalInfoBySoNo(String soNo, String orgId) {
        BanQinCheckPackingTotalInfoEntity soInfoEntity = new BanQinCheckPackingTotalInfoEntity();
        Map<String, Double> resultMap = wmSoAllocService.checkPackingTotalInfo(soNo, null, orgId);
        soInfoEntity.setCubic(resultMap.get("cubic"));
        soInfoEntity.setSkuItemQty(resultMap.get("skuItemQty"));
        soInfoEntity.setSkuQty(resultMap.get("skuQty"));
        soInfoEntity.setWeight(resultMap.get("weight"));
        return soInfoEntity;
    }

    /**
     * 根据TraceId箱号获取复核打包的商品数，单品数，总重量，总体积
     * @param traceId
     * @return
     * @throws WarehouseException
     */
    public BanQinCheckPackingTotalInfoEntity getTotalInfoByTraceId(String traceId, String orgId) {
        BanQinCheckPackingTotalInfoEntity infoEntity = new BanQinCheckPackingTotalInfoEntity();
        Map<String, Double> resultMap = wmSoAllocService.checkPackingTotalInfo(null, traceId, orgId);
        infoEntity.setCubic(resultMap.get("cubic"));
        infoEntity.setSkuItemQty(resultMap.get("skuItemQty"));
        infoEntity.setSkuQty(resultMap.get("skuQty"));
        infoEntity.setWeight(resultMap.get("weight"));
        return infoEntity;
    }
}