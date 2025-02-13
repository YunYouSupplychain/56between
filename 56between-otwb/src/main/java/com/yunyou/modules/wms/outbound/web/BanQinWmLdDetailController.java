package com.yunyou.modules.wms.outbound.web;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.wms.outbound.entity.BanQinWmLdDetailEntity;
import com.yunyou.modules.wms.outbound.entity.BanQinWmLdEntity;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoAllocToSoOrderQueryEntity;
import com.yunyou.modules.wms.outbound.service.BanQinOutboundLdService;
import com.yunyou.modules.wms.outbound.service.BanQinWmLdDetailService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 装车单明细Controller
 *
 * @author WMJ
 * @version 2019-02-14
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/outbound/banQinWmLdDetail")
public class BanQinWmLdDetailController extends BaseController {
    @Autowired
    private BanQinWmLdDetailService banQinWmLdDetailService;
    @Autowired
    private BanQinOutboundLdService banQinOutboundLdService;

	/**
	 * 分配明细列表数据
	 */
	@ResponseBody
	@RequestMapping(value = "allocData")
	public Map<String, Object> allocData(BanQinWmSoAllocToSoOrderQueryEntity entity, Model model) {
		Page page = new Page();
		List<BanQinWmSoAllocToSoOrderQueryEntity> list = banQinWmLdDetailService.wmSoAllocToSoOrderQuery(entity);
		page.setList(list);
		return getBootstrapData(page);
	}

