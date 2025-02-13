package com.yunyou.modules.wms.outbound.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdRuleAllocDetail;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhPackageEntity;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhSku;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhSkuLoc;
import com.yunyou.modules.wms.basicdata.service.BanQinCdRuleAllocDetailService;
import com.yunyou.modules.wms.basicdata.service.BanQinCdWhSkuLocService;
import com.yunyou.modules.wms.basicdata.service.BanQinCdWhSkuService;
import com.yunyou.modules.wms.common.entity.ActionCode;
import com.yunyou.modules.wms.common.entity.BanQinCdWhPackageRelationEntity;
import com.yunyou.modules.wms.common.entity.WarehouseException;
import com.yunyou.modules.wms.common.entity.WmsConstants;
import com.yunyou.modules.wms.common.service.BanQinWmsCommonService;
import com.yunyou.modules.wms.inventory.service.BanQinInvReplenishmentService;
import com.yunyou.modules.wms.outbound.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 两步分配 
 * @author WMJ
 * @version 2019/02/28
 */
@Service
public class BanQinOutboundBatchAllocTwoStepAction {
	// 分配
	@Autowired
	protected BanQinOutboundAllocService outboundAllocService;
	// 公共方法
	@Autowired
	protected BanQinWmsCommonService wmCommon;
	// 出库公共方法
	@Autowired
	protected BanQinOutboundCommonService outboundCommon;
	// 出库单
	@Autowired
	protected BanQinWmSoHeaderService wmSoHeaderService;
	// 出库单行
	@Autowired
	protected BanQinWmSoDetailService wmSoDetailService;
	// 预配明细
	@Autowired
	protected BanQinWmSoPreallocService wmSoPreallocService;
	// 分配规则
	@Autowired
	protected BanQinCdRuleAllocDetailService cdRuleAllocDetailService;
	// 商品
	@Autowired
	protected BanQinCdWhSkuService cdWhSkuService;
	// 商品拣货区
	@Autowired
	protected BanQinCdWhSkuLocService cdWhSkuLocService;
	// 补货
	@Autowired
	protected BanQinInvReplenishmentService invReplenishment;

