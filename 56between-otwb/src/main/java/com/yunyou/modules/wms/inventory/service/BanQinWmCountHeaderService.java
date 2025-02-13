package com.yunyou.modules.wms.inventory.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.config.GlobalDataRule;
import com.yunyou.common.utils.time.DateUtils;
import com.yunyou.common.utils.IdGen;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.sys.GenNoType;
import com.yunyou.modules.sys.entity.User;
import com.yunyou.modules.sys.service.SynchronizedNoService;
import com.yunyou.modules.sys.utils.UserUtils;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhSku;
import com.yunyou.modules.wms.basicdata.service.BanQinCdWhSkuService;
import com.yunyou.modules.wms.common.entity.WarehouseException;
import com.yunyou.modules.wms.common.entity.WmsCodeMaster;
import com.yunyou.modules.wms.common.entity.WmsConstants;
import com.yunyou.modules.wms.inventory.entity.*;
import com.yunyou.modules.wms.inventory.mapper.BanQinWmCountHeaderMapper;
import com.yunyou.modules.wms.inventory.mapper.BanQinWmTaskCountMapper;
import com.yunyou.modules.wms.report.entity.CountTaskLabel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 库存盘点Service
 * @author WMJ
 * @version 2019-01-28
 */
@Service
@Transactional(readOnly = true)
public class BanQinWmCountHeaderService extends CrudService<BanQinWmCountHeaderMapper, BanQinWmCountHeader> {
    @Autowired
    private SynchronizedNoService noService;
    @Autowired
    private BanQinWmTaskCountMapper wmTaskCountMapper;
    @Autowired
    private BanQinWmTaskCountService wmTaskCountService;
    @Autowired
    private BanQinWmAdHeaderService wmAdHeaderService;
    @Autowired
    private BanQinWmAdDetailService wmAdDetailService;
    @Autowired
    private BanQinWmAdSerialService wmAdSerialService;
    @Autowired
    private BanQinWmTaskCountSerialService wmTaskCountSerialService;
    @Autowired
    private BanQinWmInvSerialService wmInvSerialService;
    @Autowired
    private BanQinCdWhSkuService cdWhSkuService;
    @Autowired
    private BanQinInventoryService inventoryService;
    @Autowired
    @Lazy
    private BanQinInvRemoveService invRemoveService;

	public BanQinWmCountHeader get(String id) {
		return super.get(id);
	}

	public BanQinWmCountHeaderEntity getEntity(String id) {
	    return mapper.getEntity(id);
    }
	
	public List<BanQinWmCountHeader> findList(BanQinWmCountHeader banQinWmCountHeader) {
		return super.findList(banQinWmCountHeader);
	}
	
	public Page<BanQinWmCountHeaderEntity> findPage(Page page, BanQinWmCountHeaderEntity banQinWmCountHeaderEntity) {
        banQinWmCountHeaderEntity.setDataScope(GlobalDataRule.getDataRuleSql("a.org_id", banQinWmCountHeaderEntity.getOrgId()));
        dataRuleFilter(banQinWmCountHeaderEntity);
        banQinWmCountHeaderEntity.setPage(page);
        page.setList(mapper.findPage(banQinWmCountHeaderEntity));
		return page;
	}
	
	public BanQinWmCountHeader findFirst(BanQinWmCountHeader banQinWmCountHeader) {
        List<BanQinWmCountHeader> list = this.findList(banQinWmCountHeader);
        return CollectionUtil.isNotEmpty(list) ? list.get(0) : null;
    }

    public BanQinWmCountHeader findByCountNo(String countNo, String orgId) {
        BanQinWmCountHeader condition = new BanQinWmCountHeader();
        condition.setOrgId(orgId);
        condition.setCountNo(countNo);
        return findFirst(condition);
    }
	
	@Transactional
	public void save(BanQinWmCountHeader banQinWmCountHeader) {
        super.save(banQinWmCountHeader);
    }

    @Transactional
    public BanQinWmCountHeaderEntity saveEntity(BanQinWmCountHeaderEntity banQinWmCountHeaderEntity) {
        if (StringUtils.isEmpty(banQinWmCountHeaderEntity.getId())) {
            banQinWmCountHeaderEntity.setCountNo(noService.getDocumentNo(GenNoType.WM_COUNT_NO.name()));
        }
        banQinWmCountHeaderEntity.setIsCreateCheck(StringUtils.isNotEmpty(banQinWmCountHeaderEntity.getParentCountNo()) ? WmsConstants.YES : WmsConstants.NO);
        this.save(banQinWmCountHeaderEntity);
        return banQinWmCountHeaderEntity;
    }

    @Transactional
    public BanQinWmCountHeaderEntity saveSerialEntity(BanQinWmCountHeaderEntity banQinWmCountHeaderEntity) {
        if (StringUtils.isEmpty(banQinWmCountHeaderEntity.getId())) {
            banQinWmCountHeaderEntity.setCountNo(noService.getDocumentNo(GenNoType.WM_SERIAL_COUNT_NO.name()));
        }
        banQinWmCountHeaderEntity.setIsCreateCheck(StringUtils.isNotEmpty(banQinWmCountHeaderEntity.getParentCountNo()) ? WmsConstants.YES : WmsConstants.NO);
        this.save(banQinWmCountHeaderEntity);
        return banQinWmCountHeaderEntity;
    }
	
	@Transactional
	public void delete(BanQinWmCountHeader banQinWmCountHeader) {
		super.delete(banQinWmCountHeader);
	}

    /**
     * 删除盘点单
     */
    @Transactional
    public ResultMessage removeCountEntity(String[] ids) {
        ResultMessage msg = new ResultMessage();
        for (String id : ids) {
            BanQinWmCountHeader countHeader = this.get(id);
            try {
                ResultMessage removeMsg = invRemoveService.removeCount(countHeader.getCountNo(), countHeader.getOrgId());
                // 批量删除，不是为创建状态的拼接返回
                if (!removeMsg.isSuccess()) {
                    msg.addMessage("[" + countHeader.getCountNo() + "]状态不是新建状态<br>");
                }
            } catch (WarehouseException e) {
                msg.setSuccess(false);
                msg.addMessage(e.getMessage() + "<br>");
            } catch (NullPointerException e) {
                msg.setSuccess(false);
                msg.addMessage("数据已过期<br>");
            }
        }
        if (StringUtils.isEmpty(msg.getMessage())) {
            msg.setSuccess(true);
            msg.addMessage("操作成功");
        } else {
            throw new RuntimeException(msg.getMessage());
        }
        return msg;
    }

