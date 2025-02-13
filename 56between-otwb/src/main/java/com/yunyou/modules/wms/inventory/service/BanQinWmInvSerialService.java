package com.yunyou.modules.wms.inventory.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.config.GlobalDataRule;
import com.yunyou.common.utils.IdGen;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.sys.GenNoType;
import com.yunyou.modules.sys.service.SynchronizedNoService;
import com.yunyou.modules.sys.utils.UserUtils;
import com.yunyou.modules.wms.common.entity.WarehouseException;
import com.yunyou.modules.wms.common.entity.WmsCodeMaster;
import com.yunyou.modules.wms.common.entity.WmsConstants;
import com.yunyou.modules.wms.inbound.entity.BanQinWmAsnSerial;
import com.yunyou.modules.wms.inventory.entity.*;
import com.yunyou.modules.wms.inventory.mapper.BanQinWmInvSerialMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 序列号库存表Service
 * @author WMJ
 * @version 2019-01-25
 */
@Service
@Transactional(readOnly = true)
public class BanQinWmInvSerialService extends CrudService<BanQinWmInvSerialMapper, BanQinWmInvSerial> {
    @Autowired
    protected BanQinWmAdSerialService wmAdSerialService;
    @Autowired
    protected BanQinWmAdDetailService wmAdDetailService;
    @Autowired
    protected BanQinWmTfSerialService wmTfSerialService;
    @Autowired
    protected BanQinWmTfDetailService wmTfDetailService;
    @Autowired
    private SynchronizedNoService noService;
    @Autowired
    private BanQinWmActTranSerialService banQinWmActTranSerialService;

	public Page<BanQinWmInvSerialEntity> findPage(Page page, BanQinWmInvSerialEntity entity) {
        entity.setDataScope(GlobalDataRule.getDataRuleSql("a.org_id", entity.getOrgId()));
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
		return page;
	}

	public BanQinWmInvSerial findFirst(BanQinWmInvSerial banQinWmInvSerial) {
        List<BanQinWmInvSerial> list = this.findList(banQinWmInvSerial);
        return CollectionUtil.isNotEmpty(list) ? list.get(0) : null;
    }
    
    public List<BanQinWmInvSerial> countSerialQuery(BanQinCountSerialQuery banQinWmInvSerial) {
	    return mapper.countSerialQuery(banQinWmInvSerial);
    }

    /**
     * 保存序列号
     * @param model
     * @throws WarehouseException
     */
    @Transactional
    public void saveInvSerial(BanQinWmInvSerial model) throws WarehouseException {
        // 校验是否已存在
        BanQinWmInvSerial check = getByOwnerCodeAndSkuCodeAndSerialNo(model.getOwnerCode(), model.getSkuCode(), model.getSerialNo(), model.getOrgId());
        if (null != check) {
            throw new WarehouseException("序列号已收货，不能操作"); 
        }
        save(model);
    }

    /**
     * 按货主、商品、序列号获取库存序列号
     * @param ownerCode
     * @param skuCode
     * @param serialNo
     * @return
     */
    public BanQinWmInvSerial getByOwnerCodeAndSkuCodeAndSerialNo(String ownerCode, String skuCode, String serialNo, String orgId) {
        BanQinWmInvSerial model = new BanQinWmInvSerial();
        model.setOwnerCode(ownerCode);
        model.setSkuCode(skuCode);
        model.setSerialNo(serialNo);
        model.setOrgId(orgId);
        return this.findFirst(model);
    }

    /**
     * 按货主、商品、批次、序列号获取库存序列号
     * @param ownerCode
     * @param skuCode
     * @param lotNum
     * @param serialNo
     * @return
     */
    public BanQinWmInvSerial getByOwnerAndSkuAndLotAndSerialNo(String ownerCode, String skuCode, String lotNum, String serialNo, String orgId) {
        BanQinWmInvSerial model = new BanQinWmInvSerial();
        model.setOwnerCode(ownerCode);
        model.setSkuCode(skuCode);
        model.setLotNum(lotNum);
        model.setSerialNo(serialNo);
        model.setOrgId(orgId);
        return this.findFirst(model);
    }

