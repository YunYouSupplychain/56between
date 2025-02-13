<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>业务路线信息管理</title>
    <meta name="decorator" content="ani"/>
    <%@ include file="/webpage/include/bootstraptable.jsp" %>
    <script type="text/javascript" src="${ctxStatic}/tms/common.js"></script>
    <%@ include file="tmBusinessRouteForm.js" %>
</head>
<body class="bg-white">
<form:form id="inputForm" modelAttribute="tmBusinessRouteEntity" class="form-horizontal">
    <form:hidden path="id"/>
    <form:hidden path="orgId"/>
    <form:hidden path="recVer"/>
    <sys:message content="${message}"/>
    <table class="table">
        <tr>
            <td style="width: 20%;"><label class="pull-right"><font color="red">*</font>编码</label></td>
            <td style="width: 80%;">
                <form:input path="code" htmlEscape="false" class="form-control required" maxlength="64"/>
            </td>
        </tr>
        <tr>
            <td style="width: 20%;"><label class="pull-right"><font color="red">*</font>路线</label></td>
            <td style="width: 80%;">
                <form:input path="name" htmlEscape="false" class="form-control required" maxlength="64"/>
            </td>
        </tr>
        <tr>
            <td style="width: 20%;"><label class="pull-right">备注信息</label></td>
            <td>
                <form:input path="remarks" htmlEscape="false" class="form-control " maxlength="255"/>
            </td>
        </tr>
    </table>
</form:form>
</body>
</html>