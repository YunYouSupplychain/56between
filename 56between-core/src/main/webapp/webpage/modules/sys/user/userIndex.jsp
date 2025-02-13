<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>用户管理</title>
    <meta name="decorator" content="ani"/>
    <%@ include file="/webpage/include/bootstraptable.jsp" %>
    <link href="${ctxStatic}/plugin/bootstrapTree/bootstrap-treeview.css" rel="stylesheet" type="text/css"/>
    <script src="${ctxStatic}/plugin/bootstrapTree/bootstrap-treeview.js" type="text/javascript"></script>
    <%@ include file="userIndex.js" %>
</head>
<body>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">用户列表</h3>
        </div>
        <div class="panel-body">
            <div class="row">
                <div class="col-sm-4 col-md-3">
                    <div id="jstree"></div>
                </div>
                <div class="col-sm-8 col-md-9 animated fadeInRight">
                    <!-- 搜索框-->
                    <div class="accordion-group">
                        <div id="collapseTwo" class="accordion-body collapse">
                            <div class="accordion-inner">
                                <form id="searchForm">
                                    <table class="table well">
                                        <tbody>
                                        <tr>
                                            <td class="width-10"><label class="pull-right">归属公司</label></td>
                                            <td class="width-15">
                                                <sys:treeselect id="company" name="company.id" title="公司" url="/sys/office/treeData?type=1" cssClass="form-control"/>
                                            </td>
                                            <td class="width-10"><label class="pull-right">归属机构</label></td>
                                            <td class="width-15">
                                                <sys:treeselect id="office" name="office.id" title="机构" url="/sys/office/treeData" cssClass="form-control" notAllowSelectParent="true"/>
                                            </td>
                                            <td class="width-10"><label class="pull-right">登录名</label></td>
                                            <td class="width-15">
                                                <input type="text" name="loginName" maxlength="32" class="form-control"/>
                                            </td>
                                            <td class="width-10"><label class="pull-right">姓名</label></td>
                                            <td class="width-15">
                                                <input type="text" name="name" maxlength="16" class="form-control"/>
                                            </td>
                                        </tr>
                                        </tbody>
                                    </table>
                                </form>
                            </div>
                        </div>
                    </div>
                    <!-- 工具栏 -->
                    <div id="toolbar">
                        <shiro:hasPermission name="sys:user:add">
                            <a id="add" class="btn btn-primary" onclick="add()"> 新建</a>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="sys:user:edit">
                            <button id="edit" class="btn btn-primary" disabled onclick="edit()"> 修改</button>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="sys:user:del">
                            <button id="remove" class="btn btn-danger" disabled onclick="deleteAll()"> 删除</button>
                        </shiro:hasPermission>
                        <a id="search" class="btn btn-primary"><i class="fa fa-search"></i> 查询</a>
                        <a id="reset" class="btn btn-primary"><i class="fa fa-refresh"></i> 重置</a>
                        <a class="accordion-toggle btn btn-default " data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
                            <i class="fa fa-search"></i> 检索
                        </a>
                    </div>
                    <!-- 表格 -->
                    <table id="table" data-toolbar="#toolbar"></table>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>