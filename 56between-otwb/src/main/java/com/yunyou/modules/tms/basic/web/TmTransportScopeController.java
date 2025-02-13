package com.yunyou.modules.tms.basic.web;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yunyou.common.config.Global;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.time.DateUtils;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.common.utils.excel.ImportExcel;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.sys.entity.Area;
import com.yunyou.modules.sys.service.AreaService;
import com.yunyou.modules.tms.basic.entity.TmTransportScope;
import com.yunyou.modules.tms.basic.entity.extend.TmTransportScopeEntity;
import com.yunyou.modules.tms.basic.service.TmTransportScopeService;
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
import java.util.List;
import java.util.Map;

/**
 * 业务服务范围Controller
 *
 * @author liujianhua
 * @version 2020-02-20
 */
@Controller
@RequestMapping(value = "${adminPath}/tms/basic/tmTransportScope")
public class TmTransportScopeController extends BaseController {

    @Autowired
    private TmTransportScopeService tmTransportScopeService;
    @Autowired
    private AreaService areaService;

    @ModelAttribute
    public TmTransportScope get(@RequestParam(required = false) String id) {
        TmTransportScope entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = tmTransportScopeService.get(id);
        }
        if (entity == null) {
            entity = new TmTransportScope();
        }
        return entity;
    }

    /**
     * 业务服务范围列表页面
     */
    @RequiresPermissions("basic:tmTransportScope:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/tms/basic/tmTransportScopeList";
    }

    /**
     * 业务服务范围列表数据
     */
    @ResponseBody
    @RequiresPermissions("basic:tmTransportScope:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(TmTransportScope tmTransportScope, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<TmTransportScope> page = tmTransportScopeService.findPage(new Page<>(request, response), tmTransportScope);
        return getBootstrapData(page);
    }

    /**
     * 查看，增加，编辑业务服务范围表单页面
     */
    @RequiresPermissions(value = {"basic:tmTransportScope:view", "basic:tmTransportScope:add", "basic:tmTransportScope:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(TmTransportScope tmTransportScope, Model model) {
        model.addAttribute("tmTransportScope", tmTransportScope);
        return "modules/tms/basic/tmTransportScopeForm";
    }

    /**
     * 保存业务服务范围
     */
    @ResponseBody
    @RequiresPermissions(value = {"basic:tmTransportScope:add", "basic:tmTransportScope:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(TmTransportScope tmTransportScope, Model model, RedirectAttributes redirectAttributes) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, tmTransportScope)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            tmTransportScopeService.saveValidator(tmTransportScope);
            tmTransportScopeService.save(tmTransportScope);
            j.put("entity", tmTransportScope);
        } catch (TmsException e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 删除业务服务范围
     */
    @ResponseBody
    @RequiresPermissions("basic:tmTransportScope:del")
    @RequestMapping(value = "delete")
    public AjaxJson delete(TmTransportScope tmTransportScope, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        try {
            tmTransportScopeService.delete(tmTransportScope);
        } catch (Exception e) {
            logger.error("删除业务服务范围id=[" + tmTransportScope.getId() + "]", e);
            j.setSuccess(false);
            j.setMsg("操作失败" + e.getMessage());
        }
        return j;
    }

    /**
     * 批量删除业务服务范围
     */
    @ResponseBody
    @RequiresPermissions("basic:tmTransportScope:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();

        StringBuilder errMsg = new StringBuilder();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            TmTransportScope tmTransportScope = tmTransportScopeService.get(id);
            try {
                tmTransportScopeService.delete(tmTransportScope);
            } catch (Exception e) {
                logger.error("删除业务服务范围id=[" + id + "]", e);
                errMsg.append("<br>").append("业务服务范围[").append(tmTransportScope.getCode()).append("]删除失败");
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
    @RequiresPermissions("basic:tmTransportScope:export")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public AjaxJson exportFile(TmTransportScope tmTransportScope, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        try {
            String fileName = "业务服务范围" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            Page<TmTransportScope> page = tmTransportScopeService.findPage(new Page<>(request, response, -1), tmTransportScope);
            new ExportExcel("业务服务范围", TmTransportScope.class).setDataList(page.getList()).write(response, fileName).dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出业务服务范围记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 导入Excel数据
     */
    @RequiresPermissions("basic:tmTransportScope:import")
    @RequestMapping(value = "import", method = RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            int successNum = 0;
            int failureNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<TmTransportScope> list = ei.getDataList(TmTransportScope.class);
            for (TmTransportScope tmTransportScope : list) {
                try {
                    tmTransportScopeService.save(tmTransportScope);
                    successNum++;
                } catch (ConstraintViolationException ex) {
                    failureNum++;
                } catch (Exception ex) {
                    failureNum++;
                }
            }
            if (failureNum > 0) {
                failureMsg.insert(0, "，失败 " + failureNum + " 条业务服务范围记录。");
            }
            addMessage(redirectAttributes, "已成功导入 " + successNum + " 条业务服务范围记录" + failureMsg);
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入业务服务范围失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/tms/basic/tmTransportScope/?repage";
    }

    /**
     * 下载导入业务服务范围数据模板
     */
    @RequiresPermissions("basic:tmTransportScope:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            String fileName = "业务服务范围数据导入模板.xlsx";
            List<TmTransportScope> list = Lists.newArrayList();
            new ExportExcel("业务服务范围数据", TmTransportScope.class, 1).setDataList(list).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/tms/basic/tmTransportScope/?repage";
    }

    /**
     * 业务服务范围Grid列表数据
     */
    @ResponseBody
    @RequestMapping(value = "grid")
    public Map<String, Object> grid(TmTransportScopeEntity entity, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<TmTransportScopeEntity> page = tmTransportScopeService.findGrid(new Page<TmTransportScopeEntity>(request, response), entity);
        return getBootstrapData(page);
    }

    @RequestMapping(value = "area")
    public String area(String id, Model model) {
        model.addAttribute("tmTransportScopeEntity", tmTransportScopeService.getEntity(id));
        return "modules/tms/basic/tmTransportScopeArea";
    }

    @ResponseBody
    @RequestMapping(value = "saveArea")
    public AjaxJson saveArea(TmTransportScopeEntity entity) {
        AjaxJson j = new AjaxJson();
        try {
            tmTransportScopeService.saveArea(entity);
        } catch (TmsException e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    @ResponseBody
    @RequestMapping(value = "treeData")
    public List<Map<String, Object>> treeData(@RequestParam String headId, @RequestParam(required = false) String extId, HttpServletResponse response) {
        TmTransportScopeEntity entity = tmTransportScopeService.getEntity(headId);
        String areaIds = entity != null ? "," + entity.getAreaIds() + "," : "";
        List<Map<String, Object>> mapList = Lists.newArrayList();
        List<Area> list = areaService.findCountToArea();
        for (int i = 0; i < list.size(); i++) {
            Area e = list.get(i);
            if (StringUtils.isBlank(extId) || (!e.getId().equals(extId) && !e.getParentIds().contains("," + extId + ","))) {
                Map<String, Object> map = Maps.newHashMap();
                map.put("id", e.getId());
                if (Global.TREE_ROOT_NODE.equals(e.getParentId())) {
                    map.put("parent", "#");
                    Map<String, Object> state = Maps.newHashMap();
                    state.put("opened", true);
                    map.put("state", state);
                } else {
                    if (i == 0) {
                        map.put("parent", "#");
                    } else {
                        map.put("parent", e.getParentId());
                    }
                }
                if (areaIds.contains("," + e.getId() + ",")) {
                    Map<String, Object> state = Maps.newHashMap();
                    state.put("selected", true);
                    map.put("state", state);
                }
                // 自定义属性
                Map<String, Object> attr = Maps.newHashMap();
                attr.put("code", e.getCode());
                map.put("a_attr", attr);

                map.put("name", e.getName());
                map.put("text", e.getName());
                mapList.add(map);
            }
        }
        return mapList;
    }

}