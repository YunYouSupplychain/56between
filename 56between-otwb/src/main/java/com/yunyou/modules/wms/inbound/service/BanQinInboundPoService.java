package com.yunyou.modules.wms.inbound.service;

import java.util.Arrays;
import java.util.List;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.IdGen;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.modules.wms.common.entity.WarehouseException;
import com.yunyou.modules.wms.common.entity.WmsCodeMaster;
import com.yunyou.modules.wms.common.service.BanQinWmsCommonService;
import com.yunyou.modules.wms.inbound.entity.BanQinWmPoDetail;
import com.yunyou.modules.wms.inbound.entity.BanQinWmPoDetailEntity;
import com.yunyou.modules.wms.inbound.entity.BanQinWmPoEntity;
import com.yunyou.modules.wms.inbound.entity.BanQinWmPoHeader;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * PO单 方法类
 * @author WMJ
 * @version 2019/03/19
 */
@Service
@Transactional(readOnly = true)
public class BanQinInboundPoService {
	@Autowired
	protected BanQinWmPoHeaderService wmPoHeaderService;
	@Autowired
	protected BanQinWmPoDetailService wmPoDetailService;
	@Autowired
	protected BanQinWmsCommonService wmCommon;
	@Autowired
	protected BanQinInboundDuplicateService inboundDuplicateService;
	@Autowired
    private BanQinInboundOperationService inboundOperationService;

    /**
     * 保存PO单
     * @param wmPoEntity
     * @return
     */
    @Transactional
	public ResultMessage savePoEntity(BanQinWmPoEntity wmPoEntity) {
		BanQinWmPoHeader model = new BanQinWmPoHeader();
		BeanUtils.copyProperties(wmPoEntity, model);
		return this.wmPoHeaderService.savePoHeader(model);
	}

    /**
     * 保存PO明细
     * @param wmPoDetailEntity
     * @return
     */
    @Transactional
	public ResultMessage savePoDetailEntity(BanQinWmPoDetailEntity wmPoDetailEntity) {
        ResultMessage msg = new ResultMessage();
		BanQinWmPoHeader wmPoHeaderModel = wmPoHeaderService.findByPoNo(wmPoDetailEntity.getPoNo(), wmPoDetailEntity.getOrgId());
		if (!WmsCodeMaster.PO_NEW.getCode().equals(wmPoHeaderModel.getStatus())) {
			msg.setSuccess(false);
			msg.addMessage(wmPoDetailEntity.getPoNo() + "不是创建状态，不能操作");
			return msg;
		}
		if (WmsCodeMaster.AUDIT_CLOSE.getCode().equals(wmPoHeaderModel.getAuditStatus())) {
			msg.setSuccess(false);
			msg.addMessage(wmPoDetailEntity.getPoNo() + "已审核，不能操作");
			return msg;
		}
		BanQinWmPoDetail model = new BanQinWmPoDetail();
		BeanUtils.copyProperties(wmPoDetailEntity, model);
		msg = this.wmPoDetailService.savePoDetail(model);
		if (msg.isSuccess()) {
			msg.setSuccess(true);
		}
		return msg;
	}

    /**
     * 删除PO单 审核后不能删除
     * @param poIds
     * @return
     */
    @Transactional
	public ResultMessage removePoEntity(String[] poIds) {
        ResultMessage msg = new ResultMessage();
        ResultMessage errorMsg = new ResultMessage();
		// 校验状态
        ResultMessage checkMsg = wmPoHeaderService.checkPoStatus(poIds, new String[] { WmsCodeMaster.AUDIT_NEW.getCode(), WmsCodeMaster.AUDIT_NOT.getCode() },
				new String[] { WmsCodeMaster.PO_NEW.getCode() }, null);
		Object[] checkPoNos = (Object[]) checkMsg.getData();
		if (checkPoNos.length > 0) {
			// 校验是否存在ASN单
			errorMsg = wmPoHeaderService.checkPoIsExistAsn(Arrays.asList(checkPoNos).toArray(new String[] {}));
			Object[] updatePoNos = (Object[]) errorMsg.getData();
			if (updatePoNos.length > 0) {
				List<BanQinWmPoHeader> list = wmPoHeaderService.findByPoId(Arrays.asList(updatePoNos).toArray(new String[] {}));
				for (BanQinWmPoHeader wmPoHeaderModel : list) {
                    wmPoHeaderService.removePoEntity(wmPoHeaderModel);
				}
			}
		}
		if (StringUtils.isNotEmpty(checkMsg.getMessage())) {
			msg.addMessage(checkMsg.getMessage() + "非创建状态、已审核、已冻结的订单，不能操作");
		}
		if (StringUtils.isNotEmpty(errorMsg.getMessage())) {
			msg.addMessage(errorMsg.getMessage() + "已生成ASN单，不能操作");
		}
		if (StringUtils.isEmpty(errorMsg.getMessage()) && StringUtils.isEmpty(checkMsg.getMessage())) {
			msg.addMessage("操作成功");
		}
		return msg;
	}

