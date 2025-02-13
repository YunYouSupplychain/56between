package com.yunyou.modules.wms.inventory.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.config.GlobalDataRule;
import com.yunyou.common.utils.IdGen;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.sys.GenNoType;
import com.yunyou.modules.sys.entity.User;
import com.yunyou.modules.sys.service.SynchronizedNoService;
import com.yunyou.modules.sys.utils.UserUtils;
import com.yunyou.modules.wms.basicdata.service.BanQinCdWhLocService;
import com.yunyou.modules.wms.common.entity.*;
import com.yunyou.modules.wms.inventory.entity.BanQinWmActHold;
import com.yunyou.modules.wms.inventory.entity.BanQinWmHold;
import com.yunyou.modules.wms.inventory.entity.BanQinWmHoldEntity;
import com.yunyou.modules.wms.inventory.entity.BanQinWmInvLotLoc;
import com.yunyou.modules.wms.inventory.mapper.BanQinWmHoldMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 库存冻结Service
 * @author WMJ
 * @version 2019-01-26
 */
@Service
@Transactional(readOnly = true)
public class BanQinWmHoldService extends CrudService<BanQinWmHoldMapper, BanQinWmHold> {
    @Autowired
    private BanQinCdWhLocService cdWhLocService;
    @Autowired
    private BanQinWmInvLotLocService wmInvLotLocService;
    @Autowired
    private BanQinWmInvLotAttService wmInvLotAttService;
    @Autowired
    @Lazy
    private BanQinInventoryService inventoryService;
    @Autowired
    private SynchronizedNoService noService;
    @Autowired
    private BanQinWmActHoldService wmActHoldService;

	public BanQinWmHold get(String id) {
		return super.get(id);
	}

    public BanQinWmHoldEntity getEntity(String id) {
        return mapper.getEntity(id);
    }
	
	public List<BanQinWmHold> findList(BanQinWmHold banQinWmHold) {
		return super.findList(banQinWmHold);
	}
	
	public Page<BanQinWmHoldEntity> findPage(Page page, BanQinWmHoldEntity banQinWmHoldEntity) {
        banQinWmHoldEntity.setDataScope(GlobalDataRule.getDataRuleSql("a.org_id", banQinWmHoldEntity.getOrgId()));
        dataRuleFilter(banQinWmHoldEntity);
        banQinWmHoldEntity.setPage(page);
        page.setList(mapper.findPage(banQinWmHoldEntity));
		return page;
	}
	
	public BanQinWmHold findFirst(BanQinWmHold banQinWmHold) {
        List<BanQinWmHold> list = this.findList(banQinWmHold);
        return CollectionUtil.isNotEmpty(list) ? list.get(0) : null;
    }
	
	@Transactional
	public void save(BanQinWmHold banQinWmHold) {
		super.save(banQinWmHold);
	}
	
	@Transactional
	public void delete(BanQinWmHold banQinWmHold) {
		super.delete(banQinWmHold);
	}
	
	public BanQinWmHold findByQuery(String holdType, String traceId, String locCode, String lotNum, String ownerCode, String skuCode, String orgId) {
        BanQinWmHold model = new BanQinWmHold();
        model.setHoldType(holdType);
        model.setTraceId(traceId);
        model.setLocCode(locCode);
        model.setLotNum(lotNum);
        model.setOwnerCode(ownerCode);
        model.setSkuCode(skuCode);
        model.setOrgId(orgId);
        return this.findFirst(model);
    }

