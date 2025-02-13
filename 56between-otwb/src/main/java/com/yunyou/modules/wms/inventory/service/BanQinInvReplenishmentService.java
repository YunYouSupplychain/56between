package com.yunyou.modules.wms.inventory.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.modules.sys.GenNoType;
import com.yunyou.modules.sys.service.SynchronizedNoService;
import com.yunyou.modules.wms.basicdata.entity.*;
import com.yunyou.modules.wms.basicdata.service.BanQinCdRuleRotationDetailService;
import com.yunyou.modules.wms.basicdata.service.BanQinCdWhLocService;
import com.yunyou.modules.wms.basicdata.service.BanQinCdWhSkuLocService;
import com.yunyou.modules.wms.common.entity.*;
import com.yunyou.modules.wms.common.service.BanQinWmsCommonService;
import com.yunyou.modules.wms.inventory.entity.BanQinWmInvLotLoc;
import com.yunyou.modules.wms.outbound.service.BanQinOutboundCommonService;
import com.yunyou.modules.wms.task.entity.BanQinWmTaskRp;
import com.yunyou.modules.wms.task.service.BanQinWmTaskRpService;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 补货逻辑实现类
 * @author WMj
 * @version 2019/02/20
 */
@Service
public class BanQinInvReplenishmentService {
	@Autowired
	protected SynchronizedNoService noService;
	@Autowired
	protected JdbcTemplate jdbcTemplate;
	@Autowired
	protected BanQinInventoryService inventoryService;
	@Autowired
	protected BanQinWmTaskRpService wmTaskRpService;
	@Autowired
	protected BanQinCdRuleRotationDetailService cdRuleRotationDetailService;
	@Autowired
	protected BanQinOutboundCommonService outboundCommon;
	@Autowired
	protected BanQinWmsCommonService wmCommon;
	@Autowired
	protected BanQinWmInvLotLocService wmInvLotLocService;
	@Autowired
	protected BanQinCdWhLocService cdWhLocService;
	@Autowired
	protected BanQinCdWhSkuLocService cdWhSkuLocService;

    /**
     * 生成补货任务
     * @param ownerCode
     * @param skuCode
     * @return
     * @throws WarehouseException
     */
    @Transactional
	public ResultMessage createRpTask(String ownerCode, String skuCode, String orgId) throws WarehouseException {
		ResultMessage msg = new ResultMessage();
		// 查找需要补货的货主,商品,拣货位
		BanQinCdWhSkuLocEntity condition = new BanQinCdWhSkuLocEntity();
		condition.setOwnerCode(ownerCode);
		condition.setSkuCode(skuCode);
		condition.setOrgId(orgId);
		List<BanQinCdWhSkuLocEntity> items = cdWhSkuLocService.getRpSkuQuery(condition);
		if (CollectionUtil.isNotEmpty(items)) {
			for (BanQinCdWhSkuLocEntity item : items) {
				// 如果商品的拣货位配置中，没有勾选“是否从存储位补货”、没有勾选”是否从箱拣货位补货“，则不生成补货任务
				if (WmsConstants.NO.equals(item.getIsFmRs()) && WmsConstants.NO.equals(item.getIsFmCs())) {
					continue;
				}
				// 获取包装明细
				LinkedHashMap<String, BanQinCdWhPackageRelation> packMap = new LinkedHashMap<>();
                BanQinCdWhPackageEntity packageEntity = wmCommon.getCdWhPackageRelation(item.getPackCode(), orgId);
				for (BanQinCdWhPackageRelation packRelation : packageEntity.getCdWhPackageRelations()) {
					packMap.put(packRelation.getCdprUnitLevel(), packRelation);
				}
				// 定义需要补货的库存数量
				double qtyNeedRp = 0;
				// 周转规则
				List<BanQinCdRuleRotationDetail> ruleRotationDetailList = cdRuleRotationDetailService.getByRuleCode(item.getRotationRule(), orgId);
				// 需补货数=补货上限-库存可用数
				qtyNeedRp = item.getMaxLimit() - item.getQtyAvail();
				if (qtyNeedRp <= 0) {
					continue;
				}
				if (WmsConstants.NO.equals(item.getIsOverRp()) && item.getMaxLimit() < qtyNeedRp) {
					// 当配置了超量补货为N，并且库存上限<需补货数
					qtyNeedRp = item.getMaxLimit();
				}
				int quantity = packMap.get(item.getRpUom()).getCdprQuantity().intValue();
				qtyNeedRp = qtyNeedRp - qtyNeedRp % quantity;
				if (qtyNeedRp < item.getMinRp() * quantity) {// 需要补货数小于最小补货数
					continue;
				}
				// 先补货超量分配部分批次库存记录
				qtyNeedRp = replenishmentByAlloc(packMap, qtyNeedRp, item);
				if (qtyNeedRp <= 0) {
					continue;
				}
				if (item.getRotationType().equals(WmsCodeMaster.ROTATION.getCode())) {
					// 库存周转优先
					replenishmentByRotation(ruleRotationDetailList, packMap, qtyNeedRp, item);
				} else {
					// 包装优先
					replenishmentByPackage(ruleRotationDetailList, packMap, qtyNeedRp, item);
				}
			}
			msg.setSuccess(true);
			msg.setMessage("操作成功");
		} else {
			msg.setSuccess(false);
			msg.setMessage("没有需要补货的商品");
		}
		return msg;
	}

