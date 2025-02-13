<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>计划订单信息管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@ include file="/webpage/include/bootstraptable.jsp" %>
    <script type="text/javascript" src="${ctxStatic}/tms/common.js"></script>
    <%@ include file="tmPreTransportOrderList.js" %>
</head>
<body>
<div class="hidden">
    <input type="hidden" id="principalType" value="CUSTOMER"/>
    <input type="hidden" id="settlementType" value="SETTLEMENT"/>
    <input type="hidden" id="customerType" value="OWNER"/>
    <input type="hidden" id="outletType" value="OUTLET"/>
    <input type="hidden" id="carrierType" value="CARRIER"/>
    <input type="hidden" id="shipType" value="SHIP"/>
    <input type="hidden" id="consigneeType" value="CONSIGNEE"/>
    <input type="hidden" id="baseOrgId"/>
</div>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">计划订单信息列表</h3>
        </div>
        <div class="panel-body">
            <sys:message content="${message}"/>
            <!-- 搜索 -->
            <div class="accordion-group">
                <div id="collapseTwo" class="accordion-body collapse">
                    <div class="accordion-inner">
                        <form:form id="searchForm" modelAttribute="tmPreTransportOrderEntity" class="form">
                            <table class="table well">
                                <tr>
                                    <td class="width-10"><label class="pull-right">运输单号</label></td>
                                    <td class="width-15">
                                        <form:input path="transportNo" htmlEscape="false" maxlength="20" class=" form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">订单时间从</label></td>
                                    <td class="width-15">
                                        <div class='input-group date' id='orderTimeFm'>
                                            <input type='text' name="orderTimeFm" class="form-control"/>
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                        </div>
                                    </td>
                                    <td class="width-10"><label class="pull-right">订单时间到</label></td>
                                    <td class="width-15">
                                        <div class='input-group date' id='orderTimeTo'>
                                            <input type='text' name="orderTimeTo" class="form-control"/>
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                        </div>
                                    </td>
                                    <td class="width-10"><label class="pull-right">订单类型</label></td>
                                    <td class="width-15">
                                        <form:select path="orderType" class="form-control m-b">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('TMS_TRANSPORT_ORDER_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="width-10"><label class="pull-right">客户单号</label></td>
                                    <td class="width-15">
                                        <form:input path="customerNo" htmlEscape="false" maxlength="20" class=" form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">委托方</label></td>
                                    <td class="width-15">
                                        <sys:grid title="委托方" url="${ctx}/tms/basic/tmTransportObj/grid"
                                                  fieldId="principalCode" fieldName="principalCode" fieldKeyName="transportObjCode"
                                                  displayFieldId="principalName" displayFieldName="principalName" displayFieldKeyName="transportObjName"
                                                  fieldLabels="委托方编码|委托方名称" fieldKeys="transportObjCode|transportObjName"
                                                  searchLabels="委托方编码|委托方名称" searchKeys="transportObjCode|transportObjName"
                                                  queryParams="transportObjType|orgId" queryParamValues="principalType|baseOrgId"
                                                  cssClass="form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">客户</label></td>
                                    <td class="width-15">
                                        <sys:grid title="客户" url="${ctx}/tms/basic/tmTransportObj/grid"
                                                  fieldId="customerCode" fieldName="customerCode" fieldKeyName="transportObjCode"
                                                  displayFieldId="customerName" displayFieldName="customerName" displayFieldKeyName="transportObjName"
                                                  fieldLabels="客户编码|客户名称" fieldKeys="transportObjCode|transportObjName"
                                                  searchLabels="客户编码|客户名称" searchKeys="transportObjCode|transportObjName"
                                                  queryParams="transportObjType|orgId" queryParamValues="customerType|baseOrgId"
                                                  cssClass="form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">调度计划单号</label></td>
                                    <td class="width-15">
                                        <form:input path="dispatchPlanNo" htmlEscape="false" maxlength="20" class=" form-control"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="width-10"><label class="pull-right">来源单号</label></td>
                                    <td class="width-15">
                                        <form:input path="sourceNo" htmlEscape="false" maxlength="20" class=" form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">数据来源</label></td>
                                    <td class="width-15">
                                        <form:select path="dataSource" class="form-control m-b">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('TMS_DATA_SOURCE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                    <td class="width-10"><label class="pull-right">是否已派车</label></td>
                                    <td class="width-15">
                                        <form:select path="hasDispatch" class="form-control m-b">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('SYS_YES_NO')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
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
                <shiro:hasPermission name="order:tmPreTransportOrder:add">
                    <a id="add" class="btn btn-primary" onclick="add()"> 新建</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="order:tmPreTransportOrder:edit">
                    <button id="edit" class="btn btn-primary" disabled onclick="edit()"> 修改</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="order:tmPreTransportOrder:del">
                    <button id="remove" class="btn btn-danger" disabled onclick="deleteAll()"> 删除</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="order:tmPreTransportOrder:copy">
                    <button id="copy" class="btn btn-primary" disabled onclick="copy()"> 复制</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="order:tmPreTransportOrder:print">
                    <div class="btn-group">
                        <button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown">打印<span class="caret"></span></button>
                        <ul class="dropdown-menu"></ul>
                    </div>
                </shiro:hasPermission>
                <a id="search" class="btn btn-primary"><i class="fa fa-search"></i> 查询</a>
                <a id="reset" class="btn btn-primary"><i class="fa fa-refresh"></i> 重置</a>
                <a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
                    <i class="fa fa-search"></i> 检索
                </a>
            </div>
            <table id="allTable" class="text-nowrap" data-toolbar="#toolbar"></table>
        </div>
    </div>
</div>
</body>
</html>