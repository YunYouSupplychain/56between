package com.yunyou.modules.sys.web;

import com.yunyou.common.config.Global;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.sys.entity.Role;
import com.yunyou.modules.sys.entity.User;
import com.yunyou.modules.sys.service.SystemService;
import com.yunyou.modules.sys.utils.UserUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 角色Controller
 *
 * @author yunyou
 * @version 2016-12-05
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/role")
public class RoleController extends BaseController {
    @Autowired
    private SystemService systemService;

    @ModelAttribute("role")
    public Role get(@RequestParam(required = false) String id) {
        if (StringUtils.isNotBlank(id)) {
            return systemService.getRole(id);
        } else {
            return new Role();
        }
    }

    @RequiresPermissions("sys:role:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/sys/role/roleList";
    }

    @ResponseBody
    @RequiresPermissions("sys:role:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(Role role) {
        List<Role> list = systemService.findAllRole();
        Map<String, Object> map = new HashMap<>();
        map.put("rows", list);
        map.put("total", list.size());
        return map;
    }

    @RequiresPermissions(value = {"sys:role:view", "sys:role:add", "sys:role:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(Role role, Model model) {
        if (role.getOffice() == null) {
            role.setOffice(UserUtils.getUser().getOffice());
        }
        model.addAttribute("role", role);
        return "modules/sys/role/roleForm";
    }

    @RequiresPermissions("sys:role:auth")
    @RequestMapping(value = "auth")
    public String auth(Role role, Model model) {
        if (role.getOffice() == null) {
            role.setOffice(UserUtils.getUser().getOffice());
        }
        model.addAttribute("role", role);
        return "modules/sys/role/roleAuth";
    }

    @ResponseBody
    @RequiresPermissions(value = {"sys:role:assign", "sys:role:auth", "sys:role:add", "sys:role:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(Role role, Model model) {
        AjaxJson j = new AjaxJson();
        if (!UserUtils.getUser().isAdmin() && role.getSysData().equals(Global.YES)) {
            j.setSuccess(false);
            j.setMsg("越权操作，只有超级管理员才能修改此数据！");
            return j;
        }
        if (Global.isDemoMode() && !UserUtils.getUser().isAdmin()) {
            j.setSuccess(false);
            j.setMsg("演示模式，不允许操作！");
            return j;
        }
        if (!beanValidator(model, role)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        if (!"true".equals(checkName(role.getOldName(), role.getName()))) {
            j.setSuccess(false);
            j.setMsg("保存角色'" + role.getName() + "'失败, 角色名已存在");
            return j;
        }
        if (!"true".equals(checkEnname(role.getOldEnname(), role.getEnname()))) {
            j.setSuccess(false);
            j.setMsg("保存角色'" + role.getName() + "'失败, 英文名已存在");
            return j;
        }
        systemService.saveRole(role);
        j.setSuccess(true);
        j.setMsg("保存角色'" + role.getName() + "'成功");
        return j;
    }

    /**
     * 删除角色
     */
    @ResponseBody
    @RequiresPermissions("sys:role:del")
    @RequestMapping(value = "delete")
    public AjaxJson delete(String ids) {
        AjaxJson j = new AjaxJson();
        if (Global.isDemoMode() && !UserUtils.getUser().isAdmin()) {
            j.setSuccess(false);
            j.setMsg("演示模式，不允许操作！");
            return j;
        }
        String[] idArray = ids.split(",");
        StringBuilder msg = new StringBuilder();
        for (String id : idArray) {
            Role role = systemService.getRole(id);
            if (!UserUtils.getUser().isAdmin() && role.getSysData().equals(Global.YES)) {
                msg.append("越权操作，只有超级管理员才能修改[").append(role.getName()).append("]数据！<br/>");
            } else {
                systemService.deleteRole(role);
                msg.append("删除角色[").append(role.getName()).append("]成功<br/>");

            }
        }
        j.setSuccess(true);
        j.setMsg(msg.toString());
        return j;
    }

    /**
     * 获取所属角色用户
     */
    @ResponseBody
    @RequiresPermissions("sys:role:assign")
    @RequestMapping(value = "assign")
    public Map<String, Object> assign(HttpServletRequest request, HttpServletResponse response, Role role) {
        return getBootstrapData(systemService.findUser(new Page<>(request, response), new User(new Role(role.getId()))));
    }


    /**
     * 角色分配 -- 从角色中移除用户
     *
     * @param userId 用户ID
     * @param roleId 角色ID
     */
    @ResponseBody
    @RequiresPermissions("sys:role:assign")
    @RequestMapping(value = "outrole")
    public AjaxJson outrole(String userId, String roleId) {
        AjaxJson j = new AjaxJson();
        if (Global.isDemoMode() && !UserUtils.getUser().isAdmin()) {
            j.setMsg("演示模式，不允许操作！");
            j.setSuccess(false);
            return j;
        }
        Role role = systemService.getRole(roleId);
        User user = systemService.getUser(userId);
        if (UserUtils.getUser().getId().equals(userId)) {
            j.setMsg("无法从角色【" + role.getName() + "】中移除用户【" + user.getName() + "】自己！");
            j.setSuccess(false);
        } else {
            if (user.getRoleList().size() <= 1) {
                j.setMsg("用户【" + user.getName() + "】从角色【" + role.getName() + "】中移除失败！这已经是该用户的唯一角色，不能移除。");
                j.setSuccess(false);
            } else {
                Boolean flag = systemService.outUserInRole(role, user);
                if (!flag) {
                    j.setMsg("用户【" + user.getName() + "】从角色【" + role.getName() + "】中移除失败！");
                    j.setSuccess(false);
                } else {
                    j.setMsg("用户【" + user.getName() + "】从角色【" + role.getName() + "】中移除成功！");
                    j.setSuccess(true);
                }
            }
        }
        return j;
    }

    /**
     * 角色分配
     *
     * @param role 角色
     * @param ids  用户ID
     */
    @ResponseBody
    @RequiresPermissions("sys:role:assign")
    @RequestMapping(value = "assignrole")
    public AjaxJson assignRole(Role role, String[] ids) {
        AjaxJson j = new AjaxJson();
        if (Global.isDemoMode() && !UserUtils.getUser().isAdmin()) {
            j.setSuccess(false);
            j.setMsg("演示模式，不允许操作！");
            return j;
        }
        StringBuilder msg = new StringBuilder();
        int newNum = 0;
        for (String id : ids) {
            User user = systemService.assignUserToRole(role, systemService.getUser(id));
            if (null != user) {
                msg.append("<br/>新增用户【").append(user.getName()).append("】到角色【").append(role.getName()).append("】！");
                newNum++;
            }
        }
        j.setSuccess(true);
        j.setMsg("已成功分配 " + newNum + " 个用户" + msg);
        return j;
    }

    /**
     * 验证角色名是否有效
     *
     * @param oldName 角色名
     * @param name    新角色名
     */
    @RequiresPermissions("user")
    @ResponseBody
    @RequestMapping(value = "checkName")
    public String checkName(String oldName, String name) {
        if (name != null && name.equals(oldName)) {
            return "true";
        } else if (name != null && systemService.getRoleByName(name) == null) {
            return "true";
        }
        return "false";
    }

    /**
     * 验证角色英文名是否有效
     *
     * @param oldEnname 角色英文名
     * @param enname    新角色英文名
     */
    @RequiresPermissions("user")
    @ResponseBody
    @RequestMapping(value = "checkEnname")
    public String checkEnname(String oldEnname, String enname) {
        if (enname != null && enname.equals(oldEnname)) {
            return "true";
        } else if (enname != null && systemService.getRoleByEnname(enname) == null) {
            return "true";
        }
        return "false";
    }

}
