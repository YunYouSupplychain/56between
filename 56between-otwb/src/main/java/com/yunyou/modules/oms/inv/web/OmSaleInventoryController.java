package com.yunyou.modules.oms.inv.web;

import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.oms.inv.entity.OmSaleInventoryEntity;
import com.yunyou.modules.oms.inv.service.OmSaleInventoryService;
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
 * 销售库存Controller
 *
 * @author Jianhua Liu
 * @version 2019-05-09
 */
@Controller
@RequestMapping(value = "${adminPath}/oms/inv/omSaleInventory")
public class OmSaleInventoryController extends BaseController {

    @Autowired
    private OmSaleInventoryService omSaleInventoryService;

    @ModelAttribute
    public OmSaleInventoryEntity get(@RequestParam(required = false) String id) {
        return new OmSaleInventoryEntity();
    }

    /**
     * 销售库存列表页面
     */
    @RequiresPermissions("inv:omSaleInventory:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/oms/inv/omSaleInventoryList";
    }

    /**
     * 销售库存列表数据
     */
    @ResponseBody
    @RequiresPermissions("inv:omSaleInventory:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(OmSaleInventoryEntity omSaleInventoryEntity, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<OmSaleInventoryEntity> page = omSaleInventoryService.findInvPage(new Page(request, response), omSaleInventoryEntity);
        return getBootstrapData(page);
    }

}