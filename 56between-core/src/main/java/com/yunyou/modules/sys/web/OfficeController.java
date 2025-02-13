package com.yunyou.modules.sys.web;

import com.yunyou.common.config.Global;
import com.yunyou.common.exception.GlobalException;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.sys.OfficeType;
import com.yunyou.modules.sys.entity.Office;
import com.yunyou.modules.sys.entity.User;
import com.yunyou.modules.sys.service.OfficeService;
import com.yunyou.modules.sys.utils.UserUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 机构Controller
 *
 * @author yunyou
 * @version 2016-5-15
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/office")
public class OfficeController extends BaseController {
    @Autowired
    private OfficeService officeService;

    @RequiresPermissions("sys:office:list")
    @RequestMapping(value = {"", "list"})
    public String list(Office office, Model model) {
        if (StringUtils.isNotBlank(office.getId())) {
            office = officeService.get(office.getId());
        }
        if (office == null || office.getParentIds() == null) {
            model.addAttribute("list", officeService.findList(false));
        } else {
            model.addAttribute("list", officeService.findList(office));
        }
        return "modules/sys/office/officeList";
    }

    @RequiresPermissions(value = {"sys:office:view", "sys:office:add", "sys:office:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(Office office, Model model) {
        if (StringUtils.isNotBlank(office.getId())) {
            office = officeService.get(office.getId());
        }
        User user = UserUtils.getUser();
        if (office.getParent() == null || office.getParent().getId() == null) {
            office.setParent(user.getOffice());
        }
        office.setParent(officeService.get(office.getParent().getId()));
        if (office.getArea() == null) {
            office.setArea(user.getOffice().getArea());
        }
        // 自动生成编码
        if (StringUtils.isBlank(office.getId()) && office.getParent() != null) {
            List<Office> children = officeService.getChildren(office.getParent().getId());
            office.setCode(office.getParent().getCode() + StringUtils.leftPad(String.valueOf(children.size() + 1), 2, "0"));
        }
        model.addAttribute("office", office);
        return "modules/sys/office/officeForm";
    }

    @ResponseBody
    @RequiresPermissions(value = {"sys:office:add", "sys:office:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(Office office, Model model) {
        AjaxJson j = new AjaxJson();
        if (Global.isDemoMode() && !UserUtils.getUser().isAdmin()) {
            j.setSuccess(false);
            j.setMsg("演示模式，不允许操作！");
            return j;
        }
        if (!beanValidator(model, office)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            officeService.saveValidator(office);
        } catch (GlobalException e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
            return j;
        }
        if (StringUtils.isNotBlank(office.getId())) {
            Office old = officeService.get(office.getId());
            if (old != null) {
                office.setParentIds(old.getParentIds());
            }
        }
        try {
            officeService.save(office);
        } catch (GlobalException e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
            return j;
        }

        j.setSuccess(true);
        j.setMsg("保存机构'" + office.getName() + "'成功");
        j.put("office", office);
        return j;
    }

    @ResponseBody
    @RequiresPermissions("sys:office:del")
    @RequestMapping(value = "delete")
    public AjaxJson delete(Office office) {
        AjaxJson j = new AjaxJson();
        if (Global.isDemoMode() && !UserUtils.getUser().isAdmin()) {
            j.setSuccess(false);
            j.setMsg("演示模式，不允许操作！");
            return j;
        }
        if (StringUtils.isNotBlank(office.getId())) {
            office = officeService.get(office.getId());
        }
        officeService.delete(office);
        j.setSuccess(true);
        j.setMsg("删除成功！");
        return j;
    }

    @ResponseBody
    @RequestMapping(value = "getChildren")
    public List<Office> getChildren(String parentId) {
        if ("-1".equals(parentId)) {
            //如果是-1，没指定任何父节点，就从根节点开始查找
            parentId = "0";
        }
        return officeService.getChildren(parentId);
    }


    /**
     * 获取机构JSON数据
     *
     * @param extId 排除的ID
     * @param type  机构类型{@link OfficeType}
     */
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "treeData")
    public List<Map<String, Object>> treeData(@RequestParam(required = false) String extId,
                                              @RequestParam(required = false) String type,
                                              @RequestParam(required = false) Boolean isAll) {
        List<Map<String, Object>> mapList = Lists.newArrayList();
        // 设置节点展开
        Map<String, Object> state = Maps.newHashMap();
        state.put("opened", true);
        // 按树状层级排序
        List<Office> list = officeService.findList(isAll).stream().sorted(Comparator.comparingInt(o -> o.getParentIds().length())).collect(Collectors.toList());
        // 提取需要展示的节点ID（过滤掉排除的节点extId；筛选符合的机构类型type，type为空则全部展示，如果父节点type不符合，子节点符合，父节点依然展示用以保证树结构的完整性）
        Map<String, String> idMap = list.stream()
                .filter(o -> !o.getId().equals(extId) && !o.getParentIds().contains("," + extId + ",") && (StringUtils.isBlank(type) || o.getType().equals(type)))
                .map(o -> {
                    ArrayList<String> ids = Lists.newArrayList(o.getId());
                    ids.addAll(Arrays.asList(o.getParentIds().split(",")));
                    return ids;
                }).flatMap(ArrayList::stream).distinct().collect(Collectors.toMap(Function.identity(), Function.identity()));
        for (Office e : list) {
            if (!idMap.containsKey(e.getId())) {
                continue;
            }
            Map<String, Object> map = Maps.newHashMap();
            map.put("id", e.getId());
            if (Global.TREE_ROOT_NODE.equals(e.getParentId())) {
                map.put("parent", "#");
                map.put("state", state);
            } else {
                map.put("parent", e.getParentId());
            }
            map.put("name", e.getName());
            map.put("text", e.getName());
            map.put("type", e.getType());
            if (OfficeType.COMPANY.getValue().equals(e.getType())) {
                map.put("isParent", true);
            }
            mapList.add(map);
        }
        return mapList;
    }

    /**
     * 当前机构及下级数据
     */
    @ResponseBody
    @RequestMapping(value = "companyData")
    public Map<String, Object> companyData(Office office, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(officeService.findCompanyData(new Page<>(request, response), office));
    }

    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "bootstrap/treeData")
    public List<Map<String, Object>> bootstrapTreeData() {
        List<Map<String, Object>> mapList = Lists.newArrayList();
        List<Office> roots = officeService.getChildren(Global.TREE_ROOT_NODE);
        for (Office root : roots) {
            Map<String, Object> map = Maps.newHashMap();
            map.put("id", root.getId());
            map.put("name", root.getName());
            map.put("type", root.getType());
            deepTree(map, root);
            mapList.add(map);
        }
        return mapList;
    }

