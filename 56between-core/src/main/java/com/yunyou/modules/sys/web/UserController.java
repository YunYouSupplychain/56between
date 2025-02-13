package com.yunyou.modules.sys.web;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yunyou.common.config.Global;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.sys.entity.Office;
import com.yunyou.modules.sys.entity.Role;
import com.yunyou.modules.sys.entity.User;
import com.yunyou.modules.sys.mapper.UserMapper;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 用户Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/user")
public class UserController extends BaseController {
    @Autowired
    private SystemService systemService;
    @Autowired
    private UserMapper userMapper;

    @ModelAttribute
    public User get(@RequestParam(required = false) String id) {
        if (StringUtils.isNotBlank(id)) {
            return systemService.getUser(id);
        } else {
            return new User();
        }
    }

    @RequiresPermissions("sys:user:index")
    @RequestMapping(value = "list")
    public String index(User user, Model model) {
        return "modules/sys/user/userIndex";
    }

    @RequiresPermissions("sys:user:index")
    @RequestMapping(value = "userSelect")
    public String userSelect(boolean isMultiSelect, Model model) {
        model.addAttribute("isMultiSelect", isMultiSelect);
        return "modules/common/userSelect";
    }

    @RequiresPermissions("sys:user:index")
    @ResponseBody
    @RequestMapping(value = "data")
    public Map<String, Object> list(User user, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(systemService.findUser(new Page<>(request, response), user));
    }

    @ResponseBody
    @RequestMapping(value = "popDate")
    public Map<String, Object> popDate(User user, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(systemService.findUser(new Page<>(request, response), user));
    }

