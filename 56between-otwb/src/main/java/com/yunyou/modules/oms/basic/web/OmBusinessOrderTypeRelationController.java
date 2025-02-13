package com.yunyou.modules.oms.basic.web;

import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.oms.basic.entity.OmBusinessOrderTypeRelation;
import com.yunyou.modules.oms.basic.service.OmBusinessOrderTypeRelationService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
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
 * 业务类型-订单类型关联关系Controller
 *
 * @author zyf
 * @version 2019-07-03
 */
@Controller
@RequestMapping(value = "${adminPath}/oms/basic/omBusinessOrderTypeRelation")
public class OmBusinessOrderTypeRelationController extends BaseController {
    @Autowired
    private OmBusinessOrderTypeRelationService omBusinessOrderTypeRelationService;

    @ModelAttribute
    public OmBusinessOrderTypeRelation get(@RequestParam(required = false) String id) {
        OmBusinessOrderTypeRelation entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = omBusinessOrderTypeRelationService.get(id);
        }
        if (entity == null) {
            entity = new OmBusinessOrderTypeRelation();
        }
        return entity;
    }

    /**
     * 业务类型-订单类型关联关系列表页面
     */
    @RequiresPermissions("basic:omBusinessOrderTypeRelation:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/oms/basic/omBusinessOrderTypeRelationList";
    }

    /**
     * 业务类型-订单类型关联关系列表数据
     */
    @ResponseBody
    @RequiresPermissions("basic:omBusinessOrderTypeRelation:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(OmBusinessOrderTypeRelation omBusinessOrderTypeRelation, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(omBusinessOrderTypeRelationService.findPage(new Page<>(request, response), omBusinessOrderTypeRelation));
    }

    /**
     * 查看，增加，编辑业务类型-订单类型关联关系表单页面
     */
    @RequiresPermissions(value = {"basic:omBusinessOrderTypeRelation:view", "basic:omBusinessOrderTypeRelation:add", "basic:omBusinessOrderTypeRelation:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(OmBusinessOrderTypeRelation omBusinessOrderTypeRelation, Model model) {
        model.addAttribute("omBusinessOrderTypeRelation", omBusinessOrderTypeRelation);
        return "modules/oms/basic/omBusinessOrderTypeRelationForm";
    }

    /**
     * 保存业务类型-订单类型关联关系
     */
    @ResponseBody
    @RequiresPermissions(value = {"basic:omBusinessOrderTypeRelation:add", "basic:omBusinessOrderTypeRelation:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(OmBusinessOrderTypeRelation omBusinessOrderTypeRelation, Model model) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, omBusinessOrderTypeRelation)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            omBusinessOrderTypeRelationService.save(omBusinessOrderTypeRelation);
        } catch (DuplicateKeyException e) {
            j.setSuccess(false);
            j.setMsg("关系已存在");
        }
        return j;
    }

    /**
     * 批量删除业务类型-订单类型关联关系
     */
    @ResponseBody
    @RequiresPermissions("basic:omBusinessOrderTypeRelation:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            omBusinessOrderTypeRelationService.delete(omBusinessOrderTypeRelationService.get(id));
        }
        j.setMsg("删除业务类型-订单类型关联关系成功");
        return j;
    }
}