package com.yunyou.modules.sys.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.yunyou.common.config.Global;
import com.yunyou.common.utils.SpringContextHolder;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.DataEntity;

import java.util.Date;
import java.util.List;

/**
 * 用户Entity
 *
 * @author yunyou
 * @version 2016-12-05
 */
public class User extends DataEntity<User> {

    private static final long serialVersionUID = 1L;
    /**
     * 归属公司
     */
    private Office company;
    /**
     * 归属机构
     */
    private Office office;
    /**
     * 登录名
     */
    private String loginName;
    /**
     * 密码
     */
    private String password;
    /**
     * 工号
     */
    private String no;
    /**
     * 姓名
     */
    private String name;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 电话
     */
    private String phone;
    /**
     * 手机
     */
    @Deprecated
    private String mobile;
    /**
     * 最后登陆IP
     */
    private String loginIp;
    /**
     * 最后登陆日期
     */
    private Date loginDate;
    /**
     * 是否允许登陆
     */
    private String loginFlag;
    /**
     * 头像
     */
    private String photo;
    /**
     * 二维码
     */
    private String qrCode;
    /**
     * 原登录名
     */
    private String oldLoginName;
    /**
     * 新密码
     */
    private String newPassword;
    /**
     * gex签名
     */
    private String sign;

    /**
     * 上次登陆IP
     */
    private String oldLoginIp;
    /**
     * 上次登陆日期
     */
    private Date oldLoginDate;
    /**
     * 拥有角色列表
     */
    private List<Role> roleList = Lists.newArrayList();
    /**
     * 当前机构Id
     */
    private String currentOrgId;
    /**
     * 根据角色查询用户条件
     */
    private Role role;

    public User() {
        super();
        this.loginFlag = Global.YES;
    }

    public User(String id) {
        super(id);
    }

    public User(String id, String loginName) {
        super(id);
        this.loginName = loginName;
    }

    public User(String id, Office company) {
        super(id);
        this.company = company;
    }

    public User(Role role) {
        super();
        this.role = role;
    }

    public static boolean isAdmin(String id) {
        return "1".equals(id);
    }

    public String getPhoto() {
        if ("".equals(photo)) {
            return SpringContextHolder.getStatic() + "/common/images/flat-avatar.png";
        }
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getLoginFlag() {
        return loginFlag;
    }

    public void setLoginFlag(String loginFlag) {
        this.loginFlag = loginFlag;
    }

    public Office getCompany() {
        return company;
    }

    public void setCompany(Office company) {
        this.company = company;
    }

    public Office getOffice() {
        return office;
    }

    public void setOffice(Office office) {
        this.office = office;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Deprecated
    public String getMobile() {
        return mobile;
    }

    @Deprecated
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(Date loginDate) {
        this.loginDate = loginDate;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getOldLoginName() {
        return oldLoginName;
    }

    public void setOldLoginName(String oldLoginName) {
        this.oldLoginName = oldLoginName;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getOldLoginIp() {
        if (oldLoginIp == null) {
            return loginIp;
        }
        return oldLoginIp;
    }

    public void setOldLoginIp(String oldLoginIp) {
        this.oldLoginIp = oldLoginIp;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getOldLoginDate() {
        if (oldLoginDate == null) {
            return loginDate;
        }
        return oldLoginDate;
    }

    public void setOldLoginDate(Date oldLoginDate) {
        this.oldLoginDate = oldLoginDate;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @JsonIgnore
    public List<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }

    @JsonIgnore
    public List<String> getRoleIdList() {
        List<String> roleIdList = Lists.newArrayList();
        for (Role role : roleList) {
            roleIdList.add(role.getId());
        }
        return roleIdList;
    }

    public void setRoleIdList(List<String> roleIdList) {
        roleList = Lists.newArrayList();
        for (String roleId : roleIdList) {
            Role role = new Role();
            role.setId(roleId);
            roleList.add(role);
        }
    }

    /**
     * 用户拥有的角色名称字符串, 多个角色名称用','分隔.
     */
    public String getRoleNames() {
        return CollectionUtil.extractToString(roleList, "name", ",");
    }

    public boolean isAdmin() {
        return isAdmin(this.id);
    }

    @Override
    public String toString() {
        return id;
    }

    public String getCurrentOrgId() {
        return currentOrgId;
    }

    public void setCurrentOrgId(String currentOrgId) {
        this.currentOrgId = currentOrgId;
    }
}