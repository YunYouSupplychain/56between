<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>业务对象信息管理</title>
    <meta name="decorator" content="ani"/>
    <script type="text/javascript" src="${ctxStatic}/tms/common.js"></script>
    <%@ include file="tmTransportObjForm.js" %>
</head>
<body class="bg-white">
<div class="hide">
    <input type="hidden" id="outletOfficeType" value="7"/>
    <input type="hidden" id="carrierOfficeType" value="8"/>
    <input type="hidden" id="settleType" value="SETTLEMENT"/>
</div>
<form:form id="inputForm" modelAttribute="tmTransportObjEntity" class="form-horizontal">
    <form:hidden path="id"/>
    <form:hidden path="orgId"/>
    <form:hidden path="recVer"/>
    <form:hidden path="delFlag"/>
    <form:hidden path="repairPrice"/>
    <form:hidden path="routeId"/>
    <form:hidden path="settleCode"/>
    <form:hidden path="classification"/>
    <form:hidden path="signBy"/>
    <table class="table">
        <tr>
            <td class="width-10"><label class="pull-right asterisk">业务对象编码</label></td>
            <td class="width-15">
                <form:input path="transportObjCode" htmlEscape="false" class="form-control required" maxlength="35"/>
            </td>
            <td class="width-10"><label class="pull-right asterisk">业务对象名称</label></td>
            <td class="width-15">
                <form:input path="transportObjName" htmlEscape="false" class="form-control required" maxlength="64"/>
            </td>
            <td class="width-10"><label class="pull-right">业务对象简称</label></td>
            <td class="width-15">
                <form:input path="transportObjShortName" htmlEscape="false" class="form-control" maxlength="64"/>
            </td>
            <td class="width-10"><label class="pull-right">品牌</label></td>
            <td class="width-15">
                <form:select path="brand" class="form-control">
                    <form:option value="" label=""/>
                    <form:options items="${fns:getDictList('TMS_TRANSPORT_OBJ_BRAND')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                </form:select>
            </td>
        </tr>
        <tr>
            <td class="width-10"><label class="pull-right">联系人</label></td>
            <td class="width-15">
                <form:input path="contact" htmlEscape="false" class="form-control" maxlength="20"/>
            </td>
            <td class="width-10"><label class="pull-right">联系电话</label></td>
            <td class="width-15">
                <form:input path="phone" htmlEscape="false" class="form-control" maxlength="20"/>
            </td>
            <td class="width-10"><label class="pull-right">传真</label></td>
            <td class="width-15">
                <form:input path="fax" htmlEscape="false" class="form-control" maxlength="20"/>
            </td>
            <td class="width-10"><label class="pull-right">电子邮箱</label></td>
            <td class="width-15">
                <form:input path="email" htmlEscape="false" class="form-control" maxlength="64"/>
            </td>
        </tr>
        <tr>
            <td class="width-10"><label class="pull-right">网址</label></td>
            <td class="width-15">
                <form:input path="url" htmlEscape="false" class="form-control" maxlength="64"/>
            </td>
            <td class="width-10"><label class="pull-right">统一码</label></td>
            <td class="width-15">
                <form:input path="unCode" htmlEscape="false" class="form-control" maxlength="64"/>
            </td>
            <td class="width-10"><label class="pull-right">承运商对应机构</label></td>
            <td class="width-15">
                <sys:grid title="选择机构" url="${ctx}/sys/office/companyData" cssClass="form-control"
                          fieldId="carrierMatchedOrgId" fieldName="carrierMatchedOrgId"
                          fieldKeyName="id" fieldValue="${tmTransportObjEntity.carrierMatchedOrgId}"
                          displayFieldId="carrierMatchedOrg" displayFieldName="carrierMatchedOrg"
                          displayFieldKeyName="name" displayFieldValue="${tmTransportObjEntity.carrierMatchedOrg}"
                          fieldLabels="机构编码|机构名称" fieldKeys="code|name"
                          searchLabels="机构编码|机构名称" searchKeys="code|name"
                          queryParams="id|type" queryParamValues="orgId|carrierOfficeType"/>
            </td>
            <td class="width-10"><label class="pull-right">网点对应机构</label></td>
            <td class="width-15">
                <sys:grid title="选择机构" url="${ctx}/sys/office/outletMatchedOrg" cssClass="form-control"
                          fieldId="outletMatchedOrgId" fieldName="outletMatchedOrgId"
                          fieldKeyName="id" fieldValue="${tmTransportObjEntity.outletMatchedOrgId}"
                          displayFieldId="outletMatchedOrg" displayFieldName="outletMatchedOrg"
                          displayFieldKeyName="name" displayFieldValue="${tmTransportObjEntity.outletMatchedOrg}"
                          fieldLabels="机构编码|机构名称" fieldKeys="code|name"
                          searchLabels="机构编码|机构名称" searchKeys="code|name"
                          queryParams="id|type" queryParamValues="orgId|outletOfficeType"/>
            </td>
        </tr>
        <tr>
            <td class="width-10"><label class="pull-right asterisk">业务对象类型</label></td>
            <td colspan="7">
                <sys:checkbox id="transportObjType" name="transportObjType" items="${fns:getDictList('TMS_TRANSPORT_OBJ_TYPE')}"
                              values="${tmTransportObjEntity.transportObjType}" cssClass="i-checks required"/>
            </td>
        </tr>
        <tr>
            <td class="width-10"><label class="pull-right">所属城市</label></td>
            <td class="width-15">
                <sys:area id="areaId" name="areaId" value="${tmTransportObjEntity.areaId}"
                          labelName="area" labelValue="${tmTransportObjEntity.area}"
                          cssClass="form-control" allowSearch="true" showFullName="true"/>
            </td>
            <td class="width-10"><label class="pull-right">详细地址</label></td>
            <td colspan="5">
                <form:input path="address" htmlEscape="false" class="form-control" maxlength="255"/>
            </td>
        </tr>
    </table>
</form:form>
</body>
</html>