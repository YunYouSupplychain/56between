package com.yunyou.modules.sys.common.web;

import com.yunyou.common.exception.GlobalException;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.interfaces.interactive.action.SyncPlatformDataToWmsAction;
import com.yunyou.modules.sys.common.entity.SysWmsCarrierTypeRelation;
import com.yunyou.modules.sys.common.entity.extend.SysWmsCarrierTypeRelationEntity;
import com.yunyou.modules.sys.common.service.SysWmsCarrierTypeRelationService;
import com.yunyou.modules.sys.utils.SysDataSetUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 承运商类型关系Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/common/wms/carrierTypeRelation")
public class SysWmsCarrierTypeRelationController extends BaseController {
    @Autowired
    private SysWmsCarrierTypeRelationService sysWmsCarrierTypeRelationService;
    @Autowired
    private SyncPlatformDataToWmsAction syncPlatformDataToWmsAction;

    @ModelAttribute
    public SysWmsCarrierTypeRelationEntity get(@RequestParam(required = false) String id) {
        SysWmsCarrierTypeRelationEntity entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = sysWmsCarrierTypeRelationService.getEntity(id);
        }
        if (entity == null) {
            entity = new SysWmsCarrierTypeRelationEntity();
        }
        return entity;
    }

    /**
     * 列表页面
     */
    @RequiresPermissions("sys:common:wms:carrierTypeRelation:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/sys/common/sysWmsCarrierTypeRelationList";
    }

    /**
     * 列表数据
     */
    @ResponseBody
    @RequiresPermissions("sys:common:wms:carrierTypeRelation:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(SysWmsCarrierTypeRelationEntity entity, HttpServletRequest request, HttpServletResponse response, Model model) {
        if (StringUtils.isBlank(entity.getDataSet())) {
            entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
        }
        return getBootstrapData(sysWmsCarrierTypeRelationService.findPage(new Page<SysWmsCarrierTypeRelationEntity>(request, response), entity));
    }

    /**
     * 查看，增加，编辑表单页面
     */
    @RequiresPermissions(value = {"sys:common:wms:carrierTypeRelation:view", "sys:common:wms:carrierTypeRelation:add", "sys:common:wms:carrierTypeRelation:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(SysWmsCarrierTypeRelationEntity sysWmsCarrierTypeRelationEntity, Model model) {
        model.addAttribute("sysWmsCarrierTypeRelationEntity", sysWmsCarrierTypeRelationEntity);
        return "modules/sys/common/sysWmsCarrierTypeRelationForm";
    }

    /**
     * 保存
     */
    @ResponseBody
    @RequiresPermissions(value = {"sys:common:wms:carrierTypeRelation:add", "sys:common:wms:carrierTypeRelation:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(SysWmsCarrierTypeRelation entity, Model model) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, entity)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            sysWmsCarrierTypeRelationService.save(entity);
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
    @RequiresPermissions("sys:common:wms:carrierTypeRelation:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            sysWmsCarrierTypeRelationService.delete(sysWmsCarrierTypeRelationService.get(id));
        }
        j.setMsg("删除成功");
        return j;
    }

    @ResponseBody
    @RequiresPermissions("sys:common:wms:carrierTypeRelation:sync")
    @RequestMapping(value = "syncSelect", method = RequestMethod.POST)
    public AjaxJson syncSelect(String ids) {
        AjaxJson j = new AjaxJson();
        try {
            String[] idArr = ids.split(",");
            for (String id : idArr) {
                syncPlatformDataToWmsAction.sync(sysWmsCarrierTypeRelationService.get(id));
            }
        } catch (Exception e) {
            logger.error("同步异常", e);
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    @ResponseBody
    @RequiresPermissions("sys:common:wms:carrierTypeRelation:sync")
    @RequestMapping(value = "syncAll", method = {RequestMethod.GET, RequestMethod.POST})
    public AjaxJson syncAll(SysWmsCarrierTypeRelation entity) {
        AjaxJson j = new AjaxJson();
        try {
            if (StringUtils.isBlank(entity.getDataSet())) {
                entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
            }
            sysWmsCarrierTypeRelationService.findList(entity).forEach(syncPlatformDataToWmsAction::sync);
        } catch (Exception e) {
            logger.error("同步异常", e);
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

}