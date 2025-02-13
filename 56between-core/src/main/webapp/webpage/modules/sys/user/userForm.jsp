<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>用户管理</title>
    <meta name="decorator" content="ani"/>
    <script type="text/javascript">
        var validateForm;
        var $table; // 父页面table表格id
        var $topIndex;//弹出窗口的 index
        function doSubmit(table, index) {//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
            if (validateForm.form()) {
                $table = table;
                $topIndex = index;
                jp.loading();
                $("#inputForm").submit();
                return true;
            }
            return false;
        }

        $(document).ready(function () {
            $("#no").focus();
            validateForm = $("#inputForm").validate({
                rules: {
                    loginName: {remote: "${ctx}/sys/user/checkLoginName?oldLoginName=" + encodeURIComponent('${user.loginName}')},
                    newPassword: {isComplexPwd: true},
                    phone: {isTel: true}
                },
                messages: {
                    loginName: {remote: "用户登录名已存在"},
                    confirmNewPassword: {equalTo: "输入与上面相同的密码"}
                },
                submitHandler: function (form) {
                    jp.post("${ctx}/sys/user/save", $('#inputForm').serialize(), function (data) {
                        if (data.success) {
                            $table.bootstrapTable('refresh');
                            jp.success(data.msg);
                        } else {
                            jp.error(data.msg);
                        }
                        jp.close($topIndex);//关闭dialog
                    });
                },
                errorContainer: "#messageBox",
                errorPlacement: function (error, element) {
                    $("#messageBox").text("输入有误，请先更正。");
                    if (element.is(":checkbox") || element.is(":radio") || element.parent().is(".input-append")) {
                        error.appendTo(element.parent().parent());
                    } else {
                        error.insertAfter(element);
                    }
                }
            });
        });
    </script>
</head>
<body>
<form:form id="inputForm" modelAttribute="user" action="${ctx}/sys/user/save" method="post" class="form">
    <form:hidden path="id"/>
    <form:hidden path="photo"/>
    <sys:message content="${message}"/>
    <div class="tabs-container">
        <table class="table well">
            <tbody>
            <tr>
                <td class="width-15"><label class="pull-right asterisk">归属公司</label></td>
                <td class="width-35">
                    <sys:treeselect id="company" name="company.id" value="${user.company.id}"
                                    labelName="company.name" labelValue="${user.company.name}"
                                    title="公司" url="/sys/office/treeData?type=1" cssClass="form-control required"/>
                </td>
                <td class="width-15"><label class="pull-right asterisk">归属机构</label></td>
                <td class="width-35">
                    <sys:treeselect id="office" name="office.id" value="${user.office.id}"
                                    labelName="office.name" labelValue="${user.office.name}"
                                    title="机构" url="/sys/office/treeData" cssClass="form-control required" allowSearch="true"/>
                </td>
            </tr>
            <tr>
                <td class="width-15"><label class="pull-right asterisk">工号</label></td>
                <td class="width-35">
                    <form:input path="no" htmlEscape="false" maxlength="50" class="form-control required"/>
                </td>
                <td class="width-15"><label class="pull-right asterisk">姓名</label></td>
                <td class="width-35">
                    <form:input path="name" htmlEscape="false" maxlength="50" class="form-control required"/>
                </td>
            </tr>
            <tr>
                <td class="width-15"><label class="pull-right asterisk">登录名</label></td>
                <td class="width-35">
                    <input id="oldLoginName" name="oldLoginName" type="hidden" value="${user.loginName}">
                    <form:input path="loginName" htmlEscape="false" maxlength="50" class="form-control required userName"/>
                </td>
                <td class="width-15"><label class="pull-right asterisk">角色</label></td>
                <td class="width-35">
                    <form:select path="roleIdList" class="form-control required" multiple="false">
                        <form:option value=""/>
                        <c:forEach items="${allRoles}" var="item">
                            <form:option value="${item.id}">${item.name}</form:option>
                        </c:forEach>
                    </form:select>
                </td>
            </tr>
            <tr>
                <td class="width-15"><label class="pull-right"><c:if test="${empty user.id}"><font color="red">*</font></c:if>密码</label></td>
                <td class="width-35">
                    <input id="newPassword" name="newPassword" type="password" value="" maxlength="12" minlength="6" class="form-control ${empty user.id?'required':''}"/>
                </td>
                <td class="width-15"><label class="pull-right"><c:if test="${empty user.id}"><font color="red">*</font></c:if>确认密码</label></td>
                <td class="width-35">
                    <input id="confirmNewPassword" name="confirmNewPassword" type="password"
                           class="form-control ${empty user.id?'required':''}" value="" maxlength="12" minlength="6" equalTo="#newPassword"/>
                </td>
            </tr>
            <tr>
                <td class="width-15"><label class="pull-right">是否允许登录</label></td>
                <td class="width-35">
                    <form:select path="loginFlag" class="form-control">
                        <form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                    </form:select>
                </td>
                <td class="width-15"><label class="pull-right">电话</label></td>
                <td class="width-35">
                    <form:input path="phone" htmlEscape="false" maxlength="100" class="form-control"/>
                </td>
            </tr>
            <tr>
                <td class="width-15"><label class="pull-right">邮箱</label></td>
                <td colspan="3">
                    <form:input path="email" htmlEscape="false" maxlength="100" class="form-control email"/>
                </td>
            </tr>
            <tr>
                <td class="width-15"><label class="pull-right">备注</label></td>
                <td colspan="3">
                    <form:input path="remarks" htmlEscape="false" maxlength="200" class="form-control"/>
                </td>
            </tr>
            <c:if test="${not empty user.id}">
                <tr>
                    <td class="width-15"><label class="pull-right">创建时间</label></td>
                    <td class="width-35">
                        <span class="lbl"><fmt:formatDate value="${user.createDate}" type="both" dateStyle="full"/></span>
                    </td>
                    <td class="width-15"><label class="pull-right">最后登陆</label></td>
                    <td class="width-35">
                        <span class="lbl">IP: ${user.loginIp}&nbsp;&nbsp;&nbsp;&nbsp;<fmt:formatDate value="${user.loginDate}" type="both" dateStyle="full"/></span>
                    </td>
                </tr>
            </c:if>
            </tbody>
        </table>
    </div>
</form:form>
</body>
</html>