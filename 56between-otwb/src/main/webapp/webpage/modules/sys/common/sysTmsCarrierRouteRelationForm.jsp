<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>承运商路由信息管理</title>
    <meta name="decorator" content="ani"/>
    <%@ include file="/webpage/include/bootstraptable.jsp" %>
    <%@ include file="sysTmsCarrierRouteRelationForm.js" %>
</head>
<body>
<form:form id="inputForm" modelAttribute="sysTmsCarrierRouteRelationEntity" class="form-horizontal">
    <form:hidden path="id"/>
    <form:hidden path="recVer"/>
    <form:hidden path="oldCarrierCode"/>
    <sys:message content="${message}"/>
    <%--基本信息--%>
    <div class="tabs-container" style="max-height: 250px;">
        <ul class="nav nav-tabs">
            <li class="active"><a data-toggle="tab" href="#basicInfo" aria-expanded="true">基本信息</a></li>
        </ul>
        <div class="tab-content">
            <div id="basicInfo" class="tab-pane fade in active">
                <table class="table table-bordered">
                    <tr>
                        <td style="width: 8%;"><label class="pull-right"><font color="red">*</font>编码：</label></td>
                        <td style="width: 12%;">
                            <form:input path="code" htmlEscape="false" class="form-control required" maxlength="64"/>
                        </td>
                        <td style="width: 8%;"><label class="pull-right"><font color="red">*</font>名称：</label></td>
                        <td style="width: 12%;">
                            <form:input path="name" htmlEscape="false" class="form-control required" maxlength="64"/>
                        </td>
                        <td style="width: 8%;"><label class="pull-right"><font color="red">*</font>承运商：</label></td>
                        <td style="width: 12%;">
                            <input type="hidden" id="transportObjType" value="CARRIER" />
                            <sys:grid title="承运商" url="${ctx}/sys/common/tms/transportObj/grid"
                                      fieldId="carrierCode" fieldName="carrierCode" fieldKeyName="transportObjCode" fieldValue="${sysTmsCarrierRouteRelationEntity.carrierCode}"
                                      displayFieldId="carrierName" displayFieldName="carrierName" displayFieldKeyName="transportObjName" displayFieldValue="${sysTmsCarrierRouteRelationEntity.carrierName}"
                                      fieldLabels="承运商编码|承运商名称" fieldKeys="transportObjCode|transportObjName"
                                      searchLabels="承运商编码|承运商名称" searchKeys="transportObjCode|transportObjName"
                                      queryParams="transportObjType|dataSet" queryParamValues="transportObjType|dataSet"
                                      cssClass="form-control required"/>
                        </td>
                        <td style="width: 8%;"><label class="pull-right">标准里程(km)：</label></td>
                        <td style="width: 12%;">
                            <form:input path="mileage" htmlEscape="false" class="form-control " onkeyup="bq.numberValidator(this, 6, 0)"/>
                        </td>
                        <td style="width: 8%;"><label class="pull-right">标准时效(h)：</label></td>
                        <td style="width: 12%;">
                            <form:input path="time" htmlEscape="false" class="form-control " onkeyup="bq.numberValidator(this, 2, 0)"/>
                        </td>
                    </tr>
                    <tr>
                        <td style="width: 8%;"><label class="pull-right">启用停用：</label></td>
                        <td style="width: 12%;">
                            <form:select path="delFlag" class="form-control " disabled="true">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('del_flag')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td style="width: 8%;"><label class="pull-right"><font color="red">*</font>起始地：</label></td>
                        <td colspan="3">
                            <sys:area id="originId" name="originId" value="${sysTmsCarrierRouteRelationEntity.originId}"
                                      labelName="origin" labelValue="${sysTmsCarrierRouteRelationEntity.origin}"
                                      cssClass="form-control required" allowSearch="true" showFullName="true"/>
                        </td>
                        <td style="width: 8%;"><label class="pull-right"><font color="red">*</font>目的地：</label></td>
                        <td colspan="3">
                            <sys:area id="destinationId" name="destinationId" value="${sysTmsCarrierRouteRelationEntity.destinationId}"
                                      labelName="destination" labelValue="${sysTmsCarrierRouteRelationEntity.destination}"
                                      cssClass="form-control required" allowSearch="true" showFullName="true"/>
                        </td>
                    </tr>
                    <tr>
                        <td width="8%"><label class="pull-right"><font color="red">*</font>数据套：</label></td>
                        <td width="12%">
                            <sys:grid title="选择数据套" url="${ctx}/sys/common/dataSet/grid" cssClass="form-control required"
                                      fieldId="dataSet" fieldName="dataSet" fieldKeyName="code" fieldValue="${sysTmsCarrierRouteRelationEntity.dataSet}"
                                      displayFieldId="dataSetName" displayFieldName="dataSetName" displayFieldKeyName="name" displayFieldValue="${sysTmsCarrierRouteRelationEntity.dataSetName}"
                                      fieldLabels="编码|名称" fieldKeys="code|name"
                                      searchLabels="编码|名称" searchKeys="code|name"/>
                        </td>
                        <td style="width: 8%;"><label class="pull-right">备注信息：</label></td>
                        <td colspan="7">
                            <form:input path="remarks" htmlEscape="false" class="form-control " maxlength="255"/>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
</form:form>
</body>
</html>