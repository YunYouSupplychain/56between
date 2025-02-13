package com.yunyou.modules.wms.outbound.service;

import com.google.common.collect.Lists;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.sys.utils.SysControlParamsUtils;
import com.yunyou.modules.wms.common.entity.ControlParamCode;
import com.yunyou.modules.wms.common.entity.WarehouseException;
import com.yunyou.modules.wms.common.entity.WmsCodeMaster;
import com.yunyou.modules.wms.common.entity.WmsConstants;
import com.yunyou.modules.wms.common.service.BanQinWmsCommonService;
import com.yunyou.modules.wms.outbound.entity.*;
import com.yunyou.modules.wms.outbound.mapper.BanQinWmLdDetailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 装车单明细Service
 * @author WMJ
 * @version 2019-02-20
 */
@Service
@Transactional(readOnly = true)
public class BanQinWmLdDetailService extends CrudService<BanQinWmLdDetailMapper, BanQinWmLdDetail> {
    @Autowired
    protected BanQinWmsCommonService wmCommon;
    @Autowired
    protected BanQinWmSoAllocService wmSoAllocService;
    @Autowired
    @Lazy
    protected BanQinWmLdHeaderService wmLdHeaderService;
    @Autowired
    @Lazy
    protected BanQinWmSoHeaderService wmSoHeaderService;
    @Autowired
    @Lazy
    protected BanQinWmSoDetailService wmSoDetailService;

	public BanQinWmLdDetail get(String id) {
		return super.get(id);
	}
	
	public List<BanQinWmLdDetail> findList(BanQinWmLdDetail banQinWmLdDetail) {
		return super.findList(banQinWmLdDetail);
	}
	
	@Transactional
	public void save(BanQinWmLdDetail banQinWmLdDetail) {
		super.save(banQinWmLdDetail);
	}
	
	@Transactional
	public void delete(BanQinWmLdDetail banQinWmLdDetail) {
		super.delete(banQinWmLdDetail);
	}
    
	public BanQinWmLdDetail findFirst(BanQinWmLdDetail banQinWmLdDetail) {
        List<BanQinWmLdDetail> list = this.findList(banQinWmLdDetail);
        return CollectionUtil.isNotEmpty(list) ? list.get(0) : null;
    }
	
    /**
     * 根据分配明细生成装车明细
     * @param ldNo
     * @param allocIds
     * @throws WarehouseException
     */
    @Transactional
    public void addLdDetailByAllocId(String ldNo, String[] allocIds, String orgId) throws WarehouseException {
        if (StringUtils.isEmpty(ldNo)) {
            // 装车单号不能为空{0}
            throw new WarehouseException("装车单号不能为空");
        }
        BanQinWmLdDetail cond = new BanQinWmLdDetail();
        cond.setOrgId(orgId);
        cond.setLdNo(ldNo);
        List<BanQinWmLdDetail> list = this.findList(cond);
        int count = CollectionUtil.isEmpty(list) ? 0 : list.stream().map(t -> Integer.valueOf(t.getLineNo())).mapToInt(Integer::intValue).max().getAsInt();
        for (String allocId : allocIds) {
            if (allocId != null) {
                count = count + 1;
                String lineNo = wmCommon.getLineNo(count, 5);
                BanQinWmLdDetail model = new BanQinWmLdDetail();
                model.setLdNo(ldNo);
                model.setAllocId(allocId);
                model.setLineNo(lineNo);
                model.setStatus(WmsCodeMaster.LD_NEW.getCode());
                model.setOrgId(orgId);
                this.save(model);
            }
        }
        // 更新装车单状态
        wmLdHeaderService.updateStatus(ldNo, orgId);
        BanQinWmLdDetail condition = new BanQinWmLdDetail();
        condition.setOrgId(orgId);
        condition.setLdNo(ldNo);
        List<BanQinWmLdDetail> detailList = this.findList(condition);
        List<String> allocIdList = detailList.stream().map(BanQinWmLdDetail::getAllocId).collect(Collectors.toList());
        // 交集
        List<String> inAllocIds = Arrays.asList(allocIds);
        List<String> finalAllocIds = inAllocIds.stream().filter(item -> allocIdList.contains(item)).collect(Collectors.toList());

        BanQinWmLdDetailEntity con = new BanQinWmLdDetailEntity();
        con.setOrgId(orgId);
        con.setLdNo(ldNo);
        con.setAllocIds(finalAllocIds);
        List<BanQinWmLdDetailEntity> wmAllocByLdNoItem = mapper.getWmAllocByLdNo(con);
        List<String> soNos = new ArrayList<>();
        for (BanQinWmLdDetailEntity item : wmAllocByLdNoItem) {
            wmSoDetailService.updateLdStatus(item.getSoNo(), item.getLineNo(), orgId);
            if (!soNos.contains(item.getSoNo())) {
                soNos.add(item.getSoNo());
            }
        }
        for (String soNo : soNos) {
            wmSoHeaderService.updateLdStatus(soNo, orgId);
        }
    }

