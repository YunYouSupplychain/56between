package com.yunyou.modules.oms.basic.web;

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
import com.yunyou.modules.oms.basic.entity.OmBusinessServiceScope;
import com.yunyou.modules.oms.basic.entity.extend.OmBusinessServiceScopeEntity;
import com.yunyou.modules.oms.basic.service.OmBusinessServiceScopeService;
import com.yunyou.modules.oms.common.OmsException;
import com.yunyou.modules.sys.entity.Area;
import com.yunyou.modules.sys.service.AreaService;
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
 * 业务服务范围Controller
 *
 * @author Jianhua Liu
 * @version 2019-10-23
 */
@Controller
@RequestMapping(value = "${adminPath}/oms/basic/omBusinessServiceScope")
public class OmBusinessServiceScopeController extends BaseController {

    @Autowired
    private OmBusinessServiceScopeService omBusinessServiceScopeService;
    @Autowired
    private AreaService areaService;

    @ModelAttribute
    public OmBusinessServiceScopeEntity get(@RequestParam(required = false) String id) {
        OmBusinessServiceScopeEntity entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = omBusinessServiceScopeService.getEntity(id);
        }
        if (entity == null) {
            entity = new OmBusinessServiceScopeEntity();
        }
        return entity;
    }

    /**
     * 业务服务范围列表页面
     */
    @RequiresPermissions("basic:omBusinessServiceScope:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/oms/basic/omBusinessServiceScopeList";
    }

    /**
     * 业务服务范围列表数据
     */
    @ResponseBody
    @RequiresPermissions("basic:omBusinessServiceScope:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(OmBusinessServiceScopeEntity entity, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<OmBusinessServiceScopeEntity> page = omBusinessServiceScopeService.findPage(new Page<OmBusinessServiceScopeEntity>(request, response), entity);
        return getBootstrapData(page);
    }

    /**
     * 查看，增加，编辑业务服务范围表单页面
     */
    @RequiresPermissions(value = {"basic:omBusinessServiceScope:view", "basic:omBusinessServiceScope:add", "basic:omBusinessServiceScope:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(OmBusinessServiceScopeEntity entity, Model model) {
        model.addAttribute("omBusinessServiceScopeEntity", entity);
        return "modules/oms/basic/omBusinessServiceScopeForm";
    }

    /**
     * 保存业务服务范围
     */
    @ResponseBody
    @RequiresPermissions(value = {"basic:omBusinessServiceScope:add", "basic:omBusinessServiceScope:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(OmBusinessServiceScopeEntity entity, Model model, RedirectAttributes redirectAttributes) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, entity)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            omBusinessServiceScopeService.saveEntity(entity);
            j.setSuccess(true);
            j.setMsg("保存业务服务范围成功");
        } catch (DuplicateKeyException e) {
            j.setSuccess(false);
            j.setMsg("编码[" + entity.getGroupCode() + "]已存在");
        } catch (Exception e) {
            logger.error("【业务服务范围】", e);
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 删除业务服务范围
     */
    @ResponseBody
    @RequiresPermissions("basic:omBusinessServiceScope:del")
    @RequestMapping(value = "delete")
    public AjaxJson delete(OmBusinessServiceScope omBusinessServiceScope, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        omBusinessServiceScopeService.deleteEntity(omBusinessServiceScope.getId());
        j.setMsg("删除业务服务范围成功");
        return j;
    }

    /**
     * 批量删除业务服务范围
     */
    @ResponseBody
    @RequiresPermissions("basic:omBusinessServiceScope:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            omBusinessServiceScopeService.deleteEntity(id);
        }
        j.setMsg("删除业务服务范围成功");
        return j;
    }

    /**
     * 导出excel文件
     */
    @ResponseBody
    @RequiresPermissions("basic:omBusinessServiceScope:export")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public AjaxJson exportFile(OmBusinessServiceScope omBusinessServiceScope, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        try {
            String fileName = "业务服务范围" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            Page<OmBusinessServiceScope> page = omBusinessServiceScopeService.findPage(new Page<OmBusinessServiceScope>(request, response, -1), omBusinessServiceScope);
            new ExportExcel("业务服务范围", OmBusinessServiceScope.class).setDataList(page.getList()).write(response, fileName).dispose();
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
    @RequiresPermissions("basic:omBusinessServiceScope:import")
    @RequestMapping(value = "import", method = RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            int successNum = 0;
            int failureNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<OmBusinessServiceScope> list = ei.getDataList(OmBusinessServiceScope.class);
            for (OmBusinessServiceScope omBusinessServiceScope : list) {
                try {
                    omBusinessServiceScopeService.save(omBusinessServiceScope);
                    successNum++;
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
        return "redirect:" + Global.getAdminPath() + "/oms/basic/omBusinessServiceScope/?repage";
    }

    /**
     * 下载导入业务服务范围数据模板
     */
    @RequiresPermissions("basic:omBusinessServiceScope:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            String fileName = "业务服务范围数据导入模板.xlsx";
            List<OmBusinessServiceScope> list = Lists.newArrayList();
            new ExportExcel("业务服务范围数据", OmBusinessServiceScope.class, 1).setDataList(list).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/oms/basic/omBusinessServiceScope/?repage";
    }

    public void area(String id) {

    }

    @RequestMapping(value = "area")
    public String area(OmBusinessServiceScopeEntity entity, Model model) {
        model.addAttribute("omBusinessServiceScopeEntity", entity);
        return "modules/oms/basic/omBusinessServiceScopeArea";
    }

    @ResponseBody
    @RequestMapping(value = "treeData")
    public List<Map<String, Object>> treeData(@RequestParam String bssId, @RequestParam(required = false) String extId, HttpServletResponse response) {
        OmBusinessServiceScopeEntity entity = omBusinessServiceScopeService.getEntity(bssId);
        String areaIds = entity != null ? entity.getAreaIds() + "," : "";
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
                    boolean isEnd = false;
                    if ("4".equals(e.getType())) {
                        isEnd = true;
                    } else if (!"4".equals(e.getType())) {
                        isEnd = areaService.getChildren(e.getId()).size() == 0;
                    }
                    if (isEnd) {
                        Map<String, Object> state = Maps.newHashMap();
                        state.put("selected", true);
                        map.put("state", state);
                    }
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

    @ResponseBody
    @RequiresPermissions("basic:omBusinessServiceScope:copy")
    @RequestMapping(value = "copy")
    public AjaxJson copy(String id) {
        AjaxJson j = new AjaxJson();

        try {
            omBusinessServiceScopeService.copy(id);
        } catch (OmsException e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } catch (Exception e) {
            logger.error("业务服务范围-复制", e);
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }
}