    /**
     * 按单号和收货行号查询
     * @param asnNo
     * @param lineNo
     * @return
     */
    public List<BanQinWmInvSerial> getByAsnNoAndRcvLineNo(String asnNo, String lineNo, String orgId) {
        BanQinWmAsnSerial condition = new BanQinWmAsnSerial();
        condition.setAsnNo(asnNo);
        condition.setRcvLineNo(lineNo);
        condition.setOrgId(orgId);
        return mapper.getByAsnNoAndLineNo(condition);
    }

    /**
     * 按单号和收货行号删除
     * @param asnNo
     * @param lineNo
     */
    @Transactional
    public void removeByAsnNoAndRcvLineNo(String asnNo, String lineNo, String orgId) {
        BanQinWmAsnSerial condition = new BanQinWmAsnSerial();
        condition.setAsnNo(asnNo);
        condition.setRcvLineNo(lineNo);
        condition.setOrgId(orgId);
        mapper.deleteByAsnNoAndLineNo(condition);
    }

    /**
     * 根据分配ID删除库存序列号
     * @param allocIds
     */
    @Transactional
    public void removeByAllocId(String[] allocIds, String orgId) {
        // 删除库存序列号
        mapper.deleteByShip(Arrays.asList(allocIds), orgId);
    }

    /**
     * 根据分配ID获取库存序列号
     * @param allocIds
     * @return
     */
    public List<BanQinWmInvSerial> getByAllocIds(String[] allocIds, String orgId) {
        return mapper.getByAllocId(Arrays.asList(allocIds), orgId);
    }

    /**
     * 序列号管理商品，保存调整单明细
     * @return
     * @throws WarehouseException
     */
    @Transactional
    public BanQinWmAdDetail saveAdDetailAndSerial(BanQinWmAdDetailEntity wmAdDetailEntity, BanQinWmAdDetail model) throws WarehouseException {
        // 调多时重复
        String repeatSerial = "";
        // 调少时缺少
        String lackSerial = "";
        //
        Map<String, BanQinWmAdSerial> existAdSerialMap = new HashMap<>();
        List<BanQinWmAdSerialEntity> wmAdSerialEntitys = wmAdDetailEntity.getWmAdSerialEntitys();
        if (wmAdSerialEntitys != null) {
            int i = 1;
            for (BanQinWmAdSerialEntity wmAdSerialEntity : wmAdSerialEntitys) {
                BanQinWmAdSerial wmAdSerialModel= new BanQinWmAdSerial();
                BeanUtils.copyProperties(wmAdSerialEntity, wmAdSerialModel);
                if (StringUtils.isEmpty(wmAdSerialModel.getId())) {
                    wmAdSerialModel.setId(IdGen.uuid());
                    wmAdSerialModel.setIsNewRecord(true);
                }
                wmAdSerialModel.setAdNo(wmAdDetailEntity.getAdNo());
                wmAdSerialModel.setLotNum(wmAdDetailEntity.getLotNum());
                wmAdSerialService.save(wmAdSerialModel);
                if (StringUtils.isNotEmpty(wmAdSerialModel.getId())) {
                    existAdSerialMap.put(wmAdSerialModel.getId(), wmAdSerialModel);
                }
                BanQinWmInvSerial wmInvSerialModel = this.getByOwnerCodeAndSkuCodeAndSerialNo(wmAdSerialEntity.getOwnerCode(),
                        wmAdSerialEntity.getSkuCode(), wmAdSerialEntity.getSerialNo(), wmAdSerialEntity.getOrgId());
                // 序列号增加时，校验序列号库存表是否有重复记录
                if (wmInvSerialModel != null && WmsCodeMaster.AD_A.getCode().equals(wmAdSerialEntity.getAdMode())) {
                    repeatSerial = repeatSerial + "[" + wmAdSerialEntity.getSerialNo() + "]";
                }
                // 序列号减少时，校验序列号是否存在
                if (wmInvSerialModel == null && WmsCodeMaster.AD_R.getCode().equals(wmAdSerialEntity.getAdMode())) {
                    lackSerial = lackSerial + "[" + wmAdSerialEntity.getSerialNo() + "]";
                }
                // 比较列表中是否有重复的序列号
                for (int j = i; j < wmAdSerialEntitys.size(); j++) {
                    if (wmAdSerialEntitys.get(j).getSerialNo().equals(wmAdSerialModel.getSerialNo())) {
                        throw new WarehouseException("[" + wmAdSerialEntitys.get(j).getSerialNo() + "]" + "序列号重复");
                    }
                }
                i++;
            }
            if (StringUtils.isNotEmpty(repeatSerial) || StringUtils.isNotEmpty(lackSerial)) {
                String str = "";
                if (StringUtils.isNotEmpty(repeatSerial)) {
                    str = repeatSerial + "序列号重复";
                }
                if (StringUtils.isNotEmpty(lackSerial)) {
                    str = lackSerial + "序列号不存在";
                }
                throw new WarehouseException(str);
            }
            if (StringUtils.isNotEmpty(model.getId())) {
                // 获取序列号已保存的调整信息
                List<BanQinWmAdSerial> wmAdSerialList = wmAdSerialService.getModelsByAdNo(wmAdDetailEntity.getAdNo(), wmAdDetailEntity.getLotNum(), wmAdDetailEntity.getOrgId());
                for (BanQinWmAdSerial wmAdSerialModel : wmAdSerialList) {
                    // 当前序列号列表不存在数据库中的序列号调整记录，则删除该记录
                    if (!existAdSerialMap.containsKey(wmAdSerialModel.getId())) {
                        wmAdSerialService.delete(wmAdSerialModel);
                    }
                }
            }
        }
        wmAdDetailService.save(model);
        return model;
    }

