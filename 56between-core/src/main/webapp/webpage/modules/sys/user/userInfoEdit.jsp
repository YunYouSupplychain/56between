<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>个人信息</title>
    <meta name="decorator" content="ani"/>
</head>
<body class="bg-white" style="border: 1px solid #e3e3e3;">
<form:form id="inputForm" modelAttribute="user" action="${ctx}/sys/user/infoEdit" method="post" class="form">
    <div class="tabs-container">
        <table class="table well">
            <tr>
                <td><label class="pull-right asterisk">姓名</label></td>
                <td>
                    <form:input path="name" htmlEscape="false" maxlength="50" class="form-control required"/>
                </td>
            </tr>
            <tr>
                <td><label class="pull-right">邮箱</label></td>
                <td>
                    <form:input path="email" htmlEscape="false" maxlength="50" class="form-control email"/>
                </td>
            </tr>
            <tr>
                <td><label class="pull-right">电话</label></td>
                <td>
                    <form:input path="phone" htmlEscape="false" class="form-control" maxlength="50"/>
                </td>
            </tr>
            <tr>
                <td><label class="pull-right">手机</label></td>
                <td>
                    <form:input path="mobile" class="form-control" htmlEscape="false" maxlength="50"/>
                </td>
            </tr>
            <tr>
                <td><label class="pull-right">手机</label></td>
                <td>
                    <form:input path="remarks" class="form-control" htmlEscape="false" maxlength="200"/>
                </td>
            </tr>
        </table>
    </div>
</form:form>
</body>
</html>