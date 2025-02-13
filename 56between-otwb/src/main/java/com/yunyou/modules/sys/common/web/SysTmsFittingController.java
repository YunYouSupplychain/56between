package com.yunyou.modules.sys.common.web;

import com.yunyou.common.exception.GlobalException;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.BaseEntity;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.interfaces.interactive.action.SyncPlatformDataToTmsAction;
import com.yunyou.modules.sys.common.entity.SysTmsFitting;
import com.yunyou.modules.sys.common.entity.extend.SysTmsFittingEntity;
import com.yunyou.modules.sys.common.service.SysTmsFittingService;
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

@Controller
@RequestMapping(value = "${adminPath}/sys/common/tms/fitting")
public class SysTmsFittingController extends BaseController {
    @Autowired
    private SysTmsFittingService sysTmsFittingService;
    @Autowired
    private SyncPlatformDataToTmsAction syncPlatformDataToTmsAction;

    @ModelAttribute
    public SysTmsFittingEntity get(@RequestParam(required = false) String id) {
        SysTmsFittingEntity entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = sysTmsFittingService.getEntity(id);
        }
        if (entity == null) {
            entity = new SysTmsFittingEntity();
        }
        return entity;
    }

    @RequiresPermissions("sys:common:tms:fitting:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/sys/common/sysTmsFittingList";
    }

    @ResponseBody
    @RequiresPermissions("sys:common:tms:fitting:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(SysTmsFittingEntity entity, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(entity.getDataSet())) {
            entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
        }
        return getBootstrapData(sysTmsFittingService.findPage(new Page<SysTmsFittingEntity>(request, response), entity));
    }

    @RequiresPermissions(value = {"sys:common:tms:fitting:view", "sys:common:tms:fitting:add", "sys:common:tms:fitting:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(SysTmsFittingEntity entity, Model model) {
        model.addAttribute("sysTmsFittingEntity", entity);
        return "modules/sys/common/sysTmsFittingForm";
    }

    @ResponseBody
    @RequiresPermissions(value = {"sys:common:tms:fitting:add", "sys:common:tms:fitting:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(SysTmsFitting sysTmsFitting, Model model) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, sysTmsFitting)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            sysTmsFittingService.saveValidator(sysTmsFitting);
            sysTmsFittingService.save(sysTmsFitting);
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

    @ResponseBody
    @RequiresPermissions("sys:common:tms:fitting:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();

        StringBuilder errMsg = new StringBuilder();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            SysTmsFitting sysTmsFitting = sysTmsFittingService.get(id);
            try {
                sysTmsFittingService.delete(sysTmsFitting);
            } catch (Exception e) {
                logger.error("删除配件id=[" + id + "]", e);
                errMsg.append("<br>").append("配件[").append(sysTmsFitting.getFittingCode()).append("]删除失败");
            }
        }
        if (StringUtils.isNotBlank(errMsg)) {
            j.setMsg("操作成功，其中" + errMsg.toString());
        }
        return j;
    }

    @ResponseBody
    @RequiresPermissions(value = {"sys:common:tms:fitting:enable", "sys:common:tms:fitting:unable"}, logical = Logical.OR)
    @RequestMapping(value = "enable")
    public AjaxJson enable(@RequestParam String ids, @RequestParam String flag) {
        AjaxJson j = new AjaxJson();

        StringBuilder errMsg = new StringBuilder();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            SysTmsFitting sysTmsFitting = sysTmsFittingService.get(id);
            try {
                sysTmsFitting.setDelFlag("0".equals(flag) ? BaseEntity.DEL_FLAG_NORMAL : BaseEntity.DEL_FLAG_DELETE);
                sysTmsFitting.setUpdateBy(UserUtils.getUser());
                sysTmsFitting.setUpdateDate(new Date());
                sysTmsFittingService.save(sysTmsFitting);
            } catch (GlobalException e) {
                errMsg.append("<br>").append("配件[").append(sysTmsFitting.getFittingCode()).append("]").append(e.getMessage());
            } catch (Exception e) {
                logger.error("启用/停用配件id=[" + id + "]", e);
                errMsg.append("<br>").append("配件[").append(sysTmsFitting.getFittingCode()).append("]操作失败");
            }
        }
        if (StringUtils.isNotBlank(errMsg)) {
            j.setMsg("操作成功，其中" + errMsg.toString());
        }
        return j;
    }

    @ResponseBody
    @RequiresPermissions("sys:common:tms:fitting:sync")
    @RequestMapping(value = "syncSelect", method = RequestMethod.POST)
    public AjaxJson syncSelect(String ids) {
        AjaxJson j = new AjaxJson();
        try {
            String[] idArr = ids.split(",");
            for (String id : idArr) {
                syncPlatformDataToTmsAction.sync(sysTmsFittingService.get(id));
            }
        } catch (Exception e) {
            logger.error("同步异常", e);
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    @ResponseBody
    @RequiresPermissions("sys:common:tms:fitting:sync")
    @RequestMapping(value = "syncAll", method = {RequestMethod.GET, RequestMethod.POST})
    public AjaxJson syncAll(SysTmsFitting entity) {
        AjaxJson j = new AjaxJson();
        try {
            if (StringUtils.isBlank(entity.getDataSet())) {
                entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
            }
            sysTmsFittingService.findList(entity).forEach(syncPlatformDataToTmsAction::sync);
        } catch (Exception e) {
            logger.error("同步异常", e);
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    @ResponseBody
    @RequestMapping(value = "grid")
    public Map<String, Object> grid(SysTmsFittingEntity entity, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(entity.getDataSet())) {
            entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
        }
        return getBootstrapData(sysTmsFittingService.findGrid(new Page<SysTmsFittingEntity>(request, response), entity));
    }

}