    /**
     * 校验该批次下调整数量和序列号调整数量绝对值是否相等
     * @param wmAdDetailModels
     * @return
     */
    public ResultMessage checkAdSerialNo(List<BanQinWmAdDetail> wmAdDetailModels) {
        ResultMessage msg = new ResultMessage();
        // 调整数量绝对值
        Double adNum = 0D;
        for (BanQinWmAdDetail wmAdDetailModel : wmAdDetailModels) {
            if (WmsCodeMaster.AD_A.getCode().equals(wmAdDetailModel.getAdMode())) {
                adNum = adNum + wmAdDetailModel.getQtyAdEa();
            } else {
                adNum = adNum - wmAdDetailModel.getQtyAdEa();
            }
        }
        // 找出该批次下调整序列号
        BanQinWmAdSerial example = new BanQinWmAdSerial();
        example.setAdNo(wmAdDetailModels.get(0).getAdNo());
        example.setLotNum(wmAdDetailModels.get(0).getLotNum());
        example.setOrgId(wmAdDetailModels.get(0).getOrgId());
        List<BanQinWmAdSerial> wmAdSerialModels = wmAdSerialService.findList(example);
        // 统计调整序列号的绝对值
        Double adSerialNum = 0D;
        for (BanQinWmAdSerial wmAdSerialModel : wmAdSerialModels) {
            if (WmsCodeMaster.AD_A.getCode().equals(wmAdSerialModel.getAdMode())) {
                adSerialNum = adSerialNum + 1D;
            } else {
                adSerialNum = adSerialNum - 1D;
            }
        }
        if (adSerialNum.equals(adNum)) {
            msg.setSuccess(true);
        } else {
            msg.setSuccess(false);
        }
        msg.setData(wmAdSerialModels);
        return msg;
    }

    /**
     * 校验该批次下调整数量和序列号调整数量绝对值是否相等
     * @param adNo
     * @return
     */
    public ResultMessage checkAdSerialNo(String adNo, String orgId) {
        ResultMessage msg = new ResultMessage();
        msg.setSuccess(true);
        BanQinWmAdDetail condition = new BanQinWmAdDetail();
        condition.setAdNo(adNo);
        condition.setOrgId(orgId);
        List<BanQinWmAdDetailEntity> items = this.wmAdDetailService.wmAdCheckSerialQuery(condition);
        for (BanQinWmAdDetailEntity item : items) {
            // 获取批次下序列号调整绝对值
            BanQinWmAdSerial absSerialCondition = new BanQinWmAdSerial();
            absSerialCondition.setAdNo(adNo);
            absSerialCondition.setOwnerCode(item.getOwnerCode());
            absSerialCondition.setSkuCode(item.getSkuCode());
            absSerialCondition.setLotNum(item.getLotNum());
            absSerialCondition.setOrgId(orgId);
            Double abSerial = this.wmAdSerialService.wmAdAbsSerialQuery(absSerialCondition);
            if (abSerial != null) {
                if (!abSerial.equals(item.getLotQty())) {
                    msg.setSuccess(false);
                    msg.addMessage("[" + adNo + "][" + item.getLotNum() + "]" + "该批次的调整数量和序列号总数不匹配，不能操作");
                }
            } else {
                msg.setSuccess(false);
                msg.addMessage("[" + adNo + "][" + item.getLotNum() + "]" + "该批次的调整数量和序列号总数不匹配，不能操作");
            }
        }
        return msg;
    }

