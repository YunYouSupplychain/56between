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
import com.yunyou.modules.tms.basic.entity.TmCarrierRouteRelation;
import com.yunyou.modules.tms.basic.entity.extend.TmCarrierRouteRelationEntity;
import com.yunyou.modules.tms.basic.service.TmCarrierRouteRelationService;
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
 * 承运商路由信息Controller
 *
 * @author liujianhua
 * @version 2020-02-20
 */
@Controller
@RequestMapping(value = "${adminPath}/tms/basic/tmCarrierRouteRelation")
public class TmCarrierRouteRelationController extends BaseController {

    @Autowired
    private TmCarrierRouteRelationService tmCarrierRouteRelationService;

    @ModelAttribute
    public TmCarrierRouteRelationEntity get(@RequestParam(required = false) String id) {
        TmCarrierRouteRelationEntity entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = tmCarrierRouteRelationService.getEntity(id);
        }
        if (entity == null) {
            entity = new TmCarrierRouteRelationEntity();
        }
        return entity;
    }

    /**
     * 承运商路由信息列表页面
     */
    @RequiresPermissions("basic:tmCarrierRouteRelation:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/tms/basic/tmCarrierRouteRelationList";
    }

    /**
     * 承运商路由信息列表数据
     */
    @ResponseBody
    @RequiresPermissions("basic:tmCarrierRouteRelation:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(TmCarrierRouteRelation tmCarrierRouteRelation, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<TmCarrierRouteRelationEntity> page = tmCarrierRouteRelationService.findPage(new Page<TmCarrierRouteRelationEntity>(request, response), tmCarrierRouteRelation);
        return getBootstrapData(page);
    }

    /**
     * 查看，增加，编辑承运商路由信息表单页面
     */
    @RequiresPermissions(value = {"basic:tmCarrierRouteRelation:view", "basic:tmCarrierRouteRelation:add", "basic:tmCarrierRouteRelation:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(TmCarrierRouteRelationEntity tmCarrierRouteRelationEntity, Model model) {
        model.addAttribute("tmCarrierRouteRelationEntity", tmCarrierRouteRelationEntity);
        return "modules/tms/basic/tmCarrierRouteRelationForm";
    }

    /**
     * 保存承运商路由信息
     */
    @ResponseBody
    @RequiresPermissions(value = {"basic:tmCarrierRouteRelation:add", "basic:tmCarrierRouteRelation:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(TmCarrierRouteRelation tmCarrierRouteRelation, Model model, RedirectAttributes redirectAttributes) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, tmCarrierRouteRelation)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            tmCarrierRouteRelationService.saveValidator(tmCarrierRouteRelation);
            tmCarrierRouteRelationService.save(tmCarrierRouteRelation);
            j.put("entity", tmCarrierRouteRelation);
        } catch (TmsException e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 删除承运商路由信息
     */
    @ResponseBody
    @RequiresPermissions("basic:tmCarrierRouteRelation:del")
    @RequestMapping(value = "delete")
    public AjaxJson delete(TmCarrierRouteRelation tmCarrierRouteRelation, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        try {
            tmCarrierRouteRelationService.delete(tmCarrierRouteRelation);
        } catch (Exception e) {
            logger.error("删除承运商路由id=[" + tmCarrierRouteRelation.getId() + "]", e);
            j.setSuccess(false);
            j.setMsg("操作失败" + e.getMessage());
        }
        return j;
    }

    /**
     * 批量删除承运商路由信息
     */
    @ResponseBody
    @RequiresPermissions("basic:tmCarrierRouteRelation:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();

        StringBuilder errMsg = new StringBuilder();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            TmCarrierRouteRelation tmCarrierRouteRelation = tmCarrierRouteRelationService.get(id);
            try {
                tmCarrierRouteRelationService.delete(tmCarrierRouteRelation);
            } catch (Exception e) {
                logger.error("删除路由id=[" + id + "]", e);
                errMsg.append("<br>").append("承运商路由[").append(tmCarrierRouteRelation.getCode()).append("]删除失败");
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
    @RequiresPermissions("basic:tmCarrierRouteRelation:export")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public AjaxJson exportFile(TmCarrierRouteRelation tmCarrierRouteRelation, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        try {
            String fileName = "承运商路由信息" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            Page<TmCarrierRouteRelationEntity> page = tmCarrierRouteRelationService.findPage(new Page<>(request, response, -1), tmCarrierRouteRelation);
            new ExportExcel("承运商路由信息", TmCarrierRouteRelation.class).setDataList(page.getList()).write(response, fileName).dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出承运商路由信息记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 导入Excel数据
     */
    @RequiresPermissions("basic:tmCarrierRouteRelation:import")
    @RequestMapping(value = "import", method = RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            int successNum = 0;
            int failureNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<TmCarrierRouteRelation> list = ei.getDataList(TmCarrierRouteRelation.class);
            for (TmCarrierRouteRelation tmCarrierRouteRelation : list) {
                try {
                    tmCarrierRouteRelationService.save(tmCarrierRouteRelation);
                    successNum++;
                } catch (ConstraintViolationException ex) {
                    failureNum++;
                } catch (Exception ex) {
                    failureNum++;
                }
            }
            if (failureNum > 0) {
                failureMsg.insert(0, "，失败 " + failureNum + " 条承运商路由信息记录。");
            }
            addMessage(redirectAttributes, "已成功导入 " + successNum + " 条承运商路由信息记录" + failureMsg);
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入承运商路由信息失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/tms/basic/tmCarrierRouteRelation/?repage";
    }

    /**
     * 下载导入承运商路由信息数据模板
     */
    @RequiresPermissions("basic:tmCarrierRouteRelation:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            String fileName = "承运商路由信息数据导入模板.xlsx";
            List<TmCarrierRouteRelation> list = Lists.newArrayList();
            new ExportExcel("承运商路由信息数据", TmCarrierRouteRelation.class, 1).setDataList(list).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/tms/basic/tmCarrierRouteRelation/?repage";
    }

    /**
     * 启用/停用
     */
    @ResponseBody
    @RequiresPermissions(value = {"basic:tmCarrierRouteRelation:enable", "basic:tmCarrierRouteRelation:unable"}, logical = Logical.OR)
    @RequestMapping(value = "enable")
    public AjaxJson enable(@RequestParam String ids, @RequestParam String flag) {
        AjaxJson j = new AjaxJson();

        StringBuilder errMsg = new StringBuilder();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            TmCarrierRouteRelation tmCarrierRouteRelation = tmCarrierRouteRelationService.get(id);
            try {
                tmCarrierRouteRelation.setDelFlag("0".equals(flag) ? BaseEntity.DEL_FLAG_NORMAL : BaseEntity.DEL_FLAG_DELETE);
                tmCarrierRouteRelation.setUpdateBy(UserUtils.getUser());
                tmCarrierRouteRelation.setUpdateDate(new Date());
                tmCarrierRouteRelationService.save(tmCarrierRouteRelation);
            } catch (GlobalException e) {
                errMsg.append("<br>").append("承运商路由[").append(tmCarrierRouteRelation.getCode()).append("]").append(e.getMessage());
            } catch (Exception e) {
                logger.error("启用/停用路由id=[" + id + "]", e);
                errMsg.append("<br>").append("承运商路由[").append(tmCarrierRouteRelation.getCode()).append("]操作失败");
            }
        }
        if (StringUtils.isNotBlank(errMsg)) {
            j.setMsg("操作成功，其中" + errMsg.toString());
        }
        return j;
    }

    /**
     * 承运商路由信息Grid列表数据
     */
    @ResponseBody
    @RequestMapping(value = "grid")
    public Map<String, Object> grid(TmCarrierRouteRelationEntity entity, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<TmCarrierRouteRelationEntity> page = tmCarrierRouteRelationService.findGrid(new Page<TmCarrierRouteRelationEntity>(request, response), entity);
        return getBootstrapData(page);
    }

}