    /**
     * 批量生成普通盘点任务
     * @param ids
     * @return
     */
    @Transactional
    public ResultMessage generateOrdinaryCountTask(String ids) {
        ResultMessage msg = new ResultMessage();
        String[] idArray = ids.split(",");
        // 操作成功的第一条盘点单号的索引
        int index = -1;
        for (int i = 0, size = idArray.length; i < size; i++) {
            // 获取盘点单
            BanQinWmCountHeader wmCountHeader = get(idArray[i]);
            if (null != wmCountHeader && WmsCodeMaster.CC_NEW.getCode().equals(wmCountHeader.getStatus())) {
                try {
                    msg = invCreateTaskCount(wmCountHeader);
                    if (-1 == index && msg.isSuccess()) {
                        index = i;
                    }
                } catch (Exception e) {
                    msg.addMessage(wmCountHeader.getCountNo() + e.getMessage());
                }
            } else {
                msg.addMessage(wmCountHeader.getCountNo() + "不是新建状态");
            }
        }
        // 根据第一条成功的盘点单号生成任务返回前台，只有创建成功才提示操作成功
        if (-1 != index) {
            msg.setMessage("操作成功");
            msg.setSuccess(true);
        } else {
            msg.setSuccess(false);
            throw new RuntimeException(msg.getMessage());
        }
        return msg;
    }

    /**
     * 生成普通盘点任务
     * @param wmCountHeader
     * @return
     */
    @Transactional
    public ResultMessage invCreateTaskCount(BanQinWmCountHeader wmCountHeader) {
        ResultMessage msg = new ResultMessage();
        String[] ownerCode = StringUtils.isEmpty(wmCountHeader.getOwnerCode()) ? null : wmCountHeader.getOwnerCode().split(",");
        String[] skuCode = StringUtils.isEmpty(wmCountHeader.getSkuCode()) ? null : wmCountHeader.getSkuCode().split(",");
        String[] areaCode = StringUtils.isEmpty(wmCountHeader.getAreaCode()) ? null : wmCountHeader.getAreaCode().split(",");
        String[] zoneCode = StringUtils.isEmpty(wmCountHeader.getZoneCode()) ? null : wmCountHeader.getZoneCode().split(",");
        // 获取库存记录
        List<BanQinWmTaskCount> invCountTasks = this.invGetCountTask(wmCountHeader, ownerCode, skuCode, areaCode, zoneCode);
        if (CollectionUtil.isNotEmpty(invCountTasks)) {
            // 抽盘是按抽盘数量或者抽盘比率选择抽盘任务
            if ("2".equals(wmCountHeader.getCountRange())) {
                // 满足条件的所有库存任务记录数
                int totalNum = invCountTasks.size();
                int randomNum = wmCountHeader.getRandomNum() == null ? 0 : wmCountHeader.getRandomNum().intValue();
                int randomRate = wmCountHeader.getRandomRate() == null ? 0 : wmCountHeader.getRandomRate().intValue();
                // 计算抽盘数量
                int rateNum = (int) (randomRate / 100.0 * totalNum);
                if (totalNum >= randomNum && totalNum >= rateNum) {
                    invCountTasks = getTakeStock(invCountTasks, wmCountHeader);
                } else {
                    msg.setSuccess(false);
                    msg.setMessage(wmCountHeader.getCountNo() + "抽盘比例或抽盘数量超过库存记录数");
                    return msg;
                }
                if (totalNum <= 0) {
                    msg.setSuccess(false);
                    msg.setMessage(wmCountHeader.getCountNo() + "没有可生成的盘点任务");
                    return msg;
                }
            }

            // 生成盘点任务Id
            List<String> taskIds = noService.getDocumentNo(GenNoType.WM_COUNT_ID.name(), invCountTasks.size());
            int i = 0;
            for (BanQinWmTaskCount coutTaskItem : invCountTasks) {
                // 调用插入盘点任务表
                insertTaskCount(coutTaskItem, wmCountHeader, taskIds.get(i));
                i++;
            }
            // 判断是否序列号盘点,生成序列号盘点任务
            if ("Y".equals(wmCountHeader.getIsSerial())) {
                // 暂时没有序列号库存表
                serialCountTask(wmCountHeader, ownerCode, skuCode);
            }
        } else {
            msg.setSuccess(false);
            msg.setMessage(wmCountHeader.getCountNo() + "没有可生成的盘点任务");
            return msg;
        }
        // 更新盘点单状态为生成
        wmCountHeader.setStatus(WmsCodeMaster.CC_CREATE_TSK.getCode());
        save(wmCountHeader);
        msg.setSuccess(true);

        return msg;
    }

    private List<BanQinWmTaskCount> invGetCountTask(BanQinWmCountHeader wmCountHeader, String[] ownerCode, String[] skuCode, String[] areaCode, String[] zoneCode) {
        BanQinWmCountHeaderEntity condition = new BanQinWmCountHeaderEntity();
        BeanUtils.copyProperties(wmCountHeader, condition);
        condition.setOwnerCode(null);
        condition.setSkuCode(null);
        condition.setAreaCode(null);
        condition.setZoneCode(null);
        condition.setOwnerCodes(ownerCode);
        condition.setSkuCodes(skuCode);
        condition.setAreaCodes(areaCode);
        condition.setZoneCodes(zoneCode);
        return wmTaskCountMapper.getInvCountTask(condition);
    }

