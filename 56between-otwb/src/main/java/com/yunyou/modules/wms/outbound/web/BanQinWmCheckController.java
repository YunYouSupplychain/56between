package com.yunyou.modules.wms.outbound.web;

import com.google.common.collect.Maps;
import com.yunyou.common.ResultMessage;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhSkuBarcode;
import com.yunyou.modules.wms.basicdata.service.BanQinCdWhSkuBarcodeService;
import com.yunyou.modules.wms.outbound.entity.BanQinWmCheckEntity;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoAllocEntity;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoSerialEntity;
import com.yunyou.modules.wms.outbound.service.BanQinOutboundSoService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * 复核Controller
 * @author WMJ
 * @version 2019-09-18
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/outbound/banQinWmCheck")
public class BanQinWmCheckController extends BaseController {
	@Autowired
	private BanQinOutboundSoService outboundSoService;
	@Autowired
	private BanQinCdWhSkuBarcodeService cdWhSkuBarcodeService;

	/**
	 * 复核列表页面
	 */
	@RequiresPermissions("outbound:banQinWmCheck:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/wms/outbound/banQinWmCheckList";
	}

	/**
	 * 复核列表数据
	 */
	@ResponseBody
	@RequestMapping(value = "data")
	public AjaxJson data(BanQinWmSoAllocEntity banQinWmSoAllocEntity) {
		AjaxJson j = new AjaxJson();
		try {
			ResultMessage msg = outboundSoService.getCheckByCondition(banQinWmSoAllocEntity);
			LinkedHashMap<String, Object> map = Maps.newLinkedHashMap();
			Page page = new Page();
			BanQinWmCheckEntity data = (BanQinWmCheckEntity) msg.getData();
			page.setList(data.getCheckItems());
			map.put("entity", getBootstrapData(page));
			j.setBody(map);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg(e.getMessage());
		}
		return j;
	}

	/**
	 * 校验出库序列号
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "checkSoSerial")
	public AjaxJson checkSoSerial(@RequestBody BanQinWmCheckEntity entity) {
		AjaxJson j = new AjaxJson();
		try {
			List<BanQinCdWhSkuBarcode> byBarcode = cdWhSkuBarcodeService.findByBarcode(entity.getOwnerCode(), null, entity.getSkuCode(), entity.getOrgId());
			if (CollectionUtil.isNotEmpty(byBarcode)) {
				entity.setSkuCode(byBarcode.get(0).getSkuCode());
			}
			ResultMessage msg = outboundSoService.checkSoSerial(entity.getSoNo(), entity.getOwnerCode(), entity.getSkuCode(), entity.getSerialNo(), entity.getLotNums(), entity.getAllocIds(), entity.getOrgId());
			j.setSuccess(msg.isSuccess());
			j.setMsg(msg.getMessage());
			LinkedHashMap<String, Object> map = Maps.newLinkedHashMap();
			map.put("entity", msg.getData());
			j.setBody(map);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg(e.getMessage());
		}
		return j;
	}

	/**
	 * 获取包装信息
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getPackageInfo")
	public AjaxJson getPackageInfo(String packCode, String uom, String orgId) {
		AjaxJson j = new AjaxJson();
		try {
			ResultMessage msg = outboundSoService.getPackageRelationAndQtyUom(packCode, uom, orgId);
			j.setSuccess(msg.isSuccess());
			j.setMsg(msg.getMessage());
			LinkedHashMap<String, Object> map = Maps.newLinkedHashMap();
			map.put("entity", msg.getData());
			j.setBody(map);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg(e.getMessage());
		}
		return j;
	}

	/**
	 * 批量复核
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "checkConfirm")
	public AjaxJson checkConfirm(@RequestBody BanQinWmCheckEntity entity) {
		AjaxJson j = new AjaxJson();
		try {
			ResultMessage msg = outboundSoService.checkConfirm(entity.getAllocIds().toArray(new String[]{}), entity.getSoSerialList(), entity.getSoTrackingNo(), entity.getNoList(), entity.getProcessByCode(), entity.getOrgId());
			j.setSuccess(msg.isSuccess());
			j.setMsg(msg.getMessage());
			LinkedHashMap<String, Object> map = Maps.newLinkedHashMap();
			map.put("entity", msg.getData());
			j.setBody(map);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg(e.getMessage());
		}
		return j;
	}

	/**
	 * 取消复核
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "cancelCheck")
	public AjaxJson cancelCheck(@RequestBody BanQinWmCheckEntity entity) {
		AjaxJson j = new AjaxJson();
		try {
			ResultMessage msg = outboundSoService.cancelCheck(entity.getAllocIds().toArray(new String[]{}), entity.getOrgId());
			j.setSuccess(msg.isSuccess());
			j.setMsg(msg.getMessage());
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg(e.getMessage());
		}
		return j;
	}

	/**
	 * 获取序列号
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getSoSerialItemByAllocIds")
	public AjaxJson getSoSerialItemByAllocIds(@RequestBody BanQinWmCheckEntity entity) {
		AjaxJson j = new AjaxJson();
		try {
			List<BanQinWmSoSerialEntity> list = outboundSoService.getSoSerialItemByAllocIds(entity.getAllocIds().toArray(new String[]{}), entity.getOrgId());
			LinkedHashMap<String, Object> map = Maps.newLinkedHashMap();
			map.put("entity", list);
			j.setBody(map);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg(e.getMessage());
		}
		return j;
	}

}