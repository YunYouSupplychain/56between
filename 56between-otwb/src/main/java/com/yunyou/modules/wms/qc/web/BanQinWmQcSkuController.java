package com.yunyou.modules.wms.qc.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yunyou.common.ResultMessage;
import com.yunyou.modules.wms.inbound.service.BanQinInboundQcOperationService;
import com.yunyou.modules.wms.qc.entity.BanQinWmQcSkuEntity;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.yunyou.common.json.AjaxJson;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.wms.qc.service.BanQinWmQcSkuService;

/**
 * 质检单商品Controller
 *
 * @author WMJ
 * @version 2019-01-26
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/qc/banQinWmQcSku")
public class BanQinWmQcSkuController extends BaseController {
    @Autowired
    private BanQinWmQcSkuService banQinWmQcSkuService;
    @Autowired
    private BanQinInboundQcOperationService inboundQcOperationService;

    /**
     * 质检单商品列表数据
     */
    @ResponseBody
    @RequestMapping(value = "data")
    public Map<String, Object> data(BanQinWmQcSkuEntity banQinWmQcSkuEntity, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<BanQinWmQcSkuEntity> page = banQinWmQcSkuService.findPage(new Page<BanQinWmQcSkuEntity>(request, response), banQinWmQcSkuEntity);
        return getBootstrapData(page);
    }

    /**
     * 批量删除质检单商品
     */
    @ResponseBody
    @RequiresPermissions("qc:banQinWmQcSku:remove")
    @RequestMapping(value = "remove")
    public AjaxJson remove(BanQinWmQcSkuEntity entity, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = inboundQcOperationService.removeQcSkuEntity(entity.getQcNo(), entity.getLineNo().split(","), entity.getOrgId());
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 质检确认
     */
    @ResponseBody
    @RequiresPermissions("qc:banQinWmQcSku:qcConfirm")
    @RequestMapping(value = "qcConfirm")
    public AjaxJson qcConfirm(@RequestBody List<BanQinWmQcSkuEntity> list, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = inboundQcOperationService.inboundBatchQcConfirm(list);
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 取消质检
     */
    @ResponseBody
    @RequiresPermissions("qc:banQinWmQcSku:cancelQcConfirm")
    @RequestMapping(value = "cancelQcConfirm")
    public AjaxJson cancelQcConfirm(BanQinWmQcSkuEntity entity, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = inboundQcOperationService.batchCancelQcConfirm(entity.getQcNo(), entity.getLineNo().split(","), entity.getOrgId());
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 生成上架任务
     */
    @ResponseBody
    @RequiresPermissions("qc:banQinWmQcSku:createPaTask")
    @RequestMapping(value = "createPaTask")
    public AjaxJson createPaTask(@RequestBody List<BanQinWmQcSkuEntity> list, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = inboundQcOperationService.batchCreateTaskPa(list);
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

}