    /**
     * 序列号转移，增加序列号转入和转出任务
     * @param wmTfDetail
     * @param serialNo
     * @return
     * @throws WarehouseException
     */
    @Transactional
    public ResultMessage saveTfOrigAndDestSerial(BanQinWmTfDetail wmTfDetail, String serialNo) throws WarehouseException {
        ResultMessage msg = new ResultMessage();
        saveTfOrigSerial(wmTfDetail, serialNo);
        saveTfDestSerial(wmTfDetail, serialNo);
        return msg;
    }

    /**
     * 序列号转移，保存序列号转出任务
     * @param wmTfDetail
     * @param serialNo
     * @throws WarehouseException
     */
    @Transactional
    public void saveTfOrigSerial(BanQinWmTfDetail wmTfDetail, String serialNo) throws WarehouseException {
        // 校验序列号库存是否存在
        BanQinWmInvSerial wmInvSerialModel = this.getByOwnerAndSkuAndLotAndSerialNo(wmTfDetail.getFmOwner(), wmTfDetail.getFmSku(), wmTfDetail.getFmLot(), serialNo, wmTfDetail.getOrgId());
        if (wmInvSerialModel == null) {
            throw new WarehouseException(serialNo + "序列号不存在");
        }
        // 校验是否重复扫描序列号转出
        BanQinWmTfSerial existWmTfSerialModel = wmTfSerialService.getWmTfSerialModel(wmTfDetail.getTfNo(), wmTfDetail.getLineNo(), WmsCodeMaster.O.getCode(), serialNo, wmTfDetail.getOrgId());
        if (existWmTfSerialModel != null) {
            throw new WarehouseException(serialNo + "序列号已扫描");
        }
        BanQinWmTfSerial wmTfSerialModel = new BanQinWmTfSerial();
        wmTfSerialModel.setOwnerCode(wmTfDetail.getFmOwner());
        wmTfSerialModel.setSkuCode(wmTfDetail.getFmSku());
        wmTfSerialModel.setLotNum(wmTfDetail.getFmLot());
        wmTfSerialModel.setSerialNo(serialNo);
        wmTfSerialModel.setTfNo(wmTfDetail.getTfNo());
        wmTfSerialModel.setLineNo(wmTfDetail.getLineNo());
        wmTfSerialModel.setTfMode(WmsCodeMaster.O.getCode());
        wmTfSerialModel.setOrgId(wmTfDetail.getOrgId());
        wmTfSerialService.save(wmTfSerialModel);
    }

    /**
     * 序列号转移，保存序列号转入任务
     * @param wmTfDetail
     * @param serialNo
     * @throws WarehouseException
     */
    @Transactional
    public void saveTfDestSerial(BanQinWmTfDetail wmTfDetail, String serialNo) throws WarehouseException {
        // 校验序列号库存是否存在
        BanQinWmInvSerial wmInvSerialModel = this.getByOwnerAndSkuAndLotAndSerialNo(wmTfDetail.getToOwner(), wmTfDetail.getToSku(), wmTfDetail.getToLot(), serialNo, wmTfDetail.getOrgId());
        if (wmInvSerialModel != null) {
            throw new WarehouseException(serialNo + "序列号不存在");
        }
        // 校验是否重复扫描序列号转入
        BanQinWmTfSerial existWmTfSerialModel = wmTfSerialService.getWmTfSerialModel(wmTfDetail.getTfNo(), wmTfDetail.getLineNo(), WmsCodeMaster.I.getCode(), serialNo, wmTfDetail.getOrgId());
        if (existWmTfSerialModel != null) {
            throw new WarehouseException(serialNo + "序列号重复扫描");
        }
        BanQinWmTfSerial wmTfSerialModel = new BanQinWmTfSerial();
        wmTfSerialModel.setOwnerCode(wmTfDetail.getToOwner());
        wmTfSerialModel.setSkuCode(wmTfDetail.getToSku());
        wmTfSerialModel.setLotNum(wmTfDetail.getToLot());
        wmTfSerialModel.setSerialNo(serialNo);
        wmTfSerialModel.setTfNo(wmTfDetail.getTfNo());
        wmTfSerialModel.setLineNo(wmTfDetail.getLineNo());
        wmTfSerialModel.setTfMode(WmsCodeMaster.I.getCode());
        wmTfSerialService.save(wmTfSerialModel);
    }

