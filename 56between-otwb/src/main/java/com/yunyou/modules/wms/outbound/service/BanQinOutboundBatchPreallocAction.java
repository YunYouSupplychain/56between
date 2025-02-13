package com.yunyou.modules.wms.outbound.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdRulePreallocDetail;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdRuleRotationDetail;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdRuleRotationHeader;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhPackageEntity;
import com.yunyou.modules.wms.basicdata.service.BanQinCdRulePreallocDetailService;
import com.yunyou.modules.wms.basicdata.service.BanQinCdRuleRotationDetailService;
import com.yunyou.modules.wms.basicdata.service.BanQinCdRuleRotationHeaderService;
import com.yunyou.modules.wms.common.entity.BanQinCdRuleRotationSqlParamEntity;
import com.yunyou.modules.wms.common.entity.WarehouseException;
import com.yunyou.modules.wms.common.entity.WmsCodeMaster;
import com.yunyou.modules.wms.common.service.BanQinWmsCommonService;
import com.yunyou.modules.wms.inventory.entity.BanQinWmInvLot;
import com.yunyou.modules.wms.outbound.entity.BanQinPreallocUpdateEntity;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoDetail;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoDetailEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 预配
 * @author WMJ
 * @version 2019/02/20
 */
@Service
public class BanQinOutboundBatchPreallocAction {
	// 公共方法
	@Autowired
	protected BanQinWmsCommonService wmCommon;
	// 出库公共
	@Autowired
	protected BanQinOutboundCommonService outboundCommon;
	// 预配
	@Autowired
	protected BanQinOutboundPreallocService outboundPreallocService;
	// 出库单
	@Autowired
	protected BanQinWmSoHeaderService wmSoHeaderService;
	// 出库单明细行
	@Autowired
	protected BanQinWmSoDetailService wmSoDetailService;
	// 预配规则
	@Autowired
	protected BanQinCdRulePreallocDetailService cdRulePreallocDetailService;
	// 周转规则
	@Autowired
	protected BanQinCdRuleRotationHeaderService cdRuleRotationHeaderService;
	// 周转规则明细
	@Autowired
	protected BanQinCdRuleRotationDetailService cdRuleRotationDetailService;

    /**
     * 预配逻辑
     * @param processByCode
     * @param noList
     * @return
     */
	public ResultMessage outboundBatchPrealloc(String processByCode, List<String> noList, String orgId) {
        ResultMessage msg = new ResultMessage();
		// 1、批量校验拦截订单、冻结订单
        ResultMessage message = new ResultMessage();
		message = wmSoHeaderService.checkBatchInterceptStatus(processByCode, noList, orgId);
		if (!message.isSuccess()) {
			// 拦截状态订单提示
			msg.addMessage(message.getMessage());
		}
		message = wmSoHeaderService.checkBatchHoldStatus(processByCode, noList, orgId);
		if (!message.isSuccess()) {
			// 冻结状态订单提示
			msg.addMessage(message.getMessage());
		}
		// 2、获取可预配的订单明细行，过滤出库单行取消状态，出库单的取消、关闭、拦截、冻结状态
		List<BanQinWmSoDetailEntity> soDetailList = wmSoDetailService.findEntityByProcessCode(processByCode, noList, orgId);
		if (soDetailList.size() == 0) {
			// 没有可预配的出库单行
			msg.addMessage("没有可预配的出库单行");
			return msg;
		}
		try {
			// 3、循环订单明细行
			for (BanQinWmSoDetailEntity soDetail : soDetailList) {
				if (StringUtils.isNotEmpty(soDetail.getCdType())) {
					// 只能在越库任务界面执行出库，[{0}][{1}]属于越库，不能操作
					msg.addMessage("只能在越库任务界面执行出库" + soDetail.getSoNo() + soDetail.getLineNo() + "属于越库，不能操作");
					msg.setSuccess(false);
					continue;
				}
				// 调用按单行明细执行预配
				message = preallocBySoLineNo(soDetail);
				if (StringUtils.isNotEmpty(message.getMessage())) {
					msg.addMessage(message.getMessage());
					msg.setSuccess(false);
				}
			}
		} catch (WarehouseException e) {
			throw new RuntimeException(e.getMessage());
		} finally {
			if (StringUtils.isEmpty(msg.getMessage())) {
				msg.addMessage("操作成功");
			}
		}
		return msg;

	}