    /**
     * 从序列号库存记录中过滤出需要盘点的记录 生成盘点任务
     * @param wmCountHeaderModel
     * @param ownerCode
     * @param skuCode
     */
    @Transactional
    public void serialCountTask(BanQinWmCountHeader wmCountHeaderModel, String[] ownerCode, String[] skuCode) {
        BanQinCountSerialQuery condition = new BanQinCountSerialQuery();
        condition.setOwnerCode(null != ownerCode ? Arrays.asList(ownerCode) : null);
        condition.setSkuCode(null != skuCode ? Arrays.asList(skuCode) : null);
        condition.setLotNum(wmCountHeaderModel.getLotNum());
        condition.setOrgId(wmCountHeaderModel.getOrgId());
        condition.setLotAtt01(wmCountHeaderModel.getLotAtt01());
        condition.setLotAtt02(wmCountHeaderModel.getLotAtt02());
        condition.setLotAtt03(wmCountHeaderModel.getLotAtt03());
        condition.setLotAtt04(wmCountHeaderModel.getLotAtt04());
        condition.setLotAtt05(wmCountHeaderModel.getLotAtt05());
        condition.setLotAtt06(wmCountHeaderModel.getLotAtt06());
        condition.setLotAtt07(wmCountHeaderModel.getLotAtt07());
        condition.setLotAtt08(wmCountHeaderModel.getLotAtt08());
        condition.setLotAtt09(wmCountHeaderModel.getLotAtt09());
        condition.setLotAtt10(wmCountHeaderModel.getLotAtt10());
        condition.setLotAtt11(wmCountHeaderModel.getLotAtt11());
        condition.setLotAtt12(wmCountHeaderModel.getLotAtt12());
        List<BanQinWmInvSerial> items = wmInvSerialService.countSerialQuery(condition);
        for (BanQinWmInvSerial item : items) {
            BanQinWmTaskCountSerial taskCountSerial = new BanQinWmTaskCountSerial();
            // 将查找到的需要盘点记录复制到wmTaskCountSerialModel
            BeanUtils.copyProperties(item, taskCountSerial);
            taskCountSerial.setCountNo(wmCountHeaderModel.getCountNo());
            taskCountSerial.setStatus(WmsCodeMaster.TSK_NEW.getCode());
            taskCountSerial.setHeaderId(wmCountHeaderModel.getId());
            taskCountSerial.setId(IdGen.uuid());
            taskCountSerial.setIsNewRecord(true);
            taskCountSerial.setTraceId("*");
            wmTaskCountSerialService.save(taskCountSerial);
        }
    }

    public List<BanQinWmTaskCount> getTakeStock(List<BanQinWmTaskCount> taskCountList, BanQinWmCountHeader wmCountHeader) {
        // 满足条件的所有库存任务记录数
        int totalNum = taskCountList.size();
        // 返回最终需要抽盘的任务记录
        List<BanQinWmTaskCount> needCountItem = new ArrayList<>();
        int randomNum = wmCountHeader.getRandomNum() == null ? 0 : wmCountHeader.getRandomNum().intValue();
        int randomRate = wmCountHeader.getRandomRate() == null ? 0 : wmCountHeader.getRandomRate().intValue();
        if (randomNum != 0) {
            // 填写抽盘数量
            if (totalNum >= randomNum) {
                // 当抽盘数大于满足条件的盘点库存任务记录数
                needCountItem = taskCountList.subList(0, randomNum);
            } else {
                return needCountItem;
            }
        } else {
            // 计算抽盘数量
            int rateNum = (int) (randomRate / 100.0 * totalNum);
            if (totalNum > rateNum) {
                // 当抽盘数大于满足条件的盘点库存任务记录数
                needCountItem = taskCountList.subList(0, rateNum);
            } else {
                return needCountItem;
            }
        }
        return needCountItem;
    }

    @Transactional
    public void insertTaskCount(BanQinWmTaskCount wmTaskCount, BanQinWmCountHeader wmCountHeader, String countId) {
        User User = UserUtils.getUser();
        wmTaskCount.setCountId(countId);
        wmTaskCount.setHeaderId(wmCountHeader.getId());
        wmTaskCount.setCountNo(wmCountHeader.getCountNo());
        wmTaskCount.setStatus(WmsCodeMaster.TSK_NEW.getCode());
        wmTaskCount.setCountMethod(wmCountHeader.getCountMethod());
        wmTaskCount.setCountMode(wmCountHeader.getCountMode());
        wmTaskCount.setAreaCode(wmCountHeader.getAreaCode());
        wmTaskCount.setZoneCode(wmCountHeader.getZoneCode());
        wmTaskCount.setDataSource("SYSTEM");
        wmTaskCount.setQtyDiff(0.0);
        wmTaskCount.setOrgId(wmCountHeader.getOrgId());
        wmTaskCount.setCountOp(User.getLoginName());
        // 普通默认的的盘点数等于库存数,序列号盘点默认为零
        if ("Y".equals(wmCountHeader.getIsSerial())) {
            wmTaskCount.setQtyCountEa(0.0);
            wmTaskCount.setQtyDiff(0.0);
        } else {
            wmTaskCount.setQtyCountEa(wmTaskCount.getQty());
        }
        wmTaskCount.preInsert();
        wmTaskCountMapper.insert(wmTaskCount);
    }

