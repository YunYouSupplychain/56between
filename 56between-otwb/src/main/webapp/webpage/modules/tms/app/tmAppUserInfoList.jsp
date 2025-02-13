<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>用户信息管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@ include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="/webpage/include/treeview.jsp" %>
    <%@include file="tmAppUserInfoList.js" %>
</head>
<body>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">用户信息列表</h3>
        </div>
        <div class="panel-body">
            <sys:message content="${message}"/>

            <!-- 搜索 -->
            <div class="accordion-group">
                <div id="collapseTwo" class="accordion-body collapse">
                    <div class="accordion-inner">
                        <form:form id="searchForm" modelAttribute="tmAppUserInfoEntity" class="form form-horizontal well clearfix">
                            <table class="table table-striped table-bordered table-condensed">
                                <tr>
                                    <td style="width:10%;vertical-align:middle;"><label class="pull-right">登录名：</label></td>
                                    <td style="width:15%;vertical-align:middle;">
                                        <form:input path="loginName" htmlEscape="false" class=" form-control" maxlength="64" />
                                    </td>
                                    <td style="width:10%;vertical-align:middle;"><label class="pull-right">姓名：</label></td>
                                    <td style="width:15%;vertical-align:middle;">
                                        <form:input path="name" htmlEscape="false" class=" form-control" maxlength="64" />
                                    </td>
                                    <td style="width:10%;vertical-align:middle;"><label class="pull-right">状态：</label></td>
                                    <td style="width:15%;vertical-align:middle;">
                                        <form:select path="status" class="form-control m-b">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('TMS_APP_USER_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                </tr>
                            </table>
                            <div class="col-xs-12 col-sm-6 col-md-4">
                                <a id="search" class="btn btn-primary btn-rounded  btn-bordered btn-sm"><i class="fa fa-search"></i> 查询</a>
                                <a id="reset" class="btn btn-primary btn-rounded  btn-bordered btn-sm"><i class="fa fa-refresh"></i> 重置</a>
                            </div>
                        </form:form>
                    </div>
                </div>
            </div>
            <!-- 工具栏 -->
            <div id="toolbar">
                <shiro:hasPermission name="tms:app:tmAppUserInfo:add">
                    <a id="add" class="btn btn-primary" onclick="add()"> 新建</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="tms:app:tmAppUserInfo:edit">
                    <button id="edit" class="btn btn-primary" disabled onclick="edit()"> 修改</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="tms:app:tmAppUserInfo:del">
                    <button id="remove" class="btn btn-danger" disabled onclick="deleteAll()"> 删除</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="tms:app:tmAppUserInfo:audit">
                    <button id="audit" class="btn btn-primary" disabled onclick="audit('0')"> 审核</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="tms:app:tmAppUserInfo:unAudit">
                    <button id="unAudit" class="btn btn-primary" disabled onclick="audit('1')"> 取消审核</button>
                </shiro:hasPermission>
                <a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
                    <i class="fa fa-search"></i> 检索
                </a>
            </div>
            <!-- 表格 -->
            <table id="tmAppUserInfoTable" class="text-nowrap" data-toolbar="#toolbar"></table>
        </div>
    </div>
</div>
</body>
</html>