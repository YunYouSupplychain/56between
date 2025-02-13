<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>装车单管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="banQinWmLdHeaderList.js" %>
</head>
<body>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">装车单列表</h3>
        </div>
        <div class="panel-body">
            <sys:message content="${message}"/>
            <!-- 搜索 -->
            <div class="accordion-group">
                <div id="collapseTwo" class="accordion-body collapse">
                    <div class="accordion-inner">
                        <form:form id="searchForm" modelAttribute="banQinWmLdEntity" class="form">
                            <table class="table well">
                                <tbody>
                                <tr>
                                    <td class="width-10"><label class="pull-right">装车单号</label></td>
                                    <td class="width-15">
                                        <form:input path="ldNo" htmlEscape="false" maxlength="32" class=" form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">出库单号</label></td>
                                    <td class="width-15">
                                        <form:input path="soNo" htmlEscape="false" maxlength="32" class=" form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">状态</label></td>
                                    <td class="width-15">
                                        <form:select path="status" class="form-control m-b">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('SYS_WM_LD_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                    <td class="width-10"><label class="pull-right">出库单状态</label></td>
                                    <td class="width-15">
                                        <form:select path="soStatus" class="form-control m-b">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('SYS_WM_ALLOC_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="width-10"><label class="pull-right">装车单类型</label></td>
                                    <td class="width-15">
                                        <form:select path="ldType" class="form-control m-b">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('SYS_WM_LD_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                    <td class="width-10"><label class="pull-right">车牌号</label></td>
                                    <td class="width-15">
                                        <form:input path="vehicleNo" htmlEscape="false" maxlength="64" class="form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">车型</label></td>
                                    <td class="width-15">
                                        <form:select path="vehicleType" class="form-control m-b">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('TMS_CAR_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                    <td class="width-10"><label class="pull-right">驾驶员</label></td>
                                    <td class="width-15">
                                        <form:input path="driver" htmlEscape="false" maxlength="64" class="form-control"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="width-10"><label class="pull-right">预计装车时间从from</label></td>
                                    <td class="width-15">
                                        <div class='input-group date' id='fmLdTimeFm'>
                                            <input type='text' name="fmLdTimeFm" class="form-control"/>
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                        </div>
                                    </td>
                                    <td class="width-10"><label class="pull-right">预计装车时间从to</label></td>
                                    <td class="width-15">
                                        <div class='input-group date' id='fmLdTimeTo'>
                                            <input type='text' name="fmLdTimeTo" class="form-control"/>
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                        </div>
                                    </td>
                                    <td class="width-10"><label class="pull-right">预计装车时间到from</label></td>
                                    <td class="width-15">
                                        <div class='input-group date' id='toLdTimeFm'>
                                            <input type='text' name="toLdTimeFm" class="form-control"/>
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                        </div>
                                    </td>
                                    <td class="width-10"><label class="pull-right">预计装车时间到to</label></td>
                                    <td class="width-15">
                                        <div class='input-group date' id='toLdTimeTo'>
                                            <input type='text' name="toLdTimeTo" class="form-control"/>
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="width-10"><label class="pull-right">装车交接时间从</label></td>
                                    <td class="width-15">
                                        <div class='input-group date' id='deliverTimeFm'>
                                            <input type='text' name="deliverTimeFm" class="form-control"/>
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                        </div>
                                    </td>
                                    <td class="width-10"><label class="pull-right">装车交接时间到</label></td>
                                    <td class="width-15">
                                        <div class='input-group date' id='deliverTimeTo'>
                                            <input type='text' name="deliverTimeTo" class="form-control"/>
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                        </div>
                                    </td>
                                    <td class="width-10"><label class="pull-right">创建时间从</label></td>
                                    <td class="width-15">
                                        <div class='input-group date' id='createDateFm'>
                                            <input type='text' name="createDateFm" class="form-control"/>
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                        </div>
                                    </td>
                                    <td class="width-10"><label class="pull-right">创建时间到</label></td>
                                    <td class="width-15">
                                        <div class='input-group date' id='createDateTo'>
                                            <input type='text' name="createDateTo" class="form-control"/>
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                        </div>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </form:form>
                    </div>
                </div>
            </div>
            <!-- 工具栏 -->
            <div id="toolbar">
                <shiro:hasPermission name="outbound:banQinWmLdHeader:add">
                    <button id="add" class="btn btn-primary" onclick="add()"> 新建</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="outbound:banQinWmLdHeader:edit">
                    <button id="edit" class="btn btn-primary" disabled onclick="edit()"> 修改</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="outbound:banQinWmLdHeader:del">
                    <button id="remove" class="btn btn-danger" disabled onclick="deleteAll()"> 删除</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="outbound:banQinWmLdHeader:shipment">
                    <button id="shipment" class="btn btn-primary" disabled onclick="shipment()">发货确认</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="outbound:banQinWmLdHeader:loadDelivery">
                    <button id="loadDelivery" class="btn btn-primary" disabled onclick="loadDelivery()">装车交接</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="outbound:banQinWmLdHeader:cancelShipment">
                    <button id="cancelShipment" class="btn btn-primary" disabled onclick="cancelShipment()">取消发货</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="outbound:banQinWmLdHeader:cancelDelivery">
                    <button id="cancelDelivery" class="btn btn-primary" disabled onclick="cancelDelivery()">取消交接</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="outbound:banQinWmLdHeader:closeOrder">
                    <button id="closeOrder" class="btn btn-primary" disabled onclick="closeOrder()">关闭订单</button>
                </shiro:hasPermission>
                <div class="btn-group">
                    <button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown">打印<span class="caret"></span></button>
                    <ul class="dropdown-menu">
                        <li><a id="printStoreCheckAcceptOrder" onclick="printStoreCheckAcceptOrder()">打印门店验收单</a></li>
                        <shiro:hasPermission name="outbound:banQinWmLdHeader:printLdOrder">
                            <li><a id="printLdOrder" onclick="printLdOrder()">打印装车清单</a></li>
                        </shiro:hasPermission>
                    </ul>
                </div>
                <a id="search" class="btn btn-primary"><i class="fa fa-search"></i> 查询</a>
                <a id="reset" class="btn btn-primary"><i class="fa fa-refresh"></i> 重置</a>
                <a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
                    <i class="fa fa-search"></i> 检索
                </a>
            </div>
            <!-- 表格 -->
            <table id="banQinWmLdHeaderTable" data-toolbar="#toolbar" class="text-nowrap"></table>
        </div>
    </div>
</div>
</body>
</html>