    /**
     * 预配 - 按明细行预配
     * @param soDetail
     * @return
     */
	public ResultMessage preallocBySoLineNo(BanQinWmSoDetailEntity soDetail) {
        ResultMessage msg = new ResultMessage();
		try {
			// 1、基础数据获取及校验
			// 预配规则明细获取
			// 校验预配规则明细是否存在，预配规则明细是否有配置EA单位
			List<BanQinCdRulePreallocDetail> rulePreallocDetailList = cdRulePreallocDetailService.getByRuleCode(soDetail.getPreallocRule(), soDetail.getOrgId());
			// 周转规则
			// 校验库存周转规则是否存在，校验周转类型是否是【库存周转优先】
			BanQinCdRuleRotationHeader ruleRotationHeaderModel = cdRuleRotationHeaderService.findByRuleCode(soDetail.getRotationRule(), soDetail.getOrgId());
			if (!(ruleRotationHeaderModel.getRotationType()).equals(WmsCodeMaster.ROTATION.getCode())) {
				// {0}{1}预配时周转方式必须按库存周转优先
				msg.addMessage(soDetail.getSoNo() + soDetail.getLineNo() + "预配时周转方式必须按库存周转优先");
				return msg;
			}
			// 包装单位获取，校验明细是否记录
            BanQinCdWhPackageEntity packageEntity = wmCommon.getCdWhPackageRelation(soDetail.getPackCode(), soDetail.getOrgId());
			/*
			 * 2、获取可预配库存 查询批次库存，关联库存周转批次属性配置 如果库存周转规则没有配置明细，那么默认按批次号升序；
			 * 如果库存周转规则有配置明细，那么周转规则配置排序、过滤 如果商品开启效期控制，那么过滤不符合出库效期的库存
			 */
			// 周转规则拼SQL入参数据
			BanQinCdRuleRotationSqlParamEntity ruleRotationSqlParamEntity = new BanQinCdRuleRotationSqlParamEntity();
			// 周转规则明细
			List<BanQinCdRuleRotationDetail> ruleRotationDetailList = cdRuleRotationDetailService.getByRuleCode(soDetail.getRotationRule(), soDetail.getOrgId());
			// 设置周转规则明细
			ruleRotationSqlParamEntity.setRuleRotationDetailList(ruleRotationDetailList);
			// 出库单明细行12个批次属性值
			ruleRotationSqlParamEntity.setLotAtt01(soDetail.getLotAtt01());
			ruleRotationSqlParamEntity.setLotAtt02(soDetail.getLotAtt02());
			ruleRotationSqlParamEntity.setLotAtt03(soDetail.getLotAtt03());
			ruleRotationSqlParamEntity.setLotAtt04(soDetail.getLotAtt04());
			ruleRotationSqlParamEntity.setLotAtt05(soDetail.getLotAtt05());
			ruleRotationSqlParamEntity.setLotAtt06(soDetail.getLotAtt06());
			ruleRotationSqlParamEntity.setLotAtt07(soDetail.getLotAtt07());
			ruleRotationSqlParamEntity.setLotAtt08(soDetail.getLotAtt08());
			ruleRotationSqlParamEntity.setLotAtt09(soDetail.getLotAtt09());
			ruleRotationSqlParamEntity.setLotAtt10(soDetail.getLotAtt10());
			ruleRotationSqlParamEntity.setLotAtt11(soDetail.getLotAtt11());
			ruleRotationSqlParamEntity.setLotAtt12(soDetail.getLotAtt12());
			// 获取可预配库存记录
			List<BanQinWmInvLot> invLotList = outboundCommon.getInvLotByRuleRotation(soDetail.getOwnerCode(), soDetail.getSkuCode(), soDetail.getOrgId(), ruleRotationSqlParamEntity);
			if (invLotList.size() == 0) {
				return msg;// 无可预配库存
			}

			// 订单行可预配数量 = 订货数 - 已预配数 - 已分配数 - 已拣货数 - 已发货数
			BanQinWmSoDetail wmSoDetailModel = new BanQinWmSoDetail();
			BeanUtils.copyProperties(soDetail, wmSoDetailModel);
			Double qtyPrealloc = wmSoDetailService.getAvailableQtySoEa(wmSoDetailModel);
			// 3、循环库存记录
			for (BanQinWmInvLot invLotModel : invLotList) {
				// 该明细可预配数量预配完成
				if (qtyPrealloc <= 0) {
					break;
				}

				BanQinPreallocUpdateEntity updateEntity = new BanQinPreallocUpdateEntity();
				// 库存
				updateEntity.setInvLotModel(invLotModel);
				// 包装单位
				updateEntity.setPackageRelationList(packageEntity.getCdWhPackageRelations());
				// 预配规则明细
				updateEntity.setRulePreallocDetailList(rulePreallocDetailList);
				// 可预配数量
				updateEntity.setQtyPrealloc(qtyPrealloc);
				// 出库单明细行
				updateEntity.setSoDetail(soDetail);
				// 调用更新方法，更新预配明细，库存，出库单行数量、状态，波次单状态，出库单及波次单状态
				qtyPrealloc = outboundPreallocService.updatePrealloc(updateEntity);
			}
		} catch (WarehouseException e) {
			msg.addMessage(e.getMessage());
			msg.setSuccess(false);
		}
		return msg;
	}

}