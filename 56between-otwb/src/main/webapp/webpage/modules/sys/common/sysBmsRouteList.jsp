<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>路由管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="sysBmsRouteList.js" %>
</head>
<body>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">路由列表</h3>
        </div>
        <div class="panel-body">
            <sys:message showType="2" content="${message}"/>
            <!-- 搜索 -->
            <div class="accordion-group">
                <div id="collapseTwo" class="accordion-body collapse">
                    <div class="accordion-inner">
                        <form:form id="searchForm" modelAttribute="sysBmsRoute" class="form form-horizontal well clearfix">
                            <table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
                                <tr>
                                    <td class="width-10"><label class="label-item single-overflow pull-right">起始地编码：</label></td>
                                    <td class="width-15">
                                        <form:input path="startAreaCode" htmlEscape="false" class="form-control "/>
                                    </td>
                                    <td class="width-10"><label class="label-item single-overflow pull-right">起始地名称：</label></td>
                                    <td class="width-15">
                                        <form:input path="startAreaName" htmlEscape="false" class="form-control "/>
                                    </td>
                                    <td class="width-10"><label class="label-item single-overflow pull-right">目的地编码：</label></td>
                                    <td class="width-15">
                                        <form:input path="endAreaCode" htmlEscape="false" class="form-control "/>
                                    </td>
                                    <td class="width-10"><label class="label-item single-overflow pull-right">目的地名称：</label></td>
                                    <td class="width-15">
                                        <form:input path="endAreaName" htmlEscape="false" class="form-control "/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="width-10"><label class="label-item single-overflow pull-right">路由编码：</label></td>
                                    <td class="width-15">
                                        <form:input path="routeCode" htmlEscape="false" class="form-control "/>
                                    </td>
                                    <td class="width-10"><label class="label-item single-overflow pull-right">路由名称：</label></td>
                                    <td class="width-15">
                                        <form:input path="routeName" htmlEscape="false" class="form-control "/>
                                    </td>
                                    <td class="width-10"><label class="pull-right" title="数据套：">数据套：</label></td>
                                    <td class="width-15">
                                        <sys:grid title="选择数据套" url="${ctx}/sys/common/dataSet/grid" cssClass="form-control"
                                                  fieldId="dataSet" fieldName="dataSet" fieldKeyName="code"
                                                  displayFieldId="dataSetName" displayFieldName="dataSetName" displayFieldKeyName="name"
                                                  fieldLabels="编码|名称" fieldKeys="code|name"
                                                  searchLabels="编码|名称" searchKeys="code|name"/>
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
                <shiro:hasPermission name="sys:common:bms:route:add">
                    <a id="add" class="btn btn-primary" onclick="add()">新建</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="sys:common:bms:route:edit">
                    <button id="edit" class="btn btn-primary" disabled onclick="edit()">修改</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="sys:common:bms:route:del">
                    <button id="remove" class="btn btn-danger" disabled onclick="deleteAll()">删除</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="sys:common:bms:route:sync">
                    <button id="syncSelect" class="btn btn-primary" onclick="syncSelect()"> 同步选择数据</button>
                    <button id="syncAll" class="btn btn-primary" onclick="syncAll()"> 同步全部数据</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="sys:common:bms:route:import">
                    <button id="btnImport" class="btn btn-info"><i class="fa fa-file-excel-o"></i> 导入</button>
                    <div id="importBox" class="hide">
                        <form id="importForm" action="${ctx}/sys/common/bms/route/import" method="post" enctype="multipart/form-data"
                              style="padding-left:20px;text-align:center;"><br/>
                            <input id="uploadFile" name="file" type="file" style="width:330px"/>导入文件不能超过5M，仅允许导入“xls”或“xlsx”格式文件！<br/>　　
                        </form>
                    </div>
                </shiro:hasPermission>
                <a id="search" class="btn btn-primary"><i class="fa fa-search"></i> 查询</a>
                <a id="reset" class="btn btn-primary"><i class="fa fa-refresh"></i> 重置</a>
                <a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
                    <i class="fa fa-search"></i> 检索
                </a>
            </div>
            <!-- 表格 -->
            <table id="sysBmsRouteTable" data-toolbar="#toolbar" class="text-nowrap"></table>
        </div>
    </div>
</div>
</body>
</html>