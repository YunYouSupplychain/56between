<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>库存盘点管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="banQinWmCountHeaderList.js" %>
</head>
<body>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">库存盘点列表</h3>
        </div>
        <div class="panel-body">
            <sys:message content="${message}"/>
            <!-- 搜索 -->
            <div class="accordion-group">
                <div id="collapseTwo" class="accordion-body collapse">
                    <div class="accordion-inner">
                        <form:form id="searchForm" modelAttribute="banQinWmCountHeaderEntity" class="form">
                            <input id="orgId" name="orgName" type="hidden">
                            <table class="table well">
                                <tbody>
                                <tr>
                                    <td class="width-10">
                                        <label class="pull-right" title="盘点单号：">盘点单号</label>
                                    </td>
                                    <td class="width-15">
                                        <form:input path="countNo" htmlEscape="false" maxlength="64" class=" form-control"/>
                                    </td>
                                    <td class="width-10">
                                        <label class="pull-right" title="状态：">状态</label>
                                    </td>
                                    <td class="width-15">
                                        <form:select path="status" class="form-control m-b">
                                            <form:option value=""/>
                                            <form:options items="${fns:getDictList('SYS_WM_COUNT_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                    <td class="width-10">
                                        <label class="pull-right" title="盘点单类型：">盘点单类型</label>
                                    </td>
                                    <td class="width-15">
                                        <form:select path="countType" class="form-control m-b">
                                            <form:option value=""/>
                                            <form:options items="${fns:getDictList('SYS_WM_COUNT_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                    <td class="width-10">
                                        <label class="pull-right" title="盘点范围：">盘点范围</label>
                                    </td>
                                    <td class="width-15">
                                        <form:select path="countRange" class="form-control m-b">
                                            <form:option value=""/>
                                            <form:options items="${fns:getDictList('SYS_WM_COUNT_RANGE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="width-10">
                                        <label class="pull-right" title="盘点方式：">盘点方式</label>
                                    </td>
                                    <td class="width-15">
                                        <form:select path="countMode" class="form-control m-b">
                                            <form:option value=""/>
                                            <form:options items="${fns:getDictList('SYS_WM_COUNT_MODE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                    <td class="width-10">
                                        <label class="pull-right" title="盘点方法：">盘点方法</label>
                                    </td>
                                    <td class="width-15">
                                        <form:select path="countMethod" class="form-control m-b">
                                            <form:option value=""/>
                                            <form:options items="${fns:getDictList('SYS_WM_COUNT_METHOD')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                    <td class="width-10">
                                        <label class="pull-right" title="监盘人：">监盘人</label>
                                    </td>
                                    <td class="width-15">
                                        <form:input path="monitorOp" htmlEscape="false" maxlength="64" class="form-control"/>
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
                <shiro:hasPermission name="inventory:banQinWmCountHeader:add">
                    <a id="add" class="btn btn-primary" onclick="add()" title="库存盘点"> 新建</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="inventory:banQinWmCountHeader:edit">
                    <button id="edit" class="btn btn-primary" disabled onclick="edit()"> 修改</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="inventory:banQinWmCountHeader:del">
                    <button id="remove" class="btn btn-danger" disabled onclick="deleteAll()"> 删除</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="inventory:banQinWmCountHeader:createCount">
                    <button id="createCount" class="btn btn-primary" disabled onclick="createCount()">生成普通盘点任务</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="inventory:banQinWmCountHeader:createRecount">
                    <button id="createRecount" class="btn btn-primary" disabled onclick="createRecount()">生成复盘任务</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="inventory:banQinWmCountHeader:cancelCountTask">
                    <button id="cancelCountTask" class="btn btn-primary" disabled onclick="cancelCountTask()">取消盘点任务</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="inventory:banQinWmCountHeader:closeCount">
                    <button id="closeCount" class="btn btn-primary" disabled onclick="closeCount()">关闭盘点</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="inventory:banQinWmCountHeader:cancelCount">
                    <button id="cancelCount" class="btn btn-primary" disabled onclick="cancelCount()">取消盘点</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="inventory:banQinWmCountHeader:createAd">
                    <button id="createAd" class="btn btn-primary" disabled onclick="createAd()">生成调整单</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="inventory:banQinWmCountHeader:export">
                    <button id="export" class="btn btn-info" onclick="exportData()"><i class="fa fa-file-excel-o"></i> 导出</button>
                </shiro:hasPermission>
                <div class="btn-group">
                    <button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown">打印<span class="caret"></span></button>
                    <ul class="dropdown-menu">
                        <li><a id="printCountTask" onclick="printCountTask()">打印盘点任务</a></li>
                    </ul>
                </div>
                <a id="search" class="btn btn-primary"><i class="fa fa-search"></i> 查询</a>
                <a id="reset" class="btn btn-primary"><i class="fa fa-refresh"></i> 重置</a>
                <a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
                    <i class="fa fa-search"></i> 检索
                </a>
            </div>
            <!-- 表格 -->
            <table id="banQinWmCountHeaderTable" data-toolbar="#toolbar" class="text-nowrap"></table>
        </div>
    </div>
</div>
</body>
</html>