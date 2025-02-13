package com.yunyou.modules.wms.inbound.web;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.wms.inbound.entity.BanQinWmAsnDetail;
import com.yunyou.modules.wms.inbound.entity.BanQinWmAsnDetailEntity;
import com.yunyou.modules.wms.inbound.service.BanQinInboundDuplicateService;
import com.yunyou.modules.wms.inbound.service.BanQinInboundOperationService;
import com.yunyou.modules.wms.inbound.service.BanQinWmAsnDetailService;
import com.yunyou.modules.wms.inbound.service.BanQinWmAsnHeaderService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 入库单明细Controller
 *
 * @author WMJ
 * @version 2019-01-25
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/inbound/banQinWmAsnDetail")
public class BanQinWmAsnDetailController extends BaseController {
    @Autowired
    private BanQinWmAsnDetailService banQinWmAsnDetailService;
    @Autowired
    private BanQinWmAsnHeaderService banQinWmAsnHeaderService;
    @Autowired
    private BanQinInboundDuplicateService inboundDuplicateService;
    @Autowired
    private BanQinInboundOperationService inboundOperationService;

    /**
     * 入库单明细列表数据
     */
    @ResponseBody
    @RequestMapping(value = "data")
    public Map<String, Object> data(BanQinWmAsnDetailEntity banQinWmAsnDetail, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<BanQinWmAsnDetailEntity> page = banQinWmAsnDetailService.findPage(new Page<BanQinWmAsnDetailEntity>(request, response), banQinWmAsnDetail);
        return getBootstrapData(page);
    }

    /**
     * 保存入库单明细
     */
    @ResponseBody
    @RequiresPermissions("inbound:banQinWmAsnDetail:save")
    @RequestMapping(value = "save")
    public AjaxJson save(@RequestBody BanQinWmAsnDetailEntity banQinWmAsnDetailEntity, Model model) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, banQinWmAsnDetailEntity)) {
            j.setSuccess(false);
            j.setMsg("非法数据！");
        }
        try {
            ResultMessage msg = banQinWmAsnDetailService.saveAsnDetailEntity(banQinWmAsnDetailEntity);
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 批量删除入库单明细
     */
    @ResponseBody
    @RequiresPermissions("inbound:banQinWmAsnDetail:remove")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(BanQinWmAsnDetail banQinWmAsnDetail) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinWmAsnDetailService.removeAsnDetailEntity(banQinWmAsnDetail.getHeadId(), banQinWmAsnDetail.getLineNo().split(","));
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 入库单明细复制
     */
    @ResponseBody
    @RequiresPermissions("inbound:banQinWmAsnDetail:duplicate")
    @RequestMapping(value = "duplicate")
    public AjaxJson duplicate(BanQinWmAsnDetail banQinWmAsnDetail) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = inboundDuplicateService.duplicateAsnDetail(banQinWmAsnDetail.getAsnNo(), banQinWmAsnDetail.getLineNo(), banQinWmAsnDetail.getOrgId());
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 生成入库单码盘明细
     */
    @ResponseBody
    @RequiresPermissions("inbound:banQinWmAsnDetail:palletize")
    @RequestMapping(value = "createPalletize")
    public AjaxJson createPalletize(BanQinWmAsnDetail banQinWmAsnDetail) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = inboundOperationService.createPalletize(banQinWmAsnDetail.getHeadId(), banQinWmAsnDetail.getLineNo().split(","));
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 取消入库单码盘明细
     */
    @ResponseBody
    @RequiresPermissions("inbound:banQinWmAsnDetail:cancelPalletize")
    @RequestMapping(value = "cancelPalletize")
    public AjaxJson cancelPalletize(BanQinWmAsnDetail banQinWmAsnDetail) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = inboundOperationService.cancelPalletize(banQinWmAsnDetail.getHeadId(), banQinWmAsnDetail.getLineNo().split(","));
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 入库单明细生成质检单
     */
    @ResponseBody
    @RequiresPermissions("inbound:banQinWmAsnDetail:createQc")
    @RequestMapping(value = "createQc")
    public AjaxJson createQc(BanQinWmAsnDetail banQinWmAsnDetail) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinWmAsnHeaderService.createQcByAsnLineNo(banQinWmAsnDetail.getAsnNo(), banQinWmAsnDetail.getLineNo().split(","), banQinWmAsnDetail.getOrgId());
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 取消入库单明细订单行
     */
    @ResponseBody
    @RequiresPermissions("inbound:banQinWmAsnDetail:cancel")
    @RequestMapping(value = "cancelAsnDetail")
    public AjaxJson cancelAsnDetail(BanQinWmAsnDetail banQinWmAsnDetail) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinWmAsnDetailService.cancelAsnDetail(banQinWmAsnDetail.getHeadId(), banQinWmAsnDetail.getLineNo().split(","));
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 分摊重量
     */
    @ResponseBody
    @RequiresPermissions("inbound:banQinWmAsnDetail:apportionWeight")
    @RequestMapping(value = "apportionWeight")
    public AjaxJson apportionWeight(BanQinWmAsnDetail banQinWmAsnDetail) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinWmAsnDetailService.apportionWeight(banQinWmAsnDetail.getHeadId(), banQinWmAsnDetail.getLineNo().split(","));
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }
    
}