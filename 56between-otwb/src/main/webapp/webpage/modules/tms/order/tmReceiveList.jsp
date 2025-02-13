<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>网点收货管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@ include file="/webpage/include/bootstraptable.jsp" %>
    <script type="text/javascript" src="${ctxStatic}/tms/common.js"></script>
    <%@ include file="tmReceiveList.js" %>
</head>
<body>
<div class="hidden">
    <input type="hidden" id="principalType" value="CUSTOMER"/>
    <input type="hidden" id="customerType" value="OWNER"/>
    <input type="hidden" id="settlementType" value="SETTLEMENT"/>
    <input type="hidden" id="outletType" value="OUTLET">
    <input type="hidden" id="carrierType" value="CARRIER">
    <input type="hidden" id="shipType" value="SHIP">
    <input type="hidden" id="consigneeType" value="CONSIGNEE">
</div>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">网点收货列表</h3>
        </div>
        <div class="panel-body">
            <sys:message content="${message}"/>
            <!-- 搜索 -->
            <div class="accordion-group">
                <div id="collapseTwo" class="accordion-body collapse in">
                    <div class="accordion-inner">
                        <form:form id="searchForm" modelAttribute="tmReceiveEntity" class="form">
                            <form:hidden path="baseOrgId"/>
                            <table class="table well">
                                <tr>
                                    <td class="width-10"><label class="pull-right">标签号</label></td>
                                    <td class="width-15">
                                        <form:input path="labelNo" htmlEscape="false" maxlength="20" class=" form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">运输单号</label></td>
                                    <td class="width-15">
                                        <form:input path="transportNo" htmlEscape="false" maxlength="20" class=" form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">客户订单</label></td>
                                    <td class="width-15">
                                        <form:input path="customerNo" htmlEscape="false" maxlength="20" class=" form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">派车单号</label></td>
                                    <td class="width-15">
                                        <form:input path="dispatchNo" htmlEscape="false" maxlength="20" class=" form-control"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="width-10"><label class="pull-right">发货人单位</label></td>
                                    <td class="width-15">
                                        <sys:grid title="发货方" url="${ctx}/tms/basic/tmTransportObj/grid"
                                                  fieldId="shipCode" fieldName="shipCode" fieldKeyName="transportObjCode"
                                                  displayFieldId="shipName" displayFieldName="shipName" displayFieldKeyName="transportObjName"
                                                  fieldLabels="发货方编码|发货方名称" fieldKeys="transportObjCode|transportObjName"
                                                  searchLabels="发货方编码|发货方名称" searchKeys="transportObjCode|transportObjName"
                                                  queryParams="transportObjType|orgId" queryParamValues="shipType|baseOrgId"
                                                  cssClass="form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">发货城市</label></td>
                                    <td colspan="1">
                                        <sys:area id="shipCityId" name="shipCityId"  labelName="shipCity"
                                                  cssClass="form-control" allowSearch="true" showFullName="true"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">发货人</label></td>
                                    <td class="width-15">
                                        <form:input path="shipper" htmlEscape="false" class="form-control "/>
                                    </td>
                                    <td style="width:8%;"><label class="pull-right">发货地址</label></td>
                                    <td colspan="1">
                                        <form:input path="shipAddress" htmlEscape="false" class="form-control "/>
                                    </td>

                                </tr>
                                <tr>
                                    <td class="width-10"><label class="pull-right">收货人单位</label></td>
                                    <td class="width-15">
                                        <sys:grid title="收货方" url="${ctx}/tms/basic/tmTransportObj/grid"
                                                  fieldId="consigneeCode" fieldName="consigneeCode" fieldKeyName="transportObjCode"
                                                  displayFieldId="consigneeName" displayFieldName="consigneeName" displayFieldKeyName="transportObjName"
                                                  fieldLabels="收货方编码|收货方名称" fieldKeys="transportObjCode|transportObjName"
                                                  searchLabels="收货方编码|收货方名称" searchKeys="transportObjCode|transportObjName"
                                                  queryParams="transportObjType|orgId" queryParamValues="consigneeType|baseOrgId"
                                                  cssClass="form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">目的地城市</label></td>
                                    <td colspan="1">
                                        <sys:area id="consigneeCityId" name="consigneeCityId" labelName="consigneeCity"
                                                  cssClass="form-control" allowSearch="true" showFullName="true"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">收货人</label></td>
                                    <td class="width-15">
                                        <form:input path="consignee" htmlEscape="false" class="form-control "/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">收货地址</label></td>
                                    <td colspan="1">
                                        <form:input path="consigneeAddress" htmlEscape="false" class="form-control "/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="width-10"><label class="pull-right">发货网点</label></td>
                                    <td class="width-15">
                                        <sys:grid title="网点" url="${ctx}/tms/basic/tmTransportObj/grid"
                                                  fieldId="nowOutletCode" fieldName="nowOutletCode" fieldKeyName="transportObjCode"
                                                  displayFieldId="nowOutletName" displayFieldName="nowOutletName" displayFieldKeyName="transportObjName"
                                                  fieldLabels="网点编码|网点名称" fieldKeys="transportObjCode|transportObjName"
                                                  searchLabels="网点编码|网点名称" searchKeys="transportObjCode|transportObjName"
                                                  queryParams="transportObjType|orgId" queryParamValues="outletType|baseOrgId"
                                                  cssClass="form-control"/>
                                    </td>
                                    <td class="width-10"></td>
                                    <td class="width-15"></td>
                                    <td class="width-10"></td>
                                    <td class="width-15"></td>
                                    <td class="width-10"></td>
                                    <td class="width-15"></td>
                                </tr>
                            </table>
                        </form:form>
                    </div>
                </div>
            </div>
            <!-- 工具栏 -->
            <div id="toolbar">
                <shiro:hasPermission name="order:tmReceive:receive">
                    <button id="receive" class="btn btn-primary" disabled onclick="receive()">收货</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="order:tmReceive:receiveCondition">
                    <button id="receiveCondition" class="btn btn-primary" disabled onclick="receiveCondition()">按条件收货</button>
                </shiro:hasPermission>
                <a id="search" class="btn btn-primary"><i class="fa fa-search"></i> 查询</a>
                <a id="reset" class="btn btn-primary"><i class="fa fa-refresh"></i> 重置</a>
            </div>
            <!-- 表格 -->
            <table id="tmReceiveTable" data-toolbar="#toolbar" class="text-nowrap"></table>
        </div>
    </div>
</div>
</body>
</html>