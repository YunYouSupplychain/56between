<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>修改密码</title>
    <meta name="decorator" content="ani"/>
    <script type="text/javascript">
        $(document).ready(function () {
            $("#oldPassword").focus();
            $("#inputForm").validate({rules: {newPassword: {isComplexPwd: true}}});
        });
    </script>
</head>
<body class="bg-white" style="border: 1px solid #e3e3e3;">
<form:form id="inputForm" modelAttribute="user" action="${ctx}/sys/user/modifyPwd" method="post" class="form">
    <form:hidden path="id"/>
    <sys:message content="${message}"/>
    <div class="tabs-container">
        <table class="table well">
            <tr>
                <td class="width-20"><label class="pull-right asterisk">原密码</label></td>
                <td class="width-80">
                    <input id="oldPassword" name="oldPassword" type="password" value="" class="form-control required"
                           minlength="6" maxlength="10"/>
                </td>
            </tr>
            <tr>
                <td class="width-20" style="vertical-align: top"><label class="pull-right asterisk">新密码</label></td>
                <td class="width-80">
                    <input id="newPassword" name="newPassword" type="password" value="" class="form-control required"
                           minlength="6" maxlength="12"/>
                </td>
            </tr>
            <tr>
                <td class="width-20"><label class="pull-right asterisk">确认新密码</label></td>
                <td class="width-80">
                    <input id="confirmNewPassword" name="confirmNewPassword" type="password" value="" class="form-control required"
                           minlength="6" maxlength="12"/>
                </td>
            </tr>
        </table>
    </div>
</form:form>
</body>
</html>