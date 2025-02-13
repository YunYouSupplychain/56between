package com.yunyou.modules.tms.order.web;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.sys.utils.UserUtils;
import com.yunyou.modules.tms.order.action.TmHandoverOrderAction;
import com.yunyou.modules.tms.order.entity.TmAttachementDetail;
import com.yunyou.modules.tms.order.entity.extend.TmHandoverOrderEntity;
import com.yunyou.modules.tms.order.entity.extend.TmHandoverOrderLabelEntity;
import com.yunyou.modules.tms.order.entity.extend.TmHandoverOrderSkuEntity;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 交接单Controller
 *
 * @author zyf
 * @version 2020-03-30
 */
@Controller
@RequestMapping(value = "${adminPath}/tms/order/tmHandoverOrder")
public class TmHandoverOrderController extends BaseController {

    @Autowired
    private TmHandoverOrderAction tmHandoverOrderAction;

    @ModelAttribute
    public TmHandoverOrderEntity get(@RequestParam(required = false) String id) {
        TmHandoverOrderEntity entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = tmHandoverOrderAction.getEntity(id);
        }
        if (entity == null) {
            entity = new TmHandoverOrderEntity();
        }
        return entity;
    }

    /**
     * 交接单列表页面
     */
    @RequiresPermissions("tms:order:tmHandoverOrder:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/tms/order/tmHandoverOrderList";
    }

    /**
     * 查看，增加，编辑交接单表单页面
     */
    @RequiresPermissions(value = {"tms:order:tmHandoverOrder:view", "tms:order:tmHandoverOrder:add", "tms:order:tmHandoverOrder:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(TmHandoverOrderEntity tmHandoverOrderEntity, Model model) {
        model.addAttribute("tmHandoverOrderEntity", tmHandoverOrderEntity);
        return "modules/tms/order/tmHandoverOrderForm";
    }

    /**
     * 交接单列表数据
     */
    @ResponseBody
    @RequiresPermissions("tms:order:tmHandoverOrder:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(TmHandoverOrderEntity tmHandoverOrderEntity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(tmHandoverOrderAction.findPage(new Page<>(request, response), tmHandoverOrderEntity));
    }

    /**
     * 保存交接单
     */
    @ResponseBody
    @RequiresPermissions(value = {"tms:order:tmHandoverOrder:add", "tms:order:tmHandoverOrder:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(TmHandoverOrderEntity tmHandoverOrderEntity, Model model) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, tmHandoverOrderEntity)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        tmHandoverOrderAction.saveEntity(tmHandoverOrderEntity);
        j.setSuccess(true);
        j.setMsg("保存交接单成功");
        return j;
    }

    /**
     * 批量删除交接单
     */
    @ResponseBody
    @RequiresPermissions("tms:order:tmHandoverOrder:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            tmHandoverOrderAction.batchRemove(ids.split(","), false);
        }
        j.setMsg("删除交接单成功");
        return j;
    }

    /**
     * 交接单交接
     */
    @ResponseBody
    @RequiresPermissions("tms:order:tmHandoverOrder:handover")
    @RequestMapping(value = "handover")
    public AjaxJson handover(String ids) {
        ResultMessage message = tmHandoverOrderAction.handover(ids.split(","));

        AjaxJson j = new AjaxJson();
        j.setSuccess(message.isSuccess());
        j.setMsg(message.getMessage());
        return j;
    }

    /**
     * 导出excel文件
     */
    @ResponseBody
    @RequiresPermissions("tms:order:tmHandoverOrder:export")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public AjaxJson exportFile(TmHandoverOrderEntity tmHandoverOrderEntity, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            Page<TmHandoverOrderEntity> page = tmHandoverOrderAction.findPage(new Page<>(request, response, -1), tmHandoverOrderEntity);
            new ExportExcel("交接单", TmHandoverOrderEntity.class).setDataList(page.getList()).write(response, "交接单.xlsx").dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出交接单记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 交接单标签数据
     */
    @ResponseBody
    @RequiresPermissions("tms:order:tmHandoverOrder:list")
    @RequestMapping(value = "/label/data")
    public Map<String, Object> labelData(TmHandoverOrderLabelEntity tmHandoverOrderLabelEntity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(tmHandoverOrderAction.findLabelPage(new Page<>(request, response), tmHandoverOrderLabelEntity));
    }

    /**
     * 交接单商品数据
     */
    @ResponseBody
    @RequiresPermissions("tms:order:tmHandoverOrder:list")
    @RequestMapping(value = "/sku/data")
    public Map<String, Object> skuData(TmHandoverOrderSkuEntity tmHandoverOrderSkuEntity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(tmHandoverOrderAction.findSkuPage(new Page<>(request, response), tmHandoverOrderSkuEntity));
    }

    /**
     * 修改交接单商品数据
     */
    @ResponseBody
    @RequiresPermissions("tms:order:tmHandoverOrder:sku:edit")
    @RequestMapping(value = "/sku/edit")
    public AjaxJson skuEdit(TmHandoverOrderSkuEntity tmHandoverOrderSkuEntity, Model model) {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, tmHandoverOrderSkuEntity)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        ResultMessage message = tmHandoverOrderAction.skuEdit(tmHandoverOrderSkuEntity, UserUtils.getUser());
        j.setSuccess(message.isSuccess());
        j.setMsg(message.getMessage());
        return j;
    }

    /**
     * 交接单交接图片数据
     */
    @ResponseBody
    @RequiresPermissions("tms:order:tmHandoverOrder:list")
    @RequestMapping(value = "/img/data")
    public Map<String, Object> imgData(TmAttachementDetail tmAttachementDetail, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(tmHandoverOrderAction.findImgPage(new Page<>(request, response), tmAttachementDetail));
    }

}