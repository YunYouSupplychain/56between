<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>用户信息管理</title>
    <meta name="decorator" content="ani"/>
    <%@ include file="tmAppUserInfoForm.js" %>
</head>
<body>
<div class="hide">
    <input type="hidden" id="customerType" value="OWNER">
</div>
<form:form id="inputForm" modelAttribute="tmAppUserInfoEntity" class="form-horizontal">
    <form:hidden path="id"/>
    <form:hidden path="userId"/>
    <form:hidden path="recVer"/>
    <form:hidden path="baseOrgId"/>
    <form:hidden path="isCustomer"/>
    <form:hidden path="transportObjCode"/>
    <form:hidden path="transportObjName"/>
    <form:hidden path="isSafetyChecker"/>
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
                        <td><label class="pull-right"><font color="red">*</font>登录名：</label></td>
                        <td>
                            <form:input path="loginName" htmlEscape="false" class="form-control required" maxlength="64"/>
                        </td>
                        <td><label class="pull-right"><font color="red">*</font>密码：</label></td>
                        <td>
                            <form:input path="password" htmlEscape="false" class="form-control required" maxlength="64"/>
                        </td>
                        <td><label class="pull-right"><font color="red">*</font>姓名：</label></td>
                        <td>
                            <form:input path="name" htmlEscape="false" class="form-control required" maxlength="64"/>
                        </td>
                        <td><label class="pull-right">状态：</label></td>
                        <td>
                            <form:select path="status" class="form-control" disabled="true">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('TMS_APP_USER_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                    </tr>
                    <tr>
                        <td><label class="pull-right"><font color="red">*</font>机构：</label></td>
                        <td>
                            <sys:grid title="选择机构" url="${ctx}/tms/app/tmAppUserInfo/office/grid"
                                      cssClass="form-control required" readonly="true"
                                      fieldId="orgId" fieldName="orgId"
                                      fieldKeyName="id" fieldValue="${tmAppUserInfoEntity.orgId}"
                                      displayFieldId="orgName" displayFieldName="orgName"
                                      displayFieldKeyName="name" displayFieldValue="${tmAppUserInfoEntity.orgName}"
                                      fieldLabels="机构编码|机构名称" fieldKeys="code|name"
                                      searchLabels="机构编码|机构名称" searchKeys="code|name"
                                      afterSelect="orgSelect"/>
                        </td>
                        <td><label class="pull-right">是否运输人员：</label></td>
                        <td>
                            <input id="isDriver" name="isDriver" type="checkbox" htmlEscape="false" class="myCheckbox" onclick="isDriverChange(this.checked, '#isDriver')"/>
                        </td>
                        <td><label class="pull-right">运输人员：</label></td>
                        <td>
                            <sys:grid title="司机" url="${ctx}/tms/app/tmAppUserInfo/driverGrid"
                                      cssClass="form-control" readonly="true" disabled="true"
                                      fieldId="driver" fieldName="driver"
                                      fieldKeyName="code" fieldValue="${tmAppUserInfoEntity.driver}"
                                      displayFieldId="driverName" displayFieldName="driverName"
                                      displayFieldKeyName="name" displayFieldValue="${tmAppUserInfoEntity.driverName}"
                                      fieldLabels="编码|姓名|承运商" fieldKeys="code|name|carrierName"
                                      searchLabels="编码|姓名" searchKeys="code|name"
                                      queryParams="orgId" queryParamValues="baseOrgId"/>
                        </td>
                        <td><label class="pull-right">微信ID：</label></td>
                        <td>
                            <form:input path="def1" htmlEscape="false" class="form-control" readonly="true" maxlength="64"/>
                        </td>
                    </tr>
                    <tr>
                        <td><label class="pull-right">所属公司：</label></td>
                        <td>
                            <form:input path="def2" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td><label class="pull-right">自定义3：</label></td>
                        <td>
                            <form:input path="def3" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td><label class="pull-right">自定义4：</label></td>
                        <td>
                            <form:input path="def4" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td><label class="pull-right">自定义5：</label></td>
                        <td>
                            <form:input path="def5" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
</form:form>
</body>
</html>