    /**
     * 生成复盘任务
     * @param ids
     * @return
     */
    @Transactional
    public ResultMessage generateCompoundTask(String ids) {
        ResultMessage msg = new ResultMessage();
        String[] idArray = ids.split(",");
        // 操作成功的第一条盘点单号的索引
        int index = -1;
        for (String id : idArray) {
            // 获取盘点单
            BanQinWmCountHeader wmCountHeader = get(id);
            // 只有完全盘点的盘点单才能生成复盘任务
            if (wmCountHeader != null && "N".equals(wmCountHeader.getIsCreateCheck()) && WmsCodeMaster.CC_FULL_COUNT.getCode().equals(wmCountHeader.getStatus())) {
                // 验证调整单是否已经创建，已经生成调整单，无法生成复盘任务
                BanQinWmAdHeader wmAdHeader = new BanQinWmAdHeader();
                wmAdHeader.setCountNo(wmCountHeader.getCountNo());
                wmAdHeader.setOrgId(wmCountHeader.getOrgId());
                List<BanQinWmAdHeader> list = wmAdHeaderService.findList(wmAdHeader);
                if (CollectionUtil.isNotEmpty(list)) {
                    msg.setMessage(wmCountHeader.getCountNo() + "已经生成调整单，无法生成复盘任务");
                    msg.setSuccess(false);
                    return msg;
                }
                msg = invReCreateTaskCount(wmCountHeader);
                if (msg.isSuccess()) {
                    index = 1;
                    msg.setMessage("生成复盘单");
                } else {
                    msg.setMessage("调整单" + "没有损益数，不能生成复盘任务");
                }
            } else if (wmCountHeader != null && "Y".equals(wmCountHeader.getIsCreateCheck()) && WmsCodeMaster.CC_FULL_COUNT.getCode().equals(wmCountHeader.getStatus())) {
                msg.setMessage(wmCountHeader.getCountNo() + "已生成复盘单，不能重复生成");
            } else {
                // 非完全盘点状态不能操作
                msg.setMessage(wmCountHeader.getCountNo() + "非完全盘点状态不能操作");
            }
        }
        // 根据第一条成功的盘点单号生成任务返回前台
        if (-1 != index) {
            msg.setSuccess(true);
        } else {
            msg.setSuccess(false);
        }
        return msg;
    }

    @Transactional
    public ResultMessage invReCreateTaskCount(BanQinWmCountHeader wmsCountHeader) {
        ResultMessage msg = new ResultMessage();
        // 生成新的盘点单，把上一次的盘点单号放在parentCountNo中，然后生成新的盘点号
        BanQinWmCountHeader newCountHeaderModel = newCountHeaderModel(wmsCountHeader);
        // 获取有盘点差异的盘点任务号
        BanQinWmTaskCount condition = new BanQinWmTaskCount();
        condition.setCountNo(wmsCountHeader.getCountNo());
        condition.setHeaderId(wmsCountHeader.getId());
        condition.setOrgId(wmsCountHeader.getOrgId());
        List<BanQinWmTaskCount> items = wmTaskCountMapper.getReCreateCount(condition);
        if (CollectionUtil.isNotEmpty(items)) {
            for (BanQinWmTaskCount item : items) {
                // 将盘点有差异的盘点任务生成新的盘点任务
                BanQinWmTaskCount wmTaskCountModel = new BanQinWmTaskCount();
                BeanUtils.copyProperties(item, wmTaskCountModel);
                wmTaskCountModel.setId(IdGen.uuid());
                wmTaskCountModel.setIsNewRecord(true);
                wmTaskCountModel.setStatus(WmsCodeMaster.TSK_NEW.getCode());
                // 盘点数与库存数一致
                wmTaskCountModel.setQtyCountEa(wmTaskCountModel.getQty());
                wmTaskCountModel.setQtyDiff(null);
                wmTaskCountModel.setCountId(noService.getDocumentNo(GenNoType.WM_COUNT_ID.name()));
                wmTaskCountModel.setCountNo(newCountHeaderModel.getCountNo());
                wmTaskCountModel.setHeaderId(newCountHeaderModel.getId());
                wmTaskCountModel.preInsert();
                wmTaskCountMapper.insert(wmTaskCountModel);
            }
        } else {
            msg.setSuccess(false);
            return msg;
        }
        // 保存复盘盘点单
        save(newCountHeaderModel);
        // 更新父盘点单中是否产生复盘单
        wmsCountHeader.setIsCreateCheck("Y");
        super.save(wmsCountHeader);
        msg.setSuccess(true);
        return msg;
    }

    /**
     * 复盘时，产生新的盘点单号
     * @param wmsCountHeader
     * @return
     */
    protected BanQinWmCountHeader newCountHeaderModel(BanQinWmCountHeader wmsCountHeader) {
        BanQinWmCountHeader newCountHeader = new BanQinWmCountHeader();
        BeanUtils.copyProperties(wmsCountHeader, newCountHeader);
        newCountHeader.setCountType(WmsCodeMaster.CC_GC.getCode());
        newCountHeader.setParentCountNo(wmsCountHeader.getCountNo());
        newCountHeader.setCountNo(noService.getDocumentNo(GenNoType.WM_COUNT_NO.name()));
        newCountHeader.setStatus(WmsCodeMaster.CC_CREATE_TSK.getCode());// 复盘盘点单状态为生成任务
        newCountHeader.setCountType(WmsCodeMaster.CC_GC.getCode());
        newCountHeader.setCloseTime(null);
        newCountHeader.setId(IdGen.uuid());
        newCountHeader.setIsNewRecord(true);
        return newCountHeader;
    }

    /**
     * 取消盘点任务
     * @param ids
     * @return
     */
    @Transactional
    public ResultMessage cancelCountTask(String ids) {
        ResultMessage msg = new ResultMessage();
        String[] idArray = ids.split(",");
        // 操作成功的第一条盘点单号的索引
        int index = -1;
        for (int i = 0; i < idArray.length; i++) {
            BanQinWmCountHeader wmCountHeader = get(idArray[i]);
            boolean isSuccess = cancelTask(wmCountHeader);
            if (isSuccess && index == -1)
                index = i;
            if (!isSuccess) {
                msg.setMessage(wmCountHeader.getCountNo() + "不是生成任务状态，不能操作");
            }
        }
        // 根据第一条成功的盘点单号生成任务返回前台
        if (index == -1) {
            msg.setSuccess(false);
            return msg;
        }
        if (StringUtils.isEmpty(msg.getMessage())) {
            msg.setMessage("操作成功");
        }
        msg.setSuccess(true);
        return msg;
    }

