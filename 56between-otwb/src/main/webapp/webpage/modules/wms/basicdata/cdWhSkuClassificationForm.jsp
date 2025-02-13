<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>商品分类管理</title>
    <meta name="decorator" content="ani"/>
    <%@ include file="/webpage/include/bootstraptable.jsp" %>
    <%@ include file="cdWhSkuClassificationForm.js" %>
</head>
<body>
<form:form id="inputForm" modelAttribute="cdWhSkuClassification" class="form">
    <form:hidden path="id"/>
    <form:hidden path="orgId"/>
    <form:hidden path="delFlag"/>
    <form:hidden path="recVer"/>
    <sys:message content="${message}"/>
    <div class="tabs-container">
        <table class="table well">
            <tr>
                <td><label class="pull-right asterisk">编码</label></td>
                <td>
                    <form:input path="code" htmlEscape="false" class="form-control required" maxlength="35"/>
                </td>
            </tr>
            <tr>
                <td><label class="pull-right asterisk">名称</label></td>
                <td>
                    <form:input path="name" htmlEscape="false" class="form-control required" maxlength="64"/>
                </td>
            </tr>
        </table>
    </div>
</form:form>
</body>
</html>