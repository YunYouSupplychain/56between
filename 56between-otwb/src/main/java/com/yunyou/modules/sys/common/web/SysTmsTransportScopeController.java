package com.yunyou.modules.sys.common.web;

import com.yunyou.common.config.Global;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yunyou.common.exception.GlobalException;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.interfaces.interactive.action.SyncPlatformDataToTmsAction;
import com.yunyou.modules.sys.common.entity.SysTmsTransportScope;
import com.yunyou.modules.sys.common.service.SysTmsTransportScopeService;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 业务服务范围Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/common/tms/transportScope")
public class SysTmsTransportScopeController extends BaseController {
    @Autowired
    private SysTmsTransportScopeService sysTmsTransportScopeService;
    @Autowired
    private SyncPlatformDataToTmsAction syncPlatformDataToTmsAction;
    @Autowired
    private AreaService areaService;

    @ModelAttribute
    public SysTmsTransportScope get(@RequestParam(required = false) String id) {
        SysTmsTransportScope entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = sysTmsTransportScopeService.get(id);
        }
        if (entity == null) {
            entity = new SysTmsTransportScope();
        }
        return entity;
    }

    /**
     * 列表页面
     */
    @RequiresPermissions("sys:common:tms:transportScope:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/sys/common/sysTmsTransportScopeList";
    }

    /**
     * 列表数据
     */
    @ResponseBody
    @RequiresPermissions("sys:common:tms:transportScope:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(SysTmsTransportScope entity, HttpServletRequest request, HttpServletResponse response, Model model) {
        if (StringUtils.isBlank(entity.getDataSet())) {
            entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
        }
        return getBootstrapData(sysTmsTransportScopeService.findPage(new Page<>(request, response), entity));
    }

    /**
     * 查看，增加，编辑表单页面
     */
    @RequiresPermissions(value = {"sys:common:tms:transportScope:view", "sys:common:tms:transportScope:add", "sys:common:tms:transportScope:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(SysTmsTransportScope sysTmsTransportScope, Model model) {
        model.addAttribute("sysTmsTransportScope", sysTmsTransportScope);
        return "modules/sys/common/sysTmsTransportScopeForm";
    }

    /**
     * 保存
     */
    @ResponseBody
    @RequiresPermissions(value = {"sys:common:tms:transportScope:add", "sys:common:tms:transportScope:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(SysTmsTransportScope sysTmsTransportScope, Model model, RedirectAttributes redirectAttributes) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, sysTmsTransportScope)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            sysTmsTransportScopeService.saveValidator(sysTmsTransportScope);
            sysTmsTransportScopeService.save(sysTmsTransportScope);
            j.put("entity", sysTmsTransportScope);
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
    @RequiresPermissions("sys:common:tms:transportScope:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();

        StringBuilder errMsg = new StringBuilder();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            SysTmsTransportScope sysTmsTransportScope = sysTmsTransportScopeService.get(id);
            try {
                sysTmsTransportScopeService.delete(sysTmsTransportScope);
            } catch (Exception e) {
                logger.error("删除id=[" + id + "]", e);
                errMsg.append("<br>").append("[").append(sysTmsTransportScope.getCode()).append("]删除失败");
            }
        }
        if (StringUtils.isNotBlank(errMsg)) {
            j.setMsg("操作成功，其中" + errMsg.toString());
        }
        return j;
    }

    /**
     * Grid列表数据
     */
    @ResponseBody
    @RequestMapping(value = "grid")
    public Map<String, Object> grid(SysTmsTransportScope entity, HttpServletRequest request, HttpServletResponse response, Model model) {
        if (StringUtils.isBlank(entity.getDataSet())) {
            entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
        }
        return getBootstrapData(sysTmsTransportScopeService.findGrid(new Page<>(request, response), entity));
    }

    @RequestMapping(value = "area")
    public String area(String id, Model model) {
        model.addAttribute("sysTmsTransportScope", sysTmsTransportScopeService.get(id));
        return "modules/sys/common/sysTmsTransportScopeArea";
    }

    @ResponseBody
    @RequestMapping(value = "saveArea")
    public AjaxJson saveArea(SysTmsTransportScope entity) {
        AjaxJson j = new AjaxJson();
        try {
            sysTmsTransportScopeService.saveArea(entity);
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

    @ResponseBody
    @RequestMapping(value = "treeData")
    public List<Map<String, Object>> treeData(@RequestParam String headId, @RequestParam(required = false) String extId, HttpServletResponse response) {
        SysTmsTransportScope entity = sysTmsTransportScopeService.get(headId);
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

    @ResponseBody
    @RequiresPermissions("sys:common:tms:transportScope:sync")
    @RequestMapping(value = "syncSelect", method = RequestMethod.POST)
    public AjaxJson syncSelect(String ids) {
        AjaxJson j = new AjaxJson();
        try {
            String[] idArr = ids.split(",");
            for (String id : idArr) {
                sysTmsTransportScopeService.findSync(new SysTmsTransportScope(id)).forEach(syncPlatformDataToTmsAction::sync);
            }
        } catch (Exception e) {
            logger.error("同步异常", e);
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    @ResponseBody
    @RequiresPermissions("sys:common:tms:transportScope:sync")
    @RequestMapping(value = "syncAll", method = {RequestMethod.GET, RequestMethod.POST})
    public AjaxJson syncAll(SysTmsTransportScope entity) {
        AjaxJson j = new AjaxJson();
        try {
            if (StringUtils.isBlank(entity.getDataSet())) {
                entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
            }
            sysTmsTransportScopeService.findSync(entity).forEach(syncPlatformDataToTmsAction::sync);
        } catch (Exception e) {
            logger.error("同步异常", e);
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

}