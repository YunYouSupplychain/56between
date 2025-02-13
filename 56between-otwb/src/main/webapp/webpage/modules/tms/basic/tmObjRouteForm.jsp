<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>业务对象路由管理</title>
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp"%>
    <script type="text/javascript" src="${ctxStatic}/tms/common.js"></script>
    <%@include file="tmObjRouteForm.js"%>
</head>
<body class="bg-white">
<div class="hide">
    <input type="hidden" id="outletType" value="OUTLET"/>
    <input type="hidden" id="carrierType" value="CARRIER"/>
</div>
<form:form id="inputForm" modelAttribute="tmObjRouteEntity" class="form-horizontal">
    <form:hidden path="id"/>
    <form:hidden path="orgId"/>
    <form:hidden path="delFlag"/>
    <form:hidden path="recVer"/>
    <table class="table">
        <tr>
            <td class="width-15"><label class="pull-right asterisk">起点对象编码</label></td>
            <td class="width-35">
                <sys:grid title="选择起点对象" url="${ctx}/tms/basic/tmTransportObj/grid" cssClass="form-control required"
                          fieldId="" fieldName="" fieldKeyName=""
                          displayFieldId="startObjCode" displayFieldName="startObjCode"
                          displayFieldKeyName="transportObjCode" displayFieldValue="${tmObjRouteEntity.startObjCode}"
                          fieldLabels="编码|名称|地址" fieldKeys="transportObjCode|transportObjName|address"
                          searchLabels="编码|名称" searchKeys="transportObjCode|transportObjName"
                          queryParams="transportObjType|orgId" queryParamValues="outletType|orgId"
                          afterSelect="startObjAfterSelect"/>
            </td>
            <td class="width-15"><label class="pull-right">起点对象名称</label></td>
            <td class="width-35">
                <form:input path="startObjName" htmlEscape="false" class="form-control" readonly="true"/>
            </td>
        </tr>
        <tr>
            <td class="width-15"><label class="pull-right asterisk">起点对象地址</label></td>
            <td class="width-35" colspan="3">
                <form:input path="startObjAddress" htmlEscape="false" class="form-control required" maxlength="255"/>
            </td>
        </tr>
        <tr>
            <td class="width-15"><label class="pull-right asterisk">终点对象编码</label></td>
            <td class="width-35">
                <sys:grid title="选择终点对象" url="${ctx}/tms/basic/tmTransportObj/grid" cssClass="form-control required"
                          fieldId="" fieldName="" fieldKeyName=""
                          displayFieldId="endObjCode" displayFieldName="endObjCode"
                          displayFieldKeyName="transportObjCode" displayFieldValue="${tmObjRouteEntity.endObjCode}"
                          fieldLabels="编码|名称|地址" fieldKeys="transportObjCode|transportObjName|address"
                          searchLabels="编码|名称" searchKeys="transportObjCode|transportObjName"
                          queryParams="transportObjType|orgId" queryParamValues="outletType|orgId"
                          afterSelect="endObjAfterSelect"/>
            </td>
            <td class="width-15"><label class="pull-right">终点对象名称</label></td>
            <td class="width-35">
                <form:input path="endObjName" htmlEscape="false" class="form-control" readonly="true"/>
            </td>
        </tr>
        <tr>
            <td class="width-15"><label class="pull-right asterisk">终点对象地址</label></td>
            <td class="width-35" colspan="3">
                <form:input path="endObjAddress" htmlEscape="false" class="form-control required" maxlength="255"/>
            </td>
        </tr>
        <tr>
            <td class="width-8"><label class="pull-right asterisk">承运商</label></td>
            <td class="width-12">
                <sys:grid title="选择承运商" url="${ctx}/tms/basic/tmTransportObj/grid" cssClass="form-control required"
                          fieldId="carrierCode" fieldName="carrierCode"
                          fieldKeyName="transportObjCode" fieldValue="${tmObjRouteEntity.carrierCode}"
                          displayFieldId="carrierName" displayFieldName="carrierName"
                          displayFieldKeyName="transportObjCode" displayFieldValue="${tmObjRouteEntity.carrierName}"
                          fieldLabels="编码|名称" fieldKeys="transportObjCode|transportObjName"
                          searchLabels="编码|名称" searchKeys="transportObjCode|transportObjName"
                          queryParams="transportObjType|orgId" queryParamValues="carrierType|orgId"/>
            </td>
            <td class="width-8"><label class="pull-right">审核状态</label></td>
            <td class="width-12">
                <form:select path="auditStatus" disabled="true" cssClass="form-control">
                    <form:option value="" label=""/>
                    <form:options items="${fns:getDictList('SYS_AUDIT_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                </form:select>
            </td>
        </tr>
        <tr>
            <td class="width-8"><label class="pull-right">里程</label></td>
            <td class="width-12">
                <form:input path="mileage" htmlEscape="false" class="form-control required" maxlength="255"/>
            </td>
        </tr>
    </table>
</form:form>
</body>
</html>