    /**
     * 取消补货
     * @param wmTaskRpModel
     * @return
     * @throws WarehouseException
     */
    @Transactional
	public void cancelRpTask(BanQinWmTaskRp wmTaskRpModel) throws WarehouseException {
		// set源补货信息
		BanQinInventoryEntity fminvm = setCompleteToinvm(wmTaskRpModel);
		fminvm.setAction(ActionCode.CANCEL_RP_TASK);
		// set目标补货信息
        BanQinInventoryEntity toinvm = setCompleteFminvm(wmTaskRpModel);
		toinvm.setAction(ActionCode.CANCEL_RP_TASK);
		// 更新库存
		this.inventoryService.updateInventory(fminvm, toinvm);
		this.wmTaskRpService.delete(wmTaskRpModel);
	}

    /**
     * 补货完成
     * @param wmTaskRpModel
     * @return
     * @throws WarehouseException
     */
    @Transactional
	public void completeRpTask(BanQinWmTaskRp wmTaskRpModel) throws WarehouseException {
		// set源补货信息
        BanQinInventoryEntity fminvm = setCompleteFminvm(wmTaskRpModel);
		// set目标补货信息
        BanQinInventoryEntity toinvm = setCompleteToinvm(wmTaskRpModel);
		// 更新库存
		this.inventoryService.updateInventory(fminvm, toinvm);
		// 更新任务表状态
		wmTaskRpModel.setStatus(WmsCodeMaster.TSK_COMPLETE.getCode());
		this.wmTaskRpService.save(wmTaskRpModel);
	}

    /**
     * 收集补货到目标库位的toinvm
     * @param item
     * @param fminvm
     * @return
     */
	private BanQinInventoryEntity setToinvm(BanQinCdWhSkuLocEntity item, BanQinInventoryEntity fminvm) {
        BanQinInventoryEntity toinvm = new BanQinInventoryEntity();
		toinvm.setLotNum(fminvm.getLotNum());
		toinvm.setLocCode(item.getLocCode());
		toinvm.setTraceId(fminvm.getTraceId());
		toinvm.setSkuCode(fminvm.getSkuCode());
		toinvm.setOwnerCode(fminvm.getOwnerCode());
		toinvm.setAction(ActionCode.CREATE_RP_TASK);
		toinvm.setQtyEaOp(fminvm.getQtyEaOp());
        toinvm.setOrgId(fminvm.getOrgId());
		return toinvm;
	}

