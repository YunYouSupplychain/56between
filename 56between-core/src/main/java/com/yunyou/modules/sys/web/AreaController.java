package com.yunyou.modules.sys.web;

import com.yunyou.common.config.Global;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.sys.entity.Area;
import com.yunyou.modules.sys.service.AreaService;
import com.yunyou.modules.sys.utils.UserUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * 区域Controller
 *
 * @author yunyou
 * @version 2016-5-15
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/area")
public class AreaController extends BaseController {
    @Autowired
    private AreaService areaService;

    @ModelAttribute("area")
    public Area get(@RequestParam(required = false) String id) {
        if (StringUtils.isNotBlank(id)) {
            return areaService.get(id);
        } else {
            return new Area();
        }
    }

    @RequiresPermissions("sys:area:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/sys/area/areaList";
    }

    @RequiresPermissions(value = {"sys:area:view", "sys:area:add", "sys:area:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(Area area, Model model) {
        if (area.getParent() == null || area.getParent().getId() == null) {
            area.setParent(UserUtils.getUser().getOffice().getArea());
        } else {
            area.setParent(areaService.get(area.getParent().getId()));
        }
        model.addAttribute("area", area);
        return "modules/sys/area/areaForm";
    }

    @RequiresPermissions(value = {"sys:area:add", "sys:area:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    @ResponseBody
    public AjaxJson save(Area area, Model model) {
        AjaxJson j = new AjaxJson();
        if (Global.isDemoMode() && !UserUtils.getUser().isAdmin()) {
            j.setSuccess(false);
            j.setMsg("演示模式，不允许操作！");
            return j;
        }
        if (!beanValidator(model, area)) {
            j.setSuccess(false);
            j.setMsg("参数错误！");
            return j;
        }
        areaService.save(area);
        j.setSuccess(true);
        j.put("area", area);
        j.setMsg("保存成功！");
        return j;
    }

    @RequiresPermissions("sys:area:del")
    @RequestMapping(value = "delete")
    @ResponseBody
    public AjaxJson delete(Area area) {
        AjaxJson j = new AjaxJson();
        if (Global.isDemoMode() && !UserUtils.getUser().isAdmin()) {
            j.setSuccess(false);
            j.setMsg("演示模式，不允许操作！");
            return j;
        }
        areaService.delete(area);
        j.setSuccess(true);
        j.setMsg("删除区域成功！");
        return j;
    }

    @ResponseBody
    @RequestMapping(value = "getChildren")
    public List<Area> getChildren(String parentId) {
        if ("-1".equals(parentId)) {
            //如果是-1，没指定任何父节点，就从根节点开始查找
            parentId = "0";
        }
        return areaService.getChildren(parentId);
    }

    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "treeData")
    public List<Map<String, Object>> treeData(@RequestParam(required = false) String extId) {
        List<Map<String, Object>> mapList = Lists.newArrayList();
        List<Area> list = areaService.findAll();
        for (Area e : list) {
            if (StringUtils.isBlank(extId) || !extId.equals(e.getId()) && !e.getParentIds().contains("," + extId + ",")) {
                Map<String, Object> map = Maps.newHashMap();
                map.put("id", e.getId());
                if (Global.TREE_ROOT_NODE.equals(e.getParentId())) {
                    map.put("parent", "#");
                    Map<String, Object> state = Maps.newHashMap();
                    state.put("opened", true);
                    map.put("state", state);
                } else {
                    map.put("parent", e.getParentId());
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
    @RequestMapping(value = "getAreaCodeById")
    public String getAreaCodeById(@RequestParam(required = false) String id) {
        Area area = this.get(id);
        return area.getCode();
    }

    @ResponseBody
    @RequestMapping(value = "getAreaByUserId")
    public Area getAreaByUserId(@RequestParam(required = false) String userId) {
        return areaService.getAreaByUser(userId);
    }

    @ResponseBody
    @RequestMapping(value = "getAreaByOfficeId")
    public Area getAreaByOfficeId(@RequestParam(required = false) String officeId) {
        return areaService.getAreaByOffice(officeId);
    }

    @ResponseBody
    @RequestMapping(value = "getFullName")
    public String getFullName(String areaId){
        return areaService.getFullName(areaId);
    }
}