    /**
     * 序列号转移，删除序列号转入和转出任务
     * @param wmTfDetail
     * @param serialNo
     * @throws WarehouseException
     */
    @Transactional
    public void removeTfOrigAndDestSerial(BanQinWmTfDetail wmTfDetail, String serialNo) throws WarehouseException {
        removeTfOrigSerial(wmTfDetail, serialNo);
        removeTfDestSerial(wmTfDetail, serialNo);
    }

    /**
     * 序列号转移，删除序列号转出任务
     * @param wmTfDetail
     * @param serialNo
     * @throws WarehouseException
     */
    @Transactional
    public void removeTfOrigSerial(BanQinWmTfDetail wmTfDetail, String serialNo) throws WarehouseException {
        BanQinWmTfSerial wmTfSerialModel = wmTfSerialService.getWmTfSerialModel(wmTfDetail.getTfNo(), wmTfDetail.getLineNo(), WmsCodeMaster.O.getCode(), serialNo, wmTfDetail.getOrgId());
        if (wmTfSerialModel == null) {
            throw new WarehouseException(serialNo + "序列号不存在");
        }
        wmTfSerialService.delete(wmTfSerialModel);
    }

    /**
     * 序列号转移，删除序列号转入任务
     * @param wmTfDetail
     * @param serialNo
     * @throws WarehouseException
     */
    @Transactional
    public void removeTfDestSerial(BanQinWmTfDetail wmTfDetail, String serialNo) throws WarehouseException {
        BanQinWmTfSerial wmTfSerialModel = wmTfSerialService.getWmTfSerialModel(wmTfDetail.getTfNo(), wmTfDetail.getLineNo(), WmsCodeMaster.I.getCode(), serialNo, wmTfDetail.getOrgId());
        if (wmTfSerialModel == null) {
            throw new WarehouseException(serialNo + "序列号不存在");
        }
        wmTfSerialService.delete(wmTfSerialModel);
    }

    /**
     * 勾选记录删除序列号转移
     * @param wmTfSerial
     * @throws WarehouseException
     */
    @Transactional
    public void removeTfSerial(BanQinWmTfSerial wmTfSerial) throws WarehouseException {
        BanQinWmTfSerial wmTfSerialModel = wmTfSerialService.getWmTfSerialModel(wmTfSerial.getTfNo(), wmTfSerial.getLineNo(), wmTfSerial.getTfMode(), wmTfSerial.getSerialNo(), wmTfSerial.getOrgId());
        if (wmTfSerialModel == null) {
            throw new WarehouseException(wmTfSerial.getSerialNo() + "序列号不存在");
        }
        wmTfSerialService.delete(wmTfSerialModel);
    }