    /**
     * set补货任务表记录的信息
     * @param item
     * @param fminvm
     * @param packItem
     * @return
     */
	private BanQinWmTaskRp setWmTaskRp(BanQinCdWhSkuLocEntity item, BanQinInventoryEntity fminvm, BanQinCdWhPackageRelation packItem) {
		BanQinWmTaskRp wmTaskRpModel = new BanQinWmTaskRp();
		wmTaskRpModel.setFmId(fminvm.getTraceId());
		wmTaskRpModel.setFmLoc(fminvm.getLocCode());
		wmTaskRpModel.setLotNum(fminvm.getLotNum());
		wmTaskRpModel.setOwnerCode(fminvm.getOwnerCode());
        wmTaskRpModel.setRpId(noService.getDocumentNo(GenNoType.WM_RP_ID.name()));
		wmTaskRpModel.setToLoc(item.getLocCode());
		wmTaskRpModel.setStatus(WmsCodeMaster.TSK_NEW.getCode());
		wmTaskRpModel.setSkuCode(fminvm.getSkuCode());
		wmTaskRpModel.setUom(packItem.getCdprUnitLevel());
		wmTaskRpModel.setPackCode(item.getPackCode());
		wmTaskRpModel.setQtyRpEa(fminvm.getQtyEaOp());
		wmTaskRpModel.setQtyRpUom(fminvm.getQtyEaOp() / packItem.getCdprQuantity());
        wmTaskRpModel.setOrgId(fminvm.getOrgId());
		return wmTaskRpModel;
	}

    /**
     * 收集补货完成源库位信息
     * @param wmTaskRpModel
     * @return
     */
	private BanQinInventoryEntity setCompleteFminvm(BanQinWmTaskRp wmTaskRpModel) {
        BanQinInventoryEntity fminvm = new BanQinInventoryEntity();
		fminvm.setLotNum(wmTaskRpModel.getLotNum());
		fminvm.setLocCode(wmTaskRpModel.getFmLoc());
		fminvm.setTraceId(wmTaskRpModel.getFmId());
		fminvm.setSkuCode(wmTaskRpModel.getSkuCode());
		fminvm.setOwnerCode(wmTaskRpModel.getOwnerCode());
		fminvm.setAction(ActionCode.REPLENISHMENT);
		fminvm.setOrderNo(wmTaskRpModel.getRpId());
		fminvm.setPackCode(wmTaskRpModel.getPackCode());
		fminvm.setQtyUom(wmTaskRpModel.getQtyRpUom());
		fminvm.setUom(wmTaskRpModel.getUom());
		fminvm.setTaskId(wmTaskRpModel.getRpId());
		fminvm.setQtyEaOp(wmTaskRpModel.getQtyRpEa());
        fminvm.setOrgId(wmTaskRpModel.getOrgId());
		return fminvm;
	}

    /**
     * 收集补货完成源库位信息
     * @param wmTaskRpModel
     * @return
     */
	private BanQinInventoryEntity setCompleteToinvm(BanQinWmTaskRp wmTaskRpModel) {
        BanQinInventoryEntity toinvm = new BanQinInventoryEntity();
		toinvm.setLotNum(wmTaskRpModel.getLotNum());
		toinvm.setLocCode(wmTaskRpModel.getToLoc());
		toinvm.setTraceId(wmTaskRpModel.getToId());
		toinvm.setSkuCode(wmTaskRpModel.getSkuCode());
		toinvm.setOwnerCode(wmTaskRpModel.getOwnerCode());
		toinvm.setAction(ActionCode.REPLENISHMENT);
		toinvm.setOrderNo(wmTaskRpModel.getRpId());
		toinvm.setPackCode(wmTaskRpModel.getPackCode());
		toinvm.setQtyUom(wmTaskRpModel.getQtyRpUom());
		toinvm.setUom(wmTaskRpModel.getUom());
		toinvm.setTaskId(wmTaskRpModel.getRpId());
		toinvm.setQtyEaOp(wmTaskRpModel.getQtyRpEa());
        toinvm.setOrgId(wmTaskRpModel.getOrgId());
		return toinvm;
	}

	private List<BanQinWmInvLotLoc> getInvLotLocEntityList(List<Map<String, Object>> objList) {
		List<BanQinWmInvLotLoc> invLotLocList = Lists.newArrayList();
		for (Map<String, Object> map : objList) {
            BanQinWmInvLotLoc wmInvLotLocModel = CollectionUtil.mapToJavaBean(map, BanQinWmInvLotLoc.class);
			invLotLocList.add(wmInvLotLocModel);
		}
		return invLotLocList;
	}