    /**
     * 两步分配 按波次号、按出库单号、按出库单+行号
     * @param processByCode
     * @param noList
     * @return
     */
	public ResultMessage outboundBatchAllocTwoStep(String processByCode, List<String> noList, String orgId) {
        ResultMessage msg = new ResultMessage();
		// 1、拦截、冻结状态校验
		// 拦截状态校验
        ResultMessage message = wmSoHeaderService.checkBatchInterceptStatus(processByCode, noList, orgId);
		if (!message.isSuccess()) {
			msg.addMessage(message.getMessage());
		}
		// 冻结状态校验
		message = wmSoHeaderService.checkBatchHoldStatus(processByCode, noList, orgId);
		if (!message.isSuccess()) {
			msg.addMessage(message.getMessage());
		}
		// 2、获取预配明细，过滤出库单行取消状态，出库单的取消、关闭状态、拦截、冻结状态
		List<BanQinWmSoPreallocEntity> soPreallocEntityList = wmSoPreallocService.getEntityByProcessByCode(processByCode, noList, orgId);
		if (soPreallocEntityList == null) {
			// 没有可以分配的预配明细(没有可以操作的预配明细)
			msg.addMessage("没有可以分配的预配明细");
			msg.setSuccess(false);
			return msg;
		}
		try {
			// 3、循环预配明细
			for (BanQinWmSoPreallocEntity soPreallocEntity : soPreallocEntityList) {
				try {
					// 预配数量<=0
					if (soPreallocEntity.getQtyPreallocEa() <= 0D) {
						continue;
					}
					// 分配前基本数据获取与校验
					// 1、出库单-分配明细需要获取收货人
					BanQinWmSoHeader wmSoHeaderModel = wmSoHeaderService.findBySoNo(soPreallocEntity.getSoNo(), soPreallocEntity.getOrgId());
					// 2、出库单行
					BanQinWmSoDetail wmSoDetailModel = wmSoDetailService.findBySoNoAndLineNo(soPreallocEntity.getSoNo(), soPreallocEntity.getLineNo(), soPreallocEntity.getOrgId());
					// 3、分配规则明细获取
					List<BanQinCdRuleAllocDetail> ruleAllocDetailList = cdRuleAllocDetailService.getByRuleCode(wmSoDetailModel.getAllocRule(), wmSoDetailModel.getOrgId());
					if (ruleAllocDetailList.size() == 0) {
						// 查询不到分配规则{0}明细
						msg.addMessage("查询不到分配规则" + wmSoDetailModel.getAllocRule());
						msg.setSuccess(false);
						return msg;
					}
					// 4、包装单位换算信息获取
                    BanQinCdWhPackageEntity packageEntity = wmCommon.getCdWhPackageRelation(wmSoDetailModel.getPackCode(), wmSoDetailModel.getOrgId());
					// 5、商品信息获取，主要用于出库效期校验
					BanQinCdWhSku cdWhSkuModel = cdWhSkuService.getByOwnerAndSkuCode(wmSoDetailModel.getOwnerCode(), wmSoDetailModel.getSkuCode(), wmSoDetailModel.getOrgId());
					BanQinAllocForAllocRuleEntity entity = new BanQinAllocForAllocRuleEntity();
					entity.setWmSoDetailModel(wmSoDetailModel);// 出库单行
					entity.setRuleAllocDetailList(ruleAllocDetailList);// 分配规则明细
					entity.setPackageRelationList(packageEntity.getCdWhPackageRelations());// 包装单位换算信息
					entity.setCdWhSkuModel(cdWhSkuModel);// 商品
					entity.setWmSoPreallocEntity(soPreallocEntity);// 预配明细
					entity.setConsigneeCode(wmSoHeaderModel.getConsigneeCode());// 收货人
					entity.setTrackingNo(wmSoHeaderModel.getTrackingNo());// 快递单号
					BanQinCdWhPackageRelationEntity packageRelation = wmCommon.getPackageRelationAndQtyUom(packageEntity.getCdWhPackageRelations(), soPreallocEntity.getUom(), null);
					entity.setQtyPackUom(packageRelation.getQtyUom().doubleValue());// 预配明细单位换算数量

					// 调用按库存周转优先 分配
					msg = allocByRotation(entity);
					if (!msg.isSuccess()) {
						msg.setSuccess(false);
					}
				} catch (WarehouseException e) {
					msg.addMessage(e.getMessage());
					msg.setSuccess(false);
				}
			}
		} catch (Throwable e) {
			// 框架控制，如数据过期，抛出提示
			throw new RuntimeException(e);
		} finally {
			if (StringUtils.isNotEmpty(msg.getMessage())) {
				msg.addMessage("操作成功");
			}
		}

		return msg;
	}