    /**
     * 根据SO单号生成装车明细
     * @param ldNo
     * @param soNos
     * @throws WarehouseException
     */
    @Transactional
    public void addLdDetailBySoNo(String ldNo, String[] soNos, String orgId) throws WarehouseException {
        if (StringUtils.isEmpty(ldNo)) {
            // 装车单号不能为空{0}
            throw new WarehouseException("装车单号不能为空");
        }
        BanQinWmLdDetailEntity condition = new BanQinWmLdDetailEntity();
        condition.setOrgId(orgId);
        condition.setSoNos(Arrays.asList(soNos));
        // 装车单参数：拣货明细在何种状态下可以进行装车（PK: 完全拣货；SP: 完全发运）
        String loadAllowStatus = SysControlParamsUtils.getValue(ControlParamCode.LOAD_ALLOW_STATUS.getCode(), orgId);
        if (WmsCodeMaster.TRAN_PK.getCode().equals(loadAllowStatus)) {
            condition.setStatus(WmsCodeMaster.SO_FULL_PICKING.getCode());
        }
        if (WmsCodeMaster.TRAN_SP.getCode().equals(loadAllowStatus)) {
            condition.setStatus(WmsCodeMaster.SO_FULL_SHIPPING.getCode());
        }
        List<String> items = mapper.getWmAllocIdForLdDetail(condition);
        if (items.size() == 0) {
            // 没有可以操作的分配拣货明细
            throw new WarehouseException("没有可以操作的分配拣货明细");
        }
        String[] allocIds = new String[items.size()];
        int i = -1;
        for (String item : items) {
            i = i + 1;
            allocIds[i] = item;
        }
        addLdDetailByAllocId(ldNo, allocIds, orgId);
    }

    /**
     * 根据包裹生成装车明细
     * @param ldNo
     * @param traceIds
     * @throws WarehouseException
     */
    @Transactional
    public void addLdDetailByTraceId(String ldNo, String[] traceIds, String orgId) throws WarehouseException {
        if (StringUtils.isEmpty(ldNo)) {
            throw new WarehouseException("装车单号不能为空");
        }
        BanQinWmLdDetailEntity condition = new BanQinWmLdDetailEntity();
        condition.setOrgId(orgId);
        condition.setToIds(Arrays.asList(traceIds));
        // 装车单参数：拣货明细在何种状态下可以进行装车（PK: 完全拣货；SP: 完全发运）
        String loadAllowStatus = SysControlParamsUtils.getValue(ControlParamCode.LOAD_ALLOW_STATUS.getCode(), orgId);
        if (WmsCodeMaster.TRAN_PK.getCode().equals(loadAllowStatus)) {
            condition.setStatus(WmsCodeMaster.SO_FULL_PICKING.getCode());
        }
        if (WmsCodeMaster.TRAN_SP.getCode().equals(loadAllowStatus)) {
            condition.setStatus(WmsCodeMaster.SO_FULL_SHIPPING.getCode());
        }
        List<String> items = mapper.getWmAllocIdForLdDetail(condition);
        String[] allocIds = new String[items.size()];
        int i = -1;
        for (String item : items) {
            i = i + 1;
            allocIds[i] = item;
        }
        addLdDetailByAllocId(ldNo, allocIds, orgId);
    }

