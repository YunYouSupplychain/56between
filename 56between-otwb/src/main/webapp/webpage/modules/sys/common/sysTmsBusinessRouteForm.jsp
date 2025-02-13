<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>业务路线信息管理</title>
    <meta name="decorator" content="ani"/>
    <%@ include file="/webpage/include/bootstraptable.jsp" %>
    <%@ include file="sysTmsBusinessRouteForm.js" %>
</head>
<body>
<form:form id="inputForm" modelAttribute="sysTmsBusinessRouteEntity" class="form-horizontal">
    <form:hidden path="id"/>
    <form:hidden path="recVer"/>
    <sys:message content="${message}"/>
    <table class="table table-bordered">
        <tr>
            <td style="width: 20%;"><label class="pull-right"><font color="red">*</font>编码：</label></td>
            <td style="width: 30%;">
                <form:input path="code" htmlEscape="false" class="form-control required" maxlength="64"/>
            </td>
            <td style="width: 20%;"><label class="pull-right"><font color="red">*</font>路线：</label></td>
            <td style="width: 30%;">
                <form:input path="name" htmlEscape="false" class="form-control required" maxlength="64"/>
            </td>
        </tr>
        <tr>
            <td style="width: 20%;"><label class="pull-right"><font color="red">*</font>数据套：</label></td>
            <td style="width: 30%;">
                <sys:grid title="选择数据套" url="${ctx}/sys/common/dataSet/grid" cssClass="form-control required"
                          fieldId="dataSet" fieldName="dataSet" fieldKeyName="code" fieldValue="${sysTmsBusinessRouteEntity.dataSet}"
                          displayFieldId="dataSetName" displayFieldName="dataSetName" displayFieldKeyName="name" displayFieldValue="${sysTmsBusinessRouteEntity.dataSetName}"
                          fieldLabels="编码|名称" fieldKeys="code|name"
                          searchLabels="编码|名称" searchKeys="code|name"/>
            </td>
            <td style="width: 20%;"><label class="pull-right">备注信息：</label></td>
            <td style="width: 30%;">
                <form:input path="remarks" htmlEscape="false" class="form-control " maxlength="255"/>
            </td>
        </tr>
    </table>
</form:form>
</body>
</html>