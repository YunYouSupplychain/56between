package com.yunyou.modules.wms.inventory.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.wms.inventory.entity.BanQinWmInvLotAtt;
import com.yunyou.modules.wms.inventory.entity.BanQinWmInvLotLoc;
import com.yunyou.modules.wms.inventory.entity.BanQinWmInvLotLocEntity;
import com.yunyou.modules.wms.inventory.mapper.BanQinWmInvLotLocMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 批次库位库存表Service
 *
 * @author WMJ
 * @version 2019-01-25
 */
@Service
@Transactional(readOnly = true)
public class BanQinWmInvLotLocService extends CrudService<BanQinWmInvLotLocMapper, BanQinWmInvLotLoc> {

    @SuppressWarnings("unchecked")
    public Page<BanQinWmInvLotLocEntity> findPage(Page page, BanQinWmInvLotLocEntity banQinWmInvLotLocEntity) {
        dataRuleFilter(banQinWmInvLotLocEntity);
        banQinWmInvLotLocEntity.setPage(page);
        page.setList(mapper.findPage(banQinWmInvLotLocEntity));
        return page;
    }

    public BanQinWmInvLotLoc findFirst(BanQinWmInvLotLoc banQinWmInvLotLoc) {
        List<BanQinWmInvLotLoc> list = this.findList(banQinWmInvLotLoc);
        return CollectionUtil.isNotEmpty(list) ? list.get(0) : null;
    }

    /**
     * 通过批次号、库位、跟踪号查找库存记录
     *
     * @param lotNum
     * @param location
     * @param traceId
     * @param orgId
     */
    public BanQinWmInvLotLoc getByLotNumAndLocationAndTraceId(String lotNum, String location, String traceId, String orgId) {
        BanQinWmInvLotLoc wmInvLotLocModel = new BanQinWmInvLotLoc();
        wmInvLotLocModel.setLotNum(lotNum);
        wmInvLotLocModel.setLocCode(location);
        wmInvLotLocModel.setTraceId(traceId);
        wmInvLotLocModel.setOrgId(orgId);
        return this.findFirst(wmInvLotLocModel);
    }

    /**
     * 通过批次号查找库存记录
     *
     * @param lotNum
     * @param orgId
     */
    public List<BanQinWmInvLotLoc> getByLotNum(String lotNum, String orgId) {
        BanQinWmInvLotLoc wmInvLotLocModel = new BanQinWmInvLotLoc();
        wmInvLotLocModel.setLotNum(lotNum);
        wmInvLotLocModel.setOrgId(orgId);
        return this.findList(wmInvLotLocModel);
    }

    /**
     * 通过库位查找库存记录
     *
     * @param locCode
     * @param orgId
     */
    public List<BanQinWmInvLotLoc> getByLocCode(String locCode, String orgId) {
        BanQinWmInvLotLoc wmInvLotLocModel = new BanQinWmInvLotLoc();
        wmInvLotLocModel.setLocCode(locCode);
        wmInvLotLocModel.setOrgId(orgId);
        return this.findList(wmInvLotLocModel);
    }

    /**
     * 通过跟踪号查询库存记录
     *
     * @param traceId
     * @param orgId
     */
    public List<BanQinWmInvLotLoc> getByTraceId(String traceId, String orgId) {
        BanQinWmInvLotLoc wmInvLotLocModel = new BanQinWmInvLotLoc();
        wmInvLotLocModel.setTraceId(traceId);
        wmInvLotLocModel.setOrgId(orgId);
        return this.findList(wmInvLotLocModel);
    }

    /**
     * 通过批次号统计移库待出数，补货待出数，上架待出数
     *
     * @param lotNum
     * @param orgId
     */
    public BanQinWmInvLotLoc getInvQtyWaitOutByLotNum(String lotNum, String orgId) {
        BanQinWmInvLotLoc model = new BanQinWmInvLotLoc();
        double qtyRpOut = 0.0;
        double qtyMvOut = 0.0;
        double qtyPaOut = 0.0;
        List<BanQinWmInvLotLoc> wmInvLotLocModels = this.getByLotNum(lotNum, orgId);
        for (BanQinWmInvLotLoc invModel : wmInvLotLocModels) {
            qtyRpOut = qtyRpOut + invModel.getQtyRpOut();
            qtyMvOut = qtyMvOut + invModel.getQtyMvOut();
            qtyPaOut = qtyPaOut + invModel.getQtyPaOut();
        }
        model.setLotNum(lotNum);
        model.setQtyRpOut(qtyRpOut);
        model.setQtyMvOut(qtyMvOut);
        model.setQtyPaOut(qtyPaOut);
        return model;
    }

