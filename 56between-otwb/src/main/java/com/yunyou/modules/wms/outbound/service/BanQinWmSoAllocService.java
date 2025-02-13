package com.yunyou.modules.wms.outbound.service;

import com.google.common.collect.Lists;
import com.yunyou.common.config.GlobalDataRule;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.wms.common.entity.ProcessByCode;
import com.yunyou.modules.wms.common.entity.WarehouseException;
import com.yunyou.modules.wms.common.entity.WmsCodeMaster;
import com.yunyou.modules.wms.common.entity.WmsConstants;
import com.yunyou.modules.wms.outbound.entity.BanQinWmCheckEntity;
import com.yunyou.modules.wms.outbound.entity.BanQinWmPackEntity;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoAlloc;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoAllocEntity;
import com.yunyou.modules.wms.outbound.mapper.BanQinWmSoAllocMapper;
import com.yunyou.modules.wms.report.entity.WmShipDataEntity;
import com.yunyou.modules.wms.report.entity.WmSoSkuLabel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 分配拣货明细Service
 *
 * @author WMJ
 * @version 2019-02-14
 */
@Service
@Transactional(readOnly = true)
public class BanQinWmSoAllocService extends CrudService<BanQinWmSoAllocMapper, BanQinWmSoAlloc> {
    @Autowired
    @Lazy
    private BanQinWmLdDetailService wmLdDetailService;
    
    public BanQinWmSoAlloc get(String id) {
        return super.get(id);
    }

    public BanQinWmSoAllocEntity getEntity(String id) {
        return mapper.getEntity(id);
    }

    public List<BanQinWmSoAlloc> findList(BanQinWmSoAlloc banQinWmSoAlloc) {
        return mapper.findList(banQinWmSoAlloc);
    }

    public List<BanQinWmSoAllocEntity> findByEntity(BanQinWmSoAllocEntity entity) {
        return mapper.findEntity(entity);
    }

    public Page<BanQinWmSoAllocEntity> findPage(Page page, BanQinWmSoAllocEntity banQinWmSoAllocEntity) {
        banQinWmSoAllocEntity.setDataScope(GlobalDataRule.getDataRuleSql("a.org_id", banQinWmSoAllocEntity.getOrgId()));
        dataRuleFilter(banQinWmSoAllocEntity);
        banQinWmSoAllocEntity.setPage(page);
        page.setList(mapper.findPage(banQinWmSoAllocEntity));
        return page;
    }