    /**
     * 删除PO明细 审核后不能删除
     * @param poNo
     * @param lineNos
     * @return
     */
    @Transactional
	public ResultMessage removePoDetailEntity(String poNo, String[] lineNos, String orgId) {
		// 删除明细
		return wmPoDetailService.removeByPoNoAndLineNo(poNo, lineNos, orgId);
	}

    /**
     * 通过PO号获得单个PO单
     * @param poNo
     * @return
     */
	public BanQinWmPoEntity getPoEntityByPoNo(String poNo, String orgId) {
        BanQinWmPoEntity entity = wmPoHeaderService.findEntityByPoNo(poNo, orgId);
		entity.setWmPoDetailEntitys(wmPoDetailService.findEntityByPoNo(poNo, orgId));
		return entity;
	}

    /**
     * 根据单号和行号，查询订单明细entity
     * @param poNo
     * @param lineNo
     * @return
     */
	public BanQinWmPoDetailEntity getPoDetailEntityByPoNoAndLineNo(String poNo, String lineNo, String orgId) {
		return wmPoDetailService.findEntityByPoNoAndLineNo(poNo, lineNo, orgId);
	}

    /**
     * 根据单号，查询订单未取消的明细entity
     * @param poIds
     * @return
     */
	public List<BanQinWmPoDetailEntity> getPoDetailEntityByPoNo(String[] poIds, String orgId) {
		return wmPoDetailService.findEntityByPoId(poIds, orgId);
	}

    /**
     * 审核 审核后不能修改
     * @param poIds
     * @return
     */
    @Transactional
	public ResultMessage auditPo(String[] poIds) {
		return wmPoHeaderService.audit(poIds);
	}

    /**
     * 取消审核 不能生成生成ASN
     * @param poIds
     * @return
     */
    @Transactional
	public ResultMessage cancelAuditPo(String[] poIds) {
		return wmPoHeaderService.cancelAudit(poIds);
	}

    /**
     * 取消PO单 创建状态可以取消，取消后不能进行其他操作
     * @param poIds
     * @return
     */
    @Transactional
	public ResultMessage cancelPo(String[] poIds) {
		return wmPoHeaderService.cancel(poIds);
	}

    /**
     * 取消PO明细行
     * @param poNo
     * @param lineNos
     * @return
     */
    @Transactional
	public ResultMessage cancelPoDetail(String poNo, String[] lineNos, String orgId) {
        ResultMessage msg = new ResultMessage();
        ResultMessage errorMsg = new ResultMessage();
		// 校验状态
        ResultMessage checkMsg = wmPoDetailService.checkPoDetailStatus(poNo, null, null, lineNos, new String[] { WmsCodeMaster.PO_NEW.getCode() }, orgId);
		Object[] checkLineNos = (Object[]) checkMsg.getData();
		if (checkLineNos.length > 0) {
			// 校验是否存在ASN单
			errorMsg = wmPoDetailService.checkPoIsExistAsn(poNo, Arrays.asList(checkLineNos).toArray(new String[] {}), orgId);
			Object[] updateLineNos = (Object[]) errorMsg.getData();
			if (updateLineNos.length > 0) {
				List<BanQinWmPoDetail> list = wmPoDetailService.findByPoNoAndLineNo(poNo, Arrays.asList(updateLineNos).toArray(new String[] {}), orgId);
				for (BanQinWmPoDetail wmPoDetailModel : list) {
					wmPoDetailService.cancel(wmPoDetailModel);
				}
			}
		}
		if (StringUtils.isNotEmpty(checkMsg.getMessage())) {
			msg.addMessage("行号" + "\n" + checkMsg.getMessage());
			msg.addMessage("不是创建状态，不能操作");// 不是创建状态，不能操作
		}
		if (StringUtils.isNotEmpty(errorMsg.getMessage())) {
			msg.addMessage("行号" + "\n" + errorMsg.getMessage());
			msg.addMessage("已生成ASN单，不能操作");
		}
		if (StringUtils.isEmpty(errorMsg.getMessage()) && StringUtils.isEmpty(checkMsg.getMessage())) {
			msg.addMessage("操作成功");
		}
		return msg;
	}

