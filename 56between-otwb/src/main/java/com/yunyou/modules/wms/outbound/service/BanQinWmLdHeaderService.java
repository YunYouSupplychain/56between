package com.yunyou.modules.wms.outbound.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.config.GlobalDataRule;
import com.yunyou.common.utils.IdGen;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.sys.GenNoType;
import com.yunyou.modules.sys.service.SynchronizedNoService;
import com.yunyou.modules.sys.utils.SysControlParamsUtils;
import com.yunyou.modules.wms.common.entity.ControlParamCode;
import com.yunyou.modules.wms.common.entity.WarehouseException;
import com.yunyou.modules.wms.common.entity.WmsCodeMaster;
import com.yunyou.modules.wms.common.entity.WmsConstants;
import com.yunyou.modules.wms.common.service.BanQinWmsCommonService;
import com.yunyou.modules.wms.outbound.entity.*;
import com.yunyou.modules.wms.outbound.entity.extend.PrintLdOrder;
import com.yunyou.modules.wms.outbound.mapper.BanQinWmLdDetailMapper;
import com.yunyou.modules.wms.outbound.mapper.BanQinWmLdHeaderMapper;
import com.yunyou.modules.wms.report.entity.WmStoreCheckAcceptOrderLabel;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 装车单Service
 *
 * @author WMJ
 * @version 2019-02-20
 */
@Service
@Transactional(readOnly = true)
public class BanQinWmLdHeaderService extends CrudService<BanQinWmLdHeaderMapper, BanQinWmLdHeader> {
    @Autowired
    protected BanQinWmLdDetailService wmLdDetailService;
    @Autowired
    private BanQinWmLdDetailMapper wmLdDetailMapper;
    @Autowired
    protected BanQinWmSoHeaderService wmSoHeaderService;
    @Autowired
    protected BanQinWmSoDetailService wmSoDetailService;
    @Autowired
    protected BanQinWmsCommonService wmCommon;
    @Autowired
    private SynchronizedNoService noService;

    public BanQinWmLdEntity getEntity(String id) {
        return mapper.getEntity(id);
    }

