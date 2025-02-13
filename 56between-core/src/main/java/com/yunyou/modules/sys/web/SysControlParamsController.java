package com.yunyou.modules.sys.web;

import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.sys.entity.SysControlParams;
import com.yunyou.modules.sys.service.SysControlParamsService;
import com.yunyou.modules.sys.utils.SysControlParamsUtils;
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
 * 系统控制参数Controller
 *
 * @author Jianhua Liu
 * @version 2019-08-12
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/sysControlParams")
public class SysControlParamsController extends BaseController {

    @Autowired
    private SysControlParamsService sysControlParamsService;

    @ModelAttribute
    public SysControlParams get(@RequestParam(required = false) String id) {
        SysControlParams entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = sysControlParamsService.get(id);
        }
        if (entity == null) {
            entity = new SysControlParams();
        }
        return entity;
    }

    /**
     * 系统控制参数列表页面
     */
    @RequiresPermissions("sys:sysControlParams:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/sys/sysControlParamsList";
    }

    /**
     * 系统控制参数列表数据
     */
    @ResponseBody
    @RequiresPermissions("sys:sysControlParams:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(SysControlParams sysControlParams, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(sysControlParamsService.findPage(new Page<>(request, response), sysControlParams));
    }

    /**
     * 查看，增加，编辑系统控制参数表单页面
     */
    @RequiresPermissions(value = {"sys:sysControlParams:view", "sys:sysControlParams:add", "sys:sysControlParams:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(SysControlParams sysControlParams, Model model) {
        model.addAttribute("sysControlParams", sysControlParams);
        return "modules/sys/sysControlParamsForm";
    }

    /**
     * 保存系统控制参数
     */
    @ResponseBody
    @RequiresPermissions(value = {"sys:sysControlParams:add", "sys:sysControlParams:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(SysControlParams sysControlParams, Model model) {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, sysControlParams)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            sysControlParams.setCode(sysControlParams.getCode().trim().toUpperCase());
            sysControlParamsService.saveEntity(sysControlParams);
        } catch (DuplicateKeyException e) {
            j.setSuccess(false);
            j.setMsg("机构[" + sysControlParams.getOrgName() + "]下参数[" + sysControlParams.getCode() + "]已存在");
        } catch (RuntimeException e) {
            logger.error("", e);
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 删除系统控制参数
     */
    @ResponseBody
    @RequiresPermissions("sys:sysControlParams:del")
    @RequestMapping(value = "delete")
    public AjaxJson delete(SysControlParams sysControlParams) {
        AjaxJson j = new AjaxJson();
        sysControlParamsService.deleteEntity(sysControlParams);
        j.setMsg("删除系统控制参数成功");
        return j;
    }

    /**
     * 批量删除系统控制参数
     */
    @ResponseBody
    @RequiresPermissions("sys:sysControlParams:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            sysControlParamsService.delete(new SysControlParams(id));
        }
        j.setMsg("删除系统控制参数成功");
        return j;
    }

    @ResponseBody
    @RequestMapping(value = "getValue")
    public String getValue(String paramCode, String orgId) {
        return SysControlParamsUtils.getValue(paramCode, orgId);
    }

}