    /**
     * 补货超量分配的批次库存
     * @param packMap
     * @param qtyNeedRp
     * @param item
     * @return
     */
    @Transactional
	public Double replenishmentByAlloc(LinkedHashMap<String, BanQinCdWhPackageRelation> packMap, Double qtyNeedRp, BanQinCdWhSkuLocEntity item) {
		ResultMessage msg = new ResultMessage();
		// 实际补货数量
		double qtyActually = 0;
		// 1、获取超量分配的
		List<BanQinWmInvLotLoc> invLotLocList = wmInvLotLocService.getByOwnerAndSkuAndLocCode(item.getOwnerCode(), item.getSkuCode(), item.getLocCode(), item.getOrgId());
		for (BanQinWmInvLotLoc wmInvLotLoc : invLotLocList) {
			// 当拣货库位中库存可用数大于零的时候，这个批次库存需要优先补货
			if (wmInvLotLoc.getQty() - wmInvLotLoc.getQtyPk() - wmInvLotLoc.getQtyAlloc() - wmInvLotLoc.getQtyMvOut() - wmInvLotLoc.getQtyPaOut()
                    - wmInvLotLoc.getQtyRpOut() + wmInvLotLoc.getQtyRpIn() + wmInvLotLoc.getQtyPaIn() + wmInvLotLoc.getQtyMvIn() < 0) {
                BanQinWmInvLotLoc lotexample = new BanQinWmInvLotLoc();
				lotexample.setOwnerCode(item.getOwnerCode());
				lotexample.setSkuCode(item.getSkuCode());
				lotexample.setLotNum(wmInvLotLoc.getLotNum());
                lotexample.setOrgId(item.getOrgId());
				List<BanQinWmInvLotLoc> invLotLocModels = wmInvLotLocService.findList(lotexample);
				Iterator<String> packIt = packMap.keySet().iterator();
				// 循环包装
				while (packIt.hasNext()) {
					String packKey = packIt.next();
                    BanQinCdWhPackageRelation packItem = packMap.get(packKey);
					if (Integer.parseInt(packItem.getCdprSequencesNo()) < Integer.parseInt(packMap.get(item.getRpUom()).getCdprSequencesNo())) {
						continue;
					}
					List<BanQinWmInvLotLoc> tmpList = new ArrayList<>();
					for (BanQinWmInvLotLoc wmInvLotLocModel : invLotLocModels) {
						if (wmInvLotLocModel.getLocCode().equals(item.getLocCode())) {
							continue;
						}
						// 商品拣货位的库位类型限制
						BanQinCdWhLoc locExample = new BanQinCdWhLoc();
						locExample.setLocCode(wmInvLotLocModel.getLocCode());
                        locExample.setOrgId(wmInvLotLocModel.getOrgId());
                        BanQinCdWhLoc cdWhLocModel = cdWhLocService.findFirst(locExample);
						// 配置了允许从存储位补货，且该库存记录的库位使用类型为存储位，那么继续执行下面操作
						if (WmsConstants.YES.equals(item.getIsFmRs()) && WmsCodeMaster.LOC_USE_RS.getCode().equals(cdWhLocModel.getLocUseType())) {
							// 配置了允许从箱拣货位补货，且该库存记录的库位使用类型为箱拣货位，那么继续执行下面操作
						} else if (WmsConstants.YES.equals(item.getIsFmCs()) && WmsCodeMaster.LOC_USE_CS.getCode().equals(cdWhLocModel.getLocUseType())) {
							// 否则，不执行以下操作
						} else {
							continue;
						}
						Double curQtyNeedRp = qtyNeedRp - qtyActually;
						if (curQtyNeedRp > 0) {// 实际要补货数小于需要补货数
							// 获取可用库存
							Double qtyInvAvail = wmInvLotLocModel.getQty() - wmInvLotLocModel.getQtyAlloc() - wmInvLotLocModel.getQtyHold() - wmInvLotLocModel.getQtyMvOut()
									- wmInvLotLocModel.getQtyPk() - wmInvLotLocModel.getQtyPaOut() - wmInvLotLocModel.getQtyRpOut();
							Double qtyAllocUom = wmCommon.doubleDivide(curQtyNeedRp, packItem.getCdprQuantity());
							if (qtyAllocUom < 1D) {// 无可分配整单位数量
								continue;
							}
							Double qtyEaOp = outboundCommon.qtyEaOpByUom(curQtyNeedRp, qtyInvAvail, packItem.getCdprQuantity().intValue());
							if (qtyEaOp < 1D) {
								continue;
							}
							BanQinInventoryEntity fminvm = new BanQinInventoryEntity();
							BeanUtils.copyProperties(wmInvLotLocModel, fminvm);
							fminvm.setAction(ActionCode.CREATE_RP_TASK);
							fminvm.setQtyEaOp(qtyEaOp);
							// 收集补货到目标库位的toinvm
                            BanQinInventoryEntity toinvm = setToinvm(item, fminvm);
							// 更新库存
							BanQinInventoryEntity toEntity = this.inventoryService.updateInventory(fminvm, toinvm);
							qtyActually = qtyActually + qtyEaOp;
							if (qtyEaOp < qtyInvAvail) {
								wmInvLotLocModel.setQty(wmInvLotLocModel.getQty() - qtyEaOp);
							} else {
								tmpList.add(wmInvLotLocModel);
							}
							// 收集补货任务表
							BanQinWmTaskRp wmTaskRpModel = setWmTaskRp(item, fminvm, packItem);
							wmTaskRpModel.setToId(toEntity.getTraceId());
							// 保存补货任务表
							this.wmTaskRpService.save(wmTaskRpModel);
						} else {
							break;
						}
					}
					invLotLocModels.removeAll(tmpList);
				}
			}
		}
		qtyNeedRp = qtyNeedRp - qtyActually;
		return qtyNeedRp;
	}