    /**
     * 根据箱号获取包裹信息
     * @param traceId
     * @param wmLdEntity
     * @return
     * @throws WarehouseException
     */
    public BanQinWmSoAllocEntity getTraceByTraceId(String traceId, BanQinWmLdEntity wmLdEntity) throws WarehouseException {
        BanQinWmSoAllocEntity condition = new BanQinWmSoAllocEntity();
        condition.setOrgId(wmLdEntity.getOrgId());
        condition.setToId(traceId);
        // 扫描包裹页签的系统控制参数PK-SP
        String loadAllowStatus = SysControlParamsUtils.getValue(ControlParamCode.LOAD_ALLOW_STATUS.getCode(), wmLdEntity.getOrgId());
        if (WmsConstants.LOAD_ALLOW_STATUS_SP.equals(loadAllowStatus)) {
            condition.setStatus(WmsCodeMaster.SO_FULL_SHIPPING.getCode());
        } else {
            condition.setStatus(WmsCodeMaster.SO_FULL_PICKING.getCode());
        }
        if (wmLdEntity.getLdType().equals(WmsCodeMaster.LD_BY_CARRIER.getCode())) {
            condition.setCarrierCode(wmLdEntity.getCarrierCode());
        } else if (wmLdEntity.getLdType().equals(WmsCodeMaster.LD_BY_CONSIGNEE.getCode())) {
            condition.setConsigneeCode(wmLdEntity.getConsigneeCode());
            condition.setConsigneeName(wmLdEntity.getConsigneeName());
            condition.setConsigneeTel(wmLdEntity.getConsigneeTel());
            condition.setConsigneeAddr(wmLdEntity.getConsigneeAddr());
            condition.setContactAddr(wmLdEntity.getContactAddr());
            condition.setContactName(wmLdEntity.getContactName());
            condition.setContactTel(wmLdEntity.getContactTel());
        } else if (wmLdEntity.getLdType().equals(WmsCodeMaster.LD_BY_LINE.getCode())) {
            condition.setLine(wmLdEntity.getLine());
        }
        List<BanQinWmSoAllocEntity> item = wmSoAllocService.getWmSoAllocToTraceId(condition);
        if (CollectionUtil.isEmpty(item)) {
            throw new WarehouseException("[" + traceId + "]装箱包裹不存在");
        }
        return item.get(0);
    }

    /**
     * 根据装车单号获取装车订单记录
     * @param ldNo
     * @return
     */
    public List<BanQinWmLdDetailEntity> getSoOrderByLdNo(String ldNo, String orgId) {
        BanQinWmLdDetailEntity condition = new BanQinWmLdDetailEntity();
        condition.setLdNo(ldNo);
        condition.setOrgId(orgId);
        List<BanQinWmLdDetailEntity> result = mapper.getWmLdForSoOrder(condition);
        int i = 1;
        for (BanQinWmLdDetailEntity queryEntity : result) {
            queryEntity.setRowNum(i);
            i++;
        }
        return result;
    }

    /**
     * 根据装车单号获取装车包裹记录
     * @param ldNo
     * @return
     */
    public List<BanQinWmLdDetailEntity> getTraceIdByLdNo(String ldNo, String orgId) {
        BanQinWmLdDetailEntity condition = new BanQinWmLdDetailEntity();
        condition.setLdNo(ldNo);
        condition.setOrgId(orgId);
        List<BanQinWmLdDetailEntity> result = mapper.getWmLdForSoTraceId(condition);
        int i = 1;
        for (BanQinWmLdDetailEntity queryEntity : result) {
            queryEntity.setRowNum(i);
            i++;
        }
        return result;
    }