    /**
     * 校验冻结是否可以调整，转移，移动
     * @param wmInvLotLocModel
     * @param flag
     * @return
     */
    public ResultMessage checkHold(BanQinWmInvLotLoc wmInvLotLocModel, String flag) {
        ResultMessage msg = new ResultMessage();
        // 标记有冻结记录
        msg.setData(false);
        BanQinWmHold traceIdWmHoldModel = findByQuery(WmsCodeMaster.HOLD_BY_ID.getCode(), wmInvLotLocModel.getTraceId(), null, null, null, null, wmInvLotLocModel.getOrgId());
        checkStatus(msg, traceIdWmHoldModel, flag);

        BanQinWmHold locWmHoldModel = findByQuery(WmsCodeMaster.HOLD_BY_LOC.getCode(), null, wmInvLotLocModel.getLocCode(), null, null, null, wmInvLotLocModel.getOrgId());
        checkStatus(msg, locWmHoldModel, flag);

        BanQinWmHold lotWmHoldModel = findByQuery(WmsCodeMaster.HOLD_BY_LOT.getCode(), null, null, wmInvLotLocModel.getLotNum(), null, null, wmInvLotLocModel.getOrgId());
        checkStatus(msg, lotWmHoldModel, flag);

        BanQinWmHold skuWmHoldModel = findByQuery(WmsCodeMaster.HOLD_BY_SKU.getCode(), null, null, null, wmInvLotLocModel.getOwnerCode(), wmInvLotLocModel.getSkuCode(), wmInvLotLocModel.getOrgId());
        checkStatus(msg, skuWmHoldModel, flag);
        
        BanQinWmHold ownerWmHoldModel = findByQuery(WmsCodeMaster.HOLD_BY_OWNER.getCode(), null, null, null, wmInvLotLocModel.getOwnerCode(), null, wmInvLotLocModel.getOrgId());
        checkStatus(msg, ownerWmHoldModel, flag);
        return msg;
    }
    
    public void checkStatus(ResultMessage msg, BanQinWmHold wmHold, String flag) {
        if (null != wmHold) {
            msg.setData(true);
            if (flag.equals(WmsConstants.AD) && WmsConstants.NO.equals(wmHold.getIsAllowAd())) {
                msg.setSuccess(false);
            }
            if (flag.equals(WmsConstants.TF) && WmsConstants.NO.equals(wmHold.getIsAllowTf())) {
                msg.setSuccess(false);
            }
            if (flag.equals(WmsConstants.MV) && WmsConstants.NO.equals(wmHold.getIsAllowMv())) {
                msg.setSuccess(false);
            }
        }
    } 

