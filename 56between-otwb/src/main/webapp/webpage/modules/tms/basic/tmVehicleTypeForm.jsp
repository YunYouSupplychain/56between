<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>车型信息管理</title>
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <script type="text/javascript" src="${ctxStatic}/tms/common.js"></script>
    <%@include file="tmVehicleTypeForm.js" %>
</head>
<body class="bg-white">
<form:form id="inputForm" modelAttribute="tmVehicleType" class="form-horizontal">
    <form:hidden path="id"/>
    <form:hidden path="orgId"/>
    <form:hidden path="recVer"/>
    <table class="table">
        <tr>
            <td><label class="pull-right">编码</label></td>
            <td>
                <form:input path="code" htmlEscape="false" class="form-control" maxlength="35" readonly="true"/>
            </td>
        </tr>
        <tr>
            <td><label class="pull-right asterisk">名称</label></td>
            <td>
                <form:input path="name" htmlEscape="false" class="form-control required" maxlength="64"/>
            </td>
        </tr>
        <tr>
            <td><label class="pull-right">备注</label></td>
            <td>
                <form:input path="remarks" htmlEscape="false" class="form-control" maxlength="250"/>
            </td>
        </tr>
    </table>
</form:form>
</body>
</html>