    @RequiresPermissions(value = {"sys:user:view", "sys:user:add", "sys:user:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(User user, Model model) {
        if (user.getCompany() == null || user.getCompany().getId() == null) {
            user.setCompany(UserUtils.getUser().getCompany());
        }
        if (user.getOffice() == null || user.getOffice().getId() == null) {
            user.setOffice(UserUtils.getUser().getOffice());
        }
        model.addAttribute("user", user);
        model.addAttribute("allRoles", systemService.findRole(new Role()));
        return "modules/sys/user/userForm";
    }

    @RequiresPermissions(value = {"sys:user:add", "sys:user:edit"}, logical = Logical.OR)
    @ResponseBody
    @RequestMapping(value = "save")
    public AjaxJson save(User user, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        if (Global.isDemoMode() && !UserUtils.getUser().isAdmin()) {
            j.setSuccess(false);
            j.setMsg("演示模式，不允许操作!");
            return j;
        }
        // 修正引用赋值问题，不知道为何，Company和Office引用的一个实例地址，修改了一个，另外一个跟着修改。
        user.setCompany(new Office(request.getParameter("company.id")));
        user.setOffice(new Office(request.getParameter("office.id")));
        // 如果新密码为空，则不更换密码
        if (StringUtils.isNotBlank(user.getNewPassword())) {
            user.setPassword(SystemService.encryptPassword(user.getNewPassword()));
        }
        if (!beanValidator(model, user)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        if (!"true".equals(checkLoginName(user.getOldLoginName(), user.getLoginName()))) {
            j.setSuccess(false);
            j.setMsg("保存用户'" + user.getLoginName() + "'失败，登录名已存在!");
            return j;
        }
        // 角色数据有效性验证，过滤不在授权内的角色
        List<Role> roleList = Lists.newArrayList();
        List<String> roleIdList = user.getRoleIdList();
        for (Role r : systemService.findAllRole()) {
            if (roleIdList.contains(r.getId())) {
                roleList.add(r);
            }
        }
        user.setRoleList(roleList);
        // 保存用户信息
        systemService.saveUser(user);
        // 清除当前用户缓存
        if (user.getLoginName().equals(UserUtils.getUser().getLoginName())) {
            UserUtils.clearCache();
        }
        addMessage(redirectAttributes, "保存用户'" + user.getLoginName() + "'成功");
        j.setSuccess(true);
        j.setMsg("保存用户'" + user.getLoginName() + "'成功!");
        return j;
    }

    @ResponseBody
    @RequiresPermissions("sys:user:del")
    @RequestMapping(value = "delete")
    public AjaxJson delete(User user) {
        AjaxJson j = new AjaxJson();
        if (Global.isDemoMode() && !UserUtils.getUser().isAdmin()) {
            j.setSuccess(false);
            j.setMsg("演示模式，不允许操作!");
            return j;
        }
        if (UserUtils.getUser().getId().equals(user.getId())) {
            j.setSuccess(false);
            j.setMsg("删除失败，不允许删除当前用户!");
            return j;
        } else if (User.isAdmin(user.getId())) {
            j.setSuccess(false);
            j.setMsg("删除失败，不允许删除超级管理员!");
            return j;
        } else {
            systemService.deleteUser(user);
            j.setSuccess(true);
            j.setMsg("删除成功!");
            return j;
        }
    }

    /**
     * 批量删除用户
     */
    @ResponseBody
    @RequiresPermissions("sys:user:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        String[] idArray = ids.split(",");
        AjaxJson j = new AjaxJson();
        if (Global.isDemoMode() && !UserUtils.getUser().isAdmin()) {
            j.setSuccess(false);
            j.setMsg("演示模式，不允许操作!");
            return j;
        }
        for (String id : idArray) {
            User user = systemService.getUser(id);
            if (UserUtils.getUser().getId().equals(user.getId())) {
                j.setSuccess(false);
                j.setMsg("删除失败，不允许删除当前用户!");
            } else if (User.isAdmin(user.getId())) {
                j.setSuccess(false);
                j.setMsg("删除失败，不允许删除超级管理员!");
            } else {
                j.setSuccess(true);
                j.setMsg("删除成功!");
                systemService.deleteUser(user);
            }
        }
        return j;
    }

    /**
     * 验证登录名是否有效
     *
     * @param oldLoginName 旧登录名
     * @param loginName    新登录名
     */
    @ResponseBody
    @RequiresPermissions(value = {"sys:user:add", "sys:user:edit"}, logical = Logical.OR)
    @RequestMapping(value = "checkLoginName")
    public String checkLoginName(String oldLoginName, String loginName) {
        if (loginName != null && loginName.equals(oldLoginName)) {
            return "true";
        } else if (loginName != null && systemService.getUserByLoginName(loginName) == null) {
            return "true";
        }
        return "false";
    }

    /**
     * 返回用户信息
     */
    @RequiresPermissions("user")
    @ResponseBody
    @RequestMapping(value = "infoData")
    public AjaxJson infoData() {
        AjaxJson j = new AjaxJson();
        j.setSuccess(true);
        j.setErrorCode("-1");
        j.setMsg("获取个人信息成功!");
        j.put("data", UserUtils.getUser());
        return j;
    }

    /**
     * 修改个人用户密码
     */
    @RequiresPermissions("user")
    @RequestMapping(value = "modifyPwd")
    public String modifyPwd() {
        return "modules/sys/user/userModifyPwd";
    }

    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "savePwd")
    public AjaxJson savePwd(String oldPassword, String newPassword) {
        AjaxJson j = new AjaxJson();
        User user = UserUtils.getUser();
        if (StringUtils.isNotBlank(oldPassword) && StringUtils.isNotBlank(newPassword)) {
            if (Global.isDemoMode() && !UserUtils.getUser().isAdmin()) {
                j.setSuccess(false);
                j.setMsg("演示模式，不允许操作！");
                return j;
            }
            if (SystemService.validatePassword(oldPassword, user.getPassword())) {
                systemService.updatePasswordById(user.getId(), user.getLoginName(), newPassword);
                j.setSuccess(true);
                j.setMsg("修改密码成功！");
                return j;
            } else {
                j.setSuccess(false);
                j.setMsg("修改密码失败，旧密码错误！");
                return j;
            }
        }
        j.setSuccess(false);
        j.setMsg("参数错误！");
        return j;
    }

    @RequiresPermissions("user")
    @ResponseBody
    @RequestMapping(value = "treeData")
    public List<Map<String, Object>> treeData(@RequestParam(required = false) String officeId) {
        List<Map<String, Object>> mapList = Lists.newArrayList();
        List<User> list = systemService.findUserByOfficeId(officeId);
        for (User e : list) {
            Map<String, Object> map = Maps.newHashMap();
            map.put("id", "u_" + e.getId());
            map.put("pId", officeId);
            map.put("name", StringUtils.replace(e.getName(), " ", ""));
            mapList.add(map);
        }
        return mapList;
    }

    /**
     * web端ajax验证用户名是否可用
     */
    @ResponseBody
    @RequestMapping(value = "validateLoginName")
    public boolean validateLoginName(String loginName) {
        User user = userMapper.findUniqueByProperty("login_name", loginName);
        return user == null;
    }

    /**
     * web端ajax验证手机号是否可以注册（数据库中不存在）
     */
    @ResponseBody
    @RequestMapping(value = "validateMobile")
    public boolean validateMobile(String mobile) {
        User user = userMapper.findUniqueByProperty("mobile", mobile);
        return user == null;
    }

    /**
     * web端ajax验证手机号是否已经注册（数据库中已存在）
     */
    @ResponseBody
    @RequestMapping(value = "validateMobileExist")
    public boolean validateMobileExist(String mobile) {
        User user = userMapper.findUniqueByProperty("mobile", mobile);
        return user != null;
    }

}