    /**
     * 保存库存冻结
     * @param entity
     * @return
     */
    @Transactional
    public ResultMessage frost(BanQinWmHold entity) {
        String type = entity.getHoldType();
        ResultMessage msg = new ResultMessage();
        ResultMessage holdMsg;
        // 用于记录被保存的model
        List<BanQinWmHold> saveList = new ArrayList<>();
        try {
            BanQinWmHold model = new BanQinWmHold();
            if (WmsCodeMaster.HOLD_BY_OWNER.getCode().equals(type)) {
                // 将货主编码按“，”分割，并创建成model对象
                String[] codeArr = entity.getOwnerCode().split(",");
                for (int i = 0; i < codeArr.length; i++) {
                    BeanUtils.copyProperties(entity, model);
                    model.setId(IdGen.uuid());
                    model.setOwnerCode(codeArr[i]);
                    if (!checkUniqueHold(model)) {
                        // 冻结记录已存在不能冻结
                        msg.addMessage("货主[" + codeArr[i] + "]冻结记录已存在不能冻结");
                        continue;
                    }
                    // 进行冻结操作
                    holdMsg = invHold(model);
                    if (!holdMsg.isSuccess()) {
                        msg.addMessage("货主[" + codeArr[i] + "]没有可冻结的记录或库存不满足冻结条件");
                        continue;
                    }
                    BanQinWmHold saveModel = (BanQinWmHold) holdMsg.getData();
                    saveList.add(saveModel);
                }
            } else if (WmsCodeMaster.HOLD_BY_SKU.getCode().equals(type)) {
                // 将商品按商品对应的货主编码字符串的“,”分割
                String[] skuOwnerArr = entity.getOwnerCode().split(",");
                String[] skuArr = entity.getSkuCode().split(",");
                for (int i = 0; i < skuOwnerArr.length; i++) {
                    BeanUtils.copyProperties(entity, model);
                    model.setId(IdGen.uuid());
                    model.setOwnerCode(skuOwnerArr[i]);
                    model.setSkuCode(skuArr[i]);
                    if (!checkUniqueHold(model)) {
                        msg.addMessage("商品[" + skuArr[i] + "]已经存在冻结记录，不能重复冻结");
                        continue;
                    }
                    holdMsg = invHold(model);
                    if (!holdMsg.isSuccess()) {//
                        msg.addMessage("商品[" + skuArr[i] + "]没有可冻结的记录");
                        continue;
                    }
                    BanQinWmHold saveModel = new BanQinWmHold();
                    BeanUtils.copyProperties(holdMsg.getData(), saveModel);
                    saveList.add(saveModel);
                }
            } else if (WmsCodeMaster.HOLD_BY_LOT.getCode().equals(type)) {
                ResultMessage newMsg = wmInvLotAttService.checkByInvLotNum(entity.getLotNum(), entity.getOrgId());
                if (!newMsg.isSuccess()) {
                    msg.addMessage("查询不到批次号[" + entity.getLotNum() + "]");
                    msg.setSuccess(false);
                    return msg;
                }
                if (checkUniqueHold(entity)) {
                    BeanUtils.copyProperties(entity, model);
                    model.setId(IdGen.uuid());
                    holdMsg = invHold(model);
                    if (holdMsg.isSuccess()) {
                        BanQinWmHold saveModel = (BanQinWmHold) holdMsg.getData();
                        saveList.add(saveModel);
                    } else {
                        msg.addMessage("没有满足可冻结条件的库存记录[" + model.getLotNum() + "]");
                    }
                } else {
                    msg.addMessage("批次[" + entity.getLotNum() + "]已经存在冻结记录，不能重复冻结");
                }
            } else if (WmsCodeMaster.HOLD_BY_LOC.getCode().equals(type)) {
                String[] locArr = entity.getLocCode().split(",");
                for (int i = 0; i < locArr.length; i++) {
                    BeanUtils.copyProperties(entity, model);
                    model.setId(IdGen.uuid());
                    model.setLocCode(locArr[i]);
                    if (!checkUniqueHold(model)) {
                        msg.addMessage("库位[" + locArr[i] + "]已经存在冻结记录，不能重复冻结");
                        continue;
                    }
                    holdMsg = invHold(model);
                    if (!holdMsg.isSuccess()) {
                        msg.addMessage("库位[" + locArr[i] + "]没有满足可冻结条件的库存记录");
                        continue;
                    }
                    BanQinWmHold saveModel = (BanQinWmHold) holdMsg.getData();
                    cdWhLocService.holdLoc(entity.getLocCode(), WmsCodeMaster.LOC_STATUS_HOLD.getCode(), entity.getOrgId());
                    saveList.add(saveModel);
                }
            } else if (WmsCodeMaster.HOLD_BY_ID.getCode().equals(type)) {
                ResultMessage newMsg = wmInvLotLocService.checkByTraceId(entity.getTraceId(), entity.getOrgId());
                if (!newMsg.isSuccess()) {
                    msg.addMessage("查询不到跟踪号[" + entity.getTraceId() + "]");
                    msg.setSuccess(false);
                    return msg;
                }
                if (checkUniqueHold(entity)) {
                    BeanUtils.copyProperties(entity, model);
                    model.setId(IdGen.uuid());
                    holdMsg = invHold(model);
                    if (holdMsg.isSuccess()) {
                        BanQinWmHold saveModel = (BanQinWmHold) holdMsg.getData();
                        saveList.add(saveModel);
                    } else {
                        msg.addMessage("跟踪号[" + model.getTraceId() + "]没有满足可冻结条件的库存记录");
                    }
                } else {
                    msg.addMessage("跟踪号[" + entity.getTraceId() + "]已经存在冻结记录，不能重复冻结");
                }
            }
        } catch (WarehouseException e) {
            throw new RuntimeException(e.getMessage());
        }
        // 如果有被冻结的记录，返回提示信息
        if (saveList.size() > 0) {
            msg.setSuccess(true);
            if (StringUtils.isEmpty(msg.getMessage()))
                msg.addMessage("操作成功");
        } else {
            msg.setSuccess(false);
        }
        return msg;
    }


    /**
     * 验证hold对应的类型的字段值是否重复
     * @param model
     * @return
     */
    public Boolean checkUniqueHold(BanQinWmHold model) {
        BanQinWmHold example = new BanQinWmHold();
        String type = model.getHoldType();
        example.setOrgId(model.getOrgId());
        if (WmsCodeMaster.HOLD_BY_OWNER.getCode().equals(type)) {
            example.setHoldType(type);
            example.setOwnerCode(model.getOwnerCode());
        } else if (WmsCodeMaster.HOLD_BY_SKU.getCode().equals(type)) {
            example.setHoldType(type);
            example.setOwnerCode(model.getOwnerCode());
            example.setSkuCode(model.getSkuCode());
        } else if (WmsCodeMaster.HOLD_BY_LOT.getCode().equals(type)) {
            example.setHoldType(type);
            example.setLotNum(model.getLotNum());
        } else if (WmsCodeMaster.HOLD_BY_LOC.getCode().equals(type)) {
            example.setHoldType(type);
            example.setLocCode(model.getLocCode());
        } else if (WmsCodeMaster.HOLD_BY_ID.getCode().equals(type)) {
            example.setHoldType(type);
            example.setTraceId(model.getTraceId());
        }
        if (findList(example).size() > 0)
            return false;
        else
            return true;
    }

