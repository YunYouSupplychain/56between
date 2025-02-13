package com.yunyou.modules.tms.basic.web;

import com.google.common.collect.Lists;
import com.yunyou.common.exception.GlobalException;
import com.yunyou.common.config.Global;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.time.DateUtils;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.common.utils.excel.ImportExcel;
import com.yunyou.core.persistence.BaseEntity;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.sys.utils.UserUtils;
import com.yunyou.modules.tms.basic.entity.TmDriver;
import com.yunyou.modules.tms.basic.entity.extend.TmDriverEntity;
import com.yunyou.modules.tms.basic.service.TmDriverService;
import com.yunyou.modules.tms.common.TmsException;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 运输人员信息Controller
 *
 * @author liujianhua
 * @version 2020-02-20
 */
@Controller
@RequestMapping(value = "${adminPath}/tms/basic/tmDriver")
public class TmDriverController extends BaseController {

    @Autowired
    private TmDriverService tmDriverService;

    @ModelAttribute
    public TmDriverEntity get(@RequestParam(required = false) String id) {
        TmDriverEntity entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = tmDriverService.getEntity(id);
        }
        if (entity == null) {
            entity = new TmDriverEntity();
        }
        return entity;
    }

    /**
     * 运输人员信息列表页面
     */
    @RequiresPermissions("basic:tmDriver:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/tms/basic/tmDriverList";
    }

    /**
     * 运输人员信息列表数据
     */
    @ResponseBody
    @RequiresPermissions("basic:tmDriver:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(TmDriver tmDriver, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<TmDriverEntity> page = tmDriverService.findPage(new Page<TmDriverEntity>(request, response), tmDriver);
        return getBootstrapData(page);
    }

    /**
     * 查看，增加，编辑运输人员信息表单页面
     */
    @RequiresPermissions(value = {"basic:tmDriver:view", "basic:tmDriver:add", "basic:tmDriver:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(TmDriverEntity tmDriverEntity, Model model) {
        model.addAttribute("tmDriverEntity", tmDriverEntity);
        return "modules/tms/basic/tmDriverForm";
    }

    /**
     * 保存运输人员信息
     */
    @ResponseBody
    @RequiresPermissions(value = {"basic:tmDriver:add", "basic:tmDriver:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(TmDriver tmDriver, Model model, RedirectAttributes redirectAttributes) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, tmDriver)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            tmDriverService.saveValidator(tmDriver);
            tmDriverService.save(tmDriver);
            j.put("entity", tmDriver);
        } catch (TmsException e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 删除运输人员信息
     */
    @ResponseBody
    @RequiresPermissions("basic:tmDriver:del")
    @RequestMapping(value = "delete")
    public AjaxJson delete(TmDriver tmDriver, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();

        try {
            tmDriverService.delete(tmDriver);
        } catch (Exception e) {
            logger.error("删除运输人员id=[" + tmDriver.getId() + "]", e);
            j.setSuccess(false);
            j.setMsg("操作失败" + e.getMessage());
        }
        return j;
    }

    /**
     * 批量删除运输人员信息
     */
    @ResponseBody
    @RequiresPermissions("basic:tmDriver:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();

        StringBuilder errMsg = new StringBuilder();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            TmDriver tmDriver = tmDriverService.get(id);
            try {
                tmDriverService.delete(tmDriver);
            } catch (Exception e) {
                logger.error("删除运输人员id=[" + id + "]", e);
                errMsg.append("<br>").append("运输人员[").append(tmDriver.getCode()).append("]删除失败");
            }
        }
        if (StringUtils.isNotBlank(errMsg)) {
            j.setMsg("操作成功，其中" + errMsg.toString());
        }
        return j;
    }

    /**
     * 导出excel文件
     */
    @ResponseBody
    @RequiresPermissions("basic:tmDriver:export")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public AjaxJson exportFile(TmDriver tmDriver, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        try {
            String fileName = "运输人员信息" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            Page<TmDriverEntity> page = tmDriverService.findPage(new Page<TmDriverEntity>(request, response, -1), tmDriver);
            new ExportExcel("运输人员信息", TmDriver.class).setDataList(page.getList()).write(response, fileName).dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出运输人员信息记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 导入Excel数据
     */
    @RequiresPermissions("basic:tmDriver:import")
    @RequestMapping(value = "import", method = RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            int successNum = 0;
            int failureNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<TmDriver> list = ei.getDataList(TmDriver.class);
            for (TmDriver tmDriver : list) {
                try {
                    tmDriverService.save(tmDriver);
                    successNum++;
                } catch (ConstraintViolationException ex) {
                    failureNum++;
                } catch (Exception ex) {
                    failureNum++;
                }
            }
            if (failureNum > 0) {
                failureMsg.insert(0, "，失败 " + failureNum + " 条运输人员信息记录。");
            }
            addMessage(redirectAttributes, "已成功导入 " + successNum + " 条运输人员信息记录" + failureMsg);
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入运输人员信息失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/tms/basic/tmDriver/?repage";
    }

    /**
     * 下载导入运输人员信息数据模板
     */
    @RequiresPermissions("basic:tmDriver:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            String fileName = "运输人员信息数据导入模板.xlsx";
            List<TmDriver> list = Lists.newArrayList();
            new ExportExcel("运输人员信息数据", TmDriver.class, 1).setDataList(list).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/tms/basic/tmDriver/?repage";
    }

    /**
     * 启用/停用
     */
    @ResponseBody
    @RequiresPermissions(value = {"basic:tmDriver:enable", "basic:tmDriver:unable"}, logical = Logical.OR)
    @RequestMapping(value = "enable")
    public AjaxJson enable(@RequestParam String ids, @RequestParam String flag) {
        AjaxJson j = new AjaxJson();

        StringBuilder errMsg = new StringBuilder();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            TmDriver tmDriver = tmDriverService.get(id);
            try {
                tmDriver.setDelFlag("0".equals(flag) ? BaseEntity.DEL_FLAG_NORMAL : BaseEntity.DEL_FLAG_DELETE);
                tmDriver.setUpdateBy(UserUtils.getUser());
                tmDriver.setUpdateDate(new Date());
                tmDriverService.save(tmDriver);
            } catch (GlobalException e) {
                errMsg.append("<br>").append("运输人员[").append(tmDriver.getCode()).append("]").append(e.getMessage());
            } catch (Exception e) {
                logger.error("启用/停用运输人员id=[" + id + "]", e);
                errMsg.append("<br>").append("运输人员[").append(tmDriver.getCode()).append("]操作失败");
            }
        }
        if (StringUtils.isNotBlank(errMsg)) {
            j.setMsg("操作成功，其中" + errMsg.toString());
        }
        return j;
    }

    /**
     * 运输人员信息Grid列表数据
     */
    @ResponseBody
    @RequestMapping(value = "grid")
    public Map<String, Object> grid(TmDriverEntity entity, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<TmDriverEntity> page = tmDriverService.findGrid(new Page<TmDriverEntity>(request, response), entity);
        return getBootstrapData(page);
    }

}