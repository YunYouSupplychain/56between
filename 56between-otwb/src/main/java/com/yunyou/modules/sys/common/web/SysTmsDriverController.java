package com.yunyou.modules.sys.common.web;

import com.yunyou.common.exception.GlobalException;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.BaseEntity;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.interfaces.interactive.action.SyncPlatformDataToTmsAction;
import com.yunyou.modules.sys.common.entity.SysTmsDriver;
import com.yunyou.modules.sys.common.entity.extend.SysTmsDriverEntity;
import com.yunyou.modules.sys.common.service.SysTmsDriverService;
import com.yunyou.modules.sys.utils.SysDataSetUtils;
import com.yunyou.modules.sys.utils.UserUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * 运输人员信息Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/common/tms/driver")
public class SysTmsDriverController extends BaseController {
    @Autowired
    private SysTmsDriverService sysTmsDriverService;
    @Autowired
    private SyncPlatformDataToTmsAction syncPlatformDataToTmsAction;

    @ModelAttribute
    public SysTmsDriverEntity get(@RequestParam(required = false) String id) {
        SysTmsDriverEntity entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = sysTmsDriverService.getEntity(id);
        }
        if (entity == null) {
            entity = new SysTmsDriverEntity();
        }
        return entity;
    }

    /**
     * 列表页面
     */
    @RequiresPermissions("sys:common:tms:driver:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/sys/common/sysTmsDriverList";
    }

    /**
     * 列表数据
     */
    @ResponseBody
    @RequiresPermissions("sys:common:tms:driver:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(SysTmsDriverEntity entity, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(entity.getDataSet())) {
            entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
        }
        return getBootstrapData(sysTmsDriverService.findPage(new Page<SysTmsDriverEntity>(request, response), entity));
    }

    /**
     * 查看，增加，编辑表单页面
     */
    @RequiresPermissions(value = {"sys:common:tms:driver:view", "sys:common:tms:driver:add", "sys:common:tms:driver:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(SysTmsDriverEntity sysTmsDriverEntity, Model model) {
        model.addAttribute("sysTmsDriverEntity", sysTmsDriverEntity);
        return "modules/sys/common/sysTmsDriverForm";
    }

    /**
     * 保存
     */
    @ResponseBody
    @RequiresPermissions(value = {"sys:common:tms:driver:add", "sys:common:tms:driver:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(SysTmsDriver sysTmsDriver, Model model) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, sysTmsDriver)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            sysTmsDriverService.saveValidator(sysTmsDriver);
            sysTmsDriverService.save(sysTmsDriver);
            j.put("entity", sysTmsDriver);
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
    @RequiresPermissions("sys:common:tms:driver:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();

        StringBuilder errMsg = new StringBuilder();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            SysTmsDriver sysTmsDriver = sysTmsDriverService.get(id);
            try {
                sysTmsDriverService.delete(sysTmsDriver);
            } catch (Exception e) {
                logger.error("删除运输人员id=[" + id + "]", e);
                errMsg.append("<br>").append("运输人员[").append(sysTmsDriver.getCode()).append("]删除失败");
            }
        }
        if (StringUtils.isNotBlank(errMsg)) {
            j.setMsg("操作成功，其中" + errMsg.toString());
        }
        return j;
    }

    /**
     * 启用/停用
     */
    @ResponseBody
    @RequiresPermissions(value = {"sys:common:tms:driver:enable", "sys:common:tms:driver:unable"}, logical = Logical.OR)
    @RequestMapping(value = "enable")
    public AjaxJson enable(@RequestParam String ids, @RequestParam String flag) {
        AjaxJson j = new AjaxJson();

        StringBuilder errMsg = new StringBuilder();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            SysTmsDriver sysTmsDriver = sysTmsDriverService.get(id);
            try {
                sysTmsDriver.setDelFlag("0".equals(flag) ? BaseEntity.DEL_FLAG_NORMAL : BaseEntity.DEL_FLAG_DELETE);
                sysTmsDriver.setUpdateBy(UserUtils.getUser());
                sysTmsDriver.setUpdateDate(new Date());
                sysTmsDriverService.save(sysTmsDriver);
            } catch (GlobalException e) {
                errMsg.append("<br>").append("运输人员[").append(sysTmsDriver.getCode()).append("]").append(e.getMessage());
            } catch (Exception e) {
                logger.error("启用/停用运输人员id=[" + id + "]", e);
                errMsg.append("<br>").append("运输人员[").append(sysTmsDriver.getCode()).append("]操作失败");
            }
        }
        if (StringUtils.isNotBlank(errMsg)) {
            j.setMsg("操作成功，其中" + errMsg.toString());
        }
        return j;
    }

    @ResponseBody
    @RequiresPermissions("sys:common:tms:driver:sync")
    @RequestMapping(value = "syncSelect", method = RequestMethod.POST)
    public AjaxJson syncSelect(String ids) {
        AjaxJson j = new AjaxJson();
        try {
            String[] idArr = ids.split(",");
            for (String id : idArr) {
                sysTmsDriverService.findSync(new SysTmsDriver(id)).forEach(syncPlatformDataToTmsAction::sync);
            }
        } catch (Exception e) {
            logger.error("同步异常", e);
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    @ResponseBody
    @RequiresPermissions("sys:common:tms:driver:sync")
    @RequestMapping(value = "syncAll", method = {RequestMethod.GET, RequestMethod.POST})
    public AjaxJson syncAll(SysTmsDriver entity) {
        AjaxJson j = new AjaxJson();
        try {
            if (StringUtils.isBlank(entity.getDataSet())) {
                entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
            }
            sysTmsDriverService.findSync(entity).forEach(syncPlatformDataToTmsAction::sync);
        } catch (Exception e) {
            logger.error("同步异常", e);
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * Grid列表数据
     */
    @ResponseBody
    @RequestMapping(value = "grid")
    public Map<String, Object> grid(SysTmsDriverEntity entity, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(entity.getDataSet())) {
            entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
        }
        return getBootstrapData(sysTmsDriverService.findGrid(new Page<SysTmsDriverEntity>(request, response), entity));
    }

}