package com.yunyou.modules.sys.common.web;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.config.Global;
import com.yunyou.common.exception.GlobalException;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.common.utils.excel.ImportExcel;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.interfaces.interactive.action.SyncPlatformDataToWmsAction;
import com.yunyou.modules.sys.common.entity.SysWmsLoc;
import com.yunyou.modules.sys.common.entity.extend.SysWmsLocEntity;
import com.yunyou.modules.sys.common.service.SysWmsLocService;
import com.yunyou.modules.sys.utils.SysDataSetUtils;
import com.google.common.collect.Lists;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 库位Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/common/wms/loc")
public class SysWmsLocController extends BaseController {
    @Autowired
    private SysWmsLocService sysWmsLocService;
    @Autowired
    private SyncPlatformDataToWmsAction syncPlatformDataToWmsAction;

    @ModelAttribute
    public SysWmsLoc get(@RequestParam(required = false) String id) {
        SysWmsLoc entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = sysWmsLocService.get(id);
        }
        if (entity == null) {
            entity = new SysWmsLoc();
        }
        return entity;
    }

    /**
     * 列表页面
     */
    @RequiresPermissions("sys:common:wms:loc:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/sys/common/sysWmsLocList";
    }

    /**
     * 列表数据
     */
    @ResponseBody
    @RequiresPermissions("sys:common:wms:loc:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(SysWmsLoc entity, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(entity.getDataSet())) {
            entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
        }
        return getBootstrapData(sysWmsLocService.findPage(new Page<>(request, response), entity));
    }

    /**
     * 查看，增加，编辑表单页面
     */
    @RequiresPermissions(value = {"sys:common:wms:loc:view", "sys:common:wms:loc:add", "sys:common:wms:loc:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(SysWmsLoc entity, Model model) {
        model.addAttribute("entity", entity);
        return "modules/sys/common/sysWmsLocForm";
    }

    /**
     * 保存
     */
    @ResponseBody
    @RequiresPermissions(value = {"sys:common:wms:loc:add", "sys:common:wms:loc:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(SysWmsLoc entity, Model model) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, entity)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            sysWmsLocService.save(entity);
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
     * 删除
     */
    @ResponseBody
    @RequiresPermissions("sys:common:wms:loc:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            sysWmsLocService.delete(sysWmsLocService.get(id));
        }
        return j;
    }

    /**
     * 查看，增加，编辑库位表单页面
     */
    @RequestMapping(value = "createLocForm")
    public String createLocForm(SysWmsLocEntity entity, Model model) {
        model.addAttribute("sysWmsLocEntity", entity);
        return "modules/sys/common/sysWmsLocCreateForm";
    }

    /**
     * 生成库位
     */
    @RequiresPermissions("basicdata:banQinCdWhLoc:createLoc")
    @ResponseBody
    @RequestMapping(value = "generateLoc")
    public AjaxJson generateLoc(@RequestBody SysWmsLocEntity entity) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = sysWmsLocService.locGeneration(entity.getLocCode(), entity.getLocList(), entity.getSumLength(), entity.getDataSet());
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
            j.put("list", msg.getData());
        } catch (GlobalException e) {
            if (logger.isInfoEnabled()) {
                logger.info(null, e);
            }
            j.setSuccess(false);
            j.setMsg(e.getMessage());
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
     * 保存生成的库位信息
     */
    @ResponseBody
    @RequestMapping(value = "confirm")
    public AjaxJson confirm(@RequestBody SysWmsLocEntity entity) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = sysWmsLocService.confirm(entity.getLocList(), entity.getDataSet());
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (GlobalException e) {
            if (logger.isInfoEnabled()) {
                logger.info(null, e);
            }
            j.setSuccess(false);
            j.setMsg(e.getMessage());
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
    @RequiresPermissions("sys:common:wms:loc:sync")
    @RequestMapping(value = "syncSelect", method = RequestMethod.POST)
    public AjaxJson syncSelect(String ids) {
        AjaxJson j = new AjaxJson();
        try {
            String[] idArr = ids.split(",");
            for (String id : idArr) {
                syncPlatformDataToWmsAction.sync(sysWmsLocService.get(id));
            }
        } catch (Exception e) {
            logger.error("同步异常", e);
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    @ResponseBody
    @RequiresPermissions("sys:common:wms:loc:sync")
    @RequestMapping(value = "syncAll", method = {RequestMethod.GET, RequestMethod.POST})
    public AjaxJson syncAll(SysWmsLoc entity) {
        AjaxJson j = new AjaxJson();
        try {
            if (StringUtils.isBlank(entity.getDataSet())) {
                entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
            }
            sysWmsLocService.findList(entity).forEach(syncPlatformDataToWmsAction::sync);
        } catch (Exception e) {
            logger.error("同步异常", e);
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 弹出框数据
     */
    @ResponseBody
    @RequestMapping(value = "grid")
    public Map<String, Object> grid(SysWmsLoc entity, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(entity.getDataSet())) {
            entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
        }
        return getBootstrapData(sysWmsLocService.findGrid(new Page<>(request, response), entity));
    }

    /**
     * 导出excel文件
     */
    @ResponseBody
    @RequiresPermissions("sys:common:wms:loc:export")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public AjaxJson exportFile(@RequestBody SysWmsLoc entity, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            if (StringUtils.isBlank(entity.getDataSet())) {
                entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
            }
            List<SysWmsLoc> list = sysWmsLocService.findPage(new Page<>(request, response, -1), entity).getList();
            new ExportExcel(null, SysWmsLoc.class).setDataList(list).write(response, "库位.xlsx").dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 导入Excel数据
     */
    @RequiresPermissions("sys:common:wms:loc:import")
    @RequestMapping(value = "import", method = RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            sysWmsLocService.importFile(new ImportExcel(file, 1, 0).getDataList(SysWmsLoc.class), SysDataSetUtils.getUserDataSet().getCode());
            addMessage(redirectAttributes, "导入成功");
        } catch (Exception e) {
            logger.error("导入失败", e);
            addMessage(redirectAttributes, "导入失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/sys/common/wms/loc/?repage";
    }

    /**
     * 下载导入数据模板
     */
    @RequiresPermissions("sys:common:wms:loc:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            new ExportExcel(null, SysWmsLoc.class, 2).setDataList(Lists.newArrayList()).write(response, "库位模板.xlsx").dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/sys/common/wms/loc/?repage";
    }
}