    public void deepTree(Map<String, Object> map, Office office) {
        // 不展开
        Map<Object, Object> state = Maps.newHashMap();
        state.put("expanded", false);

        List<Office> children = officeService.getChildren(office.getId());
        List<Map<String, Object>> descendants = children.stream().map(child -> {
            Map<String, Object> childMap = Maps.newHashMap();
            childMap.put("id", child.getId());
            childMap.put("name", child.getName());
            childMap.put("type", child.getType());
            childMap.put("state", state);
            deepTree(childMap, child);
            return childMap;
        }).collect(Collectors.toList());

        map.put("text", office.getName());
        if (CollectionUtil.isNotEmpty(descendants)) {
            map.put("children", descendants);
        }
    }

    /**
     * 网点对应机构
     */
    @ResponseBody
    @RequestMapping(value = "outletMatchedOrg")
    public Map<String, Object> outletMatchedOrg(Office office, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(officeService.findOutletMatchedOrg(new Page<>(request, response), office));
    }

    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping("getOrgCenter")
    public Office getOrgCenter(String officeId) {
        return UserUtils.getOrgCenter(officeId);
    }

    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping("getDispatchCenter")
    public Office getDispatchCenter(String officeId) {
        return UserUtils.getDispatchCenter(officeId);
    }
}