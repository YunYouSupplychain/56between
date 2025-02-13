package com.yunyou.modules.sys.web;

import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.sys.entity.SysRuleGenNo;
import com.yunyou.modules.sys.service.SysRuleGenNoService;
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
 * 编号生成规则Controller
 *
 * @author 张庆生
 * @version 2017-10-17
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/ruleGenNo")
public class SysRuleGenNoController extends BaseController {
    @Autowired
    private SysRuleGenNoService sysRuleGenNoService;

    @ModelAttribute
    public SysRuleGenNo get(@RequestParam(required = false) String id) {
        SysRuleGenNo entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = sysRuleGenNoService.get(id);
        }
        if (entity == null) {
            entity = new SysRuleGenNo();
        }
        return entity;
    }

    /**
     * 编号生成规则列表页面
     */
    @RequiresPermissions("sys:ruleGenNo:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/sys/sysRuleGenNoList";
    }

    /**
     * 编号生成规则列表数据
     */
    @ResponseBody
    @RequiresPermissions("sys:ruleGenNo:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(SysRuleGenNo sysRuleGenNo, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(sysRuleGenNoService.findPage(new Page<>(request, response), sysRuleGenNo));
    }

    /**
     * 查看，增加，编辑编号生成规则表单页面
     */
    @RequiresPermissions(value = {"sys:ruleGenNo:view", "sys:ruleGenNo:add", "sys:ruleGenNo:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(SysRuleGenNo sysRuleGenNo, Model model) {
        model.addAttribute("sysRuleGenNo", sysRuleGenNo);
        if (StringUtils.isBlank(sysRuleGenNo.getId())) {
            model.addAttribute("isAdd", true);
        }
        return "modules/sys/sysRuleGenNoForm";
    }

    /**
     * 保存编号生成规则
     */
    @ResponseBody
    @RequiresPermissions(value = {"sys:ruleGenNo:add", "sys:ruleGenNo:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(SysRuleGenNo sysRuleGenNo, Model model) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, sysRuleGenNo)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        sysRuleGenNoService.save(sysRuleGenNo);
        return j;
    }

    /**
     * 删除编号生成规则
     */
    @ResponseBody
    @RequiresPermissions("sys:ruleGenNo:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();
        String[] idArr = ids.split(",");
        for (String id : idArr) {
            sysRuleGenNoService.delete(new SysRuleGenNo(id));
        }
        j.setMsg("删除编号生成规则成功");
        return j;
    }

}