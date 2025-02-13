package com.yunyou.modules.sys.common.web;

import com.yunyou.common.exception.GlobalException;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.BaseEntity;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.interfaces.interactive.action.SyncPlatformDataToTmsAction;
import com.yunyou.modules.sys.common.entity.SysTmsVehicle;
import com.yunyou.modules.sys.common.entity.extend.SysTmsVehicleEntity;
import com.yunyou.modules.sys.common.service.SysTmsVehicleService;
import com.yunyou.modules.sys.utils.SysDataSetUtils;
import com.yunyou.modules.sys.utils.UserUtils;
import com.yunyou.modules.tms.common.TmsConstants;
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
 * 车辆信息Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/common/tms/vehicle")
public class SysTmsVehicleController extends BaseController {
    @Autowired
    private SysTmsVehicleService sysTmsVehicleService;
    @Autowired
    private SyncPlatformDataToTmsAction syncPlatformDataToTmsAction;

    @ModelAttribute
    public SysTmsVehicleEntity get(@RequestParam(required = false) String id) {
        SysTmsVehicleEntity entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = sysTmsVehicleService.getEntity(id);
        }
        if (entity == null) {
            entity = new SysTmsVehicleEntity();
        }
        return entity;
    }

    /**
     * 列表页面
     */
    @RequiresPermissions("sys:common:tms:vehicle:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/sys/common/sysTmsVehicleList";
    }

    /**
     * 列表数据
     */
    @ResponseBody
    @RequiresPermissions("sys:common:tms:vehicle:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(SysTmsVehicleEntity entity, HttpServletRequest request, HttpServletResponse response, Model model) {
        if (StringUtils.isBlank(entity.getDataSet())) {
            entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
        }
        return getBootstrapData(sysTmsVehicleService.findPage(new Page<SysTmsVehicleEntity>(request, response), entity));
    }

    /**
     * 查看，增加，编辑表单页面
     */
    @RequiresPermissions(value = {"sys:common:tms:vehicle:view", "sys:common:tms:vehicle:add", "sys:common:tms:vehicle:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(SysTmsVehicleEntity sysTmsVehicleEntity, Model model) {
        model.addAttribute("sysTmsVehicleEntity", sysTmsVehicleEntity);
        return "modules/sys/common/sysTmsVehicleForm";
    }

    /**
     * 保存
     */
    @ResponseBody
    @RequiresPermissions(value = {"sys:common:tms:vehicle:add", "sys:common:tms:vehicle:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(SysTmsVehicle sysTmsVehicle, Model model) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, sysTmsVehicle)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            sysTmsVehicleService.saveValidator(sysTmsVehicle);
            sysTmsVehicleService.save(sysTmsVehicle);
            j.put("entity", sysTmsVehicle);
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
    @RequiresPermissions("sys:common:tms:vehicle:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();

        StringBuilder errMsg = new StringBuilder();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            SysTmsVehicle sysTmsVehicle = sysTmsVehicleService.get(id);
            try {
                sysTmsVehicleService.delete(sysTmsVehicle);
            } catch (Exception e) {
                logger.error("删除id=[" + id + "]", e);
                errMsg.append("<br>").append("车辆[").append(sysTmsVehicle.getCarNo()).append("]删除失败");
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
    @RequiresPermissions(value = "sys:common:tms:vehicle:enable")
    @RequestMapping(value = "enable")
    public AjaxJson enable(@RequestParam String ids, @RequestParam String flag) {
        AjaxJson j = new AjaxJson();

        StringBuilder errMsg = new StringBuilder();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            SysTmsVehicle sysTmsVehicle = sysTmsVehicleService.get(id);
            try {
                sysTmsVehicle.setDelFlag("0".equals(flag) ? BaseEntity.DEL_FLAG_NORMAL : BaseEntity.DEL_FLAG_DELETE);
                sysTmsVehicle.setUpdateBy(UserUtils.getUser());
                sysTmsVehicle.setUpdateDate(new Date());
                sysTmsVehicleService.save(sysTmsVehicle);
            } catch (Exception e) {
                logger.error("启用/停用车辆id=[" + id + "]", e);
                errMsg.append("<br>").append("车牌号[").append(sysTmsVehicle.getCarNo()).append("]操作失败");
            }
        }
        if (StringUtils.isNotBlank(errMsg)) {
            j.setMsg("操作成功，其中" + errMsg.toString());
        }
        return j;
    }

    /**
     * 可用/不可用
     */
    @ResponseBody
    @RequiresPermissions(value = "sys:common:tms:vehicle:updateStatus")
    @RequestMapping(value = "updateStatus")
    public AjaxJson updateStatus(@RequestParam String ids, @RequestParam String flag) {
        AjaxJson j = new AjaxJson();

        StringBuilder errMsg = new StringBuilder();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            SysTmsVehicle sysTmsVehicle = sysTmsVehicleService.get(id);
            try {
                sysTmsVehicle.setStatus("0".equals(flag) ? TmsConstants.VEHICLE_STATUS_00 : TmsConstants.VEHICLE_STATUS_01);
                sysTmsVehicle.setUpdateBy(UserUtils.getUser());
                sysTmsVehicle.setUpdateDate(new Date());
                sysTmsVehicleService.save(sysTmsVehicle);
            } catch (Exception e) {
                logger.error("可用/不可用车辆id=[" + id + "]", e);
                errMsg.append("<br>").append("车牌号[").append(sysTmsVehicle.getCarNo()).append("]操作失败");
            }
        }
        if (StringUtils.isNotBlank(errMsg)) {
            j.setMsg("操作成功，其中" + errMsg.toString());
        }
        return j;
    }

    @ResponseBody
    @RequiresPermissions("sys:common:tms:vehicle:sync")
    @RequestMapping(value = "syncSelect", method = RequestMethod.POST)
    public AjaxJson syncSelect(String ids) {
        AjaxJson j = new AjaxJson();
        try {
            String[] idArr = ids.split(",");
            for (String id : idArr) {
                sysTmsVehicleService.findSync(new SysTmsVehicle(id)).forEach(syncPlatformDataToTmsAction::sync);
            }
        } catch (Exception e) {
            logger.error("同步异常", e);
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    @ResponseBody
    @RequiresPermissions("sys:common:tms:vehicle:sync")
    @RequestMapping(value = "syncAll", method = {RequestMethod.GET, RequestMethod.POST})
    public AjaxJson syncAll(SysTmsVehicle entity) {
        AjaxJson j = new AjaxJson();
        try {
            if (StringUtils.isBlank(entity.getDataSet())) {
                entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
            }
            sysTmsVehicleService.findSync(entity).forEach(syncPlatformDataToTmsAction::sync);
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
    public Map<String, Object> grid(SysTmsVehicleEntity entity, HttpServletRequest request, HttpServletResponse response, Model model) {
        if (StringUtils.isBlank(entity.getDataSet())) {
            entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
        }
        return getBootstrapData(sysTmsVehicleService.findGrid(new Page<SysTmsVehicleEntity>(request, response), entity));
    }

}