    /**
     * 根据装车单号获取装车包裹记录
     * @return
     */
    public List<BanQinWmLdDetailEntity> getWmSoAllocToLdDetailQuery(BanQinWmLdDetailEntity entity) {
        return mapper.wmSoAllocToLdDetailQuery(entity);
    }

    /**
     * 根据装车单号和状态获取装车明细实体对象
     * @param ldNo
     * @param status
     * @return
     */
    public List<BanQinWmLdDetailEntity> getEntityByLdNoAndStatus(String ldNo, String status, String orgId) {
        BanQinWmLdDetailEntity condition = new BanQinWmLdDetailEntity();
        condition.setLdNo(ldNo);
        condition.setOrgId(orgId);
        condition.setStatus(status);
        return mapper.findEntity(condition);
    }

    /**
     * 根据装车单号获取要发运的装车分配明细实体对象
     * @param ldNos
     * @return
     * @throws WarehouseException
     */
    public List<BanQinWmSoAllocEntity> getWmSoAllocEntityByLdNo(String[] ldNos, String orgId) throws WarehouseException {
        List<BanQinWmSoAllocEntity> dEntitys = Lists.newArrayList();
        for (String ldNo : ldNos) {
            BanQinWmLdHeader model = wmLdHeaderService.getByLdNo(ldNo, orgId);
            if (WmsCodeMaster.LD_FULL_LOAD.getCode().equals(model.getStatus())) {
                BanQinWmLdDetailEntity condition = new BanQinWmLdDetailEntity();
                condition.setLdNo(ldNo);
                condition.setOrgId(orgId);
                List<BanQinWmLdDetailEntity> items = mapper.getWmSoAllocByLdNo(condition);
                for (BanQinWmLdDetailEntity item : items) {
                    BanQinWmSoAllocEntity entity = new BanQinWmSoAllocEntity();
                    entity.setAllocId(item.getAllocId());
                    entity.setPackCode(item.getPackCode());
                    entity.setSoNo(item.getSoNo());
                    entity.setLineNo(item.getLineNo());
                    entity.setPackCode(item.getPackCode());
                    entity.setQtyEa(item.getQtyEa());
                    entity.setQtyUom(item.getQtyUom());
                    entity.setOwnerCode(item.getOwnerCode());
                    entity.setUom(item.getUom());
                    entity.setLotNum(item.getLotNum());
                    entity.setToLoc(item.getToLoc());
                    entity.setToId(item.getToId());
                    entity.setSkuCode(item.getSkuCode());
                    entity.setWaveNo(item.getWaveNo());
                    entity.setCheckStatus(item.getCheckStatus());
                    entity.setIsSerial(item.getIsSerial());
                    entity.setOrgId(orgId);
                    dEntitys.add(entity);
                }
            } else {
                // 不是完全装车状态，不能操作{0}
                throw new WarehouseException(ldNo + "不是完全装车状态，不能操作");
            }
        }
        return dEntitys;
    }

    /**
     * 根据装车单号和状态获取装车明细
     * @param ldNo
     * @param status
     * @return
     */
    public List<BanQinWmLdDetail> getByLdNoAndStatus(String ldNo, String status, String orgId) {
        BanQinWmLdDetail model = new BanQinWmLdDetail();
        model.setLdNo(ldNo);
        model.setStatus(status);
        model.setOrgId(orgId);
        return this.findList(model);
    }

    /**
     * 根据装车单号和行号获取装车明细
     * @param ldNo
     * @param lineNo
     * @return
     * @throws WarehouseException
     */
    public BanQinWmLdDetail getByLdNoAndLineNo(String ldNo, String lineNo, String orgId) throws WarehouseException {
        BanQinWmLdDetail model = new BanQinWmLdDetail();
        model.setLdNo(ldNo);
        model.setLineNo(lineNo);
        model.setOrgId(orgId);
        model = this.findFirst(model);
        if (model == null) {
            // 查找不到装车单明细{0}{1}
            throw new WarehouseException("查找不到装车单明细", ldNo, lineNo);
        }
        return model;
    }