    /**
     * 解冻
     * @param ids
     * @return
     */
    @Transactional
    public ResultMessage thaw(String ids) {
        ResultMessage msg = new ResultMessage();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            BanQinWmHold model = get(id);
            try {
                invRelease(model);
                if (WmsCodeMaster.HOLD_BY_LOC.getCode().equals(model.getHoldType())) {
                    cdWhLocService.holdLoc(model.getLocCode(), WmsCodeMaster.LOC_STATUS_OK.getCode(), model.getOrgId());
                }
            } catch (WarehouseException e) {
                msg.addMessage(model.getHoldId() + e.getMessage());
            }
        }
        if (StringUtils.isEmpty(msg.getMessage())) {
            msg.addMessage("操作成功");
            msg.setSuccess(true);
        } else {
            msg.setSuccess(false);
            throw new RuntimeException(msg.getMessage());
        }
        return msg;
    }

    /**
     * 对满足冻结条件的库存记录进行冻结
     * @param wmHoldModel
     * @return
     * @throws WarehouseException
     */
    @Transactional
    public ResultMessage invHold(BanQinWmHold wmHoldModel) throws WarehouseException {
        ResultMessage msg;
        BanQinWmHold returnWmHoldModel;
        // 校验库存是否满足冻结条件 分配数，拣货数，上架待出数，补货待出数，移动待出数需要都为零
        msg = check(wmHoldModel);
        if (!msg.isSuccess()) {
            return msg;
        }
        // 保存库存冻结操作记录表和库存冻结表
        returnWmHoldModel = saveHoldModel(wmHoldModel);
        // 获取需要更新的库存记录
        BanQinWmInvLotLoc example = new BanQinWmInvLotLoc();
        example.setOwnerCode(wmHoldModel.getOwnerCode());
        example.setSkuCode(wmHoldModel.getSkuCode());
        example.setLotNum(wmHoldModel.getLotNum());
        example.setLocCode(wmHoldModel.getLocCode());
        example.setTraceId(wmHoldModel.getTraceId());
        example.setOrgId(wmHoldModel.getOrgId());
        List<BanQinWmInvLotLoc> wmInvLotLocModels = wmInvLotLocService.findList(example);
        for (BanQinWmInvLotLoc wmInvLotLocModel : wmInvLotLocModels) {
            if (wmInvLotLocModel.getQtyHold() == 0) {
                BanQinInventoryEntity inventoryEntity = getInvModel(wmInvLotLocModel, wmHoldModel, ActionCode.HOLD);
                inventoryService.updateInventory(inventoryEntity);
            }
        }

        msg.setData(returnWmHoldModel);
        return msg;
    }

    /**
     * 按货主进行解冻
     * @param wmHoldModel
     * @return
     * @throws WarehouseException
     */
    @Transactional
    public ResultMessage invRelease(BanQinWmHold wmHoldModel) throws WarehouseException {
        ResultMessage msg = new ResultMessage();
        // 删除库存冻结表记录和保存冻结操作记录表
        saveReleaseModel(wmHoldModel);
        // 获取解冻的库存记录
        BanQinWmInvLotLoc example = new BanQinWmInvLotLoc();
        example.setOwnerCode(wmHoldModel.getOwnerCode());
        example.setOwnerCode(wmHoldModel.getOwnerCode());
        example.setSkuCode(wmHoldModel.getSkuCode());
        example.setLotNum(wmHoldModel.getLotNum());
        example.setLocCode(wmHoldModel.getLocCode());
        example.setTraceId(wmHoldModel.getTraceId());
        example.setOrgId(wmHoldModel.getOrgId());
        List<BanQinWmInvLotLoc> wmInvLotLocModels = wmInvLotLocService.findList(example);
        for (BanQinWmInvLotLoc wmInvLotLocModel : wmInvLotLocModels) {
            if (wmInvLotLocModel.getQtyHold() > 0) {
                // 收集更新库存数据
                BanQinInventoryEntity inventoryEntity = getInvModel(wmInvLotLocModel, wmHoldModel, ActionCode.CANCEL_HOLD);
                // 调用更新库存方法
                inventoryService.updateInventory(inventoryEntity);
            }
        }

        msg.setSuccess(true);
        return msg;
    }

    /**
     * 校验商品下的库存是否满足冻结条件 分配数，拣货数，上架待出数，补货待出数，移动待出数需要都为零
     * @param wmHoldModel
     * @return
     */
    protected ResultMessage check(BanQinWmHold wmHoldModel) {
        ResultMessage msg = new ResultMessage();
        BanQinWmInvLotLoc condition = new BanQinWmInvLotLoc();
        condition.setOwnerCode(wmHoldModel.getOwnerCode());
        condition.setSkuCode(wmHoldModel.getSkuCode());
        condition.setLotNum(wmHoldModel.getLotNum());
        condition.setLocCode(wmHoldModel.getLocCode());
        condition.setTraceId(wmHoldModel.getTraceId());
        condition.setOrgId(wmHoldModel.getOrgId());
        List<BanQinWmInvLotLoc> items = wmInvLotLocService.notAvailableHoldQuery(condition);
        if (items.size() > 0) {
            msg.setSuccess(false);
            msg.addMessage(wmHoldModel.getHoldId() + "库存冻结数量不足，不能操作");
            return msg;
        }
        msg.setSuccess(true);
        return msg;
    }

    /**
     * 收集更新库存数据
     * @param wmInvLotLocModel
     * @param wmHoldModel
     * @param actionCode
     * @return
     */
    protected BanQinInventoryEntity getInvModel(BanQinWmInvLotLoc wmInvLotLocModel, BanQinWmHold wmHoldModel, ActionCode actionCode) {
        BanQinInventoryEntity invm = new BanQinInventoryEntity();
        invm.setLotNum(wmInvLotLocModel.getLotNum());
        invm.setLocCode(wmInvLotLocModel.getLocCode());
        invm.setTraceId(wmInvLotLocModel.getTraceId());
        invm.setSkuCode(wmInvLotLocModel.getSkuCode());
        invm.setOwnerCode(wmInvLotLocModel.getOwnerCode());
        invm.setOrderNo(wmHoldModel.getHoldId());
        invm.setAction(actionCode);
        invm.setQtyEaOp(wmInvLotLocModel.getQty());
        invm.setOrgId(wmHoldModel.getOrgId());
        return invm;
    }

    /**
     * 保存库存冻结和冻结操作记录表
     * @param wmHoldModel
     * @return
     * @throws WarehouseException
     */
    @Transactional
    public BanQinWmHold saveHoldModel(BanQinWmHold wmHoldModel) {
        wmHoldModel.setHoldId(noService.getDocumentNo(GenNoType.WM_HOLD_ID.name()));
        wmHoldModel.setHoldOp(UserUtils.getUser().getName());
        wmHoldModel.setHoldTime(new Date());
        save(wmHoldModel);
        BanQinWmHold returnWmHoldModel = wmHoldModel;
        BanQinWmActHold wmActHoldModel = new BanQinWmActHold();
        // 将库存冻结记录保存到库存冻结操作记录表中
        BeanUtils.copyProperties(wmHoldModel, wmActHoldModel);
        wmActHoldModel.setId(IdGen.uuid());
        wmActHoldModel.setStatus(WmsCodeMaster.HOLD_STATUS_HOLD.getCode());
        wmActHoldModel.setHoldId(returnWmHoldModel.getHoldId());
        wmActHoldModel.setOp(returnWmHoldModel.getHoldOp());
        wmActHoldModel.setOpTime(returnWmHoldModel.getHoldTime());
        wmActHoldService.save(wmActHoldModel);
        return returnWmHoldModel;
    }

    /**
     * 删除库存冻结表记录和保存冻结操作记录表
     * @param wmHoldModel
     */
    @Transactional
    public void saveReleaseModel(BanQinWmHold wmHoldModel) {
        User user = UserUtils.getUser();
        this.delete(wmHoldModel);
        BanQinWmActHold wmActHoldModel = new BanQinWmActHold();
        // 将库存冻结记录保存到库存冻结操作记录表中
        BeanUtils.copyProperties(wmHoldModel, wmActHoldModel);
        wmActHoldModel.setId(null);
        wmActHoldModel.setStatus(WmsCodeMaster.HOLD_STATUS_RELEASE.getCode());
        wmActHoldModel.setOp(user.getLoginName());
        wmActHoldModel.setOpTime(new Date());
        wmActHoldService.save(wmActHoldModel);
    }
	
}