    public Page<BanQinWmLdEntity> findPage(Page page, BanQinWmLdEntity entity) {
        entity.setDataScope(GlobalDataRule.getDataRuleSql("a.org_id", entity.getOrgId()));
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    public BanQinWmLdHeader findFirst(BanQinWmLdHeader banQinWmLdHeader) {
        List<BanQinWmLdHeader> list = this.findList(banQinWmLdHeader);
        return CollectionUtil.isNotEmpty(list) ? list.get(0) : null;
    }

    public List<BanQinWmLdEntity> findEntity(BanQinWmLdEntity banQinWmLdEntity) {
        return mapper.findEntity(banQinWmLdEntity);
    }

    /**
     * 根据装车单号删除装车单
     *
     * @param ldNos 装车单号
     */
    @Transactional
    public void removeByLdNo(String[] ldNos, String orgId) throws WarehouseException {
        StringBuilder errors = new StringBuilder();
        for (String ldNo : ldNos) {
            BanQinWmLdHeader model = this.getByLdNo(ldNo, orgId);
            if (model == null) {
                // 查找不到装车单{0}
                throw new WarehouseException("查找不到装车单" + ldNo);
            }
            String status = model.getStatus();
            if (WmsCodeMaster.LD_NEW.getCode().equals(status)) {
                // 修改出库单状态更新：装车单删除时，出库单如果无生成任何装车单，那么出库单中的装车单状态清空
                // 查询装车单明细相关的订单明细
                BanQinWmLdDetailEntity condition = new BanQinWmLdDetailEntity();
                condition.setOrgId(orgId);
                condition.setLdNo(ldNo);
                List<BanQinWmLdDetailEntity> items = wmLdDetailMapper.getWmAllocByLdNo(condition);
                List<String> soNos = new ArrayList<>();
                // 删除装车单明细
                wmLdDetailService.deleteAll(wmLdDetailService.getByLdNoAndStatus(ldNo, null, orgId));
                // 循环更新订单明细状态
                for (BanQinWmLdDetailEntity item : items) {
                    wmSoDetailService.updateLdStatus(item.getSoNo(), item.getLineNo(), orgId);
                    if (!soNos.contains(item.getSoNo())) {
                        soNos.add(item.getSoNo());
                    }
                }
                // 循环更新订单状态
                for (String soNo : soNos) {
                    wmSoHeaderService.updateLdStatus(soNo, orgId);
                }
                // 删除装车单表
                this.delete(model);
            } else {
                errors.append("不是创建状态，不能操作");
            }
        }
        if (errors.length() > 0) {
            throw new WarehouseException(errors.toString());
        }
    }

    /**
     * 根据装车单号获取装车单
     *
     * @param ldNo  装车单号
     * @param orgId 机构ID
     */
    public BanQinWmLdHeader getByLdNo(String ldNo, String orgId) {
        BanQinWmLdHeader model = new BanQinWmLdHeader();
        model.setLdNo(ldNo);
        model.setOrgId(orgId);
        return findFirst(model);
    }

    /**
     * 根据装车单号获取装车实体对象
     *
     * @param ldNo  装车单号
     * @param orgId 机构ID
     */
    public BanQinWmLdEntity getEntityByLdNo(String ldNo, String orgId) {
        BanQinWmLdEntity ldEntity = new BanQinWmLdEntity();
        BanQinWmLdEntity condition = new BanQinWmLdEntity();
        condition.setOrgId(orgId);
        condition.setLdNo(ldNo);
        List<BanQinWmLdEntity> item = mapper.findEntity(condition);
        if (item != null) {
            ldEntity = item.get(0);
            /* 装车订单记录 */
            List<BanQinWmLdDetailEntity> soOrders = wmLdDetailService.getSoOrderByLdNo(ldNo, orgId);
            /* 装车包裹记录 */
            List<BanQinWmLdDetailEntity> traceIds = wmLdDetailService.getTraceIdByLdNo(ldNo, orgId);
            /* 已经装车记录 */
            List<BanQinWmLdDetailEntity> ldDetail40Entitys = wmLdDetailService.getEntityByLdNoAndStatus(ldNo, WmsCodeMaster.LD_FULL_LOAD.getCode(), orgId);
            if (WmsCodeMaster.LD_FULL_DELIVERY.getCode().equals(ldEntity.getStatus()) || WmsCodeMaster.LD_CLOSE.getCode().equals(ldEntity.getStatus())) {
                ldDetail40Entitys = wmLdDetailService.getEntityByLdNoAndStatus(ldNo, WmsCodeMaster.LD_FULL_DELIVERY.getCode(), orgId);
            }
            /* 未经装车记录 */
            List<BanQinWmLdDetailEntity> ldDetail10Entitys = wmLdDetailService.getEntityByLdNoAndStatus(ldNo, WmsCodeMaster.LD_NEW.getCode(), orgId);
            ldEntity.setLdDetail10Entity(ldDetail10Entitys);
            ldEntity.setLdDetail40Entity(ldDetail40Entitys);
            ldEntity.setTraceIdQueryItem(traceIds);
            ldEntity.setSoOrderQueryItem(soOrders);
        }
        return ldEntity;
    }

    /**
     * 保存装车单实体
     *
     * @param wmLdEntity 装车单实体
     */
    @Transactional
    public BanQinWmLdHeader saveWmLdEntity(BanQinWmLdEntity wmLdEntity) {
        BanQinWmLdHeader model = new BanQinWmLdHeader();
        BeanUtils.copyProperties(wmLdEntity, model);
        if (StringUtils.isEmpty(model.getLdNo())) {
            model.setLdNo(noService.getDocumentNo(GenNoType.WM_LD_NO.name()));
            model.setStatus(WmsCodeMaster.LD_NEW.getCode());
            model.setDef1(WmsConstants.NO);
            model.setId(IdGen.uuid());
            model.setIsNewRecord(true);
        }
        this.save(model);
        return model;
    }

    /**
     * 创建新装车单
     *
     * @param wmLdEntity 装车单实体
     * @param ldDetails  装车单明细
     */
    @Transactional
    public BanQinWmLdEntity createNewWmLdEntity(BanQinWmLdEntity wmLdEntity, List<BanQinWmLdDetailEntity> ldDetails) throws WarehouseException {
        BanQinWmLdHeader model = new BanQinWmLdHeader();
        BeanUtils.copyProperties(wmLdEntity, model);
        model.setLdNo(noService.getDocumentNo(GenNoType.WM_LD_NO.name()));
        model.setStatus(WmsCodeMaster.LD_NEW.getCode());
        model.setId(IdGen.uuid());
        this.save(model);
        String[] allocIds = new String[ldDetails.size()];
        String[] lineNos = new String[ldDetails.size()];
        int i = 0;
        for (BanQinWmLdDetailEntity detail : ldDetails) {
            String allocId = detail.getAllocId();
            String lineNo = detail.getLineNo();
            allocIds[i] = allocId;
            lineNos[i] = lineNo;
            i++;
        }
        this.wmLdDetailService.removeByLdNoAndLineNo(wmLdEntity.getLdNo(), lineNos, wmLdEntity.getOrgId());
        wmLdDetailService.addLdDetailByAllocId(model.getLdNo(), allocIds, wmLdEntity.getOrgId());
        return this.getEntityByLdNo(model.getLdNo(), wmLdEntity.getOrgId());
    }

    /**
     * 根据装车单号交接确认
     *
     * @param ldNo 装车单号
     */
    @Transactional
    public void deliveryByLdNo(String ldNo, String orgId) throws WarehouseException {
        BanQinWmLdHeader model = this.getByLdNo(ldNo, orgId);
        if (!(WmsCodeMaster.LD_FULL_LOAD.getCode().equals(model.getStatus()) && WmsCodeMaster.ALLOC_FULL_SHIPPING.getCode().equals(model.getSoStatus()))) {
            // 不是完全装车状态，不能操作{0}
            throw new WarehouseException(ldNo + "不是完全装车和完全发运状态，不能操作");
        }
        List<BanQinWmLdDetail> details = wmLdDetailService.getByLdNoAndStatus(ldNo, null, orgId);
        for (BanQinWmLdDetail detail : details) {
            detail.setStatus(WmsCodeMaster.LD_FULL_DELIVERY.getCode());
            wmLdDetailService.save(detail);
        }
        model.setDeliverTime(new Date());
        model.setStatus(WmsCodeMaster.LD_FULL_DELIVERY.getCode());
        this.save(model);
        // 更新状态
        BanQinWmLdDetailEntity condition = new BanQinWmLdDetailEntity();
        condition.setOrgId(orgId);
        condition.setLdNo(ldNo);
        List<BanQinWmLdDetailEntity> items = wmLdDetailMapper.getWmAllocByLdNo(condition);
        List<String> soNos = Lists.newArrayList();
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

    /**
     * 更新装车单状态
     *
     * @param ldNo 装车单号
     */
    @Transactional
    public void updateStatus(String ldNo, String orgId) {
        String ldStatus = null;
        List<BanQinWmLdDetail> details = wmLdDetailService.getByLdNoAndStatus(ldNo, null, orgId);
        List<String> isNew = details.stream().map(BanQinWmLdDetail::getStatus).filter(s -> WmsCodeMaster.LD_NEW.getCode().equals(s)).collect(Collectors.toList());
        List<String> isFullLoad = details.stream().map(BanQinWmLdDetail::getStatus).filter(s -> WmsCodeMaster.LD_FULL_LOAD.getCode().equals(s)).collect(Collectors.toList());
        List<String> isPartLoad = details.stream().map(BanQinWmLdDetail::getStatus).filter(s -> WmsCodeMaster.LD_PART_LOAD.getCode().equals(s)).collect(Collectors.toList());
        List<String> isPartDelivery = details.stream().map(BanQinWmLdDetail::getStatus).filter(s -> WmsCodeMaster.LD_FULL_DELIVERY.getCode().equals(s)).collect(Collectors.toList());
        List<String> isFullDelivery = details.stream().map(BanQinWmLdDetail::getStatus).filter(s -> WmsCodeMaster.LD_PART_DELIVERY.getCode().equals(s)).collect(Collectors.toList());
        if (details.size() == 0) {
            ldStatus = WmsCodeMaster.LD_NEW.getCode();
        } else if (isNew.size() > 0 && isFullLoad.size() == 0 && isPartLoad.size() == 0 && isFullDelivery.size() == 0 && isPartDelivery.size() == 0) {
            ldStatus = WmsCodeMaster.LD_NEW.getCode();
        } else if (isPartLoad.size() > 0 || (isFullLoad.size() > 0 && isFullLoad.size() != details.size() && isFullDelivery.size() == 0 && isPartDelivery.size() == 0)) {
            ldStatus = WmsCodeMaster.LD_PART_LOAD.getCode();
        } else if (details.size() == isFullLoad.size()) {
            ldStatus = WmsCodeMaster.LD_FULL_LOAD.getCode();
        } else if (isPartDelivery.size() > 0 || (isFullDelivery.size() > 0 && details.size() != isFullDelivery.size())) {
            ldStatus = WmsCodeMaster.LD_PART_DELIVERY.getCode();
        } else if (details.size() == isFullDelivery.size()) {
            ldStatus = WmsCodeMaster.LD_FULL_DELIVERY.getCode();
        }
        if (ldStatus != null) {
            BanQinWmLdHeader model = getByLdNo(ldNo, orgId);
            model.setStatus(ldStatus);
            this.save(model);
        }
    }

    /**
     * 根据装车单号获取
     *
     * @param ldNo 装车单号
     */
    public BanQinWmLdEntity getWmLdHeaderEntityQueryItemByLdNo(String ldNo, String orgId) {
        BanQinWmLdEntity condition = new BanQinWmLdEntity();
        condition.setLdNo(ldNo);
        condition.setOrgId(orgId);
        List<BanQinWmLdEntity> items = mapper.findEntity(condition);
        if (null != items) {
            return items.get(0);
        } else {
            return null;
        }
    }

    /**
     * 发运订单界面，勾选记录生成装车单
     *
     * @param soNos 出库单号
     */
    @Transactional
    public String generateLdEntity(String[] soNos, String orgId) throws WarehouseException {
        // 新增保存装车单
        BanQinWmLdHeader model = new BanQinWmLdHeader();
        String ldNo = noService.getDocumentNo(GenNoType.WM_LD_NO.name());
        model.setLdNo(ldNo);// 装车单号
        model.setLdType(WmsCodeMaster.LD_BY_FREE.getCode());// 类型：无限制
        model.setStatus(WmsCodeMaster.LD_NEW.getCode());// 状态-创建
        model.setOrgId(orgId);
        // 控制参数
        // 装车单参数：拣货明细在何种状态下可以进行装车（PK: 完全拣货；SP: 完全发运）
        String loadAllowStatus = SysControlParamsUtils.getValue(ControlParamCode.LOAD_ALLOW_STATUS.getCode(), orgId);
        if (WmsConstants.LOAD_ALLOW_STATUS_PK.equals(loadAllowStatus)) {
            model.setSoStatus(WmsCodeMaster.SO_FULL_PICKING.getCode());
        } else if (WmsConstants.LOAD_ALLOW_STATUS_SP.equals(loadAllowStatus)) {
            model.setSoStatus(WmsCodeMaster.SO_FULL_SHIPPING.getCode());
        }
        this.save(model);
        // 根据出库单号添加装车单明细
        this.wmLdDetailService.addLdDetailBySoNo(ldNo, soNos, orgId);
        return ldNo;
    }

    /**
     * 根据装车单号取消交接
     *
     * @param ldNo  装车单号
     * @param orgId 机构ID
     */
    @Transactional
    public void cancelDeliveryByLdNo(String ldNo, String orgId) throws WarehouseException {
        BanQinWmLdHeader model = this.getByLdNo(ldNo, orgId);
        if (!WmsCodeMaster.LD_FULL_DELIVERY.getCode().equals(model.getStatus())) {
            throw new WarehouseException(ldNo + "不是完全交接状态，不能操作");
        }
        // 校验是否存在已关闭的SO单
        List<BanQinWmSoHeader> checkItems = wmSoHeaderService.checkSoIsClose(ldNo, orgId);
        if (checkItems.size() > 0) {
            throw new WarehouseException(ldNo + "存在已关闭的发运订单，不能操作");
        }
        List<BanQinWmLdDetail> details = wmLdDetailService.getByLdNoAndStatus(ldNo, null, orgId);
        for (BanQinWmLdDetail detail : details) {
            detail.setStatus(WmsCodeMaster.LD_FULL_LOAD.getCode());// 完全装车
            wmLdDetailService.save(detail);
        }
        model.setStatus(WmsCodeMaster.LD_FULL_LOAD.getCode());
        this.save(model);
        // 更新状态
        BanQinWmLdDetailEntity condition = new BanQinWmLdDetailEntity();
        condition.setOrgId(orgId);
        condition.setLdNo(ldNo);
        List<BanQinWmLdDetailEntity> items = wmLdDetailMapper.getWmAllocByLdNo(condition);
        List<String> soNos = new ArrayList<>();
        for (BanQinWmLdDetailEntity item : items) {
            wmSoDetailService.updateLdStatus(item.getSoNo(), item.getLineNo(), item.getOrgId());
            if (!soNos.contains(item.getSoNo())) {
                soNos.add(item.getSoNo());
            }
        }
        for (String soNo : soNos) {
            wmSoHeaderService.updateLdStatus(soNo, orgId);
        }
    }

    /**
     * 装车单关闭
     *
     * @param ldNo  装车单号
     * @param orgId 机构ID
     */
    @Transactional
    public ResultMessage closeLdOrder(String ldNo, String orgId) throws WarehouseException {
        ResultMessage msg = new ResultMessage();
        // 获取装车单
        BanQinWmLdHeader model = this.getByLdNo(ldNo, orgId);
        // 只有状态为完全交接才能关闭
        if (model.getStatus().equals(WmsCodeMaster.LD_FULL_DELIVERY.getCode())) {
            // 状态更新close
            model.setStatus(WmsCodeMaster.LD_CLOSE.getCode());
            // 保存
            this.save(model);
        } else {
            // {0}非完全交接状态，不能操作"
            throw new WarehouseException(ldNo + "非完全交接状态，不能操作");
        }
        // 返回信息，数据
        msg.setSuccess(true);
        return msg;
    }

    /**
     * 门店验收单报表数据
     */
    public List<WmStoreCheckAcceptOrderLabel> getStoreCheckAcceptOrder(List<String> ldOrderIds) {
        return mapper.getStoreCheckAcceptOrder(ldOrderIds);
    }

    public List<PrintLdOrder> getLdOrder(String[] ids) {
        return mapper.getLdOrder(ids);
    }
}