package com.yunyou.modules.wms.qc.web;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.wms.inbound.service.BanQinInboundQcOperationService;
import com.yunyou.modules.wms.qc.entity.BanQinWmQcDetailEntity;
import com.yunyou.modules.wms.qc.service.BanQinWmQcDetailService;
import com.yunyou.modules.wms.task.entity.BanQinWmTaskPaEntity;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 质检单明细Controller
 * @author WMJ
 * @version 2019-01-26
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/qc/banQinWmQcDetail")
public class BanQinWmQcDetailController extends BaseController {
    @Autowired
    private BanQinWmQcDetailService banQinWmQcDetailService;
    @Autowired
    private BanQinInboundQcOperationService banQinWmQcDetailEntity;

    /**
     * 质检单明细列表数据
     */
    @ResponseBody
    @RequestMapping(value = "data")
    public Map<String, Object> data(BanQinWmQcDetailEntity banQinWmQcDetailEntity, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<BanQinWmQcDetailEntity> page = banQinWmQcDetailService.findPage(new Page<BanQinWmQcDetailEntity>(request, response), banQinWmQcDetailEntity);
        return getBootstrapData(page);
    }

    /**
     * 保存质检单明细
     */
    @ResponseBody
    @RequiresPermissions(value = "qc:banQinWmQcDetail:edit")
    @RequestMapping(value = "save")
    public AjaxJson save(@RequestBody BanQinWmQcDetailEntity banQinWmQcDetailEntity, Model model, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinWmQcDetailService.saveAndSetQcSuggest(banQinWmQcDetailEntity);
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 删除上架任务
     */
    @ResponseBody
    @RequiresPermissions(value = "qc:banQinWmQcDetail:deleteTaskPa")
    @RequestMapping(value = "deleteTaskPa")
    public AjaxJson deleteTaskPa(@RequestBody List<BanQinWmTaskPaEntity> list, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinWmQcDetailEntity.batchRemoveTaskPa(list);
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 上架确认
     */
    @ResponseBody
    @RequiresPermissions(value = "qc:banQinWmQcDetail:putAway")
    @RequestMapping(value = "putAway")
    public AjaxJson putAway(@RequestBody List<BanQinWmTaskPaEntity> list, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinWmQcDetailEntity.batchPutaway(list);
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

}