    /**
     * 补货 库存周转优先
     * @param ruleRotationDetailList
     * @param packMap
     * @param qtyNeedRp
     * @param item
     * @return
     */
    @Transactional
	public ResultMessage replenishmentByRotation(List<BanQinCdRuleRotationDetail> ruleRotationDetailList, LinkedHashMap<String, BanQinCdWhPackageRelation> packMap, Double qtyNeedRp,
                                                    BanQinCdWhSkuLocEntity item) {
        ResultMessage msg = new ResultMessage();
		// 1、获取批次库位库存
		List<BanQinWmInvLotLoc> invLotLocList = getInvLotLoc(item, ruleRotationDetailList);
		//
		LinkedHashMap<String, List<BanQinWmInvLotLoc>> lotMap = new LinkedHashMap<>();
		if (invLotLocList == null || invLotLocList.size() == 0) {
			// 无可补货记录
			return msg;
		} else {
			for (BanQinWmInvLotLoc wmInvLotLocModel : invLotLocList) {
				if (lotMap.containsKey(wmInvLotLocModel.getLotNum())) {
					lotMap.get(wmInvLotLocModel.getLotNum()).add(wmInvLotLocModel);
				} else {
					List<BanQinWmInvLotLoc> wmInvLotLocModels = new ArrayList<>();
					wmInvLotLocModels.add(wmInvLotLocModel);
					lotMap.put(wmInvLotLocModel.getLotNum(), wmInvLotLocModels);
				}
			}
		}
		double qtyActually = 0;
		Iterator<String> lotIt = lotMap.keySet().iterator();
		// 循环批次所对应的库存记录
		while (lotIt.hasNext()) {
			String lotKey = lotIt.next();
			Iterator<String> packIt = packMap.keySet().iterator();
			// 循环包装
			while (packIt.hasNext()) {
				String packKey = packIt.next();
                BanQinCdWhPackageRelation packItem = packMap.get(packKey);
				if (Integer.parseInt(packItem.getCdprSequencesNo()) < Integer.parseInt(packMap.get(item.getRpUom()).getCdprSequencesNo())) {
					continue;
				}
				List<BanQinWmInvLotLoc> tmpList = new ArrayList<>();
				// 循环批次库位库存
				for (BanQinWmInvLotLoc wmInvLotLocModel : lotMap.get(lotKey)) {
					Double curQtyNeedRp = qtyNeedRp - qtyActually;
					if (curQtyNeedRp > 0) {// 实际要补货数小于需要补货数
						// 获取可用库存
						Double qtyInvAvail = wmInvLotLocModel.getQty() - wmInvLotLocModel.getQtyAlloc() - wmInvLotLocModel.getQtyHold() - wmInvLotLocModel.getQtyMvOut() - wmInvLotLocModel.getQtyPk()
								- wmInvLotLocModel.getQtyPaOut() - wmInvLotLocModel.getQtyRpOut();
						Double qtyAllocUom = wmCommon.doubleDivide(curQtyNeedRp, packItem.getCdprQuantity());
						if (qtyAllocUom < 1D) {// 无可分配整单位数量
							continue;
						}
						Double qtyEaOp = outboundCommon.qtyEaOpByUom(curQtyNeedRp, qtyInvAvail, packItem.getCdprQuantity().intValue());
						if (qtyEaOp < 1D) {
							continue;
						}
						BanQinInventoryEntity fminvm = new BanQinInventoryEntity();
						BeanUtils.copyProperties(wmInvLotLocModel, fminvm);
						fminvm.setAction(ActionCode.CREATE_RP_TASK);
						fminvm.setQtyEaOp(qtyEaOp);
						// 收集补货到目标库位的toinvm
                        BanQinInventoryEntity toinvm = setToinvm(item, fminvm);
						// 更新库存
						BanQinInventoryEntity toEntity = this.inventoryService.updateInventory(fminvm, toinvm);
						qtyActually = qtyActually + qtyEaOp;
						if (qtyEaOp < qtyInvAvail) {
							wmInvLotLocModel.setQty(wmInvLotLocModel.getQty() - qtyEaOp);
						} else {
							tmpList.add(wmInvLotLocModel);
						}
						// 收集补货任务表
						BanQinWmTaskRp wmTaskRpModel = setWmTaskRp(item, fminvm, packItem);
						wmTaskRpModel.setToId(toEntity.getTraceId());
						// 保存补货任务表
						this.wmTaskRpService.save(wmTaskRpModel);
					} else {
						break;
					}
				}
				lotMap.get(lotKey).removeAll(tmpList);
				invLotLocList.removeAll(tmpList);
			}
		}
		return msg;

	}

