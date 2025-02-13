package com.yunyou.modules.sys.common.web;

import com.yunyou.common.exception.GlobalException;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.interfaces.interactive.action.SyncPlatformDataCommonAction;
import com.yunyou.modules.sys.common.entity.SysCommonCustomerContacts;
import com.yunyou.modules.sys.common.entity.extend.SysCommonCustomerEntity;
import com.yunyou.modules.sys.common.service.SysCommonCustomerContactsService;
import com.yunyou.modules.sys.common.service.SysCommonCustomerService;
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
import javax.validation.Valid;
import java.util.Arrays;
import java.util.Map;

/**
 * 客户信息Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/common/customer")
public class SysCommonCustomerController extends BaseController {
    @Autowired
    private SysCommonCustomerService sysCommonCustomerService;
    @Autowired
    private SysCommonCustomerContactsService sysCommonCustomerContactsService;
    @Autowired
    private SyncPlatformDataCommonAction syncPlatformDataCommonAction;

    /**
     * 列表页面
     */
    @RequiresPermissions("sys:common:customer:list")
    @RequestMapping(value = {"list", ""})
    public String list(Model model) {
        model.addAttribute("sysCommonCustomerEntity", new SysCommonCustomerEntity());
        return "modules/sys/common/sysCommonCustomerList";
    }

    /**
     * 列表数据
     */
    @ResponseBody
    @RequiresPermissions("sys:common:customer:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(SysCommonCustomerEntity entity, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(entity.getDataSet())) {
            entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
        }
        return getBootstrapData(sysCommonCustomerService.findPage(new Page<SysCommonCustomerEntity>(request, response), entity));
    }

    /**
     * 查看，增加，编辑表单页面
     */
    @RequiresPermissions(value = {"sys:common:customer:view", "sys:common:customer:add", "sys:common:customer:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(SysCommonCustomerEntity entity, Model model) {
        if (StringUtils.isNotBlank(entity.getId())) {
            entity = sysCommonCustomerService.getEntity(entity.getId());
        }
        model.addAttribute("sysCommonCustomerEntity", entity);
        return "modules/sys/common/sysCommonCustomerForm";
    }

    /**
     * 保存
     */
    @ResponseBody
    @RequiresPermissions(value = {"sys:common:customer:add", "sys:common:customer:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(@Valid SysCommonCustomerEntity entity, Model model) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, entity)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            sysCommonCustomerService.save(entity);
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
     * 批量删除
     */
    @ResponseBody
    @RequiresPermissions("sys:common:customer:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            sysCommonCustomerService.delete(sysCommonCustomerService.get(id));
        }
        j.setMsg("删除成功");
        return j;
    }

    @ResponseBody
    @RequiresPermissions("sys:common:customer:del")
    @RequestMapping(value = "/contacts/deleteAll")
    public AjaxJson deleteContacts(String ids) {
        AjaxJson j = new AjaxJson();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            sysCommonCustomerContactsService.remove(new SysCommonCustomerContacts(id));
        }
        j.setMsg("删除成功");
        return j;
    }

    @ResponseBody
    @RequiresPermissions("sys:common:customer:sync")
    @RequestMapping(value = "syncSelect", method = RequestMethod.POST)
    public AjaxJson syncSelect(String ids) {
        AjaxJson j = new AjaxJson();
        try {
            SysCommonCustomerEntity entity = new SysCommonCustomerEntity();
            entity.setIds(Arrays.asList(ids.split(",")));
            syncPlatformDataCommonAction.syncCustomer(sysCommonCustomerService.findSync(entity));
        } catch (Exception e) {
            logger.error("同步异常", e);
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    @ResponseBody
    @RequiresPermissions("sys:common:customer:sync")
    @RequestMapping(value = "syncAll", method = {RequestMethod.GET, RequestMethod.POST})
    public AjaxJson syncAll(SysCommonCustomerEntity entity) {
        AjaxJson j = new AjaxJson();
        try {
            if (StringUtils.isBlank(entity.getDataSet())) {
                entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
            }
            syncPlatformDataCommonAction.syncCustomer(sysCommonCustomerService.findSync(entity));
        } catch (Exception e) {
            logger.error("同步异常", e);
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    @ResponseBody
    @RequestMapping(value = "grid")
    public Map<String, Object> grid(SysCommonCustomerEntity entity, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(entity.getDataSet())) {
            entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
        }
        return getBootstrapData(sysCommonCustomerService.findGrid(new Page<>(request, response), entity));
    }

}