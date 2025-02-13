<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>需求计划管理</title>
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <script type="text/javascript" src="${ctxStatic}/tms/common.js"></script>
    <%@include file="tmVehicleSafetyCheckForm.js" %>
</head>
<body>
<div class="hidden">
    <input type="hidden" id="principalType" value="CUSTOMER">
    <input type="hidden" id="settlementType" value="SETTLEMENT"/>
    <input type="hidden" id="customerType" value="OWNER">
    <input type="hidden" id="outletType" value="OUTLET">
    <input type="hidden" id="carrierType" value="CARRIER">
    <input type="hidden" id="shipType" value="SHIP">
    <input type="hidden" id="consigneeType" value="CONSIGNEE">
</div>
<div style="margin-left: 10px;">
    <shiro:hasPermission name="tms:order:tmVehicleSafetyCheck:edit">
        <a id="save" class="btn btn-primary btn-sm" disabled onclick="save()"> 保存</a>
    </shiro:hasPermission>
</div>
<form:form id="inputForm" modelAttribute="tmVehicleSafetyCheckEntity" class="form-horizontal">
    <form:hidden path="id"/>
    <form:hidden path="orgId"/>
    <form:hidden path="baseOrgId"/>
    <form:hidden path="recVer"/>
    <sys:message content="${message}"/>
    <%--基本信息--%>
    <div class="tabs-container" style="max-height: 300px;">
        <ul class="nav nav-tabs">
            <li class="active"><a data-toggle="tab" href="#basicInfo" aria-expanded="true">基本信息</a></li>
        </ul>
        <div class="tab-content">
            <div id="basicInfo" class="tab-pane fade in active">
                <table class="table table-bordered">
                    <tr>
                        <td style="width:8%;"><label class="pull-right"><font color="red">*</font>检查日期：</label></td>
                        <td style="width:12%;">
                            <div class='input-group form_datetime' id='checkDate'>
                                <input type='text' name="checkDate" class="form-control required"
                                       value="<fmt:formatDate value="${tmVehicleSafetyCheckEntity.checkDate}" pattern="yyyy-MM-dd"/>"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>
                        <td style="width:8%;"><label class="pull-right">天气情况：</label></td>
                        <td colspan="5">
                            <sys:checkbox id="weatherCondition" name="weatherCondition" items="${fns:getDictList('TMS_WEATHER_CONDITION')}"
                                          values="${tmVehicleSafetyCheckEntity.weatherCondition}" cssClass="i-checks required"/>
                        </td>
                        <td style="width:8%;"><label class="pull-right">气温：</label></td>
                        <td style="width:12%;">
                            <form:input path="airTemperature" htmlEscape="false" class="form-control " readonly="true"/>
                        </td>
                    </tr>
                    <tr>
                        <td style="width:8%;"><label class="pull-right">车牌号：</label></td>
                        <td style="width:12%;">
                            <form:input path="vehicleNo" htmlEscape="false" class="form-control " readonly="true"/>
                        </td>
                        <td style="width:8%;"><label class="pull-right">挂车牌号：</label></td>
                        <td style="width:12%;">
                            <form:input path="trailerNo" htmlEscape="false" class="form-control " readonly="true"/>
                        </td>
                        <td style="width:8%;"><label class="pull-right">核载吨位：</label></td>
                        <td style="width:12%;">
                            <form:input path="certifiedTonnage" htmlEscape="false" class="form-control " readonly="true"/>
                        </td>
                        <td style="width:8%;"><label class="pull-right">类项：</label></td>
                        <td style="width:12%;">
                            <form:input path="classItem" htmlEscape="false" class="form-control " readonly="true"/>
                        </td>
                    </tr>
                    <tr>
                        <td style="width:8%;"><label class="pull-right"><font color="red">*</font>出车时间：</label></td>
                        <td style="width:12%;">
                            <div class='input-group form_datetime' id='departureTime'>
                                <input type='text' name="departureTime" class="form-control required"
                                       value="<fmt:formatDate value="${tmVehicleSafetyCheckEntity.departureTime}" pattern="HH:mm:ss"/>"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>
                        <td style="width:8%;"><label class="pull-right">出车里程表数：</label></td>
                        <td style="width:12%;">
                            <form:input path="departureOdometerNumber" htmlEscape="false" class="form-control " readonly="true"/>
                        </td>
                        <td style="width:8%;"><label class="pull-right">回场时间：</label></td>
                        <td style="width:12%;">
                            <div class='input-group form_datetime' id='returnTime'>
                                <input type='text' name="returnTime" class="form-control"
                                       value="<fmt:formatDate value="${tmVehicleSafetyCheckEntity.returnTime}" pattern="HH:mm:ss"/>"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>
                        <td style="width:8%;"><label class="pull-right">回场里程表数：</label></td>
                        <td style="width:12%;">
                            <form:input path="returnOdometerNumber" htmlEscape="false" class="form-control " readonly="true"/>
                        </td>
                    </tr>
                    <tr>
                        <td style="width:8%;"><label class="pull-right">已检查项：</label></td>
                        <td colspan="9">
                            <sys:checkbox id="checkList" name="checkList" items="${fns:getDictList('TMS_SAFETY_CHECK_LIST')}"
                                          values="${tmVehicleSafetyCheckEntity.checkList}" cssClass="i-checks required"/>
                        </td>
                    </tr>
                    <tr>
                        <td style="width:8%;"><label class="pull-right">不符合项：</label></td>
                        <td style="width:12%;">
                            <form:input path="nonConformity" htmlEscape="false" class="form-control " readonly="true"/>
                        </td>
                        <td style="width:8%;"><label class="pull-right">确认结论：</label></td>
                        <td style="width: 12%;">
                            <form:select path="confirmConclusion" class="form-control ">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('TMS_SAFETY_CHECK_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td style="width:8%;"><label class="pull-right">安管员：</label></td>
                        <td style="width:12%;">
                            <form:input path="safetySign" htmlEscape="false" class="form-control " readonly="true"/>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
</form:form>
</body>
</html>