    /**
     * 补货 包装优先
     * @param ruleRotationDetailList
     * @param packMap
     * @param qtyNeedRp
     * @param item
     * @return
     */
    @Transactional
	public ResultMessage replenishmentByPackage(List<BanQinCdRuleRotationDetail> ruleRotationDetailList, LinkedHashMap<String, BanQinCdWhPackageRelation> packMap, Double qtyNeedRp, BanQinCdWhSkuLocEntity item) {
        ResultMessage msg = new ResultMessage();
		// 1、获取批次库位库存
		List<BanQinWmInvLotLoc> invLotLocList = getInvLotLoc(item, ruleRotationDetailList);
		if (invLotLocList.size() == 0) {
			// 无可分配记录
			return msg;
		}
		double qtyActually = 0;
		// 循环包装
		Iterator<String> it = packMap.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			BanQinCdWhPackageRelation packItem = packMap.get(key);
			if (Integer.valueOf(packItem.getCdprSequencesNo()) < Integer.valueOf(packMap.get(item.getRpUom()).getCdprSequencesNo())) {
				continue;
			}
			List<BanQinWmInvLotLoc> tmpList = new ArrayList<>();
			// 3、调用循环批次库位库存更新
			for (BanQinWmInvLotLoc wmInvLotLocModel : invLotLocList) {
				Double curQtyNeedRp = qtyNeedRp - qtyActually;
				if (curQtyNeedRp > 0) {// 实际要补货数小于需要补货数
					// 获取可用库存
					Double qtyInvAvail = wmInvLotLocModel.getQty() - wmInvLotLocModel.getQtyAlloc() - wmInvLotLocModel.getQtyHold() - wmInvLotLocModel.getQtyMvOut() - wmInvLotLocModel.getQtyPk()
							- wmInvLotLocModel.getQtyPaOut() - wmInvLotLocModel.getQtyRpOut();
					Double qtyAllocUom = wmCommon.doubleDivide(curQtyNeedRp, packItem.getCdprQuantity().doubleValue());
					if (qtyAllocUom < 1D) {// 无可分配整单位数量
						continue;
					}
					Double qtyEaOp = outboundCommon.qtyEaOpByUom(curQtyNeedRp, qtyInvAvail, packItem.getCdprQuantity().intValue());
					if (qtyEaOp < 1D) {
						continue;
					}
					BanQinInventoryEntity fminvm = new BanQinInventoryEntity();
					BeanUtils.copyProperties(wmInvLotLocModel, fminvm);
					fminvm.setAction(ActionCode.CREATE_RP_TASK);
					fminvm.setQtyEaOp(qtyEaOp);
					// 收集补货到目标库位的toinvm
                    BanQinInventoryEntity toinvm = setToinvm(item, fminvm);
					// 更新库存
					BanQinInventoryEntity toEntity = this.inventoryService.updateInventory(fminvm, toinvm);
					qtyActually = qtyActually + qtyEaOp;
					if (qtyEaOp < qtyInvAvail) {
						wmInvLotLocModel.setQty(wmInvLotLocModel.getQty() - qtyEaOp);
					} else {
						tmpList.add(wmInvLotLocModel);
					}
					// 收集补货任务表
					BanQinWmTaskRp wmTaskRpModel = setWmTaskRp(item, fminvm, packItem);
					wmTaskRpModel.setToId(toEntity.getTraceId());
					// 保存补货任务表
					this.wmTaskRpService.save(wmTaskRpModel);
				} else {
					break;
				}
			}
			invLotLocList.removeAll(tmpList);
		}
		return msg;
	}

	private List<BanQinWmInvLotLoc> getInvLotLoc(BanQinCdWhSkuLocEntity item, List<BanQinCdRuleRotationDetail> ruleRotationDetailList) {
		StringBuilder sql = new StringBuilder();
		sql.append(outboundCommon.getInvLotLocSql(item.getOwnerCode(), item.getSkuCode(), item.getOrgId()));
		if (WmsConstants.YES.equals(item.getIsFmRs()) && !WmsConstants.YES.equals(item.getIsFmCs())) {
			sql.append(" and cwl.loc_use_type = '").append(WmsCodeMaster.LOC_USE_RS.getCode()).append("'");
		}
		if (WmsConstants.YES.equals(item.getIsFmCs()) && !WmsConstants.YES.equals(item.getIsFmRs())) {
			sql.append(" and cwl.loc_use_type = '").append(WmsCodeMaster.LOC_USE_CS.getCode()).append("'");
		}
		if (WmsConstants.YES.equals(item.getIsFmRs()) && WmsConstants.YES.equals(item.getIsFmCs())) {
			sql.append(" and (cwl.loc_use_type = '").append(WmsCodeMaster.LOC_USE_CS.getCode())
			   .append("' or cwl.loc_use_type = '").append(WmsCodeMaster.LOC_USE_RS.getCode()).append("') ");
		}
		BanQinCdRuleRotationSqlParamEntity cdRuleRotationSqlParamEntity = new BanQinCdRuleRotationSqlParamEntity();
		cdRuleRotationSqlParamEntity.setRuleRotationDetailList(ruleRotationDetailList);
		BanQinCdRuleRotationSqlEntity sqlRotationEntity = wmCommon.getRuleRotationSql(cdRuleRotationSqlParamEntity);
		// 库存周转规则查询条件
		if (StringUtils.isNotEmpty(sqlRotationEntity.getWhereSql())) {
			sql.append(sqlRotationEntity.getWhereSql());
		}
		sql.append(" order by ");
		// 库存周转规则排序
		if (StringUtils.isNotEmpty(sqlRotationEntity.getOrderBySql())) {
			sql.append(sqlRotationEntity.getOrderBySql());
			// 拣货顺序排序
			sql.append("," + getInvLotLocOrderBySql("Y"));
		} else {
			sql.append(getInvLotLocOrderBySql("Y"));
		}
		List<Map<String, Object>> objList = jdbcTemplate.queryForList(sql.toString());
		return getInvLotLocEntityList(objList);
	}

    /**
     * 清仓优先排序，以及拣货顺序排序
     * @param isClearFirst
     * @return
     */
	private String getInvLotLocOrderBySql(String isClearFirst) {
		StringBuilder sql = new StringBuilder();
		// 是否清仓优先
		if (isClearFirst.equals(WmsConstants.YES)) {
			sql.append(" (will.qty - will.qty_hold - will.qty_alloc - will.qty_pk - will.qty_pa_out - will.qty_rp_out - will.qty_mv_out) ");
			sql.append(" asc ");
		}
		// 如果不清仓优先，那么只按拣货库位排序
		// 按拣货库位排序
		if (sql.length() > 0) {
			sql.append(",");
		}
		sql.append(" cwl.pk_seq asc");
		return sql.toString();
	}

}