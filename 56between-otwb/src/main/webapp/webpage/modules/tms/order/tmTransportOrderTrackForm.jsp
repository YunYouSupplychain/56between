<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>物流节点管理</title>
    <meta name="decorator" content="ani"/>
    <%@ include file="/webpage/include/bootstraptable.jsp" %>
    <script type="text/javascript" src="${ctxStatic}/tms/common.js"></script>
    <%@include file="tmTransportOrderTrackForm.js" %>
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
<div style="margin-left: 20px;">
    <shiro:hasPermission name="tms:order:transport:track:edit">
        <a id="save" class="btn btn-primary btn-sm" onclick="save()"> 保存</a>
    </shiro:hasPermission>
</div>
<form:form id="inputForm" modelAttribute="tmTransportOrderTrack" class="form">
    <form:hidden path="id"/>
    <form:hidden path="orgId"/>
    <form:hidden path="baseOrgId"/>
    <form:hidden path="recVer"/>
    <sys:message content="${message}"/>
    <div class="wrapper wrapper-content-my">
        <div class="panel panel-default">
            <div class="panel-heading">
                <h3 class="panel-title">基础信息</h3>
            </div>
            <div class="panel-body">
                <table class="table">
                    <tr>
                        <td class="width-10"><label class="pull-right asterisk">标签号</label></td>
                        <td class="width-15">
                            <form:input path="labelNo" htmlEscape="false" class="form-control required"/>
                        </td>
                        <td class="width-10"><label class="pull-right asterisk">运输单号</label></td>
                        <td class="width-15">
                            <form:input path="transportNo" htmlEscape="false" class="form-control required"/>
                        </td>
                        <td class="width-10"><label class="pull-right">客户单号</label></td>
                        <td class="width-15">
                            <form:input path="customerNo" htmlEscape="false" class="form-control "/>
                        </td>
                        <td class="width-10"><label class="pull-right">派车单号</label></td>
                        <td class="width-15">
                            <form:input path="dispatchNo" htmlEscape="false" class="form-control "/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">发货点</label></td>
                        <td class="width-15">
                            <sys:grid title="发货网点" url="${ctx}/tms/basic/tmTransportObj/grid"
                                      fieldId="deliverOutletCode" fieldName="deliverOutletCode"
                                      fieldKeyName="transportObjCode" fieldValue="${tmTransportOrderTrack.deliverOutletCode}"
                                      displayFieldId="deliverOutletName" displayFieldName="deliverOutletName"
                                      displayFieldKeyName="transportObjName" displayFieldValue="${tmTransportOrderTrack.deliverOutletName}"
                                      fieldLabels="网点编码|网点名称" fieldKeys="transportObjCode|transportObjName"
                                      searchLabels="网点编码|网点名称" searchKeys="transportObjCode|transportObjName"
                                      queryParams="transportObjType|orgId" queryParamValues="outletType|baseOrgId"
                                      cssClass="form-control"/>
                        </td>
                        <td class="width-10"><label class="pull-right">收货点</label></td>
                        <td class="width-15">
                            <sys:grid title="收货网点" url="${ctx}/tms/basic/tmTransportObj/grid"
                                      fieldId="receiveOutletCode" fieldName="receiveOutletCode"
                                      fieldKeyName="transportObjCode" fieldValue="${tmTransportOrderTrack.receiveOutletCode}"
                                      displayFieldId="receiveOutletName" displayFieldName="receiveOutletName"
                                      displayFieldKeyName="transportObjName" displayFieldValue="${tmTransportOrderTrack.receiveOutletName}"
                                      fieldLabels="网点编码|网点名称" fieldKeys="transportObjCode|transportObjName"
                                      searchLabels="网点编码|网点名称" searchKeys="transportObjCode|transportObjName"
                                      queryParams="transportObjType|orgId" queryParamValues="outletType|baseOrgId"
                                      cssClass="form-control"/>
                        </td>
                        <td class="width-10"><label class="pull-right">司机</label></td>
                        <td class="width-15">
                            <form:input path="driver" htmlEscape="false" class="form-control "/>
                        </td>
                        <td class="width-10"><label class="pull-right">电话</label></td>
                        <td class="width-15">
                            <form:input path="phone" htmlEscape="false" class="form-control "/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">操作人</label></td>
                        <td class="width-15">
                            <form:input path="opPerson" htmlEscape="false" class="form-control "/>
                        </td>
                        <td class="width-10"><label class="pull-right asterisk">操作时间</label></td>
                        <td class="width-15">
                            <div class='input-group form_datetime' id='opTime'>
                                <input type='text' name="opTime" class="form-control required"
                                       value="<fmt:formatDate value="${tmTransportOrderTrack.opTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>
                        <td class="width-10"><label class="pull-right asterisk">操作节点</label></td>
                        <td class="width-15">
                            <form:input path="opNode" htmlEscape="false" class="form-control required"/>
                        </td>
                        <td class="width-10"><label class="pull-right">数量</label></td>
                        <td class="width-15">
                            <form:input path="qty" htmlEscape="false" class="form-control "/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right asterisk">详细情况</label></td>
                        <td class="width-15" colspan="7">
                            <form:input path="operation" htmlEscape="false" class="form-control required"/>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
</form:form>
</body>
</html>