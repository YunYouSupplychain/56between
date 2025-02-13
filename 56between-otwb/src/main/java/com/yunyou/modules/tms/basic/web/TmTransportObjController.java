package com.yunyou.modules.tms.basic.web;

import com.google.common.collect.Lists;
import com.yunyou.common.exception.GlobalException;
import com.yunyou.common.config.Global;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.common.utils.excel.ImportExcel;
import com.yunyou.core.persistence.BaseEntity;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.sys.utils.UserUtils;
import com.yunyou.modules.tms.basic.entity.TmTransportObj;
import com.yunyou.modules.tms.basic.entity.excel.TmTransportObjExport;
import com.yunyou.modules.tms.basic.entity.excel.TmTransportObjImport;
import com.yunyou.modules.tms.basic.entity.extend.TmTransportObjEntity;
import com.yunyou.modules.tms.basic.service.TmTransportObjService;
import com.yunyou.modules.tms.common.TmsException;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 业务对象信息Controller
 *
 * @author liujianhua
 * @version 2020-02-20
 */
@Controller
@RequestMapping(value = "${adminPath}/tms/basic/tmTransportObj")
public class TmTransportObjController extends BaseController {
    @Autowired
    private TmTransportObjService tmTransportObjService;

    @ModelAttribute
    public TmTransportObjEntity get(@RequestParam(required = false) String id) {
        TmTransportObjEntity entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = tmTransportObjService.getEntity(id);
        }
        if (entity == null) {
            entity = new TmTransportObjEntity();
        }
        return entity;
    }

    /**
     * 业务对象信息列表页面
     */
    @RequiresPermissions("basic:tmTransportObj:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/tms/basic/tmTransportObjList";
    }

    /**
     * 业务对象信息列表数据
     */
    @ResponseBody
    @RequiresPermissions("basic:tmTransportObj:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(TmTransportObj tmTransportObj, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(tmTransportObjService.findPage(new Page<TmTransportObjEntity>(request, response), tmTransportObj));
    }

    /**
     * 查看，增加，编辑业务对象信息表单页面
     */
    @RequiresPermissions(value = {"basic:tmTransportObj:view", "basic:tmTransportObj:add", "basic:tmTransportObj:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(TmTransportObjEntity tmTransportObjEntity, Model model) {
        model.addAttribute("tmTransportObjEntity", tmTransportObjEntity);
        return "modules/tms/basic/tmTransportObjForm";
    }

    /**
     * 保存业务对象信息
     */
    @ResponseBody
    @RequiresPermissions(value = {"basic:tmTransportObj:add", "basic:tmTransportObj:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(TmTransportObj tmTransportObj, Model model) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, tmTransportObj)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            tmTransportObjService.saveValidator(tmTransportObj);
            tmTransportObjService.save(tmTransportObj);
            j.put("entity", tmTransportObj);
        } catch (TmsException e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 删除业务对象信息
     */
    @ResponseBody
    @RequiresPermissions("basic:tmTransportObj:del")
    @RequestMapping(value = "delete")
    public AjaxJson delete(TmTransportObj tmTransportObj) {
        AjaxJson j = new AjaxJson();
        try {
            tmTransportObjService.delete(tmTransportObj);
        } catch (Exception e) {
            logger.error("删除业务对象id=[" + tmTransportObj.getId() + "]", e);
            j.setSuccess(false);
            j.setMsg("操作失败" + e.getMessage());
        }
        return j;
    }

    /**
     * 批量删除业务对象信息
     */
    @ResponseBody
    @RequiresPermissions("basic:tmTransportObj:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();

        StringBuilder errMsg = new StringBuilder();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            TmTransportObj tmTransportObj = tmTransportObjService.get(id);
            try {
                tmTransportObjService.delete(tmTransportObj);
            } catch (Exception e) {
                logger.error("删除业务对象id=[" + id + "]", e);
                errMsg.append("<br>").append("业务对象[").append(tmTransportObj.getTransportObjCode()).append("]删除失败");
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
    @RequiresPermissions("basic:tmTransportObj:export")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public AjaxJson exportFile(TmTransportObjEntity qEntity, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            List<TmTransportObjExport> exportList = Lists.newArrayList();
            Page<TmTransportObjEntity> page = tmTransportObjService.findPage(new Page<TmTransportObjEntity>(request, response, -1), qEntity);
            List<TmTransportObjEntity> list = page.getList();
            for (TmTransportObjEntity o : list) {
                TmTransportObjExport export = new TmTransportObjExport();
                BeanUtils.copyProperties(o, export);
                exportList.add(export);
            }
            new ExportExcel("业务对象信息", TmTransportObjExport.class).setDataList(exportList).write(response, "业务对象信息.xlsx").dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出业务对象信息记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 导入Excel数据
     */
    @RequiresPermissions("basic:tmTransportObj:import")
    @RequestMapping(value = "import", method = RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
        int successNum = 0;
        int failureNum = 0;
        ImportExcel ei;
        List<TmTransportObjImport> list;
        StringBuilder failureMsg = new StringBuilder();
        try {
            ei = new ImportExcel(file, 1, 0);
            list = ei.getDataList(TmTransportObjImport.class);
        } catch (Exception e) {
            addMessage(redirectAttributes, "已成功导入 " + successNum + " 条业务对象信息记录，数据解析失败");
            return "redirect:" + Global.getAdminPath() + "/tms/basic/tmTransportObj/?repage";
        }
        for (TmTransportObjImport objImport : list) {
            try {
                tmTransportObjService.importFile(objImport);
                successNum++;
            } catch (TmsException ex) {
                failureNum++;
                failureMsg.append(ex.getMessage()).append("<br>");
            }
        }
        if (failureNum > 0) {
            failureMsg.insert(0, "，失败 " + failureNum + " 条业务对象信息记录。").append("<br>");
        }
        addMessage(redirectAttributes, "已成功导入 " + successNum + " 条业务对象信息记录" + failureMsg);
        return "redirect:" + Global.getAdminPath() + "/tms/basic/tmTransportObj/?repage";
    }

    /**
     * 下载导入业务对象信息数据模板
     */
    @RequiresPermissions("basic:tmTransportObj:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            new ExportExcel(null, TmTransportObjImport.class, 1).setDataList(Lists.newArrayList()).write(response, "业务对象信息导入模板.xlsx").dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/tms/basic/tmTransportObj/?repage";
    }

    /**
     * 启用/停用
     */
    @ResponseBody
    @RequiresPermissions(value = {"basic:tmTransportObj:enable", "basic:tmTransportObj:unable"}, logical = Logical.OR)
    @RequestMapping(value = "enable")
    public AjaxJson enable(@RequestParam String ids, @RequestParam String flag) {
        AjaxJson j = new AjaxJson();

        StringBuilder errMsg = new StringBuilder();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            TmTransportObj tmTransportObj = tmTransportObjService.get(id);
            try {
                tmTransportObj.setDelFlag("0".equals(flag) ? BaseEntity.DEL_FLAG_NORMAL : BaseEntity.DEL_FLAG_DELETE);
                tmTransportObj.setUpdateBy(UserUtils.getUser());
                tmTransportObj.setUpdateDate(new Date());
                tmTransportObjService.save(tmTransportObj);
            } catch (GlobalException e) {
                errMsg.append("<br>").append("业务对象[").append(tmTransportObj.getTransportObjCode()).append("]").append(e.getMessage());
            } catch (Exception e) {
                logger.error("启用/停用业务对象品id=[" + id + "]", e);
                errMsg.append("<br>").append("业务对象[").append(tmTransportObj.getTransportObjCode()).append("]操作失败");
            }
        }
        if (StringUtils.isNotBlank(errMsg)) {
            j.setMsg("操作成功，其中" + errMsg.toString());
        }
        return j;
    }

    /**
     * 业务对象信息Grid列表数据
     */
    @ResponseBody
    @RequestMapping(value = "grid")
    public Map<String, Object> grid(TmTransportObjEntity tmTransportObjEntity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(tmTransportObjService.findGrid(new Page<TmTransportObjEntity>(request, response), tmTransportObjEntity));
    }

    /**
     * 业务对象信息Grid列表数据
     */
    @ResponseBody
    @RequestMapping(value = "settleGrid")
    public Map<String, Object> settleGrid(TmTransportObjEntity tmTransportObjEntity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(tmTransportObjService.findSettleGrid(new Page<TmTransportObjEntity>(request, response), tmTransportObjEntity));
    }

    /**
     * 配送对象信息Grid列表数据
     */
    @ResponseBody
    @RequestMapping(value = "outletGrid")
    public Map<String, Object> outletGrid(TmTransportObjEntity tmTransportObjEntity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(tmTransportObjService.findOutletGrid(new Page<TmTransportObjEntity>(request, response), tmTransportObjEntity));
    }

}