    /**
     * 根据装车单号和行号取消装载确认
     * @param ldNo
     * @param lineNos
     * @throws WarehouseException
     */
    @Transactional
    public void cancelByLdNoAndLineNo(String ldNo, String[] lineNos, String orgId) throws WarehouseException {
        for (String lineNo : lineNos) {
            BanQinWmLdDetail model = getByLdNoAndLineNo(ldNo, lineNo, orgId);
            if (model != null) {
                String status = model.getStatus();
                if (WmsCodeMaster.LD_FULL_LOAD.getCode().equals(status)) {
                    model.setStatus(WmsCodeMaster.LD_NEW.getCode());
                    model.setLdOp(null);
                    model.setLdTime(null);
                    this.save(model);
                    // 更新装车单状态
                    wmLdHeaderService.updateStatus(ldNo, orgId);
                    BanQinWmSoAlloc soAlloc = wmSoAllocService.getByAllocId(model.getAllocId(), orgId);
                    wmSoDetailService.updateLdStatus(soAlloc.getSoNo(), soAlloc.getLineNo(), orgId);
                    wmSoHeaderService.updateLdStatus(soAlloc.getSoNo(), orgId);
                } else {
                    // 不是装车完成状态，不能操作！{0}
                    throw new WarehouseException("不是装车完成状态，不能操作" + lineNo);
                }
            }
        }
    }

    /**
     * 根据装车单号和行号删除装载明细
     * @param ldNo
     * @param lineNos
     * @throws WarehouseException
     */
    @Transactional
    public void removeByLdNoAndLineNo(String ldNo, String[] lineNos, String orgId) throws WarehouseException {
        for (String lineNo : lineNos) {
            BanQinWmLdDetail model = getByLdNoAndLineNo(ldNo, lineNo, orgId);
            if (model != null) {
                String status = model.getStatus();
                if (WmsCodeMaster.LD_NEW.getCode().equals(status)) {
                    delete(model);
                    // 更新装车单状态
                    wmLdHeaderService.updateStatus(ldNo, orgId);
                    BanQinWmSoAlloc soAlloc = wmSoAllocService.getByAllocId(model.getAllocId(), orgId);
                    wmSoDetailService.updateLdStatus(soAlloc.getSoNo(), soAlloc.getLineNo(), orgId);
                    wmSoHeaderService.updateLdStatus(soAlloc.getSoNo(), orgId);
                } else {
                    throw new WarehouseException("不是新建状态", model.getAllocId());
                }
            }
        }
    }

    /**
     * 根据装车单号和SO号删除装载明细
     * @param ldNo
     * @param soNos
     * @throws WarehouseException
     */
    @Transactional
    public void removeByLdNoAndSoNo(String ldNo, String[] soNos, String orgId) throws WarehouseException {
        String str = "";
        // 批量校验提取到循环外
        // 批量删除
        List<BanQinWmLdDetailEntity> ldDetailEntitys = this.getEntityByLdNoAndStatusAndSoNos(ldNo, WmsCodeMaster.LD_NEW.getCode(), soNos, orgId);
        if (ldDetailEntitys.size() > 0) {
            for (BanQinWmLdDetailEntity entity : ldDetailEntitys) {
                this.delete(new BanQinWmLdDetail(entity.getId()));
            }
            // 更新装车单状态
            wmLdHeaderService.updateStatus(ldNo, orgId);
            for (String soNo : soNos) {
                BanQinWmLdDetailEntity condition = new BanQinWmLdDetailEntity();
                condition.setOrgId(orgId);
                condition.setLdNo(ldNo);
                condition.setSoNo(soNo);
                List<BanQinWmLdDetailEntity> items = mapper.getWmAllocByLdNo(condition);
                for (BanQinWmLdDetailEntity item : items) {
                    wmSoDetailService.updateLdStatus(item.getSoNo(), item.getLineNo(), orgId);
                }
                wmSoHeaderService.updateLdStatus(soNo, orgId);
            }
        }
    }

