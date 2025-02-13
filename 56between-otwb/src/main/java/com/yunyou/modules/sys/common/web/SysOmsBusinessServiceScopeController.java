package com.yunyou.modules.sys.common.web;

import com.yunyou.common.config.Global;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yunyou.common.exception.GlobalException;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.interfaces.interactive.action.SyncPlatformDataToOmsAction;
import com.yunyou.modules.sys.common.entity.SysOmsBusinessServiceScope;
import com.yunyou.modules.sys.common.service.SysOmsBusinessServiceScopeService;
import com.yunyou.modules.sys.entity.Area;
import com.yunyou.modules.sys.service.AreaService;
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
import java.util.List;
import java.util.Map;

/**
 * 业务服务范围Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/common/oms/businessServiceScope")
public class SysOmsBusinessServiceScopeController extends BaseController {
    @Autowired
    private SysOmsBusinessServiceScopeService sysOmsBusinessServiceScopeService;
    @Autowired
    private SyncPlatformDataToOmsAction syncPlatformDataToOmsAction;
    @Autowired
    private AreaService areaService;

    @ModelAttribute
    public SysOmsBusinessServiceScope get(@RequestParam(required = false) String id) {
        SysOmsBusinessServiceScope entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = sysOmsBusinessServiceScopeService.get(id);
        }
        if (entity == null) {
            entity = new SysOmsBusinessServiceScope();
        }
        return entity;
    }

    /**
     * 列表页面
     */
    @RequiresPermissions("sys:common:oms:businessServiceScope:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/sys/common/sysOmsBusinessServiceScopeList";
    }

    /**
     * 列表数据
     */
    @ResponseBody
    @RequiresPermissions("sys:common:oms:businessServiceScope:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(SysOmsBusinessServiceScope entity, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(entity.getDataSet())) {
            entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
        }
        return getBootstrapData(sysOmsBusinessServiceScopeService.findPage(new Page<>(request, response), entity));
    }

    /**
     * 查看，增加，编辑表单页面
     */
    @RequiresPermissions(value = {"sys:common:oms:businessServiceScope:view", "sys:common:oms:businessServiceScope:add", "sys:common:oms:businessServiceScope:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(SysOmsBusinessServiceScope entity, Model model) {
        model.addAttribute("sysOmsBusinessServiceScope", entity);
        return "modules/sys/common/sysOmsBusinessServiceScopeForm";
    }

    /**
     * 保存
     */
    @ResponseBody
    @RequiresPermissions(value = {"sys:common:oms:businessServiceScope:add", "sys:common:oms:businessServiceScope:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(SysOmsBusinessServiceScope entity, Model model) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, entity)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            sysOmsBusinessServiceScopeService.save(entity);
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
    @RequiresPermissions("sys:common:oms:businessServiceScope:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            sysOmsBusinessServiceScopeService.delete(sysOmsBusinessServiceScopeService.get(id));
        }
        j.setMsg("删除成功");
        return j;
    }

    @ResponseBody
    @RequiresPermissions("sys:common:oms:businessServiceScope:sync")
    @RequestMapping(value = "syncSelect", method = RequestMethod.POST)
    public AjaxJson syncSelect(String ids) {
        AjaxJson j = new AjaxJson();
        try {
            String[] idArr = ids.split(",");
            for (String id : idArr) {
                sysOmsBusinessServiceScopeService.findSync(new SysOmsBusinessServiceScope(id)).forEach(syncPlatformDataToOmsAction::sync);
            }
        } catch (Exception e) {
            logger.error("同步异常", e);
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    @ResponseBody
    @RequiresPermissions("sys:common:oms:businessServiceScope:sync")
    @RequestMapping(value = "syncAll", method = {RequestMethod.GET, RequestMethod.POST})
    public AjaxJson syncAll(SysOmsBusinessServiceScope entity) {
        AjaxJson j = new AjaxJson();
        try {
            if (StringUtils.isBlank(entity.getDataSet())) {
                entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
            }
            sysOmsBusinessServiceScopeService.findSync(entity).forEach(syncPlatformDataToOmsAction::sync);
        } catch (Exception e) {
            logger.error("同步异常", e);
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    @RequestMapping(value = "area")
    public String area(SysOmsBusinessServiceScope entity, Model model) {
        model.addAttribute("sysOmsBusinessServiceScope", entity);
        return "modules/sys/common/sysOmsBusinessServiceScopeArea";
    }

    @ResponseBody
    @RequestMapping(value = "treeData")
    public List<Map<String, Object>> treeData(@RequestParam String bssId, @RequestParam(required = false) String extId) {
        SysOmsBusinessServiceScope entity = sysOmsBusinessServiceScopeService.get(bssId);
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

    /**
     * 弹出框数据
     */
    @ResponseBody
    @RequestMapping(value = "grid")
    public Map<String, Object> grid(SysOmsBusinessServiceScope entity, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(entity.getDataSet())) {
            entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
        }
        return getBootstrapData(sysOmsBusinessServiceScopeService.findGrid(new Page<>(request, response), entity));
    }
}