    /**
     * 通过库位编码,货主编码，商品编码统计库存数，移库待入数，补货待入数，上架待入数
     *
     * @param ownerCode
     * @param skuCode
     * @param locCode
     * @param orgId
     */
    public Double getInvQtyWaitInByOwnerAndSkuAndOwner(String ownerCode, String skuCode, String locCode, String orgId) {
        double qty = 0.0;
        double qtyRpIn = 0.0;
        double qtyMvIn = 0.0;
        double qtyPaIn = 0.0;
        List<BanQinWmInvLotLoc> wmInvLotLocModels = this.getByOwnerAndSkuAndLocCode(ownerCode, skuCode, locCode, orgId);
        for (BanQinWmInvLotLoc invModel : wmInvLotLocModels) {
            qty = qty + invModel.getQty();
            qtyRpIn = qtyRpIn + invModel.getQtyRpIn();
            qtyMvIn = qtyMvIn + invModel.getQtyMvIn();
            qtyPaIn = qtyPaIn + invModel.getQtyPaIn();
        }
        return qty + qtyRpIn + qtyMvIn + qtyPaIn;
    }

    /**
     * 是否是空库位，TRUE为空，FALSE为非空
     *
     * @param locCode
     * @param orgId
     */
    public boolean isEmptyLoc(String locCode, String orgId) {
        boolean flag = true;
        Double qty = this.getInvQtyWaitInByOwnerAndSkuAndOwner(null, null, locCode, orgId);
        if (qty > 0.0) {
            flag = false;
        }
        return flag;
    }

    /**
     * 是否是相同商品的库位，TRUE为是，FALSE为不是
     *
     * @param ownerCode
     * @param skuCode
     * @param locCode
     * @param orgId
     */
    public boolean isHaveSameSkuLoc(String ownerCode, String skuCode, String locCode, String orgId) {
        boolean flag = false;
        Double qty = this.getInvQtyWaitInByOwnerAndSkuAndOwner(ownerCode, skuCode, locCode, orgId);
        if (qty > 0.0) {
            flag = true;
        }
        return flag;
    }

    /**
     * 是否是非空的库位，TRUE为非空，FALSE为空
     *
     * @param locCode
     * @param orgId
     */
    public boolean isNotEmptyLoc(String locCode, String orgId) {
        boolean flag = false;
        Double qty = this.getInvQtyWaitInByOwnerAndSkuAndOwner(null, null, locCode, orgId);
        if (qty > 0.0) {
            flag = true;
        }
        return flag;
    }

    /**
     * 通过库位编码统计托盘数--带托盘号，没有忽略TraceID的情况。
     *
     * @param locCode
     * @param orgId
     */
    public Long getPalletQtyByLocCode(String locCode, String orgId) {
        List<BanQinWmInvLotLoc> list = getByLocCode(locCode, orgId);
        return list.stream().map(BanQinWmInvLotLoc::getTraceId).distinct().count();
    }

    /**
     * 通过库位编码统计托盘数--没有托盘号，忽略TraceID的情况
     *
     * @param locCode
     * @param orgId
     */
    public Long getPalletQty1ByLocCode(String locCode, String orgId) {
        BanQinWmInvLotLoc condition = new BanQinWmInvLotLoc();
        condition.setLocCode(locCode);
        condition.setOrgId(orgId);
        List<BanQinWmInvLotAtt> list = mapper.getPalletQty1ByLocCode(condition);
        return list.stream().map(BanQinWmInvLotAtt::getLotAtt07).distinct().count();
    }

    /**
     * 通过库位统计库位上的商品体积
     *
     * @param locCode
     * @param orgId
     */
    public Double getCubicByLocCode(String locCode, String orgId) {
        BanQinWmInvLotLoc condition = new BanQinWmInvLotLoc();
        condition.setLocCode(locCode);
        condition.setOrgId(orgId);
        Double wmInvCubic = mapper.getWmInvCubic(condition);
        return null == wmInvCubic ? 0d : wmInvCubic;
    }

    /**
     * 通过库位统计库位上的商品毛重
     *
     * @param locCode
     * @param orgId
     */
    public Double getGrossWeightByLocCode(String locCode, String orgId) {
        BanQinWmInvLotLoc condition = new BanQinWmInvLotLoc();
        condition.setLocCode(locCode);
        condition.setOrgId(orgId);
        Double wmInvGrossWeight = mapper.getWmInvGrossWeight(condition);
        return null == wmInvGrossWeight ? 0d : wmInvGrossWeight;
    }

    /**
     * 通过库位统计库存库位上的批次数
     *
     * @param locCode
     * @param orgId
     */
    public Map<String, List<String>> getLotNumQtyByLocCode(String locCode, String orgId) {
        Map<String, List<String>> map = new HashMap<>();
        List<BanQinWmInvLotLoc> list = this.getByLocCode(locCode, orgId);
        map.put("LOT", list.stream().map(BanQinWmInvLotLoc::getLotNum).distinct().collect(Collectors.toList()));
        return map;
    }

