package com.yunyou.modules.bms.basic.web;

import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.bms.basic.entity.extend.SettlementSkuEntity;
import com.yunyou.modules.bms.basic.service.SettlementSkuService;
import com.yunyou.modules.bms.common.BmsException;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 结算商品Controller
 *
 * @author Jianhua Liu
 * @version 2019-06-20
 */
@Controller
@RequestMapping(value = "${adminPath}/bms/basic/settlementSku")
public class SettlementSkuController extends BaseController {
    @Autowired
    private SettlementSkuService settlementSkuService;

    @ModelAttribute
    public SettlementSkuEntity get(@RequestParam(required = false) String id) {
        SettlementSkuEntity entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = settlementSkuService.getEntity(id);
        }
        if (entity == null) {
            entity = new SettlementSkuEntity();
        }
        return entity;
    }

    /**
     * 结算商品列表页面
     */
    @RequiresPermissions("basic:settlementSku:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/bms/basic/settlementSkuList";
    }

    /**
     * 结算商品列表数据
     */
    @ResponseBody
    @RequiresPermissions("basic:settlementSku:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(SettlementSkuEntity entity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(settlementSkuService.findPage(new Page<>(request, response), entity));
    }

    /**
     * 结算商品列表数据(弹出框)
     */
    @ResponseBody
    @RequestMapping(value = "grid")
    public Map<String, Object> grid(SettlementSkuEntity entity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(settlementSkuService.findGrid(new Page<>(request, response), entity));
    }

    /**
     * 结算商品列表数据(弹出框,带供应商)
     */
    @ResponseBody
    @RequestMapping(value = "gridAndSupplier")
    public Map<String, Object> gridAndSupplier(SettlementSkuEntity entity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(settlementSkuService.findGridDataAndSupplier(new Page<>(request, response), entity));
    }

    /**
     * 查看，增加，编辑结算商品表单页面
     */
    @RequiresPermissions(value = {"basic:settlementSku:view", "basic:settlementSku:add", "basic:settlementSku:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(SettlementSkuEntity settlementSkuEntity, Model model) {
        model.addAttribute("settlementSkuEntity", settlementSkuEntity);
        return "modules/bms/basic/settlementSkuForm";
    }

    /**
     * 保存结算商品
     */
    @ResponseBody
    @RequiresPermissions(value = {"basic:settlementSku:add", "basic:settlementSku:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(SettlementSkuEntity settlementSkuEntity, Model model) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, settlementSkuEntity)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            settlementSkuService.save(settlementSkuEntity);

            j.put("entity", settlementSkuEntity);
        } catch (BmsException e) {
            j.setSuccess(true);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 删除结算商品
     */
    @ResponseBody
    @RequiresPermissions("basic:settlementSku:del")
    @RequestMapping(value = "delete")
    public AjaxJson delete(SettlementSkuEntity entity) {
        AjaxJson j = new AjaxJson();
        settlementSkuService.delete(entity);
        j.setMsg("删除结算商品成功");
        return j;
    }

    /**
     * 批量删除结算商品
     */
    @ResponseBody
    @RequiresPermissions("basic:settlementSku:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            settlementSkuService.delete(settlementSkuService.getEntity(id));
        }
        j.setMsg("删除结算商品成功");
        return j;
    }

}