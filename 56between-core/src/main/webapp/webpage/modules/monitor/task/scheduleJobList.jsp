<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>定时任务管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="scheduleJobList.js" %>
</head>
<body>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">定时任务列表</h3>
        </div>
        <div class="panel-body">
            <!-- 搜索 -->
            <div class="accordion-group">
                <div id="collapseTwo" class="accordion-body collapse">
                    <div class="accordion-inner">
                        <form:form id="searchForm" modelAttribute="scheduleJob">
                            <table class="table well">
                                <tbody>
                                <tr>
                                    <td style="width:10%;vertical-align:middle;"><label class="pull-right">任务名</label></td>
                                    <td style="width:15%;vertical-align:middle;">
                                        <form:input path="name" htmlEscape="false" maxlength="64" class="form-control"/>
                                    </td>
                                    <td style="width:10%;vertical-align:middle;"></td>
                                    <td style="width:15%;vertical-align:middle;"></td>
                                    <td style="width:10%;vertical-align:middle;"></td>
                                    <td style="width:15%;vertical-align:middle;"></td>
                                    <td style="width:10%;vertical-align:middle;"></td>
                                    <td style="width:15%;vertical-align:middle;"></td>
                                </tr>
                                </tbody>
                            </table>
                        </form:form>
                    </div>
                </div>
            </div>
            <!-- 工具栏 -->
            <div id="toolbar">
                <shiro:hasPermission name="monitor:scheduleJob:add">
                    <a id="add" class="btn btn-primary" onclick="add()"> 新建</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="monitor:scheduleJob:edit">
                    <button id="edit" class="btn btn-primary" disabled onclick="edit()"> 修改</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="monitor:scheduleJob:del">
                    <button id="remove" class="btn btn-danger" disabled onclick="deleteAll()"> 删除</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="monitor:scheduleJob:stop">
                    <button id="stop" class="btn btn-primary" disabled onclick="stop()"><i class="glyphicon"></i> 暂停</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="monitor:scheduleJob:resume">
                    <button id="resume" class="btn btn-primary" disabled onclick="resume()"><i class="glyphicon"></i> 启动</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="monitor:scheduleJob:startNow">
                    <button id="startNow" class="btn btn-default" disabled onclick="startNow()"><i class="glyphicon"></i> 立即运行一次</button>
                </shiro:hasPermission>
                <a id="search" class="btn btn-primary"><i class="fa fa-search"></i> 查询</a>
                <a id="reset" class="btn btn-primary"><i class="fa fa-refresh"></i> 重置</a>
                <a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
                    <i class="fa fa-search"></i> 检索
                </a>
            </div>
            <!-- 表格 -->
            <table id="table" class="text-nowrap" data-toolbar="#toolbar"></table>
        </div>
    </div>
</div>
</body>
</html>