    /**
     * 通过库位统计库存库位上的商品品号数
     *
     * @param locCode
     * @param orgId
     */
    public Map<String, List<String>> getSkuQtyByLocCode(String locCode, String orgId) {
        Map<String, List<String>> map = new HashMap<>();
        List<BanQinWmInvLotLoc> list = this.getByLocCode(locCode, orgId);
        map.put("SKU", list.stream().map(BanQinWmInvLotLoc::getSkuCode).distinct().collect(Collectors.toList()));
        return map;
    }

    /**
     * 根据货主、商品、库位，获取批次库位库存记录
     *
     * @param ownerCode
     * @param skuCode
     * @param locCode
     * @param orgId
     */
    public List<BanQinWmInvLotLoc> getByOwnerAndSkuAndLocCode(String ownerCode, String skuCode, String locCode, String orgId) {
        BanQinWmInvLotLoc model = new BanQinWmInvLotLoc();
        model.setOwnerCode(ownerCode);
        model.setSkuCode(skuCode);
        model.setLocCode(locCode);
        model.setOrgId(orgId);
        return this.findList(model);
    }

    /**
     * 根据库位、批次号，获取批次库位库存记录
     *
     * @param lotNum
     * @param locCode
     * @param orgId
     */
    public List<BanQinWmInvLotLoc> getByLotNumAndLocCode(String lotNum, String locCode, String orgId) {
        BanQinWmInvLotLoc model = new BanQinWmInvLotLoc();
        model.setLocCode(locCode);
        model.setLotNum(lotNum);
        model.setOrgId(orgId);
        return this.findList(model);
    }

    /**
     * 可分配数量
     *
     * @param invLotLocModel
     */
    public Double getQtyAllocAvailable(BanQinWmInvLotLoc invLotLocModel) {
        // 库存可分配数量 = 库存总数量-冻结数量-分配数量-已拣货数量-上架待出数量-补货待出数量-移动待出数量
        return invLotLocModel.getQty() - invLotLocModel.getQtyHold() - invLotLocModel.getQtyAlloc()
                - invLotLocModel.getQtyPk() - invLotLocModel.getQtyPaOut()
                - invLotLocModel.getQtyRpOut() - invLotLocModel.getQtyMvOut();
    }

    /**
     * 供删除方法查找WmInvLotLocModel对象，根据库位编码作为LocCode字段查找条件
     *
     * @param locCode
     * @param orgId
     */
    public ResultMessage getDelByLocCode(String locCode, String orgId) {
        ResultMessage msg = new ResultMessage();
        // 通过locCode判断库位是否被调用
        BanQinWmInvLotLoc model = new BanQinWmInvLotLoc();
        // 设置查询对象的值
        model.setLocCode(locCode);
        model.setOrgId(orgId);
        // 查询出调用此库位的对象
        BanQinWmInvLotLoc invLotLocModel = this.findFirst(model);
        // 若此调用对象数量不为空则说明已经被调用
        if (null != invLotLocModel) {
            msg.setSuccess(false);
            return msg;
        }
        msg.setSuccess(true);
        return msg;
    }

    /**
     * 验证跟踪号是否存在
     *
     * @param traceId
     * @param orgId
     */
    public ResultMessage checkByTraceId(String traceId, String orgId) {
        ResultMessage msg = new ResultMessage();
        // 通过TraceId判断是否存在
        BanQinWmInvLotLoc model = new BanQinWmInvLotLoc();
        // 设置查询对象的值
        model.setTraceId(traceId);
        model.setOrgId(orgId);
        BanQinWmInvLotLoc invLotLocModel = this.findFirst(model);
        // 若此调用对象数量不为空则说明已经被调用
        if (null != invLotLocModel) {
            msg.setSuccess(true);
            return msg;
        }
        msg.setSuccess(false);
        return msg;
    }

    public List<BanQinWmInvLotLoc> notAvailableHoldQuery(BanQinWmInvLotLoc banQinWmInvLotLoc) {
        return mapper.notAvailableHoldQuery(banQinWmInvLotLoc);
    }

    public List<BanQinWmInvLotLocEntity> rfMVGetMovementDetailQuery(String locCode, String traceId, String lotNum, String skuCode, String orgId) {
        return mapper.rfMVGetMovementDetailQuery(locCode, traceId, lotNum, skuCode, orgId);
    }

    public List<BanQinWmInvLotLocEntity> rfInvGetSkuDetailQuery(String locCode, String traceId, String lotNum, String skuCode, String orgId) {
        return mapper.rfInvGetSkuDetailQuery(locCode, traceId, lotNum, skuCode, orgId);
    }

}