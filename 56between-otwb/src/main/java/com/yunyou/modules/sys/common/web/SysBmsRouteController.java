package com.yunyou.modules.sys.common.web;

import com.yunyou.common.exception.GlobalException;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.sys.common.entity.SysBmsRoute;
import com.yunyou.modules.sys.common.service.SysBmsRouteService;
import com.yunyou.modules.sys.utils.SysDataSetUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 路由Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/common/bms/route")
public class SysBmsRouteController extends BaseController {
    @Autowired
    private SysBmsRouteService sysBmsRouteService;

    /**
     * 路由列表页面
     */
    @RequiresPermissions("sys:common:bms:route:list")
    @RequestMapping(value = {"list", ""})
    public String list(Model model) {
        model.addAttribute("sysBmsRoute", new SysBmsRoute());
        return "modules/sys/common/sysBmsRouteList";
    }

    /**
     * 路由列表数据
     */
    @ResponseBody
    @RequiresPermissions("sys:common:bms:route:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(SysBmsRoute entity, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(entity.getDataSet())) {
            entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
        }
        return getBootstrapData(sysBmsRouteService.findPage(new Page<>(request, response), entity));
    }

    /**
     * 查看，增加，编辑路由表单页面
     */
    @RequiresPermissions(value = {"sys:common:bms:route:view", "sys:common:bms:route:add", "sys:common:bms:route:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(SysBmsRoute entity, Model model) {
        if (StringUtils.isNotBlank(entity.getId())) {
            entity = sysBmsRouteService.get(entity.getId());
        }
        model.addAttribute("sysBmsRoute", entity);
        return "modules/sys/common/sysBmsRouteForm";
    }

    /**
     * 保存路由
     */
    @ResponseBody
    @RequiresPermissions(value = {"sys:common:bms:route:add", "sys:common:bms:route:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(SysBmsRoute sysBmsRoute, Model model) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, sysBmsRoute)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            sysBmsRouteService.save(sysBmsRoute);
        } catch (GlobalException e) {
            if (logger.isInfoEnabled()) {
                logger.info(null, e);
            }
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } catch (DuplicateKeyException e) {
            if (logger.isWarnEnabled()) {
                logger.warn(null, e);
            }
            j.setSuccess(false);
            j.setMsg("数据已存在");
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error(null, e);
            }
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 批量删除路由
     */
    @ResponseBody
    @RequiresPermissions("sys:common:bms:route:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            sysBmsRouteService.delete(sysBmsRouteService.get(id));
        }
        j.setMsg("删除路由成功");
        return j;
    }

    @ResponseBody
    @RequiresPermissions("sys:common:bms:route:sync")
    @RequestMapping(value = "syncSelect", method = RequestMethod.POST)
    public AjaxJson syncSelect(String ids) {
        AjaxJson j = new AjaxJson();
        try {
        } catch (Exception e) {
            logger.error("同步异常", e);
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    @ResponseBody
    @RequiresPermissions("sys:common:bms:route:sync")
    @RequestMapping(value = "syncAll", method = {RequestMethod.GET, RequestMethod.POST})
    public AjaxJson syncAll(SysBmsRoute entity) {
        AjaxJson j = new AjaxJson();
        try {
        } catch (Exception e) {
            logger.error("同步异常", e);
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 弹出窗数据
     */
    @ResponseBody
    @RequestMapping(value = "grid")
    public Map<String, Object> grid(SysBmsRoute entity, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(entity.getDataSet())) {
            entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
        }
        return getBootstrapData(sysBmsRouteService.findPage(new Page<>(request, response), entity));
    }

}