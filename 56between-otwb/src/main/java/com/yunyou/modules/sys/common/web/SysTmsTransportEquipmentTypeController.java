package com.yunyou.modules.sys.common.web;

import com.yunyou.common.exception.GlobalException;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.BaseEntity;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.interfaces.interactive.action.SyncPlatformDataToTmsAction;
import com.yunyou.modules.sys.common.entity.SysTmsTransportEquipmentType;
import com.yunyou.modules.sys.common.entity.extend.SysTmsTransportEquipmentTypeEntity;
import com.yunyou.modules.sys.common.service.SysTmsTransportEquipmentTypeService;
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
 * 运输设备类型Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/common/tms/transportEquipmentType")
public class SysTmsTransportEquipmentTypeController extends BaseController {
    @Autowired
    private SysTmsTransportEquipmentTypeService sysTmsTransportEquipmentTypeService;
    @Autowired
    private SyncPlatformDataToTmsAction syncPlatformDataToTmsAction;

    @ModelAttribute
    public SysTmsTransportEquipmentTypeEntity get(@RequestParam(required = false) String id) {
        SysTmsTransportEquipmentTypeEntity entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = sysTmsTransportEquipmentTypeService.getEntity(id);
        }
        if (entity == null) {
            entity = new SysTmsTransportEquipmentTypeEntity();
        }
        return entity;
    }

    /**
     * 列表页面
     */
    @RequiresPermissions("sys:common:tms:transportEquipmentType:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/sys/common/sysTmsTransportEquipmentTypeList";
    }

    /**
     * 列表数据
     */
    @ResponseBody
    @RequiresPermissions("sys:common:tms:transportEquipmentType:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(SysTmsTransportEquipmentTypeEntity entity, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(entity.getDataSet())) {
            entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
        }
        return getBootstrapData(sysTmsTransportEquipmentTypeService.findPage(new Page<SysTmsTransportEquipmentTypeEntity>(request, response), entity));
    }

    /**
     * 查看，增加，编辑表单页面
     */
    @RequiresPermissions(value = {"sys:common:tms:transportEquipmentType:view", "sys:common:tms:transportEquipmentType:add", "sys:common:tms:transportEquipmentType:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(SysTmsTransportEquipmentTypeEntity sysTmsTransportEquipmentTypeEntity, Model model) {
        model.addAttribute("sysTmsTransportEquipmentTypeEntity", sysTmsTransportEquipmentTypeEntity);
        return "modules/sys/common/sysTmsTransportEquipmentTypeForm";
    }

    /**
     * 保存
     */
    @ResponseBody
    @RequiresPermissions(value = {"sys:common:tms:transportEquipmentType:add", "sys:common:tms:transportEquipmentType:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(SysTmsTransportEquipmentType sysTmsTransportEquipmentType, Model model) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, sysTmsTransportEquipmentType)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            sysTmsTransportEquipmentTypeService.saveValidator(sysTmsTransportEquipmentType);
            sysTmsTransportEquipmentTypeService.save(sysTmsTransportEquipmentType);
            j.put("entity", sysTmsTransportEquipmentType);
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
    @RequiresPermissions("sys:common:tms:transportEquipmentType:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();

        StringBuilder errMsg = new StringBuilder();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            SysTmsTransportEquipmentType sysTmsTransportEquipmentType = sysTmsTransportEquipmentTypeService.get(id);
            try {
                sysTmsTransportEquipmentTypeService.delete(sysTmsTransportEquipmentType);
            } catch (Exception e) {
                logger.error("删除id=[" + id + "]", e);
                errMsg.append("<br>").append("编码[").append(sysTmsTransportEquipmentType.getTransportEquipmentTypeCode()).append("]删除失败");
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
    @RequiresPermissions(value = {"sys:common:tms:transportEquipmentType:enable", "sys:common:tms:transportEquipmentType:unable"}, logical = Logical.OR)
    @RequestMapping(value = "enable")
    public AjaxJson enable(@RequestParam String ids, @RequestParam String flag) {
        AjaxJson j = new AjaxJson();

        StringBuilder errMsg = new StringBuilder();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            SysTmsTransportEquipmentType sysTmsTransportEquipmentType = sysTmsTransportEquipmentTypeService.get(id);
            try {
                sysTmsTransportEquipmentType.setDelFlag("0".equals(flag) ? BaseEntity.DEL_FLAG_NORMAL : BaseEntity.DEL_FLAG_DELETE);
                sysTmsTransportEquipmentType.setUpdateBy(UserUtils.getUser());
                sysTmsTransportEquipmentType.setUpdateDate(new Date());
                sysTmsTransportEquipmentTypeService.save(sysTmsTransportEquipmentType);
            } catch (GlobalException e) {
                errMsg.append("<br>").append("[").append(sysTmsTransportEquipmentType.getTransportEquipmentTypeCode()).append("]").append(e.getMessage());
            } catch (Exception e) {
                logger.error("启用/停用id=[" + id + "]", e);
                errMsg.append("<br>").append("[").append(sysTmsTransportEquipmentType.getTransportEquipmentTypeCode()).append("]操作失败");
            }
        }
        if (StringUtils.isNotBlank(errMsg)) {
            j.setMsg("操作成功，其中" + errMsg.toString());
        }
        return j;
    }

    @ResponseBody
    @RequiresPermissions("sys:common:tms:transportEquipmentType:sync")
    @RequestMapping(value = "syncSelect", method = RequestMethod.POST)
    public AjaxJson syncSelect(String ids) {
        AjaxJson j = new AjaxJson();
        try {
            String[] idArr = ids.split(",");
            for (String id : idArr) {
                sysTmsTransportEquipmentTypeService.findSync(new SysTmsTransportEquipmentType(id)).forEach(syncPlatformDataToTmsAction::sync);
            }
        } catch (Exception e) {
            logger.error("同步异常", e);
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    @ResponseBody
    @RequiresPermissions("sys:common:tms:transportEquipmentType:sync")
    @RequestMapping(value = "syncAll", method = {RequestMethod.GET, RequestMethod.POST})
    public AjaxJson syncAll(SysTmsTransportEquipmentType entity) {
        AjaxJson j = new AjaxJson();
        try {
            if (StringUtils.isBlank(entity.getDataSet())) {
                entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
            }
            sysTmsTransportEquipmentTypeService.findSync(entity).forEach(syncPlatformDataToTmsAction::sync);
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
    public Map<String, Object> grid(SysTmsTransportEquipmentTypeEntity entity, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(entity.getDataSet())) {
            entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
        }
        return getBootstrapData(sysTmsTransportEquipmentTypeService.findGrid(new Page<SysTmsTransportEquipmentTypeEntity>(request, response), entity));
    }

}