    /**
     * 根据装车单号和TraceId箱号删除装载明细
     * @param ldNo
     * @param traceIds
     * @throws WarehouseException
     */
    @Transactional
    public void removeByLdNoAndTraceId(String ldNo, String[] traceIds, String orgId) throws WarehouseException {
        String str = "";
        // 批量校验提取到事务外
        // 批量删除
        List<BanQinWmLdDetailEntity> ldDetailEntitys = this.getEntityByLdNoAndStatusAndToIds(ldNo, WmsCodeMaster.LD_NEW.getCode(), traceIds, orgId);
        if (ldDetailEntitys.size() > 0) {
            for (BanQinWmLdDetailEntity entity : ldDetailEntitys) {
                this.delete(new BanQinWmLdDetail(entity.getId()));
            }
            for (String traceId : traceIds) {
                // 更新装车单状态
                wmLdHeaderService.updateStatus(ldNo, orgId);
                BanQinWmLdDetailEntity condition = new BanQinWmLdDetailEntity();
                condition.setOrgId(orgId);
                condition.setLdNo(ldNo);
                condition.setToId(traceId);
                List<BanQinWmLdDetailEntity> items = mapper.getWmAllocByLdNo(condition);
                List<String> soNos = new ArrayList<>();
                for (BanQinWmLdDetailEntity item : items) {
                    wmSoDetailService.updateLdStatus(item.getSoNo(), item.getLineNo(), orgId);
                    if (!soNos.contains(item.getSoNo())) {
                        soNos.add(item.getSoNo());
                    }
                }
                for (String soNo : soNos) {
                    wmSoHeaderService.updateLdStatus(soNo, orgId);
                }
            }
        }
    }

    /**
     * 根据目标箱号校验是否生成装车单
     * @param toId
     * @return
     */
    public Boolean CheckIsGeneratorLdByToId(String toId, String orgId) {
        BanQinWmLdDetailEntity condition = new BanQinWmLdDetailEntity();
        condition.setToId(toId);
        condition.setOrgId(orgId);
        Integer count = mapper.isGeneratorLd(condition);
        if (null != count && count > 0) {
            return true;
        }
        return false;
    }

    /**
     * 根据分配ID校验是否生成装车单
     * @param allocId
     * @return
     */
    public Boolean CheckIsGeneratorLdByAllocId(String allocId, String orgId) {
        BanQinWmLdDetailEntity condition = new BanQinWmLdDetailEntity();
        condition.setAllocId(allocId);
        condition.setOrgId(orgId);
        Integer count = mapper.isGeneratorLd(condition);
        if (null != count && count > 0) {
            return true;
        }
        return false;
    }

    /**
     * 根据出库单号校验是否生成装车单
     * @param soNo
     * @return
     */
    public Boolean CheckIsGeneratorLdBySoNo(String soNo, String orgId) {
        BanQinWmLdDetailEntity condition = new BanQinWmLdDetailEntity();
        condition.setSoNo(soNo);
        condition.setOrgId(orgId);
        Integer count = mapper.isGeneratorLd(condition);
        if (null != count && count > 0) {
            return true;
        }
        return false;
    }

    /**
     * 根据soNos和装车单状态，获取生成装车单的soNos
     * @param soNos
     * @param status
     * @param orgId
     * @return
     */
    public List<String> checkIsGenLdBySoNos(List<String> soNos, String status, String orgId) {
        List<String> rsSoNos = new ArrayList<>();
        if (soNos.size() == 0) {
            return rsSoNos;
        }
        BanQinWmLdDetailEntity condition = new BanQinWmLdDetailEntity();
        condition.setOrgId(orgId);
        condition.setStatus(status);
        condition.setSoNos(soNos);
        
        return mapper.checkIsGenLdBySoNos(condition);
    }

