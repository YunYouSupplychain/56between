<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>角色管理</title>
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="roleList.js" %>
    <%@include file="role-userList.js" %>
</head>
<body>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">角色列表</h3>
        </div>
        <div class="panel-body">
            <div class="row">
                <div id="left" class="col-sm-12">
                    <!-- 搜索 -->
                    <div class="accordion-group">
                        <div id="collapseTwo" class="accordion-body collapse">
                            <div class="accordion-inner">
                                <form:form id="searchForm" modelAttribute="role">
                                    <table class="table well">
                                        <tbody>
                                        <tr>
                                            <td style="width:10%;vertical-align:middle;"><label class="pull-right">角色名称</label></td>
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
                        <shiro:hasPermission name="sys:role:add">
                            <a id="add" class="btn btn-primary" onclick="add()"> 新建</a>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="sys:role:edit">
                            <button id="edit" class="btn btn-primary" disabled onclick="edit()"> 修改</button>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="sys:role:del">
                            <button id="remove" class="btn btn-danger" disabled onclick="del()"> 删除</button>
                        </shiro:hasPermission>
                        <a id="search" class="btn btn-primary"><i class="fa fa-search"></i> 查询</a>
                        <a id="reset" class="btn btn-primary"><i class="fa fa-refresh"></i> 重置</a>
                        <a class="accordion-toggle btn btn-default " data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
                            <i class="fa fa-search"></i> 检索
                        </a>
                    </div>
                    <!-- 表格 -->
                    <table id="table" data-toolbar="#toolbar" data-id-field="id"></table>
                </div>
                <div id="right" class="panel panel-default col-sm-6" style="display:none">
                    <div class="panel-heading">
                        <h3 class="panel-title"><label>用户列表，所属角色: </label><font id="roleLabel"></font><input type="hidden" id="roleId"/></h3>
                    </div>
                    <div class="panel-body">
                        <div id="userToolbar">
                            <button id="assignButton" class="btn btn-primary" title="添加人员"> 添加人员</button>
                        </div>
                        <!-- 表格 -->
                        <table id="userTable" data-toolbar="#userToolbar" data-id-field="id"></table>
                    </div>
                </div>

            </div>
        </div>
    </div>
</div>
</body>
</html>