    /**
     * 订单明细列表数据
     */
	@ResponseBody
	@RequestMapping(value = "detailData")
	public AjaxJson detailData(@RequestBody BanQinWmLdDetailEntity entity, Model model) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinOutboundLdService.getEntityByLdNo(entity.getLdNo(), entity.getOrgId());
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
            LinkedHashMap<String, Object> map = new LinkedHashMap<>();
            BanQinWmLdEntity data = (BanQinWmLdEntity) msg.getData();
            Page detailPage = new Page();
            detailPage.setList(data.getSoOrderQueryItem());
            map.put("detailList", getBootstrapData(detailPage));
            Page tracePage = new Page();
            tracePage.setList(data.getTraceIdQueryItem());
            map.put("traceList", getBootstrapData(tracePage));
            Page noLoadPage = new Page();
            noLoadPage.setList(data.getLdDetail10Entity());
            map.put("noLoadList", getBootstrapData(noLoadPage));
            Page hasLoadPage = new Page();
            hasLoadPage.setList(data.getLdDetail40Entity());
            map.put("hasLoadList", getBootstrapData(hasLoadPage));
            j.setBody(map);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
	}

    /**
     * 待装车明细列表数据
     */
    @ResponseBody
    @RequestMapping(value = "loadingData")
    public Map<String, Object> loadingData(BanQinWmLdDetailEntity entity, Model model) {
        Page page = new Page();
        List<BanQinWmLdDetailEntity> list = banQinWmLdDetailService.getWmSoAllocToLdDetailQuery(entity);
        page.setList(list);
        return getBootstrapData(page);
    }

    @ResponseBody
    @RequiresPermissions("outbound:banQinWmLdDetail:addSo")
    @RequestMapping(value = "addLdDetailBySoNo")
    public AjaxJson addLdDetailBySoNo(@RequestBody BanQinWmLdDetailEntity entity) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinOutboundLdService.addLdDetailBySoNo(entity.getLdNo(), entity.getSoNos().toArray(new String[0]), entity.getOrgId());
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }

        return j;
    }

    @ResponseBody
    @RequiresPermissions("outbound:banQinWmLdDetail:removeSo")
    @RequestMapping(value = "removeByLdNoAndSoNo")
    public AjaxJson removeByLdNoAndSoNo(@RequestBody BanQinWmLdDetailEntity entity) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinOutboundLdService.removeByLdNoAndSoNo(entity.getLdNo(), entity.getSoNos().toArray(new String[0]), entity.getOrgId());
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }

        return j;
    }

    @ResponseBody
    @RequiresPermissions("outbound:banQinWmLdDetail:loadingSo")
    @RequestMapping(value = "outboundLoadingBySoNo")
    public AjaxJson outboundLoadingBySoNo(@RequestBody BanQinWmLdDetailEntity entity) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinOutboundLdService.outboundLoadingBySoNo(entity.getLdNo(), entity.getSoNos().toArray(new String[0]), entity.getOrgId());
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }

        return j;
    }

    @ResponseBody
    @RequestMapping(value = "enterByTraceId")
    public AjaxJson enterByTraceId(@RequestBody BanQinWmLdEntity entity) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinOutboundLdService.enterByTraceId(entity.getTraceId(), entity);
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
            j.setBody(new LinkedHashMap<String, Object>(){{put("entity", msg.getData());}});
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }

        return j;
    }

    @ResponseBody
    @RequiresPermissions("outbound:banQinWmLdDetail:scanning")
    @RequestMapping(value = "addLdDetailByTraceId")
    public AjaxJson addLdDetailByTraceId(@RequestBody BanQinWmLdEntity entity) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinOutboundLdService.addLdDetailByTraceId(entity.getLdNo(), entity.getTraceId().split(",", -1), entity.getOrgId());
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }

        return j;
    }

    @ResponseBody
    @RequiresPermissions("outbound:banQinWmLdDetail:removeTraceId")
    @RequestMapping(value = "removeByLdNoAndTraceId")
    public AjaxJson removeByLdNoAndTraceId(@RequestBody BanQinWmLdDetailEntity entity) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinOutboundLdService.removeByLdNoAndTraceId(entity.getLdNo(), entity.getToIds().toArray(new String[0]), entity.getOrgId());
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }

        return j;
    }

    @ResponseBody
    @RequiresPermissions("outbound:banQinWmLdDetail:loadingByTraceId")
    @RequestMapping(value = "outboundLoadingByTraceId")
    public AjaxJson outboundLoadingByTraceId(@RequestBody BanQinWmLdDetailEntity entity) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinOutboundLdService.outboundLoadingByTraceId(entity.getLdNo(), entity.getToIds().toArray(new String[0]), entity.getOrgId());
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }

        return j;
    }

    @ResponseBody
    @RequiresPermissions("outbound:banQinWmLdDetail:addPicking")
    @RequestMapping(value = "addLdDetailByAllocId")
    public AjaxJson addLdDetailByAllocId(@RequestBody BanQinWmLdDetailEntity entity) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinOutboundLdService.addLdDetailByAllocId(entity.getLdNo(), entity.getAllocIds().toArray(new String[0]), entity.getOrgId());
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }

        return j;
    }

    @ResponseBody
    @RequiresPermissions("outbound:banQinWmLdDetail:removeTraceId")
    @RequestMapping(value = "removeByLdNoAndLineNo")
    public AjaxJson removeByLdNoAndLineNo(@RequestBody BanQinWmLdDetailEntity entity) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinOutboundLdService.removeByLdNoAndLineNo(entity.getLdNo(), entity.getLineNo().split(",", -1), entity.getOrgId());
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }

        return j;
    }

    @ResponseBody
    @RequiresPermissions("outbound:banQinWmLdDetail:batchLoading")
    @RequestMapping(value = "outboundBatchLoading")
    public AjaxJson outboundBatchLoading(@RequestBody List<BanQinWmLdDetailEntity> list) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinOutboundLdService.outboundBatchLoading(list);
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }

        return j;
    }

    @ResponseBody
    @RequiresPermissions("outbound:banQinWmLdDetail:ldList")
    @RequestMapping(value = "newLd")
    public AjaxJson newLd(@RequestBody BanQinWmLdEntity entity) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinOutboundLdService.newLd(entity, entity.getLdDetail10Entity());
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }

        return j;
    }

    @ResponseBody
    @RequiresPermissions("outbound:banQinWmLdDetail:cancelLoading")
    @RequestMapping(value = "cancelByLdNoAndLineNo")
    public AjaxJson cancelByLdNoAndLineNo(@RequestBody BanQinWmLdDetailEntity entity) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinOutboundLdService.cancelByLdNoAndLineNo(entity.getLdNo(), entity.getLineNo().split(",", -1), entity.getOrgId());
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }

        return j;
    }

}