    @Transactional
    public boolean cancelTask(BanQinWmCountHeader wmCountHeader) {
        // 盘点单为生成盘点任务，可以进行取消盘点任务
        if (wmCountHeader != null && (WmsCodeMaster.CC_CREATE_TSK.getCode().equals(wmCountHeader.getStatus()))) {
            // 盘点单状态置为新建
            wmCountHeader.setStatus(WmsCodeMaster.CC_NEW.getCode());
            // 保存盘点单
            save(wmCountHeader);
            // 删除盘点任务
            wmTaskCountMapper.deleteByHeaderId(wmCountHeader.getId());
            // 是序列号盘点，删除序列号盘点任务
            if ("Y".equals(wmCountHeader.getIsSerial())) {
                BanQinWmTaskCountSerial example = new BanQinWmTaskCountSerial();
                example.setCountNo(wmCountHeader.getCountNo());
                example.setOrgId(wmCountHeader.getOrgId());
                List<BanQinWmTaskCountSerial> wmTaskCountSerialModels = wmTaskCountSerialService.findList(example);
                wmTaskCountSerialService.deleteAll(wmTaskCountSerialModels);
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * 关闭盘点
     * @param ids
     * @return
     */
    @Transactional
    public ResultMessage closeCount(String ids) {
        String[] idArray = ids.split(",");
        ResultMessage msg = new ResultMessage();
        int index = -1;// 操作成功的第一条盘点单号的索引
        for (int i = 0; i < idArray.length; i++) {
            BanQinWmCountHeader wmCountHeader = get(idArray[i]);
            ResultMessage detailMsg = close(wmCountHeader);
            if (detailMsg.isSuccess() && index == -1)
                index = i;
            if (!detailMsg.isSuccess()) {
                msg.setMessage(wmCountHeader.getCountNo() + "不是盘点状态，不能操作");
            }
        }
        // 根据第一条成功的盘点单号生成任务返回前台
        if (index == -1) {
            msg.setSuccess(false);
            return msg;
        }
        msg.setSuccess(true);
        return msg;
    }

    @Transactional
    public ResultMessage close(BanQinWmCountHeader wmCountHeader) {
        ResultMessage msg = new ResultMessage();
        BanQinWmCountHeader condition = new BanQinWmCountHeader();
        condition.setCountNo(wmCountHeader.getParentCountNo());
        condition.setOrgId(wmCountHeader.getOrgId());
        List<BanQinWmCountHeader> list = findList(condition);
        BanQinWmCountHeader newModel = CollectionUtil.isNotEmpty(list) ? list.get(0) : null;
        // 当状态为部分调整或者完全调整时才能对调整单进行关闭
        if (WmsCodeMaster.CC_PART_COUNT.getCode().equals(wmCountHeader.getStatus()) || WmsCodeMaster.CC_FULL_COUNT.getCode().equals(wmCountHeader.getStatus())) {
            // 若有上次盘点单号且为部分调整，则把上次盘点单号的盘点单中的是否复盘号标志置为N
            if (WmsCodeMaster.CC_PART_COUNT.getCode().equals(wmCountHeader.getStatus())) {
                if (null != newModel) {
                    newModel.setIsCreateCheck("N");
                    save(newModel);
                }
            }
            wmCountHeader.setStatus(WmsCodeMaster.CC_CLOSE.getCode());
            save(wmCountHeader);
            msg.setSuccess(true);
        } else {
            msg.setSuccess(false);
        }
        return msg;
    }

    /**
     * 取消盘点
     * @param ids
     * @return
     */
    @Transactional
    public ResultMessage cancelCount(String ids) {
        ResultMessage msg = new ResultMessage();
        int index = -1;// 操作成功的第一条盘点单号的索引
        String[] idArray = ids.split(",");
        for (int i = 0; i < idArray.length; i++) {
            BanQinWmCountHeader wmsCountHeader = super.get(idArray[i]);
            ResultMessage detailMsg = cancel(wmsCountHeader);
            if (detailMsg.isSuccess() && index == -1)
                index = i;
            if (!detailMsg.isSuccess()) {
                msg.setMessage(wmsCountHeader.getCountNo() + "非创建、非生成任务状态，不能操作");
            }
        }
        // 根据第一条成功的盘点单号生成任务返回前台
        if (index == -1) {
            msg.setSuccess(false);
            return msg;
        }
        if (StringUtils.isEmpty(msg.getMessage())) {
            msg.setMessage("操作成功");
        }
        msg.setSuccess(true);
        return msg;
    }

    @Transactional
    public ResultMessage cancel(BanQinWmCountHeader wmCountHeader) {
        ResultMessage msg = new ResultMessage();
        // 盘点单为创建或者生成盘点任务，可以对盘点单进行取消
        if (null != wmCountHeader && (WmsCodeMaster.CC_NEW.getCode().equals(wmCountHeader.getStatus()) || WmsCodeMaster.CC_CREATE_TSK.getCode().equals(wmCountHeader.getStatus()))) {
            wmCountHeader.setStatus(WmsCodeMaster.CC_CANCEL.getCode());
            save(wmCountHeader);
            // 增加对取消盘点的判断条件，若有上次盘点单号，则把上次盘点单号的盘点单中的是否复盘号标志置为N
            BanQinWmCountHeader condition = new BanQinWmCountHeader();
            condition.setCountNo(wmCountHeader.getParentCountNo());
            condition.setOrgId(wmCountHeader.getOrgId());
            List<BanQinWmCountHeader> list = findList(condition);
            BanQinWmCountHeader newModel = CollectionUtil.isNotEmpty(list) ? list.get(0) : null;
            if (null != newModel) {
                newModel.setIsCreateCheck("N");
                save(newModel);
            }
            msg.setSuccess(true);
        } else {
            msg.setSuccess(false);
        }
        return msg;
    }

    /**
     * 生成调整单
     * @param id
     * @return
     */
    @Transactional
    public ResultMessage generateAdOrder(String id) {
        ResultMessage msg = new ResultMessage();
        try {
            BanQinWmCountHeader countModel = get(id);
            // 只有完全盘点的才能生成调整单
            if (StringUtils.isNotEmpty(id) && !(WmsCodeMaster.CC_FULL_COUNT.getCode().equals(countModel.getStatus()))) {
                msg.setSuccess(false);
                msg.setMessage(countModel.getCountNo() + "不是完全盘点状态，不能操作");
                return msg;
            }
            // 验证调整单是否已经创建，已经产生复盘任务，不能生成调整单
            BanQinWmAdHeader wmAdHeader = new BanQinWmAdHeader();
            wmAdHeader.setCountNo(countModel.getCountNo());
            wmAdHeader.setOrgId(countModel.getOrgId());
            List<BanQinWmAdHeader> list1 = wmAdHeaderService.findList(wmAdHeader);
            if (CollectionUtil.isNotEmpty(list1)) {
                msg.setSuccess(false);
                msg.setMessage("已生成调整单，不能重复生成");
                return msg;
            }
            if (countModel.getIsCreateCheck().equals("Y")) {
                msg.setSuccess(false);
                msg.setMessage("已经产生复盘任务，不能操作");
                return msg;
            }
            // 定义一个map集合不同货主生成不同的调整单
            Map<String, List<BanQinWmTaskCount>> countMap = new HashMap<>();
            // 获取有盘点差异的盘点任务号
            BanQinWmTaskCount condition = new BanQinWmTaskCount();
            condition.setCountNo(countModel.getCountNo());
            condition.setOrgId(countModel.getOrgId());
            // 已经生成序列号盘点的有盘点差异的商品不能生成调整任务
            BanQinWmCountHeader example = new BanQinWmCountHeader();
            example.setParentCountNo(countModel.getCountNo());
            example.setIsSerial("Y");
            example.setOrgId(countModel.getOrgId());
            List<BanQinWmCountHeader> list2 = findList(example);
            BanQinWmCountHeader wmCountHeaderModel = CollectionUtil.isNotEmpty(list2) ? list2.get(0) : null;
            if (wmCountHeaderModel != null) {
                condition.setIsSerial("N");
            }
            List<BanQinWmTaskCount> items = wmTaskCountMapper.getReCreateCount(condition);
            if (CollectionUtil.isNotEmpty(items)) {
                for (BanQinWmTaskCount item : items) {
                    // 将盘点任务号按货主分组
                    if (countMap.containsKey(item.getOwnerCode())) {
                        // 当map集合中存在货主的key则，value中list加上item
                        countMap.get(item.getOwnerCode()).add(item);
                    } else {
                        // 当map不存在货主的key，则创建一个list
                        List<BanQinWmTaskCount> list = new ArrayList<>();
                        list.add(item);
                        countMap.put(item.getOwnerCode(), list);
                    }
                }
            }
            Iterator<String> it = countMap.keySet().iterator();
            // 循环遍历货主
            while (it.hasNext()) {
                // 货主编码
                String key = it.next();
                List<BanQinWmTaskCount> itemList = countMap.get(key);
                // 创建调整单
                BanQinWmAdHeader wmAdHeaderModel = new BanQinWmAdHeader();
                wmAdHeaderModel.setStatus(WmsCodeMaster.AD_NEW.getCode());
                wmAdHeaderModel.setAuditStatus(WmsCodeMaster.AUDIT_NEW.getCode());
                wmAdHeaderModel.setOwnerCode(key);
                wmAdHeaderModel.setCountNo(countModel.getCountNo());
                wmAdHeaderModel.setReasonCode("CN");
                wmAdHeaderModel.setAdTime(new Date());
                wmAdHeaderModel.setOrgId(countModel.getOrgId());
                wmAdHeaderModel.setAdNo(noService.getDocumentNo(GenNoType.WM_AD_NO.name()));
                wmAdHeaderService.save(wmAdHeaderModel);
                msg.addMessage("生成调整单号" + wmAdHeaderModel.getAdNo());
                int i = 0;
                for (BanQinWmTaskCount item : itemList) {
                    // 生成调整单明细
                    BanQinWmAdDetail wmAdDetailModel = new BanQinWmAdDetail();
                    wmAdDetailModel.setAdNo(wmAdHeaderModel.getAdNo());
                    wmAdDetailModel.setLineNo(String.format("%04d", i + 1));
                    wmAdDetailModel.setLocCode(item.getLocCode());
                    wmAdDetailModel.setLotNum(item.getLotNum());
                    wmAdDetailModel.setStatus(WmsCodeMaster.TSK_NEW.getCode());
                    wmAdDetailModel.setOwnerCode(item.getOwnerCode());
                    wmAdDetailModel.setPackCode(item.getPackCode());
                    wmAdDetailModel.setUom("EA");
                    wmAdDetailModel.setQtyAdEa(Math.abs(item.getQtyDiff()));
                    wmAdDetailModel.setQtyAdUom(Math.abs(item.getQtyDiff()));
                    wmAdDetailModel.setSkuCode(item.getSkuCode());
                    wmAdDetailModel.setTraceId(item.getTraceId());
                    wmAdDetailModel.setOrgId(wmAdHeaderModel.getOrgId());
                    wmAdDetailModel.setHeaderId(wmAdHeaderModel.getId());
                    if (item.getQtyDiff() > 0) {
                        wmAdDetailModel.setAdMode(WmsCodeMaster.AD_MODE_A.getCode());
                    } else {
                        wmAdDetailModel.setAdMode(WmsCodeMaster.AD_MODE_R.getCode());
                    }
                    i++;
                    wmAdDetailService.save(wmAdDetailModel);
                }
                // 是序列号盘点，需要将序列号盘点差异生成调整单
                if (WmsConstants.YES.equals(countModel.getIsSerial())) {
                    // 获取有盘点差异的序列号
                    BanQinWmTaskCountSerial adSerialCondition = new BanQinWmTaskCountSerial();
                    adSerialCondition.setCountNo(wmAdHeaderModel.getCountNo());
                    adSerialCondition.setOwnerCode(key);
                    adSerialCondition.setOrgId(wmAdHeaderModel.getOrgId());
                    List<BanQinWmTaskCountSerial> needAdItems = wmTaskCountSerialService.findList(adSerialCondition);
                    // 插入序列号调整表
                    for (BanQinWmTaskCountSerial needAdItem : needAdItems) {
                        BanQinWmAdSerial wmAdSerialModel = new BanQinWmAdSerial();
                        wmAdSerialModel.setAdNo(wmAdHeaderModel.getAdNo());
                        wmAdSerialModel.setOwnerCode(needAdItem.getOwnerCode());
                        wmAdSerialModel.setSkuCode(needAdItem.getSkuCode());
                        wmAdSerialModel.setLotNum(needAdItem.getLotNum());
                        wmAdSerialModel.setSerialNo(needAdItem.getSerialNo());
                        wmAdSerialModel.setOrgId(wmAdHeaderModel.getOrgId());
                        if (WmsCodeMaster.LOSS.getCode().equals(needAdItem.getCountResult())) {
                            wmAdSerialModel.setAdMode(WmsCodeMaster.AD_R.getCode());
                        } else {
                            wmAdSerialModel.setAdMode(WmsCodeMaster.AD_A.getCode());
                        }
                        wmAdSerialService.save(wmAdSerialModel);
                    }
                }
            }
            if (StringUtils.isEmpty(msg.getMessage())) {
                msg.setSuccess(false);
                msg.setMessage("没有可以生成的调整单");
            }
            return msg;
        } catch (Exception e) {
            e.printStackTrace();
            msg.setSuccess(false);
            msg.addMessage(e.getMessage());
        }
        return msg;
    }

    /**
     * 盘点确认
     * @param taskEntity
     * @return
     */
    @Transactional
    public ResultMessage confirmCount(BanQinWmTaskCountEntity taskEntity) {
        ResultMessage msg = new ResultMessage();
        if (WmsCodeMaster.TSK_COMPLETE.getCode().equals(taskEntity.getStatus())) {
            msg.addMessage(taskEntity.getCountId() + "非创建状态不能操作");
            msg.setSuccess(false);
            return msg;
        }
        taskEntity.setStatus(WmsCodeMaster.TSK_COMPLETE.getCode());
        wmTaskCountService.save(taskEntity);
        BanQinWmCountHeader wmCountHeader = this.get(taskEntity.getHeaderId());
        if (wmCountHeader.getCountRange().equals("4")) {
            this.upDateLastCountTime(taskEntity);
        }
        updateCountStatus(taskEntity.getCountNo(), taskEntity.getOrgId());
        msg.addMessage("操作成功");
        msg.setSuccess(true);
        return msg;
    }

    /**
     * 保存任务表后，同时更新盘点单的盘点状态
     * @param taskEntitys
     * @return
     */
    @Transactional
    public ResultMessage confirmSelectCount(List<BanQinWmTaskCountEntity> taskEntitys) {
        ResultMessage msg = new ResultMessage();
        for (BanQinWmTaskCountEntity taskEntity : taskEntitys) {
            if (WmsCodeMaster.TSK_COMPLETE.getCode().equals(taskEntity.getStatus())) {
                msg.addMessage(taskEntity.getCountId() + "非创建状态不能操作");
                continue;
            }
            taskEntity.setStatus(WmsCodeMaster.TSK_COMPLETE.getCode());
            wmTaskCountService.saveEntity(taskEntity);
            BanQinWmCountHeader item = this.get(taskEntity.getHeaderId());
            if (item.getCountRange().equals("4")) {
                this.upDateLastCountTime(taskEntity);
            }
        }
        if (CollectionUtil.isNotEmpty(taskEntitys)) {
            BanQinWmTaskCountEntity countEntity = taskEntitys.get(0);
            updateCountStatus(countEntity.getCountNo(), countEntity.getOrgId());
        }
        if (StringUtils.isEmpty(msg.getMessage())) {
            msg.addMessage("操作成功");
            msg.setSuccess(true);
        }
        return msg;
    }

    @Transactional
    public ResultMessage addConfirmCount(BanQinWmTaskCountEntity taskEntity) {
        ResultMessage msg = new ResultMessage();
        BanQinWmCountHeader wmCountHeader = this.get(taskEntity.getHeaderId());
        if (WmsCodeMaster.CC_CREATE_TSK.getCode().equals(wmCountHeader.getStatus()) || WmsCodeMaster.CC_PART_COUNT.getCode().equals(wmCountHeader.getStatus())) {
            taskEntity.setStatus(WmsCodeMaster.TSK_COMPLETE.getCode());
            // 将查找到的需要盘点记录复制到wmTaskCountModel
            BanQinWmInvLotAtt wmInvLotAttModel = new BanQinWmInvLotAtt();
            BanQinCdWhSku skuModel = cdWhSkuService.getByOwnerAndSkuCode(taskEntity.getOwnerCode(), taskEntity.getSkuCode(), taskEntity.getOrgId());
            if (null == skuModel) {
                msg.setSuccess(false);
                msg.setMessage("商品不存在");
                return msg;
            }
            BeanUtils.copyProperties(taskEntity, wmInvLotAttModel);
            String lotNum = inventoryService.createInvLotNum(wmInvLotAttModel);
            BanQinWmTaskCount model = new BanQinWmTaskCount();
            BeanUtils.copyProperties(taskEntity, model);
            model.setId(IdGen.uuid());
            model.setIsNewRecord(true);
            model.setQtyDiff(taskEntity.getQtyCountEa());
            model.setLotNum(lotNum);
            model.setPackCode(skuModel.getPackCode());
            model.setUom(WmsConstants.UOM_EA);
            // 获取任务编号
            model.setCountId(noService.getDocumentNo(GenNoType.WM_COUNT_ID.name()));
            model.setLotAtt01(taskEntity.getLotAtt01());
            model.setLotAtt02(taskEntity.getLotAtt02());
            model.setLotAtt03(taskEntity.getLotAtt03());
            model.setLotAtt04(taskEntity.getLotAtt04());
            model.setLotAtt05(taskEntity.getLotAtt05());
            model.setLotAtt06(taskEntity.getLotAtt06());
            model.setLotAtt07(taskEntity.getLotAtt07());
            model.setLotAtt08(taskEntity.getLotAtt08());
            model.setLotAtt09(taskEntity.getLotAtt09());
            model.setLotAtt10(taskEntity.getLotAtt10());
            model.setLotAtt11(taskEntity.getLotAtt11());
            model.setLotAtt12(taskEntity.getLotAtt12());
            wmTaskCountService.save(model);
            BanQinWmCountHeader item = this.get(taskEntity.getHeaderId());
            if ("4".equals(item.getCountRange())) {
                this.upDateLastCountTime(taskEntity);
            }
            updateCountStatus(taskEntity.getCountNo(), taskEntity.getOrgId());
            msg.addMessage("操作成功");
            msg.setSuccess(true);
        } else {
            msg.setSuccess(false);
            msg.addMessage("非生成任务或非部分盘点状态，不能操作");
        }

        return msg;
    }

    @Transactional
    public void updateCountStatus(String countNo, String orgId) {
        BanQinWmCountHeader countHeader = this.findByCountNo(countNo, orgId);
        List<BanQinWmTaskCount> wmTaskCountList = wmTaskCountService.getByCountNo(countNo, countHeader.getOrgId());
        long allTask = wmTaskCountList.size();
        long countedTask = wmTaskCountList.stream().filter(w -> w.getStatus().equals(WmsCodeMaster.TSK_COMPLETE.getCode())).count();
        // 如果已经盘点的条数小于该盘点单下的任务条数，设置盘点单为部分盘点状态
        if (countedTask < allTask && countedTask != 0) {
            countHeader.setStatus(WmsCodeMaster.CC_PART_COUNT.getCode());
        } else if (countedTask < allTask && countedTask == 0) {
            countHeader.setStatus(WmsCodeMaster.CC_CREATE_TSK.getCode());
        } else if (countedTask == allTask && countedTask != 0) {
            countHeader.setStatus(WmsCodeMaster.CC_FULL_COUNT.getCode());
        }
        super.save(countHeader);
    }

    /**
     * 根更新最后盘点时间
     * @param taskEntity
     * @return
     */
    @Transactional
    public void upDateLastCountTime(BanQinWmTaskCountEntity taskEntity) {
        BanQinCdWhSku newModel = cdWhSkuService.getByOwnerAndSkuCode(taskEntity.getOwnerCode(), taskEntity.getSkuCode(), taskEntity.getOrgId());
        if (null != newModel) {
            String dateForCircle = DateUtils.getDate("yyyy-MM-dd");
            String dateModel = DateUtils.formatDate(newModel.getLastCountTime(), "yyyy-MM-dd");
            if (!dateForCircle.equals(dateModel)) {
                newModel.setLastCountTime(new Date());
                cdWhSkuService.save(newModel);
            }
        }
    }

    public List<CountTaskLabel> getCountTaskLabel(List<String> ids) {
        return mapper.getCountTaskLabel(ids);
    }

    /**
     * 序列号盘点 普通盘点任务盘点确认
     */
    @Transactional
    public ResultMessage confirmSerialCount(List<BanQinWmTaskCountEntity> taskEntitys) {
        ResultMessage msg = new ResultMessage();
        for (BanQinWmTaskCountEntity taskEntity : taskEntitys) {
            ResultMessage rstMsg = this.confirmSerialCount(taskEntity);
            if (!rstMsg.isSuccess()) {
                msg.addMessage(rstMsg.getMessage());
            }
        }
        if (StringUtils.isEmpty(msg.getMessage())) {
            msg.setSuccess(true);
            msg.addMessage("操作成功");
        }
        return msg;
    }

    /**
     * 序列号盘点确认
     */
    @Transactional
    public ResultMessage confirmSerialCount(BanQinWmTaskCountEntity taskEntity) {
        ResultMessage msg = new ResultMessage();
        // 获取盘点任务
        BanQinWmTaskCount wmTaskCountModel = this.wmTaskCountService.getEntityByCountId(taskEntity.getCountId(), taskEntity.getOrgId());
        if (wmTaskCountModel != null) {
            if (WmsCodeMaster.TSK_COMPLETE.getCode().equals(wmTaskCountModel.getStatus())) {
                msg.addMessage("[" + taskEntity.getCountId() + "]非创建状态不能操作");
                msg.setSuccess(false);
                return msg;
            }
            // 更新盘点数量和盘点差异数
            wmTaskCountModel.setStatus(WmsCodeMaster.TSK_COMPLETE.getCode());
            wmTaskCountModel.setQtyDiff(wmTaskCountModel.getQtyCountEa() - wmTaskCountModel.getQty());
            wmTaskCountService.save(wmTaskCountModel);
            // 判断序列号盘点任务表是否有创建状态,有创建的则更新盘点单状态为部分盘点
            BanQinWmTaskCountSerial condition = new BanQinWmTaskCountSerial();
            condition.setCountNo(taskEntity.getCountNo());
            condition.setStatus(WmsCodeMaster.TSK_NEW.getCode());
            condition.setOrgId(taskEntity.getOrgId());
            List<BanQinWmTaskCountSerial> items = wmTaskCountSerialService.findList(condition);
            if (items.size() > 0) {
                // 将盘点单更新为部分盘点
                this.updateStatusByCountNo(taskEntity.getCountNo(), WmsCodeMaster.CC_PART_COUNT.getCode(), taskEntity.getOrgId());
            } else {
                // 更新状态
                updateCountStatus(taskEntity.getCountNo(), taskEntity.getOrgId());
            }
        } else {
            msg.addMessage("[" + taskEntity.getCountId() + "]数据过期");
            msg.setSuccess(false);
        }
        return msg;
    }

    /**
     * 更新盘点单的状态
     */
    @Transactional
    public void updateStatusByCountNo(String countNo, String status, String orgId) {
        BanQinWmCountHeader condition = new BanQinWmCountHeader();
        condition.setCountNo(countNo);
        condition.setOrgId(orgId);
        List<BanQinWmCountHeader> list = this.findList(condition);
        if (CollectionUtil.isNotEmpty(list)) {
            BanQinWmCountHeader countHeader = list.get(0);
            countHeader.setStatus(status);
            this.save(countHeader);
        }
    }

}