    /**
     * 根据跟踪号TOID和装车状态获取已经生成装车单分配明细
     * @param ldNo
     * @param toIds
     * @param status
     * @param orgId
     * @return
     */
    public List<String> checkIsGenLdByToIds(String ldNo, List<String> toIds, String status, String orgId) {
        List<String> rsToIds = new ArrayList<>();
        if (toIds.size() == 0) {
            return rsToIds;
        }
        BanQinWmLdDetailEntity condition = new BanQinWmLdDetailEntity();
        condition.setLdNo(ldNo);
        condition.setOrgId(orgId);
        condition.setStatus(status);
        condition.setToIds(toIds);
        return mapper.checkIsGenLdByToIds(condition);
    }

    /**
     * 根据分配ID和装车状态获取已经生成装车单分配明细
     * @param allocIds
     * @param status
     * @return
     */
    public List<String> checkIsGenLdByAllocIds(List<String> allocIds, String status, String orgId) {
        List<String> rsAllocIds = new ArrayList<>();
        if (allocIds.size() == 0) {
            return rsAllocIds;
        }
        BanQinWmLdDetailEntity condition = new BanQinWmLdDetailEntity();
        condition.setOrgId(orgId);
        condition.setStatus(status);
        condition.setAllocIds(allocIds);
        
        return mapper.checkIsGenLdByAllocIds(condition);
    }

    /**
     * 根据出库单号校验是否生成装车单,并且是否全部完全交接
     * @param soNo
     * @return
     */
    public Boolean CheckIsFullDeliveryBySoNo(String soNo, String orgId) {
        BanQinWmLdDetailEntity condition = new BanQinWmLdDetailEntity();
        condition.setSoNo(soNo);
        condition.setOrgId(orgId);
        condition.setIsFullDelivery(WmsConstants.YES);// 完全交接
        Integer count = mapper.isGeneratorLd(condition);
        if (null != count && count > 0) {
            return true;
        }
        return false;

    }

    /**
     * 根据入库单号、收货行号校验是否生成装车单(越库)
     * @param asnNo
     * @param rcvLineNo
     * @return
     */
    public Boolean checkIsGeneratorLdByCd(String asnNo, String rcvLineNo, String orgId) {
        BanQinWmLdDetailEntity condition = new BanQinWmLdDetailEntity();
        condition.setAsnNo(asnNo);
        condition.setRcvLineNo(rcvLineNo);
        condition.setOrgId(orgId);
        Integer count = mapper.isGeneratorLd(condition);
        if (null != count && count > 0) {
            return true;
        }
        return false;
    }

    /**
     * 根据装车单号和状态、出库单号 获取装车明细实体对象
     * @param ldNo
     * @param status
     * @param soNos
     * @return
     */
    public List<BanQinWmLdDetailEntity> getEntityByLdNoAndStatusAndSoNos(String ldNo, String status, String[] soNos, String orgId) {
        BanQinWmLdDetailEntity condition = new BanQinWmLdDetailEntity();
        condition.setLdNo(ldNo);
        condition.setOrgId(orgId);
        condition.setStatus(status);
        condition.setSoNos(Arrays.asList(soNos));
        
        return mapper.findEntity(condition);
    }

    /**
     * 根据装车单号和状态、目标跟踪号(包裹) 获取装车明细实体对象
     * @param ldNo
     * @param status
     * @param toIds
     * @return
     */
    public List<BanQinWmLdDetailEntity> getEntityByLdNoAndStatusAndToIds(String ldNo, String status, String[] toIds, String orgId) {
        BanQinWmLdDetailEntity condition = new BanQinWmLdDetailEntity();
        condition.setLdNo(ldNo);
        condition.setOrgId(orgId);
        condition.setStatus(status);
        condition.setToIds(Arrays.asList(toIds));
        
        return mapper.findEntity(condition);
    }

    public List<BanQinWmSoAllocToSoOrderQueryEntity> wmSoAllocToSoOrderQuery(BanQinWmSoAllocToSoOrderQueryEntity entity) {
        return mapper.wmSoAllocToSoOrderQuery(entity);
    }

}