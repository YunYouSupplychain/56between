package com.yunyou.modules.wms.outbound.web;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.time.DateUtils;
import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoDetail;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoDetailEntity;
import com.yunyou.modules.wms.outbound.service.BanQinOutboundOrderCheckService;
import com.yunyou.modules.wms.outbound.service.BanQinWmSoDetailService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;

/**
 * 订单校验Controller
 *
 * @author ZYF
 * @version 2020-05-12
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/outbound/banQinWmOrderCheck")
public class BanQinWmOrderCheckController extends BaseController {
    @Autowired
    private BanQinWmSoDetailService banQinWmSoDetailService;
    @Autowired
    private BanQinOutboundOrderCheckService banQinOutboundOrderCheckService;


    @ModelAttribute
    public BanQinWmSoDetailEntity get(@RequestParam(required = false) String id) {
        return new BanQinWmSoDetailEntity();
    }

    /**
     * 订单校验列表页面
     */
    @RequiresPermissions("outbound:banQinWmOrderCheck:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/wms/outbound/banQinWmOrderCheckList";
    }

    /**
     * 订单校验列表数据
     */
    @ResponseBody
    @RequestMapping(value = "data")
    public Map<String, Object> data(BanQinWmSoDetailEntity banQinWmSoDetailEntity, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<BanQinWmSoDetailEntity> page = banQinWmSoDetailService.findOrderCheckPage(new Page<BanQinWmSoDetailEntity>(request, response), banQinWmSoDetailEntity);
        return getBootstrapData(page);
    }

    /**
     * 分配
     */
    @ResponseBody
    @RequiresPermissions("outbound:banQinWmOrderCheck:alloc")
    @RequestMapping(value = "alloc")
    public AjaxJson alloc(BanQinWmSoDetailEntity banQinWmSoDetailEntity, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinOutboundOrderCheckService.allocBySoLine("BY_SO_LINE", Arrays.asList(banQinWmSoDetailEntity.getSoNo().split("@")), banQinWmSoDetailEntity.getOrgId());
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 平均分配
     */
    @ResponseBody
    @RequiresPermissions("outbound:banQinWmOrderCheck:allocAvg")
    @RequestMapping(value = "allocAvg")
    public AjaxJson allocAvg(BanQinWmSoDetailEntity banQinWmSoDetailEntity, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinOutboundOrderCheckService.allocAvg(Arrays.asList(banQinWmSoDetailEntity.getSoNo().split("@")), banQinWmSoDetailEntity.getOrgId());
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
    @RequiresPermissions("outbound:banQinWmOrderCheck:picking")
    @RequestMapping(value = "picking")
    public AjaxJson picking(BanQinWmSoDetailEntity banQinWmSoDetailEntity, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinOutboundOrderCheckService.pickingBySoLine("BY_SO_LINE", Arrays.asList(banQinWmSoDetailEntity.getSoNo().split("@")), banQinWmSoDetailEntity.getOrgId());
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
    @RequiresPermissions("outbound:banQinWmOrderCheck:cancelAlloc")
    @RequestMapping(value = "cancelAlloc")
    public AjaxJson cancelAlloc(BanQinWmSoDetailEntity banQinWmSoDetailEntity, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinOutboundOrderCheckService.cancelAllocBySoLine("BY_SO_LINE", Arrays.asList(banQinWmSoDetailEntity.getSoNo().split("@")), banQinWmSoDetailEntity.getOrgId());
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
    @RequiresPermissions("outbound:banQinWmOrderCheck:cancelPicking")
    @RequestMapping(value = "cancelPicking")
    public AjaxJson cancelPicking(BanQinWmSoDetailEntity banQinWmSoDetailEntity, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinOutboundOrderCheckService.cancelPickingBySoLine("BY_SO_LINE", Arrays.asList(banQinWmSoDetailEntity.getSoNo().split("@")), banQinWmSoDetailEntity.getOrgId());
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }


    /**
     * 导出excel文件
     */
    @ResponseBody
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public AjaxJson exportFile(BanQinWmSoDetail banQinWmSoDetail, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        try {
            String fileName = "订单校验" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            Page<BanQinWmSoDetail> page = banQinWmSoDetailService.findPage(new Page<BanQinWmSoDetail>(request, response, -1), banQinWmSoDetail);
            new ExportExcel("订单校验", BanQinWmSoDetail.class).setDataList(page.getList()).write(response, fileName).dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出订单校验记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }
}