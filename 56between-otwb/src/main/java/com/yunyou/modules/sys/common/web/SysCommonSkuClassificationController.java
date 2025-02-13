package com.yunyou.modules.sys.common.web;

import com.yunyou.common.exception.GlobalException;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.interfaces.interactive.action.SyncPlatformDataCommonAction;
import com.yunyou.modules.sys.common.entity.SysCommonSkuClassification;
import com.yunyou.modules.sys.common.entity.extend.SysCommonSkuClassificationEntity;
import com.yunyou.modules.sys.common.service.SysCommonSkuClassificationService;
import com.yunyou.modules.sys.utils.SysDataSetUtils;
import com.google.common.collect.Lists;
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
import java.util.List;
import java.util.Map;

/**
 * 商品分类Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/common/skuClassification")
public class SysCommonSkuClassificationController extends BaseController {
    @Autowired
    private SysCommonSkuClassificationService sysCommonSkuClassificationService;
    @Autowired
    private SyncPlatformDataCommonAction syncPlatformDataCommonAction;

    /**
     * 列表页面
     */
    @RequiresPermissions("sys:common:skuClassification:list")
    @RequestMapping(value = {"list", ""})
    public String list(Model model) {
        model.addAttribute("sysCommonSkuClassification", new SysCommonSkuClassificationEntity());
        return "modules/sys/common/sysCommonSkuClassificationList";
    }

    /**
     * 列表数据
     */
    @ResponseBody
    @RequiresPermissions("sys:common:skuClassification:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(SysCommonSkuClassificationEntity entity, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(entity.getDataSet())) {
            entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
        }
        return getBootstrapData(sysCommonSkuClassificationService.findPage(new Page<SysCommonSkuClassificationEntity>(request, response), entity));
    }

    /**
     * 查看，增加，编辑表单页面
     */
    @RequiresPermissions(value = {"sys:common:skuClassification:view", "sys:common:skuClassification:add", "sys:common:skuClassification:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(SysCommonSkuClassificationEntity entity, Model model) {
        if (StringUtils.isNotBlank(entity.getId())) {
            entity = sysCommonSkuClassificationService.getEntity(entity.getId());
        }
        model.addAttribute("sysCommonSkuClassification", entity);
        return "modules/sys/common/sysCommonSkuClassificationForm";
    }

    /**
     * 保存
     */
    @ResponseBody
    @RequiresPermissions(value = {"sys:common:skuClassification:add", "sys:common:skuClassification:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(@Valid SysCommonSkuClassification sysCommonSkuClassification, Model model) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, sysCommonSkuClassification)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            sysCommonSkuClassificationService.save(sysCommonSkuClassification);
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
            logger.error(null, e);
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 批量删除
     */
    @ResponseBody
    @RequiresPermissions("sys:common:skuClassification:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            sysCommonSkuClassificationService.delete(new SysCommonSkuClassification(id));
        }
        j.setMsg("删除成功");
        return j;
    }

    @ResponseBody
    @RequiresPermissions("sys:common:skuClassification:sync")
    @RequestMapping(value = "syncSelect", method = RequestMethod.POST)
    public AjaxJson syncSelect(String ids) {
        AjaxJson j = new AjaxJson();
        try {
            List<SysCommonSkuClassification> list = Lists.newArrayList();
            String[] idArr = ids.split(",");
            for (String id : idArr) {
                list.add(sysCommonSkuClassificationService.get(id));
            }
            syncPlatformDataCommonAction.syncSkuClassification(list);
        } catch (Exception e) {
            logger.error("同步异常", e);
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    @ResponseBody
    @RequiresPermissions("sys:common:skuClassification:sync")
    @RequestMapping(value = "syncAll", method = {RequestMethod.GET, RequestMethod.POST})
    public AjaxJson syncAll(SysCommonSkuClassificationEntity entity) {
        AjaxJson j = new AjaxJson();
        try {
            syncPlatformDataCommonAction.syncSkuClassification(sysCommonSkuClassificationService.findSync(entity));
        } catch (Exception e) {
            logger.error("同步异常", e);
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    @ResponseBody
    @RequestMapping(value = "grid")
    public Map<String, Object> grid(SysCommonSkuClassification entity, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(entity.getDataSet())) {
            entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
        }
        return getBootstrapData(sysCommonSkuClassificationService.findGrid(new Page<>(request, response), entity));
    }
}