    /**
     * 按库存周转优先 分配
     * @param entity
     * @return
     */
	public ResultMessage allocByRotation(BanQinAllocForAllocRuleEntity entity) {
        ResultMessage msg = new ResultMessage();
		BanQinWmSoPreallocEntity soPreallocEntity = entity.getWmSoPreallocEntity();
		List<BanQinCdRuleAllocDetail> ruleAllocDetailList = entity.getRuleAllocDetailList();
		BanQinWmSoDetail wmSoDetailModel = entity.getWmSoDetailModel();
		// 可分配数量，两步分配的分配数量=预配明细的预配数量
		Double qtyAllocEa = soPreallocEntity.getQtyPreallocEa();
		// 循环分配明细
		for (BanQinCdRuleAllocDetail ruleAllocDetailModel : ruleAllocDetailList) {
			try {
				// 按单位(PL\CS\IP\EA) 获取包装单位换算信息
				BanQinCdWhPackageRelationEntity packageEntity = null;
				try {
					// 按单位(PL\CS\IP\EA) 获取包装单位换算信息
					packageEntity = wmCommon.getPackageRelationAndQtyUom(entity.getPackageRelationList(), ruleAllocDetailModel.getUom(), qtyAllocEa);
				} catch (WarehouseException e) {
					// 查询不到包装单位，不抛提示，接着往下执行
					continue;
				}
				// 分配单位数量 = 可分配数量EA/分配明细单位换算数量 => 可分配数量EA 根据分配规则明细单位 换算单位数量
				Double qtyAllocUom = packageEntity.getQtyUom();
				// 不及一单位数量，循环下一个分配单位
				if (qtyAllocUom < 1D) {
					continue;
				}
				// 获取批次库位库存 入参
				BanQinAllocInvLotLocSqlParamlEntity invSqlParamEntity = new BanQinAllocInvLotLocSqlParamlEntity();
				invSqlParamEntity.setCdWhSkuModel(entity.getCdWhSkuModel());// 商品，主要用于校验出库效期
				invSqlParamEntity.setOwnerCode(wmSoDetailModel.getOwnerCode());// 货主
				invSqlParamEntity.setSkuCode(wmSoDetailModel.getSkuCode());// 商品
				invSqlParamEntity.setLotNum(entity.getWmSoPreallocEntity().getLotNum());// 批次号
				invSqlParamEntity.setAreaCode(wmSoDetailModel.getAreaCode());// 出库单行区域
				invSqlParamEntity.setZoneCode(wmSoDetailModel.getZoneCode());// 出库单行拣货区
				invSqlParamEntity.setLocCode(wmSoDetailModel.getLocCode());// 出库单行拣货位
				invSqlParamEntity.setTraceId(wmSoDetailModel.getTraceId());// 出库单行跟踪号
				invSqlParamEntity.setLocUseType(ruleAllocDetailModel.getLocUseType());// 库位使用类型
				invSqlParamEntity.setIsClearFirst(ruleAllocDetailModel.getIsClearFirst());// 清仓优先
				invSqlParamEntity.setQtyPackUom(packageEntity.getCdprQuantity().intValue());// 单位换算数量，用于过滤不足分配的库存记录
				// 商品固定拣货位
				BanQinCdWhSkuLoc cdWhSkuLocModel = null;
				if (ruleAllocDetailModel.getSkuLocType() != null) {
					// 商品拣货位
					cdWhSkuLocModel = cdWhSkuLocService.getCdWhSkuLocType(wmSoDetailModel.getOwnerCode(), wmSoDetailModel.getSkuCode(), ruleAllocDetailModel.getSkuLocType(), wmSoDetailModel.getOrgId());
					if (cdWhSkuLocModel == null) {
						// 分配规则配置商品固定拣货位，但商品未配置商品拣货位，则继续执行下一条分配规则明细
						continue;
					}
					invSqlParamEntity.setSkuLocCode(cdWhSkuLocModel.getLocCode());// 商品固定拣货位
				}
				// 获取批次库位库存
				List<BanQinAllocInvLotLocEntity> invLotLocList = outboundCommon.getInvLotLocByRotation(invSqlParamEntity, wmSoDetailModel.getOrgId());
				// 循环库存更新
				for (BanQinAllocInvLotLocEntity invLotLocEntity : invLotLocList) {
					qtyAllocUom = wmCommon.doubleDivide(qtyAllocEa, packageEntity.getCdprQuantity().doubleValue());
					if (qtyAllocUom < 1D) {// 无可分配整单位数量
						break;
					}
					try {
						BanQinAllocUpdateEntity updateEntity = new BanQinAllocUpdateEntity();
						updateEntity.setActionCode(ActionCode.ALLOCATION);// 两步分配
						updateEntity.setPreallocId(soPreallocEntity.getPreallocId());// 预配ID
						updateEntity.setSoNo(wmSoDetailModel.getSoNo());// 出库单
						updateEntity.setLineNo(wmSoDetailModel.getLineNo());// 出库单行号
						updateEntity.setWaveNo(soPreallocEntity.getWaveNo());// 波次号
						updateEntity.setPackageEntity(packageEntity);// 包装
						updateEntity.setConsigneeCode(entity.getConsigneeCode());// 收货人
						updateEntity.setTrackingNo(entity.getTrackingNo());// 快递单号
						updateEntity.setInvLotLocEntity(invLotLocEntity);// 库存
						updateEntity.setQtyAllocEa(qtyAllocEa);// 分配数量
						updateEntity.setQtyPackUom(entity.getQtyPackUom());// 预配明细单位换算数量
						// 分配更新，并且返回 剩余可分配数量
						qtyAllocEa = outboundAllocService.updateAllocTwoStep(updateEntity);
					} catch (WarehouseException e) {
						msg.addMessage(e.getMessage());
						msg.setSuccess(false);
					}
				}
				/***************************************************************
				 * 2、 超量分配逻辑开始
				 **************************************************************/
				// 如果没有配置固定拣货位，那么不能进行超量分配
				if (ruleAllocDetailModel.getSkuLocType() == null) {
					continue;
				}
				// 如果不允许超量分配，那么退出
				if (cdWhSkuLocModel == null || cdWhSkuLocModel.getIsOverAlloc().equals(WmsConstants.NO)) {
					continue;
				}
				// 如果无剩余可分配数量，那么退出
				if (qtyAllocEa <= 0) {
					break;
				}
				// 剩余可分配单位数量
				qtyAllocUom = wmCommon.doubleDivide(qtyAllocEa, packageEntity.getCdprQuantity().doubleValue());
				// 超量分配，分配规则固定拣货位不为空，并且超量分配，可分配单位数量
				// 如果剩余可分配单位数量<1，那么退出
				if (qtyAllocUom < 1) {
					continue;
				}
				// 库存获取,根据库存周转规则排序，获取批次库存可用数
				BanQinAllocInvLotLocEntity invLotLocEntity = outboundCommon.getOverInvLot(qtyAllocUom, packageEntity.getCdprQuantity().doubleValue(), cdWhSkuLocModel.getLocCode(), soPreallocEntity
						.getOwnerCode(), soPreallocEntity.getSkuCode(), soPreallocEntity.getLotNum(), soPreallocEntity.getOrgId());
				// 更新库存
				if (invLotLocEntity == null) {
					continue;
				}
				// 分配更新
				// 库存更新类型
				BanQinAllocUpdateEntity updateEntity = new BanQinAllocUpdateEntity();
				updateEntity.setActionCode(ActionCode.OVER_ALLOCATION);// 两步分配
				// 超量分配
				updateEntity.setPreallocId(soPreallocEntity.getPreallocId());// 预配ID
				updateEntity.setSoNo(wmSoDetailModel.getSoNo());// 出库单
				updateEntity.setLineNo(wmSoDetailModel.getLineNo());// 出库单行号
				updateEntity.setWaveNo(soPreallocEntity.getWaveNo());// 波次号
				updateEntity.setPackageEntity(packageEntity);// 包装
				updateEntity.setConsigneeCode(entity.getConsigneeCode());// 收货人
				updateEntity.setTrackingNo(entity.getTrackingNo());// 快递单号
				updateEntity.setInvLotLocEntity(invLotLocEntity);// 库存
				updateEntity.setQtyAllocEa(qtyAllocEa);// 分配数量
				updateEntity.setQtyPackUom(entity.getQtyPackUom());// 预配明细单位换算数量
				// 两步分配更新
				qtyAllocEa = outboundAllocService.updateAllocTwoStep(updateEntity);
				/***************************************************************
				 * 超量分配逻辑结束
				 **************************************************************/
			} catch (WarehouseException e) {
				msg.setSuccess(false);
			}
		}
		return msg;
	}

}