    public Page<WmShipDataEntity> findShipData(Page page, WmShipDataEntity entity) {
        entity.setDataScope(GlobalDataRule.getDataRuleSql("a.org_id", entity.getOrgId()));
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findShipData(entity));
        return page;
    }

    public List<BanQinWmSoAllocEntity> findEntityByIds(List<String> ids) {
        return mapper.findEntityByIds(ids);
    }

    @Transactional
    public void save(BanQinWmSoAlloc banQinWmSoAlloc) {
        super.save(banQinWmSoAlloc);
    }

    @Transactional
    public void delete(BanQinWmSoAlloc banQinWmSoAlloc) {
        super.delete(banQinWmSoAlloc);
    }

    public BanQinWmSoAlloc findFirst(BanQinWmSoAlloc banQinWmSoAlloc) {
        List<BanQinWmSoAlloc> list = this.findList(banQinWmSoAlloc);
        return CollectionUtil.isNotEmpty(list) ? list.get(0) : null;
    }

    /**
     * 根据分配编号，获取分配明细
     *
     * @param allocId
     * @return
     * @throws WarehouseException
     */
    public BanQinWmSoAlloc getByAllocId(String allocId, String orgId) throws WarehouseException {
        BanQinWmSoAlloc model = new BanQinWmSoAlloc();
        model.setAllocId(allocId);
        model.setOrgId(orgId);
        model = this.findFirst(model);
        if (model == null) {
            // 查询不到分配明细{0}
            throw new WarehouseException("查询不到分配明细", allocId);
        }
        return model;
    }

    /**
     * 根据So编号，获取分配明细
     *
     * @param soNo
     * @return
     */
    public List<BanQinWmSoAlloc> getBySoNo(String soNo, String orgId) {
        BanQinWmSoAlloc model = new BanQinWmSoAlloc();
        model.setSoNo(soNo);
        model.setOrgId(orgId);
        return mapper.findList(model);
    }

    /**
     * 根据波次单号获取分配记录
     *
     * @param waveNo
     * @return
     */
    public List<BanQinWmSoAllocEntity> getEntityByWaveNo(String waveNo, String orgId) {
        BanQinWmSoAllocEntity condition = new BanQinWmSoAllocEntity();
        condition.setWaveNo(waveNo);
        condition.setOrgId(orgId);
        return this.findByEntity(condition);
    }

    /**
     * 根据发运单号获取分配记录
     *
     * @param soNo
     * @return
     */
    public List<BanQinWmSoAllocEntity> getEntityBySoNo(String soNo, String orgId) {
        BanQinWmSoAllocEntity condition = new BanQinWmSoAllocEntity();
        condition.setSoNo(soNo);
        condition.setOrgId(orgId);
        return this.findByEntity(condition);
    }

    /**
     * 根据分配ID获取分配记录
     *
     * @param allocId
     * @return
     */
    public BanQinWmSoAllocEntity getEntityByAllocId(String allocId, String orgId) {
        BanQinWmSoAllocEntity condition = new BanQinWmSoAllocEntity();
        condition.setAllocId(allocId);
        condition.setOrgId(orgId);
        List<BanQinWmSoAllocEntity> entities = mapper.findEntity(condition);
        return CollectionUtil.isNotEmpty(entities) ? entities.get(0) : null;
    }

    /**
     * 类型 按波次单号、按出库单号、按出库单号+行号、按分配ID、按目标跟踪号 过滤出库单/行 关闭、取消状态,
     * 过滤 拦截订单，过滤 冻结订单 状态-完全分配、完全拣货(执行操作：拣货、发货、取消拣货、取消分配)
     * @param processByCode
     * @param noList
     * @param status
     * @return
     */
    public List<BanQinWmSoAllocEntity> getEntityByProcessByCode(String processByCode, List<String> noList, String status, String orgId) {
        List<BanQinWmSoAllocEntity> wmSoAllocEntityList = Lists.newArrayList();
        // 获取分配明细
        BanQinWmSoAllocEntity condition = new BanQinWmSoAllocEntity();
        condition.setOrgId(orgId);
        condition.setStatus(status);
        // 按波次单号
        if (processByCode.equals(ProcessByCode.BY_WAVE.getCode())) {
            condition.setWaveNos(noList);
            wmSoAllocEntityList = mapper.findByNo(condition);
        } else if (processByCode.equals(ProcessByCode.BY_SO.getCode())) {
            // 按出库单号
            condition.setSoNos(noList);
            wmSoAllocEntityList = mapper.findByNo(condition);
        } else if (processByCode.equals(ProcessByCode.BY_SO_LINE.getCode())) {
            // 按出库单号+行号
            condition.setLineNos(StringUtils.join(noList, ","));
            wmSoAllocEntityList = mapper.findByNo(condition);
        } else if (processByCode.equals(ProcessByCode.BY_ALLOC.getCode())) {
            // 按分配ID
            condition.setAllocIds(noList);
            wmSoAllocEntityList = mapper.findByNo(condition);
        } else if (processByCode.equals(ProcessByCode.BY_TOID.getCode())) {
            // 按目标跟踪号TO_ID
            condition.setToIds(noList);
            wmSoAllocEntityList = mapper.findByNo(condition);
        }

        return wmSoAllocEntityList;
    }

    /**
     * 获取拦截订单
     * @param processByCode 类型 按波次单号、按出库单号、按出库单号+行号、按分配ID 过滤出库单/行 关闭、取消状态,
     * @param noList
     * @param status 状态-完全分配、完全拣货(执行操作：拣货、发货、取消拣货、取消分配)
     * @return
     */
    public List<BanQinWmSoAllocEntity> getEntityByIntercept(String processByCode, List<String> noList, String status, String orgId) {
        List<BanQinWmSoAllocEntity> wmSoAllocEntityList = new ArrayList<>();
        // 按波次单号
        if (processByCode.equals(ProcessByCode.BY_WAVE.getCode())) {
            wmSoAllocEntityList = mapper.getWmInterceptSoAlloc(null, null, null, noList, orgId, status);
        } else if (processByCode.equals(ProcessByCode.BY_SO.getCode())) {
            // 按出库单号
            wmSoAllocEntityList = mapper.getWmInterceptSoAlloc(null, noList, null, null, orgId, status);
        } else if (processByCode.equals(ProcessByCode.BY_SO_LINE.getCode())) {
            // 按出库单号+行号
            wmSoAllocEntityList = mapper.getWmInterceptSoAlloc(null, null, StringUtils.join(noList.toArray(), ","), null, orgId, status);
        } else if (processByCode.equals(ProcessByCode.BY_ALLOC.getCode())) {
            // 按分配ID
            wmSoAllocEntityList = mapper.getWmInterceptSoAlloc(noList, null, null, null, orgId, status);
        }
        return wmSoAllocEntityList;
    }

    /**
     * 根据界面传入的查询条件，获取复核记录,并且所有记录必须完全拣货
     * @param condition
     * @return
     * @throws WarehouseException
     */
    @Transactional
    public BanQinWmCheckEntity getCheckByCondition(BanQinWmSoAllocEntity condition) throws WarehouseException {
        BanQinWmCheckEntity entity = new BanQinWmCheckEntity();
        // 控制参数控制允许进行部分复核
        String checkAllocPart = "Y";
        if (!checkAllocPart.equals(WmsConstants.YES)) {
            // 按出库单,存在未完全拣货记录,{0}不是完全拣货状态，不能操作
            if (StringUtils.isNotEmpty(condition.getSoNo())) {
                List<Object> countItems = this.executeSelectSql("select count(1) \n" +
                        "  from WM_SO_DETAIL WSD\n" +
                        " where 1 = 1\n" +
                        "   and WSD.QTY_SO_EA <> WSD.QTY_PK_EA + WSD.QTY_SHIP_EA\n" +
                        "   and WSD.STATUS != '90'\n" +
                        "   and WSD.SO_NO = '" + condition.getSoNo() + "'\n" +
                        "   and WSD.ORG_ID = '" + condition.getOrgId() + "'");
                if (countItems.size() > 0) {
                    Integer lineCount = countItems.size();
                    // 行数大于0，表示c
                    if (lineCount > 0) {
                        // 按出库单
                        throw new WarehouseException(condition.getSoNo() + "存在未完全拣货的记录");
                    }
                }
            }
        }
        // 查询分配拣货记录
        List<BanQinWmSoAllocEntity> items = mapper.getWmCheck(condition);
        List<BanQinWmSoAllocEntity> resultItems = new ArrayList<>();// 结果集
        if (items.size() > 0) {
            // 按箱号查询，存在未完全拣货记录,{0}不是完全拣货状态，不能操作
            if (StringUtils.isNotEmpty(condition.getToId())) {
                for (BanQinWmSoAllocEntity item : items) {
                    if (item.getStatus().compareTo(WmsCodeMaster.ALLOC_FULL_PICKING.getCode()) < 0) {
                        // 按箱号
                        throw new WarehouseException(condition.getToId() + "存在未完全拣货记录");
                    }
                }
                resultItems = items;
            } else if (StringUtils.isNotEmpty(condition.getSoNo())) {
                for (BanQinWmSoAllocEntity item : items) {
                    // 如果是拣货状态
                    if (item.getStatus().equals(WmsCodeMaster.ALLOC_FULL_PICKING.getCode())) {
                        // 如果复核并且已经生成装车单，不查询出来
                        if (item.getCheckStatus().equals(WmsCodeMaster.CHECK_CLOSE.getCode()) && this.wmLdDetailService.CheckIsGeneratorLdByAllocId(item.getAllocId(), item.getOrgId())) {
                            // 继续搜索下一行分配明细
                            continue;
                        }
                        resultItems.add(item);
                        entity.setSoTrackingNo(item.getSoTrackingNo());
                    }
                }
            }
            resultItems.forEach(v -> v.setBarcode(v.getBarcode() + "," + v.getSkuCode()));
            entity.setCheckItems(resultItems);
        }
        return entity;
    }

    /**
     * 根据界面传入的查询条件，获取打包记录,并且所有记录必须完全拣货
     * @param condition
     * @return
     * @throws WarehouseException
     */
    @Transactional
    public BanQinWmPackEntity getPackByCondition(BanQinWmSoAllocEntity condition) throws WarehouseException {
        BanQinWmPackEntity entity = new BanQinWmPackEntity();
        // 控制参数控制允许部分拣货的出库单，可查询出来
        String checkAllocPart = "Y";
        if (!checkAllocPart.equals(WmsConstants.YES)) {
            // 按出库单,存在未完全拣货记录,{0}不是完全拣货状态，不能操作
            if (StringUtils.isNotEmpty(condition.getSoNo())) {
                List<Object> countItems = this.executeSelectSql("select count(1) \n" +
                        "  from WM_SO_DETAIL WSD\n" +
                        " where 1 = 1\n" +
                        "   and WSD.QTY_SO_EA <> WSD.QTY_PK_EA + WSD.QTY_SHIP_EA\n" +
                        "   and WSD.STATUS != '90'\n" +
                        "   and WSD.SO_NO = '" + condition.getSoNo() + "'\n" +
                        "   and WSD.ORG_ID = '" + condition.getOrgId() + "'");
                if (countItems.size() > 0) {
                    Integer lineCount = countItems.size();
                    // 行数大于0，表示c
                    if (lineCount > 0) {
                        // 按出库单
                        throw new WarehouseException(condition.getSoNo() + "存在未完全拣货的记录, 不能操作");
                    }
                }
            }
        }
        // 查询分配拣货记录
        List<BanQinWmSoAllocEntity> items = mapper.getWmPack(condition);
        List<BanQinWmSoAllocEntity> resultItems = new ArrayList<>();
        if (items.size() > 0) {
            // 按箱号查询，存在未完全拣货记录,{0}不是完全拣货状态，不能操作
//            if (StringUtils.isNotEmpty(condition.getToId())) {
            if (StringUtils.isNotEmpty(condition.getCaseNo())) {
                for (BanQinWmSoAllocEntity item : items) {
                    if (item.getStatus().compareTo(WmsCodeMaster.ALLOC_FULL_PICKING.getCode()) < 0) {
                        // 按箱号
                        throw new WarehouseException(condition.getToId() + "存在未完全拣货记录");
                    }
                }
                // 已经生成装车单的订单拣货明细不允许重新打包换箱
                if (this.wmLdDetailService.CheckIsGeneratorLdByToId(condition.getToId(), condition.getOrgId())) {
                    // 已经生成装车单，不能操作
                    throw new WarehouseException("已经生成装车单，不能操作");
                }
                resultItems = items;
            } else if (StringUtils.isNotEmpty(condition.getSoNo())) {
                // 获取快递单号，获取拣货状态记录
                for (BanQinWmSoAllocEntity item : items) {
                    // 如果是拣货状态
                    if (item.getStatus().equals(WmsCodeMaster.ALLOC_FULL_PICKING.getCode())) {
                        // 2014-12-16.1 添加过滤
                        // 如果复核并且已经生成装车单，不查询出来
                        if (item.getCheckStatus().equals(WmsCodeMaster.CHECK_CLOSE.getCode()) && this.wmLdDetailService.CheckIsGeneratorLdByAllocId(item.getAllocId(), item.getOrgId())) {
                            // 继续搜索下一行分配明细
                            continue;
                        }
                        resultItems.add(item);
                        entity.setSoTrackingNo(item.getSoTrackingNo());
                    }
                }
            }
            resultItems.forEach(v -> v.setBarcode(v.getBarcode() + "," + v.getSkuCode()));
            entity.setAllocItems(resultItems);
        }
        return entity;
    }

    /**
     * 根据目标跟踪号获取快递单号
     *
     * @param toId
     * @return
     */
    public String getTrackingNoByToId(String toId, String orgId) {
        BanQinWmSoAlloc model = new BanQinWmSoAlloc();
        model.setToId(toId);
        model.setOrgId(orgId);
        model = this.findFirst(model);
        if (model != null) {
            return model.getTrackingNo();
        }
        return null;
    }

    /**
     * 根据出库订单号，获取未完全复核的发运订单
     * @param soNos
     * @return
     */
    public List<String> checkNoFullCheckBySoNOs(List<String> soNos, String orgId) {
        return mapper.checkNoFullCheckBySoNos(soNos, orgId);
    }

    /**
     * 获取取消越库分配记录
     *
     * @param asnNo
     * @param rcvLineNo
     * @param cdType
     * @param status
     * @return
     */
    public List<BanQinWmSoAllocEntity> getEntityByCd(String asnNo, String rcvLineNo, String[] cdType, String[] status, String orgId) {
        BanQinWmSoAllocEntity condition = new BanQinWmSoAllocEntity();
        condition.setAsnNo(asnNo);
        condition.setRcvLineNo(rcvLineNo);
        condition.setCdTypeList(null != cdType ? Arrays.asList(cdType) : null);
        condition.setStatusList(null != status ? Arrays.asList(status) : null);
        condition.setOrgId(orgId);
        
        return mapper.getWmSoAllocByCd(condition);
    }

    /**
     * 获取复核打包的商品数，单品数，总重量，总体积
     * @param soNo
     * @param traceId
     * @param orgId
     * @return
     */
    public Map<String, Double> checkPackingTotalInfo(String soNo, String traceId, String orgId) {
        return mapper.checkPackingTotalInfo(soNo, traceId, orgId);
    }

    /**
     * 根据波次单号获取分配记录
     * @param waveNos
     * @param uom
     * @param orgId
     * @return
     */
    public List<BanQinWmSoAllocEntity> findByWaveNos(List<String> waveNos, String uom, String orgId) {
        return mapper.findByWaveNos(waveNos, uom, orgId);
    }
    
    
    public List<BanQinWmSoAllocEntity> getWmSoAllocToTraceId(BanQinWmSoAllocEntity banQinWmSoAllocEntity) {
        return mapper.getWmSoAllocToTraceId(banQinWmSoAllocEntity);
    }

    public Map<String, Long> rfPKGetPickNumQuery(String soNo, String pickNo, String allocId, String orgId, String waveNo) {
        return mapper.rfPKGetPickNumQuery(soNo, pickNo, allocId, orgId, waveNo);
    }

    public List<BanQinWmSoAllocEntity> rfPKGetPickDetailQuery(String soNo, String pickNo, String allocId, String orgId, String waveNo) {
        return mapper.rfPKGetPickDetailQuery(soNo, pickNo, allocId, orgId, waveNo);
    }

    public List<BanQinWmSoAllocEntity> rfInvGetPickBoxDetailQuery(String soNo, String toId, String orgId) {
        return mapper.rfInvGetPickBoxDetailQuery(soNo, toId, orgId);
    }

    @Transactional
    public void updatePackInfo(String id, String trackingNo, String consigneeAddress, String caseNo) {
        mapper.updatePackInfo(id, trackingNo, consigneeAddress, caseNo);
    }

    @Transactional
    public void updatePackScanCount(String id, Integer packScanCount) {
        mapper.updatePackScanCount(id, packScanCount);
    }

    @Transactional
    public void cancelPack(String id, String checkStatus, String checkOp, Date checkTime, String updateBy) {
        mapper.cancelPack(id, checkStatus, checkOp, checkTime, updateBy);
    }

    public Page<BanQinWmSoAllocEntity> findSkuDataPage(Page page, BanQinWmSoAllocEntity entity) {
        entity.setDataScope(GlobalDataRule.getDataRuleSql("a.org_id", entity.getOrgId()));
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findSkuDataPage(entity));
        return page;
    }

    public List<WmSoSkuLabel> getWmSoSkuLabel(BanQinWmSoAllocEntity entity) {
        List<WmSoSkuLabel> printList = Lists.newArrayList();
        List<WmSoSkuLabel> wmSoSkuLabel = mapper.getWmSoSkuLabel(entity);
        if (CollectionUtil.isNotEmpty(wmSoSkuLabel)) {
            for (WmSoSkuLabel item : wmSoSkuLabel) {
                for (int i=1; i <= item.getBoxNum(); i++) {
                    WmSoSkuLabel printLabel = new WmSoSkuLabel();
                    BeanUtils.copyProperties(item, printLabel);
                    printLabel.setSeqNum(i);
                    printList.add(printLabel);
                }
            }
        }
        return printList;
    }
}