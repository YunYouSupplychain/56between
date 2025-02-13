package com.yunyou.modules.wms.outbound.web;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.wms.common.entity.ProcessByCode;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoAllocEntity;
import com.yunyou.modules.wms.outbound.entity.BanQinWmWvDetail;
import com.yunyou.modules.wms.outbound.service.BanQinOutboundWaveService;
import com.yunyou.modules.wms.report.entity.WmSoSkuLabel;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;

/**
 * 波次单明细Controller
 * @author WMJ
 * @version 2019-02-14
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/outbound/banQinWmWvDetail")
public class BanQinWmWvDetailController extends BaseController {
	@Autowired
	private BanQinOutboundWaveService banQinOutboundWaveService;

	/**
	 * 分配
	 */
	@ResponseBody
	@RequiresPermissions("outbound:banQinWmWvDetail:soAlloc")
	@RequestMapping(value = "soAlloc")
	public AjaxJson soAlloc(BanQinWmWvDetail banQinWmWvDetail, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
			ResultMessage msg = banQinOutboundWaveService.allocBySo(banQinWmWvDetail.getWaveNo(), ProcessByCode.BY_SO.getCode(), Arrays.asList(banQinWmWvDetail.getSoNo().split(",")), banQinWmWvDetail.getOrgId());
			j.setSuccess(msg.isSuccess());
			j.setMsg(msg.getMessage());
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg(e.getMessage());
		}
		return j;
	}

	/**
	 * 拣货确认
	 */
	@ResponseBody
	@RequiresPermissions("outbound:banQinWmWvDetail:soPicking")
	@RequestMapping(value = "soPicking")
	public AjaxJson soPicking(BanQinWmWvDetail banQinWmWvDetail, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
			ResultMessage msg = banQinOutboundWaveService.pickingBySo(banQinWmWvDetail.getWaveNo(),"BY_SO", Arrays.asList(banQinWmWvDetail.getSoNo().split(",")), banQinWmWvDetail.getOrgId());
			j.setSuccess(msg.isSuccess());
			j.setMsg(msg.getMessage());
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg(e.getMessage());
		}
		return j;
	}

	/**
	 * 发货确认
	 */
	@ResponseBody
	@RequiresPermissions("outbound:banQinWmWvDetail:soShipment")
	@RequestMapping(value = "soShipment")
	public AjaxJson soShipment(BanQinWmWvDetail banQinWmWvDetail, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
			ResultMessage msg = banQinOutboundWaveService.shipmentBySo(banQinWmWvDetail.getWaveNo(),"BY_SO", Arrays.asList(banQinWmWvDetail.getSoNo().split(",")), banQinWmWvDetail.getOrgId());
			j.setSuccess(msg.isSuccess());
			j.setMsg(msg.getMessage());
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg(e.getMessage());
		}
		return j;
	}

	/**
	 * 取消分配
	 */
	@ResponseBody
	@RequiresPermissions("outbound:banQinWmWvDetail:soCancelAlloc")
	@RequestMapping(value = "soCancelAlloc")
	public AjaxJson soCancelAlloc(BanQinWmWvDetail banQinWmWvDetail, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
			ResultMessage msg = banQinOutboundWaveService.cancelAllocBySo(banQinWmWvDetail.getWaveNo(),"BY_SO", Arrays.asList(banQinWmWvDetail.getSoNo().split(",")), banQinWmWvDetail.getOrgId());
			j.setSuccess(msg.isSuccess());
			j.setMsg(msg.getMessage());
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg(e.getMessage());
		}
		return j;
	}

	/**
	 * 取消拣货
	 */
	@ResponseBody
	@RequiresPermissions("outbound:banQinWmWvDetail:soCancelPicking")
	@RequestMapping(value = "soCancelPicking")
	public AjaxJson soCancelPicking(BanQinWmWvDetail banQinWmWvDetail, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
			ResultMessage msg = banQinOutboundWaveService.cancelPickingBySo(banQinWmWvDetail.getWaveNo(),"BY_SO", Arrays.asList(banQinWmWvDetail.getSoNo().split(",")), banQinWmWvDetail.getOrgId());
			j.setSuccess(msg.isSuccess());
			j.setMsg(msg.getMessage());
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg(e.getMessage());
		}
		return j;
	}

	/**
	 * 取消发货
	 */
	@ResponseBody
	@RequiresPermissions("outbound:banQinWmWvDetail:soCancelShipment")
	@RequestMapping(value = "soCancelShipment")
	public AjaxJson soCancelShipment(BanQinWmWvDetail banQinWmWvDetail, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
			ResultMessage msg = banQinOutboundWaveService.cancelShipmentBySo(banQinWmWvDetail.getWaveNo(),"BY_SO", Arrays.asList(banQinWmWvDetail.getSoNo().split(",")), banQinWmWvDetail.getOrgId());
			j.setSuccess(msg.isSuccess());
			j.setMsg(msg.getMessage());
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg(e.getMessage());
		}
		return j;
	}

	/**
	 * 分配
	 */
	@ResponseBody
	@RequiresPermissions("outbound:banQinWmWvDetail:wvAlloc")
	@RequestMapping(value = "wvAlloc")
	public AjaxJson wvAlloc(BanQinWmWvDetail banQinWmWvDetail, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
			ResultMessage msg = banQinOutboundWaveService.allocBySoLine(banQinWmWvDetail.getWaveNo(),"BY_SO_LINE", Arrays.asList(banQinWmWvDetail.getSoNo().split("@")), banQinWmWvDetail.getOrgId());
			j.setSuccess(msg.isSuccess());
			j.setMsg(msg.getMessage());
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg(e.getMessage());
		}
		return j;
	}

	/**
	 * 取消分配
	 */
	@ResponseBody
	@RequiresPermissions("outbound:banQinWmWvDetail:wvCancelAlloc")
	@RequestMapping(value = "wvCancelAlloc")
	public AjaxJson wvCancelAlloc(BanQinWmWvDetail banQinWmWvDetail, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
			ResultMessage msg = banQinOutboundWaveService.cancelAllocBySoLine(banQinWmWvDetail.getWaveNo(),"BY_SO_LINE", Arrays.asList(banQinWmWvDetail.getSoNo().split("@")), banQinWmWvDetail.getOrgId());
			j.setSuccess(msg.isSuccess());
			j.setMsg(msg.getMessage());
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg(e.getMessage());
		}
		return j;
	}

	/**
	 * 手工分配保存
	 */
	@ResponseBody
	@RequiresPermissions("outbound:banQinWmWvDetail:allocSave")
	@RequestMapping(value = "allocSave")
	public AjaxJson allocSave(@RequestBody BanQinWmSoAllocEntity banQinWmSoAllocEntity, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
			ResultMessage msg = banQinOutboundWaveService.manualAlloc(banQinWmSoAllocEntity);
			j.setSuccess(msg.isSuccess());
			j.setMsg(msg.getMessage());
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg(e.getMessage());
		}
		return j;
	}

	/**
	 * 拣货确认
	 */
	@ResponseBody
	@RequiresPermissions("outbound:banQinWmWvDetail:allocPick")
	@RequestMapping(value = "allocPick")
	public AjaxJson allocPick(@RequestBody List<BanQinWmSoAllocEntity> banQinWmSoAllocEntity, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
			ResultMessage msg = banQinOutboundWaveService.pickingByAlloc(banQinWmSoAllocEntity);
			j.setSuccess(msg.isSuccess());
			j.setMsg(msg.getMessage());
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg(e.getMessage());
		}
		return j;
	}

	/**
	 * 发货确认
	 */
	@ResponseBody
	@RequiresPermissions("outbound:banQinWmWvDetail:allocShipment")
	@RequestMapping(value = "allocShipment")
	public AjaxJson allocShipment(@RequestBody List<BanQinWmSoAllocEntity> banQinWmSoAllocEntity, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
			ResultMessage msg = banQinOutboundWaveService.shipmentByAlloc(banQinWmSoAllocEntity);
			j.setSuccess(msg.isSuccess());
			j.setMsg(msg.getMessage());
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg(e.getMessage());
		}
		return j;
	}

	/**
	 * 取消分配
	 */
	@ResponseBody
	@RequiresPermissions("outbound:banQinWmWvDetail:allocCancelAlloc")
	@RequestMapping(value = "allocCancelAlloc")
	public AjaxJson allocCancelAlloc(@RequestBody List<BanQinWmSoAllocEntity> banQinWmSoAllocEntity, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
			ResultMessage msg = banQinOutboundWaveService.cancelAllocByAlloc(banQinWmSoAllocEntity);
			j.setSuccess(msg.isSuccess());
			j.setMsg(msg.getMessage());
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg(e.getMessage());
		}
		return j;
	}

	/**
	 * 取消拣货
	 */
	@ResponseBody
	@RequiresPermissions("outbound:banQinWmWvDetail:allocCancelPick")
	@RequestMapping(value = "allocCancelPick")
	public AjaxJson allocCancelPick(@RequestBody List<BanQinWmSoAllocEntity> banQinWmSoAllocEntity, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
			ResultMessage msg = banQinOutboundWaveService.cancelPickingByAlloc(banQinWmSoAllocEntity);
			j.setSuccess(msg.isSuccess());
			j.setMsg(msg.getMessage());
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg(e.getMessage());
		}
		return j;
	}

	/**
	 * 取消发货
	 */
	@ResponseBody
	@RequiresPermissions("outbound:banQinWmWvDetail:allocCancelShipment")
	@RequestMapping(value = "allocCancelShipment")
	public AjaxJson allocCancelShipment(@RequestBody List<BanQinWmSoAllocEntity> banQinWmSoAllocEntity, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
			ResultMessage msg = banQinOutboundWaveService.cancelShipmentByAlloc(banQinWmSoAllocEntity);
			j.setSuccess(msg.isSuccess());
			j.setMsg(msg.getMessage());
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg(e.getMessage());
		}
		return j;
	}

	/**
	 * 打印标签
	 */
	@RequestMapping(value = "/printLabel")
	public String printLabel(Model model, BanQinWmSoAllocEntity entity) {
		List<WmSoSkuLabel> result = banQinOutboundWaveService.getWmSoSkuLabel(entity);
		// 报表数据源
		JRDataSource jrDataSource = new JRBeanCollectionDataSource(result);
		// 动态指定报表模板url
		model.addAttribute("url", "classpath:/jasper/wmSoSkuLabel.jasper");
		model.addAttribute("format", "pdf");
		model.addAttribute("jrMainDataSource", jrDataSource);

		return "iReportView";
	}

}