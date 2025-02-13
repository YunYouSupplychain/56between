<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>结算对象管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="sysBmsSettleObjectList.js" %>
</head>
<body>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">结算对象列表</h3>
        </div>
        <div class="panel-body">
            <sys:message content="${message}"/>
            <!-- 搜索 -->
            <div class="accordion-group">
                <div id="collapseTwo" class="accordion-body collapse">
                    <div class="accordion-inner">
                        <form:form id="searchForm" modelAttribute="sysBmsSettleObject" class="form form-horizontal well clearfix">
                            <table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
                                <tr>
                                    <td style="width:10%;vertical-align:middle;"><label class="label-item single-overflow pull-right" title="结算对象代码：">结算对象代码：</label></td>
                                    <td style="width:15%;vertical-align:middle;">
                                        <form:input path="settleObjectCode" htmlEscape="false" class="form-control "/>
                                    </td>
                                    <td style="width:10%;vertical-align:middle;"><label class="label-item single-overflow pull-right" title="结算对象名称：">结算对象名称：</label></td>
                                    <td style="width:15%;vertical-align:middle;">
                                        <form:input path="settleObjectName" htmlEscape="false" class="form-control "/>
                                    </td>
                                    <td style="width:10%;vertical-align:middle;"><label class="label-item single-overflow pull-right" title="结算类别：">结算类别：</label></td>
                                    <td style="width:15%;vertical-align:middle;">
                                        <form:select path="settleCategory" class="form-control m-b">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('BMS_SETTLEMENT_CATEGORY')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                    <td style="width:10%;vertical-align:middle;"><label class="label-item single-overflow pull-right" title="结算方式：">结算方式：</label></td>
                                    <td style="width:15%;vertical-align:middle;">
                                        <form:select path="settleType" class="form-control m-b">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('BMS_SETTLEMENT_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                </tr>
                            </table>
                        </form:form>
                    </div>
                </div>
            </div>
            <!-- 工具栏 -->
            <div id="toolbar">
                <shiro:hasPermission name="sys:common:bms:settleObject:add">
                    <a id="add" class="btn btn-primary" onclick="add()"> 新建</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="sys:common:bms:settleObject:edit">
                    <button id="edit" class="btn btn-primary" disabled onclick="edit()"> 修改</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="sys:common:bms:settleObject:del">
                    <button id="remove" class="btn btn-danger" disabled onclick="deleteAll()"> 删除</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="sys:common:bms:settleObject:sync">
                    <button id="syncSelect" class="btn btn-primary" onclick="syncSelect()"> 同步选择数据</button>
                    <button id="syncAll" class="btn btn-primary" onclick="syncAll()"> 同步全部数据</button>
                </shiro:hasPermission>
                <a id="search" class="btn btn-primary"><i class="fa fa-search"></i> 查询</a>
                <a id="reset" class="btn btn-primary"><i class="fa fa-refresh"></i> 重置</a>
                <a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
                    <i class="fa fa-search"></i> 检索
                </a>
            </div>
            <!-- 表格 -->
            <table id="sysBmsSettleObjectTable" data-toolbar="#toolbar" class="text-nowrap"></table>
        </div>
    </div>
</div>
</body>
</html>