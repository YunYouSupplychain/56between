<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>承运商路由信息管理</title>
    <meta name="decorator" content="ani"/>
    <%@ include file="/webpage/include/bootstraptable.jsp" %>
    <script type="text/javascript" src="${ctxStatic}/tms/common.js"></script>
    <%@ include file="tmCarrierRouteRelationForm.js" %>
</head>
<body class="bg-white">
<form:form id="inputForm" modelAttribute="tmCarrierRouteRelationEntity" class="form-horizontal">
    <form:hidden path="id"/>
    <form:hidden path="orgId"/>
    <form:hidden path="recVer"/>
    <form:hidden path="delFlag"/>
    <table class="table">
        <tr>
            <td class="width-10"><label class="pull-right asterisk">编码</label></td>
            <td class="width-15">
                <form:input path="code" htmlEscape="false" class="form-control required" maxlength="64"/>
            </td>
            <td class="width-10"><label class="pull-right asterisk">名称</label></td>
            <td class="width-15">
                <form:input path="name" htmlEscape="false" class="form-control required" maxlength="64"/>
            </td>
            <td class="width-10"><label class="pull-right asterisk">起始地</label></td>
            <td class="width-15">
                <sys:area id="originId" name="originId" value="${tmCarrierRouteRelationEntity.originId}"
                          labelName="origin" labelValue="${tmCarrierRouteRelationEntity.origin}"
                          cssClass="form-control required" allowSearch="true" showFullName="true"/>
            </td>
            <td class="width-10"><label class="pull-right asterisk">目的地</label></td>
            <td class="width-15">
                <sys:area id="destinationId" name="destinationId" value="${tmCarrierRouteRelationEntity.destinationId}"
                          labelName="destination" labelValue="${tmCarrierRouteRelationEntity.destination}"
                          cssClass="form-control required" allowSearch="true" showFullName="true"/>
            </td>
        </tr>
        <tr>
            <td class="width-10"><label class="pull-right asterisk">承运商</label></td>
            <td class="width-15">
                <input type="hidden" id="transportObjType" value="CARRIER" />
                <sys:grid title="承运商" url="${ctx}/tms/basic/tmTransportObj/grid"
                          fieldId="carrierCode" fieldName="carrierCode"
                          fieldKeyName="transportObjCode" fieldValue="${tmCarrierRouteRelationEntity.carrierCode}"
                          displayFieldId="carrierName" displayFieldName="carrierName"
                          displayFieldKeyName="transportObjName" displayFieldValue="${tmCarrierRouteRelationEntity.carrierName}"
                          fieldLabels="承运商编码|承运商名称" fieldKeys="transportObjCode|transportObjName"
                          searchLabels="承运商编码|承运商名称" searchKeys="transportObjCode|transportObjName"
                          queryParams="transportObjType|orgId" queryParamValues="transportObjType|orgId"
                          cssClass="form-control required"/>
            </td>
            <td class="width-10"><label class="pull-right">标准里程(km)</label></td>
            <td class="width-15">
                <form:input path="mileage" htmlEscape="false" class="form-control " onkeyup="bq.numberValidator(this, 6, 0)"/>
            </td>
            <td class="width-10"><label class="pull-right">标准时效(h)</label></td>
            <td class="width-15">
                <form:input path="time" htmlEscape="false" class="form-control " onkeyup="bq.numberValidator(this, 2, 0)"/>
            </td>
            <td class="width-10"></td>
            <td class="width-15"></td>
        </tr>
        <tr>
            <td class="width-10"><label class="pull-right">备注信息</label></td>
            <td colspan="7">
                <form:input path="remarks" htmlEscape="false" class="form-control " maxlength="255"/>
            </td>
        </tr>
    </table>
</form:form>
</body>
</html>