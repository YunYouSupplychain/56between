<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>商品分类管理</title>
    <meta name="decorator" content="ani"/>
    <%@ include file="/webpage/include/bootstraptable.jsp" %>
    <%@ include file="sysCommonSkuClassificationForm.js" %>
</head>
<body class="bg-white">
<form:form id="inputForm" modelAttribute="sysCommonSkuClassification" class="form-horizontal">
    <form:hidden path="id"/>
    <form:hidden path="dataSet"/>
    <form:hidden path="delFlag"/>
    <form:hidden path="recVer"/>
    <table class="table">
        <tr>
            <td><label class="pull-right"><font color="red">*</font>编码</label></td>
            <td>
                <form:input path="code" htmlEscape="false" class="form-control required" maxlength="35"/>
            </td>
        </tr>
        <tr>
            <td><label class="pull-right"><font color="red">*</font>名称</label></td>
            <td>
                <form:input path="name" htmlEscape="false" class="form-control required" maxlength="64"/>
            </td>
        </tr>
    </table>
</form:form>
</body>
</html>