    /**
     * 关闭PO单 ASN单关闭后，才能关闭PO单
     * @param poIds
     * @return
     */
    @Transactional
	public ResultMessage closePo(String[] poIds) {
        ResultMessage msg = new ResultMessage();
		// 校验状态
        ResultMessage checkMsg = wmPoHeaderService.checkPoStatus(poIds, null, new String[] { WmsCodeMaster.PO_FULL_RECEIVING.getCode(), WmsCodeMaster.PO_PART_RECEIVING.getCode() }, null);
		Object[] checkPoNos = (Object[]) checkMsg.getData();
		if (checkPoNos.length > 0) {
			List<BanQinWmPoHeader> list = wmPoHeaderService.findByPoId(Arrays.asList(checkPoNos).toArray(new String[] {}));
			for (BanQinWmPoHeader model : list) {
				try {
                    wmPoHeaderService.close(model);
				} catch (WarehouseException e) {
					msg.addMessage(model.getPoNo() + e.getMessage());
					continue;
				}
			}
		}
		if (StringUtils.isNotEmpty(checkMsg.getMessage())) {
			msg.addMessage(checkMsg.getMessage() + "不是收货状态，不能操作");
		}
		if (StringUtils.isEmpty(msg.getMessage()) && StringUtils.isEmpty(checkMsg.getMessage())) {
			msg.addMessage("操作成功");
		}
		return msg;
	}

    /**
     * 复制PO
     * @param poNo
     * @return
     */
	public ResultMessage duplicatePo(String poNo, String orgId) {
		return inboundDuplicateService.duplicatePo(poNo, orgId);
	}

    /**
     * 复制Po明细
     * @param poNo
     * @param lineNo
     * @return
     */
    @Transactional
	public ResultMessage duplicatePoDetail(String poNo, String lineNo, String orgId) {
        ResultMessage msg = new ResultMessage();
		BanQinWmPoHeader wmPoHeaderModel = wmPoHeaderService.findByPoNo(poNo, orgId);
		if (!WmsCodeMaster.PO_NEW.getCode().equals(wmPoHeaderModel.getStatus())) {
			msg.setSuccess(false);
			msg.addMessage("不是创建状态，不能操作");
			return msg;
		}
		if (WmsCodeMaster.AUDIT_CLOSE.getCode().equals(wmPoHeaderModel.getAuditStatus())) {
			msg.setSuccess(false);
			msg.addMessage("已审核，不能操作");
			return msg;
		}
		BanQinWmPoDetail wmPoDetailModel = wmPoDetailService.findByPoNoAndLineNo(poNo, lineNo, orgId);
        BanQinWmPoDetail newModel = new BanQinWmPoDetail();
		if (null != wmPoDetailModel) {
			wmPoDetailModel.setId(IdGen.uuid());
            ResultMessage message = wmPoDetailService.savePoDetail(wmPoDetailModel);
			if (message.isSuccess()) {
				newModel = (BanQinWmPoDetail) message.getData();
			}
		} else {
			msg.setSuccess(false);
			msg.addMessage("订单[" +poNo +"]行" + lineNo + "不存在");
			return msg;
		}
		msg.setSuccess(true);
		msg.addMessage("操作成功");
		msg.setData(this.getPoDetailEntityByPoNoAndLineNo(poNo, newModel.getLineNo(), orgId));
		return msg;
	}

    /**
     * 可选多个同货主，同供应商的PO单，生成一个ASN单。
     * @param ownerCode
     * @param supplierCode
     * @param wmPoDetailEntitys
     * @return
     */
    @Transactional
	public ResultMessage createAsn(String ownerCode, String supplierCode, List<BanQinWmPoDetailEntity> wmPoDetailEntitys, String orgId) {
		return inboundOperationService.inboundCreateAsn(ownerCode, supplierCode, wmPoDetailEntitys, orgId);
	}

}