    /**
     * 审核或执行转移时，对商品转入数量和序列号转入数量进行比较 若不相等，审核不通过
     * @param tfNo
     * @return
     */
    public ResultMessage checkTfSerialNo(String tfNo, String orgId) {
        ResultMessage msg = new ResultMessage();
        msg.setSuccess(true);
        List<BanQinWmTfDetailEntity> items = wmTfDetailService.wmTfIsSerialQuery(tfNo, orgId);
        for (BanQinWmTfDetailEntity item : items) {
            // 若转出商品是序列号商品
            if (WmsConstants.YES.equals(item.getFmIsSerial())) {
                BanQinWmTfSerial Condition = new BanQinWmTfSerial();
                Condition.setTfNo(tfNo);
                Condition.setLineNo(item.getLineNo());
                Condition.setTfMode(WmsCodeMaster.O.getCode());
                Condition.setOrgId(orgId);
                List<BanQinWmTfSerial> countItems = wmTfSerialService.findList(Condition);
                if (countItems.size() != item.getFmQtyEa().intValue()) {
                    msg.setSuccess(false);
                    msg.addMessage("[" + item.getTfNo() + "][" + item.getLineNo() + "]" + "转出数量和序列号转出数量不相等 ");
                }
            }
            if (WmsConstants.YES.equals(item.getToIsSerial())) {
                BanQinWmTfSerial condition = new BanQinWmTfSerial();
                condition.setTfNo(tfNo);
                condition.setLineNo(item.getLineNo());
                condition.setTfMode(WmsCodeMaster.I.getCode());
                condition.setOrgId(orgId);
                List<BanQinWmTfSerial> countItems = wmTfSerialService.findList(condition);
                if (countItems.size() != item.getToQtyEa().intValue()) {
                    msg.setSuccess(false);
                    msg.addMessage("[" + item.getTfNo() + "][" + item.getLineNo() + "]" + "转入数量和序列号转入数量不相等\n");
                }
            }
        }
        return msg;
    }


    /**
     * 序列号库存更新
     * @param entity
     * @throws WarehouseException
     */
    @Transactional
    public void updateInventorySerial(BanQinWmInvSerialEntity entity) throws WarehouseException {
        // 获取序列号库存
        BanQinWmInvSerial wmInvSerialModel = this.getByOwnerCodeAndSkuCodeAndSerialNo(entity.getOwnerCode(), entity.getSkuCode(), entity.getSerialNo(), entity.getOrgId());
        // 当序列号为收货、调增、转入、取消时
        if (WmsCodeMaster.RCV.getCode().equals(entity.getSerialTranType()) || WmsCodeMaster.AIN.getCode().equals(entity.getSerialTranType())
                || WmsCodeMaster.TIN.getCode().equals(entity.getSerialTranType()) || WmsCodeMaster.CSP.getCode().equals(entity.getSerialTranType())) {
            // 序列号已经存在
            if (wmInvSerialModel != null) {
                throw new WarehouseException("[" + entity.getSerialNo() + "]序列号已经存在");
            }
            // 保存序列号库存
            BanQinWmInvSerial model = new BanQinWmInvSerial();
            model.setOwnerCode(entity.getOwnerCode());
            model.setSkuCode(entity.getSkuCode());
            model.setLotNum(entity.getLotNum());
            model.setSerialNo(entity.getSerialNo());
            model.setOrgId(entity.getOrgId());
            this.save(model);
            // 写序列号库存交易
            addActTranSerial(entity);
        } else {
            // 序列号不存在
            if (wmInvSerialModel == null) {
                throw new WarehouseException("[" + entity.getSerialNo() + "]序列号不存在");
            }
            // 删除序列号库存
            this.delete(wmInvSerialModel);
            // 写序列号库存交易
            addActTranSerial(entity);
        }
    }

    @Transactional
    public void addActTranSerial(BanQinWmInvSerialEntity entity) {
        BanQinWmActTranSerial wmActTranSerialModel = new BanQinWmActTranSerial();
        wmActTranSerialModel.setLineNo(entity.getLineNo());
        wmActTranSerialModel.setLotNum(entity.getLotNum());
        wmActTranSerialModel.setOrderNo(entity.getOrderNo());
        wmActTranSerialModel.setOrderType(entity.getOrderType());
        wmActTranSerialModel.setOwnerCode(entity.getOwnerCode());
        wmActTranSerialModel.setSkuCode(entity.getSkuCode());
        wmActTranSerialModel.setSerialNo(entity.getSerialNo());
        wmActTranSerialModel.setSerialTranType(entity.getSerialTranType());
        wmActTranSerialModel.setSerialTranId(noService.getDocumentNo(GenNoType.WM_SERIAL_TRAN_ID.name()));
        wmActTranSerialModel.setTranId(entity.getTranId());
        wmActTranSerialModel.setTranOp(UserUtils.getUser().getName());
        wmActTranSerialModel.setTranTime(new Date());
        wmActTranSerialModel.setOrgId(entity.getOrgId());
        banQinWmActTranSerialService.save(wmActTranSerialModel);
    }
	
}