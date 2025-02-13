package com.yunyou.modules.sys.common.web;

import com.yunyou.common.exception.GlobalException;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.BaseEntity;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.interfaces.interactive.action.SyncPlatformDataToBmsAction;
import com.yunyou.modules.interfaces.interactive.action.SyncPlatformDataToTmsAction;
import com.yunyou.modules.sys.common.entity.SysTmsCarrierRouteRelation;
import com.yunyou.modules.sys.common.entity.extend.SysTmsCarrierRouteRelationEntity;
import com.yunyou.modules.sys.common.service.SysTmsCarrierRouteRelationService;
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
 * 承运商路由信息Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/common/tms/carrierRouteRelation")
public class SysTmsCarrierRouteRelationController extends BaseController {
    @Autowired
    private SysTmsCarrierRouteRelationService sysTmsCarrierRouteRelationService;
    @Autowired
    private SyncPlatformDataToTmsAction syncPlatformDataToTmsAction;
    @Autowired
    private SyncPlatformDataToBmsAction syncPlatformDataToBmsAction;

    @ModelAttribute
    public SysTmsCarrierRouteRelationEntity get(@RequestParam(required = false) String id) {
        SysTmsCarrierRouteRelationEntity entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = sysTmsCarrierRouteRelationService.getEntity(id);
        }
        if (entity == null) {
            entity = new SysTmsCarrierRouteRelationEntity();
        }
        return entity;
    }

    /**
     * 列表页面
     */
    @RequiresPermissions("sys:common:tms:carrierRouteRelation:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/sys/common/sysTmsCarrierRouteRelationList";
    }

    /**
     * 列表数据
     */
    @ResponseBody
    @RequiresPermissions("sys:common:tms:carrierRouteRelation:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(SysTmsCarrierRouteRelationEntity entity, HttpServletRequest request, HttpServletResponse response, Model model) {
        if (StringUtils.isBlank(entity.getDataSet())) {
            entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
        }
        return getBootstrapData(sysTmsCarrierRouteRelationService.findPage(new Page<SysTmsCarrierRouteRelationEntity>(request, response), entity));
    }

    /**
     * 查看，增加，编辑表单页面
     */
    @RequiresPermissions(value = {"sys:common:tms:carrierRouteRelation:view", "sys:common:tms:carrierRouteRelation:add", "sys:common:tms:carrierRouteRelation:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(SysTmsCarrierRouteRelationEntity sysTmsCarrierRouteRelationEntity, Model model) {
        model.addAttribute("sysTmsCarrierRouteRelationEntity", sysTmsCarrierRouteRelationEntity);
        return "modules/sys/common/sysTmsCarrierRouteRelationForm";
    }

    /**
     * 保存
     */
    @ResponseBody
    @RequiresPermissions(value = {"sys:common:tms:carrierRouteRelation:add", "sys:common:tms:carrierRouteRelation:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(SysTmsCarrierRouteRelation sysTmsCarrierRouteRelation, Model model) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, sysTmsCarrierRouteRelation)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            sysTmsCarrierRouteRelationService.saveValidator(sysTmsCarrierRouteRelation);
            sysTmsCarrierRouteRelationService.save(sysTmsCarrierRouteRelation);
            j.put("entity", sysTmsCarrierRouteRelation);
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
    @RequiresPermissions("sys:common:tms:carrierRouteRelation:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();

        StringBuilder errMsg = new StringBuilder();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            SysTmsCarrierRouteRelation sysTmsCarrierRouteRelation = sysTmsCarrierRouteRelationService.get(id);
            try {
                sysTmsCarrierRouteRelationService.delete(sysTmsCarrierRouteRelation);
            } catch (Exception e) {
                logger.error("删除路由id=[" + id + "]", e);
                errMsg.append("<br>").append("承运商路由[").append(sysTmsCarrierRouteRelation.getCode()).append("]删除失败");
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
    @RequiresPermissions(value = {"sys:common:tms:carrierRouteRelation:enable", "sys:common:tms:carrierRouteRelation:unable"}, logical = Logical.OR)
    @RequestMapping(value = "enable")
    public AjaxJson enable(@RequestParam String ids, @RequestParam String flag) {
        AjaxJson j = new AjaxJson();

        StringBuilder errMsg = new StringBuilder();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            SysTmsCarrierRouteRelation sysTmsCarrierRouteRelation = sysTmsCarrierRouteRelationService.get(id);
            try {
                sysTmsCarrierRouteRelation.setDelFlag("0".equals(flag) ? BaseEntity.DEL_FLAG_NORMAL : BaseEntity.DEL_FLAG_DELETE);
                sysTmsCarrierRouteRelation.setUpdateBy(UserUtils.getUser());
                sysTmsCarrierRouteRelation.setUpdateDate(new Date());
                sysTmsCarrierRouteRelationService.save(sysTmsCarrierRouteRelation);
            } catch (GlobalException e) {
                errMsg.append("<br>").append("承运商路由[").append(sysTmsCarrierRouteRelation.getCode()).append("]").append(e.getMessage());
            } catch (Exception e) {
                logger.error("启用/停用路由id=[" + id + "]", e);
                errMsg.append("<br>").append("承运商路由[").append(sysTmsCarrierRouteRelation.getCode()).append("]操作失败");
            }
        }
        if (StringUtils.isNotBlank(errMsg)) {
            j.setMsg("操作成功，其中" + errMsg.toString());
        }
        return j;
    }

    @ResponseBody
    @RequiresPermissions("sys:common:tms:carrierRouteRelation:sync")
    @RequestMapping(value = "syncSelect", method = RequestMethod.POST)
    public AjaxJson syncSelect(String ids) {
        AjaxJson j = new AjaxJson();
        try {
            String[] idArr = ids.split(",");
            for (String id : idArr) {
                SysTmsCarrierRouteRelation sysTmsCarrierRouteRelation = sysTmsCarrierRouteRelationService.get(id);
                syncPlatformDataToTmsAction.sync(sysTmsCarrierRouteRelation);
                syncPlatformDataToBmsAction.sync(sysTmsCarrierRouteRelation);
            }
        } catch (Exception e) {
            logger.error("同步异常", e);
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    @ResponseBody
    @RequiresPermissions("sys:common:tms:carrierRouteRelation:sync")
    @RequestMapping(value = "syncAll", method = {RequestMethod.GET, RequestMethod.POST})
    public AjaxJson syncAll(SysTmsCarrierRouteRelation entity) {
        AjaxJson j = new AjaxJson();
        try {
            if (StringUtils.isBlank(entity.getDataSet())) {
                entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
            }
            sysTmsCarrierRouteRelationService.findList(entity).forEach(o->{
                syncPlatformDataToTmsAction.sync(o);
                syncPlatformDataToBmsAction.sync(o);
            });
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
    public Map<String, Object> grid(SysTmsCarrierRouteRelationEntity entity, HttpServletRequest request, HttpServletResponse response, Model model) {
        if (StringUtils.isBlank(entity.getDataSet())) {
            entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
        }
        return getBootstrapData(sysTmsCarrierRouteRelationService.findGrid(new Page<SysTmsCarrierRouteRelationEntity>(request, response), entity));
    }

}